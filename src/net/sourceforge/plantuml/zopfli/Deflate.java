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

class Deflate {

  static enum BlockType {
    DYNAMIC,
    FIXED
  }

  // final static int WINDOW_SIZE = 0x8000;
  // final static int WINDOW_MASK = 0x7FFF;
  // final static int MAX_MATCH = 258;
  // final static int MIN_MATCH = 3;
  // final static int MAX_CHAIN_HITS = 8192; // Should be less than WINDOW_SIZE

  private static void getFixedTree(int[] llLengths, int[] dLengths) {
    for (int i = 0; i < 144; i++) {
      llLengths[i] = 8;
    }
    for (int i = 144; i < 256; i++) {
      llLengths[i] = 9;
    }
    for (int i = 256; i < 280; i++) {
      llLengths[i] = 7;
    }
    for (int i = 280; i < 288; i++) {
      llLengths[i] = 8;
    }
    for (int i = 0; i < 32; i++) {
      dLengths[i] = 5;
    }
  }

  public static void greedy(Cookie cookie, LongestMatchCache lmc, byte[] input, int from, int to, LzStore store) {
    Hash h = cookie.h;
    h.init(input, Math.max(from - 0x8000, 0), from, to);
    int prevLength = 0;
    int prevMatch = 0;
    char[] dummySubLen = cookie.c259a;
    boolean matchAvailable = false;

    for (int i = from; i < to; i++) {
      h.updateHash(input, i, to);
      findLongestMatch(cookie, lmc, from, h, input, i, to, 258, dummySubLen);
      int len = cookie.lenVal;
      int dist = cookie.distVal;
      int lengthScore = dist > 1024 ? len - 1 : len;
      int prevLengthScore = prevMatch > 1024 ? prevLength - 1 : prevLength;

      if (matchAvailable) {
        matchAvailable = false;
        if (lengthScore > prevLengthScore + 1) {
          store.append((char) (input[i - 1] & 0xFF), (char) 0);
          if (lengthScore >= 3 && len < 258) {
            matchAvailable = true;
            prevLength = len;
            prevMatch = dist;
            continue;
          }
        } else {
          store.append((char) prevLength, (char) prevMatch);
          for (int j = 2; j < prevLength; j++) {
            i++;
            h.updateHash(input, i, to);
          }
          continue;
        }
      } else if (lengthScore >= 3 && len < 258) {
        matchAvailable = true;
        prevLength = len;
        prevMatch = dist;
        continue;
      }

      if (lengthScore >= 3) {
        store.append((char) len, (char) dist);
      } else {
        len = 1;
        store.append((char) (input[i] & 0xFF), (char) 0);
      }
      for (int j = 1; j < len; j++) {
        i++;
        h.updateHash(input, i, to);
      }
    }
  }

