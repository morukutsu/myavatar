package com.me.myavatar.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.FFmpegFrameGrabber;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.FrameGrabber.ImageMode;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.VideoInputFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect;

import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

public class Webcam {
	public Texture tex;
	public LinkedList<Pixmap> pixmaps = new LinkedList<Pixmap>();
	
	// Cam
	private int[] rgbArray;
	
	// Face detection
	public boolean isFaceDetection = false;
	private CvHaarClassifierCascade classifier = null;
	public CvSeq faces = null;
	private CvMemStorage storage = null;
	private IplImage grayImage;
	private IplImage smallImage;
	
	// Other
	public int loops = 0;
	
	// Webcam parameters
	private FrameGrabber grabber = null;
	
	public Webcam() throws Exception, IOException {
		tex = new Texture(640, 480, Format.RGBA8888);
		rgbArray = new int[640 * 480];
		
		// Load the classifier file from Java resources.
		String classiferName = "haarcascade_frontalface_alt2.xml";
		
        File classifierFile = new File(classiferName);
        if (classifierFile == null || classifierFile.length() <= 0) {
        	System.out.println(classifierFile.getAbsolutePath());
            throw new IOException("Could not extract \"" + classiferName + "\" from Java resources.");
        }
        
        // Preload the opencv_objdetect module to work around a known bug.
        Loader.load(opencv_objdetect.class);
        classifier = new CvHaarClassifierCascade(cvLoad(classifierFile.getAbsolutePath()));
        if (classifier.isNull()) {
            throw new IOException("Could not load the classifier file.");
        }

        storage = CvMemStorage.create();
        
        // Alloc space for FD images
        grayImage = IplImage.create(640, 480, IPL_DEPTH_8U, 1);
    	smallImage = IplImage.create(640/2, 480/2, IPL_DEPTH_8U, 1);
	}
	
	public void Update() throws Exception {
		try
		{
			IplImage grabbedImage = grabber.grab();
			loops++;
			if(grabbedImage != null) {
				// Resize image if needed
				if(grabbedImage.width() != 640 || grabbedImage.height() != 480) {
					IplImage workImage = IplImage.create(640, 480, IPL_DEPTH_8U, 3);
					cvResize(grabbedImage, workImage, CV_INTER_AREA);
					grabbedImage = workImage;
				}
		
				// Get image from webcam and convert to a drawable format
	        	BufferedImage buf = grabbedImage.getBufferedImage();
	        	
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
	        	
	
	        	// Perform face detection
	        	if(isFaceDetection) {
	        		// FD
	        		cvClearMemStorage(storage);
	        		
	        		// Convert image to black and white downsampled
	                cvCvtColor(grabbedImage, grayImage, CV_BGR2GRAY);
	                cvResize(grayImage, smallImage, CV_INTER_AREA);
	                faces = cvHaarDetectObjects(smallImage, classifier, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
	                
	                // Draw faces on pixmap (DEBUG)
	                if (faces != null) {
	                    pixmap.setColor(1.0f, 0.0f, 0.0f, 1.0f);
	                    int total = faces.total();
	                    for (int i = 0; i < total; i++) {
	                        CvRect r = new CvRect(cvGetSeqElem(faces, i));
	                        pixmap.drawRectangle(r.x()*2, r.y()*2, r.width()*2, r.height()*2);
	                    }
	                }
	                
	                // Debug draw
	                pixmap.setColor(0.0f, 0.0f, 1.0f, 0.5f);
	                pixmap.drawLine(0, 240, 640, 240);
	                pixmap.drawLine(320, 0, 320, 480);
	        	}
	        	
	        	// keep pixmap in frames buffer
	        	pixmaps.add(pixmap);
	        }
		}
		catch(Exception e)
		{
			
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
		//grabber = new VideoInputFrameGrabber(0); 
		//grabber.setFormat("dshow");
		//grabber.setImageMode(ImageMode.COLOR);
		
		grabber = new FFmpegFrameGrabber("http://localhost:8080");
		grabber.setFormat("mjpeg");
		
		grabber.start();	
	}
	
	public void Stop() throws Exception {
		grabber.stop();
	}
}
