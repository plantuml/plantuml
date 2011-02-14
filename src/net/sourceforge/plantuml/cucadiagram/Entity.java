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
 * Revision $Revision: 6121 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.cucadiagram.dot.DrawFile;
import net.sourceforge.plantuml.graphic.HtmlColor;

public class Entity implements IEntity {

	private final String code;
	private String display;

	private final String uid;
	private EntityType type;

	private Stereotype stereotype;

	private final List<Member> fields2 = new ArrayList<Member>();
	private final List<Member> methods2 = new ArrayList<Member>();

	private Group container;

	private DrawFile imageFile;
	private String url;
	
	private boolean top;

	public final boolean isTop() {
		return top;
	}

	public final void setTop(boolean top) {
		this.top = top;
	}

	public Entity(String code, String display, EntityType type, Group entityPackage) {
		this("cl" + UniqueSequence.getValue(), code, display, type, entityPackage);
	}

	public Entity(String uid, String code, String display, EntityType type, Group entityPackage) {
		if (code == null || code.length() == 0) {
			throw new IllegalArgumentException();
		}
		if (display == null /* || display.length() == 0 */) {
			throw new IllegalArgumentException();
		}
		this.uid = uid;
		this.type = type;
		this.code = code;
		this.display = display;
		this.container = entityPackage;
		if (entityPackage != null && type != EntityType.GROUP) {
			entityPackage.addEntity(this);
		}
	}

	public void setEntityPackage(Group entityPackage) {
		if (entityPackage == null) {
			throw new IllegalArgumentException();
		}
		if (this.container != null) {
			throw new IllegalStateException();
		}
		this.container = entityPackage;
		entityPackage.addEntity(this);
	}

	public void addFieldOrMethod(String s) {
		if (isMethod(s)) {
			methods2.add(new Member(s, true));
		} else {
			addField(s);
		}
	}

	public void addField(String s) {
		fields2.add(new Member(s, false));
	}

	public void addField(Member s) {
		fields2.add(s);
	}

	private boolean isMethod(String s) {
		return s.contains("(") || s.contains(")");
	}

	public List<Member> methods2() {
		return Collections.unmodifiableList(methods2);
	}

	public List<Member> fields2() {
		return Collections.unmodifiableList(fields2);
	}

	public EntityType getType() {
		return type;
	}

	public void muteToType(EntityType newType) {
		if (type != EntityType.ABSTRACT_CLASS && type != EntityType.CLASS && type != EntityType.ENUM
				&& type != EntityType.INTERFACE) {
			throw new IllegalArgumentException("type=" + type);
		}
		if (newType != EntityType.ABSTRACT_CLASS && newType != EntityType.CLASS && newType != EntityType.ENUM
				&& newType != EntityType.INTERFACE) {
			throw new IllegalArgumentException("newtype=" + newType);
		}
		this.type = newType;
	}

	public String getCode() {
		return code;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getUid() {
		return uid;
	}

	public Stereotype getStereotype() {
		return stereotype;
	}

	public final void setStereotype(Stereotype stereotype) {
		this.stereotype = stereotype;
	}

	public final Group getParent() {
		return container;
	}

	@Override
	public String toString() {
		if (type == EntityType.GROUP) {
			return display + "(" + getType() + ")" + this.container;
		}
		return display + "(" + getType() + ")";
	}

	public void muteToCluster(Group newGroup) {
		if (type == EntityType.GROUP) {
			throw new IllegalStateException();
		}
		this.type = EntityType.GROUP;
		this.container = newGroup;
	}

	public void moveTo(Group dest) {
		this.container = dest;
		dest.addEntity(this);
	}

	public final DrawFile getImageFile() {
		return imageFile;
	}

	public final void setImageFile(DrawFile imageFile) {
		this.imageFile = imageFile;
	}

	private HtmlColor specificBackcolor;

	public HtmlColor getSpecificBackColor() {
		return specificBackcolor;
	}

	public void setSpecificBackcolor(String s) {
		this.specificBackcolor = HtmlColor.getColorIfValid(s);
	}

	public final String getUrl() {
		return url;
		// return "http://www.google.com";
	}

	public final void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		return uid.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		final IEntity other = (IEntity) obj;
		return uid.equals(other.getUid());
	}

	private final List<DrawFile> subImages = new ArrayList<DrawFile>();

	public void addSubImage(DrawFile subImage) {
		if (subImage == null) {
			throw new IllegalArgumentException();
		}
		subImages.add(subImage);
	}

	public DrawFile getImageFile(File searched) throws IOException {
		if (imageFile != null && imageFile.getPng().getCanonicalFile().equals(searched)) {
			return imageFile;
		}
		for (DrawFile f : subImages) {
			if (f.getPng().getCanonicalFile().equals(searched)) {
				return f;
			}
		}
		return null;
	}
}
