package net.sourceforge.plantuml.cheneer.command;

import static org.assertj.core.api.Assertions.assertThat;

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

public class CommandSimpleSubclassTest {

	private final Command<ChenEerDiagram> command = new CommandSimpleSubclass();

	private final ChenEerDiagram diagram = new ChenEerDiagram(UmlSource.create(new ArrayList<>(), false), null, new PreprocessingArtifact());

	@Test
	void test_parseSubset() {
		IRegex regex = CommandSimpleSubclass.getRegexConcat();
		RegexResult matcher = regex.matcher("E1 -<- E2");

		assertThat(matcher).isNotNull();
		assertThat(matcher.get("NAME1", 0)).isEqualTo("E1");
		assertThat(matcher.get("NAME2", 0)).isEqualTo("E2");
		assertThat(matcher.get("PARTICIPATION", 0)).isEqualTo("-");
		assertThat(matcher.get("DIRECTION", 0)).isEqualTo("<");
	}

	@Test
	void test_parseSubsetWithCompulsoryParticipation() {
		IRegex regex = CommandSimpleSubclass.getRegexConcat();
		RegexResult matcher = regex.matcher("E1 =<= E2");

		assertThat(matcher).isNotNull();
		assertThat(matcher.get("NAME1", 0)).isEqualTo("E1");
		assertThat(matcher.get("NAME2", 0)).isEqualTo("E2");
		assertThat(matcher.get("PARTICIPATION", 0)).isEqualTo("=");
		assertThat(matcher.get("DIRECTION", 0)).isEqualTo("<");
	}

	@Test
	void test_parseSuperset() {
		IRegex regex = CommandSimpleSubclass.getRegexConcat();
		RegexResult matcher = regex.matcher("E1 ->- E2");

		assertThat(matcher).isNotNull();
		assertThat(matcher.get("NAME1", 0)).isEqualTo("E1");
		assertThat(matcher.get("NAME2", 0)).isEqualTo("E2");
		assertThat(matcher.get("PARTICIPATION", 0)).isEqualTo("-");
		assertThat(matcher.get("DIRECTION", 0)).isEqualTo(">");
	}

	@Test
	void test_executeSubset() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E1 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E2 {"), ParserPass.ONE);

		BlocLines lines = BlocLines.singleString("E1 -<- E2");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertThat(result).matches(CommandExecutionResult::isOk);

		Link link = diagram.getLinks().get(0);
		assertThat(link.getEntity1().getName()).isEqualTo("E1");
		assertThat(link.getEntity2().getName()).isEqualTo("E2");
		assertThat(link.getType().getStyle().getStroke3().getThickness()).isEqualTo(1);
		assertThat(link.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.SUBSET);
	}

	@Test
	void test_executeSubsetWithCompulsoryParticipation() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E1 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E2 {"), ParserPass.ONE);

		BlocLines lines = BlocLines.singleString("E1 =<= E2");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertThat(result).matches(CommandExecutionResult::isOk);

		Link link = diagram.getLinks().get(0);
		assertThat(link.getEntity1().getName()).isEqualTo("E1");
		assertThat(link.getEntity2().getName()).isEqualTo("E2");
		assertThat(link.getType().getStyle().getStroke3().getThickness()).isEqualTo(2);
		assertThat(link.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.SUBSET);
	}

	@Test
	void test_executeSuperset() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E1 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E2 {"), ParserPass.ONE);

		BlocLines lines = BlocLines.singleString("E1 ->- E2");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertThat(result).matches(CommandExecutionResult::isOk);

		Link link = diagram.getLinks().get(0);
		assertThat(link.getEntity1().getName()).isEqualTo("E1");
		assertThat(link.getEntity2().getName()).isEqualTo("E2");
		assertThat(link.getType().getStyle().getStroke3().getThickness()).isEqualTo(1);
		assertThat(link.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.SUPERSET);
	}
}
