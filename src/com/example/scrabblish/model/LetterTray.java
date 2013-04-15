package com.example.scrabblish.model;

import android.graphics.Canvas;

public class LetterTray {
	private int x;
	private int y;
	private int size;
	private Object[] tray;

	public LetterTray(int x, int y, int size, Object[] tray){
		this.x = x;
		this.y = y;
		this.size = size;
		this.tray = tray;
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

	public int getSize(){
		return size;
	}

	public void setSize(int size){
		this.size = size;
	}

	public Object[] getTray(){
		return this.tray;
	}

	public Object getTileFromTray(int index){
		return this.tray[index];
	}

	public void draw(Canvas canvas){
		for(int r=0; r < size; r++){
			Object tile = this.getTileFromTray(r);
			((Tile) tile).draw(canvas);
		}
	}
}
