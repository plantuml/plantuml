/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.FileNotFoundException;
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
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.teavm.TeaVM;

// ::comment when JAVA8
import org.teavm.jso.JSBody;
// ::done

public class SignatureUtils {

	public static String toHexString(byte data[]) {
		final StringBuilder sb = new StringBuilder(data.length * 2);
		for (byte b : data)
			sb.append(String.format("%02x", b));

		return sb.toString();
	}

	public static String getMD5Hex(String s) {
		if (TeaVM.isTeaVM()) {
			// ::revert when JAVA8
			return md5Native(s);
			// return null;
			// ::done
		} else
			try {
				final byte[] digest = getMD5raw(s);
				assert digest.length == 16;
				return toHexString(digest);
			} catch (NoSuchAlgorithmException e) {
				Logme.error(e);
				throw new UnsupportedOperationException(e);
			} catch (UnsupportedEncodingException e) {
				Logme.error(e);
				throw new UnsupportedOperationException(e);
			}
	}

	public static synchronized byte[] getMD5raw(String s)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		final MessageDigest msgDigest = MessageDigest.getInstance("MD5");
		msgDigest.update(s.getBytes(UTF_8));
		return msgDigest.digest();
	}

	public static synchronized byte[] salting(String pass, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
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
			Logme.error(e);
			throw new UnsupportedOperationException(e);
		} catch (UnsupportedEncodingException e) {
			Logme.error(e);
			throw new UnsupportedOperationException(e);
		}
	}

	public static String toString(byte data[]) {
		final AsciiEncoder coder = new AsciiEncoder();
		return coder.encode(data);
	}

	public static String getSHA512Hex(String s) {
		try {
			final byte[] digest = getSHA512raw(s);
			assert digest.length == 64;
			return toHexString(digest);
		} catch (NoSuchAlgorithmException e) {
			Logme.error(e);
			throw new UnsupportedOperationException(e);
		} catch (UnsupportedEncodingException e) {
			Logme.error(e);
			throw new UnsupportedOperationException(e);
		}
	}

	public static byte[] getSHA512raw(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return getSHA512raw(s.getBytes(UTF_8));
	}

	public static synchronized byte[] getSHA512raw(byte data[])
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		final MessageDigest msgDigest = MessageDigest.getInstance("SHA-512");
		msgDigest.update(data);
		return msgDigest.digest();
	}

	public static String getSignatureSha512(SFile f) throws IOException {
		try (InputStream is = f.openFile()) {
			return getSignatureSha512(is);
		}
	}

	public static synchronized String getSignatureSha512(InputStream is) throws IOException {
		try {
			final MessageDigest msgDigest = MessageDigest.getInstance("SHA-512");
			int read = 0;
			while ((read = is.read()) != -1)
				msgDigest.update((byte) read);

			final byte[] digest = msgDigest.digest();
			return toString(digest);
		} catch (NoSuchAlgorithmException e) {
			Logme.error(e);
			throw new UnsupportedOperationException(e);
		} catch (UnsupportedEncodingException e) {
			Logme.error(e);
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

	public static synchronized String getSignature(SFile f) throws IOException {
		try (final InputStream is = f.openFile()) {
			final MessageDigest msgDigest = MessageDigest.getInstance("MD5");
			if (is == null)
				throw new FileNotFoundException();

			int read = -1;
			while ((read = is.read()) != -1)
				msgDigest.update((byte) read);

			final byte[] digest = msgDigest.digest();
			return toString(digest);
		} catch (NoSuchAlgorithmException e) {
			Logme.error(e);
			throw new UnsupportedOperationException(e);
		} catch (UnsupportedEncodingException e) {
			Logme.error(e);
			throw new UnsupportedOperationException(e);
		}
	}

	// ::comment when JAVA8
	/**
	 * Native JavaScript MD5 implementation. Based on the well-known MD5 algorithm
	 * adapted for browser environments.
	 */
	@JSBody(params = { "string" }, script = "function md5(string) {" + "  function md5cycle(x, k) {"
			+ "    var a = x[0], b = x[1], c = x[2], d = x[3];" + "    a = ff(a, b, c, d, k[0], 7, -680876936);"
			+ "    d = ff(d, a, b, c, k[1], 12, -389564586);" + "    c = ff(c, d, a, b, k[2], 17, 606105819);"
			+ "    b = ff(b, c, d, a, k[3], 22, -1044525330);" + "    a = ff(a, b, c, d, k[4], 7, -176418897);"
			+ "    d = ff(d, a, b, c, k[5], 12, 1200080426);" + "    c = ff(c, d, a, b, k[6], 17, -1473231341);"
			+ "    b = ff(b, c, d, a, k[7], 22, -45705983);" + "    a = ff(a, b, c, d, k[8], 7, 1770035416);"
			+ "    d = ff(d, a, b, c, k[9], 12, -1958414417);" + "    c = ff(c, d, a, b, k[10], 17, -42063);"
			+ "    b = ff(b, c, d, a, k[11], 22, -1990404162);" + "    a = ff(a, b, c, d, k[12], 7, 1804603682);"
			+ "    d = ff(d, a, b, c, k[13], 12, -40341101);" + "    c = ff(c, d, a, b, k[14], 17, -1502002290);"
			+ "    b = ff(b, c, d, a, k[15], 22, 1236535329);" + "    a = gg(a, b, c, d, k[1], 5, -165796510);"
			+ "    d = gg(d, a, b, c, k[6], 9, -1069501632);" + "    c = gg(c, d, a, b, k[11], 14, 643717713);"
			+ "    b = gg(b, c, d, a, k[0], 20, -373897302);" + "    a = gg(a, b, c, d, k[5], 5, -701558691);"
			+ "    d = gg(d, a, b, c, k[10], 9, 38016083);" + "    c = gg(c, d, a, b, k[15], 14, -660478335);"
			+ "    b = gg(b, c, d, a, k[4], 20, -405537848);" + "    a = gg(a, b, c, d, k[9], 5, 568446438);"
			+ "    d = gg(d, a, b, c, k[14], 9, -1019803690);" + "    c = gg(c, d, a, b, k[3], 14, -187363961);"
			+ "    b = gg(b, c, d, a, k[8], 20, 1163531501);" + "    a = gg(a, b, c, d, k[13], 5, -1444681467);"
			+ "    d = gg(d, a, b, c, k[2], 9, -51403784);" + "    c = gg(c, d, a, b, k[7], 14, 1735328473);"
			+ "    b = gg(b, c, d, a, k[12], 20, -1926607734);" + "    a = hh(a, b, c, d, k[5], 4, -378558);"
			+ "    d = hh(d, a, b, c, k[8], 11, -2022574463);" + "    c = hh(c, d, a, b, k[11], 16, 1839030562);"
			+ "    b = hh(b, c, d, a, k[14], 23, -35309556);" + "    a = hh(a, b, c, d, k[1], 4, -1530992060);"
			+ "    d = hh(d, a, b, c, k[4], 11, 1272893353);" + "    c = hh(c, d, a, b, k[7], 16, -155497632);"
			+ "    b = hh(b, c, d, a, k[10], 23, -1094730640);" + "    a = hh(a, b, c, d, k[13], 4, 681279174);"
			+ "    d = hh(d, a, b, c, k[0], 11, -358537222);" + "    c = hh(c, d, a, b, k[3], 16, -722521979);"
			+ "    b = hh(b, c, d, a, k[6], 23, 76029189);" + "    a = hh(a, b, c, d, k[9], 4, -640364487);"
			+ "    d = hh(d, a, b, c, k[12], 11, -421815835);" + "    c = hh(c, d, a, b, k[15], 16, 530742520);"
			+ "    b = hh(b, c, d, a, k[2], 23, -995338651);" + "    a = ii(a, b, c, d, k[0], 6, -198630844);"
			+ "    d = ii(d, a, b, c, k[7], 10, 1126891415);" + "    c = ii(c, d, a, b, k[14], 15, -1416354905);"
			+ "    b = ii(b, c, d, a, k[5], 21, -57434055);" + "    a = ii(a, b, c, d, k[12], 6, 1700485571);"
			+ "    d = ii(d, a, b, c, k[3], 10, -1894986606);" + "    c = ii(c, d, a, b, k[10], 15, -1051523);"
			+ "    b = ii(b, c, d, a, k[1], 21, -2054922799);" + "    a = ii(a, b, c, d, k[8], 6, 1873313359);"
			+ "    d = ii(d, a, b, c, k[15], 10, -30611744);" + "    c = ii(c, d, a, b, k[6], 15, -1560198380);"
			+ "    b = ii(b, c, d, a, k[13], 21, 1309151649);" + "    a = ii(a, b, c, d, k[4], 6, -145523070);"
			+ "    d = ii(d, a, b, c, k[11], 10, -1120210379);" + "    c = ii(c, d, a, b, k[2], 15, 718787259);"
			+ "    b = ii(b, c, d, a, k[9], 21, -343485551);" + "    x[0] = add32(a, x[0]);"
			+ "    x[1] = add32(b, x[1]);" + "    x[2] = add32(c, x[2]);" + "    x[3] = add32(d, x[3]);" + "  }"
			+ "  function cmn(q, a, b, x, s, t) {" + "    a = add32(add32(a, q), add32(x, t));"
			+ "    return add32((a << s) | (a >>> (32 - s)), b);" + "  }" + "  function ff(a, b, c, d, x, s, t) {"
			+ "    return cmn((b & c) | ((~b) & d), a, b, x, s, t);" + "  }" + "  function gg(a, b, c, d, x, s, t) {"
			+ "    return cmn((b & d) | (c & (~d)), a, b, x, s, t);" + "  }" + "  function hh(a, b, c, d, x, s, t) {"
			+ "    return cmn(b ^ c ^ d, a, b, x, s, t);" + "  }" + "  function ii(a, b, c, d, x, s, t) {"
			+ "    return cmn(c ^ (b | (~d)), a, b, x, s, t);" + "  }" + "  function md51(s) {"
			+ "    var n = s.length," + "    state = [1732584193, -271733879, -1732584194, 271733878], i;"
			+ "    for (i = 64; i <= s.length; i += 64) {" + "      md5cycle(state, md5blk(s.substring(i - 64, i)));"
			+ "    }" + "    s = s.substring(i - 64);"
			+ "    var tail = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];" + "    for (i = 0; i < s.length; i++)"
			+ "      tail[i >> 2] |= s.charCodeAt(i) << ((i % 4) << 3);" + "    tail[i >> 2] |= 0x80 << ((i % 4) << 3);"
			+ "    if (i > 55) {" + "      md5cycle(state, tail);" + "      for (i = 0; i < 16; i++) tail[i] = 0;"
			+ "    }" + "    tail[14] = n * 8;" + "    md5cycle(state, tail);" + "    return state;" + "  }"
			+ "  function md5blk(s) {" + "    var md5blks = [], i;" + "    for (i = 0; i < 64; i += 4) {"
			+ "      md5blks[i >> 2] = s.charCodeAt(i) + (s.charCodeAt(i + 1) << 8) +"
			+ "        (s.charCodeAt(i + 2) << 16) + (s.charCodeAt(i + 3) << 24);" + "    }" + "    return md5blks;"
			+ "  }" + "  var hex_chr = '0123456789abcdef'.split('');" + "  function rhex(n) {"
			+ "    var s = '', j = 0;" + "    for (; j < 4; j++)"
			+ "      s += hex_chr[(n >> (j * 8 + 4)) & 0x0F] + hex_chr[(n >> (j * 8)) & 0x0F];" + "    return s;"
			+ "  }" + "  function hex(x) {" + "    for (var i = 0; i < x.length; i++)" + "      x[i] = rhex(x[i]);"
			+ "    return x.join('');" + "  }" + "  function add32(a, b) {" + "    return (a + b) & 0xFFFFFFFF;" + "  }"
			+ "  return hex(md51(unescape(encodeURIComponent(string))));" + "}" + "return md5(string);")
	private static native String md5Native(String string);
	// ::done

}
