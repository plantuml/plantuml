/*
 * $Id: PolylineShapeIterator.java 4065 2009-09-16 23:09:11Z psoares33 $
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

import org.openpdf.text.error_messages.MessageLocalization;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.NoSuchElementException;

/**
 * PathIterator for PolylineShape. This class was originally written by wil - amristar.com.au and integrated into iText
 * by Bruno.
 */
public class PolylineShapeIterator implements PathIterator {

    /**
     * The polyline over which we are going to iterate.
     */
    protected PolylineShape poly;
    /**
     * an affine transformation to be performed on the polyline.
     */
    protected AffineTransform affine;
    /**
     * the index of the current segment in the iterator.
     */
    protected int index;

    /**
     * Creates a PolylineShapeIterator.
     */
    PolylineShapeIterator(PolylineShape l, AffineTransform at) {
        this.poly = l;
        this.affine = at;
    }

    /**
     * Returns the coordinates and type of the current path segment in the iteration. The return value is the path
     * segment type: SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A double array of length 6 must be
     * passed in and may be used to store the coordinates of the point(s). Each point is stored as a pair of double x,y
     * coordinates. SEG_MOVETO and SEG_LINETO types will return one point, SEG_QUADTO will return two points,
     * SEG_CUBICTO will return 3 points and SEG_CLOSE will not return any points.
     *
     * @see #SEG_MOVETO
     * @see #SEG_LINETO
     * @see #SEG_QUADTO
     * @see #SEG_CUBICTO
     * @see #SEG_CLOSE
     * @see java.awt.geom.PathIterator#currentSegment(double[])
     */
    public int currentSegment(double[] coords) {
        if (isDone()) {
            throw new NoSuchElementException(MessageLocalization.getComposedMessage("line.iterator.out.of.bounds"));
        }
        int type = (index == 0) ? SEG_MOVETO : SEG_LINETO;
        coords[0] = poly.x[index];
        coords[1] = poly.y[index];
        if (affine != null) {
            affine.transform(coords, 0, coords, 0, 1);
        }
        return type;
    }

    /**
     * Returns the coordinates and type of the current path segment in the iteration. The return value is the path
     * segment type: SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A double array of length 6 must be
     * passed in and may be used to store the coordinates of the point(s). Each point is stored as a pair of double x,y
     * coordinates. SEG_MOVETO and SEG_LINETO types will return one point, SEG_QUADTO will return two points,
     * SEG_CUBICTO will return 3 points and SEG_CLOSE will not return any points.
     *
     * @see #SEG_MOVETO
     * @see #SEG_LINETO
     * @see #SEG_QUADTO
     * @see #SEG_CUBICTO
     * @see #SEG_CLOSE
     * @see java.awt.geom.PathIterator#currentSegment(float[])
     */
    public int currentSegment(float[] coords) {
        if (isDone()) {
            throw new NoSuchElementException(MessageLocalization.getComposedMessage("line.iterator.out.of.bounds"));
        }
        int type = (index == 0) ? SEG_MOVETO : SEG_LINETO;
        coords[0] = poly.x[index];
        coords[1] = poly.y[index];
        if (affine != null) {
            affine.transform(coords, 0, coords, 0, 1);
        }
        return type;
    }

    /**
     * Return the winding rule for determining the insideness of the path.
     *
     * @see #WIND_EVEN_ODD
     * @see #WIND_NON_ZERO
     * @see java.awt.geom.PathIterator#getWindingRule()
     */
    public int getWindingRule() {
        return WIND_NON_ZERO;
    }

    /**
     * Tests if there are more points to read.
     *
     * @return true if there are more points to read
     * @see java.awt.geom.PathIterator#isDone()
     */
    public boolean isDone() {
        return (index >= poly.np);
    }

    /**
     * Moves the iterator to the next segment of the path forwards along the primary direction of traversal as long as
     * there are more points in that direction.
     *
     * @see java.awt.geom.PathIterator#next()
     */
    public void next() {
        index++;
    }

}
