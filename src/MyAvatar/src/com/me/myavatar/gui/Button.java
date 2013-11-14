package com.me.myavatar.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Button {
	private String text;
	private float x, y;
	private BitmapFont font;
	
	private Texture blank_texture;
	private Sprite sprite;
	
	private float time;
	
	public Button(BitmapFont _font, String _text, float _x, float _y) {
		text = _text;
		x = _x;
		y = _y;
		font = _font;
		
		blank_texture = new Texture(Gdx.files.internal("data/textures/blank.png"));
		
		TextureRegion region = new TextureRegion(blank_texture, 0, 0, 16, 16);
		sprite = new Sprite(region);
		sprite.setOrigin(0,  0);
		
		time = 0;
	}
	
	public void Draw(SpriteBatch batch, float delta) {	
		// Button background
		font.setScale(0.5f);
		TextBounds bounds = font.getBounds(text);
		
		sprite.setSize(bounds.width + 10, bounds.height + 10);
		sprite.setColor(1, 1, 1, (float)(0.2f + Math.abs(Math.sin(time)*0.2)) );
		sprite.setPosition(-bounds.width/2.0f + x - 5, - bounds.height/2.0f + y - 5);
		sprite.draw(batch);
		
		// Text Button "Client"
		font.setColor(1, 1, 1, 1.0f);
		font.draw(batch, text, -bounds.width/2.0f + x, bounds.height/2.0f + y);
		
		time += delta;
	}
}
