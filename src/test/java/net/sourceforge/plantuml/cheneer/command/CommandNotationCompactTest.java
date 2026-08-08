package net.sourceforge.plantuml.cheneer.command;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.cheneer.ChenEerDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.utils.BlocLines;

class CommandNotationCompactTest {

	private final ChenEerDiagram diagram = new ChenEerDiagram(UmlSource.create(new ArrayList<>(), false), null,
			new PreprocessingArtifact());

	@Test
	void test_parseExactSyntax() {
		assertNotNull(CommandNotationCompact.getRegexConcat().matcher("notation compact"));
		assertNull(CommandNotationCompact.getRegexConcat().matcher("notation standard"));
		assertNull(CommandNotationCompact.getRegexConcat().matcher("notation compact extra"));
	}

	@Test
	void test_repeatBeforeDeclarationIsIdempotent() throws Exception {
		final CommandNotationCompact command = new CommandNotationCompact();
		assertTrue(command.execute(diagram, BlocLines.singleString("notation compact"), ParserPass.ONE).isOk());
		assertTrue(command.execute(diagram, BlocLines.singleString("notation compact"), ParserPass.ONE).isOk());
		assertTrue(diagram.isCompactNotation());
	}

	@Test
	void test_afterDeclarationIsError() throws Exception {
		final CommandExecutionResult declaration = new CommandCreateEntity().execute(diagram,
				BlocLines.singleString("entity E {"), ParserPass.ONE);
		assertTrue(declaration.isOk());

		final CommandExecutionResult result = new CommandNotationCompact().execute(diagram,
				BlocLines.singleString("notation compact"), ParserPass.ONE);
		assertFalse(result.isOk());
		assertFalse(diagram.isCompactNotation());
	}

}
