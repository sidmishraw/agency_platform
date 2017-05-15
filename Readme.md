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

#### Changes implemented by me in the project:
* Use of pluggable update logic in agent.
* Use of parameter map to hold additional parameters (both attributes and behaviors) for Facilitator and Agent.
* Composition of ShieldSkins rather than a decorator pattern in Ultradome.
* Pluggable strike strategies for weapons, although other features like weapon name and description, ID need to be stored in the weapon object.

I basically wanted to test the extent to which the pluggable pattern can be used without increasing the complexity while keeping the programming simple.
From the look of this codebase, I believe the application programmer doesn't need to worry too much about subclassing but can instead think of adding features as new parameters to the agent/facilitator objects.
This does give the programmer some expressive power at the cost of increased complexity. But I have seen this pattern of using annonymous object definitions being passed around in android programs, so I believe this should be acceptable.

Furthermore, introduction of functional programming concepts like composition, does improve the expressibility of the scenario (just my opinion, I'd say a decorator function would be even better and the composition achieves just that).


### Examples:

* [Ultradome](http://www.cs.sjsu.edu/faculty/pearce/modules/labs/patterns/ud/index.htm)

	The Ultra Dome is a huge arena littered with items: weapons, shield skins, and medicine bowls. Gladiators in the Ultra Dome fight each other. A gladiator's randomly selects an opponent or an item. If an opponent is selected, the gladiator attacks the opponent by delivering a strike. The opponent defends against the strike. The opponent's health is decremented. If the opponent's health reaches zero, he dies.

	If a weapon is selected, then the gladiator discards his current weapon and picks up the new one. The available weapons include swords, wands, poisons, and flame throwers. These produce strikes of type iron, magic, chemical, and fire, respectively.

	If a shield skin is selected, then the gladiator stretches the skin over his shield. Thus, a shield may have many layers of skins. Different types of skins can reduce the strength of different types of strikes.

	If a medicine bowl is selected, then the gladiator drinks from the bowl until the bowl is empty or the gladiator's strength is restored to 100%.

	A strike has a type, description, and strength. The strength of the strike is a percentage of the attacker's health. As a strike passes through the skins of the defender's shield, its strength is reduced by fixed percentages. For example, each flame-resistant skin a fireball from a flamethrower passes through diminishes its strength by a fixed percentage. Thus, the more fire-resistant skins a gladiator stretches over his shield, the weaker the strike becomes and therefore the less damage it will cause. Of course fire-resistant skins do not reduce the strengths of other types of strikes.

	Ultra Dome agents are gladiators and the Ultra Dome itself is the dispatcher. A gladiator owns a weapon and a shield. Initially, the weapon is a sword and the shield is a basic shield with no skins. (Such a shield reduces the strengths of all types of blows by a small fixed percentage.)

	The Ultra Dome maintains a list of random weapons, a list of random shield skins, and a fixed quantity of medicine. During a gladiator's update cycle a random integer called luck is generated. If 0 <= luck < 10, then the gladiator drinks enough medicine to restore his health to 100% or until the medicine is gone. If 10 <= luck < 20, then the gladiator selects a new weapon, assuming any weapons remain. If 20 < luck <= 30, then the gladiator selects a shield skin and strengthens his shield with it, assuming any shield skins remain. Otherwise, the gladiator attacks (interacts with) another gladiator. During the attack the attacker uses his weapon to generate a strike. The victim defends himself by using his shield to reduce the strength of the strike before his health is decremented.

* [Prisoner's Dilemma / Game playing agents](http://www.cs.sjsu.edu/faculty/pearce/modules/projects/oop/agency/index.htm)
	
	In 1980 Robert Axelrod used agent-based modeling to demonstrate how cooperative behavior in societies could have evolved. He created a society of N prisoner agents. Each prisoner perpetually interacts with randomly selected other prisoners by playing one game of Prisoner's Dilemma. Here's how it works: prisoner A sends prisoner B a message: "defect," or "cooperate". Before looking at the message, B sends A a defect or cooperate message. If both cooperated, then both earn 3 points. If both defected, then both earn 1 point. Otherwise, the defector wins 5 points and the cooperator gets nothing.

	Prisoners have different strategies for deciding to defect or cooperate. For example: always defect or always cooperate are two strategies. As one would expect, defectors score much higher than cooperators. Tit-for-tat strategy says defect if the opponent defected on the last encounter, otherwise cooperate.
	
	
	
	
### Steps for importing the project into Eclipse (4.5+) or Mars onwards:
>Note: This project is heavily dependent on Java8 features and hence needs eclipse 4.5+ for better support.
* Create a new Java Project in eclipse, then delete the generated `src` folder.
* Copy the 3 folders `framework`, `prisonersdilemma` and `ultradome` from the zip file or repository clone directory and paste them in the project directory you created.
* Select the 3 folders inside eclipse, right mouse click to show the context menu. Select `Build Path > Use as source folder` option from the menu.
* The above step should add the `framework`, `prisonersdilemma` and `ultradome` to your classpath so that they can be executed.
* The application drivers are PrisonersDilemmaDriver.java for Prisoners Dilemma simulation and UltradomeDriver.java for Ultradome simulation respectively.

The project comes with 4 output files:
*  `op_multithreaded.txt` -- This file has the output of the UltradomeDriver.java (Ultradome simulation running in multi threaded mode) piped into it.
*  `op_singlethreaded.txt` -- This file has the output of the UltradomeDriver.java (Ultradome simulation running in single threaded mode) piped into it.
*  `pd_multithreaded.txt` -- This file has the output of the PrisonersDilemmaDriver.java (PrisonersDilemma simulation running in multi threaded mode) piped into it.
*  `pd_singlethreaded.txt` -- This file has the output of the PrisonersDilemmaDriver.java (PrisonersDilemma simulation running in single threaded mode) piped into it.
 
#### Note: this project needs Java8+ because it heavily relies on lambdas and other Java8+ features. 