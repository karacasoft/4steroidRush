package com.karacasoft.asteroidrush2.utils;

import java.util.ArrayList;

import com.karacasoft.asteroidrush2.models.Asteroid;
import com.karacasoft.asteroidrush2.models.Bullet;
import com.karacasoft.asteroidrush2.models.PowerUp;
import com.karacasoft.asteroidrush2.models.Ship;
import com.karacasoft.asteroidrush2.models.UserStatistics;
import com.karacasoft.asteroidrush2.models.ValueHolder;
import com.karacasoft.asteroidrush2.views.GameView.AsteroidRushThread.AsteroidRushAudioManager;
import com.karacasoft.asteroidrush2.views.GameView.AsteroidRushThread.AsteroidRushCollisionChecker;

/**
 * Game operations will be made from this class.
 * 
 * @author Triforce
 * 
 */
public class AsteroidRushGameMechanicEngine {
	/**
	 * Constant to define the state of running game.
	 */
	public static final int GAME_STATE_RUNNING = 0;
	/**
	 * Constant to define the state of paused game.
	 */
	public static final int GAME_STATE_PAUSED = 1;
	/**
	 * Constant to define the state of game that is about to start.
	 */
	public static final int GAME_STATE_START_UP = 2;
	/**
	 * Constant to define the state of failed game.
	 */
	public static final int GAME_STATE_FAILED = 3;
	
	public static final int GAME_DIFFICULTY_ANIMATION_TIME = 150;
	public static final int GAME_DIFFICULTY_CHANGE_TIME = 750;
	
	public static final int UPGRADE_TIME = 1000;
	
	
	private int gameState;
	private AsteroidController asteroidController;
	private ShipController shipController;
	private AsteroidRushCollisionChecker collisionChecker;
	private AsteroidRushAudioManager audioManager;
	private ValueHolder values;
	private ArrayList<Bullet> bullets;
	private ArrayList<PowerUp> powerUpList;
	
	
	private UserStatistics statistics;
	
	public int Score;
	public int Distance;
	public int UpgradeLevel=0;
	public int upgradeTimer=0;
	
	public int DifficultyLevel=1;
	private int difficultyChangeTimer=0;
	private int difficultyAnimationTimer=0;
	public boolean onDifficultyAnimation=false;
	
	private float screen_dpi_based_multiplier=1;
	
	private boolean onAnimation=false;
	
	
	
	
	/**
	 * Constructor
	 */
	public AsteroidRushGameMechanicEngine() {
		bullets = new ArrayList<Bullet>();
		shipController = new ShipController(bullets);
		
	}

	/**
	 * Constructor with AsteroidFactory
	 * 
	 * @param factory
	 *            AsteroidFactory.
	 */
	public AsteroidRushGameMechanicEngine(AsteroidController factory) {
		bullets = new ArrayList<Bullet>();
		shipController = new ShipController(bullets);
		this.asteroidController = factory;
		
	}

	/**
	 * Constructor with AsteroidController and UserStatistics
	 * 
	 * @param factory AsteroidController
	 * @param statistics UserStatistics
	 */
	public AsteroidRushGameMechanicEngine(AsteroidController factory, UserStatistics statistics) {
		bullets = new ArrayList<Bullet>();
		shipController = new ShipController(bullets);
		this.asteroidController = factory;
		this.statistics=statistics;
		
	}
	
