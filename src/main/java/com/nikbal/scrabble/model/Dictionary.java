package com.nikbal.scrabble.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Stores the file for the dictionary used by the scrabble game
 * 
 *
 *
 */
public class Dictionary {
	private File file;
	private List<List<String>> list = new ArrayList<>();
	private List<String> allChecked = new ArrayList<>();
	private List<String> legalChecked = new ArrayList<>();

	public List<List<String>> getList() {
		return list;
	}

	public void setList(List<List<String>> list) {
		this.list = list;
	}

	/**
	 * initializes the file path for the dictionary
	 *
	 */
	public Dictionary() {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource("scrabble_turkish_dictionary.txt");
		if (resource == null) {
			throw new IllegalArgumentException("file is not found!");
		} else {
			file = new File(resource.getFile());
		}
		saveWordsAlphabetically();
	}

	/**
	 * saves the words in the dictionary to arraylists, separated by first two
	 * letters in order to save time searching
	 */
	private void saveWordsAlphabetically() {
		try (Scanner scanner = new Scanner(file)){
			// read file line by line, checking if word is in the file
			String firstTwoLetters = null;
			// scrabble_turkish_dictionary.txt has approx 270000 words, divided by first two
			// letters (26*26) is average of 400-600 words per first two chars.
			// This saves time and prevents arryalist from resizing too much
			List<String> currentLetterList = new ArrayList<>(400);
			while (scanner.hasNextLine()) {
				String word = scanner.nextLine().toLowerCase();
				if (word.length() >= 2) {
					if (firstTwoLetters == null)
						firstTwoLetters = word.substring(0, 2);

					if (firstTwoLetters.equals(word.substring(0, 2)))
						currentLetterList.add(word);
					else {
						list.add(currentLetterList);
						firstTwoLetters = word.substring(0, 2);
						currentLetterList = new ArrayList<>();
						currentLetterList.add(word);
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file);
		}
		for (int i = list.size() - 1; i >= 0; i--)
			if (list.get(i).isEmpty())
				list.remove(i);
	}

	/**
	 * checks if the given word is a legal scrabble word
	 * 
	 * @param word word to check
	 * @return true or false
	 */
	public boolean isWord(String word) {
		if (legalChecked.contains(word))
			return true;
		if (allChecked.contains(word) && !legalChecked.contains(word))
			return false;
		if (inDict(word)) {
			legalChecked.add(word);
			allChecked.add(word);
			return true;
		}
		allChecked.add(word);
		return false;
	}

	/**
	 * checks if the given word is in the supplied dictionary
	 * 
	 * @param word word to check
	 * @return true or false
	 */
	private boolean inDict(String word) {
		if (word.length() < 2)
			return false;
		String w = word.toLowerCase();
		char firstLetter = w.charAt(0);
		char secondLetter = w.charAt(1);
		for (int i = 0; i < this.list.size(); i++)
			if (this.list.get(i).get(0).charAt(0) == firstLetter && this.list.get(i).get(0).charAt(1) == secondLetter) {
				for (int j = 0; j < this.list.get(i).size(); j++) {
					if (this.list.get(i).get(j).equalsIgnoreCase(w))
						return true;
				}
				return false;
			}
		return false;
	}

}
