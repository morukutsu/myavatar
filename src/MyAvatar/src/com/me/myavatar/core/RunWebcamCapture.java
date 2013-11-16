package com.me.myavatar.core;

import com.googlecode.javacv.FrameGrabber.Exception;

public class RunWebcamCapture implements Runnable {
	private Webcam webcam;
	
	public RunWebcamCapture(Webcam w) {
		webcam = w;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				webcam.Update();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
