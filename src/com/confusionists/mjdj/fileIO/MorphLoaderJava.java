/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.mjdj.fileIO;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.confusionists.mjdj.morphs.nullConnection.NullConnection;
import com.confusionists.mjdj.ui.Logger;
import com.confusionists.mjdjApi.morph.Morph;
import com.confusionists.util.RecursiveFileList;

public class MorphLoaderJava {

	
	protected static ClassLoader classLoader; // let's be serious, this is single-threaded so it can be static

	public void load(List<Morph> morphs) throws Exception {
		
		// get all .class files in the directory
		File path = ExternalDirectories.instance.morphsDir;
		load(path, morphs);
		morphs.add(new NullConnection()); // this is the one morph that is actually compiled in to Mjdj
	}
	
	private URL[] getClasspath() throws MalformedURLException {
		URL url = ExternalDirectories.instance.morphsDir.toURI().toURL();
		URL[] urls = {url}; 
		return urls;
	}
	
	private void load(File path, List<Morph> morphs) throws Exception {
		if (path == null)
			throw new NullPointerException("very null");
		if (!path.exists()) {
			throw new RuntimeException("Sorry, can't find the plugins directory, should be " + path.getAbsolutePath());
		} else {
                    System.out.println("Found and loading " + path.getAbsolutePath());
                }
		
		File[] classFiles = RecursiveFileList.getList(path, "class");
		
		
		URL[] urls = getClasspath();;
		classLoader = new URLClassLoader(urls);		

		for (File classFile : classFiles) {
			String totalPath = classFile.getAbsolutePath();
			totalPath = totalPath.substring(path.getAbsolutePath().length()); // get rid of the root
			String className = totalPath.substring(totalPath.lastIndexOf(File.separatorChar)+1);
			className = className.substring(0,className.length() - ".class".length());
			String packagePrefix;
			int lastSeparator = totalPath.lastIndexOf(File.separatorChar);
			if (lastSeparator != -1) {
				packagePrefix = totalPath.substring(0, lastSeparator+1);
			}
			else
				packagePrefix = "";
			
			packagePrefix = packagePrefix.replace(File.separatorChar, '.');
			if (packagePrefix.startsWith("."))
				packagePrefix = packagePrefix.substring(1);
			
			className = packagePrefix + className;
			Class<?> clazz;
			clazz = classLoader.loadClass(className);
			try {
				Object obj = clazz.newInstance();
				if (obj instanceof Morph) {
					Morph morph = (Morph) obj;
					Logger.log("Loaded morph " + morph.getName() + " ("  + className + ")");
					morphs.add(morph);
				} 
			} catch (InstantiationException e) {
				//Logger.log("Could not instantiate translator " + className + " probably abstract");
			} catch (IllegalAccessException e) {
				//Logger.log("Could not instantiate translator " + className + " probably private");
			} catch (Exception e) {
				Logger.log("Could not instantiate translator " + className + " " + e);
			}
		}
	}
	
	
	
	/* testing main */
	public static void main(String[] args) throws Exception {
	//	MorphsDirectories.init();
		List<Morph> morphs = new ArrayList<Morph>();			
		MorphLoaderJava loader = new MorphLoaderJava();
		loader.load(morphs);
		System.out.println("Java loader " + morphs.size());
		
	}
	
}
