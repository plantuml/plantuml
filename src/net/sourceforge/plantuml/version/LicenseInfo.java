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
 */
package net.sourceforge.plantuml.version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.SignatureUtils;
import net.sourceforge.plantuml.dedication.Dedication;
import net.sourceforge.plantuml.dedication.QBlock;

public class LicenseInfo {

	private final static Preferences prefs = Preferences.userNodeForPackage(LicenseInfo.class);
	private final static LicenseInfo NONE = new LicenseInfo();

	public static final int POS_TYPE = 2;
	public static final int POS_SIGNATURE = 10;
	public static final int POS_GENERATION = 100;
	public static final int POS_EXPIRATION = 108;
	public static final int POS_OWNER = 128;

	private final long generationDate;
	private final long expirationDate;
	private final String owner;

	private LicenseInfo() {
		this.expirationDate = 0;
		this.generationDate = 0;
		this.owner = null;
	}

	private LicenseInfo(Magic magic) throws NoSuchAlgorithmException, IOException {
		final String signature = SignatureUtils.toHexString(magic.get(LicenseInfo.POS_SIGNATURE, 64));
		final String local = SignatureUtils.toHexString(Magic.signature());
		if (local.equals(signature) == false) {
			throw new IOException();
		}
		final int type = magic.getByte(Magic.signature(), LicenseInfo.POS_TYPE);
		this.generationDate = bytesToLong(magic.get(LicenseInfo.POS_GENERATION, 8));
		this.expirationDate = bytesToLong(magic.get(LicenseInfo.POS_EXPIRATION, 8));
		this.owner = magic.getString(LicenseInfo.POS_OWNER);
	}

	public static long bytesToLong(byte[] b) {
		long result = 0;
		for (int i = 0; i < 8; i++) {
			result <<= 8;
			result |= (b[i] & 0xFF);
		}
		return result;
	}

	public static void persistMe(String key) throws BackingStoreException {
		prefs.sync();
		prefs.put("license", key);
	}

	private static LicenseInfo cache;

	public static synchronized LicenseInfo retrieveQuick() {
		if (cache == null) {
			return retrieveSlow();
		}
		return cache;
	}

	public static synchronized LicenseInfo retrieveSlow() {
		cache = LicenseInfo.NONE;
		if (OptionFlags.ALLOW_INCLUDE == false) {
			return cache;
		}
		final String key = prefs.get("license", "");
		if (key.length() > 0) {
			cache = setIfValid(retrieve(key), cache);
			if (cache.isValid()) {
				return cache;
			}
		}
		for (File f : fileCandidates()) {
			try {
				if (f.exists() && f.canRead()) {
					final LicenseInfo result = retrieve(f);
					cache = setIfValid(result, cache);
					if (cache.isValid()) {
						return cache;
					}
				}
			} catch (IOException e) {
				Log.info("Error " + e);
				// e.printStackTrace();
			}
		}
		return cache;
	}

	private static LicenseInfo setIfValid(LicenseInfo value, LicenseInfo def) {
		if (value.isValid() || def.isNone()) {
			return value;
		}
		return def;
	}

	private static LicenseInfo retrieve(File f) throws IOException {
		final BufferedReader br = new BufferedReader(new FileReader(f));
		final String s = br.readLine();
		br.close();
		final LicenseInfo result = retrieve(s);
		if (result != null) {
			Log.info("Reading license from " + f.getAbsolutePath());
		}
		return result;
	}

	public static LicenseInfo retrieve(final String key) {
		if (key.length() > 99 && key.matches("^[0-9a-z]+$")) {
			try {
				final BigInteger lu = new BigInteger(key, 36);
				final QBlock qb2 = new QBlock(lu);
				final QBlock qb3 = qb2.change(Dedication.E, Dedication.N);
				final Magic magic = qb3.toMagic();

				final String sig = SignatureUtils.toHexString(Magic.signature());
				magic.xor(SignatureUtils.getSHA512raw(SignatureUtils.salting(sig, Magic.getSalt(sig))));
				return new LicenseInfo(magic);
			} catch (Exception e) {
				// e.printStackTrace();
				Log.info("Error retrieving license info" + e);
			}
		}
		return LicenseInfo.NONE;
	}

	public static Collection<File> fileCandidates() {
		final Set<File> result = new TreeSet<File>();
		final String classpath = System.getProperty("java.class.path");
		String[] classpathEntries = classpath.split(File.pathSeparator);
		for (String s : classpathEntries) {
			File dir = new File(s);
			if (dir.isFile()) {
				dir = dir.getParentFile();
			}
			if (dir.isDirectory()) {
				result.add(new File(dir, "license.txt"));
			}
		}
		return result;
	}

	public static void main(String[] args) {
		LicenseInfo info = retrieveSlow();
		System.err.println("valid=" + info.isValid());
		System.err.println("info=" + info.owner);

	}

	public final Date getGenerationDate() {
		return new Date(generationDate);
	}

	public final Date getExpirationDate() {
		return new Date(expirationDate);
	}

	public final String getOwner() {
		return owner;
	}

	public boolean isNone() {
		return owner == null;
	}

	public boolean isValid() {
		return owner != null && System.currentTimeMillis() <= this.expirationDate;
	}

	public boolean hasExpired() {
		return owner != null && System.currentTimeMillis() > this.expirationDate;
	}

}
