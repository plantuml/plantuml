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
 */
package net.sourceforge.plantuml.gitlog;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.UmlDiagramType;

public class GitDiagram extends TitledDiagram {

	private final Collection<GNode> gnodes;
	private SmetanaForGit smetana;

	public GitDiagram(UmlSource source, GitTextArea textArea, PreprocessingArtifact preprocessing) {
		super(source, UmlDiagramType.GIT, null, preprocessing);
		this.gnodes = new GNodeBuilder(textArea.getAllCommits()).getAllNodes();
		new GNodeBuilder(textArea.getAllCommits());
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Git)");
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {

		return createImageBuilder(fileFormatOption).drawable(getTextMainBlock01970(fileFormatOption)).write(os);
	}

	private SmetanaForGit getOrCreateSmetana(StringBounder stringBounder) {
		if (smetana == null)
			smetana = new SmetanaForGit(stringBounder, getSkinParam());

		return smetana;
	}

	@Override
	protected TextBlock getTextMainBlock01970(FileFormatOption fileFormatOption) {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				getOrCreateSmetana(ug.getStringBounder()).drawMe(ug, gnodes);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return getOrCreateSmetana(stringBounder).calculateDimension(gnodes);
			}

		};
	}

	@Override
	public TextBlock getTextBlock12026(int num, FileFormatOption fileFormatOption) {
		return getTextMainBlock01970(fileFormatOption);
	}


}
