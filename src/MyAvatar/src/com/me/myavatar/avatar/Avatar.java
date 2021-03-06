package com.me.myavatar.avatar;

import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;

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
	
	// Eye contact
	private float eyeCX, eyeCY;
	private float curEyeCX, curEyeCY;
	private final float EYESPEED = 1.0f;
	private float faceTime;
	
	// Eye blink
	public float blinkTime = 0.0f;
	
	// YES / NO
	public float mov_x, mov_y;
	
	public float animationTime;
	public boolean animationActivated;
	public int animationType;
	
	public void StartAnimation(int type)
	{
		animationType = type;
		animationActivated = true;
		animationTime = 0.0f;
		mov_x = 0;
		mov_y = 0;
		
		if(type == 1)
		{
			mouth.ReloadTex(2);
		}
	}
	
	public void UpdateAnimation(float dt)
	{
		if(animationActivated)
		{
			animationTime += dt;
			
			if(animationType == 0)
			{
				mov_x = 0.0f;
				mov_y = (float)Math.sin(animationTime*4) * 50.0f;
			}
			else if(animationType == 1)
			{
				mov_x = (float)Math.sin(animationTime*4) * 50.0f;
				mov_y = 0.0f;
			}
			
			if(animationTime >= Math.PI) {
				mov_x = 0.0f;
				mov_y = 0.0f;
				
				animationActivated = false;
				animationTime = 0.0f;
				
				if(animationType == 1)
				{
					mouth.ReloadTex(1);
				}
			}
		}
	}
	
	public Avatar()
	{
		x = y = 0;
		eyeCX = eyeCY = 0;
		curEyeCX = curEyeCY = 0;
		faceTime = 0.0f;
		
		mov_x = mov_y = 0;
		animationTime = 0.0f;
		animationActivated = false;
		
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
	
	public void Draw(SpriteBatch batch, float delta) {
		UpdateAnimation(delta);
		
		// Update eye pos
		if(curEyeCX < eyeCX)
			curEyeCX += EYESPEED;
		if(curEyeCX > eyeCX)
			curEyeCX -= EYESPEED;
		if(curEyeCY < eyeCY)
			curEyeCY += EYESPEED;
		if(curEyeCY > eyeCY)
			curEyeCY -= EYESPEED;
		
		faceTime += delta;
		if(faceTime > 5.0f)
		{
			eyeCX = eyeCY = 0;
		}
		
		eyes[0].eyeBall.offsetY = curEyeCX;
		eyes[0].eyeBall.offsetX = curEyeCY;
		
		eyes[1].eyeBall.offsetY = curEyeCX;
		eyes[1].eyeBall.offsetX = curEyeCY;
		
		// Eye blink
		blinkTime -= delta;
		if(blinkTime <= 0) {
			// Launch blink anim
			blinkTime = (float)Math.random()*8 + 0.25f;
			
			// Set eye blink states to eyes
			eyes[0].StartBlink();
			eyes[1].StartBlink();
		}
		
		
		for(FaceElement e : faceElems) {
			float sx = x;
			float sy = y;
			
			if(!((e instanceof Face) || (e instanceof Hair)) && animationActivated)
			{
				e.x = x + mov_x;
				e.y = y + mov_y;
			}
			
			e.Draw(batch, delta);
			e.x = sx;
			e.y = sy;
		}	
	}
	
	public void ProcessFaces(CvSeq faces) {
		// We track only one face
		CvRect r = null;
		
		// Check if there is a face
		if (faces != null) {
            int total = faces.total();
            for (int i = 0; i < total; i++) {
                r = new CvRect(cvGetSeqElem(faces, i));
                break;
            }
        }
		
		if(r == null) {
			return;
		}
		
		faceTime = 0.0f;
		
		// Get face center
		float faceCenterX, faceCenterY;
		faceCenterX = r.x()*2 + (r.width()*2)/2;
		faceCenterY = r.y()*2 + (r.height()*2)/2;
		
		// Adjust position relative to center
		faceCenterX -= 640/2;
		faceCenterY -= 480/2;
		
		// Compute vector length (for distance)
		float length = (float)Math.sqrt((double)faceCenterX*faceCenterX + (double)faceCenterY*faceCenterY);
		
		// Compute vector angle relative to (0, 0)
		float angle = (float)Math.atan2(-(double)faceCenterX, -(double)faceCenterY);
		
		//System.out.println("L : " + length + " A : " + angle);
		
		// Move eyes
		float headSize = (r.width() + r.height())/2.0f;
		//System.out.println(headSize);
		
		// TODO : take size of the head at screen in consideration
		eyeCX = ((length / 200.0f) * 10.0f);
		eyeCY = 0.0f;
		
		float xr = eyeCX*(float)Math.cos(angle) - eyeCY*(float)Math.sin(angle);
		float yr = eyeCY*(float)Math.cos(angle) + eyeCX*(float)Math.sin(angle);
		
		eyeCX = xr;
		eyeCY = yr;
	}
}
