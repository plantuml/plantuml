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
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProcessRunnerDebug {
	// http://steveliles.github.io/invoking_processes_from_java.html

	public static boolean DEBUG = false;
	private static final AtomicInteger CPT = new AtomicInteger();

	private void DEBUG(String s) {
		if (DEBUG == false) {
			return;
		}
		final StringBuilder sb = new StringBuilder();
		final int activeCount = Thread.activeCount();
		final long freeMemory = Runtime.getRuntime().freeMemory() / 1024L / 1024L;
		final long totalMemory = Runtime.getRuntime().totalMemory() / 1024L / 1024L;
		sb.append(new Date().toGMTString() + " {");
		sb.append(ident + "} " + freeMemory + "/" + totalMemory + "/" + activeCount + " " + s);
		DebugTrace.DEBUG(sb.toString());
	}

	private void DEBUG(String s, Throwable t) {
		if (DEBUG == false) {
			return;
		}
		DebugTrace.DEBUG(s, t);
	}

	private static final long TIMEOUT_MINUTE = 15;

	private final String[] cmd;
	private final String ident = myFormat(CPT.getAndIncrement());

	private static String myFormat(int v) {
		final StringBuilder sb = new StringBuilder(Integer.toHexString(v).toUpperCase());
		while (sb.length() < 5) {
			sb.insert(0, '0');
		}
		return sb.toString();
	}

	private String error;
	private String out;

	private volatile ProcessState state = ProcessState.INIT;
	private final Lock changeState = new ReentrantLock();

	public ProcessRunnerDebug(String[] cmd) {
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
		DEBUG("run A100");
		final MainThread mainThread = new MainThread(cmd, dir, redirection, in);
		DEBUG("run A200");
		try {
			mainThread.start();
			DEBUG("run A300");
			mainThread.join(TIMEOUT_MINUTE * 60 * 1000L);
			DEBUG("run A400");
		} catch (InterruptedException e) {
			e.printStackTrace();
			DEBUG("run A500", e);
		} finally {
			DEBUG("run A600");
			changeState.lock();
			DEBUG("run A700");
			try {
				if (state == ProcessState.RUNNING) {
					state = ProcessState.TIMEOUT;
					DEBUG("run A800");
					mainThread.cancel();
					DEBUG("run A900");
				}
			} finally {
				changeState.unlock();
			}
		}
		if (state == ProcessState.TERMINATED_OK) {
			DEBUG("run A1000");
			// Ok!
			assert mainThread != null;
			this.error = mainThread.getError();
			this.out = mainThread.getOut();
		} else {
			DEBUG("run A1100");
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
				DEBUG("MainThread B100");
				runInternal();
				DEBUG("MainThread B200");
				if (state == ProcessState.RUNNING) {
					final int result = joinInternal();
					DEBUG("MainThread B300 " + result);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				DEBUG("MainThread B400", e);
			} finally {
				changeState.lock();
				try {
					if (state == ProcessState.RUNNING) {
						DEBUG("MainThread B500");
						state = ProcessState.TERMINATED_OK;
					} else {
						DEBUG("MainThread B600");
					}
				} finally {
					changeState.unlock();
				}
				if (process != null) {
					DEBUG("MainThread B700");
					process.destroy();
					DEBUG("MainThread B800");
					close(process.getErrorStream());
					DEBUG("MainThread B900");
					close(process.getOutputStream());
					DEBUG("MainThread B1000");
					close(process.getInputStream());
				}
			}

		}

		private void cancel() {
			// The changeState lock is ok
			assert changeState.tryLock();
			assert state == ProcessState.TIMEOUT;
			DEBUG("MainThread B2000");
			if (process != null) {
				DEBUG("MainThread B2000");
				errorStream.cancel();
				DEBUG("MainThread B2000");
				outStream.cancel();
				DEBUG("MainThread B2000");
				process.destroy();
				DEBUG("MainThread B2000");
				interrupt();
				DEBUG("MainThread B2000");
				close(process.getErrorStream());
				DEBUG("MainThread B2000");
				close(process.getOutputStream());
				DEBUG("MainThread B2000");
				close(process.getInputStream());
				DEBUG("MainThread B2000");
			} else {
				DEBUG("MainThread B2000");
			}
		}

		public void runInternal() {
			try {
				DEBUG("MainThread runInternal B3010");
				process = Runtime.getRuntime().exec(cmd, null, dir);
				DEBUG("MainThread runInternal B3020");
			} catch (IOException e) {
				DEBUG("MainThread runInternal B3030", e);
				changeState.lock();
				try {
					state = ProcessState.IO_EXCEPTION1;
				} finally {
					changeState.unlock();
				}
				e.printStackTrace();
				return;
			}
			DEBUG("MainThread runInternal B3040");
			errorStream = new ThreadStream(process.getErrorStream(), null);
			DEBUG("MainThread runInternal B3050");
			outStream = new ThreadStream(process.getInputStream(), redirection);
			DEBUG("MainThread runInternal B3060");
			errorStream.start();
			DEBUG("MainThread runInternal B3070");
			outStream.start();
			if (in != null) {
				DEBUG("MainThread runInternal B3080");
				final OutputStream os = process.getOutputStream();
				DEBUG("MainThread runInternal B3090");
				try {
					try {
						DEBUG("MainThread runInternal B3100");
						os.write(in);
						DEBUG("MainThread runInternal B3110");
					} finally {
						os.close();
					}
				} catch (IOException e) {
					DEBUG("MainThread runInternal B3120", e);
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
			DEBUG("MainThread joinInternal B4000");
			errorStream.join();
			DEBUG("MainThread joinInternal B4010");
			outStream.join();
			DEBUG("MainThread joinInternal B4020");
			final int result = process.waitFor();
			DEBUG("MainThread joinInternal B4030 r=" + result);
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
						DEBUG("ThreadStream TIMEOUT");
						return;
					}
					if (redirection == null) {
						sb.append((char) read);
					} else {
						redirection.write(read);
					}
				}
				DEBUG("ThreadStream end ok");
			} catch (Throwable e) {
				DEBUG("ThreadStream BB1", e);
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
				DEBUG("closing AA1 ok");
			}
		} catch (IOException e) {
			DEBUG("closing error AA2", e);
			e.printStackTrace();
		}
	}

	private void close(OutputStream os) {
		try {
			if (os != null) {
				os.close();
				DEBUG("closing BB1 ok");
			}
		} catch (IOException e) {
			DEBUG("closing error BB1", e);
			e.printStackTrace();
		}
	}

}
