package com.me.myavatar.avatar;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Avatar {
	public float x;
	public float y;
	private Face face;
	private Hair hair;
	private Mouth mouth;
	private Nose nose;
	private Eye[]  eyes = new Eye[2];
	private EyeBrow[]  eyebrows = new EyeBrow[2];
	
	private java.util.ArrayList<FaceElement> faceElems = new java.util.ArrayList<FaceElement>();
	
	public Avatar()
	{
		x = y = 0;
		
		face = new Face(1, x, y);
		
		hair = new Hair(1, x, y);
		hair.offsetY = 150;
		
		mouth = new Mouth(1, x, y);
		mouth.offsetY = -200;
		
		nose = new Nose(1, x, y);
		nose.offsetY = -75;
		
		eyes[0] = new Eye(1, x, y);
		eyes[0].offsetY = 50;
		eyes[0].offsetX = 125;
		
		eyes[1] = new Eye(1, x, y);
		eyes[1].offsetY = 50;
		eyes[1].offsetX = -125;
		
		eyebrows[0] = new EyeBrow(1, x, y);
		eyebrows[0].offsetY = 110;
		eyebrows[0].offsetX = 125;
		eyebrows[0].flipX = true;
		
		eyebrows[1] = new EyeBrow(1, x, y);
		eyebrows[1].offsetY = 110;
		eyebrows[1].offsetX = -125;
		
		faceElems.add(face);
		faceElems.add(eyebrows[0]);
		faceElems.add(eyebrows[1]);
		faceElems.add(hair);
		faceElems.add(mouth);
		faceElems.add(nose);
		faceElems.add(eyes[0]);
		faceElems.add(eyes[1]);
		
	}
	
	public void Draw(SpriteBatch batch) {
		for(FaceElement e : faceElems) {
			e.x = x;
			e.y = y;
			e.Draw(batch);
		}
	}
}
