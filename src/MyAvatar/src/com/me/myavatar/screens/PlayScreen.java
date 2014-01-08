package com.me.myavatar.screens;

import java.io.IOException;

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
import com.googlecode.javacv.FrameGrabber.Exception;
import com.me.myavatar.avatar.Avatar;
import com.me.myavatar.core.App;
import com.me.myavatar.core.RunWebcamCapture;
import com.me.myavatar.core.Webcam;
import com.me.myavatar.gui.Button;
import com.me.myavatar.gui.ImageButton;

public class PlayScreen implements Screen {
	// Useful standard variables
	private Game game;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	// Resources
	private BitmapFont font;
	private Texture blank_texture;
	private Sprite sprite;
	private ImageButton btn_yes, btn_no, btn_hello;
	
	// Avatar
	private Avatar avatar;
	
	// Webcam
	private Webcam cam;
	private Thread webcamCapThread;
	private Sprite webcamSpr;
	
	public PlayScreen(Game g) {
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
		avatar.x = 100;
		
		// Buttons
		btn_yes = new com.me.myavatar.gui.ImageButton("data/textures/yes.png", camera, -500, -100);
		btn_no = new com.me.myavatar.gui.ImageButton("data/textures/no.png", camera, -390, -100);
		btn_hello = new com.me.myavatar.gui.ImageButton("data/textures/hello.png", camera, -280, -100);
		
		// Webcam initialization
		try {
			try {
				cam = new Webcam();
				cam.isFaceDetection = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			TextureRegion region1 = new TextureRegion(cam.tex, 0, 0, 640, 480);
			webcamSpr = new Sprite(region1);
			webcamSpr.setSize(320, 240);
			webcamSpr.setOrigin(0,  0);
			webcamSpr.setPosition(-webcamSpr.getWidth() + w/2, -h/2);	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void render(float delta) {
		// Update webcam frame
		cam.isFaceDetection = false;
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
		
		// Text Create
		font.setScale(1);
		font.setColor(1, 1, 1, 1);
		font.draw(batch, "Tele-operate", -w/2.0f + 80, h/2.0f - 80);
		
		// Draw webcam preview
		webcamSpr.draw(batch);
				
		// Affichage avatar
		avatar.Draw(batch, delta);
		
		// Buttons
		btn_yes.Draw(batch, delta);
		btn_no.Draw(batch, delta);
		btn_hello.Draw(batch, delta);
		
		// Boutons actions
		if(btn_yes.isTouched())
		{
			// TODO : envoyer la commande vers le serveur
		}
		
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		webcamCapThread = new Thread(new RunWebcamCapture(cam));
		webcamCapThread.start();
	}

	@Override
	public void hide() {
		try {
			webcamCapThread.stop();
			cam.Stop();
		} catch (Exception e) {
			e.printStackTrace();
		}	
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
