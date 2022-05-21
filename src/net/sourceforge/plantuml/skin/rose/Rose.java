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
package net.sourceforge.plantuml.skin.rose;

import net.sourceforge.plantuml.AlignmentParam;
import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.PaddingParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class Rose {

	final private double paddingX = 5;
	final public static double paddingY = 5;

	public HColor getFontColor(ISkinParam skin, FontParam fontParam) {
		return skin.getFontHtmlColor(null, fontParam);
	}

	public HColor getHtmlColor(ISkinParam skin, ColorParam color) {
		return getHtmlColor(skin, null, color);
	}

	public HColor getHtmlColor(ISkinParam skin, Stereotype stereotype, ColorParam... colorParams) {
		for (ColorParam param : colorParams) {
			final HColor result = skin.getHtmlColor(param, stereotype, false);
			if (result != null)
				return result;

		}
		return colorParams[0].getDefaultValue();
	}

	public Component createComponentNote(Style[] styles, ComponentType type, ISkinParam param,
			Display stringsToDisplay) {
		checkRose();
		return createComponentNote(styles, type, param, stringsToDisplay, null);
	}

	private void checkRose() {
		// Quite ugly, but we want to ensure that TextSkin overrides those methods
		if (this.getClass() != Rose.class)
			throw new IllegalStateException("" + this.getClass());

	}

	public Component createComponentNote(Style[] styles, ComponentType type, ISkinParam param, Display stringsToDisplay,
			NotePosition notePosition) {
		checkRose();
		final HorizontalAlignment textAlign;
		final HorizontalAlignment position;
		if (notePosition == NotePosition.OVER_SEVERAL) {
			textAlign = param.getHorizontalAlignment(AlignmentParam.noteTextAlignment, null, false,
					HorizontalAlignment.LEFT);
			if (textAlign == param.getHorizontalAlignment(AlignmentParam.noteTextAlignment, null, false,
					HorizontalAlignment.CENTER))
				// Which means we use default
				position = textAlign;
			else
				position = HorizontalAlignment.CENTER;
		} else {
			textAlign = param.getHorizontalAlignment(AlignmentParam.noteTextAlignment, null, false, null);
			position = textAlign;
		}

		if (type == ComponentType.NOTE)
			return new ComponentRoseNote(styles == null ? null : styles[0], stringsToDisplay, paddingX, paddingY, param,
					textAlign, position);

		if (type == ComponentType.NOTE_HEXAGONAL)
			return new ComponentRoseNoteHexagonal(styles == null ? null : styles[0], stringsToDisplay, param);

		if (type == ComponentType.NOTE_BOX)
			return new ComponentRoseNoteBox(styles == null ? null : styles[0], stringsToDisplay, param);

		throw new UnsupportedOperationException(type.toString());
	}

	public Component createComponent(Style[] styles, ComponentType type, ArrowConfiguration config, ISkinParam param,
			Display stringsToDisplay) {
		checkRose();

		final Stereotype stereotype = stringsToDisplay == null ? null : stringsToDisplay.getStereotypeIfAny();

		if (type.isArrow())
			return createComponentArrow(null, config, param, stringsToDisplay);

		final double padding = param.getPadding(PaddingParam.PARTICIPANT);

		if (type == ComponentType.PARTICIPANT_HEAD)
			return new ComponentRoseParticipant(styles == null ? null : styles[0], styles == null ? null : styles[1],
					stringsToDisplay, param, param.minClassWidth(), false, padding);

		if (type == ComponentType.PARTICIPANT_TAIL)
			return new ComponentRoseParticipant(styles == null ? null : styles[0], styles == null ? null : styles[1],
					stringsToDisplay, param, param.minClassWidth(), false, padding);

		if (type == ComponentType.COLLECTIONS_HEAD)
			return new ComponentRoseParticipant(styles == null ? null : styles[0], styles == null ? null : styles[1],
					stringsToDisplay, param, param.minClassWidth(), true, padding);

		if (type == ComponentType.COLLECTIONS_TAIL)
			return new ComponentRoseParticipant(styles == null ? null : styles[0], styles == null ? null : styles[1],
					stringsToDisplay, param, param.minClassWidth(), true, padding);

		if (type == ComponentType.PARTICIPANT_LINE)
			return new ComponentRoseLine(param.getThemeStyle(), styles == null ? null : styles[0], false,
					param.getIHtmlColorSet());

		if (type == ComponentType.CONTINUE_LINE)
			return new ComponentRoseLine(param.getThemeStyle(), styles == null ? null : styles[0], true,
					param.getIHtmlColorSet());

		if (type == ComponentType.ACTOR_HEAD)
			return new ComponentRoseActor(param.actorStyle(), styles == null ? null : styles[0],
					styles == null ? null : styles[1], stringsToDisplay, true, param);

		if (type == ComponentType.ACTOR_TAIL)
			return new ComponentRoseActor(param.actorStyle(), styles == null ? null : styles[0],
					styles == null ? null : styles[1], stringsToDisplay, false, param);

		if (type == ComponentType.BOUNDARY_HEAD)
			return new ComponentRoseBoundary(styles == null ? null : styles[0], styles == null ? null : styles[1],
					stringsToDisplay, true, param);

		if (type == ComponentType.BOUNDARY_TAIL)
			return new ComponentRoseBoundary(styles == null ? null : styles[0], styles == null ? null : styles[1],
					stringsToDisplay, false, param);

		if (type == ComponentType.CONTROL_HEAD)
			return new ComponentRoseControl(styles == null ? null : styles[0], styles == null ? null : styles[1],
					stringsToDisplay, true, param);

		if (type == ComponentType.CONTROL_TAIL)
			return new ComponentRoseControl(styles == null ? null : styles[0], styles == null ? null : styles[1],
					stringsToDisplay, false, param);

		if (type == ComponentType.ENTITY_HEAD)
			return new ComponentRoseEntity(styles == null ? null : styles[0], styles == null ? null : styles[1],
					stringsToDisplay, true, param);

		if (type == ComponentType.ENTITY_TAIL)
			return new ComponentRoseEntity(styles == null ? null : styles[0], styles == null ? null : styles[1],
					stringsToDisplay, false, param);

		if (type == ComponentType.QUEUE_HEAD)
			return new ComponentRoseQueue(styles == null ? null : styles[0], styles == null ? null : styles[1],
					stringsToDisplay, true, param);

		if (type == ComponentType.QUEUE_TAIL)
			return new ComponentRoseQueue(styles == null ? null : styles[0], styles == null ? null : styles[1],
					stringsToDisplay, false, param);

		if (type == ComponentType.DATABASE_HEAD)
			return new ComponentRoseDatabase(styles == null ? null : styles[0], styles == null ? null : styles[1],
					stringsToDisplay, true, param);

		if (type == ComponentType.DATABASE_TAIL)
			return new ComponentRoseDatabase(styles == null ? null : styles[0], styles == null ? null : styles[1],
					stringsToDisplay, false, param);

		if (type == ComponentType.NOTE)
			throw new UnsupportedOperationException();

		if (type == ComponentType.NOTE_HEXAGONAL)
			throw new UnsupportedOperationException();

		if (type == ComponentType.NOTE_BOX)
			throw new UnsupportedOperationException();

		if (type == ComponentType.GROUPING_HEADER)
			return new ComponentRoseGroupingHeader(styles == null ? null : styles[0], styles == null ? null : styles[1],
					stringsToDisplay, param);

		if (type == ComponentType.GROUPING_ELSE)
			return new ComponentRoseGroupingElse(styles == null ? null : styles[0], stringsToDisplay.get(0), param);

		if (type == ComponentType.GROUPING_SPACE)
			return new ComponentRoseGroupingSpace(7);

		if (type == ComponentType.ALIVE_BOX_CLOSE_CLOSE)
			return new ComponentRoseActiveLine(param.getThemeStyle(), styles == null ? null : styles[0], true, true,
					param.getIHtmlColorSet());

		if (type == ComponentType.ALIVE_BOX_CLOSE_OPEN)
			return new ComponentRoseActiveLine(param.getThemeStyle(), styles == null ? null : styles[0], true, false,
					param.getIHtmlColorSet());

		if (type == ComponentType.ALIVE_BOX_OPEN_CLOSE) {
			return new ComponentRoseActiveLine(param.getThemeStyle(), styles == null ? null : styles[0], false, true,
					param.getIHtmlColorSet());
		}
		if (type == ComponentType.ALIVE_BOX_OPEN_OPEN)
			return new ComponentRoseActiveLine(param.getThemeStyle(), styles == null ? null : styles[0], false, false,
					param.getIHtmlColorSet());

		if (type == ComponentType.DELAY_LINE)
			return new ComponentRoseDelayLine(null, getHtmlColor(param, stereotype, ColorParam.sequenceLifeLineBorder));

		if (type == ComponentType.DELAY_TEXT)
			return new ComponentRoseDelayText(styles == null ? null : styles[0], stringsToDisplay, param);

		if (type == ComponentType.DESTROY)
			return new ComponentRoseDestroy(styles == null ? null : styles[0],
					getHtmlColor(param, stereotype, ColorParam.sequenceLifeLineBorder), param);

		if (type == ComponentType.NEWPAGE)
			throw new UnsupportedOperationException();

		if (type == ComponentType.DIVIDER)
			return new ComponentRoseDivider(styles == null ? null : styles[0], stringsToDisplay, param);

		if (type == ComponentType.REFERENCE)
			return new ComponentRoseReference(styles == null ? null : styles[0], styles == null ? null : styles[1],
					stringsToDisplay, param, getHtmlColor(param, stereotype, ColorParam.sequenceReferenceBackground));

		if (type == ComponentType.ENGLOBER)
			return new ComponentRoseEnglober(styles == null ? null : styles[0], stringsToDisplay, param);

		throw new UnsupportedOperationException();
	}

	public Component createComponentNewPage(ISkinParam param) {
		checkRose();
		return new ComponentRoseNewpage(null, getHtmlColor(param, ColorParam.sequenceNewpageSeparator));
	}

	public ArrowComponent createComponentArrow(Style[] styles, ArrowConfiguration config, ISkinParam param,
			Display stringsToDisplay) {
		checkRose();

		if (config.getArrowDirection() == ArrowDirection.SELF)
			return new ComponentRoseSelfArrow(styles == null ? null : styles[0], stringsToDisplay, config, param,
					param.maxMessageSize(), param.strictUmlStyle() == false);

		final ArrowDirection arrowDirection = config.getArrowDirection();

		final StyleSignatureBasic signature = StyleSignatureBasic.of(SName.root, SName.element, SName.sequenceDiagram,
				SName.arrow);
		final Style textStyle = signature.getMergedStyle(param.getCurrentStyleBuilder());
		final String value = textStyle.value(PName.HorizontalAlignment).asString();
		HorizontalAlignment messageHorizontalAlignment = textStyle.getHorizontalAlignment();

		if ("first".equalsIgnoreCase(value)) {
			final boolean isReverseDefine = config.isReverseDefine();
			if (arrowDirection == ArrowDirection.RIGHT_TO_LEFT_REVERSE) {
				if (isReverseDefine)
					messageHorizontalAlignment = HorizontalAlignment.LEFT;
				else
					messageHorizontalAlignment = HorizontalAlignment.RIGHT;

			} else {
				if (isReverseDefine)
					messageHorizontalAlignment = HorizontalAlignment.RIGHT;
				else
					messageHorizontalAlignment = HorizontalAlignment.LEFT;

			}
		} else if ("direction".equalsIgnoreCase(value)) {
			if (arrowDirection == ArrowDirection.LEFT_TO_RIGHT_NORMAL)
				messageHorizontalAlignment = HorizontalAlignment.LEFT;
			else if (arrowDirection == ArrowDirection.RIGHT_TO_LEFT_REVERSE)
				messageHorizontalAlignment = HorizontalAlignment.RIGHT;
			else if (arrowDirection == ArrowDirection.BOTH_DIRECTION)
				messageHorizontalAlignment = HorizontalAlignment.CENTER;

		} else if ("reversedirection".equalsIgnoreCase(value)) {
			if (arrowDirection == ArrowDirection.LEFT_TO_RIGHT_NORMAL)
				messageHorizontalAlignment = HorizontalAlignment.RIGHT;
			else if (arrowDirection == ArrowDirection.RIGHT_TO_LEFT_REVERSE)
				messageHorizontalAlignment = HorizontalAlignment.LEFT;
			else if (arrowDirection == ArrowDirection.BOTH_DIRECTION)
				messageHorizontalAlignment = HorizontalAlignment.CENTER;

		}

		return new ComponentRoseArrow(styles == null ? null : styles[0], stringsToDisplay, config,
				messageHorizontalAlignment, param, param.maxMessageSize(), param.strictUmlStyle() == false,
				param.responseMessageBelowArrow());
	}

	static public UStroke getStroke(ISkinParam param, LineParam lineParam, double defaultValue) {
		final UStroke result = param.getThickness(lineParam, null);
		if (result == null)
			return new UStroke(defaultValue);

		return result;
	}

}
