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
