package net.sourceforge.plantuml.cheneer.command;

import static org.assertj.core.api.Assertions.assertThat;

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

		assertThat(matcher).isNotNull();
		assertThat(matcher.get("TYPE", 0)).isEqualTo("entity");
		assertThat(matcher.get("DISPLAY", 0)).isNull();
		assertThat(matcher.get("CODE", 0)).isEqualTo("Bus_123");
		assertThat(matcher.get("STEREO", 0)).isNull();
	}

	@Test
	void test_parseRelationship() {
		IRegex regex = CommandCreateEntity.getRegexConcat();
		RegexResult matcher = regex.matcher("relationship Drives {");

		assertThat(matcher).isNotNull();
		assertThat(matcher.get("TYPE", 0)).isEqualTo("relationship");
		assertThat(matcher.get("DISPLAY", 0)).isNull();
		assertThat(matcher.get("CODE", 0)).isEqualTo("Drives");
		assertThat(matcher.get("STEREO", 0)).isNull();
	}

	@Test
	void test_parseInvalid() {
		IRegex regex = CommandCreateEntity.getRegexConcat();
		RegexResult matcher = regex.matcher("class MyClass {");

		assertThat(matcher).isNull();
	}

	@Test
	void test_parseEntityWithStereo() {
		IRegex regex = CommandCreateEntity.getRegexConcat();
		RegexResult matcher = regex.matcher("entity Bus_123 <<red>> {");

		assertThat(matcher).isNotNull();
		assertThat(matcher.get("TYPE", 0)).isEqualTo("entity");
		assertThat(matcher.get("DISPLAY", 0)).isNull();
		assertThat(matcher.get("CODE", 0)).isEqualTo("Bus_123");
		assertThat(matcher.get("STEREO", 0)).isEqualTo("<<red>>");
	}

	@Test
	void test_parseEntityWithDisplayName() {
		IRegex regex = CommandCreateEntity.getRegexConcat();
		RegexResult matcher = regex.matcher("entity \"Red Bus :)\" as Bus_123 <<red>> {");

		assertThat(matcher).isNotNull();
		assertThat(matcher.get("TYPE", 0)).isEqualTo("entity");
		assertThat(matcher.get("DISPLAY", 0)).isEqualTo("Red Bus :)");
		assertThat(matcher.get("CODE", 0)).isEqualTo("Bus_123");
		assertThat(matcher.get("STEREO", 0)).isEqualTo("<<red>>");
	}

	@Test
	void test_executeWithEntity() throws NoSuchColorException {
		BlocLines lines = BlocLines.singleString("entity \"display\" as code <<stereo>> {");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertThat(result).matches(CommandExecutionResult::isOk);

		Entity entity = diagram.quarkInContext(true, "code").getData();
		assertThat(entity).isNotNull();
		assertThat(entity.getLeafType()).isEqualTo(LeafType.CHEN_ENTITY);
		assertThat(entity.getDisplay().toString()).isEqualTo("[display]");
		assertThat(entity.getStereotype().toString()).isEqualTo("<<stereo>>");

		assertThat(diagram.peekOwner()).isSameAs(entity);
	}

	@Test
	void test_executeWithRelationship() throws NoSuchColorException {
		BlocLines lines = BlocLines.singleString("relationship \"display\" as code <<stereo>> {");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertThat(result).matches(CommandExecutionResult::isOk);

		Entity entity = diagram.quarkInContext(true, "code").getData();
		assertThat(entity).isNotNull();
		assertThat(entity.getLeafType()).isEqualTo(LeafType.CHEN_RELATIONSHIP);
		assertThat(entity.getDisplay().toString()).isEqualTo("[display]");
		assertThat(entity.getStereotype().toString()).isEqualTo("<<stereo>>");

		assertThat(diagram.peekOwner()).isSameAs(entity);
	}

	@Test
	void test_executeWithEntityWhenAlreadyExists() throws NoSuchColorException {
		BlocLines lines = BlocLines.singleString("entity \"display\" as code <<stereo>> {");
		command.execute(diagram, lines, ParserPass.ONE);

		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertThat(result).matches(CommandExecutionResult::isOk);
	}
}
