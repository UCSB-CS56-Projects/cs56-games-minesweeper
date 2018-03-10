package edu.ucsb.cs56.projects.games.minesweeper.frames;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import edu.ucsb.cs56.projects.games.minesweeper.constants.Constants;
import edu.ucsb.cs56.projects.games.minesweeper.database.DBConnector;
import edu.ucsb.cs56.projects.games.minesweeper.gamelogic.Grid;
import edu.ucsb.cs56.projects.games.minesweeper.gui.MineGUI;

/** The window that displays the game in the GUI
 * @author Ryan Wiener
 * @author Kate Perkins
 */

public class GameFrame extends JFrame {

    private static final Color NUMBER = new Color(0, 100, 0);
    private static Dimension windowSize;


    private Grid game;
    private JButton[][] buttons;
    private JTextField timeDisplay;
    private JButton refresh;
    private JButton mainMenu;
    private JButton quitMine;
    private JButton inGameHelp;
    private JButton flagBtn;
    private JPanel grid;
	private Color Grey = new Color(180,180,180);
	private Color Unpressed = new Color(158,158,158);
	private boolean firstClick=false;

    /**
     * Constructs game from the given difficulty
     * @param difficulty difficulty of the game to be played
     * @throws IOException if loading fails
     * @throws ClassNotFoundException if loading fails
     */
    public GameFrame(Constants.Difficulty difficulty) throws IOException, ClassNotFoundException {
        Constants.Difficulty gameDifficulty = difficulty;
        boolean gameReload;
        if (gameDifficulty == Constants.Difficulty.LOAD) {
            game = Grid.loadGame();
            gameReload=true;
            gameDifficulty = game.getDifficulty();
        }
        else{
            gameReload = false;
            game = new Grid(gameDifficulty);
        }
        setWindowSize(this, gameDifficulty);
        MineGUI.centerWindow(this);
        JToolBar toolbar = new JToolBar("In-game toolbar");
        createToolbar(toolbar);//Function declared below
        getContentPane().add(toolbar, BorderLayout.NORTH); //puts the game toolbar at the top of the screen
        grid = new JPanel(); //Declared at the top. Not of type Grid so this is allowed.
        grid.setLayout(new GridLayout(game.getSize() ,0)); // GridLayout(int rows, int columns)
        buttons = new JButton[game.getSize()][game.getSize()];//Array of buttons
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
            buttons[i][j] = new JButton();
            buttons[i][j].setBackground(Unpressed);
            buttons[i][j].addMouseListener(new ButtonListener(i, j));//ButtonListener defined at the bottom. Extends MouseAdapter.
            grid.add(buttons[i][j]);
            }
        }
        if (gameReload){
            reload();
        }
        getContentPane().add(grid);
        getContentPane().addComponentListener(new SizeListener());
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    /**
     * Sets the size of the window depending to the difficulty since different difficulties have different grid sizes.
     * @param window An object of type Window or any of its sub classes.
     * @param gameDifficulty The difficulty of the game
     */
    private void setWindowSize(GameFrame window, Constants.Difficulty gameDifficulty) {
            windowSize = Constants.getWindowSizes(gameDifficulty);
            int width= (int) windowSize.getWidth();
            int height= (int) windowSize.getHeight();
            window.setSize(width, height);

    }
    /**
     * Initializes toolbar at the top of the screen
     * @param toolbar toolbar to be initialized
     */
    public void createToolbar(JToolBar toolbar) {
        //make buttons
        refresh = new JButton("Reset Game");
        mainMenu = new JButton("Main Menu");
        quitMine = new JButton("Quit Minesweeper");
        inGameHelp = new JButton("Help");
        flagBtn = new JButton("Flag"); //new ImageIcon("resource/images/flag.png"));
        timeDisplay = new JTextField(game.getGameTime());
        timeDisplay.setColumns(4);
        timeDisplay.setEditable(false);
        Timer t = new Timer(); //It refreshes the timeDisplay every second.
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                timeDisplay.setText(Integer.toString(game.getGameTime()));
            }
        }, 0, 1);
        refresh.addActionListener((ActionEvent e)-> {
            if (MineGUI.overwriteSavePrompt()) {
                resetGame();
            }
        });
        mainMenu.addActionListener((ActionEvent e) -> {
            game.save();
            MineGUI.goToMainMenu();
        });
        inGameHelp.addActionListener((ActionEvent e) -> { MineGUI.setHelpScreenVisible(true); });
        quitMine.addActionListener((ActionEvent e) -> {
            game.save();
            MineGUI.quitPrompt();
        });
        flagBtn.addActionListener((ActionEvent e) -> { flag(); });
        toolbar.add(flagBtn);
        toolbar.add(mainMenu);
        toolbar.add(refresh);
        toolbar.add(inGameHelp);
        toolbar.add(quitMine);
        toolbar.setLayout(new FlowLayout(FlowLayout.RIGHT));
        toolbar.add(timeDisplay);
        toolbar.setFloatable(false);
    }
    /**
     * Set the font of a button and change background Color.
     * @param button JButton that is to be modified.
     */
    private void setUpButton(JButton button){
        int fontSize = buttons[0][0].getSize().height / 2;
        if (buttons[0][0].getSize().height / 2 > buttons[0][0].getSize().width / 4) {
            fontSize = buttons[0][0].getSize().width / 4;
        }
        button.setFont(new Font("sansserif", Font.BOLD, fontSize));
        button.setBackground(Grey);
    }

    /**
     * Updates only a single cell on the grid, to either a numerical value or to a bomb.
     * @param x xCor of button
     * @param y yCor of button
     * @param mineIcon Image of mine to be used.
     */
    private void updateSingleCell(int x, int y, ImageIcon mineIcon) {
        setUpButton(buttons[x][y]);
        if(game.getCellSymbol(x,y) == 'X'){
            buttons[x][y].setIcon(mineIcon);
        }
        else if(game.getCellSymbol(x,y) != '0') {
            buttons[x][y].setText(Character.toString(game.getCellSymbol(x, y)));
            buttons[x][y].setForeground(NUMBER);
        }
        else {
            //Cell is empty do not display 0.
        }
    }

    /**
     * Get the Grid object (the game itself)
     * @return the underlying Grid object (the game)
     */
    public Grid getGrid(){
        return game;
    }

    /**
     * Get x coordinate of a given grid button
     * used for GUITest to make Robot class move mouse button to correct coordinates
     * @param i row of grid button
     * @param j column of grid button
     * @return x coordinate of grid button
     */
    public int getGridButtonX(int i, int j) {
	return grid.getX() + buttons[i][j].getX() + 10;
    }

    /**
     * Get y coordinate of a given grid button
     * used for GUITest to make Robot class move mouse button to correct coordinates
     * @param i row of grid button
     * @param j column of grid button
     * @return y coordinate of grid button
     */
    public int getGridButtonY(int i, int j) {
	return grid.getY() + buttons[i][j].getY();
    }

    /**
     * Get x coordinate of a given refresh button
     * used for GUITest to make Robot class move mouse button to correct coordinates
     * @return x coordinate of refresh button
     */
    public int getRefreshX() {
        return refresh.getX();
    }

    /**
     * Get y coordinate of a given refresh button
     * used for GUITest to make Robot class move mouse button to correct coordinates
     * @return y coordinate of refresh button
     */
    public int getRefreshY() {
        return refresh.getY();
    }

    /**
     * Get x coordinate of a given flag button
     * used for GUITest to make Robot class move mouse button to correct coordinates
     * @return x coordinate of flag button
     */
    public int getFlagBtnX() {
        return flagBtn.getX();
    }

    /**
     * Get y coordinate of a given flag button
     * used for GUITest to make Robot class move mouse button to correct coordinates
     * @return y coordinate of flag button
     */
    public int getFlagBtnY() {
        return flagBtn.getY();
    }

    /**
     * Get x coordinate of a given main menu button
     * used for GUITest to make Robot class move mouse button to correct coordinates
     * @return x coordinate of main menu button
     */
    public int getMainMenuX() {
        return mainMenu.getX();
    }

    /**
     * Get y coordinate of a given main menu button
     * used for GUITest to make Robot class move mouse button to correct coordinates
     * @return y coordinate of main menu button
     */
    public int getMainMenuY() {
        return mainMenu.getY();
    }

    /**
     * Get x coordinate of a given help button
     * used for GUITest to make Robot class move mouse button to correct coordinates
     * @return x coordinate of help button
     */
    public int getHelpX() {
        return inGameHelp.getX();
    }

    /**
     * Get y coordinate of a given help button
     * used for GUITest to make Robot class move mouse button to correct coordinates
     * @return y coordinate of help button
     */
    public int getHelpY() {
        return inGameHelp.getY();
    }

    /**
     * switch flag buttons selected property
     * if flag button is selected then left clicking flags boxes rather than opening them
     */
    public void flag() {
	flagBtn.setSelected(!flagBtn.isSelected());
    }
    private void flagCell(int x, int y){
        game.flagBox(x, y);
        buttons[x][y].setIcon(getImageIcon("/images/flag.png"));
        String soundName = "/sounds/place_flag.wav";
        playSound(soundName);
    }

    /**
     * PreCondition: buttons at [x][y] is flagged.
     * Deflags a button and removes flag image.
     * @param x
     * @param y
     */
    private void deflagCell(int x, int y) {
        game.deflagBox(x,y);
        buttons[x][y].setIcon(null);
    }

    /**
     * plays a sound from the resources
     * @param dir name of the sound file to be played
     */
    public void playSound(String dir) {
	if (dir != null) {
	    try {
		File resource = new File("resources" + dir);
		AudioInputStream audioInputStream;
		if (resource.exists()) {
		  audioInputStream = AudioSystem.getAudioInputStream(resource.getAbsoluteFile());
		  } else {
		  audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(dir));
		  }
		  AudioFormat format = audioInputStream.getFormat();
          DataLine.Info info= new DataLine.Info(Clip.class,format);
          Clip clip = (Clip) AudioSystem.getLine(info);
          //When the Clip stops playing a sound it needs to be closed.
          clip.addLineListener(e -> {
              if (e.getType() == LineEvent.Type.STOP) {
                  clip.close();
              }
          });
          clip.open(audioInputStream);
          clip.start();
          audioInputStream.close();

	    }
	    catch (UnsupportedAudioFileException|LineUnavailableException| IOException e) {
		e.printStackTrace();
	    }

	}
    }

    /**
     * Loads specified image located in resources folder.
     * @param resource Path of the image (excluding "resources/")
     * @return ImageIcon containing the image.
     */
    private ImageIcon getImageIcon(String resource) {
	File local = new File("resources/" + resource);
	ImageIcon icon;
	if (local.exists()) {
	    icon = new ImageIcon(local.getPath());
	} else {
	    icon = new ImageIcon(getClass().getResource(resource));
	}
	Image img = icon.getImage();
	//resize icon to fit button
	Image newImg = img.getScaledInstance(grid.getWidth() / game.getSize(), grid.getHeight() / game.getSize(),  Image.SCALE_SMOOTH) ;
	return new ImageIcon(newImg);
    }

    /**
     * restores the previous state of a game loaded from save file.
     */
    private void reload() {
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                if (game.isOpen(i, j)) {
                    buttons[i][j].setBackground(Grey);
                    if (game.isMine(i, j)) {

                    } else {
                        if (game.getCellSymbol(i, j) != '0') {
                            buttons[i][j].setForeground(NUMBER);
                            buttons[i][j].setText(Character.toString(game.getCellSymbol(i, j)));
                        }
                    }
                } else if (game.isFlag(i, j)) {
                    buttons[i][j].setIcon(getImageIcon("/images/flag.png"));
                } else {
                    buttons[i][j].setIcon(null);
                    buttons[i][j].setText("");
                }
            }
        }
    }

    /**
     * Refreshes the grid to coincide with the game
     */
    private void refresh() {
	    for(int x=0; x< game.getVisibleSize(); x++){
            Dimension cor = game.getCellCor(x);
            int i = (int) cor.getWidth();
            int j = (int) cor.getHeight();
            updateSingleCell(i,j,null);
            game.incrementCorrectMoves();
        }
        game.removeAllVisible();
    }
    /**
     * Reveals all the cells in the game.
     */
    public void revealAll(){
        ImageIcon mineIcon= getImageIcon("/images/mine.jpg");
        for(int x=0; x<game.getSize();x++){
            for(int y=0; y<game.getSize();y++){
                if(game.isMine(x,y)){
                    updateSingleCell(x,y,mineIcon);
                }
                else if(!game.isOpen(x,y) || game.isFlag(x,y)){
                    if (game.isFlag(x,y)){
                        deflagCell(x,y);
                    }
                    updateSingleCell(x,y,null);
                }
            }
        }
    }
    /**
     * Reveals all the mines.
     */
    public void revealMines(){
        ImageIcon mineIcon= getImageIcon("/images/mine.jpg");
        for(int x=0; x<game.getSize();x++){
            for(int y=0; y<game.getSize();y++){
                if(game.isMine(x,y)){
                    updateSingleCell(x,y,mineIcon);
                }
            }
        }
    }


    /**
     * Reset the game with the same difficulty
     */
    private void resetGame() {
	    MineGUI.newGame(game.getDifficulty());
    }
    /**
     * Dialog that displays a congratulatory message and asks the user for input.
     * @param database
     */
    public void gameWonPrompt(boolean database) {
        String soundName = "/sounds/win.wav";
        playSound(soundName);
        if (database) {
            String user = JOptionPane.showInputDialog(null, "You win! Enter your name for the leaderboard.", "Victory!", JOptionPane.QUESTION_MESSAGE);
            if (user != null) {
                saveHighest(user, game.getGameTime());
            }
        }
        int response = JOptionPane.showOptionDialog(null, "You win! Press 'Reset Game' to start a new game.", "Victory!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Main Menu", "Reset Game"}, "default");
        if (response == JOptionPane.YES_OPTION) {
            MineGUI.goToMainMenu();
        } else if (response == JOptionPane.INFORMATION_MESSAGE) {
            resetGame();
        } else {
            //do nothing
        }
    }
    /**
     * Dialog that informs the user of defeat. Requests the user to either reset game or to go back to main menu.
     */
    public void gameLost() {
        revealMines();
        String soundName = "/sounds/explosion.wav";
        playSound(soundName);
        int response = JOptionPane.showOptionDialog(null, "You lose! Press 'Reset Game' to start a new game.", "Defeat!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Main Menu", "Reset Game"}, "default");
        if (response == JOptionPane.YES_OPTION) {
            MineGUI.goToMainMenu();
        }
        else{
            resetGame();
        }
    }


    /**
     * helper function for adding scores to database
     * @param name name of user
     * @param time how long it took the user to win
     */
    private void saveHighest(String name, int time) {
        DBConnector.addScore(name, time, game.getDifficulty().ordinal());
    }


    /**
     * inner class, reponds to resizing of component to resize font
     */
    class SizeListener implements ComponentListener {
	@Override
	public void componentHidden(ComponentEvent e) {
	}
	@Override
	public void componentMoved(ComponentEvent e) {
	}
	/**
	 * refresh the screen when resizing the frame
	 * @param e the ComponentEvent object (not used)
	 */
	@Override
	public void componentResized(ComponentEvent e) {
	    //refresh();
	}

	@Override
	public void componentShown(ComponentEvent e) {
	    // TODO Auto-generated method stub
	    }
    }

    /**
     * Inner Class, responds to the event source.
     */
    class ButtonListener extends MouseAdapter {
        private int row;
        private int col;

        /**
         * Constructs ButtonListener
         *
         * @param i row value of the ButtonListener
         * @param j column value of the ButtonListener
         */
        public ButtonListener(int i, int j) {
            super();
            row = i;
            col = j;
        }

        /**
         * Places player's symbol on button, checks for a winner or tie
         *
         * @param event when a button is clicked
         */
        public void mouseReleased(MouseEvent event) {
            if (game.getGameState() == Constants.GameState.PLAYING) {
                if(!firstClick && game.isMine(row,col)){
                    firstClick=true;
                    game.shuffleMine(row,col,game);
                }
                else if(!firstClick){
                    firstClick=true;
                }
                if (event.getButton() == MouseEvent.BUTTON1 && !game.isFlag(row, col) && !game.isOpen(row, col) && !flagBtn.isSelected()) {
                    //if you left click and the button is available (not a flag and not already opened)
                    char result = game.searchBox(row, col);
                    if (result == 'X') {
                        gameLost();
                        return;
                    } else {
                        String soundName = "/sounds/clicked.wav";
                        playSound(soundName);
                        if (result == '0') {
                            // need to update all cells since they opened up
                            refresh();
                        } else {
                            // only need to update the current cell
                            updateSingleCell(row,col,null);
                        }
                    }
                }
                else if (event.getButton() == MouseEvent.BUTTON1 && (game.isFlag(row, col) | game.isOpen(row, col)) && !flagBtn.isSelected()) {
                    // If you left click and the button is a flag or has been opened
                    String soundName = "/sounds/userError.wav";
                    playSound(soundName);
                }
                else if (event.getButton() == MouseEvent.BUTTON3 || flagBtn.isSelected()) {
                    // If you right click or have flag button selected
                    if (game.isFlag(row, col)) {
                        deflagCell(row,col);
                    } else if (!game.isOpen(row, col)) {
                        flagCell(row,col);
                    }
                }
                if (game.getGameState() == Constants.GameState.WON) {
                    gameWonPrompt(DBConnector.isConnected());
                }
            }
        } // class ButtonListener


    }


}


