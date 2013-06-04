package com.dylanbarth.wordmash.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class PauseScreen {
	private int width, height;
	private ResumeButton resumeButton;
	private RestartButton restartButton;
	private Bitmap background, pauseIcon, resumeIcon;

	public PauseScreen(int width, int height, Bitmap background, Bitmap pauseIcon, Bitmap resumeIcon, Bitmap restartIcon){
		this.width = width;
		this.height = height;
		this.background = background;
		this.pauseIcon = pauseIcon;
		this.resumeButton = new ResumeButton(resumeIcon, 2*this.width/3-resumeIcon.getWidth()/2, 2*this.height/3);
		this.restartButton = new RestartButton(restartIcon, this.width/3-restartIcon.getWidth()/2, 2*this.height/3);
	}

	public ResumeButton getResumeButton(){
		return this.resumeButton;
	}

	public RestartButton getRestartButton(){
		return this.restartButton;
	}

	public class ResumeButton{
		private Bitmap icon;
		private int imageX, imageY, left, right, top, bottom, margin=20;
		private boolean touched;
		public ResumeButton(Bitmap resumeIcon, int x, int y){
			this.icon = resumeIcon; 
			this.imageX = x;
			this.imageY = y;
			this.left = imageX-margin;
			this.top = imageY-margin;
			this.right = left+resumeIcon.getWidth()+margin;
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
			this.right = left+restartIcon.getWidth()+margin;
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
		int margin = 20;

		Paint p = new Paint();
		p.setARGB(180, 204, 0, 0);
		canvas.drawBitmap(background, 0, 0, p);
		canvas.drawRect(new RectF(0, 0, this.width, this.height), p);
		//			canvas.drawRoundRect(new RectF(0+margin, 0+margin, this.width-margin, this.height-margin), 5, 5, p);

		Paint pResume = new Paint();
		if(resumeButton.isTouched()){
			pResume.setARGB(200, 64, 64, 64);
		} else {
			pResume.setARGB(100, 64, 64, 64);
		}
		Paint pRestart = new Paint();
		if(restartButton.isTouched()){
			pRestart.setARGB(200, 64, 64, 64);
		} else {
			pRestart.setARGB(100, 64, 64, 64);
		}


		int pauseIconX = this.width/2-pauseIcon.getWidth()/2, pauseIconY = this.height/3;
		canvas.drawBitmap(pauseIcon, pauseIconX, pauseIconY, null);

		canvas.drawBitmap(resumeButton.getIcon(), resumeButton.getImageX(), resumeButton.getImageY(), null);
		canvas.drawRoundRect(new RectF(resumeButton.getLeft(), resumeButton.getTop(), resumeButton.getRight(), resumeButton.getBottom()), 10, 10, pResume);

		canvas.drawBitmap(restartButton.getIcon(), restartButton.getImageX(), restartButton.getImageY(), null);
		canvas.drawRoundRect(new RectF(restartButton.getLeft(), restartButton.getTop(), restartButton.getRight(), restartButton.getBottom()), 10, 10, pRestart);
	}

}
