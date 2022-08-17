package net.sourceforge.plantuml.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Logger {

	private static final java.util.logging.Logger logger;

	static {
		logger = java.util.logging.Logger.getLogger("com.plantuml");
		logger.setUseParentHandlers(false);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new Formatter() {
			@Override
			public synchronized String format(LogRecord lr) {
				String throwable = "";
				if (lr.getThrown() != null) {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					pw.println();
					lr.getThrown().printStackTrace(pw);
					pw.close();
					throwable = sw.toString().trim();
				}
				String message = lr.getMessage();
				StringBuilder sb = new StringBuilder();
				if (message.trim().length() > 0) {
					sb.append(message);
					sb.append(System.lineSeparator());
				}
				if (throwable.length() > 0) {
					sb.append(throwable);
					sb.append(System.lineSeparator());
				}
				return sb.toString();
			}
		});
		logger.addHandler(handler);
	}

	public static void error(Throwable thrown) {
		logger.log(Level.SEVERE, "", thrown);
	}

	public static void error(String msg, Throwable thrown) {
		logger.log(Level.SEVERE, msg, thrown);
	}

	public static void error(String msg) {
		logger.log(Level.SEVERE, msg);
	}
}
