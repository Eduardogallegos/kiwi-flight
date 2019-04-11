package com.mygdx.menudemo;

import com.badlogic.gdx.Game;


public class MenuDemo extends Game {

	public void create(){
		setScreen(new TransitionScreen(this));
	}

}
