package org.luaj.vm2.python;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Compiles the syntax tree of a module to {@link Code}, one function at a time. */
final class CodeGen {
	/** Compiles a Module node. */
	static Code compile(Ast.Module module, String filename) {
		Scope top = Resolver.resolve(module);
		CodeGen g = new CodeGen(top, "<module>", filename);
		g.genModule(module);
		return g.finish();
	}

	private static final class Label {
		int pos = -1;
		int depth = -1;
		final List<Integer> refs = new ArrayList<Integer>();
	}

	private static final class Loop {
		final Label continueLabel;
		final Label breakLabel;
		/** True for a "for" loop: the iterator is on the stack and must be popped by break. */
		final boolean iterator;

		Loop(Label c, Label b, boolean it) {
			continueLabel = c;
			breakLabel = b;
			iterator = it;
		}
	}

	private static final class Entry {
		int start, end, depth;
		Label handler;
	}

	private final Scope scope;
	private final String name;
	private final String filename;
	private int[] code = new int[64];
	private int[] lines = new int[64];
	private int size;
	private int line = 1;
	private final List<Object> consts = new ArrayList<Object>();
	private final Map<List<Object>, Integer> constIndex = new HashMap<List<Object>, Integer>();
	private final List<String> names = new ArrayList<String>();
	private final Map<String, Integer> nameIndex = new HashMap<String, Integer>();
	private int depth;
	private int maxDepth;
	private boolean reachable = true;
	private int nslots;
	private final List<Entry> entries = new ArrayList<Entry>();
	private final List<Loop> loops = new ArrayList<Loop>();
	private final List<Integer> handlerTemps = new ArrayList<Integer>();
	private Code result;
	/** The innermost class around this code, for the mangling of private names; null outside classes. */
	private String className;

	private CodeGen(Scope scope, String name, String filename) {
		this.scope = scope;
		this.name = name;
		this.filename = filename;
		this.nslots = scope.nslots;
	}

	private static PySyntaxError error(String message, Node at) {
		return new PySyntaxError(message, at.line, at.col);
	}

	// ------------------------------------------------------------ emission

	private void emit(int op, int arg) {
		if (!reachable)
			return;
		if (arg < 0 || arg > 0xFFFFFF)
			throw new PySyntaxError("program too large", line, 0);
		if (size == code.length) {
			code = Arrays.copyOf(code, size * 2);
			lines = Arrays.copyOf(lines, size * 2);
		}
		code[size] = op | (arg << 8);
		lines[size] = line;
		size++;
		depth += Op.effect(op, arg);
		if (depth > maxDepth)
			maxDepth = depth;
		if (op == Op.RETURN || op == Op.RAISE || op == Op.RERAISE)
			reachable = false;
	}

	private void emit(int op) {
		emit(op, 0);
	}

	private void emitJump(int op, Label l) {
		if (!reachable)
			return;
		int branch;
		switch (op) {
		case Op.JUMP:
		case Op.JUMP_IF_FALSE_KEEP:
		case Op.JUMP_IF_TRUE_KEEP:
			branch = depth;
			break;
		default:
			branch = depth - 1;
			break;
		}
		if (l.depth < 0)
			l.depth = branch;
		else if (l.depth != branch)
			throw new IllegalStateException("stack depth mismatch at line " + line + ": " + l.depth + " / " + branch);
		int at = size;
		if (l.pos >= 0) {
			emit(op, l.pos);
		} else {
			l.refs.add(Integer.valueOf(at));
			emit(op, 0);
		}
		if (op == Op.JUMP)
			reachable = false;
	}

	private void bind(Label l) {
		l.pos = size;
		if (reachable) {
			if (l.depth < 0)
				l.depth = depth;
			else if (l.depth != depth)
				throw new IllegalStateException("stack depth mismatch binding label at line " + line + ": " + l.depth
						+ " / " + depth);
		} else if (l.depth >= 0) {
			reachable = true;
			depth = l.depth;
		}
		for (int at : l.refs)
			code[at] = (code[at] & 0xff) | (l.pos << 8);
		l.refs.clear();
	}

	private int constant(Object c) {
		Object key = c;
		if (c instanceof Double)
			key = Long.valueOf(Double.doubleToRawLongBits(((Double) c).doubleValue()));
		List<Object> k = new ArrayList<Object>(2);
		k.add(c.getClass());
		k.add(key);
		Integer i = constIndex.get(k);
		if (i == null) {
			i = Integer.valueOf(consts.size());
			// a str constant is a value of the program: a PyStr
			consts.add(c instanceof String ? PyStr.of((String) c) : c);
			constIndex.put(k, i);
		}
		return i.intValue();
	}

