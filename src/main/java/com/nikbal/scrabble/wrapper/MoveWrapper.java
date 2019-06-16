package com.nikbal.scrabble.wrapper;

import com.nikbal.scrabble.model.MoveModel;

/**
 * 
 * 
 *
 *
 */

public class MoveWrapper {

	private Long boardId;
	private MoveModel[] moves;

	public Long getBoardId() {
		return boardId;
	}

	public void setBoardId(Long boardId) {
		this.boardId = boardId;
	}

	public MoveModel[] getMoves() {
		return moves;
	}

	public void setMoves(MoveModel[] moves) {
		this.moves = moves;
	}

}
