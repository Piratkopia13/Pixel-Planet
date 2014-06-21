package com.piratkopia13.pixelplanet;

import com.piratkopia13.pixelplanet.engine.core.*;
import com.piratkopia13.pixelplanet.engine.physics.shape.Rectangle;
import com.piratkopia13.pixelplanet.engine.physics.shape.Shape;
import com.piratkopia13.pixelplanet.engine.rendering.Mesh;
import com.piratkopia13.pixelplanet.shaders.BasicShader;
import org.newdawn.slick.opengl.Texture;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Handles the map
 * Loading, individual blockdata and more
 */
public class GameMap {

    private Map<Vector2f, MapBlock> blocks;
    private int blockSize;
    public Map<BlockType, Texture> blockTextures;
//    private SpriteSheet spritesheet;

    Texture test;

    public GameMap(String filename, int blockSize){
        this.blocks = new HashMap<>();
        this.blockTextures = new HashMap<>();
        this.blockSize = blockSize;
//        this.spritesheet = new SpriteSheet("tiles");
        this.load(filename);

        test = GameResourceLoader.loadTexture("tiles/stone.png");

        addBlockTexture(BlockType.WALL, GameResourceLoader.loadTexture("tiles/wall.png"));
        addBlockTexture(BlockType.STONE, GameResourceLoader.loadTexture("tiles/stone.png"));

    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }
    public void addBlockTexture(BlockType type, Texture texture){
        this.blockTextures.put(type, texture);
    }

    public static GameMap getTestMap(int blockSize){
        return new GameMap("bigBox", blockSize);
    }

    public void draw(){
        glPushAttrib(GL_ALL_ATTRIB_BITS);
        for(Map.Entry<Vector2f, MapBlock> entry : blocks.entrySet()) {
            MapBlock block = entry.getValue();
            if (blockTextures.containsKey(block.type))
                blockTextures.get(block.type).bind();
            if (block.isRenderable()) {
                block.mesh.draw();
            }
            BasicShader.resetColor();
        }

        glPopAttrib();
    }

    public boolean collidesWith(Vector2f point){
        for(Map.Entry<Vector2f, MapBlock> entry : blocks.entrySet()) {
            MapBlock block = entry.getValue();
            if (block.type == BlockType.WALL || block.type == BlockType.STONE) {
                if (block.shape.intersects(point)) {
                    return true;
                }
            }
        }
        return false;
    }

//    /**
//     * @param points List of points to test for collision
//     * @return Point of collision
//     */
//    public Vector2f collidesWith(Vector2f[] points){
//        for (MapBlock block : blocks){
//            if (block.type == BlockType.WALL || block.type == BlockType.STONE) {
//                for (Vector2f point : points) {
//                    if (Shape.rectangleCollision(new Vector2f(block.x * blockSize, block.y * blockSize), new Vector2f(blockSize, blockSize), point)) {
////                        return new Vector2f( Math.abs(point.getX()-block.x * blockSize), Math.abs(point.getY()-block.y * blockSize) );
//                        return new Vector2f(block.x * blockSize, block.y * blockSize);
//                    }
//                }
//            }
//        }
//        return null;
//    }

//    public Vector2f collidesWith(Shape box){
//        for(Map.Entry<Vector2f, MapBlock> entry : blocks.entrySet()) {
//            MapBlock block = entry.getValue();
//            if (block.type == BlockType.WALL || block.type == BlockType.STONE) {
//                if (box.intersects(new Vector2f(block.x, block.y), new Vector2f(blockSize, blockSize))) {
//                    return new Vector2f(block.x, block.y);
//                }
//            }
//        }
//        return null;
//    }

    public void load(String filename){
        BufferedReader br;
        BlockType type;
        char rawType;
        int x = 0, y = 0;
        try {
            br = new BufferedReader(new FileReader("res/maps/"+filename));
            String line = br.readLine();

            while (line != null) {

                // add block to render queue
                for (int j = 0; j < line.length(); j++) {
                    rawType = line.charAt(j);
                    switch (rawType){
                        case 'P':
                            type = BlockType.SPAWN;
                            break;
                        case 'S':
                            type = BlockType.SHOP;
                            break;
                        case 'D':
                            type = BlockType.WALL;
                            break;
                        case '0':
                            type = BlockType.STONE;
                            break;
                        default:
                            type = BlockType.AIR;
                            break;
                    }
                    blocks.put(new Vector2f(x,y), new MapBlock(x*blockSize, y*blockSize, type) );

                    x++; // Increase block x pos
                }
                // New row in map, reset x and add to y
                x = 0;
                y++;

                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void damageBlock(MapBlock block, int dmg){
        block.health -= dmg;
        if (block.health <= 0){
            block.type = BlockType.AIR;
            blocks.put(block.getKey(), block);
        }
    }

    public int getBlockSize() {
        return blockSize;
    }

    public MapBlock getBlockAt(int x, int y){
        return blocks.get(new Vector2f(x, y));
    }
    public MapBlock getBlockAtWorldCoords(int x, int y){
        return blocks.get(new Vector2f(x/blockSize, y/blockSize));
    }
    public MapBlock getBlockAtWorldCoords(Vector2f pos){
        return getBlockAtWorldCoords((int) pos.x, (int) pos.y);
    }

    public Map<Vector2f, MapBlock> getBlocks() {
        return blocks;
    }

    public void dispose(){
//        for(Map.Entry<BlockType, Texture> entry : blockTextures.entrySet()) {
//            entry.getValue().release(); // Release texture
//        }
        for(Map.Entry<Vector2f, MapBlock> entry : blocks.entrySet()) {
            MapBlock block = entry.getValue();
            block.mesh.dispose(); // Dispose mesh
        }
    }

    public class MapBlock{
        public int x;
        public int y;
        public BlockType type;
        public Mesh mesh;
        public Shape shape;
        public int health = 1;
//        public SpriteSheet.Sprite sprite;

        private MapBlock(int x, int y, BlockType type) {
            this.x = x;
            this.y = y;
            this.type = type;
//            if (isRenderable()){
//                this.sprite = spritesheet.get(type.toString().toLowerCase());
//                this.shape = new SpriteRectangle(x, y, blockSize, blockSize, sprite); // Get rect
//            } else
                this.shape = new Rectangle(x, y, blockSize, blockSize);
            shape.setRenderable();
            this.mesh = shape.mesh;
        }
        public Vector2f getKey(){
            return new Vector2f(x/blockSize,y/blockSize);
        }

        public boolean isCollidable(){
            return type.equals(BlockType.WALL) || type.equals(BlockType.STONE);
        }
        public boolean isBreakable(){
            return type.equals(BlockType.STONE);
        }
        public boolean isRenderable(){
            return isCollidable();
        }
    }

    public enum BlockType{
        SPAWN, SHOP, WALL, AIR, STONE;

        @Override
        public String toString() {
            return this.name();
        }
    }

}
