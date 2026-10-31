/*
 * $Id: PdfContentByte.java 4065 2009-09-16 23:09:11Z psoares33 $
 *
 * Copyright 1999, 2000, 2001, 2002 Bruno Lowagie
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

package org.openpdf.text.pdf;

import static org.openpdf.text.pdf.ExtendedColor.MAX_COLOR_VALUE;
import static org.openpdf.text.pdf.ExtendedColor.MAX_FLOAT_COLOR_VALUE;
import static org.openpdf.text.pdf.ExtendedColor.MAX_INT_COLOR_VALUE;

import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Image;
import org.openpdf.text.ImgJBIG2;
import org.openpdf.text.Rectangle;
import org.openpdf.text.error_messages.MessageLocalization;
import org.openpdf.text.exceptions.IllegalPdfSyntaxException;
import org.openpdf.text.pdf.internal.PdfXConformanceImp;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <CODE>PdfContentByte</CODE> is an object containing the user positioned
 * text and graphic contents of a page. It knows how to apply the proper font encoding.
 */

public class PdfContentByte {

    /**
     * The alignment is center
     */
    public static final int ALIGN_CENTER = Element.ALIGN_CENTER;
    /**
     * The alignment is left
     */
    public static final int ALIGN_LEFT = Element.ALIGN_LEFT;
    /**
     * The alignment is right
     */
    public static final int ALIGN_RIGHT = Element.ALIGN_RIGHT;
    /**
     * A possible line cap value
     */
    public static final int LINE_CAP_BUTT = 0;
    /**
     * A possible line join value
     */
    public static final int LINE_JOIN_MITER = 0;
    static final float MIN_FONT_SIZE = 0.0001f;
    private static final Map<PdfName, String> abrev = new HashMap<>();
    // membervariables

    static {
        abrev.put(PdfName.BITSPERCOMPONENT, "/BPC ");
        abrev.put(PdfName.COLORSPACE, "/CS ");
        abrev.put(PdfName.DECODE, "/D ");
        abrev.put(PdfName.DECODEPARMS, "/DP ");
        abrev.put(PdfName.FILTER, "/F ");
        abrev.put(PdfName.HEIGHT, "/H ");
        abrev.put(PdfName.IMAGEMASK, "/IM ");
        abrev.put(PdfName.INTENT, "/Intent ");
        abrev.put(PdfName.INTERPOLATE, "/I ");
        abrev.put(PdfName.WIDTH, "/W ");
    }

    /**
     * This is the actual content
     */
    protected ByteBuffer content = new ByteBuffer();
    /**
     * This is the writer
     */
    protected PdfWriter writer;
    /**
     * This is the PdfDocument
     */
    protected PdfDocument pdf;
    /**
     * This is the GraphicState in use
     */
    protected GraphicState state = new GraphicState();
    /**
     * The list were we save/restore the state
     */
    protected List<GraphicState> stateList = new ArrayList<>();

    /**
     * The separator between commands.
     */
    protected int separator = '\n';
    private int mcDepth = 0;
    private boolean inText = false;
    private int lastFillAlpha = MAX_COLOR_VALUE;
    private int lastStrokeAlpha = MAX_COLOR_VALUE;


    /**
     * Constructs a new <CODE>PdfContentByte</CODE>-object.
     *
     * @param wr the writer associated to this content
     */

    public PdfContentByte(PdfWriter wr) {
        if (wr != null) {
            writer = wr;
            pdf = writer.getPdfDocument();
        }
    }

    // constructors

    // methods to get the content of this object

    /**
     * Escapes a <CODE>byte</CODE> array according to the PDF conventions.
     *
     * @param b the <CODE>byte</CODE> array to escape
     * @return an escaped <CODE>byte</CODE> array
     */
    static byte[] escapeString(byte[] b) {
        ByteBuffer content = new ByteBuffer();
        escapeAndAppendString(b, content);
        return content.toByteArray();
    }

    /**
     * Escapes a <CODE>byte</CODE> array according to the PDF conventions and append it to <CODE>content</CODE>
     *
     * @param b       the <CODE>byte</CODE> array to escape
     * @param content the content to append the escaped string
     */
    static void escapeAndAppendString(byte[] b, ByteBuffer content) {
        content.append_i('(');
        for (byte c : b) {
            switch (c) {
                case '\r':
                    content.append("\\r");
                    break;
                case '\n':
                    content.append("\\n");
                    break;
                case '\t':
                    content.append("\\t");
                    break;
                case '\b':
                    content.append("\\b");
                    break;
                case '\f':
                    content.append("\\f");
                    break;
                case '(':
                case ')':
                case '\\':
                    content.append_i('\\').append_i(c);
                    break;
                default:
                    content.append_i(c);
            }
        }
        content.append(")");
    }

    /**
     * Generates an array of bezier curves to draw an arc.
     * <p>
     * (x1, y1) and (x2, y2) are the corners of the enclosing rectangle. Angles, measured in degrees, start with 0 to
     * the right (the positive X axis) and increase counter-clockwise.  The arc extends from startAng to
     * startAng+extent.  I.e. startAng=0 and extent=180 yields an openside-down semi-circle.
     * <p>
     * The resulting coordinates are of the form float[]{x1,y1,x2,y2,x3,y3, x4,y4} such that the curve goes from (x1,
     * y1) to (x4, y4) with (x2, y2) and (x3, y3) as their respective Bezier control points.
     * <p>
     * Note: this code was taken from ReportLab (www.reportlab.org), an excellent PDF generator for Python (BSD license:
     * http://www.reportlab.org/devfaq.html#1.3 ).
     *
     * @param x1       a corner of the enclosing rectangle
     * @param y1       a corner of the enclosing rectangle
     * @param x2       a corner of the enclosing rectangle
     * @param y2       a corner of the enclosing rectangle
     * @param startAng starting angle in degrees
     * @param extent   angle extent in degrees
     * @return a list of float[] with the bezier curves
     */
    public static List<float[]> bezierArc(float x1, float y1, float x2, float y2, float startAng, float extent) {
        float tmp;
        if (x1 > x2) {
            tmp = x1;
            x1 = x2;
            x2 = tmp;
        }
        if (y2 > y1) {
            tmp = y1;
            y1 = y2;
            y2 = tmp;
        }

        float fragAngle;
        int Nfrag;
        if (Math.abs(extent) <= 90f) {
            fragAngle = extent;
            Nfrag = 1;
        } else {
            Nfrag = (int) (Math.ceil(Math.abs(extent) / 90f));
            fragAngle = extent / Nfrag;
        }
        float x_cen = (x1 + x2) / 2f;
        float y_cen = (y1 + y2) / 2f;
        float rx = (x2 - x1) / 2f;
        float ry = (y2 - y1) / 2f;
        float halfAng = (float) (fragAngle * Math.PI / 360.);
        float kappa = (float) (Math.abs(4. / 3. * (1. - Math.cos(halfAng)) / Math.sin(halfAng)));
        List<float[]> pointList = new ArrayList<>();
        for (int i = 0; i < Nfrag; ++i) {
            float theta0 = (float) ((startAng + i * fragAngle) * Math.PI / 180.);
            float theta1 = (float) ((startAng + (i + 1) * fragAngle) * Math.PI / 180.);
            float cos0 = (float) Math.cos(theta0);
            float cos1 = (float) Math.cos(theta1);
            float sin0 = (float) Math.sin(theta0);
            float sin1 = (float) Math.sin(theta1);
            if (fragAngle > 0f) {
                pointList.add(new float[]{x_cen + rx * cos0,
                        y_cen - ry * sin0,
                        x_cen + rx * (cos0 - kappa * sin0),
                        y_cen - ry * (sin0 + kappa * cos0),
                        x_cen + rx * (cos1 + kappa * sin1),
                        y_cen - ry * (sin1 - kappa * cos1),
                        x_cen + rx * cos1,
                        y_cen - ry * sin1});
            } else {
                pointList.add(new float[]{x_cen + rx * cos0,
                        y_cen - ry * sin0,
                        x_cen + rx * (cos0 + kappa * sin0),
                        y_cen - ry * (sin0 - kappa * cos0),
                        x_cen + rx * (cos1 - kappa * sin1),
                        y_cen - ry * (sin1 + kappa * cos1),
                        x_cen + rx * cos1,
                        y_cen - ry * sin1});
            }
        }
        return pointList;
    }

