package com.nikbal.scrabble.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nikbal.scrabble.entity.Board;

@Repository
@Transactional
public class BoardDaoImpl implements BoardDao {

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
	public void updateBoard(Board board) {
		getCurrentSession().update(board);
	}

}
