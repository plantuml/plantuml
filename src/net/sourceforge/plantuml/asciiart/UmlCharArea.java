/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.asciiart;

import java.util.Collection;

public interface UmlCharArea extends BasicCharArea {

	int STICKMAN_HEIGHT = 5;
	int STICKMAN_UNICODE_HEIGHT = 6;

	void drawBoxSimple(int x, int y, int width, int height);

	void drawBoxSimpleUnicode(int x, int y, int width, int height);

	void drawNoteSimple(int x, int y, int width, int height);

	void drawNoteSimpleUnicode(int x, int y, int width, int height);

	void drawStickMan(int x, int y);

	void drawStickManUnicode(int x, int y);

	void drawStringsLR(Collection<? extends CharSequence> strings, int x, int y);

}