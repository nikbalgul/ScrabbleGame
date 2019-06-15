package com.nikbal.scrabble.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nikbal.scrabble.entity.Score;

@Repository
@Transactional
public class ScoreDaoImpl implements ScoreDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void saveScore(Score score) {
		getCurrentSession().save(score);
	}
}