  static void findLongestMatch(Cookie cookie, LongestMatchCache lmc, int blockStart, Hash h, byte[] array,
      int pos, int size, int limit, char[] subLen) {
    //# WINDOW_SIZE = 0x8000
    //# WINDOW_MASK = 0x7FFF
    //# MIN_MATCH = 3
    //# MAX_MATCH = 258

    int offset = pos - blockStart;
    char[] lmcLength = lmc != null ? lmc.length : null;
    if (lmc != null && ((lmcLength[offset] == 0 || lmc.dist[offset] != 0))
        && (limit == 258 || lmcLength[offset] <= limit
        || subLen != null && lmc.maxCachedSubLen(offset) >= limit)) {
      if (subLen == null || lmcLength[offset] <= lmc.maxCachedSubLen(offset)) {
        cookie.lenVal = lmcLength[offset];
        if (cookie.lenVal > limit) {
          cookie.lenVal = limit;
        }
        if (subLen != null) {
          lmc.cacheToSubLen(offset, cookie.lenVal, subLen);
          cookie.distVal = subLen[cookie.lenVal];
        } else {
          cookie.distVal = lmc.dist[offset];
        }
        return;
      }
      limit = lmcLength[offset];
    }

    if (size - pos < 3) {
      cookie.lenVal = 0;
      cookie.distVal = 0;
      return;
    }

    if (pos + limit > size) {
      limit = size - pos;
    }

    int bestDist = 0;
    int bestLength = 1;
    int arrayEnd = pos + limit;
    int chainCounter = 8192;
    int[] hPrev = h.prev;
    int[] hPrev2 = h.prev2;
    int pp = h.head[h.val];
    int threshold = h.same[pp];
    int[] hashVal2 = h.hashVal2;
    int marker = hashVal2[pp];
    int p = hPrev[pp];
    pp -= p;
    int dist = pp > 0 ? pp : pp + 0x8000;

    while (dist < 0x8000 && chainCounter > 0) {
      int scan = pos;
      int match = pos - dist;

      if (array[scan + bestLength] == array[match + bestLength]) {
        int same0 = h.same[pos & 0x7FFF];
        if (same0 > 2 && array[scan] == array[match]) {
          int same1 = h.same[match & 0x7FFF];
          int same = same0 < same1 ? same0 : same1;
          if (same > limit) {
            same = limit;
          }
          scan += same;
          match += same;
        }
        while (scan != arrayEnd && array[scan] == array[match]) {
          scan++;
          match++;
        }
        scan -= pos;

        if (scan > bestLength) {
          if (subLen != null) {
            for (int j = bestLength + 1; j <= scan; j++) {
              subLen[j] = (char) dist;
            }
          }
          bestDist = dist;
          bestLength = scan;
          if (scan >= limit) {
            break;
          }
        }
      }

      if (hPrev != hPrev2 && bestLength >= threshold && marker == hashVal2[p]) {
        hPrev = hPrev2;
      }

      pp = p;
      p = hPrev[p];
      if (p == pp) {
        break;
      }
      pp -= p;
      dist += pp > 0 ? pp : 0x8000 + pp;

      --chainCounter;
    }

    if (lmc != null && limit == 258 && subLen != null && lmcLength[offset] != 0 && lmc.dist[offset] == 0) {
      if (bestLength < 3) {
        lmc.dist[offset] = 0;
        lmcLength[offset] = 0;
      } else {
        lmc.dist[offset] = (char)bestDist;
        lmcLength[offset] = (char)bestLength;
      }
      lmc.subLenToCache(subLen, offset, bestLength);
    }

    cookie.distVal = bestDist;
    cookie.lenVal = bestLength;
  }

  private static void deflatePart(Cookie cookie, Options options, byte[] input, int from, int to, boolean flush,
      Buffer output) {
    // assert from != to
    switch (options.blockSplitting) {
      case FIRST:
        deflateSplittingFirst(cookie, options, flush, input, from, to, output);
        break;

      case LAST:
        deflateSplittingLast(cookie, options, flush, input, from, to, output);
        break;

      case NONE:
        deflateDynamicBlock(cookie, options, flush, input, from, to, output);
        break;
    }
  }

  private static void deflateDynamicBlock(Cookie cookie, Options options, boolean flush, byte[] input,
      int from, int to, Buffer output) {
    // assert from != to
    LongestMatchCache lmc = cookie.lmc;
    lmc.init(to - from);

    BlockType type = BlockType.DYNAMIC;
    LzStore store = Squeeze.optimal(cookie, options.numIterations, lmc, input, from, to);

    if (store.size < 1000) {
      LzStore fixedStore = cookie.store1;
      fixedStore.reset();
      Squeeze.bestFixedLengths(cookie, lmc, input, from, to, cookie.lengthArray, cookie.costs);
      Squeeze.optimalRun(cookie, lmc, input, from, to, cookie.lengthArray, fixedStore);
      int dynCost = calculateBlockSize(cookie, store.litLens, store.dists, 0, store.size);
      int fixedCost = calculateFixedBlockSize(cookie, fixedStore.litLens,
          fixedStore.dists, fixedStore.size);
      if (fixedCost < dynCost) {
        type = BlockType.FIXED;
        store = fixedStore;
      }
    }

    addLzBlock(cookie, type, flush, store.litLens, store.dists, 0, store.size, output);
  }

