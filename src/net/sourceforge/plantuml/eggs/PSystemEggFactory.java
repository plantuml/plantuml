/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 6750 $
 *
 */
package net.sourceforge.plantuml.eggs;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.DiagramType;
import net.sourceforge.plantuml.PSystemBasicFactory;

public class PSystemEggFactory implements PSystemBasicFactory {

	private PSystemEgg system;

	public void init(String startLine) {
	}

	final static private List<byte[]> all = Arrays
			.asList(
					EggUtils
							.toByteArrays("56092d35fce86a0dd88047a766c1d6541a7c5fd5ba212fa02db9a32a463422febd71a75a934eb135dec7d6c6325ddd17fd2fa437eba863462b28e3e92514998306a72790d93501335ed6b1262ea46ab79573142c28f8e92508978255a533d9cf7903394f9ab73a33b230a2b273033633adf16044888243b92f9bd8351f3d4f9aa2302fb264afa37546368424fa6a07919152bd2990d935092e49d9a02038b437aeb528"),
					EggUtils.toByteArrays("421e5b773c5df733a1194f716f18e8842155196b3b"));

	public boolean executeLine(String line) {
		try {
			for (byte[] crypted : all) {
				final SentenceDecoder decoder = new SentenceDecoder(line, crypted);
				if (decoder.isOk()) {
					system = new PSystemEgg(decoder.getSecret());
					return true;
				}
			}
		} catch (UnsupportedEncodingException e) {
			return false;
		}

		return false;
	}

	public PSystemEgg getSystem() {
		return system;
	}
	
	public DiagramType getDiagramType() {
		return DiagramType.UML;
	}


}
