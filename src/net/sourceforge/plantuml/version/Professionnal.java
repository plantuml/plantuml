/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.prefs.Preferences;

import net.sourceforge.plantuml.OptionPrint;
import net.sourceforge.plantuml.dedication.DecoderInputStream;
import net.sourceforge.plantuml.dedication.Dedication;
import net.sourceforge.plantuml.dedication.QBlocks;

public class Professionnal {

	final private static Preferences prefs = Preferences.userNodeForPackage(Professionnal.class);

	private final String hostname;
	private final String organization;
	private final String mail;
	private final Date date1;
	private final Date date2;

	private Professionnal(String hostname, String organization, String mail, int date1, int date2) {
		this.hostname = hostname;
		this.organization = organization;
		this.mail = mail;
		this.date1 = new Date(date1 * 1000L);
		this.date2 = new Date(date2 * 1000L);
	}

	public static Professionnal decodeNow(final String license) throws IOException {
		final QBlocks data = QBlocks.descodeAscii(license);
		final QBlocks decrypted = data.change(Dedication.E, Dedication.N);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		decrypted.writeTo(baos, Dedication.SIZE);
		baos.close();

		final String hostname = OptionPrint.getHostName();
		InputStream source1 = null;
		try {
			source1 = new DecoderInputStream(new ByteArrayInputStream(baos.toByteArray()), hostname);
			final DataInputStream src = new DataInputStream(source1);
			final int version = src.readByte();
			if (version != 42) {
				throw new IOException();
			}
			final String host1 = src.readUTF();
			final String organization = src.readUTF();
			final String mail = src.readUTF();
			final int date1 = src.readInt();
			final int date2 = src.readInt();

			return new Professionnal(host1, organization, mail, date1, date2);
		} finally {
			if (source1 != null) {
				source1.close();
			}
		}
	}

	public String getHostname() {
		return hostname;
	}

	public String getMail() {
		return mail;
	}

	public String getOrganization() {
		return organization;
	}

	public Date getDate1() {
		return date1;
	}

	public Date getDate2() {
		return date2;
	}

}
