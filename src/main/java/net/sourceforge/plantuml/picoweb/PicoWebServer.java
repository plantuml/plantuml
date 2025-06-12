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
 * Highly inspired from https://www.ssaurel.com/blog/create-a-simple-http-web-server-in-java
 * 
 */
package net.sourceforge.plantuml.picoweb;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.ErrorUmlType.SYNTAX_ERROR;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Option;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderUtil;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.eggs.QuoteUtils;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.error.PSystemErrorUtils;
import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.syntax.LanguageDescriptor;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.LineLocationImpl;
import net.sourceforge.plantuml.version.Version;

public class PicoWebServer implements Runnable {
	// ::remove folder when __CORE__

	private final Socket connect;
	private static boolean enableStop;

	public PicoWebServer(Socket c) {
		this.connect = c;
	}

	public static void main(String[] args) throws IOException {
		startServer(8080, null, false);
	}

	public static void startServer(final int port, final String bindAddress, final boolean argEnableStop)
			throws IOException {
		PicoWebServer.enableStop = argEnableStop;
		final InetAddress bindAddress1 = bindAddress == null ? null : InetAddress.getByName(bindAddress);
		final ServerSocket serverConnect = new ServerSocket(port, 50, bindAddress1);
		System.err.println("webPort=" + serverConnect.getLocalPort());
		serverLoop(serverConnect);
	}

	public static void serverLoop(final ServerSocket serverConnect) throws IOException {
		while (true) {
			final PicoWebServer myServer = new PicoWebServer(serverConnect.accept());
			final Thread thread = new Thread(myServer);
			thread.start();
		}
	}

	public void run() {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;

		try {
			in = new BufferedInputStream(connect.getInputStream());
			out = new BufferedOutputStream(connect.getOutputStream());

			final ReceivedHTTPRequest request = ReceivedHTTPRequest.fromStream(in);
			if (request.getMethod().equals("GET")) {
				if (request.getPath().startsWith("/png/") && handleGET(request, out, FileFormat.PNG))
					return;
				if (request.getPath().startsWith("/plantuml/png/") && handleGET(request, out, FileFormat.PNG))
					return;
				if (request.getPath().startsWith("/svg/") && handleGET(request, out, FileFormat.SVG))
					return;
				if (request.getPath().startsWith("/plantuml/svg/") && handleGET(request, out, FileFormat.SVG))
					return;
				if (request.getPath().startsWith("/txt/") && handleGET(request, out, FileFormat.ATXT))
					return;
				if (request.getPath().startsWith("/plantuml/txt/") && handleGET(request, out, FileFormat.ATXT))
					return;
				if (request.getPath().startsWith("/utxt/") && handleGET(request, out, FileFormat.UTXT))
					return;
				if (request.getPath().startsWith("/plantuml/utxt/") && handleGET(request, out, FileFormat.UTXT))
					return;
				if (request.getPath().startsWith("/serverinfo") && handleInfo(out))
					return;
				if (request.getPath().startsWith("/plantuml/serverinfo") && handleInfo(out))
					return;
				if (request.getPath().startsWith("/language") && handleLanguage(out))
					return;
				if (enableStop && (request.getPath().startsWith("/stopserver")
						|| request.getPath().startsWith("/plantuml/stopserver")) && handleStop(out))
					return;

			} else if (request.getMethod().equals("POST") && request.getPath().equals("/render")) {
				handleRenderRequest(request, out);
				return;
			}
			write(out, "HTTP/1.1 302 Found");
			write(out, "Location: /plantuml/png/oqbDJyrBuGh8ISmh2VNrKGZ8JCuFJqqAJYqgIotY0aefG5G00000");
			write(out, "");
			out.flush();

		} catch (Throwable e) {
			try {
				sendError(e, out);
			} catch (Throwable e1) {
				Logme.error(e);
			}
		} finally {
			try {
				in.close();
				out.close();
				connect.close();
			} catch (Throwable e) {
				Logme.error(e);
			}
		}
	}

