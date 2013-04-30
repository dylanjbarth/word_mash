package com.example.scrabblish.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.scrabblish.MainView;

public class Component {
	private int x, y, width, height, index;
	private String title;
	private boolean isButton, isTouched, clicked;
	private Paint paint = new Paint();
	private static final String TAG = MainView.class.getSimpleName();

	public Component(String title, int index, int x, int y, int width, int height, boolean isButton){
		this.setTitle(title);
		this.index = index;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isButton = isButton;
		this.isTouched = false;
        Log.d(TAG, "Initialized component:" + title + 
        		"coords: (" + x + ", " + y + "), " +
        		"h:" + height + " w:" + width);
	}

	public void draw(Canvas canvas){
		if (isTouched){
			paint.setColor(Color.DKGRAY);
		} else {
			paint.setColor(Color.LTGRAY);
		}
		
        paint.setStrokeWidth(1);
        canvas.drawRect(x, y, x+width, y+height, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(title, x+20, y+20, paint);
	}
	
	/*************************
	 * Getters * 
	 *************************/

	public String getTitle() {
		return title;
	}
	
	public boolean isButton() {
		return this.isButton;
	}
	
	public boolean isClicked() {
		return this.clicked;
	}
	
	/*************************
	 * Setters * 
	 *************************/

	public void setTitle(String title) {
		this.title = title;
	}

	public void resetClicked(){
		this.clicked = false;
	}
	

	public void handleActionDown(int eventX, int eventY) {
		if((eventX >= x) && (eventX < x+width) && (eventY >= y) && (eventY < y+height)){
			this.isTouched = true;
			Log.d(TAG, "Setting " + title + " to touched!");
		}
	}
	
	public void handleActionMove(int eventX, int eventY) {
		if(this.isTouched){
			if(!((eventX >= x) && (eventX < x+width) && (eventY >= y) && (eventY < y+height))){
				this.isTouched = false;
				Log.d(TAG, "Moved outside of " + title + ", setting touched to false!");
				// clicked(); or by button type, do each individual method here, this can be the qb
			}
		}
	}
	
	public void handleActionUp(int eventX, int eventY) {
		if(this.isTouched){
			if((eventX >= x) && (eventX < x+width) && (eventY >= y) && (eventY < y+height)){
				this.isTouched = false;
				this.clicked = true;
				Log.d(TAG, title + " has been clicked!");
				// clicked(); or by button type, do each individual method here, this can be the qb
			}
		}
	}
}
