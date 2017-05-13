/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.examples.prisonersdilemma.core
 * File: PrisonerMessage.java
 * 
 * @author sidmishraw
 *         Last modified: May 10, 2017 8:13:31 PM
 */
package edu.sjsu.sidmishraw.examples.prisonersdilemma.core;

import edu.sjsu.sidmishraw.agencyplatform.core.Agent;

/**
 * @author sidmishraw
 *
 *         Qualified Name:
 *         edu.sjsu.sidmishraw.examples.prisonersdilemma.core.PrisonerMessage
 *
 */
public class PrisonerMessage {
	
	// the back reference to the sender of the message, so that the prisoner
	// knows whom to respond back to
	private Agent<PrisonerMessage>	sender			= null;
	
	// message type is either a COOPERATE or DEFECT
	private MessageType				messageContent	= null;
	
	/**
	 * @param sender
	 * @param messageContent
	 */
	public PrisonerMessage(Agent<PrisonerMessage> sender, MessageType messageContent) {
		
		this.sender = sender;
		this.messageContent = messageContent;
	}
	
	/**
	 * @return the sender
	 */
	public Agent<PrisonerMessage> getSender() {
		return this.sender;
	}
	
	/**
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(Agent<PrisonerMessage> sender) {
		this.sender = sender;
	}
	
	/**
	 * @return the messageContent
	 */
	public MessageType getMessageContent() {
		return this.messageContent;
	}
	
	/**
	 * @param messageContent
	 *            the messageContent to set
	 */
	public void setMessageContent(MessageType messageContent) {
		this.messageContent = messageContent;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PrisonerMessage [sender=" + this.sender.toString() + ", messageContent="
				+ this.messageContent.toString() + "]";
	}
}
