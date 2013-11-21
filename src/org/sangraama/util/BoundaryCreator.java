package org.sangraama.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.Timer;

import org.sangraama.assets.Wall;
import org.sangraama.common.Constants;
import org.sangraama.coordination.staticPartition.TileCoordinator;
import org.sangraama.gameLogic.GameEngine;
import org.sangraama.jsonprotocols.send.SangraamaTile;

public class BoundaryCreator implements Runnable{

    private List<Wall> wallList = new ArrayList<Wall>();
    private float worldWidth;
    private float worldHeight;
    private float mapOriX;
    private float mapOriY;
    private float mapHeight;
    private float mapWidth;
    private float subTileWidth;
    private float subTileHeight;

    public List<Wall> calculateWallBoundary() {
        readMapAndWorldDim();
        generateWalls();
        return wallList;
    }

    private void readMapAndWorldDim() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("/conf/sangraamaserver.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        worldWidth = Float.parseFloat(prop.getProperty("maxlength"))/Constants.scale;
        worldHeight = Float.parseFloat(prop.getProperty("maxheight"))/Constants.scale;
        mapOriX = Float.parseFloat(prop.getProperty("maporiginx"))/Constants.scale;
        mapOriY = Float.parseFloat(prop.getProperty("maporiginy"))/Constants.scale;
        mapHeight = Float.parseFloat(prop.getProperty("mapwidth"))/Constants.scale;
        mapWidth = Float.parseFloat(prop.getProperty("mapheight"))/Constants.scale;
        subTileWidth = Float.parseFloat(prop.getProperty("subtilewidth"))/Constants.scale;
        subTileHeight = Float.parseFloat(prop.getProperty("subtileheight"))/Constants.scale;
    }

    private void generateWalls() {
        if(mapOriX == 0 || mapOriY == 0){
            if (mapOriX == 0 && mapOriY == 0) {
                wallList.add(new Wall(mapOriX,mapOriY,mapWidth,1));
                wallList.add(new Wall(mapOriX,mapOriY,1,mapHeight));
            }
            else if(mapOriX == 0){
                wallList.add(new Wall(0,mapOriY,1,mapHeight));
            }
            else if(mapOriY == 0){
                wallList.add(new Wall(mapOriX,0,mapWidth,1));
            }
        }
        
        if(mapOriX == (worldWidth-mapWidth)){
            wallList.add(new Wall(mapOriX+mapWidth,mapOriY,1,mapHeight));
        }
        if(mapOriY == (worldHeight-mapHeight)){
            wallList.add(new Wall(mapOriX,mapOriY+mapHeight,mapWidth,1));
        }
    }

    @Override
    public void run() {
        Timer timer = new Timer(1000,new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateWallList();
                GameEngine.INSTANCE.addWalls(wallList);
            }
        });
        timer.start();
    }
    
    private void calculateWallList(){
        float oriX, oriY;
        boolean upWall = true, downWall = true, rightWall = true, leftWall =true; 
        List<SangraamaTile> tileList = TileCoordinator.INSTANCE.getSubTilesCoordinations();
        for(SangraamaTile tile : tileList){
            oriX = tile.getX();
            oriY = tile.getY();
            tileList.remove(tile);
            if(tileList.size() != 0){
                for(SangraamaTile otherTile : tileList){
                    if(otherTile.getX() == oriX+subTileWidth && otherTile.getY() == oriY){
                        rightWall = false;
                    }
                    if(otherTile.getX() == oriX-subTileWidth && otherTile.getY() == oriY){
                        leftWall = false;
                    }
                    if(otherTile.getY() == oriY+subTileHeight && otherTile.getX() == oriX){
                        upWall = false;
                    }
                    if(otherTile.getY() == oriY-subTileHeight && otherTile.getX() == oriX){
                        downWall = false;
                    }
                }
            }
            if(rightWall){
                wallList.add(new Wall(oriX+subTileWidth,oriY,1,subTileHeight));
            }
            if(leftWall){
                wallList.add(new Wall(oriX,oriY,1,subTileHeight));
            }
            if(downWall){
                wallList.add(new Wall(oriX,oriY,subTileWidth,1));
            }
            if(upWall){
                wallList.add(new Wall(oriX,oriY+subTileHeight,subTileWidth,1));
            }
        }
    }

}