	private int name(String n) {
		Integer i = nameIndex.get(n);
		if (i == null) {
			i = Integer.valueOf(names.size());
			names.add(n);
			nameIndex.put(n, i);
		}
		return i.intValue();
	}

	private void loadConst(Object c) {
		emit(Op.LOAD_CONST, constant(c));
	}

	private int newTemp() {
		return nslots++;
	}

	private void at(Node n) {
		line = n.line;
	}

	// ------------------------------------------------------------ names

	private void loadName(String n) {
		switch (scope.kindOf(n)) {
		case Scope.K_GLOBAL:
			emit(Op.LOAD_GLOBAL, name(n));
			break;
		case Scope.K_LOCAL:
			emit(Op.LOAD_FAST, scope.slotOf(n));
			break;
		default:
			emit(Op.LOAD_CELL, scope.slotOf(n));
			break;
		}
	}

	private void storeName(String n) {
		switch (scope.kindOf(n)) {
		case Scope.K_GLOBAL:
			emit(Op.STORE_GLOBAL, name(n));
			break;
		case Scope.K_LOCAL:
			emit(Op.STORE_FAST, scope.slotOf(n));
			break;
		default:
			emit(Op.STORE_CELL, scope.slotOf(n));
			break;
		}
	}

	private void deleteName(String n) {
		switch (scope.kindOf(n)) {
		case Scope.K_GLOBAL:
			emit(Op.DELETE_GLOBAL, name(n));
			break;
		case Scope.K_LOCAL:
			emit(Op.DELETE_FAST, scope.slotOf(n));
			break;
		default:
			emit(Op.DELETE_CELL, scope.slotOf(n));
			break;
		}
	}

	// ------------------------------------------------------------ functions and modules

	private void genModule(Ast.Module module) {
		statements(module.body);
		loadConst(PyNone.NONE);
		emit(Op.RETURN);
	}

	/** The parameters that are captured by an inner scope live in cells. */
	private void makeCells() {
		for (String p : scope.slotNames)
			if ((scope.flags(p) & Scope.CELL) != 0 && scope.slots.get(p).intValue() < scope.firstFree)
				emit(Op.MAKE_CELL, scope.slotOf(p));
	}

	private void genFunctionBody(List<Ast.Stmt> body) {
		makeCells();
		statements(body);
		loadConst(PyNone.NONE);
		emit(Op.RETURN);
	}

	private void genLambdaBody(Ast.Expr body) {
		makeCells();
		expression(body);
		emit(Op.RETURN);
	}

	private void genComprehension(Ast.CompExpr n) {
		makeCells();
		boolean list = n instanceof Ast.ListComp;
		if (list)
			emit(Op.BUILD_LIST, 0);
		else
			emit(Op.BUILD_DICT, 0);
		comprehensionLoops(n, 0, list);
		emit(Op.RETURN);
	}

	private void comprehensionLoops(Ast.CompExpr n, int index, boolean list) {
		List<Ast.Comprehension> gens = n.generators;
		if (index == gens.size()) {
			at(n);
			int up = index + 1;
			if (list) {
				expression(((Ast.ListComp) n).elt);
				emit(Op.LIST_APPEND, up);
			} else {
				expression(((Ast.DictComp) n).key);
				expression(((Ast.DictComp) n).value);
				emit(Op.MAP_ADD, up);
			}
			return;
		}
		Ast.Comprehension g = gens.get(index);
		if (index == 0) {
			emit(Op.LOAD_FAST, 0);
		} else {
			expression(g.iter);
			emit(Op.GET_ITER);
		}
		Label top = new Label();
		Label end = new Label();
		bind(top);
		emitJump(Op.FOR_ITER, end);
		store(g.target);
		for (Ast.Expr cond : g.ifs) {
			expression(cond);
			emitJump(Op.JUMP_IF_FALSE, top);
		}
		comprehensionLoops(n, index + 1, list);
		emitJump(Op.JUMP, top);
		bind(end);
	}

