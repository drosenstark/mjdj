/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.mjdj.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import com.confusionists.mjdjApi.midi.MessageWrapper;
import com.confusionists.mjdjApi.midiDevice.DeviceUnavailableException;
import com.confusionists.mjdjApi.midiDevice.ReceiverDeviceWrapper;
import com.confusionists.mjdjApi.util.MjdjService;

public class ReceiverWrapperImpl extends MidiDeviceWrapperImpl implements ReceiverDeviceWrapper  {

    private Receiver receiver;

    public ReceiverWrapperImpl(MidiDevice device) {
        super(device);
    }
    
    @Override
	public void setService(MjdjService service) {    }

    public void send(MessageWrapper message) {
        receiver.send(message.getMessage(), device.getMicrosecondPosition()); // I have tried everything but nothing works, -1 is just as good as anything
    }

    @Override
	public void close() {
        receiver.close();
        super.close();
    }

	@Override
	public void open() throws DeviceUnavailableException {
		try {
			if (device == null)
				return;
			if (!device.isOpen()) {
				device.open();
			}
			receiver = device.getReceiver();
		} catch (MidiUnavailableException mex) {
			throw new DeviceUnavailableException(mex);

		}
	}

	@Override
	public void toggleUi() {
		
	}



}
