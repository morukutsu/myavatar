package com.me.myavatar.avatar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Hair extends FaceElement {
	public Texture img;
	public Sprite spr;
	
	public Hair(int _faceID, float _x,  float _y) {
		x = _x;
		y = _y;
		offsetX = offsetY = 0;
		scale = 1.0f;
		
		ReloadTex(_faceID);
	}
	
	public void ReloadTex(int id)
	{
		ID = id;
		
		String filename = "data/textures/hairs/hair" + ID + ".png";
		img = new Texture(Gdx.files.internal(filename));
		
		TextureRegion region = new TextureRegion(img, 0, 0, img.getWidth(), img.getHeight());
		spr = new Sprite(region);
		spr.setSize(img.getWidth(), img.getHeight());
		//spr.setOrigin(img.getWidth()/2,  img.getHeight()/2);
	}
	
	public void Draw(SpriteBatch batch, float delta)
	{
		spr.setScale(scale);
		spr.setPosition(x + offsetX - img.getWidth()/2, y + offsetY - img.getHeight()/2);
		spr.setOrigin(img.getWidth()/2,  img.getHeight()/2);
		spr.draw(batch);
	}
}
