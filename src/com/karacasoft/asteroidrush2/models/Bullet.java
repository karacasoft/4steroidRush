package com.karacasoft.asteroidrush2.models;

import com.karacasoft.asteroidrush2.utils.Animator;

/**
 * Bullet model.
 * @author Asteroid
 *
 */
public class Bullet {
	
	public float x;
	public float y;
	
	private float projectileSpeed=5;
	private float speedX = 0;
	
	
	private int projectileDamage=5;
	
	private boolean isDestroyed=false;
	
	public int stdAnimFrame = 0;
	public int fadeAnimFrame = 0;
	
	private Animator standardAnimation;
	private Animator fadeAnimation;
	
	/**
	 * Constructor
	 * @param x X Coordinate
	 * @param y Y Coordinate
	 */
	public Bullet(float x, float y)
	{
		this.x=x;
		this.y=y;
	}
	/**
	 * Destroy the bullet.
	 */
	public void destroy()
	{
		this.isDestroyed=true;
	}
	/**
	 * Get speed of the Bullet.
	 * @return Speed of the Bullet
	 */
	public float getProjectileSpeed() {
		return projectileSpeed;
	}
	/**
	 * Set speed of the Bullet
	 * @param projectileSpeed Speed of the Bullet
	 */
	public void setProjectileSpeed(float projectileSpeed) {
		this.projectileSpeed = projectileSpeed;
	}
	/**
	 * Get damage of the Bullet.
	 * @return Damage of the Bullet
	 */
	public int getProjectileDamage() {
		return projectileDamage;
	}
	/**
	 * Set damage of the Bullet
	 * @param projectileDamage Damage of the Bullet
	 */
	public void setProjectileDamage(int projectileDamage) {
		this.projectileDamage = projectileDamage;
	}
	/**
	 * @return the isDestroyed state
	 */
	public boolean isDestroyed() {
		return isDestroyed;
	}
	/**
	 * @param isDestroyed the isDestroyed state to set
	 */
	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}
	/**
	 * @return the fadeAnimation
	 */
	public Animator getFadeAnimation() {
		return fadeAnimation;
	}
	/**
	 * @param fadeAnimation the fadeAnimation to set
	 */
	public void setFadeAnimation(Animator fadeAnimation) {
		this.fadeAnimation = fadeAnimation;
	}
	/**
	 * @return the standardAnimation
	 */
	public Animator getStandardAnimation() {
		return standardAnimation;
	}
	/**
	 * @param standardAnimation the standardAnimation to set
	 */
	public void setStandardAnimation(Animator standardAnimation) {
		this.standardAnimation = standardAnimation;
	}
	public float getSpeedX() {
		return speedX;
	}
	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}
	
	
	
}
