package org.luaj.vm2.python;

import java.util.ArrayList;
import java.util.List;

/**
 * Recursive descent parser for the Python subset described in PYTHON.md. It
 * builds a tree shaped like CPython's {@code ast}. Constructs of Python that
 * are outside the subset are reported as syntax errors.
 */
public final class PyParser {
	/** Nesting limit, so that hostile input cannot overflow the Java stack. */
	private static final int MAX_DEPTH = 200;

	private final List<Token> tokens;
	private int p;
	private int depth;

	private PyParser(List<Token> tokens) {
		this.tokens = tokens;
	}

	/** Parses a whole source file into a {@code Module} node. */
	public static Node parse(String source) {
		PyParser parser = new PyParser(PyLexer.tokenize(source));
		return parser.module();
	}

	// ------------------------------------------------------------ helpers

	private Token peek() {
		return tokens.get(p);
	}

	private Token peekAt(int offset) {
		int i = p + offset;
		return i < tokens.size() ? tokens.get(i) : tokens.get(tokens.size() - 1);
	}

	private Token next() {
		Token t = tokens.get(p);
		if (t.kind != Token.END)
			p++;
		return t;
	}

	private void enter() {
		if (++depth > MAX_DEPTH)
			throw error("too many levels of nesting");
	}

	private PySyntaxError error(String message) {
		Token t = peek();
		return new PySyntaxError(message, t.line, t.col);
	}

	private PySyntaxError invalid() {
		Token t = peek();
		return new PySyntaxError("invalid syntax near '" + t + "'", t.line, t.col);
	}

	private boolean isOp(String op) {
		return peek().isOp(op);
	}

	private boolean isKw(String kw) {
		return peek().isKeyword(kw);
	}

	private boolean acceptOp(String op) {
		if (isOp(op)) {
			next();
			return true;
		}
		return false;
	}

	private boolean acceptKw(String kw) {
		if (isKw(kw)) {
			next();
			return true;
		}
		return false;
	}

	private Token expectOp(String op) {
		if (!isOp(op))
			throw error("expected '" + op + "' but found '" + peek() + "'");
		return next();
	}

	private Token expectKw(String kw) {
		if (!isKw(kw))
			throw error("expected '" + kw + "' but found '" + peek() + "'");
		return next();
	}

	private String expectName() {
		Token t = peek();
		if (t.kind != Token.NAME)
			throw error("expected a name but found '" + t + "'");
		next();
		return t.text;
	}

	private void expectNewline() {
		Token t = peek();
		if (t.kind == Token.NEWLINE)
			next();
		else if (t.kind != Token.END)
			throw error("expected end of line but found '" + t + "'");
	}

	private static Node n(String type, Token t, Object... nv) {
		return new Node(type, t.line, t.col, nv);
	}

	private static Node load() {
		return Node.bare("Load");
	}

	// ------------------------------------------------------------ statements

	private Node module() {
		Token start = peek();
		List<Node> body = new ArrayList<Node>();
		while (peek().kind != Token.END) {
			if (peek().kind == Token.NEWLINE) {
				next();
				continue;
			}
			statement(body);
		}
		return n("Module", start, "body", body);
	}

	private void statement(List<Node> out) {
		Token t = peek();
		if (t.kind == Token.INDENT)
			throw error("unexpected indent");
		if (t.kind == Token.KEYWORD) {
			switch (t.text) {
			case "if":
				out.add(ifStatement());
				return;
			case "while":
				out.add(whileStatement());
				return;
			case "for":
				out.add(forStatement());
				return;
			case "try":
				out.add(tryStatement());
				return;
			case "def":
				out.add(defStatement());
				return;
			case "class":
			case "with":
			case "async":
				throw error("'" + t.text + "' is not supported");
			default:
				break;
			}
		}
		if (t.isOp("@"))
			throw error("decorators are not supported");
		simpleStatements(out);
	}

	private void simpleStatements(List<Node> out) {
		out.add(simpleStatement());
		while (acceptOp(";")) {
			if (peek().kind == Token.NEWLINE || peek().kind == Token.END)
				break;
			out.add(simpleStatement());
		}
		expectNewline();
	}

	private List<Node> block() {
		enter();
		try {
			return blockBody();
		} finally {
			depth--;
		}
	}

	private List<Node> blockBody() {
		expectOp(":");
		List<Node> body = new ArrayList<Node>();
		if (peek().kind == Token.NEWLINE) {
			next();
			if (peek().kind != Token.INDENT)
				throw error("expected an indented block");
			next();
			while (peek().kind != Token.DEDENT && peek().kind != Token.END) {
				if (peek().kind == Token.NEWLINE) {
					next();
					continue;
				}
				statement(body);
			}
			if (peek().kind == Token.DEDENT)
				next();
		} else {
			simpleStatements(body);
		}
		return body;
	}

