package com.ibrazebra.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ibrazebra.game.Scenes.HUD;
import com.ibrazebra.game.Sprites.Bark_Twain;
import com.ibrazebra.game.Tools.B2WorldCreator;
import com.ibrazebra.game.Tools.WorldContactListerner;
import com.ibrazebra.game.ZooRun;

public class PlayScreen implements Screen {
    //Reference to our Game, used to set Screens
    private ZooRun game;
    private TextureAtlas atlas;


    //basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gameport;
    private HUD hud;

    //Tiled Map Variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2D Variables
    private World world;
    private Box2DDebugRenderer b2dr;

    private Bark_Twain player;

    //constructor
    public PlayScreen(ZooRun game) {
        atlas = new TextureAtlas("Sprites.pack");
        this.game = game;
        // create cam used to follow the sprite through world
        gamecam = new OrthographicCamera();

        //create FitViewport to maintain virtual aspect ratio despite screen
        gameport = new FitViewport(ZooRun.V_Width / ZooRun.PPM, ZooRun.V_Height / ZooRun.PPM, gamecam);

        //create our game HUD for scores/timers/level info
        hud = new HUD(game.batch);

        //Load our map and setup our map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("ZOO RUN MAP.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / ZooRun.PPM);

        //initially set our gamecam to be centered correctly at the start of the game
        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);

        //world creation
        world = new World(new Vector2(0, -10), true);

        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world, map);

        //sprite creation
        player = new Bark_Twain(world, this);

        world.setContactListener(new WorldContactListerner());
    }


    @Override
    public void show() {

    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    public void handleInput(float dt){
        //If user is holding down mouse over, move our camera through game world.
        if (Gdx.input.isKeyPressed(Input.Keys.D) && (player.b2body.getLinearVelocity().x <= 1.5))
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            player.b2body.applyLinearImpulse(new Vector2(0, 3f), player.b2body.getWorldCenter(), true);

    }

    public void update(float dt) {
        //handle user input first
        handleInput(dt);

        //How many times to calculate per second (the higher the more precise)
        world.step(1/60f, 6, 2);

        //updates
        player.update(dt);
        hud.update(dt);

        // track sprite
        gamecam.position.x = player.b2body.getPosition().x;

        //update our gamecam with correct coordinates after changes
        gamecam.update();

        //tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);

        //clear the game screen with black
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render our game map
        renderer.render();

        //render our Box2D Debug Lines
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        //Set our batch to now draw what the HUD camera sees.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}
