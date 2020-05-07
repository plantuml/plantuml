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
package net.sourceforge.plantuml.cute;

import java.io.IOException;
import java.io.OutputStream;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;

public class PSystemCute extends AbstractPSystem {

	// private final List<Positionned> shapes = new ArrayList<Positionned>();
	// private final Map<String, Group> groups = new HashMap<String, Group>();
	private final Group root = Group.createRoot();
	private Group currentGroup = root;

	public PSystemCute() {
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Cute)");
	}

	public void doCommandLine(String line) {
		line = StringUtils.trin(line);
		if (line.length() == 0 || line.startsWith("'")) {
			return;
		}
		if (line.startsWith("group ")) {
			final StringTokenizer st = new StringTokenizer(line);
			st.nextToken();
			final String groupName = st.nextToken();
			currentGroup = currentGroup.createChild(groupName);
		} else if (line.startsWith("}")) {
			currentGroup = currentGroup.getParent();
		} else {
			final Positionned shape = new CuteShapeFactory(currentGroup.getChildren()).createCuteShapePositionned(line);
			// if (currentGroup == null) {
			// shapes.add(shape);
			// } else {
			currentGroup.add(shape);
			// }
		}
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final int margin1;
		final int margin2;
		if (SkinParam.USE_STYLES()) {
			margin1 = SkinParam.zeroMargin(10);
			margin2 = SkinParam.zeroMargin(10);
		} else {
			margin1 = 10;
			margin2 = 10;
		}
		final ImageBuilder builder = ImageBuilder.buildB(new ColorMapperIdentity(), false, ClockwiseTopRightBottomLeft.margin1margin2((double) margin1, (double) margin2),
		null, null, null, 1.0, null);
		builder.setUDrawable(root);
		return builder.writeImageTOBEMOVED(fileFormat, seed, os);
	}
}
