/*
 * $Id: PolylineShape.java 3117 2008-01-31 05:53:22Z xlv $
 *
 * Copyright 2007 Bruno Lowagie and Wil
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * https://github.com/LibrePDF/OpenPDF
 */

package org.openpdf.text.pdf.internal;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Class that defines a Polyline shape. This class was originally written by wil - amristar.com.au and integrated into
 * iText by Bruno.
 */
public class PolylineShape implements Shape {

    /**
     * All the X-values of the coordinates in the polyline.
     */
    protected int[] x;
    /**
     * All the Y-values of the coordinates in the polyline.
     */
    protected int[] y;
    /**
     * The total number of points.
     */
    protected int np;

    /**
     * Creates a PolylineShape.
     *
     * @param nPoints The total number of points
     * @param x       An int array containing all the X-values of the coordinates in the polyline
     * @param y       An int array containing all the Y-values of the coordinates in the polyline
     */
    public PolylineShape(int[] x, int[] y, int nPoints) {
        // Should copy array (as done in Polygon)
        this.np = nPoints;
        // Take a copy.
        this.x = new int[np];
        this.y = new int[np];
        System.arraycopy(x, 0, this.x, 0, np);
        System.arraycopy(y, 0, this.y, 0, np);
    }

    /**
     * Returns the bounding box of this polyline.
     *
     * @return a {@link Rectangle2D} that is the high-precision bounding box of this line.
     * @see java.awt.Shape#getBounds2D()
     */
    public Rectangle2D getBounds2D() {
        int[] r = rect();
        return r == null ? null : new Rectangle2D.Double(r[0], r[1], r[2], r[3]);
    }

    /**
     * Returns the bounding box of this polyline.
     *
     * @see java.awt.Shape#getBounds()
     */
    public Rectangle getBounds() {
        return getBounds2D().getBounds();
    }

    /**
     * Calculates the origin (X, Y) and the width and height of a rectangle that contains all the segments of the
     * polyline.
     */
    private int[] rect() {
        if (np == 0) {
            return null;
        }
        int xMin = x[0], yMin = y[0], xMax = x[0], yMax = y[0];

        for (int i = 1; i < np; i++) {
            if (x[i] < xMin) {
                xMin = x[i];
            } else if (x[i] > xMax) {
                xMax = x[i];
            }
            if (y[i] < yMin) {
                yMin = y[i];
            } else if (y[i] > yMax) {
                yMax = y[i];
            }
        }

        return new int[]{xMin, yMin, xMax - xMin, yMax - yMin};
    }

    /**
     * A polyline can't contain a point.
     *
     * @see java.awt.Shape#contains(double, double)
     */
    public boolean contains(double x, double y) {
        return false;
    }

    /**
     * A polyline can't contain a point.
     *
     * @see java.awt.Shape#contains(java.awt.geom.Point2D)
     */
    public boolean contains(Point2D p) {
        return false;
    }

    /**
     * A polyline can't contain a point.
     *
     * @see java.awt.Shape#contains(double, double, double, double)
     */
    public boolean contains(double x, double y, double w, double h) {
        return false;
    }

    /**
     * A polyline can't contain a point.
     *
     * @see java.awt.Shape#contains(java.awt.geom.Rectangle2D)
     */
    public boolean contains(Rectangle2D r) {
        return false;
    }

    /**
     * Checks if one of the lines in the polyline intersects with a given rectangle.
     *
     * @see java.awt.Shape#intersects(double, double, double, double)
     */
    public boolean intersects(double x, double y, double w, double h) {
        return intersects(new Rectangle2D.Double(x, y, w, h));
    }

    /**
     * Checks if one of the lines in the polyline intersects with a given rectangle.
     *
     * @see java.awt.Shape#intersects(java.awt.geom.Rectangle2D)
     */
    public boolean intersects(Rectangle2D r) {
        if (np == 0) {
            return false;
        }
        Line2D line = new Line2D.Double(x[0], y[0], x[0], y[0]);
        for (int i = 1; i < np; i++) {
            line.setLine(x[i - 1], y[i - 1], x[i], y[i]);
            if (line.intersects(r)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an iteration object that defines the boundary of the polyline.
     *
     * @param at the specified {@link AffineTransform}
     * @return a {@link PathIterator} that defines the boundary of this polyline.
     * @see java.awt.Shape#intersects(java.awt.geom.Rectangle2D)
     */
    public PathIterator getPathIterator(AffineTransform at) {
        return new PolylineShapeIterator(this, at);
    }

    /**
     * There's no difference with getPathIterator(AffineTransform at); we just need this method to implement the Shape
     * interface.
     */
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new PolylineShapeIterator(this, at);
    }

}