	/**
	 * Pushes a function object for a def, a lambda or a comprehension; child is
	 * the scope the resolver made for it, args its parameters (null for a comprehension).
	 */
	private void makeFunction(Node n, Scope child, String fname, Ast.Arguments args) {
		int flags = 0;
		if (args != null) {
			List<Ast.Expr> defaults = args.defaults;
			if (!defaults.isEmpty()) {
				for (Ast.Expr d : defaults)
					expression(d);
				emit(Op.BUILD_TUPLE, defaults.size());
				flags |= 1;
			}
			List<Ast.Arg> kwargs = args.kwonlyargs;
			List<Ast.Expr> kwdef = args.kwDefaults;
			int count = 0;
			for (int i = 0; i < kwargs.size(); i++) {
				Ast.Expr d = kwdef.get(i);
				if (d != null) {
					loadConst(kwargs.get(i).arg);
					expression(d);
					count++;
				}
			}
			if (count > 0) {
				emit(Op.BUILD_DICT, count);
				flags |= 2;
			}
		}
		if (!child.freeNames.isEmpty()) {
			for (String f : child.freeNames)
				emit(Op.LOAD_CLOSURE, scope.slotOf(f));
			emit(Op.BUILD_TUPLE, child.freeNames.size());
			flags |= 4;
		}
		CodeGen g = new CodeGen(child, fname, filename);
		g.className = className;
		g.line = n.line;
		if (n instanceof Ast.CompExpr)
			g.genComprehension((Ast.CompExpr) n);
		else if (n instanceof Ast.Lambda)
			g.genLambdaBody(((Ast.Lambda) n).body);
		else
			g.genFunctionBody(((Ast.FunctionDef) n).body);
		loadConst(g.finish());
		emit(Op.MAKE_FUNCTION, flags);
	}

	/** As CPython: the decorators are evaluated from top to bottom, before the def or the class. */
	private void loadDecorators(List<Ast.Expr> decorators) {
		for (Ast.Expr d : decorators)
			expression(d);
	}

	/** ... and applied from bottom to top, once the function or the class exists. */
	private void applyDecorators(Node n, List<Ast.Expr> decorators) {
		for (int i = decorators.size() - 1; i >= 0; i--) {
			at(decorators.get(i));
			emit(Op.CALL, 1);
		}
		at(n);
	}

	/**
	 * A class statement: the body is compiled as a function without parameters
	 * that returns a dict of its locals; Builtins.BUILD_CLASS turns it into a
	 * class: BUILD_CLASS(body, name, qualname, *bases).
	 */
	private void classDef(Ast.ClassDef n) {
		List<Ast.Expr> bases = n.bases;
		if (bases.size() > 1)
			throw error("multiple inheritance is not supported", n);
		Scope child = n.scope;
		loadConst(Builtins.BUILD_CLASS);
		int flags = 0;
		if (child.freeNames.isEmpty() == false) {
			for (String f : child.freeNames)
				emit(Op.LOAD_CLOSURE, scope.slotOf(f));
			emit(Op.BUILD_TUPLE, child.freeNames.size());
			flags |= 4;
		}
		CodeGen g = new CodeGen(child, n.name, filename);
		g.className = n.name;
		g.line = n.line;
		g.genClassBody(n.body);
		loadConst(g.finish());
		emit(Op.MAKE_FUNCTION, flags);
		loadConst(n.name);
		loadConst(qualname(child));
		for (Ast.Expr b : bases)
			expression(b);
		at(n);
		emit(Op.CALL, 3 + bases.size());
	}

	private void genClassBody(List<Ast.Stmt> body) {
		statements(body);
		// the resolver only accepts straight-line statements: every local is bound here
		int count = 0;
		for (int i = 0; i < scope.firstFree; i++) {
			String local = scope.slotNames.get(i);
			loadConst(mangle(local));
			emit(Op.LOAD_FAST, i);
			count++;
		}
		emit(Op.BUILD_DICT, count);
		emit(Op.RETURN);
	}

	/**
	 * CPython's private name mangling: inside a class A, the attribute
	 * {@code __x} (two leading underscores, not two trailing ones) is
	 * {@code _A__x}. Applied to attribute names and to the names bound by a
	 * class body; local variables need no mangling, they are not observable.
	 */
	private String mangle(String n) {
		if (className == null || n.startsWith("__") == false || n.endsWith("__"))
			return n;
		int i = 0;
		while (i < className.length() && className.charAt(i) == '_')
			i++;
		if (i == className.length())
			return n;
		return "_" + className.substring(i) + n;
	}

	/** CPython's __qualname__: the enclosing classes and functions, "f.<locals>.A". */
	private static String qualname(Scope s) {
		StringBuilder sb = new StringBuilder(s.name);
		for (Scope p = s.parent; p != null && p.kind != Scope.MODULE; p = p.parent) {
			if (p.kind == Scope.CLASS)
				sb.insert(0, p.name + ".");
			else
				sb.insert(0, p.name + ".<locals>.");
		}
		return sb.toString();
	}

