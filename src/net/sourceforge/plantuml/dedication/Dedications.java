/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
 * 
 *
 */
package net.sourceforge.plantuml.dedication;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.SignatureUtils;

public class Dedications {

	private static final Map<String, Dedication> normal = new HashMap<String, Dedication>();
	private static final Map<String, Dedication> crypted = new HashMap<String, Dedication>();

	static {
		addNormal("Write your own dedication!", "dedication");
		addNormal("linux_china", "linux_china");
		addNormal("ARKBAN", "arkban");
		addNormal("Boundaries allow discipline to create true strength", "boundaries");
		addCrypted("0", "pOhci6rKgPXw32AeYXhOpSY0suoauHq5VUSwFqHLHsLYgSO6WaJ7BW5vtHBAoU6ePbcW7d8Flx99MWjPSKQTDm00");
		addCrypted("1", "LTxN3hdnhSJ515qcA7IQ841axt4GXfUd3n2wgNirYCdLnyX2360Gv1OEOnJ1-gwFzRW5B3HAqLBkR6Ge0WW_Z000");
	}

	private static void addNormal(String sentence, String name) {
		normal.put(keepLetter(sentence), new Dedication(name));
	}

	private static void addCrypted(String name, String contentKey) {
		crypted.put(contentKey, new Dedication(name));
	}

	private Dedications() {
	}

	public static Dedication get(String line) {
		final String keepLetter = keepLetter(line);
		final Dedication result = normal.get(keepLetter);
		if (result != null) {
			return result;
		}
		for (Map.Entry<String, Dedication> ent : crypted.entrySet()) {
			final Dedication dedication = ent.getValue();
			InputStream is = null;
			try {
				is = dedication.getInputStream(keepLetter);
				final String signature = SignatureUtils.getSignatureSha512(is);
				if (signature.equals(ent.getKey())) {
					return dedication;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null)
						is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return null;
	}

	public static String keepLetter(String s) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (Character.isLetterOrDigit(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
