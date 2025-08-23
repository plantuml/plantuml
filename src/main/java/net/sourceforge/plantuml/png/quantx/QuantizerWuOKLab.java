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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Wu quantization over OKLab (L, a, b) instead of RGB.
 *
 * - 3D histogram on L, a, b (32 bins per axis -> INDEX_BITS = 5, INDEX_COUNT = 33 for integrals)
 * - Cumulative moments/sums along each axis + squared sum (L^2 + a^2 + b^2)
 * - Recursive splitting (maximize variance) same as the original algorithm
 * - Average cube colors converted back to 8-bit sRGB
 *
 * Notes:
 * - L in [0,1], a,b ~ [-0.5, 0.5] for sRGB; we clamp a,b to these limits
 * - “Moments” L/a/b are stored as double; weights remain int
 */
public final class QuantizerWuOKLab {

  // Histograms / moments (3D compact indexing with INDEX_COUNT = 33)
  int[] weights;         // sum of weights
  double[] momentsL;     // sum of L * count
  double[] momentsA;     // sum of a * count
  double[] momentsB;     // sum of b * count
  double[] momentsSS;    // sum of (L^2 + a^2 + b^2) * count
	  
  Box[] cubes;

  //OKLab quantization parameters
  private static final int INDEX_BITS = 5;          // 5 bits -> 32 bacs / dimension
  private static final int INDEX_COUNT = 33;        // (1 << INDEX_BITS) + 1 = 32 + 1
  private static final int TOTAL_SIZE = INDEX_COUNT * INDEX_COUNT * INDEX_COUNT; // 35937

  //OKLab bounds for a and b (safe for sRGB)
  private static final float A_MIN = -0.5f, A_MAX = 0.5f;
  private static final float B_MIN = -0.5f, B_MAX = 0.5f;
  private static final int INDEX_MAX = (1 << INDEX_BITS) - 1; // 31


  public Map<Integer, Integer> quantize(int[] pixels, int colorCount) {
	Map<Integer, Integer> mapResult = new QuantizerMap().quantize(pixels, colorCount);
    constructHistogram(mapResult);
    createMoments();
    CreateBoxesResult createBoxesResult = createBoxes(colorCount);
    List<Integer> colors = createResult(createBoxesResult.resultCount);
    Map<Integer, Integer> resultMap = new LinkedHashMap<>();
    for (int color : colors) {
      resultMap.put(color, 0);
    }
    return resultMap;
  }

  // ---- Histogram construction in OKLab -------------------------------------

  static int getIndex(int l, int a, int b) {
    // index = l*33^2 + a*33 + b, optimized using shifts (since 33 = 32 + 1)
    return (l << (INDEX_BITS * 2)) + (l << (INDEX_BITS + 1)) + l
         + (a << INDEX_BITS) + a + b;
  }

  void constructHistogram(Map<Integer, Integer> pixels) {
    weights   = new int[TOTAL_SIZE];
    momentsL  = new double[TOTAL_SIZE];
    momentsA  = new double[TOTAL_SIZE];
    momentsB  = new double[TOTAL_SIZE];
    momentsSS = new double[TOTAL_SIZE];

    for (Map.Entry<Integer, Integer> pair : pixels.entrySet()) {
      final int argb = pair.getKey();
      final int count = pair.getValue();

      int r8 = (argb >> 16) & 0xFF;
      int g8 = (argb >>  8) & 0xFF;
      int b8 = (argb      ) & 0xFF;

      // Convert to OKLab (L,a,b) floats
      float[] lab = srgb8ToOKLab(r8, g8, b8);
      float L = lab[0];
      float A = lab[1];
      float B = lab[2];

      // Quantize L/a/b into indices 1..32 (0 and 33 reserved for integrals)
      int iL = toIndexL(L);
      int iA = toIndexA(A);
      int iB = toIndexB(B);

      int index = getIndex(iL, iA, iB);

      weights[index] += count;
      momentsL[index] += (double)L * count;
      momentsA[index] += (double)A * count;
      momentsB[index] += (double)B * count;
      momentsSS[index] += (double)count * (L * L + A * A + B * B);
    }
  }

  // ---- 3D cumulative integrals (moments) -----------------------------------

