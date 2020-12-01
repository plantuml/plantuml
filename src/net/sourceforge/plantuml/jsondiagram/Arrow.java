/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  http://plantuml.com
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * This translation is distributed under the same Licence as the original C program.
 * 
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC
 * LICENSE ("AGREEMENT"). [Eclipse Public License - v 1.0]
 * 
 * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES
 * RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 * 
 * You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package net.sourceforge.plantuml.jsondiagram;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class Arrow {

	private final Point2D p1;
	private final Point2D p2;

	public Arrow(Point2D p1, Point2D p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public void drawArrow(UGraphic ug) {
		ug = ug.apply(new UStroke());

		final ULine p1p2 = new ULine(p1, p2);
		final double dist = p1.distance(p2);

		final double alpha = Math.atan2(p1p2.getDX(), p1p2.getDY());

		final double factor = .4;
		final double factor2 = .3;
		
		final Point2D p3 = getPoint(p1, alpha + Math.PI / 2, dist * factor);
		final Point2D p4 = getPoint(p1, alpha - Math.PI / 2, dist * factor);
		final Point2D p11 = getPoint(p1, alpha, dist * factor2);

		
		final UPath path = new UPath();
		path.moveTo(p4);
		path.lineTo(p11);
		path.lineTo(p3);
		path.lineTo(p2);
		path.lineTo(p4);
		path.closePath();

		ug.draw(path);

	}

	private Point2D getPoint(Point2D center, double alpha, double len) {
		final double x = center.getX() + len * Math.sin(alpha);
		final double y = center.getY() + len * Math.cos(alpha);
		return new Point2D.Double(x, y);
	}

}
