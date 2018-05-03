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

public class Options {
  public static enum BlockSplitting {
    FIRST,
    LAST,
    NONE
  }

  public static enum OutputFormat {
    DEFLATE,
    GZIP,
    ZLIB
  }

  public final int numIterations;
  public final BlockSplitting blockSplitting;
  public final OutputFormat outputType;

  public Options(OutputFormat outputType, BlockSplitting blockSplitting,
      int numIterations) {
    this.outputType = outputType;
    this.blockSplitting = blockSplitting;
    this.numIterations = numIterations;
  }

  public Options() {
    this(OutputFormat.GZIP, BlockSplitting.FIRST, 15);
  }
}
