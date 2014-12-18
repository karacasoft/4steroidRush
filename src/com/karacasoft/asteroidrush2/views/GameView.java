package com.karacasoft.asteroidrush2.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.karacasoft.asteroidrush2.R;
import com.karacasoft.asteroidrush2.connectivity.WebDatabaseController;
import com.karacasoft.asteroidrush2.database.DatabaseHandler;
import com.karacasoft.asteroidrush2.models.Asteroid;
import com.karacasoft.asteroidrush2.models.Bullet;
import com.karacasoft.asteroidrush2.models.HighScore;
import com.karacasoft.asteroidrush2.models.Particle;
import com.karacasoft.asteroidrush2.models.PowerUp;
import com.karacasoft.asteroidrush2.models.ScreenDensityNotSupportedException;
import com.karacasoft.asteroidrush2.models.Ship;
import com.karacasoft.asteroidrush2.models.TouchableArea;
import com.karacasoft.asteroidrush2.models.TouchableArea.AsteroidRushOnTouch;
import com.karacasoft.asteroidrush2.models.UserStatistics;
import com.karacasoft.asteroidrush2.models.ValueHolder;
import com.karacasoft.asteroidrush2.utils.Animator;
import com.karacasoft.asteroidrush2.utils.AsteroidController;
import com.karacasoft.asteroidrush2.utils.AsteroidRushGameMechanicEngine;
import com.karacasoft.asteroidrush2.utils.AudioManager;
import com.karacasoft.asteroidrush2.utils.CollisionChecker;
import com.karacasoft.asteroidrush2.utils.GameGraphicalEngine;
import com.karacasoft.asteroidrush2.utils.GameTouchControls;
import com.karacasoft.asteroidrush2.utils.ParticleController;
/**
 * View class to display game content.
 * 
 * @author Triforce
 *
 */
public class GameView extends GLSurfaceView implements SurfaceHolder.Callback {
	
	Context context;
	SurfaceHolder holder;
	AsteroidRushThread thread;
	private int screen_width;
	private int screen_height;
	
	private DatabaseHandler databaseHandler;
	private String user;
	private UserStatistics statistics;
	
	private InterstitialAd interstitialAd;
	
	private boolean useOnline=true;
	
	public ValueHolder values;
	
	private int loseCounter=2;
	
	private boolean gameContinues=true;
	
	public boolean extra_graphics_on=false;
	public boolean music_on=false;
	public boolean sound_on=true;
	
	private float screen_dpi_based_multiplier=1; //This will be used to get dimensions of
												 //objects on different screens.
	
	/**
	 * 
	 * This class will calculate every change in the game.<br>
	 * <br>
	 * Things this class coordinates are:<br>
	 * -Asteroid spawn rate and spawn coordinates,<br>
	 * -Ship position changes,<br>
	 * -All drawing events,<br>
	 * -Setting up bitmaps,<br>
	 * -Timing events will be set from this class too.<br>
	 * 
	 * @author Triforce
	 *
	 */
	public class AsteroidRushThread extends Thread{
		
		/**
		 * This class will constantly add new random Asteroids to the game.
		 * 
		 * @author Triforce
		 *
		 */
		public class AsteroidRushAsteroidController extends AsteroidController
		{

			public AsteroidRushAsteroidController(Context context,
					ArrayList<Asteroid> asteroidList) {
				super(context, asteroidList);
				setScreenWidth(screen_width);
				setScreenHeight(screen_height);
			}
			
			/**
			 * Forces adding a new Asteroid to the game. Whenever it finds the
			 * possibility to add Asteroid, it adds the Asteroid to the list and
			 * returns the number of tries.
			 * @param type Asteroid type
			 * @param asteroid_width Asteroid pixel width on this device's screen
			 * @param asteroid_height  Asteroid pixel height on this device's screen
			 * @return number of tries to add a non-colliding Asteroid to the game.
			 */
			public int forceAddingNewNonCollidingAsteroid(int type,
					int asteroid_width, int asteroid_height)
			{
				int tryNo=1;
				while(!this.addNewRandomNonCollidingAsteroid(type, asteroid_width, asteroid_height))
				{
					tryNo++;
				}
				return tryNo;
			}
			
		}

		/**
		 * Adds new Power-Ups to the game.
		 * @author Triforce
		 *
		 */
		public class AsteroidRushPowerUpController
		{
			
			int powerUpCount = 1;
			
			/**
			 * Adds new point power-up to the given coordinates.
			 * @param x 
			 * @param y
			 */
			public void addNewPowerUp(float x, float y) {
				if(powerUpCount % 30 == 0)
				{
					PowerUp powerUp = new PowerUp();
					powerUp.setStandardAnimation(game_graphical_engine.powerUp2);
					powerUp.setPowerUpType(PowerUp.TYPE_LEVEL_UP);
					powerUp.x = x;
					powerUp.y = y;
					powerUp_list.add(powerUp);
				}else if(powerUpCount % 10 == 0)
				{
					PowerUp powerUp = new PowerUp();
					powerUp.setStandardAnimation(game_graphical_engine.powerUp1);
					powerUp.setPowerUpType(PowerUp.TYPE_ADD_DAMAGE);
					powerUp.x = x;
					powerUp.y = y;
					powerUp_list.add(powerUp);
				}else{
					PowerUp powerUp = new PowerUp();
					powerUp.setStandardAnimation(game_graphical_engine.powerUp0);
					powerUp.setPowerUpType(PowerUp.TYPE_POINTS);
					powerUp.x = x;
					powerUp.y = y;
					powerUp_list.add(powerUp);
				}
				powerUpCount++;
			}
			
			
		}
		
		/**
		 * Game graphics will be drawn with this class. Call
		 * {@link drawCurrentGameCanvas} to get a Canvas with everything ready.
		 * 
		 * @author Triforce
		 *
		 */
		public class AsteroidRushGameGraphicalEngine extends GameGraphicalEngine
		{
			
			public class AsteroidRushParticleController extends ParticleController
			{

				public AsteroidRushParticleController() {
					super();
					
				}
				

				public void createParticleGroupSpeedModified(float x, float y,
						int color, int particleCount, int addSpeedX,
						float addSpeedY) {
					createParticleGroup(x, y, color, particleCount);
					for(Particle p : this.getParticles())
					{
						p.setSpeedX(p.getSpeedX()+addSpeedX);
						p.setSpeedY(p.getSpeedY()+addSpeedY);
					}
					
				}
				
			}
			
			public AsteroidRushParticleController particle_controller;
			
			public static final int BACKGROUND_SCROLL_RATE=2;
			/*
			private static final int UPGRADE_LEVEL_BAR_LENGTH = 10;

			private static final int UPGRADE_LEVEL_BAR_HEIGHT = 100;
			// */
			//Animations
			public Animator asteroid_type1_animation;
			public Animator ship_animation;
			public Animator bullet_animation;
			public Animator bullet_fade_Animation;
			public Animator explosion_animation;
			public Animator powerUp0;
			public Animator powerUp1;
			public Animator powerUp2;
			
			private int backgroun_position=0;
			
			private int timerForMessage=0;
			private boolean showingMessage=false;
			private String messageText;
			private int colorOfMessageText=Color.GREEN;
			
			
			
			private int shakeScreen=0;
			
			//paints
			Paint score_text_paint;
			Paint continue_game_text_paint;
			Paint indicate_level_text_paint;
			
			
			public AsteroidRushGameGraphicalEngine(
					AsteroidRushGameMechanicEngine engine) {
				super(engine);
				particle_controller=new AsteroidRushParticleController();
				
			}
			
			
			@Override
			public Canvas drawCurrentGameCanvas() {
				Canvas c=holder.lockCanvas();
				if(c!=null)
				{
					if(extra_graphics_on)
					{
						if(shakeScreen!=0)
						{
							controlScreenShake();
						}
					}
					drawBackground(c);
					drawPowerUps(c);
					drawAsteroids(c);   //draw order affects the game display
					drawBullets(c);		// be careful about it.
					drawShip(c);
					if(extra_graphics_on)
						drawExtra(c);
					drawInfoText(c);
					drawHUD(c);
				
				
				
					return c;
				}else{
					return null;
				}
				
			}
			
