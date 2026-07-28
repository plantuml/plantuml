package net.sourceforge.plantuml.cheneer.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.cheneer.ChenEerDiagram;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.decoration.LinkMiddleDecor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.BlocLines;

public class CommandMultiSubclassTest {

	private final Command<ChenEerDiagram> command = new CommandMultiSubclass();

	private final ChenEerDiagram diagram = new ChenEerDiagram(UmlSource.create(new ArrayList<>(), false), null, new PreprocessingArtifact());

	@Test
	void test_parse() {
		IRegex regex = CommandMultiSubclass.getRegexConcat();
		RegexResult matcher = regex.matcher("E1 ->- d { E2, E3, E4 }");

		assertNotNull(matcher);
		assertEquals("E1", matcher.get("SUPERCLASS", 0));
		assertEquals(" E2, E3, E4 ", matcher.get("SUBCLASSES", 0));
		assertEquals("-", matcher.get("PARTICIPATION", 0));
		assertEquals("d", matcher.get("SYMBOL", 0));
	}

	@Test
	void test_parseWithCompulsoryParticipation() {
		IRegex regex = CommandMultiSubclass.getRegexConcat();
		RegexResult matcher = regex.matcher("E1 =>= d { E2, E3, E4 }");

		assertNotNull(matcher);
		assertEquals("E1", matcher.get("SUPERCLASS", 0));
		assertEquals(" E2, E3, E4 ", matcher.get("SUBCLASSES", 0));
		assertEquals("=", matcher.get("PARTICIPATION", 0));
		assertEquals("d", matcher.get("SYMBOL", 0));
	}

	@Test
	void test_parseOverlapping() {
		IRegex regex = CommandMultiSubclass.getRegexConcat();
		RegexResult matcher = regex.matcher("E1 ->- o { E2, E3, E4 }");

		assertNotNull(matcher);
		assertEquals("E1", matcher.get("SUPERCLASS", 0));
		assertEquals(" E2, E3, E4 ", matcher.get("SUBCLASSES", 0));
		assertEquals("-", matcher.get("PARTICIPATION", 0));
		assertEquals("o", matcher.get("SYMBOL", 0));
	}

	@Test
	void test_parseCategory() {
		IRegex regex = CommandMultiSubclass.getRegexConcat();
		RegexResult matcher = regex.matcher("E1 ->- U { E2, E3, E4 }");

		assertNotNull(matcher);
		assertEquals("E1", matcher.get("SUPERCLASS", 0));
		assertEquals(" E2, E3, E4 ", matcher.get("SUBCLASSES", 0));
		assertEquals("-", matcher.get("PARTICIPATION", 0));
		assertEquals("U", matcher.get("SYMBOL", 0));
	}

	@Test
	void test_executeOverlappingSubclasses() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E1 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E2 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E3 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E4 {"), ParserPass.ONE);

		BlocLines lines = BlocLines.singleString("E1 ->- o { E2, E3, E4 }");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertTrue(result.isOk());

		assertEquals(4, diagram.getLinks().size());
		Link link1 = diagram.getLinks().get(0);
		assertEquals("E1", link1.getEntity1().getName());
		assertEquals("E1/o E2, E3, E4 /center", link1.getEntity2().getName());
		assertEquals(1, link1.getType().getStyle().getStroke3().getThickness());
		assertEquals(LinkMiddleDecor.NONE, link1.getType().getMiddleDecor());
		Link link2 = diagram.getLinks().get(1);
		assertEquals("E1/o E2, E3, E4 /center", link2.getEntity1().getName());
		assertEquals("E2", link2.getEntity2().getName());
		assertEquals(1, link2.getType().getStyle().getStroke3().getThickness());
		assertEquals(LinkMiddleDecor.SUPERSET, link2.getType().getMiddleDecor());
		Link link3 = diagram.getLinks().get(2);
		assertEquals("E1/o E2, E3, E4 /center", link3.getEntity1().getName());
		assertEquals("E3", link3.getEntity2().getName());
		assertEquals(1, link3.getType().getStyle().getStroke3().getThickness());
		assertEquals(LinkMiddleDecor.SUPERSET, link3.getType().getMiddleDecor());
		Link link4 = diagram.getLinks().get(3);
		assertEquals("E1/o E2, E3, E4 /center", link4.getEntity1().getName());
		assertEquals("E4", link4.getEntity2().getName());
		assertEquals(1, link4.getType().getStyle().getStroke3().getThickness());
		assertEquals(LinkMiddleDecor.SUPERSET, link4.getType().getMiddleDecor());
	}

