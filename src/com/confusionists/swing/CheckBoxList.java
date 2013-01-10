/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

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

		@Override
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
