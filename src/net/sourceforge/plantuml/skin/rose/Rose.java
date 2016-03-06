/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 19109 $
 *
 */
package net.sourceforge.plantuml.skin.rose;

import net.sourceforge.plantuml.AlignParam;
import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class Rose implements Skin {

	final private double paddingX = 5;
	final private double paddingY = 5;

	public HtmlColor getFontColor(ISkinParam skin, FontParam fontParam) {
		return skin.getFontHtmlColor(null, fontParam);
	}

	public HtmlColor getHtmlColor(ISkinParam param, ColorParam color) {
		return getHtmlColor(param, color, null);
	}

	public HtmlColor getHtmlColor(ISkinParam param, ColorParam color, Stereotype stereotype) {
		HtmlColor result = param.getHtmlColor(color, stereotype, false);
		if (result == null) {
			result = color.getDefaultValue();
			if (result == null) {
				throw new IllegalArgumentException();
			}
		}
		return result;
	}

	private FontConfiguration getUFont2(ISkinParam skinParam, FontParam fontParam) {
		return new FontConfiguration(skinParam, fontParam, null);
	}

	public Component createComponent(ComponentType type, ArrowConfiguration config, ISkinParam param,
			Display stringsToDisplay) {
		final UFont fontGrouping = param.getFont(null, false, FontParam.SEQUENCE_GROUP);

		final UFont newFontForStereotype = param.getFont(null, false, FontParam.SEQUENCE_STEREOTYPE);

		if (type.isArrow()) {
			// if (param.maxMessageSize() > 0) {
			// final FontConfiguration fc = new FontConfiguration(fontArrow, HtmlColorUtils.BLACK);
			// stringsToDisplay = DisplayUtils.breakLines(stringsToDisplay, fc, param, param.maxMessageSize());
			// }
			final HtmlColor sequenceArrow = config.getColor() == null ? getHtmlColor(param, ColorParam.sequenceArrow)
					: config.getColor();
			if (config.getArrowDirection() == ArrowDirection.SELF) {
				return new ComponentRoseSelfArrow(sequenceArrow, getUFont2(param, FontParam.SEQUENCE_ARROW),
						stringsToDisplay, config, param, param.maxMessageSize(), param.strictUmlStyle() == false);
			}
			final HorizontalAlignment messageHorizontalAlignment = param
					.getHorizontalAlignment(AlignParam.SEQUENCE_MESSAGE_ALIGN);
			final HorizontalAlignment textHorizontalAlignment = param
					.getHorizontalAlignment(AlignParam.SEQUENCE_MESSAGETEXT_ALIGN);
			return new ComponentRoseArrow(sequenceArrow, getUFont2(param, FontParam.SEQUENCE_ARROW), stringsToDisplay,
					config, messageHorizontalAlignment, param, textHorizontalAlignment, param.maxMessageSize(),
					param.strictUmlStyle() == false);
		}
		if (type == ComponentType.PARTICIPANT_HEAD) {
			return new ComponentRoseParticipant(getSymbolContext(param, ColorParam.participantBorder), getUFont2(param,
					FontParam.PARTICIPANT), stringsToDisplay, param, param.getRoundCorner(), newFontForStereotype,
					getFontColor(param, FontParam.SEQUENCE_STEREOTYPE));
		}
		if (type == ComponentType.PARTICIPANT_TAIL) {
			return new ComponentRoseParticipant(getSymbolContext(param, ColorParam.participantBorder), getUFont2(param,
					FontParam.PARTICIPANT), stringsToDisplay, param, param.getRoundCorner(), newFontForStereotype,
					getFontColor(param, FontParam.SEQUENCE_STEREOTYPE));
		}
		if (type == ComponentType.PARTICIPANT_LINE) {
			final HtmlColor borderColor = getHtmlColor(param, ColorParam.sequenceLifeLineBorder);
			return new ComponentRoseLine(borderColor, false, getStroke(param, LineParam.sequenceLifeLineBorder, 1));
		}
		if (type == ComponentType.CONTINUE_LINE) {
			final HtmlColor borderColor = getHtmlColor(param, ColorParam.sequenceLifeLineBorder);
			return new ComponentRoseLine(borderColor, true, getStroke(param, LineParam.sequenceLifeLineBorder, 1.5));
		}
		if (type == ComponentType.ACTOR_HEAD) {
			return new ComponentRoseActor(getSymbolContext(param, ColorParam.actorBorder), getUFont2(param,
					FontParam.ACTOR), stringsToDisplay, true, param, newFontForStereotype, getFontColor(param,
					FontParam.SEQUENCE_STEREOTYPE));
		}
		if (type == ComponentType.ACTOR_TAIL) {
			return new ComponentRoseActor(getSymbolContext(param, ColorParam.actorBorder), getUFont2(param,
					FontParam.ACTOR), stringsToDisplay, false, param, newFontForStereotype, getFontColor(param,
					FontParam.SEQUENCE_STEREOTYPE));
		}
		if (type == ComponentType.BOUNDARY_HEAD) {
			return new ComponentRoseBoundary(getSymbolContext(param, ColorParam.actorBorder), getUFont2(param,
					FontParam.ACTOR), stringsToDisplay, true, param, newFontForStereotype, getFontColor(param,
					FontParam.SEQUENCE_STEREOTYPE));
		}
		if (type == ComponentType.BOUNDARY_TAIL) {
			return new ComponentRoseBoundary(getSymbolContext(param, ColorParam.actorBorder), getUFont2(param,
					FontParam.ACTOR), stringsToDisplay, false, param, newFontForStereotype, getFontColor(param,
					FontParam.SEQUENCE_STEREOTYPE));
		}
		if (type == ComponentType.CONTROL_HEAD) {
			return new ComponentRoseControl(getSymbolContext(param, ColorParam.actorBorder), getUFont2(param,
					FontParam.ACTOR), stringsToDisplay, true, param, newFontForStereotype, getFontColor(param,
					FontParam.SEQUENCE_STEREOTYPE));
		}
		if (type == ComponentType.CONTROL_TAIL) {
			return new ComponentRoseControl(getSymbolContext(param, ColorParam.actorBorder), getUFont2(param,
					FontParam.ACTOR), stringsToDisplay, false, param, newFontForStereotype, getFontColor(param,
					FontParam.SEQUENCE_STEREOTYPE));
		}
		if (type == ComponentType.ENTITY_HEAD) {
			return new ComponentRoseEntity(getSymbolContext(param, ColorParam.actorBorder), getUFont2(param,
					FontParam.ACTOR), stringsToDisplay, true, param, newFontForStereotype, getFontColor(param,
					FontParam.SEQUENCE_STEREOTYPE));
		}
		if (type == ComponentType.ENTITY_TAIL) {
			return new ComponentRoseEntity(getSymbolContext(param, ColorParam.actorBorder), getUFont2(param,
					FontParam.ACTOR), stringsToDisplay, false, param, newFontForStereotype, getFontColor(param,
					FontParam.SEQUENCE_STEREOTYPE));
		}
		if (type == ComponentType.DATABASE_HEAD) {
			return new ComponentRoseDatabase(getSymbolContext(param, ColorParam.actorBorder), getUFont2(param,
					FontParam.ACTOR), stringsToDisplay, true, param, newFontForStereotype, getFontColor(param,
					FontParam.SEQUENCE_STEREOTYPE));
		}
		if (type == ComponentType.DATABASE_TAIL) {
			return new ComponentRoseDatabase(getSymbolContext(param, ColorParam.actorBorder), getUFont2(param,
					FontParam.ACTOR), stringsToDisplay, false, param, newFontForStereotype, getFontColor(param,
					FontParam.SEQUENCE_STEREOTYPE));
		}
		if (type == ComponentType.NOTE) {
			return new ComponentRoseNote(getSymbolContext(param, ColorParam.noteBorder), getUFont2(param,
					FontParam.NOTE), stringsToDisplay, paddingX, paddingY, param);
		}
		if (type == ComponentType.NOTE_HEXAGONAL) {
			return new ComponentRoseNoteHexagonal(getSymbolContext(param, ColorParam.noteBorder), getUFont2(param,
					FontParam.NOTE), stringsToDisplay, param);
		}
		if (type == ComponentType.NOTE_BOX) {
			return new ComponentRoseNoteBox(getSymbolContext(param, ColorParam.noteBorder), getUFont2(param,
					FontParam.NOTE), stringsToDisplay, param);
		}
		final FontConfiguration bigFont = getUFont2(param, FontParam.SEQUENCE_GROUP_HEADER);
		if (type == ComponentType.GROUPING_HEADER) {
			FontConfiguration smallFont = bigFont.forceFont(fontGrouping, null);
			final HtmlColor smallColor = SkinParamUtils.getFontColor(param, FontParam.SEQUENCE_GROUP, null);
			if (smallColor != null) {
				smallFont = smallFont.changeColor(smallColor);
			}
			return new ComponentRoseGroupingHeader(param.getBackgroundColor(), getSymbolContext(param,
					ColorParam.sequenceGroupBorder), bigFont, smallFont, stringsToDisplay, param);
		}
		if (type == ComponentType.GROUPING_ELSE) {
			return new ComponentRoseGroupingElse(getHtmlColor(param, ColorParam.sequenceGroupBorder), getUFont2(param,
					FontParam.SEQUENCE_GROUP), stringsToDisplay.get(0), param, param.getBackgroundColor());
		}
		if (type == ComponentType.GROUPING_SPACE) {
			return new ComponentRoseGroupingSpace(7);
		}
		if (type == ComponentType.ALIVE_BOX_CLOSE_CLOSE) {
			return new ComponentRoseActiveLine(getSymbolContext(param, ColorParam.sequenceLifeLineBorder), true, true);
		}
		if (type == ComponentType.ALIVE_BOX_CLOSE_OPEN) {
			return new ComponentRoseActiveLine(getSymbolContext(param, ColorParam.sequenceLifeLineBorder), true, false);
		}
		if (type == ComponentType.ALIVE_BOX_OPEN_CLOSE) {
			return new ComponentRoseActiveLine(getSymbolContext(param, ColorParam.sequenceLifeLineBorder), false, true);
		}
		if (type == ComponentType.ALIVE_BOX_OPEN_OPEN) {
			return new ComponentRoseActiveLine(getSymbolContext(param, ColorParam.sequenceLifeLineBorder), false, false);
		}
		if (type == ComponentType.DELAY_LINE) {
			return new ComponentRoseDelayLine(getHtmlColor(param, ColorParam.sequenceLifeLineBorder));
		}
		if (type == ComponentType.DELAY_TEXT) {
			return new ComponentRoseDelayText(getUFont2(param, FontParam.SEQUENCE_DELAY), stringsToDisplay, param);
		}
		if (type == ComponentType.DESTROY) {
			return new ComponentRoseDestroy(getHtmlColor(param, ColorParam.sequenceLifeLineBorder));
		}
		if (type == ComponentType.NEWPAGE) {
			return new ComponentRoseNewpage(getFontColor(param, FontParam.SEQUENCE_GROUP));
		}
		if (type == ComponentType.DIVIDER) {
			return new ComponentRoseDivider(getUFont2(param, FontParam.SEQUENCE_DIVIDER), getHtmlColor(param,
					ColorParam.sequenceDividerBackground), stringsToDisplay, param, deltaShadow(param) > 0, getStroke(
					param, LineParam.sequenceDividerBorder, 2), getHtmlColor(param, ColorParam.sequenceDividerBorder));
		}
		if (type == ComponentType.REFERENCE) {
			return new ComponentRoseReference(getUFont2(param, FontParam.SEQUENCE_REFERENCE), getSymbolContext(param,
					ColorParam.sequenceReferenceBorder), bigFont, stringsToDisplay,
					param.getHorizontalAlignment(AlignParam.SEQUENCE_REFERENCE_ALIGN), param, getHtmlColor(param,
							ColorParam.sequenceReferenceBackground));
		}
		if (type == ComponentType.TITLE) {
			return new ComponentRoseTitle(getUFont2(param, FontParam.SEQUENCE_TITLE), stringsToDisplay, param);
		}
		if (type == ComponentType.SIGNATURE) {
			return new ComponentRoseTitle(fontGrouping.toFont2(HtmlColorUtils.BLACK, param.useUnderlineForHyperlink(),
					param.getHyperlinkColor(), param.getTabSize()), Display.create("This skin was created ",
					"in April 2009."), param);
		}
		if (type == ComponentType.ENGLOBER) {
			return new ComponentRoseEnglober(getSymbolContext(param, ColorParam.sequenceBoxBorder), stringsToDisplay,
					getUFont2(param, FontParam.SEQUENCE_BOX), param);
		}

		return null;
	}

	private double deltaShadow(ISkinParam param) {
		return param.shadowing() ? 4.0 : 0;
	}

	private SymbolContext getSymbolContext(ISkinParam param, ColorParam color) {
		if (color == ColorParam.participantBorder) {
			return new SymbolContext(getHtmlColor(param, ColorParam.participantBackground), getHtmlColor(param,
					ColorParam.participantBorder)).withStroke(
					getStroke(param, LineParam.sequenceParticipantBorder, 1.5)).withDeltaShadow(deltaShadow(param));
		}
		if (color == ColorParam.actorBorder) {
			return new SymbolContext(getHtmlColor(param, ColorParam.actorBackground), getHtmlColor(param,
					ColorParam.actorBorder)).withStroke(getStroke(param, LineParam.sequenceActorBorder, 2))
					.withDeltaShadow(deltaShadow(param));
		}
		if (color == ColorParam.sequenceLifeLineBorder) {
			return new SymbolContext(getHtmlColor(param, ColorParam.sequenceLifeLineBackground), getHtmlColor(param,
					ColorParam.sequenceLifeLineBorder)).withDeltaShadow(deltaShadow(param));
		}
		if (color == ColorParam.noteBorder) {
			return new SymbolContext(getHtmlColor(param, ColorParam.noteBackground), getHtmlColor(param,
					ColorParam.noteBorder)).withStroke(getStroke(param, LineParam.noteBorder, 1)).withDeltaShadow(
					deltaShadow(param));
		}
		if (color == ColorParam.sequenceGroupBorder) {
			return new SymbolContext(getHtmlColor(param, ColorParam.sequenceGroupBackground), getHtmlColor(param,
					ColorParam.sequenceGroupBorder)).withStroke(getStroke(param, LineParam.sequenceGroupBorder, 2))
					.withDeltaShadow(deltaShadow(param));
		}
		if (color == ColorParam.sequenceBoxBorder) {
			return new SymbolContext(getHtmlColor(param, ColorParam.sequenceBoxBackground), getHtmlColor(param,
					ColorParam.sequenceBoxBorder));
		}
		if (color == ColorParam.sequenceReferenceBorder) {
			return new SymbolContext(getHtmlColor(param, ColorParam.sequenceReferenceHeaderBackground), getHtmlColor(
					param, ColorParam.sequenceReferenceBorder)).withStroke(
					getStroke(param, LineParam.sequenceReferenceBorder, 2)).withDeltaShadow(deltaShadow(param));
		}
		throw new IllegalArgumentException();
	}

	static public UStroke getStroke(ISkinParam param, LineParam lineParam, double defaultValue) {
		final UStroke result = param.getThickness(lineParam, null);
		if (result == null) {
			return new UStroke(defaultValue);
		}
		return result;
	}

	public Object getProtocolVersion() {
		return 1;
	}

}
