package com.karacasoft.asteroidrush2.models;

import com.karacasoft.asteroidrush2.utils.Animator;

/**
 * Ship data model.
 * @author Triforce
 *
 */
public class Ship {
	
	public static final int PROJECTILE_TYPE_NORMAL_SINGLE=0;
	public static final int PROJECTILE_TYPE_NORMAL_DOUBLE=1;
//	public static final int PROJECTILE_TYPE_NORMAL_TRIPLE=2;
	
	public static final int PROJECTILE_TYPE_SPREAD_TRIPLE=2;
	public static final int PROJECTILE_TYPE_SPREAD_FOUR=3;
	public static final int PROJECTILE_TYPE_SPREAD_FIVE=4;
	public static final int PROJECTILE_TYPE_SPREAD_SIX=5;
	public static final int PROJECTILE_TYPE_SPREAD_SEVEN=6;
	public static final int PROJECTILE_TYPE_SPREAD_EIGHT=7;
	public static final int PROJECTILE_TYPE_SPREAD_NINE=8;
	public static final int PROJECTILE_TYPE_SPREAD_TEN=9;
	
	public float x;
	public float y;
	public float speed;
	public float baseSpeed=2;
	
	private int weaponRechargeTime;
	
	private int projectileType;
	
	private Animator destroyAnimation;
	
	private boolean destroyed=false;
	
	/**
	 * Constructor
	 */
	public Ship() {
		
	}
	/**
	 * Destroy ship. Game Over. You Lost.
	 */
	public void destroy()
	{
		this.destroyed=true;
	}
	
	/**
	 * Get weapon recharge time in game ticks.
	 * @return Recharge time
	 */
	public int getWeaponRechargeTime() {
		return weaponRechargeTime;
	}
	/**
	 * Set weapon recharge time in game ticks.
	 * @param weaponRechargeTime Recharge time 
	 */
	public void setWeaponRechargeTime(int weaponRechargeTime) {
		this.weaponRechargeTime = weaponRechargeTime;
	}
	/**
	 * Get weapon type of this Ship. There are 3 types of weapon:<br />
	 * -PROJECTILE_TYPE_NORMAL_SINGLE,<br />
	 * -PROJECTILE_TYPE_NORMAL_DOUBLE,<br />
	 * -PROJECTILE_TYPE_NORMAL_TRIPLE,<br />
	 * <p>I may add more types of weapon later. Like laser-shot or else...</p>
	 * 
	 * @return Weapon type.
	 */
	public int getProjectileType() {
		return projectileType;
	}
	/**
	 * Get weapon type of this Ship. There are 3 types of weapon:<br />
	 * -PROJECTILE_TYPE_NORMAL_SINGLE,<br />
	 * -PROJECTILE_TYPE_NORMAL_DOUBLE,<br />
	 * -PROJECTILE_TYPE_NORMAL_TRIPLE,<br />
	 * 
	 * @param projectileType Weapon type.
	 */
	public void setProjectileType(int projectileType) {
		this.projectileType = projectileType;
	}
	/**
	 * check if the ship is destroyed.
	 * @return true if destroyed.
	 */
	public boolean isDestroyed()
	{
		return destroyed;
	}
	/**
	 * @return the destroyAnimation
	 */
	public Animator getDestroyAnimation() {
		return destroyAnimation;
	}
	/**
	 * @param destroyAnimation the destroyAnimation to set
	 */
	public void setDestroyAnimation(Animator destroyAnimation) {
		this.destroyAnimation = destroyAnimation;
	}
}
