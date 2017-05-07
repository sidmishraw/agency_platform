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