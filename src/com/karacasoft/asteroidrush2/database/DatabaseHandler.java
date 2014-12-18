package com.karacasoft.asteroidrush2.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.karacasoft.asteroidrush2.models.HighScore;
import com.karacasoft.asteroidrush2.models.UserStatistics;

public class DatabaseHandler {

	SQLiteDatabase db;
	DatabaseHelper helper;
	
	public DatabaseHandler(Context context) {
		helper=new DatabaseHelper(context);
	}
	
	public void openDatabase()
	{
		db=helper.getWritableDatabase();
	}
	
	public void closeDatabase()
	{
		db.close();
	}
	
	public void addHighScore(HighScore highscore)
	{
		
		ContentValues values=new ContentValues();
		//values.put(DatabaseHelper.HIGHSCORES_KEY_ID, 1);
		values.put(DatabaseHelper.HIGHSCORES_KEY_NAME, highscore.getUser());
		values.put(DatabaseHelper.HIGHSCORES_KEY_SCORE, highscore.getScore());
		values.put(DatabaseHelper.HIGHSCORES_KEY_DISTANCE, highscore.getDistance());
		values.put(DatabaseHelper.HIGHSCORES_KEY_COMBINED, highscore.getCombined());
				
		db.insert(DatabaseHelper.HIGHSCORES_TABLE_NAME, null, values);
		
	}
	
	public void addStatistics(UserStatistics statistics)
	{
		ContentValues values=new ContentValues();
		values.put(DatabaseHelper.STATISTICS_KEY_USER, statistics.getUser());
		values.put(DatabaseHelper.STATISTICS_KEY_SCORE_GAINED, statistics.getScoreSoFar());
		values.put(DatabaseHelper.STATISTICS_KEY_DISTANCE, statistics.getDistanceSoFar());
		values.put(DatabaseHelper.STATISTICS_KEY_ASTEROIDS_DESTROYED, statistics.getAsteroidsDestroyed());
		values.put(DatabaseHelper.STATISTICS_KEY_BULLETS_FIRED, statistics.getBulletsFired());
		values.put(DatabaseHelper.STATISTICS_KEY_BULLETS_HIT, statistics.getBulletsHit());
		values.put(DatabaseHelper.STATISTICS_KEY_BULLETS_MISSED, statistics.getBulletsMissed());
		values.put(DatabaseHelper.STATISTICS_KEY_TIMES_DEAD, statistics.getDeathCounter());
		
		db.insert(DatabaseHelper.STATISTICS_TABLE_NAME, null, values);
		
	}
	
	public HighScore getHighscore(int position) throws Exception
	{
		
		Cursor cursor=db.query(DatabaseHelper.HIGHSCORES_TABLE_NAME,
				new String[]{
				DatabaseHelper.HIGHSCORES_KEY_ID,
				DatabaseHelper.HIGHSCORES_KEY_NAME,
				DatabaseHelper.HIGHSCORES_KEY_SCORE,
				DatabaseHelper.HIGHSCORES_KEY_DISTANCE,
				DatabaseHelper.HIGHSCORES_KEY_COMBINED
				}, 
				null,
				null, null, null, DatabaseHelper.HIGHSCORES_KEY_COMBINED + " DESC");
		
		if(cursor!=null && cursor.getCount()>position)
		{
			cursor.moveToPosition(position);
			
			HighScore score=new HighScore();
			score.setUser(cursor.getString(1));
			score.setScore(cursor.getInt(2));
			score.setDistance(cursor.getInt(3));
			score.setCombined(cursor.getInt(4));
			
			return score;
		}else
		{
			
			throw new Exception("Requested Data Not Found. Highscore: " + position);
		}
		
		
		
	}
	
