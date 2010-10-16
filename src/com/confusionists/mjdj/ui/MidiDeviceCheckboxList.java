/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

Linking this library statically or dynamically with other modules is making a combined work based on this library. Thus, the terms and conditions of the GNU General Public License cover the whole combination. 

As a special exception, the copyright holders of this library give you permission to link this library with independent modules to produce an executable, regardless of the license terms of these independent modules, and to copy and distribute the resulting executable under terms of your choice, provided that you also meet, for each linked independent module, the terms and conditions of the license of that module. An independent module is a module which is not derived from or based on this library. If you modify this library, you may extend this exception to your version of the library, but you are not obligated to do so. If you do not wish to do so, delete this exception statement from your version. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.mjdj.ui;

import java.util.ArrayList;
import java.util.List;

import com.confusionists.mjdj.midi.Logger;
import com.confusionists.mjdjApi.midiDevice.DeviceWrapper;
import com.confusionists.mjdjApi.midiDevice.ReceiverDeviceWrapper;
import com.confusionists.swing.CheckBoxList;

@SuppressWarnings("serial")
public class MidiDeviceCheckboxList extends CheckBoxList {

    private List<String> names = new ArrayList<String>();
    private List<? extends DeviceWrapper> wrappers = null;

    public MidiDeviceCheckboxList(String name) {
        setName(name);
    }

    /* not sure why I cannot simply cast this */
    public void setListReceivers(List<ReceiverDeviceWrapper> wrappersIn) {
    		List<DeviceWrapper> wrappers = new ArrayList<DeviceWrapper>();
    		for (ReceiverDeviceWrapper receiver : wrappersIn) {
    			wrappers.add(receiver);
    		}
    		setList(wrappers);
    	
    }

    public void setList(List<? extends DeviceWrapper> wrappers) {
        this.wrappers = wrappers;
        names = new ArrayList<String>();

        MidiDeviceCheckbox[] checkboxes = new MidiDeviceCheckbox[wrappers.size()];
        for (int i = 0; i < wrappers.size(); i++) {
            DeviceWrapper wrapper = wrappers.get(i);
            checkboxes[i] = new MidiDeviceCheckbox(wrapper);
            names.add(wrapper.toString());
            
        }
        setListData(checkboxes);

        if (checkboxes.length == 0) {
            Logger.log("No " + this.getName() + " found ");
        } else {
            Logger.log(checkboxes.length + " " + this.getName() + " found ");
        }
    }

    public List<String> getNames() {
        return names;
    }

    public List<? extends DeviceWrapper> getWrappers() {
        return wrappers;
    }
}
