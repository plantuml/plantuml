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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Random;

import net.sourceforge.plantuml.OptionPrint;
import net.sourceforge.plantuml.SignatureUtils;
import net.sourceforge.plantuml.dedication.TurningBytes;

public class Magic {

	private final byte buffer[] = new byte[512];

	@Override
	public String toString() {
		return SignatureUtils.toString(buffer);
	}

	public String toHexString() {
		return SignatureUtils.toHexString(buffer);
	}

	private void xor(TurningBytes turningBytes) {
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] ^= turningBytes.nextByte();
		}
	}

	public void xor(byte[] key) {
		xor(new TurningBytes(key));
	}

	public static Magic fromHexString(String s) {
		if (s.length() != 1024) {
			throw new IllegalArgumentException();
		}
		final Magic result = new Magic();
		for (int i = 0; i < 512; i++) {
			result.buffer[i] = (byte) (Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16) & 0xFF);
		}
		return result;
	}

	public final byte[] getBuffer() {
		return buffer;
	}

	public void setByte(byte[] shrink, int pos, int data) {
		buffer[pos] = (byte) (0xFF & (data ^ shrink(shrink)));
	}

	public int getByte(byte[] shrink, int pos) {
		return buffer[pos] ^ shrink(shrink);
	}

	public void set(int pos, byte[] data) {
		for (int i = 0; i < data.length; i++) {
			buffer[pos + i] = data[i];
		}
	}

	public void setString(int pos, String s) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		final byte[] tmp = s.getBytes("UTF-8");
		buffer[pos] = (byte) tmp.length;
		set(pos + 1, tmp);
		// set(pos + 1 + tmp.length, SignatureUtils.getSHA512raw(s));
	}

	public String getString(int pos) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		final int len = buffer[pos];
		if (len < 0 || len > 127) {
			throw new IllegalArgumentException();
		}
		final String result = new String(get(pos + 1, len), "UTF-8");
		// if (isEquals(SignatureUtils.getSHA512raw(result), get(pos + 1 + len, 64)) == false) {
		// throw new UnsupportedEncodingException();
		// }
		return result;
	}

	public byte[] get(int pos, int len) {
		final byte result[] = new byte[len];
		System.arraycopy(buffer, pos, result, 0, len);
		return result;
	}

	private boolean isEquals(byte data1[], byte[] data2) {
		if (data1.length != data2.length) {
			return false;
		}
		for (int i = 0; i < data1.length; i++) {
			if (data1[i] != data2[i]) {
				return false;
			}
		}
		return true;
	}

	public static byte[] signature() throws IOException {
		final String signature = OptionPrint.getHostName() + getMacAddress();
		try {
			return SignatureUtils.getSHA512raw(SignatureUtils.salting(signature, getSalt(signature)));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException();
		}
	}

	public static byte shrink(byte data[]) {
		byte result = 42;
		for (byte b : data) {
			result ^= b;
		}
		return result;
	}

	public static byte[] getSalt(final String signature) throws UnsupportedEncodingException {
		final Random rnd = new Random(getSeed(signature.getBytes("UTF-8")));
		final byte salt[] = new byte[512];
		rnd.nextBytes(salt);
		return salt;
	}

	private static long getSeed(byte[] bytes) {
		long result = 19;
		for (byte b : bytes) {
			result = result * 41 + b;
		}
		return result;
	}

	private static String getMacAddress() throws IOException {

		final Enumeration<NetworkInterface> net = NetworkInterface.getNetworkInterfaces();
		final StringBuilder result = new StringBuilder();
		while (net.hasMoreElements()) {
			final NetworkInterface element = net.nextElement();
			byte[] mac = element.getHardwareAddress();
			if (mac != null) {
				for (byte b : mac) {
					result.append(String.format("%02x", b));
				}
			}
		}
		return result.toString();
	}

	public static void main(String[] args) throws IOException {
		System.err.println(SignatureUtils.toHexString(signature()));
		System.out.println("Mac: " + getMacAddress());

	}

}
