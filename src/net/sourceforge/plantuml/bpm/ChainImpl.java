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
package net.sourceforge.plantuml.bpm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

public class ChainImpl<O> implements Chain<O> {

	private final List<O> positive = new ArrayList<O>();
	private final List<O> negative = new ArrayList<O>();
	private int currentVersion;

	public boolean remove(O data) {
		updateStructuralVersion();
		boolean result = positive.remove(data);
		if (result == false) {
			result = negative.remove(data);
		}
		return result;
	}

	public ChainImpl<O> cloneMe() {
		final ChainImpl<O> result = new ChainImpl<O>();
		result.currentVersion = this.currentVersion;
		result.positive.addAll(this.positive);
		result.negative.addAll(this.negative);
		return result;
	}

	public int compare(O a, O b) {
		if (a.equals(b)) {
			return 0;
		}
		for (int i = negative.size() - 1; i >= 0; i--) {
			if (a.equals(negative.get(i))) {
				return -1;
			}
			if (b.equals(negative.get(i))) {
				return 1;
			}
		}
		for (O cur : positive) {
			if (a.equals(cur)) {
				return -1;
			}
			if (b.equals(cur)) {
				return 1;
			}
		}
		throw new UnsupportedOperationException();
	}

	public List<O> toList() {
		final List<O> result = new ArrayList<O>();
		for (O element : negative) {
			if (element != null) {
				result.add(0, element);
			}
		}
		for (O element : positive) {
			if (element != null) {
				result.add(element);
			}
		}
		return Collections.unmodifiableList(result);
	}

	private ChainImpl() {
	}

	public ChainImpl(O root) {
		if (root == null) {
			throw new IllegalArgumentException();
		}
		this.positive.add(root);
	}

	private int updateStructuralVersion() {
		currentVersion++;
		return currentVersion;
	}

	public boolean contains(O data) {
		if (data == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < Math.max(positive.size(), negative.size()); i++) {
			if (i < positive.size() && data == positive.get(i)) {
				return true;
			}
			if (i < negative.size() && data == negative.get(i)) {
				return true;
			}
		}
		return false;
	}

	public Navigator<O> navigator(O data) {
		if (data == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < Math.max(positive.size(), negative.size()); i++) {
			if (i < positive.size() && data == positive.get(i)) {
				final InternalNavigator result = new InternalNavigator(i, currentVersion);
				assert result.get() == data;
				return result;
			}
			if (i < negative.size() && data == negative.get(i)) {
				final InternalNavigator result = new InternalNavigator(-i - 1, currentVersion);
				assert result.get() == data;
				return result;
			}
		}
		throw new IllegalArgumentException();
	}

	private O getInternal(int position) {
		ensure(position);
		if (position >= 0) {
			return positive.get(position);
		} else {
			return negative.get(-position - 1);
		}
	}

	private void setInternal(int position, O data) {
		if (data == null) {
			throw new IllegalArgumentException();
		}
		ensure(position);
		if (position >= 0) {
			positive.set(position, data);
		} else {
			negative.set(-position - 1, data);
		}
	}

	private void insertInternal(int position, O data) {
		if (data == null) {
			throw new IllegalArgumentException();
		}
		ensure(position);
		if (position >= 0) {
			positive.add(position, data);
		} else {
			negative.add(-position - 1, data);
		}
	}

	private void ensure(int position) {
		if (position >= 0) {
			ensureInternal(position, positive);
		} else {
			ensureInternal(-position - 1, negative);
		}
	}

	private void ensureInternal(int position, List<O> list) {
		assert position >= 0 : "position=" + position;
		while (list.size() <= position) {
			list.add(null);
		}
		assert list.size() > position;
		// Just check that list.get(position) does not throw Exception
		assert list.get(position) != this;
	}

	class InternalNavigator implements Navigator<O> {

		private int position = 0;
		private int version;

		private InternalNavigator(int position, int version) {
			this.position = position;
			this.version = version;
		}

		private void checkConsistency() {
			if (version != currentVersion) {
				throw new ConcurrentModificationException();
			}
		}

		public O next() {
			checkConsistency();
			position++;
			return get();
		}

		public O previous() {
			checkConsistency();
			position--;
			return get();
		}

		public O get() {
			checkConsistency();
			return getInternal(position);
		}

		public void set(O data) {
			checkConsistency();
			setInternal(position, data);
		}

		public void insertBefore(O data) {
			version = updateStructuralVersion();
			insertInternal(position, data);
		}

		public void insertAfter(O data) {
			version = updateStructuralVersion();
			insertInternal(position + 1, data);
		}
	}

}
