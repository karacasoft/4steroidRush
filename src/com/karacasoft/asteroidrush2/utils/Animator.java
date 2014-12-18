package com.karacasoft.asteroidrush2.utils;

import java.util.ArrayList;

import android.graphics.Bitmap;
/**
 * Class for displaying animations. Holds bitmaps as frames and gives them out in
 * a pre-set order.
 * 
 * @author Triforce
 *
 */
public class Animator {
	/**
	 * Listener for animation.
	 * @author Triforce
	 *
	 */
	public interface AnimatorEventListener
	{
		
		
		/**
		 * Method that will be called on Animation start.
		 * @return true if handled.
		 */
		public boolean animationOnStart();
		/**
		 * Method that will be called on Animation end.
		 * @return true if handled
		 */
		public boolean animationOnEnd();
		/**
		 * Method that will be called on any kind of frame change of the
		 *  Animation.
		 * @return true if handled
		 */
		public boolean animationOnFrameChange();
		
	}
	
	private AnimatorEventListener listener;
	
	public static final int ANIMATION_WAY_FORWARD = 0;
	public static final int ANIMATION_WAY_BACKWARD = 1;

	private ArrayList<Bitmap> frames;
	private String name;
	private int activeFrame;
	private int animationWay = ANIMATION_WAY_FORWARD;
	
	/**
	 * Constructor with only name.
	 * @param name Name of the animation.
	 */
	public Animator(String name) {
		this.name=name;
	}
	/**
	 * Constructor with name and frames.
	 * @param name Name of the animation.
	 * @param frames Bitmap ArrayList for frames.
	 */
	public Animator(String name, ArrayList<Bitmap> frames)
	{
		this.name=name;
		this.frames=frames;
	}
	/**
	 * Constructor with name, frames and animationWay
	 * @param name Name of the animation
	 * @param frames Bitmap ArrayList for frames.
	 * @param animationWay Way of the animation.
	 */
	public Animator(String name, ArrayList<Bitmap> frames, int animationWay)
	{
		this.name=name;
		this.frames=frames;
		this.animationWay=animationWay;
	}
	public Animator(Animator animation)
	{
		this.name=animation.name;
		this.frames=animation.frames;
		this.animationWay=animation.animationWay;
		this.activeFrame=animation.activeFrame;
		if(animation.listener!=null)
		{
			this.listener=animation.listener;
		}
		
	}
	/**
	 * Sets the Name of the Animation.
	 * @param name Name of the Animation
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Returns the Name of the Animation.
	 * @return Name of the Animation
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Sets frames.
	 * @param frames Frames as Bitmap ArrayList.
	 */
	public void setFrames(ArrayList<Bitmap> frames) {
		this.frames = frames;
	}
	/**
	 * Returns frames as Bitmap ArrayList.
	 * @return frames as Bitmap ArrayList
	 */
	public ArrayList<Bitmap> getFrames() {
		return frames;
	}
	/**
	 * Sets current(active) frame of the animation.
	 * @param frameNo active frame to be set
	 */
	public void setActiveFrame(int frameNo) {
		this.activeFrame = frameNo;
	}
	/**
	 * Returns current(active) frame of the animation.
	 * @return Active frame
	 */
	public int getActiveFrame() {
		return activeFrame;
	}
	/**
	 * Sets Animation Way.
	 * @param way Animation way. Can be 
	 * Animator.ANIMATION_WAY_FORWARD or Animator.ANIMATION_WAY_BACKWARD.
	 */
	public void setAnimationWay(int way) {
		this.animationWay = way;
	}
	/**
	 * Returns Animation Way.
	 * @return Animation way. Can be 
	 * Animator.ANIMATION_WAY_FORWARD or Animator.ANIMATION_WAY_BACKWARD.
	 */
	public int getAnimationWay() {
		return this.animationWay;
	}
	/**
	 * Returns the frame at the number given.
	 * @param frame frame number(n)
	 * @return (n)th Frame
	 */
	public Bitmap getFrame(int frame) {
		return frames.get(frame);
	}
	/**
	 * Returns the next active frame and increases or decreases the active
	 * frame by 1, considering the animation way. Also calls animation event
	 * listener's methods when those events occur.
	 * 
	 * @return Next frame of the animation as Bitmap
	 * @throws IllegalArgumentException when animation way not defined properly
	 */
	public Bitmap nextFrame() throws IllegalArgumentException {
		if (animationWay == ANIMATION_WAY_FORWARD) {
			activeFrame++;
			if(activeFrame==frames.size())
			{
				if(listener!=null)
				{
					listener.animationOnEnd();
				}
			}
			if (activeFrame >= frames.size()) {
				activeFrame = 0;
				if(listener!=null)
				{
					listener.animationOnStart();
				}
			}
			if(listener!=null)
			{
				listener.animationOnFrameChange();
			}
			
			return frames.get(activeFrame);
		} else if (animationWay == ANIMATION_WAY_BACKWARD) {
			activeFrame--;
			if(activeFrame==0)
			{
				if(listener!=null)
				{
					listener.animationOnEnd();
				}
			}
			if (activeFrame < 0) {
				activeFrame = frames.size() - 1;
				if(listener!=null)
				{
					listener.animationOnStart();
				}
			}
			if(listener!=null)
			{
				listener.animationOnFrameChange();
			}
			return frames.get(activeFrame);
		} else {
			throw new IllegalArgumentException(
					"Animasyon yönü yanlýþ belirtilmiþ. " +
					"Animator.ANIMATION_WAY_FORWARD veya " +
					"Animator.ANIMATION_WAY_BACKWARD olmak zorunda");
		}

	}
	/**
	 * Returns the previous active frame and increases or decreases the active
	 * frame by 1, considering the animation way. This method does call
	 * animationOnFrameChange() method but doesn't call animationOnStart()
	 * and animationOnEnd() methods.
	 * 
	 * @return Previous frame of the animation as Bitmap
	 * @throws IllegalArgumentException when animation way not defined properly
	 */
	public Bitmap previousFrame() throws IllegalArgumentException
	{
		if (animationWay == ANIMATION_WAY_BACKWARD) {
			activeFrame++;
			if (activeFrame >= frames.size()) {
				activeFrame = 0;
			}
			if(listener!=null)
			{
				listener.animationOnFrameChange();
			}
			return frames.get(activeFrame);
		} else if (animationWay == ANIMATION_WAY_FORWARD) {
			activeFrame--;
			if (activeFrame < 0) {
				activeFrame = frames.size() - 1;
			}
			if(listener!=null)
			{
				listener.animationOnFrameChange();
			}
			return frames.get(activeFrame);
		} else {
			throw new IllegalArgumentException(
					"Animasyon yönü yanlýþ belirtilmiþ. " +
					"Animator.ANIMATION_WAY_FORWARD veya " +
					"Animator.ANIMATION_WAY_BACKWARD olmak zorunda");
		}
	}
	/**
	 * Returns the animation's event listener.
	 * @return animation's event listener
	 */
	public AnimatorEventListener getListener() {
		return listener;
	}
	/**
	 * Sets the animation's event listener.
	 * @param listener animation event listener
	 */
	public void setListener(AnimatorEventListener listener) {
		this.listener = listener;
	}

}
