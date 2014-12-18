package com.karacasoft.asteroidrush2.utils;

import java.util.ArrayList;

import android.view.MotionEvent;

import com.karacasoft.asteroidrush2.models.TouchableArea;

/**
 * Abstract class to handle Touch Events
 * @author Triforce
 *
 */
public abstract class GameTouchControls {
	
	private ArrayList<TouchableArea> touchable_areas;
	
	private int activePointerID;
	
	/**
	 * Constructor
	 */
	public GameTouchControls()
	{
		touchable_areas=new ArrayList<TouchableArea>();
	}
	/**
	 * Constructor with TouchableAreas included.
	 * @param touchable_areas TouchableAreas
	 */
	public GameTouchControls(ArrayList<TouchableArea> touchable_areas)
	{
		this.touchable_areas=touchable_areas;
	}
	
	
	
	/**
	 * Override this method by your needs to handle touch events.
	 * @param event MotionEvent
	 */
	public abstract void doActions(MotionEvent event);
	
	/**
	 * Checks for touchable areas and calls their methods, like
	 * onTouchDown, onTouchUp,...
	 * @param event
	 */
	public void checkTouchableAreas(MotionEvent event)
	{
		float x=event.getX();
		float y=event.getY();
		
		for(TouchableArea ta : this.getTouchableAreas())
		{
			
			if(x>ta.getLeft() && x<ta.getRight() && y>ta.getTop() && y<ta.getBottom())
			{
				switch(event.getAction() & MotionEvent.ACTION_MASK)
				{
				case MotionEvent.ACTION_DOWN:
					ta.onTouchDown(event);
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					ta.onTouchDown(event);
					break;
				case MotionEvent.ACTION_UP:
					ta.onTouchUp(event);
					break;
				case MotionEvent.ACTION_POINTER_UP:
					ta.onTouchUp(event);
					break;
				case MotionEvent.ACTION_MOVE:
					ta.onTouchMove(event);
					break;
				}
			}
			//reset touchablearea touching states.
			switch(event.getAction() & MotionEvent.ACTION_MASK)
			{
			case MotionEvent.ACTION_UP:
				ta.setHoldingState(false);
				break;
			}
		}
		
	}
	/**
	 * Get TouchableArea List
	 * @return TouchableArea List
	 */
	public ArrayList<TouchableArea> getTouchableAreas() {
		return touchable_areas;
	}
	/**
	 * Set TouchableArea List
	 * @param touchable_areas TouchableArea List
	 */
	public void setTouchableAreas(ArrayList<TouchableArea> touchable_areas) {
		this.touchable_areas = touchable_areas;
	}
	/**
	 * Get Active Pointer ID
	 * @return ActivePointerID
	 */
	public int getActivePointerID() {
		return activePointerID;
	}
	/**
	 * Set Active Pointer ID
	 * @param activePointerID ActivePointerID
	 */
	public void setActivePointerID(int activePointerID) {
		this.activePointerID = activePointerID;
	}
	
}
