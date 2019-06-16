package com.nikbal.scrabble.model;

/**
 * represents each square on the board grid, storing information on the letters,
 * score modifiers and special tiles placed on the square
 * 
 *
 *
 */
public class Square {

	private String scoreModifier;
	private Tile letter;
	private int turnPlaced = 0;

	/**
	 * initializes the square, to default of no score modifiers, special tiles
	 * or letters
	 */
	public Square() {
		scoreModifier = null;
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
	 * sets the score modifier to the given type
	 * 
	 * @param modifier
	 *            type of score modifier
	 */
	public void setScoreModifier(String modifier) {
		scoreModifier = modifier;
	}

	/**
	 * returns the score modifier
	 * 
	 * @return score modifier
	 */
	public String getScoreModifier() {
		return scoreModifier;
	}

	/**
	 * updates the given value or score based on the score modifier
	 * 
	 * @param value
	 *            either a score multiplier or score itself, to be updated
	 * @param type
	 *            letter or word score
	 * @return updated value
	 */
	public int updateScore(int value, String type) {
		// check the type of score to be modified, and the modifier amount
		if (scoreModifier == null)
			return value;
		else if (type.equals("letter")) {
			if (scoreModifier.equals("2xl"))
				return value * 2;
			else if (scoreModifier.equals("3xl"))
				return value * 3;
		} else if (type.equals("word")) {
			if (scoreModifier.equals("2xw"))
				return value * 2;
			else if (scoreModifier.equals("3xw"))
				return value * 3;
		}
		return value;
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

	@Override
	public String toString() {
		if (letter != null)
			return letter.getLetter() + " ";
		else if (scoreModifier != null) {
			if (scoreModifier.equals("2xl"))
				return 1 + " ";
			else if (scoreModifier.equals("3xl"))
				return 2 + " ";
			else if (scoreModifier.equals("2xw"))
				return 3 + " ";
			else if (scoreModifier.equals("3xw"))
				return 4 + " ";
		}
		return "- ";
	}
}
