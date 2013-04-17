package com.example.scrabblish.model;

import com.example.scrabblish.MainView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class Tile {
	private Bitmap bitmap;
	private int x;
	private int y;
	private int width;
	private int height;
	private int index;
	private boolean touched;
	private char letter;
	private static final String TAG = MainView.class.getSimpleName();

	public Tile(Bitmap passedBitmap, int width, int height, int index, int startX){
		this.bitmap = scaleBitmap(passedBitmap, width, height);
		this.x = startX; // b/c tiles are vertical column, X is constant
		this.y = positionTileY(index, height); // positionY
		this.index = index;
		this.width = width;
		this.height = height;
		this.letter = letter; // generate rando 
	}
	
	public int getIndex(){
		return index;
	}
	
	public Bitmap getBitmap(){
		return bitmap;
	}
	
	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}
	
	public int getX(){
		return x;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getLetter(){
		return letter;
	}
	
	public void setLetter(char letter){
		this.letter = letter;
	}
	
	public boolean isTouched(){
		return touched;
	}
	
	public void setTouched(boolean touched){
		this.touched = touched;
	}
	
	public int positionTileY(int index, int height){
		int y = index*height;
		return y;
	}
	
	public Bitmap scaleBitmap(Bitmap tileSpaceImage, int imgWidth, int imgHeight){
		Bitmap resizedBitmap=Bitmap.createScaledBitmap(tileSpaceImage, imgWidth, imgHeight, true);
		return resizedBitmap;
	}
	
	public void draw(Canvas canvas){
		canvas.drawBitmap(bitmap, x, y, null);
//		Log.d(TAG, "Just drew Tile index: " + index + "x=" + x + " y=" + y);
	}
	
	public void handleActionDown(int eventX, int eventY){
		 if (eventX >= (x - bitmap.getWidth() / 2) && (eventX <= (x + bitmap.getWidth()/2))) {
			 if (eventY >= (y - bitmap.getHeight() / 2) && (y <= (y + bitmap.getHeight() / 2))) {
				 setTouched(true);
			 } else {
				 setTouched(false);
			 }
		 } else {
			 setTouched(false);
		 }
	}

}