	private Code finish() {
		if (result != null)
			return result;
		// resolve the exception table
		List<Entry> live = new ArrayList<Entry>();
		for (Entry e : entries)
			if (e.end > e.start && e.handler.pos >= 0)
				live.add(e);
		int[] es = new int[live.size()], ee = new int[live.size()], eh = new int[live.size()],
				ed = new int[live.size()];
		for (int i = 0; i < live.size(); i++) {
			Entry e = live.get(i);
			es[i] = e.start;
			ee[i] = e.end;
			eh[i] = e.handler.pos;
			ed[i] = e.depth;
		}
		String[] slotNames = new String[nslots];
		for (int i = 0; i < scope.slotNames.size(); i++)
			slotNames[i] = scope.slotNames.get(i);
		int nparams = scope.argcount + scope.kwonlycount;
		String[] paramNames = new String[nparams];
		for (int i = 0; i < nparams; i++)
			paramNames[i] = scope.params.get(i);
		int varargsSlot = scope.varargs == null ? -1 : scope.slotOf(scope.varargs);
		int kwargsSlot = scope.kwargs == null ? -1 : scope.slotOf(scope.kwargs);
		result = new Code(name, filename, Arrays.copyOf(code, size), Arrays.copyOf(lines, size),
				consts.toArray(), names.toArray(new String[0]), nslots, maxDepth + 1, scope.argcount,
				scope.kwonlycount, varargsSlot, kwargsSlot, paramNames, scope.firstFree, scope.freeNames.size(),
				scope.kind == Scope.MODULE, es, ee, eh, ed, slotNames);
		return result;
	}

	// ------------------------------------------------------------ statements

	private void statements(List<Ast.Stmt> body) {
		for (Ast.Stmt s : body)
			statement(s);
	}

	private void statement(Ast.Stmt n) {
		at(n);
		if (n instanceof Ast.ExprStmt) {
			Ast.Expr v = ((Ast.ExprStmt) n).value;
			if (v instanceof Ast.Constant)
				return; // docstrings and the like
			expression(v);
			emit(Op.POP);
		} else if (n instanceof Ast.Assign) {
			assign((Ast.Assign) n);
		} else if (n instanceof Ast.AugAssign) {
			augAssign((Ast.AugAssign) n);
		} else if (n instanceof Ast.Pass) {
			// nothing
		} else if (n instanceof Ast.If) {
			ifStatement((Ast.If) n);
		} else if (n instanceof Ast.While) {
			whileStatement((Ast.While) n);
		} else if (n instanceof Ast.For) {
			forStatement((Ast.For) n);
		} else if (n instanceof Ast.Break) {
			if (loops.isEmpty())
				throw error("'break' outside loop", n);
			Loop l = loops.get(loops.size() - 1);
			if (l.iterator)
				emit(Op.POP);
			emitJump(Op.JUMP, l.breakLabel);
		} else if (n instanceof Ast.Continue) {
			if (loops.isEmpty())
				throw error("'continue' not properly in loop", n);
			emitJump(Op.JUMP, loops.get(loops.size() - 1).continueLabel);
		} else if (n instanceof Ast.Return) {
			Ast.Return r = (Ast.Return) n;
			if (scope.kind == Scope.MODULE)
				throw error("'return' outside function", n);
			if (r.value == null)
				loadConst(PyNone.NONE);
			else
				expression(r.value);
			at(n);
			emit(Op.RETURN);
		} else if (n instanceof Ast.FunctionDef) {
			Ast.FunctionDef f = (Ast.FunctionDef) n;
			loadDecorators(f.decorators);
			makeFunction(f, f.scope, f.name, f.args);
			applyDecorators(f, f.decorators);
			storeName(f.name);
		} else if (n instanceof Ast.ClassDef) {
			Ast.ClassDef c = (Ast.ClassDef) n;
			loadDecorators(c.decorators);
			classDef(c);
			applyDecorators(c, c.decorators);
			storeName(c.name);
		} else if (n instanceof Ast.Delete) {
			for (Ast.Expr t : ((Ast.Delete) n).targets)
				delete(t);
		} else if (n instanceof Ast.NameDeclaration) {
			// global and nonlocal: handled by the resolver
		} else if (n instanceof Ast.Assert) {
			assertStatement((Ast.Assert) n);
		} else if (n instanceof Ast.Raise) {
			raiseStatement((Ast.Raise) n);
		} else if (n instanceof Ast.Try) {
			tryStatement((Ast.Try) n);
		} else if (n instanceof Ast.Import) {
			for (Ast.Alias a : ((Ast.Import) n).names) {
				String full = a.name;
				emit(Op.IMPORT, name(full));
				if (a.asname != null) {
					storeName(a.asname);
				} else {
					int dot = full.indexOf('.');
					storeName(dot >= 0 ? full.substring(0, dot) : full);
				}
			}
		} else {
			throw error("unsupported statement " + n.type(), n);
		}
	}

	private void assign(Ast.Assign n) {
		expression(n.value);
		List<Ast.Expr> targets = n.targets;
		for (int i = 0; i < targets.size(); i++) {
			if (i < targets.size() - 1)
				emit(Op.DUP);
			store(targets.get(i));
		}
	}