  void createMoments() {
    for (int l = 1; l < INDEX_COUNT; ++l) {
      int[] areaW = new int[INDEX_COUNT];
      double[] areaL = new double[INDEX_COUNT];
      double[] areaA = new double[INDEX_COUNT];
      double[] areaB = new double[INDEX_COUNT];
      double[] areaSS = new double[INDEX_COUNT];

      for (int a = 1; a < INDEX_COUNT; ++a) {
        int lineW = 0;
        double lineL = 0.0;
        double lineA = 0.0;
        double lineB = 0.0;
        double lineSS = 0.0;

        for (int b = 1; b < INDEX_COUNT; ++b) {
          int index = getIndex(l, a, b);

          lineW  += weights[index];
          lineL  += momentsL[index];
          lineA  += momentsA[index];
          lineB  += momentsB[index];
          lineSS += momentsSS[index];

          areaW[b]  += lineW;
          areaL[b]  += lineL;
          areaA[b]  += lineA;
          areaB[b]  += lineB;
          areaSS[b] += lineSS;

          int prev = getIndex(l - 1, a, b);
          weights[index]  = weights[prev]  + areaW[b];
          momentsL[index] = momentsL[prev] + areaL[b];
          momentsA[index] = momentsA[prev] + areaA[b];
          momentsB[index] = momentsB[prev] + areaB[b];
          momentsSS[index]= momentsSS[prev]+ areaSS[b];
        }
      }
    }
  }

  // ---- Splitting (partitioning) --------------------------------------------

  CreateBoxesResult createBoxes(int maxColorCount) {
    cubes = new Box[maxColorCount];
    for (int i = 0; i < maxColorCount; i++) cubes[i] = new Box();

    double[] volumeVariance = new double[maxColorCount];

    Box first = cubes[0];
    first.l1 = INDEX_COUNT - 1;
    first.a1 = INDEX_COUNT - 1;
    first.b1 = INDEX_COUNT - 1;

    int generatedColorCount = maxColorCount;
    int next = 0;
    for (int i = 1; i < maxColorCount; i++) {
      if (cut(cubes[next], cubes[i])) {
        volumeVariance[next] = (cubes[next].vol > 1) ? variance(cubes[next]) : 0.0;
        volumeVariance[i]    = (cubes[i].vol    > 1) ? variance(cubes[i])    : 0.0;
      } else {
        volumeVariance[next] = 0.0;
        i--;
      }

      next = 0;
      double best = volumeVariance[0];
      for (int j = 1; j <= i; j++) {
        if (volumeVariance[j] > best) {
          best = volumeVariance[j];
          next = j;
        }
      }
      if (best <= 0.0) {
        generatedColorCount = i + 1;
        break;
      }
    }
    return new CreateBoxesResult(maxColorCount, generatedColorCount);
  }

  List<Integer> createResult(int colorCount) {
    List<Integer> colors = new ArrayList<>();
    for (int i = 0; i < colorCount; ++i) {
      Box cube = cubes[i];
      int w = volume(cube, weights);
      if (w > 0) {
        double L = volume(cube, momentsL) / w;
        double A = volume(cube, momentsA) / w;
        double B = volume(cube, momentsB) / w;
        int argb = oklabToSrgb8Clamp((float)L, (float)A, (float)B);
        colors.add(argb);
      }
    }
    return colors;
  }

  double variance(Box cube) {
    double dL = volume(cube, momentsL);
    double dA = volume(cube, momentsA);
    double dB = volume(cube, momentsB);

    double xx =
        momentsSS[getIndex(cube.l1, cube.a1, cube.b1)]
      - momentsSS[getIndex(cube.l1, cube.a1, cube.b0)]
      - momentsSS[getIndex(cube.l1, cube.a0, cube.b1)]
      + momentsSS[getIndex(cube.l1, cube.a0, cube.b0)]
      - momentsSS[getIndex(cube.l0, cube.a1, cube.b1)]
      + momentsSS[getIndex(cube.l0, cube.a1, cube.b0)]
      + momentsSS[getIndex(cube.l0, cube.a0, cube.b1)]
      - momentsSS[getIndex(cube.l0, cube.a0, cube.b0)];

    double hyp = dL * dL + dA * dA + dB * dB;
    int volW = volume(cube, weights);
    return xx - hyp / (double) volW;
  }

