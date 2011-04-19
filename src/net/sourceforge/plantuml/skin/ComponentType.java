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
 * Revision $Revision: 5191 $
 *
 */
package net.sourceforge.plantuml.skin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentType {

	private static final Map<ArrowConfiguration, ComponentType> arrows = new HashMap<ArrowConfiguration, ComponentType>();
	private static final List<ComponentType> nonArrows = new ArrayList<ComponentType>();

	static public final ComponentType ACTOR_HEAD = new ComponentType("ACTOR_HEAD");
	static public final ComponentType ACTOR_TAIL = new ComponentType("ACTOR_TAIL");

	//
	static public final ComponentType ALIVE_BOX_CLOSE_CLOSE = new ComponentType("ALIVE_BOX_CLOSE_CLOSE");
	static public final ComponentType ALIVE_BOX_CLOSE_OPEN = new ComponentType("ALIVE_BOX_CLOSE_OPEN");
	static public final ComponentType ALIVE_BOX_OPEN_CLOSE = new ComponentType("ALIVE_BOX_OPEN_CLOSE");
	static public final ComponentType ALIVE_BOX_OPEN_OPEN = new ComponentType("ALIVE_BOX_OPEN_OPEN");
	
	static public final ComponentType DELAY_TEXT = new ComponentType("DELAY_TEXT");
	static public final ComponentType DESTROY = new ComponentType("DESTROY");

	static public final ComponentType DELAY_LINE = new ComponentType("DELAY_LINE");
	static public final ComponentType PARTICIPANT_LINE = new ComponentType("PARTICIPANT_LINE");
	static public final ComponentType CONTINUE_LINE = new ComponentType("CONTINUE_LINE");

	//
	static public final ComponentType GROUPING_BODY = new ComponentType("GROUPING_BODY");
	static public final ComponentType GROUPING_ELSE = new ComponentType("GROUPING_ELSE");
	static public final ComponentType GROUPING_HEADER = new ComponentType("GROUPING_HEADER");
	static public final ComponentType GROUPING_TAIL = new ComponentType("GROUPING_TAIL");
	//
	static public final ComponentType NEWPAGE = new ComponentType("NEWPAGE");
	static public final ComponentType NOTE = new ComponentType("NOTE");
	static public final ComponentType DIVIDER = new ComponentType("DIVIDER");
	static public final ComponentType REFERENCE = new ComponentType("REFERENCE");
	static public final ComponentType ENGLOBER = new ComponentType("ENGLOBER");

	//
	static public final ComponentType PARTICIPANT_HEAD = new ComponentType("PARTICIPANT_HEAD");
	static public final ComponentType PARTICIPANT_TAIL = new ComponentType("PARTICIPANT_TAIL");

	//
	static public final ComponentType TITLE = new ComponentType("TITLE");
	static public final ComponentType SIGNATURE = new ComponentType("SIGNATURE");

	private final ArrowConfiguration arrowConfiguration;
	private final String name;

	private ComponentType(String name) {
		this(name, null);
		nonArrows.add(this);
	}

	private ComponentType(String name, ArrowConfiguration arrowConfiguration) {
		this.name = name;
		this.arrowConfiguration = arrowConfiguration;
	}

	public static ComponentType getArrow(ArrowDirection direction) {
		final ArrowConfiguration config = ArrowConfiguration.withDirection(direction);
		return getArrow(config);
	}

	private static ComponentType getArrow(ArrowConfiguration config) {
		ComponentType result = arrows.get(config);
		if (result == null) {
			result = new ComponentType(config.name(), config);
			arrows.put(config, result);
		}
		return result;
	}

	public ComponentType withAsync() {
		checkArrow();
		return ComponentType.getArrow(arrowConfiguration.withAsync());
	}

	public ComponentType withDotted() {
		checkArrow();
		return ComponentType.getArrow(arrowConfiguration.withDotted());
	}

	public ComponentType withPart(ArrowPart part) {
		checkArrow();
		return ComponentType.getArrow(arrowConfiguration.withPart(part));
	}

	public String name() {
		return name;
	}

	public boolean isArrow() {
		return this.arrowConfiguration != null;
	}

	private void checkArrow() {
		if (this.arrowConfiguration == null) {
			throw new IllegalArgumentException(name());
		}
	}

	public static Collection<ComponentType> all() {
		// ARROW, DOTTED_ARROW, DOTTED_SELF_ARROW, RETURN_ARROW,
		// RETURN_DOTTED_ARROW, SELF_ARROW,
		// ASYNC_ARROW, ASYNC_DOTTED_ARROW, ASYNC_RETURN_ARROW,
		// ASYNC_RETURN_DOTTED_ARROW,

		final List<ComponentType> all = new ArrayList<ComponentType>();
		all.add(ComponentType.getArrow(ArrowDirection.LEFT_TO_RIGHT_NORMAL));
		all.add(ComponentType.getArrow(ArrowDirection.RIGHT_TO_LEFT_REVERSE));
		all.add(ComponentType.getArrow(ArrowDirection.SELF));
		all.add(ComponentType.getArrow(ArrowDirection.LEFT_TO_RIGHT_NORMAL).withDotted());
		all.add(ComponentType.getArrow(ArrowDirection.RIGHT_TO_LEFT_REVERSE).withDotted());
		all.add(ComponentType.getArrow(ArrowDirection.SELF).withDotted());

		for (ComponentType type : new ArrayList<ComponentType>(all)) {
			all.add(type.withAsync());
		}
		
		final List<ComponentType> simples = new ArrayList<ComponentType>(all);
		for (ComponentType type : simples) {
			all.add(type.withPart(ArrowPart.TOP_PART));
		}
		for (ComponentType type : simples) {
			all.add(type.withPart(ArrowPart.BOTTOM_PART));
		}

		all.addAll(nonArrows);
		return Collections.unmodifiableCollection(all);
	}

	public final ArrowConfiguration getArrowConfiguration() {
		return arrowConfiguration;
	}

}
