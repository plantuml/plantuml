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
 * With assistance from ChatGPT (OpenAI)
 *
 */
package net.sourceforge.plantuml.licensing;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Minimal Ed25519 in pure Java.
 */
public final class Ed25519 {

    // p = 2^255 - 19
    private static final BigInteger P = new BigInteger("7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffed", 16);
    // L = subgroup order
    private static final BigInteger L = new BigInteger("1000000000000000000000000000000014def9dea2f79cd65812631a5cf5d3ed", 16);

    // curve constants (twisted Edwards, a = -1)
    private static final BigInteger D;       // d = -121665/121666 mod p
    private static final BigInteger SQRT_M1; // sqrt(-1) mod p

    // basepoint B (x, y) — standard Ed25519 values
    private static final BigInteger BX = new BigInteger(
        "15112221349535400772501151409588531511454012693041857206046113283949847762202");
    private static final BigInteger BY = new BigInteger(
        "46316835694926478169428394003475163141307993866256225615783033603165251855960");

    static {
        // d = -121665 * inv(121666) mod p
        D = BigInteger.valueOf(-121665).mod(P)
                .multiply(BigInteger.valueOf(121666).modInverse(P)).mod(P);
        // sqrt(-1) = 2^((p-1)/4) mod p
        SQRT_M1 = BigInteger.valueOf(2).modPow(P.subtract(BigInteger.ONE).shiftRight(2), P);
    }

    private Ed25519() {}

    // --- key API ---

    /** Generates a pair (private seed 32 bytes, public key 32 bytes). */
    public static KeyPair generateKeyPair(SecureRandom rnd) {
        byte[] seed = new byte[32];
        rnd.nextBytes(seed);
        byte[] pub = publicKeyFromSeed(seed);
        return new KeyPair(seed, pub);
    }

    /** Derives the public key (32 bytes) from a private seed (32 bytes). */
    public static byte[] publicKeyFromSeed(byte[] seed32) {
        if (seed32 == null || seed32.length != 32) throw new IllegalArgumentException("seed must be 32 bytes");
        Digest d = sha512(seed32);
        byte[] aBytes = clamp(Arrays.copyOfRange(d.bytes, 0, 32));
        BigInteger a = leToInt(aBytes);
        Point A = scalarMulBase(a);
        return encodePoint(A);
    }

    // --- signature ---

    /**
     * Signs a message with a private seed (32 bytes). Returns a 64-byte signature (R||S).
     * The public key encoder is implicitly the one derived from the seed.
     */
    public static byte[] sign(byte[] seed32, byte[] message) {
        if (seed32 == null || seed32.length != 32) throw new IllegalArgumentException("seed must be 32 bytes");
        if (message == null) message = new byte[0];

        // H = SHA-512(seed)
        Digest h = sha512(seed32);
        byte[] aBytes = clamp(Arrays.copyOfRange(h.bytes, 0, 32)); // clamped "a" (LE)
        byte[] prefix = Arrays.copyOfRange(h.bytes, 32, 64);

        BigInteger a = leToInt(aBytes);
        byte[] Aenc = encodePoint(scalarMulBase(a));

        // r = SHA-512(prefix || M) mod L
        Digest rDig = sha512(prefix, message);
        BigInteger r = leToInt(rDig.bytes).mod(L);

        // R = r * B
        byte[] Renc = encodePoint(scalarMulBase(r));

        // k = SHA-512(Renc || Aenc || M) mod L
        Digest kDig = sha512(Renc, Aenc, message);
        BigInteger k = leToInt(kDig.bytes).mod(L);

        // S = r + k*a (mod L)
        BigInteger S = r.add(k.multiply(a)).mod(L);

        // sig = Renc (32) || S(le,32)
        byte[] sig = new byte[64];
        System.arraycopy(Renc, 0, sig, 0, 32);
        System.arraycopy(intToLe(S, 32), 0, sig, 32, 32);
        return sig;
    }

    /**
     * Verifies a 64-byte signature on a message using a 32-byte public key.
     */
    public static boolean verify(byte[] publicKey32, byte[] message, byte[] signature64) {
        if (publicKey32 == null || publicKey32.length != 32) return false;
        if (signature64 == null || signature64.length != 64) return false;
        if (message == null) message = new byte[0];

        try {
            // parse signature
            byte[] Renc = Arrays.copyOfRange(signature64, 0, 32);
            byte[] Senc = Arrays.copyOfRange(signature64, 32, 64);
            BigInteger S = leToInt(Senc);
            if (S.signum() < 0 || S.compareTo(L) >= 0) return false;

            Point A = decodePoint(publicKey32);
            Point R = decodePoint(Renc);

            // k = SHA-512(Renc || Aenc || M) mod L
            Digest kDig = sha512(Renc, publicKey32, message);
            BigInteger k = leToInt(kDig.bytes).mod(L);

            // check: [S]B == R + [k]A
            Point left = scalarMulBase(S);
            Point right = add(R, scalarMul(A, k));
            return left.equals(right);
        } catch (Exception ex) {
            return false;
        }
    }

    // --- structures & utilities ---

    public static final class KeyPair {
        private final byte[] seed32;     // private (seed)
        private final byte[] public32;   // public key (encoded)
        public KeyPair(byte[] seed32, byte[] public32) {
            this.seed32 = Arrays.copyOf(seed32, 32);
            this.public32 = Arrays.copyOf(public32, 32);
        }
        public byte[] getPrivateSeed() { return Arrays.copyOf(seed32, 32); }
        public byte[] getPublicKey()  { return Arrays.copyOf(public32, 32); }
    }

