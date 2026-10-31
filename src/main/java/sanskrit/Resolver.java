package sanskrit;

import java.util.ArrayList;
import java.util.List;

/**
 * Decides, for every name of every scope, whether it is local, global, a cell
 * (local captured by an inner function) or free (captured from an enclosing
 * function). The rule is Python's: a name bound anywhere in a function is local
 * to it, unless declared {@code global} or {@code nonlocal}; names bound at the
 * top level of the module are globals.
 */
final class Resolver {
	/**
	 * The deepest tree this pass, and CodeGen which follows the same tree, accept:
	 * both recurse once per level. The parser limits the nesting it recurses on
	 * (200 levels) but builds a chain such as {@code 1 + 1 + ... + 1} or
	 * {@code a.b.c...} with a loop, so only this limit bounds those: without it,
	 * a long chain overflows the stack, which a Go port could not recover from.
	 */
	private static final int MAX_NESTING = 1000;

	private final List<Scope> all = new ArrayList<Scope>();
	/** The number of levels of the tree around the node being visited. */
	private int nesting;

	/** Analyses a Module node; the scope of each function-like node is stored in its scope field. */
	static Scope resolve(Ast.Module module) {
		Resolver r = new Resolver();
		Scope top = new Scope(Scope.MODULE, "<module>", null, module);
		module.scope = top;
		r.all.add(top);
		r.statements(top, module.body);
		for (Scope s : r.all)
			r.resolveNames(s);
		for (Scope s : r.all)
			s.layout();
		return top;
	}

	private static PySyntaxError error(String message, Node at) {
		return new PySyntaxError(message, at.line, at.col);
	}

	// ------------------------------------------------------------ analysis

	private void statements(Scope s, List<Ast.Stmt> body) {
		for (Ast.Stmt n : body)
			statement(s, n);
	}

	private void bind(Scope s, String name) {
		s.addFlags(name, Scope.BOUND);
	}

	/** Enters a level of the tree; the caller leaves it with {@code nesting--}. After an error nothing is reused. */
	private void enter(Node at, int levels) {
		nesting += levels;
		if (nesting > MAX_NESTING)
			throw error("too many levels of nesting", at);
	}

	private void statement(Scope s, Ast.Stmt n) {
		enter(n, 1);
		if (n instanceof Ast.FunctionDef) {
			Ast.FunctionDef f = (Ast.FunctionDef) n;
			decorators(s, f.decorators);
			bind(s, f.name);
			defaults(s, f.args);
			Scope child = function(s, f.name, f, f.args);
			f.scope = child;
			statements(child, f.body);
		} else if (n instanceof Ast.ClassDef) {
			Ast.ClassDef c = (Ast.ClassDef) n;
			decorators(s, c.decorators);
			bind(s, c.name);
			for (Ast.Expr b : c.bases)
				expression(s, b);
			Scope child = new Scope(Scope.CLASS, c.name, s, c);
			c.scope = child;
			all.add(child);
			for (Ast.Stmt m : c.body) {
				checkClassStatement(m);
				statement(child, m);
			}
		} else if (n instanceof Ast.Return) {
			expressionOrNull(s, ((Ast.Return) n).value);
		} else if (n instanceof Ast.Delete) {
			for (Ast.Expr t : ((Ast.Delete) n).targets)
				target(s, t);
		} else if (n instanceof Ast.Assign) {
			Ast.Assign a = (Ast.Assign) n;
			expression(s, a.value);
			for (Ast.Expr t : a.targets)
				target(s, t);
		} else if (n instanceof Ast.AugAssign) {
			Ast.AugAssign a = (Ast.AugAssign) n;
			expression(s, a.value);
			if (a.target instanceof Ast.Name) {
				s.addFlags(((Ast.Name) a.target).id, Scope.USED);
				target(s, a.target);
			} else {
				target(s, a.target);
				expression(s, a.target);
			}
		} else if (n instanceof Ast.For) {
			Ast.For f = (Ast.For) n;
			expression(s, f.iter);
			target(s, f.target);
			statements(s, f.body);
			statements(s, f.orelse);
		} else if (n instanceof Ast.While) {
			Ast.While w = (Ast.While) n;
			expression(s, w.test);
			statements(s, w.body);
			statements(s, w.orelse);
		} else if (n instanceof Ast.If) {
			Ast.If i = (Ast.If) n;
			expression(s, i.test);
			statements(s, i.body);
			statements(s, i.orelse);
		} else if (n instanceof Ast.Raise) {
			Ast.Raise r = (Ast.Raise) n;
			expressionOrNull(s, r.exc);
			expressionOrNull(s, r.cause);
		} else if (n instanceof Ast.Try) {
			Ast.Try t = (Ast.Try) n;
			statements(s, t.body);
			for (Ast.ExceptHandler h : t.handlers) {
				expressionOrNull(s, h.exceptionType);
				if (h.name != null)
					bind(s, h.name);
				statements(s, h.body);
			}
			statements(s, t.orelse);
			// finally is rejected by the parser: there is no final body
		} else if (n instanceof Ast.Assert) {
			Ast.Assert a = (Ast.Assert) n;
			expression(s, a.test);
			expressionOrNull(s, a.msg);
		} else if (n instanceof Ast.Import) {
			for (Ast.Alias a : ((Ast.Import) n).names) {
				String name = a.asname != null ? a.asname : a.name;
				int dot = name.indexOf('.');
				if (dot >= 0)
					name = name.substring(0, dot);
				bind(s, name);
			}
		} else if (n instanceof Ast.ImportFrom) {
			for (Ast.Alias a : ((Ast.ImportFrom) n).names)
				bind(s, a.asname != null ? a.asname : a.name);
		} else if (n instanceof Ast.NameDeclaration) {
			declaration(s, (Ast.NameDeclaration) n);
		} else if (n instanceof Ast.ExprStmt) {
			expression(s, ((Ast.ExprStmt) n).value);
		} else if (n instanceof Ast.Pass || n instanceof Ast.Break || n instanceof Ast.Continue) {
			// nothing to resolve
		} else {
			throw error("unsupported statement " + n.type(), n);
		}
		nesting--;
	}

