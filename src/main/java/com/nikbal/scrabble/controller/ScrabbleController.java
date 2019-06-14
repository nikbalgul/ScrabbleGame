package com.nikbal.scrabble.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nikbal.scrabble.entity.Board;
import com.nikbal.scrabble.entity.Game;
import com.nikbal.scrabble.entity.Move;
import com.nikbal.scrabble.entity.Score;
import com.nikbal.scrabble.model.Dictionary;
import com.nikbal.scrabble.model.MoveWrapper;
import com.nikbal.scrabble.model.Square;
import com.nikbal.scrabble.service.ScrabbleService;

@RestController
@RequestMapping("/service")
public class ScrabbleController {
	@Autowired
	private ScrabbleService scrabbleService;
	static int boardLength = 15;
	int sumScore = 0;
	Game game;
	private Dictionary dict;
	private Square[][] boardArr;

	@RequestMapping(value = "/startGame", method = RequestMethod.GET)
	public ResponseEntity<String> startGame() {
		game = scrabbleService.saveOrUpdateGame(game);
		game.initTileBag();
		return ResponseEntity.ok().body("Game Started!");
	}

	@RequestMapping(value = "/createBoard", method = RequestMethod.GET)
	public ResponseEntity<String> createBoard() {
		Board board = new Board();
		setBoardParams(board);
		scrabbleService.saveBoard(board);
		game.getBoardList().add(board);
		return ResponseEntity.ok().body("Board ID:" + board.getBoardId());
	}

	private void setBoardParams(Board board) {
		dict = new Dictionary();
		board.setDict(dict);
		// initialize 2d array of Squares
		boardArr = new Square[boardLength][boardLength];
		for (int r = 0; r < boardLength; r++)
			for (int c = 0; c < boardLength; c++)
				boardArr[r][c] = new Square();
		board.setBoardArr(boardArr);
		board.setStatus(String.valueOf('A'));
		board.setGameId(game.getGameId());
	}

	// {"moves":[{"name":"shail1","age":"2"},{"name":"shail2","age":"3"}]}

	@RequestMapping(value = "/play", method = RequestMethod.POST)
	private ResponseEntity<String> play(@RequestParam("boardId") Long boardId, @RequestBody MoveWrapper moves) {
		Board board = game.getBoardList().get(boardId.intValue());
		for (int i = 0; i < moves.getMoves().size(); i++) {
			game.insertWord(board, moves.getMoves().get(i));
			sumScore += moves.getMoves().get(i).getSumScore();
			Move move = setMoveParams(moves.getMoves().get(i));
			move.setBoardId(boardId);
			scrabbleService.saveMove(move);
			Score score = setScoreParams(move);
			scrabbleService.saveScore(score);
		}

		return ResponseEntity.ok().body("Hamleni yaptın! Puanın " + sumScore + "!");

	}

	private Score setScoreParams(Move move) {
		Score score = new Score();
		score.setMoveId(move.getMoveId());
		score.setValue(move.getSumScore());
		return score;
	}

	private Move setMoveParams(Move fromMove) {
		Move move = new Move();
		move.setStartx(fromMove.getStartx());
		move.setStarty(fromMove.getStarty());
		move.setEndx(fromMove.getEndx());
		move.setEndy(fromMove.getEndy());
		move.setText(fromMove.getText());
		move.setSumScore(fromMove.getSumScore());
		return move;
	}

}
