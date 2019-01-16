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
package net.sourceforge.plantuml.graph2;

/*
 * Copyright (c) 2009 the authors listed at the following URL, and/or the
 * authors of referenced articles or incorporated external code:
 * http://en.literateprograms.org/Dijkstra's_algorithm_(Java)?action=history&offset=20081113161332
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * Retrieved from:
 * http://en.literateprograms.org/Dijkstra's_algorithm_(Java)?oldid=15444
 */

// http://www.google.fr/search?hl=fr&source=hp&q=A+star+java&btnG=Recherche+Google&meta=&aq=f&oq=
// http://www.edenwaith.com/products/pige/tutorials/a-star.php
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {

	static class Vertex implements Comparable<Vertex> {
		private final Object data;
		private final List<Edge> adjacencies = new ArrayList<Edge>();
		private double minDistance = Double.POSITIVE_INFINITY;
		private Vertex previous;

		Vertex(Object data) {
			this.data = data;
		}

		public void addAdjacencies(Vertex target, double dist) {
			if (target == null) {
				throw new IllegalArgumentException();
			}
			adjacencies.add(new Edge(target, dist));
		}

		public String toString() {
			return "[ " + data.toString() + " (" + minDistance + ") ] ";
		}

		public int compareTo(Vertex other) {
			return Double.compare(minDistance, other.minDistance);
		}

		public final Object getData() {
			return data;
		}

	}

	static class Edge {
		private final Vertex target;
		private final double weight;

		Edge(Vertex argTarget, double argWeight) {
			target = argTarget;
			weight = argWeight;
		}
	}

	private final List<Vertex> vertices = new ArrayList<Vertex>();

	public Vertex addVertex(Object data) {
		final Vertex v = new Vertex(data);
		vertices.add(v);
		return v;
	}

	private void computePaths(Vertex source) {
		source.minDistance = 0.;
		final PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(source);

		while (vertexQueue.isEmpty() == false) {
			final Vertex u = vertexQueue.poll();

			// Visit each edge exiting u
			for (Edge e : u.adjacencies) {
				final Vertex v = e.target;
				final double weight = e.weight;
				final double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
					vertexQueue.remove(v);

					v.minDistance = distanceThroughU;
					v.previous = u;
					vertexQueue.add(v);
				}
			}
		}
	}

	public List<Vertex> getShortestPathTo(Vertex source, Vertex target) {
		computePaths(source);
		final List<Vertex> path = new ArrayList<Vertex>();
		for (Vertex vertex = target; vertex != null; vertex = vertex.previous) {
			path.add(0, vertex);
		}

		return path;
	}

}
