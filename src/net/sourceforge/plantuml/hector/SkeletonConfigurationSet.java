/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * Revision $Revision: 5079 $
 *
 */
package net.sourceforge.plantuml.hector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SkeletonConfigurationSet implements Iterable<SkeletonConfiguration> {

	private final List<SkeletonConfiguration> all;
	private final SkeletonConfigurationComparator comparator;
	private final int limitSize;

	public SkeletonConfigurationSet(int limitSize, SkeletonConfigurationEvaluator evaluator) {
		this.comparator = new SkeletonConfigurationComparator(evaluator);
		this.all = new ArrayList<SkeletonConfiguration>();
		this.limitSize = limitSize;
	}

	public void add(SkeletonConfiguration skeletonConfiguration) {
		this.all.add(skeletonConfiguration);
		sortAndTruncate();
	}

	public void addAll(Collection<SkeletonConfiguration> others) {
		all.addAll(others);
		sortAndTruncate();
	}

	private void sortAndTruncate() {
		Collections.sort(all, comparator);
		while (all.size() > limitSize) {
			all.remove(all.size() - 1);
		}
	}

	@Override
	public String toString() {
		return all.toString();
	}

	public int size() {
		return all.size();
	}

	public Iterator<SkeletonConfiguration> iterator() {
		return new ArrayList<SkeletonConfiguration>(all).iterator();
	}

	public SkeletonConfiguration first() {
		return all.get(0);
	}

}
