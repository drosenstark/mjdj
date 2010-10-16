package com.confusionists.mjdj;

import java.util.Timer;

import com.confusionists.mjdj.midi.MidiDriverManager;
import com.confusionists.mjdj.midi.time.ClockHandler;
import com.confusionists.mjdj.ui.SyncButton;

public class Universe {
	
	public static final Universe instance = new Universe();
	public MidiDriverManager midiDriverManager = new MidiDriverManager();
	// Empirically I've found that it's a bad idea to make more than one Timer instance
	public final Timer centralTimer = new Timer();
	public ClockHandler clockHandler = new ClockHandler();
	public Main main = null;
	public SyncButton syncButton = new SyncButton();
	
	private Universe() {}
	
	public boolean isDebug() {
		return main.debugToggle.isSelected();
	}
}
