/* MatrixAtom.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A box representing a matrix.
 */
public class MatrixAtom extends Atom {

    public static SpaceAtom hsep = new SpaceAtom(TeXConstants.UNIT_EM, 1f, 0.0f, 0.0f);
    public static SpaceAtom semihsep = new SpaceAtom(TeXConstants.UNIT_EM, 0.5f, 0.0f, 0.0f);
    public static SpaceAtom vsep_in = new SpaceAtom(TeXConstants.UNIT_EX, 0.0f, 1f, 0.0f);
    public static SpaceAtom vsep_ext_top = new SpaceAtom(TeXConstants.UNIT_EX, 0.0f, 0.4f, 0.0f);
    public static SpaceAtom vsep_ext_bot = new SpaceAtom(TeXConstants.UNIT_EX, 0.0f, 0.4f, 0.0f);

    public static final int ARRAY = 0;
    public static final int MATRIX = 1;
    public static final int ALIGN = 2;
    public static final int ALIGNAT = 3;
    public static final int FLALIGN = 4;
    public static final int SMALLMATRIX = 5;
    public static final int ALIGNED = 6;
    public static final int ALIGNEDAT = 7;

    private static final Box nullBox = new StrutBox(0, 0, 0, 0);

    private ArrayOfAtoms matrix;
    private int[] position;
    private Map<Integer, VlineAtom> vlines = new HashMap<Integer, VlineAtom>();
    private int type;
    private boolean isPartial;
    private boolean spaceAround;

    private static SpaceAtom align = new SpaceAtom(TeXConstants.MEDMUSKIP);

    /**
     * Creates an empty matrix
     *
     */
    public MatrixAtom(boolean isPartial, ArrayOfAtoms array, String options, boolean spaceAround) {
        this.isPartial = isPartial;
        this.matrix = array;
        this.type = ARRAY;
        this.spaceAround = spaceAround;
        parsePositions(new StringBuffer(options));
    }

    /**
     * Creates an empty matrix
     *
     */
    public MatrixAtom(boolean isPartial, ArrayOfAtoms array, String options) {
        this(isPartial, array, options, false);
    }

    /**
     * Creates an empty matrix
     *
     */
    public MatrixAtom(ArrayOfAtoms array, String options) {
        this(false, array, options);
    }

    public MatrixAtom(boolean isPartial, ArrayOfAtoms array, int type) {
        this(isPartial, array, type, false);
    }

    public MatrixAtom(boolean isPartial, ArrayOfAtoms array, int type, boolean spaceAround) {
        this.isPartial = isPartial;
        this.matrix = array;
        this.type = type;
        this.spaceAround = spaceAround;

        if (type != MATRIX && type != SMALLMATRIX) {
            position = new int[matrix.col];
            for (int i = 0; i < matrix.col; i += 2) {
                position[i] = TeXConstants.ALIGN_RIGHT;
                if (i + 1 < matrix.col) {
                    position[i + 1] = TeXConstants.ALIGN_LEFT;
                }
            }
        } else {
            position = new int[matrix.col];
            for (int i = 0; i < matrix.col; i++) {
                position[i] = TeXConstants.ALIGN_CENTER;
            }
        }
    }

    public MatrixAtom(boolean isPartial, ArrayOfAtoms array, int type, int alignment) {
        this(isPartial, array, type, alignment, true);
    }

    public MatrixAtom(boolean isPartial, ArrayOfAtoms array, int type, int alignment, boolean spaceAround) {
        this.isPartial = isPartial;
        this.matrix = array;
        this.type = type;
        this.spaceAround = spaceAround;

        position = new int[matrix.col];
        for (int i = 0; i < matrix.col; i++) {
            position[i] = alignment;
        }
    }

    public MatrixAtom(ArrayOfAtoms array, int type) {
        this(false, array, type);
    }

