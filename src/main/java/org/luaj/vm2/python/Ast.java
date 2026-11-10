package org.luaj.vm2.python;

import java.util.ArrayList;
import java.util.List;

/**
 * The kinds of syntax tree nodes. Statements extend {@link Stmt}, expressions
 * extend {@link Expr}; the other nodes (arguments, handlers, keywords...) extend
 * {@link Node} directly. Operators and contexts are enums, whose constants carry
 * the names CPython gives them (they are printed as is by {@link AstDump}).
 *
 * <p>A field holding a list of nodes is never null; an optional node is null.
 * The values of a {@link Constant} are: null (None), a {@link Boolean}, a
 * {@link Long} (int), a {@link Double} (float) or a {@link String}.
 */
public final class Ast {
	private Ast() {
	}

	/** Whether a name is read, assigned or deleted. */
	public enum Ctx {
		Load, Store, Del
	}

	/** Binary operators. */
	public enum Operator {
		Add, Sub, Mult, Div, FloorDiv, Mod, Pow, LShift, RShift, BitAnd, BitOr, BitXor
	}

	public enum UnaryOperator {
		Invert, Not, UAdd, USub
	}

	public enum BoolOperator {
		And, Or
	}

	public enum CmpOperator {
		Eq, NotEq, Lt, LtE, Gt, GtE, Is, IsNot, In, NotIn
	}

	/** A statement. */
	public abstract static class Stmt extends Node {
		Stmt(int line, int col) {
			super(line, col);
		}
	}

	/** An expression. */
	public abstract static class Expr extends Node {
		Expr(int line, int col) {
			super(line, col);
		}
	}

	/** An expression that can be a target: it carries a context. */
	public abstract static class CtxExpr extends Expr {
		public Ctx ctx = Ctx.Load;

		CtxExpr(int line, int col) {
			super(line, col);
		}
	}

	// ------------------------------------------------------------ module and statements

	public static final class Module extends Node {
		public final List<Stmt> body;
		/** Set by the resolver. */
		Scope scope;

		Module(int line, int col, List<Stmt> body) {
			super(line, col);
			this.body = body;
		}

		@Override
		public String type() {
			return "Module";
		}

		@Override
		Object[] fields() {
			return new Object[] { "body", body };
		}
	}

	public static final class FunctionDef extends Stmt {
		public final String name;
		public final Arguments args;
		public final List<Stmt> body;
		public final List<Expr> decorators;
		/** Set by the resolver. */
		Scope scope;

		FunctionDef(int line, int col, String name, Arguments args, List<Stmt> body, List<Expr> decorators) {
			super(line, col);
			this.name = name;
			this.args = args;
			this.body = body;
			this.decorators = decorators;
		}

		@Override
		public String type() {
			return "FunctionDef";
		}

		@Override
		Object[] fields() {
			return new Object[] { "name", name, "args", args, "body", body, "decorator_list", decorators, "returns",
					null };
		}
	}

	public static final class ClassDef extends Stmt {
		public final String name;
		public final List<Expr> bases;
		public final List<Stmt> body;
		public final List<Expr> decorators;
		/** Set by the resolver. */
		Scope scope;

		ClassDef(int line, int col, String name, List<Expr> bases, List<Stmt> body, List<Expr> decorators) {
			super(line, col);
			this.name = name;
			this.bases = bases;
			this.body = body;
			this.decorators = decorators;
		}

		@Override
		public String type() {
			return "ClassDef";
		}

		@Override
		Object[] fields() {
			return new Object[] { "name", name, "bases", bases, "keywords", new ArrayList<Keyword>(), "body", body,
					"decorator_list", decorators };
		}
	}

	public static final class Return extends Stmt {
		public final Expr value;

		Return(int line, int col, Expr value) {
			super(line, col);
			this.value = value;
		}

		@Override
		public String type() {
			return "Return";
		}

		@Override
		Object[] fields() {
			return new Object[] { "value", value };
		}
	}

	public static final class Delete extends Stmt {
		public final List<Expr> targets;

		Delete(int line, int col, List<Expr> targets) {
			super(line, col);
			this.targets = targets;
		}

		@Override
		public String type() {
			return "Delete";
		}

		@Override
		Object[] fields() {
			return new Object[] { "targets", targets };
		}
	}

	public static final class Assign extends Stmt {
		public final List<Expr> targets;
		public final Expr value;

		Assign(int line, int col, List<Expr> targets, Expr value) {
			super(line, col);
			this.targets = targets;
			this.value = value;
		}

		@Override
		public String type() {
			return "Assign";
		}

