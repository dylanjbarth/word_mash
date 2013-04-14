package com.example.scrabblish.model;


public class Board {
	private int x;
	private int y;
	private int size;
	private Object[][] tileSpaces;
	
	public Board(int x, int y, int size, Object[][] tileSpaces){
		this.x = x;
		this.y = y;
		this.size = size;
		this.tileSpaces = tileSpaces;
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
	 
	public Object getTileSpace(int row, int col){
		Object tileSpace = tileSpaces[row][col];
//		System.out.println(((TileSpace) tileSpace).getRow());
		return tileSpace;
	}
	
	public Object[][] getAllTileSpaces(){
		return tileSpaces;
	}
	
	public void setTileSpace(int row, int col, TileSpace tile){
		this.tileSpaces[row][col] = tile;
	}
}
