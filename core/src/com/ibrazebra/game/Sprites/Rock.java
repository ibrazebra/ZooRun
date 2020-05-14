package com.ibrazebra.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ibrazebra.game.ZooRun;

public class Rock extends InteractiveTileObject{
    public Rock(World world, TiledMap map, Rectangle bounds){
        super(world, map, bounds);

    }
}