		@Override
		Object[] fields() {
			return new Object[] { "targets", targets, "value", value };
		}
	}

	public static final class AugAssign extends Stmt {
		public final Expr target;
		public final Operator op;
		public final Expr value;

		AugAssign(int line, int col, Expr target, Operator op, Expr value) {
			super(line, col);
			this.target = target;
			this.op = op;
			this.value = value;
		}

		@Override
		public String type() {
			return "AugAssign";
		}

		@Override
		Object[] fields() {
			return new Object[] { "target", target, "op", op, "value", value };
		}
	}

	public static final class For extends Stmt {
		public final Expr target;
		public final Expr iter;
		public final List<Stmt> body;
		public final List<Stmt> orelse;

		For(int line, int col, Expr target, Expr iter, List<Stmt> body, List<Stmt> orelse) {
			super(line, col);
			this.target = target;
			this.iter = iter;
			this.body = body;
			this.orelse = orelse;
		}

		@Override
		public String type() {
			return "For";
		}

		@Override
		Object[] fields() {
			return new Object[] { "target", target, "iter", iter, "body", body, "orelse", orelse };
		}
	}

	public static final class While extends Stmt {
		public final Expr test;
		public final List<Stmt> body;
		public final List<Stmt> orelse;

		While(int line, int col, Expr test, List<Stmt> body, List<Stmt> orelse) {
			super(line, col);
			this.test = test;
			this.body = body;
			this.orelse = orelse;
		}

		@Override
		public String type() {
			return "While";
		}

		@Override
		Object[] fields() {
			return new Object[] { "test", test, "body", body, "orelse", orelse };
		}
	}

	public static final class If extends Stmt {
		public final Expr test;
		public final List<Stmt> body;
		public final List<Stmt> orelse;

		If(int line, int col, Expr test, List<Stmt> body, List<Stmt> orelse) {
			super(line, col);
			this.test = test;
			this.body = body;
			this.orelse = orelse;
		}

		@Override
		public String type() {
			return "If";
		}

		@Override
		Object[] fields() {
			return new Object[] { "test", test, "body", body, "orelse", orelse };
		}
	}

	public static final class Raise extends Stmt {
		public final Expr exc;
		public final Expr cause;

		Raise(int line, int col, Expr exc, Expr cause) {
			super(line, col);
			this.exc = exc;
			this.cause = cause;
		}

		@Override
		public String type() {
			return "Raise";
		}

		@Override
		Object[] fields() {
			return new Object[] { "exc", exc, "cause", cause };
		}
	}

	public static final class Try extends Stmt {
		public final List<Stmt> body;
		public final List<ExceptHandler> handlers;
		public final List<Stmt> orelse;

		Try(int line, int col, List<Stmt> body, List<ExceptHandler> handlers, List<Stmt> orelse) {
			super(line, col);
			this.body = body;
			this.handlers = handlers;
			this.orelse = orelse;
		}

		@Override
		public String type() {
			return "Try";
		}

		@Override
		Object[] fields() {
			// 'finally' is rejected by the parser: finalbody is always empty
			return new Object[] { "body", body, "handlers", handlers, "orelse", orelse, "finalbody",
					new ArrayList<Stmt>() };
		}
	}

	public static final class Assert extends Stmt {
		public final Expr test;
		public final Expr msg;

		Assert(int line, int col, Expr test, Expr msg) {
			super(line, col);
			this.test = test;
			this.msg = msg;
		}

		@Override
		public String type() {
			return "Assert";
		}

		@Override
		Object[] fields() {
			return new Object[] { "test", test, "msg", msg };
		}
	}

	public static final class Import extends Stmt {
		public final List<Alias> names;

		Import(int line, int col, List<Alias> names) {
			super(line, col);
			this.names = names;
		}

		@Override
		public String type() {
			return "Import";
		}

		@Override
		Object[] fields() {
			return new Object[] { "names", names };
		}
	}

	/** Common part of {@link Global} and {@link Nonlocal}. */
	public abstract static class NameDeclaration extends Stmt {
		public final List<String> names;

		NameDeclaration(int line, int col, List<String> names) {
			super(line, col);
			this.names = names;
		}

		@Override
		Object[] fields() {
			return new Object[] { "names", names };
		}
	}

	public static final class Global extends NameDeclaration {
		Global(int line, int col, List<String> names) {
			super(line, col, names);
		}

		@Override
		public String type() {
			return "Global";
		}
	}

	public static final class Nonlocal extends NameDeclaration {
		Nonlocal(int line, int col, List<String> names) {
			super(line, col, names);
		}

		@Override
		public String type() {
			return "Nonlocal";
		}
	}

