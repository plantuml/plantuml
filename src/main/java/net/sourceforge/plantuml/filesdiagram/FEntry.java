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
 * Original Author:  Arnaud Roques
 *
 */
package net.sourceforge.plantuml.filesdiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.utils.MyCollections;

/**
 * One node of the tree: a folder, a file or a note. This is only the model;
 * drawing is done by {@link FilesListing} and {@link FilesTxt}.
 */
public class FEntry implements Iterable<FEntry> {

	private final FEntry parent;
	private final List<String> note;
	private final String name;
	private FilesType type;
	private final List<FEntry> children = new ArrayList<>();

	private String description;
	private List<String> stereotypes = Collections.emptyList();
	private String color;
	private String icon;

	public static FEntry createRoot() {
		return new FEntry(null, FilesType.FOLDER, "", null);
	}

	private FEntry(FEntry parent, FilesType type, String name, List<String> note) {
		this.parent = parent;
		this.note = note;
		this.name = name;
		this.type = type;
	}

	/**
	 * Adds a path relative to this entry: {@code a/b/c.txt} creates (or reuses)
	 * the folders {@code a} and {@code b}, then a file {@code c.txt}. A trailing
	 * {@code /} makes the last element a folder.
	 *
	 * @return the entry for the last element of the path
	 */
	public FEntry addRawEntry(String raw) {
		// A file that gets children is a folder after all
		this.type = FilesType.FOLDER;
		while (raw.startsWith("./"))
			raw = raw.substring(2);
		while (raw.startsWith("/"))
			raw = raw.substring(1);
		if (raw.length() == 0)
			return this;
		final int x = raw.indexOf('/');
		if (x == -1) {
			final FEntry result = new FEntry(this, FilesType.DATA, raw, null);
			children.add(result);
			return result;
		}
		final FEntry folder = getOrCreateFolder(raw.substring(0, x));
		final String remain = raw.substring(x + 1);
		if (remain.length() != 0)
			return folder.addRawEntry(remain);
		return folder;
	}

	public boolean isFolder() {
		return type == FilesType.FOLDER;
	}

	public boolean isNote() {
		return type == FilesType.NOTE;
	}

	public boolean isRoot() {
		return parent == null;
	}

	void forceFolder() {
		if (type == FilesType.DATA)
			type = FilesType.FOLDER;
	}

	public void addNote(List<String> note) {
		final FEntry result = new FEntry(this, FilesType.NOTE, "NONE", note);
		children.add(result);
	}

	private FEntry getOrCreateFolder(String folderName) {
		for (FEntry child : children)
			if (child.type != FilesType.NOTE && child.getName().equals(folderName)) {
				child.type = FilesType.FOLDER;
				return child;
			}

		final FEntry result = new FEntry(this, FilesType.FOLDER, folderName, null);
		children.add(result);
		return result;
	}

	void applyLabel(FLabel label) {
		if (label.getDescription() != null)
			this.description = label.getDescription();
		if (label.getStereotypes().size() > 0) {
			final List<String> tmp = new ArrayList<>(this.stereotypes);
			tmp.addAll(label.getStereotypes());
			this.stereotypes = Collections.unmodifiableList(tmp);
		}
		if (label.getColor() != null)
			this.color = label.getColor();
		if (label.getIcon() != null)
			this.icon = label.getIcon();
	}

	@Override
	public Iterator<FEntry> iterator() {
		return MyCollections.unmodifiableCollection(children).iterator();
	}

	public List<FEntry> getChildren() {
		return Collections.unmodifiableList(children);
	}

	/**
	 * The last child that is not a note: the one where the vertical connector
	 * line stops.
	 */
	public FEntry getLastEntryChild() {
		for (int i = children.size() - 1; i >= 0; i--)
			if (children.get(i).isNote() == false)
				return children.get(i);
		return null;
	}

	public FEntry getParent() {
		return parent;
	}

	public String getName() {
		return name;
	}

	public List<String> getNote() {
		return note;
	}

	public String getDescription() {
		return description;
	}

	public List<String> getStereotypes() {
		return stereotypes;
	}

	public String getColor() {
		return color;
	}

	public String getIcon() {
		return icon;
	}

}
