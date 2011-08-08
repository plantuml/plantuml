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
 * Revision $Revision: 6932 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.graphic.FontChange;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.FontStyle;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlCommand;
import net.sourceforge.plantuml.graphic.Img;
import net.sourceforge.plantuml.graphic.Splitter;
import net.sourceforge.plantuml.graphic.Text;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UFont;

public final class DotExpression {

	private final StringBuilder sb = new StringBuilder();

	private final UFont normalFont;

	private FontConfiguration fontConfiguration;

	private final boolean underline;

	private final String fontFamily;

	private final FileFormat fileFormat;

	private final ColorMapper colorMapper;

	private boolean hasImg;

	public DotExpression(ColorMapper colorMapper, String html, int defaultFontSize, HtmlColor color, String fontFamily,
			int style, FileFormat fileFormat) {
		if (html.contains("\n")) {
			throw new IllegalArgumentException(html);
		}
		this.colorMapper = colorMapper;
		this.fontFamily = fontFamily;
		this.normalFont = new UFont("SansSerif", Font.PLAIN, defaultFontSize);
		this.fontConfiguration = new FontConfiguration(normalFont, color);
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
		List<HtmlCommand> htmlCommands = splitter.getHtmlCommands(false);
		for (HtmlCommand command : htmlCommands) {
			if (command instanceof Img) {
				hasImg = true;
			}
		}
		if (hasImg) {
			htmlCommands = splitter.getHtmlCommands(true);
			sb.append("<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLPADDING=\"0\" CELLSPACING=\"0\">");
			for (Collection<HtmlCommand> cmds : split(htmlCommands)) {
				sb.append("<TR><TD><TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLPADDING=\"0\" CELLSPACING=\"0\"><TR>");
				manageCommands(cmds);
				sb.append("</TR></TABLE></TD></TR>");
			}
			sb.append("</TABLE>");
		} else {
			manageCommands(htmlCommands);
		}
	}

	private static List<Collection<HtmlCommand>> split(Collection<HtmlCommand> all) {
		final List<Collection<HtmlCommand>> result = new ArrayList<Collection<HtmlCommand>>();
		Collection<HtmlCommand> current = null;
		for (HtmlCommand c : all) {
			if (c instanceof Text && ((Text) c).isNewline()) {
				current = null;
			} else {
				if (current == null) {
					current = new ArrayList<HtmlCommand>();
					result.add(current);
				}
				current.add(c);
			}
		}
		return result;
	}

	private void manageCommands(Collection<HtmlCommand> htmlCommands) {
		for (HtmlCommand command : htmlCommands) {
			if (command instanceof Text) {
				manage((Text) command);
			} else if (command instanceof FontChange) {
				manage((FontChange) command);
			} else if (command instanceof Img) {
				manageImage((Img) command);
			} else {
				Log.error("Cannot manage " + command);
			}
		}
	}

	private void manageImage(Img img) {
		try {
			final File f = FileSystem.getInstance().getFile(img.getFilePath());
			if (f.exists() == false) {
				throw new IOException();
			}
			sb.append("<TD><IMG SRC=\"" + f.getAbsolutePath() + "\"/></TD>");
		} catch (IOException e) {
			sb.append("<TD>File Not Found</TD>");
		}
	}

	private void manage(FontChange command) {
		fontConfiguration = command.apply(fontConfiguration);
	}

	private void manage(Text command) {
		if (hasImg) {
			sb.append("<TD>");
		}
		underline(false);
		sb.append(getFontTag());

		String text = command.getText();
		text = text.replace("<", "&lt;");
		text = text.replace(">", "&gt;");

		if (hasImg == false) {
			text = text.replace("\\n", "<BR/>");
		}

		sb.append(text);
		sb.append("</FONT>");
		underline(true);
		if (hasImg) {
			sb.append("</TD>");
			if (text.contains("\\n")) {
				throw new IllegalStateException();
			}
		}
	}

	private String getFontTag() {
		final int size = fontConfiguration.getFont().getSize();
		final StringBuilder sb = new StringBuilder("<FONT POINT-SIZE=\"");
		sb.append(size);
		sb.append("\" ");
		appendFontWithFamily(sb);

		final HtmlColor col = fontConfiguration.getColor();
		sb.append(" COLOR=\"").append(StringUtils.getAsHtml(colorMapper.getMappedColor(col))).append("\"");
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
