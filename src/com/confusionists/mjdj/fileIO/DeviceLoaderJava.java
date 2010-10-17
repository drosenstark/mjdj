/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

Linking this library statically or dynamically with other modules is making a combined work based on this library. Thus, the terms and conditions of the GNU General Public License cover the whole combination. 

As a special exception, the copyright holders of this library give you permission to link this library with independent modules to produce an executable, regardless of the license terms of these independent modules, and to copy and distribute the resulting executable under terms of your choice, provided that you also meet, for each linked independent module, the terms and conditions of the license of that module. An independent module is a module which is not derived from or based on this library. If you modify this library, you may extend this exception to your version of the library, but you are not obligated to do so. If you do not wish to do so, delete this exception statement from your version. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.mjdj.fileIO;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import com.confusionists.mjdj.ui.Logger;
import com.confusionists.mjdjApi.midiDevice.ReceiverDeviceWrapper;
import com.confusionists.mjdjApi.midiDevice.TransmitterDeviceWrapper;
import com.confusionists.util.RecursiveFileList;

public class DeviceLoaderJava {
	/**
	 * let's be serious, this is single-threaded so it can be static
	 */
	private static ClassLoader classLoader; 

	public void load(List<TransmitterDeviceWrapper> transmitters, List<ReceiverDeviceWrapper> receivers) {
		try {
			// get all .class files in the directory
			File path = ExternalDirectories.instance.devicesDir;
			load(path, transmitters, receivers);
		} catch (Exception e) {
			Logger.log("Problem loading devices", e);

		}

	}

	private URL[] getClasspath() throws MalformedURLException {
		URL url = ExternalDirectories.instance.devicesDir.toURI().toURL();
		URL[] urls = { url };
		return urls;
	}

	private void load(File path, List<TransmitterDeviceWrapper> transmitters, List<ReceiverDeviceWrapper> receivers) throws Exception {
		if (path == null)
			throw new NullPointerException("very null");
		if (!path.exists()) {
			throw new RuntimeException("Sorry, can't find the plugins directory, should be " + path.getAbsolutePath());
		} else {
			System.out.println("Found and loading " + path.getAbsolutePath());
		}

		File[] classFiles = RecursiveFileList.getList(path, "class");

		URL[] urls = getClasspath();
		classLoader = new URLClassLoader(urls);

		for (File classFile : classFiles) {
			String totalPath = classFile.getAbsolutePath();
			totalPath = totalPath.substring(path.getAbsolutePath().length()); // get rid of the root
			String className = totalPath.substring(totalPath.lastIndexOf(File.separatorChar) + 1);
			className = className.substring(0, className.length() - ".class".length());
			String packagePrefix;
			int lastSeparator = totalPath.lastIndexOf(File.separatorChar);
			if (lastSeparator != -1) {
				packagePrefix = totalPath.substring(0, lastSeparator + 1);
			} else
				packagePrefix = "";

			packagePrefix = packagePrefix.replace(File.separatorChar, '.');
			if (packagePrefix.startsWith("."))
				packagePrefix = packagePrefix.substring(1);

			className = packagePrefix + className;
			Class<?> clazz;
			clazz = classLoader.loadClass(className);
			try {
				
				Object obj = clazz.newInstance();

				if (obj instanceof ReceiverDeviceWrapper) {
					ReceiverDeviceWrapper receiver = (ReceiverDeviceWrapper) obj;
					Logger.log("Loaded receiver " + receiver.getName());
					receivers.add(receiver);
				} else if (obj instanceof TransmitterDeviceWrapper) {
					TransmitterDeviceWrapper transmitter = (TransmitterDeviceWrapper) clazz.newInstance();
					Logger.log("Loaded transmitter " + transmitter.getName());
					transmitters.add(transmitter);
				}
			} catch (InstantiationException e) {
				// Logger.log("Could not instantiate device " + className +
				// " probably abstract");
			} catch (IllegalAccessException e) {
				//Logger.log("Could not instantiate device " + className + " probably private");
			} catch (Exception e) {
				Logger.log("Could not instantiate device " + className, e);
			}
		}
	}

	/* testing main */
	public static void main(String[] args) throws Exception {

	}

}
