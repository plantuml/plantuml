/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2026, Arnaud Roques
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 */
package net.sourceforge.plantuml.cheneer;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.cheneer.command.CommandCreateAttribute;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.utils.LineLocation;

final class CommandCreateCompactAttribute extends CommandCreateAttribute {

	@Override
	protected CommandExecutionResult executeArg(ChenEerDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) throws NoSuchColorException {
		final Entity owner = diagram.peekOwner();
		if (owner == null)
			return CommandExecutionResult
					.error("Attribute must be inside an entity, relationship or another attribute");

		final String rawCode = arg.get("CODE", 0).trim();
		final String identity = diagram.cleanId(getIdentity(rawCode));
		String displayText = arg.get("DISPLAY", 0);
		if (displayText == null)
			displayText = identity;

		final String stereo = arg.get("STEREO", 0);
		final Stereotype stereotype = stereo == null ? null : Stereotype.build(stereo);
		final boolean composite = arg.get("COMPOSITE", 0) != null;
		color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		if (diagram.addCompactAttribute(location, identity, displayText, getDomain(rawCode), stereotype,
				composite) == false)
			return CommandExecutionResult.error("Attribute already exists");

		return CommandExecutionResult.ok();
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.LINE);
	}

	private static String getIdentity(String rawCode) {
		final int colon = rawCode.indexOf(':');
		return (colon < 0 ? rawCode : rawCode.substring(0, colon)).trim();
	}

	private static String getDomain(String rawCode) {
		final int colon = rawCode.indexOf(':');
		if (colon < 0)
			return null;

		final String result = rawCode.substring(colon + 1).trim();
		return result.length() == 0 ? null : result;
	}

}
