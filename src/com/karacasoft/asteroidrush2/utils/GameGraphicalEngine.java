package com.karacasoft.asteroidrush2.utils;

import android.graphics.Canvas;
/**
 * Use this class to make graphical operations of the game.
 * 
 * @author Triforce
 *
 */
public abstract class GameGraphicalEngine {
	
	private AsteroidRushGameMechanicEngine engine;
	
	/**
	 * Empty constructor. 
	 */
	public GameGraphicalEngine()
	{
		
	}
	/**
	 * Constructor
	 * @param engine Game Engine
	 */
	public GameGraphicalEngine(AsteroidRushGameMechanicEngine engine) {
		this.setEngine(engine);
	}
	/**
	 * Draw game canvas.
	 * @return Canvas that is ready to display.
	 */
	public abstract Canvas drawCurrentGameCanvas();
	/**
	 * Initiate game bitmaps.
	 */
	public abstract void setBitmaps();
	
	/**
	 * Get GameEngine
	 * @return GameEngine
	 */
	public AsteroidRushGameMechanicEngine getEngine() {
		return engine;
	}
	/**
	 * Set GameEngine
	 * @param engine GameEngine;
	 */
	public void setEngine(AsteroidRushGameMechanicEngine engine) {
		this.engine = engine;
	}
	
}
