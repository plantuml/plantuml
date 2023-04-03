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
 * Creator:  Hisashi Miyashita
 *
 * 
 */

package net.sourceforge.plantuml.svek.image;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.EntityPosition;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.Rankdir;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.Cluster;
import net.sourceforge.plantuml.svek.ShapeType;

public abstract class AbstractEntityImageBorder extends AbstractEntityImage {
	public final EntityPosition entityPosition;
	protected final Cluster parent;
	protected final Bibliotekon bibliotekon;
	protected final Rankdir rankdir;

	protected abstract StyleSignatureBasic getSignature();

	final protected Style getStyle() {
		final Stereotype stereotype = getEntity().getStereotype();
		return getSignature().withTOBECHANGED(stereotype).getMergedStyle(getSkinParam().getCurrentStyleBuilder());
	}

	AbstractEntityImageBorder(Entity leaf, ISkinParam skinParam, Cluster parent, Bibliotekon bibliotekon,
			FontParam fontParam) {
		super(leaf, skinParam);

		this.parent = parent;
		this.bibliotekon = bibliotekon;
		this.entityPosition = leaf.getEntityPosition();
		this.rankdir = skinParam.getRankdir();

		if (entityPosition == EntityPosition.NORMAL)
			throw new IllegalArgumentException();
	}

	protected final TextBlock getDesc() {
		final Entity leaf = (Entity) getEntity();
		final FontConfiguration fc = FontConfiguration.create(getSkinParam(), getStyle());
		return leaf.getDisplay().create(fc, HorizontalAlignment.CENTER, getSkinParam());
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return entityPosition.getDimension(rankdir);
	}

	public ShapeType getShapeType() {
		return entityPosition.getShapeType();
	}

}
