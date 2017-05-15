/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.examples.prisonersdilemma.core
 * File: ScoreComputation.java
 * 
 * @author sidmishraw
 *         Last modified: May 14, 2017 11:53:56 AM
 */
package edu.sjsu.sidmishraw.examples.prisonersdilemma.core;

import edu.sjsu.sidmishraw.agencyplatform.core.Agent;

/**
 * @author sidmishraw
 *
 *         Qualified Name:
 *         edu.sjsu.sidmishraw.examples.prisonersdilemma.core.ScoreComputation
 *
 */
@FunctionalInterface
public interface ScoreComputation {
	
	public void compute(Agent<PrisonerMessage> prisoner1, MessageType prisoner1Msg,
			Agent<PrisonerMessage> prisoner2, MessageType prisoner2Msg);
}