			private void controlScreenShake()
			{
				if(shakeScreen>0)
				{
					shakeScreen-=(int)screen_dpi_based_multiplier;
					shakeScreen=-shakeScreen;
				}else if(shakeScreen<0){
					shakeScreen+=(int)screen_dpi_based_multiplier;
					shakeScreen=-shakeScreen;
				}
			}
			
			@Override
			public void setBitmaps() {
				
				//set Paints
				score_text_paint=new Paint();
				score_text_paint.setTypeface(type_face);
				score_text_paint.setTextSize(values.getScore_text_size());
				score_text_paint.setColor(Color.GREEN);
				
				continue_game_text_paint=new Paint();
				continue_game_text_paint.setTypeface(type_face);
				continue_game_text_paint.setTextSize(values.getContinue_game_text_size());
				continue_game_text_paint.setColor(Color.GREEN);
				
				indicate_level_text_paint=new Paint();
				indicate_level_text_paint.setTypeface(type_face);
				indicate_level_text_paint.setTextSize(values.getLevel_indicator_text_size());
				indicate_level_text_paint.setTextAlign(Align.CENTER);
				indicate_level_text_paint.setColor(Color.GREEN);
				
				
				//set Bitmaps
				asteroid_type1=BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_type1);
				ship=BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
				bullet=BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
				
				continue_mark=BitmapFactory.decodeResource(context.getResources(), R.drawable.continue_mark);
				
				exit_button=BitmapFactory.decodeResource(context.getResources(), R.drawable.button_exit);
				exit_button_pressed=BitmapFactory.decodeResource(context.getResources(), R.drawable.button_exit_pressed);
				
				restart_game_button=BitmapFactory.decodeResource(context.getResources(), R.drawable.button_restart_game);
				restart_game_button_pressed=BitmapFactory.decodeResource(context.getResources(), R.drawable.button_restart_game_pressed);
				
				weapon_damage_upgrade_button=BitmapFactory.decodeResource(context.getResources(), R.drawable.button_upgrade_weapon_damage);
				weapon_damage_upgrade_button_pressed=BitmapFactory.decodeResource(context.getResources(), R.drawable.button_upgrade_weapon_damage_pressed);
				weapon_damage_upgrade_button_disabled=BitmapFactory.decodeResource(context.getResources(), R.drawable.button_upgrade_weapon_damage_not_available);
				
				weapon_type_upgrade_button=BitmapFactory.decodeResource(context.getResources(), R.drawable.button_upgrade_weapon_type);
				weapon_type_upgrade_button_pressed=BitmapFactory.decodeResource(context.getResources(), R.drawable.button_upgrade_weapon_type_pressed);
				weapon_type_upgrade_button_disabled=BitmapFactory.decodeResource(context.getResources(), R.drawable.button_upgrade_weapon_type_not_available);
				
				
				
