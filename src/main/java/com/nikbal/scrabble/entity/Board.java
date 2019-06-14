package com.nikbal.scrabble.entity;

import javax.persistence.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.nikbal.scrabble.model.Coord;
import com.nikbal.scrabble.model.Dictionary;
import com.nikbal.scrabble.model.Square;

/*
 * 
 * 
 * 
 * 
 * */

@Entity
@Table(name = "BOARD")
public class Board {
	@Id
	@SequenceGenerator(name = "BOARD_SEQUENCE", sequenceName = "BOARD_SEQUENCE_ID", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "BOARD_SEQUENCE")
	private Long boardId;
	@Column(name = "GAMEID")
	private Long gameId;
	@Column(name = "STATUS")
	private String status;
	@Transient
	private Dictionary dict;
	@Transient
	private int length = 15;
	@Transient
	private Square[][] boardArr;

	public Long getBoardId() {
		return boardId;
	}

	public void setBoardId(Long boardId) {
		this.boardId = boardId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Dictionary getDict() {
		return dict;
	}

	public void setDict(Dictionary dict) {
		this.dict = dict;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Square[][] getBoardArr() {
		return boardArr;
	}

	public void setBoardArr(Square[][] boardArr) {
		this.boardArr = boardArr;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	/**
	 * inserts the given letters onto the board and updates the list of letters on
	 * the board
	 * 
	 * @param letters list of letters
	 */
	public void insertWord(Move move) {
		for (int i = 0; i < move.getLetters().size(); i++) {
			int row = move.getLetters().get(i).getLoc().getRow();
			int col = move.getLetters().get(i).getLoc().getCol();
			boardArr[row][col].setLetter(move.getLetters().get(i));
		}
	}

	/**
	 * returns a boolean if the square coordinate has an adjacent square containing
	 * a letter, in eithe rof the four directions
	 * 
	 * @param coord location of the square
	 * @return an array of booleans
	 */
	public boolean[] hasNeighbor(Coord coord) {
		// first index repreests the vertical neighbors and the second
		// index represents the horizontal neighbors
		boolean[] hasAdj = new boolean[2];
		int r = coord.getRow();
		int c = coord.getCol();
		// check if a letter exists above or below the given coordinate
		if ((r - 1 >= 0 && boardArr[r - 1][c].getLetter() != null)
				|| (r + 1 < length && boardArr[r + 1][c].getLetter() != null))
			hasAdj[0] = true;
		// check if a letter exists left or right of the given coordinate
		if ((c - 1 >= 0 && boardArr[r][c - 1].getLetter() != null)
				|| (c + 1 < length && boardArr[r][c + 1].getLetter() != null))
			hasAdj[1] = true;
		return hasAdj;
	}

	/**
	 * returns the total score earned for the placed letters, for any word made in
	 * the given direction
	 * 
	 * @param coord location on the board to start the score counting
	 * @param dir   direction to check for the word
	 * @return the total score earned for the word
	 */
	public int getWordScore(Coord coord, String dirPlaced, String dirCheck) {
		if (coord.getRow() < 0 || coord.getCol() < 0 || coord.getRow() >= length && coord.getCol() >= length)
			return 0;

		int score = 0;
		// the factor by which the final score of the word is multipled by
		int wordScoreModifier = 1;
		// variables that change the column or row depending on the orientation
		// of the added word
		int rowChange = 0;
		int colChange = 0;
		int r = coord.getRow();
		int c = coord.getCol();

		// set the direction of the coordinate change variables
		if (dirCheck.equals("vertical"))
			rowChange++;
		else if (dirCheck.equals("horizontal"))
			colChange++;

		// for each letter left of or above (depending on direction)
		// the given coordinate, add the score value and trigger any unused
		// scoreModifiers on the newly placed tiles' squares
		while (r >= 0 && c >= 0 && boardArr[r][c].getLetter() != null) {
			// update the score for any letter-based score modifiers
			score += boardArr[r][c].updateScore(boardArr[r][c].getLetter().getValue(), "letter");
			// update the word score factor for any word score modifiers
			wordScoreModifier = boardArr[r][c].updateScore(wordScoreModifier, "word");
			r -= rowChange;
			c -= colChange;
		}

		r = coord.getRow() + rowChange;
		c = coord.getCol() + colChange;

		// for each letter right of or below (depending on direction)
		// the given coordinate, add the score value and trigger any unused
		// scoreModifiers on the newly placed tiles' squares
		while (r < length && c < length && boardArr[r][c].getLetter() != null) {
			score += boardArr[r][c].updateScore(boardArr[r][c].getLetter().getValue(), "letter");
			wordScoreModifier = boardArr[r][c].updateScore(wordScoreModifier, "word");
			r += rowChange;
			c += colChange;
		}
		if (!dirCheck.equals(dirPlaced) && !makesNewWord(coord, dirCheck))
			return 0;
		// return the word score multiplied by any word score modifiers
		return score * wordScoreModifier;
	}

	/**
	 * checks if a new word is made in the given direction
	 * 
	 * @param coord coordinate to start from
	 * @param dir   direction to check
	 * @return true if a new word is made
	 */
	private boolean makesNewWord(Coord coord, String dir) {
		// variables that change the column or row depending on the orientation
		// of the added word
		int rowChange = 0;
		int colChange = 0;
		int r = coord.getRow();
		int c = coord.getCol();
		int turn = boardArr[r][c].getTurnPlaced();

		// set the direction of the coordinate change variables
		if (dir.equals("vertical"))
			rowChange++;
		else if (dir.equals("horizontal"))
			colChange++;

		// check if the letter above or to the left of the current letter was
		// placed before this turn, meaning this turn creates a new word in that
		// direction (depends on given direction)
		if (r - rowChange >= 0 && c - colChange >= 0 && boardArr[r - rowChange][c - colChange].getLetter() != null) {
			if (boardArr[r - rowChange][c - colChange].getTurnPlaced() < turn)
				return true;
		}

		// check if the letter below or to the right of the current letter was
		// placed before this turn, meaning this turn creates a new word in that
		// direction (depends on given direction)
		if (r + rowChange >= 0 && c + colChange >= 0 && boardArr[r + rowChange][c + colChange].getLetter() != null) {
			if (boardArr[r + rowChange][c + colChange].getTurnPlaced() < turn)
				return true;
		}
		return false;
	}

	/**
	 * returns a square at the given coordinate
	 * 
	 * @param row row of board
	 * @param col column of board
	 * @return Square at the location
	 */
	public Square getSquare(int row, int col) {
		if (row < 0 || col < 0 || row >= length || col >= length)
			return null;
		return boardArr[row][col];
	}

}
