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
 */
package net.sourceforge.plantuml.jungle;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.UDrawableUtils;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.LimitFinder;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class PSystemTree extends AbstractPSystem {

	private GNode root;
	private List<GNode> stack = new ArrayList<GNode>();
	private final Rendering rendering = Rendering.NEEDLE;

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Tree)");
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final int margin1;
		final int margin2;
		if (SkinParam.USE_STYLES()) {
			margin1 = SkinParam.zeroMargin(5);
			margin2 = SkinParam.zeroMargin(5);
		} else {
			margin1 = 5;
			margin2 = 5;
		}
		final ImageBuilder builder = ImageBuilder.buildB(new ColorMapperIdentity(), false, ClockwiseTopRightBottomLeft.margin1margin2((double) margin1, (double) margin2),
		null, null, null, 1.0, HColorUtils.WHITE);
		if (rendering == Rendering.NEEDLE) {
			final UDrawable tmp = Needle.getNeedle(root, 200, 0, 60);
			final LimitFinder limitFinder = new LimitFinder(fileFormat.getDefaultStringBounder(), true);
			tmp.drawU(limitFinder);
			final double minY = limitFinder.getMinY();
			builder.setUDrawable(UDrawableUtils.move(tmp, 0, -minY));
		} else {
			builder.setUDrawable(new GTileOneLevelFactory().createGTile(root));
		}
		return builder.writeImageTOBEMOVED(fileFormat, seed, os);
	}

	public CommandExecutionResult addParagraph(int level, String label) {

		if (level == 1 && root == null) {
			root = new GNode(Display.create(label));
			stack.add(root);
			return CommandExecutionResult.ok();
		} else if (level == 1 && root != null) {
			return CommandExecutionResult.error("Not allowed 1");
		}

		final GNode parent = stack.get(level - 2);
		final GNode newNode = parent.addChild(Display.create(label));

		if (level > stack.size() + 1) {
			return CommandExecutionResult.error("Not allowed 2");
		} else if (level - 1 == stack.size()) {
			stack.add(newNode);
		} else {
			stack.set(level - 1, newNode);
		}

		return CommandExecutionResult.ok();
	}

}
