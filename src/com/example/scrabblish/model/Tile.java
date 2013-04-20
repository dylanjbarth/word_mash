package com.example.scrabblish.model;

import com.example.scrabblish.MainView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class Tile {
	private Bitmap bitmap;
	private int resetX;
	private int resetY;
	private int x;
	private int y;
	private int centerX;
	private int centerY;
	private int width;
	private int height;
	private int index;
	private boolean touched;
	private char letter;
	private static final String TAG = MainView.class.getSimpleName();

	public Tile(Bitmap passedBitmap, int width, int height, int index, int startX){
		this.bitmap = scaleBitmap(passedBitmap, width, height);
		this.resetX = startX;
		this.resetY = index*height;
		this.x = resetX; // b/c tiles are vertical column, X is constant
		this.y = resetY; // positionY
		this.index = index;
		this.width = width;
		this.height = height;
		this.centerX = updateCenterX(x);
		this.centerY = updateCenterY(y);
//		this.letter = letter; // generate rando 
		Log.d(TAG, "Created tile index: " + index + ", x:" + x + ", y:" + y);
	}

	/*************************
	 * Getters * 
	 *************************/
	public int getIndex(){
		return index;
	}

	public Bitmap getBitmap(){
		return bitmap;
	}

	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getCenterX(){
		return centerX;
	}
	
	public int getCenterY(){
		return centerY;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getLetter(){
		return letter;
	}
	
	public boolean isTouched(){
		return touched;
	}
	
	/*************************
	 * Setters * 
	 *************************/
	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}
	
	public void setX(int x){
		this.x = x;
		this.centerX = updateCenterX(x);
	}
	
	public void setY(int y){
		this.y = y;
		this.centerY = updateCenterY(y);
	}

	public void dragSetX(int x){
		setX(x-width/2);
	}
	
	public void dragSetY(int y){
		setY(y-height/2);
	}
	
	public int updateCenterX(int x){
		return x+width/2;
	}
	
	public int updateCenterY(int y){
		return y+height/2;
	}

	public void setLetter(char letter){
		this.letter = letter;
	}

	public void setTouched(boolean touched){
		this.touched = touched;
	}
	
	/*************************
	 * Helpers * 
	 *************************/

	public Bitmap scaleBitmap(Bitmap tileSpaceImage, int imgWidth, int imgHeight){
		Bitmap resizedBitmap=Bitmap.createScaledBitmap(tileSpaceImage, imgWidth, imgHeight, true);
		return resizedBitmap;
	}

	public void draw(Canvas canvas){
		canvas.drawBitmap(bitmap, x, y, null);
		//		Log.d(TAG, "Just drew Tile index: " + index + "x=" + x + " y=" + y);
	}

	public void handleActionDown(int eventX, int eventY){
//		Log.d(TAG, "*****************************************");
//		Log.d(TAG, "Index: " + index + ", x: " + x + ", y: " + y + ", width: " + width + ", height: " + height);
//		Log.d(TAG, "*****************************************");
		if ((eventX >= x) && (eventX <= (x + width))) {
			if ((eventY >= y) && (eventY <= (y + height))) {
				setTouched(true);
				// here, a function that quickly checks to see if it was on a tileSpace, and if it was, set occupied back to false.
				// probably here, draw large bitmap
//				Log.d(TAG, "Setting touch! Tile index: " + index);
//				Log.d(TAG, "EventX=" + eventX + " which should be between TileX coords: " + x + ", " + (x + width));
//				Log.d(TAG, "EventY=" + eventY + " which should be between TileY coords: " + y + ", " + (y + height));
			} else {
				setTouched(false);
			}
		} else {
			setTouched(false);
		}
	}
	
	public void resetPosition(){
		this.x = resetX;
		this.y = resetY;
		Log.d(TAG, "Resetting pos, index:" + index + ", x:" + resetX + " y:" + resetY);
	}

}
