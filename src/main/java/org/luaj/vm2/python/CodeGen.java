package org.luaj.vm2.python;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Compiles the syntax tree of a module to {@link Code}, one function at a time. */
final class CodeGen {
	/** Compiles a Module node. */
	static Code compile(Node module, String filename) {
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
			consts.add(c);
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

	private void genModule(Node module) {
		statements(module.nodes("body"));
		loadConst(PyNone.NONE);
		emit(Op.RETURN);
	}

	private void genFunctionBody(Node n) {
		for (String p : scope.slotNames)
			if ((scope.flags(p) & Scope.CELL) != 0 && scope.slots.get(p).intValue() < scope.firstFree)
				emit(Op.MAKE_CELL, scope.slotOf(p));
		if (n.is("Lambda")) {
			expression(n.node("body"));
			emit(Op.RETURN);
			return;
		}
		statements(n.nodes("body"));
		loadConst(PyNone.NONE);
		emit(Op.RETURN);
	}

	private void genComprehension(Node n) {
		for (String p : scope.slotNames)
			if ((scope.flags(p) & Scope.CELL) != 0 && scope.slots.get(p).intValue() < scope.firstFree)
				emit(Op.MAKE_CELL, scope.slotOf(p));
		boolean list = n.is("ListComp");
		if (list)
			emit(Op.BUILD_LIST, 0);
		else
			emit(Op.BUILD_DICT, 0);
		comprehensionLoops(n, n.nodes("generators"), 0, list);
		emit(Op.RETURN);
	}

	private void comprehensionLoops(Node n, List<Node> gens, int index, boolean list) {
		if (index == gens.size()) {
			at(n);
			int up = index + 1;
			if (list) {
				expression(n.node("elt"));
				emit(Op.LIST_APPEND, up);
			} else {
				expression(n.node("key"));
				expression(n.node("value"));
				emit(Op.MAP_ADD, up);
			}
			return;
		}
		Node g = gens.get(index);
		if (index == 0) {
			emit(Op.LOAD_FAST, 0);
		} else {
			expression(g.node("iter"));
			emit(Op.GET_ITER);
		}
		Label top = new Label();
		Label end = new Label();
		bind(top);
		emitJump(Op.FOR_ITER, end);
		store(g.node("target"));
		for (Node cond : g.nodes("ifs")) {
			expression(cond);
			emitJump(Op.JUMP_IF_FALSE, top);
		}
		comprehensionLoops(n, gens, index + 1, list);
		emitJump(Op.JUMP, top);
		bind(end);
	}

	/** Pushes a function object for a def, a lambda or a comprehension. */
	private void makeFunction(Node n, String fname, Node args) {
		Scope child = (Scope) n.info;
		int flags = 0;
		if (args != null) {
			List<Node> defaults = args.nodes("defaults");
			if (!defaults.isEmpty()) {
				for (Node d : defaults)
					expression(d);
				emit(Op.BUILD_TUPLE, defaults.size());
				flags |= 1;
			}
			List<Node> kwargs = args.nodes("kwonlyargs");
			List<Node> kwdef = args.nodes("kw_defaults");
			int count = 0;
			for (int i = 0; i < kwargs.size(); i++) {
				Node d = kwdef.get(i);
				if (d != null) {
					loadConst(kwargs.get(i).str("arg"));
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
		g.line = n.line;
		if (n.is("ListComp") || n.is("DictComp"))
			g.genComprehension(n);
		else
			g.genFunctionBody(n);
		loadConst(g.finish());
		emit(Op.MAKE_FUNCTION, flags);
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

	private void statements(List<Node> body) {
		for (Node s : body)
			statement(s);
	}

	private void statement(Node n) {
		at(n);
		switch (n.type) {
		case "Expr": {
			Node v = n.node("value");
			if (v.is("Constant"))
				break; // docstrings and the like
			expression(v);
			emit(Op.POP);
			break;
		}
		case "Assign":
			assign(n);
			break;
		case "AugAssign":
			augAssign(n);
			break;
		case "Pass":
			break;
		case "If":
			ifStatement(n);
			break;
		case "While":
			whileStatement(n);
			break;
		case "For":
			forStatement(n);
			break;
		case "Break": {
			if (loops.isEmpty())
				throw error("'break' outside loop", n);
			Loop l = loops.get(loops.size() - 1);
			if (l.iterator)
				emit(Op.POP);
			emitJump(Op.JUMP, l.breakLabel);
			break;
		}
		case "Continue": {
			if (loops.isEmpty())
				throw error("'continue' not properly in loop", n);
			emitJump(Op.JUMP, loops.get(loops.size() - 1).continueLabel);
			break;
		}
		case "Return":
			if (scope.kind == Scope.MODULE)
				throw error("'return' outside function", n);
			if (n.node("value") == null)
				loadConst(PyNone.NONE);
			else
				expression(n.node("value"));
			at(n);
			emit(Op.RETURN);
			break;
		case "FunctionDef":
			makeFunction(n, n.str("name"), n.node("args"));
			storeName(n.str("name"));
			break;
		case "Delete":
			for (Node t : n.nodes("targets"))
				delete(t);
			break;
		case "Global":
		case "Nonlocal":
			break;
		case "Assert":
			assertStatement(n);
			break;
		case "Raise":
			raiseStatement(n);
			break;
		case "Try":
			tryStatement(n);
			break;
		case "Import":
			for (Node a : n.nodes("names")) {
				String full = a.str("name");
				String as = (String) a.get("asname");
				emit(Op.IMPORT, name(full));
				if (as != null) {
					storeName(as);
				} else {
					int dot = full.indexOf('.');
					storeName(dot >= 0 ? full.substring(0, dot) : full);
				}
			}
			break;
		default:
			throw error("unsupported statement " + n.type, n);
		}
	}

	private void assign(Node n) {
		expression(n.node("value"));
		List<Node> targets = n.nodes("targets");
		for (int i = 0; i < targets.size(); i++) {
			if (i < targets.size() - 1)
				emit(Op.DUP);
			store(targets.get(i));
		}
	}

	/** Stores the value on top of the stack into a target. */
	private void store(Node t) {
		switch (t.type) {
		case "Name":
			storeName(t.str("id"));
			break;
		case "Subscript":
			expression(t.node("value"));
			expression(t.node("slice"));
			at(t);
			emit(Op.SETITEM);
			break;
		case "Tuple":
		case "List": {
			List<Node> elts = t.nodes("elts");
			for (Node e : elts)
				if (e.is("Starred"))
					throw error("starred assignment targets are not supported", e);
			emit(Op.UNPACK, elts.size());
			for (Node e : elts)
				store(e);
			break;
		}
		case "Attribute":
			throw error("attribute assignment is not supported", t);
		default:
			throw error("cannot assign to " + t.type, t);
		}
	}

	private void delete(Node t) {
		switch (t.type) {
		case "Name":
			deleteName(t.str("id"));
			break;
		case "Subscript":
			expression(t.node("value"));
			expression(t.node("slice"));
			at(t);
			emit(Op.DELITEM);
			break;
		case "Tuple":
		case "List":
			for (Node e : t.nodes("elts"))
				delete(e);
			break;
		default:
			throw error("cannot delete " + t.type, t);
		}
	}

	private static int binOp(Node op) {
		switch (op.type) {
		case "Add":
			return Op.BIN_ADD;
		case "Sub":
			return Op.BIN_SUB;
		case "Mult":
			return Op.BIN_MUL;
		case "Div":
			return Op.BIN_DIV;
		case "FloorDiv":
			return Op.BIN_FLOORDIV;
		case "Mod":
			return Op.BIN_MOD;
		case "Pow":
			return Op.BIN_POW;
		case "LShift":
			return Op.BIN_LSHIFT;
		case "RShift":
			return Op.BIN_RSHIFT;
		case "BitAnd":
			return Op.BIN_AND;
		case "BitOr":
			return Op.BIN_OR;
		case "BitXor":
			return Op.BIN_XOR;
		default:
			throw new PySyntaxError("unsupported operator " + op.type, 0, 0);
		}
	}

	private void augAssign(Node n) {
		Node t = n.node("target");
		int op = binOp(n.node("op")) | Op.INPLACE;
		switch (t.type) {
		case "Name":
			loadName(t.str("id"));
			expression(n.node("value"));
			at(n);
			emit(Op.BINARY_OP, op);
			storeName(t.str("id"));
			break;
		case "Subscript":
			expression(t.node("value"));
			expression(t.node("slice"));
			emit(Op.DUP2);
			at(t);
			emit(Op.GETITEM);
			expression(n.node("value"));
			at(n);
			emit(Op.BINARY_OP, op);
			emit(Op.ROT3);
			emit(Op.SETITEM);
			break;
		case "Attribute":
			throw error("attribute assignment is not supported", t);
		default:
			throw error("illegal target for augmented assignment", t);
		}
	}

	private void ifStatement(Node n) {
		Label orelse = new Label();
		Label end = new Label();
		expression(n.node("test"));
		emitJump(Op.JUMP_IF_FALSE, orelse);
		statements(n.nodes("body"));
		if (!n.nodes("orelse").isEmpty()) {
			emitJump(Op.JUMP, end);
			bind(orelse);
			statements(n.nodes("orelse"));
			bind(end);
		} else {
			bind(orelse);
		}
	}

	private void whileStatement(Node n) {
		Label top = new Label();
		Label orelse = new Label();
		Label brk = new Label();
		bind(top);
		expression(n.node("test"));
		emitJump(Op.JUMP_IF_FALSE, orelse);
		loops.add(new Loop(top, brk, false));
		statements(n.nodes("body"));
		loops.remove(loops.size() - 1);
		emitJump(Op.JUMP, top);
		bind(orelse);
		statements(n.nodes("orelse"));
		bind(brk);
	}

	private void forStatement(Node n) {
		Label top = new Label();
		Label orelse = new Label();
		Label brk = new Label();
		expression(n.node("iter"));
		at(n);
		emit(Op.GET_ITER);
		bind(top);
		emitJump(Op.FOR_ITER, orelse);
		store(n.node("target"));
		loops.add(new Loop(top, brk, true));
		statements(n.nodes("body"));
		loops.remove(loops.size() - 1);
		emitJump(Op.JUMP, top);
		bind(orelse);
		statements(n.nodes("orelse"));
		bind(brk);
	}

	private void assertStatement(Node n) {
		Label ok = new Label();
		expression(n.node("test"));
		emitJump(Op.JUMP_IF_TRUE, ok);
		at(n);
		emit(Op.LOAD_GLOBAL, name("AssertionError"));
		if (n.node("msg") != null) {
			expression(n.node("msg"));
			emit(Op.CALL, 1);
		} else {
			emit(Op.CALL, 0);
		}
		emit(Op.RAISE, 1);
		bind(ok);
	}

	private void raiseStatement(Node n) {
		if (n.node("cause") != null)
			throw error("'raise ... from ...' is not supported", n);
		if (n.node("exc") == null) {
			if (handlerTemps.isEmpty()) {
				emit(Op.RAISE, 0);
			} else {
				emit(Op.LOAD_FAST, handlerTemps.get(handlerTemps.size() - 1).intValue());
				emit(Op.RERAISE);
			}
			return;
		}
		expression(n.node("exc"));
		at(n);
		emit(Op.RAISE, 1);
	}

	private void tryStatement(Node n) {
		if (!n.nodes("finalbody").isEmpty())
			throw error("'finally' is not supported", n);
		int tmp = newTemp();
		int depth0 = depth;
		Label handler = new Label();
		Label end = new Label();
		int start = size;
		statements(n.nodes("body"));
		int stop = size;
		if (stop == start) {
			statements(n.nodes("orelse"));
			return;
		}
		Entry e = new Entry();
		e.start = start;
		e.end = stop;
		e.depth = depth0;
		e.handler = handler;
		entries.add(e);
		statements(n.nodes("orelse"));
		emitJump(Op.JUMP, end);

		handler.depth = depth0 + 1;
		bind(handler);
		if (depth > maxDepth)
			maxDepth = depth;
		at(n);
		emit(Op.STORE_FAST, tmp);
		for (Node h : n.nodes("handlers")) {
			at(h);
			Label next = new Label();
			if (h.node("type") != null) {
				emit(Op.LOAD_FAST, tmp);
				expression(h.node("type"));
				at(h);
				emit(Op.EXC_MATCH);
				emitJump(Op.JUMP_IF_FALSE, next);
			}
			String hname = (String) h.get("name");
			if (hname != null) {
				emit(Op.LOAD_FAST, tmp);
				storeName(hname);
			}
			handlerTemps.add(Integer.valueOf(tmp));
			statements(h.nodes("body"));
			handlerTemps.remove(handlerTemps.size() - 1);
			if (hname != null)
				deleteName(hname);
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

	private void expressionOrNone(Node e) {
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
			if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE)
				throw new PySyntaxError("integer literal does not fit in 32 bits", line, 0);
			return Integer.valueOf((int) l);
		}
		return v;
	}

	private void expression(Node e) {
		at(e);
		switch (e.type) {
		case "Constant":
			loadConst(constantValue(e.get("value")));
			break;
		case "Name":
			loadName(e.str("id"));
			break;
		case "BoolOp": {
			Label end = new Label();
			int op = e.node("op").is("And") ? Op.JUMP_IF_FALSE_KEEP : Op.JUMP_IF_TRUE_KEEP;
			List<Node> values = e.nodes("values");
			for (int i = 0; i < values.size(); i++) {
				expression(values.get(i));
				if (i < values.size() - 1)
					emitJump(op, end);
			}
			bind(end);
			break;
		}
		case "BinOp":
			expression(e.node("left"));
			expression(e.node("right"));
			at(e);
			emit(Op.BINARY_OP, binOp(e.node("op")));
			break;
		case "UnaryOp":
			unary(e);
			break;
		case "IfExp": {
			Label orelse = new Label();
			Label end = new Label();
			expression(e.node("test"));
			emitJump(Op.JUMP_IF_FALSE, orelse);
			expression(e.node("body"));
			emitJump(Op.JUMP, end);
			bind(orelse);
			expression(e.node("orelse"));
			bind(end);
			break;
		}
		case "Compare":
			compare(e);
			break;
		case "Call":
			call(e);
			break;
		case "Attribute":
			expression(e.node("value"));
			at(e);
			emit(Op.LOAD_ATTR, name(e.str("attr")));
			break;
		case "Subscript":
			expression(e.node("value"));
			expression(e.node("slice"));
			at(e);
			emit(Op.GETITEM);
			break;
		case "Slice":
			expressionOrNone(e.node("lower"));
			expressionOrNone(e.node("upper"));
			expressionOrNone(e.node("step"));
			at(e);
			emit(Op.BUILD_SLICE);
			break;
		case "List":
		case "Tuple": {
			List<Node> elts = e.nodes("elts");
			for (Node x : elts) {
				if (x.is("Starred"))
					throw error("starred expressions in displays are not supported", x);
				expression(x);
			}
			at(e);
			emit(e.is("List") ? Op.BUILD_LIST : Op.BUILD_TUPLE, elts.size());
			break;
		}
		case "Dict": {
			List<Node> keys = e.nodes("keys");
			List<Node> values = e.nodes("values");
			for (int i = 0; i < keys.size(); i++) {
				expression(keys.get(i));
				expression(values.get(i));
			}
			at(e);
			emit(Op.BUILD_DICT, keys.size());
			break;
		}
		case "JoinedStr": {
			List<Node> values = e.nodes("values");
			if (values.isEmpty()) {
				loadConst("");
				break;
			}
			for (Node v : values)
				expression(v);
			at(e);
			emit(Op.BUILD_STRING, values.size());
			break;
		}
		case "FormattedValue": {
			expression(e.node("value"));
			int flags = 0;
			long conv = ((Long) e.get("conversion")).longValue();
			if (conv == 's')
				flags = 1;
			else if (conv == 'r')
				flags = 2;
			else if (conv == 'a')
				flags = 3;
			if (e.node("format_spec") != null) {
				expression(e.node("format_spec"));
				flags |= 4;
			}
			at(e);
			emit(Op.FORMAT_VALUE, flags);
			break;
		}
		case "Lambda":
			makeFunction(e, "<lambda>", e.node("args"));
			break;
		case "ListComp":
		case "DictComp": {
			makeFunction(e, e.is("ListComp") ? "<listcomp>" : "<dictcomp>", null);
			expression(e.nodes("generators").get(0).node("iter"));
			at(e);
			emit(Op.GET_ITER);
			emit(Op.CALL, 1);
			break;
		}
		case "Starred":
			throw error("can't use starred expression here", e);
		default:
			throw error("unsupported expression " + e.type, e);
		}
	}

	private void unary(Node e) {
		String op = e.node("op").type;
		Node operand = e.node("operand");
		if (op.equals("USub") && operand.is("Constant")) {
			Object v = operand.get("value");
			if (v instanceof Long) {
				long l = -((Long) v).longValue();
				if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE)
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
		case "USub":
			emit(Op.UNARY_NEG);
			break;
		case "UAdd":
			emit(Op.UNARY_POS);
			break;
		case "Invert":
			emit(Op.UNARY_INVERT);
			break;
		default:
			emit(Op.UNARY_NOT);
			break;
		}
	}

	private void compareOp(Node op) {
		switch (op.type) {
		case "Eq":
			emit(Op.COMPARE_OP, Op.CMP_EQ);
			break;
		case "NotEq":
			emit(Op.COMPARE_OP, Op.CMP_NE);
			break;
		case "Lt":
			emit(Op.COMPARE_OP, Op.CMP_LT);
			break;
		case "LtE":
			emit(Op.COMPARE_OP, Op.CMP_LE);
			break;
		case "Gt":
			emit(Op.COMPARE_OP, Op.CMP_GT);
			break;
		case "GtE":
			emit(Op.COMPARE_OP, Op.CMP_GE);
			break;
		case "Is":
			emit(Op.IS_OP, 0);
			break;
		case "IsNot":
			emit(Op.IS_OP, 1);
			break;
		case "In":
			emit(Op.CONTAINS_OP, 0);
			break;
		default:
			emit(Op.CONTAINS_OP, 1);
			break;
		}
	}

	private void compare(Node e) {
		List<Node> ops = e.nodes("ops");
		List<Node> comps = e.nodes("comparators");
		expression(e.node("left"));
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

	private void call(Node e) {
		List<Node> args = e.nodes("args");
		List<Node> kws = e.nodes("keywords");
		boolean ex = false;
		for (Node a : args)
			if (a.is("Starred"))
				ex = true;
		for (Node k : kws)
			if (k.get("arg") == null)
				ex = true;
		expression(e.node("func"));
		if (!ex) {
			for (Node a : args)
				expression(a);
			if (kws.isEmpty()) {
				at(e);
				emit(Op.CALL, args.size());
				return;
			}
			Object[] names = new Object[kws.size()];
			for (int i = 0; i < kws.size(); i++) {
				for (int j = 0; j < i; j++)
					if (names[j].equals(kws.get(i).get("arg")))
						throw error("keyword argument repeated: " + names[j], kws.get(i));
				names[i] = kws.get(i).get("arg");
				expression(kws.get(i).node("value"));
			}
			loadConst(new PyTuple(names));
			at(e);
			emit(Op.CALL_KW, args.size() + kws.size());
			return;
		}
		emit(Op.BUILD_LIST, 0);
		for (Node a : args) {
			if (a.is("Starred")) {
				expression(a.node("value"));
				emit(Op.LIST_EXTEND, 1);
			} else {
				expression(a);
				emit(Op.LIST_APPEND, 1);
			}
		}
		boolean dict = !kws.isEmpty();
		if (dict) {
			emit(Op.BUILD_DICT, 0);
			for (Node k : kws) {
				if (k.get("arg") == null) {
					expression(k.node("value"));
					emit(Op.DICT_MERGE_KW, 1);
				} else {
					loadConst(k.get("arg"));
					expression(k.node("value"));
					emit(Op.DICT_ADD_KW, 1);
				}
			}
		}
		at(e);
		emit(Op.CALL_EX, dict ? 1 : 0);
	}
}
