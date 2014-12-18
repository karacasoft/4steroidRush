package com.karacasoft.asteroidrush2.models;

import android.graphics.Bitmap;

import com.karacasoft.asteroidrush2.utils.Animator;

public class PowerUp {
	
	public static final int TYPE_POINTS = 0;
	public static final int TYPE_ADD_DAMAGE = 1;
	public static final int TYPE_LEVEL_UP = 2;
	
	private int mPowerUpType = 0;
	
	public float x = 0;
	public float y = 0;
	
	public float speedY = -15;
	
	private int animationFrameIndex = 0;
	
	private Animator standardAnimation;
	
	public int getPowerUpType() {
		return mPowerUpType;
	}

	public void setPowerUpType(int powerUpType) {
		this.mPowerUpType = powerUpType;
	}

	public Bitmap nextFrame()
	{
		Bitmap bmp = standardAnimation.getFrame(animationFrameIndex); 
		if(animationFrameIndex < standardAnimation.getFrames().size() - 1)
		{
			animationFrameIndex++;
		}else{
			animationFrameIndex = 0;
		}
		return bmp;
	}

	public Animator getStandardAnimation() {
		return standardAnimation;
	}

	public void setStandardAnimation(Animator standardAnimation) {
		this.standardAnimation = standardAnimation;
	}
	
	
	
}
