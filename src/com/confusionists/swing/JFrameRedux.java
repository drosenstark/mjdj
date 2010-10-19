/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.swing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JFrameRedux extends JFrame implements WindowListener, WindowFocusListener, WindowStateListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4365460923289361895L;
	protected JMenuBar jMenuBar1 = new JMenuBar();
	protected JMenu jMenuFile = new JMenu();
	protected JMenuItem jMenuFileExit = new JMenuItem();
	protected JMenu jMenuView = new JMenu();


	public JFrameRedux() {
		this.addWindowListener(this);
		this.addWindowStateListener(this);
		this.addWindowFocusListener(this);
		SwingOps.setLookAndFeel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(this);

		jMenuFile.setText("File");
		jMenuFileExit.setText("Exit");
		jMenuFileExit.addActionListener(new ActionListener()  {
			public void actionPerformed(ActionEvent e) {
				jMenuFileExit_actionPerformed(e);
			}
		});


		jMenuView.setText("View");
		UIManager.LookAndFeelInfo info[] = UIManager.getInstalledLookAndFeels();
		for (int x=0; x<info.length; x++) {
			final String className = info[x].getClassName();
			final String name;

			if (className.equals(UIManager.getSystemLookAndFeelClassName()))
				name = info[x].getName() + " (Default)";
			else
				name = info[x].getName();
			JMenuItem item = new JMenuItem(name);
			item.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateLookAndFeel(className);
				}
			});
			jMenuView.add(item);
		}

		jMenuFile.add(jMenuFileExit);
		jMenuBar1.add(jMenuFile);
		jMenuBar1.add(jMenuView);

		this.setJMenuBar(jMenuBar1);
		SwingUtilities.updateComponentTreeUI ( this ) ;		

	}

	protected void updateLookAndFeel(String className) {
		try {
			UIManager.setLookAndFeel(className);
			update(getGraphics());
			SwingUtilities.updateComponentTreeUI(this);
			Dimension size = getSize();
			setSize(size);
		} catch (Exception e2) {
			throw new RuntimeException(e2);
		}
	}
	
	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}


	/**File | Exit action performed*/
	public void jMenuFileExit_actionPerformed(ActionEvent e) {
		System.exit(0);
	}

	/**Overridden so we can exit when window is closed*/
	@Override
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			jMenuFileExit_actionPerformed(null);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void windowGainedFocus(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowLostFocus(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowStateChanged(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}



}
