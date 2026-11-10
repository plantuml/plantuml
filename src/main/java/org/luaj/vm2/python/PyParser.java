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
	public static Ast.Module parse(String source) {
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

	// ------------------------------------------------------------ statements

	private Ast.Module module() {
		Token start = peek();
		List<Ast.Stmt> body = new ArrayList<Ast.Stmt>();
		while (peek().kind != Token.END) {
			if (peek().kind == Token.NEWLINE) {
				next();
				continue;
			}
			statement(body);
		}
		return new Ast.Module(start.line, start.col, body);
	}

	private void statement(List<Ast.Stmt> out) {
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
				out.add(defStatement(new ArrayList<Ast.Expr>()));
				return;
			case "class":
				out.add(classStatement(new ArrayList<Ast.Expr>()));
				return;
			case "with":
			case "async":
				throw error("'" + t.text + "' is not supported");
			default:
				break;
			}
		}
		if (t.isOp("@")) {
			out.add(decorated());
			return;
		}
		simpleStatements(out);
	}

	/** One or more "@expr" lines, then the def or class they apply to. */
	private Ast.Stmt decorated() {
		List<Ast.Expr> decorators = new ArrayList<Ast.Expr>();
		while (acceptOp("@")) {
			decorators.add(test());
			Token end = peek();
			if (end.kind != Token.NEWLINE)
				throw error("expected end of line after a decorator but found '" + end + "'");
			next();
			while (peek().kind == Token.NEWLINE)
				next();
		}
		Token t = peek();
		if (t.kind == Token.KEYWORD && t.text.equals("def"))
			return defStatement(decorators);
		if (t.kind == Token.KEYWORD && t.text.equals("class"))
			return classStatement(decorators);
		if (t.kind == Token.KEYWORD && t.text.equals("async"))
			throw error("'async' is not supported");
		throw error("a decorator must be followed by def or class");
	}

	private void simpleStatements(List<Ast.Stmt> out) {
		out.add(simpleStatement());
		while (acceptOp(";")) {
			if (peek().kind == Token.NEWLINE || peek().kind == Token.END)
				break;
			out.add(simpleStatement());
		}
		expectNewline();
	}

	private List<Ast.Stmt> block() {
		enter();
		try {
			return blockBody();
		} finally {
			depth--;
		}
	}

	private List<Ast.Stmt> blockBody() {
		expectOp(":");
		List<Ast.Stmt> body = new ArrayList<Ast.Stmt>();
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

	private Ast.If ifStatement() {
		Token t = next(); // if / elif
		Ast.Expr test = test();
		List<Ast.Stmt> body = block();
		List<Ast.Stmt> orelse = new ArrayList<Ast.Stmt>();
		if (isKw("elif")) {
			orelse.add(ifStatement());
		} else if (isKw("else")) {
			next();
			orelse = block();
		}
		return new Ast.If(t.line, t.col, test, body, orelse);
	}

	private Ast.While whileStatement() {
		Token t = next();
		Ast.Expr test = test();
		List<Ast.Stmt> body = block();
		List<Ast.Stmt> orelse = new ArrayList<Ast.Stmt>();
		if (acceptKw("else"))
			orelse = block();
		return new Ast.While(t.line, t.col, test, body, orelse);
	}

	private Ast.For forStatement() {
		Token t = next();
		Ast.Expr target = targetList();
		setContext(target, Ast.Ctx.Store);
		expectKw("in");
		Ast.Expr iter = testList();
		List<Ast.Stmt> body = block();
		List<Ast.Stmt> orelse = new ArrayList<Ast.Stmt>();
		if (acceptKw("else"))
			orelse = block();
		return new Ast.For(t.line, t.col, target, iter, body, orelse);
	}

	private Ast.Try tryStatement() {
		Token t = next();
		List<Ast.Stmt> body = block();
		List<Ast.ExceptHandler> handlers = new ArrayList<Ast.ExceptHandler>();
		while (isKw("except")) {
			Token h = next();
			if (isOp("*"))
				throw error("except* is not supported");
			Ast.Expr type = null;
			String name = null;
			if (!isOp(":")) {
				type = test();
				if (acceptKw("as"))
					name = expectName();
			}
			List<Ast.Stmt> hbody = block();
			handlers.add(new Ast.ExceptHandler(h.line, h.col, type, name, hbody));
		}
		if (isKw("finally"))
			throw error("'finally' is not supported");
		if (handlers.isEmpty())
			throw error("expected 'except' block");
		List<Ast.Stmt> orelse = new ArrayList<Ast.Stmt>();
		if (acceptKw("else"))
			orelse = block();
		if (isKw("finally"))
			throw error("'finally' is not supported");
		return new Ast.Try(t.line, t.col, body, handlers, orelse);
	}

	private Ast.FunctionDef defStatement(List<Ast.Expr> decorators) {
		Token t = next();
		String name = expectName();
		expectOp("(");
		Ast.Arguments args = parameters(")");
		expectOp(")");
		if (isOp("->"))
			throw error("return annotations are not supported");
		List<Ast.Stmt> body = block();
		return new Ast.FunctionDef(t.line, t.col, name, args, body, decorators);
	}

	private Ast.ClassDef classStatement(List<Ast.Expr> decorators) {
		Token t = next();
		String name = expectName();
		List<Ast.Expr> bases = new ArrayList<Ast.Expr>();
		if (acceptOp("(")) {
			while (isOp(")") == false) {
				if (isOp("*") || isOp("**"))
					throw error("starred class bases are not supported");
				bases.add(test());
				if (isOp("="))
					throw error("class keyword arguments are not supported");
				if (acceptOp(",") == false)
					break;
			}
			expectOp(")");
		}
		List<Ast.Stmt> body = block();
		return new Ast.ClassDef(t.line, t.col, name, bases, body, decorators);
	}

	/** Parameters of a def or lambda, up to (not including) the closing token. */
	private Ast.Arguments parameters(String closer) {
		Token start = peek();
		List<Ast.Arg> args = new ArrayList<Ast.Arg>();
		List<Ast.Expr> defaults = new ArrayList<Ast.Expr>();
		Ast.Arg vararg = null;
		List<Ast.Arg> kwonly = new ArrayList<Ast.Arg>();
		List<Ast.Expr> kwDefaults = new ArrayList<Ast.Expr>();
		Ast.Arg kwarg = null;
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
				Ast.Arg a = parameter();
				Ast.Expr dflt = null;
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
		return new Ast.Arguments(start.line, start.col, args, vararg, kwonly, kwDefaults, kwarg, defaults);
	}

	private Ast.Arg parameter() {
		Token t = peek();
		String name = expectName();
		if (isOp(":") && !isLambdaParameters)
			throw error("annotations are not supported");
		return new Ast.Arg(t.line, t.col, name);
	}

	private boolean isLambdaParameters;

	private Ast.Stmt simpleStatement() {
		Token t = peek();
		if (t.kind == Token.KEYWORD) {
			switch (t.text) {
			case "pass":
				next();
				return new Ast.Pass(t.line, t.col);
			case "break":
				next();
				return new Ast.Break(t.line, t.col);
			case "continue":
				next();
				return new Ast.Continue(t.line, t.col);
			case "return": {
				next();
				Ast.Expr value = startsExpression(peek()) ? testList() : null;
				return new Ast.Return(t.line, t.col, value);
			}
			case "raise": {
				next();
				Ast.Expr exc = null;
				Ast.Expr cause = null;
				if (startsExpression(peek())) {
					exc = test();
					if (acceptKw("from"))
						cause = test();
				}
				return new Ast.Raise(t.line, t.col, exc, cause);
			}
			case "assert": {
				next();
				Ast.Expr test = test();
				Ast.Expr msg = acceptOp(",") ? test() : null;
				return new Ast.Assert(t.line, t.col, test, msg);
			}
			case "global":
			case "nonlocal": {
				next();
				List<String> names = new ArrayList<String>();
				names.add(expectName());
				while (acceptOp(","))
					names.add(expectName());
				if (t.text.equals("global"))
					return new Ast.Global(t.line, t.col, names);
				return new Ast.Nonlocal(t.line, t.col, names);
			}
			case "del": {
				next();
				List<Ast.Expr> targets = new ArrayList<Ast.Expr>();
				while (true) {
					Ast.Expr target = test();
					setContext(target, Ast.Ctx.Del);
					targets.add(target);
					if (!acceptOp(","))
						break;
					if (!startsExpression(peek()))
						break;
				}
				return new Ast.Delete(t.line, t.col, targets);
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

	private Ast.Import importStatement() {
		Token t = next();
		List<Ast.Alias> names = new ArrayList<Ast.Alias>();
		while (true) {
			Token nt = peek();
			StringBuilder name = new StringBuilder(expectName());
			while (acceptOp("."))
				name.append('.').append(expectName());
			String as = null;
			if (acceptKw("as"))
				as = expectName();
			names.add(new Ast.Alias(nt.line, nt.col, name.toString(), as));
			if (!acceptOp(","))
				break;
		}
		return new Ast.Import(t.line, t.col, names);
	}

	private static Ast.Operator augOp(String text) {
		switch (text) {
		case "+=":
			return Ast.Operator.Add;
		case "-=":
			return Ast.Operator.Sub;
		case "*=":
			return Ast.Operator.Mult;
		case "/=":
			return Ast.Operator.Div;
		case "//=":
			return Ast.Operator.FloorDiv;
		case "%=":
			return Ast.Operator.Mod;
		case "**=":
			return Ast.Operator.Pow;
		case "<<=":
			return Ast.Operator.LShift;
		case ">>=":
			return Ast.Operator.RShift;
		case "&=":
			return Ast.Operator.BitAnd;
		case "|=":
			return Ast.Operator.BitOr;
		case "^=":
			return Ast.Operator.BitXor;
		default:
			return null;
		}
	}

	private Ast.Stmt expressionStatement() {
		Token t = peek();
		Ast.Expr first = testList();
		Token op = peek();
		if (op.kind == Token.OP) {
			Ast.Operator aug = augOp(op.text);
			if (aug != null) {
				if (!(first instanceof Ast.Name || first instanceof Ast.Attribute || first instanceof Ast.Subscript))
					throw new PySyntaxError("illegal expression for augmented assignment", op.line, op.col);
				next();
				setContext(first, Ast.Ctx.Store);
				Ast.Expr value = testList();
				return new Ast.AugAssign(t.line, t.col, first, aug, value);
			}
			if (op.text.equals("=")) {
				List<Ast.Expr> exprs = new ArrayList<Ast.Expr>();
				exprs.add(first);
				while (acceptOp("="))
					exprs.add(testList());
				Ast.Expr value = exprs.remove(exprs.size() - 1);
				for (Ast.Expr target : exprs)
					setContext(target, Ast.Ctx.Store);
				return new Ast.Assign(t.line, t.col, exprs, value);
			}
			if (op.text.equals(":"))
				throw error("annotated assignments are not supported");
			if (op.text.equals(":="))
				throw error("the walrus operator is not supported");
		}
		return new Ast.ExprStmt(t.line, t.col, first);
	}

	private void setContext(Ast.Expr target, Ast.Ctx ctx) {
		if (target instanceof Ast.Name || target instanceof Ast.Attribute || target instanceof Ast.Subscript) {
			((Ast.CtxExpr) target).ctx = ctx;
			return;
		}
		if (target instanceof Ast.TupleExpr || target instanceof Ast.ListExpr) {
			((Ast.CtxExpr) target).ctx = ctx;
			List<Ast.Expr> elts = target instanceof Ast.TupleExpr ? ((Ast.TupleExpr) target).elts
					: ((Ast.ListExpr) target).elts;
			for (Ast.Expr e : elts)
				setContext(e, ctx);
			return;
		}
		if (target instanceof Ast.Starred)
			throw new PySyntaxError("starred assignment targets are not supported", target.line, target.col);
		String what = target instanceof Ast.Constant ? "literal"
				: target instanceof Ast.Call ? "function call" : target.type().toLowerCase();
		throw new PySyntaxError("cannot " + (ctx == Ast.Ctx.Del ? "delete " : "assign to ") + what, target.line,
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
	private Ast.Expr testList() {
		Token t = peek();
		Ast.Expr first = test();
		if (!isOp(","))
			return first;
		List<Ast.Expr> elts = new ArrayList<Ast.Expr>();
		elts.add(first);
		while (acceptOp(",")) {
			if (!startsExpression(peek()))
				break;
			elts.add(test());
		}
		return new Ast.TupleExpr(t.line, t.col, elts);
	}

	/** Targets of a for loop or comprehension: bitwise-or expressions separated by commas. */
	private Ast.Expr targetList() {
		Token t = peek();
		Ast.Expr first = bitOr();
		if (!isOp(","))
			return first;
		List<Ast.Expr> elts = new ArrayList<Ast.Expr>();
		elts.add(first);
		while (acceptOp(",")) {
			if (!startsExpression(peek()) || isKw("in"))
				break;
			elts.add(bitOr());
		}
		return new Ast.TupleExpr(t.line, t.col, elts);
	}

	private Ast.Expr test() {
		enter();
		try {
			return testBody();
		} finally {
			depth--;
		}
	}

	private Ast.Expr testBody() {
		Token t = peek();
		if (t.isKeyword("lambda"))
			return lambda();
		if (t.isKeyword("yield") || t.isKeyword("await"))
			throw error("'" + t.text + "' is not supported");
		Ast.Expr body = orTest();
		if (isKw("if")) {
			next();
			Ast.Expr cond = orTest();
			expectKw("else");
			Ast.Expr orelse = test();
			return new Ast.IfExp(t.line, t.col, cond, body, orelse);
		}
		return body;
	}

	private Ast.Expr lambda() {
		Token t = next();
		boolean saved = isLambdaParameters;
		isLambdaParameters = true;
		Ast.Arguments args = parameters(":");
		isLambdaParameters = saved;
		expectOp(":");
		Ast.Expr body = test();
		return new Ast.Lambda(t.line, t.col, args, body);
	}

	private Ast.Expr orTest() {
		Token t = peek();
		Ast.Expr first = andTest();
		if (!isKw("or"))
			return first;
		List<Ast.Expr> values = new ArrayList<Ast.Expr>();
		values.add(first);
		while (acceptKw("or"))
			values.add(andTest());
		return new Ast.BoolOp(t.line, t.col, Ast.BoolOperator.Or, values);
	}

	private Ast.Expr andTest() {
		Token t = peek();
		Ast.Expr first = notTest();
		if (!isKw("and"))
			return first;
		List<Ast.Expr> values = new ArrayList<Ast.Expr>();
		values.add(first);
		while (acceptKw("and"))
			values.add(notTest());
		return new Ast.BoolOp(t.line, t.col, Ast.BoolOperator.And, values);
	}

	private Ast.Expr notTest() {
		Token t = peek();
		if (acceptKw("not")) {
			enter();
			try {
				return new Ast.UnaryOp(t.line, t.col, Ast.UnaryOperator.Not, notTest());
			} finally {
				depth--;
			}
		}
		return comparison();
	}

	private Ast.CmpOperator comparisonOperator() {
		Token t = peek();
		if (t.kind == Token.OP) {
			switch (t.text) {
			case "<":
				next();
				return Ast.CmpOperator.Lt;
			case ">":
				next();
				return Ast.CmpOperator.Gt;
			case "<=":
				next();
				return Ast.CmpOperator.LtE;
			case ">=":
				next();
				return Ast.CmpOperator.GtE;
			case "==":
				next();
				return Ast.CmpOperator.Eq;
			case "!=":
				next();
				return Ast.CmpOperator.NotEq;
			default:
				return null;
			}
		}
		if (t.isKeyword("in")) {
			next();
			return Ast.CmpOperator.In;
		}
		if (t.isKeyword("not") && peekAt(1).isKeyword("in")) {
			next();
			next();
			return Ast.CmpOperator.NotIn;
		}
		if (t.isKeyword("is")) {
			next();
			return acceptKw("not") ? Ast.CmpOperator.IsNot : Ast.CmpOperator.Is;
		}
		return null;
	}

	private Ast.Expr comparison() {
		Token t = peek();
		Ast.Expr left = bitOr();
		List<Ast.CmpOperator> ops = null;
		List<Ast.Expr> comparators = null;
		Ast.CmpOperator op;
		while ((op = comparisonOperator()) != null) {
			if (ops == null) {
				ops = new ArrayList<Ast.CmpOperator>();
				comparators = new ArrayList<Ast.Expr>();
			}
			ops.add(op);
			comparators.add(bitOr());
		}
		if (ops == null)
			return left;
		return new Ast.Compare(t.line, t.col, left, ops, comparators);
	}

	private Ast.Expr binary(Ast.Expr left, Ast.Operator op, Ast.Expr right, Token t) {
		return new Ast.BinOp(t.line, t.col, left, op, right);
	}

	private Ast.Expr bitOr() {
		Token t = peek();
		Ast.Expr left = bitXor();
		while (isOp("|")) {
			next();
			left = binary(left, Ast.Operator.BitOr, bitXor(), t);
		}
		return left;
	}

	private Ast.Expr bitXor() {
		Token t = peek();
		Ast.Expr left = bitAnd();
		while (isOp("^")) {
			next();
			left = binary(left, Ast.Operator.BitXor, bitAnd(), t);
		}
		return left;
	}

	private Ast.Expr bitAnd() {
		Token t = peek();
		Ast.Expr left = shift();
		while (isOp("&")) {
			next();
			left = binary(left, Ast.Operator.BitAnd, shift(), t);
		}
		return left;
	}

	private Ast.Expr shift() {
		Token t = peek();
		Ast.Expr left = arith();
		while (isOp("<<") || isOp(">>")) {
			Ast.Operator op = next().text.equals("<<") ? Ast.Operator.LShift : Ast.Operator.RShift;
			left = binary(left, op, arith(), t);
		}
		return left;
	}

	private Ast.Expr arith() {
		Token t = peek();
		Ast.Expr left = term();
		while (isOp("+") || isOp("-")) {
			Ast.Operator op = next().text.equals("+") ? Ast.Operator.Add : Ast.Operator.Sub;
			left = binary(left, op, term(), t);
		}
		return left;
	}

	private Ast.Expr term() {
		Token t = peek();
		Ast.Expr left = factor();
		while (true) {
			Ast.Operator op;
			if (isOp("*"))
				op = Ast.Operator.Mult;
			else if (isOp("/"))
				op = Ast.Operator.Div;
			else if (isOp("//"))
				op = Ast.Operator.FloorDiv;
			else if (isOp("%"))
				op = Ast.Operator.Mod;
			else if (isOp("@"))
				throw error("the '@' operator is not supported");
			else
				return left;
			next();
			left = binary(left, op, factor(), t);
		}
	}

	private Ast.Expr factor() {
		Token t = peek();
		if (isOp("-") || isOp("+") || isOp("~")) {
			next();
			Ast.UnaryOperator op = t.text.equals("-") ? Ast.UnaryOperator.USub
					: t.text.equals("+") ? Ast.UnaryOperator.UAdd : Ast.UnaryOperator.Invert;
			enter();
			try {
				return new Ast.UnaryOp(t.line, t.col, op, factor());
			} finally {
				depth--;
			}
		}
		return power();
	}

	private Ast.Expr power() {
		Token t = peek();
		Ast.Expr base = atomExpression();
		if (isOp("**")) {
			next();
			return binary(base, Ast.Operator.Pow, factor(), t);
		}
		return base;
	}

	private Ast.Expr atomExpression() {
		Token t = peek();
		Ast.Expr e = atom();
		while (true) {
			if (isOp("(")) {
				e = call(e, t);
			} else if (isOp("[")) {
				Token st = next();
				Ast.Expr slice = subscriptItems();
				expectOp("]");
				e = new Ast.Subscript(t.line, t.col, e, slice);
			} else if (isOp(".")) {
				next();
				String attr = expectName();
				e = new Ast.Attribute(t.line, t.col, e, attr);
			} else {
				return e;
			}
		}
	}

	private Ast.Expr call(Ast.Expr func, Token t) {
		expectOp("(");
		List<Ast.Expr> args = new ArrayList<Ast.Expr>();
		List<Ast.Keyword> keywords = new ArrayList<Ast.Keyword>();
		while (!isOp(")")) {
			Token at = peek();
			if (isOp("*")) {
				next();
				args.add(new Ast.Starred(at.line, at.col, test()));
			} else if (isOp("**")) {
				next();
				keywords.add(new Ast.Keyword(at.line, at.col, null, test()));
			} else if (at.kind == Token.NAME && peekAt(1).isOp("=")) {
				next();
				next();
				keywords.add(new Ast.Keyword(at.line, at.col, at.text, test()));
			} else {
				Ast.Expr value = test();
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
		return new Ast.Call(t.line, t.col, func, args, keywords);
	}

	private Ast.Expr subscriptItems() {
		Token t = peek();
		Ast.Expr first = subscriptItem();
		if (!isOp(","))
			return first;
		List<Ast.Expr> elts = new ArrayList<Ast.Expr>();
		elts.add(first);
		while (acceptOp(",")) {
			if (isOp("]"))
				break;
			elts.add(subscriptItem());
		}
		return new Ast.TupleExpr(t.line, t.col, elts);
	}

	private Ast.Expr subscriptItem() {
		Token t = peek();
		Ast.Expr lower = null;
		if (!isOp(":"))
			lower = test();
		if (!isOp(":"))
			return lower;
		next();
		Ast.Expr upper = null;
		Ast.Expr step = null;
		if (!isOp(":") && !isOp(",") && !isOp("]"))
			upper = test();
		if (acceptOp(":")) {
			if (!isOp(",") && !isOp("]"))
				step = test();
		}
		return new Ast.Slice(t.line, t.col, lower, upper, step);
	}

	private Ast.Expr atom() {
		Token t = peek();
		switch (t.kind) {
		case Token.NAME:
			next();
			return new Ast.Name(t.line, t.col, t.text);
		case Token.NUMBER:
			next();
			return new Ast.Constant(t.line, t.col, t.value);
		case Token.STRING:
			return strings();
		case Token.KEYWORD:
			switch (t.text) {
			case "None":
				next();
				return new Ast.Constant(t.line, t.col, null);
			case "True":
				next();
				return new Ast.Constant(t.line, t.col, Boolean.TRUE);
			case "False":
				next();
				return new Ast.Constant(t.line, t.col, Boolean.FALSE);
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
		case "with":
		case "async":
			return error("'" + t.text + "' is not supported");
		default:
			return invalid();
		}
	}

	private Ast.Expr parenthesized() {
		Token t = next();
		if (acceptOp(")"))
			return new Ast.TupleExpr(t.line, t.col, new ArrayList<Ast.Expr>());
		if (isOp("*"))
			throw error("starred expressions are not supported");
		Ast.Expr first = test();
		if (isKw("for"))
			throw error("generator expressions are not supported");
		if (isOp(",")) {
			List<Ast.Expr> elts = new ArrayList<Ast.Expr>();
			elts.add(first);
			while (acceptOp(",")) {
				if (isOp(")"))
					break;
				elts.add(test());
			}
			expectOp(")");
			return new Ast.TupleExpr(t.line, t.col, elts);
		}
		if (isOp(":="))
			throw error("the walrus operator is not supported");
		expectOp(")");
		return first;
	}

	private Ast.Expr listDisplay() {
		Token t = next();
		List<Ast.Expr> elts = new ArrayList<Ast.Expr>();
		if (acceptOp("]"))
			return new Ast.ListExpr(t.line, t.col, elts);
		if (isOp("*"))
			throw error("starred expressions are not supported");
		Ast.Expr first = test();
		if (isKw("for")) {
			List<Ast.Comprehension> generators = comprehensions();
			expectOp("]");
			return new Ast.ListComp(t.line, t.col, first, generators);
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
		return new Ast.ListExpr(t.line, t.col, elts);
	}

	private Ast.Expr dictDisplay() {
		Token t = next();
		List<Ast.Expr> keys = new ArrayList<Ast.Expr>();
		List<Ast.Expr> values = new ArrayList<Ast.Expr>();
		if (acceptOp("}"))
			return new Ast.DictExpr(t.line, t.col, keys, values);
		if (isOp("**"))
			throw error("dictionary unpacking is not supported");
		Ast.Expr key = test();
		if (!isOp(":"))
			throw error("set displays and set comprehensions are not supported");
		next();
		Ast.Expr value = test();
		if (isKw("for")) {
			List<Ast.Comprehension> generators = comprehensions();
			expectOp("}");
			return new Ast.DictComp(t.line, t.col, key, value, generators);
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
		return new Ast.DictExpr(t.line, t.col, keys, values);
	}

	private List<Ast.Comprehension> comprehensions() {
		List<Ast.Comprehension> generators = new ArrayList<Ast.Comprehension>();
		while (isKw("for")) {
			Token t = next();
			Ast.Expr target = targetList();
			setContext(target, Ast.Ctx.Store);
			expectKw("in");
			Ast.Expr iter = orTest();
			List<Ast.Expr> ifs = new ArrayList<Ast.Expr>();
			while (isKw("if")) {
				next();
				ifs.add(orTest());
			}
			generators.add(new Ast.Comprehension(t.line, t.col, target, iter, ifs));
		}
		if (isKw("async"))
			throw error("'async' is not supported");
		return generators;
	}

	// ------------------------------------------------------------ strings

	private Ast.Expr strings() {
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
			return new Ast.Constant(first.line, first.col, merged.isEmpty() ? "" : (String) merged.get(0));
		List<Ast.Expr> values = new ArrayList<Ast.Expr>();
		for (Object part : merged)
			values.add(part instanceof String ? new Ast.Constant(first.line, first.col, part) : (Ast.Expr) part);
		return new Ast.JoinedStr(first.line, first.col, values);
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
		Ast.Expr value = subExpression(exprText, t);
		long conversion = -1;
		Ast.Expr spec = null;
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
			List<Ast.Expr> specValues = new ArrayList<Ast.Expr>();
			for (Object part : specParts)
				specValues.add(part instanceof String ? new Ast.Constant(t.line, t.col, part) : (Ast.Expr) part);
			spec = new Ast.JoinedStr(t.line, t.col, specValues);
		}
		if (i >= len || body.charAt(i) != '}')
			throw new PySyntaxError("f-string: expecting '}'", t.line, t.col);
		out.add(new Ast.FormattedValue(t.line, t.col, value, conversion, spec));
		return i + 1;
	}

	private Ast.Expr subExpression(String text, Token t) {
		PyParser sub;
		try {
			sub = new PyParser(PyLexer.tokenize("(" + text + "\n)"));
		} catch (PySyntaxError e) {
			throw new PySyntaxError("f-string: " + e.getMessage(), t.line, t.col);
		}
		Ast.Expr e = sub.testListInParens();
		return e;
	}

	/** Used for f-string fields: the tokens are '(' testlist NEWLINE? ')' END. */
	private Ast.Expr testListInParens() {
		expectOp("(");
		Ast.Expr e = testList();
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
