/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class MindMapDiagram extends UmlDiagram {

	private Branch left = new Branch();
	private Branch right = new Branch();

	private Direction defaultDirection = Direction.RIGHT;

	public final void setDefaultDirection(Direction defaultDirection) {
		this.defaultDirection = defaultDirection;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("MindMap");
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.MINDMAP;
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final Scale scale = getScale();

		final double dpiFactor = scale == null ? getScaleCoef(fileFormatOption) : scale.getScale(100, 100);
		final ISkinParam skinParam = getSkinParam();
		final int margin1;
		final int margin2;
		if (SkinParam.USE_STYLES()) {
			margin1 = SkinParam.zeroMargin(10);
			margin2 = SkinParam.zeroMargin(10);
		} else {
			margin1 = 10;
			margin2 = 10;
		}
		final ImageBuilder imageBuilder = ImageBuilder.buildB(skinParam.getColorMapper(), skinParam.handwritten(), ClockwiseTopRightBottomLeft.margin1margin2((double) margin1, (double) margin2),
		null, fileFormatOption.isWithMetadata() ? getMetadata() : null, "", dpiFactor, skinParam.getBackgroundColor(false));
		TextBlock result = getTextBlock();

		result = new AnnotatedWorker(this, skinParam, fileFormatOption.getDefaultStringBounder()).addAdd(result);
		imageBuilder.setUDrawable(result);

		return imageBuilder.writeImageTOBEMOVED(fileFormatOption, seed(), os);
	}

	private TextBlockBackcolored getTextBlock() {
		return new TextBlockBackcolored() {

			public void drawU(UGraphic ug) {
				drawMe(ug);
			}

			public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				return null;
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				computeFinger();
				final double y1 = right.finger == null ? 0 : right.finger.getFullThickness(stringBounder) / 2;
				final double y2 = left.finger == null ? 0 : left.finger.getFullThickness(stringBounder) / 2;
				final double y = Math.max(y1, y2);

				final double x = left.finger == null ? 0 : left.finger.getFullElongation(stringBounder);

				final double width = right.finger == null ? x : x + right.finger.getFullElongation(stringBounder);
				final double height = y
						+ Math.max(left.finger == null ? 0 : left.finger.getFullThickness(stringBounder) / 2,
								right.finger == null ? 0 : right.finger.getFullThickness(stringBounder) / 2);
				return new Dimension2DDouble(width, height);

			}

			public MinMax getMinMax(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}

			public HColor getBackcolor() {
				return null;
			}
		};
	}

	private void drawMe(UGraphic ug) {
		if (left.root == null && right.root == null) {
			return;
		}
		computeFinger();

		final StringBounder stringBounder = ug.getStringBounder();
		final double y1 = right.finger == null ? 0 : right.finger.getFullThickness(stringBounder) / 2;
		final double y2 = left.finger == null ? 0 : left.finger.getFullThickness(stringBounder) / 2;
		final double y = Math.max(y1, y2);

		final double x = left.finger == null ? 0
				: left.finger.getFullElongation(stringBounder) + ((FingerImpl) left.finger).getX12();
		if (right.finger != null) {
			right.finger.drawU(ug.apply(new UTranslate(x, y)));
		}
		if (left.finger != null) {
			left.finger.drawU(ug.apply(new UTranslate(x, y)));
		}
	}

	private void computeFinger() {
		if (left.finger == null && right.finger == null) {
			if (left.root.hasChildren()) {
				left.finger = FingerImpl.build(left.root, getSkinParam(), Direction.LEFT);
			}
			if (left.finger == null || right.root.hasChildren()) {
				right.finger = FingerImpl.build(right.root, getSkinParam(), Direction.RIGHT);
			}
			if (left.finger != null && right.finger != null) {
				left.finger.doNotDrawFirstPhalanx();
			}
		}
	}

	public CommandExecutionResult addIdea(HColor backColor, int level, Display label, IdeaShape shape) {
		return addIdea(backColor, level, label, shape, defaultDirection);
	}

	public CommandExecutionResult addIdea(HColor backColor, int level, Display label, IdeaShape shape,
			Direction direction) {
		String stereotype = label.getEndingStereotype();
		if (stereotype != null) {
			label = label.removeEndingStereotype();
		}
		return addIdeaInternal(stereotype, backColor, level, label, shape, direction);
	}

	public CommandExecutionResult addIdea(String stereotype, HColor backColor, int level, Display label,
			IdeaShape shape) {
		return addIdeaInternal(stereotype, backColor, level, label, shape, defaultDirection);
	}

	private CommandExecutionResult addIdeaInternal(String stereotype, HColor backColor, int level, Display label,
			IdeaShape shape, Direction direction) {
		if (level == 0) {
			if (this.right.root != null) {
				return CommandExecutionResult.error(
						"I don't know how to draw multi-root diagram. You should suggest an image so that the PlantUML team implements it :-)");
			}
			right.initRoot(getSkinParam().getCurrentStyleBuilder(), backColor, label, shape, stereotype);
			left.initRoot(getSkinParam().getCurrentStyleBuilder(), backColor, label, shape, stereotype);
			return CommandExecutionResult.ok();
		}
		if (direction == Direction.LEFT) {
			return left.add(getSkinParam().getCurrentStyleBuilder(), backColor, level, label, shape, stereotype);
		}
		return right.add(getSkinParam().getCurrentStyleBuilder(), backColor, level, label, shape, stereotype);
	}

	static class Branch {
		private Idea root;
		private Idea last;
		private Finger finger;

		private void initRoot(StyleBuilder styleBuilder, HColor backColor, Display label, IdeaShape shape,
				String stereotype) {
			root = new Idea(styleBuilder, backColor, label, shape, stereotype);
			last = root;
		}

		private Idea getParentOfLast(int nb) {
			Idea result = last;
			for (int i = 0; i < nb; i++) {
				result = result.getParent();
			}
			return result;
		}

		private CommandExecutionResult add(StyleBuilder styleBuilder, HColor backColor, int level, Display label,
				IdeaShape shape, String stereotype) {
			if (last == null) {
				return CommandExecutionResult.error("Check your indentation ?");
			}
			if (level == last.getLevel() + 1) {
				final Idea newIdea = last.createIdea(styleBuilder, backColor, level, label, shape, stereotype);
				last = newIdea;
				return CommandExecutionResult.ok();
			}
			if (level <= last.getLevel()) {
				final int diff = last.getLevel() - level + 1;
				final Idea newIdea = getParentOfLast(diff).createIdea(styleBuilder, backColor, level, label, shape,
						stereotype);
				last = newIdea;
				return CommandExecutionResult.ok();
			}
			return CommandExecutionResult.error("error42L");
		}

	}

}
