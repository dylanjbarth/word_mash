package com.example.scrabblish.model;

import com.example.scrabblish.MainView;

import android.graphics.Canvas;

public class LetterTray {
	private int x;
	private int y;
	private int size;
	private Tile[] tray;
	private static final String TAG = MainView.class.getSimpleName();

	public LetterTray(int x, int y, int size, Tile[] tray){
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

	public Tile getTileFromTray(int index){
		return this.tray[index];
	}

	public void draw(Canvas canvas){
		// iterates through all tile objects and draws them 
		for(int r=0; r < size; r++){
			Object tile = this.getTileFromTray(r);
			((Tile) tile).draw(canvas);
		}
	}
	
	public void handleActionDown(int eventX, int eventY){
		// iterates through all tile objects and asks them to handle the individual action down event
		for(int r=0; r < size; r++){
			Object tile = this.getTileFromTray(r);
			((Tile) tile).handleActionDown(eventX, eventY);
		}
	}
	
	public Tile tileTouched(){
		// iterates through all tile objects and returns the object that is currently touched
		for(int r=0; r < size; r++){
			Tile tile = this.getTileFromTray(r);
			if (((Tile) tile).isTouched()){
				return tile;
			}
		}
		return null;
	}
}
