package org.luaj.vm2.python;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests of the Python subset runtime (lists, dicts, tuples, ranges, string
 * methods, numbers and builtins). Each program is run by {@link Interp}; the
 * expected output was produced by CPython 3 and is the reference.
 */
class PyUnitTest {

	private static String run(String source) {
		StringBuilder out = new StringBuilder();
		new Interp(out::append).run(source, "unit.py");
		return out.toString();
	}

	@Test
	void listOperations() {
		String source = "\na = [3, 1, 2]\na.append(4)\na.extend([5, 6])\na.insert(0, 0)\nprint(a)\nprint(a.pop())\nprint(a.pop(0))\nprint(a)\na.sort()\nprint(a)\na.reverse()\nprint(a)\nprint(a.index(3), a.count(2))\nprint(a[-1], a[1:3], a[-2:])\n";
		String expected = "[0, 3, 1, 2, 4, 5, 6]\n6\n0\n[3, 1, 2, 4, 5]\n[1, 2, 3, 4, 5]\n[5, 4, 3, 2, 1]\n2 1\n1 [4, 3] [2, 1]\n";
		assertEquals(expected, run(source));
	}

	@Test
	void listRemoveAndSliceAssign() {
		String source = "\na = [1, 2, 3, 2]\na.remove(2)\nprint(a)\ndel a[0]\nprint(a)\na[0:1] = [9, 9]\nprint(a)\n";
		String expected = "[1, 3, 2]\n[3, 2]\n[9, 9, 2]\n";
		assertEquals(expected, run(source));
	}

	@Test
	void dictOperations() {
		String source = "\nd = {'b': 2, 'a': 1}\nprint(d)\nd.update({'c': 3})\nprint(d.get('x', 0), d.get('a'))\nprint(d.pop('b'))\nprint(d)\nprint(d.setdefault('z', 26))\nprint(sorted(d.items()))\nprint('a' in d, 'q' in d)\nfor k in d:\n    print(k)\n";
		String expected = "{'b': 2, 'a': 1}\n0 1\n2\n{'a': 1, 'c': 3}\n26\n[('a', 1), ('c', 3), ('z', 26)]\nTrue False\na\nc\nz\n";
		assertEquals(expected, run(source));
	}

	@Test
	void tupleAndRange() {
		String source = "\nt = (1, 2, 3)\nprint(t[1:], len(t), t + (4,))\nprint(list(range(2, 10, 3)))\nprint(list(reversed(range(3))))\nprint(tuple(zip([1, 2], 'ab')))\n";
		String expected = "(2, 3) 3 (1, 2, 3, 4)\n[2, 5, 8]\n[2, 1, 0]\n((1, 'a'), (2, 'b'))\n";
		assertEquals(expected, run(source));
	}

	@Test
	void stringMethods() {
		String source = "\ns = '  Hello, World  '\nprint(s.strip().lower().replace('o', '0'))\nprint(','.join(['a', 'b']))\nprint('a,b,,c'.split(','))\nprint('x'.center(5, '*'))\nprint('abc'.upper().startswith('AB'))\nprint('%d-%s' % (7, 'x'))\nprint('{:>4}|{:<3}|'.format(5, 'a'))\n";
		String expected = "hell0, w0rld\na,b\n['a', 'b', '', 'c']\n**x**\nTrue\n7-x\n   5|a  |\n";
		assertEquals(expected, run(source));
	}

	@Test
	void numbersAndBuiltins() {
		String source = "\nprint(7 // 2, -7 // 2, 7 % 3, -7 % 3, 2 ** 10, 7 / 2)\nprint(abs(-3), round(2.5), round(3.5), divmod(17, 5))\nprint(int('42') + 1, float('1.5') * 2, hex(255), bin(5), oct(8))\nprint(max([3, 9, 2]), min(4, 2), sum(range(5)))\n";
		String expected = "3 -4 1 2 1024 3.5\n3 2 4 (3, 2)\n43 3.0 0xff 0b101 0o10\n9 2 10\n";
		assertEquals(expected, run(source));
	}

	@Test
	void comprehensionsAndClosures() {
		String source = "\nprint([x * x for x in range(4) if x % 2 == 0])\nprint({k: v for k, v in [('a', 1), ('b', 2)]})\ndef make(n):\n    return lambda x: x + n\nprint(make(3)(4))\nprint(sorted(['bb', 'a', 'ccc'], key=len))\n";
		String expected = "[0, 4]\n{'a': 1, 'b': 2}\n7\n['a', 'bb', 'ccc']\n";
		assertEquals(expected, run(source));
	}
}
