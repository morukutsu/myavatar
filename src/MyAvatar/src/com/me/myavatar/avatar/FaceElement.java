package com.me.myavatar.avatar;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class FaceElement {
	protected int ID;
	public float x, y, offsetX, offsetY, scale;
	public boolean flipX = false;
	
	public abstract void Draw(SpriteBatch b, float delta);
}