	/** A global or nonlocal statement. */
	private void declaration(Scope s, Ast.NameDeclaration n) {
		boolean global = n instanceof Ast.Global;
		for (String name : n.names) {
			if (!global && s.kind == Scope.MODULE)
				throw error("nonlocal declaration not allowed at module level", n);
			int f = s.flags(name);
			if ((f & Scope.PARAM) != 0)
				throw error("name '" + name + "' is parameter and " + (global ? "global" : "nonlocal"), n);
			if ((f & (Scope.BOUND | Scope.USED)) != 0 && s.kind != Scope.MODULE)
				throw error("name '" + name + "' is assigned to or used before " + (global ? "global" : "nonlocal")
						+ " declaration", n);
			if ((f & (global ? Scope.NONLOCAL : Scope.GLOBAL)) != 0)
				throw error("name '" + name + "' is nonlocal and global", n);
			s.addFlags(name, global ? Scope.GLOBAL : Scope.NONLOCAL);
		}
	}

	/**
	 * The decorators of a def or a class, evaluated in the enclosing scope. A
	 * decorator is a name, an attribute or a call of one of those
	 * ({@code @macro}, {@code @puml.macro}, {@code @macro("$color")}). The
	 * decorators of the builtin descriptors are rejected: they would need
	 * descriptors, which the interpreter does not have.
	 */
	private void decorators(Scope s, List<Ast.Expr> decorators) {
		for (Ast.Expr d : decorators) {
			Ast.Expr callee = d instanceof Ast.Call ? ((Ast.Call) d).func : d;
			Ast.Expr root = callee;
			while (root instanceof Ast.Attribute)
				root = ((Ast.Attribute) root).value;
			if (root instanceof Ast.Name == false)
				throw error("this form of decorator is not supported: use a name, an attribute or a call", d);
			if (callee instanceof Ast.Name) {
				String name = ((Ast.Name) callee).id;
				if (name.equals("staticmethod") || name.equals("classmethod") || name.equals("property"))
					throw error("@" + name + " is not supported", d);
			}
			expression(s, d);
		}
	}

	/**
	 * Only straight-line statements are accepted in a class body: every name it
	 * binds is then bound when the body ends, and becomes an attribute. Special
	 * methods other than __init__ are rejected rather than ignored.
	 */
	private static void checkClassStatement(Ast.Stmt m) {
		if (m instanceof Ast.FunctionDef) {
			checkClassMember(((Ast.FunctionDef) m).name, m);
		} else if (m instanceof Ast.ClassDef) {
			checkClassMember(((Ast.ClassDef) m).name, m);
		} else if (m instanceof Ast.Assign) {
			for (Ast.Expr t : ((Ast.Assign) m).targets)
				checkClassTarget(t);
		} else if (m instanceof Ast.AugAssign) {
			checkClassTarget(((Ast.AugAssign) m).target);
		} else if (m instanceof Ast.ExprStmt || m instanceof Ast.Pass) {
			// allowed
		} else {
			throw error("this statement is not supported in a class body", m);
		}
	}

