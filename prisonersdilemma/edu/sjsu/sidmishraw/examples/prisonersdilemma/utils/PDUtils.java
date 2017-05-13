/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.examples.prisonersdilemma.utils
 * File: PDUtils.java
 * 
 * @author sidmishraw
 *         Last modified: May 10, 2017 8:56:19 PM
 */
package edu.sjsu.sidmishraw.examples.prisonersdilemma.utils;

/**
 * @author sidmishraw
 *
 *         Qualified Name:
 *         edu.sjsu.sidmishraw.examples.prisonersdilemma.utils.PDUtils
 *
 */
public class PDUtils {
	
	/**
	 * The stratAccumulator generates the strategy depending upon the following
	 * truth table
	 * T, T -> T
	 * T, F -> F
	 * F, T -> T
	 * F, F -> F
	 * 
	 * @param op1
	 * @param op2
	 * @return {@link Boolean}
	 */
	public static boolean stratAccumulator(boolean op1, boolean op2) {
		
		if (op1 && op2) {
			
			return true;
		} else if (op1 && !op2) {
			
			return false;
		} else if (!op1 && op2) {
			
			return true;
		} else {
			
			return false;
		}
	}
}
