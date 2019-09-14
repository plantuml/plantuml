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
package net.sourceforge.plantuml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import net.sourceforge.plantuml.code.AsciiEncoder;

public class SignatureUtils {

	// private static byte[] salting(String pass, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException,
	// UnsupportedEncodingException {
	// final byte[] tmp = salting2(pass, salt);
	// return SignatureUtils.getSHA512raw(tmp);
	// }

	public static synchronized byte[] salting(String pass, byte[] salt) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		final int iterations = 500;
		final int keyLength = 512;
		final SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		final PBEKeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, iterations, keyLength);
		final SecretKey key = skf.generateSecret(spec);
		final byte[] tmp = key.getEncoded();
		return tmp;
	}

	public static String getSignature(String s) {
		try {
			final byte[] digest = getMD5raw(s);
			return toString(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		}
	}

	public static String toString(byte data[]) {
		final AsciiEncoder coder = new AsciiEncoder();
		return coder.encode(data);
	}

	public static String toHexString(byte data[]) {
		final StringBuilder sb = new StringBuilder(data.length * 2);
		for (byte b : data) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	public static String getMD5Hex(String s) {
		try {
			final byte[] digest = getMD5raw(s);
			assert digest.length == 16;
			return toHexString(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		}
	}

	public static String getSHA512Hex(String s) {
		try {
			final byte[] digest = getSHA512raw(s);
			assert digest.length == 64;
			return toHexString(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		}
	}

	public static synchronized byte[] getMD5raw(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		final MessageDigest msgDigest = MessageDigest.getInstance("MD5");
		msgDigest.update(s.getBytes("UTF-8"));
		return msgDigest.digest();
	}

	public static byte[] getSHA512raw(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return getSHA512raw(s.getBytes("UTF-8"));
	}

	public static synchronized byte[] getSHA512raw(byte data[]) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		final MessageDigest msgDigest = MessageDigest.getInstance("SHA-512");
		msgDigest.update(data);
		return msgDigest.digest();
	}

	public static String getSignatureSha512(File f) throws IOException {
		final InputStream is = new FileInputStream(f);
		try {
			return getSignatureSha512(is);
		} finally {
			is.close();
		}
	}

	public static synchronized String getSignatureSha512(InputStream is) throws IOException {
		try {
			final MessageDigest msgDigest = MessageDigest.getInstance("SHA-512");
			int read = 0;
			while ((read = is.read()) != -1) {
				msgDigest.update((byte) read);
			}
			final byte[] digest = msgDigest.digest();
			return toString(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		}
	}

	public static String getSignatureWithoutImgSrc(String s) {
		s = getSignature(purge(s));
		return s;
	}

	public static String purge(String s) {
		final String regex = "(?i)\\<img\\s+src=\"(?:[^\"]+[/\\\\])?([^/\\\\\\d.]+)\\d*(\\.\\w+)\"/\\>";
		s = s.replaceAll(regex, "<img src=\"$1$2\"/>");
		final String regex2 = "(?i)image=\"(?:[^\"]+[/\\\\])?([^/\\\\\\d.]+)\\d*(\\.\\w+)\"";
		s = s.replaceAll(regex2, "image=\"$1$2\"");
		return s;
	}

	public static synchronized String getSignature(File f) throws IOException {
		try {
			final MessageDigest msgDigest = MessageDigest.getInstance("MD5");
			final FileInputStream is = new FileInputStream(f);
			int read = -1;
			while ((read = is.read()) != -1) {
				msgDigest.update((byte) read);
			}
			is.close();
			final byte[] digest = msgDigest.digest();
			return toString(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		}
	}
}