	/**
	 * Method to change every needed change in the game. <br />
	 * -Asteroid position changes.<br />
	 * -Power-Up position changes.<br />
	 * -Asteroid spawn-rate.<br />
	 * -Bullets position changes.<br />
	 * -Bullet timers.<br /> 
	 * -Power-Up timers.<br />
	 * 
	 */
	public void gameTick() {
		switch (gameState) {
		case GAME_STATE_START_UP:

			Score = 0;
			Distance = 0;
			UpgradeLevel = 0;
			upgradeTimer = 0;
			
			DifficultyLevel = 1;
			difficultyChangeTimer=0;
			
			asteroidController.setDifficultyLevel(DifficultyLevel);
			onDifficultyAnimation=false;
			difficultyAnimationTimer=0;
			
			shipController.getShip().setProjectileType(
					Ship.PROJECTILE_TYPE_NORMAL_SINGLE);
			
			shipController.setFireLockOn(false);
			
			asteroidController.getAsteroidList().clear();
			bullets.clear();
			powerUpList.clear();
			
			break;
		case GAME_STATE_RUNNING:
			
			upgradeTimer++;
			difficultyChangeTimer++;
			
			shipController.fireCooldown--;
			
			if(upgradeTimer>UPGRADE_TIME)
			{
				UpgradeLevel++;
				upgradeTimer=0;
			}
			
			if(difficultyChangeTimer>GAME_DIFFICULTY_CHANGE_TIME)
			{
				DifficultyLevel++;
				audioManager.setMusicSpeed(1.0f+DifficultyLevel/10);
				asteroidController.setDifficultyLevel(DifficultyLevel);
				
				difficultyChangeTimer=0;
				onDifficultyAnimation=true;
			}
			
			if(onDifficultyAnimation)
			{
				difficultyAnimationTimer++;
				if(difficultyAnimationTimer>GAME_DIFFICULTY_ANIMATION_TIME)
				{
					difficultyAnimationTimer=0;
					onDifficultyAnimation=false;
				}
			}else{
				
				asteroidController.addNewRandomNonCollidingAsteroid(Asteroid.ASTEROID_TYPE_1,
						values.getAsteroid_width(), values.getAsteroid_height());			
			}
			
			Ship s = shipController.getShip();
			Distance += s.speed;

			
			try {

				int asteroidListSize = asteroidController.getAsteroidList()
						.size();
				int indexAst = 0;
				while (indexAst < asteroidListSize) {
					Asteroid a = asteroidController.getAsteroidList().get(
							indexAst);
					if (a != null) {
						
						a.y += (a.speed + s.speed)
								* screen_dpi_based_multiplier;
						a.rotation+=a.rotation_speed;
						while(a.rotation>360)
						{
							a.rotation-=360;
						}
						// a.y += a.speed + s.speed;
						collisionChecker
								.checkAsteroidShipCollision(a, s,
										values.getAsteroid_width(),
										values.getAsteroid_height(),
										values.getShip_width(),
										values.getShip_height());
					}
					indexAst++;
				}
			} catch (IndexOutOfBoundsException e) {
				// e.printStackTrace();
			}
			
			try {
				int bulletListSize=shipController.getBullets().size();
				int indexBullet=0;
				while (indexBullet<bulletListSize) {
					Bullet b=shipController.getBullets().get(indexBullet);
					if (b != null) {
						b.y -= (b.getProjectileSpeed() - s.speed)
								* screen_dpi_based_multiplier;
						b.x += (b.getSpeedX()) * screen_dpi_based_multiplier;
						// b.y -= b.getProjectileSpeed() - s.speed;
						try {
							int asteroidListSize = asteroidController
									.getAsteroidList().size();
							int indexAst = 0;

							while (indexAst < asteroidListSize) {
								Asteroid a = asteroidController
										.getAsteroidList().get(indexAst);
								if (a != null) {
									collisionChecker
											.checkAsteroidBulletCollision(
													a,
													b,
													values.getAsteroid_width(),
													values.getAsteroid_height(),
													values.getBullet_width(),
													values.getBullet_height());
								}
								indexAst++;
							}
						} catch (IndexOutOfBoundsException e) {
							// e.printStackTrace();
						}
					}
					indexBullet++;
				}
			} catch (IndexOutOfBoundsException e) {
				//e.printStackTrace();
			}
			
			int powerUpListSize = powerUpList.size();
			int indexPowerUp = 0;
			try {
				while(indexPowerUp < powerUpListSize)
				{
					PowerUp p = powerUpList.get(indexPowerUp);
					if(p != null)
					{
						p.y += p.speedY;
						p.speedY += 1;
						collisionChecker.checkShipPowerUpCollision(s, p, 16, 16,
								values.getShip_width(), values.getShip_height());
						
						if(p.y > asteroidController.getScreenHeight())
						{
							powerUpList.remove(indexPowerUp);
							powerUpListSize--;
						}
					}
					
					indexPowerUp++;
				}
			} catch (IndexOutOfBoundsException e1) {
//				e1.printStackTrace();
			}
			
			
			if(shipController.getFireReadyState())
				shipController.fire(values.getBullet_width(), values.getShip_width(), statistics);
			if(shipController.isFireLockOn())
				shipController.fireTimerTick();
			try
			{
				asteroidController.deleteNeedlessAsteroids();
				deleteNeedlessBullets();
			}
			catch(IndexOutOfBoundsException e)
			{
				
			}

			break;
		case GAME_STATE_PAUSED:
			
			break;
		case GAME_STATE_FAILED:
			
			break;
		}
		
		
	}
	
	/**
	 * Get gameState
	 * 
	 * @return Game Status
	 */
	public int getGameState() {
		return gameState;
	}

	/**
	 * Set gameState
	 * 
	 * @param gameState
	 *            Game Status. Can be <i>GAME_STATE_RUNNING</i>,
	 *            <i>GAME_STATE_PAUSED</i>, <i>GAME_STATE_START_UP</i> or
	 *            <i>GAME_STATE_FAILED</i>.
	 */
	public void setGameState(int gameState) {
		this.gameState = gameState;
	}

