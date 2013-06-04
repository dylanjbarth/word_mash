package com.dylanbarth.wordmash.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class PauseScreen {
	private int width, height;
	private Bitmap background, pauseIcon, resumeIcon, restartIcon;
	
	public PauseScreen(int width, int height, Bitmap background, Bitmap pauseIcon, Bitmap resumeIcon, Bitmap restartIcon){
		this.width = width;
		this.height = height;
		this.background = background;
		this.pauseIcon = pauseIcon;
		this.resumeIcon = resumeIcon;
		this.restartIcon = restartIcon;
	}
	
	public class ResumeButton{
		public ResumeButton(Bitmap resumeIcon, int left, int top, int right, int bottom){
			
		}
	}
	
	public void draw(Canvas canvas){
		Paint p = new Paint();
		p.setARGB(180, 204, 0, 0);
		int margin = 20;
		
		canvas.drawBitmap(background, 0, 0, p);
		canvas.drawRect(new RectF(0, 0, this.width, this.height), p);
		//			canvas.drawRoundRect(new RectF(0+margin, 0+margin, this.width-margin, this.height-margin), 5, 5, p);

		p.setColor(Color.BLACK);
		p.setARGB(100, 64, 64, 64);
		
		int pauseIconX = this.width/2-pauseIcon.getWidth()/2, pauseIconY = this.height/3;
		canvas.drawBitmap(pauseIcon, pauseIconX, pauseIconY, p);
		
		canvas.drawBitmap(resumeIcon, 2*this.width/3-resumeIcon.getWidth()/2, 2*this.height/3, p);
		canvas.drawRoundRect(new RectF(pauseIconX-margin, pauseIconY-margin, pauseIconX+pauseIcon.getWidth()+margin, pauseIconY+pauseIcon.getHeight()+margin), 10, 10, p);

		canvas.drawBitmap(restartIcon, this.width/3-restartIcon.getWidth()/2, 2*this.height/3, p);
	}

}
