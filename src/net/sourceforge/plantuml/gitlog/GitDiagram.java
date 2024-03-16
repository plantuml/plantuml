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
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.skin.UmlDiagramType;

public class GitDiagram extends UmlDiagram {

	private final Collection<GNode> gnodes;

	public GitDiagram(UmlSource source, GitTextArea textArea) {
		super(source, UmlDiagramType.GIT, null);
		this.gnodes = new GNodeBuilder(textArea.getAllCommits()).getAllNodes();
		new GNodeBuilder(textArea.getAllCommits());
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Git)");
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {

		return createImageBuilder(fileFormatOption).drawable(getTextMainBlock(fileFormatOption)).write(os);
	}

	private void drawInternal(UGraphic ug) {

		new SmetanaForGit(ug, getSkinParam()).drawMe(gnodes);

//		final Display display = Display.getWithNewlines("Your data does not sound like GIT data");
//		final FontConfiguration fontConfiguration = FontConfiguration.blackBlueTrue(UFont.courier(14));
//		TextBlock result = display.create(fontConfiguration, HorizontalAlignment.LEFT, getSkinParam());
//		result = TextBlockUtils.withMargin(result, 5, 2);
//		result.drawU(ug);

	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				drawInternal(ug);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return null;
			}

		};
	}

}