	/** Stores the value on top of the stack into a target. */
	private void store(Ast.Expr t) {
		if (t instanceof Ast.Name) {
			storeName(((Ast.Name) t).id);
		} else if (t instanceof Ast.Subscript) {
			expression(((Ast.Subscript) t).value);
			expression(((Ast.Subscript) t).slice);
			at(t);
			emit(Op.SETITEM);
		} else if (t instanceof Ast.TupleExpr) {
			storeSequence(((Ast.TupleExpr) t).elts);
		} else if (t instanceof Ast.ListExpr) {
			storeSequence(((Ast.ListExpr) t).elts);
		} else if (t instanceof Ast.Attribute) {
			expression(((Ast.Attribute) t).value);
			at(t);
			emit(Op.STORE_ATTR, name(mangle(((Ast.Attribute) t).attr)));
		} else {
			throw error("cannot assign to " + t.type(), t);
		}
	}

	private void storeSequence(List<Ast.Expr> elts) {
		for (Ast.Expr e : elts)
			if (e instanceof Ast.Starred)
				throw error("starred assignment targets are not supported", e);
		emit(Op.UNPACK, elts.size());
		for (Ast.Expr e : elts)
			store(e);
	}

	private void delete(Ast.Expr t) {
		if (t instanceof Ast.Name) {
			deleteName(((Ast.Name) t).id);
		} else if (t instanceof Ast.Subscript) {
			expression(((Ast.Subscript) t).value);
			expression(((Ast.Subscript) t).slice);
			at(t);
			emit(Op.DELITEM);
		} else if (t instanceof Ast.TupleExpr) {
			for (Ast.Expr e : ((Ast.TupleExpr) t).elts)
				delete(e);
		} else if (t instanceof Ast.ListExpr) {
			for (Ast.Expr e : ((Ast.ListExpr) t).elts)
				delete(e);
		} else if (t instanceof Ast.Attribute) {
			expression(((Ast.Attribute) t).value);
			at(t);
			emit(Op.DELETE_ATTR, name(mangle(((Ast.Attribute) t).attr)));
		} else {
			throw error("cannot delete " + t.type(), t);
		}
	}

	private static int binOp(Ast.Operator op) {
		switch (op) {
		case Add:
			return Op.BIN_ADD;
		case Sub:
			return Op.BIN_SUB;
		case Mult:
			return Op.BIN_MUL;
		case Div:
			return Op.BIN_DIV;
		case FloorDiv:
			return Op.BIN_FLOORDIV;
		case Mod:
			return Op.BIN_MOD;
		case Pow:
			return Op.BIN_POW;
		case LShift:
			return Op.BIN_LSHIFT;
		case RShift:
			return Op.BIN_RSHIFT;
		case BitAnd:
			return Op.BIN_AND;
		case BitOr:
			return Op.BIN_OR;
		case BitXor:
			return Op.BIN_XOR;
		default:
			throw new PySyntaxError("unsupported operator " + op.name(), 0, 0);
		}
	}

	private void augAssign(Ast.AugAssign n) {
		Ast.Expr t = n.target;
		int op = binOp(n.op) | Op.INPLACE;
		if (t instanceof Ast.Name) {
			String id = ((Ast.Name) t).id;
			loadName(id);
			expression(n.value);
			at(n);
			emit(Op.BINARY_OP, op);
			storeName(id);
		} else if (t instanceof Ast.Subscript) {
			expression(((Ast.Subscript) t).value);
			expression(((Ast.Subscript) t).slice);
			emit(Op.DUP2);
			at(t);
			emit(Op.GETITEM);
			expression(n.value);
			at(n);
			emit(Op.BINARY_OP, op);
			emit(Op.ROT3);
			emit(Op.SETITEM);
		} else if (t instanceof Ast.Attribute) {
			Ast.Attribute attr = (Ast.Attribute) t;
			expression(attr.value);
			emit(Op.DUP);
			at(t);
			emit(Op.LOAD_ATTR, name(mangle(attr.attr)));
			expression(n.value);
			at(n);
			emit(Op.BINARY_OP, op);
			emit(Op.ROT2);
			at(t);
			emit(Op.STORE_ATTR, name(mangle(attr.attr)));
		} else {
			throw error("illegal target for augmented assignment", t);
		}
	}

	private void ifStatement(Ast.If n) {
		Label orelse = new Label();
		Label end = new Label();
		expression(n.test);
		emitJump(Op.JUMP_IF_FALSE, orelse);
		statements(n.body);
		if (!n.orelse.isEmpty()) {
			emitJump(Op.JUMP, end);
			bind(orelse);
			statements(n.orelse);
			bind(end);
		} else {
			bind(orelse);
		}
	}

