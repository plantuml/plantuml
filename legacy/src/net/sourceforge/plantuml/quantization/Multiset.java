package net.sourceforge.plantuml.quantization;

import java.util.Collection;
import java.util.Set;

/**
 * A collection which permits duplicates, and provides methods adding/removing
 * several counts of an element.
 *
 * @param <E> the element type
 */
public interface Multiset<E> extends Collection<E> {
	/**
	 * Add n counts of an element.
	 *
	 * @param element the element to add
	 * @param n       how many to add
	 */
	public void add(E element, int n);

	/**
	 * Remove up to n counts of an element.
	 *
	 * @param element the element the remove
	 * @param n       how many to remove
	 * @return the number of elements removed
	 */
	public int remove(Object element, int n);

	public int count(Object element);

	public Set<E> getDistinctElements();
}
