# Poker multiplayer

## About

A simple multiplayer console game.

## Rules 

First, the player must create a room or join it.
The game will start when all players indicate that they are ready.

Each player starts with 200 tokens.
Small Blind and Big Blind are also selected at the beginning
The first one gives 5 and the second one gives 10.
The next players must equalize or give a higher amount.
If players keep entering the same amount (i.e. Check), the system will start a new "betting round"
and will put more cards on the table.
The so-called Show Down will start if 1 player remains (the others have passed),
or when there are 5 cards on the table.
After such a turn, players can decide whether they want to continue playing the game or not.


## Technologies

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![SonarQube](https://img.shields.io/badge/SonarQube-4E9BCD?logo=sonarqube&logoColor=white&style=for-the-badge)

## How to run

First method:
    
1. Run sever package with Intellij Idea
2. Run client using the [Multi Run extension](https://plugins.jetbrains.com/plugin/7248-multirun)
   

Second method:

1. Build the project (run the `mvn package` command on the root pom.xml)
2. `*-jar-with-dependencies.jar` files will appear in the target folders (in poker-client and poker-server)
3. First, start the server using the `java -jar "poker-server-1.0-SNAPSHOT-jar-with-dependencies.jar"`
4. And then clients using `java -jar "poker-client-1.0-SNAPSHOT-jar-with-dependencies.jar"`