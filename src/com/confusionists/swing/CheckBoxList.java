/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

Linking this library statically or dynamically with other modules is making a combined work based on this library. Thus, the terms and conditions of the GNU General Public License cover the whole combination. 

As a special exception, the copyright holders of this library give you permission to link this library with independent modules to produce an executable, regardless of the license terms of these independent modules, and to copy and distribute the resulting executable under terms of your choice, provided that you also meet, for each linked independent module, the terms and conditions of the license of that module. An independent module is a module which is not derived from or based on this library. If you modify this library, you may extend this exception to your version of the library, but you are not obligated to do so. If you do not wish to do so, delete this exception statement from your version. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
 */
package com.confusionists.swing;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

/* Class developed thanks to http://www.devx.com/tips/Tip/5342 */
@SuppressWarnings("serial")
public class CheckBoxList extends JList {
	/**
	 * 
	 */
	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	public CheckBoxList() {
		setCellRenderer(new CellRenderer());

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int index = locationToIndex(e.getPoint());
				if (index != -1) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						CheckBoxList.this.onLeftClick(e, index);
					} else {
						CheckBoxList.this.onRightClick(e, index);
					}
				}
			}
		});

		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	private void onLeftClick(MouseEvent e, int index) {
		JCheckBox checkBox = (JCheckBox)getModel().getElementAt(index);
		checkBox.setSelected(!checkBox.isSelected());
		repaint();
	}
	
	private void onRightClick(MouseEvent e, int index) {
		Object checkbox = getModel().getElementAt(index);
		if (checkbox instanceof RightClickable) {
			RightClickable clickMe = (RightClickable) checkbox;
			clickMe.onRightClick();
		}
	}


	public void addCheckbox(JCheckBox checkBox) {
		ListModel currentList = this.getModel();
		JCheckBox[] newList = new JCheckBox[currentList.getSize() + 1];
		for (int i = 0; i < currentList.getSize(); i++) {
			newList[i] = (JCheckBox) currentList.getElementAt(i);
		}
		newList[newList.length - 1] = checkBox;
		setListData(newList);
	}
	
	// TODO change name to checkbox from wrapper
	public void swap(boolean withPrevious, JCheckBox wrapper) {
		ListModel currentList = this.getModel();

		int wrapperIndex  = 0;
		for (int i = 0; i < currentList.getSize(); i++) {
			if (currentList.getElementAt(i) == wrapper) {
				wrapperIndex = i;
				break;
			}
		}
		
		int swapWithIndex = withPrevious ? wrapperIndex - 1 : wrapperIndex + 1;
		
		// check for out-of-bounds
		if (swapWithIndex < 0 || swapWithIndex > currentList.getSize()-1)
			return;
		
		JCheckBox[] newList = new JCheckBox[currentList.getSize()];
		for (int i = 0; i < currentList.getSize(); i++) {
			if (i == swapWithIndex) 
				newList[i] = wrapper;
			else if (i == wrapperIndex)
				newList[i] = (JCheckBox) currentList.getElementAt(swapWithIndex);
			else
				newList[i] = (JCheckBox) currentList.getElementAt(i);
		}
		setListData(newList);
		
	}

	protected class CellRenderer implements ListCellRenderer {

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JCheckBox checkbox = (JCheckBox) value;

			checkbox.setBackground(isSelected ? getSelectionBackground() : getBackground());
			checkbox.setForeground(isSelected ? getSelectionForeground() : getForeground());
			checkbox.setOpaque(true);
			checkbox.setEnabled(isEnabled());
			checkbox.setFont(getFont());
			checkbox.setFocusPainted(false);
			checkbox.setBorderPainted(true);
			checkbox.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);

			return (JComponent) value;
		}
	}

	public JCheckBox getSelected() {
		return (JCheckBox)this.getSelectedValue();
	}

}
