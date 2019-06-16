package com.nikbal.scrabble.wrapper;

import java.util.List;

import com.nikbal.scrabble.model.MoveModel;

/**
 * 
 * 
 *
 *
 */

public class MoveWrapper {

	private Long boardId;
	private List<MoveModel> moves;

	public Long getBoardId() {
		return boardId;
	}

	public void setBoardId(Long boardId) {
		this.boardId = boardId;
	}

	public List<MoveModel> getMoves() {
		return moves;
	}

	public void setMoves(List<MoveModel> moves) {
		this.moves = moves;
	}

}
