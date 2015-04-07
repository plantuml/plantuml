/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 3837 $
 *
 */
package net.sourceforge.plantuml.telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.SourceStringReader;

class AcceptTelnetClient extends Thread {
	final private Socket clientSocket;
	final private BufferedReader br;
	final private OutputStream os;

	AcceptTelnetClient(Socket socket) throws Exception {
		clientSocket = socket;
		System.out.println("Client Connected ...");
		br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		os = clientSocket.getOutputStream();

		start();
	}

	public String runInternal() throws IOException {
		final StringBuilder sb = new StringBuilder();
		while (true) {
			final String s = br.readLine();
			if (s == null) {
				return sb.toString();
			}
			Log.println("S=" + s);
			sb.append(s);
			sb.append('\n');
			if (s.equalsIgnoreCase("@enduml")) {
				return sb.toString();
			}
		}
	}

	public void run() {
		try {
			final String uml = runInternal();
			Log.println("UML=" + uml);
			final SourceStringReader s = new SourceStringReader(uml);
			s.generateImage(os, new FileFormatOption(FileFormat.ATXT));
			os.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}