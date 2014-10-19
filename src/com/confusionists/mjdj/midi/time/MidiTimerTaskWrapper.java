package com.confusionists.mjdj.midi.time;

import com.confusionists.mjdjApi.util.MidiTimerTask;

import java.util.TimerTask;

/**
 * Created by dr2050 on 10/19/14.
 */
public class MidiTimerTaskWrapper extends TimerTask {

    MidiTimerTask task;

    public MidiTimerTaskWrapper(MidiTimerTask task) {
        this.task = task;
    }

    @Override
    public void run() {
        this.task.run();
    }
}
