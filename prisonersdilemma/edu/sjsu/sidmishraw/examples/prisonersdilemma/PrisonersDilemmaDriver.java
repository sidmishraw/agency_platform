/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.examples.prisonersdilemma.core
 * File: PrisonersDilemmaDriver.java
 * 
 * @author sidmishraw
 *         Last modified: May 10, 2017 8:11:37 PM
 */
package edu.sjsu.sidmishraw.examples.prisonersdilemma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

import edu.sjsu.sidmishraw.agencyplatform.core.Agent;
import edu.sjsu.sidmishraw.agencyplatform.core.Facilitator;
import edu.sjsu.sidmishraw.agencyplatform.core.Message;
import edu.sjsu.sidmishraw.agencyplatform.core.Parameter;
import edu.sjsu.sidmishraw.agencyplatform.exceptions.IllegalAgentException;
import edu.sjsu.sidmishraw.agencyplatform.factory.AgentFactory;
import edu.sjsu.sidmishraw.agencyplatform.factory.InstanceFactory;
import edu.sjsu.sidmishraw.examples.prisonersdilemma.constants.PDConstants;
import edu.sjsu.sidmishraw.examples.prisonersdilemma.core.MessageType;
import edu.sjsu.sidmishraw.examples.prisonersdilemma.core.PrisonerMessage;
import edu.sjsu.sidmishraw.examples.prisonersdilemma.factory.PDFactory;
import edu.sjsu.sidmishraw.examples.prisonersdilemma.utils.PDUtils;

/**
 * @author sidmishraw
 *
 *         Qualified Name: edu.sjsu.sidmishraw.examples.prisonersdilemma.core.
 *         PrisonersDilemmaDriver
 *
 */
public class PrisonersDilemmaDriver {
	
	// The main driver for the Prisoner's Dilemma Simulation
	
	/**
	 * @param args
	 */
	@SuppressWarnings({ "unchecked" })
	public static void main(String[] args) {
		
		Facilitator<PrisonerMessage> society = InstanceFactory
				.getInstance(Facilitator.class);
		
		AgentFactory agentFactory = InstanceFactory.getInstance(AgentFactory.class);
		
		PDFactory pdFactory = InstanceFactory.getInstance(PDFactory.class);
		
		// some constants
		final int MAX_PRISONER_COUNT = 16;
		final int MAX_ROUNDS = 20;
		
		for (int i = 1; i <= MAX_PRISONER_COUNT; i++) {
			
			// make a prisoner
			Agent<PrisonerMessage> prisoner = agentFactory.makeAgent(
					"Prisoner#" + String.valueOf(i), PrisonerMessage.class,
					
					// the score is maintained by each Prisoner
					new Parameter(PDConstants.SCORE, 0),
					
					// the prisoners need to maintain the
					// history of games played against each other prisoner
					// and decide their strategy accordingly
					new Parameter(PDConstants.HISTORY,
							new HashMap<String, List<Boolean>>()),
					
					// each prisoner keeps track of max rounds played, which is
					// deducted after
					// he interacts with one of the agents
					new Parameter(PDConstants.ROUNDS_REMAINING, MAX_ROUNDS));
			
			// define the behaviors of the prisoner
			agentFactory.addBehaviors(prisoner,
					new Parameter(PDConstants.INTERACT,
							new Consumer<Agent<PrisonerMessage>>() {
								
								/**
								 * Interact with other prisoners of the
								 * simulation
								 * play the game
								 */
								@Override
								public void accept(Agent<PrisonerMessage> me) {
									
									synchronized (me) {
										
										// ask the society to set my partner
										me.getFacilitator().getPartner(me);
										
										if (null != me.getPartner()) {
											
											// use the history to generate the
											// message
											// content
											MessageType myMessage = ((Function<Agent<PrisonerMessage>, MessageType>) me
													.getParameter(Function.class,
															PDConstants.GENERATE_MY_MSG_CONTENT))
																	.apply(me);
											
											PrisonerMessage msg = pdFactory.makeMessage(
													me,
													myMessage);
											
											Message<PrisonerMessage> messageWrapper = new Message<PrisonerMessage>(
													msg);
											
											// ask the facilitator to send in
											// the
											// message
											// for you
											me.getFacilitator().sendMessage(me,
													messageWrapper);
											
											// reduce the max rounds by one,
											// need to stop when the nbr
											// reaches 0
											int roundsRemaining = (Integer) me
													.getParameter(Integer.class,
															PDConstants.ROUNDS_REMAINING);
											
											roundsRemaining--;
											
											me.setParameter(PDConstants.ROUNDS_REMAINING,
													roundsRemaining);
										} else {
											
											System.out
													.println(
															"No partner to send a play PrisonersDilemma with!");
										}
									}
								}
							}),
					new Parameter(PDConstants.GENERATE_MY_MSG_CONTENT,
							new Function<Agent<PrisonerMessage>, MessageType>() {
								
								/**
								 * Generates the response for the prisoner after
								 * looking at the history
								 * of the partner agent
								 */
								@Override
								public MessageType apply(Agent<PrisonerMessage> me) {
									
									synchronized (me) {
										
										Map<String, List<Boolean>> historyTable = (Map<String, List<Boolean>>) me
												.getParameter(Map.class, PDConstants.HISTORY);
										
										List<Boolean> pastScores = historyTable
												.get(me.getPartner().getDescription());
										
										boolean value = true;
										
										// use the stream of pastscores with the
										// partner to deduce the next
										// step of action
										if (null != pastScores) {
											
											value = pastScores.stream().reduce(true,
													(acc, val) -> {
														
														return PDUtils.stratAccumulator(acc,
																val);
													});
													
											pastScores.add(value);
										} else {
											
											// if there is no history yet, just
											Random random = new Random();
											int flipper = random.nextInt(10);
											value = flipper % 2 == 0;
											
											// add history to the
											pastScores = new ArrayList<>();
											
											pastScores.add(value);
											
											historyTable.put(me.getPartner().getDescription(),
													pastScores);
										}
										
										MessageType content = null;
										
										if (value) {
											
											content = MessageType.COOPERATE;
										} else {
											
											content = MessageType.DEFECT;
										}
										
										return content;
									}
								}
							}));
			
			// sets the update logic for the prisoner
			prisoner.setUpdateLogic(() -> {
				
				int numRounds = prisoner.getParameter(Integer.class,
						PDConstants.ROUNDS_REMAINING);
				
				// stop if there are no remaining rounds
				if (numRounds <= 0) {
					
					prisoner.halt();
					return;
				}
				
				// TODO interact
				((Consumer<Agent<PrisonerMessage>>) prisoner.getParameter(Consumer.class,
						PDConstants.INTERACT)).accept(prisoner);
				
				// TODO check for msg from msg queue
				// if msg exists, reply back
				Message<PrisonerMessage> currMsg = null;
				
				while (null != (currMsg = prisoner.getMailbox().poll())) {
					
					PrisonerMessage content = currMsg.getContent();
					
					if (null != content.getSender()) {
						
						// set the partner to the new guy
						// ask the facilitator to send a msg to him
						// drop the partner
						// compute new score
					}
				}
				
				// TODO update score
			});
			
			// add the prisoner to the society
			try {
				
				society.add(prisoner);
			} catch (IllegalAgentException e) {
				
				e.printStackTrace();
			}
		}
		
		// for multithreading or single threaded mode of execution
		society.setMultiThread(false);
		society.start();
	}
}
