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
package net.sourceforge.plantuml.mindmap;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.Rankdir;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.SkinParam;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.utils.Direction;

public class MindMapDiagram extends UmlDiagram {

	private final List<MindMap> mindmaps = new ArrayList<>();

	private boolean defaultDirection = true;

	public final void setDefaultDirection(Direction direction) {
		this.defaultDirection = direction == Direction.RIGHT || direction == Direction.DOWN;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("MindMap");
	}

	public MindMapDiagram(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, UmlDiagramType.MINDMAP, null, preprocessing);
		((SkinParam) getSkinParam()).setRankdir(Rankdir.LEFT_TO_RIGHT);
		this.mindmaps.add(new MindMap(getSkinParam()));
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {

		return createImageBuilder(fileFormatOption).drawable(getTextMainBlock(fileFormatOption)).write(os);
	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				for (MindMap mindmap : mindmaps) {
					mindmap.drawU(ug);
					final XDimension2D dim = mindmap.calculateDimension(ug.getStringBounder());
					ug = ug.apply(UTranslate.dy(dim.getHeight()));
				}
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				double width = 0;
				double height = 0;
				for (MindMap mindmap : mindmaps) {
					final XDimension2D dim = mindmap.calculateDimension(stringBounder);
					height += dim.getHeight();
					width = Math.max(width, dim.getWidth());
				}
				return new XDimension2D(width, height);
			}

		};
	}

	public CommandExecutionResult addIdea(HColor backColor, int level, Display label, IdeaShape shape) {
		return addIdea(backColor, level, label, shape, defaultDirection);
	}

	private MindMap last() {
		return mindmaps.get(mindmaps.size() - 1);
	}

	public CommandExecutionResult addIdea(HColor backColor, int level, Display label, IdeaShape shape,
			boolean direction) {
		final Stereotype stereotype = label.getEndingStereotype();
		if (stereotype != null)
			label = label.removeEndingStereotype();

		if (last().isFull(level))
			this.mindmaps.add(new MindMap(getSkinParam()));

		return last().addIdeaInternal(stereotype, backColor, level, label, shape, direction);
	}

	public CommandExecutionResult addIdea(Stereotype stereotype, HColor backColor, int level, Display label,
			IdeaShape shape) {
		if (last().isFull(level))
			this.mindmaps.add(new MindMap(getSkinParam()));

		return last().addIdeaInternal(stereotype, backColor, level, label, shape, defaultDirection);
	}

	private String first;

	public int getSmartLevel(String type) {
		if (first == null)
			first = type;

		if (type.endsWith("**"))
			type = type.replace('\t', ' ').trim();

		type = type.replace('\t', ' ');
		if (type.contains(" ") == false)
			return type.length() - 1;

		if (type.endsWith(first))
			return type.length() - first.length();

		if (type.trim().length() == 1)
			return type.length() - 1;

		if (type.startsWith(first))
			return type.length() - first.length();

		throw new UnsupportedOperationException("type=<" + type + ">[" + first + "]");
	}

}
