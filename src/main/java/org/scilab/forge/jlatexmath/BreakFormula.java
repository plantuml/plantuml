/* BreakFormula.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/jlatexmath
 *
 * Copyright (C) 2011 DENIZET Calixte
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

import java.util.List;
import java.util.Stack;

public final class BreakFormula {

    public static Box split(Box box, float width, float interline) {
        if (box instanceof HorizontalBox) {
            return split((HorizontalBox) box, width, interline);
        } else if (box instanceof VerticalBox) {
            return split((VerticalBox) box, width, interline);
        } else {
            return box;
        }
    }

    public static Box split(HorizontalBox hbox, float width, float interline) {
        VerticalBox vbox = new VerticalBox();
        HorizontalBox first;
        HorizontalBox second = null;
        Stack<Position> positions = new Stack<Position>();
        float w = -1;
        while (hbox.width > width && (w = canBreak(positions, hbox, width)) != hbox.width) {
            Position pos = positions.pop();
            HorizontalBox[] hboxes = pos.hbox.split(pos.index - 1);
            first = hboxes[0];
            second = hboxes[1];
            while (!positions.isEmpty()) {
                pos = positions.pop();
                hboxes = pos.hbox.splitRemove(pos.index);
                hboxes[0].add(first);
                hboxes[1].add(0, second);
                first = hboxes[0];
                second = hboxes[1];
            }
            vbox.add(first, interline);
            hbox = second;
        }

        if (second != null) {
            vbox.add(second, interline);
            return vbox;
        }

        return hbox;
    }

    private static Box split(VerticalBox vbox, float width, float interline) {
        VerticalBox newBox = new VerticalBox();
        for (Box box:vbox.children) {
            newBox.add(split(box, width, interline));
        }

        return newBox;
    }

    private static float canBreak(Stack<Position> stack, HorizontalBox hbox, float width) {
        List<Box> children = hbox.children;
        float[] cumWidth = new float[children.size() + 1];
        cumWidth[0] = 0;
        for (int i = 0; i < children.size(); i++) {
            Box box = children.get(i);
            cumWidth[i + 1] = cumWidth[i] + box.width;
            if (cumWidth[i + 1] > width) {
                int pos = getBreakPosition(hbox, i);
                if (box instanceof HorizontalBox) {
                    Stack<Position> newStack = new Stack<Position>();
                    float w = canBreak(newStack, (HorizontalBox) box, width - cumWidth[i]);
                    if (w != box.width && (cumWidth[i] + w <= width || pos == -1)) {
                        stack.push(new Position(i - 1, hbox));
                        stack.addAll(newStack);
                        return cumWidth[i] + w;
                    }
                }

                if (pos != -1) {
                    stack.push(new Position(pos, hbox));
                    return cumWidth[pos];
                }
            }
        }

        return hbox.width;
    }

    private static int getBreakPosition(HorizontalBox hb, int i) {
        if (hb.breakPositions == null) {
            return -1;
        }

        if (hb.breakPositions.size() == 1 && hb.breakPositions.get(0) <= i) {
            return hb.breakPositions.get(0);
        }

        int pos = 0;
        for (; pos < hb.breakPositions.size(); pos++) {
            if (hb.breakPositions.get(pos) > i) {
                if (pos == 0) {
                    return -1;
                }
                return hb.breakPositions.get(pos - 1);
            }
        }

        return hb.breakPositions.get(pos - 1);
    }

    private static class Position {

        int index;
        HorizontalBox hbox;

        Position(int index, HorizontalBox hbox) {
            this.index = index;
            this.hbox = hbox;
        }
    }
}
