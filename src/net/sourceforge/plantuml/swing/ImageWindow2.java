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
package net.sourceforge.plantuml.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.ImageSelection;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.version.PSystemVersion;

class ImageWindow2 extends JFrame {

	private final static Preferences prefs = Preferences.userNodeForPackage(ImageWindow2.class);
	private final static String KEY_ZOOM_FIT = "zoomfit";
	private final static String KEY_WIDTH_FIT = "widthfit";

	private SimpleLine2 simpleLine2;
	private final JScrollPane scrollPane;
	private final JButton next = new JButton("Next");
	private final JButton copy = new JButton("Copy");
	private final JButton previous = new JButton("Previous");
	private final JCheckBox zoomFitButt = new JCheckBox("Zoom fit");
	private final JCheckBox widthFitButt = new JCheckBox("Width fit");
	private final JButton zoomMore = new JButton("+");
	private final JButton zoomLess = new JButton("-");
	private final MainWindow2 main;

	private final ListModel listModel;
	private int index;
	private int zoomFactor = 0;

	private enum SizeMode {
		FULL_SIZE, ZOOM_FIT, WIDTH_FIT
	};

	private SizeMode sizeMode = SizeMode.FULL_SIZE;

	private int startX, startY;

	public ImageWindow2(SimpleLine2 simpleLine, final MainWindow2 main, ListModel listModel, int index) {
		super(simpleLine.toString());
		setIconImage(PSystemVersion.getPlantumlSmallIcon2());
		this.simpleLine2 = simpleLine;
		this.listModel = listModel;
		this.index = index;
		this.main = main;

		final JPanel north = new JPanel();
		north.add(previous);
		north.add(copy);
		north.add(next);
		north.add(zoomFitButt);
		north.add(widthFitButt);
		north.add(zoomMore);
		north.add(zoomLess);
		copy.setFocusable(false);
		copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				copy();
			}
		});
		next.setFocusable(false);
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				next();
			}
		});
		previous.setFocusable(false);
		previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				previous();
			}
		});
		zoomFitButt.setFocusable(false);
		zoomFitButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				widthFitButt.setSelected(false);
				zoomFit();
			}
		});
		widthFitButt.setFocusable(false);
		widthFitButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				zoomFitButt.setSelected(false);
				zoomFit();
			}
		});
		zoomMore.setFocusable(false);
		zoomMore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				zoomFactor++;
				refreshImage(false);
			}
		});
		zoomLess.setFocusable(false);
		zoomLess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				zoomFactor--;
				refreshImage(false);
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

		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentResized(java.awt.event.ComponentEvent e) {
				super.componentResized(e);
				refreshImage(false);
			}
		});

		final boolean zoomChecked = prefs.getBoolean(KEY_ZOOM_FIT, false);
		zoomFitButt.setSelected(zoomChecked);
		if (zoomChecked) {
			sizeMode = SizeMode.ZOOM_FIT;
		}
		final boolean widthZoomChecked = prefs.getBoolean(KEY_WIDTH_FIT, false);
		widthFitButt.setSelected(widthZoomChecked);
		if (widthZoomChecked) {
			sizeMode = SizeMode.WIDTH_FIT;
		}

		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_RIGHT) {
					next();
				} else if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_LEFT) {
					previous();
				} else if (evt.isAltDown() && evt.getKeyCode() == KeyEvent.VK_RIGHT) {
					next();
				} else if (evt.isAltDown() && evt.getKeyCode() == KeyEvent.VK_LEFT) {
					previous();
				} else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
					imageRight();
				} else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
					imageLeft();
				} else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
					imageDown();
				} else if (evt.getKeyCode() == KeyEvent.VK_UP) {
					imageUp();
				} else if (evt.getKeyCode() == KeyEvent.VK_C) {
					copy();
				} else if (evt.getKeyCode() == KeyEvent.VK_Z) {
					zoomFitButt.setSelected(!zoomFitButt.isSelected());
					zoomFit();
				}
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

	private void imageDown() {
		final JScrollBar bar = scrollPane.getVerticalScrollBar();
		bar.setValue(bar.getValue() + bar.getBlockIncrement());
	}

	private void imageUp() {
		final JScrollBar bar = scrollPane.getVerticalScrollBar();
		bar.setValue(bar.getValue() - bar.getBlockIncrement());
	}

	private void imageLeft() {
		final JScrollBar bar = scrollPane.getHorizontalScrollBar();
		bar.setValue(bar.getValue() - bar.getBlockIncrement());
	}

	private void imageRight() {
		final JScrollBar bar = scrollPane.getHorizontalScrollBar();
		bar.setValue(bar.getValue() + bar.getBlockIncrement());
	}

	private void zoomFit() {
		final boolean selectedZoom = zoomFitButt.isSelected();
		final boolean selectedWidth = widthFitButt.isSelected();
		prefs.putBoolean(KEY_ZOOM_FIT, selectedZoom);
		prefs.putBoolean(KEY_WIDTH_FIT, selectedWidth);
		zoomFactor = 0;
		if (selectedZoom) {
			sizeMode = SizeMode.ZOOM_FIT;
		} else if (selectedWidth) {
			sizeMode = SizeMode.WIDTH_FIT;
		} else {
			sizeMode = SizeMode.FULL_SIZE;
		}
		refreshImage(false);
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
		refreshImage(false);
	}

	private void refreshSimpleLine() {
		for (SimpleLine2 line : main.getCurrentDirectoryListing2()) {
			if (line.getFile().equals(simpleLine2.getFile())) {
				simpleLine2 = line;
				setTitle(simpleLine2.toString());
			}
		}
	}

	private ScrollablePicture buildScrollablePicture() {
		final GeneratedImage generatedImage = simpleLine2.getGeneratedImage();
		if (generatedImage == null) {
			return null;
		}
		final File png = generatedImage.getPngFile();
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(png.getAbsolutePath()));
			if (sizeMode == SizeMode.ZOOM_FIT) {
				final Dimension imageDim = new Dimension(image.getWidth(), image.getHeight());
				final Dimension newImgDim = ImageHelper
						.getScaledDimension(imageDim, scrollPane.getViewport().getSize());
				image = ImageHelper.getScaledInstance(image, newImgDim, getHints(), true);
			} else if (sizeMode == SizeMode.WIDTH_FIT) {
				final Dimension imageDim = new Dimension(image.getWidth(), image.getHeight());
				final Dimension newImgDim = ImageHelper.getScaledDimensionWidthFit(imageDim, scrollPane.getViewport()
						.getSize());
				image = ImageHelper.getScaledInstance(image, newImgDim, getHints(), false);
			} else if (zoomFactor != 0) {
				final Dimension imageDim = new Dimension(image.getWidth(), image.getHeight());
				final Dimension newImgDim = ImageHelper.getScaledDimension(imageDim, getZoom());
				image = ImageHelper.getScaledInstance(image, newImgDim, getHints(), false);
			}
		} catch (IOException ex) {
			final String msg = "Error reading file: " + ex.toString();
			final TextBlockBackcolored error = GraphicStrings.createForError(Arrays.asList(msg), false);
			final ImageBuilder imageBuilder = ImageBuilder.buildA(new ColorMapperIdentity(),
					false, null, null, null, 1.0, error.getBackcolor());
			imageBuilder.setUDrawable(error);
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				imageBuilder.writeImageTOBEMOVED(new FileFormatOption(FileFormat.PNG), 42, baos);
				baos.close();
				image = ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		final ImageIcon imageIcon = new ImageIcon(image, simpleLine2.toString());
		final ScrollablePicture scrollablePicture = new ScrollablePicture(imageIcon, 1);

		scrollablePicture.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				super.mousePressed(me);
				startX = me.getX();
				startY = me.getY();
			}
		});
		scrollablePicture.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent me) {
				super.mouseDragged(me);
				final int diffX = me.getX() - startX;
				final int diffY = me.getY() - startY;

				final JScrollBar hbar = scrollPane.getHorizontalScrollBar();
				hbar.setValue(hbar.getValue() - diffX);
				final JScrollBar vbar = scrollPane.getVerticalScrollBar();
				vbar.setValue(vbar.getValue() - diffY);
			}
		});

		return scrollablePicture;
	}

	private RenderingHints getHints() {
		final RenderingHints hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		return hints;
	}

	private double getZoom() {
		// if (zoomFactor <= -10) {
		// return 0.05;
		// }
		// return 1.0 + zoomFactor / 10.0;
		return Math.pow(1.1, zoomFactor);
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

	private int v1;
	private int v2;

	public void refreshImage(boolean external) {
		final JScrollBar bar1 = scrollPane.getVerticalScrollBar();
		final JScrollBar bar2 = scrollPane.getHorizontalScrollBar();
		if (external && isError() == false) {
			v1 = bar1.getValue();
			v2 = bar2.getValue();
		}
		scrollPane.setViewportView(buildScrollablePicture());
		force();
		if (external) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					refreshSimpleLine();
					if (isError() == false) {
						bar1.setValue(v1);
						bar2.setValue(v2);
					}
				}
			});
		}
	}

	private boolean isError() {
		return simpleLine2.getGeneratedImage() != null && simpleLine2.getGeneratedImage().lineErrorRaw() != -1;

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
