package com.me.myavatar.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.me.myavatar.screens.CreateScreen;
import com.me.myavatar.screens.IntroScreen;
import com.me.myavatar.screens.MenuScreen;

public class App extends Game implements ApplicationListener {
	// Instances of every screen created when the App is launched
	public IntroScreen introScreen;
	public MenuScreen 	menuScreen;
	public CreateScreen createScreen;
	
	@Override
	public void create() {
		Texture.setEnforcePotImages(false);
		
		// Create gamestates
		introScreen = new IntroScreen(this);
		menuScreen  = new MenuScreen(this);
		createScreen = new CreateScreen(this);
		
		setScreen(createScreen);
	}

}
