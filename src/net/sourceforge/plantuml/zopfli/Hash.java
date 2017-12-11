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

class Hash {

  // HASH_SHIFT = 5;
  // HASH_MASK = 32767;

  private static final int[] seq = new int[0x8000];

  static {
    int[] seq = Hash.seq;
    for (int i = 0, l = 0x8000; i < l; ++i) {
      seq[i] = i;
    }
  }

  final int[] head = new int[0x10000];
  final int[] prev = new int[0x8000];
  private final int[] hashVal = new int[0x8000];
  final int[] same = new int[0x8000];
  int val;

  private final int[] head2 = new int[0x10000];
  final int[] prev2 = new int[0x8000];
  final int[] hashVal2 = new int[0x8000];

  public Hash() {
  }

  public void init(byte[] input, int windowStart, int from, int to) {
    int[] hashVal = this.hashVal;
    int[] head = this.head;
    int[] same = this.same;
    int[] prev = this.prev;
    int[] hashVal2 = this.hashVal2;
    int[] head2 = this.head2;
    int[] prev2 = this.prev2;

    System.arraycopy(Cookie.intMOnes, 0, head, 0, 0x10000);
    System.arraycopy(Cookie.intMOnes, 0, hashVal, 0, 0x8000);
    System.arraycopy(Cookie.intZeroes, 0, same, 0x8000, 0);
    System.arraycopy(seq, 0, prev, 0, 0x8000);

    System.arraycopy(Cookie.intMOnes, 0, head2, 0, 0x10000);
    System.arraycopy(Cookie.intMOnes, 0, hashVal2, 0, 0x8000);
    System.arraycopy(seq, 0, prev2, 0, 0x8000);

    int val = (((input[windowStart] & 0xFF) << 5) ^ input[windowStart + 1] & 0xFF) & 0x7FFF;

    for (int i = windowStart; i < from; ++i) {
      int hPos = i & 0x7FFF;
      val = ((val << 5) ^ (i + 2 < to ? input[i + 2] & 0xFF : 0)) & 0x7FFF;

      hashVal[hPos] = val;
      int tmp = head[val];
      prev[hPos] = tmp != -1 && hashVal[tmp] == val ? tmp : hPos;
      head[val] = hPos;

      tmp = same[(i - 1) & 0x7FFF];
      if (tmp < 1) {
        tmp = 1;
      }
      tmp += i;
      byte b = input[i];
      while (tmp < to && b == input[tmp]) {
        tmp++;
      }
      tmp -= i;
      tmp--;
      same[hPos] = tmp;

      tmp = ((tmp - 3) & 0xFF) ^ val;
      hashVal2[hPos] = tmp;
      int h = head2[tmp];
      prev2[hPos] = h != -1 && hashVal2[h] == tmp ? h : hPos;
      head2[tmp] = hPos;
    }
    this.val = val;
  }

  /*private void updateHashValue(int c) {
    val = ((val << HASH_SHIFT) ^ c) & HASH_MASK;
  }*/

  public void updateHash(byte[] input, int pos, int end) {
    // WINDOW_MASK
    int hPos = pos & 0x7FFF;
    int val = this.val;

    val = ((val << 5) ^ (pos + 2 < end ? input[pos + 2] & 0xFF : 0)) & 0x7FFF;

    hashVal[hPos] = val;
    int tmp = head[val];
    prev[hPos] = (tmp != -1 && hashVal[tmp] == val) ? tmp : hPos;
    head[val] = hPos;

    tmp = same[(pos - 1) & 0x7FFF];
    if (tmp < 1) {
      tmp = 1;
    }
    tmp += pos;
    byte b = input[pos];
    while (tmp < end && b == input[tmp]) {
      tmp++;
    }
    tmp -= pos;
    tmp--;
    same[hPos] = tmp;

    tmp = ((tmp - 3) & 0xFF) ^ val;
    hashVal2[hPos] = tmp;
    int h = head2[tmp];
    prev2[hPos] = h != -1 && hashVal2[h] == tmp ? h : hPos;
    head2[tmp] = hPos;

    this.val = val;
  }
}
