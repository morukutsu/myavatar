package com.me.myavatar.core;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class Webcam {
	public Texture tex;
	public LinkedList<Pixmap> pixmaps = new LinkedList<Pixmap>();

	// Webcam parameters
	private FrameGrabber grabber = null;
	
	public Webcam() throws Exception {
		tex = new Texture(640, 480, Format.RGBA8888);
	}
	
	public void Update() throws Exception {
		IplImage grabbedImage = grabber.grab();
		
		if(grabbedImage != null) {
        	//System.out.println("grabbed image " + grabbedImage.width() + "x" + grabbedImage.height() + "x" + grabbedImage.nChannels() );
        	
        	BufferedImage buf = grabbedImage.getBufferedImage();
        	
        	int[] rgbArray = new int[grabbedImage.width() * grabbedImage.height()];
        	
        	buf.getRGB(0, 0, grabbedImage.width(), grabbedImage.height(), rgbArray, 0, 640); 
        	
        	Pixmap pixmap = new Pixmap(grabbedImage.width(), grabbedImage.height(), Format.RGBA8888);
        	pixmap.getPixels().asIntBuffer().put(rgbArray);
        	
        	pixmaps.add(pixmap);
        }
	}
	
	public void updateFrame() {
    	Pixmap pixmap = pixmaps.poll();
    	if(pixmap != null) {
    		tex.draw(pixmap, 0, 0);
    		pixmap.dispose();
    	}
	}
	
	public void Start() throws Exception {
		grabber = FrameGrabber.createDefault(0);
		grabber.start();
	}
	
	public void Stop() throws Exception {
		grabber.stop();
	}
}
