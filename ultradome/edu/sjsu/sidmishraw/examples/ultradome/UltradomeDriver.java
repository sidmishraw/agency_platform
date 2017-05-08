/**
 * Project: AgencyPlatform
 * Package: edu.sjsu.sidmishraw.examples.ultradome.core
 * File: Ultradome.java
 * 
 * @author sidmishraw
 *         Last modified: May 7, 2017 11:15:25 AM
 */
package edu.sjsu.sidmishraw.examples.ultradome;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;

import edu.sjsu.sidmishraw.agencyplatform.core.Agent;
import edu.sjsu.sidmishraw.agencyplatform.core.Facilitator;
import edu.sjsu.sidmishraw.agencyplatform.core.Message;
import edu.sjsu.sidmishraw.agencyplatform.core.Parameter;
import edu.sjsu.sidmishraw.agencyplatform.exceptions.IllegalAgentException;
import edu.sjsu.sidmishraw.agencyplatform.factory.AgentFactory;
import edu.sjsu.sidmishraw.agencyplatform.factory.InstanceFactory;
import edu.sjsu.sidmishraw.examples.ultradome.core.MedicineBowl;
import edu.sjsu.sidmishraw.examples.ultradome.core.Shield;
import edu.sjsu.sidmishraw.examples.ultradome.core.ShieldSkin;
import edu.sjsu.sidmishraw.examples.ultradome.core.Strike;
import edu.sjsu.sidmishraw.examples.ultradome.core.Weapon;
import edu.sjsu.sidmishraw.examples.ultradome.core.WeaponStrikeType;
import edu.sjsu.sidmishraw.examples.ultradome.core.WeaponType;
import edu.sjsu.sidmishraw.examples.ultradome.factory.UltradomeFactory;

/**
 * @author sidmishraw
 *
 *         Qualified Name:
 *         edu.sjsu.sidmishraw.examples.ultradome.core.UltradomeDriver
 *
 */
public class UltradomeDriver {
	
	/**
	 * Main driver for the ultradome simulation
	 * 
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		/**
		 * The ultradome is the facilitator, in my framework, I don't let the
		 * classes be subclassed
		 * rather I want them to be instantiated and provided their specific
		 * logic for execution
		 * `code as data` style.
		 */
		Facilitator<Strike> ultradome = InstanceFactory.getInstance(Facilitator.class);
		
		// for making agents and assigning them behaviors
		AgentFactory agentFactory = InstanceFactory.getInstance(AgentFactory.class);
		
		// for ultradome specific factory manufacturing tasks
		UltradomeFactory ultradomeFactory = InstanceFactory.getInstance(UltradomeFactory.class);
		
		// add weapons, shield skins and medicine bowls to the ultradome world
		ultradome.setParameter(UltradomeFactory.WEAPONS, ultradomeFactory.makeWorldWeapons());
		ultradome.setParameter(UltradomeFactory.SHIELD_SKINS, ultradomeFactory.makeWorldShieldSkins());
		ultradome.setParameter(UltradomeFactory.MEDICINE_BOWLS, ultradomeFactory.makeWorldMedicineBowls());
		
		// let the arena has 10 gladiators
		final int MAX_GLADIATORS = 5;
		
		final float MAX_HP = 10.0F;
		
