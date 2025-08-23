/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
 * With assistance from ChatGPT (OpenAI)
 *
 */
package net.sourceforge.plantuml.png.quantx;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class OKLabPaletteMapper {

    public static BufferedImage quantize(BufferedImage src, Map<Integer, Integer> palette, boolean floydSteinberg) {
        final int w = src.getWidth();
        final int h = src.getHeight();

        // --- 1) Build the 256 palette entries (ARGB -> r[], g[], b[], a[]) ---
        final int paletteSize = Math.min(256, palette.size());
        byte[] r = new byte[256];
        byte[] g = new byte[256];
        byte[] b = new byte[256];
        byte[] a = new byte[256];

        int[] paletteInts = new int[Math.max(1, paletteSize)];
        int idx = 0;
        for (Integer c : palette.keySet()) {
            if (idx == paletteSize) break;
            int argb = c;
            a[idx] = (byte) ((argb >>> 24) & 0xFF);
            r[idx] = (byte) ((argb >>> 16) & 0xFF);
            g[idx] = (byte) ((argb >>> 8) & 0xFF);
            b[idx] = (byte) (argb & 0xFF);
            paletteInts[idx] = argb;
            idx++;
        }

       // Fill up to 256 if needed
        final int fillFrom = Math.max(0, paletteSize);
        final int fillArgb = (paletteSize == 0) ? 0xFF000000 : paletteInts[paletteSize - 1];
        final byte fillA = (byte) ((fillArgb >>> 24) & 0xFF);
        final byte fillR = (byte) ((fillArgb >>> 16) & 0xFF);
        final byte fillG = (byte) ((fillArgb >>> 8) & 0xFF);
        final byte fillB = (byte) (fillArgb & 0xFF);
        for (int i = fillFrom; i < 256; i++) {
            a[i] = fillA;
            r[i] = fillR;
            g[i] = fillG;
            b[i] = fillB;
        }

        IndexColorModel icm = new IndexColorModel(8, 256, r, g, b, a);
        BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_INDEXED, icm);
        byte[] out = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData();

        // Edge case: empty palette => everything mapped to index 0
        if (paletteSize == 0) {
            Arrays.fill(out, (byte) 0);
            return dst;
        }

        // --- 2) Precompute the palette in OKLab ---
        float[] pL = new float[paletteSize];
        float[] pA = new float[paletteSize];
        float[] pB = new float[paletteSize];
        int transparentIndex = -1;
        for (int i = 0; i < paletteSize; i++) {
            int argb = paletteInts[i];
            int pa = (argb >>> 24) & 0xFF;
            int pr = (argb >>> 16) & 0xFF;
            int pg = (argb >>> 8) & 0xFF;
            int pb_ = argb & 0xFF;
            float[] lab = srgbToOKLab(pr, pg, pb_);
            pL[i] = lab[0];
            pA[i] = lab[1];
            pB[i] = lab[2];
            if (pa == 0 && transparentIndex < 0) transparentIndex = i; // détection simple
        }

       // --- 3) Pixel mapping -> nearest palette index (OKLab + alpha-aware) ---
        final int[] srcPixels = src.getRGB(0, 0, w, h, null, 0, w);

        // Alpha weight in the distance metric (tune if needed)

        final float alphaWeight = 0.015f; // 0.0f to ignore alpha

        if (!floydSteinberg) {
        	// No dithering: keep a cache (useful for flat-color images)
            final Map<Integer, Byte> cache = new HashMap<>(4096);
            for (int y = 0; y < h; y++) {
                int rowOff = y * w;
                for (int x = 0; x < w; x++) {
                    int pos = rowOff + x;
                    int argb = srcPixels[pos];

                    Byte cached = cache.get(argb);
                    if (cached != null) {
                        out[pos] = cached;
                        continue;
                    }

                    int A0 = (argb >>> 24) & 0xFF;
                    int R0 = (argb >>> 16) & 0xFF;
                    int G0 = (argb >>> 8) & 0xFF;
                    int B0 = argb & 0xFF;

                 // If we have a transparent index and alpha is very low, map directly
                    if (transparentIndex >= 0 && A0 < 8) {
                        byte bi = (byte) transparentIndex;
                        cache.put(argb, bi);
                        out[pos] = bi;
                        continue;
                    }

                    float[] lab0 = srgbToOKLab(R0, G0, B0);
                    byte best = findNearestOKLabIndex(lab0[0], lab0[1], lab0[2], A0, pL, pA, pB, paletteInts, alphaWeight);
                    cache.put(argb, best);
                    out[pos] = best;
                }
            }
            return dst;
        }

        // --- Floyd–Steinberg dithering (serpentine), error diffusion in OKLab ---
        final boolean serpentine = true;

        // Per-row errors for L, a, b (current row and next row)
        float[] errLRow = new float[w];
        float[] errARow = new float[w];
        float[] errBRow = new float[w];
        float[] errLNext = new float[w];
        float[] errANext = new float[w];
        float[] errBNext = new float[w];

        final float w7 = 7f / 16f;
        final float w5 = 5f / 16f;
        final float w3 = 3f / 16f;
        final float w1 = 1f / 16f;

        for (int y = 0; y < h; y++) {
            int rowOff = y * w;
            boolean leftToRight = !serpentine || ((y & 1) == 0);

         // reset next row
            Arrays.fill(errLNext, 0f);
            Arrays.fill(errANext, 0f);
            Arrays.fill(errBNext, 0f);

            if (leftToRight) {
                for (int x = 0; x < w; x++) {
                    int pos = rowOff + x;
                    int argb = srcPixels[pos];

                    int A0 = (argb >>> 24) & 0xFF;
                    int R0 = (argb >>> 16) & 0xFF;
                    int G0 = (argb >>> 8) & 0xFF;
                    int B0 = argb & 0xFF;

                 // Nearly transparent => no error diffusion
                    if (transparentIndex >= 0 && A0 < 8) {
                        out[pos] = (byte) transparentIndex;
                        continue;
                    }

                    float[] lab0 = srgbToOKLab(R0, G0, B0);
                    float L = clamp(lab0[0] + errLRow[x], -0.5f, 1.5f);
                    float Aok = clamp(lab0[1] + errARow[x], -1.5f, 1.5f);
                    float Bok = clamp(lab0[2] + errBRow[x], -1.5f, 1.5f);

                    byte best = findNearestOKLabIndex(L, Aok, Bok, A0, pL, pA, pB, paletteInts, alphaWeight);
                    out[pos] = best;

                    int bi = best & 0xFF;
                    float eL = L - pL[bi];
                    float eA = Aok - pA[bi];
                    float eB = Bok - pB[bi];

                    // Diffusion (x+1, y)
                    if (x + 1 < w) {
                        errLRow[x + 1] += eL * w7;
                        errARow[x + 1] += eA * w7;
                        errBRow[x + 1] += eB * w7;
                    }
                    // (x-1, y+1)
                    if (x - 1 >= 0) {
                        errLNext[x - 1] += eL * w3;
                        errANext[x - 1] += eA * w3;
                        errBNext[x - 1] += eB * w3;
                    }
                    // (x, y+1)
                    errLNext[x] += eL * w5;
                    errANext[x] += eA * w5;
                    errBNext[x] += eB * w5;

                    // (x+1, y+1)
                    if (x + 1 < w) {
                        errLNext[x + 1] += eL * w1;
                        errANext[x + 1] += eA * w1;
                        errBNext[x + 1] += eB * w1;
                    }
                }
            } else {
                for (int xi = w - 1; xi >= 0; xi--) {
                    int pos = rowOff + xi;
                    int argb = srcPixels[pos];

                    int A0 = (argb >>> 24) & 0xFF;
                    int R0 = (argb >>> 16) & 0xFF;
                    int G0 = (argb >>> 8) & 0xFF;
                    int B0 = argb & 0xFF;

                 // Nearly transparent => no error diffusion
                    if (transparentIndex >= 0 && A0 < 8) {
                        out[pos] = (byte) transparentIndex;
                        continue;
                    }

                    float[] lab0 = srgbToOKLab(R0, G0, B0);
                    float L = clamp(lab0[0] + errLRow[xi], -0.5f, 1.5f);
                    float Aok = clamp(lab0[1] + errARow[xi], -1.5f, 1.5f);
                    float Bok = clamp(lab0[2] + errBRow[xi], -1.5f, 1.5f);

                    byte best = findNearestOKLabIndex(L, Aok, Bok, A0, pL, pA, pB, paletteInts, alphaWeight);
                    out[pos] = best;

                    int bi = best & 0xFF;
                    float eL = L - pL[bi];
                    float eA = Aok - pA[bi];
                    float eB = Bok - pB[bi];

                 // Horizontal mirror:
                    // (x-1, y)
                    if (xi - 1 >= 0) {
                        errLRow[xi - 1] += eL * w7;
                        errARow[xi - 1] += eA * w7;
                        errBRow[xi - 1] += eB * w7;
                    }
                    // (x+1, y+1)
                    if (xi + 1 < w) {
                        errLNext[xi + 1] += eL * w3;
                        errANext[xi + 1] += eA * w3;
                        errBNext[xi + 1] += eB * w3;
                    }
                    // (x, y+1)
                    errLNext[xi] += eL * w5;
                    errANext[xi] += eA * w5;
                    errBNext[xi] += eB * w5;

                    // (x-1, y+1)
                    if (xi - 1 >= 0) {
                        errLNext[xi - 1] += eL * w1;
                        errANext[xi - 1] += eA * w1;
                        errBNext[xi - 1] += eB * w1;
                    }
                }
            }

         // Move to next row
            float[] tmp;
            tmp = errLRow; errLRow = errLNext; errLNext = tmp;
            tmp = errARow; errARow = errANext; errANext = tmp;
            tmp = errBRow; errBRow = errBNext; errBNext = tmp;
        }

        return dst;
    }

    // ----------------------------------------------------------------------
    // Nearest palette index search in OKLab, with alpha penalty
    // ----------------------------------------------------------------------
    private static byte findNearestOKLabIndex(float L0, float A0, float B0, int alphaSrc,
                                              float[] pL, float[] pA, float[] pB,
                                              int[] paletteInts, float alphaWeight) {
        int bestI = 0;
        double bestD = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pL.length; i++) {
            float dL = L0 - pL[i];
            float dA = A0 - pA[i];
            float dB = B0 - pB[i];
            double dist = dL * dL + dA * dA + dB * dB;

            int pa = (paletteInts[i] >>> 24) & 0xFF;
            float da = (alphaSrc - pa);
            dist += alphaWeight * (da * da);

            if (dist < bestD) {
                bestD = dist;
                bestI = i;
                if (bestD == 0.0) break;
            }
        }
        return (byte) bestI;
    }

    // ----------------------------------------------------------------------
    // Conversions sRGB -> OKLab
    // ----------------------------------------------------------------------

    // sRGB (0..255) -> OKLab (L, a, b) as float
    private static float[] srgbToOKLab(int R, int G, int B) {
        // 1) sRGB -> linRGB
        double r = srgbToLinear(R / 255.0);
        double g = srgbToLinear(G / 255.0);
        double b = srgbToLinear(B / 255.0);

     // 2) linear RGB -> LMS (matrix recommended in OKLab)
        double l = 0.4122214708 * r + 0.5363325363 * g + 0.0514459929 * b;
        double m = 0.2119034982 * r + 0.6806995451 * g + 0.1073969566 * b;
        double s = 0.0883024619 * r + 0.2817188376 * g + 0.6299787005 * b;

        // 3) cube roots
        double l_ = Math.cbrt(l);
        double m_ = Math.cbrt(m);
        double s_ = Math.cbrt(s);

        // 4) LMS' -> OKLab
        double L = 0.2104542553 * l_ + 0.7936177850 * m_ - 0.0040720468 * s_;
        double A = 1.9779984951 * l_ - 2.4285922050 * m_ + 0.4505937099 * s_;
        double Bv = 0.0259040371 * l_ + 0.7827717662 * m_ - 0.8086757660 * s_;

        return new float[] { (float) L, (float) A, (float) Bv };
    }

    private static double srgbToLinear(double c) {
    	// sRGB inverse gamma
        return (c <= 0.04045) ? (c / 12.92) : Math.pow((c + 0.055) / 1.055, 2.4);
    }

    private static float clamp(float v, float lo, float hi) {
        return (v < lo) ? lo : (v > hi) ? hi : v;
    }
}
