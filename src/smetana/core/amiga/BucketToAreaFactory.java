/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  http://plantuml.com
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * This translation is distributed under the same Licence as the original C program.
 * 
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC
 * LICENSE ("AGREEMENT"). [Eclipse Public License - v 1.0]
 * 
 * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES
 * RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 * 
 * You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package smetana.core.amiga;

import smetana.core.Bucket;
import smetana.core.CType;
import smetana.core.JUtils;
import smetana.core.__array_of_double__;
import smetana.core.__array_of_integer__;
import smetana.core.__array_of_ptr__;
import smetana.core.__array_of_struct__;

public class BucketToAreaFactory {

	public static Area createArea(Bucket bucket, StarStruct parent) {
		if (bucket.ctype.getArrayLength() != 0) {
			return createAreaArray(bucket, bucket.ctype.getArrayLength());
		}
		if (bucket.ctype.functionPointer()) {
			// return PointerToNull.nullPointerTo();
			// return new AreaFunctionPointer();
			return null;
		}
		if (bucket.ctype.isIntStar()) {
			return null;
		}
		if (bucket.ctype.isDoubleStar()) {
			return null;
		}
		if (bucket.ctype.isVoidStar()) {
			return null;
			// return PointerToNull.nullPointerTo();
			// return new AreaVoidStar();
		}
		if (bucket.ctype.containsStar()) {
			final String type = bucket.ctype.getType();
			if (type.matches("\\w+\\*")) {
				final Class theClass = CType.getClassFrom(type.substring(0, type.length() - 1));
				JUtils.LOG("theClass=" + theClass);
				// return PointerToNull.nullPointerTo();
				return null;
			}
			throw new UnsupportedOperationException(bucket.toString());
		}
		if (bucket.ctype.isEnum()) {
			return new AreaInt();
		}
		if (bucket.ctype.isPrimitive()) {
			if (bucket.ctype.isInteger()) {
				return new AreaInt();
			}
			if (bucket.ctype.isChar()) {
				return new AreaInt();
			}
			if (bucket.ctype.isShort()) {
				return new AreaInt();
			}
			if (bucket.ctype.isLong()) {
				return new AreaInt();
			}
			if (bucket.ctype.isBoolean()) {
				return new AreaInt();
			}
			if (bucket.ctype.isDoubleOrFloat()) {
				return new AreaDouble();
			}
			throw new UnsupportedOperationException();
		}
		if (bucket.inlineStruct()) {
			final Class theClass = bucket.ctype.getTypeClass();
			return new StarStruct(theClass, parent);
		}
		if (bucket.ctype.isArrayOfCString()) {
			return null;
		}
		if (bucket.ctype.isCString()) {
			// return new AreaCString();
			// return PointerToNull.nullPointerTo();
			return null;
		}
		final Class theClass = bucket.ctype.getTypeClass();
		if (theClass != null) {
			return null;
			// return PointerToNull.nullPointerTo();
		}
		JUtils.LOG("BucketToAreaFactory:: theClass = " + theClass);
		JUtils.LOG("BucketToAreaFactory:: bucket=" + bucket);
		JUtils.LOG("BucketToAreaFactory:: bucket.ctype=" + bucket.ctype);
		throw new UnsupportedOperationException();
	}

	private static Area createAreaArray(Bucket bucket, int arrayLength) {
		JUtils.LOG("BucketToAreaFactory:createAreaArray: bucket=" + bucket);
		JUtils.LOG("BucketToAreaFactory:createAreaArray: arrayLength=" + arrayLength);
		JUtils.LOG("BucketToAreaFactory:createAreaArray: type=" + bucket.ctype);
		if (bucket.ctype.getType().matches("char \\w+\\[\\d+\\]")) {
			// Array of char
			return __array_of_integer__.mallocInteger(arrayLength);
		}
		if (bucket.ctype.getType().matches("int \\w+\\[\\d+\\]")) {
			// Array of int
			return __array_of_integer__.mallocInteger(arrayLength);
		}
		if (bucket.ctype.getType().matches("double \\w+\\[\\d+\\]")) {
			// Array of double
			return __array_of_double__.mallocDouble(arrayLength);
		}
		if (bucket.ctype.getType().matches("\\w+ \\*\\w+\\[\\d+\\]")) {
			// Array of pointer
			final String element = bucket.ctype.getType().split(" ")[0];
			JUtils.LOG("element=" + element);
			final Class theClass = CType.getClassFrom(element);
			JUtils.LOG("theClass=" + theClass);
			return __array_of_ptr__.malloc_empty(arrayLength);
		}
		if (bucket.ctype.getType().matches("\\w+ \\w+\\[\\d+\\]")) {
			// Array of Struct
			final String element = bucket.ctype.getType().split(" ")[0];
			JUtils.LOG("element=" + element);
			final Class theClass = CType.getClassFrom(element);
			JUtils.LOG("theClass=" + theClass);
			return __array_of_struct__.malloc(theClass, arrayLength);
		}
		throw new UnsupportedOperationException();
	}
}
