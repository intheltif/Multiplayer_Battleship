# Battleship
*Author: Evert Ball*

*Author: Carlee Yancey*

## Description

A client-server implementation of a game of Battleship.
Allows players to join, play, and disconnect from a command line version 
of a game of Battleship. Players can see their boards along with the 
placement of their ships. Players can also see their opponents boards 
with Hits and Misses but not the location of the opponents ships.

## Files Used
**Client:**
```bash
BattleClient.java
BattleDriver.java
PrintStreamMessageListener.java
```
**Common:**
```bash
ConnectionAgent.java
MessageListener.java
MessageSource.java
```
**Server:**
```bash
BattleServer.java
BattleShipDriver.java
Game.java
Grid.java
Ship.java
```
## Usage

**To compile the game:**

```bash
>cd src/
>javac */*.java
```

**To run the game:**
```bash
>java server/BattleShipDriver <Port Number> <Grid Size (5-10)>

>java client/BattleDriver <Host> <Port Number> <username> **
```
###### ** There can be multiple clients added to the game before the game is started

**Commands for Clients:**
```bash
/play   - Starts a game of battleship
/show <username> - Allows players to see the boards of themselves and others
/attack <username> <[0-9]+> <[0-9]+> ** - Attacks another player at coordinates.
/quit - Quits the player from a game, whether in progress or not.
/help - To see the list of commands available.
```
    
###### ** The range of for attack is based off the board size that you use.

**Server Logging:**
```bash
* Will display which client connection Agent is sending what command to the the 
    servers connection agent. 

* Will display when the game is over.

* If there are more than two players, and when the first one loses it shows who
    lost and that they where removed from the game.

* Will display who's current turn during the game.
```

## Implementation Change Reasoning 
**PrintMessageListener**
* The way we interpreted PrintMessageListener is that when a BattleClient 
receives a message the PrintMessageListener is called and the message is printed 
in messageReceived to the console.

**Public to Private:**
* Broadcast in BattleServer is only used in battleSever, thus there is no 
need for any other class to have access to the broadcast method.
 
* Close in the ConnectionAgent for same reason as above.

* Connect in BattleClient, the only place that a BattleClient should call 
connect is in the constructor of BattleClient. 

## Known Issues

**_Release as of 11/20/2019_**

* There are currently no known issues.
  
**_Release as of 12/06/2019_**
    
* When all clients disconnect, an Exception is thrown server side
and then the server just hangs.

**_Release as of 12/07/2019_**
    
* There are currently no known issues.
