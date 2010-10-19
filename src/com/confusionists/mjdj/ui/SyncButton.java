/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.mjdj.ui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import com.confusionists.mjdj.Universe;


public class SyncButton extends JButton {
	
	private static final long serialVersionUID = 4864746506655184898L;
	private static final String INITIAL_LABEL = "Sync";

	Timer timer;
	
	public SyncButton() {
		super(INITIAL_LABEL);
		this.setBorder(BorderFactory.createBevelBorder(0, Color.RED, Color.BLACK));
		this.setBorderPainted(false);
	}
	
	public void pulse() {
		setBorderPainted(true);
	}
	
	public void hideBorder() {
		setBorderPainted(false);
	}

	public void reset() {
		setText(INITIAL_LABEL);
		hideBorder();
	}
	
	@Override
	protected void processMouseEvent(MouseEvent e) {
		super.processMouseEvent(e);
		if (e.getButton()!=MouseEvent.NOBUTTON)
			Universe.instance.clockHandler.sync();
	}
	
	
}