  Boolean cut(Box one, Box two) {
    double wholeL = volume(one, momentsL);
    double wholeA = volume(one, momentsA);
    double wholeB = volume(one, momentsB);
    int    wholeW = volume(one, weights);

    MaximizeResult maxL = maximize(one, Direction.L, one.l0 + 1, one.l1, wholeL, wholeA, wholeB, wholeW);
    MaximizeResult maxA = maximize(one, Direction.A, one.a0 + 1, one.a1, wholeL, wholeA, wholeB, wholeW);
    MaximizeResult maxB = maximize(one, Direction.B, one.b0 + 1, one.b1, wholeL, wholeA, wholeB, wholeW);

    Direction dir;
    MaximizeResult best = maxL;
    dir = Direction.L;

    if (maxA.maximum >= best.maximum) { best = maxA; dir = Direction.A; }
    if (maxB.maximum >= best.maximum) { best = maxB; dir = Direction.B; }

    if (best.cutLocation < 0) return false;

    two.l1 = one.l1; two.a1 = one.a1; two.b1 = one.b1;

    switch (dir) {
      case L:
        one.l1 = best.cutLocation;
        two.l0 = one.l1;
        two.a0 = one.a0;
        two.b0 = one.b0;
        break;
      case A:
        one.a1 = best.cutLocation;
        two.l0 = one.l0;
        two.a0 = one.a1;
        two.b0 = one.b0;
        break;
      case B:
        one.b1 = best.cutLocation;
        two.l0 = one.l0;
        two.a0 = one.a0;
        two.b0 = one.b1;
        break;
    }

    one.vol = (one.l1 - one.l0) * (one.a1 - one.a0) * (one.b1 - one.b0);
    two.vol = (two.l1 - two.l0) * (two.a1 - two.a0) * (two.b1 - two.b0);
    return true;
  }

  MaximizeResult maximize(
      Box cube,
      Direction direction,
      int first,
      int last,
      double wholeL,
      double wholeA,
      double wholeB,
      int wholeW) {

    double bottomL = bottom(cube, direction, momentsL);
    double bottomA = bottom(cube, direction, momentsA);
    double bottomB = bottom(cube, direction, momentsB);
    int    bottomW = bottom(cube, direction, weights);

    double max = 0.0;
    int cut = -1;

    for (int i = first; i < last; i++) {
      double halfL = bottomL + top(cube, direction, i, momentsL);
      double halfA = bottomA + top(cube, direction, i, momentsA);
      double halfB = bottomB + top(cube, direction, i, momentsB);
      int    halfW = bottomW + top(cube, direction, i, weights);

      if (halfW != 0) {
        double t = (halfL * halfL + halfA * halfA + halfB * halfB) / (double) halfW;

        double rL = wholeL - halfL;
        double rA = wholeA - halfA;
        double rB = wholeB - halfB;
        int    rW = wholeW - halfW;

        if (rW != 0) {
          t += (rL * rL + rA * rA + rB * rB) / (double) rW;
          if (t > max) { max = t; cut = i; }
        }
      }
    }
    return new MaximizeResult(cut, max);
  }

  // ---- Integrals: volume/top/bottom helpers --------------------------------

  static int volume(Box c, int[] m) {
    return  m[getIndex(c.l1, c.a1, c.b1)]
          - m[getIndex(c.l1, c.a1, c.b0)]
          - m[getIndex(c.l1, c.a0, c.b1)]
          + m[getIndex(c.l1, c.a0, c.b0)]
          - m[getIndex(c.l0, c.a1, c.b1)]
          + m[getIndex(c.l0, c.a1, c.b0)]
          + m[getIndex(c.l0, c.a0, c.b1)]
          - m[getIndex(c.l0, c.a0, c.b0)];
  }

  static double volume(Box c, double[] m) {
    return  m[getIndex(c.l1, c.a1, c.b1)]
          - m[getIndex(c.l1, c.a1, c.b0)]
          - m[getIndex(c.l1, c.a0, c.b1)]
          + m[getIndex(c.l1, c.a0, c.b0)]
          - m[getIndex(c.l0, c.a1, c.b1)]
          + m[getIndex(c.l0, c.a1, c.b0)]
          + m[getIndex(c.l0, c.a0, c.b1)]
          - m[getIndex(c.l0, c.a0, c.b0)];
  }

