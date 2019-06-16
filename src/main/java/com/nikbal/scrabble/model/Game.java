package com.nikbal.scrabble.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.nikbal.scrabble.constant.ScrabbleConstant;
import com.nikbal.scrabble.entity.Board;
import com.nikbal.scrabble.entity.Move;

/**
 *
 */
public class Game {
    private List<Tile> tileBag = new ArrayList<>();
    private List<Board> boardList = new ArrayList<>();
    private String filePath = "src/main/resources/";
    private String tilesFile = "tiles.txt";
    private Dictionary dictionary;

    private static final Logger logger = Logger.getLogger(Game.class);

    public Game() {
        dictionary = new Dictionary();
    }

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

    public String getTilesFile() {
        return tilesFile;
    }

    public void setTilesFile(String tilesFile) {
        this.tilesFile = tilesFile;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * initializes the bag of tiles, read from a file
     *
     * @throws FileNotFoundException
     */
    public void initTileBag() {
        File file;
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(tilesFile);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            file = new File(resource.getFile());
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String name = scanner.next();
                int value = Integer.parseInt(scanner.next());
                // add a letter tile to the tileBag
                char letter = name.charAt(0);
                tileBag.add(new Tile(letter, value));
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found: " + filePath + "tiles.txt");
        }
    }

    public Move isLegalMove(Move word, Board board) {
        // no moves if there is no input
        if (word.getText().isEmpty())
            return null;
        // create a list of letter tiles from the given string
        for (int i = 0; i < word.getText().length(); i++) {
            word.getLetters().add(new Tile(word.getText().toUpperCase().charAt(i), -1, new Coord(word.getStartx(), word.getStarty())));
        }
        return board.getPossibility(word, this.getDictionary());
    }

    /**
     * inserts the given word at the given letter coordinates also activates any
     * score modifiers
     *
     * @param board current board
     * @param move  given word
     */
    public void insertWord(Board board, Move move) {
        // direction to be added onto the board, by default = ScrabbleConstant.VERTICAL
        String dir = ScrabbleConstant.SINGLE;
        Move tempMove = new Move();
        int startx = move.getStartx();
        int starty = move.getStarty();
        int endx = move.getEndx();
        int endy = move.getEndy();

        if (startx == endx) {
            dir = ScrabbleConstant.HORIZONTAL;
        } else if (starty == endy) {
            dir = ScrabbleConstant.VERTICAL;
        }

        for (int i = 0; i < move.getText().length(); i++) {

            char chr = move.getText().charAt(i);
            Tile tile = new Tile(chr, getTileValueFromTileBag(chr));
            if (dir.equals(ScrabbleConstant.VERTICAL)) {
                tile.setLoc(new Coord(startx++, starty));
            } else {
                tile.setLoc(new Coord(startx, starty++));
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

        // insert letters in move
        move.setLetters(tempMove.getLetters());
        board.insertWord(move);

        // tally the score for each individual word created by the tempMove
        // also triggers any score modifiers (in square.java)
        for (int i = 0; i < move.getText().length(); i++) {
            // if the tempMove was placed in the vertical direction...
            if (dir.equals(ScrabbleConstant.VERTICAL)) {

                if (i == 0) {
                    int wordScore = board.getWordScore(move.getLetters().get(i).getLoc(), dir, ScrabbleConstant.VERTICAL);
                    move.setSumScore(wordScore);
                }

                // check the score of the word created in the horizontal
                // direction, for the current coordinate
                if (board.hasNeighbor(move.getLetters().get(i).getLoc())[1]) {
                    int wordScore = board.getWordScore(move.getLetters().get(i).getLoc(), dir, ScrabbleConstant.HORIZONTAL);
                    move.setSumScore(wordScore);
                }

            }
            // if the tempMove was placed in the horizontal direction...
            if (dir.equals(ScrabbleConstant.HORIZONTAL)) {
                // to avoid repeated scoring, only check the score of the
                // created word in the horizontal direction once
                if (i == 0) {
                    int wordScore = board.getWordScore(move.getLetters().get(i).getLoc(), dir, ScrabbleConstant.HORIZONTAL);
                    move.setSumScore(wordScore);
                }

                // check the score of the word created in the horizontal
                // direction, for the current coordinate
                if (board.hasNeighbor(move.getLetters().get(i).getLoc())[0]) {
                    int wordScore = board.getWordScore(move.getLetters().get(i).getLoc(), dir, ScrabbleConstant.VERTICAL);
                    move.setSumScore(wordScore);
                }
            }
            // if the tempMove contains a single direction, add the score only for
            // the directions that have adjacent letters on the board
            if (dir.equals(ScrabbleConstant.SINGLE)) {
                int wordScore;
                wordScore = board.getWordScore(move.getLetters().get(i).getLoc(), dir, ScrabbleConstant.HORIZONTAL);
                if (wordScore > move.getLetters().get(i).getValue())
                    move.setSumScore(wordScore);
                wordScore = board.getWordScore(move.getLetters().get(i).getLoc(), dir, ScrabbleConstant.VERTICAL);
                if (wordScore > move.getLetters().get(i).getValue())
                    move.setSumScore(wordScore);
            }
        }
    }

    private int getTileValueFromTileBag(char chr) {
        for (Tile tile : this.getTileBag()) {
            if (Character.toLowerCase(tile.getLetter()) == Character.toLowerCase(chr)) {
                return tile.getValue();
            }
        }
        return 0;
    }

    /**
     *
     */
    public boolean hasLetter(char letter) {
        for (Tile tile : this.getTileBag()) {
            if (tile.getLetter() == letter) {
                return true;
            }
        }
        return false;
    }

    public Board getBoardFromGame(Long boardId) {
        for (Board currentBoard :
                getBoardList()) {
            if (currentBoard.getBoardId().equals(boardId)) {
                return currentBoard;
            }
        }
        return null;
    }

}
