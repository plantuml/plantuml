/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package net.sourceforge.plantuml.style;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.cucadiagram.StereotypeDecoration;

public class FromSkinparamToStyle {

	static class Data {
		final private SName[] styleNames;
		final private PName propertyName;

		Data(SName[] styleNames, PName propertyName) {
			this.propertyName = propertyName;
			this.styleNames = styleNames;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("[");
			for (SName s : styleNames) {
				sb.append(s.toString());
				sb.append(".");
			}
			sb.setLength(sb.length() - 1);
			sb.append("]");
			sb.append(propertyName.toString());
			return sb.toString();
		}
	}

	private static final Map<String, List<Data>> knowlegde = new HashMap<String, List<Data>>();

	static {
		addConvert("participantClickableBackgroundColor", PName.BackGroundColor, SName.participant, SName.clickable);
		addConvert("participantClickableBorderColor", PName.LineColor, SName.participant, SName.clickable);
		addMagic(SName.participant);

		addMagic(SName.boundary);
		addMagic(SName.control);
		addMagic(SName.collections);
		addMagic(SName.actor);
		addMagic(SName.database);
		addMagic(SName.entity);

		addConFont("header", SName.document, SName.header);
		addConFont("footer", SName.document, SName.footer);
		addConFont("caption", SName.document, SName.caption);

		addConvert("defaultFontSize", PName.FontSize, SName.element);

		addConvert("sequenceStereotypeFontSize", PName.FontSize, SName.stereotype);
		addConvert("sequenceStereotypeFontStyle", PName.FontStyle, SName.stereotype);
		addConvert("sequenceStereotypeFontColor", PName.FontColor, SName.stereotype);
		addConvert("sequenceStereotypeFontName", PName.FontName, SName.stereotype);
		addConvert("SequenceReferenceBorderColor", PName.LineColor, SName.reference);
		addConvert("SequenceReferenceBorderColor", PName.LineColor, SName.referenceHeader);
		addConvert("SequenceReferenceBackgroundColor", PName.BackGroundColor, SName.reference);
		addConvert("sequenceReferenceHeaderBackgroundColor", PName.BackGroundColor, SName.referenceHeader);
		addConFont("sequenceReference", SName.reference);
		addConFont("sequenceReference", SName.referenceHeader);
		addConvert("sequenceGroupBorderThickness", PName.LineThickness, SName.group);
		addConvert("SequenceGroupBorderColor", PName.LineColor, SName.group);
		addConvert("SequenceGroupBorderColor", PName.LineColor, SName.groupHeader);
		addConvert("SequenceGroupBackgroundColor", PName.BackGroundColor, SName.groupHeader);
		addConFont("SequenceGroup", SName.group);
		addConFont("SequenceGroupHeader", SName.groupHeader);
		addConvert("SequenceBoxBorderColor", PName.LineColor, SName.box);
		addConvert("SequenceBoxBackgroundColor", PName.BackGroundColor, SName.box);
		addConvert("SequenceBoxFontColor", PName.FontColor, SName.box);
		addConvert("SequenceLifeLineBorderColor", PName.LineColor, SName.lifeLine);
		addConvert("SequenceLifeLineBackgroundColor", PName.BackGroundColor, SName.lifeLine);
		addConvert("sequenceDividerBackgroundColor", PName.BackGroundColor, SName.separator);
		addConvert("sequenceDividerBorderColor", PName.LineColor, SName.separator);
		addConFont("sequenceDivider", SName.separator);
		addConvert("sequenceDividerBorderThickness", PName.LineThickness, SName.separator);
		addConvert("SequenceMessageAlignment", PName.HorizontalAlignment, SName.arrow);

		addConFont("note", SName.note);
		addConvert("noteBorderThickness", PName.LineThickness, SName.note);
		addConvert("noteBorderColor", PName.LineColor, SName.note);
		addConvert("noteBackgroundColor", PName.BackGroundColor, SName.note);

		addConvert("packageBackgroundColor", PName.BackGroundColor, SName.group);
		addConvert("packageBorderColor", PName.LineColor, SName.group);
		addMagic(SName.package_);

		addConvert("PartitionBorderColor", PName.LineColor, SName.composite);
		addConvert("PartitionBackgroundColor", PName.BackGroundColor, SName.composite);
		addConFont("Partition", SName.composite);

		addConvert("hyperlinkColor", PName.HyperLinkColor, SName.root);

		addConvert("activityStartColor", PName.BackGroundColor, SName.circle, SName.start);
		addConvert("activityEndColor", PName.LineColor, SName.circle, SName.end);
		addConvert("activityStopColor", PName.LineColor, SName.circle, SName.stop);
		addConvert("activityBarColor", PName.BackGroundColor, SName.activityBar);
		addConvert("activityBorderColor", PName.LineColor, SName.activity);
		addConvert("activityBorderThickness", PName.LineThickness, SName.activity);
		addConvert("activityBackgroundColor", PName.BackGroundColor, SName.activity);
		addConFont("activity", SName.activity);
		addConvert("activityDiamondBackgroundColor", PName.BackGroundColor, SName.diamond);
		addConvert("activityDiamondBorderColor", PName.LineColor, SName.diamond);
		addConFont("activityDiamond", SName.diamond);

		addConFont("arrow", SName.arrow);
		addConvert("arrowThickness", PName.LineThickness, SName.arrow);
		addConvert("arrowColor", PName.LineColor, SName.arrow);
		addConvert("arrowStyle", PName.LineStyle, SName.arrow);
		addConvert("arrowHeadColor", PName.HeadColor, SName.arrow);

		addConvert("defaulttextalignment", PName.HorizontalAlignment, SName.root);
		addConvert("defaultFontName", PName.FontName, SName.root);
		addConvert("defaultFontColor", PName.FontColor, SName.root);

		addConFont("SwimlaneTitle", SName.swimlane);
		addConvert("SwimlaneTitleBackgroundColor", PName.BackGroundColor, SName.swimlane);
		addConvert("SwimlaneBorderColor", PName.LineColor, SName.swimlane);
		addConvert("SwimlaneBorderThickness", PName.LineThickness, SName.swimlane);

		addConvert("roundCorner", PName.RoundCorner, SName.root);

		addConvert("titleBorderThickness", PName.LineThickness, SName.title);
		addConvert("titleBorderColor", PName.LineColor, SName.title);
		addConvert("titleBackgroundColor", PName.BackGroundColor, SName.title);
		addConvert("titleBorderRoundCorner", PName.RoundCorner, SName.title);
		addConFont("title", SName.document, SName.title);

		addConvert("legendBorderThickness", PName.LineThickness, SName.legend);
		addConvert("legendBorderColor", PName.LineColor, SName.legend);
		addConvert("legendBackgroundColor", PName.BackGroundColor, SName.legend);
		addConvert("legendBorderRoundCorner", PName.RoundCorner, SName.legend);
		addConFont("legend", SName.legend);

		addConvert("noteTextAlignment", PName.HorizontalAlignment, SName.note);

		addConvert("BackgroundColor", PName.BackGroundColor, SName.document);

		addConvert("classBackgroundColor", PName.BackGroundColor, SName.element, SName.class_);
		addConvert("classBorderColor", PName.LineColor, SName.element, SName.class_);
		addConFont("class", SName.element, SName.class_);
		addConFont("classAttribute", SName.element, SName.class_);
		addConvert("classBorderThickness", PName.LineThickness, SName.element, SName.class_);
		addConvert("classHeaderBackgroundColor", PName.BackGroundColor, SName.element, SName.class_, SName.header);

		addConvert("objectBackgroundColor", PName.BackGroundColor, SName.object);
		addConvert("objectBorderColor", PName.LineColor, SName.object);
		addConFont("object", SName.object);
		addConFont("objectAttribute", SName.object);
		addConvert("objectBorderThickness", PName.LineThickness, SName.object);

		addConvert("stateBackgroundColor", PName.BackGroundColor, SName.state);
		addConvert("stateBorderColor", PName.LineColor, SName.state);
		addConFont("state", SName.state);
		addConFont("stateAttribute", SName.state);
		addConvert("stateBorderThickness", PName.LineThickness, SName.state);

		addMagic(SName.agent);
		addMagic(SName.artifact);
		addMagic(SName.card);
		addMagic(SName.interface_);
		addMagic(SName.cloud);
		addMagic(SName.component);
		addMagic(SName.file);
		addMagic(SName.folder);
		addMagic(SName.frame);
		addMagic(SName.hexagon);
		addMagic(SName.node);
		addMagic(SName.person);
		addMagic(SName.queue);
		addMagic(SName.rectangle);
		addMagic(SName.stack);
		addMagic(SName.storage);
		addMagic(SName.usecase);
		addMagic(SName.map);
		addMagic(SName.archimate);

		addConvert("IconPrivateColor", PName.LineColor, SName.visibilityIcon, SName.private_);
		addConvert("IconPrivateBackgroundColor", PName.BackGroundColor, SName.visibilityIcon, SName.private_);
		addConvert("IconPackageColor", PName.LineColor, SName.visibilityIcon, SName.package_);
		addConvert("IconPackageBackgroundColor", PName.BackGroundColor, SName.visibilityIcon, SName.package_);
		addConvert("IconProtectedColor", PName.LineColor, SName.visibilityIcon, SName.protected_);
		addConvert("IconProtectedBackgroundColor", PName.BackGroundColor, SName.visibilityIcon, SName.protected_);
		addConvert("IconPublicColor", PName.LineColor, SName.visibilityIcon, SName.public_);
		addConvert("IconPublicBackgroundColor", PName.BackGroundColor, SName.visibilityIcon, SName.public_);

		addConvert("MinClassWidth", PName.MinimumWidth);

//		addConvert("nodeStereotypeFontSize", PName.FontSize, SName.node, SName.stereotype);
//		addConvert("sequenceStereotypeFontSize", PName.FontSize, SName.stereotype);
//		addConvert("sequenceStereotypeFontStyle", PName.FontStyle, SName.stereotype);
//		addConvert("sequenceStereotypeFontColor", PName.FontColor, SName.stereotype);
//		addConvert("sequenceStereotypeFontName", PName.FontName, SName.stereotype);

		addConvert("lifelineStrategy", PName.LineStyle, SName.lifeLine);
		addConvert("wrapWidth", PName.MaximumWidth, SName.element);
		addConvert("HyperlinkUnderline", PName.HyperlinkUnderlineThickness, SName.element);


	}

