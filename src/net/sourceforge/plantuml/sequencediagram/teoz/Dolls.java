/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.DollLeaf;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.ParticipantEnglober;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class Dolls {

	private final List<DollLeaf> dolls = new ArrayList<>();

	public Dolls(TileArguments tileArguments) {
		DollLeaf pending = null;
		for (Participant p : tileArguments.getLivingSpaces().participants()) {
			final ParticipantEnglober englober = tileArguments.getLivingSpaces().get(p).getEnglober();
			if (englober == null) {
				pending = null;
				continue;
			}
			assert englober != null;
			if (pending != null && englober == pending.getParticipantEnglober()) {
				pending.add(p);
				continue;
			}
			pending = DollLeaf.createTeoz(englober, p, tileArguments,
					tileArguments.getSkinParam().getCurrentStyleBuilder());
			dolls.add(pending);
		}
	}

	public int size() {
		return dolls.size();
	}

	public double getOffsetForEnglobers(StringBounder stringBounder) {
		double result = 0;
		for (DollLeaf doll : dolls) {
			final double height = doll.getPreferredHeight();
			if (height > result)
				result = height;

		}
		return result;
	}

	public void addConstraints(StringBounder stringBounder) {
		DollLeaf last = null;
		for (DollLeaf doll : dolls) {
			doll.addInternalConstraints();
			if (last != null)
				last.addConstraintAfter(doll);

			last = doll;
		}
	}

	public void drawEnglobers(UGraphic ug, double height, Context2D context) {
		for (DollLeaf doll : dolls)
			doll.drawMe(ug, height, context);

	}

	public Real getMinX(StringBounder stringBounder) {
		if (size() == 0)
			throw new IllegalStateException();

		final List<Real> result = new ArrayList<>();
		for (DollLeaf doll : dolls)
			result.add(doll.getMinX(stringBounder));

		return RealUtils.min(result);
	}

	public Real getMaxX(StringBounder stringBounder) {
		if (size() == 0)
			throw new IllegalStateException();

		final List<Real> result = new ArrayList<>();
		for (DollLeaf doll : dolls)
			result.add(doll.getMaxX(stringBounder));

		return RealUtils.max(result);
	}

}