  private static void deflateSplittingLast(Cookie cookie, Options options, boolean flush,
      byte[] input, int from, int to, Buffer output) {
    // assert from != to
    LongestMatchCache lmc = cookie.lmc;
    lmc.init(to - from);

    LzStore store = Squeeze.optimal(cookie, options.numIterations, lmc, input, from, to);

    int nPoints = BlockSplitter.splitLz(cookie, store.litLens, store.dists, store.size);

    int[] splitPoints = cookie.splitPoints;
    for (int i = 1; i <= nPoints; i++) {
      int start = splitPoints[i - 1];
      int end = splitPoints[i];
      addLzBlock(cookie, BlockType.DYNAMIC, i == nPoints && flush, store.litLens, store.dists, start, end, output);
    }
  }

  private static void deflateSplittingFirst(Cookie cookie, Options options, boolean flush,
      byte[] input, int from, int to, Buffer output) {
    // assert from != to
    int nPoints = BlockSplitter.split(cookie, input, from, to);
    int[] splitPoints = cookie.splitPoints;
    for (int i = 1; i <= nPoints; ++i) {
      deflateDynamicBlock(cookie, options, i == nPoints && flush, input, splitPoints[i - 1], splitPoints[i], output);
    }
  }

  static int calculateBlockSize(Cookie cookie, char[] litLens, char[] dists, int lStart, int lEnd) {
    int[] llLengths = cookie.i288a;
    System.arraycopy(Cookie.intZeroes, 0, llLengths, 0, 288);
    int[] dLengths = cookie.i32a;
    System.arraycopy(Cookie.intZeroes, 0, dLengths, 0, 32);

    int result = 3;

    int[] llCounts = cookie.i288b;
    System.arraycopy(Cookie.intZeroes, 0, llCounts, 0, 288);
    int[] dCounts = cookie.i32b;
    System.arraycopy(Cookie.intZeroes, 0, dCounts, 0, 32);

    int[] lengthSymbol = Util.LENGTH_SYMBOL;
    int[] cachedDistSymbol = Util.CACHED_DIST_SYMBOL;
    int[] lengthExtraBits = Util.LENGTH_EXTRA_BITS;
    for (int i = lStart; i < lEnd; i++) {
      int d = dists[i];
      int l = litLens[i];
      if (d == 0) {
        llCounts[l]++;
      } else {
        llCounts[lengthSymbol[l]]++;
        int distSymbol = cachedDistSymbol[d];
        dCounts[distSymbol]++;
        result += lengthExtraBits[l];
        if (distSymbol > 3) {
          result += (distSymbol / 2) - 1;
        }
      }
    }
    llCounts[256] = 1;


    int[] llCountsCopy = cookie.i288c;
    System.arraycopy(llCounts, 0, llCountsCopy, 0, 288);
    optimizeHuffmanForRle(cookie, llCountsCopy);
    Katajainen.lengthLimitedCodeLengths(cookie, llCountsCopy, 15, llLengths);

    int[] dCountsCopy = cookie.i32c;
    System.arraycopy(dCounts, 0, dCountsCopy, 0, 32);
    optimizeHuffmanForRle(cookie, dCountsCopy);
    Katajainen.lengthLimitedCodeLengths(cookie, dCountsCopy, 15, dLengths);
    patchDistanceCodesForBuggyDecoders(dLengths);

    result += simulateAddDynamicTree(cookie, llLengths, dLengths);

    for (int i = 0; i < 288; ++i) {
      result += llCounts[i] * llLengths[i];
    }
    for (int i = 0; i < 32; ++i) {
      result += dCounts[i] * dLengths[i];
    }
    return result;
  }