    private void parsePositions(StringBuffer opt) {
        int len = opt.length();
        int pos = 0;
        char ch;
        TeXFormula tf;
        TeXParser tp;
        List<Integer> lposition = new ArrayList<Integer>();
        while (pos < len) {
            ch = opt.charAt(pos);
            switch (ch) {
            case 'l' :
                lposition.add(TeXConstants.ALIGN_LEFT);
                break;
            case 'r' :
                lposition.add(TeXConstants.ALIGN_RIGHT);
                break;
            case 'c' :
                lposition.add(TeXConstants.ALIGN_CENTER);
                break;
            case '|' :
                int nb = 1;
                while (++pos < len) {
                    ch = opt.charAt(pos);
                    if (ch != '|') {
                        pos--;
                        break;
                    } else {
                        nb++;
                    }
                }
                vlines.put(lposition.size(), new VlineAtom(nb));
                break;
            case '@' :
                pos++;
                tf = new TeXFormula();
                tp = new TeXParser(isPartial, opt.substring(pos), tf, false);
                Atom at = tp.getArgument();
                matrix.col++;
                for (int j = 0; j < matrix.row; j++) {
                    matrix.array.get(j).add(lposition.size(), at);
                }

                lposition.add(TeXConstants.ALIGN_NONE);
                pos += tp.getPos();
                pos--;
                break;
            case '*' :
                pos++;
                tf = new TeXFormula();
                tp = new TeXParser(isPartial, opt.substring(pos), tf, false);
                String[] args = tp.getOptsArgs(2, 0);
                pos += tp.getPos();
                int nrep =  Integer.parseInt(args[1]);
                String str = "";
                for (int j = 0; j < nrep; j++) {
                    str += args[2];
                }
                opt.insert(pos, str);
                len = opt.length();
                pos--;
                break;
            case ' ':
            case '\t':
                break;
            default :
                lposition.add(TeXConstants.ALIGN_CENTER);
            }
            pos++;
        }

        for (int j = lposition.size(); j < matrix.col; j++) {
            lposition.add(TeXConstants.ALIGN_CENTER);
        }

        if (lposition.size() != 0) {
            Integer[] tab = lposition.toArray(new Integer[0]);
            position = new int[tab.length];
            for (int i = 0; i < tab.length; i++) {
                position[i] = tab[i];
            }
        } else {
            position = new int[] {TeXConstants.ALIGN_CENTER};
        }
    }

    public Box[] getColumnSep(TeXEnvironment env, float width) {
        int col = matrix.col;
        Box[] arr = new Box[col + 1];
        Box Align, AlignSep, Hsep;
        float h, w = env.getTextwidth();
        int i;

        if (type == ALIGNED || type == ALIGNEDAT) {
            w = Float.POSITIVE_INFINITY;
        }

        switch (type) {
        case ARRAY :
            //Array : hsep_col/2 elem hsep_col elem hsep_col ... hsep_col elem hsep_col/2
            i = 1;
            if (position[0] == TeXConstants.ALIGN_NONE) {
                arr[1] = new StrutBox(0.0f, 0.0f, 0.0f, 0.0f);
                i = 2;
            }
            if (spaceAround) {
                arr[0] = semihsep.createBox(env);
            } else {
                arr[0] = new StrutBox(0.0f, 0.0f, 0.0f, 0.0f);
            }
            arr[col] = arr[0];
            Hsep = hsep.createBox(env);
            for (; i < col; i++) {
                if (position[i] == TeXConstants.ALIGN_NONE) {
                    arr[i] = new StrutBox(0.0f, 0.0f, 0.0f, 0.0f);
                    arr[i + 1] = arr[i];
                    i++;
                } else {
                    arr[i] = Hsep;
                }
            }

            return arr;
        case MATRIX :
        case SMALLMATRIX :
            //Simple matrix : (hsep_col/2 or 0) elem hsep_col elem hsep_col ... hsep_col elem (hsep_col/2 or 0)
            arr[0] = nullBox;
            arr[col] = arr[0];
            Hsep = hsep.createBox(env);
            for (i = 1; i < col; i++) {
                arr[i] = Hsep;
            }

            return arr;
        case ALIGNED :
        case ALIGN :
            //Align env. : hsep=(textwidth-matWidth)/(2n+1) and hsep eq_lft \medskip el_rgt hsep ... hsep elem hsep
            Align = align.createBox(env);
            if (w != Float.POSITIVE_INFINITY) {
                h = Math.max((w - width - (col / 2) * Align.getWidth()) / (float) Math.floor((col + 3)/ 2), 0);
                AlignSep = new StrutBox(h, 0.0f, 0.0f, 0.0f);
            } else {
                AlignSep = hsep.createBox(env);
            }

            arr[col] = AlignSep;
            for (i = 0; i < col; i++) {
                if (i % 2 == 0) {
                    arr[i] = AlignSep;
                } else {
                    arr[i] = Align;
                }
            }

            break;
        case ALIGNEDAT :
        case ALIGNAT :
            //Alignat env. : hsep=(textwidth-matWidth)/2 and hsep elem ... elem hsep
            if (w != Float.POSITIVE_INFINITY) {
                h = Math.max((w - width) / 2, 0);
            } else {
                h = 0;
            }

            Align = align.createBox(env);
            Box empty = nullBox;
            arr[0] = new StrutBox(h, 0.0f, 0.0f, 0.0f);
            arr[col] = arr[0];
            for (i = 1; i < col; i++) {
                if (i % 2 == 0) {
                    arr[i] = empty;
                } else {
                    arr[i] = Align;
                }
            }

            break;
        case FLALIGN :
            //flalign env. : hsep=(textwidth-matWidth)/(2n+1) and hsep eq_lft \medskip el_rgt hsep ... hsep elem hsep
            Align = align.createBox(env);
            if (w != Float.POSITIVE_INFINITY) {
                h = Math.max((w - width - (col / 2) * Align.getWidth()) / (float) Math.floor((col - 1)/ 2), 0);
                AlignSep = new StrutBox(h, 0.0f, 0.0f, 0.0f);
            } else {
                AlignSep = hsep.createBox(env);
            }

            arr[0] = nullBox;
            arr[col] = arr[0];
            for (i = 1; i < col; i++) {
                if (i % 2 == 0) {
                    arr[i] = AlignSep;
                } else {
                    arr[i] = Align;
                }
            }

            break;
        }

        if (w == Float.POSITIVE_INFINITY) {
            arr[0] = nullBox;
            arr[col] = arr[0];
        }

        return arr;

    }

