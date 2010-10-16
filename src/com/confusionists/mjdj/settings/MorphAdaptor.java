package com.confusionists.mjdj.settings;

import com.confusionists.mjdj.morphs.nullConnection.NullConnection;
import com.confusionists.mjdjApi.morph.Morph;

public class MorphAdaptor implements Comparable<MorphAdaptor> {
	private transient Morph morph;
	private boolean active;
	public int orderNumber;
	private boolean dead;
	private Object serializablePiece; // the Morph uses this to store its settings
	
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
