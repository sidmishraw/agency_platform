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
import edu.sjsu.sidmishraw.agencyplatform.core.Parameter;
import edu.sjsu.sidmishraw.agencyplatform.exceptions.IllegalAgentException;
import edu.sjsu.sidmishraw.agencyplatform.factory.AgentFactory;
import edu.sjsu.sidmishraw.agencyplatform.factory.InstanceFactory;
import edu.sjsu.sidmishraw.examples.prisonersdilemma.constants.PDConstants;
import edu.sjsu.sidmishraw.examples.prisonersdilemma.core.MessageType;
import edu.sjsu.sidmishraw.examples.prisonersdilemma.core.PrintScores;
import edu.sjsu.sidmishraw.examples.prisonersdilemma.core.PrisonerMessage;
import edu.sjsu.sidmishraw.examples.prisonersdilemma.core.ScoreComputation;
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
	/*
	 * Change of plans for prisoner's dilemma implementation.
	 * Note: all this needs to occur inside a synchronized block:
	 * 
	 * 1. Changing the interaction between the prisoners, I suggest that the
	 * moment
	 * iteract() is called upon the prisoner, it will ask the society for a
	 * partner, if the partner exists:
	 * then it will generate it's response(MessageType) depending upon the past
	 * history.
	 * Then inside a synchronized block, the partner if it exists will also
	 * generate the response.
	 * After both the prisoners have generated their responses, it will sent to
	 * the
	 * society who will compute the score and update the scores of the prisoners.
	 */
	/**
	 * @param args
	 */
	@SuppressWarnings({ "unchecked" })
	public static void main(String[] args) {
		
		Facilitator<PrisonerMessage> society = InstanceFactory
				.getInstance(Facilitator.class);
		
		AgentFactory agentFactory = InstanceFactory.getInstance(AgentFactory.class);
		
		// add score computation the society
		society.setParameter(PDConstants.COMPUTE_SCORES, new ScoreComputation() {
			
			/**
			 * Score computation chart:
			 * player1/player2 DEFECT COOPERATE
			 * DEFECT 1pt/1pt 5pt/0pt
			 * COOPERATE opt/5pt 3pt/3pt
			 */
			@Override
			public void compute(Agent<PrisonerMessage> prisoner1, MessageType prisoner1Msg,
					Agent<PrisonerMessage> prisoner2, MessageType prisoner2Msg) {
				
				synchronized (prisoner1) {
					
					synchronized (prisoner2) {
						
						// for message construction
						StringBuffer buffer = new StringBuffer();
						
						// Both prisoners get 1 point each
						// get old scores
						int p1OldScore = prisoner1.getParameter(Integer.class, PDConstants.SCORE);
						int p2OldScore = prisoner2.getParameter(Integer.class, PDConstants.SCORE);
						
						// new scores
						int p1NewScore = p1OldScore;
						int p2NewScore = p2OldScore;
						
						if (prisoner1Msg.equals(MessageType.DEFECT)
								&& prisoner2Msg.equals(MessageType.DEFECT)) {
							
							// compute the new scores
							// both get 1 point each
							p1NewScore = p1OldScore + 1;
							p2NewScore = p2OldScore + 1;
							
							buffer.append("Both the prisoners ")
									.append(prisoner1.getDescription())
									.append(" and ")
									.append(prisoner2.getDescription())
									.append(
											" have chosen to DEFECT, so they get 1 point each. So, scores:\n\t");
						} else if (prisoner1Msg.equals(MessageType.DEFECT)
								&& prisoner2Msg.equals(MessageType.COOPERATE)) {
							
							// compute the new scores
							// prisoner1 gets 5 points for defection
							p1NewScore = p1OldScore + 5;
							
							buffer.append("The prisoner ")
									.append(prisoner1.getDescription())
									.append(" has `DEFECT`ed and the prisoner ")
									.append(prisoner2.getDescription())
									.append(
											" has `COOPERATE`ed , so defecter gets 5 points and the cooperater gets 0 points. So, scores:\n\t");
						} else if (prisoner1Msg.equals(MessageType.COOPERATE)
								&& prisoner2Msg.equals(MessageType.DEFECT)) {
							
							// compute the new scores
							// prisoner2 gets 5 points for defection
							p2NewScore = p2OldScore + 5;
							
							buffer.append("The prisoner ")
									.append(prisoner1.getDescription())
									.append(" has `COOPERATE`ed and the prisoner ")
									.append(prisoner2.getDescription())
									.append(
											" has `DEFECT`ed , so defecter gets 5 points and the cooperater gets 0 points. So, scores:\n\t");
						} else if (prisoner1Msg.equals(MessageType.COOPERATE)
								&& prisoner2Msg.equals(MessageType.COOPERATE)) {
							
							// compute the new scores
							// both get 3 points each
							p1NewScore = p1OldScore + 3;
							p2NewScore = p2OldScore + 3;
							
							buffer.append("Both the prisoners ")
									.append(prisoner1.getDescription())
									.append(" and ")
									.append(prisoner2.getDescription())
									.append(
											" have chosen to COOPERATE, so they get 3 points each. So, scores:\n\t");
						}
						
						// set new scores
						prisoner1.setParameter(PDConstants.SCORE, p1NewScore);
						prisoner2.setParameter(PDConstants.SCORE, p2NewScore);
						
						// print msg
						buffer.append(prisoner1.getDescription() + ": oldScore = ")
								.append(String.valueOf(p1OldScore))
								.append(" newScore = ")
								.append(String.valueOf(p1NewScore))
								.append("\n\t")
								.append(prisoner2.getDescription() + ": oldScore = ")
								.append(String.valueOf(p2OldScore))
								.append(" newScore = ")
								.append(String.valueOf(p2NewScore));
						
						System.out.println(buffer.toString());
					}
				}
			}
		});
		
		// add score print logic
		society.setParameter(PDConstants.PRINT_SCORES, new PrintScores() {
			
			@Override
			public void printScores() {
				
				List<Agent<PrisonerMessage>> deadPrisoners = new ArrayList<>();
				
				// add all the deadPrisoners
				society.getFreeAgents().stream().filter(agent -> agent.getDead())
						.forEach(agent -> deadPrisoners.add(agent));
				society.getBookedAgents().stream().filter(agent -> agent.getDead())
						.forEach(agent -> deadPrisoners.add(agent));
				
				deadPrisoners.forEach(
						prisoner -> System.out.println(prisoner.getDescription() + " final score = "
								+ String
										.valueOf(prisoner.getParameter(Integer.class, PDConstants.SCORE))));
			}
		});
		
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
								
								@Override
								public void accept(Agent<PrisonerMessage> prisoner) {
									
									synchronized (prisoner) {
										
										System.out.println("Prisoner: " + prisoner.getDescription()
												+ " is asking for partner!");
										
										// ask the society to set my partner
										prisoner.getFacilitator().getPartner(prisoner);
										
										Agent<PrisonerMessage> partner = prisoner.getPartner();
										
										if (null != partner) {
											
											System.out.println("Prisoner: " + prisoner.getDescription()
													+ " is playing PD with Prisoner: " + partner.getDescription());
											
											// I have a partner and so can't be bothered
											prisoner.setReady(false);
											
											if (!partner.isReady()) {
												
												prisoner.setReady(true);
												return;
											}
											
											synchronized (partner) {
												
												// partner is ingame, shouldn't call it's update anymore
												// since this one update is enough
												partner.setReady(false);
												
												int prisonerRounds = prisoner.getParameter(Integer.class,
														PDConstants.ROUNDS_REMAINING);
												
												System.out.println("Prisoner: " + prisoner.getDescription()
														+ " has " + prisonerRounds + " remaining");
												
												int partnerRounds = partner.getParameter(Integer.class,
														PDConstants.ROUNDS_REMAINING);
												
												System.out.println("Prisoner: " + partner.getDescription()
														+ " has " + partnerRounds + " remaining");
												
												// use the history to generate the
												// message content for prisoner
												MessageType myMessage = ((Function<Agent<PrisonerMessage>, MessageType>) prisoner
														.getParameter(Function.class,
																PDConstants.GENERATE_MY_MSG_CONTENT))
																		.apply(prisoner);
												
												System.out.println("Prisoner: " + prisoner.getDescription()
														+ " has " + myMessage.toString());
												
												// use the history to generate the
												// message content for partner
												MessageType partnerMessage = ((Function<Agent<PrisonerMessage>, MessageType>) prisoner
														.getParameter(Function.class,
																PDConstants.GENERATE_MY_MSG_CONTENT))
																		.apply(partner);
												
												System.out.println("Prisoner: " + partner.getDescription()
														+ " has " + partnerMessage.toString());
												
												prisoner.getFacilitator()
														.getParameter(ScoreComputation.class,
																PDConstants.COMPUTE_SCORES)
														.compute(prisoner, myMessage, partner, partnerMessage);
												
												prisoner.setParameter(
														PDConstants.ROUNDS_REMAINING, --prisonerRounds);
												
												partner.setParameter(
														PDConstants.ROUNDS_REMAINING, --partnerRounds);
												
												// no longer a partner
												partner.setReady(true);
												
												prisoner.getFacilitator().dropPartner(prisoner);
												
												// no longer have a partner
												prisoner.setReady(true);
											}
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
							}));
			
			// sets the update logic for the prisoner
			prisoner.setUpdateLogic(() -> {
				
				if (prisoner.isReady()) {
					
					System.out.println(prisoner.getDescription() + " Ready");
					int numRounds = prisoner.getParameter(Integer.class,
							PDConstants.ROUNDS_REMAINING);
					
					System.out.println(
							"Rounds remaining for " + prisoner.getDescription() + " = " + numRounds);
					
					// stop if there are no remaining rounds
					if (numRounds <= 0) {
						
						prisoner.halt();
						return;
					} else {
						
						prisoner.getParameter(Consumer.class, PDConstants.INTERACT).accept(prisoner);
					}
				} else {
					
					System.out.println(prisoner.getDescription() + " is not ready for a game yet!");
				}
				
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
		
		// wait till everyone is done with their games
		society.waitForMe();
		
		// print the final scores at the end
		System.out.println();
		System.out.println("After the games with Rounds: " + MAX_ROUNDS);
		society.getParameter(PrintScores.class, PDConstants.PRINT_SCORES).printScores();
		System.out.println("Done!");
	}
}
