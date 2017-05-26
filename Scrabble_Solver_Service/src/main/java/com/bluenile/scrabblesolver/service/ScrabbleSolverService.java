package com.bluenile.scrabblesolver.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

@Service
public class ScrabbleSolverService {
	
	/*
	 * Stores the dictionary of words with each scrabled word stored together
	 */
	Map<String, List<String>> wordmap = new HashMap<String, List<String>>();
	/*
	 * Static map to generate the score of a word. Rules are as follows:
	 * The point values are:
     * 	Points | Letters
    	-------+-----------------------------
           1   | A, E, I, L, N, O, R, S, T, U
           2   | D, G
           3   | B, C, M, P
           4   | F, H, V, W, Y
           5   | K
           8   | J, X
          10   | Q, Z
	 */
	static int score[] = {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
	
	/*
	 * Constructor initializes the wordmap by reading dictionary from "http://www-01.sil.org/linguistics/wordlists/english/wordlist/wordsEn.txt"
	 */
	ScrabbleSolverService() {
		// initialize the map
		URL url;
		try {
			url = new URL("http://www-01.sil.org/linguistics/wordlists/english/wordlist/wordsEn.txt");
			Scanner inputScanner = new Scanner(url.openStream());
			while (inputScanner.hasNext()) {
				String word = inputScanner.next();
				char[] wordChars = word.toCharArray();
		        Arrays.sort(wordChars);
		        String sortedWord = new String(wordChars); 
		        if(wordmap.containsKey(sortedWord)){
		        	wordmap.get(sortedWord).add(word);
		        } else {
		        	List <String> list = new ArrayList<String>();
		        	list.add(word);
		        	wordmap.put(sortedWord, list);
		        }
			   }
			inputScanner.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	/*
	 * Get the list of scrable words sorted by scrabble score, from highest to lowest scoring
	 */
	public List<String> getScrableWords(String word) {
		List<String> list = new ArrayList<String>();
		char[] wordChars = word.toCharArray();
        Arrays.sort(wordChars);
        String sortedWord = new String(wordChars); 
		for(Map.Entry<String, List<String>> entry : wordmap.entrySet()){
			if(containsAllChars(sortedWord.toLowerCase(), entry.getKey().toLowerCase())){
				list.addAll(entry.getValue());
			} 
		}
		List<String> reverselist = sortByScrableScore(list);
		return reverselist;
	}

	/*
	 * Sorted the list of words by scrabble score, from highest to lowest scoring
	 */
	private List<String> sortByScrableScore(List<String> list) {
		Map<Integer,PriorityQueue<String>> map = new TreeMap<Integer, PriorityQueue<String>>(Collections.reverseOrder());
		List<String> reverselist = new ArrayList<String>();
		for(String word : list){
			int sum_score = 0;
			for(char c : word.toCharArray()){
				sum_score+=score[c-'a'];
			}
			if(map.containsKey(sum_score)){
				PriorityQueue<String> queue = map.get(sum_score);
				queue.add(word);
				map.put(sum_score, queue);
			} else {
				PriorityQueue<String> queue = new PriorityQueue<String>();
				queue.add(word);
				map.put(sum_score,queue);
			}
		}
		for(PriorityQueue<String> p : map.values()){
			while(!p.isEmpty()){
				reverselist.add(p.poll());
			}
		}
		return reverselist;
	}

	/*
	 * Check if the string container contains all letters from the string containee
	 */
	private boolean containsAllChars(String container, String containee) {
		int[] map = new int[256]; // taking in account "'"
		for(char c : container.toCharArray()){
			map[c]++;
		}
		for(char c : containee.toCharArray()){
			map[c]--;
			if(map[c]<0){
				return false;
			}
		}
		return true;
	}
}
