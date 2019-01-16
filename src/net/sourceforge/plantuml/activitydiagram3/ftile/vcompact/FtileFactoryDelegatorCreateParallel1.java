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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.ForkStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactoryDelegator;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;

public class FtileFactoryDelegatorCreateParallel1 extends FtileFactoryDelegator {

	public FtileFactoryDelegatorCreateParallel1(FtileFactory factory) {
		super(factory);
	}

	private Ftile allOverlapped(Swimlane swimlane, List<Ftile> all, ForkStyle style, String label) {
		return new FtileForkInnerOverlapped(all);
	}

	@Override
	public Ftile createParallel(Swimlane swimlane, List<Ftile> list, ForkStyle style, String label) {

		final Ftile inner = super.createParallel(swimlane, list, style, label);

		ParallelFtilesBuilder builder;

		if (style == ForkStyle.SPLIT) {
			builder = new ParallelBuilderSplit(skinParam(), getStringBounder(), list, inner, swimlane);
		} else if (style == ForkStyle.MERGE) {
			builder = new ParallelBuilderMerge(skinParam(), getStringBounder(), list, inner, swimlane);
		} else if (style == ForkStyle.FORK) {
			builder = new ParallelBuilderFork(skinParam(), getStringBounder(), list, inner, swimlane, label);
		} else {
			throw new IllegalStateException();
		}
		return builder.build();
	}

}
