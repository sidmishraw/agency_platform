/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.examples.ultradome.core
 * File: MedicineBowl.java
 * 
 * @author sidmishraw
 *         Last modified: May 7, 2017 12:41:33 PM
 */
package edu.sjsu.sidmishraw.examples.ultradome.core;

/**
 * @author sidmishraw
 *
 *         Qualified Name:
 *         edu.sjsu.sidmishraw.examples.ultradome.core.MedicineBowl
 *
 */
public class MedicineBowl {
	
	// used to keep count of the number of bowls in the world
	private static int	medicineBowlCount	= 0;
	
	// number of units of contents
	// the content is decremented everytime the consume on the
	// medicine bowl is called till it reaches 0
	private int			contents			= 25;
	
	/**
	 * 
	 */
	public MedicineBowl() {}
	
	/**
	 * 
	 * @param contents
	 */
	public MedicineBowl(int contents) {
		
		this.contents = contents;
	}
	
	/**
	 * This is called when someone tries to consume the medicine in the bowl
	 * It decrements the contents of the medicine
	 */
	public void consume() {
		
		if (contents > 0) {
			
			this.contents--;
		} else {
			
			System.out.println("Medicine bowl is empty");
		}
	}
	
	/**
	 * @return the medicineBowlCount
	 */
	public static int getMedicineBowlCount() {
		
		return medicineBowlCount;
	}
	
	/**
	 * @param medicineBowlCount
	 *            the medicineBowlCount to set
	 */
	public static void setMedicineBowlCount(int medicineBowlCount) {
		
		MedicineBowl.medicineBowlCount = medicineBowlCount;
	}
	
	/**
	 * @return the contents
	 */
	public int getContents() {
		
		return this.contents;
	}
	
	/**
	 * @param contents
	 *            the contents to set
	 */
	public void setContents(int contents) {
		
		this.contents = contents;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return "MedicineBowl [contents=" + this.contents + "]";
	}
}
