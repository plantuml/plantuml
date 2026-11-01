/* FramedBox.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/jlatexmath
 *
 * Copyright (C) 2009 DENIZET Calixte
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 *
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 *
 * Linking this library statically or dynamically with other modules
 * is making a combined work based on this library. Thus, the terms
 * and conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce
 * an executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under terms
 * of your choice, provided that you also meet, for each linked independent
 * module, the terms and conditions of the license of that module.
 * An independent module is a module which is not derived from or based
 * on this library. If you modify this library, you may extend this exception
 * to your version of the library, but you are not obliged to do so.
 * If you do not wish to do so, delete this exception statement from your
 * version.
 *
 */

package org.scilab.forge.jlatexmath;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

/**
 * A box representing a rotated box.
 */
public class ShadowBox extends FramedBox {

    private float shadowRule;

    public ShadowBox(FramedBox fbox, float shadowRule) {
        super(fbox.box, fbox.thickness, fbox.space);
        this.shadowRule = shadowRule;
        depth += shadowRule;
        width += shadowRule;
    }

    public void draw(Graphics2D g2, float x, float y) {
        float th = thickness / 2;
        box.draw(g2, x + space + thickness, y);
        Stroke st = g2.getStroke();
        g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        g2.draw(new Rectangle2D.Float(x + th, y - height + th, width - shadowRule - thickness, height + depth - shadowRule - thickness));
        float penth = (float) Math.abs(1 / g2.getTransform().getScaleX());
        g2.setStroke(new BasicStroke(penth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        g2.fill(new Rectangle2D.Float(x + shadowRule - penth, y + depth - shadowRule - penth, width - shadowRule, shadowRule));
        g2.fill(new Rectangle2D.Float(x + width - shadowRule - penth, y - height + th + shadowRule, shadowRule, depth + height - 2 * shadowRule - th));
        //drawDebug(g2, x, y);
        g2.setStroke(st);
    }

    public int getLastFontId() {
        return box.getLastFontId();
    }
}
/*

    public void draw(Graphics2D g2, float x, float y) {
	float th = thickness / 2;
	float sh = shadowRule / 2;
	box.draw(g2, x + space + thickness, y);
	Stroke st = g2.getStroke();
	g2.setStroke(new BasicStroke(shadowRule, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
	g2.draw(new Line2D.Float(x + shadowRule, y + depth - sh, x + width, y +  depth - sh));
	g2.draw(new Line2D.Float(x + width - sh, y - height + shadowRule, x + width - sh, y + depth - shadowRule));
	g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
	g2.draw(new Rectangle2D.Float(x + th, y - height + th, width - shadowRule - thickness, height + depth - shadowRule - thickness));
	//drawDebug(g2, x, y);
	g2.setStroke(st);
    }

*/
