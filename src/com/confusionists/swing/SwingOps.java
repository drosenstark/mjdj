/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.swing;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.UIManager;

public class SwingOps {

  /**
   * Calls methods setMax, setMin, setPreferred and setSize
   * on a JComponent.
   * @param comp
   * @param dimension
   */
  public static void setAllSizes(Container comp, Dimension dimension) {
    comp.setMaximumSize(dimension);
    comp.setMinimumSize(dimension);
    comp.setPreferredSize(dimension);
    comp.setSize(dimension);
  }

  /**
   * @param component
   * @param width
   * @param height
   */
  public static void setAllSizes(Container container, int width, int height) {
    setAllSizes(container, new Dimension(width, height));
  }

  public static void setLookAndFeel() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}