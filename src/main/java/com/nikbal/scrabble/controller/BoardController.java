package com.nikbal.scrabble.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nikbal.scrabble.entity.Board;
import com.nikbal.scrabble.model.Game;
import com.nikbal.scrabble.model.Square;
import com.nikbal.scrabble.service.BoardService;

@RestController
@RequestMapping(value= "/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	@Autowired
	Game game;

	private static final Logger logger = Logger.getLogger(BoardController.class);
	
	@RequestMapping(value = "/createBoard", method = RequestMethod.GET)
	public ResponseEntity<Long> createBoard() {
		Board board = new Board();
		setBoardParams(board);
		boardService.saveBoard(board);
		game.getBoardList().add(board);
		logger.info("Board ID:" + board.getBoardId() + " is created.");
		return ResponseEntity.ok().body(board.getBoardId());
	}

	@RequestMapping(value = "/setStatus", method = RequestMethod.GET)
	private ResponseEntity<String> setStatus(@RequestParam("boardId") Long boardId,
			@RequestParam("status") String status) {
		Board board = game.getBoardFromGame(boardId);
		if (status.equals(String.valueOf('A')) && board.getStatus().equals(String.valueOf('P'))) {
			logger.info("Board aktif yapılamaz! Board Status : P (Passive)");
			return ResponseEntity.ok().body("Board pasiftir!");
		}
		if (status.equals(String.valueOf('A'))){
			board.setStatus(String.valueOf('A'));
		} else {
			board.setStatus(String.valueOf('P'));
		}
		logger.info("Board ID:" + board.getBoardId() + " was be passive.");
		boardService.updateBoard(board);
		return ResponseEntity.ok().body(board.getStatus());
	}

	private void setBoardParams(Board board) {
		board.setDict(game.getDictionary());
		// initialize 2d array of Squares
		Square[][] boardArr = new Square[board.getLength()][board.getLength()];
		for (int r = 0; r < board.getLength(); r++)
			for (int c = 0; c < board.getLength(); c++)
				boardArr[r][c] = new Square();
		board.setBoardArr(boardArr);
		board.setStatus(String.valueOf('A'));
		board.setMoveSeq(0);
	}

}