    // methods to add graphical content

    /**
     * Returns the <CODE>String</CODE> representation of this <CODE>PdfContentByte</CODE>-object.
     *
     * @return a <CODE>String</CODE>
     */

    public String toString() {
        return content.toString();
    }

    /**
     * Gets the internal buffer.
     *
     * @return the internal buffer
     */
    public ByteBuffer getInternalBuffer() {
        return content;
    }

    /**
     * Returns the PDF representation of this <CODE>PdfContentByte</CODE>-object.
     *
     * @param writer the <CODE>PdfWriter</CODE>
     * @return a <CODE>byte</CODE> array with the representation
     */

    public byte[] toPdf(PdfWriter writer) {
        sanityCheck();
        return content.toByteArray();
    }

    /**
     * Adds the content of another <CODE>PdfContent</CODE>-object to this object.
     *
     * @param other another <CODE>PdfByteContent</CODE>-object
     */

    /**
     * Changes the <VAR>Flatness</VAR>.
     * <p>
     * <VAR>Flatness</VAR> sets the maximum permitted distance in device pixels between the
     * mathematically correct path and an approximation constructed from straight line segments.<BR>
     *
     * @param flatness a value
     */

    /**
     * Changes the <VAR>Line cap style</VAR>.
     * <p>
     * The <VAR>line cap style</VAR> specifies the shape to be used at the end of open subpaths when they are
     * stroked.<BR> Allowed values are LINE_CAP_BUTT, LINE_CAP_ROUND and LINE_CAP_PROJECTING_SQUARE.<BR>
     *
     * @param style a value
     */

    public void setLineCap(int style) {
        if (style >= 0 && style <= 2) {
            content.append(style).append(" J").append_i(separator);
        }
    }

    /**
     * Changes the value of the <VAR>line dash pattern</VAR>.
     * <p>
     * The line dash pattern controls the pattern of dashes and gaps used to stroke paths. It is specified by an
     * <I>array</I> and a <I>phase</I>. The array specifies the length of the alternating dashes and gaps. The phase
     * specifies the distance into the dash pattern to start the dash.<BR>
     *
     * @param phase the value of the phase
     */

    public void setLineDash(float phase) {
        content.append("[] ").append(phase).append(" d").append_i(separator);
    }

    /**
     * Changes the value of the <VAR>line dash pattern</VAR>.
     * <p>
     * The line dash pattern controls the pattern of dashes and gaps used to stroke paths. It is specified by an
     * <I>array</I> and a <I>phase</I>. The array specifies the length of the alternating dashes and gaps. The phase
     * specifies the distance into the dash pattern to start the dash.<BR>
     *
     * @param phase   the value of the phase
     * @param unitsOn the number of units that must be 'on' (equals the number of units that must be 'off').
     */

    /**
     * Changes the value of the <VAR>line dash pattern</VAR>.
     * <p>
     * The line dash pattern controls the pattern of dashes and gaps used to stroke paths. It is specified by an
     * <I>array</I> and a <I>phase</I>. The array specifies the length of the alternating dashes and gaps. The phase
     * specifies the distance into the dash pattern to start the dash.<BR>
     *
     * @param phase    the value of the phase
     * @param unitsOn  the number of units that must be 'on'
     * @param unitsOff the number of units that must be 'off'
     */

    public void setLineDash(float unitsOn, float unitsOff, float phase) {
        content.append("[").append(unitsOn).append(' ').append(unitsOff).append("] ").append(phase).append(" d")
                .append_i(separator);
    }

    /**
     * Changes the value of the <VAR>line dash pattern</VAR>.
     * <p>
     * The line dash pattern controls the pattern of dashes and gaps used to stroke paths. It is specified by an
     * <I>array</I> and a <I>phase</I>. The array specifies the length of the alternating dashes and gaps. The phase
     * specifies the distance into the dash pattern to start the dash.<BR>
     *
     * @param array length of the alternating dashes and gaps
     * @param phase the value of the phase
     */

    /**
     * Changes the <VAR>Line join style</VAR>.
     * <p>
     * The <VAR>line join style</VAR> specifies the shape to be used at the corners of paths that are stroked.<BR>
     * Allowed values are LINE_JOIN_MITER (Miter joins), LINE_JOIN_ROUND (Round joins) and LINE_JOIN_BEVEL (Bevel
     * joins).<BR>
     *
     * @param style a value
     */

    public void setLineJoin(int style) {
        if (style >= 0 && style <= 2) {
            content.append(style).append(" j").append_i(separator);
        }
    }

    /**
     * Changes the <VAR>line width</VAR>.
     * <p>
     * The line width specifies the thickness of the line used to stroke a path and is measured in user space
     * units.<BR>
     *
     * @param w a width
     */

    public void setLineWidth(float w) {
        content.append(w).append(" w").append_i(separator);
    }

    /**
     * Changes the <VAR>Miter limit</VAR>.
     * <p>
     * When two line segments meet at a sharp angle and mitered joins have been specified as the line join style, it is
     * possible for the miter to extend far beyond the thickness of the line stroking path. The miter limit imposes a
     * maximum on the ratio of the miter length to the line witdh. When the limit is exceeded, the join is converted
     * from a miter to a bevel.<BR>
     *
     * @param miterLimit a miter limit
     */

    /**
     * Modify the current clipping path by intersecting it with the current path, using the nonzero winding number rule
     * to determine which regions lie inside the clipping path.
     */

    /**
     * Modify the current clipping path by intersecting it with the current path, using the even-odd rule to determine
     * which regions lie inside the clipping path.
     */

    /**
     * Changes the currentgray tint for filling paths (device dependent colors!).
     * <p>
     * Sets the color space to <B>DeviceGray</B> (or the <B>DefaultGray</B> color space), and sets the gray tint to use
     * for filling paths.</P>
     *
     * @param gray a value between 0 (black) and 1 (white)
     */

    public void setGrayFill(float gray, float alpha) {
        saveColorFill(new GrayColor(gray, alpha));
        content.append(gray).append(" g").append_i(separator);
    }

    /**
     * Changes the current gray tint for filling paths to black.
     */

    public void resetGrayFill() {
        saveColorFill(GrayColor.GRAYBLACK);
        content.append("0 g").append_i(separator);
    }

    /**
     * Changes the currentgray tint for stroking paths (device dependent colors!).
     * <p>
     * Sets the color space to <B>DeviceGray</B> (or the <B>DefaultGray</B> color space), and sets the gray tint to use
     * for stroking paths.</P>
     *
     * @param gray a value between 0 (black) and 1 (white)
     */

    public void setGrayStroke(float gray, float alpha) {
        saveColorStroke(new GrayColor(gray, alpha));
        content.append(gray).append(" G").append_i(separator);
    }

    /**
     * Changes the current gray tint for stroking paths to black.
     */

    public void resetGrayStroke() {
        saveColorStroke(GrayColor.GRAYBLACK);
        content.append("0 G").append_i(separator);
    }

    /**
     * Helper to validate and write the RGB color components
     *
     * @param red   the intensity of red. A value between 0 and 255
     * @param green the intensity of green. A value between 0 and 255
     * @param blue  the intensity of blue. A value between 0 and 255
     */
    private void HelperRGB(int red, int green, int blue) {
        HelperRGB((float) (red & MAX_COLOR_VALUE) / MAX_INT_COLOR_VALUE,
                (float) (green & MAX_COLOR_VALUE) / MAX_INT_COLOR_VALUE,
                (float) (blue & MAX_COLOR_VALUE) / MAX_INT_COLOR_VALUE);
    }

