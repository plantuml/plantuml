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
package net.sourceforge.plantuml.style;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class FromSkinparamToStyle {

	static class Data {
		final private PName propertyName;
		final private SName[] styleNames;

		Data(PName propertyName, SName[] styleNames) {
			this.propertyName = propertyName;
			this.styleNames = styleNames;
		}
	}

	private static final Map<String, List<Data>> knowlegde = new HashMap<String, List<Data>>();

	static {
		addConvert("participantClickableBackgroundColor", PName.BackGroundColor, SName.participant, SName.clickable);
		addConvert("participantClickableBorderColor", PName.LineColor, SName.participant, SName.clickable);
		addConvert("participantBackgroundColor", PName.BackGroundColor, SName.participant);
		addConvert("participantBorderColor", PName.LineColor, SName.participant);
		addConvert("participantBorderThickness", PName.LineThickness, SName.participant);
		addConFont("participant", SName.participant);
		addConvert("boundaryBackgroundColor", PName.BackGroundColor, SName.boundary);
		addConvert("boundaryBorderColor", PName.LineColor, SName.boundary);
		addConvert("boundaryBorderThickness", PName.LineThickness, SName.boundary);
		addConFont("boundary", SName.boundary);
		addConvert("controlBackgroundColor", PName.BackGroundColor, SName.control);
		addConvert("controlBorderColor", PName.LineColor, SName.control);
		addConvert("controlBorderThickness", PName.LineThickness, SName.control);
		addConFont("control", SName.control);
		addConvert("collectionsBackgroundColor", PName.BackGroundColor, SName.collections);
		addConvert("collectionsBorderColor", PName.LineColor, SName.collections);
		addConvert("collectionsBorderThickness", PName.LineThickness, SName.collections);
		addConFont("collections", SName.collections);
		addConvert("actorBackgroundColor", PName.BackGroundColor, SName.actor);
		addConvert("actorBorderColor", PName.LineColor, SName.actor);
		addConvert("actorBorderThickness", PName.LineThickness, SName.actor);
		addConFont("actor", SName.actor);
		addConvert("databaseBackgroundColor", PName.BackGroundColor, SName.database);
		addConvert("databaseBorderColor", PName.LineColor, SName.database);
		addConvert("databaseBorderThickness", PName.LineThickness, SName.database);
		addConFont("database", SName.database);
		addConvert("entityBackgroundColor", PName.BackGroundColor, SName.entity);
		addConvert("entityBorderColor", PName.LineColor, SName.entity);
		addConvert("entityBorderThickness", PName.LineThickness, SName.entity);
		addConFont("entity", SName.entity);
		addConFont("footer", SName.footer);

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
		addConvert("SequenceLifeLineBorderColor", PName.LineColor, SName.lifeLine);
		addConvert("SequenceLifeLineBackgroundColor", PName.BackGroundColor, SName.lifeLine);
		addConvert("sequenceDividerBackgroundColor", PName.BackGroundColor, SName.separator);
		addConvert("sequenceDividerBorderColor", PName.LineColor, SName.separator);
		addConFont("sequenceDivider", SName.separator);
		addConvert("sequenceDividerBorderThickness", PName.LineThickness, SName.separator);
		addConvert("SequenceMessageAlignment", PName.HorizontalAlignment, SName.arrow);
		
		addConFont("note", SName.note);
		addConvert("noteBorderThickness", PName.LineThickness, SName.note);
		addConvert("noteBackgroundColor", PName.BackGroundColor, SName.note);
		addConvert("packageBackgroundColor", PName.BackGroundColor, SName.group);
		addConvert("packageBorderColor", PName.LineColor, SName.group);
		addConvert("PartitionBorderColor", PName.LineColor, SName.partition);
		addConvert("PartitionBackgroundColor", PName.BackGroundColor, SName.partition);
		addConFont("Partition", SName.partition);
		addConvert("hyperlinkColor", PName.HyperLinkColor, SName.root);
		addConvert("activityStartColor", PName.LineColor, SName.circle);
		addConvert("activityBarColor", PName.LineColor, SName.activityBar);
		addConvert("activityBorderColor", PName.LineColor, SName.activity);
		addConvert("activityBorderThickness", PName.LineThickness, SName.activity);
		addConvert("activityBackgroundColor", PName.BackGroundColor, SName.activity);
		addConFont("activity", SName.activity);
		addConvert("activityDiamondBackgroundColor", PName.BackGroundColor, SName.diamond);
		addConvert("activityDiamondBorderColor", PName.LineColor, SName.diamond);
		addConFont("activityDiamond", SName.diamond);
		addConvert("arrowColor", PName.LineColor, SName.arrow);
		
		addConFont("arrow", SName.arrow);
		addConvert("arrowThickness", PName.LineThickness, SName.arrow);
		addConvert("arrowColor", PName.LineColor, SName.arrow);
		addConvert("arrowStyle", PName.LineStyle, SName.arrow);

		addConvert("defaulttextalignment", PName.HorizontalAlignment, SName.root);
		addConvert("defaultFontName", PName.FontName, SName.root);
		addConFont("SwimlaneTitle", SName.swimlane);
		addConvert("SwimlaneTitleBackgroundColor", PName.BackGroundColor, SName.swimlane);
		addConvert("SwimlaneBorderColor", PName.LineColor, SName.swimlane);
		addConvert("SwimlaneBorderThickness", PName.LineThickness, SName.swimlane);
		addConvert("roundCorner", PName.RoundCorner, SName.root);
		addConvert("titleBorderThickness", PName.LineThickness, SName.title);
		addConvert("titleBorderColor", PName.LineColor, SName.title);
		addConvert("titleBackgroundColor", PName.BackGroundColor, SName.title);
		addConvert("titleBorderRoundCorner", PName.RoundCorner, SName.title);
		addConFont("title", SName.title);
		addConvert("legendBorderThickness", PName.LineThickness, SName.legend);
		addConvert("legendBorderColor", PName.LineColor, SName.legend);
		addConvert("legendBackgroundColor", PName.BackGroundColor, SName.legend);
		addConvert("legendBorderRoundCorner", PName.RoundCorner, SName.legend);
		addConFont("legend", SName.legend);
		addConvert("noteTextAlignment", PName.HorizontalAlignment, SName.note);

	}

	private final List<Style> styles = new ArrayList<Style>();
	private String stereo = null;

	public FromSkinparamToStyle(String key, String value, final AutomaticCounter counter) {
		if (value.equals("right:right")) {
			value = "right";
		}
		if (key.contains("<<")) {
			final StringTokenizer st = new StringTokenizer(key, "<>");
			key = st.nextToken();
			stereo = st.nextToken();
		}

		final List<Data> datas = knowlegde.get(key.toLowerCase());

		if (datas != null) {
			for (Data data : datas) {
				addStyle(data.propertyName, new ValueImpl(value, counter), data.styleNames);
			}
		} else if (key.equalsIgnoreCase("shadowing")) {
			addStyle(PName.Shadowing, getShadowingValue(value, counter), SName.root);
		} else if (key.equalsIgnoreCase("noteshadowing")) {
			addStyle(PName.Shadowing, getShadowingValue(value, counter), SName.root, SName.note);
		}
	}

	private ValueImpl getShadowingValue(final String value, final AutomaticCounter counter) {
		if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no")) {
			return new ValueImpl("0", counter);
		}
		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes")) {
			return new ValueImpl("3", counter);
		}
		return new ValueImpl(value, counter);
	}

	private void addStyle(PName propertyName, Value value, SName... styleNames) {
		final Map<PName, Value> map = new EnumMap<PName, Value>(PName.class);
		map.put(propertyName, value);
		StyleSignature sig = StyleSignature.of(styleNames);
		if (stereo != null) {
			sig = sig.add(stereo);
		}
		styles.add(new Style(sig, map));
	}

	public List<Style> getStyles() {
		return Collections.unmodifiableList(styles);
	}

	private static void addConvert(String skinparam, PName propertyName, SName... styleNames) {
		skinparam = skinparam.toLowerCase();
		List<Data> datas = knowlegde.get(skinparam);
		if (datas == null) {
			datas = new ArrayList<Data>();
			knowlegde.put(skinparam, datas);
		}
		datas.add(new Data(propertyName, styleNames));
	}

	private static void addConFont(String skinparam, SName... styleNames) {
		addConvert(skinparam + "FontSize", PName.FontSize, styleNames);
		addConvert(skinparam + "FontStyle", PName.FontStyle, styleNames);
		addConvert(skinparam + "FontColor", PName.FontColor, styleNames);
		addConvert(skinparam + "FontName", PName.FontName, styleNames);
	}

}