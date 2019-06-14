package com.nikbal.scrabble.model;

/*
 * 
 * 
 * 
 * 
 * */

public class BoardReportDTO {

	private int wordScore;
	private String wordText;

	public BoardReportDTO(int sumScore, String text) {
		this.wordScore = sumScore;
		this.wordText = text;
	}

	public int getWordScore() {
		return wordScore;
	}

	public void setWordScore(int wordScore) {
		this.wordScore = wordScore;
	}

	public String getWordText() {
		return wordText;
	}

	public void setWordText(String wordText) {
		this.wordText = wordText;
	}

}
