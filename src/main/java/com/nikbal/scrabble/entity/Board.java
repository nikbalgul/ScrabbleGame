package com.nikbal.scrabble.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.nikbal.scrabble.constant.ScrabbleConstant;
import com.nikbal.scrabble.model.Coord;
import com.nikbal.scrabble.model.Dictionary;
import com.nikbal.scrabble.model.Square;
import com.nikbal.scrabble.model.Tile;
import org.springframework.util.CollectionUtils;

/*
 *
 *	BOARD Entity
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
    @Column(name = "STATUS")
    private String status;
    @Column(name = "MOVE_SEQ")
    private Integer moveSeq;
    @Transient
    private Dictionary dict;
    @Transient
    private int length = 15;
    @Transient
    private Square[][] boardArr;
    @Transient
    private Set<Tile> tilesOnBoard;
    @Transient
    private int sumScore = 0;

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

    public Integer getMoveSeq() {
        return moveSeq;
    }

    public void setMoveSeq(Integer moveSeq) {
        this.moveSeq = moveSeq;
    }

    public Set<Tile> getTilesOnBoard() {
        return tilesOnBoard;
    }

    public void setTilesOnBoard(Set<Tile> tilesOnBoard) {
        this.tilesOnBoard = tilesOnBoard;
    }

    public int getSumScore() {
        return sumScore;
    }

    public void setSumScore(int sumScore) {
        this.sumScore = sumScore;
    }

    /**
     * inserts the given letters onto the board and updates the list of letters on
     * the board
     *
     * @param move list of letters
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
     * @param coord     location on the board to start the score counting
     * @param dirPlaced direction to check for the word
     * @param dirCheck  direction to check for the word
     * @return the total score earned for the word
     */
    public int getWordScore(Coord coord, String dirPlaced, String dirCheck) {
        if (coord.getRow() < 0 || coord.getCol() < 0 || coord.getRow() >= length && coord.getCol() >= length)
            return 0;

        int score = 0;
        // variables that change the column or row depending on the orientation
        // of the added word
        int rowChange = 0;
        int colChange = 0;
        int r = coord.getRow();
        int c = coord.getCol();

        // set the direction of the coordinate change variables
        if (dirCheck.equals(ScrabbleConstant.VERTICAL))
            rowChange++;
        else if (dirCheck.equals(ScrabbleConstant.HORIZONTAL))
            colChange++;

        // for each letter left of or above (depending on direction)
        // the given coordinate, add the score value and trigger any unused
        // scoreModifiers on the newly placed tiles' squares
        while (r >= 0 && c >= 0 && boardArr[r][c].getLetter() != null) {
            // update the score
            score += boardArr[r][c].getLetter().getValue();
            r -= rowChange;
            c -= colChange;
        }

        r = coord.getRow() + rowChange;
        c = coord.getCol() + colChange;

        // for each letter right of or below (depending on direction)
        // the given coordinate, add the score value and trigger any unused
        // scoreModifiers on the newly placed tiles' squares
        while (r < length && c < length && boardArr[r][c].getLetter() != null) {
            score += boardArr[r][c].getLetter().getValue();
            r += rowChange;
            c += colChange;
        }
        if (!dirCheck.equals(dirPlaced) && !makesNewWord(coord, dirCheck))
            return 0;
        // return the word score multiplied by any word score modifiers
        return score;
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
        if (dir.equals(ScrabbleConstant.VERTICAL))
            rowChange++;
        else if (dir.equals(ScrabbleConstant.HORIZONTAL))
            colChange++;

        // check if the letter above or to the left of the current letter was
        // placed before this turn, meaning this turn creates a new word in that
        // direction (depends on given direction)
        if (r - rowChange >= 0 && c - colChange >= 0 && boardArr[r - rowChange][c - colChange].getLetter() != null
                && boardArr[r - rowChange][c - colChange].getTurnPlaced() < turn) {
            return true;
        }

        // check if the letter below or to the right of the current letter was
        // placed before this turn, meaning this turn creates a new word in that
        // direction (depends on given direction)
        if (r + rowChange >= 0 && c + colChange >= 0 && boardArr[r + rowChange][c + colChange].getLetter() != null
                && boardArr[r + rowChange][c + colChange].getTurnPlaced() < turn) {
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

    /**
     * checks if the board contains the given letter
     *
     * @param letter the letter to be checked
     * @return true if on the board
     */
    public boolean contains(Tile letter) {
        while (tilesOnBoard.iterator().hasNext()) {
            if (letter.getLetter() == tilesOnBoard.iterator().next().getLetter()) {
                return true;
            }
        }
        return false;
    }

    public Move getPossibility(Move letters, Dictionary dictionary) {
        int startx = letters.getStartx();
        int starty = letters.getStarty();
        int endx = letters.getEndx();
        int endy = letters.getEndy();
        String dir = ScrabbleConstant.SINGLE;
        if (startx == endx) {
            dir = ScrabbleConstant.HORIZONTAL;
        } else if (starty == endy) {
            dir = ScrabbleConstant.VERTICAL;
        }
        dict = dictionary;
        // check for an empty letter list (invalid input)
        if (letters == null || letters.getLetters().isEmpty())
            return null;
        // check if the move is the first move of the game
        if (CollectionUtils.isEmpty(this.tilesOnBoard))
            return getFirstMove(letters, dir);
        List<Tile> tilesOnBoardList = new ArrayList<>(this.tilesOnBoard);

        Coord coord = new Coord(startx, starty);
        // for each letter on the board, check if the word can be added in the
        // vertical or horizontal direction using the letter already on the
        // board, which is compared with every letter in the word
        for (int i = 0; i < tilesOnBoardList.size(); i++) {
            for (int j = 0; j < letters.getLetters().size(); j++) {
                if (Character.toLowerCase(tilesOnBoardList.get(i).getLetter()) == Character.toLowerCase(letters.getLetters().get(j).getLetter())
                        && letters.getLetters().size() > 1) {
                    // move is a singular possible move for a list of letters,
                    if (dir.equals(ScrabbleConstant.VERTICAL)) {
                        return findVerticalIntersections(letters);
                    } else {
                        return findHorizontalIntersections(letters);
                    }
                }
            }
        }
        // move is a singular possible move for a list of letters
        Move move = null;
        // for every coordinate in the list of adjacent sqaures, check if
        // the word can be added in the vertical or horizontal direction,
        // using the coordinate as the starting point for each letter in the
        // word being checked
        if (dir.equals(ScrabbleConstant.VERTICAL)) {
            move = findVerticalAdj(letters, coord);
        } else {
            move = findHorizontalAdj(letters, coord);
        }
        return move;
    }

    /**
     * returns a list of possible moves for the first word on the board
     *
     * @param letters list of letters to try
     * @param dir
     * @return list of possible letter locations
     */
    private Move getFirstMove(Move letters, String dir) {
        // check if the word is a one-letter word
        if (letters.getLetters().size() == 1 && !isWord(String.valueOf(letters.getLetters().get(0).getLetter())))
            return null;
        // create a coordinate at the center of the board
        Coord coord = new Coord(letters.getStartx(), letters.getStarty());
        // move is a singular possible move for a list of letters
        Move move = null;
        // for each letter, check vertical and horizontal directions that the
        // word can be placed, starting at the given coordinate (the center)
        if (dir.equals(ScrabbleConstant.VERTICAL)) {
            move = findVerticalAdj(letters, coord);
        } else {
            move = findHorizontalAdj(letters, coord);
        }
        return move;
    }

    /**
     * find any words in the vertical direction, with at least one letter
     * intersecting with an existing letter
     *
     * @param letters list of letters to check
     * @return list of letters with coordinates on the board
     */
    private Move findVerticalIntersections(Move letters) {
        Move move = new Move();
        int r = letters.getStartx();
        int c = letters.getStarty();
        for (int i = 0; i < letters.getLetters().size(); i++) {
            if (boardArr[r + i][c].getLetter() == null || (boardArr[r + i][c].getLetter() != null
                    && Character.toLowerCase(boardArr[r + i][c].getLetter().getLetter()) == Character.toLowerCase(letters.getLetters().get(i).getLetter()))) {
                Tile temp = new Tile(letters.getLetters().get(i).getLetter(), -1);
                temp.setLoc(new Coord(r + i, c));
                move.getLetters().add(temp);
            } else {
                return null;
            }
        }
        if (!isLegalMove(move, ScrabbleConstant.VERTICAL))
            return null;

        if (move.getLetters().isEmpty())
            return null;
        return move;
    }

    /**
     * find any words in the vertical direction, without any letter intersecting
     * with an existing letter
     *
     * @param letters list of letters to check
     * @param coord   coordinate of the board to start at
     * @return list of letters with coordinates on the board
     */
    private Move findVerticalAdj(Move letters, Coord coord) {
        Move move = new Move();
        for (int i = 0; i < letters.getLetters().size(); i++) {
            if ((coord.getRow() - i >= 0)
                    && (coord.getRow() - i + letters.getLetters().size() < length)) {
                int r = coord.getRow() - i;
                int c = coord.getCol();
                if (boardArr[r + i][c].getLetter() == null) {
                    Tile temp = new Tile(letters.getLetters().get(i).getLetter(), -1);
                    temp.setLoc(new Coord(r + i, c));
                    move.getLetters().add(temp);
                } else {
                    return null;
                }
            }
        }
        if (!isLegalMove(move, ScrabbleConstant.VERTICAL))
            return null;
        if (move.getLetters().isEmpty())
            return null;
        return move;
    }

    /**
     * find any words in the horizontal direction, with at least one letter
     * intersecting with an existing letter
     *
     * @param letters list of letters to check
     * @return list of letters with coordinates on the board
     */
    private Move findHorizontalIntersections(Move letters) {
        Move move = new Move();
        int r = letters.getStartx();
        int c = letters.getStarty();
        for (int i = 0; i < letters.getLetters().size(); i++) {
            if (boardArr[r][c + i].getLetter() == null || (boardArr[r][c + i].getLetter() != null
                    && Character.toLowerCase(boardArr[r][c + i].getLetter().getLetter()) == Character.toLowerCase(letters.getLetters().get(i).getLetter()))) {
                Tile temp = new Tile(letters.getLetters().get(i).getLetter(), -1);
                temp.setLoc(new Coord(r, c + i));
                move.getLetters().add(temp);
            } else {
                return null;
            }
        }
        if (!isLegalMove(move, ScrabbleConstant.HORIZONTAL))
            return null;

        if (move.getLetters().isEmpty())
            return null;
        return move;
    }

    /**
     * find any words in the horizontal direction, without any letter intersecting
     * with an existing letter
     *
     * @param letters list of letters to check
     * @param coord   coordinate of the board to start at
     * @return list of letters with coordinates on the board
     */
    private Move findHorizontalAdj(Move letters, Coord coord) {
        Move move = new Move();

        for (int i = 0; i < letters.getLetters().size(); i++) {
            if ((coord.getCol() - i >= 0)
                    && (coord.getCol() - i + letters.getLetters().size() < length)) {
                int r = coord.getRow();
                int c = coord.getCol() - i;

                if (boardArr[r][c + i].getLetter() == null) {
                    Tile temp = new Tile(letters.getLetters().get(i).getLetter(), -1);
                    temp.setLoc(new Coord(r, c + i));
                    move.getLetters().add(temp);
                } else {
                    return null;
                }
            }
        }
        if (!isLegalMove(move, ScrabbleConstant.HORIZONTAL))
            return null;

        if (move.getLetters().isEmpty())
            return null;
        return move;
    }

    /**
     * check if a move creates a legal word in every possible direction
     *
     * @param move list of letters with coordinates
     * @param dir  direction the letters would be added to the board
     * @return
     */
    private boolean isLegalMove(Move move, String dir) {
        // one of the possibly many words created by the move, to be checked
        // with the supplied dictionary
        String word = "";
        int r = 0;
        int c = 0;
        // check the word created in the vertical direction only once, then for
        // every letter in the move, check the word created in the horizontal
        // direction
        if (dir.equals(ScrabbleConstant.VERTICAL)) {
            // add the letters in the move to the word created
            for (int i = 0; i < move.getLetters().size(); i++) {
                word += move.getLetters().get(i).getLetter();
            }
            // add any adjacent letters on the board above the first letter in
            // move, adds the letters to the beginning of the word created
            r = move.getLetters().get(0).getLoc().getRow() - 1;
            c = move.getLetters().get(0).getLoc().getCol();
            while (r >= 0 && boardArr[r][c].getLetter() != null) {
                word = boardArr[r][c].getLetter().getLetter() + word;
                r--;
            }
            // add any adjacent letters on the board below the last letter in
            // move, adds the letters to the end of the word created
            r = move.getLetters().get(move.getLetters().size() - 1).getLoc().getRow() + 1;
            while (r < length && boardArr[r][c].getLetter() != null) {
                word += boardArr[r][c].getLetter().getLetter();
                r++;
            }
            // check if the created word is in the dictionary provided if
            // the word created is longer than one letter
            if (word.length() > 1 && !isWord(word))
                return false;

            // add any adjacent letters on the board that are left and right of
            // the current letter of move at the given coordinate, adds the
            // letters to the beginning and end of the word created,
            // respectively
            for (int i = 0; i < move.getLetters().size(); i++) {
                // clear the word to be checked
                word = "";
                // add the current letter to the word to be checked
                word += move.getLetters().get(i).getLetter();
                r = move.getLetters().get(i).getLoc().getRow();
                c = move.getLetters().get(i).getLoc().getCol() - 1;
                // add letters left of the current letter
                while (c >= 0 && boardArr[r][c].getLetter() != null) {
                    word = boardArr[r][c].getLetter().getLetter() + word;
                    c--;
                }
                // add letters right of the current letter
                c = move.getLetters().get(move.getLetters().size() - 1).getLoc().getCol() + 1;
                while (c < length && boardArr[r][c].getLetter() != null) {
                    word += boardArr[r][c].getLetter().getLetter();
                    c++;
                }
                // check if the created word is in the dictionary provided if
                // the word created is longer than one letter
                if (word.length() > 1 && !isWord(word))
                    return false;
            }
        }
        if (dir.equals(ScrabbleConstant.HORIZONTAL)) {
            // add the letters in the move to the word created
            for (int i = 0; i < move.getLetters().size(); i++) {
                word += move.getLetters().get(i).getLetter();
            }
            // add any adjacent letters on the board left of the first letter in
            // move, adds the letters to the beginning of the word created
            r = move.getLetters().get(0).getLoc().getRow();
            c = move.getLetters().get(0).getLoc().getCol() - 1;
            while (c >= 0 && boardArr[r][c].getLetter() != null) {
                word = boardArr[r][c].getLetter().getLetter() + word;
                c--;
            }
            // add any adjacent letters on the board right of the last letter in
            // move, adds the letters to the end of the word created
            c = move.getLetters().get(move.getLetters().size() - 1).getLoc().getCol() + 1;
            while (c < length && boardArr[r][c].getLetter() != null) {
                word += boardArr[r][c].getLetter().getLetter();
                c++;
            }
            // check if the created word is in the dictionary provided if
            // the word created is longer than one letter
            if (word.length() > 1 && !isWord(word))
                return false;

            // add any adjacent letters on the board that are above and below
            // the current letter of move at the given coordinate, adds the
            // letters to the beginning and end of the word created,
            // respectively
            for (int i = 0; i < move.getLetters().size(); i++) {
                // clear the word to be checked
                word = "";
                // add the current letter to the word to be checked
                word += move.getLetters().get(i).getLetter();
                r = move.getLetters().get(i).getLoc().getRow() - 1;
                c = move.getLetters().get(i).getLoc().getCol();
                // add letters above the current letter
                while (r >= 0 && boardArr[r][c].getLetter() != null) {
                    word = boardArr[r][c].getLetter().getLetter() + word;
                    r--;
                }
                // add letters below the current letter
                r = move.getLetters().get(move.getLetters().size() - 1).getLoc().getRow() + 1;
                while (r < length && boardArr[r][c].getLetter() != null) {
                    word += boardArr[r][c].getLetter().getLetter();
                    r++;
                }
                // check if the created word is in the dictionary provided if
                // the word created is longer than one letter
                if (word.length() > 1 && !isWord(word))
                    return false;
            }
        }
        return !isReplacing(move);
    }

    private boolean isReplacing(Move move) {
        for (int i = 0; i < move.getLetters().size(); i++) {
            int r = move.getLetters().get(i).getLoc().getRow();
            int c = move.getLetters().get(i).getLoc().getCol();
            if (boardArr[r][c].getLetter() == null)
                return false;
        }
        return true;
    }

    /**
     * checks if the given word is part of the dictionary
     *
     * @param word the given word to check
     * @return true if word is in the dictionary
     */
    private boolean isWord(String word) {
        return dict.isWord(word);
    }

    public String toString() {
        return "BOARD(" + " STATUS:" + status + " MOVE_SEQ:" + moveSeq + " )";
    }
}
