package com.example.scrabblish.model;

import java.util.HashMap;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.scrabblish.MainView;

public class Tile {
	private Bitmap bitmap;
	private int resetX, resetY, x, y, centerX, centerY, width, height, index, SCALOR = 2, value;
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
		this.value = returnLetterValue(letter);
		Log.d(TAG, "letter: " + letter + ", value: " + value);
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

	/*************************
	 * Helpers * 
	 *************************/

	public Bitmap scaleBitmap(Bitmap tileSpaceImage, int imgWidth, int imgHeight){
		Bitmap resizedBitmap=Bitmap.createScaledBitmap(tileSpaceImage, imgWidth, imgHeight, true);
		return resizedBitmap;
	}

	public void draw(Canvas canvas){
		canvas.drawBitmap(bitmap, x, y, null);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawText(letter, centerX, centerY, paint);
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
	
	/******************************
	 * letters helpers * 
	 ******************************/
	
	private String randomLetter(){
		Random rand = new Random();
		int i = rand.nextInt(26) + 1;
		Log.d(TAG, "Created new rand int " + i);
		String s = i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null; // http://stackoverflow.com/a/10813256
		return s;
	}
	
	public int returnLetterValue(String letter){
		HashMap<String, Integer> tileValues = new HashMap<String, Integer>();
		tileValues.put("A", 1);
		tileValues.put("B", 3);
		tileValues.put("C", 3);
		tileValues.put("D", 2);
		tileValues.put("E", 1);
		tileValues.put("F", 4);
		tileValues.put("G", 2);
		tileValues.put("H", 4);
		tileValues.put("I", 1);
		tileValues.put("J", 8);
		tileValues.put("K", 5);
		tileValues.put("L", 1);
		tileValues.put("M", 3);
		tileValues.put("N", 1);
		tileValues.put("O", 1);
		tileValues.put("P", 3);
		tileValues.put("Q", 10);
		tileValues.put("R", 1);
		tileValues.put("S", 1);
		tileValues.put("T", 1);
		tileValues.put("U", 1);
		tileValues.put("V", 1);
		tileValues.put("W", 4);
		tileValues.put("X", 8);
		tileValues.put("Y", 4);
		tileValues.put("Z", 10);
		int value = (Integer) tileValues.get(letter);
		return value;
	}

	//	public void expandImage(){
	//		this.bitmap = scaleBitmap(bitmap, width*SCALOR, height*SCALOR);
	//		this.x = ;
	//		this.y = resetY;
	//		Log.d(TAG, "Resetting pos, index:" + index + ", x:" + resetX + " y:" + resetY);
	//	}

}
