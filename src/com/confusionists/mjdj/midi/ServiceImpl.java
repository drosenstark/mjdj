/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
 */
package com.confusionists.mjdj.midi;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;

import com.confusionists.mjdj.Universe;
import com.confusionists.mjdj.keystrokes.Keyboard;
import com.confusionists.mjdj.midi.time.BeatLockedTimerTask;
import com.confusionists.mjdj.ui.Logger;
import com.confusionists.mjdj.ui.MorphCheckbox;
import com.confusionists.mjdj.ui.MorphCheckboxList;
import com.confusionists.mjdjApi.midi.MessageWrapper;
import com.confusionists.mjdjApi.midiDevice.DeviceWrapper;
import com.confusionists.mjdjApi.midiDevice.ReceiverDeviceWrapper;
import com.confusionists.mjdjApi.morph.Morph;
import com.confusionists.mjdjApi.util.MidiTimerTask;
import com.confusionists.mjdjApi.util.MjdjService;

public class ServiceImpl implements MjdjService {

	public static ServiceImpl instance = new ServiceImpl();

	public void morph(MessageWrapper msgWrapper, String from) {
		Universe.instance.midiDriverManager.sendToMorphs(msgWrapper, from);
	}

	public void morph(MessageWrapper message, String from, Morph avoidMorph) {
		Universe.instance.midiDriverManager.sendToMorphs(message, from, avoidMorph);
	}

	public void send(MessageWrapper message) {
		send(message, (String) null);
	}

	public void send(MessageWrapper message, String to) {
		List<String> tos = null;
		if (to != null) {
			tos = new ArrayList<String>();
			tos.add(to);
		}
		send(message, tos);
	}

	/* if to is null send to all */
	public void send(MessageWrapper message, List<String> to) {
		for (DeviceWrapper device : Universe.instance.midiDriverManager.outputReceivers) {
			ReceiverDeviceWrapper receiver = (ReceiverDeviceWrapper) device;
			if (receiver.isActive() && (to == null || to.contains(receiver.toString()))) {
				receiver.send(message);
				Logger.log(message, receiver);
			}
		}
	}

	public void send(byte[] bytes) throws InvalidMidiDataException {
		send(bytes, null);
	}

	public void send(byte[] bytes, List<String> to) throws InvalidMidiDataException {
		send(MessageWrapper.newInstance(bytes), to);
	}

	public void debugLog(String text) {
		Logger.debugLog(text);
	}

	public void debugLog(String text, Exception e) {
		Logger.debugLog(text, e);
	}

	public void log(String text) {
		Logger.log(text);
	}

	public void log(String string, Exception e) {
		Logger.log(string, e);

	}

	public boolean isMorphActive(String name) {
		boolean retVal = false;
		MorphCheckbox box = MorphCheckboxList.instance.getCheckbox(name);
		if (box == null) {
			Logger.log("Error getting status (returning false)! Morpher not found " + name);
		} else {
			retVal = box.isSelected();
		}
		return retVal;
	}

	public void setMorphActive(String name, boolean status) {
		MorphCheckbox box = MorphCheckboxList.instance.getCheckbox(name);
		if (box == null) {
			Logger.log("Error setting status! Morpher not found " + name);
		} else {
			if (box.isSelected() != status) {
				box.doClick();
			}
		}
	}

	public void sendKeystrokes(String keystrokes) {
		try {
			System.out.println("Mjdj sending keystrokes " + keystrokes);
			new Keyboard().type(keystrokes);
		} catch (Exception e) {
			Logger.log("Error sending keystrokes " + keystrokes, e);
		}

	}

	public boolean schedule(MidiTimerTask task, int beatsBeforeLaunch) {
		return new BeatLockedTimerTask().setTask(task, beatsBeforeLaunch);
	}

	public boolean schedule(MidiTimerTask task, int beatsBeforeLaunch, float delayAfterBeat) {
		if (!(delayAfterBeat < 1 || delayAfterBeat < 0)) {
			log(task.getMorph().getName()
					+ " is trying to schedule something with a delayAfterBeat greater than 1 or less than zero. Task not scheduled.");
			return false;
		}
		return new BeatLockedTimerTask().setTask(task, beatsBeforeLaunch, delayAfterBeat);
	}

	public boolean scheduleInMs(MidiTimerTask task, int delay) {
		Universe.instance.centralTimer.schedule(task, delay);
		return true;
	}

	public float getAfterBeat() {
		return Universe.instance.clockHandler.getSinceLastBeat();

	}

	public boolean isDebug() {
		return Universe.instance.isDebug();
	}

	public int compareMorphToMorph(Morph one, Morph two) {
		int indexOne = MorphCheckboxList.instance.getMorphs().indexOf(one);
		int indexTwo = MorphCheckboxList.instance.getMorphs().indexOf(two);
		if (indexOne > indexTwo)
			return 1;
		if (indexOne < indexTwo)
			return -1;
		return 0;
	}

}
