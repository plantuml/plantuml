package net.sourceforge.plantuml.cheneer.command;

import static org.assertj.core.api.Assertions.assertThat;

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

		assertThat(matcher).isNotNull();
		assertThat(matcher.get("DISPLAY", 0)).isNull();
		assertThat(matcher.get("CODE", 0)).isEqualTo("attr");
		assertThat(matcher.get("STEREO", 0)).isNull();
		assertThat(matcher.get("COMPOSITE", 0)).isNull();
	}

	@Test
	void test_parseWithType() {
		IRegex regex = CommandCreateAttribute.getRegexConcat();
		RegexResult matcher = regex.matcher("attr : String");

		assertThat(matcher).isNotNull();
		assertThat(matcher.get("DISPLAY", 0)).isNull();
		assertThat(matcher.get("CODE", 0)).isEqualTo("attr : String");
		assertThat(matcher.get("STEREO", 0)).isNull();
		assertThat(matcher.get("COMPOSITE", 0)).isNull();
	}

	@Test
	void test_parseComposite() {
		IRegex regex = CommandCreateAttribute.getRegexConcat();
		RegexResult matcher = regex.matcher("attr{");

		assertThat(matcher).isNotNull();
		assertThat(matcher.get("DISPLAY", 0)).isNull();
		assertThat(matcher.get("CODE", 0)).isEqualTo("attr");
		assertThat(matcher.get("STEREO", 0)).isNull();
		assertThat(matcher.get("COMPOSITE", 0)).isNotNull();
	}

	@Test
	void test_parseWithDisplay() {
		IRegex regex = CommandCreateAttribute.getRegexConcat();
		RegexResult matcher = regex.matcher("\"My Attribute\" as attr");

		assertThat(matcher).isNotNull();
		assertThat(matcher.get("DISPLAY", 0)).isEqualTo("My Attribute");
		assertThat(matcher.get("CODE", 0)).isEqualTo("attr");
		assertThat(matcher.get("STEREO", 0)).isNull();
		assertThat(matcher.get("COMPOSITE", 0)).isNull();
	}

	@Test
	void test_parseWithStereo() {
		IRegex regex = CommandCreateAttribute.getRegexConcat();
		RegexResult matcher = regex.matcher("attr<<red>>");

		assertThat(matcher).isNotNull();
		assertThat(matcher.get("DISPLAY", 0)).isNull();
		assertThat(matcher.get("CODE", 0)).isEqualTo("attr");
		assertThat(matcher.get("STEREO", 0)).isEqualTo("<<red>>");
		assertThat(matcher.get("COMPOSITE", 0)).isNull();
	}

	@Test
	void test_execute() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E {"), ParserPass.ONE);

		BlocLines lines = BlocLines.singleString("\"Attribute\" as attr<<stereo>>");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertThat(result).matches(CommandExecutionResult::isOk);

		Entity entity = diagram.quarkInContext(true, "E").getData();
		assertThat(entity).isNotNull();

		Entity attribute = diagram.quarkInContext(true, "E/attr").getData();
		assertThat(attribute).isNotNull();
		assertThat(attribute.getDisplay().toString()).isEqualTo("[Attribute]");
		assertThat(attribute.getStereotype().toString()).isEqualTo("<<stereo>>");

		assertThat(diagram.getLinks().size()).isEqualTo(1);
		Link link = diagram.getLinks().get(0);
		assertThat(link).isNotNull();
		assertThat(link.getEntity1()).isSameAs(attribute);
		assertThat(link.getEntity2()).isSameAs(entity);
	}

	@Test
	void test_executeNested() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E {"), ParserPass.ONE);

		CommandExecutionResult result1 = command.execute(diagram, BlocLines.singleString("attr1{"), ParserPass.ONE);
		CommandExecutionResult result2 = command.execute(diagram, BlocLines.singleString("attr2"), ParserPass.ONE);

		assertThat(result1).matches(CommandExecutionResult::isOk);
		assertThat(result2).matches(CommandExecutionResult::isOk);

		Entity entity = diagram.quarkInContext(true, "E").getData();
		assertThat(entity).isNotNull();

		Entity attribute1 = diagram.quarkInContext(true, "E/attr1").getData();
		assertThat(attribute1).isNotNull();
		assertThat(attribute1.getDisplay().toString()).isEqualTo("[attr1]");

		Entity attribute2 = diagram.quarkInContext(true, "E/attr1/attr2").getData();
		assertThat(attribute2).isNotNull();
		assertThat(attribute2.getDisplay().toString()).isEqualTo("[attr2]");

		assertThat(diagram.getLinks().size()).isEqualTo(2);
		Link link1 = diagram.getLinks().get(0);
		assertThat(link1).isNotNull();
		assertThat(link1.getEntity1()).isSameAs(attribute1);
		assertThat(link1.getEntity2()).isSameAs(entity);
		Link link2 = diagram.getLinks().get(1);
		assertThat(link2).isNotNull();
		assertThat(link2.getEntity1()).isSameAs(attribute2);
		assertThat(link2.getEntity2()).isSameAs(attribute1);
	}

	@Test
	void test_executeNonNested() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E {"), ParserPass.ONE);

		CommandExecutionResult result1 = command.execute(diagram, BlocLines.singleString("attr1"), ParserPass.ONE);
		CommandExecutionResult result2 = command.execute(diagram, BlocLines.singleString("attr2"), ParserPass.ONE);

		assertThat(result1).matches(CommandExecutionResult::isOk);
		assertThat(result2).matches(CommandExecutionResult::isOk);

		Entity entity = diagram.quarkInContext(true, "E").getData();
		assertThat(entity).isNotNull();

		Entity attribute1 = diagram.quarkInContext(true, "E/attr1").getData();
		assertThat(attribute1).isNotNull();
		assertThat(attribute1.getDisplay().toString()).isEqualTo("[attr1]");

		Entity attribute2 = diagram.quarkInContext(true, "E/attr2").getData();
		assertThat(attribute2).isNotNull();
		assertThat(attribute2.getDisplay().toString()).isEqualTo("[attr2]");

		assertThat(diagram.getLinks().size()).isEqualTo(2);
		Link link1 = diagram.getLinks().get(0);
		assertThat(link1).isNotNull();
		assertThat(link1.getEntity1()).isSameAs(attribute1);
		assertThat(link1.getEntity2()).isSameAs(entity);
		Link link2 = diagram.getLinks().get(1);
		assertThat(link2).isNotNull();
		assertThat(link2.getEntity1()).isSameAs(attribute2);
		assertThat(link2.getEntity2()).isSameAs(entity);
	}
}
