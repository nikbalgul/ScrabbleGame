package com.nikbal.scrabble.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nikbal.scrabble.dto.BoardReportDTO;
import com.nikbal.scrabble.dto.SequenceDTO;
import com.nikbal.scrabble.entity.Board;
import com.nikbal.scrabble.entity.Move;
import com.nikbal.scrabble.entity.Score;
import com.nikbal.scrabble.model.Game;
import com.nikbal.scrabble.model.MoveModel;
import com.nikbal.scrabble.model.Tile;
import com.nikbal.scrabble.service.BoardService;
import com.nikbal.scrabble.service.MoveService;
import com.nikbal.scrabble.service.ScoreService;
import com.nikbal.scrabble.wrapper.MoveWrapper;

@RestController
@RequestMapping(value = "/move")
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

	private static final Logger logger = Logger.getLogger(BoardController.class);
	
	@PostMapping(value = "/play", consumes={"application/json"})
    @ResponseBody
	private ResponseEntity<List<Move>> play(@RequestBody MoveWrapper moveWrapper) {
		Board board = game.getBoardFromGame(moveWrapper.getBoardId());
		if (board == null || board.getStatus().equals(String.valueOf('P'))) {
			logger.info("Oynamak istediğiniz board pasiftir!");
			return ResponseEntity.notFound().build();
		}
		board.setMoveSeq(board.getMoveSeq() + 1);
		List<Move> moveList = new ArrayList<>();
		for (MoveModel moveModel : moveWrapper.getMoves()) {
			Move move = convertToMove(moveModel, board);
			Move legalMove = game.isLegalMove(move, board);
			if (legalMove == null) {
				return ResponseEntity.notFound().build();
			}
			move.setLetters(legalMove.getLetters());
			game.insertWord(board, move);
			board.setSumScore(board.getSumScore() + move.getSumScore());
			moveService.saveMove(move);
			Score score = setScoreParams(move);
			scoreService.saveScore(score);
			setBoardTiles(board, move);
			moveList.add(move);
		}
		boardService.updateBoard(board);
		return ResponseEntity.ok().body(moveList);

	}

	private void setBoardTiles(Board board, Move move) {
		if (CollectionUtils.isEmpty(board.getTilesOnBoard())){
			board.setTilesOnBoard(new HashSet<Tile>(move.getLetters()));
		} else {
			board.getTilesOnBoard().addAll(move.getLetters());
		}
	}

	private Move convertToMove(MoveModel moveModel, Board board) {
		Move move = new Move();
		move.setText(moveModel.getText());
		move.setStartx(moveModel.getStartx());
		move.setStarty(moveModel.getStarty());
		move.setEndx(moveModel.getEndx());
		move.setEndy(moveModel.getEndy());
		move.setBoardId(board.getBoardId());
		move.setMoveNum(move.getMoveNum() + 1);
		move.setMoveSeq(board.getMoveSeq());
		return move;
	}

	@RequestMapping(value = "/getWords", method = RequestMethod.GET)
	private ResponseEntity<List<BoardReportDTO>> getWords(@RequestParam("boardId") Long boardId) {
		Board board = game.getBoardFromGame(boardId);
		if (board.getStatus().equals(String.valueOf('P'))) {
			logger.info("Board Status : P (Passive)");
			return ResponseEntity.notFound().build();
		}
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
		Board board = game.getBoardFromGame(boardId);
		if (board == null || board.getStatus().equals(String.valueOf('P'))) {
			logger.info("Board Status : P (Passive)");
			return ResponseEntity.notFound().build();
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

}
