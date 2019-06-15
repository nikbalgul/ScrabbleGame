package com.nikbal.scrabble.service;

import com.nikbal.scrabble.entity.Board;

public interface BoardService {
	
	void saveBoard(Board board);

	void updateBoard(Board board);
}
