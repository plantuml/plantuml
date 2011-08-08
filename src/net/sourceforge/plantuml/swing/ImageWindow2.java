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
 * Revision $Revision: 5885 $
 *
 */
package net.sourceforge.plantuml.swing;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.WindowConstants;

import net.sourceforge.plantuml.GeneratedImage;

class ImageWindow2 extends JFrame {

	private SimpleLine2 simpleLine2;
	final private JScrollPane scrollPane;
	private final JButton next = new JButton("Next");
	private final JButton previous = new JButton("Previous");
	private final ListModel listModel;
	private int index;

	public ImageWindow2(SimpleLine2 simpleLine, final MainWindow2 main, ListModel listModel, int index) {
		super(simpleLine.toString());
		this.simpleLine2 = simpleLine;
		this.listModel = listModel;
		this.index = index;

		final JPanel north = new JPanel();
		north.add(previous);
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
