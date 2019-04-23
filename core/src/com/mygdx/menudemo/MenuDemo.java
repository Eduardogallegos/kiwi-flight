package com.mygdx.menudemo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;


public class MenuDemo extends Game {

	private static final AssetManager assetManager = new AssetManager();

	@Override
	public void create(){
		setScreen(new TransitionScreen(this));
	}

	public static AssetManager getAssetManager() {
		return assetManager;
	}

}
