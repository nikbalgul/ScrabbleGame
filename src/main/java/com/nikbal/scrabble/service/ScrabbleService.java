package com.nikbal.scrabble.service;

import java.util.List;

import com.nikbal.scrabble.entity.Board;
import com.nikbal.scrabble.entity.Game;
import com.nikbal.scrabble.entity.Move;
import com.nikbal.scrabble.entity.Score;

public interface ScrabbleService {
	
	void saveBoard(Board board);

	Game saveOrUpdateGame(Game game);

	void saveMove(Move move);

	void saveScore(Score score);

	void updateBoard(Board board);

	List<Move> getMoveListByBoardId(Long boardId);

	List<Move> getMoveListBySequence(Long boardId, Integer sequence);
}