		for (int counter = 1; counter <= MAX_GLADIATORS; counter++) {
			
			Agent<Strike> gladiator = agentFactory.makeAgent("Gladiator#" + String.valueOf(counter), Strike.class,
					new Parameter("health", MAX_HP),
					new Parameter("weapon", ultradomeFactory.makeWeapon("Baby Sword", WeaponType.SWORD, () -> {
						
						Strike strike = new Strike("Baby sword strike from hell!!!", 0.01F, WeaponStrikeType.IRON);
						
						return strike;
					})), new Parameter("shield", new Shield()));
			
			agentFactory.addBehaviors(gladiator, new Parameter("defend", new BiConsumer<Agent<Strike>, Strike>() {
				
				/**
				 * The gladiator accepts the incoming strike and updates his
				 * health
				 * after reducing the damage and recalculating the incoming
				 * strike strength
				 * as it passes through the various shield skins adorned by the
				 * his shield.
				 * 
				 */
				@Override
				public void accept(Agent<Strike> me, Strike t) {
					
					Shield shield = me.getParameter(Shield.class, "shield");
					
					// ask the shield to reduce the strike strength before
					// dealing dmg
					Strike newStrike = shield.reduceStrike(t);
					
					float health = me.getParameter(Float.class, "health");
					
					// reduce the health according to the modified dmg, taking
					// into account all the shield skins etc
					health = health - (newStrike.getStrength() * health);
					
					// set the new health of the gladiator
					me.setParameter("health", health);
				}
			}), new Parameter("strengthenShield", new BiConsumer<Agent<Strike>, ShieldSkin>() {
				
				/**
				 * The gladiator accepts a new shield skin from the world and
				 * applies it on his shield
				 */
				@Override
				public void accept(Agent<Strike> me, ShieldSkin shieldSkin) {
					
					Shield shield = me.getParameter(Shield.class, "shield");
					shield.addSkin(shieldSkin);
				}
			}), new Parameter("interact", new BiConsumer<Agent<Strike>, Agent<Strike>>() {
				
				/**
				 * Interacts with other gladiators, makes a strike and sends it
				 * in.
				 */
				@Override
				public void accept(Agent<Strike> me, Agent<Strike> myPartner) {
					
					// don't know why myPartner was included into the mix, but
					// whatever
					Strike myStrike = me.getParameter(Weapon.class, "weapon").makeStrike();
					
					me.getFacilitator().sendMessage(me, new Message<>(myStrike));
				}
			}));
			
			/**
			 * I was tempted to keep this outside the factory's make methods
			 * because
			 * I felt it was too cramped.
			 */
			gladiator.setUpdateLogic(() -> {
				
				if (gladiator.getParameter(Float.class, "health") == 0.0F) {
					
					gladiator.halt();
				}
				
				// update health according to all the strikes received till date
				Message<Strike> msg = null;
				
				float currentHealth = gladiator.getParameter(Float.class, "health");
				
				while (null != (msg = gladiator.getMailbox().poll())) {
					
					if (null != msg) {
						
						Strike strike = msg.getContent();
						
						currentHealth = currentHealth - (strike.getStrength() * currentHealth);
						
						if (currentHealth == 0.0F) {
							
							gladiator.halt();
						}
					}
				}
				
				gladiator.setParameter("health", currentHealth);
				
				/**
				 * During a gladiator's update cycle a random integer called
				 * luck is generated.
				 * 
				 * If 0 <= luck < 10, then the gladiator
				 * drinks enough medicine to restore his health to 100% or until
				 * the medicine is gone.
				 * 
				 * If 10 <= luck < 20, then the gladiator
				 * selects a new weapon, assuming any weapons remain.
				 * 
				 * If 20 < luck <= 30, then the gladiator selects a shield skin
				 * and strengthens his shield with it, assuming any shield skins
				 * remain.
				 * 
				 * Otherwise, the gladiator attacks (interacts with)
				 * another gladiator.
				 */
				Random random = new Random();
				
				int luck = random.nextInt(40);
				
				System.out.println("Gladitor: " + gladiator.getDescription() + " has luck = " + luck);
				
				if (0 <= luck && luck < 10) {
					
					// drink medicine
					MedicineBowl medicine = ((ConcurrentLinkedQueue<MedicineBowl>) gladiator.getFacilitator()
							.getParameter(ConcurrentLinkedQueue.class, UltradomeFactory.MEDICINE_BOWLS)).poll();
					
					if (null != medicine) {
						
						while (medicine.getContents() != 0 && gladiator.getParameter(Float.class, "health") < MAX_HP) {
							
							System.out.println("Healing gladiator: " + gladiator.getDescription() + "by +1");
							
							float incrementHealth = gladiator.getParameter(Float.class, "health") + 1;
							gladiator.setParameter("health", incrementHealth);
							medicine.consume();
						}
					}
				} else if (10 <= luck && luck < 20) {
					
					Weapon weapon = ((ConcurrentLinkedQueue<Weapon>) gladiator.getFacilitator()
							.getParameter(ConcurrentLinkedQueue.class, UltradomeFactory.WEAPONS)).poll();
					
					if (null != weapon) {
						
						Weapon oldWeapon = gladiator.getParameter(Weapon.class, "weapon");
						
						System.out.println("Swtiched weapon to for gladiator: " + gladiator.getDescription() + " to "
								+ weapon.getName() + " from " + oldWeapon.getName());
						
						gladiator.setParameter("weapon", weapon);
					}
				} else if (20 <= luck && luck <= 30) {
					
					ShieldSkin shieldSkin = ((ConcurrentLinkedQueue<ShieldSkin>) gladiator.getFacilitator()
							.getParameter(ConcurrentLinkedQueue.class, UltradomeFactory.SHIELD_SKINS)).poll();
					
					if (null != shieldSkin) {
						
						System.out.println("Applying new shield skin for gladiator: " + gladiator.getDescription());
						
						gladiator.getParameter(Shield.class, "shield").addSkin(shieldSkin);
						
						System.out.println("Now gladiator: " + gladiator.getDescription() + " has "
								+ gladiator.getParameter(Shield.class, "shield").nbrSkins() + " skins on his shield!");
					}
				} else {
					
					// interact with other agents
					// in short --- Fight!
					// request a partner
					gladiator.getFacilitator().getPartner(gladiator);
					
					System.out.println("Gladiator: " + gladiator.getDescription() + " is gonna fight with "
							+ ((gladiator.getPartner() == null) ? "Noone" : gladiator.getPartner().getDescription()));
					
					// send a strike to the partner
					if (null != gladiator.getPartner()) {
						
						((BiConsumer<Agent<Strike>, Agent<Strike>>) gladiator.getParameter(BiConsumer.class,
								"interact")).accept(gladiator, gladiator.getPartner());
					}
					
					System.out.println("Strike sent, dropping the partner");
					
					gladiator.dropPartner();
				}
			});
			
			try {
				
				ultradome.add(gladiator);
			} catch (IllegalAgentException e) {
				e.printStackTrace();
			}
		}
		
		// ultradome.setMultiThread(false);
		
		ultradome.start();
	}
}
