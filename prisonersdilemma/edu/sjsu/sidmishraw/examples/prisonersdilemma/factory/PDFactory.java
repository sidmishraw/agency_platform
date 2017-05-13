/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.examples.prisonersdilemma
 * File: PDFactory.java
 * 
 * @author sidmishraw
 *         Last modified: May 10, 2017 8:36:26 PM
 */
package edu.sjsu.sidmishraw.examples.prisonersdilemma.factory;

import edu.sjsu.sidmishraw.agencyplatform.core.Agent;
import edu.sjsu.sidmishraw.examples.prisonersdilemma.core.MessageType;
import edu.sjsu.sidmishraw.examples.prisonersdilemma.core.PrisonerMessage;

/**
 * @author sidmishraw
 *
 *         Qualified Name:
 *         edu.sjsu.sidmishraw.examples.prisonersdilemma.PDFactory
 *
 */
public class PDFactory {
	
	public PrisonerMessage makeMessage(Agent<PrisonerMessage> sender,
			MessageType messageContent) {
		
		PrisonerMessage msg = new PrisonerMessage(sender, messageContent);
		
		System.out.println("Made a message for " + sender.getDescription()
				+ " with contents " + messageContent.toString());
		
		return msg;
	}
}
