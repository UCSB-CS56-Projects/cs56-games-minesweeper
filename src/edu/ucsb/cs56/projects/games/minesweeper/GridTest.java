package edu.ucsb.cs56.projects.games.minesweeper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/** Test class for Grid

    @author David Acevedo
    @version 2015/03/04 for lab07, cs56, W15
    @see Grid

*/

public class GridTest {

	/**
	 * Test case for default constructor of the Grid class
	 *
	 * @see Grid#Grid
	 */
	@Test
	public void test_Grid(){
	Grid g1 = new Grid(true, 0);	
	Grid g2 = new Grid(true, 1);	
	Grid g3 = new Grid(true, 2);
	assertEquals(true,g1.getIsGUI());
	}
	
	/**
	 * Test case for getIsGUI of the Grid class
	 *
	 * @see Grid#getIsGUI()
	 */
	 
	@Test
	public void test_getIsGUI(){
		Grid g1 = new Grid(true);
		assertEquals(true, g1.getIsGUI());
	}
	
	/**
	 * Test case for setZero method of the Grid class
	 *
	 * @see Grid#setZero()
	 */
	
	@Test
	public void test_setZero(){
		//Can not test due to not knowing how to check a double array without overflowing 
		//the test with 100 zeros and not using a for loop for this test and writing a getter
		//for the grid in the Grid class
	}
	
	/**
	 * Test case for blankToMine method of the Grid class
	 *
	 * @see Grid#blankToMine()
	 */
	
	@Test
	public void test_blankToMine(){
		//not able to test due to the use of Math.random, too random for me
	}
	
	/**
	 * Test case for insertNums method of the Grid class
	 *
	 * @see Grid#insertNums()
	 */
	
	@Test
	public void test_insertNums(){
		//not able to test due to needing a getter for grid in the Grid class and that
		//grid variable being a double array, making the test extremely messy, although isOpen
		//checks in a way if this works
	}
	
	/**
	 * Test case for mapMaker method of the Grid class
	 *
	 * @see Grid#mapMaker()
	 */
	 
	@Test
	public void test_mapMaker(){
		//Not able to test due to needing a getter for map and map being a double array
	}
	
	/**
	 * Test case for toString method of the Grid class
	 *
	 * @see Grid#toString()
	 */
	 
	@Test
	public void test_toString(){
		//not able to test due to there being way too many characters, about 200, maybe more 
	}
	
	 
	/**
	 * Test case for isOpen method of the Grid class
	 *
	 * @see Grid#isOpen
	 */
	
	@Test
	public void test_isOpen1()  {
		Grid g1 = new Grid(true);
		
		assertEquals(false,g1.isOpen(1));
		}
		
	
	/**
	 * Test case for setZero method of the Grid class
	 *
	 * @see Grid#isOpen()
	 */
	
	@Test
	public void test_isOpen2()  {
		Grid g1 = new Grid(false);
		
		assertEquals(false,g1.isOpen(1));
		}
		
	/**
	 * Test case for isMine method of the Grid class
	 *
	 * @see Grid#isMine()
	 */
	 
	@Test
	public void test_isMine(){
		//not able to test due to the mine being at a random position
	}
	
	/**
	 * Test case for isFlag method of the Grid class
	 *
	 * @see Grid#isFlag()
	*/
	
	@Test
	public void test_isFlag1()  {
		Grid g1 = new Grid(true);
		
		assertEquals(false,g1.isOpen(99));
		}
		
	/**
	 * Test case for isFlag method of the Grid class
	 *
	 * @see Grid#isFlag()
	*/
	
	@Test
	public void test_isFlag2()  {
		Grid g1 = new Grid(false);
		
		assertEquals(false,g1.isOpen(99));
		}

	/**
	 * Test case for searchBox method of the Grid class
	 *
	 * @see Grid#searchBox()
	*/
	
	@Test
	public void test_searchBox(){
		//no real way to test it because it depends on random from other methods
	}
	
	/**
	 * Test case for flagBox method of the Grid class
	 *
	 * @see Grid#flagBox()
	*/
	
	@Test
	public void test_flagBox(){
		//no getter for the double array or a way to search the specific section of the array
		//so I can not test without that getter
	}

	/**
	 * Test case for deflagBox method of the Grid class
	 *
	 * @see Grid#deflagBox()
	*/
	
	@Test
	public void test_deflagBox(){
		//no getter for the double array or a way to search the specific section of the array
		//so I can not test without that getter
	}
	
	/**
	 * Test case for findAllZeros method of the Grid class
	 *
	 * @see Grid#findAllZeros()
	*/
	
	@Test
	public void test_findAllZeros(){
		//Due to the need of random from other methods, I can not test it.
	}
	
	/**
	 * Test case for gameStatus method of the Grid class
	 *
	 * @see Grid#gameStatus()
	*/
	
	@Test
	public void test_gameStatus(){
		Grid g1 = new Grid(true);
		assertEquals(0,g1.gameStatus(0));
	}

	/**
	 * Test case for getCell method of the Grid class
	 *
	 * @see Grid#getCell()
	*/
	
	@Test
	public void test_getCell(){
		//I can't test due to the use of Math.random in another method
	}
}
