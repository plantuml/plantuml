package net.sourceforge.plantuml.quantization;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public final class HashMultiset<E> extends AbstractCollection<E> implements Multiset<E> {
	private final Map<E, Count> elementCounts = new HashMap<>();
	private int size;

	public HashMultiset() {
	}

	public HashMultiset(Collection<E> source) {
		addAll(source);
	}

	@Override
	public void add(E element, int n) {
		Count count = elementCounts.get(element);
		if (count != null)
			count.value += n;
		else
			elementCounts.put(element, new Count(n));

		size += n;
	}

	@Override
	public boolean add(E element) {
		add(element, 1);
		return true;
	}

	@Override
	public int remove(Object element, int n) {
		Count count = elementCounts.get(element);
		if (count == null)
			return 0;

		if (n < count.value) {
			count.value -= n;
			size -= n;
			return n;
		}

		elementCounts.remove(element);
		size -= count.value;
		return count.value;
	}

	@Override
	public boolean remove(Object element) {
		return remove(element, 1) > 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new HashMultisetIterator();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public int count(Object element) {
		Count countOrNull = elementCounts.get(element);
		return countOrNull != null ? countOrNull.value : 0;
	}

	@Override
	public Set<E> getDistinctElements() {
		return elementCounts.keySet();
	}

	private final class HashMultisetIterator implements Iterator<E> {
		final private Iterator<Map.Entry<E, Count>> distinctElementIterator;
		private E currentElement;
		private int currentCount;
		private boolean currentElementRemoved;

		HashMultisetIterator() {
			this.distinctElementIterator = elementCounts.entrySet().iterator();
			this.currentCount = 0;
		}

		@Override
		public boolean hasNext() {
			return currentCount > 0 || distinctElementIterator.hasNext();
		}

		@Override
		public E next() {
			if (hasNext() == false)
				throw new NoSuchElementException("iterator has been exhausted");

			if (currentCount == 0) {
				Map.Entry<E, Count> next = distinctElementIterator.next();
				currentElement = next.getKey();
				currentCount = next.getValue().value;
			}

			currentCount--;
			currentElementRemoved = false;
			return currentElement;
		}

		@Override
		public void remove() {
			if (currentElement == null)
				throw new IllegalStateException("next() has not been called");

			if (currentElementRemoved)
				throw new IllegalStateException("remove() already called for current element");

			HashMultiset.this.remove(currentElement);
		}
	}

	private static final class Count {
		private int value;

		Count(int value) {
			this.value = value;
		}
	}
}