	/** An expression used as a statement (CPython's {@code Expr} node). */
	public static final class ExprStmt extends Stmt {
		public final Expr value;

		ExprStmt(int line, int col, Expr value) {
			super(line, col);
			this.value = value;
		}

		@Override
		public String type() {
			return "Expr";
		}

		@Override
		Object[] fields() {
			return new Object[] { "value", value };
		}
	}

	public static final class Pass extends Stmt {
		Pass(int line, int col) {
			super(line, col);
		}

		@Override
		public String type() {
			return "Pass";
		}

		@Override
		Object[] fields() {
			return new Object[0];
		}
	}

	public static final class Break extends Stmt {
		Break(int line, int col) {
			super(line, col);
		}

		@Override
		public String type() {
			return "Break";
		}

		@Override
		Object[] fields() {
			return new Object[0];
		}
	}

	public static final class Continue extends Stmt {
		Continue(int line, int col) {
			super(line, col);
		}

		@Override
		public String type() {
			return "Continue";
		}

		@Override
		Object[] fields() {
			return new Object[0];
		}
	}

	// ------------------------------------------------------------ nodes that are neither statements nor expressions

	public static final class ExceptHandler extends Node {
		/** The class(es) caught, or null for a bare except. */
		public final Expr exceptionType;
		/** The name after "as", or null. */
		public final String name;
		public final List<Stmt> body;

		ExceptHandler(int line, int col, Expr exceptionType, String name, List<Stmt> body) {
			super(line, col);
			this.exceptionType = exceptionType;
			this.name = name;
			this.body = body;
		}

		@Override
		public String type() {
			return "ExceptHandler";
		}

		@Override
		Object[] fields() {
			return new Object[] { "type", exceptionType, "name", name, "body", body };
		}
	}

	/** One name of an import statement. */
	public static final class Alias extends Node {
		public final String name;
		/** The name after "as", or null. */
		public final String asname;

		Alias(int line, int col, String name, String asname) {
			super(line, col);
			this.name = name;
			this.asname = asname;
		}

		@Override
		public String type() {
			return "alias";
		}

		@Override
		Object[] fields() {
			return new Object[] { "name", name, "asname", asname };
		}
	}

	/** The parameters of a def or a lambda. */
	public static final class Arguments extends Node {
		public final List<Arg> args;
		/** The parameter after "*", or null. */
		public final Arg vararg;
		public final List<Arg> kwonlyargs;
		/** One per keyword-only parameter: its default, or null. */
		public final List<Expr> kwDefaults;
		/** The parameter after "**", or null. */
		public final Arg kwarg;
		/** The defaults of the last positional parameters. */
		public final List<Expr> defaults;

		Arguments(int line, int col, List<Arg> args, Arg vararg, List<Arg> kwonlyargs, List<Expr> kwDefaults,
				Arg kwarg, List<Expr> defaults) {
			super(line, col);
			this.args = args;
			this.vararg = vararg;
			this.kwonlyargs = kwonlyargs;
			this.kwDefaults = kwDefaults;
			this.kwarg = kwarg;
			this.defaults = defaults;
		}

		@Override
		public String type() {
			return "arguments";
		}

		@Override
		Object[] fields() {
			return new Object[] { "posonlyargs", new ArrayList<Arg>(), "args", args, "vararg", vararg, "kwonlyargs",
					kwonlyargs, "kw_defaults", kwDefaults, "kwarg", kwarg, "defaults", defaults };
		}
	}

	/** One parameter. */
	public static final class Arg extends Node {
		public final String arg;

		Arg(int line, int col, String arg) {
			super(line, col);
			this.arg = arg;
		}

		@Override
		public String type() {
			return "arg";
		}

		@Override
		Object[] fields() {
			return new Object[] { "arg", arg, "annotation", null };
		}
	}

	/** A keyword argument of a call: {@code name=value}, or {@code **value} when name is null. */
	public static final class Keyword extends Node {
		public final String arg;
		public final Expr value;

		Keyword(int line, int col, String arg, Expr value) {
			super(line, col);
			this.arg = arg;
			this.value = value;
		}

		@Override
		public String type() {
			return "keyword";
		}

		@Override
		Object[] fields() {
			return new Object[] { "arg", arg, "value", value };
		}
	}

	/** One {@code for ... in ... if ...} clause of a comprehension. */
	public static final class Comprehension extends Node {
		public final Expr target;
		public final Expr iter;
		public final List<Expr> ifs;

		Comprehension(int line, int col, Expr target, Expr iter, List<Expr> ifs) {
			super(line, col);
			this.target = target;
			this.iter = iter;
			this.ifs = ifs;
		}

