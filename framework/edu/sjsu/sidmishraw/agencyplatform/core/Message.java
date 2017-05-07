/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.agencyplatform.core
 * File: Message.java
 * 
 * @author sidmishraw
 *         Last modified: May 3, 2017 12:02:02 AM
 */
package edu.sjsu.sidmishraw.agencyplatform.core;

import java.io.Serializable;

/**
 * @author sidmishraw
 *
 *         Qualified Name: edu.sjsu.sidmishraw.agencyplatform.core.Message
 *
 */
public class Message<T> implements Serializable {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private T					content;
	
	/**
	 * 
	 */
	public Message() {}
	
	/**
	 * @param content
	 */
	public Message(T content) {
		
		this.content = content;
	}
	
	/**
	 * @return the content
	 */
	public T getContent() {
		
		return this.content;
	}
	
	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(T content) {
		
		this.content = content;
	}
}
