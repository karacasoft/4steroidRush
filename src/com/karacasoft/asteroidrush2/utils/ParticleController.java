package com.karacasoft.asteroidrush2.utils;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.karacasoft.asteroidrush2.models.Particle;

public abstract class ParticleController {
	
	private ArrayList<Particle> particles=new ArrayList<Particle>();
	
	private float screen_dpi_multiplier=1;
	
	public ParticleController()
	{
		
	}
	
	public void createParticleGroup(float x, float y, int color, int particleCount)
	{
		int i=0;
		Random rand=new Random();
		while (i<particleCount)
		{
			particles.add(new Particle(x, y, rand.nextFloat()*rand.nextInt(20)-10,
					rand.nextFloat()*rand.nextInt(10)-2, color));
			i++;
		}
	}
	
	/**
	 * @return the particles
	 */
	public ArrayList<Particle> getParticles() {
		return particles;
	}

	/**
	 * @param particles the particles to set
	 */
	public void setParticles(ArrayList<Particle> particles) {
		this.particles = particles;
	}

	public void displayParticles(Canvas c)
	{
		int i=0;
		int sayi=particles.size();
		while(i<sayi)
		{
			Particle p=particles.get(i);
		
			if(p.getDieOutTimer()<0)
			{
				particles.remove(p);
				sayi--;
			}else
			{
				p.setDieOutTimer(p.getDieOutTimer()-1);
				Paint a=new Paint();
				a.setColor(p.getColor());
				c.drawCircle(p.getX(), p.getY(), 1*screen_dpi_multiplier, a);
				
				p.setX(p.getX()+p.getSpeedX());
				p.setY(p.getY()+p.getSpeedY());
				
			}
			i++;
		}
	}

	/**
	 * @return the screen_dpi_multiplier
	 */
	public float getScreen_dpi_multiplier() {
		return screen_dpi_multiplier;
	}

	/**
	 * @param screen_dpi_multiplier the screen_dpi_multiplier to set
	 */
	public void setScreen_dpi_multiplier(float screen_dpi_multiplier) {
		this.screen_dpi_multiplier = screen_dpi_multiplier;
	}
	
}
