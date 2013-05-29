package com.dylanbarth.wordmash.model;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Canvas;
import android.util.Log;

import com.dylanbarth.wordmash.MainView;

public class GameMenu {
	private int x, y, width, height;
	private Component[] components;
	private String state;
	private static final String TAG = MainView.class.getSimpleName();
	
	// names: 

	public GameMenu(int x, int y, int width, int height, Component[] components, String state){
		this.x = x;
		this.y = y;
		this.components = components;
		this.width = width;
		this.height = height;
		this.state = state;
	}

	public void draw(Canvas canvas, String gameState, String boardState){
		for(int i=0; i < this.components.length; i++){
			components[i].draw(canvas, gameState, boardState);
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

	public Component getComponent(String buttonName){
		for(int i=0; i < this.components.length; i++){
			Component comp = components[i];
			if(comp.getTitle() == buttonName){
				return comp;
			}
		}
		return null;
	}

	/*************************
	 * preGame methods * 
	 *************************/

	public void preGameActionDown(int eventX, int eventY) {
		getComponent("changeGameState").handleActionDown(eventX, eventY);
	}

	public void preGameActionMove(int eventX, int eventY) {
		getComponent("changeGameState").handleActionMove(eventX, eventY);
	}

	public void preGameActionUp(int eventX, int eventY) {
		getComponent("changeGameState").handleActionUp(eventX, eventY);
	}

	/*************************
	 * inGame methods * 
	 *************************/
	
	public void handleActionDown(int eventX, int eventY) {
		// only for buttons
		ArrayList<Component> buttons = getAllButtons();
		for(int i=0; i < buttons.size(); i++){
			buttons.get(i).handleActionDown(eventX, eventY);
		}
	}

	public void handleActionMove(int eventX, int eventY) {
		// only for buttons
		ArrayList<Component> buttons = getAllButtons();
		for(int i=0; i < buttons.size(); i++){
			buttons.get(i).handleActionMove(eventX, eventY);
		}
	}
	
	public void handleActionUp(int eventX, int eventY) {
		// only for buttons
		ArrayList<Component> buttons = getAllButtons();
		for(int i=0; i < buttons.size(); i++){
			buttons.get(i).handleActionUp(eventX, eventY);
		}
		checkIfButtonClicked("changeGameState");
	}
	
	/*************************
	 * Button helper methods * 
	 *************************/
	
	public boolean checkIfButtonClicked(String buttonName) {
		return getComponent(buttonName).isClicked();
	}
	
	public void resetButtonClicked(String buttonName){
		getComponent(buttonName).resetClicked();
	}
	
	
}
