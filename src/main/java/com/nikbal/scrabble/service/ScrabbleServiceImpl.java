package com.nikbal.scrabble.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikbal.scrabble.dao.ScrabbleDao;
import com.nikbal.scrabble.entity.Board;
import com.nikbal.scrabble.entity.Game;
import com.nikbal.scrabble.entity.Move;
import com.nikbal.scrabble.entity.Score;

@Service
public class ScrabbleServiceImpl implements ScrabbleService {

	@Autowired
	private ScrabbleDao scrabbleDao;

	@Override
	public void saveBoard(Board board) {
		scrabbleDao.saveBoard(board);
	}

	@Override
	public Game saveOrUpdateGame(Game game) {
		if (game != null) {
			game.setStatus(String.valueOf('P'));
			scrabbleDao.updateGame(game);
		}
		game = new Game();
		game.setStatus(String.valueOf('A'));
		scrabbleDao.saveGame(game);
		return game;
	}

	@Override
	public void saveMove(Move move) {
		scrabbleDao.saveMove(move);
	}

	@Override
	public void saveScore(Score score) {
		scrabbleDao.saveScore(score);
	}

}
