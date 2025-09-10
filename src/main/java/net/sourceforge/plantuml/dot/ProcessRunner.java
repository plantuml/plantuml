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
package net.sourceforge.plantuml.dot;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import net.sourceforge.plantuml.cli.GlobalConfig;
import net.sourceforge.plantuml.cli.GlobalConfigKey;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SFile;

public class ProcessRunner {
	// ::remove file when __CORE__

	private final String[] cmd;
	private String error;
	private String out;

	public ProcessRunner(String[] cmd) {
		this.cmd = cmd;
	}

	public ProcessState run(byte[] in, OutputStream redirection) {
		return run(in, redirection, null);
	}

	public ProcessState run(byte[] in, OutputStream redirection, SFile dir) {
		Process process = null;
		try {
			final ProcessBuilder builder = new ProcessBuilder(cmd);
			if (dir != null)
				builder.directory(dir.conv());
			builder.redirectErrorStream(true);

			process = builder.start();

			// Handling input to the process
			if (in != null)
				try (OutputStream os = process.getOutputStream()) {
					os.write(in);
				}

			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try (InputStream is = process.getInputStream()) {
				final byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer)) != -1) {
					outputStream.write(buffer, 0, length);
					if (redirection != null)
						redirection.write(buffer, 0, length);
				}
			}

			// Wait for process to terminate
			final long timeoutMs = (Long) GlobalConfig.getInstance().value(GlobalConfigKey.TIMEOUT_MS);
			final boolean finished = process.waitFor(timeoutMs, TimeUnit.MILLISECONDS);
			outputStream.close();
			if (finished) {
				this.out = new String(outputStream.toByteArray(), "UTF-8");
				return ProcessState.TERMINATED_OK();
			}

			return ProcessState.TIMEOUT();

		} catch (Throwable e) {
			Logme.error(e);
			this.error = e.toString();
			return ProcessState.EXCEPTION(e);
		} finally {
			if (process != null && out == null && process.isAlive()) {
				// Process did not finish in time, kill it
				process.destroy();
				// Not really sure that we should overwrite "this.error" here
				this.error = "Timeout - kill";
				try {
					if (process.waitFor(500, TimeUnit.MILLISECONDS) == false) {
						process.destroyForcibly();
						this.error = "Timeout - kill force";
					}
				} catch (InterruptedException e) {
					// Nothing we can really do
					e.printStackTrace();
				}

			}

		}
	}

	public final String getError() {
		return error;
	}

	public final String getOut() {
		return out;
	}

}
