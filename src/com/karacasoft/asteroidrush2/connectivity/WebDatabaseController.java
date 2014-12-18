package com.karacasoft.asteroidrush2.connectivity;

import java.util.ArrayList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.karacasoft.asteroidrush2.models.HighScore;

public abstract class WebDatabaseController {

	Context context;
	
	public WebDatabaseController(Context context)
	{
		this.context=context;
	}
	
	public abstract void sendScore();
	public abstract ArrayList<HighScore> getBestScoresList();
	public abstract ArrayList<HighScore> getScoreList();
	
	public boolean connectionAvailable()
	{
		ConnectivityManager conManager=(ConnectivityManager) context.getSystemService(
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
