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
 */
package net.sourceforge.plantuml.sequencediagram;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DottedNumber {

	private final List<Integer> nums;
	private final List<String> separators;

	private DottedNumber(List<Integer> nums, List<String> separators) {
		this.nums = nums;
		this.separators = separators;
	}

	public static DottedNumber create(String value) {
		final Pattern p = Pattern.compile("(\\d+)|(\\D+)");
		final Matcher m = p.matcher(value);
		final List<Integer> nums = new ArrayList<Integer>();
		final List<String> separators = new ArrayList<String>();
		while (m.find()) {
			final String part = m.group();
			if (isDigit(part.charAt(0))) {
				nums.add(Integer.parseInt(part));
			} else {
				separators.add(part);
			}
		}
		return new DottedNumber(nums, separators);
	}

	private static boolean isDigit(final char c) {
		return c >= '0' && c <= '9';
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < nums.size(); i++) {
			sb.append(nums.get(i));
			if (i < separators.size()) {
				sb.append(separators.get(i));
			}
		}
		return sb.toString();
	}

	public void incrementMinor(int step) {
		final int last = nums.size() - 1;
		final int newValue = nums.get(last) + step;
		nums.set(last, newValue);
	}

	public void incrementIntermediate() {
		final int intermediate = nums.size() == 1 ? 0 : nums.size() - 2;
		incrementIntermediate(intermediate);
	}

	public void incrementIntermediate(int position) {
		final int intermediate = position;
		final int newValue = nums.get(intermediate) + 1;
		for (int i = intermediate + 1; i < nums.size(); i++) {
			nums.set(i, 1);
		}
		nums.set(intermediate, newValue);
	}

	public String format(DecimalFormat format) {
		if (nums.size() == 1 && separators.size() == 0) {
			return format.format(nums.get(0));
		}
		return "<b>" + toString() + "</b>";
	}

}