	private Node ifStatement() {
		Token t = next(); // if / elif
		Node test = test();
		List<Node> body = block();
		List<Node> orelse = new ArrayList<Node>();
		if (isKw("elif")) {
			orelse.add(ifStatement());
		} else if (isKw("else")) {
			next();
			orelse = block();
		}
		return n("If", t, "test", test, "body", body, "orelse", orelse);
	}

	private Node whileStatement() {
		Token t = next();
		Node test = test();
		List<Node> body = block();
		List<Node> orelse = new ArrayList<Node>();
		if (acceptKw("else"))
			orelse = block();
		return n("While", t, "test", test, "body", body, "orelse", orelse);
	}

	private Node forStatement() {
		Token t = next();
		Node target = targetList();
		setContext(target, "Store");
		expectKw("in");
		Node iter = testList();
		List<Node> body = block();
		List<Node> orelse = new ArrayList<Node>();
		if (acceptKw("else"))
			orelse = block();
		return n("For", t, "target", target, "iter", iter, "body", body, "orelse", orelse);
	}

	private Node tryStatement() {
		Token t = next();
		List<Node> body = block();
		List<Node> handlers = new ArrayList<Node>();
		while (isKw("except")) {
			Token h = next();
			if (isOp("*"))
				throw error("except* is not supported");
			Node type = null;
			String name = null;
			if (!isOp(":")) {
				type = test();
				if (acceptKw("as"))
					name = expectName();
			}
			List<Node> hbody = block();
			handlers.add(n("ExceptHandler", h, "type", type, "name", name, "body", hbody));
		}
		if (isKw("finally"))
			throw error("'finally' is not supported");
		if (handlers.isEmpty())
			throw error("expected 'except' block");
		List<Node> orelse = new ArrayList<Node>();
		if (acceptKw("else"))
			orelse = block();
		if (isKw("finally"))
			throw error("'finally' is not supported");
		return n("Try", t, "body", body, "handlers", handlers, "orelse", orelse, "finalbody",
				new ArrayList<Node>());
	}

	private Node defStatement() {
		Token t = next();
		String name = expectName();
		expectOp("(");
		Node args = parameters(")");
		expectOp(")");
		if (isOp("->"))
			throw error("return annotations are not supported");
		List<Node> body = block();
		return n("FunctionDef", t, "name", name, "args", args, "body", body, "decorator_list",
				new ArrayList<Node>(), "returns", null);
	}

	/** Parameters of a def or lambda, up to (not including) the closing token. */
	private Node parameters(String closer) {
		Token start = peek();
		List<Node> args = new ArrayList<Node>();
		List<Node> defaults = new ArrayList<Node>();
		Node vararg = null;
		List<Node> kwonly = new ArrayList<Node>();
		List<Node> kwDefaults = new ArrayList<Node>();
		Node kwarg = null;
		boolean seenStar = false;
		boolean seenDefault = false;
		while (!isOp(closer)) {
			if (isOp("/"))
				throw error("positional-only parameters are not supported");
			if (isOp("*")) {
				Token st = next();
				if (seenStar)
					throw error("duplicate '*' in parameters");
				seenStar = true;
				if (!isOp(",") && !isOp(closer)) {
					vararg = parameter();
				} else if (isOp(closer)) {
					throw new PySyntaxError("named arguments must follow bare *", st.line, st.col);
				}
			} else if (isOp("**")) {
				next();
				kwarg = parameter();
				acceptOp(",");
				if (!isOp(closer))
					throw error("arguments cannot follow var-keyword argument");
				break;
			} else {
				Node a = parameter();
				Node dflt = null;
				if (acceptOp("="))
					dflt = test();
				if (seenStar) {
					kwonly.add(a);
					kwDefaults.add(dflt);
				} else {
					args.add(a);
					if (dflt != null) {
						defaults.add(dflt);
						seenDefault = true;
					} else if (seenDefault) {
						throw error("non-default argument follows default argument");
					}
				}
			}
			if (!acceptOp(","))
				break;
		}
		return n("arguments", start, "posonlyargs", new ArrayList<Node>(), "args", args, "vararg", vararg,
				"kwonlyargs", kwonly, "kw_defaults", kwDefaults, "kwarg", kwarg, "defaults", defaults);
	}

	private Node parameter() {
		Token t = peek();
		String name = expectName();
		if (isOp(":") && !isLambdaParameters)
			throw error("annotations are not supported");
		return n("arg", t, "arg", name, "annotation", null);
	}

