package com.confusionists.mjdj.midi.time;

import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.ShortMessage;

import com.confusionists.mjdj.Universe;
import com.confusionists.mjdj.ui.InternalClockUi;
import com.confusionists.mjdjApi.midi.ShortMessageWrapper;
import com.confusionists.mjdjApi.midiDevice.AbstractTransmitterDeviceWrapper;
import com.confusionists.mjdjApi.midiDevice.DeviceUnavailableException;

/**
 * This clock is for diagnostic purposes only
 * @author DanielRosenstark [at_sign] confusionists.com
 *
 */
public class InternalClock extends AbstractTransmitterDeviceWrapper {
	
	Timer timer;
	private int ticks = 0;
	public int bpm  = 120;
	private InternalClockUi ui;

	@Override
	public void close() {
		if (timer != null)
		timer.cancel(); // idempotent
	}

	@Override
	public String getName() {
		return "Internal Clock";
	}

	@Override
	public void open() throws DeviceUnavailableException {
	}
	
	public void resetBpm(int bpm) {
		this.bpm = bpm;
		setActive(isActive());
	}
	
	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (active) 
			start();
	}
	
	private void start() {
		close(); // kill any old timers
		float bpm = (float)this.bpm;

		// milliseconds are too imprecise to do this, so we use a small number
		long smallTickMs = 1; // this needs to be much finer than (1000f/(bpm/60f))/24f)  since the timers do not fire each other
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				if (ticks < 23) {
					InternalClock.this.sendBeat();
					ticks++;
				}
			}
		}, 0, smallTickMs);
		
		
		long bigTickMs = Math.round(1000 / (bpm / 60));
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
					InternalClock.this.sendBeat();
					ticks = 0;
			}
		}, smallTickMs, bigTickMs);
	}

	protected void sendBeat() {
		if (!isActive() || !isClockSource())
			return;
		Universe.instance.clockHandler.onClock(); // internally, we MUST call this directly, there is no other way in
		try {
			ShortMessage message = new ShortMessage();
			message.setMessage(ShortMessage.TIMING_CLOCK);
			// TODO: allow choosing ports for this
			service.send(ShortMessageWrapper.newInstance(message));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Override
	public void toggleUi() {
		super.toggleUi();
		if (ui == null)
			ui = new InternalClockUi(this);
		else
			ui.setVisible(true);
		
	}
	
	
	

}
