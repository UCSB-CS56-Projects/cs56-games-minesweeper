package edu.ucsb.cs56.projects.games.minesweeper;
import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event. ActionEvent;
import java.awt.Font;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboPopup;

/**
 * An Swing component for playing MineSweeper

   @author Daniel Reta
   @author David Acevedo
   @author Caleb Nelson
   @version 2015/03/04 for lab07, cs56, W15
   @see Grid
 */
public class MineComponent extends JComponent 
{
    private Grid game;
    private Messager m;
    private int size;
    private int status = 0; //allows the StartMenu class to know when the game is done
	StartMenu start;
    private Color zero = new Color (0, 0, 128);
    private Color number = new Color (0, 100, 0);
    private JButton[][] buttons;

    /** Constructor
	
	@param game an object that implements the Inferface interface to keep track
	            of the moves in each game, ensuring the rules are followed and detecting
	            when someone has won.
	@param md an object that implements the MessageDestination interface.  This is just
	           a place to send any messages that need to be communicated to the user.
		   Making this separate allows a user of this components to decide to
		   send those messages to the console, or to a variety of different
		   swing Widgets, or even to a web page, as needed.
	@param start a StartMenu object so we can use the MineComponent class to modify the 
			GUI on the JFrame
    */
       
    public MineComponent(Grid game, Messager m, StartMenu start) {
	super(); // is this line necessary?  what does it do?
	this.start=start;
	
	this.game = game;  // the Interface game
	this.m = m;  // a place we can write messages to
	this.size = game.getSize();
	buttons = new JButton[size][size];

	// note columns ignored when rows are set
	// number of columns is implicit from the number of things added

	this.setLayout(new GridLayout(this.size ,0)); 
	for(int i=0; i< this.size; i++) {
	    for(int j=0; j< this.size; j++){
		String label=String.format("%d",i* this.size +j);
		JButton jb = new JButton(label);
		buttons[i][j] = jb;
		jb.addMouseListener(new ButtonListener(i* this.size +j));
		jb.setFont(new Font("sansserif",Font.BOLD,12));
		jb.setText("");
		this.add(jb);
	    }
	}
	
    }

    /**
     * Inner Class, responds to the event source.
    */

    class ButtonListener extends MouseAdapter
    {
	private int num;

	public ButtonListener(int i) {
	    super();  
	    this.num = i;
	}

	/**
	   Places player's symbol on button, checks for a winner or tie 
	   
	   @param event when a button is clicked

	 */

	//status = 0;

	public void mouseReleased(MouseEvent event) {
	    if(game.gameStatus(status) == 0){
		if(event.getButton() == MouseEvent.BUTTON1){
		    game.searchBox(num);
		    for(int i=0; i< size; i++){
			for(int j=0; j< size; j++){
			    JButton jb = buttons[i][j];
			    if(game.getCell(i*size+j) != '?'){
				jb.setFont(new Font("sansserif",Font.BOLD,34));
				if (game.getCell(i*size+j) == 48)
				    jb.setForeground(zero);
				else if (game.getCell(i*size+j) == 70)
				    jb.setForeground(Color.RED);
				else if (game.getCell(i*size+j) == 88)
				    jb.setForeground(Color.BLACK);
				else
				    jb.setForeground(number);
				jb.setText(Character.toString(game.getCell(i*size+j)));
			    }
			}
		    }

		status = game.gameStatus(status);

		if (status == -1){
		    JOptionPane.showMessageDialog(MineComponent.this, "You lose! Press esc to start a new game.");
		    m.append("You lose!!\n");
			start.setLabel("You lose!!! Press esc to start a New Game");
		}
		else if (status == 1){
		    JOptionPane.showMessageDialog(MineComponent.this, "You win!! Press esc to start a new game.");
		    m.append("You win!!\n");
			start.setLabel("You win!!! Press esc to start a New Game");
		}
		
	    }

	    else if(event.getButton() == MouseEvent.BUTTON3){
		if(game.isFlag(num)){
		    game.deflagBox(num);
		    JButton jb = buttons[num/size][num%size];
		    jb.setFont(new Font("sansserif",Font.BOLD,12));
		    jb.setForeground(Color.BLACK);
		    jb.setText(""); 
		}	    
		else if(!(game.isOpen(num))){
		    game.flagBox(num);
		    JButton jb = buttons[num/size][num%size];
		    jb.setFont(new Font("sansserif",Font.BOLD,30));
		    jb.setForeground(Color.RED);
		    jb.setText("F"); 
		}

		int status = game.gameStatus(0);

		if (status == 1){
			
		    m.append("You win!!\n");
			start.setLabel("You win!!! Press esc to start a New Game");
		}
	    }
	    }
	}
	
    }
	int getStatus(){
		return status;
	}
	Grid getGrid(){
		return game;
	}
}

