package com.karacasoft.asteroidrush2.models;

public class HighScore {

	private String user;
	private int score;
	private int distance;
	private int combined;
	
	public HighScore() {
		
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return the distance
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getCombined() {
		return combined;
	}

	public void setCombined(int combined) {
		this.combined = combined;
	}

}
