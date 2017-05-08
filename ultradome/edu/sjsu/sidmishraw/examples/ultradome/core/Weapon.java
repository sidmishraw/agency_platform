/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.examples.ultradome.core
 * File: Weapon.java
 * 
 * @author sidmishraw
 *         Last modified: May 7, 2017 12:39:31 PM
 */
package edu.sjsu.sidmishraw.examples.ultradome.core;

/**
 * @author sidmishraw
 *
 *         Qualified Name: edu.sjsu.sidmishraw.examples.ultradome.core.Weapon
 *
 */
public class Weapon {
	
	// used for generating IDs for the weapons
	private static int		weaponCount		= 0;
	
	// the ID of the weapon
	private int				ID;
	
	// name of the weapon
	private String			name;
	
	// weapon type, can be SWORD, WAND, etc
	private WeaponType		type;
	
	// a do nothing default strike strategy
	private StrikeStrategy	strikeStrategy	= () -> null;
	
	/**
	 * @param name
	 * @param type
	 * @param attackMessage
	 * @param attackModifier
	 */
	public Weapon(String name, WeaponType type) {
		
		this.ID = Weapon.weaponCount;
		
		this.name = name;
		this.type = type;
		
		Weapon.weaponCount++;
	}
	
	public Strike makeStrike() {
		
		return this.strikeStrategy.makeStrike();
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		
		return this.name;
	}
	
	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		
		this.name = name;
	}
	
	/**
	 * @return the type
	 */
	public WeaponType getType() {
		
		return this.type;
	}
	
	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(WeaponType type) {
		
		this.type = type;
	}
	
	/**
	 * @return the strikeStrategy
	 */
	public StrikeStrategy getStrikeStrategy() {
		return this.strikeStrategy;
	}
	
	/**
	 * @param strikeStrategy
	 *            the strikeStrategy to set
	 */
	public void setStrikeStrategy(StrikeStrategy strikeStrategy) {
		
		this.strikeStrategy = strikeStrategy;
	}
	
	/**
	 * @return the weaponCount
	 */
	public static int getWeaponCount() {
		return weaponCount;
	}
	
	/**
	 * @return the iD
	 */
	public int getID() {
		return this.ID;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Weapon [ID=" + this.ID + ", name=" + this.name + ", type=" + this.type + ", strikeStrategy="
				+ this.strikeStrategy + "]";
	}
}