	private boolean isLambdaParameters;

	private Node simpleStatement() {
		Token t = peek();
		if (t.kind == Token.KEYWORD) {
			switch (t.text) {
			case "pass":
				next();
				return n("Pass", t);
			case "break":
				next();
				return n("Break", t);
			case "continue":
				next();
				return n("Continue", t);
			case "return": {
				next();
				Node value = startsExpression(peek()) ? testList() : null;
				return n("Return", t, "value", value);
			}
			case "raise": {
				next();
				Node exc = null;
				Node cause = null;
				if (startsExpression(peek())) {
					exc = test();
					if (acceptKw("from"))
						cause = test();
				}
				return n("Raise", t, "exc", exc, "cause", cause);
			}
			case "assert": {
				next();
				Node test = test();
				Node msg = acceptOp(",") ? test() : null;
				return n("Assert", t, "test", test, "msg", msg);
			}
			case "global":
			case "nonlocal": {
				next();
				List<Object> names = new ArrayList<Object>();
				names.add(expectName());
				while (acceptOp(","))
					names.add(expectName());
				return n(t.text.equals("global") ? "Global" : "Nonlocal", t, "names", names);
			}
			case "del": {
				next();
				List<Node> targets = new ArrayList<Node>();
				while (true) {
					Node target = test();
					setContext(target, "Del");
					targets.add(target);
					if (!acceptOp(","))
						break;
					if (!startsExpression(peek()))
						break;
				}
				return n("Delete", t, "targets", targets);
			}
			case "import":
				return importStatement();
			case "from":
				throw error("'from ... import' is not supported");
			case "yield":
				throw error("'yield' is not supported");
			case "await":
				throw error("'await' is not supported");
			default:
				break;
			}
		}
		return expressionStatement();
	}

	private Node importStatement() {
		Token t = next();
		List<Node> names = new ArrayList<Node>();
		while (true) {
			Token nt = peek();
			StringBuilder name = new StringBuilder(expectName());
			while (acceptOp("."))
				name.append('.').append(expectName());
			String as = null;
			if (acceptKw("as"))
				as = expectName();
			names.add(n("alias", nt, "name", name.toString(), "asname", as));
			if (!acceptOp(","))
				break;
		}
		return n("Import", t, "names", names);
	}

	private static String augOp(String text) {
		switch (text) {
		case "+=":
			return "Add";
		case "-=":
			return "Sub";
		case "*=":
			return "Mult";
		case "/=":
			return "Div";
		case "//=":
			return "FloorDiv";
		case "%=":
			return "Mod";
		case "**=":
			return "Pow";
		case "<<=":
			return "LShift";
		case ">>=":
			return "RShift";
		case "&=":
			return "BitAnd";
		case "|=":
			return "BitOr";
		case "^=":
			return "BitXor";
		default:
			return null;
		}
	}

	private Node expressionStatement() {
		Token t = peek();
		Node first = testList();
		Token op = peek();
		if (op.kind == Token.OP) {
			String aug = augOp(op.text);
			if (aug != null) {
				if (!(first.is("Name") || first.is("Attribute") || first.is("Subscript")))
					throw new PySyntaxError("illegal expression for augmented assignment", op.line, op.col);
				next();
				setContext(first, "Store");
				Node value = testList();
				return n("AugAssign", t, "target", first, "op", Node.bare(aug), "value", value);
			}
			if (op.text.equals("=")) {
				List<Node> exprs = new ArrayList<Node>();
				exprs.add(first);
				while (acceptOp("="))
					exprs.add(testList());
				Node value = exprs.remove(exprs.size() - 1);
				for (Node target : exprs)
					setContext(target, "Store");
				return n("Assign", t, "targets", exprs, "value", value);
			}
			if (op.text.equals(":"))
				throw error("annotated assignments are not supported");
			if (op.text.equals(":="))
				throw error("the walrus operator is not supported");
		}
		return n("Expr", t, "value", first);
	}

	private void setContext(Node target, String ctx) {
		switch (target.type) {
		case "Name":
		case "Attribute":
		case "Subscript":
			target.set("ctx", Node.bare(ctx));
			return;
		case "Tuple":
		case "List":
			if (ctx.equals("Del") && target.is("List") && false)
				break;
			target.set("ctx", Node.bare(ctx));
			for (Node e : target.nodes("elts"))
				setContext(e, ctx);
			return;
		case "Starred":
			throw new PySyntaxError("starred assignment targets are not supported", target.line, target.col);
		default:
			break;
		}
		String what = target.is("Constant") ? "literal" : target.is("Call") ? "function call"
				: target.type.toLowerCase();
		throw new PySyntaxError("cannot " + (ctx.equals("Del") ? "delete " : "assign to ") + what, target.line,
				target.col);
	}

