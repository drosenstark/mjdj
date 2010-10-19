/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.mjdj.ui;

import java.util.*;

import javax.swing.JCheckBox;
import javax.swing.ListModel;

import com.confusionists.mjdj.settings.MorphAdaptor;
import com.confusionists.mjdj.settings.Settings;
import com.confusionists.mjdjApi.morph.Morph;
import com.confusionists.swing.CheckBoxList;

@SuppressWarnings("serial")
public class MorphCheckboxList extends CheckBoxList {

	private Dictionary<String, MorphCheckbox> indexedList = new Hashtable<String, MorphCheckbox>();
	public static MorphCheckboxList instance = null;
	private List<MorphAdaptor> sortedMorphs = null;

	public MorphCheckboxList() {
		instance = this;
	}

	public MorphCheckbox getCheckbox(String name) {
		MorphCheckbox retVal = indexedList.get(name);
		return retVal;
	}

	public List<MorphAdaptor> getMorphs() {
		return getMorphs(null);
	}

	public List<MorphAdaptor> getMorphs(Morph afterMorph) {
		if (afterMorph == null)
			return sortedMorphs;

		int morphIndex = Settings.getInstance().getMorphAdaptor(afterMorph).orderNumber + 1;

		List<MorphAdaptor> retVal = sortedMorphs.subList(morphIndex, sortedMorphs.size());
		return retVal;
	}

	public void setMorphs(List<Morph> morphs) {

		ArrayList<MorphAdaptor> morphAdaptors = new ArrayList<MorphAdaptor>();
		for (Morph morph : morphs) {
			morphAdaptors.add(Settings.getInstance().getMorphAdaptor(morph));
		}
		morphs = null; // let's not get confused

		Collections.sort(morphAdaptors);

		this.sortedMorphs = morphAdaptors;

		for (MorphAdaptor morph : this.sortedMorphs) {
			if (!morph.isDead()) {
				this.addCheckbox(new MorphCheckbox(morph));
			}
		}
	}

	public void moveSelectedMorph(boolean moveUp) {
		MorphCheckbox box = (MorphCheckbox) getSelected();
		if (box != null)
			swap(moveUp, box);
		this.setSelectedValue(box, true);
	}

	@Override
	public void swap(boolean withPrevious, JCheckBox checkBox) {
		/* checks for non-swappables */
		MorphAdaptor adaptor = ((MorphCheckbox) checkBox).morph;
		if (!adaptor.isSwappable())
			return;
		if (!withPrevious) {
			MorphAdaptor testMorph = ((MorphCheckbox) this.getModel().getElementAt(adaptor.orderNumber + 1)).morph;
			if (!testMorph.isSwappable())
				return;
		}
		/* end checks for non-swappables */
		
		
		super.swap(withPrevious, checkBox);

		// clear out the morphs list and put it back in order
		sortedMorphs.clear();
		ListModel currentList = this.getModel();
		for (int i = 0; i < currentList.getSize(); i++) {
			MorphAdaptor morph = ((MorphCheckbox) currentList.getElementAt(i)).morph;
			sortedMorphs.add(morph);
			morph.orderNumber = i;
		}
	}

	/**
	 * this is the basis for allowing other morphs to move this morph up or down
	 * 
	 * @param morph
	 * @return
	 */
	public MorphCheckbox getMorphCheckbox(Morph morph) {
		ListModel currentList = this.getModel();
		for (int i = 0; i < currentList.getSize(); i++) {
			MorphCheckbox checkbox = (MorphCheckbox) currentList.getElementAt(i);
			if (checkbox.morph == morph)
				return checkbox;
		}
		throw new NullPointerException("Error 22622256"); // can't happen in
															// theory
	}

}
