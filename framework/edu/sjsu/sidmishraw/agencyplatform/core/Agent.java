/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.agencyplatform.core
 * File: Agent.java
 * 
 * @author sidmishraw
 *         Last modified: May 3, 2017 12:00:37 AM
 */
package edu.sjsu.sidmishraw.agencyplatform.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import edu.sjsu.sidmishraw.agencyplatform.service.UpdateLogic;

/**
 * @author sidmishraw
 *
 *         Qualified Name: edu.sjsu.sidmishraw.agencyplatform.core.Agent
 *
 */
public final class Agent<T> implements Runnable {
	
	// id of the agent in the world
	private int											id;
	
	// flag that tells if the agent is dead
	private Boolean										dead			= false;
	
	// a flag used by the agent to show that it is not ready for a partner
	private boolean										ready			= true;
	
	// description of the agent
	private String										description		= "";
	
	// the mailbox is the queue of messages that arrive from the partners
	// every time the agent's update is called, it will poll a mesasge from the
	// queue and execute it
	private volatile ConcurrentLinkedQueue<Message<T>>	mailbox			= new ConcurrentLinkedQueue<>();
	
	// a map holding all the parameters of the agent, this is my approach to
	// have only one agent class and customize the agent through just the update
	// logic
	private volatile ConcurrentMap<String, Object>		parametersMap	= new ConcurrentHashMap<>();
	
	// initial update logic is a DO-NOTHING anonymous object
	// the non-sugarcoated implementation of the lambda
	private UpdateLogic									updateLogic		= () -> {};
	
	// the partner agent reference
	private volatile Agent<T>							partner			= null;
	
	// the facilitator reference
	private Facilitator<T>								facilitator		= null;
	
	/**
	 * 
	 */
	public Agent() {}
	
	/**
	 * @param id
	 * @param dead
	 * @param description
	 */
	public Agent(int id, Boolean dead, String description) {
		
		this.id = id;
		this.dead = dead;
		this.description = description;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		// as long as the agent is not dead
		// it will keep updating
		while (!this.dead) {
			
			update();
		}
		
		System.out.println("The agent " + this.description + " is dead! T_T");
	}
	
	/**
	 * Intrinsic update functionality that is to be implemented in the
	 * framework.
	 */
	public final synchronized void update() {
		
		// it executes the update logic specified by the
		// user of the framework
		this.updateLogic.executeUpdateLogic();
	}
	
	/**
	 * @return the updateLogic
	 */
	public UpdateLogic getUpdateLogic() {
		
		return this.updateLogic;
	}
	
	/**
	 * @param updateLogic
	 *            the updateLogic to set
	 */
	public void setUpdateLogic(UpdateLogic updateLogic) {
		
		this.updateLogic = updateLogic;
	}
	
	/**
	 * {Kills the agent} -- Halts all the processes of the agent
	 * and signals that the agent is dead. It does this by setting the
	 * `dead` boolean flag to true.
	 * Notifys all the threads waiting on any resources the agent was using.
	 * Then does a Thread.yield() to yield all the resources.
	 */
	public void halt() {
		
		this.dead = true;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		
		return this.id;
	}
	
	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		
		this.id = id;
	}
	
	/**
	 * @return the dead
	 */
	public Boolean getDead() {
		
		return this.dead;
	}
	
	/**
	 * @param dead
	 *            the dead to set
	 */
	public void setDead(Boolean dead) {
		
		this.dead = dead;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		
		return this.description;
	}
	
	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		
		this.description = description;
	}
	
	/**
	 * @return the mailbox
	 */
	public ConcurrentLinkedQueue<Message<T>> getMailbox() {
		
		return this.mailbox;
	}
	
	/**
	 * @param mailbox
	 *            the mailbox to set
	 */
	public void setMailbox(ConcurrentLinkedQueue<Message<T>> mailbox) {
		
		this.mailbox = mailbox;
	}
	
	/**
	 * It allows the agent to store additional data specific to the agent.
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
	 * Sets the additional information needed by the agent instance
	 * 
	 * @param parameterName
	 * @param parameterValue
	 */
	public <K> void setParameter(String parameterName, K parameterValue) {
		
		this.parametersMap.put(parameterName, parameterValue);
	}
	
	/**
	 * @return the partner
	 */
	public Agent<T> getPartner() {
		return this.partner;
	}
	
	/**
	 * @param partner
	 *            the partner to set
	 */
	public void setPartner(Agent<T> partner) {
		this.partner = partner;
	}
	
	/**
	 * @return the world
	 */
	public Facilitator<T> getFacilitator() {
		return this.facilitator;
	}
	
	/**
	 * @param facilitator
	 *            the facilitator to set
	 */
	public void setFacilitator(Facilitator<T> facilitator) {
		this.facilitator = facilitator;
	}
	
	/**
	 * Sets the partner to null, i.e, drops the partner.
	 */
	public void dropMyPartner() {
		
		this.partner = null;
	}
	
	/**
	 * @return the ready
	 */
	public boolean isReady() {
		return this.ready;
	}
	
	/**
	 * @param ready
	 *            the ready to set
	 */
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Agent [id=" + this.id + ", description=" + this.description + "]";
	}
}