		@Override
		public String type() {
			return "comprehension";
		}

		@Override
		Object[] fields() {
			return new Object[] { "target", target, "iter", iter, "ifs", ifs, "is_async", Long.valueOf(0) };
		}
	}

	// ------------------------------------------------------------ expressions

	public static final class BoolOp extends Expr {
		public final BoolOperator op;
		public final List<Expr> values;

		BoolOp(int line, int col, BoolOperator op, List<Expr> values) {
			super(line, col);
			this.op = op;
			this.values = values;
		}

		@Override
		public String type() {
			return "BoolOp";
		}

		@Override
		Object[] fields() {
			return new Object[] { "op", op, "values", values };
		}
	}

	public static final class BinOp extends Expr {
		public final Expr left;
		public final Operator op;
		public final Expr right;

		BinOp(int line, int col, Expr left, Operator op, Expr right) {
			super(line, col);
			this.left = left;
			this.op = op;
			this.right = right;
		}

		@Override
		public String type() {
			return "BinOp";
		}

		@Override
		Object[] fields() {
			return new Object[] { "left", left, "op", op, "right", right };
		}
	}

	public static final class UnaryOp extends Expr {
		public final UnaryOperator op;
		public final Expr operand;

		UnaryOp(int line, int col, UnaryOperator op, Expr operand) {
			super(line, col);
			this.op = op;
			this.operand = operand;
		}

		@Override
		public String type() {
			return "UnaryOp";
		}

		@Override
		Object[] fields() {
			return new Object[] { "op", op, "operand", operand };
		}
	}

	public static final class Lambda extends Expr {
		public final Arguments args;
		public final Expr body;
		/** Set by the resolver. */
		Scope scope;

		Lambda(int line, int col, Arguments args, Expr body) {
			super(line, col);
			this.args = args;
			this.body = body;
		}

		@Override
		public String type() {
			return "Lambda";
		}

		@Override
		Object[] fields() {
			return new Object[] { "args", args, "body", body };
		}
	}

	public static final class IfExp extends Expr {
		public final Expr test;
		public final Expr body;
		public final Expr orelse;

		IfExp(int line, int col, Expr test, Expr body, Expr orelse) {
			super(line, col);
			this.test = test;
			this.body = body;
			this.orelse = orelse;
		}

		@Override
		public String type() {
			return "IfExp";
		}

		@Override
		Object[] fields() {
			return new Object[] { "test", test, "body", body, "orelse", orelse };
		}
	}

	public static final class DictExpr extends Expr {
		public final List<Expr> keys;
		public final List<Expr> values;

		DictExpr(int line, int col, List<Expr> keys, List<Expr> values) {
			super(line, col);
			this.keys = keys;
			this.values = values;
		}

		@Override
		public String type() {
			return "Dict";
		}

		@Override
		Object[] fields() {
			return new Object[] { "keys", keys, "values", values };
		}
	}

	/** Common part of {@link ListComp} and {@link DictComp}. */
	public abstract static class CompExpr extends Expr {
		public final List<Comprehension> generators;
		/** Set by the resolver. */
		Scope scope;

		CompExpr(int line, int col, List<Comprehension> generators) {
			super(line, col);
			this.generators = generators;
		}
	}

	public static final class ListComp extends CompExpr {
		public final Expr elt;

		ListComp(int line, int col, Expr elt, List<Comprehension> generators) {
			super(line, col, generators);
			this.elt = elt;
		}

		@Override
		public String type() {
			return "ListComp";
		}

		@Override
		Object[] fields() {
			return new Object[] { "elt", elt, "generators", generators };
		}
	}

	public static final class DictComp extends CompExpr {
		public final Expr key;
		public final Expr value;

		DictComp(int line, int col, Expr key, Expr value, List<Comprehension> generators) {
			super(line, col, generators);
			this.key = key;
			this.value = value;
		}

		@Override
		public String type() {
			return "DictComp";
		}

		@Override
		Object[] fields() {
			return new Object[] { "key", key, "value", value, "generators", generators };
		}
	}

	public static final class Compare extends Expr {
		public final Expr left;
		public final List<CmpOperator> ops;
		public final List<Expr> comparators;

		Compare(int line, int col, Expr left, List<CmpOperator> ops, List<Expr> comparators) {
			super(line, col);
			this.left = left;
			this.ops = ops;
			this.comparators = comparators;
		}

		@Override
		public String type() {
			return "Compare";
		}

		@Override
		Object[] fields() {
			return new Object[] { "left", left, "ops", ops, "comparators", comparators };
		}
	}

