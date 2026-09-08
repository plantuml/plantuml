/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.ArrayList;

import net.sourceforge.plantuml.classdiagram.ClassDiagram;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;

public class CommandPackageTest {
	private final Command<AbstractEntityDiagram> command = new CommandPackage();

	private AbstractEntityDiagram newDiagram() {
		return new ClassDiagram(UmlSource.create(new ArrayList<>(), false), null, new PreprocessingArtifact());
	}

	// ---- Regex-level tests -------------------------------------------------

	@Test
	void test_parseCodeAsDisplay() {
		final IRegex regex = getRegex();
		final RegexResult matcher = regex.matcher("package uid as \"Hello\" {");

		assertNotNull(matcher);
		assertEquals("uid", matcher.getLazzy("CODE", 0));
		assertEquals("\"Hello\"", matcher.getLazzy("DISPLAY", 0));
	}

	@Test
	void test_parseDisplayAsCode() {
		final IRegex regex = getRegex();
		final RegexResult matcher = regex.matcher("package \"Hello\" as uid {");

		assertNotNull(matcher);
		assertEquals("uid", matcher.getLazzy("CODE", 0));
		assertEquals("\"Hello\"", matcher.getLazzy("DISPLAY", 0));
	}

	@Test
	void test_parseCodeAsDisplayWithVisibilityAndStereotype() {
		final IRegex regex = getRegex();
		final RegexResult matcher = regex.matcher("+package uid as \"Hello\" <<Frame>> {");

		assertNotNull(matcher, "package CODE as \"DISPLAY\" with a leading visibility "
						+ "modifier must parse (issue #2846)");
		assertEquals("+", matcher.get("VISIBILITY", 0));
		assertEquals("uid", matcher.getLazzy("CODE", 0));
		assertEquals("\"Hello\"", matcher.getLazzy("DISPLAY", 0));
		assertEquals("<<Frame>>", matcher.get("STEREOTYPE", 0));
	}

	@Test
	void test_parseDisplayAsCodeWithVisibilityAndStereotype() {
		final IRegex regex = getRegex();
		final RegexResult matcher = regex.matcher("+package \"Hello\" as uid <<Frame>> {");

		assertNotNull(matcher);
		assertEquals("+", matcher.get("VISIBILITY", 0));
		assertEquals("uid", matcher.getLazzy("CODE", 0));
		assertEquals("\"Hello\"", matcher.getLazzy("DISPLAY", 0));
		assertEquals("<<Frame>>", matcher.get("STEREOTYPE", 0));
	}

	@Test
	void test_parseCodeOnlyNoDisplay() {
		final IRegex regex = getRegex();
		final RegexResult matcher = regex.matcher("package uid {");

		assertNotNull(matcher);
		assertEquals("uid", matcher.getLazzy("CODE", 0));
		assertNull(matcher.getLazzy("DISPLAY", 0));
	}

	@Test
	void test_parseQuotedNameOnlyNoAs() {
		final IRegex regex = getRegex();
		final RegexResult matcher = regex.matcher("package \"Hello\" {");

		assertNotNull(matcher);
		assertEquals("\"Hello\"", matcher.getLazzy("CODE", 0));
		assertNull(matcher.getLazzy("DISPLAY", 0));
	}

	@Test
	void test_parseAnonymousPackage() {
		final IRegex regex = getRegex();
		final RegexResult matcher = regex.matcher("package {");

		assertNotNull(matcher);
		assertEquals("", matcher.getLazzy("CODE", 0));
	}

	private static IRegex getRegex() {
		return CommandPackage.getRegexConcat();
	}

	// ---- Execution-level tests ----------------------------------------------

	@Test
	void test_executeCodeAsDisplay() throws NoSuchColorException {
		final AbstractEntityDiagram diagram = newDiagram();
		final BlocLines lines = BlocLines.singleString("package uid as \"Hello\" {");

		final CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);
		assertTrue(result.isOk());

		final Entity p = diagram.getCurrentGroup();
		assertNotNull(p);
		assertEquals("[Hello]", p.getDisplay().toString());
	}

	@Test
	void test_executeDisplayAsCode() throws NoSuchColorException {
		final AbstractEntityDiagram diagram = newDiagram();
		final BlocLines lines = BlocLines.singleString("package \"Hello\" as uid {");

		final CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);
		assertTrue(result.isOk());

		final Entity p = diagram.getCurrentGroup();
		assertNotNull(p);
		assertEquals("[Hello]", p.getDisplay().toString());
	}

	@Test
	void test_executeCodeAsDisplayWithVisibility() throws NoSuchColorException {
		final AbstractEntityDiagram diagram = newDiagram();
		final BlocLines lines = BlocLines.singleString("+package uid as \"Hello\" {");

		final CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);
		assertTrue(result.isOk(), "issue #2846: package CODE as \"DISPLAY\" with a leading "
						+ "visibility modifier must execute successfully, not just parse");

		final Entity p = diagram.getCurrentGroup();
		assertNotNull(p);
		assertEquals("[Hello]", p.getDisplay().toString());
		assertEquals(VisibilityModifier.PUBLIC_METHOD, p.getVisibilityModifier());
	}

	@Test
	void test_executeDisplayAsCodeWithVisibility() throws NoSuchColorException {
		final AbstractEntityDiagram diagram = newDiagram();
		final BlocLines lines = BlocLines.singleString("+package \"Hello\" as uid {");

		final CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);
		assertTrue(result.isOk());

		final Entity p = diagram.getCurrentGroup();
		assertNotNull(p);
		assertEquals("[Hello]", p.getDisplay().toString());
		assertEquals(VisibilityModifier.PUBLIC_METHOD, p.getVisibilityModifier());
	}

	/**
	 * Both orderings, combined with visibility, must resolve to the exact same
	 * id, display, and visibility modifier - this is the core regression check
	 * for issue #2846: the two forms are meant to be equivalent, not merely
	 * both individually parseable.
	 */
	@Test
	void test_bothOrderingsWithVisibilityProduceEquivalentEntities() throws NoSuchColorException {
		final AbstractEntityDiagram diagramA = newDiagram();
		command.execute(diagramA, BlocLines.singleString("+package uid as \"Hello\" {"), ParserPass.ONE);
		final Entity a = diagramA.getCurrentGroup();

		final AbstractEntityDiagram diagramB = newDiagram();
		command.execute(diagramB, BlocLines.singleString("+package \"Hello\" as uid {"), ParserPass.ONE);
		final Entity b = diagramB.getCurrentGroup();

		assertNotNull(a);
		assertNotNull(b);
		assertEquals(a.getDisplay().toString(), b.getDisplay().toString());
		assertEquals(a.getVisibilityModifier(), b.getVisibilityModifier());
		assertEquals(a.getGroupType(), b.getGroupType());
	}

	@Test
	void test_executeCodeOnlyNoDisplay() throws NoSuchColorException {
		final AbstractEntityDiagram diagram = newDiagram();
		final BlocLines lines = BlocLines.singleString("package uid {");

		final CommandExecutionResult result = command.execute(diagram, lines, ParserPass.ONE);
		assertTrue(result.isOk());

		final Entity p = diagram.getCurrentGroup();
		assertNotNull(p);
		assertEquals("[uid]", p.getDisplay().toString());
	}
}
