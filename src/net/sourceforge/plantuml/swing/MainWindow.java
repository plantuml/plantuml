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
 * Revision $Revision: 6075 $
 *
 */
package net.sourceforge.plantuml.swing;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
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

import net.sourceforge.plantuml.DirWatcher;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.Option;

public class MainWindow extends JFrame {

	final private static Preferences prefs = Preferences.userNodeForPackage(MainWindow.class);
	final private static String KEY_DIR = "cur";
	final private static String KEY_PATTERN = "pat";

	private final JList jList1 = new JList();
	private final JScrollPane scrollPane;
	private final JButton changeDirButton = new JButton("Change Directory");
	// private final JButton refreshButton = new JButton("Refresh");
	private final JTextField extensions = new JTextField();

	final private List<SimpleLine> currentDirectoryListing = new ArrayList<SimpleLine>();
	final private Set<ImageWindow> openWindows = new HashSet<ImageWindow>();
	final private Option option;

	private DirWatcher dirWatcher;

	public MainWindow(Option option) {
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
	
	private void refreshReloadDir() {
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

	private MainWindow(File dir, Option option) {
		super(dir.getAbsolutePath());
		this.option = option;
		dirWatcher = new DirWatcher(dir, option, getRegexpPattern(getExtensions()));

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

//		final JPanel southSouth = new JPanel(new GridLayout(1, 2));
//		southSouth.add(changeDirButton);
//		southSouth.add(refreshButton);
//		south.add(southSouth, BorderLayout.SOUTH);
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
					doubleClick((SimpleLine) jList1.getModel().getElementAt(index), jList1.getModel(), index);
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
//		refreshButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				refreshReloadDir();
//			}
//		});

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
		dirWatcher = new DirWatcher(dir, option, getRegexpPattern(getExtensions()));
		setTitle(dir.getAbsolutePath());
		Log.info("Creating DirWatcher");
		currentDirectoryListing.clear();
		jList1.setListData(new Vector<SimpleLine>(currentDirectoryListing));
		jList1.setVisible(true);
	}

	private void doubleClick(SimpleLine simpleLine, ListModel listModel, int index) {
		for (ImageWindow win : openWindows) {
			if (win.getSimpleLine().equals(simpleLine)) {
				win.setVisible(true);
				win.setExtendedState(Frame.NORMAL);
				return;
			}
		}
		openWindows.add(new ImageWindow(simpleLine, this, listModel, index));
	}

	private void tick() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					final boolean changed = refreshDir();
					if (changed) {
						jList1.setListData(new Vector<SimpleLine>(currentDirectoryListing));
						jList1.setVisible(true);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private boolean refreshDir() throws IOException, InterruptedException {
		final Collection<GeneratedImage> createdFiles2 = dirWatcher.buildCreatedFiles();

		boolean changed = false;

		for (GeneratedImage g : createdFiles2) {
			final SimpleLine simpleLine = new SimpleLine(g);
			mayRefreshImageWindow(g.getPngFile());
			if (currentDirectoryListing.contains(simpleLine) == false) {
				removeAllThatUseThisFile(g.getPngFile());
				currentDirectoryListing.add(simpleLine);
				changed = true;
			}
		}
		for (final Iterator<SimpleLine> it = currentDirectoryListing.iterator(); it.hasNext();) {
			final SimpleLine s = it.next();
			if (s.exists() == false) {
				it.remove();
				changed = true;
			}

		}
		Collections.sort(currentDirectoryListing);
		return changed;
	}

	private void removeAllThatUseThisFile(File pngFile) {
		for (final Iterator<SimpleLine> it = currentDirectoryListing.iterator(); it.hasNext();) {
			final SimpleLine line = it.next();
			if (line.getGeneratedImage().getPngFile().equals(pngFile)) {
				it.remove();
			}

		}

	}

	private void mayRefreshImageWindow(File pngFile) {
		for (ImageWindow win : openWindows) {
			if (pngFile.equals(win.getSimpleLine().getGeneratedImage().getPngFile())) {
				win.refreshImage();
			}
		}

	}

	public void closing(ImageWindow imageWindow) {
		final boolean ok = openWindows.remove(imageWindow);
		if (ok == false) {
			throw new IllegalStateException();
		}
	}

}
