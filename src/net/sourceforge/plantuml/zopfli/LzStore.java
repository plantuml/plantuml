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

final class LzStore {
  final char[] litLens;
  final char[] dists;
  int size;

  LzStore(final int maxBlockSize) {
    litLens = new char[maxBlockSize];
    dists = new char[maxBlockSize];
  }

  final void append(final char length, final char dist) {
    litLens[size] = length;
    dists[size++] = dist;
  }

  final void reset() {
    size = 0;
  }

  final void copy(final LzStore source) {
    size = source.size;
    System.arraycopy(source.litLens, 0, litLens, 0, size);
    System.arraycopy(source.dists, 0, dists, 0, size);
  }
}
