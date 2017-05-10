/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.examples.ultradome.core
 * File: Composition.java
 * 
 * @author sidmishraw
 *         Last modified: May 9, 2017 11:08:06 PM
 */
package edu.sjsu.sidmishraw.examples.ultradome.core;

/**
 * @author sidmishraw
 *
 *         Qualified Name:
 *         edu.sjsu.sidmishraw.examples.ultradome.core.Composition
 *
 */
@FunctionalInterface
public interface Composition<F1, F2, V, R> {
	
	public R apply(F1 function1, F2 function2, V incomingValue);
}