	private void whileStatement(Ast.While n) {
		Label top = new Label();
		Label orelse = new Label();
		Label brk = new Label();
		bind(top);
		expression(n.test);
		emitJump(Op.JUMP_IF_FALSE, orelse);
		loops.add(new Loop(top, brk, false));
		statements(n.body);
		loops.remove(loops.size() - 1);
		emitJump(Op.JUMP, top);
		bind(orelse);
		statements(n.orelse);
		bind(brk);
	}

	private void forStatement(Ast.For n) {
		Label top = new Label();
		Label orelse = new Label();
		Label brk = new Label();
		expression(n.iter);
		at(n);
		emit(Op.GET_ITER);
		bind(top);
		emitJump(Op.FOR_ITER, orelse);
		store(n.target);
		loops.add(new Loop(top, brk, true));
		statements(n.body);
		loops.remove(loops.size() - 1);
		emitJump(Op.JUMP, top);
		bind(orelse);
		statements(n.orelse);
		bind(brk);
	}

	private void assertStatement(Ast.Assert n) {
		Label ok = new Label();
		expression(n.test);
		emitJump(Op.JUMP_IF_TRUE, ok);
		at(n);
		emit(Op.LOAD_GLOBAL, name("AssertionError"));
		if (n.msg != null) {
			expression(n.msg);
			emit(Op.CALL, 1);
		} else {
			emit(Op.CALL, 0);
		}
		emit(Op.RAISE, 1);
		bind(ok);
	}

	private void raiseStatement(Ast.Raise n) {
		if (n.cause != null)
			throw error("'raise ... from ...' is not supported", n);
		if (n.exc == null) {
			if (handlerTemps.isEmpty()) {
				emit(Op.RAISE, 0);
			} else {
				emit(Op.LOAD_FAST, handlerTemps.get(handlerTemps.size() - 1).intValue());
				emit(Op.RERAISE);
			}
			return;
		}
		expression(n.exc);
		at(n);
		emit(Op.RAISE, 1);
	}

	private void tryStatement(Ast.Try n) {
		int tmp = newTemp();
		int depth0 = depth;
		Label handler = new Label();
		Label end = new Label();
		int start = size;
		statements(n.body);
		int stop = size;
		if (stop == start) {
			statements(n.orelse);
			return;
		}
		Entry e = new Entry();
		e.start = start;
		e.end = stop;
		e.depth = depth0;
		e.handler = handler;
		entries.add(e);
		statements(n.orelse);
		emitJump(Op.JUMP, end);

		handler.depth = depth0 + 1;
		bind(handler);
		if (depth > maxDepth)
			maxDepth = depth;
		at(n);
		emit(Op.STORE_FAST, tmp);
		for (Ast.ExceptHandler h : n.handlers) {
			at(h);
			Label next = new Label();
			if (h.exceptionType != null) {
				emit(Op.LOAD_FAST, tmp);
				expression(h.exceptionType);
				at(h);
				emit(Op.EXC_MATCH);
				emitJump(Op.JUMP_IF_FALSE, next);
			}
			if (h.name != null) {
				emit(Op.LOAD_FAST, tmp);
				storeName(h.name);
			}
			handlerTemps.add(Integer.valueOf(tmp));
			statements(h.body);
			handlerTemps.remove(handlerTemps.size() - 1);
			if (h.name != null)
				deleteName(h.name);
			emitJump(Op.JUMP, end);
			bind(next);
		}
		if (reachable) {
			emit(Op.LOAD_FAST, tmp);
			emit(Op.RERAISE);
		}
		bind(end);
	}

	// ------------------------------------------------------------ expressions

	private void expressionOrNone(Ast.Expr e) {
		if (e == null)
			loadConst(PyNone.NONE);
		else
			expression(e);
	}

	private Object constantValue(Object v) {
		if (v == null)
			return PyNone.NONE;
		if (v instanceof Long) {
			long l = ((Long) v).longValue();
			if (PyInt.fits(l) == false)
				throw new PySyntaxError("integer literal does not fit in 32 bits", line, 0);
			return Integer.valueOf((int) l);
		}
		return v;
	}

