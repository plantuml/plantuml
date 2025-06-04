package net.sourceforge.plantuml.tim.expression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.tim.Eater;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TFunction;
import net.sourceforge.plantuml.tim.TFunctionSignature;
import net.sourceforge.plantuml.tim.TMemory;

class EaterMock extends Eater {

	public EaterMock(StringLocated stringLocated) {
		super(stringLocated);
	}

	@Override
	public void analyze(TContext context, TMemory memory) throws EaterException {
		throw new UnsupportedOperationException();

	}

	public TokenStack eatTokenStackPublic() throws EaterException {
		return eatTokenStack();
	}

}

class KnowledgeEmpty implements Knowledge {

	@Override
	public TValue getVariable(String name) throws EaterException {
		return null;
	}

	@Override
	public TFunction getFunction(TFunctionSignature signature) {
		return null;
	}

}

class ShuntingYardTest {

	@Test
	public void testSimpleAddition() throws Exception {
		// Expression "1 + 2"
		final TokenStack stack = new TokenStack();
		stack.add(new Token("1", TokenType.NUMBER, null));
		stack.add(new Token("+", TokenType.OPERATOR, null));
		stack.add(new Token("2", TokenType.NUMBER, null));

		final ShuntingYard shuntingYard = new ShuntingYard(stack.tokenIterator(), new KnowledgeEmpty(), null);
		final TokenStack queue = shuntingYard.getQueue();

		assertEquals(3, queue.size());
		assertEquals("[NUMBER{1}, NUMBER{2}, OPERATOR{+}]", queue.toString());
		final TokenIterator it = queue.tokenIterator();
		assertEquals("1", it.nextToken().getSurface());
		assertEquals("2", it.nextToken().getSurface());
		assertEquals("+", it.nextToken().getSurface());
	}

	@Test
	public void testSimpleAddition2() throws Exception {

		final EaterMock eater = new EaterMock(new StringLocated("1+2", null));
		final TokenStack stack = eater.eatTokenStackPublic();
		assertEquals("[NUMBER{1}, OPERATOR{+}, NUMBER{2}]", stack.toString());

		final ShuntingYard shuntingYard = new ShuntingYard(stack.tokenIterator(), new KnowledgeEmpty(), null);
		final TokenStack queue = shuntingYard.getQueue();

		assertEquals("[NUMBER{1}, NUMBER{2}, OPERATOR{+}]", queue.toString());

	}

	@Test
	public void testFunction1() throws Exception {

		final EaterMock eater = new EaterMock(new StringLocated("$foo(1,2)", null));
		final TokenStack stack = eater.eatTokenStackPublic();
		assertEquals("[PLAIN_TEXT{$foo}, OPEN_PAREN_MATH{(}, NUMBER{1}, COMMA{,}, NUMBER{2}, CLOSE_PAREN_MATH{)}]",
				stack.toString());
		stack.guessFunctions(null);

		final ShuntingYard shuntingYard = new ShuntingYard(stack.tokenIterator(), new KnowledgeEmpty(), null);
		final TokenStack queue = shuntingYard.getQueue();

		assertEquals("[NUMBER{1}, NUMBER{2}, OPEN_PAREN_FUNC{2}, FUNCTION_NAME{$foo}]", queue.toString());

	}

	@Test
	public void testFunction2() throws Exception {

		final EaterMock eater = new EaterMock(new StringLocated("$foo(1+1,2)", null));
		final TokenStack stack = eater.eatTokenStackPublic();
		assertEquals(
				"[PLAIN_TEXT{$foo}, OPEN_PAREN_MATH{(}, NUMBER{1}, OPERATOR{+}, NUMBER{1}, COMMA{,}, NUMBER{2}, CLOSE_PAREN_MATH{)}]",
				stack.toString());
		stack.guessFunctions(null);

		final ShuntingYard shuntingYard = new ShuntingYard(stack.tokenIterator(), new KnowledgeEmpty(), null);
		final TokenStack queue = shuntingYard.getQueue();

		assertEquals("[NUMBER{1}, NUMBER{1}, OPERATOR{+}, NUMBER{2}, OPEN_PAREN_FUNC{2}, FUNCTION_NAME{$foo}]",
				queue.toString());

	}

	@Test
	public void testFunction3() throws Exception {

		final EaterMock eater = new EaterMock(new StringLocated("$foo(1 + 1, 2)", null));
		TokenStack stack = eater.eatTokenStackPublic();
		assertEquals(
				"[PLAIN_TEXT{$foo}, OPEN_PAREN_MATH{(}, NUMBER{1}, SPACES{ }, OPERATOR{+}, SPACES{ }, NUMBER{1}, COMMA{,}, SPACES{ }, NUMBER{2}, CLOSE_PAREN_MATH{)}]",
				stack.toString());
		stack = stack.withoutSpace();
		stack.guessFunctions(null);

		final ShuntingYard shuntingYard = new ShuntingYard(stack.tokenIterator(), new KnowledgeEmpty(), null);
		final TokenStack queue = shuntingYard.getQueue();

		assertEquals("[NUMBER{1}, NUMBER{1}, OPERATOR{+}, NUMBER{2}, OPEN_PAREN_FUNC{2}, FUNCTION_NAME{$foo}]",
				queue.toString());

	}

	@Test
	public void testFunction4() throws Exception {

		final EaterMock eater = new EaterMock(new StringLocated("$foo(1 + 1, $named = 2)", null));
		TokenStack stack = eater.eatTokenStackPublic();
		stack = stack.withoutSpace();
		assertEquals(
				"[PLAIN_TEXT{$foo}, OPEN_PAREN_MATH{(}, NUMBER{1}, OPERATOR{+}, NUMBER{1}, COMMA{,}, PLAIN_TEXT{$named}, AFFECTATION{=}, NUMBER{2}, CLOSE_PAREN_MATH{)}]",
				stack.toString());
		stack.guessFunctions(null);

		final ShuntingYard shuntingYard = new ShuntingYard(stack.tokenIterator(), new KnowledgeEmpty(), null);
		final TokenStack queue = shuntingYard.getQueue();

		assertEquals(
				"[NUMBER{1}, NUMBER{1}, OPERATOR{+}, QUOTED_STRING{$named}, NUMBER{2}, AFFECTATION{=}, OPEN_PAREN_FUNC{2}, FUNCTION_NAME{$foo}]",
				queue.toString());

	}

	@Test
	public void testFunction5() throws Exception {

		final EaterMock eater = new EaterMock(new StringLocated("$foo(1+1,$named=2+2)", null));
		TokenStack stack = eater.eatTokenStackPublic();
		stack = stack.withoutSpace();
		assertEquals(
				"[PLAIN_TEXT{$foo}, OPEN_PAREN_MATH{(}, NUMBER{1}, OPERATOR{+}, NUMBER{1}, COMMA{,}, PLAIN_TEXT{$named}, AFFECTATION{=}, NUMBER{2}, OPERATOR{+}, NUMBER{2}, CLOSE_PAREN_MATH{)}]",
				stack.toString());
		stack.guessFunctions(null);

		final ShuntingYard shuntingYard = new ShuntingYard(stack.tokenIterator(), new KnowledgeEmpty(), null);
		final TokenStack queue = shuntingYard.getQueue();

		assertEquals(
				"[NUMBER{1}, NUMBER{1}, OPERATOR{+}, QUOTED_STRING{$named}, NUMBER{2}, NUMBER{2}, OPERATOR{+}, AFFECTATION{=}, OPEN_PAREN_FUNC{2}, FUNCTION_NAME{$foo}]",
				queue.toString());

	}

}
