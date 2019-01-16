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
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;

import net.sourceforge.plantuml.version.PSystemVersion;
import net.sourceforge.plantuml.version.Version;

class AboutWindow extends JFrame {

	/*
	 * - the PlantUML version - the Dot version - the PlantUML authors - the PlantUML license
	 */
	public AboutWindow() {
		super();
		setIconImage(PSystemVersion.getPlantumlSmallIcon2());

		this.setTitle("About PlantUML (" + Version.versionString() + ")");

		final JPanel panel1 = new JPanel(new GridLayout(2, 1));
		panel1.add(getInfoVersion());
		panel1.add(getInfoAuthors());

		getContentPane().add(getNorthLabel(), BorderLayout.NORTH);
		getContentPane().add(panel1, BorderLayout.CENTER);
		getContentPane().add(getSouthLabel(), BorderLayout.SOUTH);

		pack();
		this.setLocationRelativeTo(this.getParent());
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	private JComponent getNorthLabel() {
		final JLabel text = new JLabel("PlantUML (" + Version.versionString() + ")");
		final Font font = text.getFont().deriveFont(Font.BOLD, (float) 20.0);
		text.setFont(font);
		final JPanel ptext = new JPanel();
		ptext.add(text);

		final JLabel icon = new JLabel(new ImageIcon(PSystemVersion.getPlantumlImage()));

		final JPanel result = new JPanel(new BorderLayout());
		result.add(ptext, BorderLayout.CENTER);
		result.add(icon, BorderLayout.EAST);

		return result;
	}

	private JComponent getSouthLabel() {
		final JPanel result = new JPanel();
		final JButton license = new JButton("License");
		license.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new LicenseWindow();
			}
		});
		final JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});
		result.add(license);
		result.add(ok);
		return result;
	}

	private JComponent getInfoVersion() {
		final PSystemVersion p1 = PSystemVersion.createShowVersion();
		return getJComponent(skip(p1.getLines()));
	}

	private JComponent getInfoAuthors() {
		final PSystemVersion p1 = PSystemVersion.createShowAuthors();
		return getJComponent(skip(p1.getLines()));
	}

	private List<String> skip(List<String> lines) {
		return lines.subList(2, lines.size());
	}

	private JComponent getJComponent(List<String> lines) {
		final StringBuilder sb = new StringBuilder("<html>");
		for (String s : lines) {
			sb.append(s + "</b></i></u>");
			sb.append("<br>");
		}
		sb.append("</html>");
		final JEditorPane text = new JEditorPane("text/html", sb.toString());
		text.setEditable(false);
		CompoundBorder border = new CompoundBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED),
				BorderFactory.createEmptyBorder(5, 5, 5, 5));
		border = new CompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, getBackground()), border);
		text.setBorder(border);

		return text;
	}

	public static void main(String arg[]) {
		new AboutWindow();
	}

}
