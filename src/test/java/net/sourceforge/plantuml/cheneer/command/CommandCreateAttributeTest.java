package net.sourceforge.plantuml.cheneer.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.abel.Entity;
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

public class CommandCreateAttributeTest {

	private final Command<ChenEerDiagram> command = new CommandCreateAttribute();

	private final ChenEerDiagram diagram = new ChenEerDiagram(UmlSource.create(new ArrayList<>(), false), null, new PreprocessingArtifact());

	@Test
	void test_parse() {
		IRegex regex = CommandCreateAttribute.getRegexConcat();
		RegexResult matcher = regex.matcher("attr");

		assertNotNull(matcher);
		assertNull(matcher.get("DISPLAY", 0));
		assertEquals("attr", matcher.get("CODE", 0));
		assertNull(matcher.get("STEREO", 0));
		assertNull(matcher.get("COMPOSITE", 0));
	}

	@Test
	void test_parseWithType() {
		IRegex regex = CommandCreateAttribute.getRegexConcat();
		RegexResult matcher = regex.matcher("attr : String");

		assertNotNull(matcher);
		assertNull(matcher.get("DISPLAY", 0));
		assertEquals("attr : String", matcher.get("CODE", 0));
		assertNull(matcher.get("STEREO", 0));
		assertNull(matcher.get("COMPOSITE", 0));
	}

	@Test
	void test_parseComposite() {
		IRegex regex = CommandCreateAttribute.getRegexConcat();
		RegexResult matcher = regex.matcher("attr{");

		assertNotNull(matcher);
		assertNull(matcher.get("DISPLAY", 0));
		assertEquals("attr", matcher.get("CODE", 0));
		assertNull(matcher.get("STEREO", 0));
		assertNotNull(matcher.get("COMPOSITE", 0));
	}

	@Test
	void test_parseWithDisplay() {
		IRegex regex = CommandCreateAttribute.getRegexConcat();
		RegexResult matcher = regex.matcher("\"My Attribute\" as attr");

		assertNotNull(matcher);
		assertEquals("My Attribute", matcher.get("DISPLAY", 0));
		assertEquals("attr", matcher.get("CODE", 0));
		assertNull(matcher.get("STEREO", 0));
		assertNull(matcher.get("COMPOSITE", 0));
	}

	@Test
	void test_parseWithStereo() {
		IRegex regex = CommandCreateAttribute.getRegexConcat();
		RegexResult matcher = regex.matcher("attr<<red>>");

		assertNotNull(matcher);
		assertNull(matcher.get("DISPLAY", 0));
		assertEquals("attr", matcher.get("CODE", 0));
		assertEquals("<<red>>", matcher.get("STEREO", 0));
		assertNull(matcher.get("COMPOSITE", 0));
	}

	@Test
	void test_execute() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E {"), ParserPass.ONE);

		BlocLines lines = BlocLines.singleString("\"Attribute\" as attr<<stereo>>");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertTrue(result.isOk());

		Entity entity = diagram.quarkInContext(true, "E").getData();
		assertNotNull(entity);

		Entity attribute = diagram.quarkInContext(true, "E/attr").getData();
		assertNotNull(attribute);
		assertEquals("[Attribute]", attribute.getDisplay().toString());
		assertEquals("<<stereo>>", attribute.getStereotype().toString());

		assertEquals(1, diagram.getLinks().size());
		Link link = diagram.getLinks().get(0);
		assertNotNull(link);
		assertSame(attribute, link.getEntity1());
		assertSame(entity, link.getEntity2());
	}

	@Test
	void test_executeNested() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E {"), ParserPass.ONE);

		CommandExecutionResult result1 = command.execute(diagram, BlocLines.singleString("attr1{"), ParserPass.ONE);
		CommandExecutionResult result2 = command.execute(diagram, BlocLines.singleString("attr2"), ParserPass.ONE);

		assertTrue(result1.isOk());
		assertTrue(result2.isOk());

		Entity entity = diagram.quarkInContext(true, "E").getData();
		assertNotNull(entity);

		Entity attribute1 = diagram.quarkInContext(true, "E/attr1").getData();
		assertNotNull(attribute1);
		assertEquals("[attr1]", attribute1.getDisplay().toString());

		Entity attribute2 = diagram.quarkInContext(true, "E/attr1/attr2").getData();
		assertNotNull(attribute2);
		assertEquals("[attr2]", attribute2.getDisplay().toString());

		assertEquals(2, diagram.getLinks().size());
		Link link1 = diagram.getLinks().get(0);
		assertNotNull(link1);
		assertSame(attribute1, link1.getEntity1());
		assertSame(entity, link1.getEntity2());
		Link link2 = diagram.getLinks().get(1);
		assertNotNull(link2);
		assertSame(attribute2, link2.getEntity1());
		assertSame(attribute1, link2.getEntity2());
	}

	@Test
	void test_executeNonNested() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E {"), ParserPass.ONE);

		CommandExecutionResult result1 = command.execute(diagram, BlocLines.singleString("attr1"), ParserPass.ONE);
		CommandExecutionResult result2 = command.execute(diagram, BlocLines.singleString("attr2"), ParserPass.ONE);

		assertTrue(result1.isOk());
		assertTrue(result2.isOk());

		Entity entity = diagram.quarkInContext(true, "E").getData();
		assertNotNull(entity);

		Entity attribute1 = diagram.quarkInContext(true, "E/attr1").getData();
		assertNotNull(attribute1);
		assertEquals("[attr1]", attribute1.getDisplay().toString());

		Entity attribute2 = diagram.quarkInContext(true, "E/attr2").getData();
		assertNotNull(attribute2);
		assertEquals("[attr2]", attribute2.getDisplay().toString());

		assertEquals(2, diagram.getLinks().size());
		Link link1 = diagram.getLinks().get(0);
		assertNotNull(link1);
		assertSame(attribute1, link1.getEntity1());
		assertSame(entity, link1.getEntity2());
		Link link2 = diagram.getLinks().get(1);
		assertNotNull(link2);
		assertSame(attribute2, link2.getEntity1());
		assertSame(entity, link2.getEntity2());
	}
}
