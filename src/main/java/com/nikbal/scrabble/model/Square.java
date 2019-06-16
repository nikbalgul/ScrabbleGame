package com.nikbal.scrabble.model;

/**
 * represents each square on the board grid, storing information on the letters,
 * score modifiers and special tiles placed on the square
 * 
 *
 *
 */
public class Square {

	private Tile letter;
	private int turnPlaced = 0;

	/**
	 * initializes the square, to default of no score modifiers, special tiles
	 * or letters
	 */
	public Square() {
		letter = null;
	}
	
	/**
	 * sets the turn on which the letter was placed
	 * 
	 * @param turnPlaced turn number
	 */
	public void setTurnPlaced(int turnPlaced) {
		this.turnPlaced = turnPlaced;
	}
	
	/**
	 * gets the turn the letter tile was placed
	 * 
	 * @return turn
	 */
	public int getTurnPlaced() {
		return turnPlaced;
	}


	/**
	 * adds a letter to the square
	 * 
	 * @param tile
	 *            letter tile to add
	 */
	public void setLetter(Tile tile) {
		letter = tile;
	}

	/**
	 * removes the set letter of the square, setting to null
	 */
	public void removeLetter() {
		letter = null;
		turnPlaced = 0;
	}

	/**
	 * returns the letter tile on the square
	 * 
	 * @return letter, or null if none exists
	 */
	public Tile getLetter() {
		return letter;
	}
}
