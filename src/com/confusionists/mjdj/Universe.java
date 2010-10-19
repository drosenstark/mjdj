/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
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