  static int bottom(Box c, Direction d, int[] m) {
    switch (d) {
      case L:
        return -m[getIndex(c.l0, c.a1, c.b1)] + m[getIndex(c.l0, c.a1, c.b0)]
             +  m[getIndex(c.l0, c.a0, c.b1)] - m[getIndex(c.l0, c.a0, c.b0)];
      case A:
        return -m[getIndex(c.l1, c.a0, c.b1)] + m[getIndex(c.l1, c.a0, c.b0)]
             +  m[getIndex(c.l0, c.a0, c.b1)] - m[getIndex(c.l0, c.a0, c.b0)];
      case B:
        return -m[getIndex(c.l1, c.a1, c.b0)] + m[getIndex(c.l1, c.a0, c.b0)]
             +  m[getIndex(c.l0, c.a1, c.b0)] - m[getIndex(c.l0, c.a0, c.b0)];
    }
    throw new IllegalArgumentException("unexpected direction " + d);
  }

  static double bottom(Box c, Direction d, double[] m) {
    switch (d) {
      case L:
        return -m[getIndex(c.l0, c.a1, c.b1)] + m[getIndex(c.l0, c.a1, c.b0)]
             +  m[getIndex(c.l0, c.a0, c.b1)] - m[getIndex(c.l0, c.a0, c.b0)];
      case A:
        return -m[getIndex(c.l1, c.a0, c.b1)] + m[getIndex(c.l1, c.a0, c.b0)]
             +  m[getIndex(c.l0, c.a0, c.b1)] - m[getIndex(c.l0, c.a0, c.b0)];
      case B:
        return -m[getIndex(c.l1, c.a1, c.b0)] + m[getIndex(c.l1, c.a0, c.b0)]
             +  m[getIndex(c.l0, c.a1, c.b0)] - m[getIndex(c.l0, c.a0, c.b0)];
    }
    throw new IllegalArgumentException("unexpected direction " + d);
  }

  static int top(Box c, Direction d, int pos, int[] m) {
    switch (d) {
      case L:
        return  m[getIndex(pos, c.a1, c.b1)] - m[getIndex(pos, c.a1, c.b0)]
              - m[getIndex(pos, c.a0, c.b1)] + m[getIndex(pos, c.a0, c.b0)];
      case A:
        return  m[getIndex(c.l1, pos, c.b1)] - m[getIndex(c.l1, pos, c.b0)]
              - m[getIndex(c.l0, pos, c.b1)] + m[getIndex(c.l0, pos, c.b0)];
      case B:
        return  m[getIndex(c.l1, c.a1, pos)] - m[getIndex(c.l1, c.a0, pos)]
              - m[getIndex(c.l0, c.a1, pos)] + m[getIndex(c.l0, c.a0, pos)];
    }
    throw new IllegalArgumentException("unexpected direction " + d);
  }

  static double top(Box c, Direction d, int pos, double[] m) {
    switch (d) {
      case L:
        return  m[getIndex(pos, c.a1, c.b1)] - m[getIndex(pos, c.a1, c.b0)]
              - m[getIndex(pos, c.a0, c.b1)] + m[getIndex(pos, c.a0, c.b0)];
      case A:
        return  m[getIndex(c.l1, pos, c.b1)] - m[getIndex(c.l1, pos, c.b0)]
              - m[getIndex(c.l0, pos, c.b1)] + m[getIndex(c.l0, pos, c.b0)];
      case B:
        return  m[getIndex(c.l1, c.a1, pos)] - m[getIndex(c.l1, c.a0, pos)]
              - m[getIndex(c.l0, c.a1, pos)] + m[getIndex(c.l0, c.a0, pos)];
    }
    throw new IllegalArgumentException("unexpected direction " + d);
  }

  private static enum Direction { L, A, B }

  private static final class MaximizeResult {
    int cutLocation;   // < 0 si impossible
    double maximum;
    MaximizeResult(int cut, double max) { this.cutLocation = cut; this.maximum = max; }
  }

  private static final class CreateBoxesResult {
    int requestedCount;
    int resultCount;
    CreateBoxesResult(int requestedCount, int resultCount) {
      this.requestedCount = requestedCount;
      this.resultCount = resultCount;
    }
  }

  private static final class Box {
    int l0 = 0, l1 = 0;
    int a0 = 0, a1 = 0;
    int b0 = 0, b1 = 0;
    int vol = 0;
  }

  // ---- OKLab -> index quantization (1..32) ---------------------------------

