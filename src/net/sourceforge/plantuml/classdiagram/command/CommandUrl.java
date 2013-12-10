/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 4161 $
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import java.util.List;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.IEntity;

public class CommandUrl extends SingleLineCommand<AbstractEntityDiagram> {

	public CommandUrl() {
		super("(?i)^url\\s*(?:of|for)?\\s+([\\p{L}0-9_.]+|\"[^\"]+\")\\s+(?:is)?\\s*(" + UrlBuilder.getRegexp() + ")$");
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractEntityDiagram diagram, List<String> arg) {
		final Code code = Code.of(arg.get(0));
		final String urlString = arg.get(1);
		final IEntity entity;
		if (diagram.leafExist(code)) {
			entity = diagram.getOrCreateLeaf(code, null);
		} else if (diagram.isGroup(code)) {
			entity = diagram.getGroup(code);
		} else {
			return CommandExecutionResult.error(code + " does not exist");
		}
		// final IEntity entity = diagram.getOrCreateLeaf(code, null);
		final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
		final Url url = urlBuilder.getUrl(urlString);
		entity.addUrl(url);
		return CommandExecutionResult.ok();
	}

}