	// ------------------------------------------------------------ expressions

	/** True if the token can start an expression. */
	private static boolean startsExpression(Token t) {
		switch (t.kind) {
		case Token.NAME:
		case Token.NUMBER:
		case Token.STRING:
			return true;
		case Token.KEYWORD:
			return t.text.equals("not") || t.text.equals("lambda") || t.text.equals("None")
					|| t.text.equals("True") || t.text.equals("False") || t.text.equals("yield")
					|| t.text.equals("await");
		case Token.OP:
			switch (t.text) {
			case "(":
			case "[":
			case "{":
			case "-":
			case "+":
			case "~":
			case "*":
			case "**":
			case "...":
				return true;
			default:
				return false;
			}
		default:
			return false;
		}
	}

	/** test (',' test)* [','] : a Tuple when there is a comma. */
	private Node testList() {
		Token t = peek();
		Node first = test();
		if (!isOp(","))
			return first;
		List<Node> elts = new ArrayList<Node>();
		elts.add(first);
		while (acceptOp(",")) {
			if (!startsExpression(peek()))
				break;
			elts.add(test());
		}
		return n("Tuple", t, "elts", elts, "ctx", load());
	}

	/** Targets of a for loop or comprehension: bitwise-or expressions separated by commas. */
	private Node targetList() {
		Token t = peek();
		Node first = bitOr();
		if (!isOp(","))
			return first;
		List<Node> elts = new ArrayList<Node>();
		elts.add(first);
		while (acceptOp(",")) {
			if (!startsExpression(peek()) || isKw("in"))
				break;
			elts.add(bitOr());
		}
		return n("Tuple", t, "elts", elts, "ctx", load());
	}

	private Node test() {
		enter();
		try {
			return testBody();
		} finally {
			depth--;
		}
	}

	private Node testBody() {
		Token t = peek();
		if (t.isKeyword("lambda"))
			return lambda();
		if (t.isKeyword("yield") || t.isKeyword("await"))
			throw error("'" + t.text + "' is not supported");
		Node body = orTest();
		if (isKw("if")) {
			next();
			Node cond = orTest();
			expectKw("else");
			Node orelse = test();
			return n("IfExp", t, "test", cond, "body", body, "orelse", orelse);
		}
		return body;
	}

	private Node lambda() {
		Token t = next();
		boolean saved = isLambdaParameters;
		isLambdaParameters = true;
		Node args = parameters(":");
		isLambdaParameters = saved;
		expectOp(":");
		Node body = test();
		return n("Lambda", t, "args", args, "body", body);
	}

	private Node orTest() {
		Token t = peek();
		Node first = andTest();
		if (!isKw("or"))
			return first;
		List<Node> values = new ArrayList<Node>();
		values.add(first);
		while (acceptKw("or"))
			values.add(andTest());
		return n("BoolOp", t, "op", Node.bare("Or"), "values", values);
	}

	private Node andTest() {
		Token t = peek();
		Node first = notTest();
		if (!isKw("and"))
			return first;
		List<Node> values = new ArrayList<Node>();
		values.add(first);
		while (acceptKw("and"))
			values.add(notTest());
		return n("BoolOp", t, "op", Node.bare("And"), "values", values);
	}

	private Node notTest() {
		Token t = peek();
		if (acceptKw("not")) {
			enter();
			try {
				return n("UnaryOp", t, "op", Node.bare("Not"), "operand", notTest());
			} finally {
				depth--;
			}
		}
		return comparison();
	}

	private String comparisonOperator() {
		Token t = peek();
		if (t.kind == Token.OP) {
			switch (t.text) {
			case "<":
				next();
				return "Lt";
			case ">":
				next();
				return "Gt";
			case "<=":
				next();
				return "LtE";
			case ">=":
				next();
				return "GtE";
			case "==":
				next();
				return "Eq";
			case "!=":
				next();
				return "NotEq";
			default:
				return null;
			}
		}
		if (t.isKeyword("in")) {
			next();
			return "In";
		}
		if (t.isKeyword("not") && peekAt(1).isKeyword("in")) {
			next();
			next();
			return "NotIn";
		}
		if (t.isKeyword("is")) {
			next();
			return acceptKw("not") ? "IsNot" : "Is";
		}
		return null;
	}

