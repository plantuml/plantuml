/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 10765 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProcessRunner {
	// http://steveliles.github.io/invoking_processes_from_java.html

	private static final long TIMEOUT_MINUTE = 15;

	private final String[] cmd;

	private String error;
	private String out;

	private volatile ProcessState state = ProcessState.INIT;
	private final Lock changeState = new ReentrantLock();

	public ProcessRunner(String[] cmd) {
		this.cmd = cmd;
	}

	public ProcessState run2(byte in[], OutputStream redirection) {
		return run2(in, redirection, null);
	}

	public ProcessState run2(byte in[], OutputStream redirection, File dir) {
		if (this.state != ProcessState.INIT) {
			throw new IllegalStateException();
		}
		this.state = ProcessState.RUNNING;
		final MainThread mainThread = new MainThread(cmd, dir, redirection, in);
		try {
			mainThread.start();
			mainThread.join(TIMEOUT_MINUTE * 60 * 1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			changeState.lock();
			try {
				if (state == ProcessState.RUNNING) {
					state = ProcessState.TIMEOUT;
					mainThread.cancel();
				}
			} finally {
				changeState.unlock();
			}
		}
		if (state == ProcessState.TERMINATED_OK) {
			assert mainThread != null;
			this.error = mainThread.getError();
			this.out = mainThread.getOut();
		}
		return state;
	}

	class MainThread extends Thread {

		private final String[] cmd;
		private final File dir;
		private final OutputStream redirection;
		private final byte[] in;
		private volatile Process process;
		private volatile ThreadStream errorStream;
		private volatile ThreadStream outStream;

		public MainThread(String[] cmd, File dir, OutputStream redirection, byte[] in) {
			this.cmd = cmd;
			this.dir = dir;
			this.redirection = redirection;
			this.in = in;
		}

		public String getOut() {
			return outStream.getString();
		}

		public String getError() {
			return errorStream.getString();
		}

		@Override
		public void run() {
			try {
				runInternal();
				if (state == ProcessState.RUNNING) {
					final int result = joinInternal();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				changeState.lock();
				try {
					if (state == ProcessState.RUNNING) {
						state = ProcessState.TERMINATED_OK;
					}
				} finally {
					changeState.unlock();
				}
				if (process != null) {
					process.destroy();
					close(process.getErrorStream());
					close(process.getOutputStream());
					close(process.getInputStream());
				}
			}

		}

		private void cancel() {
			// The changeState lock is ok
			assert changeState.tryLock();
			assert state == ProcessState.TIMEOUT;
			if (process != null) {
				errorStream.cancel();
				outStream.cancel();
				process.destroy();
				interrupt();
				close(process.getErrorStream());
				close(process.getOutputStream());
				close(process.getInputStream());
			}
		}

		public void runInternal() {
			try {
				process = Runtime.getRuntime().exec(cmd, null, dir);
			} catch (IOException e) {
				changeState.lock();
				try {
					state = ProcessState.IO_EXCEPTION1;
				} finally {
					changeState.unlock();
				}
				e.printStackTrace();
				return;
			}
			errorStream = new ThreadStream(process.getErrorStream(), null);
			outStream = new ThreadStream(process.getInputStream(), redirection);
			errorStream.start();
			outStream.start();
			if (in != null) {
				final OutputStream os = process.getOutputStream();
				try {
					try {
						os.write(in);
					} finally {
						os.close();
					}
				} catch (IOException e) {
					changeState.lock();
					try {
						state = ProcessState.IO_EXCEPTION2;
					} finally {
						changeState.unlock();
					}
					e.printStackTrace();
				}
			}
		}

		public int joinInternal() throws InterruptedException {
			errorStream.join();
			outStream.join();
			final int result = process.waitFor();
			return result;
		}

	}

	class ThreadStream extends Thread {

		private volatile InputStream streamToRead;
		private volatile OutputStream redirection;
		private volatile StringBuffer sb = new StringBuffer();

		ThreadStream(InputStream streamToRead, OutputStream redirection) {
			this.streamToRead = streamToRead;
			this.redirection = redirection;
		}

		public String getString() {
			return sb.toString();
		}

		public void cancel() {
			assert state == ProcessState.TIMEOUT;
			this.interrupt();
			sb = null;
			streamToRead = null;
			redirection = null;
			// Because of this, some NPE may occurs in run() method, but we do not care
		}

		@Override
		public void run() {
			int read = 0;
			try {
				while ((read = streamToRead.read()) != -1) {
					if (state == ProcessState.TIMEOUT) {
						return;
					}
					if (redirection == null) {
						sb.append((char) read);
					} else {
						redirection.write(read);
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
				sb.append('\n');
				sb.append(e.toString());
			}
		}
	}

	public final String getError() {
		return error;
	}

	public final String getOut() {
		return out;
	}

	private void close(InputStream is) {
		try {
			if (is != null) {
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void close(OutputStream os) {
		try {
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
