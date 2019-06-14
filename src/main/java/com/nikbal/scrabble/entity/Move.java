package com.nikbal.scrabble.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.nikbal.scrabble.model.Tile;

/**
 * 
 * 
 *
 *
 */

@Entity
@Table(name = "MOVE")
public class Move {
	@Id
	@SequenceGenerator(name = "MOVE_SEQUENCE", sequenceName = "MOVE_SEQUENCE_ID", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MOVE_SEQUENCE")
	private Long moveId;
	@Column(name = "BOARDID")
	private Long boardId;
	@Column(name = "START_X")
	private int startx;
	@Column(name = "START_Y")
	private int starty;
	@Column(name = "END_X")
	private int endx;
	@Column(name = "END_Y")
	private int endy;
	@Column(name = "MOVE_SEQ")
	private Integer moveSeq;
	@Column(name = "TEXT")
	private String text;
	@Column(name = "MOVE_NUM")
	private int moveNum;
	@Column(name = "SUM_SCORE")
	private int sumScore;
	@Transient
	private List<Tile> letters = new ArrayList<>();

	public Long getMoveId() {
		return moveId;
	}

	public void setMoveId(Long moveId) {
		this.moveId = moveId;
	}

	public Long getBoardId() {
		return boardId;
	}

	public void setBoardId(Long boardId) {
		this.boardId = boardId;
	}

	public int getStartx() {
		return startx;
	}

	public void setStartx(int startx) {
		this.startx = startx;
	}

	public int getStarty() {
		return starty;
	}

	public void setStarty(int starty) {
		this.starty = starty;
	}

	public int getEndx() {
		return endx;
	}

	public void setEndx(int endx) {
		this.endx = endx;
	}

	public int getEndy() {
		return endy;
	}

	public void setEndy(int endy) {
		this.endy = endy;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getMoveNum() {
		return moveNum;
	}

	public void setMoveNum(int moveNum) {
		this.moveNum = moveNum;
	}

	public int getSumScore() {
		return sumScore;
	}

	public void setSumScore(int sumScore) {
		this.sumScore = sumScore;
	}

	public List<Tile> getLetters() {
		return letters;
	}

	public void setLetters(List<Tile> letters) {
		this.letters = letters;
	}

	public Integer getMoveSeq() {
		return moveSeq;
	}

	public void setMoveSeq(Integer moveSeq) {
		this.moveSeq = moveSeq;
	}

}
