package com.nikbal.scrabble.service;

import java.util.List;

import com.nikbal.scrabble.entity.Move;

public interface MoveService {

	void saveMove(Move move);

	List<Move> getMoveListByBoardId(Long boardId);

	List<Move> getMoveListBySequence(Long boardId, Integer sequence);
}
