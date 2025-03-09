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

public class CommandMultiSubclassTest {

	private final Command<ChenEerDiagram> command = new CommandMultiSubclass();

	private final ChenEerDiagram diagram = new ChenEerDiagram(UmlSource.create(new ArrayList<>(), false), null, new PreprocessingArtifact());

	@Test
	void test_parse() {
		IRegex regex = CommandMultiSubclass.getRegexConcat();
		RegexResult matcher = regex.matcher("E1 ->- d { E2, E3, E4 }");

		assertThat(matcher).isNotNull();
		assertThat(matcher.get("SUPERCLASS", 0)).isEqualTo("E1");
		assertThat(matcher.get("SUBCLASSES", 0)).isEqualTo(" E2, E3, E4 ");
		assertThat(matcher.get("PARTICIPATION", 0)).isEqualTo("-");
		assertThat(matcher.get("SYMBOL", 0)).isEqualTo("d");
	}

	@Test
	void test_parseWithCompulsoryParticipation() {
		IRegex regex = CommandMultiSubclass.getRegexConcat();
		RegexResult matcher = regex.matcher("E1 =>= d { E2, E3, E4 }");

		assertThat(matcher).isNotNull();
		assertThat(matcher.get("SUPERCLASS", 0)).isEqualTo("E1");
		assertThat(matcher.get("SUBCLASSES", 0)).isEqualTo(" E2, E3, E4 ");
		assertThat(matcher.get("PARTICIPATION", 0)).isEqualTo("=");
		assertThat(matcher.get("SYMBOL", 0)).isEqualTo("d");
	}

	@Test
	void test_parseOverlapping() {
		IRegex regex = CommandMultiSubclass.getRegexConcat();
		RegexResult matcher = regex.matcher("E1 ->- o { E2, E3, E4 }");

		assertThat(matcher).isNotNull();
		assertThat(matcher.get("SUPERCLASS", 0)).isEqualTo("E1");
		assertThat(matcher.get("SUBCLASSES", 0)).isEqualTo(" E2, E3, E4 ");
		assertThat(matcher.get("PARTICIPATION", 0)).isEqualTo("-");
		assertThat(matcher.get("SYMBOL", 0)).isEqualTo("o");
	}

	@Test
	void test_parseCategory() {
		IRegex regex = CommandMultiSubclass.getRegexConcat();
		RegexResult matcher = regex.matcher("E1 ->- U { E2, E3, E4 }");

		assertThat(matcher).isNotNull();
		assertThat(matcher.get("SUPERCLASS", 0)).isEqualTo("E1");
		assertThat(matcher.get("SUBCLASSES", 0)).isEqualTo(" E2, E3, E4 ");
		assertThat(matcher.get("PARTICIPATION", 0)).isEqualTo("-");
		assertThat(matcher.get("SYMBOL", 0)).isEqualTo("U");
	}

	@Test
	void test_executeOverlappingSubclasses() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E1 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E2 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E3 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E4 {"), ParserPass.ONE);

		BlocLines lines = BlocLines.singleString("E1 ->- o { E2, E3, E4 }");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertThat(result).matches(CommandExecutionResult::isOk);

		assertThat(diagram.getLinks().size()).isEqualTo(4);
		Link link1 = diagram.getLinks().get(0);
		assertThat(link1.getEntity1().getName()).isEqualTo("E1");
		assertThat(link1.getEntity2().getName()).isEqualTo("E1/o E2, E3, E4 /center");
		assertThat(link1.getType().getStyle().getStroke3().getThickness()).isEqualTo(1);
		assertThat(link1.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.NONE);
		Link link2 = diagram.getLinks().get(1);
		assertThat(link2.getEntity1().getName()).isEqualTo("E1/o E2, E3, E4 /center");
		assertThat(link2.getEntity2().getName()).isEqualTo("E2");
		assertThat(link2.getType().getStyle().getStroke3().getThickness()).isEqualTo(1);
		assertThat(link2.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.SUPERSET);
		Link link3 = diagram.getLinks().get(2);
		assertThat(link3.getEntity1().getName()).isEqualTo("E1/o E2, E3, E4 /center");
		assertThat(link3.getEntity2().getName()).isEqualTo("E3");
		assertThat(link3.getType().getStyle().getStroke3().getThickness()).isEqualTo(1);
		assertThat(link3.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.SUPERSET);
		Link link4 = diagram.getLinks().get(3);
		assertThat(link4.getEntity1().getName()).isEqualTo("E1/o E2, E3, E4 /center");
		assertThat(link4.getEntity2().getName()).isEqualTo("E4");
		assertThat(link4.getType().getStyle().getStroke3().getThickness()).isEqualTo(1);
		assertThat(link4.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.SUPERSET);
	}

