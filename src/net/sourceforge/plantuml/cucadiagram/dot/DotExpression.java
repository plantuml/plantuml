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
 * Revision $Revision: 5396 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.Color;
import java.awt.Font;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.graphic.FontChange;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.FontStyle;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlCommand;
import net.sourceforge.plantuml.graphic.Splitter;
import net.sourceforge.plantuml.graphic.Text;

final class DotExpression {

	private final StringBuilder sb = new StringBuilder();

	private final Font normalFont;

	private FontConfiguration fontConfiguration;

	private final boolean underline;

	private final String fontFamily;

	private final FileFormat fileFormat;

	DotExpression(String html, int defaultFontSize, HtmlColor color, String fontFamily, int style, FileFormat fileFormat) {
		this.fontFamily = fontFamily;
		this.normalFont = new Font("SansSerif", Font.PLAIN, defaultFontSize);
		this.fontConfiguration = new FontConfiguration(normalFont, color.getColor());
		this.fileFormat = fileFormat;

		if ((style & Font.ITALIC) != 0) {
			html = "<i>" + html;
		}

		if ((style & Font.BOLD) != 0) {
			html = "<b>" + html;
		}

		html = html.replaceAll(" \\<[uU]\\>", "<u>");
		html = html.replaceAll("\\</[uU]\\> ", "</u>");
		underline = html.contains("<u>") || html.contains("<U>");
		final Splitter splitter = new Splitter(html);
		for (HtmlCommand command : splitter.getHtmlCommands()) {
			if (command instanceof Text) {
				manage((Text) command);
			} else if (command instanceof FontChange) {
				manage((FontChange) command);
			} else {
				Log.error("Cannot manage " + command);
			}

		}
	}

	private void manage(FontChange command) {
		fontConfiguration = command.apply(fontConfiguration);
	}

	private void manage(Text command) {
		underline(false);
		sb.append(getFontTag());

		String text = command.getText();
		text = text.replace("<", "&lt;");
		text = text.replace(">", "&gt;");
		text = text.replace("\\n", "<BR/>");

		sb.append(text);
		sb.append("</FONT>");
		underline(true);
	}

	private String getFontTag() {
		final int size = fontConfiguration.getFont().getSize();
		final StringBuilder sb = new StringBuilder("<FONT POINT-SIZE=\"");
		sb.append(size);
		sb.append("\" ");
		appendFontWithFamily(sb);

		final Color col = fontConfiguration.getColor();
		sb.append(" COLOR=\"").append(HtmlColor.getAsHtml(col)).append("\"");
		sb.append(">");
		return sb.toString();
	}

	private void appendFontWithFamily(final StringBuilder sb) {
		String modifier = null;
		if (fontConfiguration.containsStyle(FontStyle.ITALIC)) {
			modifier = "italic";
		} else if (fontConfiguration.containsStyle(FontStyle.BOLD)) {
			modifier = "bold";
		}
		appendFace(sb, fontFamily, modifier);
	}

	private static void appendFace(StringBuilder sb, String fontFamily, String modifier) {
		if (fontFamily == null && modifier == null) {
			return;
		}
		sb.append("FACE=\"");
		if (fontFamily != null && modifier != null) {
			sb.append(fontFamily + " " + modifier);
		} else if (fontFamily != null) {
			assert modifier == null;
			sb.append(fontFamily);
		} else if (modifier != null) {
			assert fontFamily == null;
			sb.append(modifier);
		} else {
			assert false;
		}
		sb.append("\"");
	}

	private void underline(boolean closing) {
		if (fontConfiguration.containsStyle(FontStyle.UNDERLINE)) {
			String special = "_";
			if (closing && fileFormat == FileFormat.EPS) {
				special = "_!";
			}
			sb.append("<FONT COLOR=\"#FEFECF\">" + special + "</FONT>");
		}
	}

	public String getDotHtml() {
		return sb.toString();

	}

	boolean isUnderline() {
		return underline;
	}

}
