package com.me.myavatar.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.myavatar.core.App;
import com.me.myavatar.gui.Button;

public class MenuScreen implements Screen {
	// Useful standard variables
	private Game game;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	// Resources
	private BitmapFont font;
	private Texture blank_texture;
	private Sprite sprite;
	
	// Buttons
	private Button avatars_buttons[] = new Button[3];
	
	// Others
	private float time;
		
	public MenuScreen(Game g) {
		game = g;
		
		batch = new SpriteBatch();
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(w, h);
		
		// Load resources
		font = new BitmapFont(Gdx.files.internal("data/fonts/segoe.fnt"));
		blank_texture = new Texture(Gdx.files.internal("data/textures/blank.png"));
		
		TextureRegion region = new TextureRegion(blank_texture, 0, 0, 16, 16);
		sprite = new Sprite(region);
		sprite.setSize(w, h);
		sprite.setOrigin(0,  0);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		
		// Creating buttons
		float start_button_y = h/2 - 200;
		avatars_buttons[0] = new Button(font, camera, "User 1", -w/2.0f + 250 -400, start_button_y);
		avatars_buttons[1] = new Button(font, camera, "User 2", -w/2.0f + 250 -400, start_button_y - 100);
		avatars_buttons[2] = new Button(font, camera, "User 3", -w/2.0f + 250 -400, start_button_y - 200);
	}
	
	@Override
	public void render(float delta) {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		// Easing
		float start_button_y = h/2 - 200;
		avatars_buttons[0].setPosition(penner.easing.Back.easeInOut(time < 1.0f ? time : 1.0f, -w/2.0f + 250 -400, 400, 1.0f), start_button_y);
		avatars_buttons[1].setPosition(penner.easing.Back.easeInOut(time < 0.9f ? time + 0.1f : 1.0f, -w/2.0f + 250 -400, 400, 1.0f), start_button_y -100);
		avatars_buttons[2].setPosition(penner.easing.Back.easeInOut(time < 0.8f ? time + 0.2f: 1.0f, -w/2.0f + 250 -400, 400, 1.0f), start_button_y -200);
		
		
		// Display
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// Start drawing
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		// Background color
		sprite.setSize(w, h);
		sprite.setOrigin(0,  0);
		sprite.setColor(37.0f/255.0f, 9.0f/255.0f, 49.0f/255.0f, 1.0f);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		sprite.draw(batch);
		
		// Text Avatars
		font.setScale(1);
		font.setColor(1, 1, 1, 1);
		font.draw(batch, "Avatar", -w/2.0f + 40, h/2.0f - 40);
		
		// Boutons avatars
		for(int i = 0; i < 3; i++) {
			avatars_buttons[i].Draw(batch, delta);
			
			if(avatars_buttons[i].isTouched())
			{
				App g = (App) game;
				game.setScreen(g.createScreen);
			}
		}
		
		batch.end();
		
		// Time increment
		time += delta;
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		time = 0.0f;
	}

	@Override
	public void hide() {
	
	}

	@Override
	public void pause() {
	
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
	
	}
}
