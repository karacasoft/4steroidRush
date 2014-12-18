package com.karacasoft.asteroidrush2.utils;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;

import com.karacasoft.asteroidrush2.R;
import com.karacasoft.asteroidrush2.models.Asteroid;

/**
 * Implement this class and its methods to add asteroids.
 * 
 * @author Triforce
 *
 */
public class AsteroidController {
	
	private int screen_width;
	private int screen_height;
	
	private ArrayList<Asteroid> asteroidList;
	private int asteroidLimit;
	private Context context;
	
	private Animator asteroidAnimation;
	private Animator asteroidDestroyAnimation;
	
	private int difficultyLevel=1;
	/**
	 * Constructor
	 * @param context Activity Context.
	 * @param asteroidList Asteroid ArrayList to control.
	 */
	public AsteroidController(Context context, ArrayList<Asteroid> asteroidList) {
		
		this.setAsteroidList(asteroidList);
		this.context=context;
	}
	/**
	 * @deprecated use addNewRandomNonCollidingAsteroid instead.
	 * 
	 * Implement this method to add new random Asteroid to the Asteroid List of 
	 * the game
	 * @param type Asteroid type
	 * @return true if successfully added. false if Asteroid limit is reached.
	 */
	@Deprecated
	public boolean addNewRandomAsteroid(int type)
	{
		if(this.getAsteroidList().size()>this.getAsteroidLimit())
		{
			return false;
		}
		else
		{
			Asteroid a=new Asteroid();
			Random r=new Random();
			
			float generatedX=r.nextFloat()*screen_width;
			a.x=generatedX;
			a.y=-this.getContext().getResources().getInteger(R.integer.standard_asteroid_height);
			
			//Below values are just for test purposes.
			//TODO they should be changed to be random values based on game level
			a.speed=((float)1/(float)4)+((float)difficultyLevel/(float)4);
			a.rotation_speed=(int)r.nextFloat()*25;;
			a.type=type;
			
			//generate random health between 20-30+difficulty level
			int randomHealth=(int) (r.nextFloat()*5+15);
			a.asteroid_health=randomHealth+difficultyLevel*2;
			Animator animation=asteroidAnimation;
			//Animator destroyAnimation=new Animator(asteroidDestroyAnimation);
			a.setStandardAnimation(animation);
			
			//a.setDestroyAnimation(destroyAnimation);
			this.getAsteroidList().add(a);
			return true;
		}
	}
	/**
	 * Implement this method to add new random Asteroid that doesn't collide with
	 * other Asteroids graphically. If random values collide with an other
	 * Asteroid, random Asteroid will not be added to the list and the method
	 * will return false.
	 * @param type Asteroid type
	 * @param asteroid_width Asteroid pixel width on this device's screen
	 * @param asteroid_height  Asteroid pixel height on this device's screen
	 * @return true if successfully added. false if Asteroid limit is reached or
	 * if the random Asteroid collides with another Asteroid.
	 */
	public boolean addNewRandomNonCollidingAsteroid(int type, int asteroid_width,
			int asteroid_height)
	{
		if(this.getAsteroidList().size()>this.getAsteroidLimit())
		{
			return false;
		}
		else
		{
			boolean collision=false;
			Asteroid a=new Asteroid();
			Random r=new Random();
			
			float generatedX=r.nextFloat()*screen_width-asteroid_width/2;
			float generatedY=-asteroid_height;
			for(Asteroid asteroidToCheck : this.getAsteroidList())
			{
				if(!collision)
				{
					if((generatedX>asteroidToCheck.x-4*asteroid_width/3
								&&
							generatedX<asteroidToCheck.x+4*asteroid_width/3)
									&&
						(generatedY>asteroidToCheck.y-4*asteroid_height/3
								&&
							generatedY<asteroidToCheck.y+4*asteroid_height/3))
					{
						collision=true;
					}
				}
				else
				{
					break;
				}
			}
			if(!collision)
			{
				a.x=generatedX;
				a.y=generatedY;
			
			
				//Below values are just for test purposes.
				//TODO they should be changed to be random values based on game level
				a.speed=((float)1/(float)4)+((float)difficultyLevel/(float)4);
				a.rotation_speed=(int)(r.nextFloat()*15-10);
				a.type=type;
				//generate random health between 20-30+difficulty level
				int randomHealth=(int) (r.nextFloat()*5+10);
				a.asteroid_health=randomHealth+difficultyLevel*5;
				Animator animation=asteroidAnimation;
				//Animator destroyAnimation=new Animator(asteroidDestroyAnimation);
				a.setStandardAnimation(animation);
				
				//a.setDestroyAnimation(destroyAnimation);
				this.getAsteroidList().add(a);
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	/**
	 * Deletes Asteroids at the out of bounds of the screen.
	 */
	public void deleteNeedlessAsteroids() throws IndexOutOfBoundsException
	{
		ArrayList<Asteroid> newList=new ArrayList<Asteroid>();
		int i=0;
		int goal=this.getAsteroidList().size();
		while(i<goal)
		{
			
			//Surround with try/catch if needed...
			Asteroid a=this.getAsteroidList().get(i);
			//-----------------------------------
			if(a!=null)
			{
				if(a.y>screen_height)
				{
				
				}else
				{
					newList.add(a);
				}
			}
			i++;
		}
		
		/*for(Asteroid a : this.getAsteroidList())
		{
			if(a.y>screen_height)
			{
				
			}else
			{
				newList.add(a);
			}
		}*/
		
		this.getAsteroidList().clear();
		this.getAsteroidList().addAll(newList);
		
	}
	
	
	/**
	 * Returns the Asteroid ArrayList of the game
	 * @return Asteroid ArrayList
	 */
	public ArrayList<Asteroid> getAsteroidList() {
		return asteroidList;
	}
	/**
	 * Sets an Asteroid ArrayList to play with
	 * @param asteroidList Asteroid ArrayList
	 */
	public void setAsteroidList(ArrayList<Asteroid> asteroidList) {
		this.asteroidList = asteroidList;
	}
	/**
	 * Returns the asteroid limit of the game.
	 * @return limit of Asteroids
	 */
	public int getAsteroidLimit() {
		return asteroidLimit;
	}
	/**
	 * Sets the asteroid limit of the game.
	 * @param asteroidLimit limit of Asteroids
	 */
	public void setAsteroidLimit(int asteroidLimit) {
		this.asteroidLimit = asteroidLimit;
	}
	/**
	 * Returns the activity context
	 * @return Activity Context
	 */
	public Context getContext()
	{
		return context;
	}
	/**
	 * Get screen width in pixels.
	 * @return Screen width
	 */
	public int getScreenWidth()
	{
		return screen_width;
	}
	/**
	 * Get screen height in pixels
	 * @return Screen height
	 */
	public int getScreenHeight()
	{
		return screen_height;
	}
	/**
	 * Set screen width.
	 * @param screen_width Screen width
	 */
	public void setScreenWidth(int screen_width)
	{
		this.screen_width=screen_width;
	}
	/**
	 * Set screen height.
	 * @param screen_height Screen height
	 */
	public void setScreenHeight(int screen_height)
	{
		this.screen_height=screen_height;
	}
	/**
	 * @return the asteroidAnimation
	 */
	public Animator getAsteroidAnimation() {
		return asteroidAnimation;
	}
	/**
	 * @param asteroidAnimation the asteroidAnimation to set
	 */
	public void setAsteroidAnimation(Animator asteroidAnimation) {
		this.asteroidAnimation = asteroidAnimation;
	}
	/**
	 * @return the asteroidDestroyAnimation
	 */
	public Animator getAsteroidDestroyAnimation() {
		return asteroidDestroyAnimation;
	}
	/**
	 * @param asteroidDestroyAnimation the asteroidDestroyAnimation to set
	 */
	public void setAsteroidDestroyAnimation(Animator asteroidDestroyAnimation) {
		this.asteroidDestroyAnimation = asteroidDestroyAnimation;
	}
	/**
	 * @return the difficultyLevel
	 */
	public int getDifficultyLevel() {
		return difficultyLevel;
	}
	/**
	 * @param difficultyLevel the difficultyLevel to set
	 */
	public void setDifficultyLevel(int difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}
	
	
}
