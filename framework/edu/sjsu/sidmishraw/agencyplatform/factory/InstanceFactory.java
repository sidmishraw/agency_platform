/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.agencyplatform.factory
 * File: InstanceFactory.java
 * 
 * @author sidmishraw
 *         Last modified: May 3, 2017 3:09:19 PM
 */
package edu.sjsu.sidmishraw.agencyplatform.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sidmishraw
 *
 *         Qualified Name:
 *         edu.sjsu.sidmishraw.agencyplatform.factory.InstanceFactory
 *
 */
public class InstanceFactory {
	
	private static final Map<String, Object> instanceMap = new HashMap<>();
	
	/**
	 * 
	 */
	private InstanceFactory() {}
	
	/**
	 * Creates single instances for the classes passed to it. This will
	 * guarantee a singleton
	 * instance all the time.
	 * 
	 * @param clasz
	 *            -- The class whose singleton instance is needed
	 * @return T -- The singleton instance
	 */
	@SuppressWarnings("unchecked")
	public static final <T> T getInstance(Class<T> clasz) {
		
		if (null == instanceMap.get(clasz.getName())) {
			
			try {
				
				instanceMap.put(clasz.getName(), clasz.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				
				e.printStackTrace();
			}
		}
		
		return (T) instanceMap.get(clasz.getName());
	}
}
