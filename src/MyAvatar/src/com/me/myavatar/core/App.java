package com.me.myavatar.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.me.myavatar.screens.IntroScreen;

public class App extends Game implements ApplicationListener {
	// Instances of every screen created when the App is launched
	private IntroScreen introScreen;
	
	@Override
	public void create() {
		// Create gamestates
		introScreen = new IntroScreen(this);
		setScreen(introScreen);
	}

}
