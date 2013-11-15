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
import com.me.myavatar.avatar.Avatar;

public class CreateScreen implements Screen {
	// Useful standard variables
	private Game game;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	// Resources
	private BitmapFont font;
	private Texture blank_texture;
	private Sprite sprite;
	
	// Avatar
	private Avatar avatar;
	
	public CreateScreen(Game g) {
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
		
		// Avatar
		avatar = new Avatar();
		avatar.x = 200;
	}
	
	@Override
	public void render(float delta) {
		// Display
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		// Start drawing
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		// Background color
		sprite.setSize(w, h);
		sprite.setOrigin(0,  0);
		sprite.setColor(37.0f/255.0f, 9.0f/255.0f, 49.0f/255.0f, 1.0f);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		sprite.draw(batch);
		
		// Text Create
		font.setScale(1);
		font.setColor(1, 1, 1, 1);
		font.draw(batch, "Create", -w/2.0f + 40, h/2.0f - 40);
		
		// Affichage avatar
		avatar.Draw(batch);
		
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
