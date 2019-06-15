package com.nikbal.scrabble.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nikbal.scrabble.dto.BoardReportDTO;
import com.nikbal.scrabble.dto.SequenceDTO;
import com.nikbal.scrabble.entity.Board;
import com.nikbal.scrabble.entity.Game;
import com.nikbal.scrabble.entity.Move;
import com.nikbal.scrabble.entity.Score;
import com.nikbal.scrabble.service.BoardService;
import com.nikbal.scrabble.service.MoveService;
import com.nikbal.scrabble.service.ScoreService;
import com.nikbal.scrabble.wrapper.MoveWrapper;

@RestController
@RequestMapping("/move")
public class MoveController {
	@Autowired
	private MoveService moveService;
	@Autowired
	private BoardService boardService;
	@Autowired
	private ScoreService scoreService;
	int sumScore = 0;
	@Autowired
	Game game;

	// {"moves":[{"name":"shail1","age":"2"},{"name":"shail2","age":"3"}]}

	@RequestMapping(value = "/play", method = RequestMethod.POST)
	private ResponseEntity<String> play(@RequestParam("boardId") Long boardId, @RequestBody MoveWrapper moves) {
		Board board = game.getBoardList().get(boardId.intValue());
		if (board.getStatus().equals(String.valueOf('P'))) {
			return ResponseEntity.ok().body("Yeni kelime ekleyemezsiniz! Board Status : P (Passive).");
		}
		board.setMoveSeq(board.getMoveSeq() + 1);
		List<Move> movesOnBoard = moveService.getMoveListByBoardId(board.getBoardId());
		for (Move move : movesOnBoard) {
			board.getTilesOnBoard().addAll(move.getLetters());
		}
		for (Move move : moves.getMoves()) {
			if (game.isLegalMove(move, board) == null) {
				return ResponseEntity.ok().body("Geçersiz hamle!");
			}
			game.insertWord(board, move);
			sumScore += move.getSumScore();
			move = setMoveParams(move, board);
			moveService.saveMove(move);
			Score score = setScoreParams(move);
			scoreService.saveScore(score);
		}
		boardService.updateBoard(board);
		return ResponseEntity.ok().body("Hamleni yaptın! Puanın " + sumScore + "!");

	}

	@RequestMapping(value = "/getWords", method = RequestMethod.GET)
	private ResponseEntity<List<BoardReportDTO>> getWords(@RequestParam("boardId") Long boardId) {
		Board board = game.getBoardList().get(boardId.intValue());
		List<Move> moveList = moveService.getMoveListByBoardId(board.getBoardId());
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

	@RequestMapping(value = "/getBoardContent", method = RequestMethod.GET)
	private ResponseEntity<List<SequenceDTO>> getBoardContent(@RequestParam("boardId") Long boardId,
			@RequestParam("sequence") Integer sequence) {
		Board board = game.getBoardList().get(boardId.intValue());
		if (board.getStatus().equals(String.valueOf('P'))) {
			ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Pasif halde olan board görüntülenmez! Board Status : P (Passive)");
		}
		List<Move> moveList = moveService.getMoveListBySequence(board.getBoardId(), sequence);
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
