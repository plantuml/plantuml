package net.sourceforge.plantuml.code.deflate;

import java.io.ByteArrayOutputStream;

/* 
 * Simple DEFLATE decompressor
 * Copyright (c) Project Nayuki
 * 
 * https://www.nayuki.io/page/simple-deflate-decompressor
 * https://github.com/nayuki/Simple-DEFLATE-decompressor
 */

import java.io.Closeable;
import java.io.IOException;

public class OutputStreamProtected implements Closeable {

	private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	private int counter = 0;

	public void close() throws IOException {
		baos.close();
	}

	public byte[] toByteArray() {
		return baos.toByteArray();
	}

	public void write(int b) throws IOException {
		this.counter++;
		baos.write(b);
		if (counter > 128000) {
			throw new IOException("Too big");
		}

	}

}
