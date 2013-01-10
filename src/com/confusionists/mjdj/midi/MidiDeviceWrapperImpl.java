/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.mjdj.midi;

import java.util.ArrayList;

import javax.sound.midi.MidiDevice;

import com.confusionists.mjdjApi.midiDevice.DeviceUnavailableException;
import com.confusionists.mjdjApi.midiDevice.DeviceWrapper;
import com.confusionists.mjdjApi.util.MjdjService;

public abstract class MidiDeviceWrapperImpl implements DeviceWrapper  {

    private String name;
    public MidiDevice device;
    private MjdjService service;
    private boolean active = false;

    private String truncate(String name, boolean truncateBeginning, int maxLength) {
    		if (name.length() > maxLength) {
    			if (!truncateBeginning) {
    				name = name.substring(0, maxLength);
    				return name + "É";
    			} else {
    				int startIndex = name.length() - maxLength;
    				name = name.substring(startIndex, name.length() - startIndex);
    				return "É" + name;
    			}
    			
    		} else
    			return name;
    	
    }
    public MidiDeviceWrapperImpl(MidiDevice device) {
    		if (device == null)
    			return;
        MidiDevice.Info info = device.getDeviceInfo();
        this.device = device;
        String leftName = truncate(info.getVendor(), false, 40);
        String rightName = truncate(info.getName(), true, 40);
        setName(leftName + " - " + rightName);
    }

	@Override
	public void setService(MjdjService service) {
		this.service = service;
	}
	
	@Override
	public MjdjService getService() {
		return service;
	}
    
    /* (non-Javadoc)
	 * @see com.confusionists.mjdj.midi.DeviceWrapper#setActive(boolean)
	 */
    @Override
	public void setActive(boolean active) {
        this.active = active;
    }

    /* (non-Javadoc)
	 * @see com.confusionists.mjdj.midi.DeviceWrapper#isActive()
	 */
    @Override
	public boolean isActive() {
        return active;
    }

    /* (non-Javadoc)
	 * @see com.confusionists.mjdj.midi.DeviceWrapper#close()
	 */
    @Override
	public void close() {
    		if (device == null) return;
        if (device.isOpen()) {
            //device.close();
        }
    }

    /* (non-Javadoc)
	 * @see com.confusionists.mjdj.midi.DeviceWrapper#open()
	 */
    @Override
	public abstract void open() throws DeviceUnavailableException;

    /* (non-Javadoc)
	 * @see com.confusionists.mjdj.midi.DeviceWrapper#toString()
	 */
    @Override
    public String toString() {
        return name;
    }

    /* (non-Javadoc)
	 * @see com.confusionists.mjdj.midi.DeviceWrapper#getName()
	 */
    @Override
	public String getName() {
        return name;
    }

    /* (non-Javadoc)
	 * @see com.confusionists.mjdj.midi.DeviceWrapper#setName(java.lang.String)
	 */
    public void setName(String name) {
        this.name = name;
    }
    
    
    private boolean nameExistsInList(String name, ArrayList<String> existingIds) {
    		for (String existingId : existingIds) {
    			if (existingId.equals(name))
    				return true;
    		}
    		
    		return false;
    		
    }
	@Override
	public void makeNewId(ArrayList<String> existingIds) {
		int i=2;
		String tryThis = this.toString();
		while (this.nameExistsInList(tryThis, existingIds)) {
			tryThis = this.toString() + (i++);
		}

		this.name = tryThis;
		
	}
    
    
}
