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
package net.sourceforge.plantuml.png;

import static net.sourceforge.plantuml.utils.ImageIOUtils.createImageReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import net.sourceforge.plantuml.security.ImageIO;
import net.sourceforge.plantuml.security.SFile;

public class MetadataTag {

	private final Object source;
	private final String tag;

	public MetadataTag(SFile file, String tag) throws FileNotFoundException {
		this.source = file.conv();
		this.tag = tag;
	}

	public MetadataTag(java.io.File file, String tag) {
		this.source = file;
		this.tag = tag;
	}

	public MetadataTag(InputStream is, String tag) {
		this.source = is;
		this.tag = tag;
	}

	public String getData() throws IOException {
		final ImageReader reader = createImageReader(ImageIO.createImageInputStream(source));
		return findMetadataValue(reader.getImageMetadata(0), tag);
	}

	public static String findMetadataValue(IIOMetadata metadata, String tag) {
			final String[] names = metadata.getMetadataFormatNames();
			final int length = names.length;
			for (int i = 0; i < length; i++) {
				final String result = displayMetadata(metadata.getAsTree(names[i]), tag);
				if (result != null) {
					return result;
				}
			}

		return null;
	}

	private static String displayMetadata(Node root, String tag) {
		return displayMetadata(root, tag, 0);
	}

	private static String displayMetadata(Node node, String tag, int level) {
		final NamedNodeMap map = node.getAttributes();
		if (map != null) {
			final Node keyword = map.getNamedItem("keyword");
			if (keyword != null && tag.equals(keyword.getNodeValue())) {
				final Node text = map.getNamedItem("value");
				if (text != null) {
					return text.getNodeValue();
				}
			}
		}

		Node child = node.getFirstChild();

		// children, so close current tag
		while (child != null) {
			// print children recursively
			final String result = displayMetadata(child, tag, level + 1);
			if (result != null) {
				return result;
			}
			child = child.getNextSibling();
		}

		return null;

	}

}
