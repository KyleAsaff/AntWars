# AntWars
Winning Ant Wars: Evolving a Human-Competitive Game Strategy

# About The Game

Ant Wars was one of the competitions organized within GECCO’2007, Genetic
and Evolutionary Computation Conference, in London, England, July 7–12,
2007. The goal was to evolve a controller for a virtual ant that collects food
in a square toroidal grid environment in the presence of a competing ant. In a
sense, this game is an extension of the so-called Santa-Fe trail task, a popular
genetic programming benchmark, to two-player environment.
Ant Wars may be classified as a probabilistic, two-person board game of imperfect
information. Each game is played on a 11x11 toroidal board. Before the
game starts, 15 pieces of food are randomly distributed over the board and two
players (ants) are placed at predetermined board locations. The starting coordinates
of ant 1 and ant 2 are (5, 2) and (5, 8), respectively. No piece of food can
be located in the starting cells. An ant has a limited field of view – a square
neighborhood of size 5x5 centered at its current location, and receives complete
information about the states (empty, food, enemy) of all cells within it.
The game lasts for 35 turns per player. In each turn ant moves into one of 8
neighboring cells. Ant 1 moves first. If an ant moves into a cell with food, it scores
1 point and the cell is emptied. If it moves into a cell occupied by the opponent,
it kills it: no points are scored, but only the survivor can go on collecting food
until the end of the game. Moving into an empty cell has no extra effect. A game
is won by the ant that attains higher score. In case of tie, Ant 1 is the winner.

# Competition
This was a project for CSCI3154 to see who could create the most efficient ant AI in the class (i.e. win the most games in a simulation). In a 1000 game simulation, my ant placed third in the class.

# Improvements to the AI

I have edited AIprocessorRand.java and made the following changes to the AI:

1. The AI hashes all nearby food locations into a hash table using the key as the priority of the food (1=highest)
    and will then attempt to reach each food location it has hashed.
2. If the AI comes across an opponent 1 space away, it will kill the opponent.
3. If there is no food near by, the AI visits each corner of the quadrant it is in and determines
    which quadrant it is in (NW, NE, SW, SE).
4. After the AI visits all 4 corners of the sector, the AI will visit a quadrant it has not been to yet.
5. It will repeat the 4 above steps and if the game is not ended by the end after visiting all 4 quadrants, the AI
   will go in random directions in an attempt to hash any food it comes across and it will try to reach it.

# How to Run the Game

1. Initialize and compile all files by running scripts/compileAll.bat
2. Run AntWarsClient.bat, AntWarsServer.bat, AntWarsVizClient.bat
3. Use the AntWarsServer GUI to start the game
4. Use the arrow keys to move the human controlled ant in AntWarsVizClient
