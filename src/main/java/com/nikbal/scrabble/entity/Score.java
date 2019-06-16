package com.nikbal.scrabble.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * 
 *
 *
 */

@Entity
@Table(name = "SCORE")
public class Score {
	@Id
	@SequenceGenerator(name = "SCORE_SEQUENCE", sequenceName = "SCORE_SEQUENCE_ID", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SCORE_SEQUENCE")
	private Long scoreId;
	@Column(name = "MOVEID")
	private Long moveId;
	@Column(name = "VALUE")
	private int value;

	public Long getScoreId() {
		return scoreId;
	}

	public void setScoreId(Long scoreId) {
		this.scoreId = scoreId;
	}

	public Long getMoveId() {
		return moveId;
	}

	public void setMoveId(Long moveId) {
		this.moveId = moveId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String toString() {
		return "SCORE(" + " MOVEID: " + moveId + " VALUE:" + value + " )";
	}
}
