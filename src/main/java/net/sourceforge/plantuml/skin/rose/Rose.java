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
 *
 */
package net.sourceforge.plantuml.skin.rose;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.skin.AlignmentParam;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.ColorParam;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.LineParam;
import net.sourceforge.plantuml.skin.PaddingParam;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class Rose {

	final private double paddingX = 5;
	final public static double paddingY = 5;

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

	private void checkRose() {
		// Quite ugly, but we want to ensure that TextSkin overrides those methods
		if (this.getClass() != Rose.class)
			throw new IllegalStateException("" + this.getClass());

	}

	public Component createComponentNote(Style[] styles, ComponentType type, ISkinParam param, Display stringsToDisplay,
			Colors colors) {
		checkRose();
		return createComponentNote(styles, type, param, stringsToDisplay, colors, null);
	}

	public Component createComponentNote(Style[] styles, ComponentType type, ISkinParam param, Display stringsToDisplay,
			Colors colors, NotePosition notePosition) {
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
			return new ComponentRoseNote(styles[0], stringsToDisplay, paddingX, paddingY, param, textAlign, position,
					colors);

		if (type == ComponentType.NOTE_HEXAGONAL)
			return new ComponentRoseNoteHexagonal(styles[0], stringsToDisplay, param, colors);

		if (type == ComponentType.NOTE_BOX)
			return new ComponentRoseNoteBox(styles[0], stringsToDisplay, param, colors);

		throw new UnsupportedOperationException(type.toString());
	}

	public Component createComponentParticipant(Participant p, ComponentType type, ArrowConfiguration config,
			ISkinParam param, Display stringsToDisplay) {
		checkRose();
		final Style[] styles = p.getUsedStyles();

		// final Stereotype stereotype = stringsToDisplay == null ? null : stringsToDisplay.getStereotypeIfAny();

		final double padding = param.getPadding(PaddingParam.PARTICIPANT);

		if (type == ComponentType.PARTICIPANT_HEAD)
			return new ComponentRoseParticipant(styles[0], styles[1], stringsToDisplay, param,
					getMinClassWidth(styles[0]), false, padding);

		if (type == ComponentType.PARTICIPANT_TAIL)
			return new ComponentRoseParticipant(styles[0], styles[1], stringsToDisplay, param,
					getMinClassWidth(styles[0]), false, padding);

		if (type == ComponentType.COLLECTIONS_HEAD)
			return new ComponentRoseParticipant(styles[0], styles[1], stringsToDisplay, param,
					getMinClassWidth(styles[0]), true, padding);

		if (type == ComponentType.COLLECTIONS_TAIL)
			return new ComponentRoseParticipant(styles[0], styles[1], stringsToDisplay, param,
					getMinClassWidth(styles[0]), true, padding);

		if (type == ComponentType.ACTOR_HEAD)
			return new ComponentRoseActor(param.actorStyle(), styles[0], styles == null ? null : styles[1],
					stringsToDisplay, true, param);

		if (type == ComponentType.ACTOR_TAIL)
			return new ComponentRoseActor(param.actorStyle(), styles[0], styles == null ? null : styles[1],
					stringsToDisplay, false, param);

		if (type == ComponentType.BOUNDARY_HEAD)
			return new ComponentRoseBoundary(styles[0], styles[1], stringsToDisplay, true, param);

		if (type == ComponentType.BOUNDARY_TAIL)
			return new ComponentRoseBoundary(styles[0], styles[1], stringsToDisplay, false, param);

		if (type == ComponentType.CONTROL_HEAD)
			return new ComponentRoseControl(styles[0], styles[1], stringsToDisplay, true, param);

		if (type == ComponentType.CONTROL_TAIL)
			return new ComponentRoseControl(styles[0], styles[1], stringsToDisplay, false, param);

		if (type == ComponentType.ENTITY_HEAD)
			return new ComponentRoseEntity(styles[0], styles[1], stringsToDisplay, true, param);

		if (type == ComponentType.ENTITY_TAIL)
			return new ComponentRoseEntity(styles[0], styles[1], stringsToDisplay, false, param);

		if (type == ComponentType.QUEUE_HEAD)
			return new ComponentRoseQueue(styles[0], styles[1], stringsToDisplay, true, param);

		if (type == ComponentType.QUEUE_TAIL)
			return new ComponentRoseQueue(styles[0], styles[1], stringsToDisplay, false, param);

		if (type == ComponentType.DATABASE_HEAD)
			return new ComponentRoseDatabase(styles[0], styles[1], stringsToDisplay, true, param);

		if (type == ComponentType.DATABASE_TAIL)
			return new ComponentRoseDatabase(styles[0], styles[1], stringsToDisplay, false, param);

		throw new UnsupportedOperationException();
	}

	public Component createComponent(Style[] styles, ComponentType type, ArrowConfiguration config, ISkinParam param,
			Display stringsToDisplay) {
		checkRose();

		if (type == ComponentType.DELAY_LINE || type == ComponentType.GROUPING_SPACE || type == ComponentType.DESTROY) {

		} else {
			if (styles == null)
				throw new UnsupportedOperationException(type.toString());
		}

		// final Stereotype stereotype = stringsToDisplay == null ? null : stringsToDisplay.getStereotypeIfAny();

		if (type.isArrow())
			return createComponentArrow(null, config, param, stringsToDisplay);

		if (type == ComponentType.PARTICIPANT_LINE)
			return new ComponentRoseLine(styles[0], false, stringsToDisplay, param);

		// if (type == ComponentType.CONTINUE_LINE)
		//	return new ComponentRoseLine(styles[0], true, stringsToDisplay, param);

		if (type == ComponentType.NOTE)
			throw new UnsupportedOperationException();

		if (type == ComponentType.NOTE_HEXAGONAL)
			throw new UnsupportedOperationException();

		if (type == ComponentType.NOTE_BOX)
			throw new UnsupportedOperationException();

		if (type == ComponentType.GROUPING_HEADER_LEGACY)
			return new ComponentRoseGroupingHeader(false, styles[0], styles[1], stringsToDisplay, param);

		if (type == ComponentType.GROUPING_HEADER_TEOZ)
			return new ComponentRoseGroupingHeader(true, styles[0], styles[1], stringsToDisplay, param);

		if (type == ComponentType.GROUPING_ELSE_LEGACY)
			return new ComponentRoseGroupingElse(false, styles[0], stringsToDisplay.get(0), param);

		if (type == ComponentType.GROUPING_ELSE_TEOZ)
			return new ComponentRoseGroupingElse(true, styles[0], stringsToDisplay.get(0), param);

		if (type == ComponentType.GROUPING_SPACE)
			return new ComponentRoseGroupingSpace(7);

		if (type == ComponentType.ACTIVATION_BOX_CLOSE_CLOSE)
			return new ComponentRoseActiveLine(styles[0], true, true, stringsToDisplay, param);

		if (type == ComponentType.ACTIVATION_BOX_CLOSE_OPEN)
			return new ComponentRoseActiveLine(styles[0], true, false, stringsToDisplay, param);

		if (type == ComponentType.ACTIVATION_BOX_OPEN_CLOSE) {
			return new ComponentRoseActiveLine(styles[0], false, true, stringsToDisplay, param);
		}
		if (type == ComponentType.ACTIVATION_BOX_OPEN_OPEN)
			return new ComponentRoseActiveLine(styles[0], false, false, stringsToDisplay, param);

		if (type == ComponentType.DELAY_LINE)
			return new ComponentRoseDelayLine(styles[0], param);

		if (type == ComponentType.DELAY_TEXT)
			return new ComponentRoseDelayText(styles[0], stringsToDisplay, param);

		if (type == ComponentType.DESTROY)
			return new ComponentRoseDestroy(styles[0], param);

		if (type == ComponentType.NEWPAGE)
			throw new UnsupportedOperationException();

		if (type == ComponentType.DIVIDER)
			return new ComponentRoseDivider(styles[0], stringsToDisplay, param);

		if (type == ComponentType.REFERENCE)
			return new ComponentRoseReference(styles[0], styles[1], stringsToDisplay, param);

		if (type == ComponentType.ENGLOBER)
			return new ComponentRoseEnglober(styles[0], stringsToDisplay, param);

		throw new UnsupportedOperationException();
	}

	private double getMinClassWidth(Style style) {
		return style.value(PName.MinimumWidth).asDouble();
	}

	public Component createComponentNewPage(Style[] styles, ISkinParam param) {
		checkRose();
		return new ComponentRoseNewpage(styles[0], param);
	}

	public ArrowComponent createComponentArrow(Style[] styles, ArrowConfiguration config, ISkinParam param,
			Display stringsToDisplay) {
		checkRose();

		if (config.getArrowDirection() == ArrowDirection.SELF)
			return new ComponentRoseSelfArrow(styles[0], stringsToDisplay, config, param, param.maxMessageSize(),
					param.strictUmlStyle() == false);

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

		return new ComponentRoseArrow(styles[0], stringsToDisplay, config, messageHorizontalAlignment, param,
				param.maxMessageSize(), param.strictUmlStyle() == false, param.responseMessageBelowArrow());
	}

	static public UStroke getStroke(ISkinParam param, LineParam lineParam, double defaultValue) {
		final UStroke result = param.getThickness(lineParam, null);
		if (result == null)
			return UStroke.withThickness(defaultValue);

		return result;
	}

}
