package com.nikbal.scrabble.dto;

/*
 * 
 * 
 * 
 * 
 * */

public class SequenceDTO {

	private int sequence;
	private String wordText;

	public SequenceDTO(int sequence, String wordText) {
		this.sequence = sequence;
		this.wordText = wordText;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getWordText() {
		return wordText;
	}

	public void setWordText(String wordText) {
		this.wordText = wordText;
	}

}