	/**
	 * Start the Game.
	 */
	public void startGame() {
		this.setGameState(GAME_STATE_RUNNING);
		audioManager.playMusic();
	}

	/**
	 * Pause game.
	 */
	public void pause() {
		this.setGameState(GAME_STATE_PAUSED);
	}

	/**
	 * Restart game. Reset score, delete all Asteroids,...
	 * 
	 * <p>
	 * Remember that it will only reset everything and return to the starting
	 * point of the game. It will not start the game.
	 * </p>
	 */
	public void restartGame() {
		this.setGameState(GAME_STATE_START_UP);
	}

	/**
	 * Continue game.
	 */
	public void continuePausedGame() {
		this.setGameState(GAME_STATE_RUNNING);
	}
	/**
	 * Make the game lost.
	 */
	public void failGame()
	{
		audioManager.stopMusic();
		this.setGameState(GAME_STATE_FAILED);
	}
	/**
	 * Delete bullets that are out of screen boundaries.
	 */
	public void deleteNeedlessBullets() throws IndexOutOfBoundsException {
		ArrayList<Bullet> newList = new ArrayList<Bullet>();
		int i=0;
		int goal=this.getBullets().size();
		while(i<goal)
		{
			Bullet b = null;
			/*try
			{*/
				b=this.getBullets().get(i);
			/*}catch(Exception e)
			{
				e.printStackTrace();
			}*/
			if (b != null) {
				
				if (b.y < 0) {
					statistics.setBulletsMissed(statistics.getBulletsMissed()+1);
				} else {
					
					newList.add(b);
					
				}
			}
			i++;
		}
		
		/*for (Bullet b : this.getBullets()) {
			if (b.y < 0) {

			} else {
				newList.add(b);
			}
		}*/

		this.getBullets().clear();
		this.getBullets().addAll(newList);
	}
	/**
	 * Get AsteroidController
	 * 
	 * @return AsteroidController
	 */
	public AsteroidController getAsteroidFactory() {
		return asteroidController;
	}

	/**
	 * Set AsteroidController
	 * 
	 * @param asteroidFactory
	 *            AsteroidController
	 */
	public void setAsteroidFactory(AsteroidController asteroidFactory) {
		this.asteroidController = asteroidFactory;
	}

	/**
	 * Get Bullets List.
	 * 
	 * @return Bullets
	 */
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	/**
	 * Set Bullets List.
	 * 
	 * @param bullets
	 *            Bullets
	 */
	public void setBullets(ArrayList<Bullet> bullets) {
		this.bullets = bullets;
	}

	/**
	 * Get ShipController.
	 * 
	 * @return ShipController.
	 */
	public ShipController getShipController() {
		return shipController;
	}

	/**
	 * Set ShipController.
	 * 
	 * @param shipController
	 *            ShipController
	 */
	public void setShipController(ShipController shipController) {
		this.shipController = shipController;
	}
	/**
	 * Get screen density multiplier.
	 * @return Screen density multiplier 
	 */
	public float getScreen_dpi_based_multiplier() {
		return screen_dpi_based_multiplier;
	}
	/**
	 * Set screen density multiplier.
	 * @param screen_dpi_based_multiplier Screen density multiplier
	 */
	public void setScreen_dpi_based_multiplier(float screen_dpi_based_multiplier) {
		this.screen_dpi_based_multiplier = screen_dpi_based_multiplier;
	}

	/**
	 * @return the onAnimation state
	 */
	public boolean isOnAnimation() {
		return onAnimation;
	}

	/**
	 * @param onAnimation the onAnimation state to set
	 */
	public void setOnAnimation(boolean onAnimation) {
		this.onAnimation = onAnimation;
	}

	/**
	 * @return the collisionChecker
	 */
	public AsteroidRushCollisionChecker getCollisionChecker() {
		return collisionChecker;
	}

	/**
	 * @param collisionChecker the collisionChecker to set
	 */
	public void setCollisionChecker(AsteroidRushCollisionChecker collisionChecker) {
		this.collisionChecker = collisionChecker;
	}

	/**
	 * @return the values
	 */
	public ValueHolder getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(ValueHolder values) {
		this.values = values;
	}

	/**
	 * @return the audioManager
	 */
	public AsteroidRushAudioManager getAudioManager() {
		return audioManager;
	}

	/**
	 * @param audioManager the audioManager to set
	 */
	public void setAudioManager(AsteroidRushAudioManager audioManager) {
		this.audioManager = audioManager;
	}

	public ArrayList<PowerUp> getPowerUpList() {
		return powerUpList;
	}

	public void setPowerUpList(ArrayList<PowerUp> powerUpList) {
		this.powerUpList = powerUpList;
	}
	
	
}
