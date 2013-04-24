package com.example.scrabblish.model;

import java.util.ArrayList;

import android.graphics.Canvas;

import com.example.scrabblish.MainView;

public class GameMenu {
	private int x, y, width, height;
	private Component[] components;
	private static final String TAG = MainView.class.getSimpleName();

	public GameMenu(int x, int y, int width, int height, Component[] components){
		this.x = x;
		this.y = y;
		this.components = components;
		this.width = width;
		this.height = height;
	}

	public void draw(Canvas canvas){
		for(int i=0; i < this.components.length; i++){
			components[i].draw(canvas);
		}
	}

	public ArrayList<Component> getAllButtons(){
		ArrayList<Component> buttons = new ArrayList<Component>();
		for(int i=0; i < this.components.length; i++){
			Component comp = components[i];
			if(comp.isButton()){
				buttons.add(comp);
			}
		}
		return buttons;
	}

	public void handleActionDown(int eventX, int eventY) {
		// only for buttons
		ArrayList<Component> buttons = getAllButtons();
		for(int i=0; i < buttons.size(); i++){
			buttons.get(i).handleActionDown(eventX, eventY);
		}
	}

	public void handleActionUp(int eventX, int eventY) {
		// only for buttons
		ArrayList<Component> buttons = getAllButtons();
		for(int i=0; i < buttons.size(); i++){
			buttons.get(i).handleActionUp(eventX, eventY);
		}
	}

	public void handleActionMove(int eventX, int eventY) {
		// only for buttons
		ArrayList<Component> buttons = getAllButtons();
		for(int i=0; i < buttons.size(); i++){
			buttons.get(i).handleActionMove(eventX, eventY);
		}
	}


}
