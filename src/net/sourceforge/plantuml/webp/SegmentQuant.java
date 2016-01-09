/*	This file is part of javavp8decoder.

    javavp8decoder is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    javavp8decoder is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with javavp8decoder.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.plantuml.webp;

public class SegmentQuant {
	private int filterStrength;
	private int Qindex;
	private int uvac;
	private int uvdc;
	private int y1ac;
	private int y1dc;
	private int y2ac;
	private int y2dc;

	private int clip(int val, int max) {
		int r = val;
		if (val > max)
			r = max;
		if (r < 0)
			r = 0;
		return r;
	}

	public int getQindex() {
		return Qindex;
	}

	public int getUvac_delta_q() {
		return uvac;
	}

	public int getUvdc_delta_q() {
		return uvdc;
	}

	public int getY1ac() {
		return y1ac;
	}

	public int getY1dc() {
		return y1dc;
	}

	public int getY2ac_delta_q() {
		return y2ac;
	}

	public int getY2dc() {
		return y2dc;
	}

	public void setFilterStrength(int value) {
		this.filterStrength = value;
	}

	public void setQindex(int qindex) {
		Qindex = qindex;
	}

	public void setUvac_delta_q(int uvac_delta_q) {
		this.uvac = Globals.vp8AcQLookup[clip(Qindex + uvac_delta_q, 127)];
	}

	public void setUvdc_delta_q(int uvdc_delta_q) {
		this.uvdc = Globals.vp8DcQLookup[clip(Qindex + uvdc_delta_q, 127)];
	}

	public void setY1ac() {
		this.y1ac = Globals.vp8AcQLookup[clip(Qindex, 127)];
	}

	public void setY1dc(int y1dc) {
		this.y1dc = Globals.vp8DcQLookup[clip(Qindex + y1dc, 127)];
		this.setY1ac();
	}

	public void setY2ac_delta_q(int y2ac_delta_q) {
		this.y2ac = Globals.vp8AcQLookup[clip(Qindex + y2ac_delta_q, 127)] * 155 / 100;
		if (this.y2ac < 8)
			this.y2ac = 8;
	}

	public void setY2dc(int y2dc_delta_q) {
		this.y2dc = Globals.vp8DcQLookup[clip(Qindex + y2dc_delta_q, 127)] * 2;
	}

	public int getFilterStrength() {
		return filterStrength;
	}
}
