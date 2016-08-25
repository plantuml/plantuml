/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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

import net.sourceforge.plantuml.code.AsciiEncoder;

public class SignatureUtils {

	public static String getSignature(String s) {
		try {
			final AsciiEncoder coder = new AsciiEncoder();
			final MessageDigest msgDigest = MessageDigest.getInstance("MD5");
			msgDigest.update(s.getBytes("UTF-8"));
			final byte[] digest = msgDigest.digest();
			return coder.encode(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		}
	}

	public static String getSignatureSha512(File f) throws IOException {
		final InputStream is = new FileInputStream(f);
		try {
			return getSignatureSha512(is);
		} finally {
			is.close();
		}
	}

	public static String getSignatureSha512(InputStream is) throws IOException {
		try {
			final AsciiEncoder coder = new AsciiEncoder();
			final MessageDigest msgDigest = MessageDigest.getInstance("SHA-512");
			int read = 0;
			while ((read = is.read()) != -1) {
				msgDigest.update((byte) read);
			}
			final byte[] digest = msgDigest.digest();
			return coder.encode(digest);
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

	public static String getSignature(File f) throws IOException {
		try {
			final AsciiEncoder coder = new AsciiEncoder();
			final MessageDigest msgDigest = MessageDigest.getInstance("MD5");
			final FileInputStream is = new FileInputStream(f);
			int read = -1;
			while ((read = is.read()) != -1) {
				msgDigest.update((byte) read);
			}
			is.close();
			final byte[] digest = msgDigest.digest();
			return coder.encode(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		}
	}
}
