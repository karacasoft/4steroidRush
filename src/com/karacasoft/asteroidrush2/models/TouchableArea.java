package com.karacasoft.asteroidrush2.models;

import android.graphics.Bitmap;
import android.view.MotionEvent;

/**
 * Touchable areas for AsteroidRushOnTouchListener
 * @author Triforce
 *
 */
public class TouchableArea {
	/**
	 * Interface for handling different touch events
	 * @author Triforce
	 *
	 */
	public interface AsteroidRushOnTouch
	{
		public void onTouchDown(MotionEvent event);
		public void onTouchUp(MotionEvent event);
		public void onTouchMove(MotionEvent event);
	}
	private AsteroidRushOnTouch onTouchEvent;
	private float top;
	private float left;
	private float bottom;
	private float right;
	
	private Bitmap idleStateBitmap;
	private Bitmap pressedStateBitmap;
	private Bitmap unavailableBitmap;
	
	private boolean holdingState=false;
	private boolean clickedOnce=false;
	private boolean clickable=true;
	
	/**
	 * Constructor
	 * @param top X coordinate of object's top
	 * @param left Y coordinate of object's left
	 * @param bottom X coordinate of object's bottom
	 * @param right Y coordinate of object's right
	 */
	public TouchableArea(float top, float left, float bottom, float right) {
		this.top=top;
		this.left=left;
		this.bottom=bottom;
		this.right=right;
	}
	/**
	 * Top getter
	 * @return top
	 */
	public float getTop() {
		return top;
	}
	/**
	 * Top setter
	 * @param top top
	 */
	public void setTop(float top) {
		this.top = top;
	}
	/**
	 * Left getter
	 * @return left
	 */
	public float getLeft() {
		return left;
	}
	/**
	 * Left setter
	 * @param left left
	 */
	public void setLeft(float left) {
		this.left = left;
	}
	/**
	 * Bottom getter
	 * @return bottom
	 */
	public float getBottom() {
		return bottom;
	}
	/**
	 * Bottom setter
	 * @param bottom bottom
	 */
	public void setBottom(float bottom) {
		this.bottom = bottom;
	}
	/**
	 * Right getter
	 * @return right
	 */
	public float getRight() {
		return right;
	}
	/**
	 * Right setter
	 * @param right right
	 */
	public void setRight(float right) {
		this.right = right;
	}
	/**
	 * Returns on touch events for this area
	 * @return on touch events.
	 */
	public AsteroidRushOnTouch getAsteroidRushOnTouchEvent()
	{
		return onTouchEvent;
	}
	/**
	 * Sets on touch events for this area
	 * @param events on touch events
	 */
	public void setAsteroidRushOnTouchEvent(AsteroidRushOnTouch events)
	{
		this.onTouchEvent=events;
	}
	
	/**
	 * Method that should be called on ACTION_DOWN
	 */
	public void onTouchDown(MotionEvent event)
	{
		if(onTouchEvent!=null)
		{
			onTouchEvent.onTouchDown(event);
			setHoldingState(true);
		}
	}
	/**
	 * Method that should be called on ACTION_UP
	 */
	public void onTouchUp(MotionEvent event)
	{
		if(onTouchEvent!=null)
		{
			onTouchEvent.onTouchUp(event);
		}
	}
	/**
	 * Method that should be called on ACTION_MOVE
	 */
	public void onTouchMove(MotionEvent event)
	{
		if(onTouchEvent!=null)
		{
			onTouchEvent.onTouchMove(event);
		}
	}
	/**
	 * @return the idleStateBitmap
	 */
	public Bitmap getIdleStateBitmap() {
		return idleStateBitmap;
	}
	/**
	 * @param idleStateBitmap the idleStateBitmap to set
	 */
	public void setIdleStateBitmap(Bitmap idleStateBitmap) {
		this.idleStateBitmap = idleStateBitmap;
	}
	/**
	 * @return the pressedStateBitmap
	 */
	public Bitmap getPressedStateBitmap() {
		return pressedStateBitmap;
	}
	/**
	 * @param pressedStateBitmap the pressedStateBitmap to set
	 */
	public void setPressedStateBitmap(Bitmap pressedStateBitmap) {
		this.pressedStateBitmap = pressedStateBitmap;
	}
	/**
	 * @return the holdingState
	 */
	public boolean isHoldingState() {
		return holdingState;
	}
	/**
	 * @param holdingState the holdingState to set
	 */
	public void setHoldingState(boolean holdingState) {
		this.holdingState = holdingState;
	}
	/**
	 * @return the clickedOnce
	 */
	public boolean isClickedOnce() {
		return clickedOnce;
	}
	/**
	 * @param clickedOnce the clickedOnce to set
	 */
	public void setClickedOnce(boolean clickedOnce) {
		this.clickedOnce = clickedOnce;
	}
	/**
	 * @return the unavailableBitmap
	 */
	public Bitmap getUnavailableBitmap() {
		return unavailableBitmap;
	}
	/**
	 * @param unavailableBitmap the unavailableBitmap to set
	 */
	public void setUnavailableBitmap(Bitmap unavailableBitmap) {
		this.unavailableBitmap = unavailableBitmap;
	}
	/**
	 * @return the clickable
	 */
	public boolean isClickable() {
		return clickable;
	}
	/**
	 * @param clickable the clickable to set
	 */
	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}
	
}
