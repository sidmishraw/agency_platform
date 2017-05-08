/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.examples.ultradome.core
 * File: StrikeStrategy.java
 * 
 * @author sidmishraw
 *         Last modified: May 7, 2017 12:55:05 PM
 */
package edu.sjsu.sidmishraw.examples.ultradome.core;

/**
 * @author sidmishraw
 *
 *         Qualified Name:
 *         edu.sjsu.sidmishraw.examples.ultradome.core.StrikeStrategy
 *
 */
@FunctionalInterface
public interface StrikeStrategy {
	
	public Strike makeStrike();
}