	private boolean handleStop(BufferedOutputStream out) throws IOException {
		write(out, "HTTP/1.1 " + "200");
		write(out, "Cache-Control: no-cache");
		write(out, "Server: PlantUML PicoWebServer " + Version.versionString());
		write(out, "Date: " + new Date());
		write(out, "");

		write(out, "<html>Stoping...</html>");

		out.flush();

		final Thread stop = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000L);
				} catch (InterruptedException e) {
				}
				System.exit(0);
			}
		});
		stop.start();

		return true;
	}

	private boolean handleInfo(BufferedOutputStream out) throws IOException {
		write(out, "HTTP/1.1 " + "200");
		write(out, "Cache-Control: no-cache");
		write(out, "Server: PlantUML PicoWebServer " + Version.versionString());
		write(out, "Date: " + new Date());
		write(out, "Content-Type: application/json");
		write(out, "");

		final JsonArray formats = new JsonArray();
		formats.add("png");
		formats.add("svg");
		formats.add("txt");
		final JsonObject json = Json.object() //
				.add("version", Version.versionString()) //
				.add("PicoWebServer", true) //
				.add("formats", formats); //
		write(out, json.toString());

		out.flush();

		return true;
	}

	private boolean handleLanguage(BufferedOutputStream out) throws IOException {
		write(out, "HTTP/1.1 " + "200");
		write(out, "Cache-Control: no-cache");
		write(out, "Server: PlantUML PicoWebServer " + Version.versionString());
		write(out, "Date: " + new Date());
		write(out, "Content-Type: text/text");
		write(out, "");

		final PrintStream ps = new PrintStream(out);
		new LanguageDescriptor().print(ps);

		out.flush();

		return true;
	}

	private boolean handleGET(ReceivedHTTPRequest request, BufferedOutputStream out, final FileFormat format)
			throws IOException {
		final int x = request.getPath().lastIndexOf('/');
		final String compressed = request.getPath().substring(x + 1);
		final Transcoder transcoder = TranscoderUtil.getDefaultTranscoderProtected();
		final String source = transcoder.decode(compressed);
		final SourceStringReader ssr = new SourceStringReader(source);

		final FileFormatOption fileFormatOption = new FileFormatOption(format);
		final List<BlockUml> blocks = ssr.getBlocks();
		if (blocks.size() > 0) {
			final Diagram system = blocks.get(0).getDiagram();
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			final ImageData imageData = system.exportDiagram(os, 0, fileFormatOption);
			os.close();
			sendDiagram(out, system, fileFormatOption, httpReturnCode(imageData.getStatus()), imageData,
					os.toByteArray());
			return true;
		}
		return false;
	}

	private void handleRenderRequest(ReceivedHTTPRequest request, BufferedOutputStream out) throws Exception {
		if (request.getBody().length == 0) {
			throw new BadRequest400("No request body");
		}

		final RenderRequest renderRequest;
		try {
			renderRequest = RenderRequest.fromJson(new String(request.getBody(), UTF_8));
		} catch (Exception e) {
			throw new BadRequest400("Error parsing request json: " + e.getMessage(), e);
		}

		handleRenderRequest(renderRequest, out);
	}

	public void handleRenderRequest(RenderRequest renderRequest, BufferedOutputStream out) throws Exception {

		final Option option = new Option(renderRequest.getOptions());

		final String source = renderRequest.getSource().startsWith("@start") ? renderRequest.getSource()
				: "@startuml\n" + renderRequest.getSource() + "\n@enduml";

		final SFile newCurrentDir = option.getFileDir() == null ? null : new SFile(option.getFileDir());
		final SourceStringReader ssr = new SourceStringReader(option.getDefaultDefines(), source, UTF_8,
				option.getConfig(), newCurrentDir);
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final Diagram system;
		final ImageData imageData;

		if (ssr.getBlocks().size() == 0) {
			system = PSystemErrorUtils.buildV2(null,
					new ErrorUml(SYNTAX_ERROR, "No valid @start/@end found, please check the version", 0,
							new LineLocationImpl("", null), null),
					null, Collections.<StringLocated>emptyList(), new PreprocessingArtifact());
			imageData = ssr.noValidStartFound(os, option.getFileFormatOption());
		} else {
			system = ssr.getBlocks().get(0).getDiagram();
			imageData = system.exportDiagram(os, 0, option.getFileFormatOption());
		}

		sendDiagram(out, system, option.getFileFormatOption(), "200", imageData, os.toByteArray());
	}

	private void sendDiagram(final BufferedOutputStream out, final Diagram system,
			final FileFormatOption fileFormatOption, final String returnCode, final ImageData imageData,
			final byte[] fileData) throws IOException {

		write(out, "HTTP/1.1 " + returnCode);
		write(out, "Cache-Control: no-cache");
		write(out, "Server: PlantUML PicoWebServer " + Version.versionString());
		write(out, "Date: " + new Date());
		write(out, "Access-Control-Allow-Origin: *");
		write(out, "Content-type: " + fileFormatOption.getFileFormat().getMimeType());
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
		if (system.getTitleDisplay() != null && system.getTitleDisplay().size() == 1) {
			final String encode = URLEncoder.encode(system.getTitleDisplay().asList().get(0).toString(), "UTF-8");
			if (encode.length() < 256)
				write(out, "X-PlantUML-Diagram-Title: " + encode);
		}

		write(out, "X-Patreon: Support us on https://plantuml.com/patreon");
		write(out, "X-Donate: https://plantuml.com/paypal");
		write(out, "X-Quote: " + StringUtils.rot(QuoteUtils.getSomeQuote()));
		write(out, "");
		out.flush();
		out.write(fileData);
		out.flush();
	}

	private void sendError(Throwable e, BufferedOutputStream out) throws Exception {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintWriter printWriter = new PrintWriter(baos);

		if (e instanceof BadRequest400 && e.getCause() == null) {
			printWriter.write(e.getMessage());
		} else {
			e.printStackTrace(printWriter);
		}
		printWriter.close();

		write(out, "HTTP/1.1 " + (e instanceof BadRequest400 ? "400 Bad Request" : "500 Internal Server Error"));
		write(out, "Content-type: text/plain");
		write(out, "Content-length: " + baos.size());
		write(out, "");
		out.write(baos.toByteArray());
		out.flush();
	}

	private String httpReturnCode(int status) {
		if (status == 0 || status == 200) {
			return "200 OK";
		}
		return "" + status + " ERROR";
	}

	private void write(OutputStream os, String s) throws IOException {
		s = s + "\r\n";
		os.write(s.getBytes(UTF_8));
	}

}