	@Test
	void test_executeOverlappingSubclassesWithCompulsaryParticipation() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E1 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E2 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E3 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E4 {"), ParserPass.ONE);

		BlocLines lines = BlocLines.singleString("E1 =>= o { E2, E3, E4 }");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertThat(result).matches(CommandExecutionResult::isOk);

		assertThat(diagram.getLinks().size()).isEqualTo(4);
		Link link1 = diagram.getLinks().get(0);
		assertThat(link1.getEntity1().getName()).isEqualTo("E1");
		assertThat(link1.getEntity2().getName()).isEqualTo("E1/o E2, E3, E4 /center");
		assertThat(link1.getType().getStyle().getStroke3().getThickness()).isEqualTo(2);
		assertThat(link1.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.NONE);
		Link link2 = diagram.getLinks().get(1);
		assertThat(link2.getEntity1().getName()).isEqualTo("E1/o E2, E3, E4 /center");
		assertThat(link2.getEntity2().getName()).isEqualTo("E2");
		assertThat(link2.getType().getStyle().getStroke3().getThickness()).isEqualTo(1);
		assertThat(link2.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.SUPERSET);
		Link link3 = diagram.getLinks().get(2);
		assertThat(link3.getEntity1().getName()).isEqualTo("E1/o E2, E3, E4 /center");
		assertThat(link3.getEntity2().getName()).isEqualTo("E3");
		assertThat(link3.getType().getStyle().getStroke3().getThickness()).isEqualTo(1);
		assertThat(link3.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.SUPERSET);
		Link link4 = diagram.getLinks().get(3);
		assertThat(link4.getEntity1().getName()).isEqualTo("E1/o E2, E3, E4 /center");
		assertThat(link4.getEntity2().getName()).isEqualTo("E4");
		assertThat(link4.getType().getStyle().getStroke3().getThickness()).isEqualTo(1);
		assertThat(link4.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.SUPERSET);
	}

	@Test
	void test_executeCategories() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E1 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E2 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E3 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E4 {"), ParserPass.ONE);

		BlocLines lines = BlocLines.singleString("E1 ->- U { E2, E3, E4 }");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertThat(result).matches(CommandExecutionResult::isOk);

		assertThat(diagram.getLinks().size()).isEqualTo(4);
		Link link1 = diagram.getLinks().get(0);
		assertThat(link1.getEntity1().getName()).isEqualTo("E1");
		assertThat(link1.getEntity2().getName()).isEqualTo("E1/U E2, E3, E4 /center");
		assertThat(link1.getType().getStyle().getStroke3().getThickness()).isEqualTo(1);
		assertThat(link1.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.SUPERSET);
		Link link2 = diagram.getLinks().get(1);
		assertThat(link2.getEntity1().getName()).isEqualTo("E1/U E2, E3, E4 /center");
		assertThat(link2.getEntity2().getName()).isEqualTo("E2");
		assertThat(link2.getType().getStyle().getStroke3().getThickness()).isEqualTo(1);
		assertThat(link2.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.NONE);
		Link link3 = diagram.getLinks().get(2);
		assertThat(link3.getEntity1().getName()).isEqualTo("E1/U E2, E3, E4 /center");
		assertThat(link3.getEntity2().getName()).isEqualTo("E3");
		assertThat(link3.getType().getStyle().getStroke3().getThickness()).isEqualTo(1);
		assertThat(link3.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.NONE);
		Link link4 = diagram.getLinks().get(3);
		assertThat(link4.getEntity1().getName()).isEqualTo("E1/U E2, E3, E4 /center");
		assertThat(link4.getEntity2().getName()).isEqualTo("E4");
		assertThat(link4.getType().getStyle().getStroke3().getThickness()).isEqualTo(1);
		assertThat(link4.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.NONE);
	}

	@Test
	void test_executeCategoriesWithCompulsaryParticipation() throws NoSuchColorException {
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E1 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E2 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E3 {"), ParserPass.ONE);
		new CommandCreateEntity().execute(diagram, BlocLines.singleString("entity E4 {"), ParserPass.ONE);

		BlocLines lines = BlocLines.singleString("E1 =>= U { E2, E3, E4 }");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertThat(result).matches(CommandExecutionResult::isOk);

		assertThat(diagram.getLinks().size()).isEqualTo(4);
		Link link1 = diagram.getLinks().get(0);
		assertThat(link1.getEntity1().getName()).isEqualTo("E1");
		assertThat(link1.getEntity2().getName()).isEqualTo("E1/U E2, E3, E4 /center");
		assertThat(link1.getType().getStyle().getStroke3().getThickness()).isEqualTo(2);
		assertThat(link1.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.SUPERSET);
		Link link2 = diagram.getLinks().get(1);
		assertThat(link2.getEntity1().getName()).isEqualTo("E1/U E2, E3, E4 /center");
		assertThat(link2.getEntity2().getName()).isEqualTo("E2");
		assertThat(link2.getType().getStyle().getStroke3().getThickness()).isEqualTo(1);
		assertThat(link2.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.NONE);
		Link link3 = diagram.getLinks().get(2);
		assertThat(link3.getEntity1().getName()).isEqualTo("E1/U E2, E3, E4 /center");
		assertThat(link3.getEntity2().getName()).isEqualTo("E3");
		assertThat(link3.getType().getStyle().getStroke3().getThickness()).isEqualTo(1);
		assertThat(link3.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.NONE);
		Link link4 = diagram.getLinks().get(3);
		assertThat(link4.getEntity1().getName()).isEqualTo("E1/U E2, E3, E4 /center");
		assertThat(link4.getEntity2().getName()).isEqualTo("E4");
		assertThat(link4.getType().getStyle().getStroke3().getThickness()).isEqualTo(1);
		assertThat(link4.getType().getMiddleDecor()).isEqualTo(LinkMiddleDecor.NONE);
	}
}
