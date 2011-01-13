/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.mjdj.ui;

import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.confusionists.mjdj.settings.Settings;
import com.confusionists.mjdjApi.midiDevice.DeviceWrapper;
import com.confusionists.mjdjApi.midiDevice.TransmitterDeviceWrapper;

public class ClockSourceCombo extends JComboBox {

	private static final long serialVersionUID = -2738258304665340551L;
	private List<TransmitterDeviceWrapper> wrappers;
	
	public ClockSourceCombo() {
		
		
	}
	
	public void setList(List<TransmitterDeviceWrapper> wrappers) {
		this.wrappers = wrappers;
		String[] items = new String[wrappers.size()];
		for (int i = 0; i < wrappers.size(); i++) {
			items[i] = wrappers.get(i).getName();
		}
		setModel(new DefaultComboBoxModel(items));		
		String value = Settings.getInstance().clockSourceCombo;
		setSelectedItem(value);
		onAfterSelectedItemChanged();
	}
	
	@Override
	protected void selectedItemChanged() {
		super.selectedItemChanged();
		onAfterSelectedItemChanged();
	}
	
	
	public void onAfterSelectedItemChanged() {
		String selected = (String)this.getSelectedItem();
		for (DeviceWrapper wrapper : wrappers) {
			TransmitterDeviceWrapper wrapper2 = (TransmitterDeviceWrapper)wrapper;
			wrapper2.setClockSource( (wrapper2.getName().equals(selected)));
		}
		if (selected != null)
			Settings.getInstance().clockSourceCombo = selected;
	}
	
}
