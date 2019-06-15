package com.nikbal.scrabble.dao;

import java.util.List;

import com.nikbal.scrabble.entity.Move;

public interface MoveDao{

	void saveMove(Move move);

	List<Move> findMoveListByBoardId(Long boardId);

	List<Move> findMoveListBySequence(Long boardId, Integer sequence);
}