  private static int calculateFixedBlockSize(Cookie cookie, char[] litLens, char[] dists, int size) {
    int[] llLengths = cookie.i288a;
    int[] dLengths = cookie.i32a;
    getFixedTree(llLengths, dLengths);

    int result = 3;

    int[] cachedDistExtraBits = Util.CACHED_DIST_EXTRA_BITS;
    int[] lengthExtraBits = Util.LENGTH_EXTRA_BITS;
    int[] lengthSymbol = Util.LENGTH_SYMBOL;

    for (int i = 0; i < size; i++) {
      int d = dists[i];
      int l = litLens[i];
      if (d == 0) {
        result += llLengths[l];
      } else {
        result += llLengths[lengthSymbol[l]];
        result += lengthExtraBits[l];
        result += 5;
        result += d < 4097 ? cachedDistExtraBits[d] : d < 16385 ? d < 8193 ? 11 : 12 : 13;
      }
    }
    result += llLengths[256];

    return result;
  }

  private static void lzCounts(char[] litLens, char[] dists, int start, int end, int[] llCount, int[] dCount) {
    int[] lengthSymbol = Util.LENGTH_SYMBOL;
    int[] cachedDistSymbol = Util.CACHED_DIST_SYMBOL;
    for (int i = start; i < end; i++) {
      int d = dists[i];
      int l = litLens[i];
      if (d == 0) {
        llCount[l]++;
      } else {
        llCount[lengthSymbol[l]]++;
        dCount[cachedDistSymbol[d]]++;
      }
    }

    llCount[256] = 1;
  }

  static void compress(Cookie cookie, Options options, byte[] input, Buffer output) {
    int i = 0;
    while (i < input.length) {
      int j = Math.min(i + cookie.masterBlockSize, input.length);
      deflatePart(cookie, options, input, i, j, j == input.length, output);
      i = j;
    }
  }

  private static void patchDistanceCodesForBuggyDecoders(int[] dLengths) {
    int numDistCodes = 0;
    for (int i = 0; i < 30; i++) {
      if (dLengths[i] != 0) {
        numDistCodes++;
        if (numDistCodes == 2) {
          return;
        }
      }
    }

    if (numDistCodes == 0) {
      dLengths[0] = 1;
      dLengths[1] = 1;
    } else if (numDistCodes == 1) {
      dLengths[dLengths[0] != 0 ? 1 : 0] = 1;
    }
  }

  private static void addDynamicTree(Cookie cookie, int[] llLengths, int[] dLengths, Buffer output) {
    int best = 0;
    int bestSize = Integer.MAX_VALUE;

    for(int i = 0; i < 8; i++) {
      int size = simulateEncodeTree(cookie, llLengths, dLengths, (i & 1) != 0, (i & 2) != 0, (i & 4) != 0);
      if (size < bestSize) {
        bestSize = size;
        best = i;
      }
    }

    encodeTree(cookie, llLengths, dLengths, (best & 1) != 0, (best & 2) != 0, (best & 4) != 0, output);
  }

