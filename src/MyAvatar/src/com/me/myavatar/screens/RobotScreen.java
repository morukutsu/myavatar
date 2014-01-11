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
import com.me.myavatar.core.RunWebcamCapture;
import com.me.myavatar.core.Server;
import com.me.myavatar.core.Webcam;

/**
 * RobotScreen displays the current avatar and performs face detection, speech recognition
 * and send the webcam feed to the client
 */
public class RobotScreen implements Screen {
	// Useful standard variables
	private Game game;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	// Resources
	private BitmapFont font;
	private Texture blank_texture;
	private Sprite sprite;
	
	// Webcam
	private Webcam cam;
	private Thread webcamCapThread;
	private Sprite webcamSpr;
	
	// Avatar
	private Avatar avatar;
	
	// Server
	private Server server;
	
	// Others
	private boolean firstFrame = true;
	
	// Background color animations
	private float background_time = 0.0f;
	private float bg_r, bg_g, bg_b;
	private boolean background_anim = false;
	
	public RobotScreen(Game g) {
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
		avatar.x = 0;
	}
	
	@Override
	public void render(float delta) {
		// Update webcam frame
		cam.updateFrame();
		
		// Get commands received from client
		ProcessCommands();
		
		// Process detected faces from webcam
		avatar.ProcessFaces(cam.faces);
		
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
		
		DisplayBackgroundColorAnim(delta);
		
		// Text Robot
		font.setScale(1);
		font.setColor(1, 1, 1, 1);
		font.draw(batch, "Robot", -w/2.0f + 40, h/2.0f - 40);
		
		// Affichage avatar
		if(server.isClientConnected)
		{
			avatar.Draw(batch, delta);
				
			// Draw webcam preview
			webcamSpr.draw(batch);
		}
		else
		{
			if(firstFrame)
			{
				font.draw(batch, "Waiting for client...", -300, 0);
			}
			else
			{
				server.acceptClient();
			}
		}
		
		batch.end();
		
		firstFrame = false;
	}
	
	public void ProcessCommands()
	{
		// Get a command sended from server
		String command = server.commands.poll();
		if(command != null) 
		{
			if(command.contains("YES") ) 
			{
				StartBackgroundColorAnim("YES");
				System.out.println("## Processed YES command");
				avatar.StartAnimation(0);
			} 
			else if(command.contains("NO")) 
			{
				StartBackgroundColorAnim("NO");
				System.out.println("## Processed NO command");
				avatar.StartAnimation(1);
			}
			else if(command.contains("HELLO")) 
			{
				StartBackgroundColorAnim("HELLO");
				System.out.println("## Processed HELLO command");
			}
		}
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void show() {
		// Webcam initialization
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
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
			webcamSpr.setSize(320*0.75f, 240*0.75f);
			webcamSpr.setOrigin(0,  0);
			webcamSpr.setPosition(-webcamSpr.getWidth() + w/2, -h/2);	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Server initialization
		server = new Server();
				
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
	
	// Background color
	public void StartBackgroundColorAnim(String command) {
		background_time = 0.0f;
		background_anim = true;
		
		if(command.equals("YES"))
		{
			bg_r = 0f;
			bg_g = 0.75f;
			bg_b = 0f;
		}
		else if(command.equals("NO"))
		{
			bg_r = 0.75f;
			bg_g = 0f;
			bg_b = 0f;
		}
		else if(command.equals("HELLO"))
		{
			bg_r = 0f;
			bg_g = 0f;
			bg_b = 0.75f;
		}
	}
	
	public void DisplayBackgroundColorAnim(float dt) {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		background_time += dt;
		
		if(background_anim)
		{
			// Compute alpha
			float alpha_bg = (float)Math.abs(Math.sin(background_time));
			
			// Display
			sprite.setSize(w, h);
			sprite.setOrigin(0,  0);
			sprite.setColor(bg_r, bg_g, bg_b, alpha_bg);
			sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
			sprite.draw(batch);
		}
		
		if(background_time >= 3) {
			background_anim = false;
		}
	}
}
