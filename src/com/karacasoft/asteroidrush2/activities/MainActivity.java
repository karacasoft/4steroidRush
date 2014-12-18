package com.karacasoft.asteroidrush2.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karacasoft.asteroidrush2.R;
import com.karacasoft.asteroidrush2.database.DatabaseHandler;
import com.karacasoft.asteroidrush2.models.HighScore;
import com.karacasoft.asteroidrush2.models.UserStatistics;
import com.karacasoft.asteroidrush2.utils.HighScoresListAdapter;

public class MainActivity extends Activity {
	
	private static final int SCREEN_MAIN=0;
	private static final int SCREEN_HIGH_SCORES=1;
	private static final int SCREEN_STATISTICS=2;
	private static final int SCREEN_GLOBAL_HIGH_SCORES=3;
	
	private int currentScreen;
	
	ListView highScoresListView;
	ListView statisticsList;
	ListView userList;
	
	EditText editTextNickName;
	
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	
	CheckBox useFacebook;
	CheckBox extraGraphics;
	CheckBox useOnline;
	CheckBox musicOn;
	CheckBox soundOn;
	
	private OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.startGameButton)
			{
				Intent i=new Intent(getApplicationContext(), com.karacasoft.asteroidrush2.activities.GameActivity.class);
				
				i.putExtra("useOnline", useOnline.isChecked());
				i.putExtra("music_on", musicOn.isChecked());
				i.putExtra("sound_on", soundOn.isChecked());
				i.putExtra("nickname", editTextNickName.getText().toString());
				sharedPrefEditor.putString("AsteroidRushLastUserSharedPrefKey", 
						editTextNickName.getText().toString());
				
				sharedPrefEditor.putBoolean("AsteroidRushLastUseOnlineSharedPrefKey", useOnline.isChecked());
				sharedPrefEditor.putBoolean("AsteroidRushLastMusicOnSharedPrefKey", musicOn.isChecked());
				sharedPrefEditor.putBoolean("AsteroidRushLastSoundOnSharedPrefKey", soundOn.isChecked());
				
				sharedPrefEditor.commit();
				
				startActivity(i);
			}else if(v.getId()==R.id.highScoresButton)
			{
				initializeHighScoreList();
				changeScreen(SCREEN_HIGH_SCORES);
			}else if(v.getId()==R.id.statisticsButton)
			{
				initializeStatisticsList();
				changeScreen(SCREEN_STATISTICS);
			}else if(v.getId()==R.id.globalHighScoresButton)
			{
				if(connectionAvailable())
				{
					initializeGlobalHighScoreList();
					changeScreen(SCREEN_GLOBAL_HIGH_SCORES);
				}else{
					Toast.makeText(getApplicationContext(), "No active connection available.", Toast.LENGTH_LONG).show();
				}
			}
			
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		currentScreen=SCREEN_MAIN;
		
		sharedPref=getSharedPreferences("AsteroidRushSharedPreferencesKey", MODE_PRIVATE);
		sharedPrefEditor=sharedPref.edit();
		
		useOnline=(CheckBox) findViewById(R.id.checkBox1);
		musicOn=(CheckBox) findViewById(R.id.music_on);
		soundOn=(CheckBox) findViewById(R.id.sound_on);
		
		
		String last_user=sharedPref.getString("AsteroidRushLastUserSharedPrefKey", "");
		
		editTextNickName=(EditText)findViewById(R.id.editTextNickName);
		
		editTextNickName.setText(last_user);
		
		
		boolean last_use_online=sharedPref.getBoolean("AsteroidRushLastUseOnlineSharedPrefKey", true);
		boolean last_music_on=sharedPref.getBoolean("AsteroidRushLastMusicOnSharedPrefKey", false);
		boolean last_sound_on=sharedPref.getBoolean("AsteroidRushLastSoundOnSharedPrefKey", true);
		
		useOnline.setChecked(last_use_online);
		musicOn.setChecked(last_music_on);
		soundOn.setChecked(last_sound_on);
		
		Button startGameButton=(Button)findViewById(R.id.startGameButton);
		Button highScoresButton=(Button)findViewById(R.id.highScoresButton);
		Button statisticsButton=(Button)findViewById(R.id.statisticsButton);
		Button globalHighScoresButton=(Button)findViewById(R.id.globalHighScoresButton);
		
		
		/*
		useFacebook=(CheckBox)findViewById(R.id.chkBoxUseFacebook);
		useFacebook.setEnabled(false);
		
		extraGraphics=(CheckBox)findViewById(R.id.chkboxExtraGraphics);
		//*/
		startGameButton.setOnClickListener(onClickListener);
		highScoresButton.setOnClickListener(onClickListener);
		statisticsButton.setOnClickListener(onClickListener);
		globalHighScoresButton.setOnClickListener(onClickListener);
		
		TextView textVersionInfo=(TextView)findViewById(R.id.text_version_info);
		try {
			textVersionInfo.setText("Asteroid Rush version " + this.getPackageManager().
					getPackageInfo(getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			//e.printStackTrace();
		}
		
	}
	
	private void initializeHighScoreList()
	{
		DatabaseHandler dbHandler=new DatabaseHandler(getApplicationContext());
		dbHandler.openDatabase();
		
		highScoresListView=new ListView(this);
		highScoresListView.setBackgroundResource(R.drawable.background);
		ArrayList<HighScore> highScoresList=new ArrayList<HighScore>();
		for(int i=0; i<10; i++)
		{
			try {
				highScoresList.add(dbHandler.getHighscore(i));
			} catch (Exception e) {
				break;
			}
		}
		
		HighScoresListAdapter highScoresListAdapter=new HighScoresListAdapter(
				this, R.layout.high_scores_list_item, highScoresList);
		
		highScoresListView.setAdapter(highScoresListAdapter);
		dbHandler.closeDatabase();
		
	}
	ArrayList<HighScore> globalHighScoresList;
	private void initializeGlobalHighScoreList()
	{
		
		
		
		highScoresListView=new ListView(this);
		highScoresListView.setBackgroundResource(R.drawable.background);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					globalHighScoresList=getGlobalHighScores();
					onEndLoadingGlobalHighScores();
				} catch (Exception e) {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "Network Error",
									Toast.LENGTH_LONG).show();
							
						}
					});
					e.printStackTrace();
				}
				
			}
		}).start();
		
		
		
		
	}
	private void onEndLoadingGlobalHighScores()
	{
		final HighScoresListAdapter highScoresListAdapter=new HighScoresListAdapter(
				this, R.layout.high_scores_list_item, globalHighScoresList);
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				highScoresListView.setAdapter(highScoresListAdapter);
				((HighScoresListAdapter)highScoresListView.getAdapter()).notifyDataSetChanged();
				
			}
		});
	}
	private void initializeStatisticsList()
	{
		final DatabaseHandler dbHandler=new DatabaseHandler(getApplicationContext());
		dbHandler.openDatabase();
		
		userList=new ListView(this);
		userList.setBackgroundResource(R.drawable.background);
		ArrayList<String> users=dbHandler.getUserList();
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.users_list_item, users);
		
		
		userList.setAdapter(adapter);
		final LayoutInflater inflater=LayoutInflater.from(this);
		
		final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		
		userList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String user=(String)arg0.getAdapter().getItem(arg2);
				
				final LinearLayout layout=(LinearLayout)
						inflater.inflate(R.layout.activity_show_statistics, null);
				
				final TextView score_so_far=(TextView)layout.findViewById(R.id.text_score_so_far);
				final TextView distance_so_far=(TextView)layout.findViewById(R.id.text_distance_so_far);
				final TextView asteroids_destroyed=(TextView)layout.findViewById(R.id.text_asteroids_destroyed);
				final TextView bullets_fired=(TextView)layout.findViewById(R.id.text_bullets_fired);
				final TextView bullets_hit=(TextView)layout.findViewById(R.id.text_bullets_hit);
				final TextView bullets_missed=(TextView)layout.findViewById(R.id.text_bullets_missed);
				final TextView death_counter=(TextView)layout.findViewById(R.id.text_death_counter);
				
				
				dbHandler.openDatabase();
				UserStatistics statistics=dbHandler.getStatistics(user);
				dbHandler.closeDatabase();
				//Log.d("AsteroidRushDebug", Locale.getDefault().getDisplayLanguage());
				if (Locale.getDefault().getDisplayLanguage().equals("Türkçe")) {
					dialog.setTitle(user + " Ýstatistikleri");
					score_so_far.setText("Kazanýlan Toplam Puan: "
							+ statistics.getScoreSoFar());
					distance_so_far.setText("Gidilen Toplam Yol: "
							+ statistics.getDistanceSoFar());
					asteroids_destroyed.setText(statistics
							.getAsteroidsDestroyed() + " Asteroid Yok Etti");
					bullets_fired.setText(statistics.getBulletsFired()
							+ " Mermi Ateþ Etti");
					bullets_hit.setText(statistics.getBulletsHit()
							+ " Mermi Ýsabet Ettirdi");
					bullets_missed.setText(statistics.getBulletsMissed()
							+ " Mermi Kaçýrdý");
					death_counter.setText(
							statistics.getDeathCounter() + " Kez Öldü");
				} else {
					dialog.setTitle(user + " Statistics");
					score_so_far.setText("Total Score Gained: "
							+ statistics.getScoreSoFar());
					distance_so_far.setText("Total Distance Travelled: "
							+ statistics.getDistanceSoFar());
					asteroids_destroyed.setText(statistics
							.getAsteroidsDestroyed() + " Asteroids Destroyed");
					bullets_fired.setText(statistics.getBulletsFired()
							+ " Bullets Fired");
					bullets_hit.setText(statistics.getBulletsHit()
							+ " Bullets Hit");
					bullets_missed.setText(statistics.getBulletsMissed()
							+ " Bullets Missed");
					death_counter.setText("Died "
							+ statistics.getDeathCounter() + " Times");
				}
				dialog.setView(layout);
				dialog.show();
				
			}
		});
		
		dbHandler.closeDatabase();
	}
	private void changeScreen(int screen)
	{
		if(screen==SCREEN_MAIN)
		{
			setContentView(R.layout.activity_main);
			
			
			String last_user=sharedPref.getString("AsteroidRushLastUserSharedPrefKey", "");
			
			editTextNickName=(EditText)findViewById(R.id.editTextNickName);
			
			editTextNickName.setText(last_user);
			
			Button startGameButton=(Button)findViewById(R.id.startGameButton);
			Button highScoresButton=(Button)findViewById(R.id.highScoresButton);
			Button statisticsButton=(Button)findViewById(R.id.statisticsButton);
			Button globalHighScoresButton=(Button)findViewById(R.id.globalHighScoresButton);
			
			
			startGameButton.setOnClickListener(onClickListener);
			highScoresButton.setOnClickListener(onClickListener);
			statisticsButton.setOnClickListener(onClickListener);
			globalHighScoresButton.setOnClickListener(onClickListener);
			
			useOnline=(CheckBox) findViewById(R.id.checkBox1);
			musicOn=(CheckBox) findViewById(R.id.music_on);
			soundOn=(CheckBox) findViewById(R.id.sound_on);
			
			
			boolean last_use_online=sharedPref.getBoolean("AsteroidRushLastUseOnlineSharedPrefKey", true);
			boolean last_music_on=sharedPref.getBoolean("AsteroidRushLastMusicOnSharedPrefKey", false);
			boolean last_sound_on=sharedPref.getBoolean("AsteroidRushLastSoundOnSharedPrefKey", true);
			
			useOnline.setChecked(last_use_online);
			musicOn.setChecked(last_music_on);
			soundOn.setChecked(last_sound_on);
			
			
			
			TextView textVersionInfo=(TextView)findViewById(R.id.text_version_info);
			try {
				textVersionInfo.setText("Asteroid Rush version " + this.getPackageManager().
						getPackageInfo(getPackageName(), 0).versionName);
			} catch (NameNotFoundException e) {
				//e.printStackTrace();
			}
			
			currentScreen=SCREEN_MAIN;
		}
		else if(screen==SCREEN_HIGH_SCORES)
		{
			setContentView(highScoresListView);
			((HighScoresListAdapter)highScoresListView.getAdapter()).notifyDataSetChanged();
			currentScreen=SCREEN_HIGH_SCORES;
		}
		else if(screen==SCREEN_STATISTICS)
		{
			
			setContentView(userList);
			((ArrayAdapter<?>)userList.getAdapter()).notifyDataSetChanged();
			currentScreen=SCREEN_STATISTICS;
		}
		else if(screen==SCREEN_GLOBAL_HIGH_SCORES)
		{
			setContentView(highScoresListView);
			//((HighScoresListAdapter)highScoresListView.getAdapter()).notifyDataSetChanged();
			currentScreen=SCREEN_GLOBAL_HIGH_SCORES;
		}
	}
	
	@Override
	public void onBackPressed() {
		
		if(currentScreen==SCREEN_MAIN)
		{
			super.onBackPressed();
		}else if(currentScreen==SCREEN_HIGH_SCORES)
		{
			changeScreen(SCREEN_MAIN);
		}else if(currentScreen==SCREEN_STATISTICS)
		{
			changeScreen(SCREEN_MAIN);
		}else if(currentScreen==SCREEN_GLOBAL_HIGH_SCORES)
		{
			changeScreen(SCREEN_MAIN);
		}
		
	}
	
	private ArrayList<HighScore> getGlobalHighScores()
	{
		String result="";
		JSONArray jsonArray=null;
		
		
		HttpClient client=new DefaultHttpClient();
		
	
		String url="http://www.karacasoft.com/asteroidrush2/webservice/getshighscores.php"; 
//		String url="http://www.facebook.com/";
	
		HttpPost request=new HttpPost(url);
//		request.addHeader("content-type", "application/json");
		
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
			Log.d("log_tag", result);
//			Log.d("log_tag", "convert response to string completed!");	
//			result=result.substring(2);
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
//		if (jsonArray != null) {
			int size = jsonArray.length();
			int i = 0;
			while (i < size) {
				try {
					HighScore h = new HighScore();
					JSONObject json = jsonArray.getJSONObject(i);
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
		/*}
		else
		{
			throw new Exception("Network Error");
		}//*/
	}
	public boolean connectionAvailable()
	{
		ConnectivityManager conManager=(ConnectivityManager) this.getSystemService(
				Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo=conManager.getActiveNetworkInfo();
		if(activeNetworkInfo!=null)
		{
			return activeNetworkInfo.isConnected();
		}else{
			return false;
		}
	}
}
