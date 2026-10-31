package org.luaj.vm2.python;

/** A variable shared between a function and the functions nested in it. A null value means "unbound". */
final class Cell {
	Object value;
}