	private static void checkClassTarget(Ast.Expr t) {
		if (t instanceof Ast.Name) {
			checkClassMember(((Ast.Name) t).id, t);
		} else if (t instanceof Ast.TupleExpr) {
			for (Ast.Expr e : ((Ast.TupleExpr) t).elts)
				checkClassTarget(e);
		} else if (t instanceof Ast.ListExpr) {
			for (Ast.Expr e : ((Ast.ListExpr) t).elts)
				checkClassTarget(e);
		}
	}

	private static void checkClassMember(String name, Node at) {
		if (name.startsWith("__") && name.endsWith("__") && name.length() > 4 && isSupportedSpecial(name) == false)
			throw error("special method or attribute '" + name + "' is not supported", at);
	}

	/** The special names a class body may define: the others are rejected rather than ignored. */
	private static boolean isSupportedSpecial(String name) {
		return name.equals("__init__") || name.equals("__str__") || name.equals("__repr__") || name.equals("__lt__")
				|| name.equals("__le__") || name.equals("__gt__") || name.equals("__ge__");
	}

	private void defaults(Scope s, Ast.Arguments args) {
		for (Ast.Expr d : args.defaults)
			expression(s, d);
		for (Ast.Expr d : args.kwDefaults)
			expressionOrNull(s, d);
	}

	private Scope function(Scope parent, String name, Node node, Ast.Arguments args) {
		Scope child = new Scope(Scope.FUNCTION, name, parent, node);
		all.add(child);
		for (Ast.Arg a : args.args)
			param(child, a);
		child.argcount = args.args.size();
		for (Ast.Arg a : args.kwonlyargs)
			param(child, a);
		child.kwonlycount = args.kwonlyargs.size();
		if (args.vararg != null) {
			param(child, args.vararg);
			child.varargs = args.vararg.arg;
		}
		if (args.kwarg != null) {
			param(child, args.kwarg);
			child.kwargs = args.kwarg.arg;
		}
		return child;
	}

	private void param(Scope s, Ast.Arg arg) {
		String name = arg.arg;
		if ((s.flags(name) & Scope.PARAM) != 0)
			throw error("duplicate argument '" + name + "' in function definition", arg);
		s.addFlags(name, Scope.PARAM | Scope.BOUND);
		s.params.add(name);
	}

	private void target(Scope s, Ast.Expr t) {
		if (t instanceof Ast.Name) {
			s.addFlags(((Ast.Name) t).id, Scope.BOUND);
		} else if (t instanceof Ast.TupleExpr) {
			for (Ast.Expr e : ((Ast.TupleExpr) t).elts)
				target(s, e);
		} else if (t instanceof Ast.ListExpr) {
			for (Ast.Expr e : ((Ast.ListExpr) t).elts)
				target(s, e);
		} else if (t instanceof Ast.Subscript) {
			expression(s, ((Ast.Subscript) t).value);
			expression(s, ((Ast.Subscript) t).slice);
		} else if (t instanceof Ast.Attribute) {
			expression(s, ((Ast.Attribute) t).value);
		} else {
			throw error("cannot assign to " + t.type(), t);
		}
	}

	private void expressionOrNull(Scope s, Ast.Expr e) {
		if (e != null)
			expression(s, e);
	}

	private void expressions(Scope s, List<Ast.Expr> list) {
		for (Ast.Expr e : list)
			expression(s, e);
	}

