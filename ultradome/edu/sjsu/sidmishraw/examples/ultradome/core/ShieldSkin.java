/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.examples.ultradome.core
 * File: ShieldSkin.java
 * 
 * @author sidmishraw
 *         Last modified: May 7, 2017 12:41:20 PM
 */
package edu.sjsu.sidmishraw.examples.ultradome.core;

import java.util.function.Function;

/**
 * @author sidmishraw
 *
 *         Qualified Name:
 *         edu.sjsu.sidmishraw.examples.ultradome.core.ShieldSkin
 *
 */
public class ShieldSkin {
	
	// thinking of having this as a wrapper to reduce code
	// bloat on screen and keep it readable
	
	// by default a shield skin will reduce the incoming strength by 1%
	// reduce strike is the custom implementation for the shield skin
	// comes with a default 1% reduction
	// this should be customized how it impacts the incoming strike
	private Function<Strike, Strike>	reduceStrike		= (incomingStrike) -> {
																
																float incomingStrength = incomingStrike
																		.getStrength();
																float outgoingStrength = incomingStrength
																		- ((DEFAULT_REDUCTION)
																				* incomingStrength);
																
																incomingStrike
																		.setStrength(
																				outgoingStrength);
																
																System.out
																		.println(
																				"Default ShieldSkin has acted: reduced from oldStrength = "
																						+ incomingStrength
																						+ " to newStrength = "
																						+ outgoingStrength);
																
																return incomingStrike;
															};
	
	public static final float			DEFAULT_REDUCTION	= 0.01F;
	
	/**
	 * 
	 * @param reduceStrike
	 */
	public ShieldSkin(Function<Strike, Strike> reduceStrike) {
		
		if (null != reduceStrike) {
			
			this.reduceStrike = reduceStrike;
		}
	}
	
	/**
	 * @return the reduceStrike
	 */
	public Function<Strike, Strike> getReduceStrike() {
		
		return this.reduceStrike;
	}
	
	/**
	 * @param reduceStrike
	 *            the reduceStrike to set
	 */
	public void setReduceStrike(Function<Strike, Strike> reduceStrike) {
		
		this.reduceStrike = reduceStrike;
	}
	
	/**
	 * @return the defaultReduction
	 */
	public static float getDefaultReduction() {
		
		return DEFAULT_REDUCTION;
	}
}