	private void expression(Ast.Expr e) {
		at(e);
		if (e instanceof Ast.Constant) {
			loadConst(constantValue(((Ast.Constant) e).value));
		} else if (e instanceof Ast.Name) {
			loadName(((Ast.Name) e).id);
		} else if (e instanceof Ast.BoolOp) {
			Ast.BoolOp b = (Ast.BoolOp) e;
			Label end = new Label();
			int op = b.op == Ast.BoolOperator.And ? Op.JUMP_IF_FALSE_KEEP : Op.JUMP_IF_TRUE_KEEP;
			List<Ast.Expr> values = b.values;
			for (int i = 0; i < values.size(); i++) {
				expression(values.get(i));
				if (i < values.size() - 1)
					emitJump(op, end);
			}
			bind(end);
		} else if (e instanceof Ast.BinOp) {
			Ast.BinOp b = (Ast.BinOp) e;
			expression(b.left);
			expression(b.right);
			at(e);
			emit(Op.BINARY_OP, binOp(b.op));
		} else if (e instanceof Ast.UnaryOp) {
			unary((Ast.UnaryOp) e);
		} else if (e instanceof Ast.IfExp) {
			Ast.IfExp i = (Ast.IfExp) e;
			Label orelse = new Label();
			Label end = new Label();
			expression(i.test);
			emitJump(Op.JUMP_IF_FALSE, orelse);
			expression(i.body);
			emitJump(Op.JUMP, end);
			bind(orelse);
			expression(i.orelse);
			bind(end);
		} else if (e instanceof Ast.Compare) {
			compare((Ast.Compare) e);
		} else if (e instanceof Ast.Call) {
			call((Ast.Call) e);
		} else if (e instanceof Ast.Attribute) {
			Ast.Attribute a = (Ast.Attribute) e;
			expression(a.value);
			at(e);
			emit(Op.LOAD_ATTR, name(mangle(a.attr)));
		} else if (e instanceof Ast.Subscript) {
			Ast.Subscript s = (Ast.Subscript) e;
			expression(s.value);
			expression(s.slice);
			at(e);
			emit(Op.GETITEM);
		} else if (e instanceof Ast.Slice) {
			Ast.Slice s = (Ast.Slice) e;
			expressionOrNone(s.lower);
			expressionOrNone(s.upper);
			expressionOrNone(s.step);
			at(e);
			emit(Op.BUILD_SLICE);
		} else if (e instanceof Ast.ListExpr) {
			List<Ast.Expr> elts = ((Ast.ListExpr) e).elts;
			displayElements(elts);
			at(e);
			emit(Op.BUILD_LIST, elts.size());
		} else if (e instanceof Ast.TupleExpr) {
			List<Ast.Expr> elts = ((Ast.TupleExpr) e).elts;
			displayElements(elts);
			at(e);
			emit(Op.BUILD_TUPLE, elts.size());
		} else if (e instanceof Ast.DictExpr) {
			Ast.DictExpr d = (Ast.DictExpr) e;
			for (int i = 0; i < d.keys.size(); i++) {
				expression(d.keys.get(i));
				expression(d.values.get(i));
			}
			at(e);
			emit(Op.BUILD_DICT, d.keys.size());
		} else if (e instanceof Ast.JoinedStr) {
			List<Ast.Expr> values = ((Ast.JoinedStr) e).values;
			if (values.isEmpty()) {
				loadConst("");
				return;
			}
			for (Ast.Expr v : values)
				expression(v);
			at(e);
			emit(Op.BUILD_STRING, values.size());
		} else if (e instanceof Ast.FormattedValue) {
			Ast.FormattedValue f = (Ast.FormattedValue) e;
			expression(f.value);
			int flags = 0;
			long conv = f.conversion;
			if (conv == 's')
				flags = 1;
			else if (conv == 'r')
				flags = 2;
			else if (conv == 'a')
				flags = 3;
			if (f.formatSpec != null) {
				expression(f.formatSpec);
				flags |= 4;
			}
			at(e);
			emit(Op.FORMAT_VALUE, flags);
		} else if (e instanceof Ast.Lambda) {
			Ast.Lambda l = (Ast.Lambda) e;
			makeFunction(l, l.scope, "<lambda>", l.args);
		} else if (e instanceof Ast.CompExpr) {
			Ast.CompExpr c = (Ast.CompExpr) e;
			makeFunction(c, c.scope, c instanceof Ast.ListComp ? "<listcomp>" : "<dictcomp>", null);
			expression(c.generators.get(0).iter);
			at(e);
			emit(Op.GET_ITER);
			emit(Op.CALL, 1);
		} else if (e instanceof Ast.Starred) {
			throw error("can't use starred expression here", e);
		} else {
			throw error("unsupported expression " + e.type(), e);
		}
	}

	/** The elements of a list or tuple display. */
	private void displayElements(List<Ast.Expr> elts) {
		for (Ast.Expr x : elts) {
			if (x instanceof Ast.Starred)
				throw error("starred expressions in displays are not supported", x);
			expression(x);
		}
	}

