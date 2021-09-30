package net.sourceforge.plantuml.utils;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public class CollectionUtils {

	public static <E> List<E> immutableList(E... elements) {
		return unmodifiableList(asList(elements));
	}
}
