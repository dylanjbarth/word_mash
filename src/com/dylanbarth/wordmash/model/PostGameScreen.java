package com.dylanbarth.wordmash.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Typeface;

public class PostGameScreen {
	private int width, height, score;
	private RestartButton restartButton;
	private Bitmap background, pauseIcon, resumeIcon, logo;

	public PostGameScreen(int score, int width, int height, Bitmap background, Bitmap restartIcon, Bitmap logo){
		this.width = width;
		this.height = height;
		this.background = background;
		this.logo = logo;
		this.score = score;
		this.restartButton = new RestartButton(restartIcon, this.width/2-restartIcon.getWidth()/2, 2*this.height/3);
	}

	public RestartButton getRestartButton(){
		return this.restartButton;
	}
	
	public void updateScore(int score){
		this.score = score;
	}

	
	public class RestartButton{
		private Bitmap icon;
		private int imageX, imageY, left, right, top, bottom, margin=20;
		private boolean touched;
		public RestartButton(Bitmap restartIcon, int x, int y){
			this.icon = restartIcon; 
			this.imageX = x;
			this.imageY = y;
			this.left = imageX-margin;
			this.top = imageY-margin;
			this.right = left+icon.getWidth()+margin;
			this.bottom = top+icon.getHeight()+margin;
			this.touched = false;
		}
		public Bitmap getIcon() {
			return icon;
		}
		public int getImageX() {
			return imageX;
		}
		public int getImageY() {
			return imageY;
		}
		public int getLeft() {
			return left;
		}
		public int getRight() {
			return right;
		}
		public int getTop() {
			return top;
		}
		public int getBottom() {
			return bottom;
		}
		
		public int getWidth(){
			return getRight()-getLeft();
		}
		public int getMargin() {
			return margin;
		}
		public boolean isTouched(){
			return touched;
		}
		public void setTouched(boolean touched){
			this.touched = touched;
		}
		public void setIcon(Bitmap icon) {
			this.icon = icon;
		}
		public void setImageX(int imageX) {
			this.imageX = imageX;
		}
		public void setImageY(int imageY) {
			this.imageY = imageY;
		}
		public void setLeft(int left) {
			this.left = left;
		}
		public void setRight(int right) {
			this.right = right;
		}
		public void setTop(int top) {
			this.top = top;
		}
		public void setBottom(int bottom) {
			this.bottom = bottom;
		}
		public void setMargin(int margin) {
			this.margin = margin;
		}
		public boolean coordsInside(int x, int y){
			boolean inside = false;
			if(this.left <= x && this.right >= x && this.top <= y && this.bottom >= y){
				inside = true;
			}
			return inside;
		}
	}

	public void draw(Canvas canvas){
		Paint p = new Paint();
		p.setARGB(120, 204, 229, 255);
		canvas.drawBitmap(background, 0, 0, p);
		canvas.drawRect(new RectF(0, 0, this.width, this.height), p);
		//			canvas.drawRoundRect(new RectF(0+margin, 0+margin, this.width-margin, this.height-margin), 5, 5, p);

		Paint pResume = new Paint();
		Paint pRestart = new Paint();
		if(restartButton.isTouched()){
			pRestart.setARGB(100, 64, 64, 64);
		} else {
			pRestart.setARGB(50, 64, 64, 64);
		}

		int logoX = this.width/2-logo.getWidth()/2, logoY = this.height/9;
		canvas.drawBitmap(logo, logoX, logoY, null);

		canvas.drawBitmap(restartButton.getIcon(), restartButton.getImageX(), restartButton.getImageY(), null);
		canvas.drawRoundRect(new RectF(restartButton.getLeft(), restartButton.getTop(), restartButton.getRight(), restartButton.getBottom()), 10, 10, pRestart);
		
		p.setAntiAlias(true);
		p.setTypeface(Typeface.SANS_SERIF);
		p.setTextSize(50);
		p.setARGB(255, 51, 153, 255);
		int margin = 60;
		canvas.drawText("Final Score: " + Integer.toString(this.score), this.width/3, logoY+logo.getHeight()+margin,p);
	}

}
