package com.example.scrabblish.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class TileSpace {
	private Bitmap bitmap;
	private int row;
	private int col;
	private int x;
	private int y;
	private boolean occupied;
	private int multiplier;
	
	public TileSpace(Bitmap bitmap, int row, int col, int x, int y, boolean occupied, int multiplier){
		this.bitmap = bitmap;
		this.row = row;
		this.col = col;
		this.x = x;
		this.y = y;
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
	
	public int getx(){
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
		canvas.drawBitmap(bitmap, x, y, null);
	}
}
