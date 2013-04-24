package com.example.scrabblish.model;

import com.example.scrabblish.MainView;

public class Component {
	private int x;
	private int y;
	private int width;
	private int height;
	private String title;
	private boolean isButton;
	private static final String TAG = MainView.class.getSimpleName();

	public Component(String title, int x, int y, int width, int height, boolean isButton){
		this.title = title;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isButton = isButton;
	}
}
