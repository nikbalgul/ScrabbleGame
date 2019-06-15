package com.nikbal.scrabble.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nikbal.scrabble.entity.Board;
import com.nikbal.scrabble.entity.Game;
import com.nikbal.scrabble.model.Dictionary;
import com.nikbal.scrabble.model.Square;
import com.nikbal.scrabble.service.BoardService;

@RestController
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	@Autowired
	Game game;

	@RequestMapping(value = "/createBoard", method = RequestMethod.GET)
	public ResponseEntity<String> createBoard() {
		Board board = new Board();
		setBoardParams(board);
		boardService.saveBoard(board);
		game.getBoardList().add(board);
		return ResponseEntity.ok().body("Board ID:" + board.getBoardId());
	}

	@RequestMapping(value = "/setStatus", method = RequestMethod.GET)
	private ResponseEntity<String> setStatus(@RequestParam("boardId") Long boardId,
			@RequestParam("status") String status) {
		Board board = game.getBoardList().get(boardId.intValue());
		if (status.equals(String.valueOf('A')) && board.getStatus().equals(String.valueOf('P'))) {
			return ResponseEntity.ok().body("Board aktif yapılamaz! Board Status : P (Passive)");
		}
		board.setStatus(String.valueOf('P'));
		boardService.updateBoard(board);
		return ResponseEntity.ok().body("Board Status : P (Passive)");
	}

	private void setBoardParams(Board board) {
		Dictionary dict = new Dictionary();
		board.setDict(dict);
		// initialize 2d array of Squares
		Square[][] boardArr = new Square[board.getLength()][board.getLength()];
		for (int r = 0; r < board.getLength(); r++)
			for (int c = 0; c < board.getLength(); c++)
				boardArr[r][c] = new Square();
		board.setBoardArr(boardArr);
		board.setStatus(String.valueOf('A'));
		board.setGameId(game.getGameId());
		board.setMoveSeq(0);
	}

}
