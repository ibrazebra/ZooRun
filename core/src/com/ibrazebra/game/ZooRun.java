package com.ibrazebra.game;

import com.ibrazebra.game.Screens.PlayScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


// Changed the extension from ApplicationAdapter API to Game to API (refer to ZooRun_Class_API) Ibra
public class ZooRun extends Game {
    // Virtual Width and Height
    public static final int V_Width = 400;
    public static final int V_Height = 208;
    // Pixels Per Meter (Box2D uses meters, kilos, and seconds for units of measurements so when we
	// as it's radius that is doubled to 10 meters so about 30 feet.
	public static final float PPM = 100;
    // SpriteBatch is a container that holds all of our images or textures (memory intensive)
	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		batch.dispose();
	}
}
