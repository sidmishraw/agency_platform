/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.agencyplatform.core
 * File: Facilitator.java
 * 
 * @author sidmishraw
 *         Last modified: May 2, 2017 11:57:49 PM
 */
package edu.sjsu.sidmishraw.agencyplatform.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import edu.sjsu.sidmishraw.agencyplatform.exceptions.IllegalAgentException;

/**
 * @author sidmishraw
 *
 *         Qualified Name: edu.sjsu.sidmishraw.agencyplatform.core.Facilitator
 *
 */
public final class Facilitator<T> implements Runnable {
	
	// the flag that says if the facilitator is single threaded
	// or multi-threaded
	private Boolean										multiThread		= true;
	
	// a queue holding the agents that are without partners and are waiting for
	// partners
	private volatile ConcurrentLinkedQueue<Agent<T>>	freeAgents		= new ConcurrentLinkedQueue<>();
	
	// holds the agents that have a partner
	private volatile ConcurrentLinkedQueue<Agent<T>>	bookedAgents	= new ConcurrentLinkedQueue<>();
	
	/**
	 * The parameter map holding all the necessary extra parameters specific to
	 * the facilitator instance
	 * This is the approach taken to tackle the problem of subclassing
	 * So when I say, `Ultradome is a facilitator`, it should mean Ultradome is
	 * an instance
	 * of Facilitator instead of subclass
	 */
	private volatile ConcurrentMap<String, Object>		parametersMap	= new ConcurrentHashMap<>();
	
	/**
	 * Facilitates the messaging between agents
	 * 
	 * @param agent
	 * @param message
	 */
	public final synchronized void sendMessage(Agent<T> agent, Message<T> message) {
		
		Agent<T> partnerAgent = agent.getPartner();
		
		if (null == partnerAgent || partnerAgent.getDead()) {
			
			System.out.println("No eligible partner to send a message, try again after hooking up");
			return;
		}
		
		if (null != message) {
			
			System.out.println("Added a message from " + agent.getDescription() + " to " + partnerAgent.getDescription()
					+ "'s mailbox.");
			
			partnerAgent.getMailbox().add(message);
		}
	}
	
	/**
	 * Returns the partner for the agent that calls this method on the
	 * Facilitator.
	 * 
	 * @param agent
	 *            -- the requesting agent
	 * 
	 * @return {@link Agent}: The partner agent
	 */
	public final synchronized void getPartner(Agent<T> agent) {
		
		if (null != agent.getPartner() || null == agent) {
			
			return;
		}
		
		// will try 5 times to get a partner else will return null
		int retriesLeft = 5;
		
		while (!(retriesLeft == 0)) {
			
			System.out.println("Retry for " + agent.getDescription() + " #" + retriesLeft);
			// pop from the freeAgents queue
			Agent<T> wannabePartnerAgent = this.freeAgents.poll();
			
			// if the wannabeAgent is not null
			// and it is the not the same agent as requesting agent
			// and it is not dead and is ready for a partner
			// if the agent hasn't been assigned to anyone else
			// assign the wannabePartner to the agent and vice-versa
			if (null != wannabePartnerAgent && !agent.equals(wannabePartnerAgent)
					&& null == wannabePartnerAgent.getPartner() && !wannabePartnerAgent.getDead()
					&& wannabePartnerAgent.isReady()) {
				
				// set the partners
				wannabePartnerAgent.setPartner(agent);
				agent.setPartner(wannabePartnerAgent);
				
				// book the agents
				this.bookedAgents.add(wannabePartnerAgent);
				this.bookedAgents.add(agent);
				
				// remove the agent from freeagent since it is no
				// longer free and is booked
				this.freeAgents.remove(agent);
				
				break;
			} else if (null != wannabePartnerAgent
					&& ((wannabePartnerAgent.equals(agent) && null == wannabePartnerAgent.getPartner())
							|| wannabePartnerAgent.getDead() || !wannabePartnerAgent.isReady())) {
				
				// put back the polled agent to the free pool
				// by default all dead agents are kept in the free queue
				this.freeAgents.add(wannabePartnerAgent);
			}
			
			// decrement the retrylimit
			retriesLeft--;
		}
	}
	
	/**
	 * Drops the agent's partner and adds them to the free queue
	 * 
	 * @param agent
	 */
	public final synchronized void dropPartner(Agent<T> agent) {
		
		System.out.println("Free agents before drop by " + agent.getDescription() + " = " + this.freeAgents.size()
				+ " Booked agents before drop by " + agent.getDescription() + " = " + this.bookedAgents.size());
		
		Agent<T> prevPartner = agent.getPartner();
		
		// if the agent doesn't have a partner, there is none to drop
		// it will return right away, otherwise if the agent is without a
		// partner and it is not on the freeAgents queue, then I add it to the
		// queue and return right away.
		if (null == prevPartner && this.freeAgents.contains(agent)) {
			
			return;
		} else if (null == prevPartner && !this.freeAgents.contains(agent)) {
			
			this.freeAgents.add(agent);
			return;
		}
		
		// otherwise, dropped the partners and added both to the free
		// agent queue
		prevPartner.dropPartner();
		agent.dropPartner();
		
		// remove them from the bookedagents
		this.bookedAgents.remove(agent);
		this.bookedAgents.remove(prevPartner);
		
		// add them to the free agents queue
		this.freeAgents.add(agent);
		this.freeAgents.add(prevPartner);
		
		System.out.println("Free agents after drop by " + agent.getDescription() + " = " + this.freeAgents.size()
				+ " Booked agents after drop by " + agent.getDescription() + " = " + this.bookedAgents.size());
	}
	