	private static void addMagic(SName sname) {
		final String cleanName = sname.name().replace("_", "");
		addConvert(cleanName + "BackgroundColor", PName.BackGroundColor, sname);
		addConvert(cleanName + "BorderColor", PName.LineColor, sname);
		addConvert(cleanName + "BorderThickness", PName.LineThickness, sname);
		addConvert(cleanName + "RoundCorner", PName.RoundCorner, sname);
		addConvert(cleanName + "DiagonalCorner", PName.DiagonalCorner, sname);
		addConvert(cleanName + "BorderStyle", PName.LineStyle, sname);
		addConFont(cleanName, sname);
		addConvert(cleanName + "Shadowing", PName.Shadowing, sname);

		addConvert(cleanName + "StereotypeFontSize", PName.FontSize, SName.stereotype, sname);
		addConvert(cleanName + "StereotypeFontStyle", PName.FontStyle, SName.stereotype, sname);
		addConvert(cleanName + "StereotypeFontColor", PName.FontColor, SName.stereotype, sname);
		addConvert(cleanName + "StereotypeFontName", PName.FontName, SName.stereotype, sname);

	}

	private final List<Style> styles = new ArrayList<>();
	private final String stereo;
	private final String key;

	public FromSkinparamToStyle(String key) {

		if (key.contains("<<")) {
			final StringTokenizer st = new StringTokenizer(key, "<>");
			this.key = st.nextToken();
			this.stereo = st.hasMoreTokens() ? st.nextToken().trim() : null;
		} else {
			this.key = key;
			this.stereo = null;
		}

	}

