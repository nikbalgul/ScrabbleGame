package com.nikbal.scrabble.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikbal.scrabble.dao.MoveDao;
import com.nikbal.scrabble.entity.Move;

@Service
public class MoveServiceImpl implements MoveService {

	@Autowired
	private MoveDao moveDao;

	@Override
	public void saveMove(Move move) {
		moveDao.saveMove(move);
	}

	@Override
	public List<Move> getMoveListByBoardId(Long boardId) {
		return moveDao.findMoveListByBoardId(boardId); 
	}

	@Override
	public List<Move> getMoveListBySequence(Long boardId, Integer sequence) {
		return moveDao.findMoveListBySequence(boardId, sequence); 
	}

}