	@Test
	void test_executeOverlappingSubclassesWithCompulsaryParticipation() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E1 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E2 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E3 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E4 {"), ParserPass.ONE);

		BlocLines lines = BlocLines.singleString("E1 =>= o { E2, E3, E4 }");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertTrue(result.isOk());

		assertEquals(4, diagram.getLinks().size());
		Link link1 = diagram.getLinks().get(0);
		assertEquals("E1", link1.getEntity1().getName());
		assertEquals("E1/o E2, E3, E4 /center", link1.getEntity2().getName());
		assertEquals(2, link1.getType().getStyle().getStroke3().getThickness());
		assertEquals(LinkMiddleDecor.NONE, link1.getType().getMiddleDecor());
		Link link2 = diagram.getLinks().get(1);
		assertEquals("E1/o E2, E3, E4 /center", link2.getEntity1().getName());
		assertEquals("E2", link2.getEntity2().getName());
		assertEquals(1, link2.getType().getStyle().getStroke3().getThickness());
		assertEquals(LinkMiddleDecor.SUPERSET, link2.getType().getMiddleDecor());
		Link link3 = diagram.getLinks().get(2);
		assertEquals("E1/o E2, E3, E4 /center", link3.getEntity1().getName());
		assertEquals("E3", link3.getEntity2().getName());
		assertEquals(1, link3.getType().getStyle().getStroke3().getThickness());
		assertEquals(LinkMiddleDecor.SUPERSET, link3.getType().getMiddleDecor());
		Link link4 = diagram.getLinks().get(3);
		assertEquals("E1/o E2, E3, E4 /center", link4.getEntity1().getName());
		assertEquals("E4", link4.getEntity2().getName());
		assertEquals(1, link4.getType().getStyle().getStroke3().getThickness());
		assertEquals(LinkMiddleDecor.SUPERSET, link4.getType().getMiddleDecor());
	}

	@Test
	void test_executeCategories() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E1 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E2 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E3 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E4 {"), ParserPass.ONE);

		BlocLines lines = BlocLines.singleString("E1 ->- U { E2, E3, E4 }");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertTrue(result.isOk());

		assertEquals(4, diagram.getLinks().size());
		Link link1 = diagram.getLinks().get(0);
		assertEquals("E1", link1.getEntity1().getName());
		assertEquals("E1/U E2, E3, E4 /center", link1.getEntity2().getName());
		assertEquals(1, link1.getType().getStyle().getStroke3().getThickness());
		assertEquals(LinkMiddleDecor.SUPERSET, link1.getType().getMiddleDecor());
		Link link2 = diagram.getLinks().get(1);
		assertEquals("E1/U E2, E3, E4 /center", link2.getEntity1().getName());
		assertEquals("E2", link2.getEntity2().getName());
		assertEquals(1, link2.getType().getStyle().getStroke3().getThickness());
		assertEquals(LinkMiddleDecor.NONE, link2.getType().getMiddleDecor());
		Link link3 = diagram.getLinks().get(2);
		assertEquals("E1/U E2, E3, E4 /center", link3.getEntity1().getName());
		assertEquals("E3", link3.getEntity2().getName());
		assertEquals(1, link3.getType().getStyle().getStroke3().getThickness());
		assertEquals(LinkMiddleDecor.NONE, link3.getType().getMiddleDecor());
		Link link4 = diagram.getLinks().get(3);
		assertEquals("E1/U E2, E3, E4 /center", link4.getEntity1().getName());
		assertEquals("E4", link4.getEntity2().getName());
		assertEquals(1, link4.getType().getStyle().getStroke3().getThickness());
		assertEquals(LinkMiddleDecor.NONE, link4.getType().getMiddleDecor());
	}

	@Test
	void test_executeCategoriesWithCompulsaryParticipation() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E1 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E2 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E3 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E4 {"), ParserPass.ONE);

		BlocLines lines = BlocLines.singleString("E1 =>= U { E2, E3, E4 }");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertTrue(result.isOk());

		assertEquals(4, diagram.getLinks().size());
		Link link1 = diagram.getLinks().get(0);
		assertEquals("E1", link1.getEntity1().getName());
		assertEquals("E1/U E2, E3, E4 /center", link1.getEntity2().getName());
		assertEquals(2, link1.getType().getStyle().getStroke3().getThickness());
		assertEquals(LinkMiddleDecor.SUPERSET, link1.getType().getMiddleDecor());
		Link link2 = diagram.getLinks().get(1);
		assertEquals("E1/U E2, E3, E4 /center", link2.getEntity1().getName());
		assertEquals("E2", link2.getEntity2().getName());
		assertEquals(1, link2.getType().getStyle().getStroke3().getThickness());
		assertEquals(LinkMiddleDecor.NONE, link2.getType().getMiddleDecor());
		Link link3 = diagram.getLinks().get(2);
		assertEquals("E1/U E2, E3, E4 /center", link3.getEntity1().getName());
		assertEquals("E3", link3.getEntity2().getName());
		assertEquals(1, link3.getType().getStyle().getStroke3().getThickness());
		assertEquals(LinkMiddleDecor.NONE, link3.getType().getMiddleDecor());
		Link link4 = diagram.getLinks().get(3);
		assertEquals("E1/U E2, E3, E4 /center", link4.getEntity1().getName());
		assertEquals("E4", link4.getEntity2().getName());
		assertEquals(1, link4.getType().getStyle().getStroke3().getThickness());
		assertEquals(LinkMiddleDecor.NONE, link4.getType().getMiddleDecor());
	}
}
