package com.me.myavatar;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.me.myavatar.core.App;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "my avatar - Master in Artificial Intelligence";
		cfg.useGL20 = false;
		cfg.width = 1280;
		cfg.height = 720;
		
		new LwjglApplication(new App(), cfg);
	}
}