	private void unary(Ast.UnaryOp e) {
		Ast.UnaryOperator op = e.op;
		Ast.Expr operand = e.operand;
		if (op == Ast.UnaryOperator.USub && operand instanceof Ast.Constant) {
			Object v = ((Ast.Constant) operand).value;
			if (v instanceof Long) {
				long l = -((Long) v).longValue();
				if (PyInt.fits(l) == false)
					throw new PySyntaxError("integer literal does not fit in 32 bits", line, 0);
				loadConst(Integer.valueOf((int) l));
				return;
			}
			if (v instanceof Double) {
				loadConst(Double.valueOf(-((Double) v).doubleValue()));
				return;
			}
		}
		expression(operand);
		at(e);
		switch (op) {
		case USub:
			emit(Op.UNARY_NEG);
			break;
		case UAdd:
			emit(Op.UNARY_POS);
			break;
		case Invert:
			emit(Op.UNARY_INVERT);
			break;
		default:
			emit(Op.UNARY_NOT);
			break;
		}
	}

	private void compareOp(Ast.CmpOperator op) {
		switch (op) {
		case Eq:
			emit(Op.COMPARE_OP, Op.CMP_EQ);
			break;
		case NotEq:
			emit(Op.COMPARE_OP, Op.CMP_NE);
			break;
		case Lt:
			emit(Op.COMPARE_OP, Op.CMP_LT);
			break;
		case LtE:
			emit(Op.COMPARE_OP, Op.CMP_LE);
			break;
		case Gt:
			emit(Op.COMPARE_OP, Op.CMP_GT);
			break;
		case GtE:
			emit(Op.COMPARE_OP, Op.CMP_GE);
			break;
		case Is:
			emit(Op.IS_OP, 0);
			break;
		case IsNot:
			emit(Op.IS_OP, 1);
			break;
		case In:
			emit(Op.CONTAINS_OP, 0);
			break;
		default:
			emit(Op.CONTAINS_OP, 1);
			break;
		}
	}

	private void compare(Ast.Compare e) {
		List<Ast.CmpOperator> ops = e.ops;
		List<Ast.Expr> comps = e.comparators;
		expression(e.left);
		if (ops.size() == 1) {
			expression(comps.get(0));
			at(e);
			compareOp(ops.get(0));
			return;
		}
		Label cleanup = new Label();
		Label end = new Label();
		for (int i = 0; i < ops.size(); i++) {
			expression(comps.get(i));
			at(e);
			if (i < ops.size() - 1) {
				emit(Op.DUP);
				emit(Op.ROT3);
				compareOp(ops.get(i));
				emitJump(Op.JUMP_IF_FALSE_KEEP, cleanup);
			} else {
				compareOp(ops.get(i));
			}
		}
		emitJump(Op.JUMP, end);
		bind(cleanup);
		emit(Op.ROT2);
		emit(Op.POP);
		bind(end);
	}

	private void call(Ast.Call e) {
		List<Ast.Expr> args = e.args;
		List<Ast.Keyword> kws = e.keywords;
		boolean ex = false;
		for (Ast.Expr a : args)
			if (a instanceof Ast.Starred)
				ex = true;
		for (Ast.Keyword k : kws)
			if (k.arg == null)
				ex = true;
		expression(e.func);
		if (!ex) {
			for (Ast.Expr a : args)
				expression(a);
			if (kws.isEmpty()) {
				at(e);
				emit(Op.CALL, args.size());
				return;
			}
			Object[] names = new Object[kws.size()];
			for (int i = 0; i < kws.size(); i++) {
				for (int j = 0; j < i; j++)
					if (names[j].equals(kws.get(i).arg))
						throw error("keyword argument repeated: " + names[j], kws.get(i));
				names[i] = kws.get(i).arg;
				expression(kws.get(i).value);
			}
			loadConst(new PyTuple(names));
			at(e);
			emit(Op.CALL_KW, args.size() + kws.size());
			return;
		}
		emit(Op.BUILD_LIST, 0);
		for (Ast.Expr a : args) {
			if (a instanceof Ast.Starred) {
				expression(((Ast.Starred) a).value);
				emit(Op.LIST_EXTEND, 1);
			} else {
				expression(a);
				emit(Op.LIST_APPEND, 1);
			}
		}
		boolean dict = !kws.isEmpty();
		if (dict) {
			emit(Op.BUILD_DICT, 0);
			for (Ast.Keyword k : kws) {
				if (k.arg == null) {
					expression(k.value);
					emit(Op.DICT_MERGE_KW, 1);
				} else {
					loadConst(k.arg);
					expression(k.value);
					emit(Op.DICT_ADD_KW, 1);
				}
			}
		}
		at(e);
		emit(Op.CALL_EX, dict ? 1 : 0);
	}
}
