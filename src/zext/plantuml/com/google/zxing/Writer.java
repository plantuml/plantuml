/*
 * Copyright 2008 ZXing authors
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

package zext.plantuml.com.google.zxing;

import java.util.Hashtable;

import zext.plantuml.com.google.zxing.common.BitMatrix;

/**
 * The base class for all objects which encode/generate a barcode image.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public interface Writer {

  /**
   * Encode a barcode using the default settings.
   *
   * @param contents The contents to encode in the barcode
   * @param format The barcode format to generate
   * @return The generated barcode as a Matrix of unsigned bytes (0 == black, 255 == white)
   */
  BitMatrix encode(String contents, BarcodeFormat format, int multiple)
      throws WriterException;

  /**
   *
   * @param contents The contents to encode in the barcode
   * @param format The barcode format to generate
   * @param hints Additional parameters to supply to the encoder
   * @return The generated barcode as a Matrix of unsigned bytes (0 == black, 255 == white)
   */
  BitMatrix encode(String contents, BarcodeFormat format, int multiple, Hashtable hints)
      throws WriterException;

}
