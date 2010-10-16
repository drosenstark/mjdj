package com.confusionists.mjdj.morphs.nullConnection;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.List;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public @SuppressWarnings("serial")
class UiRow extends JPanel {
	JComboBox leftBox;
	JComboBox rightBox;
	JLabel label;
	Ui ui;
	JButton lessButton = new JButton("-");
	JButton moreButton = new JButton("+");
	
	public UiRow(Ui ui, List<String> inDevices, List<String> outDevices) {
		this.ui = ui;
		MigLayout mig = new MigLayout();		
		mig.setColumnConstraints("[][grow, center][][center][center]");
		this.setLayout(mig);
		
		leftBox = new JComboBox(inDevices.toArray());
		rightBox = new JComboBox(outDevices.toArray());
		label = new JLabel("send to");
		label.setAlignmentX(SwingConstants.CENTER);
		
		this.add(leftBox);
		this.add(label);
		this.add(rightBox);
		
		lessButton.setMaximumSize(new Dimension(20, 20));
		lessButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UiRow.this.ui.removeRow(UiRow.this);
			}
		});
		
		moreButton.setMaximumSize(new Dimension(20, 20));
		this.add(lessButton);
		this.add(moreButton);
		moreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UiRow.this.ui.addRowAfter(UiRow.this);
			}
		});
		
	}
	
	public void setStatus(List<UiRow> list) {
		int position = list.indexOf(this);
		if (position == 0) lessButton.setVisible(false);
	}
	
	
	public String getLeftName() {
		return (String)leftBox.getSelectedItem();
	}
	
	public String getRightName() {
		return (String)rightBox.getSelectedItem();
	}
	
	public Hashtable<String, String> getSerializable() {
		Hashtable<String, String> retVal = new Hashtable<String, String>();
		retVal.put("in", getLeftName());
		retVal.put("out", getRightName());
		return retVal;
	}
	
	public void setSerializable(Hashtable<String, String> serialiable) {
		leftBox.setSelectedItem(serialiable.get("in"));
		rightBox.setSelectedItem(serialiable.get("out"));
	}
	
}