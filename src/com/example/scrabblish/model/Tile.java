package com.example.scrabblish.model;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.example.scrabblish.MainView;

public class Tile {
	private Bitmap bitmap;
	private int resetX, resetY, x, y, centerX, centerY, width, height, index, SCALOR = 2;
	private boolean touched;
	private String letter;
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
		this.letter = randomLetter(); // generate rando 
		Log.d(TAG, "Created tile index: " + index + ", x:" + x + ", y:" + y);
		Log.d(TAG, "letter: " + letter);
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

	public String getLetter(){
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

	public void setTouched(boolean touched){
		this.touched = touched;
	}
	
	private String randomLetter(){
		Random rand = new Random();
		int i = rand.nextInt(26+1);
		Log.d(TAG, "Created new rand int " + i);
		return getCharForNumber(i);
	}
	
	private String getCharForNumber(int i) {
		// code below borrowed from: http://stackoverflow.com/a/10813256
		String s = i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
	    return s;
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
	}

	public void drawBig(Canvas canvas){
		int SCALOR = 2;
		int newWidth = width*SCALOR;
		int newHeight = height*SCALOR;
		int newX = x - (newWidth)/2;
		int newY = y - (newHeight)/2;
		canvas.drawBitmap(scaleBitmap(bitmap, newWidth, newHeight), newX, newY, null);
	}

	public void handleActionDown(int eventX, int eventY){
		if (((eventX >= x) && (eventX <= (x + width))) && ((eventY >= y) && (eventY <= (y + height)))) {
			setTouched(true);

			//			expandImage(); here would be where to blow up image
		} else {
			setTouched(false);
		}
	}

	public void resetPosition(){
		this.x = resetX;
		this.y = resetY;
		Log.d(TAG, "Resetting pos, index:" + index + ", x:" + resetX + " y:" + resetY);
	}

	//	public void expandImage(){
	//		this.bitmap = scaleBitmap(bitmap, width*SCALOR, height*SCALOR);
	//		this.x = ;
	//		this.y = resetY;
	//		Log.d(TAG, "Resetting pos, index:" + index + ", x:" + resetX + " y:" + resetY);
	//	}

}
