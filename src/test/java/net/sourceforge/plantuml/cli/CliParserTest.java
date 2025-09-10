package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CliParserTest {

	@Test
	void testParse0010() {
		assertEquals("{AUTHOR=true, GRAPHVIZ_DOT=foo.exe}",
				CliParser.parse2("-author", "-graphvizdot", "foo.exe").toString());
	}

	@Test
	void testParse0011() {
		assertEquals("{AUTHOR=true}", CliParser.parse2("-authors").toString());
	}

	@Test
	void testParse0012() {
		assertEquals("{T_SVG=true}", CliParser.parse2("-svg").toString());
		assertEquals("{T_SVG=true}", CliParser.parse2("-tsvg").toString());
	}

	@Test
	void testParse0020() {
		assertEquals("{DEFINE=FOO=42}", CliParser.parse2("-DFOO=42").toString());
	}

	@Test
	void testParse0030() {
		assertEquals("{DEFINE=DUMMY=null}", CliParser.parse2("-DDUMMY").toString());
	}

	@Test
	void testParse00430() {
		assertEquals("{FTP=null}", CliParser.parse2("-ftp").toString());
		assertEquals("{FTP=42}", CliParser.parse2("-ftp:42").toString());
	}

}
