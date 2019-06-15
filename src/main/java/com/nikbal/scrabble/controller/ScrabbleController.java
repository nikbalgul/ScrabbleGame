package com.nikbal.scrabble.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.nikbal.scrabble.model.BoardReportDTO;
import com.nikbal.scrabble.model.Dictionary;
import com.nikbal.scrabble.model.MoveWrapper;
import com.nikbal.scrabble.model.SequenceDTO;
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

	// {"moves":[{"name":"shail1","age":"2"},{"name":"shail2","age":"3"}]}

	@RequestMapping(value = "/play", method = RequestMethod.POST)
	private ResponseEntity<String> play(@RequestParam("boardId") Long boardId, @RequestBody MoveWrapper moves) {
		Board board = game.getBoardList().get(boardId.intValue());
		board.setMoveSeq(board.getMoveSeq() + 1);
		if (board.getStatus().equals(String.valueOf('P'))) {
			return ResponseEntity.ok().body("Yeni kelime ekleyemezsiniz! Board Status : P (Passive).");
		}
		List<Move> movesOnBoard = scrabbleService.getMoveListByBoardId(board.getBoardId());
		for (Move move : movesOnBoard) {
			board.getTilesOnBoard().addAll(move.getLetters());
		}
		for (Move move : moves.getMoves()) {
			if (game.isLegalMove(move, board) == null) {
				return ResponseEntity.ok().body("Geçersiz hamle! Lütfen bir daha deneyiniz!");
			}
			game.insertWord(board, move);
			sumScore += move.getSumScore();
			move = setMoveParams(move, board);
			scrabbleService.saveMove(move);
			Score score = setScoreParams(move);
			scrabbleService.saveScore(score);
		}
		scrabbleService.updateBoard(board);
		return ResponseEntity.ok().body("Hamleni yaptın! Puanın " + sumScore + "!");

	}

	@RequestMapping(value = "/getWords", method = RequestMethod.GET)
	private ResponseEntity<List<BoardReportDTO>> getWords(@RequestParam("boardId") Long boardId) {
		Board board = game.getBoardList().get(boardId.intValue());
		List<Move> moveList = scrabbleService.getMoveListByBoardId(board.getBoardId());
		List<BoardReportDTO> boardReportList = new ArrayList<>();
		for (Move move : moveList) {
			BoardReportDTO report = new BoardReportDTO(move.getSumScore(), move.getText());
			boardReportList.add(report);
		}
		if (!boardReportList.isEmpty()) {
			return ResponseEntity.ok().body(boardReportList);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/setStatus", method = RequestMethod.GET)
	private ResponseEntity<String> setStatus(@RequestParam("boardId") Long boardId,
			@RequestParam("status") String status) {
		Board board = game.getBoardList().get(boardId.intValue());
		if (status.equals(String.valueOf('A')) && board.getStatus().equals(String.valueOf('P'))) {
			return ResponseEntity.ok().body("Board aktif yapılamaz! Board Status : P (Passive)");
		}
		board.setStatus(String.valueOf('P'));
		scrabbleService.updateBoard(board);
		return ResponseEntity.ok().body("Board Status : P (Passive)");
	}

	@RequestMapping(value = "/getBoardContent", method = RequestMethod.GET)
	private ResponseEntity<List<SequenceDTO>> getBoardContent(@RequestParam("boardId") Long boardId,
			@RequestParam("sequence") Integer sequence) {
		Board board = game.getBoardList().get(boardId.intValue());
		List<Move> moveList = scrabbleService.getMoveListBySequence(board.getBoardId(), sequence);
		List<SequenceDTO> sequenceList = new ArrayList<>();

		for (Move move : moveList) {
			SequenceDTO dto = new SequenceDTO(move.getMoveSeq(), move.getText());
			sequenceList.add(dto);
		}
		if (!sequenceList.isEmpty()) {
			return ResponseEntity.ok().body(sequenceList);
		} else {
			return ResponseEntity.notFound().build();
		}
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
		board.setMoveSeq(0);
	}

	private Score setScoreParams(Move move) {
		Score score = new Score();
		score.setMoveId(move.getMoveId());
		score.setValue(move.getSumScore());
		return score;
	}

	private Move setMoveParams(Move fromMove, Board board) {
		Move move = new Move();
		move.setStartx(fromMove.getStartx());
		move.setStarty(fromMove.getStarty());
		move.setEndx(fromMove.getEndx());
		move.setEndy(fromMove.getEndy());
		move.setText(fromMove.getText());
		move.setSumScore(fromMove.getSumScore());
		move.setBoardId(board.getBoardId());
		move.setMoveSeq(board.getMoveSeq());
		return move;
	}

}
