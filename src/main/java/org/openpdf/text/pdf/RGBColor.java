/*
 * Copyright 1999-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openpdf.text.pdf;

/**
 * @author Douglas SIX (http://github.com/sixdouglas)
 */
public class RGBColor extends ExtendedColor {

    /**
     * Constructs a RGB Color based on 3 color values (values are integers from 0 to 255).
     *
     * @param intRed   the red
     * @param intGreen the green
     * @param intBlue  the blue
     */
    public RGBColor(int intRed, int intGreen, int intBlue) {
        this(normalize(intRed) / MAX_INT_COLOR_VALUE, normalize(intGreen) / MAX_INT_COLOR_VALUE,
                normalize(intBlue) / MAX_INT_COLOR_VALUE, MAX_FLOAT_COLOR_VALUE);
    }

    /**
     * Constructs a RGB Color based on 3 color values (values are integers from 0 to 255).
     *
     * @param intRed   the red
     * @param intGreen the green
     * @param intBlue  the blue
     * @param intAlpha the alpha
     */
    public RGBColor(int intRed, int intGreen, int intBlue, int intAlpha) {
        this(normalize(intRed) / MAX_INT_COLOR_VALUE, normalize(intGreen) / MAX_INT_COLOR_VALUE,
                normalize(intBlue) / MAX_INT_COLOR_VALUE, normalize(intAlpha) / MAX_INT_COLOR_VALUE);
    }

    /**
     * Construct a RGB Color (values are floats from 0 to 1).
     *
     * @param floatRed   the red
     * @param floatGreen the green
     * @param floatBlue  the blue
     */
    public RGBColor(float floatRed, float floatGreen, float floatBlue) {
        this(floatRed, floatGreen, floatBlue, MAX_FLOAT_COLOR_VALUE);
    }

    /**
     * Construct a RGB Color (values are floats from 0 to 1).
     *
     * @param floatRed   the red
     * @param floatGreen the green
     * @param floatBlue  the blue
     * @param floatAlpha the alpha
     */
    public RGBColor(float floatRed, float floatGreen, float floatBlue, float floatAlpha) {
        super(TYPE_RGB, normalize(floatRed), normalize(floatGreen), normalize(floatBlue), normalize(floatAlpha));
    }
}