    /**
     * Helper to validate and write the RGB color components
     *
     * @param red   the intensity of red. A value between 0 and 1
     * @param green the intensity of green. A value between 0 and 1
     * @param blue  the intensity of blue. A value between 0 and 1
     */
    private void HelperRGB(float red, float green, float blue) {
        PdfXConformanceImp.checkPDFXConformance(writer, PdfXConformanceImp.PDFXKEY_RGB, null);
        if (red < 0.0f) {
            red = 0.0f;
        } else if (red > MAX_FLOAT_COLOR_VALUE) {
            red = MAX_FLOAT_COLOR_VALUE;
        }
        if (green < 0.0f) {
            green = 0.0f;
        } else if (green > MAX_FLOAT_COLOR_VALUE) {
            green = MAX_FLOAT_COLOR_VALUE;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        } else if (blue > MAX_FLOAT_COLOR_VALUE) {
            blue = MAX_FLOAT_COLOR_VALUE;
        }
        content.append(red).append(' ').append(green).append(' ').append(blue);
    }

    /**
     * Changes the current color for filling paths (device dependent colors!).
     * <p>
     * Sets the color space to <B>DeviceRGB</B> (or the <B>DefaultRGB</B> color space), and sets the color to use for
     * filling paths.</P>
     * <p>
     * Following the PDF manual, each operand must be a number between 0 (minimum intensity) and 1 (maximum
     * intensity).</P>
     *
     * @param red   the intensity of red. A value between 0 and 1
     * @param green the intensity of green. A value between 0 and 1
     * @param blue  the intensity of blue. A value between 0 and 1
     */

    /**
     * Changes the current color for filling paths to black. Resetting using gray color to keep the backward
     * compatibility.
     */

    public void resetRGBColorFill() {
        resetGrayFill();
    }

    /**
     * Changes the current color for stroking paths (device dependent colors!).
     * <p>
     * Sets the color space to <B>DeviceRGB</B> (or the <B>DefaultRGB</B> color space), and sets the color to use for
     * stroking paths.</P>
     * <p>
     * Following the PDF manual, each operand must be a number between 0 (miniumum intensity) and 1 (maximum
     * intensity).
     *
     * @param red   the intensity of red. A value between 0 and 1
     * @param green the intensity of green. A value between 0 and 1
     * @param blue  the intensity of blue. A value between 0 and 1
     */

    /**
     * Changes the current color for stroking paths to black. Resetting using gray color to keep the backward
     * compatibility.
     */

    public void resetRGBColorStroke() {
        resetGrayStroke();
    }

    /**
     * Helper to validate and write the CMYK color components.
     *
     * @param cyan    the intensity of cyan. A value between 0 and 255
     * @param magenta the intensity of magenta. A value between 0 and 255
     * @param yellow  the intensity of yellow. A value between 0 and 255
     * @param black   the intensity of black. A value between 0 and 255
     */
    private void HelperCMYK(int cyan, int magenta, int yellow, int black) {
        HelperCMYK((float) (cyan & MAX_COLOR_VALUE) / MAX_INT_COLOR_VALUE,
                (float) (magenta & MAX_COLOR_VALUE) / MAX_INT_COLOR_VALUE,
                (float) (yellow & MAX_COLOR_VALUE) / MAX_INT_COLOR_VALUE,
                (float) (black & MAX_COLOR_VALUE) / MAX_INT_COLOR_VALUE);
    }

    /**
     * Helper to validate and write the CMYK color components.
     *
     * @param cyan    the intensity of cyan. A value between 0 and 1
     * @param magenta the intensity of magenta. A value between 0 and 1
     * @param yellow  the intensity of yellow. A value between 0 and 1
     * @param black   the intensity of black. A value between 0 and 1
     */
    private void HelperCMYK(float cyan, float magenta, float yellow, float black) {
        if (cyan < 0.0f) {
            cyan = 0.0f;
        } else if (cyan > MAX_FLOAT_COLOR_VALUE) {
            cyan = MAX_FLOAT_COLOR_VALUE;
        }
        if (magenta < 0.0f) {
            magenta = 0.0f;
        } else if (magenta > MAX_FLOAT_COLOR_VALUE) {
            magenta = MAX_FLOAT_COLOR_VALUE;
        }
        if (yellow < 0.0f) {
            yellow = 0.0f;
        } else if (yellow > MAX_FLOAT_COLOR_VALUE) {
            yellow = MAX_FLOAT_COLOR_VALUE;
        }
        if (black < 0.0f) {
            black = 0.0f;
        } else if (black > MAX_FLOAT_COLOR_VALUE) {
            black = MAX_FLOAT_COLOR_VALUE;
        }
        content.append(cyan).append(' ').append(magenta).append(' ').append(yellow).append(' ').append(black);
    }

    /**
     * Changes the current color for filling paths (device dependent colors!).
     * <p>
     * Sets the color space to <B>DeviceCMYK</B> (or the <B>DefaultCMYK</B> color space), and sets the color to use for
     * filling paths.</P>
     * <p>
     * Following the PDF manual, each operand must be a number between 0 (no ink) and 1 (maximum ink).</P>
     *
     * @param cyan    the intensity of cyan. A value between 0 and 1
     * @param magenta the intensity of magenta. A value between 0 and 1
     * @param yellow  the intensity of yellow. A value between 0 and 1
     * @param black   the intensity of black. A value between 0 and 1
     */

    public void setCMYKColorFillF(float cyan, float magenta, float yellow, float black, float alpha) {
        saveColorFill(new CMYKColor(cyan, magenta, yellow, black, alpha));
        HelperCMYK(cyan, magenta, yellow, black);
        content.append(" k").append_i(separator);
    }

    /**
     * Changes the current color for filling paths to black.
     */

    /**
     * Changes the current color for stroking paths (device dependent colors!).
     * <p>
     * Sets the color space to <B>DeviceCMYK</B> (or the <B>DefaultCMYK</B> color space), and sets the color to use for
     * stroking paths.</P>
     * <p>
     * Following the PDF manual, each operand must be a number between 0 (miniumum intensity) and 1 (maximum
     * intensity).
     *
     * @param cyan    the intensity of cyan. A value between 0 and 1
     * @param magenta the intensity of magenta. A value between 0 and 1
     * @param yellow  the intensity of yellow. A value between 0 and 1
     * @param black   the intensity of black. A value between 0 and 1
     */

    public void setCMYKColorStrokeF(float cyan, float magenta, float yellow, float black, float alpha) {
        saveColorStroke(new CMYKColor(cyan, magenta, yellow, black, alpha));
        HelperCMYK(cyan, magenta, yellow, black);
        content.append(" K").append_i(separator);
    }

    /**
     * Changes the current color for stroking paths to black.
     */

    /**
     * Move the current point <I>(x, y)</I>, omitting any connecting line segment.
     *
     * @param x new x-coordinate
     * @param y new y-coordinate
     */

    public void moveTo(float x, float y) {
        content.append(x).append(' ').append(y).append(" m").append_i(separator);
    }

    /**
     * Appends a straight line segment from the current point <I>(x, y)</I>. The new current point is <I>(x, y)</I>.
     *
     * @param x new x-coordinate
     * @param y new y-coordinate
     */

    public void lineTo(float x, float y) {
        content.append(x).append(' ').append(y).append(" l").append_i(separator);
    }

