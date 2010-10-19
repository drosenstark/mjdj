/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.mjdj.fileIO;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

@Deprecated
public class DynamicCompiler {

   JavaCompiler compiler;

   public static DynamicCompiler instance = new DynamicCompiler();
   private DynamicCompiler() {} // singleton
   
   private JavaCompiler getCompiler() {
	   if (compiler == null)
		   compiler = ToolProvider.getSystemJavaCompiler();
	   return compiler;
   }

   public void compile(String[] filesToCompile) {
	   compile(null, filesToCompile);
   }
   
   public void compile(String outputDirectory, String[] filesToCompile) {
	  if (outputDirectory != null) {
		  String[] filesToCompileRedux = new String[filesToCompile.length + 2];
		  filesToCompileRedux[0] = "-d";
		  filesToCompileRedux[1] = outputDirectory;
		  for (int i = 0; i < filesToCompile.length; i++) {
			  filesToCompileRedux[i+2] = filesToCompile[i];
		  }
		  filesToCompile = filesToCompileRedux;
	  } 
	   
      this.getCompiler().run(null, System.out, System.err,filesToCompile);
   }

   public static void main(String[] args) throws Exception {
     String[] filesToCompile = { "testFiles/test/Something.java" };
     DynamicCompiler.instance.compile("testfiles/blah", filesToCompile);
   }
}