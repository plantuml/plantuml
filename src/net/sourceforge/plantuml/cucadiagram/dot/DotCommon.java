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
 * Revision $Revision: 6295 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.ColorMapper;

abstract class DotCommon {

	private final DotData data;
	private final FileFormat fileFormat;
	private boolean underline;

	private final Rose rose = new Rose();

	DotCommon(FileFormat fileFormat, DotData data) {
		this.fileFormat = fileFormat;
		this.data = data;
	}

	protected final Stereotype getStereotype(IEntity entity) {
		if (data.showPortion(EntityPortion.STEREOTYPE, entity) == false) {
			return null;
		}
		return entity.getStereotype();
	}

	protected final ColorMapper getColorMapper() {
		return data.getColorMapper();
	}

	protected final boolean isThereLabel(final Stereotype stereotype) {
		return stereotype != null && stereotype.getLabel() != null;
	}

	protected final double getMagicFactorForImageDpi() {
		return 10500 / 100000.0;
	}

	protected final void appendLabelAndStereotype(IEntity entity, final StringBuilder sb, boolean classes) {
		final Stereotype stereotype = getStereotype(entity);
		final String stereo = entity.getStereotype() == null ? null : entity.getStereotype().getLabel();
		if (isThereLabel(stereotype)) {
			sb.append("<BR ALIGN=\"LEFT\"/>");
			for (String st : stereotype.getLabels()) {
				sb.append(manageHtmlIB(st, classes ? FontParam.CLASS_STEREOTYPE : FontParam.OBJECT_STEREOTYPE, stereo));
				sb.append("<BR/>");
			}
		}
		String display = StringUtils.getMergedLines(entity.getDisplay2());
		final boolean italic = entity.getType() == EntityType.ABSTRACT_CLASS
				|| entity.getType() == EntityType.INTERFACE;
		if (italic) {
			display = "<i>" + display;
		}
		sb.append(manageHtmlIB(display, classes ? FontParam.CLASS : FontParam.OBJECT, stereo));
	}

	protected final String manageHtmlIB(String s, FontParam param, String stereotype) {
		s = unicode(s);
		final int fontSize = data.getSkinParam().getFont(param, stereotype).getSize();
		final int style = data.getSkinParam().getFont(param, stereotype).getStyle();
		final String fontFamily = data.getSkinParam().getFont(param, stereotype).getFamily(null);
		final DotExpression dotExpression = new DotExpression(getColorMapper(), s, fontSize, getFontHtmlColor(param,
				stereotype), fontFamily, style, fileFormat);
		final String result = dotExpression.getDotHtml();
		if (dotExpression.isUnderline()) {
			underline = true;
		}
		return result;
	}

	protected final HtmlColor getFontHtmlColor(FontParam fontParam, String stereotype) {
		return data.getSkinParam().getFontHtmlColor(fontParam, stereotype);
	}

