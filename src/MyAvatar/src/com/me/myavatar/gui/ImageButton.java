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

public class ImageButton {
	private String text;
	private float x, y;
	private OrthographicCamera camera;
	
	private Texture blank_texture;
	private Sprite sprite;
	
	private float time;
	
	public boolean touched = false;
	private boolean pressed = false;
	
	public ImageButton(String imgfile, OrthographicCamera cam, float _x, float _y) {
		x = _x;
		y = _y;
		camera = cam;
		
		blank_texture = new Texture(Gdx.files.internal(imgfile));
		
		TextureRegion region = new TextureRegion(blank_texture, 0, 0, 128, 128);
		sprite = new Sprite(region);
		
		
		time = 0;
	}
	
	public void Draw(SpriteBatch batch, float delta) {	
		// Button background
		sprite.setOrigin(0,  0);
		sprite.setSize(100, 100);
		
		if(!isHover() )
		{
			sprite.setColor(1.0f, 1.0f, 1.0f, 0.75f);
			sprite.setScale(1.0f);
		}
		else
		{
			sprite.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			sprite.setScale(1.05f);
		}
		
		sprite.setPosition(-100/2.0f + x, -100/2.0f + y);
		sprite.draw(batch);
		
		time += delta;
	}
	
	public boolean isHover() {
		Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(mouse);
		
		if(mouse.x > -100/2.0f + x && mouse.x < 100/2.0f + x
				&& mouse.y > -100/2.0f + y && mouse.y < 100/2.0f + y)
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
	
	public boolean isClicked() {
		boolean save = touched;
		return save;
	}
	
	public void setPosition(float _x, float _y) {
		x = _x;
		y = _y;
	}
	
	public void Update() {
		touched = false;
		boolean old_pressed = pressed;
		pressed = isTouched();
		
		if(pressed != old_pressed && old_pressed == true)
			touched = true;
	}
}
