package net.sourceforge.plantuml.url;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class UbrexUrlBuilderTest {
	@ParameterizedTest
	@CsvSource(value = {
		" [[http://foo]],                   STRICT, http://foo, http://foo, http://foo ",
		" [[http://foo{tooltip} text]],     STRICT, http://foo, tooltip, text ",
		" [[http://foo {tooltip} text]],    STRICT, http://foo, tooltip, text ",
		" [[http://foo {tooltip}]],         STRICT, http://foo, tooltip, http://foo ",
		" [[http://foo text]],              STRICT, http://foo, http://foo, text ",
		" [[  http://foo  ]],               STRICT, http://foo, http://foo, http://foo ",
		" [[\"http://foo text\"]],          STRICT, http://foo text, http://foo text, http://foo text ",
		" start [[\"http://foo test\"]] end, ANYWHERE, http://foo test, http://foo test, http://foo test ",
		" [[http://google.com{a nice toolip} <img:HelloWorld.png{scale=2}>]], STRICT, http://google.com, a nice toolip, <img:HelloWorld.png{scale=2}> ",
		" [[http://google.com {a nice toolip} <img:HelloWorld.png{scale=2}>]], STRICT, http://google.com, a nice toolip, <img:HelloWorld.png{scale=2}> ",
		" [[http://google.com <img:HelloWorld.png{scale=2}>]], STRICT, http://google.com, http://google.com, <img:HelloWorld.png{scale=2}> ",
		" [[http://foo?dummy={123}&action=edit]], STRICT, http://foo?dummy={123}&action=edit, http://foo?dummy={123}&action=edit, http://foo?dummy={123}&action=edit",
		" '[[http://www.plantuml.com{Json: {\"firstName\":\"Bob\", \"lastName\":\"Smith\"}}]]', STRICT, http://www.plantuml.com, 'Json: {\"firstName\":\"Bob\", \"lastName\":\"Smith\"}', http://www.plantuml.com",
		" [[http://foo?dummy={123}z{}]],    STRICT, http://foo?dummy=, 123}z{, http://foo?dummy= ",
		" [[{tooltip} some text]],          STRICT, '', tooltip, some text ",
		" fromor [[www.google.com POST /session/csrStart]] end, ANYWHERE, www.google.com, www.google.com, POST /session/csrStart ",
		" '[[ blaha(xyz,12){ tooltip } ]]', STRICT, 'blaha(xyz,12)', ' tooltip ', 'blaha(xyz,12)' ",
		" '[[{Json: {\"firstName\":\"Bob\", \"lastName\":\"Smith\"}}]]', STRICT, '', 'Json: {\"firstName\":\"Bob\", \"lastName\":\"Smith\"}','' ",
		" [[http://foo?dummy={123}{}]],     STRICT, http://foo?dummy=, 123}{, http://foo?dummy= ",
		" [[\"http://foo?dummy={123}\"{}]], STRICT, http://foo?dummy={123}, '', http://foo?dummy={123} ",
	})
	public void testUrl(String input, String urlMode, String urlLink, String tooltip, String label) {
		final UbrexUrlBuilder b = new UbrexUrlBuilder(null, UrlMode.valueOf(urlMode));
		final Url url = b.getUrl(input);
		assertEquals(urlLink, url.getUrl(), "url");
		assertEquals(tooltip, url.getTooltip(), "tooltip");
		assertEquals(label, url.getLabel(), "label");
	}
}
