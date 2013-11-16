package com.me.myavatar.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.me.myavatar.core.App;
import com.me.myavatar.core.RunWebcamCapture;
import com.me.myavatar.core.Webcam;
import com.me.myavatar.gui.Button;

public class IntroScreen implements Screen {
	// Useful standard variables
	private Game game;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	// Resources
	private BitmapFont font;
	private Texture blank_texture;
	private Sprite sprite;
	
	private Button btn_client, btn_server;
	
	// Others
	private float time;
	private Webcam cam;
	private Thread webcamCapThread;
	private Sprite webcamSpr;
	
	public IntroScreen(Game g) {
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
		
		btn_client = new com.me.myavatar.gui.Button(font, camera, "Tele-operate a robot", -200, -100);
		btn_server = new com.me.myavatar.gui.Button(font, camera, "Robot side program", 200, -100);
		
		try {
			cam = new Webcam();
			
			TextureRegion region1 = new TextureRegion(cam.tex, 0, 0, 640, 480);
			webcamSpr = new Sprite(region1);
			webcamSpr.setSize(640, 480);
			webcamSpr.setOrigin(0,  0);
			webcamSpr.setPosition(-webcamSpr.getWidth()/2, -webcamSpr.getHeight()/2);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void render(float delta) {
		// Update positions for elements
		btn_client.setPosition(penner.easing.Back.easeInOut(time < 1.5f ? time : 1.5f, -1000, 800, 1.5f), -100);
		btn_server.setPosition(penner.easing.Back.easeInOut(time < 1.5f ? time : 1.5f, 1000, -800, 1.5f), -100);
		
		// Check input
		if(btn_client.isTouched()) {
			App g = (App) game;
			game.setScreen(g.menuScreen);
		} else if(btn_server.isTouched()) {
			
		}
		
		// Update webcam frame
		cam.updateFrame();
		
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
		
		// Splash text
		font.setScale(1.0f);
		TextBounds bounds = font.getBounds("My Avatar");
		font.draw(batch, "My Avatar", -bounds.width/2.0f, bounds.height/2.0f);
		
		// Buttons
		btn_client.Draw(batch, delta);
		btn_server.Draw(batch, delta);
		
		webcamSpr.draw(batch);
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
		
		try {
			cam.Start();
			webcamCapThread = new Thread(new RunWebcamCapture(cam));
			webcamCapThread.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void hide() {
		time = 0.0f;
		
		try {
			webcamCapThread.stop();
			cam.Stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