	public void convertNow(String value, final AutomaticCounter counter) {
		if (key.endsWith("shadowing")) {
			if (value.equalsIgnoreCase("false"))
				value = "0";
			else if (value.equalsIgnoreCase("true"))
				value = "3";
		} else if (key.equals("lifelinestrategy")) {
			if (value.equalsIgnoreCase("solid"))
				value = "0";
		} else if (key.equals("hyperlinkunderline")) {
			if (value.equalsIgnoreCase("false"))
				value = "0";
		}

		if (value.equalsIgnoreCase("right:right"))
			value = "right";
		if (value.equalsIgnoreCase("dotted"))
			value = "1;3";
		if (value.equalsIgnoreCase("dashed"))
			value = "7;7";

		final List<Data> datas = knowlegde.get(key.toLowerCase());

		if (datas == null) {
			if (key.equalsIgnoreCase("shadowing"))
				addStyle(PName.Shadowing, getShadowingValue(value, counter), SName.root);
			else if (key.equalsIgnoreCase("noteshadowing"))
				addStyle(PName.Shadowing, getShadowingValue(value, counter), SName.root, SName.note);
			return;
		}

		final boolean complex = isComplexValue(value);
		if (complex) {
			if (value.contains(";")) {
				if (value.startsWith(";"))
					value = " " + value;
				final StringTokenizer st = new StringTokenizer(value, ";");
				value = st.nextToken();
				while (st.hasMoreTokens()) {
					final String read = st.nextToken();
					readValue(read, datas, counter);
				}
			} else {
				readValue(value, datas, counter);
				return;
			}
		}

		if (" ".equals(value) == false)
			for (Data data : datas)
				addStyle(data.propertyName, ValueImpl.regular(value, counter), data.styleNames);

	}