	private Node comparison() {
		Token t = peek();
		Node left = bitOr();
		List<Node> ops = null;
		List<Node> comparators = null;
		String op;
		while ((op = comparisonOperator()) != null) {
			if (ops == null) {
				ops = new ArrayList<Node>();
				comparators = new ArrayList<Node>();
			}
			ops.add(Node.bare(op));
			comparators.add(bitOr());
		}
		if (ops == null)
			return left;
		return n("Compare", t, "left", left, "ops", ops, "comparators", comparators);
	}

	private Node binary(Node left, String op, Node right, Token t) {
		return n("BinOp", t, "left", left, "op", Node.bare(op), "right", right);
	}

	private Node bitOr() {
		Token t = peek();
		Node left = bitXor();
		while (isOp("|")) {
			next();
			left = binary(left, "BitOr", bitXor(), t);
		}
		return left;
	}

	private Node bitXor() {
		Token t = peek();
		Node left = bitAnd();
		while (isOp("^")) {
			next();
			left = binary(left, "BitXor", bitAnd(), t);
		}
		return left;
	}

	private Node bitAnd() {
		Token t = peek();
		Node left = shift();
		while (isOp("&")) {
			next();
			left = binary(left, "BitAnd", shift(), t);
		}
		return left;
	}

	private Node shift() {
		Token t = peek();
		Node left = arith();
		while (isOp("<<") || isOp(">>")) {
			String op = next().text.equals("<<") ? "LShift" : "RShift";
			left = binary(left, op, arith(), t);
		}
		return left;
	}

	private Node arith() {
		Token t = peek();
		Node left = term();
		while (isOp("+") || isOp("-")) {
			String op = next().text.equals("+") ? "Add" : "Sub";
			left = binary(left, op, term(), t);
		}
		return left;
	}

	private Node term() {
		Token t = peek();
		Node left = factor();
		while (true) {
			String op;
			if (isOp("*"))
				op = "Mult";
			else if (isOp("/"))
				op = "Div";
			else if (isOp("//"))
				op = "FloorDiv";
			else if (isOp("%"))
				op = "Mod";
			else if (isOp("@"))
				throw error("the '@' operator is not supported");
			else
				return left;
			next();
			left = binary(left, op, factor(), t);
		}
	}

	private Node factor() {
		Token t = peek();
		if (isOp("-") || isOp("+") || isOp("~")) {
			next();
			String op = t.text.equals("-") ? "USub" : t.text.equals("+") ? "UAdd" : "Invert";
			enter();
			try {
				return n("UnaryOp", t, "op", Node.bare(op), "operand", factor());
			} finally {
				depth--;
			}
		}
		return power();
	}

	private Node power() {
		Token t = peek();
		Node base = atomExpression();
		if (isOp("**")) {
			next();
			return binary(base, "Pow", factor(), t);
		}
		return base;
	}

	private Node atomExpression() {
		Token t = peek();
		Node e = atom();
		while (true) {
			if (isOp("(")) {
				e = call(e, t);
			} else if (isOp("[")) {
				Token st = next();
				Node slice = subscriptItems();
				expectOp("]");
				e = n("Subscript", t, "value", e, "slice", slice, "ctx", load());
			} else if (isOp(".")) {
				next();
				String attr = expectName();
				e = n("Attribute", t, "value", e, "attr", attr, "ctx", load());
			} else {
				return e;
			}
		}
	}

	private Node call(Node func, Token t) {
		expectOp("(");
		List<Node> args = new ArrayList<Node>();
		List<Node> keywords = new ArrayList<Node>();
		while (!isOp(")")) {
			Token at = peek();
			if (isOp("*")) {
				next();
				args.add(n("Starred", at, "value", test(), "ctx", load()));
			} else if (isOp("**")) {
				next();
				keywords.add(n("keyword", at, "arg", null, "value", test()));
			} else if (at.kind == Token.NAME && peekAt(1).isOp("=")) {
				next();
				next();
				keywords.add(n("keyword", at, "arg", at.text, "value", test()));
			} else {
				Node value = test();
				if (isKw("for"))
					throw error("generator expressions are not supported");
				if (!keywords.isEmpty())
					throw new PySyntaxError("positional argument follows keyword argument", at.line, at.col);
				args.add(value);
			}
			if (!acceptOp(","))
				break;
		}
		expectOp(")");
		return n("Call", t, "func", func, "args", args, "keywords", keywords);
	}

	private Node subscriptItems() {
		Token t = peek();
		Node first = subscriptItem();
		if (!isOp(","))
			return first;
		List<Node> elts = new ArrayList<Node>();
		elts.add(first);
		while (acceptOp(",")) {
			if (isOp("]"))
				break;
			elts.add(subscriptItem());
		}
		return n("Tuple", t, "elts", elts, "ctx", load());
	}

