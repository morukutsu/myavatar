package com.me.myavatar.screens;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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
import com.me.myavatar.core.RunVoiceCapture;
import com.me.myavatar.core.RunWebcamCapture;
import com.me.myavatar.core.Voice;
import com.me.myavatar.core.Webcam;
import com.me.myavatar.gui.Button;
import com.me.myavatar.gui.ImageButton;

import java.util.Properties;

public class PlayScreen implements Screen {
	// Useful standard variables
	private Game game;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	// Resources
	private BitmapFont font;
	private Texture blank_texture;
	private Sprite sprite;
	private ImageButton btn_yes, btn_no, btn_hello, btn_up, btn_down, btn_left, btn_right, btn_tright, btn_tleft;
	
	// Avatar
	private Avatar avatar;
	
	// Webcam
	private Webcam cam;
	private Thread webcamCapThread;
	private Sprite webcamSpr;
	
	// Net
	private Socket socket;
	
	// Background color animations
	private float background_time = 0.0f;
	private float bg_r, bg_g, bg_b;
	private boolean background_anim = false;
	
	private Voice voice;
	private Thread voiceCapThread;
	
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
		btn_yes = new com.me.myavatar.gui.ImageButton("data/textures/yes.png", camera, -500 + 50, -100);
		btn_no = new com.me.myavatar.gui.ImageButton("data/textures/no.png", camera, -390 + 50, -100);
		btn_hello = new com.me.myavatar.gui.ImageButton("data/textures/hello.png", camera, -280 + 50, -100);
		
		btn_up = new com.me.myavatar.gui.ImageButton("data/textures/up.png", camera, -390, -210);
		
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
		
		DisplayBackgroundColorAnim(delta);
		
		// Text Create
		font.setScale(0.5f);
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
		//btn_up.Draw(batch, delta);
		
		// Boutons actions
		btn_yes.Update();
		btn_no.Update();
		btn_hello.Update();
		//btn_up.Update();
		
		if(!background_anim)
		{
			if(btn_yes.isClicked())
			{
				SendCommand("YES");
				StartBackgroundColorAnim("YES");
				avatar.StartAnimation(0);
			}
			else if(btn_no.isClicked())
			{
				SendCommand("NO");
				StartBackgroundColorAnim("NO");
				avatar.StartAnimation(1);
			}
			else if(btn_hello.isClicked())
			{
				SendCommand("HELLO");
				StartBackgroundColorAnim("HELLO");
			}
		}
		
		// Poll voice commands
		String voiceCom = voice.commands.poll();
		if(voiceCom != null && !background_anim) {
			if(voiceCom.contains("YES"))
				avatar.StartAnimation(0);
			else if(voiceCom.contains("NO"))
				avatar.StartAnimation(1);
			SendCommand(voiceCom);
			StartBackgroundColorAnim(voiceCom);
		}
		
		batch.end();
	}
	
	public void SendCommand(String command) {
		PrintWriter out;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			System.out.println("Sending " + command + " command");
			out.println(command);
		} catch (IOException e) {
			e.printStackTrace();
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
				cam.isFaceDetection = false;
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
				
		webcamCapThread = new Thread(new RunWebcamCapture(cam));
		webcamCapThread.start();
		
		// Get server IP from file
        InputStream is;

		is = this.getClass().getResourceAsStream("settings.txt");
		
		// load the properties file
        Properties prop = new Properties();
        try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        // get the value for app.name key
        try {
        	InetAddress addr = InetAddress.getByName(prop.getProperty("app.commandserver"));
			socket = new Socket(addr, 8000);		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       
        
		// Start voice
        voice = new Voice();
        voiceCapThread = new Thread(new RunVoiceCapture(voice));
        voiceCapThread.start();
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
