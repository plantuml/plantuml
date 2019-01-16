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
package net.sourceforge.plantuml.hector2.mpos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.hector2.MinMax;
import net.sourceforge.plantuml.hector2.layering.Layer;

public class Distribution {

	private final List<Layer> layers;

	public Distribution(List<Layer> layers) {
		this.layers = new ArrayList<Layer>(layers);
	}

	public Distribution mute(MutationLayer mutation) {
		final Distribution result = new Distribution(this.layers);
		final int idx = result.layers.indexOf(mutation.getOriginal());
		if (idx == -1) {
			throw new IllegalArgumentException();
		}
		result.layers.set(idx, mutation.mute());
		return result;
	}

	public double cost(Collection<Link> links) {
		double result = 0;
		for (Link link : links) {
			result += getLength(link);
		}
		return result;
	}

	private double getLength(Link link) {
		final IEntity ent1 = link.getEntity1();
		final IEntity ent2 = link.getEntity2();
		final int y1 = ent1.getHectorLayer();
		final int x1 = layers.get(y1).getLongitude(ent1);
		final int y2 = ent2.getHectorLayer();
		final int x2 = layers.get(y2).getLongitude(ent2);
		final int dx = x2 - x1;
		final int dy = y2 - y1;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public List<MutationLayer> getPossibleMutations() {
		final List<MutationLayer> result = new ArrayList<MutationLayer>();
		for (Layer layer : layers) {
			result.addAll(layer.getPossibleMutations());
		}
		return Collections.unmodifiableList(result);
	}

	public final List<Layer> getLayers() {
		return Collections.unmodifiableList(layers);
	}

	public MinMax getMinMaxLongitudes() {
		MinMax result = null;
		for (Layer layer : layers) {
			if (result == null) {
				result = layer.getMinMaxLongitudes();
			} else {
				result = result.add(layer.getMinMaxLongitudes());
			}
		}
		return result;
	}

	public double getNbLayers() {
		return layers.size();
	}

}
