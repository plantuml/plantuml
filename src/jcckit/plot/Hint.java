/*
 * Copyright 2003-2004, Franz-Josef Elmer, All rights reserved
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details
 * (http://www.gnu.org/copyleft/lesser.html).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package jcckit.plot;


/**
 * Marker interface of all types of hints. Hints are used to calculate 
 * {@link jcckit.graphic.GraphicalElement} representing a point in a {@link
 * Curve}. For example, in a chart with stacked
 * bars the data determines the height of a bar but the foot of
 * a bar is determined by the height of the bar below. Its value will be
 * stored in a {@link PositionHint}.
 * 
 * @author Franz-Josef Elmer
 */
public interface Hint {}
