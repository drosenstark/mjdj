/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.mjdj.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;

import com.confusionists.mjdj.midi.time.InternalClock;


@SuppressWarnings("serial")
public class InternalClockUi extends JFrame {
	
	static final int TOP_SPEED = 250;
	static final int BOTTOM_SPEED = 50;
	InternalClock clock;
	
	JComboBox box = new JComboBox();
	public InternalClockUi(InternalClock clock) {
		this.setTitle("Set BPM for Internal Clock");
		this.clock = clock;
		String[] choices = new String[TOP_SPEED-BOTTOM_SPEED];
		for (int i=0; i<choices.length; i++) {
			choices[i] = (BOTTOM_SPEED + i) + "";
		}
		box = new JComboBox(choices);
		box.setSelectedIndex(clock.bpm - BOTTOM_SPEED);
		box.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				InternalClockUi.this.onComboChanged();
			}
		});
		
		this.add(box);
		setSize(new Dimension(300, 100));
		setAlwaysOnTop(true);
		setVisible(true);
		
		
	}
	
	public void onComboChanged() {
		clock.resetBpm((Integer.parseInt((String)box.getSelectedItem())));
		
	}

}