  private static void encodeTree(Cookie cookie, int[] llLengths, int[] dLengths,
      boolean use16, boolean use17, boolean use18, Buffer output) {
    int hLit = 29;
    int hDist = 29;

    while (hLit > 0 && llLengths[256 + hLit] == 0) {
      hLit--;
    }
    while (hDist > 0 && dLengths[hDist] == 0) {
      hDist--;
    }

    int lldTotal = hLit + 258 + hDist;
    int[] lldLengths = cookie.i320b;
    System.arraycopy(llLengths, 0, lldLengths, 0, 257 + hLit);
    System.arraycopy(dLengths, 0, lldLengths, 257 + hLit, hDist + 1);

    int rleSize = 0;
    int[] rle = cookie.i320a;
    int[] rleBits = cookie.i320c;

    for (int i = 0; i < lldTotal; i++) {
      int count = 1;
      int symbol = lldLengths[i];
      if (use16 || (symbol == 0 && (use17 || use18))) {
        for (int j = i + 1; j < lldTotal && symbol == lldLengths[j]; j++) {
          count++;
        }
      }
      i += count - 1;

      if (symbol == 0 && count > 2) {
        if (use18) {
          while (count > 10) {
            int delta = count > 138 ? 138 : count;
            rle[rleSize] = 18;
            rleBits[rleSize++] = delta - 11;
            count -= delta;
          }
        }
        if (use17) {
          while (count > 2) {
            int delta = count > 10 ? 10 : count;
            rle[rleSize] = 17;
            rleBits[rleSize++] = delta - 3;
            count -= delta;
          }
        }
      }

      if (use16 && count > 3) {
        count--;
        rle[rleSize] = symbol;
        rleBits[rleSize++] = 0;
        while (count > 2) {
          int delta = count > 6 ? 6 : count;
          rle[rleSize] = 16;
          rleBits[rleSize++] = delta - 3;
          count -= delta;
        }
      }

      while (count != 0) {
        rle[rleSize] = symbol;
        rleBits[rleSize++] = 0;
        count--;
      }
    }

    int[] clCounts = cookie.i19a;
    System.arraycopy(Cookie.intZeroes, 0, clCounts, 0, 19);
    for (int i = 0; i < rleSize; ++i) {
      clCounts[rle[i]]++;
    }

    int[] clCl = cookie.i19b;
    System.arraycopy(Cookie.intZeroes, 0, clCl, 0, 19);
    Katajainen.lengthLimitedCodeLengths(cookie, clCounts, 7, clCl);
    int[] clSymbols = cookie.i19c;
    lengthsToSymbols(clCl, 19, 7, clSymbols, cookie.i16a, cookie.i16b);

    int[] order = Util.ORDER;
    int hcLen = 15;
    while (hcLen > 0 && clCounts[order[hcLen + 3]] == 0) {
      hcLen--;
    }

    output.addBits(hLit, 5);
    output.addBits(hDist, 5);
    output.addBits(hcLen, 4);

    for (int i = 0; i < hcLen + 4; i++) {
      output.addBits(clCl[order[i]], 3);
    }

    for (int i = 0; i < rleSize; i++) {
      int symbol = clSymbols[rle[i]];
      output.addHuffmanBits(symbol, clCl[rle[i]]);
      if (rle[i] == 16) {
        output.addBits(rleBits[i], 2);
      } else if (rle[i] == 17) {
        output.addBits(rleBits[i], 3);
      } else if (rle[i] == 18) {
        output.addBits(rleBits[i], 7);
      }
    }
  }

  private static int simulateAddDynamicTree(Cookie cookie, int[] llLengths, int[] dLengths) {
    int bestSize = Integer.MAX_VALUE;

    for(int i = 0; i < 8; i++) {
      int size = simulateEncodeTree(cookie, llLengths, dLengths, (i & 1) != 0, (i & 2) != 0, (i & 4) != 0);
      if (size < bestSize) {
        bestSize = size;
      }
    }
    return bestSize;
  }

