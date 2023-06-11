/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  https://plantuml.com
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
package net.sourceforge.plantuml.gitlog;

import java.util.ArrayList;
import java.util.List;

import h.ST_Agedgeinfo_t;
import h.ST_bezier;
import h.ST_pointf;
import h.ST_splines;
import net.sourceforge.plantuml.jsondiagram.Arrow;
import net.sourceforge.plantuml.jsondiagram.Mirror;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;

public class GitCurve {

	private final List<XPoint2D> points = new ArrayList<>();

	private final Mirror xMirror;

	private final XPoint2D sp;
	private final XPoint2D ep;

	public GitCurve(ST_Agedgeinfo_t data, Mirror xMirror) {
		this.xMirror = xMirror;
		final ST_splines splines = data.spl;
		if (splines.size != 1)
			throw new IllegalStateException();

		final ST_bezier beziers = splines.list.get__(0);
		for (int i = 0; i < beziers.size; i++) {
			final XPoint2D pt = getPoint(splines, i);
			points.add(pt);
		}

		if (beziers.sp.x == 0 && beziers.sp.y == 0)
			sp = null;
		else
			sp = new XPoint2D(beziers.sp.x, beziers.sp.y);

		if (beziers.ep.x == 0 && beziers.ep.y == 0)
			ep = null;
		else
			ep = new XPoint2D(beziers.ep.x, beziers.ep.y);

	}

	private XPoint2D getPoint(ST_splines splines, int i) {
		final ST_bezier beziers = splines.list.get__(0);
		final ST_pointf pt = beziers.list.get__(i);
		return new XPoint2D(pt.x, pt.y);
	}

	public void drawCurve(HColor color, UGraphic ug) {
		ug = ug.apply(new UStroke(2, 2, 1));
		final UPath path = UPath.none();

		path.moveTo(xMirror.invGit(points.get(0)));

		for (int i = 1; i < points.size(); i += 3) {
			final XPoint2D pt2 = xMirror.invGit(points.get(i));
			final XPoint2D pt3 = xMirror.invGit(points.get(i + 1));
			final XPoint2D pt4 = xMirror.invGit(points.get(i + 2));
			path.cubicTo(pt2, pt3, pt4);
		}
		ug.draw(path);

		if (ep != null) {
			final XPoint2D last = xMirror.invGit(points.get(points.size() - 1));
			final XPoint2D trueEp = xMirror.invGit(ep);
			new Arrow(last, trueEp).drawArrow(ug.apply(color.bg()));
		}
	}

}
