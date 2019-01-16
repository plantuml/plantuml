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
 *
 */
package net.sourceforge.plantuml.ftp;

// server

// FtpServer.java
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.StringUtils;

class FtpLoop implements Runnable {
	enum Mode {
		ACTIF, PASSIF
	};

	final private Socket incoming;
	final private FtpServer ftpServer;
	final private BufferedReader br;
	final private PrintWriter pw;

	private FtpConnexion connexion;
	private String ipClient = null;
	private int port = -1;
	private Mode mode;

	public FtpLoop(Socket socket, FtpServer ftpServer) throws IOException {
		this.incoming = socket;
		this.ftpServer = ftpServer;
		this.br = new BufferedReader(new InputStreamReader(incoming.getInputStream(), ftpServer.getCharset()));
		this.pw = new PrintWriter(incoming.getOutputStream(), true);
	}

	// http://www.ncftp.com/libncftp/doc/ftp_overview.html
	// http://www.nsftools.com/tips/RawFTP.htm
	// http://www.commentcamarche.net/contents/internet/ftp.php3
	// http://en.wikipedia.org/wiki/List_of_FTP_server_return_codes
	// http://www.freefire.org/articles/ftpexample.php
	// http://forum.hardware.fr/hfr/Programmation/VB-VBA-VBS/transfert-sujet_59989_1.htm
	// http://www.excel-downloads.com/forum/104130-telechargement-ftp-via-vba.html
	// http://www.pcreview.co.uk/forums/ftp-vba-macro-t949945.html
	private void runInternal() throws IOException, InterruptedException {
		localLog("Starting Loop");
		myOut("220 PlantUML");
		while (true) {
			final String s = br.readLine();
			localLog("s=" + s);
			if (s == null) {
				pw.close();
				br.close();
				return;
			}
			final boolean finish = manage(s);
			if (finish) {
				return;
			}
		}
	}

	private boolean manage(final String cmd) throws UnknownHostException, IOException, InterruptedException {
		final String upper = StringUtils.goUpperCase(cmd);
		if (upper.startsWith("USER")) {
			myOut("331 Password required");
			final String user = cmd.substring("USER ".length());
			connexion = ftpServer.getFtpConnexion(user);
		} else if (upper.startsWith("PASS")) {
			myOut("230 Logged in.");
		} else if (upper.startsWith("PWD")) {
			// myOut("/");
			// myOut("200 OK /");
			myOut("257 \"/\" is current directory.");
		} else if (upper.startsWith("CWD")) {
			final String dir = cmd.substring("CWD ".length());
			myOut("250 \"" + dir + "\" is new working directory..");
		} else if (upper.startsWith("TYPE")) {
			myOut("200 Command okay.");
			// localLog("type=" + s);
		} else if (upper.startsWith("PORT")) {
			mode = Mode.ACTIF;
			final StringTokenizer st = new StringTokenizer(cmd, " ,");
			st.nextToken();
			ipClient = st.nextToken() + "." + st.nextToken() + "." + st.nextToken() + "." + st.nextToken();
			port = Integer.parseInt(st.nextToken()) * 256 + Integer.parseInt(st.nextToken());
			// localLog("ipClient=" + ipClient);
			// localLog("port=" + port);

			myOut("200 Command okay.");
		} else if (upper.startsWith("LIST")) {
			if (mode == Mode.ACTIF) {
				listActif();
			} else {
				listPassif();
			}
		} else if (upper.startsWith("STOR")) {
			if (mode == Mode.ACTIF) {
				storActif(cmd);
			} else {
				storPassif(cmd);
			}
		} else if (upper.startsWith("PASV")) {
			mode = Mode.PASSIF;
			port = ftpServer.getFreePort();
			final int p1 = port / 256;
			final int p2 = port % 256;
			assert port == p1 * 256 + p2;
			localLog("adr=" + incoming.getInetAddress().getHostAddress());
			final String ipServer = ftpServer.getIpServer();
			localLog("server=" + ipServer);
			myOut("227 Entering Passive Mode (" + ipServer.replace('.', ',') + "," + p1 + "," + p2 + ").");
			ipClient = ipServer;
		} else if (upper.startsWith("RETR")) {
			if (mode == Mode.ACTIF) {
				retrActif(cmd);
			} else {
				retrPassif(cmd);
			}
		} else if (upper.startsWith("DELE")) {
			final String file = cmd.substring("DELE ".length());
			connexion.delete(file);
			myOut("200 Command okay.");
		} else if (upper.startsWith("QUIT")) {
			myOut("221 Goodbye.");
			return true;
		} else if (upper.startsWith("SYST")) {
			myOut("215 UNIX Type: L8.");
		} else {
			myOut("502 Command not implemented.");
		}
		return false;
	}