  // TODO: GetRid of RLE
  private static int simulateEncodeTree(Cookie cookie, int[] llLengths, int[] dLengths,
      boolean use16, boolean use17, boolean use18) {
    int hLit = 29;
    int hDist = 29;

    while (hLit > 0 && llLengths[256 + hLit] == 0) {
      hLit--;
    }
    while (hDist > 0 && dLengths[hDist] == 0) {
      hDist--;
    }

    int lldTotal = hLit + 258 + hDist;
    int[] lldLengths = cookie.i320b;
    System.arraycopy(llLengths, 0, lldLengths, 0, 257 + hLit);
    System.arraycopy(dLengths, 0, lldLengths, 257 + hLit, hDist + 1);

    int[] rle = cookie.i320a;
    int rleSize = 0;

    for (int i = 0; i < lldTotal; i++) {
      int count = 1;
      int symbol = lldLengths[i];
      if (use16 || (symbol == 0 && (use17 || use18))) {
        for (int j = i + 1; j < lldTotal && symbol == lldLengths[j]; j++) {
          count++;
        }
      }
      i += count - 1;

      if (symbol == 0 && count > 2) {
        if (use18) {
          while (count > 10) {
            rle[rleSize++] = 18;
            count -= count > 138 ? 138 : count;
          }
        }
        if (use17) {
          while (count > 2) {
            rle[rleSize++] = 17;
            count -= count > 10 ? 10 : count;
          }
        }
      }

      if (use16 && count > 3) {
        count--;
        rle[rleSize++] = symbol;
        while (count > 2) {
          rle[rleSize++] = 16;
          count -= count > 6 ? 6 : count;
        }
      }

      while (count != 0) {
        rle[rleSize++] = symbol;
        count--;
      }
    }

    int[] clCounts = cookie.i19a;
    System.arraycopy(Cookie.intZeroes, 0, clCounts, 0, 19);
    for (int i = 0; i < rleSize; ++i) {
      clCounts[rle[i]]++;
    }

    int[] clCl = cookie.i19b;
    System.arraycopy(Cookie.intZeroes, 0, clCl, 0, 19);
    Katajainen.lengthLimitedCodeLengths(cookie, clCounts, 7, clCl);
    clCl[16] += 2;
    clCl[17] += 3;
    clCl[18] += 7;

    int[] order = Util.ORDER;
    int hcLen = 15;
    while (hcLen > 0 && clCounts[order[hcLen + 3]] == 0) {
      hcLen--;
    }

    int result = 5 + 5 + 4 + (hcLen + 4) * 3;
    for (int i = 0; i < 19; i++) {
      result += clCl[i] * clCounts[i];
    }

    return result;
  }

  private static void addLzBlock(Cookie cookie, BlockType type, boolean last, char[] litLens, char[] dists,
      int lStart, int lEnd, Buffer output) {
    int[] llLengths = cookie.i288a;
    System.arraycopy(Cookie.intZeroes, 0, llLengths, 0, 288);
    int[] dLengths = cookie.i32a;
    System.arraycopy(Cookie.intZeroes, 0, dLengths, 0, 32);
    int[] llCounts = cookie.i288b;
    System.arraycopy(Cookie.intZeroes, 0, llCounts, 0, 288);
    int[] dCounts = cookie.i32b;
    System.arraycopy(Cookie.intZeroes, 0, dCounts, 0, 32);

    output.addHuffmanBits(last ? 1 : 0, 1);
    if (type == BlockType.FIXED) {
      output.addHuffmanBits(2, 2); // 1, 0
    } else { // DYNAMIC
      output.addHuffmanBits(1, 2); // 0, 1
    }

    if (type == BlockType.FIXED) {
      getFixedTree(llLengths, dLengths);
    } else { // DYNAMIC
      lzCounts(litLens, dists, lStart, lEnd, llCounts, dCounts);
      optimizeHuffmanForRle(cookie, llCounts);
      Katajainen.lengthLimitedCodeLengths(cookie, llCounts, 15, llLengths);
      optimizeHuffmanForRle(cookie, dCounts);
      Katajainen.lengthLimitedCodeLengths(cookie, dCounts, 15, dLengths);
      patchDistanceCodesForBuggyDecoders(dLengths);
      addDynamicTree(cookie, llLengths, dLengths, output);
    }

    int[] llSymbols = cookie.i288c;
    System.arraycopy(Cookie.intZeroes, 0, llSymbols, 0, 288);
    lengthsToSymbols(llLengths, 288, 15, llSymbols, cookie.i16a, cookie.i16b);

    int[] dSymbols = cookie.i32b;
    System.arraycopy(Cookie.intZeroes, 0, dSymbols, 0, 32);
    lengthsToSymbols(dLengths, 32, 15, dSymbols, cookie.i16a, cookie.i16b);

    addLzData(litLens, dists, lStart, lEnd, llSymbols, llLengths, dSymbols, dLengths, output);
    output.addHuffmanBits(llSymbols[256], llLengths[256]);
  }

