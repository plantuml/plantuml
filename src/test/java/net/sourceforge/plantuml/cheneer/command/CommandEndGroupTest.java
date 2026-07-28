package net.sourceforge.plantuml.cheneer.command;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.ArrayList;

import org.junit.jupiter.api.Test;

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

public class CommandEndGroupTest {

	private final Command<ChenEerDiagram> command = new CommandEndGroup();

	private final ChenEerDiagram diagram = new ChenEerDiagram(UmlSource.create(new ArrayList<>(), false), null, new PreprocessingArtifact());

	@Test
	void test_parse() {
		IRegex regex = CommandEndGroup.getRegexConcat();
		RegexResult matcher = regex.matcher("}");

		assertNotNull(matcher);
	}

	@Test
	void test_execute() throws NoSuchColorException {
		diagram.pushOwner(null);

		BlocLines lines = BlocLines.singleString("}");
		CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);

		assertTrue(result.isOk());

		// popOwner() was called by CommandEndGroup.execute(): the owner stack
		// (which had exactly one entry from pushOwner(null) above) is now
		// empty, so popping again returns false.
		assertFalse(diagram.popOwner());
	}
}
