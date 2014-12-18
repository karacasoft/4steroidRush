package com.karacasoft.asteroidrush2.utils;

import java.util.ArrayList;

import com.karacasoft.asteroidrush2.models.Bullet;
import com.karacasoft.asteroidrush2.models.Ship;
import com.karacasoft.asteroidrush2.models.UserStatistics;
import com.karacasoft.asteroidrush2.views.GameView.AsteroidRushThread.AsteroidRushAudioManager;
/**
 * Rudder of the Ship :P
 * 
 *                    TR    EN
 * (Google Translate:Dumen=rudder)
 * @author Triforce
 *
 */
public class ShipController {
	
	public static final int STANDARD_BULLET_SPEED=10;
	
	public static final int STANDARD_BULLET_DAMAGE=5;
	
	private boolean fireLockOn=false;
	private int autoFireTimer=0;
	public int fireCooldown=0;
	
	private int bulletDamageModifier=0;
	
	private ArrayList<Bullet> bullets;
	
	private Animator standardAnimation;
	private Animator fadeAnimation;
	
	private AsteroidRushAudioManager audio_manager;
	
	private Ship ship;
	/**
	 * Constructor
	 */
	public ShipController() {
		ship=new Ship();
	}
	/**
	 * Constructor with a set of bullets.
	 * @param bullets Bullet List
	 */
	public ShipController(ArrayList<Bullet> bullets)
	{
		ship=new Ship();
		this.bullets=bullets;
	}
	/**
	 * Change position of the ship.
	 * @param x X Coordinate
	 * @param y Y Coordinate
	 * @param screen_height Screen height in pixels.
	 */
	public void changePosition(float x, float y, int screen_height)
	{
		ship.x=x;
		ship.y=y;
		
		float fingerpositionpercentage=100-((y/screen_height)*100);
		
		ship.speed=ship.baseSpeed+(Math.min(fingerpositionpercentage + 20,50)/18);
		
	}
	/**
	 * Fire the projectile.
	 * @param bullet_width Bullet width on the screen in pixels.
	 * @param ship_width Ship width on the screen in pixels.
	 * @param statistics 
	 */
	public void fire(int bullet_width, int ship_width, UserStatistics statistics)
	{
		
		if (fireCooldown<0) {
			audio_manager.playFireSound();
			ArrayList<Bullet> collection = new ArrayList<Bullet>();
			final Bullet b1 = new Bullet(0, 0);
			final Bullet b2 = new Bullet(0, 0);
			final Bullet b3 = new Bullet(0, 0);
			final Bullet b4 = new Bullet(0, 0);
			final Bullet b5 = new Bullet(0, 0);
			final Bullet b6 = new Bullet(0, 0);
			final Bullet b7 = new Bullet(0, 0);
			final Bullet b8 = new Bullet(0, 0);
			final Bullet b9 = new Bullet(0, 0);
			final Bullet b10 = new Bullet(0, 0);
			
			switch (ship.getProjectileType()) {
			case Ship.PROJECTILE_TYPE_NORMAL_SINGLE:

				b1.x = ship.x + (ship_width / 2) - (bullet_width / 2);
				b1.y = ship.y;
				b1.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b1.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b1.setStandardAnimation(standardAnimation);
				b1.setFadeAnimation(fadeAnimation);

				collection.add(b1);

				statistics.setBulletsFired(statistics.getBulletsFired() + 1);

				break;
			case Ship.PROJECTILE_TYPE_NORMAL_DOUBLE:
				b1.x = ship.x;
				b1.y = ship.y;
				b1.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b1.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b1.setStandardAnimation(standardAnimation);
				b1.setFadeAnimation(fadeAnimation);

				collection.add(b1);
				b2.x = ship.x + ship_width - bullet_width;
				b2.y = ship.y;
				b2.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b2.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b2.setStandardAnimation(standardAnimation);
				b2.setFadeAnimation(fadeAnimation);

				collection.add(b2);

				statistics.setBulletsFired(statistics.getBulletsFired() + 2);

				break;
//			case Ship.PROJECTILE_TYPE_NORMAL_TRIPLE:
//				b1.x = ship.x;
//				b1.y = ship.y;
//				b1.setProjectileDamage(STANDARD_BULLET_DAMAGE
//						+ bulletDamageModifier);
//				b1.setProjectileSpeed(STANDARD_BULLET_SPEED);
//
//				b1.setStandardAnimation(standardAnimation);
//				b1.setFadeAnimation(fadeAnimation);
//
//				collection.add(b1);
//				b2.x = ship.x + ship_width - bullet_width;
//				b2.y = ship.y;
//				b2.setProjectileDamage(STANDARD_BULLET_DAMAGE
//						+ bulletDamageModifier);
//				b2.setProjectileSpeed(STANDARD_BULLET_SPEED);
//
//				b2.setStandardAnimation(standardAnimation);
//				b2.setFadeAnimation(fadeAnimation);
//
//				collection.add(b2);
//				b3.x = ship.x + (ship_width / 2) - (bullet_width / 2);
//				b3.y = ship.y - 5;
//				b3.setProjectileDamage(STANDARD_BULLET_DAMAGE
//						+ bulletDamageModifier);
//				b3.setProjectileSpeed(STANDARD_BULLET_SPEED);
//
//				b3.setStandardAnimation(standardAnimation);
//				b3.setFadeAnimation(fadeAnimation);
//
//				collection.add(b3);
//
//				statistics.setBulletsFired(statistics.getBulletsFired() + 3);
//
//				break;
			case Ship.PROJECTILE_TYPE_SPREAD_TRIPLE:
				b1.x = ship.x;
				b1.y = ship.y;
				b1.setSpeedX(-2);
				b1.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b1.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b1.setStandardAnimation(standardAnimation);
				b1.setFadeAnimation(fadeAnimation);

				collection.add(b1);
				b2.x = ship.x + ship_width - bullet_width;
				b2.y = ship.y;
				b2.setSpeedX(2);
				b2.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b2.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b2.setStandardAnimation(standardAnimation);
				b2.setFadeAnimation(fadeAnimation);

				collection.add(b2);
				b3.x = ship.x + (ship_width / 2) - (bullet_width / 2);
				b3.y = ship.y - 5;
				b3.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b3.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b3.setStandardAnimation(standardAnimation);
				b3.setFadeAnimation(fadeAnimation);

				collection.add(b3);
				
				statistics.setBulletsFired(statistics.getBulletsFired() + 3);
				
				break;
			case Ship.PROJECTILE_TYPE_SPREAD_FOUR:
				b1.x = ship.x;
				b1.y = ship.y;
				b1.setSpeedX(-2);
				b1.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b1.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b1.setStandardAnimation(standardAnimation);
				b1.setFadeAnimation(fadeAnimation);

				collection.add(b1);
				b2.x = ship.x + ship_width - bullet_width;
				b2.y = ship.y;
				b2.setSpeedX(2);
				b2.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b2.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b2.setStandardAnimation(standardAnimation);
				b2.setFadeAnimation(fadeAnimation);

				collection.add(b2);
				b3.x = ship.x + (ship_width / 2) - (bullet_width / 2) - bullet_width;
				b3.y = ship.y - 5;
				b3.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b3.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b3.setStandardAnimation(standardAnimation);
				b3.setFadeAnimation(fadeAnimation);

				collection.add(b3);

				b4.x = ship.x + (ship_width / 2) - (bullet_width / 2) + bullet_width;
				b4.y = ship.y - 5;
				b4.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b4.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b4.setStandardAnimation(standardAnimation);
				b4.setFadeAnimation(fadeAnimation);

				collection.add(b4);
				
				statistics.setBulletsFired(statistics.getBulletsFired() + 4);
				
				break;
			case Ship.PROJECTILE_TYPE_SPREAD_FIVE:
				b1.x = ship.x;
				b1.y = ship.y;
				b1.setSpeedX(-3);
				b1.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b1.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b1.setStandardAnimation(standardAnimation);
				b1.setFadeAnimation(fadeAnimation);

				collection.add(b1);
				b2.x = ship.x + ship_width - bullet_width;
				b2.y = ship.y;
				b2.setSpeedX(3);
				b2.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b2.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b2.setStandardAnimation(standardAnimation);
				b2.setFadeAnimation(fadeAnimation);

				collection.add(b2);
				b3.x = ship.x + (ship_width / 2) - (bullet_width / 2) - bullet_width;
				b3.y = ship.y - 5;
				b3.setSpeedX(-1);
				b3.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b3.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b3.setStandardAnimation(standardAnimation);
				b3.setFadeAnimation(fadeAnimation);

				collection.add(b3);
				b4.x = ship.x + (ship_width / 2) - (bullet_width / 2) + bullet_width;
				b4.y = ship.y - 5;
				b4.setSpeedX(1);
				b4.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b4.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b4.setStandardAnimation(standardAnimation);
				b4.setFadeAnimation(fadeAnimation);

				collection.add(b4);
				
				b5.x = ship.x + (ship_width / 2) - (bullet_width / 2);
				b5.y = ship.y - 5;
				b5.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b5.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b5.setStandardAnimation(standardAnimation);
				b5.setFadeAnimation(fadeAnimation);

				collection.add(b5);
				
				statistics.setBulletsFired(statistics.getBulletsFired() + 5);
				
				break;
			case Ship.PROJECTILE_TYPE_SPREAD_SIX:
				b1.x = ship.x;
				b1.y = ship.y;
				b1.setSpeedX(-3);
				b1.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b1.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b1.setStandardAnimation(standardAnimation);
				b1.setFadeAnimation(fadeAnimation);

				collection.add(b1);
				b2.x = ship.x + ship_width - bullet_width;
				b2.y = ship.y;
				b2.setSpeedX(3);
				b2.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b2.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b2.setStandardAnimation(standardAnimation);
				b2.setFadeAnimation(fadeAnimation);

				collection.add(b2);
				b3.x = ship.x + (ship_width / 2) - (bullet_width / 2) - bullet_width;
				b3.y = ship.y - 5;
				b3.setSpeedX(-1);
				b3.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b3.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b3.setStandardAnimation(standardAnimation);
				b3.setFadeAnimation(fadeAnimation);

				collection.add(b3);
				b4.x = ship.x + (ship_width / 2) - (bullet_width / 2) + bullet_width;
				b4.y = ship.y - 5;
				b4.setSpeedX(1);
				b4.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b4.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b4.setStandardAnimation(standardAnimation);
				b4.setFadeAnimation(fadeAnimation);

				collection.add(b4);
				
				b5.x = ship.x + (ship_width / 2) - (bullet_width / 2) - bullet_width;
				b5.y = ship.y - 5;
				b5.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b5.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b5.setStandardAnimation(standardAnimation);
				b5.setFadeAnimation(fadeAnimation);

				collection.add(b5);
				
				b6.x = ship.x + (ship_width / 2) - (bullet_width / 2) + bullet_width;
				b6.y = ship.y - 5;
				b6.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b6.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b6.setStandardAnimation(standardAnimation);
				b6.setFadeAnimation(fadeAnimation);

				collection.add(b6);
				
				statistics.setBulletsFired(statistics.getBulletsFired() + 6);
				
				break;
			case Ship.PROJECTILE_TYPE_SPREAD_SEVEN:
				b1.x = ship.x;
				b1.y = ship.y;
				b1.setSpeedX(-4);
				b1.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b1.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b1.setStandardAnimation(standardAnimation);
				b1.setFadeAnimation(fadeAnimation);

				collection.add(b1);
				b2.x = ship.x + ship_width - bullet_width;
				b2.y = ship.y;
				b2.setSpeedX(4);
				b2.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b2.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b2.setStandardAnimation(standardAnimation);
				b2.setFadeAnimation(fadeAnimation);

				collection.add(b2);
				
				b3.x = ship.x + (ship_width / 2) - (bullet_width / 2) - bullet_width;
				b3.y = ship.y - 5;
				b3.setSpeedX(-2);
				b3.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b3.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b3.setStandardAnimation(standardAnimation);
				b3.setFadeAnimation(fadeAnimation);

				collection.add(b3);
				
				b7.x = ship.x + (ship_width / 2) - (bullet_width / 2) - bullet_width + bullet_width/2;
				b7.y = ship.y - 5;
				b7.setSpeedX(-2);
				b7.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b7.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b7.setStandardAnimation(standardAnimation);
				b7.setFadeAnimation(fadeAnimation);

				collection.add(b7);
				
				b4.x = ship.x + (ship_width / 2) - (bullet_width / 2) + bullet_width;
				b4.y = ship.y - 5;
				b4.setSpeedX(2);
				b4.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b4.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b4.setStandardAnimation(standardAnimation);
				b4.setFadeAnimation(fadeAnimation);

				collection.add(b4);
				
				b5.x = ship.x + (ship_width / 2) - (bullet_width / 2) - bullet_width;
				b5.y = ship.y - 5;
				b5.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b5.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b5.setStandardAnimation(standardAnimation);
				b5.setFadeAnimation(fadeAnimation);

				collection.add(b5);
				
				b6.x = ship.x + (ship_width / 2) - (bullet_width / 2) + bullet_width;
				b6.y = ship.y - 5;
				b6.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b6.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b6.setStandardAnimation(standardAnimation);
				b6.setFadeAnimation(fadeAnimation);

				collection.add(b6);
				
				statistics.setBulletsFired(statistics.getBulletsFired() + 7);
				
				break;
			case Ship.PROJECTILE_TYPE_SPREAD_EIGHT:
				b1.x = ship.x;
				b1.y = ship.y;
				b1.setSpeedX(-4);
				b1.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b1.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b1.setStandardAnimation(standardAnimation);
				b1.setFadeAnimation(fadeAnimation);

				collection.add(b1);
				b2.x = ship.x + ship_width - bullet_width;
				b2.y = ship.y;
				b2.setSpeedX(4);
				b2.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b2.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b2.setStandardAnimation(standardAnimation);
				b2.setFadeAnimation(fadeAnimation);

				collection.add(b2);
				
				b3.x = ship.x + (ship_width / 2) - (bullet_width / 2) - bullet_width;
				b3.y = ship.y - 5;
				b3.setSpeedX(-2);
				b3.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b3.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b3.setStandardAnimation(standardAnimation);
				b3.setFadeAnimation(fadeAnimation);

				collection.add(b3);
				
				b7.x = ship.x + (ship_width / 2) - (bullet_width / 2) - bullet_width + bullet_width/2;
				b7.y = ship.y - 5;
				b7.setSpeedX(-2);
				b7.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b7.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b7.setStandardAnimation(standardAnimation);
				b7.setFadeAnimation(fadeAnimation);

				collection.add(b7);
				
				b4.x = ship.x + (ship_width / 2) - (bullet_width / 2) + bullet_width;
				b4.y = ship.y - 5;
				b4.setSpeedX(2);
				b4.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b4.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b4.setStandardAnimation(standardAnimation);
				b4.setFadeAnimation(fadeAnimation);

				collection.add(b4);
				
				b8.x = ship.x + (ship_width / 2) - (bullet_width / 2) + bullet_width - bullet_width/2;
				b8.y = ship.y - 5;
				b8.setSpeedX(2);
				b8.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b8.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b8.setStandardAnimation(standardAnimation);
				b8.setFadeAnimation(fadeAnimation);

				collection.add(b8);
				
				b5.x = ship.x + (ship_width / 2) - (bullet_width / 2) - bullet_width;
				b5.y = ship.y - 5;
				b5.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b5.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b5.setStandardAnimation(standardAnimation);
				b5.setFadeAnimation(fadeAnimation);

				collection.add(b5);
				
				b6.x = ship.x + (ship_width / 2) - (bullet_width / 2) + bullet_width;
				b6.y = ship.y - 5;
				b6.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b6.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b6.setStandardAnimation(standardAnimation);
				b6.setFadeAnimation(fadeAnimation);

				collection.add(b6);
				
				statistics.setBulletsFired(statistics.getBulletsFired() + 8);
				
				break;
			case Ship.PROJECTILE_TYPE_SPREAD_NINE:
				b1.x = ship.x;
				b1.y = ship.y;
				b1.setSpeedX(-4);
				b1.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b1.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b1.setStandardAnimation(standardAnimation);
				b1.setFadeAnimation(fadeAnimation);

				collection.add(b1);
				
				b9.x = ship.x - bullet_width / 2;
				b9.y = ship.y;
				b9.setSpeedX(-4);
				b9.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b9.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b9.setStandardAnimation(standardAnimation);
				b9.setFadeAnimation(fadeAnimation);

				collection.add(b9);
				
				b2.x = ship.x + ship_width - bullet_width;
				b2.y = ship.y;
				b2.setSpeedX(4);
				b2.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b2.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b2.setStandardAnimation(standardAnimation);
				b2.setFadeAnimation(fadeAnimation);

				collection.add(b2);
				
				b3.x = ship.x + (ship_width / 2) - (bullet_width / 2) - bullet_width;
				b3.y = ship.y - 5;
				b3.setSpeedX(-2);
				b3.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b3.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b3.setStandardAnimation(standardAnimation);
				b3.setFadeAnimation(fadeAnimation);

				collection.add(b3);
				
				b7.x = ship.x + (ship_width / 2) - (bullet_width / 2) - bullet_width + bullet_width/2;
				b7.y = ship.y - 5;
				b7.setSpeedX(-2);
				b7.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b7.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b7.setStandardAnimation(standardAnimation);
				b7.setFadeAnimation(fadeAnimation);

				collection.add(b7);
				
				b4.x = ship.x + (ship_width / 2) - (bullet_width / 2) + bullet_width;
				b4.y = ship.y - 5;
				b4.setSpeedX(2);
				b4.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b4.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b4.setStandardAnimation(standardAnimation);
				b4.setFadeAnimation(fadeAnimation);

				collection.add(b4);
				
				b8.x = ship.x + (ship_width / 2) - (bullet_width / 2) + bullet_width - bullet_width/2;
				b8.y = ship.y - 5;
				b8.setSpeedX(2);
				b8.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b8.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b8.setStandardAnimation(standardAnimation);
				b8.setFadeAnimation(fadeAnimation);

				collection.add(b8);
				
				b5.x = ship.x + (ship_width / 2) - (bullet_width / 2) - bullet_width;
				b5.y = ship.y - 5;
				b5.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b5.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b5.setStandardAnimation(standardAnimation);
				b5.setFadeAnimation(fadeAnimation);

				collection.add(b5);
				
				b6.x = ship.x + (ship_width / 2) - (bullet_width / 2) + bullet_width;
				b6.y = ship.y - 5;
				b6.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b6.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b6.setStandardAnimation(standardAnimation);
				b6.setFadeAnimation(fadeAnimation);

				collection.add(b6);
				
				statistics.setBulletsFired(statistics.getBulletsFired() + 9);
				
				break;
			case Ship.PROJECTILE_TYPE_SPREAD_TEN:
				b1.x = ship.x;
				b1.y = ship.y;
				b1.setSpeedX(-4);
				b1.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b1.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b1.setStandardAnimation(standardAnimation);
				b1.setFadeAnimation(fadeAnimation);

				collection.add(b1);
				
				b9.x = ship.x - bullet_width / 2;
				b9.y = ship.y;
				b9.setSpeedX(-4);
				b9.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b9.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b9.setStandardAnimation(standardAnimation);
				b9.setFadeAnimation(fadeAnimation);

				collection.add(b9);
				
				b2.x = ship.x + ship_width - bullet_width;
				b2.y = ship.y;
				b2.setSpeedX(4);
				b2.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b2.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b2.setStandardAnimation(standardAnimation);
				b2.setFadeAnimation(fadeAnimation);

				collection.add(b2);
				
				b10.x = ship.x + ship_width - bullet_width + bullet_width / 2;
				b10.y = ship.y;
				b10.setSpeedX(4);
				b10.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b10.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b10.setStandardAnimation(standardAnimation);
				b10.setFadeAnimation(fadeAnimation);

				collection.add(b10);
				
				b3.x = ship.x + (ship_width / 2) - (bullet_width / 2) - bullet_width;
				b3.y = ship.y - 5;
				b3.setSpeedX(-2);
				b3.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b3.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b3.setStandardAnimation(standardAnimation);
				b3.setFadeAnimation(fadeAnimation);

				collection.add(b3);
				
				b7.x = ship.x + (ship_width / 2) - (bullet_width / 2) - bullet_width + bullet_width/2;
				b7.y = ship.y - 5;
				b7.setSpeedX(-2);
				b7.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b7.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b7.setStandardAnimation(standardAnimation);
				b7.setFadeAnimation(fadeAnimation);

				collection.add(b7);
				
				b4.x = ship.x + (ship_width / 2) - (bullet_width / 2) + bullet_width;
				b4.y = ship.y - 5;
				b4.setSpeedX(2);
				b4.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b4.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b4.setStandardAnimation(standardAnimation);
				b4.setFadeAnimation(fadeAnimation);

				collection.add(b4);
				
				b8.x = ship.x + (ship_width / 2) - (bullet_width / 2) + bullet_width - bullet_width/2;
				b8.y = ship.y - 5;
				b8.setSpeedX(2);
				b8.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b8.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b8.setStandardAnimation(standardAnimation);
				b8.setFadeAnimation(fadeAnimation);

				collection.add(b8);
				
				b5.x = ship.x + (ship_width / 2) - (bullet_width / 2) - bullet_width;
				b5.y = ship.y - 5;
				b5.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b5.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b5.setStandardAnimation(standardAnimation);
				b5.setFadeAnimation(fadeAnimation);

				collection.add(b5);
				
				b6.x = ship.x + (ship_width / 2) - (bullet_width / 2) + bullet_width;
				b6.y = ship.y - 5;
				b6.setProjectileDamage(STANDARD_BULLET_DAMAGE
						+ bulletDamageModifier);
				b6.setProjectileSpeed(STANDARD_BULLET_SPEED);

				b6.setStandardAnimation(standardAnimation);
				b6.setFadeAnimation(fadeAnimation);

				collection.add(b6);
				
				statistics.setBulletsFired(statistics.getBulletsFired() + 10);
				
				break;
			}
			bullets.addAll(collection);
			fireCooldown=7;
		}
	}
	/**
	 * Get Bullet List.
	 * @return Bullet List
	 */
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
	/**
	 * Set Bullet List.
	 * @param bullets Bullet List
	 */
	public void setBullets(ArrayList<Bullet> bullets) {
		this.bullets = bullets;
	}
	/**
	 * Get Ship that is being controlled.
	 * @return Ship
	 */
	public Ship getShip()
	{
		return this.ship;
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
	 * @return the fireLockOn
	 */
	public boolean isFireLockOn() {
		return fireLockOn;
	}
	/**
	 * @param fireLockOn the fireLockOn to set
	 */
	public void setFireLockOn(boolean fireLockOn) {
		this.fireLockOn = fireLockOn;
	}
	/**
	 * Returns if the ship is available to fire bullets.
	 * @return true if ship can fire.
	 */
	public boolean getFireReadyState()
	{
		if(fireLockOn)
		{
			if(autoFireTimer>15)
			{
				resetFireTimer();
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	/**
	 * Recharges ship weapon for auto-fire.
	 */
	public void fireTimerTick()
	{
		autoFireTimer++;
	}
	/**
	 * Resets auto fire timer.
	 */
	public void resetFireTimer()
	{
		autoFireTimer=0;
	}
	/**
	 * @return the bulletDamageModifier
	 */
	public int getBulletDamageModifier() {
		return bulletDamageModifier;
	}
	/**
	 * @param bulletDamageModifier the bulletDamageModifier to set
	 */
	public void setBulletDamageModifier(int bulletDamageModifier) {
		this.bulletDamageModifier = bulletDamageModifier;
	}
	/**
	 * @return the audio_manager
	 */
	public AsteroidRushAudioManager getAudio_manager() {
		return audio_manager;
	}
	/**
	 * @param audio_manager the audio_manager to set
	 */
	public void setAudio_manager(AsteroidRushAudioManager audio_manager) {
		this.audio_manager = audio_manager;
	}
}
