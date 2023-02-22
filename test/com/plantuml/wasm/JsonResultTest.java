package com.plantuml.wasm;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import com.plantuml.api.cheerpj.JsonResult;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.BlockUmlBuilder;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.preproc.Defines;

class JsonResultTest {

	@Test
	public void testNoDataFound() {
		final Object json1 = JsonResult.noDataFound(System.currentTimeMillis());
		final JsonObject json = (JsonObject) Json.parse(json1.toString());

		assertTrue(json.getInt("duration", -1) >= 0);
		assertEquals("No data found", json.getString("status", ""), "status");

		final TreeSet<String> names = new TreeSet<>(json.names());
		final TreeSet<String> expected = new TreeSet<>();
		expected.add("status");
		expected.add("duration");
		assertEquals(expected, names, "keys:" + expected.toString());
	}

	@Test
	public void testFromCrash() {
		Throwable t = new UnsupportedOperationException("hello");

		final Object json1 = JsonResult.fromCrash(System.currentTimeMillis(), t);
		final JsonObject json = (JsonObject) Json.parse(json1.toString());

		assertTrue(json.getInt("duration", -1) >= 0);
		assertEquals("General failure", json.getString("status", ""), "status");
		assertEquals("java.lang.UnsupportedOperationException: hello", json.getString("exception", ""), "exception");

		final TreeSet<String> names = new TreeSet<>(json.names());
		final TreeSet<String> expected = new TreeSet<>();
		expected.add("status");
		expected.add("duration");
		expected.add("exception");
		assertEquals(expected, names, "keys:" + expected.toString());
	}

	@Test
	public void testOk() throws IOException {

		final String text = "@startuml\nalice->bob\n@enduml";
		final BlockUmlBuilder builder = new BlockUmlBuilder(Collections.<String>emptyList(), UTF_8,
				Defines.createEmpty(), new StringReader(text), null, "string");
		final BlockUml block = builder.getBlockUmls().get(0);
		final Diagram diagram = block.getDiagram();
		final ImageData imageData = new ImageDataSimple(200, 100);

		final Object json1 = JsonResult.ok(System.currentTimeMillis(), imageData, diagram);
		final JsonObject json = (JsonObject) Json.parse(json1.toString());

		assertTrue(json.getInt("duration", -1) >= 0);
		assertEquals("ok", json.getString("status", ""), "status");
		assertEquals(200, json.getInt("width", -1), "width");
		assertEquals(100, json.getInt("height", -1), "height");
		assertEquals("(2 participants)", json.getString("description", ""), "description");

		final TreeSet<String> names = new TreeSet<>(json.names());
		final TreeSet<String> expected = new TreeSet<>();
		expected.add("status");
		expected.add("duration");
		expected.add("width");
		expected.add("height");
		expected.add("description");
		assertEquals(expected, names, "keys:" + expected.toString());
	}

	@Test
	public void testError() throws IOException {

		final String text = "@startuml\nalicebob\n@enduml";
		final BlockUmlBuilder builder = new BlockUmlBuilder(Collections.<String>emptyList(), UTF_8,
				Defines.createEmpty(), new StringReader(text), null, "string");
		final BlockUml block = builder.getBlockUmls().get(0);
		final Diagram diagram = block.getDiagram();

		final Object json1 = JsonResult.fromError(System.currentTimeMillis(), (PSystemError) diagram);
		final JsonObject json = (JsonObject) Json.parse(json1.toString());

		assertTrue(json.getInt("duration", -1) >= 0);
		assertEquals("Parsing error", json.getString("status", ""), "status");
		assertEquals(1, json.getInt("line", -1), "line");
		assertEquals("Syntax Error?", json.getString("error", ""), "error");

		final TreeSet<String> names = new TreeSet<>(json.names());
		final TreeSet<String> expected = new TreeSet<>();
		expected.add("status");
		expected.add("duration");
		expected.add("line");
		expected.add("error");
		assertEquals(expected, names, "keys:" + expected.toString());
	}

}
