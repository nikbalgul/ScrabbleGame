package com.nikbal.scrabble.entity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.nikbal.scrabble.model.Coord;
import com.nikbal.scrabble.model.Tile;

/**
 * 
 * 
 * 
 *
 *
 */
@Entity
@Table(name = "GAME")
public class Game {
	@Id
	@SequenceGenerator(name = "GAME_SEQUENCE", sequenceName = "GAME_SEQUENCE_ID", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "GAME_SEQUENCE")
	private Long gameId;
	@Column(name = "STATUS")
	private String status;
	@Transient
	private List<Tile> tileBag = new ArrayList<>();
	@Transient
	private List<Board> boardList = new ArrayList<>();
	@Transient
	private String filePath = "src/main/resources/";
	@Transient
	private String tilesFile = "tiles.txt";

	public List<Tile> getTileBag() {
		return tileBag;
	}

	public void setTileBag(List<Tile> tileBag) {
		this.tileBag = tileBag;
	}

	public List<Board> getBoardList() {
		return boardList;
	}

	public void setBoardList(List<Board> boardList) {
		this.boardList = boardList;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public String getTilesFile() {
		return tilesFile;
	}

	public void setTilesFile(String tilesFile) {
		this.tilesFile = tilesFile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * initializes the bag of tiles, read from a file
	 * 
	 * @throws FileNotFoundException
	 */
	public void initTileBag() {
		try (Scanner scanner = new Scanner(new File(filePath + tilesFile))) {
			while (scanner.hasNext()) {
				String name = scanner.next();
				int value = Integer.parseInt(scanner.next());
				// add a letter tile to the tileBag
				char letter = name.charAt(0);
				tileBag.add(new Tile(letter, value));
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + filePath + "tiles.txt");
		}
	}

	/**
	 * inserts the given word at the given letter coordinates also activates any
	 * score modifiers
	 * 
	 * @param locs list of letters
	 *
	 */
	public void insertWord(Board board, Move move) {
		// direction to be added onto the board, by default = "vertical"
		String dir = "single";
		Move tempMove = new Move();
		int startx = move.getStartx();
		int starty = move.getStarty();
		int endx = move.getEndx();
		int endy = move.getEndy();

		if (startx == endx) {
			dir = "vertical";
		} else if (starty == endy) {
			dir = "horizontal";
		} else {
			return;
		}

		for (int i = 0; i < move.getText().length(); i++) {

			char chr = move.getText().charAt(i);
			Tile tile = new Tile(chr, 0);
			if (dir.equals("vertical")) {
				tile.setLoc(new Coord(startx, starty++));
			} else {
				tile.setLoc(new Coord(startx++, starty));
			}

			int row = tile.getLoc().getRow();
			int col = tile.getLoc().getCol();

			// check if letter should take place of a blank tile
			if (!hasLetter(chr) && board.getSquare(row, col).getLetter() == null) {
				// creates a new Tile based on the letter the
				// blank tile will be used for
				tempMove.getLetters().add(tile);
			}
		}

		// insert letters in tempMove
		board.insertWord(tempMove);

		List<Integer> gapIndex = new ArrayList<>();
		gapIndex.add(0);
		if (tempMove.getText().length() > 1) {
			int prevR = tempMove.getLetters().get(0).getLoc().getRow();
			int prevC = tempMove.getLetters().get(0).getLoc().getCol();
			for (int i = 1; i < tempMove.getText().length(); i++)
				if (tempMove.getLetters().get(i).getLoc().getRow() - prevR > 1
						|| tempMove.getLetters().get(i).getLoc().getCol() - prevC > 1)
					gapIndex.add(i);
		}

		// tally the score for each individual word created by the tempMove
		// also triggers any score modifiers (in square.java)
		for (int i = 0; i < tempMove.getText().length(); i++) {
			int row = tempMove.getLetters().get(i).getLoc().getRow();
			int col = tempMove.getLetters().get(i).getLoc().getCol();
			// if the tempMove was placed in the vertical direction...
			if (dir.equals("vertical")) {
				// to avoid repeated scoring, only check the score of the
				// created word in the vertical direction once, or at the start
				// of every gap
				for (int g = 0; g < gapIndex.size(); g++) {
					if (i == gapIndex.get(g)) {
						int wordScore = board.getWordScore(tempMove.getLetters().get(i).getLoc(), dir, "vertical");
						tempMove.setSumScore(tempMove.getSumScore() + wordScore);
						board.setWordScore(wordScore);
					}
				}

				// check the score of the word created in the horizontal
				// direction, for the current coordinate
				if (board.hasNeighbor(tempMove.getLetters().get(i).getLoc())[1]) {
					int wordScore = board.getWordScore(tempMove.getLetters().get(i).getLoc(), dir, "horizontal");
					tempMove.setSumScore(tempMove.getSumScore() + wordScore);
					board.setWordScore(wordScore);
				}

			}
			// if the tempMove was placed in the horizontal direction...
			if (dir.equals("horizontal")) {
				// to avoid repeated scoring, only check the score of the
				// created word in the horizontal direction once
				if (i == 0) {
					int wordScore = board.getWordScore(tempMove.getLetters().get(i).getLoc(), dir, "horizontal");
					tempMove.setSumScore(tempMove.getSumScore() + wordScore);
					board.setWordScore(wordScore);
				}

				// check the score of the word created in the horizontal
				// direction, for the current coordinate
				if (board.hasNeighbor(tempMove.getLetters().get(i).getLoc())[0]) {
					int wordScore = board.getWordScore(tempMove.getLetters().get(i).getLoc(), dir, "vertical");
					tempMove.setSumScore(tempMove.getSumScore() + wordScore);
					board.setWordScore(wordScore);
				}
			}
			// if the tempMove contains a single direction, add the score only for
			// the directions that have adjacent letters on the board
			if (dir.equals("single")) {
				int wordScore;
				wordScore = board.getWordScore(tempMove.getLetters().get(i).getLoc(), dir, "horizontal");
				board.setWordScore(wordScore);
				if (wordScore > tempMove.getLetters().get(i).getValue())
					tempMove.setSumScore(tempMove.getSumScore() + wordScore);
				wordScore = board.getWordScore(tempMove.getLetters().get(i).getLoc(), dir, "vertical");
				board.setWordScore(wordScore);
				if (wordScore > tempMove.getLetters().get(i).getValue())
					tempMove.setSumScore(tempMove.getSumScore() + wordScore);
			}
			// removes any score modifier used during the move
			board.getSquare(row, col).setScoreModifier(null);
		}
	}

	/**
	 * 
	 * 
	 * 
	 * 
	 */
	public boolean hasLetter(char letter) {
		for (Tile tile : this.getTileBag()) {
			if (tile.getChar() == letter) {
				return true;
			}
		}
		return false;
	}
}
