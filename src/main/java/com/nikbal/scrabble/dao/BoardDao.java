package com.nikbal.scrabble.dao;

import com.nikbal.scrabble.entity.Board;

public interface BoardDao {

	void saveBoard(Board board);

	void updateBoard(Board board);
}
