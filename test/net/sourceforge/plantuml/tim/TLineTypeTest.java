package net.sourceforge.plantuml.tim;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TLineTypeTest {

	@Retention(RetentionPolicy.RUNTIME)
	@ParameterizedTest
	@CsvSource({
			" ''                  , PLAIN ",             // the empty string
			" x                   , PLAIN ",
			" ''''                , COMMENT_SIMPLE ",    // '''' means test one single quote !
			" '''' x              , COMMENT_SIMPLE ",
			" /'                  , COMMENT_LONG_START ",
			" /' x                , COMMENT_LONG_START ",
			" /' x '/             , COMMENT_SIMPLE ",
			" /' x '/  y          , PLAIN ",
			" /''/                , COMMENT_SIMPLE ",
			" '!assert'           , PLAIN ",
			" '!assert '          , ASSERT ",
			" '!assert x'         , ASSERT ",
			" '!define'           , PLAIN ",
			" '!define x'         , AFFECTATION_DEFINE ",
			" '!define x('        , LEGACY_DEFINE ",
			" '!definelong x'     , LEGACY_DEFINELONG ",
			" '!dump_memory'      , DUMP_MEMORY ",
			" '!dump_memory x'    , DUMP_MEMORY ",
			" '!else'             , ELSE ",
			" '!else x'           , ELSE ",
			" '!elseif'           , ELSEIF ",
			" '!elseif x'         , ELSEIF ",
			" '!end definelong'   , END_FUNCTION ",
			" '!end definelong x' , END_FUNCTION ",
			" '!end function'     , END_FUNCTION ",
			" '!end function x'   , END_FUNCTION ",
			" '!end procedure'    , END_FUNCTION ",
			" '!end procedure x'  , END_FUNCTION ",
			" '!enddefinelong'    , END_FUNCTION ",
			" '!enddefinelong x'  , END_FUNCTION ",
			" '!endfor'           , ENDFOREACH ",
			" '!endfor x'         , ENDFOREACH ",
			" '!endfunction'      , END_FUNCTION ",
			" '!endfunction x'    , END_FUNCTION ",
			" '!endif'            , ENDIF ",
			" '!endif x'          , ENDIF ",
			" '!endprocedure'     , END_FUNCTION ",
			" '!endprocedure x'   , END_FUNCTION ",
			" '!endsub'           , ENDSUB ",
			" '!endsub x'         , ENDSUB ",
			" '!endwhile'         , ENDWHILE ",
			" '!endwhile x'       , ENDWHILE ",
			" '!final function x'                 , DECLARE_RETURN_FUNCTION ",
			" '!final procedure x'                , DECLARE_PROCEDURE ",
			" '!final unquoted function x'        , DECLARE_RETURN_FUNCTION ",
			" '!final unquoted procedure x'       , DECLARE_PROCEDURE ",
			" '!foreach'          , PLAIN ",
			" '!foreach '         , FOREACH ",
			" '!foreach x'        , FOREACH ",
			" '!function x'       , DECLARE_RETURN_FUNCTION ",
			" '!global'           , PLAIN ",
			" '!global x='        , AFFECTATION ",
			" '!if'               , PLAIN ",
			" '!if '              , IF ",
			" '!if x'             , IF ",
			" '!ifdef'            , PLAIN ",
			" '!ifdef '           , IFDEF ",
			" '!ifdef x'          , IFDEF ",
			" '!ifndef'           , PLAIN ",
			" '!ifndef '          , IFNDEF ",
			" '!ifndef x'         , IFNDEF ",
			" '!import'           , IMPORT ",
			" '!import x'         , IMPORT ",
			" '!include'          , INCLUDE ",
			" '!include x'        , INCLUDE ",
			" '!include_many'     , INCLUDE ",
			" '!include_many x'   , INCLUDE ",
			" '!include_once'     , INCLUDE ",
			" '!include_once x'   , INCLUDE ",
			" '!includedef'       , INCLUDE_DEF ",
			" '!includedef x'     , INCLUDE_DEF ",
			" '!includesub'       , INCLUDESUB ",
			" '!includesub x'     , INCLUDESUB ",
			" '!includeurl'       , INCLUDE ",
			" '!includeurl x'     , INCLUDE ",
			" '!local'            , PLAIN ",
			" '!local x='         , AFFECTATION ",
			" '!log'              , LOG ",
			" '!log x'            , LOG ",
			" '!procedure x'      , DECLARE_PROCEDURE ",
			" '!return'           , RETURN ",
			" '!return x'         , RETURN ",
			" '!startsub'         , PLAIN ",
			" '!startsub '        , STARTSUB ",
			" '!startsub x'       , STARTSUB ",
			" '!theme'            , THEME ",
			" '!theme x'          , THEME ",
			" '!undef'            , PLAIN ",
			" '!undef '           , UNDEF ",
			" '!undef x'          , UNDEF ",
			" '!unquoted final function x'    , DECLARE_RETURN_FUNCTION ",
			" '!unquoted final procedure x'   , DECLARE_PROCEDURE ",
			" '!unquoted function x'          , DECLARE_RETURN_FUNCTION ",
			" '!unquoted procedure x'         , DECLARE_PROCEDURE ",
			" '!while'            , PLAIN ",
			" '!while '           , WHILE ",
			" '!while x'          , WHILE ",
	})
	@interface TestCases {
	}

	@TestCases
	void test_getFromLineInternal(String line, TLineType expected) {
		testedValues.add(expected);
		assertThat(TLineType.getFromLineInternal(line))
				.isEqualTo(expected);
	}

	@TestCases
	void test_getFromLineInternal_white_space_before(String line, TLineType expected) {
		testedValues.add(expected);
		assertThat(TLineType.getFromLineInternal(" \t" + line))
				.isEqualTo(expected);
	}

	@TestCases
	void test_getFromLineInternal_white_space_after(String line, TLineType expected) {
		if (TLineType.REQUIRES_TRAILING_SPACE.contains(line)) {
			return;  // Does not make sense to test these here
		}
		testedValues.add(expected);
		assertThat(TLineType.getFromLineInternal(line + " \t"))
				.isEqualTo(expected);
	}

	@TestCases
	@SuppressWarnings("unused")
	void test_getFromLineInternal_junk_before(String line, TLineType expected) {
		testedValues.add(expected);
		assertThat(TLineType.getFromLineInternal("x" + line))
				.isEqualTo(TLineType.PLAIN);
	}

	private static final Set<TLineType> testedValues = new HashSet<>();

	@AfterAll
	static void afterAll() {
		final String missed = stream(TLineType.values())
				.filter(v -> !testedValues.contains(v))
				.map(TLineType::toString)
				.sorted()
				.collect(Collectors.joining(" "));

		if (!missed.isEmpty()) {
			fail("These values were not tested: %s", missed);
		}
	}
}
