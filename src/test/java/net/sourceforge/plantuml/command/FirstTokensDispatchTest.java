package net.sourceforge.plantuml.command;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.PSystemBuilder;
import net.sourceforge.plantuml.api.PSystemFactory;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.BlocLines;

/**
 * What the command index rests on: a command that accepts a line must have declared that line's
 * first token, or have declared nothing at all. Declaring too much only wastes a regex attempt,
 * but a command that leaves out a token it accepts silently stops being reachable for it, and no
 * diagram test would necessarily notice -- another command usually answers instead.
 *
 * Most declarations are read from the patterns by {@code FirstTokens}, so this is mostly a check on
 * that reading; the few written by hand, for patterns it cannot read, are checked the same way. It
 * runs against every command of every factory and every line the Vega corpus holds.
 */
class FirstTokensDispatchTest {

	/**
	 * Commands whose pattern also accepts their keyword glued to what follows it --
	 * {@code "returnfoo"}, {@code "groupfoo {"} -- and whose hand-written declaration leaves that form
	 * out on purpose, since no list of tokens can hold it. For them, and for them only, a token that
	 * merely extends one of their declared keywords is not reported.
	 */
	private static final Set<String> GLUED_KEYWORD_LEFT_OUT = new HashSet<>(Arrays.asList( //
			"net.sourceforge.plantuml.sequencediagram.command.CommandReturn", //
			"net.sourceforge.plantuml.nwdiag.CommandGroup", //
			"net.sourceforge.plantuml.nwdiag.CommandNetwork"));

	private static boolean isGluedKeywordLeftOut(Command cmd, Collection<String> declared, String token) {
		if (GLUED_KEYWORD_LEFT_OUT.contains(cmd.getClass().getName()) == false)
			return false;

		for (String keyword : declared)
			if (keyword.isEmpty() == false && token.length() > keyword.length() && token.startsWith(keyword))
				return true;

		return false;
	}

	@Test
	void everyAcceptedLineHasItsTokenDeclared() throws Exception {
		final List<Command> commands = new ArrayList<>();
		final List<Collection<String>> declared = new ArrayList<>();
		for (PSystemCommandFactory factory : allCommandFactories()) {
			final List<Command> cmds = new ArrayList<>();
			factory.initCommandsList(cmds);
			for (Command cmd : cmds) {
				final Collection<String> own = cmd.mandatoryFirstTokensSlow();
				commands.add(cmd);
				declared.add(own);
			}
		}
		assertTrue(commands.size() > 500, "commands=" + commands.size());

		final List<String> problems = new ArrayList<>();
		for (String line : corpusLines()) {
			final StringLocated located = new StringLocated(line, null);
			final String token = located.getFirstToken();
			final BlocLines single = BlocLines.single(located);
			for (int i = 0; i < commands.size(); i++) {
				final Collection<String> own = declared.get(i);
				if (own == null || own.contains(token))
					continue;

				final Command cmd = commands.get(i);
				if (isGluedKeywordLeftOut(cmd, own, token))
					continue;

				final CommandControl result;
				try {
					result = cmd.isValid(single);
				} catch (Exception e) {
					continue;
				}
				// CommandDecoratorMultine answers OK_PARTIAL to every line on purpose: it gathers
				// lines first and only then hands them over, so only its OK says anything here.
				// The line it gathers from is still this one, hence the same first token.
				if (result == CommandControl.OK || (result == CommandControl.OK_PARTIAL
						&& cmd instanceof CommandDecoratorMultine == false))
					problems.add(cmd.getClass().getName() + " accepts <" + line + "> whose first token is <" + token
							+ ">, but declares " + new TreeSet<>(own));
			}
		}
		assertTrue(problems.isEmpty(), problems.size() + " undeclared:\n" + String.join("\n", problems));
	}

	private static List<PSystemCommandFactory> allCommandFactories() throws Exception {
		final Constructor<PSystemBuilder> constructor = PSystemBuilder.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		final Field field = PSystemBuilder.class.getDeclaredField("factories");
		field.setAccessible(true);
		final List<PSystemCommandFactory> result = new ArrayList<>();
		for (PSystemFactory factory : (List<PSystemFactory>) field.get(constructor.newInstance()))
			if (factory instanceof PSystemCommandFactory)
				result.add((PSystemCommandFactory) factory);

		return result;
	}

	private static Set<String> corpusLines() throws IOException {
		final Set<String> result = new LinkedHashSet<>();
		try (Stream<Path> walk = Files.walk(Paths.get("src/test/resources/vega"))) {
			for (Path path : (Iterable<Path>) walk.filter(p -> p.toString().endsWith(".puml"))::iterator)
				for (String line : new String(Files.readAllBytes(path), StandardCharsets.UTF_8).split("\n"))
					result.add(line.replace("\r", ""));
		}
		return result;
	}

}