				Bitmap bullet_fade_frame1=BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_fade_frame1);
				Bitmap bullet_fade_frame2=BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_fade_frame2);
				Bitmap bullet_fade_frame3=BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_fade_frame3);
				Bitmap bullet_fade_frame4=BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_fade_frame4);
				Bitmap bullet_fade_frame5=BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_fade_frame5);
				Bitmap bullet_fade_frame6=BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_fade_frame6);
				Bitmap bullet_fade_frame7=BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_fade_frame7);
				Bitmap bullet_fade_frame8=BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_fade_frame8);
				Bitmap bullet_fade_frame9=BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_fade_frame9);
				Bitmap bullet_fade_frame10=BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_fade_frame10);
				Bitmap bullet_fade_frame11=BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_fade_frame11);
				
				Bitmap explosion_frame1=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_frame1)
						,values.getAsteroid_width(),values.getAsteroid_height(),true);
				Bitmap explosion_frame2=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_frame2)
						,values.getAsteroid_width(),values.getAsteroid_height(),true);
				Bitmap explosion_frame3=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_frame3)
						,values.getAsteroid_width(),values.getAsteroid_height(),true);
				Bitmap explosion_frame4=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_frame4)
						,values.getAsteroid_width(),values.getAsteroid_height(),true);
				Bitmap explosion_frame5=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_frame5)
						,values.getAsteroid_width(),values.getAsteroid_height(),true);
				Bitmap explosion_frame6=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_frame6)
						,values.getAsteroid_width(),values.getAsteroid_height(),true);
				Bitmap explosion_frame7=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_frame7)
						,values.getAsteroid_width(),values.getAsteroid_height(),true);
				Bitmap explosion_frame8=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_frame8)
						,values.getAsteroid_width(),values.getAsteroid_height(),true);
				Bitmap explosion_frame9=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_frame9)
						,values.getAsteroid_width(),values.getAsteroid_height(),true);
				
				Bitmap power1frame1 = BitmapFactory.decodeResource(getResources(), R.drawable.power_up0);
				Bitmap power2frame1 = BitmapFactory.decodeResource(getResources(), R.drawable.power_up1);
				Bitmap power3frame1 = BitmapFactory.decodeResource(getResources(), R.drawable.power_up2);
				
				
				//setBackgrounds
				main_background=BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
				tutorial=BitmapFactory.decodeResource(context.getResources(), R.drawable.tutorial);
				
				//scale Backgrounds
				main_background=Bitmap.createScaledBitmap(main_background, getScreenWidth(), getScreenHeight(), true);
				tutorial=Bitmap.createScaledBitmap(tutorial, getScreenWidth(), getScreenHeight(), true);
				
				//set Animators
				ArrayList<Bitmap> asteroid_type1_frames=new ArrayList<Bitmap>();
				asteroid_type1_frames.add(asteroid_type1);
				asteroid_type1_animation=new Animator("asteroid_type1", asteroid_type1_frames, Animator.ANIMATION_WAY_FORWARD);
				asteroid_factory.setAsteroidAnimation(asteroid_type1_animation);
				
				ArrayList<Bitmap> ship_frames=new ArrayList<Bitmap>();
				ship_frames.add(ship);
				ship_animation=new Animator("ship", ship_frames, Animator.ANIMATION_WAY_FORWARD);
				
				ArrayList<Bitmap> bullet_fade_frames=new ArrayList<Bitmap>();
				bullet_fade_frames.add(bullet_fade_frame1);
				bullet_fade_frames.add(bullet_fade_frame2);
				bullet_fade_frames.add(bullet_fade_frame3);
				bullet_fade_frames.add(bullet_fade_frame4);
				bullet_fade_frames.add(bullet_fade_frame5);
				bullet_fade_frames.add(bullet_fade_frame6);
				bullet_fade_frames.add(bullet_fade_frame7);
				bullet_fade_frames.add(bullet_fade_frame8);
				bullet_fade_frames.add(bullet_fade_frame9);
				bullet_fade_frames.add(bullet_fade_frame10);
				bullet_fade_frames.add(bullet_fade_frame11);
				bullet_fade_Animation=new Animator("bullet_fade", bullet_fade_frames, Animator.ANIMATION_WAY_FORWARD);
				
				
				ArrayList<Bitmap> bullet_frames=new ArrayList<Bitmap>();
				bullet_frames.add(bullet);
				bullet_animation=new Animator("bullet", bullet_frames, Animator.ANIMATION_WAY_FORWARD);
				game_engine.getShipController().setStandardAnimation(bullet_animation);
				game_engine.getShipController().setFadeAnimation(bullet_fade_Animation);
				
				
				
				ArrayList<Bitmap> explosion_frames=new ArrayList<Bitmap>();
				explosion_frames.add(explosion_frame1);
				explosion_frames.add(explosion_frame2);
				explosion_frames.add(explosion_frame3);
				explosion_frames.add(explosion_frame4);
				explosion_frames.add(explosion_frame5);
				explosion_frames.add(explosion_frame6);
				explosion_frames.add(explosion_frame7);
				explosion_frames.add(explosion_frame8);
				explosion_frames.add(explosion_frame9);
				explosion_animation=new Animator("explosion", explosion_frames, Animator.ANIMATION_WAY_FORWARD);
				
				
				ArrayList<Bitmap> power1 = new ArrayList<Bitmap>();
				power1.add(power1frame1);
				powerUp0 = new Animator("powerUp0", power1, Animator.ANIMATION_WAY_FORWARD);
				
				ArrayList<Bitmap> power2 = new ArrayList<Bitmap>();
				power2.add(power2frame1);
				powerUp1 = new Animator("powerUp1", power2, Animator.ANIMATION_WAY_FORWARD);
				
				ArrayList<Bitmap> power3 = new ArrayList<Bitmap>();
				power3.add(power3frame1);
				powerUp2 = new Animator("powerUp2", power3, Animator.ANIMATION_WAY_FORWARD);
				
			}
			
			private void drawBackground(Canvas c)
			{
				if(backgroun_position<screen_height)
				{
					c.drawBitmap(main_background, 0+shakeScreen, backgroun_position, null);
					c.drawBitmap(main_background, 0+shakeScreen, backgroun_position-screen_height, null);
					if(game_engine.getGameState()==AsteroidRushGameMechanicEngine.GAME_STATE_RUNNING)
						backgroun_position+=BACKGROUND_SCROLL_RATE+
						game_engine.getShipController().getShip().speed/3;
				}else{
					backgroun_position-=screen_height;
					c.drawBitmap(main_background, 0+shakeScreen, backgroun_position, null);
					c.drawBitmap(main_background, 0+shakeScreen, backgroun_position-screen_height, null);
					if(game_engine.getGameState()==AsteroidRushGameMechanicEngine.GAME_STATE_RUNNING)
						backgroun_position+=BACKGROUND_SCROLL_RATE+
						game_engine.getShipController().getShip().speed/3;
				}
				
				
			}
			
			/**
			 * Draws Power Ups to given Canvas object
			 * @param c
			 */
			private void drawPowerUps(Canvas c)
			{
				int sayi = powerUp_list.size();
				int i = 0;
				
				try {
					while(i < sayi)
					{
						PowerUp p = powerUp_list.get(i);
						if(p != null)
						{
							c.drawBitmap(p.nextFrame(), p.x, p.y, null);
						}
						i++;
					}
				} catch (IndexOutOfBoundsException e) {
					//e.printStackTrace();
				}
				
			}
			
			/**
			 * Draws Asteroids to given Canvas object.
			 * @param c Canvas to draw Asteroids
			 */
			private void drawAsteroids(Canvas c)
			{
				int sayi=asteroid_list.size();
				int i=0;
				
				
				try {
					while (i<sayi) {
						Asteroid a = asteroid_list.get(i);
						if (a != null) {
							if (a.isDestroyed()) {
								if (a.getDestroyTimer() >= 0) {
									if (a.getDestroyTimer() > 3) {
										c.drawBitmap(a.getStandardAnimation()
												.getFrame(0), a.x, a.y, null);
									}
									c.drawBitmap(explosion_animation
											.getFrame(8 - a.getDestroyTimer()),
											a.x + shakeScreen, a.y, null);
									a.setDestroyTimer(a.getDestroyTimer() - 1);
								} else {
									game_power_up_controller.addNewPowerUp(a.x + values.getAsteroid_width()/2,
											a.y + values.getAsteroid_height()/2);
									asteroid_list.remove(i);
								}
							} else {
								
								c.drawBitmap(a.getStandardAnimation()
										.getFrame(0), a.x + shakeScreen, a.y,
										null);
								
							}
						}
						i++;
					}
				} catch (IndexOutOfBoundsException e) {
					//e.printStackTrace();
				}

			}
			/**
			 * Draws Ship to given Canvas object.
			 * @param c Canvas to draw Ship
			 */
			private void drawShip(Canvas c)
			{
				Ship s=game_engine.getShipController().getShip();
				if(game_engine.getGameState()==AsteroidRushGameMechanicEngine.GAME_STATE_PAUSED
						||
				   game_engine.getGameState()==AsteroidRushGameMechanicEngine.GAME_STATE_RUNNING)
					c.drawBitmap(ship_animation.nextFrame(), s.x, s.y, null);
				
			}
			/**
			 * Draws Bullets to given Canvas object
			 * @param c Canvas to draw Bullets
			 */
			private void drawBullets(Canvas c)
			{
				
				try {
					for(Bullet b : game_engine.getBullets())
					{
						if (b != null) {
							if (b.isDestroyed()) {
								c.drawBitmap(b.getFadeAnimation().getFrame(b.fadeAnimFrame),
										b.x + shakeScreen, b.y, null);
								b.fadeAnimFrame++;
								if(b.fadeAnimFrame > 10)
								{
									game_engine.getBullets().remove(b);
								}
							} else {
								c.drawBitmap(b.getStandardAnimation().getFrame(0), b.x + shakeScreen, b.y,
										null);
							}
						}
					}
				} catch (ConcurrentModificationException e) {
					
					//e.printStackTrace();
				}
				
			}
			
			/**
			 * Draws HUD to given Canvas object.
			 * @param c Canvas to draw HUD
			 */
			private void drawHUD(Canvas c)
			{
				if(game_engine.getGameState()==AsteroidRushGameMechanicEngine.GAME_STATE_PAUSED)
				{
					Ship s=game_engine.getShipController().getShip();
					int markPosX=(int) (s.x-values.getShip_width()/4);
					int markPosY=(int) (s.y+values.getShip_height()*3);
					//c.drawBitmap(continue_mark, markPosX, markPosY, null);
					int touchablearealistsize=touchable_areas.size();
					int counter=0;
					while(counter<touchablearealistsize)
					{
						try
						{
							TouchableArea ta=touchable_areas.get(counter);
							if(ta.isHoldingState())
							{
								c.drawBitmap(ta.getPressedStateBitmap(), ta.getLeft(), ta.getTop(), null);
							}
							else
							{
								c.drawBitmap(ta.getIdleStateBitmap(), ta.getLeft(), ta.getTop(), null);
							}
							counter++;
						}catch(IndexOutOfBoundsException e)
						{
							break;
						}
						
					}
					
					if(markPosX>(2*screen_width)/3)
					{
						continue_game_text_paint.setTextAlign(Align.RIGHT);
						c.drawText(context.getString(R.string.str_hold_to_continue), markPosX+values.getShip_width()*2,
								markPosY+values.getShip_height()+values.getContinue_game_text_size(),
								continue_game_text_paint);
						
						
					}
					else if(markPosX<screen_width/3)
					{
						continue_game_text_paint.setTextAlign(Align.LEFT);
						c.drawText(context.getString(R.string.str_hold_to_continue), markPosX,
								markPosY+values.getShip_height()+values.getContinue_game_text_size(),
								continue_game_text_paint);
						
						
					}
					else
					{
						continue_game_text_paint.setTextAlign(Align.CENTER);
						c.drawText(context.getString(R.string.str_hold_to_continue), markPosX,
								markPosY+values.getShip_height()+values.getContinue_game_text_size(),
								continue_game_text_paint);
						//Paint p=new Paint();
						//p.setColor(Color.GREEN);
						//c.drawRect(0, 0, 300, 300, p);
					}
				}
				if(game_engine.getGameState()==AsteroidRushGameMechanicEngine.GAME_STATE_PAUSED
						||
					game_engine.getGameState()==AsteroidRushGameMechanicEngine.GAME_STATE_RUNNING
						||
					game_engine.getGameState()==AsteroidRushGameMechanicEngine.GAME_STATE_FAILED)
				{
					
					/*upgrade level bar
					
					Paint bar_inside_color=new Paint();
					bar_inside_color.setColor(Color.LTGRAY);
					bar_inside_color.setStyle(Style.FILL);
					Paint bar_fill_color=new Paint();
					bar_fill_color.setColor(Color.BLUE);
					bar_fill_color.setStyle(Style.FILL);
					c.drawRect(screen_width-UPGRADE_LEVEL_BAR_LENGTH*screen_dpi_based_multiplier,
							screen_height/2-(UPGRADE_LEVEL_BAR_HEIGHT/2)*screen_dpi_based_multiplier,
							screen_width,
							screen_height/2+(UPGRADE_LEVEL_BAR_HEIGHT/2)*screen_dpi_based_multiplier,
							bar_inside_color);
					int percentage=(game_engine.upgradeTimer*100/AsteroidRushGameMechanicEngine.UPGRADE_TIME);
					int percentageToLength=(int) ((percentage*UPGRADE_LEVEL_BAR_HEIGHT*screen_dpi_based_multiplier)/100);
					c.drawRect(screen_width-UPGRADE_LEVEL_BAR_LENGTH*screen_dpi_based_multiplier,
							screen_height/2+(UPGRADE_LEVEL_BAR_HEIGHT/2)*screen_dpi_based_multiplier-percentageToLength,
							screen_width,
							screen_height/2+(UPGRADE_LEVEL_BAR_HEIGHT/2)*screen_dpi_based_multiplier,
							bar_fill_color);
					
					//-----------------------------*/
					
					c.drawText(context.getString(R.string.str_score) + " " + game_engine.Score, 0, values.getScore_text_size(), score_text_paint);
					c.drawText(context.getString(R.string.str_distance) + " " + game_engine.Distance, 0, values.getScore_text_size()*2, score_text_paint);
					//c.drawText("FPS: " + FPS , 0, values.getScore_text_size()*3, score_text_paint);
					if(game_engine.onDifficultyAnimation)
					{
						c.drawText(context.getString(R.string.str_level) + " " + game_engine.DifficultyLevel, screen_width/2, screen_height/4, indicate_level_text_paint);
					}
					
					/*
					Paint p=new Paint(indicate_level_text_paint);
					p.setTextSize(indicate_level_text_paint.getTextSize()/2);
					c.drawText(game_engine.UpgradeLevel + " UPGRADE POINTS AVAILABLE", screen_width/2, screen_height - 2*values.getLevel_indicator_text_size(), p);
					c.drawText("Pause Game To Use Them", screen_width/2, screen_height - 1*values.getLevel_indicator_text_size(), p);
					//*/
				}
				if(game_engine.getGameState()==AsteroidRushGameMechanicEngine.GAME_STATE_START_UP)
				{
					c.drawBitmap(tutorial, 0, 0, null);
				}
				if(game_engine.getGameState()==AsteroidRushGameMechanicEngine.GAME_STATE_FAILED)
				{
					
					try {
						int touchablearealistsize=touchable_areas.size();
						int counter=0;
						while(counter<touchablearealistsize)
						{
							TouchableArea ta=touchable_areas.get(counter);
							if(ta.isHoldingState())
							{
								c.drawBitmap(ta.getPressedStateBitmap(), ta.getLeft(), ta.getTop(), null);
							}
							else
							{
								c.drawBitmap(ta.getIdleStateBitmap(), ta.getLeft(), ta.getTop(), null);
							}
							counter++;
						}
					} catch (IndexOutOfBoundsException e) {
						e.printStackTrace();
					}
				}
			}
			/**
			 * Draws Extra graphical effects to given Canvas object.
			 * @param c Canvas to draw Extra graphical effects
			 */
			private void drawExtra(Canvas c)
			{
				try {
					Iterator<Asteroid> i=asteroid_list.iterator();
					while (i.hasNext()) {
						Asteroid a=i.next();
						if(a.isDestroyed())
						{
							if(a.getDestroyTimer()==4)
							{
								particle_controller.createParticleGroupSpeedModified(
										a.x+(values.getAsteroid_width()/2),
										a.y+(values.getAsteroid_height()/2),
										Color.DKGRAY, 15, 0, a.speed);
							}
						}else{
							
						}
						
					}
				} catch (ConcurrentModificationException e) {

					//e.printStackTrace();
				}
				particle_controller.displayParticles(c);
			}
			/**
			 * Changes the info text and makes it shown for a given time
			 * of ticks
			 * @param text Text to be shown
			 * @param time Time limit for it.
			 */
			public void showInfoText(String text, int time, int color)
			{
				this.messageText=text;
				this.timerForMessage=time;
				this.showingMessage=true;
				this.colorOfMessageText=color;
			}
			/**
			 * Draws Info Text for a given time limit.
			 * @param c Canvas to be drawn
			 * @param text String to be written on screen.
			 */
			private void drawInfoText(Canvas c)
			{
				if(showingMessage)
				{
					Paint p=new Paint(indicate_level_text_paint);
					p.setColor(colorOfMessageText);
					c.drawText(messageText, screen_width/2, (3*screen_height)/4, p);
					timerForMessage--;
					if(timerForMessage==0)
					{
						showingMessage=false;
					}
				}
			}


			/**
			 * @return the shakeScreen
			 */
			public int getShakeScreen() {
				return shakeScreen;
			}


			/**
			 * @param shakeScreen the shakeScreen to set
			 */
			public void setShakeScreen(int shakeScreen) {
				this.shakeScreen = shakeScreen;
			}
			
		}
		public class AsteroidRushCollisionChecker extends CollisionChecker
		{

			
			
			public AsteroidRushCollisionChecker(float screen_dpi_multiplier) {
				super(screen_dpi_multiplier);
			}

			@Override
			public void onAsteroidShipCollision(Asteroid asteroid, Ship ship) {
				ship.destroy();
				thread.game_engine.failGame();
				touch_handler.setTouchableAreasOnFail();
				
				statistics.setDeathCounter(statistics.getDeathCounter()+1);
				statistics.setScoreSoFar(statistics.getScoreSoFar()+game_engine.Score);
				statistics.setDistanceSoFar(statistics.getDistanceSoFar()+game_engine.Distance);
				
				
				HighScore score=new HighScore();
				score.setUser(user);
				score.setScore(game_engine.Score);
				score.setDistance(game_engine.Distance);
				score.setCombined(game_engine.Score + game_engine.Distance);
				databaseHandler.openDatabase();
				databaseHandler.addHighScore(score);
				databaseHandler.closeDatabase();
				if(useOnline)
				{
					if(game_web_database_controller.connectionAvailable())
					{
						game_web_database_controller.sendScore();
					}else{
						((Activity)context).runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Toast.makeText(context, context.getString(R.string.str_err_no_connection),
										Toast.LENGTH_SHORT).show();
								
							}
						});
						
					}
				}
				
				if (loseCounter == 0) {
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (interstitialAd.isLoaded()) {
								interstitialAd.show();
							}

						}
					});
					loseCounter=2;
				}
				else
				{
					loseCounter--;
				}
				if(loseCounter==1)
				{
					loadAd();
				}
			}

			@Override
			public void onAsteroidBulletCollision(Asteroid asteroid,
					Bullet bullet) {
				if(bullet.isDestroyed() || asteroid.isDestroyed())
				{
					
				}else{
					game_audio_manager.playHitSound();
					game_graphical_engine.particle_controller.createParticleGroup(
							bullet.x,
							bullet.y,
							Color.BLUE,
							5);
					asteroid.asteroid_health-=bullet.getProjectileDamage();
					bullet.destroy();
					
					statistics.setBulletsHit(statistics.getBulletsHit()+1);
					
					bullet.setProjectileSpeed(-asteroid.speed);
					if(asteroid.asteroid_health<=0)
					{
						asteroid.destroy();
						statistics.setAsteroidsDestroyed(statistics.getAsteroidsDestroyed()+1);
						game_engine.Score+=300+(40*game_engine.DifficultyLevel);
						if(extra_graphics_on)
							game_graphical_engine.shakeScreen=(int) (4*screen_dpi_based_multiplier);
					}
				}
			}

			@Override
			public void onShipPowerUpCollision(Ship ship, PowerUp powerUp) {
				//TODO: PowerUp alýnmasý durumu
				if(powerUp.getPowerUpType() == PowerUp.TYPE_POINTS)
				{
					game_engine.Score += 500;
				}else if(powerUp.getPowerUpType() == PowerUp.TYPE_ADD_DAMAGE)
				{
					game_engine.getShipController().setBulletDamageModifier(
							game_engine.getShipController().getBulletDamageModifier() + 1);
				}else if(powerUp.getPowerUpType() == PowerUp.TYPE_LEVEL_UP)
				{
					int weaponLevel = game_engine.getShipController().getShip().getProjectileType();
					game_engine.getShipController().getShip().setProjectileType(
							Math.min(9, weaponLevel+1));
				}
				powerUp_list.remove(powerUp);
			}
			
			
		}
		
		public class AsteroidRushAudioManager extends AudioManager
		{
			private int buttonSoundID;
			private int fireSoundID;
			private int hitSoundID;
			
			MediaPlayer mediaPlayer;
			
			public AsteroidRushAudioManager(Context context) {
				super();
				if(music_on)
				{
					mediaPlayer=MediaPlayer.create(context, R.raw.music);
				}
			}
			
			public  void loadMusic()
			{
				if(music_on)
				{
					mediaPlayer.setLooping(true);
					try {
						mediaPlayer.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			public void playMusic()
			{
				if(music_on)
				{
					mediaPlayer.start();
				}
			}
			
			public void setMusicSpeed(float rate)
			{
				
			}
			
			public void stopMusic()
			{
				if(music_on)
				{
					mediaPlayer.pause();
					mediaPlayer.seekTo(0);
				}
			}
			
			public void destroyMediaPlayer()
			{
				if(music_on)
				{
					thread.game_audio_manager.mediaPlayer.stop();
					thread.game_audio_manager.mediaPlayer.reset();
					thread.game_audio_manager.mediaPlayer.release();
				}
			}
			
			public void loadButtonSound()
			{
				if(sound_on)
				{
					buttonSoundID=this.getSoundPool().load(context, R.raw.button, 1);
				}
			}
			
			public void playButtonSound()
			{
				if(sound_on)
				{
					this.getSoundPool().play(buttonSoundID,
							1.0f, 1.0f, 1, 0, 1.0f);
				}
			}
			
			public void loadFireSound()
			{
				if(sound_on)
				{
					fireSoundID=this.getSoundPool().load(context, R.raw.fire, 1);
				}
			}
			
			public void playFireSound()
			{
				if(sound_on)
				{
					this.getSoundPool().play(fireSoundID,
							0.7f, 0.7f, 1, 0, 1.0f);
				}
			}
			
			public void loadHitSound()
			{
				if(sound_on)
				{
					hitSoundID=this.getSoundPool().load(context, R.raw.hit, 1);
				}
			}
			
			public void playHitSound()
			{
				if(sound_on)
				{
					this.getSoundPool().play(hitSoundID,
							0.5f, 0.5f, 1, 0, 1.0f);
				}
			}
			
		}
		public class AsteroidRushWebDatabaseController extends WebDatabaseController
		{
			
			public AsteroidRushWebDatabaseController(Context context) {
				super(context);
				
			}

			@Override
			public void sendScore(){
				new Thread(new Runnable() {
					@Override
					public void run() {
						try
						{
							sendScoreThreadWork();
						}catch(Exception e){
							final Exception exception=e;
							((Activity)context).runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									Toast.makeText(context, "Error: " + 
											exception.getLocalizedMessage(),
											Toast.LENGTH_LONG).show();
									
								}
							});
							
							
						}
						
					}
				}).start();
				
			}
			
			private void sendScoreThreadWork() throws Exception
			{
				JSONObject json=new JSONObject();
				
				json.put("user", user);
				json.put("score", game_engine.Score);
				json.put("distance", game_engine.Distance);
				json.put("combined", game_engine.Score + game_engine.Distance);
				json.put("verify", "bhs7bc5GTDVsW821TYft3dGcst");
				json.put("locale", Locale.getDefault().getCountry());
				json.put("time", System.currentTimeMillis());
				 
				HttpClient client=new DefaultHttpClient();
				
				String url="http://www.karacasoft.com/asteroidrush2/webservice/savehighscore.php";
				
				HttpPost request=new HttpPost(url);
				
				request.setEntity(new ByteArrayEntity(json.toString().getBytes("UTF8")));
				
				request.setHeader("json",json.toString());
				//client.execute(request);
				
				
				//*
				HttpResponse response=client.execute(request);
				HttpEntity entity=response.getEntity();
				//Log.d("encoding", "encode");
				InputStream is=entity.getContent();
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				String result=sb.toString();
				Log.d("log_tag", "Result: " + result);
				Log.d("log_tag", response.getStatusLine().toString());
				//*/
				((Activity)context).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(context, "Score Sent!", Toast.LENGTH_SHORT).show();
						
					}
				});
			}
			
			@Override
			public ArrayList<HighScore> getBestScoresList() {
				String result="";
				JSONArray jsonArray=null;
				
				HttpParams httpparams=new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpparams, 30000);
				HttpConnectionParams.setSoTimeout(httpparams, 30000);
				HttpClient client=new DefaultHttpClient(httpparams);
			
				String url="http://www.karacasoft.com/asteroidrush2/webservice/gethighscores.php"; 
				
			
				HttpPost request=new HttpPost(url);
				
				try {
					HttpResponse response=client.execute(request);
					HttpEntity entity=response.getEntity();
					//Log.d("encoding", "encode");
					InputStream is=entity.getContent();
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
					}
					is.close();
					result=sb.toString();
					//Log.d("log_tag", "convert response to string completed!");	
					result=result.substring(2);
					jsonArray=new JSONArray(result);
					
					
					
					
					
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception err)
				{
					 Log.d("hata", "Error converting result "+err.toString());
				}
				
				ArrayList<HighScore> highscoresList=new ArrayList<HighScore>();
				
				int size=jsonArray.length();
				int i=0;
				while(i<size)
				{
					try {
						HighScore h=new HighScore();
						JSONObject json=jsonArray.getJSONObject(i);
						h.setUser(json.getString("user"));
						h.setScore(json.getInt("score"));
						h.setDistance(json.getInt("distance"));
						h.setCombined(json.getInt("combined"));
						highscoresList.add(h);
						
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
					
					i++;
				}
				
				return highscoresList;
			}

			@Override
			public ArrayList<HighScore> getScoreList() {
				
				return null;
			}
			
			
		}
		
		AsteroidRushAsteroidController asteroid_factory;
		
		AsteroidRushCollisionChecker collision_checker;
		
		AsteroidRushGameMechanicEngine game_engine;
		AsteroidRushGameGraphicalEngine game_graphical_engine;
		
		AsteroidRushAudioManager game_audio_manager;
		
		AsteroidRushWebDatabaseController game_web_database_controller;
		
		AsteroidRushPowerUpController game_power_up_controller;
		
		ArrayList<Asteroid> asteroid_list;
		ArrayList<PowerUp> powerUp_list = new ArrayList<PowerUp>();
		
		Typeface type_face;
		
		@Override
		public void run()
		{
			
			
			try {
				initialize();
			} catch (ScreenDensityNotSupportedException e) {
				e.printStackTrace();
				((Activity)context).finish();
				Thread.interrupted();
			}
			asteroid_factory.setAsteroidLimit(20);
			while(gameContinues)
			{
				synchronized (this) {
					while(keepWaiting)
					{
						try {
							wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
				//if(game_engine.gameTickHappened)
				//{
				Canvas c = game_graphical_engine.drawCurrentGameCanvas();

				game_engine.gameTick();
				if (c != null) {

					holder.unlockCanvasAndPost(c);
				}
					//game_engine.gameTickHappened=false;
				//}
				
				
			}
		}
		/**
		 * Returns view's width in pixel.
		 * @return View's Width
		 */
		public int getScreenWidth()
		{
			return screen_width;
		}
		/**
		 * Returns view's height in pixels
		 * @return View's Height
		 */
		public int getScreenHeight()
		{
			return screen_height;
		}
		/**
		 * Game initialize events like setting bitmaps,...
		 * <p>Call this method in a Thread and put a loading icon to
		 *  make a responsive start-up. Don't let the player interrupt loading
		 *  by touching the screen.(In other words, close touch events ;))</p> 
		 * @return true if everything is okay.
		 * @throws ScreenDensityNotSupportedException if screen density is not compatible.
		 */
		public boolean initialize() throws ScreenDensityNotSupportedException
		{
			
			databaseHandler=new DatabaseHandler(context);
			
			databaseHandler.openDatabase();
			statistics=databaseHandler.getStatistics(user);
			databaseHandler.closeDatabase();
			
			type_face=Typeface.createFromAsset(context.getAssets(), "Digital_tech.otf");
			
			asteroid_list=new ArrayList<Asteroid>();
			asteroid_factory=new AsteroidRushAsteroidController(context, asteroid_list);
			game_engine=new AsteroidRushGameMechanicEngine(asteroid_factory, statistics);
			
			game_graphical_engine=new AsteroidRushGameGraphicalEngine(game_engine);
			
			
			game_engine.setGameState(AsteroidRushGameMechanicEngine.GAME_STATE_START_UP);
			
			game_audio_manager=new AsteroidRushAudioManager(context);
			game_audio_manager.loadButtonSound();
			game_audio_manager.loadFireSound();
			game_audio_manager.loadHitSound();
			game_audio_manager.loadMusic();
			
			game_engine.getShipController().setAudio_manager(game_audio_manager);
			game_engine.setAudioManager(game_audio_manager);
			
			
			DisplayMetrics metrics=context.getResources().getDisplayMetrics();
			switch(metrics.densityDpi)
			{
			case DisplayMetrics.DENSITY_LOW:
				screen_dpi_based_multiplier=(float)3/(float)4;
				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				screen_dpi_based_multiplier=1;
				break;
			case DisplayMetrics.DENSITY_HIGH:
				screen_dpi_based_multiplier=(float)3/(float)2;
				break;
			case DisplayMetrics.DENSITY_XHIGH:
				screen_dpi_based_multiplier=2;
				break;
			case DisplayMetrics.DENSITY_XXHIGH:
				screen_dpi_based_multiplier=3;
				break;
			default:
				throw new ScreenDensityNotSupportedException("Ekran çözünürlüðünüz" +
						" desteklenmiyor. Ekran çözünürlüðü sabiti: '" + metrics.densityDpi + "'");
			}
			
			game_graphical_engine.particle_controller.setScreen_dpi_multiplier(screen_dpi_based_multiplier);
			
			collision_checker=new AsteroidRushCollisionChecker(screen_dpi_based_multiplier);
			
			game_engine.setCollisionChecker(collision_checker);
			
			game_engine.setScreen_dpi_based_multiplier(screen_dpi_based_multiplier);
			
			
			//defining dimensions
			values.setAsteroid_width(Math.round(context.getResources()
					.getInteger(R.integer.standard_asteroid_width)*screen_dpi_based_multiplier));
			values.setAsteroid_height(Math.round(context.getResources()
					.getInteger(R.integer.standard_asteroid_height)*screen_dpi_based_multiplier));
			
			values.setShip_width(Math.round(context.getResources()
					.getInteger(R.integer.standard_ship_width)*screen_dpi_based_multiplier));
			values.setShip_height(Math.round(context.getResources()
					.getInteger(R.integer.standard_ship_height)*screen_dpi_based_multiplier));
			
			values.setBullet_width(Math.round(context.getResources()
					.getInteger(R.integer.standard_bullet_width)*screen_dpi_based_multiplier));
			values.setBullet_height(Math.round(context.getResources()
					.getInteger(R.integer.standard_bullet_height)*screen_dpi_based_multiplier));
			
			values.setScore_text_size(Math.round(context.getResources()
					.getInteger(R.integer.standard_score_text_size)*screen_dpi_based_multiplier));
			values.setContinue_game_text_size(Math.round(context.getResources()
					.getInteger(R.integer.standard_score_text_size)*screen_dpi_based_multiplier*2));
			values.setLevel_indicator_text_size(Math.round(context.getResources()
					.getInteger(R.integer.standard_level_indicator_text_size)*screen_dpi_based_multiplier));
			
			values.setExit_button_width(Math.round(context.getResources()
					.getInteger(R.integer.standard_exit_button_width)*screen_dpi_based_multiplier));
			values.setExit_button_height(Math.round(context.getResources()
					.getInteger(R.integer.standard_exit_button_height)*screen_dpi_based_multiplier));
			values.setWeapon_damage_upgrade_button_width(Math.round(context.getResources()
					.getInteger(R.integer.standard_weapon_damage_upgrade_button_width)*screen_dpi_based_multiplier));
			values.setWeapon_damage_upgrade_button_height(Math.round(context.getResources()
					.getInteger(R.integer.standard_weapon_damage_upgrade_button_height)*screen_dpi_based_multiplier));
			values.setWeapon_type_upgrade_button_width(Math.round(context.getResources()
					.getInteger(R.integer.standard_weapon_type_upgrade_button_width)*screen_dpi_based_multiplier));
			values.setWeapon_type_upgrade_button_height(Math.round(context.getResources()
					.getInteger(R.integer.standard_weapon_type_upgrade_button_height)*screen_dpi_based_multiplier));
			values.setRestart_game_button_height(Math.round(context.getResources()
					.getInteger(R.integer.standard_restart_game_button_height)*screen_dpi_based_multiplier));
			values.setRestart_game_button_width(Math.round(context.getResources()
					.getInteger(R.integer.standard_restart_game_button_width)*screen_dpi_based_multiplier));
			values.setShare_on_facebook_button_dimensions(Math.round(context.getResources()
					.getInteger(R.integer.standard_share_on_facebook_button_width_and_height)*screen_dpi_based_multiplier));
			values.setShare_on_twitter_button_dimensions(Math.round(context.getResources()
					.getInteger(R.integer.standard_share_on_twitter_button_width_and_height)*screen_dpi_based_multiplier));
			
			game_power_up_controller = new AsteroidRushPowerUpController();
			game_engine.setPowerUpList(powerUp_list);
			
			game_graphical_engine.setBitmaps();
			game_engine.setValues(values);
			
			game_web_database_controller=new AsteroidRushWebDatabaseController(context);
			
			
			
			return true;
			
		}
		
		private void loadAd()
		{
			//---------------------SETTING ADMOB
			interstitialAd=new InterstitialAd(context);
			interstitialAd.setAdUnitId("ca-app-pub-7145760295966799/4519070667");
			
			final AdRequest adRequest = new AdRequest.Builder().build();
			
			((Activity)context).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					interstitialAd.loadAd(adRequest);
					
				}
			});
			
			//-----------------------------------
		}
		
	}
	//Bitmaps-Sprites
	Bitmap asteroid_type1;
	Bitmap ship;
	Bitmap bullet;
	Bitmap continue_mark;
	Bitmap exit_button;
	Bitmap exit_button_pressed;
	Bitmap restart_game_button;
	Bitmap restart_game_button_pressed;
	
	Bitmap weapon_type_upgrade_button;
	Bitmap weapon_type_upgrade_button_pressed;
	Bitmap weapon_type_upgrade_button_disabled;
	Bitmap weapon_damage_upgrade_button;
	Bitmap weapon_damage_upgrade_button_pressed;
	Bitmap weapon_damage_upgrade_button_disabled;
	
	//Bitmaps-Backgrounds
	Bitmap main_background;
	Bitmap tutorial;
	/**
	 * Touch events will be handled with this class.
	 * @author Triforce
	 *
	 */
	public class AsteroidRushGameTouchControls extends GameTouchControls
	{
		private static final float CONTINUE_BUTTON_OFFSET = 5;
		//private boolean upgradeButtonsSet=false;
		/**
		 * Constructor from superclass
		 */
		public AsteroidRushGameTouchControls() {
			super();
			
		}
		/**
		 * Constructor from superclass
		 * @param touchable_areas Touchable Area List
		 */
		public AsteroidRushGameTouchControls(
				ArrayList<TouchableArea> touchable_areas) {
			super(touchable_areas);
			
		}
		
		@Override
		public void doActions(MotionEvent event) {
			
			float x=event.getX();
			float y=event.getY();
			
			int gameState=thread.game_engine.getGameState();
			
			switch(gameState)
			{
			case AsteroidRushGameMechanicEngine.GAME_STATE_START_UP:
				if((event.getAction() & MotionEvent.ACTION_MASK)==MotionEvent.ACTION_DOWN)
				{
					this.setActivePointerID(event.getPointerId(event.getActionIndex()));
					thread.game_engine.startGame();
				}
				else if((event.getAction() & MotionEvent.ACTION_MASK)==MotionEvent.ACTION_UP)
				{
					//impossible event
				}
				else if((event.getAction() & MotionEvent.ACTION_MASK)==MotionEvent.ACTION_MOVE)
				{
					//no need
				}
				
				break;
			case AsteroidRushGameMechanicEngine.GAME_STATE_RUNNING:
				if(((event.getAction() & MotionEvent.ACTION_MASK) & MotionEvent.ACTION_MASK)==MotionEvent.ACTION_DOWN)
				{
					if(this.getActivePointerID()!=event.getPointerId(event.getActionIndex()))
					{
						thread.game_engine.getShipController().fire(values.getBullet_width(), values.getShip_width(), statistics);
						thread.game_engine.getShipController().setFireLockOn(true);
					}
				}
				else if((event.getAction() & MotionEvent.ACTION_MASK)==MotionEvent.ACTION_POINTER_DOWN)
				{
					if(event.getPointerCount()>1)
					{
						thread.game_engine.getShipController().fire(values.getBullet_width(), values.getShip_width(), statistics);
						thread.game_engine.getShipController().setFireLockOn(true);
					}
				}
				else if((event.getAction() & MotionEvent.ACTION_MASK)==MotionEvent.ACTION_UP)
				{
					if(this.getActivePointerID()==event.getPointerId(event.getActionIndex()))
					{
						thread.game_engine.pause();
						//---------Setting touchable areas-----------------
						setTouchableAreasOnPause();
					}
					thread.game_engine.getShipController().setFireLockOn(false);
					thread.game_engine.getShipController().resetFireTimer();
				}
				else if((event.getAction() & MotionEvent.ACTION_MASK)==MotionEvent.ACTION_POINTER_UP)
				{
					if(this.getActivePointerID()==event.getPointerId(event.getActionIndex()))
					{
						thread.game_engine.pause();
						//---------Setting touchable areas-----------------
						setTouchableAreasOnPause();
					}
					thread.game_engine.getShipController().setFireLockOn(false);
					thread.game_engine.getShipController().resetFireTimer();
					
				}
				else if((event.getAction() & MotionEvent.ACTION_MASK)==MotionEvent.ACTION_MOVE)
				{
					if(this.getActivePointerID()==event.getPointerId(event.getActionIndex()))
						thread.game_engine.getShipController().changePosition(x, y-(3*values.getShip_height()), screen_height);
				}
				
				break;
			case AsteroidRushGameMechanicEngine.GAME_STATE_PAUSED:
				
				this.checkTouchableAreas(event);
				
				break;
			case AsteroidRushGameMechanicEngine.GAME_STATE_FAILED:
				
				this.checkTouchableAreas(event);
				break;
			}
			
			
		}
		
		private void setTouchableAreasOnPause()
		{
			
			//TouchableArea to continue game....
			final TouchableArea touchToContinue=new TouchableArea(thread.game_engine.getShipController().getShip().y+3*values.getShip_height()-CONTINUE_BUTTON_OFFSET*screen_dpi_based_multiplier,
					thread.game_engine.getShipController().getShip().x-CONTINUE_BUTTON_OFFSET*screen_dpi_based_multiplier,
					thread.game_engine.getShipController().getShip().y+4*values.getShip_height()+CONTINUE_BUTTON_OFFSET*screen_dpi_based_multiplier,
					thread.game_engine.getShipController().getShip().x+values.getShip_width()+CONTINUE_BUTTON_OFFSET*screen_dpi_based_multiplier);
			final TouchableArea touchToExit=new TouchableArea(0,
					screen_width-values.getExit_button_width(),
					values.getExit_button_height(),
					screen_width);
			
			
			//setting the continue button
			touchToContinue.setIdleStateBitmap(continue_mark);
			touchToContinue.setPressedStateBitmap(continue_mark);
			
			touchToContinue.setAsteroidRushOnTouchEvent(new AsteroidRushOnTouch() {
				
				@Override
				public void onTouchUp(MotionEvent event) {
					// nothing to do
				}
				
				@Override
				public void onTouchMove(MotionEvent event) {
					// nothing to do
				}
				
				@Override
				public void onTouchDown(MotionEvent event) {
					thread.game_engine.continuePausedGame();
					touchable_areas.remove(touchToContinue);
					touchable_areas.remove(touchToExit);
					//touchable_areas.remove(upgradeWeaponDamage);
					//touchable_areas.remove(upgradeWeaponType);
					setActivePointerID(event.getPointerId(event.getActionIndex()));
				}
			});
			//TouchableArea to exit game
			
			touchToExit.setIdleStateBitmap(exit_button);
			touchToExit.setPressedStateBitmap(exit_button_pressed);
			
			touchToExit.setAsteroidRushOnTouchEvent(new AsteroidRushOnTouch() {
				
				private boolean touchLock=false;
				private float x;
				private float y;
				
				private static final int cancelOffsetX=10;
				private static final int cancelOffsetY=10;
				
				@Override
				public void onTouchUp(MotionEvent event) {
					if(touchLock)
					{
						
						statistics.setScoreSoFar(statistics.getScoreSoFar()+thread.game_engine.Score);
						statistics.setDistanceSoFar(statistics.getDistanceSoFar()+thread.game_engine.Distance);
						
						databaseHandler.openDatabase();
						databaseHandler.updateStatistics(statistics);
						databaseHandler.closeDatabase();
						
						
						((Activity)context).finish();
					
						thread.game_audio_manager.playButtonSound();
						
						thread.game_audio_manager.destroyMediaPlayer();
					}
					
				}
				
				@Override
				public void onTouchMove(MotionEvent event) {
					//basically if cursor out of sight
					//change touchlock false
					//this will not solve the problem completely,
					// Find a better way later...
					
					//Nevermind this is the best way...
					if(Math.abs(event.getX()-x)>cancelOffsetX
							||
							Math.abs(event.getY()-y)>cancelOffsetY)
					{
						touchLock=false;
					}
				}
				
				@Override
				public void onTouchDown(MotionEvent event) {
					touchLock=true;
					x=event.getX();
					y=event.getY();
					
				}
			});
			
			
			/*Setting Upgrade Buttons--------------------
			if(!upgradeButtonsSet)
			{
				setUpgradeButtons();
			}
			//--------------------------------------------*/
			touchable_areas.add(touchToExit);
			touchable_areas.add(touchToContinue);
			
			
		}
		
		public void setTouchableAreasOnFail()
		{
			touchable_areas.clear();
			//upgradeButtonsSet=false;
			
			final TouchableArea restartGame=new TouchableArea(
					screen_height/2-values.getRestart_game_button_height()/2,
					screen_width/2-values.getRestart_game_button_width()/2,
					screen_height/2+values.getRestart_game_button_height()/2,
					screen_width/2+values.getRestart_game_button_width()/2);
			final TouchableArea touchToExit=new TouchableArea(0,
					screen_width-values.getExit_button_width(),
					values.getExit_button_height(),
					screen_width);
			
			restartGame.setIdleStateBitmap(restart_game_button);
			restartGame.setPressedStateBitmap(restart_game_button_pressed);
			
			restartGame.setAsteroidRushOnTouchEvent(new AsteroidRushOnTouch() {
				

				private boolean touchLock=false;
				private float x;
				private float y;
				
				private static final int cancelOffsetX=10;
				private static final int cancelOffsetY=10;
				
				@Override
				public void onTouchUp(MotionEvent event) {
					if(touchLock)
					{
						thread.game_engine.restartGame();
						getTouchableAreas().remove(restartGame);
						thread.game_audio_manager.playButtonSound();
					}
					
				}
				
				@Override
				public void onTouchMove(MotionEvent event) {
					//basically if cursor out of sight
					//change touchlock false
					//this will not solve the problem completely,
					//Find a better way later...

					//Nevermind this is the best way...
					if(Math.abs(event.getX()-x)>cancelOffsetX
							||
							Math.abs(event.getY()-y)>cancelOffsetY)
					{
						touchLock=false;
					}
				}
				
				@Override
				public void onTouchDown(MotionEvent event) {
					touchLock=true;
					x=event.getX();
					y=event.getY();
					
				}
			});
			
			touchToExit.setIdleStateBitmap(exit_button);
			touchToExit.setPressedStateBitmap(exit_button_pressed);
			
			touchToExit.setAsteroidRushOnTouchEvent(new AsteroidRushOnTouch() {
				
				private boolean touchLock=false;
				private float x;
				private float y;
				
				private static final int cancelOffsetX=10;
				private static final int cancelOffsetY=10;
				
				@Override
				public void onTouchUp(MotionEvent event) {
					if(touchLock)
					{
						
						statistics.setScoreSoFar(statistics.getScoreSoFar()+thread.game_engine.Score);
						statistics.setDistanceSoFar(statistics.getDistanceSoFar()+thread.game_engine.Distance);
						
						databaseHandler.openDatabase();
						databaseHandler.updateStatistics(statistics);
						databaseHandler.closeDatabase();
						
						
						((Activity)context).finish();
						thread.game_audio_manager.playButtonSound();
						thread.game_audio_manager.destroyMediaPlayer();
					}
					
				}
				
				@Override
				public void onTouchMove(MotionEvent event) {
					//basically if cursor out of sight
					//change touchlock false
					//this will not solve the problem completely,
					//Find a better way later...

					//Never mind this is the best way...
					if(Math.abs(event.getX()-x)>cancelOffsetX
							||
							Math.abs(event.getY()-y)>cancelOffsetY)
					{
						touchLock=false;
					}
				}
				
				@Override
				public void onTouchDown(MotionEvent event) {
					touchLock=true;
					x=event.getX();
					y=event.getY();
					
				}
			});
			
			touchable_areas.add(touchToExit);
			touchable_areas.add(restartGame);
		}
		/*
		private void setUpgradeButtons()
		{
			
			final TouchableArea upgradeWeaponDamage=new TouchableArea(screen_height-values.getWeapon_damage_upgrade_button_height(),
					0, screen_height,
					values.getWeapon_damage_upgrade_button_width());
			final TouchableArea upgradeWeaponType=new TouchableArea(screen_height-values.getWeapon_type_upgrade_button_height(),
					screen_width-values.getWeapon_type_upgrade_button_width(),
					screen_height,
					screen_width);
			
			upgradeWeaponDamage.setIdleStateBitmap(weapon_damage_upgrade_button);
			upgradeWeaponDamage.setPressedStateBitmap(weapon_damage_upgrade_button_pressed);
			upgradeWeaponDamage.setUnavailableBitmap(weapon_damage_upgrade_button_disabled);
			upgradeWeaponDamage.setAsteroidRushOnTouchEvent(new AsteroidRushOnTouch() {
				private boolean touchLock=false;
				private float x;
				private float y;
				
				private static final int cancelOffsetX=10;
				private static final int cancelOffsetY=10;
				
				@Override
				public void onTouchUp(MotionEvent event) {
					if(touchLock)
					{
						int upgradePoints=thread.game_engine.UpgradeLevel;
						if(upgradePoints>0)
						{
							thread.game_engine.UpgradeLevel--;
							thread.game_engine.getShipController().setBulletDamageModifier(
									thread.game_engine.getShipController().getBulletDamageModifier()+1);
							thread.game_graphical_engine.showInfoText("Upgraded Bullet Damage " +
									thread.game_engine.UpgradeLevel + " Upgrade Points Left", 150, Color.GREEN);
						}else{
							thread.game_graphical_engine.showInfoText("Insufficient Upgrade Points " +
									thread.game_engine.UpgradeLevel + " Upgrade Points Left"
									, 150, Color.RED);
						}
							
					}
				}
				
				@Override
				public void onTouchMove(MotionEvent event) {
					//basically if cursor out of sight
					//change touchlock false
					//this will not solve the problem completely,
					if(Math.abs(event.getX()-x)>cancelOffsetX
							||
							Math.abs(event.getY()-y)>cancelOffsetY)
					{
						touchLock=false;
					}
					
				}
				
				@Override
				public void onTouchDown(MotionEvent event) {
					
					touchLock=true;
					x=event.getX();
					y=event.getY();
					
				}
			});
			upgradeWeaponType.setIdleStateBitmap(weapon_type_upgrade_button);
			upgradeWeaponType.setPressedStateBitmap(weapon_type_upgrade_button_pressed);
			upgradeWeaponType.setUnavailableBitmap(weapon_type_upgrade_button_disabled);
			upgradeWeaponType.setAsteroidRushOnTouchEvent(new AsteroidRushOnTouch() {
				
				private boolean touchLock=false;
				private float x;
				private float y;
				
				private static final int cancelOffsetX=10;
				private static final int cancelOffsetY=10;
				
				@Override
				public void onTouchUp(MotionEvent event) {
					if(touchLock)
					{
						int upgradePoints=thread.game_engine.UpgradeLevel;
						if(upgradePoints>4)
						{
							if(thread.game_engine.getShipController().getShip().getProjectileType()>=2)
							{
								thread.game_graphical_engine.showInfoText("Cannot Upgrade Weapon Type Anymore", 150, Color.RED);
							}else{
								thread.game_engine.UpgradeLevel-=5;
								thread.game_engine.getShipController().getShip().setProjectileType(
										thread.game_engine.getShipController().getShip().getProjectileType()+1);
								thread.game_graphical_engine.showInfoText("Upgraded Weapon Type " +
										thread.game_engine.UpgradeLevel + " Upgrade Points Left", 150, Color.GREEN);
							}
						}else{
							thread.game_graphical_engine.showInfoText("Need 5 points for this. " +
									thread.game_engine.UpgradeLevel + " Upgrade Points Left"
									, 150, Color.RED);
						}
							
					}
				}
				
				@Override
				public void onTouchMove(MotionEvent event) {
					//basically if cursor out of sight
					//change touchlock false
					//this will not solve the problem completely,
					if(Math.abs(event.getX()-x)>cancelOffsetX
							||
							Math.abs(event.getY()-y)>cancelOffsetY)
					{
						touchLock=false;
					}
					
				}
				
				@Override
				public void onTouchDown(MotionEvent event) {
					
					touchLock=true;
					x=event.getX();
					y=event.getY();
					
				}
			});
			
			touchable_areas.add(upgradeWeaponDamage);
			touchable_areas.add(upgradeWeaponType);
			upgradeButtonsSet=true;
		}
		//*/
		
	}
	
	ArrayList<TouchableArea> touchable_areas;
	
	AsteroidRushGameTouchControls touch_handler;
	private boolean keepWaiting=false;
	
	
	/**
	 * Constructor, only with Context
	 * @param context Android Activity Context
	 */
	public GameView(Context context) {
		super(context);
		this.context=context;
		this.holder=getHolder();
		holder.addCallback(this);
		this.thread=new AsteroidRushThread();
		touchable_areas=new ArrayList<TouchableArea>();
		touch_handler=new AsteroidRushGameTouchControls(touchable_areas);
		values=new ValueHolder();
		setDisplayVariables();
		thread.start();
		
	}
	/**
	 * Constructor with context and attribute set
	 * @param context Android Activity Context
	 * @param attrs Attributes
	 */
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		this.holder=getHolder();
		holder.addCallback(this);
		this.thread=new AsteroidRushThread();
		touchable_areas=new ArrayList<TouchableArea>();
		touch_handler=new AsteroidRushGameTouchControls();
		values=new ValueHolder();
		setDisplayVariables();
		thread.start();
		
	}
	/**
	 * Constructor with context, attributes and defStyle
	 * @param context Android Activity Context
	 * @param attrs Attributes
	 * @param defStyle defStyle
	 */
	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		screen_width=width;
		screen_height=height;
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		screen_width=this.getWidth();
		screen_height=this.getHeight();
		synchronized (thread) {
			thread.notify();
			keepWaiting=false;
		}
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		keepWaiting=true;
		
	}
	/**
	 * Returns game thread
	 * @return Game Main Thread
	 */
	public AsteroidRushThread getThread()
	{
		return thread;
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		
		touch_handler.doActions(e);
		
		invalidate();
		
		return true;
	}
	
	private void setDisplayVariables()
	{
		DisplayMetrics dm=new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		screen_width=dm.widthPixels;
		screen_height=dm.heightPixels;
		
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
	public boolean getUseOnline() {
		return useOnline;
	}
	public void setUseOnline(boolean useOnline) {
		this.useOnline = useOnline;
	}
}