  private static void addLzData(char[] litLens, char[] dists, int lStart, int lEnd,
      int[] llSymbols, int[] llLengths, int[] dSymbols, int[] dLengths, Buffer output) {
    int[] cachedDistExtraBits = Util.CACHED_DIST_EXTRA_BITS;
    int[] lengthExtraBits = Util.LENGTH_EXTRA_BITS;
    int[] lengthExtraBitsValue = Util.LENGTH_EXTRA_BITS_VALUE;
    int[] lengthSymbol = Util.LENGTH_SYMBOL;
    int[] cachedDistSymbol = Util.CACHED_DIST_SYMBOL;
    for (int i = lStart; i < lEnd; i++) {
      int dist = dists[i];
      int litLen = litLens[i];
      if (dist == 0) {
        output.addHuffmanBits(llSymbols[litLen], llLengths[litLen]);
      } else {
        int lls = lengthSymbol[litLen];
        int ds = cachedDistSymbol[dist];
        output.addHuffmanBits(llSymbols[lls], llLengths[lls]);
        output.addBits(lengthExtraBitsValue[litLen], lengthExtraBits[litLen]);
        output.addHuffmanBits(dSymbols[ds], dLengths[ds]);
        output.addBits(Util.distExtraBitsValue(dist),
            dist < 4097 ? cachedDistExtraBits[dist] : dist < 16385 ? dist < 8193 ? 11 : 12 : 13);
      }
    }
  }

  private static void lengthsToSymbols(int[] lengths, int n, int maxBits, int[] symbols, int[] blCount, int[] nextCode) {
    System.arraycopy(Cookie.intZeroes, 0, blCount, 0, maxBits + 1);
    System.arraycopy(Cookie.intZeroes, 0, nextCode, 0, maxBits + 1);
    for (int i = 0; i < n; ++i) {
      blCount[lengths[i]]++;
    }
    int code = 0;
    blCount[0] = 0;
    for (int bits = 1; bits <= maxBits; bits++) {
      code = (code + blCount[bits - 1]) << 1;
      nextCode[bits] = code;
    }
    for (int i = 0; i < n; i++) {
      int len = lengths[i];
      if (len != 0) {
        symbols[i] = nextCode[len];
        nextCode[len]++;
      }
    }
  }

  private static void optimizeHuffmanForRle(Cookie cookie, int[] counts) {
    int[] goodForRle = cookie.i289a;
    int length = counts.length;
    for (; length >= 0; --length) {
      if (length == 0) {
        return;
      }
      if (counts[length - 1] != 0) {
        break;
      }
    }
    System.arraycopy(Cookie.intZeroes, 0, goodForRle, 0, length + 1);

    int symbol = counts[0];
    int stride = 0;
    for (int i = 0; i < length + 1; ++i) {
      if (i == length || counts[i] != symbol) {
        if ((symbol == 0 && stride >= 5) || (symbol != 0 && stride >= 7)) {
          for (int k = 0; k < stride; ++k) {
            goodForRle[i - k - 1] = 1;
          }
        }
        stride = 1;
        if (i != length) {
          symbol = counts[i];
        }
      } else {
        ++stride;
      }
    }

    stride = 0;
    int limit = counts[0];
    int sum = 0;
    for (int i = 0; i < length + 1; ++i) {
      if ((i == length) || (goodForRle[i] != 0) || (counts[i] - limit >= 4) || (limit - counts[i] >= 4)) {
        if ((stride >= 4) || ((stride >= 3) && (sum == 0))) {
          int count = (sum + stride / 2) / stride;
          if (count < 1) count = 1;
          if (sum == 0) {
            count = 0;
          }
          for (int k = 0; k < stride; ++k) {
            counts[i - k - 1] = count;
          }
        }
        stride = 0;
        sum = 0;
        if (i < length - 3) {
          limit = (counts[i] + counts[i + 1] + counts[i + 2] + counts[i + 3] + 2) / 4;
        } else if (i < length) {
          limit = counts[i];
        } else {
          limit = 0;
        }
      }
      ++stride;
      if (i != length) {
        sum += counts[i];
      }
    }
  }
}
