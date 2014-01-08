package com.me.myavatar.avatar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Eye extends FaceElement {
	public Texture img;
	public Sprite spr;
	public EyeBall eyeBall;
	
	public boolean blinking = false;
	public float blinkAnimTime = 0.0f;
	
	public Eye(int _faceID, float _x,  float _y) {
		x = _x;
		y = _y;
		offsetX = offsetY = 0;
		scale = 1.0f;
		
		eyeBall = new EyeBall(1, x, y);
		
		ReloadTex(_faceID);
	}
	
	public void StartBlink() 
	{
		blinking = true;
		blinkAnimTime = 1.0f/8.0f;
	}
	
	public void ReloadTex(int id)
	{
		ID = id;
		
		String filename = "data/textures/eyes/eye" + ID + ".png";
		img = new Texture(Gdx.files.internal(filename));
		
		TextureRegion region = new TextureRegion(img, 0, 0, img.getWidth(), img.getHeight());
		spr = new Sprite(region);
		spr.setSize(img.getWidth(), img.getHeight());
	}
	
	public void Draw(SpriteBatch batch, float delta)
	{
		// blink
		if(blinking) {
			blinkAnimTime -= delta;
			if(blinkAnimTime <= 0) {
				blinking = false;
			}
		} else {
			blinkAnimTime = 0.0f;
		}
		
		// Draw
		float blinkScale = 1.0f - blinkAnimTime*8.0f*0.8f;
		
		spr.setScale(scale, blinkScale);
		spr.setPosition(x + offsetX - img.getWidth()/2, y + offsetY - img.getHeight()/2);
		spr.setOrigin(img.getWidth()/2,  img.getHeight()/2);
		spr.draw(batch);
		
		eyeBall.x = x + offsetX;
		eyeBall.y = y + offsetY;
		
		eyeBall.scaleY = blinkScale;
		eyeBall.Draw(batch, delta);
	}
}
