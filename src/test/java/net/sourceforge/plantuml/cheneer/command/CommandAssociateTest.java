package net.sourceforge.plantuml.cheneer.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.cheneer.ChenEerDiagram;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.BlocLines;

public class CommandAssociateTest {

	private final Command<ChenEerDiagram> command = new CommandAssociate();

	private final ChenEerDiagram diagram = new ChenEerDiagram(UmlSource.create(new ArrayList<>(), false), null, new PreprocessingArtifact());

	@Test
	void test_parse() {
		IRegex regex = CommandAssociate.getRegexConcat();
		RegexResult matcher = regex.matcher("E1 -- E2");

		assertNotNull(matcher);
		assertEquals("E1", matcher.get("NAME1", 0));
		assertEquals("E2", matcher.get("NAME2", 0));
		assertEquals("-", matcher.get("PARTICIPATION", 0));
		assertNull(matcher.get("CARDINALITY", 0));
	}

	@Test
	void test_parseWithCompulsoryParticipation() {
		IRegex regex = CommandAssociate.getRegexConcat();
		RegexResult matcher = regex.matcher("E1 == E2");

		assertNotNull(matcher);
		assertEquals("E1", matcher.get("NAME1", 0));
		assertEquals("E2", matcher.get("NAME2", 0));
		assertEquals("=", matcher.get("PARTICIPATION", 0));
		assertNull(matcher.get("CARDINALITY", 0));
	}

	@Test
	void test_parseWithCardinality() {
		IRegex regex = CommandAssociate.getRegexConcat();
		RegexResult matcher = regex.matcher("E1 -N- E2");

		assertNotNull(matcher);
		assertEquals("E1", matcher.get("NAME1", 0));
		assertEquals("E2", matcher.get("NAME2", 0));
		assertEquals("-", matcher.get("PARTICIPATION", 0));
		assertEquals("N", matcher.get("CARDINALITY", 0));
	}

	@Test
	void test_parseCardinalityRange() {
		IRegex regex = CommandAssociate.getRegexConcat();
		RegexResult matcher = regex.matcher("E1 -(1, n)- E2");

		assertNotNull(matcher);
		assertEquals("E1", matcher.get("NAME1", 0));
		assertEquals("E2", matcher.get("NAME2", 0));
		assertEquals("-", matcher.get("PARTICIPATION", 0));
		assertEquals("(1, n)", matcher.get("CARDINALITY", 0));
	}

	@Test
	void test_execute() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E1 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E2 {"), ParserPass.ONE);

		BlocLines lines = BlocLines.singleString("E1 -- E2");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertTrue(result.isOk());

		assertEquals(1, diagram.getLinks().size());
		Link link = diagram.getLinks().get(0);
		assertEquals("E1", link.getEntity1().getName());
		assertEquals("E2", link.getEntity2().getName());
		assertEquals(1, link.getType().getStyle().getStroke3().getThickness());
		assertEquals("NULL", link.getLabel().toString());
	}

	@Test
	void test_executeWithCompulsoryParticipation() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E1 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E2 {"), ParserPass.ONE);

		BlocLines lines = BlocLines.singleString("E1 == E2");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertTrue(result.isOk());

		assertEquals(1, diagram.getLinks().size());
		Link link = diagram.getLinks().get(0);
		assertEquals("E1", link.getEntity1().getName());
		assertEquals("E2", link.getEntity2().getName());
		assertEquals(2, link.getType().getStyle().getStroke3().getThickness());
		assertEquals("NULL", link.getLabel().toString());
	}

	@Test
	void test_executeWithCardinality() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E1 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E2 {"), ParserPass.ONE);

		BlocLines lines = BlocLines.singleString("E1 -N- E2");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertTrue(result.isOk());

		assertEquals(1, diagram.getLinks().size());
		Link link = diagram.getLinks().get(0);
		assertEquals("E1", link.getEntity1().getName());
		assertEquals("E2", link.getEntity2().getName());
		assertEquals(1, link.getType().getStyle().getStroke3().getThickness());
		assertEquals("[N]", link.getLabel().toString());
	}
}
