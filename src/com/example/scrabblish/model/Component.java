package com.example.scrabblish.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.scrabblish.MainView;

public class Component {
	private int x;
	private int y;
	private int width;
	private int height;
	private String title;
	private boolean isButton;
	private Paint paint = new Paint();
	private static final String TAG = MainView.class.getSimpleName();

	public Component(String title, int x, int y, int width, int height, boolean isButton){
		this.setTitle(title);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isButton = isButton;
        Log.d(TAG, "Initialized component:" + title + 
        		"coords: (" + x + ", " + y + "), " +
        		"h:" + height + " w:" + width);
	}

	public void draw(Canvas canvas){
		paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(1);
        canvas.drawRect(x, y, x+width, y+height, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(title, x+20, y+20, paint);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
