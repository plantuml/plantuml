package net.sourceforge.plantuml.picoweb;

import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

public class ReceivedHTTPRequest {

	private static final String CONTENT_LENGTH_HEADER = "content-length: ";

	private String method;

	private String path;

	private byte[] body;

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public byte[] getBody() {
		return body;
	}

	public static ReceivedHTTPRequest fromStream(InputStream in) throws IOException {
		final ReceivedHTTPRequest request = new ReceivedHTTPRequest();

		final String requestLine = readLine(in);

		final StringTokenizer tokenizer = new StringTokenizer(requestLine);
		if (tokenizer.countTokens() != 3) {
			throw new BadRequest400("Bad request line");
		}

		request.method = tokenizer.nextToken().toUpperCase();
		request.path = tokenizer.nextToken();

		// Headers
		int contentLength = 0;

		while (true) {
			String line = readLine(in);
			if (line.isEmpty()) {
				break;
			} else if (line.toLowerCase().startsWith(CONTENT_LENGTH_HEADER)) {
				contentLength = parseContentLengthHeader(line);
			}
		}

		request.body = readBody(in, contentLength);
		return request;
	}

	private static int parseContentLengthHeader(String line) throws IOException {
		int contentLength;

		try {
			contentLength = Integer.parseInt(line.substring(CONTENT_LENGTH_HEADER.length()).trim());
		} catch (NumberFormatException e) {
			throw new BadRequest400("Invalid content length");
		}

		if (contentLength < 0) {
			throw new BadRequest400("Negative content length");
		}

		return contentLength;
	}

	private static byte[] readBody(InputStream in, int contentLength) throws IOException {
		if (contentLength == 0) {
			return new byte[0];
		}

		final byte[] body = new byte[contentLength];
		int n = 0;
		int offset = 0;

		// java.io.InputStream.readNBytes() can replace this from Java 9
		while (n < contentLength) {
			int count = in.read(body, offset + n, contentLength - n);
			if (count < 0) {
				throw new BadRequest400("Body too short");
			}
			n += count;
		}
		return body;
	}

	private static String readLine(InputStream in) throws IOException {
		final StringBuilder builder = new StringBuilder();

		while (true) {
			int c = in.read();
			if (c == -1 || c == '\n') {
				break;
			}
			builder.append((char) c);
		}

		if (builder.length() > 0 && builder.charAt(builder.length() - 1) == '\r') {
			builder.deleteCharAt(builder.length() - 1);
		}

		return builder.toString();
	}
}