	/**
	 * Adds the agent to the Facilitator's agents list. It also
	 * sets the Facilitator in the agent's instance so that the agent knows of
	 * the Facilitator.
	 * 
	 * @param agent
	 *            {@link Agent}<T> -- T is the messagecontent class
	 * 
	 * @throws IllegalAgentException
	 */
	public final void add(Agent<T> agent) throws IllegalAgentException {
		
		if (null == agent) {
			
			throw new IllegalAgentException("The agent is null.");
		}
		
		// set this faciliator as the facilitator of the agent
		// and add the agent to the freeAgents queue
		agent.setFacilitator(this);
		this.freeAgents.add(agent);
	}
	
	/**
	 * Starts the facilitating world.
	 */
	public final void start() {
		
		if (this.freeAgents.size() > 0) {
			
			if (multiThread) {
				
				new Thread(this).start();
			} else {
				
				this.singleThreadedRun();
			}
		} else {
			
			System.out.println("No agents to facilitate.");
		}
	}
	
	/**
	 * Single threaded run for the Facilitator
	 */
	private final void singleThreadedRun() {
		
		System.out.println("Single threaded running facilitator");
		
		List<Agent<T>> deadAgents = new ArrayList<>();
		
		int size = this.freeAgents.size() + this.bookedAgents.size();
		
		// the facilitator is going to be alive till all the agents are alive
		// run till facilitator is alive, i.e all the agents are dead, both
		// booked and free agents
		while (size != deadAgents.size()) {
			
			for (Agent<T> agent : this.freeAgents.stream().filter(agent -> !agent.getDead())
					.collect(Collectors.toList())) {
				
				agent.update();
				
				if (agent.getDead()) {
					
					deadAgents.add(agent);
					
					System.out.println("The agent " + agent.getDescription() + " is dead! T_T");
				}
			}
			
			for (Agent<T> agent : this.bookedAgents.stream().filter(agent -> !agent.getDead())
					.collect(Collectors.toList())) {
				
				agent.update();
				
				if (agent.getDead()) {
					
					deadAgents.add(agent);
					
					System.out.println("The agent " + agent.getDescription() + " is dead! T_T");
				}
			}
		}
	}
	
	// /**
	// *
	// * @param agent
	// * @param message
	// */
	// public void send(Agent<T> agent, Message<T> message) {
	//
	// System.out.println(
	// "Sending a message to agent: " + agent.toString() + "with content: " +
	// message.getContent().toString());
	// }
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		System.out.println("Facilatator started --- Multithreaded mode");
		
		// I use the list of running threads to call join upon the
		// agents
		List<Thread> runningThreads = new ArrayList<>();
		
		// start all the agents
		this.freeAgents.forEach(agent -> {
			
			Thread t = new Thread(agent);
			runningThreads.add(t);
			t.start();
		});
		
		// join on all the child threads -- agents to die
		runningThreads.forEach(thread -> {
			
			try {
				
				thread.join();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * @return the multiThread
	 */
	public Boolean getMultiThread() {
		
		return this.multiThread;
	}
	
	/**
	 * @param multiThread
	 *            the multiThread to set
	 */
	public void setMultiThread(Boolean multiThread) {
		
		this.multiThread = multiThread;
	}
	
	/**
	 * @return the freeAgents
	 */
	public ConcurrentLinkedQueue<Agent<T>> getFreeAgents() {
		
		return this.freeAgents;
	}
	
	/**
	 * @param freeAgents
	 *            the freeAgents to set
	 */
	public void setFreeAgents(ConcurrentLinkedQueue<Agent<T>> freeAgents) {
		
		this.freeAgents = freeAgents;
	}
	
	/**
	 * @return the bookedAgents
	 */
	public ConcurrentLinkedQueue<Agent<T>> getBookedAgents() {
		
		return this.bookedAgents;
	}
	
	/**
	 * @param bookedAgents
	 *            the bookedAgents to set
	 */
	public void setBookedAgents(ConcurrentLinkedQueue<Agent<T>> bookedAgents) {
		
		this.bookedAgents = bookedAgents;
	}
	
	/**
	 * It allows the facilitator to store additional data specific to the
	 * facilitator.
	 * The parameters are added by the user of the framework
	 * 
	 * @param clasz
	 * @param parameterName
	 * @return parameter value K
	 */
	@SuppressWarnings("unchecked")
	public <K> K getParameter(Class<K> clasZ, String parameterName) {
		
		return (K) this.parametersMap.get(parameterName);
	}
	
	/**
	 * Sets the additional information needed by the facilitator instance
	 * 
	 * @param parameterName
	 * @param parameterValue
	 */
	public <K> void setParameter(String parameterName, K parameterValue) {
		
		this.parametersMap.put(parameterName, parameterValue);
	}
}