    public Box createBox(TeXEnvironment env) {
        int row = matrix.row;
        int col = matrix.col;
        Box[][] boxarr = new Box[row][col];
        float[] lineDepth = new float[row];
        float[] lineHeight = new float[row];
        float[] rowWidth = new float[col];
        float matW = 0;
        float drt = env.getTeXFont().getDefaultRuleThickness(env.getStyle());

        if (type == SMALLMATRIX) {
            env = env.copy();
            env.setStyle(TeXConstants.STYLE_SCRIPT);
        }

        List<MulticolumnAtom> listMulti = new ArrayList<MulticolumnAtom>();

        for (int i = 0; i < row; i++) {
            lineDepth[i] = 0;
            lineHeight[i] = 0;
            for (int j = 0; j < col; j++) {
                Atom at = null;
                try {
                    at = matrix.array.get(i).get(j);
                } catch (Exception e) {
                    //The previous atom was an intertext atom
                    //position[j - 1] = -1;
                    boxarr[i][j - 1].type = TeXConstants.TYPE_INTERTEXT;
                    j = col - 1;
                }

                boxarr[i][j] = (at == null) ? nullBox : at.createBox(env);

                lineDepth[i] = Math.max(boxarr[i][j].getDepth(), lineDepth[i]);
                lineHeight[i] = Math.max(boxarr[i][j].getHeight(), lineHeight[i]);

                if (boxarr[i][j].type != TeXConstants.TYPE_MULTICOLUMN) {
                    rowWidth[j] = Math.max(boxarr[i][j].getWidth(), rowWidth[j]);
                } else {
                    ((MulticolumnAtom) at).setRowColumn(i, j);
                    listMulti.add((MulticolumnAtom) at);
                }
            }
        }

        for (int i = 0; i < listMulti.size(); i++) {
            MulticolumnAtom multi = listMulti.get(i);
            int c = multi.getCol();
            int r = multi.getRow();
            int n = multi.getSkipped();
            float w = 0;
            for (int j = c; j < c + n; j++) {
                w += rowWidth[j];
            }
            if (boxarr[r][c].getWidth() > w) {
                float extraW = (boxarr[r][c].getWidth() - w) / n;
                for (int j = c; j < c + n; j++) {
                    rowWidth[j] += extraW;
                }
            }
        }

        for (int j = 0; j < col; j++) {
            matW += rowWidth[j];
        }
        Box[] Hsep = getColumnSep(env, matW);

        for (int j = 0; j < col + 1; j++) {
            matW += Hsep[j].getWidth();
            if (vlines.get(j) != null) {
                matW += vlines.get(j).getWidth(env);
            }
        }

        VerticalBox vb = new VerticalBox();
        Box Vsep = vsep_in.createBox(env);
        vb.add(vsep_ext_top.createBox(env));
        float totalHeight = 0;

        for (int i = 0; i < row; i++) {
            HorizontalBox hb = new HorizontalBox();
            for (int j = 0; j < col; j++) {
                switch (boxarr[i][j].type) {
                case -1 :
                case TeXConstants.TYPE_MULTICOLUMN :
                    if (j == 0) {
                        if (vlines.get(0) != null) {
                            VlineAtom vat = vlines.get(0);
                            vat.setHeight(lineHeight[i] + lineDepth[i] + Vsep.getHeight());
                            vat.setShift(lineDepth[i] + Vsep.getHeight() / 2);
                            Box vatBox = vat.createBox(env);
                            hb.add(new HorizontalBox(vatBox, Hsep[0].getWidth() + vatBox.getWidth(), TeXConstants.ALIGN_LEFT));
                        } else {
                            hb.add(Hsep[0]);
                        }
                    }

                    boolean lastVline = true;

                    if (boxarr[i][j].type == -1) {
                        hb.add(new HorizontalBox(boxarr[i][j], rowWidth[j], position[j]));
                    } else {
                        Box b = generateMulticolumn(env, Hsep, rowWidth, i, j);
                        MulticolumnAtom matom = (MulticolumnAtom) matrix.array.get(i).get(j);
                        j += matom.getSkipped() - 1;
                        hb.add(b);
                        lastVline = matom.hasRightVline();
                    }

                    if (lastVline && vlines.get(j + 1) != null) {
                        VlineAtom vat = vlines.get(j + 1);
                        vat.setHeight(lineHeight[i] + lineDepth[i] + Vsep.getHeight());
                        vat.setShift(lineDepth[i] + Vsep.getHeight() / 2);
                        Box vatBox = vat.createBox(env);
                        if (j < col - 1) {
                            hb.add(new HorizontalBox(vatBox, Hsep[j + 1].getWidth() + vatBox.getWidth(), TeXConstants.ALIGN_CENTER));
                        } else {
                            hb.add(new HorizontalBox(vatBox, Hsep[j + 1].getWidth() + vatBox.getWidth(), TeXConstants.ALIGN_RIGHT));
                        }
                    } else {
                        hb.add(Hsep[j + 1]);
                    }
                    break;
                case TeXConstants.TYPE_INTERTEXT :
                    float f = env.getTextwidth();
                    f = f == Float.POSITIVE_INFINITY ? rowWidth[j] : f;
                    hb = new HorizontalBox(boxarr[i][j], f, TeXConstants.ALIGN_LEFT);
                    j = col - 1;
                    break;
                case TeXConstants.TYPE_HLINE :
                    HlineAtom at = (HlineAtom) matrix.array.get(i).get(j);
                    at.setWidth(matW);
                    if (i >= 1 && matrix.array.get(i - 1).get(j) instanceof HlineAtom) {
                        hb.add(new StrutBox(0, 2 * drt, 0, 0));
                        at.setShift(-Vsep.getHeight() / 2 + drt);
                    } else {
                        at.setShift(-Vsep.getHeight() / 2);
                    }

                    hb.add(at.createBox(env));
                    j = col;
                    break;
                }
            }

            if (boxarr[i][0].type != TeXConstants.TYPE_HLINE) {
                hb.setHeight(lineHeight[i]);
                hb.setDepth(lineDepth[i]);
                vb.add(hb);

                if (i < row - 1)
                    vb.add(Vsep);
            } else {
                vb.add(hb);
            }
        }

        vb.add(vsep_ext_bot.createBox(env));
        totalHeight = vb.getHeight() + vb.getDepth();

        float axis = env.getTeXFont().getAxisHeight(env.getStyle());
        vb.setHeight(totalHeight / 2 + axis);
        vb.setDepth(totalHeight / 2 - axis);

        return vb;
    }

    private Box generateMulticolumn(TeXEnvironment env, Box[] Hsep, float[] rowWidth, int i, int j) {
        float w = 0;
        MulticolumnAtom mca = (MulticolumnAtom) matrix.array.get(i).get(j);
        int k, n = mca.getSkipped();
        for (k = j; k < j + n - 1; k++) {
            w += rowWidth[k] + Hsep[k + 1].getWidth();
            if (vlines.get(k + 1) != null) {
                w += vlines.get(k + 1).getWidth(env);
            }
        }
        w += rowWidth[k];

        Box b = mca.createBox(env);
        float bw = b.getWidth();
        if (bw > w) {
            // It isn't a good idea but for the moment I have no other solution !
            w = 0;
        }

        mca.setWidth(w);
        b = mca.createBox(env);
        return b;
    }
}
