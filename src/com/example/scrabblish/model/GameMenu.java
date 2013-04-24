package com.example.scrabblish.model;

import com.example.scrabblish.MainView;

public class GameMenu {
	private int x;
	private int y;
	private int width;
	private int height;
	private Component[] components;
	private static final String TAG = MainView.class.getSimpleName();

	public GameMenu(int x, int y, int width, int height, Component[] components){
		this.x = x;
		this.y = y;
		this.components = components;
		this.width = width;
		this.height = height;
	}
}
