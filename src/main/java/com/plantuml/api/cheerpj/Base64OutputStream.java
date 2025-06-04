package com.plantuml.api.cheerpj;

import java.io.IOException;
import java.io.OutputStream;

public class Base64OutputStream extends OutputStream {

	private final StringBuilder sb = new StringBuilder(2048);

	private int nb;
	private byte a;
	private byte b;
	private byte c;

	@Override
	public void write(int data) throws IOException {
		switch (nb) {
		case 0:
			a = (byte) (data & 0xFF);
			nb = 1;
			break;
		case 1:
			b = (byte) (data & 0xFF);
			nb = 2;
			break;
		case 2:
			c = (byte) (data & 0xFF);
			nb = 0;
			append3bytes(sb, a, b, c);
			break;
		}
	}

	@Override
	public void close() throws IOException {
		switch (nb) {
		case 0:
			break;
		case 1:
			append3bytes(sb, a, (byte) 0, (byte) 0);
			break;
		case 2:
			append3bytes(sb, a, b, (byte) 0);
			break;
		}
		super.close();
	}

	@Override
	public String toString() {
		return sb.toString();
	}

	private void append3bytes(StringBuilder sb, byte a, byte b, byte c) {
		final int i0 = a & 0xff;
		final int i1 = b & 0xff;
		final int i2 = c & 0xff;
		final int o0 = i0 >>> 2;
		final int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
		final int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
		final int o3 = i2 & 0x3F;
		sb.append(getBase(o0));
		sb.append(getBase(o1));
		sb.append(getBase(o2));
		sb.append(getBase(o3));
	}

	private String getBase(int i) {
		switch (i) {
		case 0:
			return "A";
		case 1:
			return "B";
		case 2:
			return "C";
		case 3:
			return "D";
		case 4:
			return "E";
		case 5:
			return "F";
		case 6:
			return "G";
		case 7:
			return "H";
		case 8:
			return "I";
		case 9:
			return "J";
		case 10:
			return "K";
		case 11:
			return "L";
		case 12:
			return "M";
		case 13:
			return "N";
		case 14:
			return "O";
		case 15:
			return "P";
		case 16:
			return "Q";
		case 17:
			return "R";
		case 18:
			return "S";
		case 19:
			return "T";
		case 20:
			return "U";
		case 21:
			return "V";
		case 22:
			return "W";
		case 23:
			return "X";
		case 24:
			return "Y";
		case 25:
			return "Z";
		case 26:
			return "a";
		case 27:
			return "b";
		case 28:
			return "c";
		case 29:
			return "d";
		case 30:
			return "e";
		case 31:
			return "f";
		case 32:
			return "g";
		case 33:
			return "h";
		case 34:
			return "i";
		case 35:
			return "j";
		case 36:
			return "k";
		case 37:
			return "l";
		case 38:
			return "m";
		case 39:
			return "n";
		case 40:
			return "o";
		case 41:
			return "p";
		case 42:
			return "q";
		case 43:
			return "r";
		case 44:
			return "s";
		case 45:
			return "t";
		case 46:
			return "u";
		case 47:
			return "v";
		case 48:
			return "w";
		case 49:
			return "x";
		case 50:
			return "y";
		case 51:
			return "z";
		case 52:
			return "0";
		case 53:
			return "1";
		case 54:
			return "2";
		case 55:
			return "3";
		case 56:
			return "4";
		case 57:
			return "5";
		case 58:
			return "6";
		case 59:
			return "7";
		case 60:
			return "8";
		case 61:
			return "9";
		case 62:
			return "+";
		case 63:
			return "/";

		}
		return "*";
	}

}
