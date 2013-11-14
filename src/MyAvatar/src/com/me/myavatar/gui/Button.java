package com.me.myavatar.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.me.myavatar.core.App;

public class Button {
	private String text;
	private float x, y;
	private BitmapFont font;
	private OrthographicCamera camera;
	
	private Texture blank_texture;
	private Sprite sprite;
	
	private float time;
	
	public static float OFFSET_X = 20;
	public static float OFFSET_Y = 40;
	
	public Button(BitmapFont _font, OrthographicCamera cam, String _text, float _x, float _y) {
		text = _text;
		x = _x;
		y = _y;
		font = _font;
		camera = cam;
		
		blank_texture = new Texture(Gdx.files.internal("data/textures/blank.png"));
		
		TextureRegion region = new TextureRegion(blank_texture, 0, 0, 16, 16);
		sprite = new Sprite(region);
		
		
		time = 0;
	}
	
	public void Draw(SpriteBatch batch, float delta) {	
		// Button background
		font.setScale(0.5f);
		TextBounds bounds = font.getBounds(text);
		
		sprite.setOrigin(0,  0);
		sprite.setSize(bounds.width + OFFSET_X, bounds.height + OFFSET_Y);
		
		if(!isHover() )
			sprite.setColor(74/255.0f, 75/255.0f, 168/255.0f, (float)(0.5f + Math.abs(Math.sin(time*2)*0.2)) );
		else
			sprite.setColor(223/255.0f, 152/255.0f, 24/255.0f, 1.0f);
		
		sprite.setPosition(-bounds.width/2.0f + x - OFFSET_X/2, -bounds.height/2.0f + y - OFFSET_Y/2);
		sprite.draw(batch);
		
		// Text Button
		font.setColor(1, 1, 1, 1.0f);
		font.draw(batch, text, -bounds.width/2.0f + x, bounds.height/2.0f + y);
		
		time += delta;
	}
	
	public boolean isHover() {
		Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(mouse);
		
		font.setScale(0.5f);
		TextBounds bounds = font.getBounds(text);
		
		if(mouse.x > -bounds.width/2.0f + x - OFFSET_X/2
				&& mouse.x < bounds.width/2.0f + x + OFFSET_X/2
				&& mouse.y > -bounds.height/2.0f + y - OFFSET_Y/2
				&& mouse.y < bounds.height/2.0f + y + OFFSET_Y/2)
		{
			return true;
		}

		return false;
	}
	
	public boolean isTouched() {
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && isHover() ) {
			return true;
		}
			
		return false;
	}
	
	public void setPosition(float _x, float _y) {
		x = _x;
		y = _y;
	}
}
