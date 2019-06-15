package com.nikbal.scrabble.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikbal.scrabble.dao.BoardDao;
import com.nikbal.scrabble.entity.Board;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDao boardDao;

	@Override
	public void saveBoard(Board board) {
		boardDao.saveBoard(board);
	}

	@Override
	public void updateBoard(Board board) {
		boardDao.updateBoard(board);
	}

}
