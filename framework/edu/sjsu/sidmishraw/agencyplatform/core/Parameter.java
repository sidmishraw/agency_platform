/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.agencyplatform.core
 * File: Parameter.java
 * 
 * @author sidmishraw
 *         Last modified: May 3, 2017 3:50:54 PM
 */
package edu.sjsu.sidmishraw.agencyplatform.core;

/**
 * @author sidmishraw
 *
 *         Qualified Name: edu.sjsu.sidmishraw.agencyplatform.core.Parameter
 *
 */
public final class Parameter {
	
	public final String	parameterName;
	public final Object	value;
	
	/**
	 * 
	 */
	public Parameter(String parameterName, Object value) {
		
		this.parameterName = parameterName;
		this.value = value;
	}
}
