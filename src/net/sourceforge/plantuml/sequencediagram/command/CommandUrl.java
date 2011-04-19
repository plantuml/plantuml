/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.List;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class CommandUrl extends SingleLineCommand<SequenceDiagram> {

	public CommandUrl(SequenceDiagram diagram) {
		super(diagram,
				"(?i)^url\\s*(?:of|for)?\\s+([\\p{L}0-9_.]+|\"[^\"]+\")\\s+(?:is)?\\s*\\[\\[([^|]*)(?:\\|([^|]*))?\\]\\]$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		final String code = arg.get(0);
		String url = arg.get(1);
		final String title = arg.get(2);
		final Participant p = getSystem().getOrCreateParticipant(code);
		if (url.startsWith("http:") == false && url.startsWith("https:") == false) {
			final String top = getSystem().getSkinParam().getValue("topurl");
			if (top != null) {
				url = top + url;
			}
		}
		p.setUrl(new Url(url, title));
		return CommandExecutionResult.ok();
	}

}
