package com.karacasoft.asteroidrush2.utils;

import android.media.SoundPool;

public abstract class AudioManager {
	private SoundPool soundPool;
	
	public AudioManager()
	{
		setSoundPool(new SoundPool(5, android.media.AudioManager.STREAM_MUSIC, 0));
		
	}
	
	/**
	 * @return the soundPool
	 */
	public SoundPool getSoundPool() {
		return soundPool;
	}

	/**
	 * @param soundPool the soundPool to set
	 */
	public void setSoundPool(SoundPool soundPool) {
		this.soundPool = soundPool;
	}
	
}
