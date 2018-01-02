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

class Squeeze {

  static LzStore optimal(Cookie cookie, int numIterations, LongestMatchCache lmc, byte[] input, int from, int to) {
    LzStore currentStore = cookie.store1;
    currentStore.reset();
    LzStore store = cookie.store2;
    Deflate.greedy(cookie, lmc, input, from, to, currentStore);
    SymbolStats stats = cookie.stats;
    SymbolStats bestStats = cookie.bestStats;
    SymbolStats lastStats = cookie.lastStats;
    stats.getFreqs(currentStore);

    char[] lengthArray = cookie.lengthArray;
    long[] costs = cookie.costs;

    int cost;
    int bestCost = Integer.MAX_VALUE;
    int lastCost = 0;
    int lastRandomStep = -1;

    for (int i = 0; i < numIterations; i++) {
      currentStore.reset();
      bestLengths(cookie, lmc, from, input, from, to, stats.minCost(), stats, lengthArray, costs);
      optimalRun(cookie, lmc, input, from, to, lengthArray, currentStore);
      cost = Deflate.calculateBlockSize(cookie, currentStore.litLens, currentStore.dists, 0, currentStore.size);
      if (cost < bestCost) {
        store.copy(currentStore);
        bestStats.copy(stats);
        bestCost = cost;
      }
      lastStats.copy(stats);
      stats.getFreqs(currentStore);
      if (lastRandomStep != -1) {
        stats.alloy(lastStats);
        stats.calculate();
      }
      if (i > 5 && cost == lastCost) {
        stats.copy(bestStats);
        cookie.rnd = stats.randomizeFreqs(cookie.rnd);
        stats.calculate();
        lastRandomStep = i;
      }
      lastCost = cost;
    }
    return store;
  }

  static void optimalRun(Cookie cookie, LongestMatchCache lmc, byte[] input, int from, int to,
      char[] lengthArray, LzStore store) {
    // assert from != to
    char[] path = cookie.path;
    int pathSize = 0;
    int size = to - from;
    do {
      char las = lengthArray[size];
      path[pathSize++] = las;
      size -= las;
    } while (size != 0);

    int windowStart = Math.max(from - 0x8000, 0);
    Hash h = cookie.h;
    h.init(input, windowStart, from, to);
    int pos = from;

    do {
      h.updateHash(input, pos, to);
      int length = path[--pathSize];
      if (length >= 3) {
        Deflate.findLongestMatch(cookie, lmc, from, h, input, pos, to, length, null);
        store.append((char) length, (char) cookie.distVal);
      } else {
        length = 1;
        store.append((char) (input[pos] & 0xFF), (char) 0);
      }

      for (int j = 1; j < length; ++j) {
        h.updateHash(input, pos + j, to);
      }
      pos += length;
    } while (pathSize != 0);
  }

  private static long fixedCost(int litLen, int dist) {
    if (dist == 0) {
      if (litLen <= 143) {
        return 8;
      }
      return 9;
    } else {
      long cost = 12 + (dist < 4097 ? Util.CACHED_DIST_EXTRA_BITS[dist] : dist < 16385 ? dist < 8193 ? 11 : 12 : 13)
          + Util.LENGTH_EXTRA_BITS[litLen];
      if (Util.LENGTH_SYMBOL[litLen] > 279) {
        return cost + 1;
      }
      return cost;
    }
  }

  private static void bestLengths(Cookie cookie, LongestMatchCache lmc, int blockStart, byte[] input, int from, int to,
      long minCost, SymbolStats stats, char[] lengthArray, long[] costs) {
    //# WINDOW_SIZE = 0x8000
    //# WINDOW_MASK = 0x7FFF
    //# MAX_MATCH = 258

    int windowStart = Math.max(from - 0x8000, 0);
    Hash h = cookie.h;
    h.init(input, windowStart, from, to);
    Cookie.fillCostMax(costs, to - from + 1);
    costs[0] = 0L;
    lengthArray[0] = 0;
    int[] same = h.same;

    char[] subLen = cookie.c259a;
    System.arraycopy(Cookie.charZeroes, 0, subLen, 0, 259);

    long[] slLiterals = stats.lLiterals;
    long[] slLengths = stats.lLengths;
    long[] sdSymbols = stats.dSymbols;
    long stepCost = slLengths[258] + sdSymbols[0];

    int[] cachedDistSymbol = Util.CACHED_DIST_SYMBOL;

    int i = from;
    int j = 0;
    while (i < to) {
      h.updateHash(input, i, to);

      if (same[i & 0x7FFF] > 516 && i > from + 259 && i + 517 < to && same[(i - 258) & 0x7FFF] > 258) {
        for (int k = 0; k < 258; ++k) {
          costs[j + 258] = costs[j] + stepCost;
          lengthArray[j + 258] = 258;
          i++;
          j++;
          h.updateHash(input, i, to);
        }
      }

      Deflate.findLongestMatch(cookie, lmc, blockStart, h, input, i, to, 258, subLen);

      long costsJ = costs[j];
      if (i + 1 <= to) {
        long newCost = costsJ + slLiterals[input[i] & 0xFF];
        if (newCost < costs[j + 1]) {
          costs[j + 1] = newCost;
          lengthArray[j + 1] = 1;
        }
      }
      int lenValue = cookie.lenVal;
      long baseCost = minCost + costsJ;
      if (lenValue > to - i) {
        lenValue = to - i;
      }
      int jpk = j + 3;
      for (char k = 3; k <= lenValue; k++, jpk++) {
        if (costs[jpk] > baseCost) {
          long newCost = costsJ + (slLengths[k] + sdSymbols[cachedDistSymbol[subLen[k]]]);
          if (costs[jpk] > newCost) {
            costs[jpk] = newCost;
            lengthArray[jpk] = k;
          }
        }
      }

      i++;
      j++;
    }
  }

  static void bestFixedLengths(Cookie cookie, LongestMatchCache lmc, byte[] input, int from, int to,
      char[] lengthArray, long[] costs) {
    int windowStart = Math.max(from - 0x8000, 0);
    Hash h = cookie.h;
    h.init(input, windowStart, from, to);
    Cookie.fillCostMax(costs, to - from + 1);
    costs[0] = 0L;
    lengthArray[0] = 0;

    char[] subLen = cookie.c259a;
    for (int i = from; i < to; i++) {
      int j = i - from;
      h.updateHash(input, i, to);

      if (h.same[i & 0x7FFF] > 258 * 2
          && i > from + 258 + 1
          && i + 258 * 2 + 1 < to
          && h.same[(i - 258) & 0x7FFF]
          > 258) {
        long symbolCost = fixedCost(258, 1);
        for (int k = 0; k < 258; k++) {
          costs[j + 258] = costs[j] + symbolCost;
          lengthArray[j + 258] = 258;
          i++;
          j++;
          h.updateHash(input, i, to);
        }
      }

      Deflate.findLongestMatch(cookie, lmc, from, h, input, i, to, 258, subLen);

      if (i + 1 <= to) {
        long newCost = costs[j] + fixedCost(input[i] & 0xFF, 0);
        if (newCost < costs[j + 1]) {
          costs[j + 1] = newCost;
          lengthArray[j + 1] = 1;
        }
      }
      int lenValue = cookie.lenVal;
      for (char k = 3; k <= lenValue && i + k <= to; k++) {
        if (costs[j + k] - costs[j] <= 12.0) {
          continue;
        }

        long newCost = costs[j] + fixedCost(k, subLen[k]);
        if (newCost < costs[j + k]) {
          costs[j + k] = newCost;
          lengthArray[j + k] = k;
        }
      }
    }
  }
}