	public UserStatistics getStatistics(String user)
	{
		boolean userFound=false;
		ArrayList<String> users=this.getUserList();
		for(String u : users)
		{
			if(u.equals(user))
			{
				userFound=true;
				break;
			}
		}
		UserStatistics statistics=new UserStatistics();
		Cursor cursor = null;
		if (userFound) {
			try {
				cursor = db
						.query(DatabaseHelper.STATISTICS_TABLE_NAME,
								new String[] {
										DatabaseHelper.STATISTICS_KEY_USER,
										DatabaseHelper.STATISTICS_KEY_SCORE_GAINED,
										DatabaseHelper.STATISTICS_KEY_DISTANCE,
										DatabaseHelper.STATISTICS_KEY_ASTEROIDS_DESTROYED,
										DatabaseHelper.STATISTICS_KEY_BULLETS_FIRED,
										DatabaseHelper.STATISTICS_KEY_BULLETS_HIT,
										DatabaseHelper.STATISTICS_KEY_BULLETS_MISSED,
										DatabaseHelper.STATISTICS_KEY_TIMES_DEAD },
										DatabaseHelper.STATISTICS_KEY_USER + "=?", 
										new String[]{user}, null, null, null);
			} catch (SQLiteException e) {

				e.printStackTrace();
			}

			cursor.moveToFirst();

			statistics.setUser(cursor.getString(0));
			statistics.setScoreSoFar(cursor.getInt(1));
			statistics.setDistanceSoFar(cursor.getInt(2));
			statistics.setAsteroidsDestroyed(cursor.getInt(3));
			statistics.setBulletsFired(cursor.getInt(4));
			statistics.setBulletsHit(cursor.getInt(5));
			statistics.setBulletsMissed(cursor.getInt(6));
			statistics.setDeathCounter(cursor.getInt(7));

			return statistics;
		} else {

			statistics.setUser(user);
			statistics.setScoreSoFar(0);
			statistics.setDistanceSoFar(0);
			statistics.setAsteroidsDestroyed(0);
			statistics.setBulletsFired(0);
			statistics.setBulletsHit(0);
			statistics.setBulletsMissed(0);
			statistics.setDeathCounter(0);
			addStatistics(statistics);

			return statistics;
		}
	}
	
	public void updateStatistics(UserStatistics statistics)
	{
		ContentValues values=new ContentValues();
		values.put(DatabaseHelper.STATISTICS_KEY_USER, statistics.getUser());
		values.put(DatabaseHelper.STATISTICS_KEY_SCORE_GAINED, statistics.getScoreSoFar());
		values.put(DatabaseHelper.STATISTICS_KEY_DISTANCE, statistics.getDistanceSoFar());
		values.put(DatabaseHelper.STATISTICS_KEY_ASTEROIDS_DESTROYED, statistics.getAsteroidsDestroyed());
		values.put(DatabaseHelper.STATISTICS_KEY_BULLETS_FIRED, statistics.getBulletsFired());
		values.put(DatabaseHelper.STATISTICS_KEY_BULLETS_HIT, statistics.getBulletsHit());
		values.put(DatabaseHelper.STATISTICS_KEY_BULLETS_MISSED, statistics.getBulletsMissed());
		values.put(DatabaseHelper.STATISTICS_KEY_TIMES_DEAD, statistics.getDeathCounter());
		
		db.update(DatabaseHelper.STATISTICS_TABLE_NAME, values,
				DatabaseHelper.STATISTICS_KEY_USER + "=?", 
				new String[]{statistics.getUser()});
		
		
	}
	
	public ArrayList<String> getUserList()
	{
		
		Cursor cursor=db.query(DatabaseHelper.STATISTICS_TABLE_NAME,
				new String[]{
				DatabaseHelper.STATISTICS_KEY_USER
				},
				null, null, null, null, null);
		cursor.moveToFirst();
		
		ArrayList<String> users=new ArrayList<String>();
		
		while(!cursor.isAfterLast())
		{
			users.add(cursor.getString(0));
			cursor.moveToNext();
		}
		
		return users;
	}
	
}
