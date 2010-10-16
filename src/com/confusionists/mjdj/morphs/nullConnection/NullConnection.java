package com.confusionists.mjdj.morphs.nullConnection;
import java.util.ArrayList;
import java.util.Hashtable;

import com.confusionists.mjdjApi.midi.MessageWrapper;
import com.confusionists.mjdjApi.morph.DeviceNotFoundException;
import com.confusionists.mjdjApi.morph.AbstractMorph;


public class NullConnection extends AbstractMorph {
	private final static String ANY = "All Active";
	Ui ui = null;
	

	public String getName() {
		return "Null-Connection (Right-Click to Open)";
	}
 
	public void init() throws DeviceNotFoundException {
		if (ui == null) {
			getInDeviceNames().add(0, ANY);
			getOutDeviceNames().add(0, ANY);
			ui = new Ui(this, getInDeviceNames(), getOutDeviceNames());
		}
	}
	
	@Override
	public void toggleUi() {
		ui.setVisible(true);
		
	}
	

	public boolean process(MessageWrapper message, String from) throws Exception {
		// we should put in a safeguard for not sending twice, but for now we'll leave it alone
		for (UiRow row : ui.rows) {
			if ((row.getLeftName()==ANY || row.getLeftName().equals(from))) {
				if (row.getRightName() == ANY)
					getService().send(message);
				else
					getService().send(message, row.getRightName());
			}
		}
		
		return true;
	}

	public String diagnose() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getSerializable() { 
		ArrayList<Hashtable<String, String>> retVal = new ArrayList<Hashtable<String, String>>(); // this monstrosity is because XStream can't get access to our classes since they're not loaded
		if (ui == null) return null; // for testing mostly
		for (UiRow row : ui.rows) {
			retVal.add(row.getSerializable());
		}
		return retVal;
	}

	@SuppressWarnings("unchecked")
	public void setSerializable(Object value) {
		if (value == null)
			return;
		ui.removeRows();
		ArrayList<Hashtable<String, String>>  serializable = (ArrayList<Hashtable<String, String>>)value;
		boolean isFirst = true;
		for (Hashtable<String, String> sRow : serializable) {
			UiRow row;

			if (isFirst) 
				row = ui.rows.get(0);
			else
				row = new UiRow(ui, getInDeviceNames(), getOutDeviceNames());
			
			row.setSerializable(sRow);
			
			if (isFirst) 
				isFirst = false;
			else
				ui.addRow(row);
		}
		
	}
	

}

