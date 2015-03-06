cs56-games-minesweeper
======================


Migrated from: https://foo.cs.ucsb.edu/56mantis/view.php?id=0000900 by http://github.com/pconrad

Code prior to W14: https://foo.cs.ucsb.edu/cs56/issues/0000900/

project history
===============
```
 W14 | calebnelson | andrewberls 4pm | jgee67, davidacevedo | (pconrad) Minesweeper game
```


==============================================================================
High-level description
==============================================================================

This is a program that runs the game minesweeper. Currently, the game itself is able to be played. The game can be played via gui or through text, depending on the user's preference. There is a start menu, with three buttons: new game, help, and quit. Selecting new game will start a new game. Selecting help will bring the user to a page with instructions of how to play. Quit exits the program. During a game, the user can press esc at any time to pause the game. From here the user can resume, start a new game, or quit. Once the game is over, the user gets a message indicating whetheer he/she won or lost. The user can then press esc to return to the main menu. 

==============================================================================
Developer Notes/Documentation
==============================================================================

The end game message is currently a JLabel located in StartMenu.java. The variable name for this label is status. To change the text in this label, there is a method in StartMenu.java called setLabel(string s).

StartMenu is a class that creates a menu that the user can see and pick an option. It also initializes the game on call.

Grid is a class that is the foundation for minesweeper, applies mine locations, checks if something is open, makes flags functional, etc.

HelpScreen.java is a panel that displays help messages and a button to return to the start menu. It is displayed only after the user clicks "Help" from either the start menu or the pause menu.

MineComponent is a class that sets up the minesweeper Gui, ie. sets up the grid with numbered buttons.

==============================================================================
How to Run
=============================================================================

To run the GuiGame, use "ant mine".
To run the TextGame, use "ant textmine".
