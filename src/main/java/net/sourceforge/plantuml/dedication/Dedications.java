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
 *
 */
package net.sourceforge.plantuml.dedication;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.log.Logme;

public class Dedications {

	private static final List<Dedication> all = new ArrayList<>();

	static {
		try {
			all.add(new DedicationSimple(load("dedication"), "Write your own dedication!"));
			all.add(new DedicationSimple(load("linux_china"), "linux_china"));
			all.add(new DedicationSimple(load("arkban"), "arkban"));
			all.add(new DedicationSimple(load("boundaries"), "Boundaries allow discipline to create true strength"));
			all.add(new DedicationSimple(load("dr_chet"), "Thank you, Dr. Chet. I wouldn't be where I am without you"));
			all.add(new DedicationSimple(load("ben"), "Ben and Jen 2020"));
			all.add(new DedicationSimple(load("baraye"), "baraye"));
			all.add(new DedicationSimple(load("nothing"), "Nothing of value is ever easy"));
			all.add(new DedicationSimple(load("friends"), "Have good friends and be a good friend to other people"));
			all.add(new DedicationSimple(load("fatherson"),
					"By the time a man realizes that maybe his father was right, he usually has a son who thinks he is wrong"));
			all.add(secret(5, "835ff5d643b58cd35a20db6480071d05751aa6a0e01da78662ceafd0161f3f5e", new BigInteger(
					"1182423723677118831606503500858825217076578422970565964857326298418401529955036896808663335300684244453386039908536275400945824932191521017102701344437753036730900076162922741167523337650578479960119614237031234925702200473053235777")));
			all.add(secret(3, "514816d583044efbd336882227deb822194ff63e3bdc3cf707a01f17770d5a6a", new BigInteger(
					"538955952645999836068094511687012232127213955837942782605199622268460518023083462090291844640318324475656060087513198129259364840841077651829017347845508167869708224054456044836844382437974410757740941102771969965334031780041648873")));
			all.add(secret(1, "ae8a7cf3997ccd6418866fc59e596502e1bd1c0265bba2fc380ad7f51c76518f", new BigInteger(
					"987988542836850639056829173787067531749177506648884857100630852970876999799588072360773169026225182488073794585127241896588994816566037813451743416913613428321215803586563629080034406083114565732322220091545330060636171674602040157")));
		} catch (Throwable t) {
			Logme.error(t);
		}
	}

	private static DedicationCrypted secret(int tiny, String sig, BigInteger pq) throws IOException {
		return new DedicationCrypted(load(sig), tiny, sig, pq);
	}

	private static byte[] load(String name) throws IOException {
		final InputStream tmp = PSystemDedication.class.getResourceAsStream(name + ".png");
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileUtils.copyInternal(tmp, baos, true);
		return baos.toByteArray();
	}

	private Dedications() {
	}

	public static BufferedImage get(String line) {
		if (line.length() > 25) {
			final TinyHashableString sentence = new TinyHashableString(line);
			synchronized (Dedications.class) {
				for (Dedication dedication : all) {
					final BufferedImage image = dedication.getImage(sentence);
					if (image != null)
						return image;
				}
			}
		}
		return null;
	}

}
