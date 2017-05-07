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
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.sjsu.sidmishraw.agencyplatform.exceptions.IllegalAgentException;

/**
 * @author sidmishraw
 *
 *         Qualified Name: edu.sjsu.sidmishraw.agencyplatform.core.Facilitator
 *
 */
public class Facilitator<T> implements Runnable {
	
	// the flag that says if the facilitator is single threaded
	// or multi-threaded
	private Boolean										multiThread		= true;
	
	// a queue holding the agents that are without partners and are waiting for
	// partners
	private volatile ConcurrentLinkedQueue<Agent<T>>	freeAgents		= new ConcurrentLinkedQueue<>();
	
	// holds the agents that have a partner
	private volatile ConcurrentLinkedQueue<Agent<T>>	bookedAgents	= new ConcurrentLinkedQueue<>();
	
	/**
	 * Returns the partner for the agent that calls this method on the
	 * Facilitator.
	 * 
	 * @param agent
	 * 
	 * @return {@link Agent}: The partner agent
	 */
	public synchronized void getPartner(Agent<T> agent) {
		
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
			// if the agent hasn't been assigned to anyone else
			// assign the wannabePartner to the agent and vice-versa
			if (null != wannabePartnerAgent && !agent.equals(wannabePartnerAgent)
					&& null == wannabePartnerAgent.getPartner()) {
				
				// set the partners
				wannabePartnerAgent.setPartner(agent);
				agent.setPartner(wannabePartnerAgent);
				
				// book the agents
				this.bookedAgents.add(wannabePartnerAgent);
				this.bookedAgents.add(agent);
				
				break;
			} else if (wannabePartnerAgent.equals(agent) && null == wannabePartnerAgent.getPartner()) {
				
				// put back the polled agent to the free pool
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
	public synchronized void dropPartner(Agent<T> agent) {
		System.out.println("Free agents before drop by " + agent.getDescription() + " = " + this.freeAgents.size());
		
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
		
		this.freeAgents.add(agent);
		this.freeAgents.add(prevPartner);
		
		System.out.println("Free agents after drop by " + agent.getDescription() + " = " + this.freeAgents.size());
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
	public void add(Agent<T> agent) throws IllegalAgentException {
		
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
	public void start() {
		
		System.out.println("Facilatator started");
		
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
		
		if (this.freeAgents.size() > 0) {
			
			this.start();
		} else {
			
			System.out.println("No agents to facilitate.");
		}
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
}
