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
package net.sourceforge.plantuml.anim;

import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.ugraphic.MinMax;

public class AffineTransformation {

	static private final Pattern rotate = Pattern.compile("rotate\\s+(-?\\d+\\.?\\d*)");
	static private final Pattern shear = Pattern.compile("shear\\s+(-?\\d+\\.?\\d*)\\s+(-?\\d+\\.?\\d*)");
	static private final Pattern translate = Pattern.compile("translate\\s+(-?\\d+\\.?\\d*)\\s+(-?\\d+\\.?\\d*)");
	static private final Pattern scale = Pattern.compile("scale\\s+(-?\\d+\\.?\\d*)\\s+(-?\\d+\\.?\\d*)");
	static private final Pattern color = Pattern.compile("color\\s+.*");

	private final AffineTransform affineTransform;
	private Dimension2D dimension;

	private AffineTransformation(AffineTransform affineTransform) {
		this.affineTransform = affineTransform;
		if (affineTransform == null) {
			throw new IllegalArgumentException();
		}
	}

	private AffineTransformation compose(AffineTransformation other) {
		final AffineTransform tmp = new AffineTransform(this.affineTransform);
		tmp.concatenate(other.affineTransform);
		return new AffineTransformation(tmp);
	}

	public static AffineTransformation from(AffineTransform affineTransform) {
		return new AffineTransformation(affineTransform);
	}

	static AffineTransformation create(String value) {
		final StringTokenizer st = new StringTokenizer(value, "|");
		AffineTransformation result = null;
		while (st.hasMoreTokens()) {
			final String s = st.nextToken();
			final AffineTransformation tmp = createSimple(s);
			if (tmp != null) {
				if (result == null) {
					result = tmp;
				} else {
					result = result.compose(tmp);
				}
			}
		}
		return result;
	}

	private static AffineTransformation createSimple(String value) {
		Matcher m = rotate.matcher(StringUtils.trin(value));
		if (m.find()) {
			final double angle = Double.parseDouble(m.group(1));
			return new AffineTransformation(AffineTransform.getRotateInstance(angle * Math.PI / 180.0));
		}
		m = shear.matcher(value);
		if (m.find()) {
			final double shx = Double.parseDouble(m.group(1));
			final double shy = Double.parseDouble(m.group(2));
			return new AffineTransformation(AffineTransform.getShearInstance(shx, shy));
		}
		m = translate.matcher(value);
		if (m.find()) {
			final double tx = Double.parseDouble(m.group(1));
			final double ty = Double.parseDouble(m.group(2));
			return new AffineTransformation(AffineTransform.getTranslateInstance(tx, ty));
		}
		m = scale.matcher(value);
		if (m.find()) {
			final double scalex = Double.parseDouble(m.group(1));
			final double scaley = Double.parseDouble(m.group(2));
			return new AffineTransformation(AffineTransform.getScaleInstance(scalex, scaley));
		}
		m = color.matcher(value);
		if (m.find()) {
			return new AffineTransformation(new AffineTransform());
		}
		return null;
	}

	public final AffineTransform getAffineTransform() {
		return getAffineTransform(dimension);
	}

	private AffineTransform getAffineTransform(Dimension2D dimension) {
		if (dimension == null) {
			throw new IllegalStateException();
		}
		final AffineTransform at = AffineTransform.getTranslateInstance(dimension.getWidth() / 2,
				dimension.getHeight() / 2);
		at.concatenate(affineTransform);
		at.translate(-dimension.getWidth() / 2, -dimension.getHeight() / 2);

		return at;
	}

	public void setDimension(Dimension2D dim) {
		this.dimension = dim;

	}

	public MinMax getMinMax(Dimension2D rect) {
		MinMax result = MinMax.getEmpty(false);
		final AffineTransform tmp = getAffineTransform(rect);
		result = result.addPoint(tmp.transform(new Point2D.Double(0, 0), null));
		result = result.addPoint(tmp.transform(new Point2D.Double(0, rect.getHeight()), null));
		result = result.addPoint(tmp.transform(new Point2D.Double(rect.getWidth(), 0), null));
		result = result.addPoint(tmp.transform(new Point2D.Double(rect.getWidth(), rect.getHeight()), null));
		return result;
	}

}
