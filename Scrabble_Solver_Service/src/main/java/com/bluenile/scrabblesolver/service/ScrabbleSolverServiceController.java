package com.bluenile.scrabblesolver.service;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScrabbleSolverServiceController {

	ScrabbleSolverService scrabbleSolverService;

	ScrabbleSolverServiceController() {
		scrabbleSolverService = new ScrabbleSolverService();
	}

	@RequestMapping("/words/{word}")
	public List<String> getScrableWords(@PathVariable String word) {
		return scrabbleSolverService.getScrableWords(word);
	}

}