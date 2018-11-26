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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
	private final static LicenseInfo NONE = new LicenseInfo(LicenseType.NONE, 0, 0, null, null);

	public static final int POS_TYPE = 2;
	public static final int POS_CONTEXT = 4;
	public static final int POS_SIGNATURE = 10;
	public static final int POS_GENERATION = 100;
	public static final int POS_EXPIRATION = 108;
	public static final int POS_OWNER = 128;

	private final LicenseType type;
	private final long generationDate;
	private final long expirationDate;
	private final String owner;
	private final String context;

	private LicenseInfo(LicenseType type, long generationDate, long expirationDate, String owner, String context) {
		this.type = type;
		this.generationDate = generationDate;
		this.expirationDate = expirationDate;
		this.owner = owner;
		this.context = context;
	}

	private static LicenseInfo buildNamed(Magic magic, boolean doCheck) throws NoSuchAlgorithmException, IOException {
		final String signature = SignatureUtils.toHexString(magic.get(LicenseInfo.POS_SIGNATURE, 64));
		if (doCheck) {
			final String local = SignatureUtils.toHexString(Magic.signature());
			if (local.equals(signature) == false) {
				throw new IOException();
			}
		}
		final LicenseType type = LicenseType.fromInt(magic.getByte(Magic.signature(), LicenseInfo.POS_TYPE));
		final long generation = bytesToLong(magic.get(LicenseInfo.POS_GENERATION, 8));
		final long expiration = bytesToLong(magic.get(LicenseInfo.POS_EXPIRATION, 8));
		final String owner = magic.getString(LicenseInfo.POS_OWNER);
		return new LicenseInfo(type, generation, expiration, owner, null);
	}

	private static LicenseInfo buildDistributor(Magic magic) throws IOException, NoSuchAlgorithmException {
		final LicenseType type = LicenseType.fromInt(magic.getByte(LicenseInfo.POS_TYPE));
		final long generation = bytesToLong(magic.get(LicenseInfo.POS_GENERATION, 8));
		final long expiration = bytesToLong(magic.get(LicenseInfo.POS_EXPIRATION, 8));
		final String owner = magic.getString(LicenseInfo.POS_OWNER);
		final String context = magic.getString(LicenseInfo.POS_CONTEXT);
		return new LicenseInfo(type, generation, expiration, owner, context);
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
			cache = retrieveDistributor();
		}
		if (cache == null) {
			cache = retrieveNamedSlow();
		}
		return cache;
	}

	public static boolean retrieveNamedOrDistributorQuickIsValid() {
		return retrieveQuick().isValid();
	}

	public static synchronized LicenseInfo retrieveNamedSlow() {
		cache = LicenseInfo.NONE;
		if (OptionFlags.ALLOW_INCLUDE == false) {
			return cache;
		}
		final String key = prefs.get("license", "");
		if (key.length() > 0) {
			cache = setIfValid(retrieveNamed(key), cache);
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

	public static LicenseInfo retrieveNamed(final String key) {
		if (key.length() > 99 && key.matches("^[0-9a-z]+$")) {
			try {
				final String sig = SignatureUtils.toHexString(Magic.signature());
				return retrieveNamed(sig, key, true);
			} catch (Exception e) {
				// e.printStackTrace();
				Log.info("Error retrieving license info" + e);
			}
		}
		return LicenseInfo.NONE;
	}

	public static LicenseInfo retrieveNamed(final String sig, final String key, boolean doCheck)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeySpecException, IOException {
		final BigInteger lu = new BigInteger(key, 36);
		final QBlock qb2 = new QBlock(lu);
		final QBlock qb3 = qb2.change(Dedication.E, Dedication.N);
		final Magic magic = qb3.toMagic();

		magic.xor(SignatureUtils.getSHA512raw(SignatureUtils.salting(sig, Magic.getSalt(sig))));
		return LicenseInfo.buildNamed(magic, doCheck);
	}

	public static LicenseInfo retrieveDistributor() {
		final InputStream dis = PSystemVersion.class.getResourceAsStream("/distributor.txt");
		if (dis == null) {
			return null;
		}
		try {
			final BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			final String licenseString = br.readLine();
			br.close();
			final BigInteger lu = new BigInteger(licenseString, 36);
			final QBlock qb2 = new QBlock(lu);
			final QBlock qb3 = qb2.change(Dedication.E, Dedication.N);
			final Magic magic = qb3.toMagic();
			final LicenseInfo result = LicenseInfo.buildDistributor(magic);

			final Throwable creationPoint = new Throwable();
			creationPoint.fillInStackTrace();
			for (StackTraceElement ste : creationPoint.getStackTrace()) {
				if (ste.toString().contains(result.context)) {
					return result;
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
			if (dir != null && dir.isDirectory()) {
				result.add(new File(dir, "license.txt"));
			}
		}
		return result;
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
		final LicenseInfo result = retrieveNamed(s);
		if (result != null) {
			Log.info("Reading license from " + f.getAbsolutePath());
		}
		return result;
	}



	public static void main(String[] args) {
		LicenseInfo info = retrieveNamedSlow();
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

	public final LicenseType getLicenseType() {
		return type;
	}

	public final String getContext() {
		return context;
	}

}
