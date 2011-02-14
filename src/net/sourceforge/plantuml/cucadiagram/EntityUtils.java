/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 4749 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.dot.DrawFile;
import net.sourceforge.plantuml.graphic.HtmlColor;

public abstract class EntityUtils {

	private static IEntity withNoParent(final IEntity ent) {
		if (ent.getType() == EntityType.GROUP) {
			throw new IllegalArgumentException();
		}
		return new IEntity() {
			public List<Member> fields2() {
				return ent.fields2();
			}

			public String getDisplay() {
				return ent.getDisplay();
			}

			public Group getParent() {
				return null;
			}

			public Stereotype getStereotype() {
				return ent.getStereotype();
			}

			public void setStereotype(Stereotype stereotype) {
				ent.setStereotype(stereotype);
			}

			public EntityType getType() {
				return ent.getType();
			}

			public String getUid() {
				return ent.getUid();
			}

			public String getUrl() {
				return ent.getUrl();
			}

			public List<Member> methods2() {
				return ent.methods2();
			}

			public DrawFile getImageFile() {
				return ent.getImageFile();
			}

			public HtmlColor getSpecificBackColor() {
				return ent.getSpecificBackColor();
			}

			public void setSpecificBackcolor(String specificBackcolor) {
				throw new UnsupportedOperationException();
			}

			@Override
			public int hashCode() {
				return ent.hashCode();
			}

			@Override
			public boolean equals(Object obj) {
				return ent.equals(obj);
			}

			@Override
			public String toString() {
				return "NoParent " + ent.toString();
			}

			public String getCode() {
				return ent.getCode();
			}

			public DrawFile getImageFile(File searched) throws IOException {
				return ent.getImageFile(searched);
			}

			public boolean isTop() {
				return ent.isTop();
			}

			public void setTop(boolean top) {
				ent.setTop(top);
			}

		};
	}

}
