package mx.tec.kiwiFlight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;


public class kiwiFlight extends Game {

	private static final AssetManager assetManager = new AssetManager();
	private static Preferences preferences;


	@Override
	public void create(){
		Gdx.input.setCatchBackKey(true);
		preferences = Gdx.app.getPreferences(kiwiFlight.class.getName());
		this.setScreen(new TransitionScreen(this));
	}


	public static AssetManager getAssetManager() {
		return assetManager;
	}

	public static Preferences getPreferences() {
		return preferences;
	}
}
