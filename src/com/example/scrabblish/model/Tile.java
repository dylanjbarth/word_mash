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
	private boolean touched, validity, locked;
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
		this.validity = false;
		this.locked = false;
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

	public int getResetX(){
		return resetX;
	}

	public int getResetY(){
		return resetY;
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

	public int getValue(){
		return value;
	}

	public boolean isLocked(){
		return locked;
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

	public void setValidity(boolean validity) {
		this.validity = validity;
	}

	public void setLetter(String string) {
		this.letter = string;		
	}

	public void setLocked(boolean lockedStatus) {
		this.locked = lockedStatus;		
	}
	
	public void setIndex(int index) {
		this.index = index;		
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
		if(validity == false){
			paint.setColor(Color.BLACK);
		} else {
			paint.setColor(Color.RED);
		}
		canvas.drawText(letter, centerX, centerY, paint);
		canvas.drawText(Integer.toString(value), centerX+12, centerY+18, paint);
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
		if(this.locked == false){
			if (((eventX >= x) && (eventX <= (x + width))) && ((eventY >= y) && (eventY <= (y + height)))) {
				setTouched(true);
				//			expandImage(); here would be where to blow up image
			} else {
				setTouched(false);
			}
		}
	}

	public void resetPosition(){
		this.x = resetX;
		this.y = resetY;
		this.centerX = updateCenterX(resetX);
		this.centerY = updateCenterY(resetY);
		Log.d(TAG, "Resetting pos, index:" + index + ", x:" + resetX + " y:" + resetY);
	}

	/******************************
	 * letters helpers * 
	 ******************************/

	private String randomLetter(){
		Random rand = new Random();
		int i = rand.nextInt(97) + 1;
		Log.d(TAG, "Created new rand int " + i);
		//		String s = i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null; // http://stackoverflow.com/a/10813256
		String s = null;
		if(i<=12){ // x12
			s = "E";
		} else if (i<=21){ // x9
			s= "A";
		} else if (i<=30){ // x9
			s= "I";
		} else if (i<=39){ // x8
			s= "O";
		} else if (i<=44){ // x6
			s= "N";
		} else if (i<=50){ // x6
			s= "R";
		} else if (i<=56){ // x6
			s= "T";
		} else if (i<=60){ // x4
			s= "L";
		} else if (i<=64){ // x4
			s= "S";
		} else if (i<=68){ // x4
			s= "U";
		} else if (i<=72){ // x4
			s= "D";
		} else if (i<=75){ // x3
			s= "G";
		} else if (i<=77){ // x2
			s= "B";
		} else if (i<=79){ // x2
			s= "C";
		} else if (i<=81){ // x2
			s= "M";
		} else if (i<=83){ // x2
			s= "P";
		} else if (i<=85){ // x2
			s= "F";
		} else if (i<=87){ // x2
			s= "H";
		} else if (i<=89){ // x2
			s= "V";
		} else if (i<=91){ // x2
			s= "W";
		} else if (i<=93){ // x2
			s= "Y";
		} else if (i<=94){ // x1
			s= "K";
		} else if (i<=95){ // x1
			s= "J";
		} else if (i<=96){ // x1
			s= "X";
		} else if (i<=97){ // x1
			s= "Q";
		} else if (i<=98){ // x1
			s= "Z";
		} 
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
		tileValues.put("V", 3);
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
