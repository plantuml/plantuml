package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.Run;

class CliParsedTest {

	@Test
	void graphvizdot() throws CliParsingException {
		final CliParsed parsed = CliParsed.parse("-graphvizdot", "foo.exe");
		assertEquals("{GRAPHVIZ_DOT=[foo.exe]}", parsed.toString());
		assertTrue(parsed.isTrue(CliFlag.GRAPHVIZ_DOT));
		assertFalse(parsed.isTrue(CliFlag.CHARSET));
	}

	@Test
	void about() throws CliParsingException {
		final CliParsed parsed = CliParsed.parse("-about");
		assertTrue(parsed.getObject(CliFlag.AUTHOR) instanceof CliAction);
		assertTrue(parsed.isTrue(CliFlag.AUTHOR));
	}

	@Test
	void tsvg() throws CliParsingException {
		final CliParsed parsed = CliParsed.parse("-tsvg");
		assertEquals("{T_SVG=[SVG]}", parsed.toString());
		assertEquals(FileFormat.SVG, parsed.getObject(CliFlag.T_SVG));
		assertTrue(parsed.isTrue(CliFlag.T_SVG));
	}

	@Test
	void svg() throws CliParsingException {
		final CliParsed parsed = CliParsed.parse("-svg");
		assertEquals("{T_SVG=[SVG]}", parsed.toString());
		assertEquals(FileFormat.SVG, parsed.getObject(CliFlag.T_SVG));
		assertEquals("UTF-8", parsed.getString(CliFlag.CHARSET));
		assertTrue(parsed.isTrue(CliFlag.T_SVG));
	}

	@Test
	void charset() throws CliParsingException {
		final CliParsed parsed = CliParsed.parse("-svg", "-charset", "us-ascii");
		assertEquals("{CHARSET=[us-ascii], T_SVG=[SVG]}", parsed.toString());
		assertEquals(FileFormat.SVG, parsed.getObject(CliFlag.T_SVG));
		assertEquals("us-ascii", parsed.getString(CliFlag.CHARSET));
		assertTrue(parsed.isTrue(CliFlag.T_SVG));
		assertTrue(parsed.isTrue(CliFlag.CHARSET));
	}

	@Test
	void define10() throws CliParsingException {
		final CliParsed parsed = CliParsed.parse("-DFOO=42");
		assertEquals("{DEFINE={FOO=42}}", parsed.toString());
		assertTrue(parsed.isTrue(CliFlag.DEFINE));
		final Map<String, String> map = parsed.getMap(CliFlag.DEFINE);
		assertEquals("{FOO=42}", map.toString());
	}

	@Test
	void define11() throws CliParsingException {
		final CliParsed parsed = CliParsed.parse("--define", "FOO=42");
		assertEquals("{DEFINE_LONG=[FOO=42]}", parsed.toString());
		assertTrue(parsed.isTrue(CliFlag.DEFINE_LONG));
		final List<Object> map = parsed.getList(CliFlag.DEFINE_LONG);
		assertEquals("[FOO=42]", map.toString());
	}

	@Test
	void define12() throws CliParsingException {
		final CliParsed parsed = CliParsed.parse("-D", "FOO=42");
		assertEquals("{DEFINE={FOO=42}}", parsed.toString());
		assertTrue(parsed.isTrue(CliFlag.DEFINE));
		final Map<String, String> map = parsed.getMap(CliFlag.DEFINE);
		assertEquals("{FOO=42}", map.toString());
	}

	@Test
	void define20() throws CliParsingException {
		final CliParsed parsed = CliParsed.parse("-DFOO=42", "-DDUMMY=43");
		assertEquals("{DEFINE={FOO=42, DUMMY=43}}", parsed.toString());
		assertTrue(parsed.isTrue(CliFlag.DEFINE));
		final Map<String, String> map = parsed.getMap(CliFlag.DEFINE);
		assertEquals("{FOO=42, DUMMY=43}", map.toString());
	}

	@Test
	void define30() throws CliParsingException {
		final CliParsed parsed = CliParsed.parse("-DDUMMY");
		assertEquals("{DEFINE={DUMMY=null}}", parsed.toString());
		assertTrue(parsed.isTrue(CliFlag.DEFINE));
		final Map<String, String> map = parsed.getMap(CliFlag.DEFINE);
		assertEquals("{DUMMY=null}", map.toString());
	}

	@Test
	void ftp() throws CliParsingException {
		final CliParsed parsed = CliParsed.parse("-ftp");
		final List<Object> list = parsed.getList(CliFlag.FTP);
		assertEquals(0, list.size());
		assertTrue(parsed.isTrue(CliFlag.FTP));
		assertEquals("{FTP=[]}", parsed.toString());
	}

	@Test
	void ftp42() throws CliParsingException {
		final CliParsed parsed = CliParsed.parse("-ftp:42");
		final List<Object> list = parsed.getList(CliFlag.FTP);
		assertEquals("{FTP=[42]}", parsed.toString());
		assertTrue(parsed.isTrue(CliFlag.FTP));
		assertEquals(1, list.size());
		assertEquals("42", list.get(0));
	}

	@Test
	void stdrpt() throws CliParsingException {
		assertEquals("{STDRPT=[]}", CliParsed.parse("-stdrpt").toString());
		assertEquals("{STDRPT=[1]}", CliParsed.parse("-stdrpt:1").toString());
		assertEquals("{STDRPT=[2]}", CliParsed.parse("-stdrpt:2").toString());
	}

	@Test
	void picoweb() throws CliParsingException {
		assertEquals("{PICOWEB=[]}", CliParsed.parse("-picoweb").toString());
		assertEquals("{PICOWEB=[8000]}", CliParsed.parse("-picoweb:8000").toString());

		final CliParsed parsed = CliParsed.parse("-picoweb:8000:127.0.0.0");
		assertEquals("{PICOWEB=[8000, 127.0.0.0]}", parsed.toString());
		final List<Object> list = parsed.getList(CliFlag.PICOWEB);
		assertEquals(2, list.size());
		assertEquals("8000", list.get(0));
		assertEquals("127.0.0.0", list.get(1));
	}

	@Test
	void png() throws CliParsingException {
		final CliParsed parsed = CliParsed.parse("-png");
		assertEquals("{T_PNG=[PNG]}", parsed.toString());
		assertEquals(FileFormat.PNG, parsed.getObject(CliFlag.T_PNG));
	}

	@Test
	void testGetFromTypePng() throws CliParsingException {
		final CliParsed parsed = CliParsed.parse("-png");
		assertEquals("{T_PNG=[PNG]}", parsed.toString());
		assertEquals(FileFormat.PNG, parsed.getObject(CliFlag.T_PNG));
		assertEquals(FileFormat.PNG, parsed.getFromType(FileFormat.class));
	}

	@Test
	void testGetFromTypeSvg() throws CliParsingException {
		final CliParsed parsed = CliParsed.parse("-svg");
		assertEquals("{T_SVG=[SVG]}", parsed.toString());
		assertEquals(FileFormat.SVG, parsed.getObject(CliFlag.T_SVG));
		assertEquals(FileFormat.SVG, parsed.getFromType(FileFormat.class));
	}

	@Test
	void testMissingValue() throws CliParsingException {
		CliParsingException ex = assertThrows(CliParsingException.class, () -> CliParsed.parse("-I"));
		assertEquals("-I [INCLUDE]: missing value", ex.getMessage());

	}

}
