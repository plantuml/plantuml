/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.webp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.version.PSystemVersion;

public class Portraits {

	private final static List<Portrait> all = new ArrayList<Portrait>();
	private final static AtomicInteger current = new AtomicInteger();

	private static InputStream getInputStream() {
		return PSystemVersion.class.getResourceAsStream("out.png");
	}

	static {
		final InputStream is = getInputStream();
		if (is != null) {
			try {
				read(is);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static void read(InputStream is) throws IOException {
		final DataInputStream dis = new DataInputStream(is);
		final int nb = dis.readShort();
		final List<String> names = new ArrayList<String>();
		final List<Integer> ages = new ArrayList<Integer>();
		final List<String> quotes = new ArrayList<String>();
		for (int i = 0; i < nb; i++) {
			names.add(dis.readUTF());
			ages.add((int) dis.readByte());
			quotes.add(dis.readUTF());
		}
		for (int i = 0; i < nb; i++) {
			final int len = dis.readShort();
			final byte data[] = new byte[len];
			dis.readFully(data);
			all.add(new Portrait(names.get(i), ages.get(i), quotes.get(i), data));
		}
		Collections.shuffle(all);
	}

	public static Portrait getOne() {
		if (all.size() == 0) {
			return null;
		}
		final int nb = current.get() % all.size();
		return all.get(nb);
	}

	public static void nextOne() {
		current.getAndIncrement();
	}

	public static Portrait getOne(String line) {
		Portrait candidat = null;
		for (Portrait p : all) {
			final int dist = similar(p.getName(), line);
			if (dist <= 3) {
				if (candidat != null && dist < similar(candidat.getName(), line)) {
					continue;
				}
				candidat = p;
			}
		}
		return candidat;
	}

	public static int similar(String s1, String s2) {
		final int[] tab1 = countLetter(s1);
		final int[] tab2 = countLetter(s2);
		int result = 0;
		for (int i = 0; i < tab1.length; i++) {
			result += Math.abs(tab1[i] - tab2[i]);
		}
		return result;
	}

	private static String noAccent(String str) {
		final String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
		final Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	private static int[] countLetter(String s) {
		s = noAccent(s).toLowerCase(Locale.US);
		final int[] result = new int[26];
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c >= 'a' && c <= 'z') {
				result[c - 'a']++;
			}
		}
		return result;
	}

	static final List<Portrait> getAll() {
		return all;
	}
}
