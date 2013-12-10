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
 * Revision $Revision: 5885 $
 *
 */
package net.sourceforge.plantuml.swing;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.WindowConstants;

import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.version.PSystemVersion;

class ImageWindow2 extends JFrame {

	private SimpleLine2 simpleLine2;
	final private JScrollPane scrollPane;
	private final JButton next = new JButton("Next");
	private final JButton copy = new JButton("Copy");
	private final JButton previous = new JButton("Previous");
	private final ListModel listModel;
	private int index;

	public ImageWindow2(SimpleLine2 simpleLine, final MainWindow2 main, ListModel listModel, int index) {
		super(simpleLine.toString());
		setIconImage(PSystemVersion.getPlantumlSmallIcon2());
		this.simpleLine2 = simpleLine;
		this.listModel = listModel;
		this.index = index;

		final JPanel north = new JPanel();
		north.add(previous);
		north.add(copy);
		copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				copy();
			}
		});
		north.add(next);
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				next();
			}
		});
		previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				previous();
			}
		});

		scrollPane = new JScrollPane(buildScrollablePicture());
		getContentPane().add(north, BorderLayout.NORTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		setSize(640, 400);
		this.setLocationRelativeTo(this.getParent());
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				main.closing(ImageWindow2.this);
			}
		});
	}

	private void next() {
		index++;
		updateSimpleLine();
	}

	private void previous() {
		index--;
		updateSimpleLine();
	}

	private void updateSimpleLine() {
		if (index < 0) {
			index = 0;
		}
		if (index > listModel.getSize() - 1) {
			index = listModel.getSize() - 1;
		}
		simpleLine2 = (SimpleLine2) listModel.getElementAt(index);
		setTitle(simpleLine2.toString());
		refreshImage();
	}

	private ScrollablePicture buildScrollablePicture() {
		final GeneratedImage generatedImage = simpleLine2.getGeneratedImage();
		if (generatedImage == null) {
			return null;
		}
		final File png = generatedImage.getPngFile();
		final Image image = Toolkit.getDefaultToolkit().createImage(png.getAbsolutePath());
		final ImageIcon imageIcon = new ImageIcon(image, simpleLine2.toString());
		final ScrollablePicture scrollablePicture = new ScrollablePicture(imageIcon, 1);
		return scrollablePicture;
	}

	private void copy() {
		final GeneratedImage generatedImage = simpleLine2.getGeneratedImage();
		if (generatedImage == null) {
			return;
		}
		final File png = generatedImage.getPngFile();
		final Image image = Toolkit.getDefaultToolkit().createImage(png.getAbsolutePath());
		final ImageSelection imgSel = new ImageSelection(image);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imgSel, null);
	}

	public SimpleLine2 getSimpleLine() {
		return simpleLine2;
	}

	public void refreshImage() {
		scrollPane.setViewportView(buildScrollablePicture());
		force();
	}

	private void force() {
		// setVisible(true);
		repaint();
		// validate();
		// getContentPane().validate();
		// getContentPane().setVisible(true);
		// getContentPane().repaint();
		// scrollPane.validate();
		// scrollPane.setVisible(true);
		// scrollPane.repaint();
	}

}

// This class is used to hold an image while on the clipboard.
class ImageSelection implements Transferable {
	private Image image;

	public ImageSelection(Image image) {
		this.image = image;
	}

	// Returns supported flavors
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { DataFlavor.imageFlavor };
	}

	// Returns true if flavor is supported
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return DataFlavor.imageFlavor.equals(flavor);
	}

	// Returns image
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if (!DataFlavor.imageFlavor.equals(flavor)) {
			throw new UnsupportedFlavorException(flavor);
		}
		return image;
	}
}
