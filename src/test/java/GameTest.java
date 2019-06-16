
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nikbal.scrabble.entity.Board;
import com.nikbal.scrabble.entity.Move;
import com.nikbal.scrabble.model.Coord;
import com.nikbal.scrabble.model.Game;
import com.nikbal.scrabble.model.Square;
import com.nikbal.scrabble.model.Tile;

public class GameTest {

    private Game g;
    private Move move;
    private Move move2;
    private Board board;

    @Before
    public void setUp() throws Exception {
        g = new Game();
        board = createBoard();
        move = createMove();
    }

    @Test
    public void testGame() {
        assertFalse(g.getDictionary().getList().isEmpty());
    }

    @Test
    public void isNotLegalMove() {
        insertBoardArr(board, move);
        assertFalse(move.getText().isEmpty());
        // create a list of letter tiles from the given string
        for (int i = 0; i < move.getText().length(); i++) {
            move.getLetters().add(new Tile(move.getText().toUpperCase().charAt(i), -1,
                    new Coord(move.getStartx(), move.getStarty())));
        }
        Assert.assertNull(board.getPossibility(move, board.getDict()));
    }

    private Board createBoard() {
        Board board = new Board();
        board.setBoardId(1L);
        board.setStatus("A");
        board.setSumScore(0);
        board.setDict(g.getDictionary());
        board.setMoveSeq(0);
        return board;
    }

    private Move createMove() {
        Move move = new Move();
        Tile t1 = new Tile('a', 1, new Coord(5, 9));
        Tile t2 = new Tile('m', 3, new Coord(5, 10));
        Tile t3 = new Tile('i', 2, new Coord(5, 11));
        Tile t4 = new Tile('r', 4, new Coord(5, 12));
        List<Tile> tiles = new ArrayList<>();
        tiles.add(t1);
        tiles.add(t2);
        tiles.add(t3);
        tiles.add(t4);
        move.setText("amir");
        move.setStartx(5);
        move.setStarty(9);
        move.setEndx(5);
        move.setEndy(12);
        move.setBoardId(1L);
        move.setMoveNum(0);
        move.setMoveSeq(0);
        move.setLetters(tiles);
        return move;
    }

    public void insertBoardArr(Board board, Move move) {
        Square[][] boardArr = new Square[15][15];
        for (int i = 0; i < move.getLetters().size(); i++) {
            Square square = new Square();
            int row = move.getLetters().get(i).getLoc().getRow();
            int col = move.getLetters().get(i).getLoc().getCol();
            boardArr[row][col] = square;
            boardArr[row][col].setLetter(move.getLetters().get(i));
        }
        board.setBoardArr(boardArr);
    }
}