	private Node subscriptItem() {
		Token t = peek();
		Node lower = null;
		if (!isOp(":"))
			lower = test();
		if (!isOp(":"))
			return lower;
		next();
		Node upper = null;
		Node step = null;
		if (!isOp(":") && !isOp(",") && !isOp("]"))
			upper = test();
		if (acceptOp(":")) {
			if (!isOp(",") && !isOp("]"))
				step = test();
		}
		return n("Slice", t, "lower", lower, "upper", upper, "step", step);
	}

	private Node atom() {
		Token t = peek();
		switch (t.kind) {
		case Token.NAME:
			next();
			return n("Name", t, "id", t.text, "ctx", load());
		case Token.NUMBER:
			next();
			return n("Constant", t, "value", t.value);
		case Token.STRING:
			return strings();
		case Token.KEYWORD:
			switch (t.text) {
			case "None":
				next();
				return n("Constant", t, "value", null);
			case "True":
				next();
				return n("Constant", t, "value", Boolean.TRUE);
			case "False":
				next();
				return n("Constant", t, "value", Boolean.FALSE);
			default:
				throw invalidOrUnsupported(t);
			}
		case Token.OP:
			if (t.text.equals("("))
				return parenthesized();
			if (t.text.equals("["))
				return listDisplay();
			if (t.text.equals("{"))
				return dictDisplay();
			if (t.text.equals("..."))
				throw error("'...' is not supported");
			throw invalid();
		default:
			throw invalid();
		}
	}

	private PySyntaxError invalidOrUnsupported(Token t) {
		switch (t.text) {
		case "yield":
		case "await":
		case "class":
		case "with":
		case "async":
			return error("'" + t.text + "' is not supported");
		default:
			return invalid();
		}
	}

	private Node parenthesized() {
		Token t = next();
		if (acceptOp(")"))
			return n("Tuple", t, "elts", new ArrayList<Node>(), "ctx", load());
		if (isOp("*"))
			throw error("starred expressions are not supported");
		Node first = test();
		if (isKw("for"))
			throw error("generator expressions are not supported");
		if (isOp(",")) {
			List<Node> elts = new ArrayList<Node>();
			elts.add(first);
			while (acceptOp(",")) {
				if (isOp(")"))
					break;
				elts.add(test());
			}
			expectOp(")");
			return n("Tuple", t, "elts", elts, "ctx", load());
		}
		if (isOp(":="))
			throw error("the walrus operator is not supported");
		expectOp(")");
		return first;
	}

	private Node listDisplay() {
		Token t = next();
		List<Node> elts = new ArrayList<Node>();
		if (acceptOp("]"))
			return n("List", t, "elts", elts, "ctx", load());
		if (isOp("*"))
			throw error("starred expressions are not supported");
		Node first = test();
		if (isKw("for")) {
			List<Node> generators = comprehensions();
			expectOp("]");
			return n("ListComp", t, "elt", first, "generators", generators);
		}
		elts.add(first);
		while (acceptOp(",")) {
			if (isOp("]"))
				break;
			if (isOp("*"))
				throw error("starred expressions are not supported");
			elts.add(test());
		}
		expectOp("]");
		return n("List", t, "elts", elts, "ctx", load());
	}

	private Node dictDisplay() {
		Token t = next();
		List<Node> keys = new ArrayList<Node>();
		List<Node> values = new ArrayList<Node>();
		if (acceptOp("}"))
			return n("Dict", t, "keys", keys, "values", values);
		if (isOp("**"))
			throw error("dictionary unpacking is not supported");
		Node key = test();
		if (!isOp(":"))
			throw error("set displays and set comprehensions are not supported");
		next();
		Node value = test();
		if (isKw("for")) {
			List<Node> generators = comprehensions();
			expectOp("}");
			return n("DictComp", t, "key", key, "value", value, "generators", generators);
		}
		keys.add(key);
		values.add(value);
		while (acceptOp(",")) {
			if (isOp("}"))
				break;
			if (isOp("**"))
				throw error("dictionary unpacking is not supported");
			keys.add(test());
			expectOp(":");
			values.add(test());
		}
		expectOp("}");
		return n("Dict", t, "keys", keys, "values", values);
	}

	private List<Node> comprehensions() {
		List<Node> generators = new ArrayList<Node>();
		while (isKw("for")) {
			Token t = next();
			Node target = targetList();
			setContext(target, "Store");
			expectKw("in");
			Node iter = orTest();
			List<Node> ifs = new ArrayList<Node>();
			while (isKw("if")) {
				next();
				ifs.add(orTest());
			}
			generators.add(n("comprehension", t, "target", target, "iter", iter, "ifs", ifs, "is_async",
					Long.valueOf(0)));
		}
		if (isKw("async"))
			throw error("'async' is not supported");
		return generators;
	}

