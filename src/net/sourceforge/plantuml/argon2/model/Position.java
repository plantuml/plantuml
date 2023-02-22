/* 	This file is taken from
	https://github.com/andreas1327250/argon2-java

	Original Author: Andreas Gadermaier <up.gadermaier@gmail.com>
 */
package net.sourceforge.plantuml.argon2.model;

public class Position {

	public int pass;
	public int lane;
	public int slice;
	public int index;

	public Position(int pass, int lane, int slice, int index) {
		this.pass = pass;
		this.lane = lane;
		this.slice = slice;
		this.index = index;
	}
}
