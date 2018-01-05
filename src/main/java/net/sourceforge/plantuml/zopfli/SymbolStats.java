/*
Copyright 2014 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Author: eustas.ru@gmail.com (Eugene Klyuchnikov)
*/

package net.sourceforge.plantuml.zopfli;

final class SymbolStats {
  private final static double INV_LOG_2 = 1.4426950408889 * 0x10000L; /* 1.0 / log(2.0) */
  private final int[] litLens = new int[288];
  private final int[] dists = new int[32]; // Why 32? Expect 30.
  final long[] lLiterals = new long[288];
  final long[] lLengths = new long[259];
  final long[] dSymbols = new long[32];

  void getFreqs(LzStore store) {
    int[] sLitLens = this.litLens;
    int[] sDists = this.dists;
    System.arraycopy(Cookie.intZeroes, 0, sLitLens, 0, 288);
    System.arraycopy(Cookie.intZeroes, 0, sDists, 0, 32);

    int size = store.size;
    char[] litLens = store.litLens;
    char[] dists = store.dists;
    int[] lengthSymbol = Util.LENGTH_SYMBOL;
    int[] cachedDistSymbol = Util.CACHED_DIST_SYMBOL;
    for (int i = 0; i < size; i++) {
      int d = dists[i];
      int l = litLens[i];
      if (d == 0) {
        sLitLens[l]++;
      } else {
        sLitLens[lengthSymbol[l]]++;
        sDists[cachedDistSymbol[d]]++;
      }
    }
    sLitLens[256] = 1;
    calculate();
  }

  final void copy(final SymbolStats source) {
    System.arraycopy(source.litLens, 0, litLens, 0, 288);
    System.arraycopy(source.dists, 0, dists, 0, 32);
    System.arraycopy(source.lLiterals, 0, lLiterals, 0, 288);
    System.arraycopy(source.lLengths, 0, lLengths, 0, 259);
    System.arraycopy(source.dSymbols, 0, dSymbols, 0, 32);
  }

  final void calculate() {
    calculateLens();
    calculateDists();
  }

  final void calculateLens() {
    int sum = 0;
    int[] litLens = this.litLens;
    for (int i = 0; i < 288; ++i) {
      sum += litLens[i];
    }
    double log2sum = (sum == 0 ? Math.log(288) : Math.log(sum)) * INV_LOG_2;
    long[] lLiterals = this.lLiterals;
    for (int i = 0; i < 288; ++i) {
      if (litLens[i] == 0) {
        lLiterals[i] = (long)log2sum;
      } else {
        lLiterals[i] = (long)(log2sum - Math.log(litLens[i]) * INV_LOG_2);
      }
      if (lLiterals[i] < 0) {
        lLiterals[i] = 0;
      }
    }
    long[] lLengths = this.lLengths;
    int[] lengthSymbol = Util.LENGTH_SYMBOL;
    int[] lengthExtraBits = Util.LENGTH_EXTRA_BITS;
    for (int i = 0; i < 259; ++i) {
      lLengths[i] = lLiterals[lengthSymbol[i]] + (lengthExtraBits[i] * 0x10000L);
    }
  }

  final void calculateDists() {
    int sum = 0;
    int[] dists = this.dists;
    for (int i = 0; i < 32; ++i) {
      sum += dists[i];
    }
    double log2sum = (sum == 0 ? Math.log(32) : Math.log(sum)) * INV_LOG_2;
    long[] dSymbols = this.dSymbols;
    for (int i = 0; i < 32; ++i) {
      if (dists[i] == 0) {
        dSymbols[i] = (long)log2sum;
      } else {
        dSymbols[i] = (long)(log2sum - Math.log(dists[i]) * INV_LOG_2);
      }
      if (dSymbols[i] < 0) {
        dSymbols[i] = 0;
      }
    }
    for (int i = 4; i < 30; ++i) {
      dSymbols[i] += 0x10000L * ((i / 2) - 1);
    }
  }

  final void alloy(final SymbolStats ligand) {
    int[] ligandLitLens = ligand.litLens;
    for (int i = 0; i < 288; i++) {
      litLens[i] += ligandLitLens[i] / 2;
    }
    litLens[256] = 1;

    int[] ligandDists = ligand.dists;
    for (int i = 0; i < 32; i++) {
      dists[i] += ligandDists[i] / 2;
    }
  }

  final int randomizeFreqs(int z) {
    int[] data = litLens;
    int n = data.length;
    for (int i = 0; i < n; i++) {
      z = 0x7FFFFFFF & (1103515245 * z + 12345);
      if ((z >>> 4) % 3 == 0) {
        z = 0x7FFFFFFF & (1103515245 * z + 12345);
        int p = z % n;
        if (data[i] < data[p]) {
          data[i] = data[p];
        }
      }
    }
    data[256] = 1;

    data = dists;
    n = data.length;
    for (int i = 0; i < n; i++) {
      z = 0x7FFFFFFF & (1103515245 * z + 12345);
      if ((z >>> 4) % 3 == 0) {
        z = 0x7FFFFFFF & (1103515245 * z + 12345);
        int p = z % n;
        if (data[i] < data[p]) {
          data[i] = data[p];
        }
      }
    }

    return z;
  }

  final long minCost() {
    long[] lLengths = this.lLengths;
    long minLengthCost = lLengths[3];
    for (int i = 4; i < 259; i++) {
      long c = lLengths[i];
      if (c < minLengthCost) {
        minLengthCost = c;
      }
    }

    long[] dSymbols = this.dSymbols;
    long minDistCost = dSymbols[0];
    for (int i = 1; i < 30; i++) {
      long c = dSymbols[i];
      if (c < minDistCost) {
        minDistCost = c;
      }
    }

    return minDistCost + minLengthCost;
  }
}
