package com.karacasoft.asteroidrush2.models;

public class UserStatistics {

	private String user;
	private int scoreSoFar;
	private int distanceSoFar;
	private int asteroidsDestroyed;
	private int bulletsFired;
	private int bulletsHit;
	private int bulletsMissed;
	private int deathCounter;
	
	public UserStatistics() {
		
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
	 * @return the scoreSoFar
	 */
	public int getScoreSoFar() {
		return scoreSoFar;
	}

	/**
	 * @param scoreSoFar the scoreSoFar to set
	 */
	public void setScoreSoFar(int scoreSoFar) {
		this.scoreSoFar = scoreSoFar;
	}

	/**
	 * @return the distanceSoFar
	 */
	public int getDistanceSoFar() {
		return distanceSoFar;
	}

	/**
	 * @param distanceSoFar the distanceSoFar to set
	 */
	public void setDistanceSoFar(int distanceSoFar) {
		this.distanceSoFar = distanceSoFar;
	}

	/**
	 * @return the asteroidsDestroyed
	 */
	public int getAsteroidsDestroyed() {
		return asteroidsDestroyed;
	}

	/**
	 * @param asteroidsDestroyed the asteroidsDestroyed to set
	 */
	public void setAsteroidsDestroyed(int asteroidsDestroyed) {
		this.asteroidsDestroyed = asteroidsDestroyed;
	}

	/**
	 * @return the bulletsFired
	 */
	public int getBulletsFired() {
		return bulletsFired;
	}

	/**
	 * @param bulletsFired the bulletsFired to set
	 */
	public void setBulletsFired(int bulletsFired) {
		this.bulletsFired = bulletsFired;
	}

	/**
	 * @return the bulletsHit
	 */
	public int getBulletsHit() {
		return bulletsHit;
	}

	/**
	 * @param bulletsHit the bulletsHit to set
	 */
	public void setBulletsHit(int bulletsHit) {
		this.bulletsHit = bulletsHit;
	}

	/**
	 * @return the bulletsMissed
	 */
	public int getBulletsMissed() {
		return bulletsMissed;
	}

	/**
	 * @param bulletsMissed the bulletsMissed to set
	 */
	public void setBulletsMissed(int bulletsMissed) {
		this.bulletsMissed = bulletsMissed;
	}

	/**
	 * @return the deathCounter
	 */
	public int getDeathCounter() {
		return deathCounter;
	}

	/**
	 * @param deathCounter the deathCounter to set
	 */
	public void setDeathCounter(int deathCounter) {
		this.deathCounter = deathCounter;
	}

}
