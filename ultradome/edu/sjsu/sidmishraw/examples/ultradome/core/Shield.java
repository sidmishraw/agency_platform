/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.examples.ultradome.core
 * File: Shield.java
 * 
 * @author sidmishraw
 *         Last modified: May 7, 2017 1:25:04 PM
 */
package edu.sjsu.sidmishraw.examples.ultradome.core;

import java.util.Stack;

/**
 * @author sidmishraw
 *
 *         Qualified Name: edu.sjsu.sidmishraw.examples.ultradome.core.Shield
 *
 */
public class Shield {
	
	// the skins are stacked on top of each other
	// newest skin is applied over the older skins
	// so the newest is the one to be removed first
	// traditional LIFO styled structure, hence making
	// a stack.
	private Stack<ShieldSkin> skins = new Stack<>();
	
	/**
	 * Sets the default skin on the shield when created
	 */
	public Shield() {
		
		this.skins.push(new ShieldSkin(null));
	}
	
	/**
	 * @param skins
	 */
	public Shield(Stack<ShieldSkin> skins) {
		
		this.skins = skins;
	}
	
	/**
	 * The incoming strike needs to pass through all the skins on the shield,
	 * each skin reduces the strike by some percentage defined by the
	 * shieldskins'
	 * reduceStrike logic.
	 * 
	 * @param incomingStrike
	 * @return Strike
	 */
	public Strike reduceStrike(Strike incomingStrike) {
		
		float oldStrength = incomingStrike.getStrength();
		
		for (ShieldSkin skin : skins) {
			
			incomingStrike = skin.getReduceStrike().apply(incomingStrike);
		}
		
		System.out.println("Reduced strike " + incomingStrike.getDescription() + " from strength = " + oldStrength
				+ " to strength =" + incomingStrike.getStrength());
		
		return incomingStrike;
	}
	
	/**
	 * Add a new skin to the skins on the shield
	 * 
	 * @param newSkin
	 */
	public void addSkin(ShieldSkin newSkin) {
		
		this.skins.push(newSkin);
	}
	
	/**
	 * Removes the topmost layer of the shield
	 */
	public void removeSkin() {
		
		this.skins.pop();
	}
	
	/**
	 * Returns the number of skins currently on the shield of the gladiator
	 * 
	 * @return int
	 */
	public int nbrSkins() {
		
		return this.skins.size();
	}
}
