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
 * Highly inspired from https://www.ssaurel.com/blog/create-a-simple-http-web-server-in-java
 * 
 */
package net.sourceforge.plantuml.picoweb;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.code.NoPlantumlCompressionException;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderUtil;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.graphic.QuoteUtils;
import net.sourceforge.plantuml.version.Version;

public class PicoWebServer implements Runnable {

	private final Socket connect;

	public PicoWebServer(Socket c) {
		this.connect = c;
	}

	public static void main(String[] args) throws IOException {
		startServer(8080, null);
	}

	public static void startServer(final int port, final String bindAddress) throws IOException {
		final InetAddress bindAddress1 = bindAddress == null ? null : InetAddress.getByName(bindAddress);
		final ServerSocket serverConnect = new ServerSocket(port, 50, bindAddress1);
		System.err.println("webPort=" + serverConnect.getLocalPort());
		while (true) {
			final PicoWebServer myServer = new PicoWebServer(serverConnect.accept());
			final Thread thread = new Thread(myServer);
			thread.start();
		}
	}

	public void run() {
		BufferedReader in = null;
		BufferedOutputStream out = null;

		try {
			in = new BufferedReader(new InputStreamReader(connect.getInputStream(), "UTF-8"));
			out = new BufferedOutputStream(connect.getOutputStream());

			final String first = in.readLine();
			if (first == null) {
				return;
			}

			final StringTokenizer parse = new StringTokenizer(first);
			final String method = parse.nextToken().toUpperCase();

			if (method.equals("GET")) {
				final String path = parse.nextToken();
				if (path.startsWith("/png/") && sendDiagram(out, path, "image/png", FileFormat.PNG))
					return;
				if (path.startsWith("/plantuml/png/") && sendDiagram(out, path, "image/png", FileFormat.PNG))
					return;
				if (path.startsWith("/svg/") && sendDiagram(out, path, "image/svg+xml", FileFormat.SVG))
					return;
				if (path.startsWith("/plantuml/svg/") && sendDiagram(out, path, "image/svg+xml", FileFormat.SVG))
					return;
			}
			write(out, "HTTP/1.1 302 Found");
			write(out, "Location: /plantuml/png/oqbDJyrBuGh8ISmh2VNrKGZ8JCuFJqqAJYqgIotY0aefG5G00000");
			write(out, "");
			out.flush();

		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
				connect.close();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	private boolean sendDiagram(BufferedOutputStream out, String path, final String mime, final FileFormat format)
			throws NoPlantumlCompressionException, IOException {
		final int x = path.lastIndexOf('/');
		final String compressed = path.substring(x + 1);
		final Transcoder transcoder = TranscoderUtil.getDefaultTranscoderProtected();
		final String source = transcoder.decode(compressed);
		final SourceStringReader ssr = new SourceStringReader(source);

		final List<BlockUml> blocks = ssr.getBlocks();
		if (blocks.size() > 0) {
			final Diagram system = blocks.get(0).getDiagram();
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			final ImageData imageData = system.exportDiagram(os, 0, new FileFormatOption(format));
			os.close();
			final byte[] fileData = os.toByteArray();
			write(out, "HTTP/1.1 " + httpReturnCode(imageData.getStatus()));
			write(out, "Cache-Control: no-cache");
			write(out, "Server: PlantUML PicoWebServer " + Version.versionString());
			write(out, "Date: " + new Date());
			write(out, "Content-type: " + mime);
			write(out, "Content-length: " + fileData.length);
			write(out, "X-PlantUML-Diagram-Width: " + imageData.getWidth());
			write(out, "X-PlantUML-Diagram-Height: " + imageData.getHeight());
			write(out, "X-PlantUML-Diagram-Description: " + system.getDescription().getDescription());
			if (system instanceof PSystemError) {
				final PSystemError error = (PSystemError) system;
				for (ErrorUml err : error.getErrorsUml()) {
					write(out, "X-PlantUML-Diagram-Error: " + err.getError());
					write(out, "X-PlantUML-Diagram-Error-Line: " + (1 + err.getLineLocation().getPosition()));
				}
			}
			write(out, "X-Patreon: Support us on https://plantuml.com/patreon");
			write(out, "X-Donate: https://plantuml.com/paypal");
			write(out, "X-Quote: " + StringUtils.rot(QuoteUtils.getSomeQuote()));
			write(out, "");
			out.flush();
			out.write(fileData);
			out.flush();
			return true;
		}
		return false;
	}

	private String httpReturnCode(int status) {
		if (status == 0 || status == 200) {
			return "200 OK";
		}
		return "" + status + " ERROR";
	}

	private void write(OutputStream os, String s) throws IOException {
		s = s + "\r\n";
		os.write(s.getBytes("UTF-8"));
	}

}