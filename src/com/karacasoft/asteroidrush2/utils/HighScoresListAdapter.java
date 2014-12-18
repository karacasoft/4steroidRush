package com.karacasoft.asteroidrush2.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.karacasoft.asteroidrush2.R;
import com.karacasoft.asteroidrush2.models.HighScore;

public class HighScoresListAdapter extends ArrayAdapter<HighScore> {

	
	public HighScoresListAdapter(Context context, int resource) {
		super(context, resource);
		this.context=context;
	}

	private Context context;
	private ArrayList<HighScore> highscores_list;
	
	public HighScoresListAdapter(Context context, int textViewResourceId, ArrayList<HighScore> items)
	{
		super(context, textViewResourceId,items);
		
		this.context=context;
		this.highscores_list=items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v=convertView;
		if(v==null)
		{
			LayoutInflater li=((Activity)context).getLayoutInflater();
			v=li.inflate(R.layout.high_scores_list_item, null);
		}
		
		HighScore hs=this.highscores_list.get(position);
		
		
		if(hs!=null)
		{
			TextView tv_name=(TextView) v.findViewById(R.id.text_name);
			TextView tv_score=(TextView) v.findViewById(R.id.text_score);
			TextView tv_distance=(TextView) v.findViewById(R.id.text_distance);
			TextView tv_combined=(TextView) v.findViewById(R.id.text_combined);
			if(tv_name!=null)
			{
				tv_name.setText((position+1) + ": " + hs.getUser());
			}
			if(tv_score!=null)
			{
				tv_score.setText(context.getString(R.string.str_score_on_list) + " " + String.valueOf(hs.getScore()));
			}
			if(tv_distance!=null)
			{
				tv_distance.setText(context.getString(R.string.str_distance_on_list) + " " +String.valueOf(hs.getDistance()));
			}
			if(tv_combined!=null)
			{
				tv_combined.setText(context.getString(R.string.str_combined_score) + " " +
						String.valueOf(hs.getDistance()+hs.getScore()));
			}
		}
		
		return v;
	}
	
}
