package com.nikbal.scrabble.service;

import com.nikbal.scrabble.entity.Board;
import com.nikbal.scrabble.entity.Game;
import com.nikbal.scrabble.entity.Move;
import com.nikbal.scrabble.entity.Score;

public interface ScrabbleService {
	void saveBoard(Board board);

	Game saveOrUpdateGame(Game game);

	void saveMove(Move move);

	void saveScore(Score score);
}
