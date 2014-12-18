package com.karacasoft.asteroidrush2.models;

public class Particle {
	private float x;
	private float y;
	private int color;
	private float speedX;
	private float speedY;
	private int dieOutTimer=25;
	
	public Particle(float x,float y,float speedX,float speedY,int color)
	{
		this.setX(x);
		this.setY(y);
		this.setSpeedX(speedX);
		this.setSpeedY(speedY);
		this.setColor(color);
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return the color
	 */
	public int getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * @return the speedX
	 */
	public float getSpeedX() {
		return speedX;
	}

	/**
	 * @param speedX the speedX to set
	 */
	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	/**
	 * @return the speedY
	 */
	public float getSpeedY() {
		return speedY;
	}

	/**
	 * @param speedY the speedY to set
	 */
	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}

	/**
	 * @return the dieOutTimer
	 */
	public int getDieOutTimer() {
		return dieOutTimer;
	}

	/**
	 * @param dieOutTimer the dieOutTimer to set
	 */
	public void setDieOutTimer(int dieOutTimer) {
		this.dieOutTimer = dieOutTimer;
	}
}
