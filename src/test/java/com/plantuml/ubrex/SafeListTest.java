package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SafeListTest {

	@Test
	public void test1() {
		assertEquals(0, SafeList.createEmpty().size());
	}

	@Test
	public void testAdd() {
		final SafeList<String> list1 = SafeList.createEmpty();
		final SafeList<String> list2 = list1.add("Alpha");
		final SafeList<String> list3 = list2.add("Beta");

		assertEquals(0, list1.size());
		assertEquals(1, list2.size());
		assertEquals(2, list3.size());

		assertEquals("[]", list1.toString());
		assertEquals("[Alpha]", list2.toString());
		assertEquals("[Alpha, Beta]", list3.toString());
	}

	@Test
	public void testMerge() {
		final SafeList<String> listABC = SafeList.<String>createEmpty().add("A").add("B").add("C");
		final SafeList<String> listDE = SafeList.<String>createEmpty().add("D").add("E");

		assertEquals("[A, B, C]", listABC.toString());
		assertEquals("[D, E]", listDE.toString());
		
		final SafeList<String> listABCDE = listABC.addAll(listDE);
		assertEquals("[A, B, C]", listABC.toString());
		assertEquals("[D, E]", listDE.toString());
		assertEquals("[A, B, C, D, E]", listABCDE.toString());

	}

}
