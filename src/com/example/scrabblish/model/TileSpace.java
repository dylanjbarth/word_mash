package com.example.scrabblish.model;

public class TileSpace {
	private int x;
	private int y;
	private boolean occupied;
	private int multiplier;
	
	public TileSpace(int x, int y, boolean occupied, int multiplier){
		this.x = x;
		this.y = y;
		this.occupied = false;
		this.multiplier = 1;
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
}
