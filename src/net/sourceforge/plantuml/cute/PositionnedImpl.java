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

import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class PositionnedImpl implements Positionned {

	private final CuteShape cuteShape;
	private final HColor color;
	private final UTranslate position;
	private final RotationZoom rotationZoom;

	@Override
	public String toString() {
		return "Positionned " + position + " " + cuteShape;
	}

	public PositionnedImpl(CuteShape cuteShape, VarArgs args) {
		this.cuteShape = cuteShape;
		this.color = args.getAsColor("color");
		this.position = args.getPosition();
		this.rotationZoom = RotationZoom.fromVarArgs(args);
	}

	private PositionnedImpl(CuteShape cuteShape, HColor color, UTranslate position, RotationZoom rotationZoom) {
		this.cuteShape = cuteShape;
		this.color = color;
		this.position = position;
		this.rotationZoom = rotationZoom;
	}

	public PositionnedImpl(Group group, RotationZoom rotation) {
		this.cuteShape = group;
		this.color = HColorUtils.BLACK;
		this.position = new UTranslate();
		this.rotationZoom = rotation;
	}

	public PositionnedImpl(Group group, UTranslate translation) {
		this.cuteShape = group;
		this.color = HColorUtils.BLACK;
		this.position = translation;
		this.rotationZoom = RotationZoom.none();
	}

	private UGraphic applyColor(UGraphic ug) {
		return ug.apply(color.bg()).apply(color);

	}

	public void drawU(UGraphic ug) {
		ug = applyColor(ug);
		ug = ug.apply(position);
		final UDrawable tmp = rotationZoom.isNone() ? cuteShape : cuteShape.rotateZoom(rotationZoom);
		// System.err.println("rotationZoom=" + rotationZoom + " tmp=" + tmp);
		tmp.drawU(ug);
	}

	public Positionned rotateZoom(RotationZoom other) {
		return new PositionnedImpl(cuteShape, color, other.getUTranslate(position), rotationZoom.compose(other));
	}

	public Positionned translate(UTranslate other) {
		return new PositionnedImpl(cuteShape, color, position.compose(other), rotationZoom);
	}

}
