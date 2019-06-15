package com.nikbal.scrabble.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikbal.scrabble.dao.ScoreDao;
import com.nikbal.scrabble.entity.Score;

@Service
public class ScoreServiceImpl implements ScoreService {

	@Autowired
	private ScoreDao scoreDao;

	@Override
	public void saveScore(Score score) {
		scoreDao.saveScore(score);
	}

}
