package com.nikbal.scrabble.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nikbal.scrabble.entity.Move;

@Repository
@Transactional
public class MoveDaoImpl implements MoveDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void saveMove(Move move) {
		getCurrentSession().save(move);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Move> findMoveListByBoardId(Long boardId) {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(Move.class);
		criteria.add(Restrictions.eq("boardId", boardId));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Move> findMoveListBySequence(Long boardId, Integer sequence) {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(Move.class);
		criteria.add(Restrictions.eq("sequence", sequence));
		criteria.add(Restrictions.eq("boardId", boardId));
		return criteria.list();
	}
}