	// ------------------------------------------------------------ strings

	private Node strings() {
		Token first = peek();
		List<Object> parts = new ArrayList<Object>();
		boolean anyF = false;
		while (peek().kind == Token.STRING) {
			Token t = next();
			Token.StringLit lit = (Token.StringLit) t.value;
			if (lit.fstring) {
				anyF = true;
				fstringParts(lit.body, lit.raw, t, parts);
			} else {
				parts.add(lit.raw ? lit.body : unescape(lit.body, t));
			}
		}
		// merge adjacent literal parts
		List<Object> merged = new ArrayList<Object>();
		StringBuilder pending = null;
		for (Object part : parts) {
			if (part instanceof String) {
				if (pending == null)
					pending = new StringBuilder();
				pending.append((String) part);
			} else {
				if (pending != null && pending.length() > 0)
					merged.add(pending.toString());
				pending = null;
				merged.add(part);
			}
		}
		if (pending != null && (pending.length() > 0 || !anyF))
			merged.add(pending.toString());
		if (!anyF)
			return n("Constant", first, "value", merged.isEmpty() ? "" : (String) merged.get(0));
		List<Node> values = new ArrayList<Node>();
		for (Object part : merged)
			values.add(part instanceof String ? n("Constant", first, "value", part) : (Node) part);
		return n("JoinedStr", first, "values", values);
	}

	/** Splits the body of an f-string into literal Strings and FormattedValue nodes. */
	private void fstringParts(String body, boolean raw, Token t, List<Object> out) {
		StringBuilder lit = new StringBuilder();
		int i = 0;
		int len = body.length();
		while (i < len) {
			char c = body.charAt(i);
			if (c == '{') {
				if (i + 1 < len && body.charAt(i + 1) == '{') {
					lit.append('{');
					i += 2;
					continue;
				}
				flushLiteral(lit, raw, t, out);
				i = replacementField(body, i + 1, raw, t, out);
			} else if (c == '}') {
				if (i + 1 < len && body.charAt(i + 1) == '}') {
					lit.append('}');
					i += 2;
				} else {
					throw new PySyntaxError("f-string: single '}' is not allowed", t.line, t.col);
				}
			} else if (c == '\\' && !raw && i + 1 < len) {
				lit.append(c).append(body.charAt(i + 1));
				i += 2;
			} else {
				lit.append(c);
				i++;
			}
		}
		flushLiteral(lit, raw, t, out);
	}

	private void flushLiteral(StringBuilder lit, boolean raw, Token t, List<Object> out) {
		if (lit.length() == 0)
			return;
		String s = lit.toString();
		out.add(raw ? s : unescape(s, t));
		lit.setLength(0);
	}

	/** Parses a replacement field whose expression starts at {@code i}; returns the index after '}'. */
	private int replacementField(String body, int i, boolean raw, Token t, List<Object> out) {
		int len = body.length();
		int start = i;
		int depth = 0;
		while (i < len) {
			char c = body.charAt(i);
			if (c == '\'' || c == '"') {
				int j = i + 1;
				while (j < len && body.charAt(j) != c) {
					if (body.charAt(j) == '\\')
						j++;
					j++;
				}
				if (j >= len)
					throw new PySyntaxError("f-string: unterminated string", t.line, t.col);
				i = j + 1;
				continue;
			}
			if (c == '(' || c == '[' || c == '{')
				depth++;
			else if (c == ')' || c == ']' || c == '}') {
				if (depth == 0)
					break;
				depth--;
			} else if (depth == 0 && c == '!' && !(i + 1 < len && body.charAt(i + 1) == '=')) {
				break;
			} else if (depth == 0 && c == ':') {
				break;
			} else if (depth == 0 && c == '=' && i + 1 < len && "!:}".indexOf(body.charAt(i + 1)) >= 0
					&& "=!<>".indexOf(body.charAt(i - 1)) < 0) {
				throw new PySyntaxError("f-string: the '=' specifier is not supported", t.line, t.col);
			}
			i++;
		}
		if (i >= len)
			throw new PySyntaxError("f-string: expecting '}'", t.line, t.col);
		String exprText = body.substring(start, i);
		if (exprText.trim().isEmpty())
			throw new PySyntaxError("f-string: empty expression not allowed", t.line, t.col);
		Node value = subExpression(exprText, t);
		long conversion = -1;
		Node spec = null;
		if (body.charAt(i) == '!') {
			if (i + 1 >= len)
				throw new PySyntaxError("f-string: expecting '}'", t.line, t.col);
			char conv = body.charAt(i + 1);
			if (conv != 'r' && conv != 's' && conv != 'a')
				throw new PySyntaxError("f-string: invalid conversion character", t.line, t.col);
			conversion = conv;
			i += 2;
		}
		if (i < len && body.charAt(i) == ':') {
			i++;
			int specStart = i;
			int d = 0;
			while (i < len) {
				char c = body.charAt(i);
				if (c == '{')
					d++;
				else if (c == '}') {
					if (d == 0)
						break;
					d--;
				}
				i++;
			}
			if (i >= len)
				throw new PySyntaxError("f-string: expecting '}'", t.line, t.col);
			List<Object> specParts = new ArrayList<Object>();
			fstringParts(body.substring(specStart, i), raw, t, specParts);
			List<Node> specValues = new ArrayList<Node>();
			for (Object part : specParts)
				specValues.add(part instanceof String ? n("Constant", t, "value", part) : (Node) part);
			spec = n("JoinedStr", t, "values", specValues);
		}
		if (i >= len || body.charAt(i) != '}')
			throw new PySyntaxError("f-string: expecting '}'", t.line, t.col);
		out.add(n("FormattedValue", t, "value", value, "conversion", Long.valueOf(conversion), "format_spec", spec));
		return i + 1;
	}

