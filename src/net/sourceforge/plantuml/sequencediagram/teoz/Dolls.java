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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Doll;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.ParticipantEnglober;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class Dolls {

	private final List<Doll> dolls = new ArrayList<>();

	private final Map<ParticipantEnglober, Doll> groups = new HashMap<>();

	public Dolls(TileArguments tileArguments) {
		Doll pending = null;
		for (Participant p : tileArguments.getLivingSpaces().participants()) {
			final ParticipantEnglober englober = tileArguments.getLivingSpaces().get(p).getEnglober();
			if (englober == null) {
				pending = null;
				continue;
			}
			assert englober != null;
			if (pending != null && englober == pending.getParticipantEnglober()) {
				pending.addParticipant(p);
				continue;
			}

			if (groups.containsKey(englober)) {
				groups.get(englober).addParticipant(p);
				continue;
			}

			final ParticipantEnglober parent = englober.getParent();
			pending = Doll.createTeoz(englober, p, tileArguments,
					tileArguments.getSkinParam().getCurrentStyleBuilder());
			if (parent != null && groups.containsKey(parent) == false)
				groups.put(parent, Doll.createGroup(parent, tileArguments,
						tileArguments.getSkinParam().getCurrentStyleBuilder(), true));

			if (parent != null)
				getParent(pending).addDoll(pending);

			dolls.add(pending);
		}
	}

	public int size() {
		return dolls.size();
	}

	public double getOffsetForEnglobers(StringBounder stringBounder) {
		double result = 0;
		for (Doll doll : dolls) {
			double height = doll.getTitlePreferredHeight();
			final Doll group = getParent(doll);
			if (group != null)
				height += group.getTitlePreferredHeight();

			if (height > result)
				result = height;

		}
		return result;
	}

	public void addConstraints(StringBounder stringBounder) {
		Doll last = null;
		for (Doll doll : dolls) {
			doll.addInternalConstraints();
			if (last != null)
				last.addConstraintAfter(doll);

			last = doll;
		}
	}

	private Doll getParent(Doll doll) {
		final ParticipantEnglober parent = doll.getParticipantEnglober().getParent();
		if (parent == null)
			return null;
		return groups.get(parent);
	}

	public void drawEnglobers(UGraphic ug, double height, Context2D context) {
		for (Doll group : groups.values()) {
			group.drawGroup(ug, height, context);

		}
//		DollGroup pending = null;
//		for (DollLeaf doll : dolls) {
//			final DollGroup group = doll.getGroup();
//			if (group==null) {
//				
//			}
//			// if (pending==null || pending.equals(group))
//			if (group != null) {
//				// group.drawMe(ug, height, context, doll.getX1().getCurrentValue(), doll.getX2().getCurrentValue());
//			}
//		}

		for (Doll doll : dolls)
			doll.drawMe(ug, height, context, getParent(doll));

	}

	public Real getMinX(StringBounder stringBounder) {
		if (size() == 0)
			throw new IllegalStateException();

		final List<Real> result = new ArrayList<>();
		for (Doll doll : dolls)
			result.add(doll.getMinX(stringBounder));

		return RealUtils.min(result);
	}

	public Real getMaxX(StringBounder stringBounder) {
		if (size() == 0)
			throw new IllegalStateException();

		final List<Real> result = new ArrayList<>();
		for (Doll doll : dolls)
			result.add(doll.getMaxX(stringBounder));

		return RealUtils.max(result);
	}

}
