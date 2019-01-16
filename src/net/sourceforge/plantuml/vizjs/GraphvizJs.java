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
package net.sourceforge.plantuml.vizjs;

import java.io.File;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import net.sourceforge.plantuml.cucadiagram.dot.ExeState;
import net.sourceforge.plantuml.cucadiagram.dot.Graphviz;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizVersion;
import net.sourceforge.plantuml.cucadiagram.dot.ProcessState;

public class GraphvizJs implements Graphviz {

	private final static ExecutorService executorService = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactory() {
				public Thread newThread(Runnable runnable) {
					return new JsThread(runnable);
				}
			});

	static class JsThread extends Thread {

		private final Runnable runnable;
		private VizJsEngine engine;

		public JsThread(Runnable runnable) {
			this.runnable = runnable;
		}

		@Override
		public void run() {
			if (engine == null) {
				try {
					this.engine = new VizJsEngine();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			runnable.run();
		}

	}

	private final String dotString;

	public GraphvizJs(String dotString) {
		this.dotString = dotString;
	}

	public ProcessState createFile3(OutputStream os) {
		try {
			final String svg = submitJob().get();
			os.write(svg.getBytes());
			return ProcessState.TERMINATED_OK();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GraphvizJsRuntimeException(e);
		}
	}

	private Future<String> submitJob() {
		return executorService.submit(new Callable<String>() {
			public String call() throws Exception {
				final JsThread th = (JsThread) Thread.currentThread();
				final VizJsEngine engine = th.engine;
				return engine.execute(dotString);
			}
		});
	}

	public File getDotExe() {
		return null;
	}

	public String dotVersion() {
		return "VizJs";
	}

	public ExeState getExeState() {
		return ExeState.OK;
	}

	public static GraphvizVersion getGraphvizVersion(final boolean modeSafe) {
		return new GraphvizVersion() {
			public boolean useShield() {
				return true;
			}

			public boolean useProtectionWhenThereALinkFromOrToGroup() {
				return true;
			}

			public boolean useXLabelInsteadOfLabel() {
				return modeSafe;
			}

			public boolean isVizjs() {
				return true;
			}

			public boolean ignoreHorizontalLinks() {
				return false;
			}
		};
	}

}
