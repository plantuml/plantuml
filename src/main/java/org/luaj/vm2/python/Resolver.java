package org.luaj.vm2.python;

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
	private final List<Scope> all = new ArrayList<Scope>();

	/** Analyses a Module node; the scope of each function-like node is stored in its info field. */
	static Scope resolve(Node module) {
		Resolver r = new Resolver();
		Scope top = new Scope(Scope.MODULE, "<module>", null, module);
		module.info = top;
		r.all.add(top);
		r.statements(top, module.nodes("body"));
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

	private void statements(Scope s, List<Node> body) {
		for (Node n : body)
			statement(s, n);
	}

	private void bind(Scope s, String name, Node at) {
		if ((s.flags(name) & Scope.GLOBAL) == 0 && s.kind != Scope.MODULE) {
			// plain local binding
		}
		s.addFlags(name, Scope.BOUND);
	}

	private void statement(Scope s, Node n) {
		switch (n.type) {
		case "FunctionDef": {
			bind(s, n.str("name"), n);
			Node args = n.node("args");
			defaults(s, args);
			Scope child = function(s, n.str("name"), n, args);
			statements(child, n.nodes("body"));
			break;
		}
		case "Return":
			expressionOrNull(s, n.node("value"));
			break;
		case "Delete":
			for (Node t : n.nodes("targets"))
				target(s, t);
			break;
		case "Assign":
			expression(s, n.node("value"));
			for (Node t : n.nodes("targets"))
				target(s, t);
			break;
		case "AugAssign":
			expression(s, n.node("value"));
			Node t = n.node("target");
			if (t.is("Name")) {
				s.addFlags(t.str("id"), Scope.USED);
				target(s, t);
			} else {
				target(s, t);
				expression(s, t);
			}
			break;
		case "For":
			expression(s, n.node("iter"));
			target(s, n.node("target"));
			statements(s, n.nodes("body"));
			statements(s, n.nodes("orelse"));
			break;
		case "While":
		case "If":
			expression(s, n.node("test"));
			statements(s, n.nodes("body"));
			statements(s, n.nodes("orelse"));
			break;
		case "Raise":
			expressionOrNull(s, n.node("exc"));
			expressionOrNull(s, n.node("cause"));
			break;
		case "Try":
			statements(s, n.nodes("body"));
			for (Node h : n.nodes("handlers")) {
				expressionOrNull(s, h.node("type"));
				String name = (String) h.get("name");
				if (name != null)
					bind(s, name, h);
				statements(s, h.nodes("body"));
			}
			statements(s, n.nodes("orelse"));
			statements(s, n.nodes("finalbody"));
			break;
		case "Assert":
			expression(s, n.node("test"));
			expressionOrNull(s, n.node("msg"));
			break;
		case "Import":
			for (Node a : n.nodes("names")) {
				String as = (String) a.get("asname");
				String name = as != null ? as : a.str("name");
				int dot = name.indexOf('.');
				if (dot >= 0)
					name = name.substring(0, dot);
				bind(s, name, a);
			}
			break;
		case "Global":
		case "Nonlocal": {
			boolean global = n.is("Global");
			for (Object o : (List<?>) n.get("names")) {
				String name = (String) o;
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
			break;
		}
		case "Expr":
			expression(s, n.node("value"));
			break;
		case "Pass":
		case "Break":
		case "Continue":
			break;
		default:
			throw error("unsupported statement " + n.type, n);
		}
	}

	private void defaults(Scope s, Node args) {
		for (Node d : args.nodes("defaults"))
			expression(s, d);
		for (Node d : args.nodes("kw_defaults"))
			expressionOrNull(s, d);
	}

	private Scope function(Scope parent, String name, Node node, Node args) {
		Scope child = new Scope(Scope.FUNCTION, name, parent, node);
		node.info = child;
		all.add(child);
		for (Node a : args.nodes("args"))
			param(child, a);
		child.argcount = args.nodes("args").size();
		for (Node a : args.nodes("kwonlyargs"))
			param(child, a);
		child.kwonlycount = args.nodes("kwonlyargs").size();
		Node va = args.node("vararg");
		if (va != null) {
			param(child, va);
			child.varargs = va.str("arg");
		}
		Node kw = args.node("kwarg");
		if (kw != null) {
			param(child, kw);
			child.kwargs = kw.str("arg");
		}
		return child;
	}

	private void param(Scope s, Node arg) {
		String name = arg.str("arg");
		if ((s.flags(name) & Scope.PARAM) != 0)
			throw error("duplicate argument '" + name + "' in function definition", arg);
		s.addFlags(name, Scope.PARAM | Scope.BOUND);
		s.params.add(name);
	}

	private void target(Scope s, Node t) {
		switch (t.type) {
		case "Name":
			s.addFlags(t.str("id"), Scope.BOUND);
			break;
		case "Tuple":
		case "List":
			for (Node e : t.nodes("elts"))
				target(s, e);
			break;
		case "Subscript":
			expression(s, t.node("value"));
			expression(s, t.node("slice"));
			break;
		case "Attribute":
			expression(s, t.node("value"));
			break;
		default:
			throw error("cannot assign to " + t.type, t);
		}
	}

	private void expressionOrNull(Scope s, Node e) {
		if (e != null)
			expression(s, e);
	}

	private void expression(Scope s, Node e) {
		switch (e.type) {
		case "Name":
			s.addFlags(e.str("id"), Scope.USED);
			break;
		case "Constant":
			break;
		case "BoolOp":
			for (Node v : e.nodes("values"))
				expression(s, v);
			break;
		case "BinOp":
			expression(s, e.node("left"));
			expression(s, e.node("right"));
			break;
		case "UnaryOp":
			expression(s, e.node("operand"));
			break;
		case "IfExp":
			expression(s, e.node("test"));
			expression(s, e.node("body"));
			expression(s, e.node("orelse"));
			break;
		case "Dict":
			for (Node k : e.nodes("keys"))
				expression(s, k);
			for (Node v : e.nodes("values"))
				expression(s, v);
			break;
		case "List":
		case "Tuple":
			for (Node v : e.nodes("elts"))
				expression(s, v);
			break;
		case "Compare":
			expression(s, e.node("left"));
			for (Node c : e.nodes("comparators"))
				expression(s, c);
			break;
		case "Call":
			expression(s, e.node("func"));
			for (Node a : e.nodes("args"))
				expression(s, a);
			for (Node k : e.nodes("keywords"))
				expression(s, k.node("value"));
			break;
		case "Starred":
			expression(s, e.node("value"));
			break;
		case "Attribute":
			expression(s, e.node("value"));
			break;
		case "Subscript":
			expression(s, e.node("value"));
			expression(s, e.node("slice"));
			break;
		case "Slice":
			expressionOrNull(s, e.node("lower"));
			expressionOrNull(s, e.node("upper"));
			expressionOrNull(s, e.node("step"));
			break;
		case "JoinedStr":
			for (Node v : e.nodes("values"))
				expression(s, v);
			break;
		case "FormattedValue":
			expression(s, e.node("value"));
			expressionOrNull(s, e.node("format_spec"));
			break;
		case "Lambda": {
			Node args = e.node("args");
			defaults(s, args);
			Scope child = function(s, "<lambda>", e, args);
			expression(child, e.node("body"));
			break;
		}
		case "ListComp":
		case "DictComp":
			comprehension(s, e);
			break;
		default:
			throw error("unsupported expression " + e.type, e);
		}
	}

	private void comprehension(Scope s, Node e) {
		String name = e.is("ListComp") ? "<listcomp>" : "<dictcomp>";
		Scope child = new Scope(Scope.COMPREHENSION, name, s, e);
		e.info = child;
		all.add(child);
		child.addFlags(".0", Scope.PARAM | Scope.BOUND);
		child.params.add(".0");
		child.argcount = 1;
		boolean first = true;
		for (Node g : e.nodes("generators")) {
			if (first)
				expression(s, g.node("iter")); // evaluated in the enclosing scope
			else
				expression(child, g.node("iter"));
			first = false;
			target(child, g.node("target"));
			for (Node c : g.nodes("ifs"))
				expression(child, c);
		}
		if (e.is("ListComp")) {
			expression(child, e.node("elt"));
		} else {
			expression(child, e.node("key"));
			expression(child, e.node("value"));
		}
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
		for (Scope p = s.parent; p != null && p.kind != Scope.MODULE; p = p.parent) {
			int pf = p.flags(name);
			if ((pf & Scope.GLOBAL) != 0)
				return false;
			if (p.isLocal(name)) {
				p.addFlags(name, Scope.CELL);
				for (Scope q = s; q != p; q = q.parent)
					q.freeNames.add(name);
				return true;
			}
		}
		return false;
	}
}
