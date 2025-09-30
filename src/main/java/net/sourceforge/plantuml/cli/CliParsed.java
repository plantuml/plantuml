/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.plantuml.utils.Peeker;
import net.sourceforge.plantuml.utils.PeekerUtils;

public class CliParsed {

	private final Map<CliFlag, List<Object>> lists = new EnumMap<>(CliFlag.class);
	private final Map<CliFlag, Map<String, String>> maps = new EnumMap<>(CliFlag.class);
	private final List<String> args = new ArrayList<>();

	public void putValue(CliFlag flag, Object value) {
		final List<Object> list = lists.computeIfAbsent(flag, k -> new ArrayList<>());
		list.add(value);
	}

	private void putMapValue(CliFlag flag, String key, String value) {
		final Map<String, String> map = maps.computeIfAbsent(flag, k -> new LinkedHashMap<>());
		map.put(key, value);
	}

	public boolean isTrue(CliFlag flag) {
		if (maps.containsKey(flag))
			return true;

		final List<Object> list = lists.get(flag);

		// Useless: cannot be Boolean.FALSE
		if (list != null && list.size() > 0 && list.get(0) instanceof Boolean)
			return (Boolean) list.get(0);

		return list != null;
	}

	public String getString(CliFlag flag) {
		final List<Object> list = lists.get(flag);
		if (list != null && list.size() > 0)
			return (String) list.get(0);

		return flag.getDefaultValue();
	}

	public Object getObject(CliFlag flag) {
		final List<Object> list = lists.get(flag);
		if (list != null)
			return list.get(0);

		return flag.getDefaultValue();
	}

	public Object getFromType(Class<?> clazz) {
		for (List<Object> list : lists.values())
			if (list.size() == 1) {
				final Object element = list.get(0);
				if (element.getClass() == clazz)
					return element;
			}

		return null;
	}

	public Map<String, String> getMap(CliFlag flag) {
		return maps.get(flag);
	}

	public List<Object> getList(CliFlag flag) {
		return lists.get(flag);
	}

	@Override
	public String toString() {
		final Map<CliFlag, Object> merge = new EnumMap<>(CliFlag.class);
		merge.putAll(lists);
		merge.putAll(maps);
		return merge.toString();
	}

	public CliAction getImmediateAction() {
		for (Entry<CliFlag, List<Object>> entry : lists.entrySet())
			if (entry.getKey().getType() == Arity.UNARY_IMMEDIATE_ACTION && entry.getValue().size() == 1) {
				final Object data = entry.getValue().get(0);
				if (data instanceof CliAction)
					return (CliAction) data;
			}
		return null;
	}

	public void triggerNonImmediateCliAction() {
		for (Entry<CliFlag, List<Object>> entry : lists.entrySet())
			if (entry.getKey().getType() != Arity.UNARY_IMMEDIATE_ACTION && entry.getValue().size() == 1) {
				final Object data = entry.getValue().get(0);
				if (data instanceof CliAction)
					((CliAction) data).runAction();

			}

	}

	public static CliParsed parse(String... args) {
		final CliParsed result = new CliParsed();
		for (final Peeker<String> peeker = PeekerUtils.peeker(Arrays.asList(args)); peeker.peek(0) != null; peeker
				.jump()) {

			final String peek0 = peeker.peek(0);
			boolean found = false;
			for (CliFlag flag : CliFlag.values()) {
				if (found || flag.match(peek0) == false)
					continue;

				found = true;
				switch (flag.getType()) {

				case UNARY_BOOLEAN:
					final Object foo = flag.getFoo();
					result.putValue(flag, foo == null ? Boolean.TRUE : foo);
					break;

				case UNARY_IMMEDIATE_ACTION:
					result.putValue(flag, flag.getFoo());
					break;

				case BINARY_NEXT_ARGUMENT_VALUE:
					peeker.jump();
					result.putValue(flag, peeker.peek(0));
					break;

				case UNARY_INLINE_KEY_OR_KEY_VALUE:
					final String s = peeker.peek(0).substring(2);
					final int eqIndex = s.indexOf('=');
					final String k = eqIndex < 0 ? s : s.substring(0, eqIndex);
					final String v = eqIndex < 0 ? null : s.substring(eqIndex + 1);
					result.putMapValue(flag, k, v);
					break;

				case UNARY_OPTIONAL_COLON:
					final int colon = peek0.indexOf(':');
					if (colon == -1) {
						result.lists.computeIfAbsent(flag, k1 -> new ArrayList<>());
					} else
						for (String part : peek0.substring(colon + 1).split(":"))
							result.putValue(flag, part);
					break;
				}
			}
			if (found == false)
				result.args.add(peek0);

		}

		return result;

	}

	public List<String> getRemainingArgs() {
		return Collections.unmodifiableList(args);
	}

}
