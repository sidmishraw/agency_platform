/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.agencyplatform
 * File: TestDriver.java
 * 
 * @author sidmishraw
 *         Last modified: May 4, 2017 4:20:54 PM
 */
package edu.sjsu.sidmishraw.agencyplatform;

import edu.sjsu.sidmishraw.agencyplatform.core.Agent;
import edu.sjsu.sidmishraw.agencyplatform.core.Facilitator;
import edu.sjsu.sidmishraw.agencyplatform.factory.AgentFactory;
import edu.sjsu.sidmishraw.agencyplatform.factory.InstanceFactory;

/**
 * @author sidmishraw
 *
 *         Qualified Name: edu.sjsu.sidmishraw.agencyplatform.TestDriver
 *
 */
public class TestDriver {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// world is singleton
		@SuppressWarnings("unchecked")
		Facilitator<String> world = InstanceFactory.getInstance(Facilitator.class);
		
		Agent<String> agent1 = InstanceFactory.getInstance(AgentFactory.class).makeAgent("Agent 1", String.class);
		
		agent1.setUpdateLogic(() -> {
			
			System.out.println(agent1.getDescription());
			
			agent1.getFacilitator().getPartner(agent1);
			
			System.out.println(agent1 + "partner = " + agent1.getPartner());
			
			agent1.getFacilitator().dropPartner(agent1);
			
			System.out.println(agent1 + "partner = " + agent1.getPartner());
			
			agent1.halt();
		});
		
		Agent<String> agent2 = InstanceFactory.getInstance(AgentFactory.class).makeAgent("Agent 2", String.class);
		
		agent2.setUpdateLogic(() -> {
			
			System.out.println(agent2.getDescription());
			
			agent2.getFacilitator().getPartner(agent2);
			
			System.out.println(agent2 + "partner = " + agent2.getPartner());
			
			agent2.getFacilitator().dropPartner(agent2);
			
			System.out.println(agent2 + "partner = " + agent2.getPartner());
			
			agent2.halt();
		});
		
		Agent<String> agent3 = InstanceFactory.getInstance(AgentFactory.class).makeAgent("Agent 3", String.class);
		
		agent3.setUpdateLogic(() -> {
			
			System.out.println(agent3.getDescription());
			
			agent3.getFacilitator().getPartner(agent3);
			
			System.out.println(agent3 + "partner = " + agent3.getPartner());
			
			agent3.getFacilitator().dropPartner(agent3);
			
			System.out.println(agent3 + "partner = " + agent3.getPartner());
			
			agent3.halt();
		});
		
		try {
			
			world.add(agent1);
			world.add(agent2);
			world.add(agent3);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		new Thread(world).start();
	}
	
}
