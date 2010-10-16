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

import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.confusionists.mjdj.Universe;
import com.confusionists.mjdj.settings.Settings;
import com.confusionists.mjdjApi.midiDevice.DeviceWrapper;
import com.confusionists.mjdjApi.midiDevice.ReceiverDeviceWrapper;

public class MidiDeviceCheckbox extends JCheckBox implements ChangeListener {

	boolean initializing = true;
	private static final long serialVersionUID = 847604558283904051L;
	DeviceWrapper wrapper;

	private Hashtable<String, Boolean> getSettings() {
		return getSettings(false);
	}

	private Hashtable<String, Boolean> getSettings(boolean getOpposite) {
		boolean useReceiverList = (wrapper instanceof ReceiverDeviceWrapper);
		if (getOpposite)
			useReceiverList = !useReceiverList;

		if (useReceiverList) {
			return Settings.getInstance().receivers;
		} else {
			return Settings.getInstance().transmitters;
		}
	}

	public MidiDeviceCheckbox(DeviceWrapper wrapper) {
		super(wrapper.getName());
		this.wrapper = wrapper;
		addChangeListener(this);

		boolean value = Settings.getBoolean(getSettings(), wrapper.toString(), false);
		if (value != isSelected()) {
			doClick();
		}
		initializing = false;
	}

	public void close() {
		this.wrapper = null;
	}

	public void stateChanged(ChangeEvent e) {
		boolean valid = validateSelection();
		if (!valid) {
			this.setSelected(Settings.getBoolean(getSettings(), wrapper.toString()));
			return;
		}

		getSettings().put(wrapper.toString(), this.isSelected());
		boolean realValue = Settings.getBoolean(getSettings(), wrapper.toString());
		wrapper.setActive(realValue);
		if (realValue != this.isSelected()) {
			this.setSelected(realValue); // we might loop once here, but it's
		}
	}
	
	@Override
	public String getName() {
		return wrapper.getName();
	}

	/* avoid feedback loops especially for IAC-type ports */
	private boolean validateSelection() {
		if (initializing)
			return true;
		if (Settings.getBoolean(getSettings(true), this.getName()) && ((Boolean) this.isSelected()).booleanValue()) {
			int result = JOptionPane.showConfirmDialog(Universe.instance.main, "About to turn " + this.getName() + " on. The opposite port with the same name  is on.\n" + 
					"This could create a feedback loop. Do it anyway?", "Mjdj - Confirm Selection", JOptionPane.OK_CANCEL_OPTION);
			if (result != JOptionPane.OK_OPTION)
				return false;
		}
		return true;

	}

}
