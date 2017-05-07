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
}
