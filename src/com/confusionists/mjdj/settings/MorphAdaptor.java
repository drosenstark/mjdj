/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.mjdj.settings;

import com.confusionists.mjdj.morphs.nullConnection.NullConnection;
import com.confusionists.mjdjApi.morph.Morph;

public class MorphAdaptor implements Comparable<MorphAdaptor> {
	private transient Morph morph;
	private boolean active;
	public int orderNumber;
	private boolean dead;
	private Object serializablePiece; // the Morph uses this to store its settings
	
	@Override
	public int compareTo(MorphAdaptor o) {
		if (o.morph instanceof NullConnection)
			return -1;
		if (orderNumber < o.orderNumber)
			return -1;
		if (orderNumber > o.orderNumber)
			return 1;
		return 0;
	}
	
	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}
	/**
	 * @param morph the morph to set
	 */
	public void setMorph(Morph morph) {
		this.morph = morph;
	}
	/**
	 * @return the morph
	 */
	public Morph getMorph() {
		return morph;
	}
	
	public void setDead(boolean dead) { 
		this.dead = dead; 
	}
	
	public boolean isDead() {
		return dead;
	}

	public void saveMorphSettings() {
		this.serializablePiece = getMorph().getSerializable();
	}

	public void restore() {
		if (morph != null)
			morph.setSerializable(this.serializablePiece);
		
	}

	public boolean isSwappable() {
		if (getMorph() instanceof NullConnection)
			return false;
		return true;
	}

}
