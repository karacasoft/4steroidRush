package com.karacasoft.asteroidrush2.models;

import com.karacasoft.asteroidrush2.utils.Animator;

/**
 * Asteroid property model.
 * @author Triforce
 *
 */
public class Asteroid {
	
	public static final int ASTEROID_TYPE_1=0;
	public static final int ASTEROID_TYPE_2=1;
	public static final int ASTEROID_TYPE_3=2;
	
	public float x;
	public float y;
	public int type;
	public float speed;
	public float rotation=0;
	public int rotation_speed; //0 if not rotating...
	public int asteroid_health=30;
	
	//private Matrix matrix;
	
	private boolean destroyed=false;
	private int destroyTimer=8;
	
	private Animator standardAnimation;
	
	public Asteroid() {
		
	}
	/**
	 * Destroys Asteroid. Starts the destroy animation for it.
	 *  Closes collision listeners for this Asteroid too.
	 */
	public void destroy()
	{
		destroyed=true;
	}
	
	public boolean isDestroyed()
	{
		return destroyed;
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
	/**
	 * @return the destroyTimer
	 */
	public int getDestroyTimer() {
		return destroyTimer;
	}
	/**
	 * @param destroyTimer the destroyTimer to set
	 */
	public void setDestroyTimer(int destroyTimer) {
		this.destroyTimer = destroyTimer;
	}
	
	
}
