/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.salt;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.salt.element.Element;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class PSystemSalt extends AbstractPSystem {

	private final List<String> data;

	@Deprecated
	public PSystemSalt(List<String> data) {
		this.data = data;
	}

	public PSystemSalt() {
		this(new ArrayList<String>());
	}

	public void add(String s) {
		data.add(s);
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat)
			throws IOException {
		try {
			final Element salt = SaltUtils.createElement(data);

			final Dimension2D size = salt.getPreferredDimension(fileFormat.getDefaultStringBounder(), 0, 0);
			final ImageBuilder builder = new ImageBuilder(new ColorMapperIdentity(), 1.0, HtmlColorUtils.WHITE, null,
					null, 5, 5, null, false);
			builder.setUDrawable(new UDrawable() {

				public void drawU(UGraphic ug) {
					ug = ug.apply(new UChangeColor(HtmlColorUtils.BLACK));
					salt.drawU(ug, 0, new Dimension2DDouble(size.getWidth(), size.getHeight()));
					salt.drawU(ug, 1, new Dimension2DDouble(size.getWidth(), size.getHeight()));
				}
			});
			return builder.writeImageTOBEMOVED(fileFormat, os);
		} catch (Exception e) {
			e.printStackTrace();
			UmlDiagram.exportDiagramError(os, e, fileFormat, getMetadata(), "none", new ArrayList<String>());
			return new ImageDataSimple();
		}
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Salt)");
	}

}