	private void expression(Scope s, Ast.Expr e) {
		enter(e, 1);
		if (e instanceof Ast.Name) {
			s.addFlags(((Ast.Name) e).id, Scope.USED);
		} else if (e instanceof Ast.Constant) {
			// nothing to resolve
		} else if (e instanceof Ast.BoolOp) {
			expressions(s, ((Ast.BoolOp) e).values);
		} else if (e instanceof Ast.BinOp) {
			expression(s, ((Ast.BinOp) e).left);
			expression(s, ((Ast.BinOp) e).right);
		} else if (e instanceof Ast.UnaryOp) {
			expression(s, ((Ast.UnaryOp) e).operand);
		} else if (e instanceof Ast.IfExp) {
			Ast.IfExp i = (Ast.IfExp) e;
			expression(s, i.test);
			expression(s, i.body);
			expression(s, i.orelse);
		} else if (e instanceof Ast.DictExpr) {
			expressions(s, ((Ast.DictExpr) e).keys);
			expressions(s, ((Ast.DictExpr) e).values);
		} else if (e instanceof Ast.ListExpr) {
			expressions(s, ((Ast.ListExpr) e).elts);
		} else if (e instanceof Ast.SetExpr) {
			expressions(s, ((Ast.SetExpr) e).elts);
		} else if (e instanceof Ast.TupleExpr) {
			expressions(s, ((Ast.TupleExpr) e).elts);
		} else if (e instanceof Ast.Compare) {
			expression(s, ((Ast.Compare) e).left);
			expressions(s, ((Ast.Compare) e).comparators);
		} else if (e instanceof Ast.Call) {
			Ast.Call c = (Ast.Call) e;
			if (c.func instanceof Ast.Name && ((Ast.Name) c.func).id.equals("super"))
				throw error("super() is not supported: call the base class explicitly, e.g. Base.__init__(self)",
						e);
			expression(s, c.func);
			expressions(s, c.args);
			for (Ast.Keyword k : c.keywords)
				expression(s, k.value);
		} else if (e instanceof Ast.Starred) {
			expression(s, ((Ast.Starred) e).value);
		} else if (e instanceof Ast.Attribute) {
			expression(s, ((Ast.Attribute) e).value);
		} else if (e instanceof Ast.Subscript) {
			expression(s, ((Ast.Subscript) e).value);
			expression(s, ((Ast.Subscript) e).slice);
		} else if (e instanceof Ast.Slice) {
			Ast.Slice sl = (Ast.Slice) e;
			expressionOrNull(s, sl.lower);
			expressionOrNull(s, sl.upper);
			expressionOrNull(s, sl.step);
		} else if (e instanceof Ast.JoinedStr) {
			expressions(s, ((Ast.JoinedStr) e).values);
		} else if (e instanceof Ast.FormattedValue) {
			expression(s, ((Ast.FormattedValue) e).value);
			expressionOrNull(s, ((Ast.FormattedValue) e).formatSpec);
		} else if (e instanceof Ast.Lambda) {
			Ast.Lambda l = (Ast.Lambda) e;
			defaults(s, l.args);
			Scope child = function(s, "<lambda>", l, l.args);
			l.scope = child;
			expression(child, l.body);
		} else if (e instanceof Ast.CompExpr) {
			comprehension(s, (Ast.CompExpr) e);
		} else {
			throw error("unsupported expression " + e.type(), e);
		}
		nesting--;
	}

	private void comprehension(Scope s, Ast.CompExpr e) {
		Scope child = new Scope(Scope.COMPREHENSION, e.scopeName(), s, e);
		e.scope = child;
		all.add(child);
		child.addFlags(".0", Scope.PARAM | Scope.BOUND);
		child.params.add(".0");
		child.argcount = 1;
		enter(e, e.generators.size()); // CodeGen nests one loop per generator
		boolean first = true;
		for (Ast.Comprehension g : e.generators) {
			if (first)
				expression(s, g.iter); // evaluated in the enclosing scope
			else
				expression(child, g.iter);
			first = false;
			target(child, g.target);
			expressions(child, g.ifs);
		}
		if (e.elt() != null) {
			expression(child, e.elt());
		} else {
			expression(child, ((Ast.DictComp) e).key);
			expression(child, ((Ast.DictComp) e).value);
		}
		nesting -= e.generators.size();
	}

	// ------------------------------------------------------------ resolution

	private void resolveNames(Scope s) {
		if (s.kind == Scope.MODULE)
			return;
		List<String> names = new ArrayList<String>(s.symbols.keySet());
		for (String name : names) {
			int f = s.flags(name);
			if ((f & Scope.GLOBAL) != 0)
				continue;
			if ((f & Scope.NONLOCAL) != 0) {
				if (!markFree(s, name))
					throw new PySyntaxError("no binding for nonlocal '" + name + "' found", s.node.line, s.node.col);
			} else if ((f & (Scope.BOUND | Scope.PARAM)) == 0) {
				markFree(s, name); // otherwise it is a global
			}
		}
	}

	/** Looks for the binding of a name in the enclosing functions; makes it a cell there and free in between. */
	private boolean markFree(Scope s, String name) {
		Scope shadowing = null;
		for (Scope p = s.parent; p != null && p.kind != Scope.MODULE; p = p.parent) {
			if (p.kind == Scope.CLASS) {
				// the names of a class body are not visible from the functions nested in it
				if (p.isLocal(name))
					shadowing = p;
				continue;
			}
			int pf = p.flags(name);
			if ((pf & Scope.GLOBAL) != 0)
				return false;
			if (p.isLocal(name)) {
				if (shadowing != null)
					throw new PySyntaxError("class attribute '" + name
							+ "' has the name of a variable of the enclosing function used inside the class",
							shadowing.node.line, shadowing.node.col);
				p.addFlags(name, Scope.CELL);
				for (Scope q = s; q != p; q = q.parent)
					q.freeNames.add(name);
				return true;
			}
		}
		return false;
	}
}
