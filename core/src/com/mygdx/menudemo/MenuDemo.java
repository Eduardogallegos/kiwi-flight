package com.mygdx.menudemo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;


public class MenuDemo extends Game {

	private static final AssetManager assetManager = new AssetManager();
	private static Preferences preferences;


	@Override
	public void create(){
		preferences = Gdx.app.getPreferences(MenuDemo.class.getName());
		setScreen(new TransitionScreen(this));
	}


	public static AssetManager getAssetManager() {
		return assetManager;
	}

	public static Preferences getPreferences() {
		return preferences;
	}
}
