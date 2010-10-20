/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.mjdj.midi.time;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.confusionists.mjdj.Universe;
import com.confusionists.mjdj.ui.Logger;

public class ClockHandler {

	private long lastTick = -1;
	private int ticks = -1;
	public long differenceInMs = -1;
	// these values are used to make sure that we're not following noise in
	// clock differences
	private long newDifferenceInMs = -1;
	private float beatsPerMinute = -1;
	private boolean suspendFlatlineTimer = false; // flatline timer is suspended

	private List<BeatLockedTimerTask> tasks = new ArrayList<BeatLockedTimerTask>();

	public ClockHandler() {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				checkFlatline();
			}
		};

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(task, 400, 400);
	}

	public void onClock() {
		suspendFlatlineTimer = true;
		long thisTick = System.currentTimeMillis();
		if (lastTick == -1) {
			lastTick = System.currentTimeMillis();
		} else if (ticks == 23) {
			long diff = (thisTick - lastTick) - 1;
			boolean acceptNewDiff = false; 
			// if the value varies widely just
			// once, we ignore it...newDifference is the last diff
			if (differenceInMs == -1) {
				acceptNewDiff = true;
			} else if (diff != differenceInMs && diff == newDifferenceInMs) {
				acceptNewDiff = true;
			}
			newDifferenceInMs = diff;
			if (acceptNewDiff) {
				differenceInMs = diff;
				float newBeatsPerMinute = 60000 / newDifferenceInMs;
				if (newBeatsPerMinute != beatsPerMinute) {
					beatsPerMinute = newBeatsPerMinute;
					Universe.instance.syncButton.setText(beatsPerMinute + "bpm");
					Logger.log("Synced to host tempo: " + beatsPerMinute + "bpm");
				}
			}

			lastTick = System.currentTimeMillis();
			ticks = -1;
			Universe.instance.syncButton.pulse(); // this must come before we set the text

			// copy Tasks is just to avoid concurrently modifying the collection
			// tasks
			for (BeatLockedTimerTask task : tasks.toArray(new BeatLockedTimerTask[tasks.size()])) {
				task.onBeat();
				if (task.isDead())
					tasks.remove(task);
			}
			
			Universe.instance.midiDriverManager.onBeat();
			
		} else if (ticks == 5) {
			Universe.instance.syncButton.hideBorder();
		}

		ticks++;
		suspendFlatlineTimer = false;
	}

	public void checkFlatline() {
		if (suspendFlatlineTimer || beatsPerMinute == -1)
			return;

		if (System.currentTimeMillis() - lastTick > 1000)
			onFlatline();
	}

	public void onFlatline() {
		if (beatsPerMinute == -1 && tasks.size() == 0)
			return; // initial startup
		Logger.log("Lost sync to host, clearing " + tasks.size() + " BeatLocked tasks.");
		Universe.instance.syncButton.reset();
		beatsPerMinute = -1;
		differenceInMs = -1;
		newDifferenceInMs = -1;
		ticks = -1;
		lastTick = -1;
		tasks.clear(); // those that have already hit their beat before will
		// fire, but then cannot reschedule. Others will be
		// killed immediately.
	}

	public void sync() {
		ticks = 0;
		lastTick = System.currentTimeMillis();
	}

	private long getMsSinceLastBeat() {
		return System.currentTimeMillis() - lastTick;
	}

	public float getSinceLastBeat() {
		return (float) getMsSinceLastBeat() / (float) differenceInMs;
	}

	/**
	 * @param task
	 * @return true if scheduled, returns false if task cannot be scheduled
	 *         (because there's no clock)
	 */
	public boolean pleaseCallMeOnEachBeat(BeatLockedTimerTask task) {
		if (differenceInMs == -1)
			return false;

		tasks.add(task);
		return true;
	}

	public void diagnose() {
		Logger.log("ClockHandler checking in, BPM is at " + beatsPerMinute + " and we are holding " + tasks.size() + " tasks for firing.");

	}
}
