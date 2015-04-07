/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * Revision $Revision: 6170 $
 *
 */
package net.sourceforge.plantuml.anim;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.ugraphic.MinMax;

public class Animation {

	private final List<AffineTransformation> all;

	private Animation(List<AffineTransformation> all) {
		if (all.size() == 0) {
			throw new IllegalArgumentException();
		}
		this.all = all;
	}

	public static Animation singleton(AffineTransformation affineTransformation) {
		if (affineTransformation == null) {
			return null;
		}
		return new Animation(Collections.singletonList(affineTransformation));
	}

	public static Animation create(List<String> descriptions) {
		final List<AffineTransformation> all = new ArrayList<AffineTransformation>();
		for (String s : descriptions) {
			final AffineTransformation tmp = AffineTransformation.create(s);
			if (tmp != null) {
				all.add(tmp);
			}
		}
		return new Animation(all);
	}

	public Collection<AffineTransformation> getAll() {
		return Collections.unmodifiableCollection(all);
	}

	public void setDimension(Dimension2D dim) {
		for (AffineTransformation affineTransform : all) {
			affineTransform.setDimension(dim);
		}

	}

	public AffineTransformation getFirst() {
		return all.get(0);
	}

	public MinMax getMinMax(Dimension2D dim) {
		MinMax result = MinMax.getEmpty(false);
		for (AffineTransformation affineTransform : all) {
			final MinMax m = affineTransform.getMinMax(dim);
			result = result.addMinMax(m);
		}
		return result;
	}

}
