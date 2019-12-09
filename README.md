# Battleship
## Description

A client-server implementation of a game of Battleship.
Allows players to join, play, and disconnect from a command line version 
of a game of Battleship. Players can see their boards along with the 
placement of their ships. Players can also see their opponents boards 
with Hits and Misses but not the location of the opponents ships.

## Contributors

Developer(s): *Evert Ball, Carlee Yancey*

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
/quit   - Quits the player from a game, whether in progress or not.
/help   - To see the list of commands available.
```
    
###### ** The attack range is based on the board size that used.

**Server Logging:**
```bash
* Will display which client connection Agent is sending what command to 
  the servers connection agent. 

* Will display when the game is over.

* If there are more than two players, and when the first one loses it 
  shows who lost and that they where removed from the game.

* Will display who's current turn during the game.
```

## Implementation Change Reasoning 
**PrintMessageListener**
* The way we interpreted PrintMessageListener is that when a 
  BattleClient receives a message the PrintMessageListener is called 
  and the message is printed in messageReceived to the console.

**Public to Private:**
* Broadcast in BattleServer is only used in battleSever, thus there is no 
need for any other class to have access to the broadcast method.
 
* Close in the ConnectionAgent for same reason as above.

* Connect in BattleClient, the only place that a BattleClient should call 
connect is in the constructor of BattleClient. 

**Game Over/Win Condition:**
* When a player wins a game, the quit command is sent to all clients
  to force them to quit the game as the only way to join a game is to
  start a new client session.

* Since all clients have quit from the server, the server exits as well.

## Known Issues

**_Release as of 11/20/2019_**

* There are currently no known issues.
  
**_Release as of 12/06/2019_**
    
* ~~When all clients disconnect, an Exception is thrown server side
  and then the server just hangs.~~

**_Release as of 12/07/2019_**
    
* There are currently no known issues.

**_Release as of 12/08/2019_**

* ~~__\<GAME BREAKING\>__ When a client joins and the game has not 
  started, the player is allowed to send another correctly formatted 
  join command that will add a new player and disconnect the client 
  from the username that they originally joined as, leaving that 
  username in a sort of player limbo where nobody has control of that 
  player anymore.~~
  
* ~~__\<GAME BREAKING\>__ When play is in progress and clients that have 
  already joined can send a correctly formatted play command it will 
  allow the instantiation of a new player on the same socket as the 
  previously connected player, leaving the former username in a player 
  limbo.~~
  
* ~~__\<GAME BREAKING\>__ When 3+ players are playing and one player 
  loses, the game continues another round of play until it gets back to 
  the losing player. Once the game gets back to the losing player, the 
  server is unable to continue as it still thinks the losing player is 
  an active player.~~
  
* ~~If a player tries to join and the username is taken, the game 
  broadcasts that the taken name has surrendered upon the game starting.~~
  
**_Release as of 12/08/2019 @ 23:41hrs_**

* There are currently no known issues.
