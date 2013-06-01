package com.dylanbarth.wordmash.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;

import com.dylanbarth.wordmash.MainView;

public class TileSpace {
	private Bitmap bitmap;
	private Tile tile;
	private int row, col, x, y, centerX, centerY, width, height, penaltyY;
	private boolean occupied, multiplierAnimation;
	private String tileType, animationType;
	private static final String TAG = MainView.class.getSimpleName();

	public TileSpace(Bitmap passedBitmap, int row, int col, int width, int height, String tileType){
		this.bitmap = scaleBitmap(passedBitmap, width, height);
		this.row = row;
		this.col = col;
		this.width = width;
		this.height = height;
		this.occupied = false;
		this.tileType = tileType;
		this.x = positionTileX(row, width);
		this.y = positionTileY(col, height);
		this.centerX = x+width/2;
		this.centerY = y+height/2;
		this.tile = null;
		this.penaltyY = this.y;
		this.animationType = "tile_space";
		this.multiplierAnimation = false;
	}

	/*************************
	 * Getters * 
	 *************************/
	public Bitmap getBitmap(){
		return bitmap;
	}

	public int getRow(){
		return row;
	}

	public int getCol(){
		return col;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public int getWidth(){
		return width;
	}

	public int getHeight(){
		return height;
	}

	public boolean isOccupied(){
		return occupied;
	}

	public String getTileType(){
		return tileType;
	}

	public int[] getCoords(){
		int[] coords = {row, col};
		return coords;
	}

	public Tile getCurrentTile(){
		if(tile != null){
			return tile;
		} else {
			return null;
		}
	}
	
	public int getPenaltyY(){
		return this.penaltyY;
	}

	/*************************
	 * Setters * 
	 *************************/
	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}

	public void setRow(int row){
		this.row = row;
	}

	public void setCol(int col){
		this.col = col;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public void setHeight(int height){
		this.height = height;
	}

	public void setOccupied(boolean occupied){
		this.occupied = occupied;
	}

	public void setTile(Tile tile){
		this.tile = tile;
	}
	
	public void setMultiplierAnimation(boolean bool){
		this.multiplierAnimation = bool;
	}
	
	public void setPenaltyY(int y){
		this.penaltyY = y;
	}

	/*************************
	 * Helpers * 
	 *************************/	
	public int positionTileX(int row, int imgWidth){
		// gives top left x of tile
		int x = imgWidth*row;
		return x;
	}

	public int positionTileY(int col, int imgHeight){
		// gives center of tile in x, y coordinates
		int y = imgHeight*col;
		return y;
	}

	public Bitmap scaleBitmap(Bitmap tileSpaceImage, int imgWidth, int imgHeight){
		Bitmap resizedBitmap=Bitmap.createScaledBitmap(tileSpaceImage, imgWidth, imgHeight, true);
		return resizedBitmap;
	}

	public void draw(Canvas canvas){
		canvas.drawBitmap(bitmap, x, y, null);
		if(this.multiplierAnimation){
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setTypeface(Typeface.SANS_SERIF);
			paint.setTextSize(10);
			paint.setColor(Color.GREEN);
			String multiplier = "";
			if(this.tileType=="tw_space"){
				multiplier = "Triple Word!!";
			} else if(this.tileType=="dw_space"){
				multiplier = "Double Word!";
			} else if(this.tileType=="tl_space"){
				multiplier = "3x!";
			} else if(this.tileType=="dl_space"){
				multiplier = "2x!";
			}
			canvas.drawText(multiplier, x, penaltyY, paint);
		}
	}

	public TileSpace handleTileSnapping(Board board, Tile tile){
		int tileCenterX = tile.getCenterX();
		int tileCenterY = tile.getCenterY();
		if ((tileCenterX >= this.x) && (tileCenterX < (this.x + this.width))) {
			if ((tileCenterY > this.y) && (tileCenterY <= (this.y + this.height))) {
				if(this.occupied){
					TileSpace freeSpace = board.getClosestAvailableTileSpace(tileCenterX, tileCenterY);
					Log.d(TAG, "Calling animation");
					tile.animateSnap(freeSpace.getX(), freeSpace.getY());
					tile.setCurrentTileSpace(this);
					freeSpace.setOccupied(true);
					Log.d(TAG, "Returning freeSpace.");
					return freeSpace;
				} else {
					tile.setX(this.x);
					tile.setY(this.y);
					tile.setCurrentTileSpace(this);
					this.setOccupied(true);
					Log.d(TAG, "Returning tileSpace.");
					return this;
				}
			} 
		} 
		return null;
	}

	public boolean coordsInside(int eventX, int eventY){
		if ((eventX >= x) && (eventX < (x + width))) {
			if ((eventY > y) && (eventY <= (y + height))) {
				return true;
			} 
		}
		return false;
	}

	public void animateMultiplier(String tileType) {
		final int animateSpeed = 20;
		final int deltaY = 1;
		final int threadRevolutions = 50;
		final Handler handler=new Handler();
		final Runnable r = new Runnable()
		{
			public void run() 
			{	
				setMultiplierAnimation(true);
				for(int i=0; i<threadRevolutions; i++){
					Log.d(TAG, "Animating score! PenaltyY:"+getPenaltyY());
					setPenaltyY(getPenaltyY()-deltaY);
					android.os.SystemClock.sleep(10);
				}
				setPenaltyY(getY());
				setMultiplierAnimation(false);
			}
		};
		handler.postDelayed(r, animateSpeed);
		
	}
}