    /**
     * Appends a B&#xea;zier curve to the path, starting from the current point.
     *
     * @param x1 x-coordinate of the first control point
     * @param y1 y-coordinate of the first control point
     * @param x2 x-coordinate of the second control point
     * @param y2 y-coordinate of the second control point
     * @param x3 x-coordinate of the ending point (= new current point)
     * @param y3 y-coordinate of the ending point (= new current point)
     */

    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        content.append(x1).append(' ').append(y1).append(' ').append(x2).append(' ').append(y2).append(' ').append(x3)
                .append(' ').append(y3).append(" c").append_i(separator);
    }

    /**
     * Appends a B&#xea;zier curve to the path, starting from the current point.
     *
     * @param x2 x-coordinate of the second control point
     * @param y2 y-coordinate of the second control point
     * @param x3 x-coordinate of the ending point (= new current point)
     * @param y3 y-coordinate of the ending point (= new current point)
     */

    /**
     * Appends a B&#xea;zier curve to the path, starting from the current point.
     *
     * @param x1 x-coordinate of the first control point
     * @param y1 y-coordinate of the first control point
     * @param x3 x-coordinate of the ending point (= new current point)
     * @param y3 y-coordinate of the ending point (= new current point)
     */

    /**
     * Adds a rectangle to the current path.
     *
     * @param x x-coordinate of the starting point
     * @param y y-coordinate of the starting point
     * @param w width
     * @param h height
     */

    public void rectangle(float x, float y, float w, float h) {
        content.append(x).append(' ').append(y).append(' ').append(w).append(' ').append(h).append(" re")
                .append_i(separator);
    }

    private boolean compareColors(Color c1, Color c2) {
        if (c1 == null && c2 == null) {
            return true;
        }
        if (c1 == null || c2 == null) {
            return false;
        }
        if (c1 instanceof ExtendedColor) {
            return c1.equals(c2);
        }
        return c2.equals(c1);
    }

    /**
     * Adds a variable width border to the current path. Only use if
     * {@link org.openpdf.text.Rectangle#isUseVariableBorders() Rectangle.isUseVariableBorders} = true.
     *
     * @param rect a <CODE>Rectangle</CODE>
     */
    public void variableRectangle(Rectangle rect) {
        float t = rect.getTop();
        float b = rect.getBottom();
        float r = rect.getRight();
        float l = rect.getLeft();
        float wt = rect.getBorderWidthTop();
        float wb = rect.getBorderWidthBottom();
        float wr = rect.getBorderWidthRight();
        float wl = rect.getBorderWidthLeft();
        Color ct = rect.getBorderColorTop();
        Color cb = rect.getBorderColorBottom();
        Color cr = rect.getBorderColorRight();
        Color cl = rect.getBorderColorLeft();
        saveState();
        setLineCap(PdfContentByte.LINE_CAP_BUTT);
        setLineJoin(PdfContentByte.LINE_JOIN_MITER);
        float clw = 0;
        boolean cdef = false;
        Color ccol = null;
        boolean cdefi = false;
        Color cfil = null;
        // draw top
        if (wt > 0) {
            setLineWidth(clw = wt);
            cdef = true;
            if (ct == null) {
                resetRGBColorStroke();
            } else {
                setColorStroke(ct);
            }
            ccol = ct;
            moveTo(l, t - wt / 2f);
            lineTo(r, t - wt / 2f);
            stroke();
        }

        // Draw bottom
        if (wb > 0) {
            if (wb != clw) {
                setLineWidth(clw = wb);
            }
            if (!cdef || !compareColors(ccol, cb)) {
                cdef = true;
                if (cb == null) {
                    resetRGBColorStroke();
                } else {
                    setColorStroke(cb);
                }
                ccol = cb;
            }
            moveTo(r, b + wb / 2f);
            lineTo(l, b + wb / 2f);
            stroke();
        }

        // Draw right
        if (wr > 0) {
            if (wr != clw) {
                setLineWidth(clw = wr);
            }
            if (!cdef || !compareColors(ccol, cr)) {
                cdef = true;
                if (cr == null) {
                    resetRGBColorStroke();
                } else {
                    setColorStroke(cr);
                }
                ccol = cr;
            }
            boolean bt = compareColors(ct, cr);
            boolean bb = compareColors(cb, cr);
            moveTo(r - wr / 2f, bt ? t : t - wt);
            lineTo(r - wr / 2f, bb ? b : b + wb);
            stroke();
            if (!bt || !bb) {
                cdefi = true;
                if (cr == null) {
                    resetRGBColorFill();
                } else {
                    setColorFill(cr);
                }
                cfil = cr;
                if (!bt) {
                    moveTo(r, t);
                    lineTo(r, t - wt);
                    lineTo(r - wr, t - wt);
                    fill();
                }
                if (!bb) {
                    moveTo(r, b);
                    lineTo(r, b + wb);
                    lineTo(r - wr, b + wb);
                    fill();
                }
            }
        }

        // Draw Left
        if (wl > 0) {
            if (wl != clw) {
                setLineWidth(wl);
            }
            if (!cdef || !compareColors(ccol, cl)) {
                if (cl == null) {
                    resetRGBColorStroke();
                } else {
                    setColorStroke(cl);
                }
            }
            boolean bt = compareColors(ct, cl);
            boolean bb = compareColors(cb, cl);
            moveTo(l + wl / 2f, bt ? t : t - wt);
            lineTo(l + wl / 2f, bb ? b : b + wb);
            stroke();
            if (!bt || !bb) {
                if (!cdefi || !compareColors(cfil, cl)) {
                    if (cl == null) {
                        resetRGBColorFill();
                    } else {
                        setColorFill(cl);
                    }
                }
                if (!bt) {
                    moveTo(l, t);
                    lineTo(l, t - wt);
                    lineTo(l + wl, t - wt);
                    fill();
                }
                if (!bb) {
                    moveTo(l, b);
                    lineTo(l, b + wb);
                    lineTo(l + wl, b + wb);
                    fill();
                }
            }
        }
        restoreState();
    }

    /**
     * Adds a border (complete or partially) to the current path..
     *
     * @param rectangle a <CODE>Rectangle</CODE>
     */

    public void rectangle(Rectangle rectangle) {
        // the coordinates of the border are retrieved
        float x1 = rectangle.getLeft();
        float y1 = rectangle.getBottom();
        float x2 = rectangle.getRight();
        float y2 = rectangle.getTop();

        // the backgroundcolor is set
        Color background = rectangle.getBackgroundColor();
        if (background != null) {
            saveState();
            setColorFill(background);
            rectangle(x1, y1, x2 - x1, y2 - y1);
            fill();
            restoreState();
        }

        // if the element hasn't got any borders, nothing is added
        if (!rectangle.hasBorders()) {
            return;
        }

        // if any of the individual border colors are set
        // we draw the borders all around using the
        // different colors
        if (rectangle.isUseVariableBorders()) {
            variableRectangle(rectangle);
        } else {
            // the width is set to the width of the element
            if (rectangle.getBorderWidth() != Rectangle.UNDEFINED) {
                setLineWidth(rectangle.getBorderWidth());
            }

            // the color is set to the color of the element
            Color color = rectangle.getBorderColor();
            if (color != null) {
                setColorStroke(color);
            }

            // if the box is a rectangle, it is added as a rectangle
            if (rectangle.hasBorder(Rectangle.BOX)) {
                rectangle(x1, y1, x2 - x1, y2 - y1);
            } else {
                // if the border isn't a rectangle, the different sides are added apart
                if (rectangle.hasBorder(Rectangle.RIGHT)) {
                    moveTo(x2, y1);
                    lineTo(x2, y2);
                }
                if (rectangle.hasBorder(Rectangle.LEFT)) {
                    moveTo(x1, y1);
                    lineTo(x1, y2);
                }
                if (rectangle.hasBorder(Rectangle.BOTTOM)) {
                    moveTo(x1, y1);
                    lineTo(x2, y1);
                }
                if (rectangle.hasBorder(Rectangle.TOP)) {
                    moveTo(x1, y2);
                    lineTo(x2, y2);
                }
            }

            stroke();

            if (color != null) {
                resetRGBColorStroke();
            }
        }
    }

    /**
     * Closes the current subpath by appending a straight line segment from the current point to the starting point of
     * the subpath.
     */

    public void closePath() {
        content.append("h").append_i(separator);
    }

    /**
     * Ends the path without filling or stroking it.
     */

    public void newPath() {
        content.append("n").append_i(separator);
    }

    /**
     * Strokes the path.
     */

    public void stroke() {
        content.append("S").append_i(separator);
    }

    /**
     * Closes the path and strokes it.
     */

    /**
     * Fills the path, using the non-zero winding number rule to determine the region to fill.
     */

    public void fill() {
        content.append("f").append_i(separator);
    }

    /**
     * Fills the path, using the even-odd rule to determine the region to fill.
     */

    public void eoFill() {
        content.append("f*").append_i(separator);
    }

    /**
     * Fills the path using the non-zero winding number rule to determine the region to fill and strokes it.
     */

    public void fillStroke() {
        content.append("B").append_i(separator);
    }

    /**
     * Closes the path, fills it using the non-zero winding number rule to determine the region to fill and strokes it.
     */

    /**
     * Fills the path, using the even-odd rule to determine the region to fill and strokes it.
     */

    public void eoFillStroke() {
        content.append("B*").append_i(separator);
    }

    /**
     * Closes the path, fills it using the even-odd rule to determine the region to fill and strokes it.
     */

    /**
     * Adds an <CODE>Image</CODE> to the page. The <CODE>Image</CODE> must have absolute positioning.
     *
     * @param image the <CODE>Image</CODE> object
     * @throws DocumentException if the <CODE>Image</CODE> does not have absolute positioning
     */
    public void addImage(Image image) throws DocumentException {
        addImage(image, false);
    }

    /**
     * Adds an <CODE>Image</CODE> to the page. The <CODE>Image</CODE> must have absolute positioning. The image can be
     * placed inline.
     *
     * @param image       the <CODE>Image</CODE> object
     * @param inlineImage <CODE>true</CODE> to place this image inline, <CODE>false</CODE> otherwise
     * @throws DocumentException if the <CODE>Image</CODE> does not have absolute positioning
     */
    public void addImage(Image image, boolean inlineImage) throws DocumentException {
        if (!image.hasAbsoluteY()) {
            throw new DocumentException(
                    MessageLocalization.getComposedMessage("the.image.must.have.absolute.positioning"));
        }
        float[] matrix = image.matrix();
        matrix[Image.CX] = image.getAbsoluteX() - matrix[Image.CX];
        matrix[Image.CY] = image.getAbsoluteY() - matrix[Image.CY];
        addImage(image, matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5], inlineImage);
    }

    /**
     * Adds an <CODE>Image</CODE> to the page. The positioning of the <CODE>Image</CODE> is done with the transformation
     * matrix. To position an <CODE>image</CODE> at (x,y) use addImage(image, image_width, 0, 0, image_height, x, y).
     * The image can be placed inline.
     *
     * @param image       the <CODE>Image</CODE> object
     * @param a           an element of the transformation matrix
     * @param b           an element of the transformation matrix
     * @param c           an element of the transformation matrix
     * @param d           an element of the transformation matrix
     * @param e           an element of the transformation matrix
     * @param f           an element of the transformation matrix
     * @param inlineImage <CODE>true</CODE> to place this image inline, <CODE>false</CODE> otherwise
     * @throws DocumentException on error
     */
    public void addImage(Image image, float a, float b, float c, float d, float e, float f, boolean inlineImage)
            throws DocumentException {
        try {
            if (image.isImgTemplate()) {
                writer.addDirectImageSimple(image);
                PdfTemplate template = image.getTemplateData();
                float w = template.getWidth();
                float h = template.getHeight();
                addTemplate(template, a / w, b / w, c / h, d / h, e, f);
            } else {
                content.append("q ");
                content.append(a).append(' ');
                content.append(b).append(' ');
                content.append(c).append(' ');
                content.append(d).append(' ');
                content.append(e).append(' ');
                content.append(f).append(" cm");
                if (inlineImage) {
                    content.append("\nBI\n");
                    PdfImage pimage = new PdfImage(image, "", null);
                    if (image instanceof ImgJBIG2) {
                        byte[] globals = ((ImgJBIG2) image).getGlobalBytes();
                        if (globals != null) {
                            PdfDictionary decodeparms = new PdfDictionary();
                            decodeparms.put(PdfName.JBIG2GLOBALS, writer.getReferenceJBIG2Globals(globals));
                            pimage.put(PdfName.DECODEPARMS, decodeparms);
                        }
                    }
                    for (PdfName key : pimage.getKeys()) {
                        PdfObject value = pimage.get(key);
                        String s = abrev.get(key);
                        if (s == null) {
                            continue;
                        }
                        content.append(s);
                        boolean check = true;
                        if (key.equals(PdfName.COLORSPACE) && value.isArray()) {
                            PdfArray ar = (PdfArray) value;
                            if (ar.size() == 4
                                    && PdfName.INDEXED.equals(ar.getAsName(0))
                                    && ar.getPdfObject(1).isName()
                                    && ar.getPdfObject(2).isNumber()
                                    && ar.getPdfObject(3).isString()
                            ) {
                                check = false;
                            }

                        }
                        if (check && key.equals(PdfName.COLORSPACE) && !value.isName()) {
                            PdfName cs = writer.getColorspaceName();
                            PageResources prs = getPageResources();
                            prs.addColor(cs, writer.addToBody(value).getIndirectReference());
                            value = cs;
                        }
                        value.toPdf(null, content);
                        content.append('\n');
                    }
                    content.append("ID\n");
                    pimage.writeContent(content);
                    content.append("\nEI\nQ").append_i(separator);
                } else {
                    PdfName name;
                    PageResources prs = getPageResources();
                    Image maskImage = image.getImageMask();
                    if (maskImage != null) {
                        name = writer.addDirectImageSimple(maskImage);
                        prs.addXObject(name, writer.getImageReference(name));
                    }
                    name = writer.addDirectImageSimple(image);
                    name = prs.addXObject(name, writer.getImageReference(name));
                    content.append(' ').append(name.getBytes()).append(" Do Q").append_i(separator);
                }
            }
            if (image.hasBorders()) {
                saveState();
                float w = image.getWidth();
                float h = image.getHeight();
                concatCTM(a / w, b / w, c / h, d / h, e, f);
                rectangle(image);
                restoreState();
            }
        } catch (Exception ee) {
            throw new DocumentException(ee);
        }
    }

    /**
     * Makes this <CODE>PdfContentByte</CODE> empty. Calls <code>reset( true )</code>
     */
    public void reset() {
        reset(true);
    }

    /**
     * Makes this <CODE>PdfContentByte</CODE> empty.
     *
     * @param validateContent will call <code>sanityCheck()</code> if true.
     * @since 2.1.6
     */
    public void reset(boolean validateContent) {
        content.reset();
        if (validateContent) {
            sanityCheck();
        }
        state = new GraphicState();
    }

    /**
     * Starts the writing of text.
     */
    public void beginText() {
        if (inText) {
            throw new IllegalPdfSyntaxException(
                    MessageLocalization.getComposedMessage("unbalanced.begin.end.text.operators"));
        }
        inText = true;
        state.xTLM = 0;
        state.yTLM = 0;
        content.append("BT").append_i(separator);
    }

    /**
     * Ends the writing of text and makes the current font invalid.
     */
    public void endText() {
        if (!inText) {
            throw new IllegalPdfSyntaxException(
                    MessageLocalization.getComposedMessage("unbalanced.begin.end.text.operators"));
        }
        inText = false;
        content.append("ET").append_i(separator);
    }

    /**
     * Saves the graphic state. <CODE>saveState</CODE> and
     * <CODE>restoreState</CODE> must be balanced.
     */
    public void saveState() {
        content.append("q").append_i(separator);
        if (state != null) {
            stateList.add(new GraphicState(state));
        }
    }

    /**
     * Restores the graphic state. <CODE>saveState</CODE> and
     * <CODE>restoreState</CODE> must be balanced.
     */
    public void restoreState() {
        content.append("Q").append_i(separator);
        int idx = stateList.size() - 1;
        if (idx < 0) {
            throw new IllegalPdfSyntaxException(
                    MessageLocalization.getComposedMessage("unbalanced.save.restore.state.operators"));
        }

        if (stateList.get(idx) != null) {
            state.restore(stateList.get(idx));
        }
        stateList.remove(idx);
    }

    /**
     * Set the font and the size for the subsequent text writing.
     *
     * @param bf   the font
     * @param size the font size in points
     */
    public void setFontAndSize(BaseFont bf, float size) {
        checkWriter();
        if (size < MIN_FONT_SIZE && size > -MIN_FONT_SIZE) {
            throw new IllegalArgumentException(
                    MessageLocalization.getComposedMessage("font.size.too.small.1", String.valueOf(size)));
        }
        state.size = size;
        state.fontDetails = writer.addSimple(bf);
        PageResources prs = getPageResources();
        PdfName name = state.fontDetails.getFontName();
        name = prs.addFont(name, state.fontDetails.getIndirectReference());
        content.append(name.getBytes()).append(' ').append(size).append(" Tf").append_i(separator);
    }

    /**
     * A helper to insert into the content stream the <CODE>text</CODE> converted to bytes according to the font's
     * encoding.
     *
     * @param text the text to write
     */
    private void showText2(String text) {
        if (state.fontDetails == null) {
            throw new NullPointerException(
                    MessageLocalization.getComposedMessage("font.and.size.must.be.set.before.writing.any.text"));
        }
        byte[] b = state.fontDetails.convertToBytes(text, getPdfDocument().getTextRenderingOptions());
        escapeAndAppendString(b, content);
    }

    /**
     * Shows the <CODE>text</CODE>.
     *
     * @param text the text to write
     */
    public void showText(String text) {
        if (state.fontDetails == null) {
            throw new NullPointerException(
                    MessageLocalization.getComposedMessage("font.and.size.must.be.set.before.writing.any.text"));
        }
        showTextBasic(text);
    }

    /**
     * Shows the <CODE>text</CODE>. LayoutProcessor is not used.
     *
     * @param text the text to write
     */
    public void showTextBasic(String text) {
        showText2(text);
        content.append("Tj").append_i(separator);
    }

    /**
     * Changes the text matrix.
     * <p>
     * Remark: this operation also initializes the current point position.</P>
     *
     * @param a operand 1,1 in the matrix
     * @param b operand 1,2 in the matrix
     * @param c operand 2,1 in the matrix
     * @param d operand 2,2 in the matrix
     * @param x operand 3,1 in the matrix
     * @param y operand 3,2 in the matrix
     */
    public void setTextMatrix(float a, float b, float c, float d, float x, float y) {
        state.xTLM = x;
        state.yTLM = y;
        content.append(a).append(' ').append(b).append_i(' ')
                .append(c).append_i(' ').append(d).append_i(' ')
                .append(x).append_i(' ').append(y).append(" Tm").append_i(separator);
    }

    /**
     * Changes the text matrix. The first four parameters are {1,0,0,1}.
     * <p>
     * Remark: this operation also initializes the current point position.</P>
     *
     * @param x operand 3,1 in the matrix
     * @param y operand 3,2 in the matrix
     */
    public void setTextMatrix(float x, float y) {
        setTextMatrix(1, 0, 0, 1, x, y);
    }

    /**
     * Gets the size of this content.
     *
     * @return the size of the content
     */
    int size() {
        return content.size();
    }

    /**
     * Computes the width of the given string taking in account the current values of "Character spacing", "Word
     * Spacing" and "Horizontal Scaling". The additional spacing is not computed for the last character of the string.
     *
     * @param text   the string to get width of
     * @param kerned the kerning option
     * @return the width
     */

    /**
     * Concatenate a matrix to the current transformation matrix.
     *
     * @param a an element of the transformation matrix
     * @param b an element of the transformation matrix
     * @param c an element of the transformation matrix
     * @param d an element of the transformation matrix
     * @param e an element of the transformation matrix
     * @param f an element of the transformation matrix
     **/
    public void concatCTM(float a, float b, float c, float d, float e, float f) {
        content.append(a).append(' ').append(b).append(' ').append(c).append(' ');
        content.append(d).append(' ').append(e).append(' ').append(f).append(" cm").append_i(separator);
    }

    /**
     * Draws a partial ellipse inscribed within the rectangle x1,y1,x2,y2, starting at startAng degrees and covering
     * extent degrees. Angles start with 0 to the right (+x) and increase counter-clockwise.
     *
     * @param x1       a corner of the enclosing rectangle
     * @param y1       a corner of the enclosing rectangle
     * @param x2       a corner of the enclosing rectangle
     * @param y2       a corner of the enclosing rectangle
     * @param startAng starting angle in degrees
     * @param extent   angle extent in degrees
     */
    public void arc(float x1, float y1, float x2, float y2, float startAng, float extent) {
        List<float[]> ar = bezierArc(x1, y1, x2, y2, startAng, extent);
        if (ar.isEmpty()) {
            return;
        }
        float[] pt = ar.get(0);
        moveTo(pt[0], pt[1]);
        for (float[] anAr : ar) {
            pt = anAr;
            curveTo(pt[2], pt[3], pt[4], pt[5], pt[6], pt[7]);
        }
    }

    /**
     * Draws an ellipse inscribed within the rectangle x1,y1,x2,y2.
     *
     * @param x1 a corner of the enclosing rectangle
     * @param y1 a corner of the enclosing rectangle
     * @param x2 a corner of the enclosing rectangle
     * @param y2 a corner of the enclosing rectangle
     */
    public void ellipse(float x1, float y1, float x2, float y2) {
        arc(x1, y1, x2, y2, 0f, 360f);
    }

    /**
     * Creates a new template.
     * <p>
     * Creates a new template that is nothing more than a form XObject. This template can be included in this
     * <CODE>PdfContentByte</CODE> or in another template. Templates are only written to the output when the document
     * is
     * closed permitting things like showing text in the first page that is only defined in the last page.
     *
     * @param width  the bounding box width
     * @param height the bounding box height
     * @return the created template
     */
    public PdfTemplate createTemplate(float width, float height) {
        return createTemplate(width, height, null);
    }

    PdfTemplate createTemplate(float width, float height, PdfName forcedName) {
        checkWriter();
        PdfTemplate template = new PdfTemplate(writer);
        template.setWidth(width);
        template.setHeight(height);
        writer.addDirectTemplateSimple(template, forcedName);
        return template;
    }

    /**
     * Adds a template to this content.
     *
     * @param template the template
     * @param a        an element of the transformation matrix
     * @param b        an element of the transformation matrix
     * @param c        an element of the transformation matrix
     * @param d        an element of the transformation matrix
     * @param e        an element of the transformation matrix
     * @param f        an element of the transformation matrix
     */
    public void addTemplate(PdfTemplate template, float a, float b, float c, float d, float e, float f) {
        checkWriter();
        checkNoPattern(template);
        PdfName name = writer.addDirectTemplateSimple(template, null);
        PageResources prs = getPageResources();
        name = prs.addXObject(name, template.getIndirectReference());
        content.append("q ");
        content.append(a).append(' ');
        content.append(b).append(' ');
        content.append(c).append(' ');
        content.append(d).append(' ');
        content.append(e).append(' ');
        content.append(f).append(" cm ");
        content.append(name.getBytes()).append(" Do Q").append_i(separator);
    }

    /**
     * Adds a template to this content.
     *
     * @param template the template
     * @param x        the x location of this template
     * @param y        the y location of this template
     */
    public void addTemplate(PdfTemplate template, float x, float y) {
        addTemplate(template, 1, 0, 0, 1, x, y);
    }

    /**
     * Adds a template to this content using double matrices.
     *
     * @param template the template
     * @param a        an element of the transformation matrix
     * @param b        an element of the transformation matrix
     * @param c        an element of the transformation matrix
     * @param d        an element of the transformation matrix
     * @param e        an element of the transformation matrix
     * @param f        an element of the transformation matrix
     */
    public void addTemplate(final PdfTemplate template, final double a, final double b, final double c, final double d,
            final double e, final double f) {
        checkWriter();
        checkNoPattern(template);

        PdfName name = writer.addDirectTemplateSimple(template, null);
        PageResources prs = getPageResources();
        name = prs.addXObject(name, template.getIndirectReference());

        content.append("q ");
        content.append(a).append(' ');
        content.append(b).append(' ');
        content.append(c).append(' ');
        content.append(d).append(' ');
        content.append(e).append(' ');
        content.append(f).append(" cm ");
        content.append(name.getBytes()).append(" Do Q").append_i(separator);
    }

    /**
     * Changes the current color for filling paths (device dependent colors!).
     * <p>
     * Sets the color space to <B>DeviceCMYK</B> (or the <B>DefaultCMYK</B> color space), and sets the color to use for
     * filling paths.</P>
     * <p>
     * This method is described in the 'Portable Document Format Reference Manual version 1.3' section 8.5.2.1 (page
     * 331).</P>
     * <p>
     * Following the PDF manual, each operand must be a number between 0 (no ink) and 1 (maximum ink). This method
     * however accepts only integers between 0x00 and 0xFF.</P>
     *
     * @param cyan    the intensity of cyan
     * @param magenta the intensity of magenta
     * @param yellow  the intensity of yellow
     * @param black   the intensity of black
     */

    /**
     * Changes the current color for stroking paths (device dependent colors!).
     * <p>
     * Sets the color space to <B>DeviceCMYK</B> (or the <B>DefaultCMYK</B> color space), and sets the color to use for
     * stroking paths.</P>
     * <p>
     * This method is described in the 'Portable Document Format Reference Manual version 1.3' section 8.5.2.1 (page
     * 331).</P> Following the PDF manual, each operand must be a number between 0 (minimum intensity) and 1 (maximum
     * intensity). This method however accepts only integers between 0x00 and 0xFF.
     *
     * @param cyan    the intensity of red
     * @param magenta the intensity of green
     * @param yellow  the intensity of blue
     * @param black   the intensity of black
     */

    /**
     * Changes the current color for filling paths (device dependent colors!).
     * <p>
     * Sets the color space to <B>DeviceRGB</B> (or the <B>DefaultRGB</B> color space), and sets the color to use for
     * filling paths.</P>
     * <p>
     * This method is described in the 'Portable Document Format Reference Manual version 1.3' section 8.5.2.1 (page
     * 331).</P>
     * <p>
     * Following the PDF manual, each operand must be a number between 0 (minimum intensity) and 1 (maximum intensity).
     * This method however accepts only integers between 0x00 and 0xFF.</P>
     *
     * @param red   the intensity of red
     * @param green the intensity of green
     * @param blue  the intensity of blue
     */

    public void setRGBColorFill(int red, int green, int blue, int alpha) {
        saveColorFill(new RGBColor(red, green, blue, alpha));
        HelperRGB(red, green, blue);
        content.append(" rg").append_i(separator);
    }

    /**
     * Changes the current color for stroking paths (device dependent colors!).
     * <p>
     * Sets the color space to <B>DeviceRGB</B> (or the <B>DefaultRGB</B> color space), and sets the color to use for
     * stroking paths.</P>
     * <p>
     * This method is described in the 'Portable Document Format Reference Manual version 1.3' section 8.5.2.1 (page
     * 331).</P> Following the PDF manual, each operand must be a number between 0 (minimum intensity) and 1 (maximum
     * intensity). This method however accepts only integers between 0x00 and 0xFF.
     *
     * @param red   the intensity of red
     * @param green the intensity of green
     * @param blue  the intensity of blue
     */

    public void setRGBColorStroke(int red, int green, int blue, int alpha) {
        saveColorStroke(new RGBColor(red, green, blue, alpha));
        HelperRGB(red, green, blue);
        content.append(" RG").append_i(separator);
    }

    /**
     * Sets the stroke color. <CODE>color</CODE> can be an
     * <CODE>ExtendedColor</CODE>.
     *
     * @param color the color
     */
    public void setColorStroke(Color color) {
        PdfXConformanceImp.checkPDFXConformance(writer, PdfXConformanceImp.PDFXKEY_COLOR, color);
        int type = ExtendedColor.getType(color);
        switch (type) {
            case ExtendedColor.TYPE_GRAY: {
                final GrayColor grayColor = (GrayColor) color;
                setGrayStroke(grayColor.getGray(), grayColor.getAlpha());
                break;
            }
            case ExtendedColor.TYPE_CMYK: {
                CMYKColor cmyk = (CMYKColor) color;
                setCMYKColorStrokeF(cmyk.getCyan(), cmyk.getMagenta(), cmyk.getYellow(), cmyk.getBlack(),
                        cmyk.getAlpha());
                break;
            }
            case ExtendedColor.TYPE_SEPARATION: {
                SpotColor spot = (SpotColor) color;
                setColorStroke(spot.getPdfSpotColor(), spot.getTint());
                break;
            }
            case ExtendedColor.TYPE_SHADING: {
                ShadingColor shading = (ShadingColor) color;
                setShadingStroke(shading.getPdfShadingPattern());
                break;
            }
            default:
                setRGBColorStroke(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        }
    }

    private void saveColorStroke(ExtendedColor extendedColor) {
        if (lastStrokeAlpha != extendedColor.getAlpha()) {
            PdfGState gState = new PdfGState();
            gState.setStrokeOpacity(extendedColor.getAlpha() / MAX_INT_COLOR_VALUE);
            setGState(gState);
            lastStrokeAlpha = extendedColor.getAlpha();
        }
        if (state != null) {
            state.colorStroke = extendedColor;
        }
    }

    /**
     * Sets the fill color. <CODE>color</CODE> can be an
     * <CODE>ExtendedColor</CODE>.
     *
     * @param color the color
     */
    public void setColorFill(Color color) {
        PdfXConformanceImp.checkPDFXConformance(writer, PdfXConformanceImp.PDFXKEY_COLOR, color);
        int type = ExtendedColor.getType(color);
        switch (type) {
            case ExtendedColor.TYPE_GRAY: {
                GrayColor grayColor = (GrayColor) color;
                setGrayFill(grayColor.getGray(), color.getAlpha() / MAX_INT_COLOR_VALUE);
                break;
            }
            case ExtendedColor.TYPE_CMYK: {
                CMYKColor cmyk = (CMYKColor) color;
                setCMYKColorFillF(cmyk.getCyan(), cmyk.getMagenta(), cmyk.getYellow(), cmyk.getBlack(),
                        cmyk.getAlpha() / MAX_INT_COLOR_VALUE);
                break;
            }
            case ExtendedColor.TYPE_SEPARATION: {
                SpotColor spot = (SpotColor) color;
                setColorFill(spot.getPdfSpotColor(), spot.getTint());
                break;
            }
            case ExtendedColor.TYPE_SHADING: {
                ShadingColor shading = (ShadingColor) color;
                setShadingFill(shading.getPdfShadingPattern());
                break;
            }
            default:
                setRGBColorFill(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        }
    }

    private void saveColorFill(ExtendedColor extendedColor) {
        if (lastFillAlpha != extendedColor.getAlpha()) {
            PdfGState gState = new PdfGState();
            gState.setFillOpacity(extendedColor.getAlpha() / MAX_INT_COLOR_VALUE);
            setGState(gState);
            lastFillAlpha = extendedColor.getAlpha();
        }
        if (state != null) {
            state.colorFill = extendedColor;
        }
    }

    /**
     * Sets the fill color to a spot color.
     *
     * @param sp   the spot color
     * @param tint the tint for the spot color. 0 is no color and 1 is 100% color
     */
    public void setColorFill(PdfSpotColor sp, float tint) {
        checkWriter();
        saveColorFill(new SpotColor(sp, tint));
        state.colorDetails = writer.addSimple(sp);
        PageResources prs = getPageResources();
        PdfName name = state.colorDetails.getColorName();
        name = prs.addColor(name, state.colorDetails.getIndirectReference());
        content.append(name.getBytes()).append(" cs ").append(tint).append(" scn").append_i(separator);
    }

    /**
     * Sets the stroke color to a spot color.
     *
     * @param sp   the spot color
     * @param tint the tint for the spot color. 0 is no color and 1 is 100% color
     */
    public void setColorStroke(PdfSpotColor sp, float tint) {
        checkWriter();
        saveColorStroke(new SpotColor(sp, tint));
        state.colorDetails = writer.addSimple(sp);
        PageResources prs = getPageResources();
        PdfName name = state.colorDetails.getColorName();
        name = prs.addColor(name, state.colorDetails.getIndirectReference());
        content.append(name.getBytes()).append(" CS ").append(tint).append(" SCN").append_i(separator);
    }

    /**
     * Sets the shading fill pattern.
     *
     * @param shading the shading pattern
     */
    public void setShadingFill(PdfShadingPattern shading) {
        checkWriter();
        saveColorFill(new ShadingColor(shading));
        writer.addSimpleShadingPattern(shading);
        PageResources prs = getPageResources();
        PdfName name = prs.addPattern(shading.getPatternName(), shading.getPatternReference());
        content.append(PdfName.PATTERN.getBytes()).append(" cs ").append(name.getBytes()).append(" scn")
                .append_i(separator);
        ColorDetails details = shading.getColorDetails();
        if (details != null) {
            prs.addColor(details.getColorName(), details.getIndirectReference());
        }
    }

    /**
     * Sets the shading stroke pattern
     *
     * @param shading the shading pattern
     */
    public void setShadingStroke(PdfShadingPattern shading) {
        checkWriter();
        saveColorStroke(new ShadingColor(shading));
        writer.addSimpleShadingPattern(shading);
        PageResources prs = getPageResources();
        PdfName name = prs.addPattern(shading.getPatternName(), shading.getPatternReference());
        content.append(PdfName.PATTERN.getBytes()).append(" CS ").append(name.getBytes()).append(" SCN")
                .append_i(separator);
        ColorDetails details = shading.getColorDetails();
        if (details != null) {
            prs.addColor(details.getColorName(), details.getIndirectReference());
        }
    }

    /**
     * Check if we have a valid PdfWriter.
     */
    protected void checkWriter() {
        if (writer == null) {
            throw new NullPointerException(
                    MessageLocalization.getComposedMessage("the.writer.in.pdfcontentbyte.is.null"));
        }
    }

    /**
     * Gets the <CODE>PdfWriter</CODE> in use by this object.
     *
     * @return the <CODE>PdfWriter</CODE> in use by this object
     */
    public PdfWriter getPdfWriter() {
        return writer;
    }

    /**
     * Gets the <CODE>PdfDocument</CODE> in use by this object.
     *
     * @return the <CODE>PdfDocument</CODE> in use by this object
     */
    public PdfDocument getPdfDocument() {
        return pdf;
    }

    /**
     * Adds a round rectangle to the current path.
     *
     * @param x x-coordinate of the starting point
     * @param y y-coordinate of the starting point
     * @param w width
     * @param h height
     * @param r radius of the arc corner
     */
    public void roundRectangle(float x, float y, float w, float h, float r) {
        if (w < 0) {
            x += w;
            w = -w;
        }
        if (h < 0) {
            y += h;
            h = -h;
        }
        if (r < 0) {
            r = -r;
        }
        float b = 0.4477f;
        moveTo(x + r, y);
        lineTo(x + w - r, y);
        curveTo(x + w - r * b, y, x + w, y + r * b, x + w, y + r);
        lineTo(x + w, y + h - r);
        curveTo(x + w, y + h - r * b, x + w - r * b, y + h, x + w - r, y + h);
        lineTo(x + r, y + h);
        curveTo(x + r * b, y + h, x, y + h - r * b, x, y + h - r);
        lineTo(x, y + r);
        curveTo(x, y + r * b, x + r * b, y, x + r, y);
    }

    /**
     * Throws an error if it is a pattern.
     *
     * @param t the object to check
     */
    void checkNoPattern(PdfTemplate t) {
        if (t.getType() == PdfTemplate.TYPE_PATTERN) {
            throw new RuntimeException(
                    MessageLocalization.getComposedMessage("invalid.use.of.a.pattern.a.template.was.expected"));
        }
    }

    PageResources getPageResources() {
        return pdf.getPageResources();
    }

    /**
     * Sets the graphic state
     *
     * @param gstate the graphic state
     */
    public void setGState(PdfGState gstate) {
        if (writer == null) {
            return;
        }
        PdfObject[] obj = writer.addSimpleExtGState(gstate);
        PageResources prs = getPageResources();
        PdfName name = prs.addExtGState((PdfName) obj[0], (PdfIndirectReference) obj[1]);
        if (state != null) {
            state.extGState = gstate;
        }
        content.append(name.getBytes()).append(" gs").append_i(separator);
    }

    /**
     * Checks for any dangling state: Mismatched save/restore state, begin/end text, begin/end layer, or begin/end
     * marked content sequence. If found, this function will throw.  This function is called automatically during a
     * reset() (from Document.newPage() for example), and before writing itself out in toPdf(). One possible cause: not
     * calling myPdfGraphics2D.dispose() will leave dangling saveState() calls.
     *
     * @throws IllegalPdfSyntaxException (a runtime exception)
     * @since 2.1.6
     */
    public void sanityCheck() {
        if (mcDepth != 0) {
            throw new IllegalPdfSyntaxException(
                    MessageLocalization.getComposedMessage("unbalanced.marked.content.operators"));
        }
        if (inText) {
            throw new IllegalPdfSyntaxException(
                    MessageLocalization.getComposedMessage("unbalanced.begin.end.text.operators"));
        }
        if (!stateList.isEmpty()) {
            throw new IllegalPdfSyntaxException(
                    MessageLocalization.getComposedMessage("unbalanced.save.restore.state.operators"));
        }
    }

    /**
     * This class keeps the graphic state of the current page
     */

    static class GraphicState {

        /**
         * The x position of the text line matrix.
         */
        protected float xTLM = 0;
        /**
         * The y position of the text line matrix.
         */
        protected float yTLM = 0;
        /**
         * The current text leading.
         */
        protected float leading = 0;
        /**
         * The current horizontal scaling
         */
        protected float scale = 100;
        /**
         * The current character spacing
         */
        protected float charSpace = 0;
        /**
         * The current word spacing
         */
        protected float wordSpace = 0;
        protected ExtendedColor colorFill = GrayColor.GRAYBLACK;
        protected ExtendedColor colorStroke = GrayColor.GRAYBLACK;
        protected PdfObject extGState = null;
        /**
         * This is the font in use
         */
        FontDetails fontDetails;
        /**
         * This is the color in use
         */
        ColorDetails colorDetails;
        /**
         * This is the font size in use
         */
        float size;

        GraphicState() {
        }

        GraphicState(GraphicState cp) {
            copyParameters(cp);
        }

        void copyParameters(final GraphicState cp) {
            fontDetails = cp.fontDetails;
            colorDetails = cp.colorDetails;
            size = cp.size;
            xTLM = cp.xTLM;
            yTLM = cp.yTLM;
            leading = cp.leading;
            scale = cp.scale;
            charSpace = cp.charSpace;
            wordSpace = cp.wordSpace;
            colorFill = cp.colorFill;
            colorStroke = cp.colorStroke;
            extGState = cp.extGState;
        }

        void restore(final GraphicState restore) {
            copyParameters(restore);
        }
    }
}
