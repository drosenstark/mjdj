package com.confusionists.mjdj.midi.time;

import java.util.Timer;
import java.util.TimerTask;

import com.confusionists.mjdj.Universe;
import com.confusionists.mjdjApi.midiDevice.AbstractTransmitterDeviceWrapper;
import com.confusionists.mjdjApi.midiDevice.DeviceUnavailableException;

public class InternalClock extends AbstractTransmitterDeviceWrapper {
	
	Timer timer;

	@Override
	public void close() {
		timer.cancel();
	}

	@Override
	public String getName() {
		return "Internal Clock (@125bpm)";
	}

	@Override
	public void open() throws DeviceUnavailableException {
		long ms = 1000 / (125 / 60 * 24);
		System.out.println("scheduling one every bpm " + ms);
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				InternalClock.this.sendBeat();
			}
		}, 0, ms);

	}

	protected void sendBeat() {
		if (!isActive() || !isClockSource())
			return;
		Universe.instance.clockHandler.onClock(); // internally, we MUST call this directly, there is no other way in
		/*

		built-in OSX drivers don't allow this: not surprised, really
		service.send(ShortMessageWrapper.newInstance(message));
			
		try {
			ShortMessage message = new ShortMessage();
			message.setMessage(ShortMessage.TIMING_CLOCK);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		*/
		
	}
	
	public static void main(String[] args) {
		byte[] messageBytes = { (byte)0x0fe };
		int firstByte = messageBytes[0] & 0xff;
		if (firstByte == 0xfe || firstByte == 0xf8) {
			System.out.println("good");
		}
		
	}
	
	

}