	private Node subExpression(String text, Token t) {
		PyParser sub;
		try {
			sub = new PyParser(PyLexer.tokenize("(" + text + "\n)"));
		} catch (PySyntaxError e) {
			throw new PySyntaxError("f-string: " + e.getMessage(), t.line, t.col);
		}
		Node e = sub.testListInParens();
		return e;
	}

	/** Used for f-string fields: the tokens are '(' testlist NEWLINE? ')' END. */
	private Node testListInParens() {
		expectOp("(");
		Node e = testList();
		expectOp(")");
		if (peek().kind == Token.NEWLINE)
			next();
		if (peek().kind != Token.END)
			throw invalid();
		return e;
	}

	/** Decodes the escape sequences of a non-raw string literal body. */
	static String unescape(String s, Token t) {
		if (s.indexOf('\\') < 0)
			return s;
		StringBuilder sb = new StringBuilder();
		int n = s.length();
		for (int i = 0; i < n;) {
			char c = s.charAt(i++);
			if (c != '\\') {
				sb.append(c);
				continue;
			}
			if (i >= n) {
				sb.append('\\');
				break;
			}
			char e = s.charAt(i++);
			switch (e) {
			case '\n':
				break;
			case '\\':
				sb.append('\\');
				break;
			case '\'':
				sb.append('\'');
				break;
			case '"':
				sb.append('"');
				break;
			case 'a':
				sb.append((char) 7);
				break;
			case 'b':
				sb.append('\b');
				break;
			case 'f':
				sb.append('\f');
				break;
			case 'n':
				sb.append('\n');
				break;
			case 'r':
				sb.append('\r');
				break;
			case 't':
				sb.append('\t');
				break;
			case 'v':
				sb.append((char) 11);
				break;
			case 'x':
				i = hexEscape(s, i, 2, sb, t);
				break;
			case 'u':
				i = hexEscape(s, i, 4, sb, t);
				break;
			case 'U':
				i = hexEscape(s, i, 8, sb, t);
				break;
			case 'N':
				throw new PySyntaxError("\\N{...} escapes are not supported", t.line, t.col);
			default:
				if (e >= '0' && e <= '7') {
					int v = e - '0';
					int count = 1;
					while (count < 3 && i < n && s.charAt(i) >= '0' && s.charAt(i) <= '7') {
						v = v * 8 + (s.charAt(i++) - '0');
						count++;
					}
					sb.appendCodePoint(v);
				} else {
					sb.append('\\').append(e);
				}
			}
		}
		return sb.toString();
	}

	private static int hexEscape(String s, int i, int digits, StringBuilder sb, Token t) {
		if (i + digits > s.length())
			throw new PySyntaxError("truncated escape sequence", t.line, t.col);
		int v = 0;
		for (int k = 0; k < digits; k++) {
			int d = Character.digit(s.charAt(i + k), 16);
			if (d < 0)
				throw new PySyntaxError("invalid escape sequence", t.line, t.col);
			v = v * 16 + d;
		}
		if (v > 0x10FFFF || v < 0)
			throw new PySyntaxError("escape sequence out of range", t.line, t.col);
		sb.appendCodePoint(v);
		return i + digits;
	}
}
