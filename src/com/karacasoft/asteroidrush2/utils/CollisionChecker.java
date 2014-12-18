package com.karacasoft.asteroidrush2.utils;

import com.karacasoft.asteroidrush2.models.Asteroid;
import com.karacasoft.asteroidrush2.models.Bullet;
import com.karacasoft.asteroidrush2.models.PowerUp;
import com.karacasoft.asteroidrush2.models.Ship;

/**
 * Checks for collisions.
 * @author Triforce
 *
 */
public abstract class CollisionChecker {
	//TODO this static should be modified with dpi multiplier
	public static final int COLLISION_MERCY=5;
	public static final int COLLISION_MERCY_FOR_SHIP=12;
	
	private float screen_dpi_multiplier=1;
	
	/**
	 * Constructor
	 */
	public CollisionChecker(float screen_dpi_multiplier)
	{
		this.screen_dpi_multiplier=screen_dpi_multiplier;
	}
	/**
	 * Override this method to make things happen on Asteroid-Ship collision.
	 * @param asteroid Asteroid that collides with Ship
	 * @param ship Ship that collides with Asteroid
	 */
	public abstract void onAsteroidShipCollision(Asteroid asteroid, Ship ship);
	/**
	 * Override this method to make things happen on Asteroid-Bullet collision.
	 * @param asteroid Asteroid that collides with Bullet.
	 * @param bullet Bullet that collides with Asteroid
	 */
	public abstract void onAsteroidBulletCollision(Asteroid asteroid, Bullet bullet);
	/**
	 * Override this method to make things happen on Ship-PowerUp collision.
	 * @param ship Ship that collides with Power-Up
	 * @param powerUp Power-Up that collides with Ship
	 */
	public abstract void onShipPowerUpCollision(Ship ship, PowerUp powerUp);
	/**
	 * Checks if given objects collide or not. Calls onAsteroidBulletCollision()
	 * method if they collide
	 * @param asteroid Asteroid
	 * @param bullet Bullet
	 * @param asteroid_width Asteroid width
	 * @param asteroid_height Asteroid height
	 * @param bullet_width Bullet width
	 * @param bullet_height Bullet height
	 * @return true if they collide, false otherwise.
	 */
	public boolean checkAsteroidBulletCollision(Asteroid asteroid, Bullet bullet,
			int asteroid_width, int asteroid_height,
			int bullet_width, int bullet_height)
	{
		if(((bullet.x>asteroid.x-bullet_width+COLLISION_MERCY*screen_dpi_multiplier)
				&&
			(bullet.x<asteroid.x+asteroid_width-COLLISION_MERCY*screen_dpi_multiplier))
					&&
			((bullet.y>asteroid.y-bullet_height+COLLISION_MERCY*screen_dpi_multiplier)
				&&
			(bullet.y<asteroid.y+asteroid_height-COLLISION_MERCY*screen_dpi_multiplier)))
		{
			onAsteroidBulletCollision(asteroid, bullet);
			return true;
		}else{
			return false;
		}
	}
	/**
	 * Checks if given objects collide or not. Calls onAsteroidShipCollision()
	 * method if they collide
	 * @param asteroid Asteroid
	 * @param ship Ship
	 * @param asteroid_width Asteroid width
	 * @param asteroid_height Asteroid height
	 * @param ship_width Ship width
	 * @param ship_height Ship height
	 * @return true if they collide, false otherwise.
	 */
	public boolean checkAsteroidShipCollision(Asteroid asteroid, Ship ship,
			int asteroid_width, int asteroid_height,
			int ship_width, int ship_height)
	{
		if(((ship.x>asteroid.x-ship_width+COLLISION_MERCY_FOR_SHIP*screen_dpi_multiplier)
				&&
			(ship.x<asteroid.x+asteroid_width-COLLISION_MERCY_FOR_SHIP*screen_dpi_multiplier))
					&&
			((ship.y>asteroid.y-ship_height+COLLISION_MERCY_FOR_SHIP*screen_dpi_multiplier)
				&&
			(ship.y<asteroid.y+asteroid_height-COLLISION_MERCY_FOR_SHIP*screen_dpi_multiplier))
				&&
			!asteroid.isDestroyed())
		{
			onAsteroidShipCollision(asteroid, ship);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Checks if given objects collide or not. Calls 
	 * {@link #onShipPowerUpCollision(Ship, PowerUp)} on collision.
	 * @param ship
	 * @param powerUp
	 * @param powerUp_width
	 * @param powerUp_height
	 * @param ship_width
	 * @param ship_height
	 * @return
	 */
	public boolean checkShipPowerUpCollision(Ship ship, PowerUp powerUp,
			int powerUp_width, int powerUp_height,
			int ship_width, int ship_height)
	{
		if(((ship.x>powerUp.x-ship_width+COLLISION_MERCY*screen_dpi_multiplier)
				&&
			(ship.x<powerUp.x+powerUp_width*screen_dpi_multiplier-COLLISION_MERCY*screen_dpi_multiplier))
					&&
			((ship.y>powerUp.y-ship_height+COLLISION_MERCY*screen_dpi_multiplier)
				&&
			(ship.y<powerUp.y+powerUp_height*screen_dpi_multiplier-COLLISION_MERCY*screen_dpi_multiplier)))
		{
			onShipPowerUpCollision(ship, powerUp);
			return true;
		}else{
			return false;
		}
	}
	
}
