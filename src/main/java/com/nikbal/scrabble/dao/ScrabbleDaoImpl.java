package com.nikbal.scrabble.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nikbal.scrabble.entity.Board;
import com.nikbal.scrabble.entity.Game;
import com.nikbal.scrabble.entity.Move;
import com.nikbal.scrabble.entity.Score;

@Repository
@Transactional
public class ScrabbleDaoImpl implements ScrabbleDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void saveBoard(Board board) {
		getCurrentSession().save(board);
	}

	@Override
	public void saveGame(Game game) {
		getCurrentSession().save(game);
	}

	@Override
	public void updateGame(Game game) {
		getCurrentSession().update(game);
	}

	@Override
	public void saveMove(Move move) {
		getCurrentSession().save(move);
	}

	@Override
	public void saveScore(Score score) {
		getCurrentSession().save(score);
	}

	@Override
	public void updateBoard(Board board) {
		getCurrentSession().update(board);
	}

	@Override
	public List<Move> findMoveListByBoardId(Long boardId) {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(Move.class);
		criteria.add("boardId", boardId);
		criteria.list();
	}
}
