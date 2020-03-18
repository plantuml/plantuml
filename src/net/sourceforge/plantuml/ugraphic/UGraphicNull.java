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
package net.sourceforge.plantuml.ugraphic;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.EnsureVisible;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.TikzFontDistortion;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;

public class UGraphicNull extends AbstractUGraphic<String> implements EnsureVisible, UGraphic2 {

	@Override
	protected AbstractCommonUGraphic copyUGraphic() {
		return new UGraphicNull(this);
	}

	private UGraphicNull(UGraphicNull other) {
		super(other);
	}

	public UGraphicNull() {
		super(new ColorMapperIdentity(), "foo");
	}

	public StringBounder getStringBounder() {
		return FileFormat.PNG.getDefaultStringBounder(TikzFontDistortion.getDefault());
	}

	public void startUrl(Url url) {
	}

	public void closeAction() {
	}

	public void writeImageTOBEMOVED(OutputStream os, String metadata, int dpi) throws IOException {
	}

	public void ensureVisible(double x, double y) {
	}

}
