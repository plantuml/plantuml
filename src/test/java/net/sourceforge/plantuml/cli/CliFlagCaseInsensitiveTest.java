package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

// https://github.com/plantuml/plantuml/issues/2820
// Regression: CliFlag.match(...) became case-sensitive when the CLI parser was
// rewritten (53c20db08, shipped in v1.2025.9), whereas every flag was matched with
// String#equalsIgnoreCase before that rewrite (see Option.java pre-15fa06c1f). This
// silently broke any flag typed in non-canonical case (-pipeNoStderr, -TSVG, ...):
// the flag was simply ignored instead of raising an error, so the regression was easy
// to miss. These tests pin the restored behavior, whole-token flags are matched
// case-insensitively again, while the single-letter prefix flags (-D/-I/-P/-S) stay
// case-sensitive on purpose, since making *those* case-insensitive would let e.g.
// "-svg" be swallowed as a "-S" skinparam definition instead of reaching T_SVG.
class CliFlagCaseInsensitiveTest {

	@Test
	void unaryBooleanFlag_matchesRegardlessOfCase() {
		assertTrue(CliFlag.PIPENOSTDERR.match("-pipenostderr"));
		assertTrue(CliFlag.PIPENOSTDERR.match("-pipeNoStderr"));
		assertTrue(CliFlag.PIPENOSTDERR.match("-PIPENOSTDERR"));
	}

	@Test
	void unaryBooleanFlagAlias_matchesRegardlessOfCase() {
		assertTrue(CliFlag.T_SVG.match("-tsvg"));
		assertTrue(CliFlag.T_SVG.match("-TSVG"));
		assertTrue(CliFlag.T_SVG.match("-svg"));
		assertTrue(CliFlag.T_SVG.match("-SVG"));
		assertTrue(CliFlag.T_SVG.match("--svg"));
	}

	@Test
	void unaryOptionalColonFlag_matchesRegardlessOfCase() {
		assertTrue(CliFlag.STDRPT.match("-stdrpt"));
		assertTrue(CliFlag.STDRPT.match("-STDRPT"));
		assertTrue(CliFlag.STDRPT.match("-stdrpt:1"));
		assertTrue(CliFlag.STDRPT.match("-StdRpt:1"));

		assertTrue(CliFlag.PICOWEB.match("-picoweb"));
		assertTrue(CliFlag.PICOWEB.match("-PicoWeb"));
		assertTrue(CliFlag.PICOWEB.match("--http-server"));
		assertTrue(CliFlag.PICOWEB.match("--HTTP-SERVER"));
	}

	@Test
	void inlineKeyFlag_prefixStaysCaseSensitive_toAvoidCollisions() {
		// "-S..." (skinparam) must not swallow "-svg" meant for T_SVG. Note this only
		// protects the lowercase spelling: "-SVG" (all-uppercase, no leading "t") was
		// already ambiguous with "-S" pre-fix too, since "-S" is trivially a same-case
		// prefix of "-SVG" regardless of case-insensitivity; that pre-existing, narrower
		// ambiguity is out of scope here.
		assertFalse(CliFlag.SKINPARAM.match("-svg"));
		assertTrue(CliFlag.SKINPARAM.match("-Sbackgroundcolor=red"));
		assertFalse(CliFlag.SKINPARAM.match("-sbackgroundcolor=red"));

		assertTrue(CliFlag.DEFINE.match("-DFoo=1"));
		assertFalse(CliFlag.DEFINE.match("-dFoo=1"));
	}

	@Test
	void inlineKeyFlag_keyCasingIsPreserved() throws InterruptedException, IOException, CliParsingException {
		final CliOptions options = new CliOptions("-DFooBar=1", "-tsvg");
		assertEquals("1", options.flags.getMap(CliFlag.DEFINE).get("FooBar"));
	}

}
