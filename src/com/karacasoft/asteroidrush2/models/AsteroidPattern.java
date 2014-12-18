package com.karacasoft.asteroidrush2.models;

import java.util.ArrayList;
/**
 * It'll be used to give standart Asteroid Patterns to
 * break randomness of the game a little bit.
 * @author Triforce
 *
 */
public class AsteroidPattern {
	
	private ArrayList<Asteroid> pattern;
	
	public AsteroidPattern() {
		
	}
	/**
	 * Get Asteroid Pattern
	 * @return ArrayList of Asteroids
	 */
	public ArrayList<Asteroid> getPattern() {
		return pattern;
	}
	/**
	 * Set Asteroid Pattern
	 * @param pattern ArrayList of Asteroids
	 */
	public void setPattern(ArrayList<Asteroid> pattern) {
		this.pattern = pattern;
	}
	
}
