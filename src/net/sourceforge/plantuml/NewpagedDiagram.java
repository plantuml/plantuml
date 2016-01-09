/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * Revision $Revision: 10459 $
 *
 */
package net.sourceforge.plantuml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramDescriptionImpl;
import net.sourceforge.plantuml.core.ImageData;

public class NewpagedDiagram extends AbstractPSystem {

	private final List<Diagram> diagrams = new ArrayList<Diagram>();

	public NewpagedDiagram(AbstractPSystem diag1, AbstractPSystem diag2) {
		if (diag1 instanceof NewpagedDiagram) {
			throw new IllegalArgumentException();
		}
		if (diag2 instanceof NewpagedDiagram) {
			throw new IllegalArgumentException();
		}
		this.diagrams.add(diag1);
		this.diagrams.add(diag2);
	}

	@Override
	public String toString() {
		return super.toString() + " SIZE=" + diagrams.size() + " " + diagrams;
	}

	public Diagram getLastDiagram() {
		return diagrams.get(diagrams.size() - 1);
	}

	public CommandExecutionResult executeCommand(Command cmd, BlocLines lines) {
		final int nb = diagrams.size();
		final CommandExecutionResult tmp = cmd.execute(diagrams.get(nb - 1), lines);
		if (tmp.getNewDiagram() instanceof NewpagedDiagram) {
			final NewpagedDiagram new1 = (NewpagedDiagram) tmp.getNewDiagram();
			// System.err.println("this=" + this);
			// System.err.println("new1=" + new1);
			if (new1.size() != 2) {
				throw new IllegalStateException();
			}
			if (new1.diagrams.get(0) != this.diagrams.get(nb - 1)) {
				throw new IllegalStateException();
			}
			this.diagrams.add(new1.diagrams.get(1));
			return tmp.withDiagram(this);

		}
		return tmp;
	}

	private int size() {
		return diagrams.size();
	}

	public ImageData exportDiagram(OutputStream os, int num, FileFormatOption fileFormat) throws IOException {
		return diagrams.get(num).exportDiagram(os, 0, fileFormat);
	}

	public int getNbImages() {
		int nb = 0;
		for (Diagram d : diagrams) {
			nb += d.getNbImages();
		}
		return nb;
	}

	public DiagramDescription getDescription() {
		final StringBuilder sb = new StringBuilder();
		for (Diagram d : diagrams) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(d.getDescription());
		}
		return new DiagramDescriptionImpl(sb.toString(), getClass());
	}

	public String getWarningOrError() {
		final StringBuilder sb = new StringBuilder();
		for (Diagram d : diagrams) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(d.getWarningOrError());
		}
		return sb.toString();
	}

	@Override
	public void makeDiagramReady() {
		super.makeDiagramReady();
		for (Diagram diagram : diagrams) {
			((AbstractPSystem) diagram).makeDiagramReady();
		}
	}

}
