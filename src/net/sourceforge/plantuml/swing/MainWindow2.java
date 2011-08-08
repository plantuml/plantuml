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
 * Revision $Revision: 6533 $
 *
 */
package net.sourceforge.plantuml.swing;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import net.sourceforge.plantuml.DirWatcher2;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.Option;

public class MainWindow2 extends JFrame {

	final private static Preferences prefs = Preferences.userNodeForPackage(MainWindow2.class);
	final private static String KEY_DIR = "cur";
	final private static String KEY_PATTERN = "pat";

	private final JList jList1 = new JList();
	private final JScrollPane scrollPane;
	private final JButton changeDirButton = new JButton("Change Directory");
	private final JTextField extensions = new JTextField();

	final private List<SimpleLine2> currentDirectoryListing2 = new ArrayList<SimpleLine2>();
	final private Set<ImageWindow2> openWindows2 = new HashSet<ImageWindow2>();
	final private Option option;

	private DirWatcher2 dirWatcher;

	public MainWindow2(Option option) {
		this(new File(prefs.get(KEY_DIR, ".")), option);

	}

	private String getExtensions() {
		return prefs.get(KEY_PATTERN, getDefaultFileExtensions());
	}

	private String getDefaultFileExtensions() {
		return "txt, tex, java, htm, html, c, h, cpp, apt";
	}

	private void changeExtensions(String ext) {
		if (ext.equals(getExtensions())) {
			return;
		}
		final Pattern p = Pattern.compile("\\w+");
		final Matcher m = p.matcher(ext);
		final StringBuilder sb = new StringBuilder();

		while (m.find()) {
			final String value = m.group();
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(value);

		}
		ext = sb.toString();
		if (ext.length() == 0) {
			ext = getDefaultFileExtensions();
		}
		extensions.setText(ext);
		prefs.put(KEY_PATTERN, ext);
		changeDir(dirWatcher.getDir());
	}

	private String getRegexpPattern(String ext) {
		final Pattern p = Pattern.compile("\\w+");
		final Matcher m = p.matcher(ext);
		final StringBuilder filePattern = new StringBuilder("(?i)^.*\\.(");

		while (m.find()) {
			final String value = m.group();
			if (filePattern.toString().endsWith("(") == false) {
				filePattern.append("|");
			}
			filePattern.append(value);
		}
		if (filePattern.toString().endsWith("(") == false) {
			filePattern.append(")$");
			return filePattern.toString();
		}
		return Option.getPattern();
	}

	private MainWindow2(File dir, Option option) {
		super(dir.getAbsolutePath());
		this.option = option;
		dirWatcher = new DirWatcher2(dir, option, getRegexpPattern(getExtensions()));

		Log.info("Showing MainWindow");
		scrollPane = new JScrollPane(jList1);

		final JPanel south = new JPanel(new BorderLayout());
		final JLabel labelFileExtensions = new JLabel("File extensions: ");
		extensions.setText(getExtensions());

		labelFileExtensions.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		south.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), BorderFactory
				.createEtchedBorder()));
		south.add(labelFileExtensions, BorderLayout.WEST);
		south.add(extensions, BorderLayout.CENTER);

		south.add(changeDirButton, BorderLayout.SOUTH);

		getContentPane().add(south, BorderLayout.SOUTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		setSize(320, 200);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					final int index = jList1.locationToIndex(e.getPoint());
					doubleClick((SimpleLine2) jList1.getModel().getElementAt(index), jList1.getModel(), index);
				}
			}
		};
		jList1.addMouseListener(mouseListener);
		changeDirButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.err.println("Opening Directory Window");
				displayDialogChangeDir();
			}
		});

		extensions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeExtensions(extensions.getText());
			}
		});
		extensions.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent e) {
				changeExtensions(extensions.getText());
			}
		});

		startTimer();
	}

	private void startTimer() {
		Log.info("Init done");
		final Timer timer = new Timer(3000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.setInitialDelay(0);
		timer.start();
		Log.info("Timer started");
	}

	private void displayDialogChangeDir() {
		final JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
		chooser.setDialogTitle("Directory to watch:");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		final String currentPath = prefs.get(KEY_DIR, ".");
		chooser.setCurrentDirectory(new File(currentPath));
		Log.info("Showing OpenDialog");
		final int returnVal = chooser.showOpenDialog(this);
		Log.info("Closing OpenDialog");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			final File dir = chooser.getSelectedFile();
			changeDir(dir);
		}

	}

	private void changeDir(File dir) {
		prefs.put(KEY_DIR, dir.getAbsolutePath());
		dirWatcher.cancel();
		dirWatcher = new DirWatcher2(dir, option, getRegexpPattern(getExtensions()));
		setTitle(dir.getAbsolutePath());
		Log.info("Creating DirWatcher");
		currentDirectoryListing2.clear();
		jList1.setListData(new Vector<SimpleLine2>(currentDirectoryListing2));
		jList1.setVisible(true);
	}

	private void doubleClick(SimpleLine2 simpleLine, ListModel listModel, int index) {
		for (ImageWindow2 win : openWindows2) {
			if (win.getSimpleLine().equals(simpleLine)) {
				win.setVisible(true);
				win.setExtendedState(Frame.NORMAL);
				return;
			}
		}
		openWindows2.add(new ImageWindow2(simpleLine, this, listModel, index));
	}

	private void tick() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					final boolean changed = refreshDir();
					if (changed) {
						jList1.setListData(new Vector<SimpleLine2>(currentDirectoryListing2));
						jList1.setVisible(true);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private boolean refreshDir() throws IOException, InterruptedException, ExecutionException {
		final Map<File, Future<List<GeneratedImage>>> createdFiles2 = dirWatcher.buildCreatedFiles();
		boolean changed = false;

		for (Map.Entry<File, Future<List<GeneratedImage>>> ent : createdFiles2.entrySet()) {
			final File file = ent.getKey();
			removeAllThatUseThisFile(file);
			final Future<List<GeneratedImage>> future = ent.getValue();
			final SimpleLine2 simpleLine = new SimpleLine2(file, null, future);
			currentDirectoryListing2.add(simpleLine);
			changed = true;
		}

		for (SimpleLine2 line : new ArrayList<SimpleLine2>(currentDirectoryListing2)) {
			if (line.pendingAndFinished()) {
				currentDirectoryListing2.remove(line);
				changed = true;
				final Future<List<GeneratedImage>> future = line.getFuture();
				for (GeneratedImage im : future.get()) {
					mayRefreshImageWindow(im.getPngFile());
					final SimpleLine2 simpleLine = new SimpleLine2(line.getFile(), im, null);
					currentDirectoryListing2.add(simpleLine);
				}
			}
		}
		Collections.sort(currentDirectoryListing2);
		return changed;
	}

	private void removeAllThatUseThisFile(File file) {
		for (final Iterator<SimpleLine2> it = currentDirectoryListing2.iterator(); it.hasNext();) {
			final SimpleLine2 line = it.next();
			if (line.getFile().equals(file)) {
				it.remove();
			}
		}
	}

	private void mayRefreshImageWindow(File pngFile) {
		for (ImageWindow2 win : openWindows2) {
			if (pngFile.equals(win.getSimpleLine().getGeneratedImage().getPngFile())) {
				win.refreshImage();
			}
		}

	}

	public void closing(ImageWindow2 imageWindow) {
		final boolean ok = openWindows2.remove(imageWindow);
		if (ok == false) {
			throw new IllegalStateException();
		}
	}

}