	private void readValue(final String read, final List<Data> datas, final AutomaticCounter counter) {
		if (read.startsWith("text:")) {
			final String value2 = read.split(":")[1];
			for (Data data : datas)
				addStyle(PName.FontColor, ValueImpl.regular(value2, counter), data.styleNames);
		} else if (read.startsWith("line.dotted")) {
			for (Data data : datas)
				addStyle(PName.LineStyle, ValueImpl.regular("1;3", counter), data.styleNames);
		} else if (read.startsWith("line.dashed")) {
			for (Data data : datas)
				addStyle(PName.LineStyle, ValueImpl.regular("7;7", counter), data.styleNames);
		} else if (read.toLowerCase().contains("bold")) {
			for (Data data : datas)
				addStyle(PName.LineThickness, ValueImpl.regular("2", counter), data.styleNames);
		}
	}

	private boolean isComplexValue(String value) {
		if (value.contains(";"))
			return true;
		if (value.startsWith("text:"))
			return true;
		return false;
	}

	private ValueImpl getShadowingValue(final String value, final AutomaticCounter counter) {
		if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no"))
			return ValueImpl.regular("0", counter);

		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes"))
			return ValueImpl.regular("3", counter);

		return ValueImpl.regular(value, counter);
	}

	private void addStyle(PName propertyName, Value value, SName... styleNames) {
		Map<PName, Value> map = new EnumMap<PName, Value>(PName.class);
		map.put(propertyName, value);
		StyleSignatureBasic sig = StyleSignatureBasic.of(styleNames);
		if (stereo != null) {
			map = StyleLoader.addPriorityForStereotype(map);
			for (String s : stereo.split("\\&"))
				sig = sig.add(StereotypeDecoration.PREFIX + s);
		}

		final Style style = new Style(sig, map);
		styles.add(style);
	}

	public List<Style> getStyles() {
		return Collections.unmodifiableList(styles);
	}

	private static void addConvert(String skinparam, PName propertyName, SName... styleNames) {
		skinparam = skinparam.toLowerCase();
		List<Data> datas = knowlegde.get(skinparam);
		if (datas == null) {
			datas = new ArrayList<>();
			knowlegde.put(skinparam, datas);
		}
		datas.add(new Data(styleNames, propertyName));
	}

	private static void addConFont(String skinparam, SName... styleNames) {
		addConvert(skinparam + "FontSize", PName.FontSize, styleNames);
		addConvert(skinparam + "FontStyle", PName.FontStyle, styleNames);
		addConvert(skinparam + "FontColor", PName.FontColor, styleNames);
		addConvert(skinparam + "FontName", PName.FontName, styleNames);
	}

}
