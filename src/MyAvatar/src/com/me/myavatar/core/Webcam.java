package com.me.myavatar.core;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.FrameGrabber.ImageMode;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.VideoInputFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;

public class Webcam {
	public Texture tex;
	public LinkedList<Pixmap> pixmaps = new LinkedList<Pixmap>();
	public int loops = 0;
	
	// Webcam parameters
	private FrameGrabber grabber = null;
	
	public Webcam() throws Exception {
		tex = new Texture(640, 480, Format.RGBA8888);
	}
	
	public void Update() throws Exception {
		IplImage grabbedImage = grabber.grab();
		loops++;
		if(grabbedImage != null) {
			//cvSaveImage("capture-" + loops + ".jpg", grabbedImage);
        	BufferedImage buf = grabbedImage.getBufferedImage();
        	
        	int[] rgbArray = new int[grabbedImage.width() * grabbedImage.height()];
        	
        	buf.getRGB(0, 0, grabbedImage.width(), grabbedImage.height(), rgbArray, 0, 640); 
        	
        	Pixmap pixmap = new Pixmap(grabbedImage.width(), grabbedImage.height(), Format.RGBA8888);
        	for(int i = 0; i < 640; i++) {
        		for(int j = 0; j < 480; j++) {
        			int p = rgbArray[i * 480 + j];
        			int r = (p & 0xFF);
        			int g = ((p >> 8) & 0xFF);
        			int b = ((p >> 16) & 0xFF);
        			int a = ((p >> 24) & 0xFF);
        			
        			
            		rgbArray[i * 480 + j] = (b << 24) | (g << 16) | (r << 8) | a;
            	}
        	}
        	
        	pixmap.getPixels().asIntBuffer().put(rgbArray);
        	
        	pixmaps.add(pixmap);
        }
		
		//System.out.println("BPP : " + grabber.getBitsPerPixel() + ", FMT : " + grabber.getPixelFormat());
	}
	
	public void updateFrame() {
    	Pixmap pixmap = pixmaps.poll();
    	if(pixmap != null) {
    		tex.draw(pixmap, 0, 0);
    		pixmap.dispose();
    	}
	}
	
	public void Start() throws Exception {
		grabber = new VideoInputFrameGrabber(0); 
		//grabber.setFormat("dshow");
		//grabber.setImageMode(ImageMode.COLOR);
		grabber.start();
		
		
	}
	
	public void Stop() throws Exception {
		grabber.stop();
	}
}
