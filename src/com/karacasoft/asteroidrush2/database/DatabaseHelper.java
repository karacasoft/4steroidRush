package com.karacasoft.asteroidrush2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION=11;
	public static final String DATABASE_NAME="AsteroidRushDatabase";
	
	//tables
	public static final String HIGHSCORES_TABLE_NAME="highScoresAsteroidRush";
	public static final String STATISTICS_TABLE_NAME="statisticsAsteroidRush";
	
	public static final String HIGHSCORES_KEY_ID="_id";
	public static final String HIGHSCORES_KEY_NAME="name";
	public static final String HIGHSCORES_KEY_SCORE="score";
	public static final String HIGHSCORES_KEY_DISTANCE="distance";
	public static final String HIGHSCORES_KEY_COMBINED="combined";
	
	public static final String STATISTICS_KEY_USER="user";
	public static final String STATISTICS_KEY_SCORE_GAINED="scoreGained";
	public static final String STATISTICS_KEY_DISTANCE="distanceTravelled";
	public static final String STATISTICS_KEY_ASTEROIDS_DESTROYED="asteroidsDestroyed";
	public static final String STATISTICS_KEY_BULLETS_FIRED="bulletsFired";
	public static final String STATISTICS_KEY_BULLETS_HIT="bulletsHit";
	public static final String STATISTICS_KEY_BULLETS_MISSED="bulletsMissed";
	public static final String STATISTICS_KEY_TIMES_DEAD="deadCounter";
	
	
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		String createTableQueryHighScores="CREATE TABLE " + HIGHSCORES_TABLE_NAME +
				" (" +
				HIGHSCORES_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				HIGHSCORES_KEY_NAME + " TEXT," +
				HIGHSCORES_KEY_SCORE + " INTEGER," +
				HIGHSCORES_KEY_DISTANCE + " INTEGER," +
				HIGHSCORES_KEY_COMBINED + " INTEGER" +
				");";
		String createTableQueryStatistics="CREATE TABLE " + STATISTICS_TABLE_NAME +
				" (" +
				STATISTICS_KEY_USER + " TEXT," +
				STATISTICS_KEY_SCORE_GAINED + " INTEGER," +
				STATISTICS_KEY_DISTANCE + " INTEGER," +
				STATISTICS_KEY_ASTEROIDS_DESTROYED + " INTEGER," +
				STATISTICS_KEY_BULLETS_FIRED + " INTEGER," +
				STATISTICS_KEY_BULLETS_HIT + " INTEGER," +
				STATISTICS_KEY_BULLETS_MISSED + " INTEGER," +
				STATISTICS_KEY_TIMES_DEAD + " INTEGER" +
				");";
		arg0.execSQL(createTableQueryHighScores);
		arg0.execSQL(createTableQueryStatistics);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + HIGHSCORES_TABLE_NAME + ";");
		db.execSQL("DROP TABLE IF EXISTS " + STATISTICS_TABLE_NAME + ";");
		
		onCreate(db);

	}

}
