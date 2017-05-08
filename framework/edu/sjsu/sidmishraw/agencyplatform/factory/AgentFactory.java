/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.agencyplatform.factory
 * File: AgentFactory.java
 * 
 * @author sidmishraw
 *         Last modified: May 3, 2017 3:16:34 PM
 */
package edu.sjsu.sidmishraw.agencyplatform.factory;

import java.util.Arrays;

import edu.sjsu.sidmishraw.agencyplatform.core.Agent;
import edu.sjsu.sidmishraw.agencyplatform.core.Parameter;

/**
 * Makes new Agents for the world.
 * 
 * @author sidmishraw
 *
 *         Qualified Name:
 *         edu.sjsu.sidmishraw.agencyplatform.factory.AgentFactory
 *
 */
public class AgentFactory {
	
	public static int agentCount = 0;
	
	/**
	 * Makes a brand new agent for the world.
	 * 
	 * @param agentDescription
	 *            -- The description contained within the agent body
	 * @param updateLogic
	 *            -- The update logic to be executed by the agent
	 * 
	 * @param messageContentClass
	 *            -- The class for the message content <T>
	 * 
	 * @param parameters
	 *            --- all other instance specific parameters
	 * 
	 * @return {@link Agent}<T>
	 */
	public <T> Agent<T> makeAgent(String agentDescription, Class<T> messageContentClass, Parameter... parameters) {
		
		Agent<T> agent = new Agent<>(AgentFactory.agentCount, false, agentDescription);
		
		Arrays.asList(parameters).forEach((parameter) -> {
			
			agent.setParameter(parameter.parameterName, parameter.value);
		});
		
		agentCount++;
		
		return agent;
	}
	
	/**
	 * Used to add behaviors for the agent
	 * Behaviors are functional interfaces : for eg:
	 * Adding a defend behavior to the gladiator agent, the functional interface
	 * is a BiConsumer from java.utils.function
	 * 
	 * agentFactory.addBehaviors(gladiator, new Parameter("defend", new
	 * BiConsumer<Agent<Strike>, Strike>() {
	 * 
	 * @Override
	 * 			public void accept(Agent<Strike> me, Strike t) {
	 * 
	 *           Shield shield = me.getParameter(Shield.class, "shield");
	 * 
	 *           // ask the shield to reduce the strike strenght before
	 *           // dealing dmg
	 *           Strike newStrike = shield.reduceStrike(t);
	 * 
	 *           float health = me.getParameter(Float.class, "health");
	 * 
	 *           // reduce the health according to the modified dmg, taking
	 *           // into account all the shield skins etc
	 *           health = health - (newStrike.getStrength() * health);
	 * 
	 *           // set the new health of the gladiator
	 *           me.setParameter("health", health);
	 *           }
	 *           }));
	 * 
	 * @param agent
	 * @param behaviors
	 */
	public <T> void addBehaviors(Agent<T> agent, Parameter... behaviors) {
		
		Arrays.asList(behaviors).forEach(behavior -> {
			
			agent.setParameter(behavior.parameterName, behavior.value);
		});
	}
}