	static String unicode(String s) {
		final StringBuilder result = new StringBuilder();
		for (char c : s.toCharArray()) {
			if (c > 127 || c == '&' || c == '|') {
				// if (c > 127 || c == '&') {
				final int i = c;
				result.append("&#" + i + ";");
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}

	protected final void addTdImageBugB1983(final StringBuilder sb, final String absolutePath) throws IOException {
		// http://www.graphviz.org/bugs/b1983.html
		final BufferedImage im = ImageIO.read(new File(absolutePath));
		final int height = im.getHeight();
		final int width = im.getWidth();
		final double f = 1.0 / data.getDpiFactor();
		final int w = (int) (width * f);
		final int h = (int) (height * f);
		final int w2 = (int) (width * getMagicFactorForImageDpi());
		final int h2 = (int) (height * getMagicFactorForImageDpi());
		sb.append(getTdHeaderForDpi(w, h));
		sb.append("<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\">");
		sb.append("<TR>");
		sb.append(getTdHeaderForDpi(w2, h2));
		sb.append("<IMG SCALE=\"TRUE\" SRC=\"" + absolutePath + "\"/>");
		sb.append("</TD>");
		sb.append("</TR>");
		sb.append("</TABLE>");
		sb.append("</TD>");
	}

	protected final String getTdHeaderForDpi(final double w, final double h) {
		// return "<TD BGCOLOR=\"#000000\" FIXEDSIZE=\"TRUE\" WIDTH=\"" + w +
		// "\" HEIGHT=\"" + h + "\">";
		return "<TD FIXEDSIZE=\"TRUE\" WIDTH=\"" + w + "\" HEIGHT=\"" + h + "\">";
	}

	public final boolean isUnderline() {
		return underline;
	}

	protected final DotData getData() {
		return data;
	}

	protected final FileFormat getFileFormat() {
		return fileFormat;
	}

	protected final void setUnderline(boolean underline) {
		this.underline = underline;
	}

	protected final int getLongestMethods(IEntity entity) {
		int result = 0;
		for (Member att : entity.getMethodsToDisplay()) {
			final int size = att.getDisplayWithVisibilityChar().length();
			if (size > result) {
				result = size;
			}
		}
		return result;

	}

	protected final int getLongestField(IEntity entity) {
		int result = 0;
		for (Member att : entity.getFieldsToDisplay()) {
			final int size = att.getDisplayWithVisibilityChar().length();
			if (size > result) {
				result = size;
			}
		}
		return result;
	}

	protected final String getWitdh55() {
		if (getData().getDpi() == 96) {
			return "WIDTH=\"55\"";
		}
		return "WIDTH=\"55\"";
	}

	protected final int computeSpring(final int current, final int maximum, int maxSpring) {
		if (maximum <= current) {
			return 0;
		}
		final int spring = maximum - current;
		if (spring > maxSpring) {
			return maxSpring;
		}
		return spring;
	}

	protected final int getLonguestHeader(IEntity entity) {
		int result = StringUtils.getMergedLines(entity.getDisplay2()).length();
		final Stereotype stereotype = getStereotype(entity);
		if (isThereLabel(stereotype)) {
			final int size = stereotype.getLabel().length();
			if (size > result) {
				result = size;
			}
		}
		return result;
	}

	protected final String getColorString(ColorParam colorParam, String stereotype) {
		return "\"" + getAsHtml(rose.getHtmlColor(getData().getSkinParam(), colorParam, stereotype)) + "\"";
	}

	protected final String getAsHtml(HtmlColor htmlColor) {
		return StringUtils.getAsHtml(getColorMapper().getMappedColor(htmlColor));
	}

	protected final int getLongestFieldOrAttribute(IEntity entity) {
		return Math.max(getLongestField(entity), getLongestMethods(entity));
	}

	protected final boolean hasStatic(Collection<Member> attributes) {
		for (Member att : attributes) {
			if (att.isStatic()) {
				return true;
			}
		}
		return false;
	}

	protected final String manageHtmlIBspecial(Member att, FontParam param, boolean hasStatic, String backColor,
			boolean withVisibilityChar) {
		String prefix = "";
		if (hasStatic) {
			prefix = "<FONT COLOR=" + backColor + ">_</FONT>";
		}
		if (att.isAbstract()) {
			return prefix + manageHtmlIB("<i>" + att.getDisplay(withVisibilityChar), param, null);
		}
		if (att.isStatic()) {
			return manageHtmlIB("<u>" + att.getDisplay(withVisibilityChar), param, null);
		}
		return prefix + manageHtmlIB(att.getDisplay(withVisibilityChar), param, null);
	}

	final protected String getBackColorAroundEntity(IEntity entity) {
		String backColor = getSpecificBackColor(entity);
		if (backColor == null) {
			backColor = getColorString(ColorParam.background, null);
		}
		return backColor;
	}

	private String getSpecificBackColor(IEntity entity) {
		final Group parent = entity.getParent();
		if (parent == null) {
			return null;
		}
		if (parent.getBackColor() == null) {
			return null;
		}
		return "\"" + getAsHtml(parent.getBackColor()) + "\"";
	}

	final protected void appendImageAsTD(StringBuilder sb, String circleAbsolutePath) throws IOException {
		if (circleAbsolutePath.endsWith(".png")) {
			if (getData().getDpi() == 96) {
				final BufferedImage im = ImageIO.read(new File(circleAbsolutePath));
				final int height = im.getHeight();
				final int width = im.getWidth();
				sb.append("<TD FIXEDSIZE=\"TRUE\" WIDTH=\"" + width + "\" HEIGHT=\"" + height + "\"><IMG SRC=\""
						+ circleAbsolutePath + "\"/></TD>");
			} else {
				addTdImageBugB1983(sb, circleAbsolutePath);
			}
		} else if (circleAbsolutePath.endsWith(".eps")) {
			sb.append("<TD><IMG SRC=\"" + circleAbsolutePath + "\"/></TD>");
		}
	}

}
