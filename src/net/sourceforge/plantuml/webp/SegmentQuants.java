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

import java.io.IOException;

public class SegmentQuants {

	private static DeltaQ get_delta_q(BoolDecoder bc, int prev)
			throws IOException {
		DeltaQ ret = new DeltaQ();
		ret.v = 0;
		ret.update = false;

		if (bc.readBit() > 0) {
			ret.v = bc.readLiteral(4);

			if (bc.readBit() > 0)
				ret.v = -ret.v;
		}

		/* Trigger a quantizer update if the delta-q value has changed */
		if (ret.v != prev)
			ret.update = true;

		return ret;
	}

	private int qIndex;

	private SegmentQuant[] segQuants = new SegmentQuant[Globals.MAX_MB_SEGMENTS];

	public SegmentQuants() {
		for (int x = 0; x < Globals.MAX_MB_SEGMENTS; x++)
			segQuants[x] = new SegmentQuant();
	}

	public int getqIndex() {
		return qIndex;
	}

	public SegmentQuant[] getSegQuants() {
		return segQuants;
	}

	public void parse(BoolDecoder bc, boolean segmentation_enabled,
			boolean mb_segement_abs_delta) throws IOException {
		qIndex = bc.readLiteral(7);
		boolean q_update = false;
		DeltaQ v = get_delta_q(bc, 0);
		int y1dc_delta_q = v.v;
		q_update = q_update || v.update;
		v = get_delta_q(bc, 0);
		int y2dc_delta_q = v.v;
		q_update = q_update || v.update;
		v = get_delta_q(bc, 0);
		int y2ac_delta_q = v.v;
		q_update = q_update || v.update;
		v = get_delta_q(bc, 0);
		int uvdc_delta_q = v.v;
		q_update = q_update || v.update;
		v = get_delta_q(bc, 0);
		int uvac_delta_q = v.v;
		q_update = q_update || v.update;

		for (SegmentQuant s : segQuants) {
			if (!segmentation_enabled) {
				s.setQindex(qIndex);
			} else if (!mb_segement_abs_delta) {
				s.setQindex(s.getQindex() + qIndex);
			}

			s.setY1dc(y1dc_delta_q);
			s.setY2dc(y2dc_delta_q);
			s.setY2ac_delta_q(y2ac_delta_q);
			s.setUvdc_delta_q(uvdc_delta_q);
			s.setUvac_delta_q(uvac_delta_q);

		}
	}

	public void setSegQuants(SegmentQuant[] segQuants) {
		this.segQuants = segQuants;
	}
}
