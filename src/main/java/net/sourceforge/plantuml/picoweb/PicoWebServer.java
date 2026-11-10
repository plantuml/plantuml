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
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cli.CliFlag;
import net.sourceforge.plantuml.cli.CliOptions;
import net.sourceforge.plantuml.cli.CliParsed;
import net.sourceforge.plantuml.cli.CliParser;
import net.sourceforge.plantuml.cli.CliParsingException;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderUtil;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.crash.CrashReportHandler;
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
import net.sourceforge.plantuml.version.Version;

public class PicoWebServer implements Runnable {

	private final Socket connect;
	static boolean enableStop;
	// The format the client asked for, once known: an unexpected failure is then reported in
	// that format (see sendError). Null as long as the request is not understood.
	private FileFormat requestedFormat;

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
		System.err.println("webAddress=" + (bindAddress == null ? "0.0.0.0" : bindAddress));
		serverLoop(serverConnect);
	}

	public static void serverLoop(final ServerSocket serverConnect) throws IOException {
		try {
			while (serverConnect.isClosed() == false) {
				final PicoWebServer myServer = new PicoWebServer(serverConnect.accept());
				final Thread thread = new Thread(myServer);
				thread.start();
			}
		} catch (SocketException e) {
			if (serverConnect.isClosed())
				return;
			throw e;
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
		requestedFormat = format;
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

		// A zero-length array is not "no options": CliOptions' own zero-arg
		// constructor reads it as "this is a bare `java -jar plantuml.jar`
		// invocation with nothing after it" and reacts by asking to launch the
		// graphical UI (GlobalConfigKey.GUI) - a convenience meant for a human
		// at a terminal, not for an HTTP render request that simply has nothing
		// extra to configure. RenderRequest.fromJson() returns exactly that
		// empty array whenever the JSON body omits "options", which is the
		// common case. Padding with an inert, already-default flag keeps that
		// unrelated branch from ever firing here.
		final String[] requestOptions = renderRequest.getOptions();
		final String[] options = requestOptions.length == 0 ? new String[] { "--charset", "UTF-8" } : requestOptions;
		// Before parsing: the options themselves may be what fails
		requestedFormat = guessFormat(requestOptions);
		// Before CliParser.parse() too: it reads the files named by --config and -I with a plain
		// FileReader, outside the security profiles, and the answer is drawn in the image.
		checkOptions(options);
		final CliOptions option = CliParser.parse(options);

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
					new ErrorUml(SYNTAX_ERROR, "No valid @start/@end found, please check the version"), null,
					Collections.<StringLocated>emptyList(), new PreprocessingArtifact());
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
		write(out, "X-PlantUML-Diagram-Description: " + oneLine(system.getDescription().getDescription()));
		if (system instanceof PSystemError) {
			final PSystemError error = (PSystemError) system;
			for (ErrorUml err : error.getErrorsUml()) {
				write(out, "X-PlantUML-Diagram-Error: " + oneLine(err.getError()));
				write(out, "X-PlantUML-Diagram-Error-Line: " + (1 + err.getLineLocation().getPosition()));
			}
		} else if (imageData.getRootCause() != null) {
			// The diagram was understood but could not be drawn: all a client used to get was
			// the status 503, the reason being only inside the image. There is no line of the
			// source to give here.
			write(out, "X-PlantUML-Diagram-Error: " + singleLine(imageData.getRootCause()));
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

	/**
	 * The options a client of "POST /render" may give: the ones that shape the image
	 * (format, dark mode, theme, defines, skin parameters, pragmas, charset...). All the
	 * others - --config, -I (read a file of the server), --dot-path (runs a program),
	 * -o, -filedir, --http-server... - act on the machine of the server and are refused:
	 * the security profile (PLANTUML_SECURITY_PROFILE) does not apply to them, so an
	 * allow list is the only protection. A flag that is not in the list is refused by
	 * default, whatever its spelling, as the check works on the flags the parser itself
	 * recognized.
	 */
	private static final Set<CliFlag> ALLOWED_RENDER_FLAGS = EnumSet.of(CliFlag.FORMAT, CliFlag.DARK_MODE,
			CliFlag.DEFINE, CliFlag.DEFINE_LONG, CliFlag.SKINPARAM, CliFlag.SKINPARAM_LONG, CliFlag.PRAGMA,
			CliFlag.PRAGMA_LONG, CliFlag.THEME, CliFlag.CHARSET, CliFlag.NO_METADATA, CliFlag.DECIMAL);

	static void checkOptions(String[] options) throws BadRequest400 {
		final CliParsed parsed;
		try {
			parsed = CliParsed.parse(options);
		} catch (CliParsingException e) {
			// Not ours to judge: CliParser.parse() reports it the usual way
			return;
		}
		for (CliFlag flag : parsed.getFlags()) {
			if (ALLOWED_RENDER_FLAGS.contains(flag))
				continue;
			// The output formats: -tsvg, -tpng, -ttxt...
			if (flag.getFoo() instanceof FileFormat)
				continue;
			throw new BadRequest400("Option not allowed here: " + oneLine(flag.toString()));
		}
	}

	/**
	 * The formats an error can be drawn in. Any other one (pdf, xmi...) falls back to png:
	 * an image is more useful to the client than the text of a stack trace.
	 */
	static FileFormat guessFormat(String[] options) {
		FileFormat result = FileFormat.PNG;
		for (int i = 0; i < options.length; i++) {
			final String o = options[i];
			if (o.equals("-tsvg") || o.equals("-svg") || o.equals("--svg"))
				result = FileFormat.SVG;
			else if (o.equals("-ttxt") || o.equals("-txt") || o.equals("--txt"))
				result = FileFormat.ATXT;
			else if (o.equals("-tutxt") || o.equals("-utxt") || o.equals("--utxt"))
				result = FileFormat.UTXT;
			else if (o.equals("-tpng") || o.equals("-png") || o.equals("--png"))
				result = FileFormat.PNG;
			else if ((o.equals("-f") || o.equals("--format")) && i + 1 < options.length)
				result = fromCli(options[i + 1], result);
		}
		return result;
	}

	private static FileFormat fromCli(String name, FileFormat defaultFormat) {
		final FileFormat format = FileFormat.fromCli(name);
		if (format == FileFormat.PNG || format == FileFormat.SVG || format == FileFormat.ATXT
				|| format == FileFormat.UTXT)
			return format;

		return defaultFormat;
	}

	/**
	 * An unexpected failure is sent as an image (or as text for the text formats) of the
	 * crash report, with the stack trace in it, in the format the client asked for: a client
	 * that expects an image has no use for a text/plain body. Nothing is written before the
	 * whole image exists, so that a failure of the image itself leaves the text answer below.
	 */
	private void sendErrorImage(Throwable e, BufferedOutputStream out, FileFormat format) throws Exception {
		final CrashReportHandler report = new CrashReportHandler(e, null, null);
		report.add("An error has occurred : " + e);
		report.addEmptyLine();
		report.add("PlantUML (" + Version.versionString() + ") could not process this request.");
		report.addEmptyLine();
		report.addProperties();
		report.addEmptyLine();
		report.youShouldSendThisDiagram();
		report.addEmptyLine();

		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		report.exportDiagramError(new FileFormatOption(format), 0, os);
		os.close();

		write(out, "HTTP/1.1 500 Internal Server Error");
		write(out, "Cache-Control: no-cache");
		write(out, "Server: PlantUML PicoWebServer " + Version.versionString());
		write(out, "Date: " + new Date());
		write(out, "Access-Control-Allow-Origin: *");
		write(out, "Content-type: " + format.getMimeType());
		write(out, "Content-length: " + os.size());
		write(out, "X-PlantUML-Diagram-Error: " + singleLine(e));
		write(out, "");
		out.flush();
		out.write(os.toByteArray());
		out.flush();
	}

	private void sendError(Throwable e, BufferedOutputStream out) throws Exception {
		// A bad request is the client's mistake, told in a few words: not a crash report
		if (requestedFormat != null && e instanceof BadRequest400 == false) {
			try {
				sendErrorImage(e, out, requestedFormat);
				return;
			} catch (Throwable e2) {
				Logme.error(e2);
			}
		}
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

	/**
	 * A text on one line, so that it can be a header value. The error texts come from the
	 * diagram itself, where %chr(13)%chr(10) can build a real CR/LF (an !include of a missing
	 * file puts its expanded path in the message): left as is, the client would control the
	 * headers of the response, and even a second response (HTTP response splitting).
	 * Every other control character is folded as well: none belongs in a header value.
	 */
	static String oneLine(String text) {
		if (text == null)
			return "";
		return text.replaceAll("[\\p{Cntrl}]+", " ");
	}

	/** An exception on one line, so that it can be a header value. */
	private static String singleLine(Throwable t) {
		final String message = t.getMessage();
		return oneLine(t.getClass().getName() + (message == null ? "" : ": " + message));
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