  private static int toIndexL(float L) {
    if (L < 0f) L = 0f; else if (L > 1f) L = 1f;
    int i = (int)(L * INDEX_MAX);
    if (i < 0) i = 0; else if (i > INDEX_MAX) i = INDEX_MAX;
    return i + 1;
  }

  private static int toIndexA(float a) {
    if (a < A_MIN) a = A_MIN; else if (a > A_MAX) a = A_MAX;
    float norm = (a - A_MIN) / (A_MAX - A_MIN); // 0..1
    int i = (int)(norm * INDEX_MAX);
    if (i < 0) i = 0; else if (i > INDEX_MAX) i = INDEX_MAX;
    return i + 1;
  }

  private static int toIndexB(float b) {
    if (b < B_MIN) b = B_MIN; else if (b > B_MAX) b = B_MAX;
    float norm = (b - B_MIN) / (B_MAX - B_MIN); // 0..1
    int i = (int)(norm * INDEX_MAX);
    if (i < 0) i = 0; else if (i > INDEX_MAX) i = INDEX_MAX;
    return i + 1;
  }

  // ---- Conversions sRGB <-> OKLab ------------------------------------------

  private static float[] srgb8ToOKLab(int r8, int g8, int b8) {
    // 8-bit -> [0,1]
    double r = r8 / 255.0;
    double g = g8 / 255.0;
    double b = b8 / 255.0;

    // sRGB -> lin
    double rl = srgbToLinear(r);
    double gl = srgbToLinear(g);
    double bl = srgbToLinear(b);

    // linear sRGB -> LMS (matrix recommended by Bjorn Ottosson)
    
    double l = 0.4122214708 * rl + 0.5363325363 * gl + 0.0514459929 * bl;
    double m = 0.2119034982 * rl + 0.6806995451 * gl + 0.1073969566 * bl;
    double s = 0.0883024619 * rl + 0.2817188376 * gl + 0.6299787005 * bl;

    // cube root
    double l_ = Math.cbrt(l);
    double m_ = Math.cbrt(m);
    double s_ = Math.cbrt(s);

    double L = 0.2104542553 * l_ + 0.7936177850 * m_ - 0.0040720468 * s_;
    double A = 1.9779984951 * l_ - 2.4285922050 * m_ + 0.4505937099 * s_;
    double B = 0.0259040371 * l_ + 0.7827717662 * m_ - 0.8086757660 * s_;

    return new float[]{ (float)L, (float)A, (float)B };
  }

  private static int oklabToSrgb8Clamp(float L, float A, float B) {
    // OKLab -> LMS^
    double l_ = L + 0.3963377774 * A + 0.2158037573 * B;
    double m_ = L - 0.1055613458 * A - 0.0638541728 * B;
    double s_ = L - 0.0894841775 * A - 1.2914855480 * B;

    // ^3
    double l = l_ * l_ * l_;
    double m = m_ * m_ * m_;
    double s = s_ * s_ * s_;

    // LMS -> lin sRGB
    double rl =  4.0767416621 * l - 3.3077115913 * m + 0.2309699292 * s;
    double gl = -1.2684380046 * l + 2.6097574011 * m - 0.3413193965 * s;
    double bl = -0.0041960863 * l - 0.7034186147 * m + 1.7076147010 * s;

    // lin -> sRGB (borne 0..1)
    double r = linearToSrgb(rl);
    double g = linearToSrgb(gl);
    double b = linearToSrgb(bl);

    int r8 = clamp8((int)Math.round(r * 255.0));
    int g8 = clamp8((int)Math.round(g * 255.0));
    int b8 = clamp8((int)Math.round(b * 255.0));

    return (0xFF << 24) | (r8 << 16) | (g8 << 8) | b8;
  }

  private static double srgbToLinear(double c) {
    return (c <= 0.04045) ? (c / 12.92) : Math.pow((c + 0.055) / 1.055, 2.4);
  }

  private static double linearToSrgb(double x) {
    if (x <= 0.0) return 0.0;
    if (x >= 1.0) return 1.0;
    return (x <= 0.0031308) ? (12.92 * x) : (1.055 * Math.pow(x, 1.0 / 2.4) - 0.055);
  }

  private static int clamp8(int v) {
    if (v < 0) return 0;
    if (v > 255) return 255;
    return v;
  }
}
