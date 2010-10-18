/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

Linking this library statically or dynamically with other modules is making a combined work based on this library. Thus, the terms and conditions of the GNU General Public License cover the whole combination. 

As a special exception, the copyright holders of this library give you permission to link this library with independent modules to produce an executable, regardless of the license terms of these independent modules, and to copy and distribute the resulting executable under terms of your choice, provided that you also meet, for each linked independent module, the terms and conditions of the license of that module. An independent module is a module which is not derived from or based on this library. If you modify this library, you may extend this exception to your version of the library, but you are not obligated to do so. If you do not wish to do so, delete this exception statement from your version. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.mjdj.settings;

import java.io.*;
import java.util.Hashtable;

import com.confusionists.mjdjApi.morph.Morph;
import com.thoughtworks.xstream.XStream;

public class Settings  {

	private static Settings instance = null;
	protected static String FILENAME = "settings.xml";
	public Hashtable<String, Boolean> receivers = new Hashtable<String, Boolean>();
	public Hashtable<String, Boolean> transmitters = new Hashtable<String, Boolean>();
	public Hashtable<String, MorphAdaptor> morphAdaptors = new Hashtable<String, MorphAdaptor>(); 
	public String clockSourceCombo;
	public boolean doNotUseComSunDrivers;
	
	public static Settings getInstance() {
		return getInstance(false, false);
	}
	
	private Settings() {}
	
	public MorphAdaptor getMorphAdaptor(Morph morph) {
		MorphAdaptor retVal = getMorphActive(morph.getName());
		retVal.setMorph(morph); // TODO this will hard to find if there are two morphs with same name, put in protections against this
		return retVal;
	}
	
	private MorphAdaptor getMorphActive(String name) {
		MorphAdaptor retVal = morphAdaptors.get(name);
		if (retVal == null) {
			retVal = new MorphAdaptor();
			morphAdaptors.put(name, retVal);
		}
		return retVal;
	}
	

	public static Settings getInstance(boolean forceReload, boolean forceNew) {
		if (forceNew)
			instance = new Settings();
		else if (forceReload || instance == null) {
			try {
				load();
			} catch (FileNotFoundException fe) {
				System.out.println("Settings probably not found in file " + FILENAME);
				instance = new Settings();
			} catch (Exception e) {
				throw new RuntimeException("Problem loading settings from file " + new File(FILENAME).getAbsolutePath(),e);
				
			}
		}
		return instance;
	}

	public static boolean getBoolean(Hashtable<String, Boolean> settings, String key, boolean defaultSetting) {
		assert (settings != null);
		assert (key != null);
		Boolean obj = settings.get(key);
		if (obj == null)
			return defaultSetting;
		else
			return obj.booleanValue();
	}

	public static boolean getBoolean(Hashtable<String, Boolean> settings, String key) {
		return getBoolean(settings, key, false);
	}

	
	public static void load() throws FileNotFoundException  {
		// Create input streams.
			FileInputStream fis = new FileInputStream(FILENAME);
			XStream xstream = new XStream();
			instance = (Settings) xstream.fromXML(fis);
			System.out.println("Settings reloaded sucessfully.");
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace(); // What does it mean if you cannot close the file? No idea... 
			}
	}

	public void save() throws IOException {
		// Create output stream.
		FileOutputStream fos = new FileOutputStream(FILENAME);
		// Create XML encoder.
		XStream xstream = new XStream();
		xstream.toXML(this, fos);
		System.out.println("Settings have been saved.");
		fos.close();
	}

	
	

}
