package com.ibrazebra.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ibrazebra.game.Screens.PlayScreen;
import com.ibrazebra.game.ZooRun;

public class Bark_Twain extends Sprite {
    //Defining all different states our sprite will encounter
    public enum State { FALLING, JUMPING, STANDING, RUNNING};
    public State currentState;
    public State previousState;

    //World Bark Twain will live in
    public World world;
    public Body b2body;

    //Defining the animations
    private Animation Bark_Twain_Run;
    private Animation Bark_Twain_Jump;

    //Track the timer of the state
    private float stateTimer;


    //Define Bark Twain at idle
    private TextureRegion Bark_Twain_Idle;

    //constructor
    public Bark_Twain(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("Bark Twain Sheet"));
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;

        //create an array of texture regions to pass the constructor for the animations
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 3; i < 7; i++)
            frames.add(new TextureRegion(getTexture(), i * 70, 0, 60, 37 ));
        Bark_Twain_Run = new Animation(0.1f, frames);
        frames.clear();

        //Jump
        for(int i = 7; i < 8; i++)
            frames.add(new TextureRegion(getTexture(), i * 70, 0, 60, 38 ));
        Bark_Twain_Jump = new Animation(0.1f, frames);
        frames.clear();

        //Idle
        Bark_Twain_Idle = new TextureRegion(getTexture(), 0, 0, 68, 38);
        defineBark_Twain();
        setBounds(0,0, 65 / ZooRun.PPM, 37 / ZooRun.PPM);
        setRegion(Bark_Twain_Idle);
    }

    //this function is used to attach our sprite to our 2dbody
    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 );
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case JUMPING:
                region = (TextureRegion) Bark_Twain_Jump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) Bark_Twain_Run.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
                region = (TextureRegion) Bark_Twain_Run.getKeyFrame(stateTimer, true);
            default:
                region = Bark_Twain_Idle;
                break;
        }
        stateTimer = currentState == previousState ? stateTimer + dt: 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    //creating the function defineBark_Twain
    public void defineBark_Twain(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32  / ZooRun.PPM, 32  / ZooRun.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(17  / ZooRun.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);

        // creating a sensor on Bark Twain's head
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-20 / ZooRun.PPM, -6 / ZooRun.PPM), new Vector2(20 / ZooRun.PPM, -6 / ZooRun.PPM));
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head");
    }
}
