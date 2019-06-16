package com.nikbal.scrabble.model;

/**
 * tile representing a letter with a point value
 * 
 *
 *
 */
public class Tile {

	private char letter;
	private int value;
	private Coord loc;

	/**
	 * initializes the letter tile
	 * 
	 * @param l letter value
	 */
	public Tile(char l, int v) {
		letter = l;
		value = v;
		loc = new Coord(-1, -1);
	}

	public Tile(char chr) {
		this.letter = chr;
	}

	public int getValue() {
		return value;
	}
	
	public char getLetter() {
		return letter;
	}

	public void setLetter(char letter) {
		this.letter = letter;
	}

	public Coord getLoc() {
		return loc;
	}

	public void setLoc(Coord loc) {
		this.loc = loc;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return letter + "[" + value + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Tile))
			return false;
		if (obj == this)
			return true;
		Tile c = (Tile) obj;
		return (this.letter == c.letter);
	}

	@Override
	public int hashCode() {
		return 0;
	}

}
