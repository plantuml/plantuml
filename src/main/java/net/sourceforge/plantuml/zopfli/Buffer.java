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

public class Buffer {

  byte[] data;
  int size;
  private int bp;

  Buffer() {
    data = new byte[65536];
  }

  public byte[] getData() {
    return data;
  }
  
  public byte[] getResult() {
      byte[] copy = new byte[size];
      System.arraycopy(data,0,copy,0,size);
      return copy;
  }

  public int getSize() {
    return size;
  }

  void append(byte value) {
    if (size == data.length) {
      byte[] copy = new byte[size * 2];
      System.arraycopy(data, 0, copy, 0, size);
      data = copy;
    }
    data[size++] = value;
  }

  void addBits(int symbol, int length) {
    for (int i = 0; i < length; i++) {
      if (bp == 0) {
        append((byte) 0);
      }
      int bit = (symbol >> i) & 1;
      data[size - 1] |= bit << bp;
      bp = (bp + 1) & 7;
    }
  }

  void addHuffmanBits(int symbol, int length) {
    for (int i = 0; i < length; i++) {
      if (bp == 0) {
        append((byte) 0);
      }
      int bit = (symbol >> (length - i - 1)) & 1;
      data[size - 1] |= bit << bp;
      bp = (bp + 1) & 7;
    }
  }
}
