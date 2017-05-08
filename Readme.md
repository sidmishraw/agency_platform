## Agent Based Architecture Framework
### Agency Platform
[Based on Dr. Pearce's lecture](http://www.cs.sjsu.edu/faculty/pearce/modules/projects/oop/agency/index.htm)

The Agency is a platform for creating and running agent-based applications. 
It consists of three components: Facilitator, Agent, and Message.

A typical customization would create extensions of all three classes.

#### Facilitator
The facilitator maintains a list of all agents. Agents are added to the list using the add method. This method provides 
an agent with a reference to the facilitator and a unique id number. The facilitator provides synchronized services 
for delivering messages and for finding partners. The facilitator's start method runs 
in two modes: multi-threaded and single-threaded. In multi-threaded mode each agent is started, then joined. 
In single-threaded mode the facilitator repeatedly iterates through the agents, calling each 
ones update method until all of the agents are dead.

#### Agent
The Agent update method is abstract. It must be implemented in an extension. 
Usually the update method doesn't try to do too much: request a partner from the facilitator, 
send a message to a partner, check for incoming messages, etc. (Be careful, most Agent methods need to be synchronized.)

#### Message
The message is just a generic wrapper containing contents.


### Examples:

* [Ultradome](http://www.cs.sjsu.edu/faculty/pearce/modules/labs/patterns/ud/index.htm)
  The Ultra Dome is a huge arena littered with items: weapons, shield skins, and medicine bowls. Gladiators in the Ultra Dome fight each other. A gladiator's randomly selects an opponent or an item. If an opponent is selected, the gladiator attacks the opponent by delivering a strike. The opponent defends against the strike. The opponent's health is decremented. If the opponent's health reaches zero, he dies.

If a weapon is selected, then the gladiator discards his current weapon and picks up the new one. The available weapons include swords, wands, poisons, and flame throwers. These produce strikes of type iron, magic, chemical, and fire, respectively.

If a shield skin is selected, then the gladiator stretches the skin over his shield. Thus, a shield may have many layers of skins. Different types of skins can reduce the strength of different types of strikes.

If a medicine bowl is selected, then the gladiator drinks from the bowl until the bowl is empty or the gladiator's strength is restored to 100%.

A strike has a type, description, and strength. The strength of the strike is a percentage of the attacker's health. As a strike passes through the skins of the defender's shield, its strength is reduced by fixed percentages. For example, each flame-resistant skin a fireball from a flamethrower passes through diminishes its strength by a fixed percentage. Thus, the more fire-resistant skins a gladiator stretches over his shield, the weaker the strike becomes and therefore the less damage it will cause. Of course fire-resistant skins do not reduce the strengths of other types of strikes.

Ultra Dome agents are gladiators and the Ultra Dome itself is the dispatcher. A gladiator owns a weapon and a shield. Initially, the weapon is a sword and the shield is a basic shield with no skins. (Such a shield reduces the strengths of all types of blows by a small fixed percentage.)

The Ultra Dome maintains a list of random weapons, a list of random shield skins, and a fixed quantity of medicine. During a gladiator's update cycle a random integer called luck is generated. If 0 <= luck < 10, then the gladiator drinks enough medicine to restore his health to 100% or until the medicine is gone. If 10 <= luck < 20, then the gladiator selects a new weapon, assuming any weapons remain. If 20 < luck <= 30, then the gladiator selects a shield skin and strengthens his shield with it, assuming any shield skins remain. Otherwise, the gladiator attacks (interacts with) another gladiator. During the attack the attacker uses his weapon to generate a strike. The victim defends himself by using his shield to reduce the strength of the strike before his health is decremented.