	private void localLog(String s) {
	}

	private void retr(final String fileName, Socket soc) throws UnknownHostException, IOException, InterruptedException {
		final OutputStream os = soc.getOutputStream();
		final byte[] data = connexion.getData(fileName);

		if (data != null) {
			os.write(data);
		}
		os.flush();
		os.close();
		soc.close();
		myOut("226 Transfer complete.");
	}

	private void retrPassif(final String s) throws UnknownHostException, IOException, InterruptedException {
		String fileName = s.substring("STOR ".length());
		fileName = removeStartingsSlash(fileName);
		if (connexion.willExist(fileName) == false) {
			myOut("550 No such file.");
			return;
		}
		myOut("150 Opening");
		waitForMe(fileName);
		final ServerSocket ss = new ServerSocket(port);
		final Socket incoming = ss.accept();
		retr(fileName, incoming);
		ss.close();
	}

	private void waitForMe(String fileName) throws InterruptedException {
		do {
			if (connexion.doesExist(fileName)) {
				return;
			}
			Thread.sleep(200L);
		} while (true);
	}

	private void retrActif(final String s) throws UnknownHostException, IOException, InterruptedException {
		String fileName = s.substring("STOR ".length());
		fileName = removeStartingsSlash(fileName);
		if (connexion.willExist(fileName) == false) {
			myOut("550 No such file.");
			return;
		}
		myOut("150 Opening");
		waitForMe(fileName);
		final Socket soc = new Socket(ipClient, port);
		retr(fileName, soc);
	}

	private void storActif(final String s) throws IOException {
		final String fileName = removeStartingsSlash(s.substring("STOR ".length()));
		myOut("150 FILE: " + fileName);
		final Socket soc = new Socket(ipClient, port);
		stor(fileName, soc);
	}

	private void storPassif(final String s) throws IOException {
		final String fileName = removeStartingsSlash(s.substring("STOR ".length()));
		myOut("150 FILE: " + fileName);
		final ServerSocket ss = new ServerSocket(port);
		final Socket incoming = ss.accept();
		stor(fileName, incoming);
		ss.close();
	}

	private String removeStartingsSlash(String fileName) {
		while (fileName.startsWith("/")) {
			fileName = fileName.substring(1);
		}
		return fileName;
	}

	private void stor(String fileName, Socket socket) throws UnknownHostException, IOException {
		final InputStream is = socket.getInputStream();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileUtils.copyToStream(is, baos);

		myOut("226 Transfer complete.");

		if ("png".equalsIgnoreCase(fileName)) {
			connexion.setFileFormat(FileFormat.PNG);
		} else if ("svg".equalsIgnoreCase(fileName)) {
			connexion.setFileFormat(FileFormat.SVG);
		} else if ("eps".equalsIgnoreCase(fileName)) {
			connexion.setFileFormat(FileFormat.EPS);
		}

		if (fileName.length() > 3) {
			final String data = new String(baos.toByteArray(), ftpServer.getCharset());
			final String pngFileName = connexion.getFutureFileName(fileName);
			connexion.futureOutgoing(pngFileName);
			connexion.addIncoming(fileName, data);

			ftpServer.processImage(connexion, fileName);
		}
	}

	private void listActif() throws UnknownHostException, IOException {
		myOut("150 Opening ASCII mode data");
		final Socket soc = new Socket(ipClient, port);
		list(soc);
	}

	private void listPassif() throws UnknownHostException, IOException {
		myOut("150 Opening ASCII mode data");
		final ServerSocket ss = new ServerSocket(port);
		final Socket incoming = ss.accept();
		list(incoming);
		ss.close();
	}

	private void list(final Socket soc) throws IOException {
		final PrintWriter listing = new PrintWriter(soc.getOutputStream(), true);
		final Collection<String> files = connexion.getFiles();
		if (files.size() > 0) {
			int total = 0;
			for (String n : files) {
				total += (connexion.getSize(n) + 511) / 512;
			}
			listing.println("total " + total);
			// localLog(total);
			for (String n : files) {
				final String ls = String.format("%10s %4d %-8s %-8s %8d %3s %2s %5s %s", "-rw-rw-r--", 1, "plantuml",
						"plantuml", connexion.getSize(n), "Sep", 28, 2006, n);
				listing.println(ls);
				// localLog(ls);
			}
		}
		listing.flush();
		listing.close();
		soc.close();
		myOut("226 Listing completed.");
	}

	private void myOut(String s) {
		if (s.indexOf('\t') != -1) {
			throw new IllegalArgumentException();
		}
		pw.println(s);
		pw.flush();
	}

	public void run() {
		try {
			runInternal();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

}