	public static final class Call extends Expr {
		public final Expr func;
		public final List<Expr> args;
		public final List<Keyword> keywords;

		Call(int line, int col, Expr func, List<Expr> args, List<Keyword> keywords) {
			super(line, col);
			this.func = func;
			this.args = args;
			this.keywords = keywords;
		}

		@Override
		public String type() {
			return "Call";
		}

		@Override
		Object[] fields() {
			return new Object[] { "func", func, "args", args, "keywords", keywords };
		}
	}

	public static final class FormattedValue extends Expr {
		public final Expr value;
		/** -1 (none), or the code of 's', 'r' or 'a'. */
		public final long conversion;
		/** A {@link JoinedStr}, or null. */
		public final Expr formatSpec;

		FormattedValue(int line, int col, Expr value, long conversion, Expr formatSpec) {
			super(line, col);
			this.value = value;
			this.conversion = conversion;
			this.formatSpec = formatSpec;
		}

		@Override
		public String type() {
			return "FormattedValue";
		}

		@Override
		Object[] fields() {
			return new Object[] { "value", value, "conversion", Long.valueOf(conversion), "format_spec", formatSpec };
		}
	}

	public static final class JoinedStr extends Expr {
		public final List<Expr> values;

		JoinedStr(int line, int col, List<Expr> values) {
			super(line, col);
			this.values = values;
		}

		@Override
		public String type() {
			return "JoinedStr";
		}

		@Override
		Object[] fields() {
			return new Object[] { "values", values };
		}
	}

	public static final class Constant extends Expr {
		/** null (None), Boolean, Long, Double or String. */
		public final Object value;

		Constant(int line, int col, Object value) {
			super(line, col);
			this.value = value;
		}

		@Override
		public String type() {
			return "Constant";
		}

		@Override
		Object[] fields() {
			return new Object[] { "value", value };
		}
	}

	public static final class Attribute extends CtxExpr {
		public final Expr value;
		public final String attr;

		Attribute(int line, int col, Expr value, String attr) {
			super(line, col);
			this.value = value;
			this.attr = attr;
		}

		@Override
		public String type() {
			return "Attribute";
		}

		@Override
		Object[] fields() {
			return new Object[] { "value", value, "attr", attr, "ctx", ctx };
		}
	}

	public static final class Subscript extends CtxExpr {
		public final Expr value;
		public final Expr slice;

		Subscript(int line, int col, Expr value, Expr slice) {
			super(line, col);
			this.value = value;
			this.slice = slice;
		}

		@Override
		public String type() {
			return "Subscript";
		}

		@Override
		Object[] fields() {
			return new Object[] { "value", value, "slice", slice, "ctx", ctx };
		}
	}

	public static final class Starred extends CtxExpr {
		public final Expr value;

		Starred(int line, int col, Expr value) {
			super(line, col);
			this.value = value;
		}

		@Override
		public String type() {
			return "Starred";
		}

		@Override
		Object[] fields() {
			return new Object[] { "value", value, "ctx", ctx };
		}
	}

	public static final class Name extends CtxExpr {
		public final String id;

		Name(int line, int col, String id) {
			super(line, col);
			this.id = id;
		}

		@Override
		public String type() {
			return "Name";
		}

		@Override
		Object[] fields() {
			return new Object[] { "id", id, "ctx", ctx };
		}
	}

	public static final class ListExpr extends CtxExpr {
		public final List<Expr> elts;

		ListExpr(int line, int col, List<Expr> elts) {
			super(line, col);
			this.elts = elts;
		}

		@Override
		public String type() {
			return "List";
		}

		@Override
		Object[] fields() {
			return new Object[] { "elts", elts, "ctx", ctx };
		}
	}

	public static final class TupleExpr extends CtxExpr {
		public final List<Expr> elts;

		TupleExpr(int line, int col, List<Expr> elts) {
			super(line, col);
			this.elts = elts;
		}

		@Override
		public String type() {
			return "Tuple";
		}

		@Override
		Object[] fields() {
			return new Object[] { "elts", elts, "ctx", ctx };
		}
	}

	public static final class Slice extends Expr {
		public final Expr lower;
		public final Expr upper;
		public final Expr step;

		Slice(int line, int col, Expr lower, Expr upper, Expr step) {
			super(line, col);
			this.lower = lower;
			this.upper = upper;
			this.step = step;
		}

		@Override
		public String type() {
			return "Slice";
		}

		@Override
		Object[] fields() {
			return new Object[] { "lower", lower, "upper", upper, "step", step };
		}
	}
}
