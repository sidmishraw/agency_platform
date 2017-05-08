/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.examples.ultradome.core
 * File: Strike.java
 * 
 * @author sidmishraw
 *         Last modified: May 7, 2017 12:56:41 PM
 */
package edu.sjsu.sidmishraw.examples.ultradome.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sidmishraw
 *
 *         Qualified Name: edu.sjsu.sidmishraw.examples.ultradome.core.Strike
 *
 */
public class Strike {
	
	// types of strike IRON, FIRE, POISON etc
	private List<WeaponStrikeType>	strikeType	= new ArrayList<>();
	
	// the strike description
	private String					description;
	
	// strength of the strike; The strength of the strike is a percentage of the
	// attacker's health.
	private float					strength;
	
	/**
	 * @param strikeType
	 * @param description
	 * @param strength
	 */
	public Strike(String description, float strength, WeaponStrikeType... strikeType) {
		
		this.description = description;
		this.strength = strength;
		
		Arrays.asList(strikeType).forEach(striketype -> {
			
			this.strikeType.add(striketype);
		});
	}
	
	/**
	 * @return the strikeType
	 */
	public List<WeaponStrikeType> getStrikeType() {
		
		return this.strikeType;
	}
	
	/**
	 * @param strikeType
	 *            the strikeType to set
	 */
	public void setStrikeType(List<WeaponStrikeType> strikeType) {
		
		this.strikeType = strikeType;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		
		return this.description;
	}
	
	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		
		this.description = description;
	}
	
	/**
	 * @return the strength
	 */
	public float getStrength() {
		
		return this.strength;
	}
	
	/**
	 * @param strength
	 *            the strength to set
	 */
	public void setStrength(float strength) {
		
		this.strength = strength;
	}
}
