package com.nikbal.scrabble.dao;

import java.util.List;

import com.nikbal.scrabble.entity.Board;
import com.nikbal.scrabble.entity.Game;
import com.nikbal.scrabble.entity.Move;
import com.nikbal.scrabble.entity.Score;

public interface ScrabbleDao{

	void saveBoard(Board board);

	void saveGame(Game game);

	void updateGame(Game game);

	void saveMove(Move move);

	void saveScore(Score score);

	void updateBoard(Board board);

	List<Move> findMoveListByBoardId(Long boardId);
}
