*Author: Evert Ball*

*Author: Carlee Yancey*

# Battleship

## Description

DR. BARLOWE NOTE: We demo-ed this project to you at 0900hrs on 11/19/2019.
                  I spoke to you at approximately 1230hrs on 11/21/2019 about 
                  forgetting to actually submit the project to you on Agora 
                  and we agreed that I could submit it to you today.

                  You should be able to view any changes made and when they 
                  were made by entering the command "git log" to see each of
                  our commits. The last commit that exists should be up to
                  when we demo-ed the project to you.

                  Thanks again for understanding!

A client-server implementation of a game of Battleship.
Allows players to join, play, and disconnect from a command line version of a
game of Battleship. Players can see their boards along with the placement of
their ships. Players can also see their opponents boards with Hits and Misses
but not the location of the opponents ships.

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

>java client/BattleDriver <Host> <Port Number> <username>
```

**Commands for Clients:**
```bash
/play
/show <username>
/attack <username> <[0-9]+> <[0-9]+> *
/quit
/help
```
    
###### * The range of for attack is based off the board size that you use.

**Commands for Server:**
```bash
* Will display which client connection Agent is sending what command to the the 
    servers connection agent. 

* Will display when the game is over.

* If there are more than two players, and when the first one loses it shows who
    lost and that they where removed from the game.

* Will display who's current turn during the game.
```

## Known Issues

**_Release as of 11/20/2019_**
    * There are currently no known issues.
    
**_Release as of 12/6/2019_**
    
* Once a game has been started one more client can still join
the game. But if another try's to join is when does not allow 
for that client to join.
    
