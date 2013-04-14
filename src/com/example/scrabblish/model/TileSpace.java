package com.example.scrabblish.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class TileSpace {
	private Bitmap bitmap;
	private int row;
	private int col;
	private boolean occupied;
	private int multiplier;
	
	public TileSpace(Bitmap bitmap, int row, int col, boolean occupied, int multiplier){
		this.bitmap = bitmap;
		this.row = row;
		this.col = col;
		this.occupied = false;
		this.multiplier = 1;
	}
	
	public Bitmap getBitmap(){
		return bitmap;
	}
	
	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}
	
	public int getRow(){
		return row;
	}
	
	public void setRow(int row){
		this.row = row;
	}
	
	public int getCol(){
		return col;
	}
	
	public void setCol(int col){
		this.col = col;
	}
	
	public boolean isOccupied(){
		return occupied;
	}
	
	public void setOccupied(boolean occupied){
		this.occupied = occupied;
	}
	
	public int getMultiplier(){
		return multiplier;
	}
	
	public void setMultiplier(int multiplier){
		this.multiplier = multiplier;
	}
	
	public void draw(Canvas canvas){
//		System.out.println("Inside onDraw");
		canvas.drawBitmap(bitmap, row - (bitmap.getWidth()/2), col - (bitmap.getHeight()/2), null);
	}
}