    private static final class Digest {
        final byte[] bytes;
        Digest(byte[] b) { this.bytes = b; }
    }

    private static Digest sha512(byte[]... parts) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            for (byte[] p : parts) md.update(p);
            return new Digest(md.digest());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static byte[] clamp(byte[] a) {
        // a: 32 bytes LE
        a[0]  &= (byte) 0xF8;
        a[31] &= (byte) 0x7F;
        a[31] |= (byte) 0x40;
        return a;
    }

    private static byte[] intToLe(BigInteger x, int len) {
        byte[] be = x.toByteArray(); // signed BE
        if (be.length > 1 && be[0] == 0) be = Arrays.copyOfRange(be, 1, be.length);
        byte[] out = new byte[len];
        // copy BE -> end, then reverse to LE
        int copy = Math.min(be.length, len);
        System.arraycopy(be, be.length - copy, out, len - copy, copy);
        for (int i = 0, j = len - 1; i < j; i++, j--) { byte t = out[i]; out[i] = out[j]; out[j] = t; }
        return out;
    }

    private static BigInteger leToInt(byte[] le) {
        byte[] be = Arrays.copyOf(le, le.length);
        for (int i = 0, j = be.length - 1; i < j; i++, j--) { byte t = be[i]; be[i] = be[j]; be[j] = t; }
        return new BigInteger(1, be);
    }

    private static BigInteger inv(BigInteger x) { return x.modInverse(P); }

    // point on the curve (affine coordinates)
    private static final class Point {
        final BigInteger x, y;
        Point(BigInteger x, BigInteger y) { this.x = x.mod(P); this.y = y.mod(P); }
        @Override public boolean equals(Object o) {
            if (!(o instanceof Point)) return false;
            Point p = (Point) o;
            return this.x.equals(p.x) && this.y.equals(p.y);
        }
    }

    private static final Point B = new Point(BX, BY);
    private static final Point ID = new Point(BigInteger.ZERO, BigInteger.ONE); // neutral element

    // Edwards addition (a = -1)
    private static Point add(Point P1, Point P2) {
        BigInteger x1 = P1.x, y1 = P1.y, x2 = P2.x, y2 = P2.y;
        BigInteger x1x2 = x1.multiply(x2).mod(P);
        BigInteger y1y2 = y1.multiply(y2).mod(P);
        BigInteger x1y2 = x1.multiply(y2).mod(P);
        BigInteger y1x2 = y1.multiply(x2).mod(P);
        BigInteger denX = BigInteger.ONE.add(D.multiply(x1x2).mod(P).multiply(y1y2).mod(P)).mod(P);
        BigInteger denY = BigInteger.ONE.subtract(D.multiply(x1x2).mod(P).multiply(y1y2).mod(P)).mod(P);

        BigInteger x3 = x1y2.add(y1x2).mod(P).multiply(inv(denX)).mod(P);
        BigInteger y3 = y1y2.add(x1x2).mod(P).multiply(inv(denY)).mod(P);
        return new Point(x3, y3);
    }

    private static Point dbl(Point P) { return add(P, P); }

    private static Point scalarMul(Point P, BigInteger k) {
        Point Q = ID;
        for (int i = k.bitLength() - 1; i >= 0; i--) {
            Q = dbl(Q);
            if (k.testBit(i)) Q = add(Q, P);
        }
        return Q;
    }

    private static Point scalarMulBase(BigInteger k) { return scalarMul(B, k); }

    // encoding (y in LE 255 bits + sign bit of x in MSB)
    private static byte[] encodePoint(Point P) {
        BigInteger x = P.x, y = P.y;
        BigInteger yMasked = y; // 255 bits
        byte[] enc = intToLe(yMasked, 32);
        // set MSB (bit 255) with the parity bit of x
        if (x.testBit(0)) enc[31] = (byte) (enc[31] | 0x80);
        else enc[31] = (byte) (enc[31] & 0x7F);
        return enc;
    }

    // decoding from 32 bytes LE (y || sign(x))
    private static Point decodePoint(byte[] enc) {
        if (enc == null || enc.length != 32) throw new IllegalArgumentException("point encoding must be 32 bytes");
        byte[] yLe = Arrays.copyOf(enc, 32);
        int signX = (yLe[31] & 0x80) >>> 7;
        yLe[31] &= 0x7F; // mask of sign bit
        BigInteger y = leToInt(yLe);

        // x^2 = (y^2 - 1) / (d*y^2 + 1)
        BigInteger y2 = y.multiply(y).mod(P);
        BigInteger u = y2.subtract(BigInteger.ONE).mod(P);
        BigInteger v = D.multiply(y2).add(BigInteger.ONE).mod(P);
        BigInteger x2 = u.multiply(inv(v)).mod(P);

        // square root mod p (p ≡ 5 (mod 8))
        BigInteger x = x2.modPow(P.add(BigInteger.valueOf(3)).shiftRight(3), P);
        if (!x.multiply(x).mod(P).equals(x2)) {
            x = x.multiply(SQRT_M1).mod(P);
        }
        if (!x.multiply(x).mod(P).equals(x2)) {
            throw new IllegalArgumentException("invalid point encoding");
        }
        if (x.testBit(0) != (signX == 1)) {
            x = P.subtract(x);
        }
        return new Point(x, y);
    }
}
