/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6049 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.geom.Dimension2D;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class SegmentColored {

	final private Segment segment;
	final private HtmlColor backcolor;

	SegmentColored(double pos1, double pos2, HtmlColor backcolor) {
		this(new Segment(pos1, pos2), backcolor);
	}

	private SegmentColored(Segment segment, HtmlColor backcolor) {
		this.segment = segment;
		this.backcolor = backcolor;
	}

	public HtmlColor getSpecificBackColor() {
		return backcolor;
	}

	@Override
	public boolean equals(Object obj) {
		final SegmentColored this2 = (SegmentColored) obj;
		return this.segment.equals(this2.segment);
	}

	@Override
	public int hashCode() {
		return this.segment.hashCode();
	}

	@Override
	public String toString() {
		return this.segment.toString();
	}

	public void drawU(UGraphic ug, Component comp, int level) {
		final double atX = ug.getTranslateX();
		final double atY = ug.getTranslateY();

		final StringBounder stringBounder = ug.getStringBounder();
		ug.translate((level - 1) * comp.getPreferredWidth(stringBounder) / 2, segment.getPos1());
		final Dimension2D dim = new Dimension2DDouble(comp.getPreferredWidth(stringBounder), segment.getPos2()
				- segment.getPos1());
		comp.drawU(ug, dim, new SimpleContext2D(false));
		ug.setTranslate(atX, atY);
	}

	public Collection<SegmentColored> cutSegmentIfNeed(Collection<Segment> allDelays) {
		return new Coll2(segment.cutSegmentIfNeed(allDelays));
	}

	public SegmentColored merge(SegmentColored this2) {
		return new SegmentColored(this.segment.merge(this2.segment), backcolor);
	}

	public final Segment getSegment() {
		return segment;
	}


	class Iterator2 implements Iterator<SegmentColored> {

		private final Iterator<Segment> it;

		public Iterator2(Iterator<Segment> it) {
			this.it = it;
		}

		public boolean hasNext() {
			return it.hasNext();
		}

		public SegmentColored next() {
			return new SegmentColored(it.next(), backcolor);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	class Coll2 extends AbstractCollection<SegmentColored> {

		private final Collection<Segment> col;

		public Coll2(Collection<Segment> col) {
			this.col = col;
		}

		@Override
		public Iterator<SegmentColored> iterator() {
			return new Iterator2(col.iterator());
		}

		@Override
		public int size() {
			return col.size();
		}

	}

}
