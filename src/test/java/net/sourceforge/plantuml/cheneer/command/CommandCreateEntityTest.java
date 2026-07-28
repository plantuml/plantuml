package net.sourceforge.plantuml.cheneer.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
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

public class CommandCreateEntityTest {

	private final Command<ChenEerDiagram> command = new CommandCreateEntity();

	private final ChenEerDiagram diagram = new ChenEerDiagram(UmlSource.create(new ArrayList<>(), false), null, new PreprocessingArtifact());

	@Test
	void test_parseEntity() {
		IRegex regex = CommandCreateEntity.getRegexConcat();
		RegexResult matcher = regex.matcher("entity Bus_123 {");

		assertNotNull(matcher);
		assertEquals("entity", matcher.get("TYPE", 0));
		assertNull(matcher.get("DISPLAY", 0));
		assertEquals("Bus_123", matcher.get("CODE", 0));
		assertNull(matcher.get("STEREO", 0));
	}

	@Test
	void test_parseRelationship() {
		IRegex regex = CommandCreateEntity.getRegexConcat();
		RegexResult matcher = regex.matcher("relationship Drives {");

		assertNotNull(matcher);
		assertEquals("relationship", matcher.get("TYPE", 0));
		assertNull(matcher.get("DISPLAY", 0));
		assertEquals("Drives", matcher.get("CODE", 0));
		assertNull(matcher.get("STEREO", 0));
	}

	@Test
	void test_parseInvalid() {
		IRegex regex = CommandCreateEntity.getRegexConcat();
		RegexResult matcher = regex.matcher("class MyClass {");

		assertNull(matcher);
	}

	@Test
	void test_parseEntityWithStereo() {
		IRegex regex = CommandCreateEntity.getRegexConcat();
		RegexResult matcher = regex.matcher("entity Bus_123 <<red>> {");

		assertNotNull(matcher);
		assertEquals("entity", matcher.get("TYPE", 0));
		assertNull(matcher.get("DISPLAY", 0));
		assertEquals("Bus_123", matcher.get("CODE", 0));
		assertEquals("<<red>>", matcher.get("STEREO", 0));
	}

	@Test
	void test_parseEntityWithDisplayName() {
		IRegex regex = CommandCreateEntity.getRegexConcat();
		RegexResult matcher = regex.matcher("entity \"Red Bus :)\" as Bus_123 <<red>> {");

		assertNotNull(matcher);
		assertEquals("entity", matcher.get("TYPE", 0));
		assertEquals("Red Bus :)", matcher.get("DISPLAY", 0));
		assertEquals("Bus_123", matcher.get("CODE", 0));
		assertEquals("<<red>>", matcher.get("STEREO", 0));
	}

	@Test
	void test_executeWithEntity() throws NoSuchColorException {
		BlocLines lines = BlocLines.singleString("entity \"display\" as code <<stereo>> {");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertTrue(result.isOk());

		Entity entity = diagram.quarkInContext(true, "code").getData();
		assertNotNull(entity);
		assertEquals(LeafType.CHEN_ENTITY, entity.getLeafType());
		assertEquals("[display]", entity.getDisplay().toString());
		assertEquals("<<stereo>>", entity.getStereotype().toString());

		assertSame(entity, diagram.peekOwner());
	}

	@Test
	void test_executeWithRelationship() throws NoSuchColorException {
		BlocLines lines = BlocLines.singleString("relationship \"display\" as code <<stereo>> {");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertTrue(result.isOk());

		Entity entity = diagram.quarkInContext(true, "code").getData();
		assertNotNull(entity);
		assertEquals(LeafType.CHEN_RELATIONSHIP, entity.getLeafType());
		assertEquals("[display]", entity.getDisplay().toString());
		assertEquals("<<stereo>>", entity.getStereotype().toString());

		assertSame(entity, diagram.peekOwner());
	}

	@Test
	void test_executeWithEntityWhenAlreadyExists() throws NoSuchColorException {
		BlocLines lines = BlocLines.singleString("entity \"display\" as code <<stereo>> {");
		command.execute(diagram, lines, ParserPass.ONE);

		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertTrue(result.isOk());
	}
}
