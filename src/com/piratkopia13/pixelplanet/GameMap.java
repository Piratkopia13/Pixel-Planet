package com.piratkopia13.pixelplanet;

import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import com.piratkopia13.pixelplanet.engine.rendering.Mesh;
import com.piratkopia13.pixelplanet.engine.rendering.Shape;
import com.piratkopia13.pixelplanet.shaders.BasicShader;
import org.newdawn.slick.opengl.Texture;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Handles the map
 * Loading, individual blockdata and more
 */
public class GameMap {

    private List<MapBlock> blocks;
    private int blockSize;
    private Map<BlockType, Texture> blockTextures;

    public GameMap(String filename, int blockSize){
        this.blocks = new ArrayList<>();
        this.blockTextures = new HashMap<>();
        this.blockSize = blockSize;
        this.load(filename);
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }
    public void addBlockTexture(BlockType type, Texture texture){
        this.blockTextures.put(type, texture);
    }

    public static GameMap getTestMap(int blockSize){
        return new GameMap("benv1", blockSize);
    }

    public void draw(){
        glPushAttrib(GL_ALL_ATTRIB_BITS);
        for (int i = 0; i < blocks.size(); i++) {
            MapBlock block = blocks.get(i);
            if (blockTextures.containsKey(block.type))
                blockTextures.get(block.type).bind();

            if (block.type != BlockType.AIR) {
                switch (block.type){
                    case WALL:
                        BasicShader.setColor(0.1f, 0.1f, 0.1f, 1);
                        break;
                    case STONE:
                        BasicShader.setColor(0.7f, 0.7f, 0.7f, 1);
                        break;
                }
                block.mesh.draw();
                BasicShader.resetColor();
            }
        }

        glPopAttrib();
    }

    public boolean collidesWith(Vector2f point){
        for (MapBlock block : blocks){
            if (block.type == BlockType.WALL || block.type == BlockType.STONE) {
                if (Shape.rectangleCollision(new Vector2f(block.x * blockSize, block.y * blockSize), new Vector2f(blockSize, blockSize), point)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param points List of points to test for collision
     * @return Point of collision
     */
    public Vector2f collidesWith(Vector2f[] points){
        for (MapBlock block : blocks){
            if (block.type == BlockType.WALL || block.type == BlockType.STONE) {
                for (Vector2f point : points) {
                    if (Shape.rectangleCollision(new Vector2f(block.x * blockSize, block.y * blockSize), new Vector2f(blockSize, blockSize), point)) {
//                        return new Vector2f( Math.abs(point.getX()-block.x * blockSize), Math.abs(point.getY()-block.y * blockSize) );
                        return new Vector2f(block.x * blockSize, block.y * blockSize);
                    }
                }
            }
        }
        return null;
    }

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
                    blocks.add( new MapBlock(x, y, type) );

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

    public int getBlockSize() {
        return blockSize;
    }

    public List<MapBlock> getBlocks() {
        return blocks;
    }

    public void dispose(){
        for(Map.Entry<BlockType, Texture> entry : blockTextures.entrySet()) {
            entry.getValue().release(); // Release texture
        }
        for (MapBlock block : blocks){
            block.mesh.dispose(); // Dispose mesh
        }
    }

    public class MapBlock{
        public int x;
        public int y;
        public BlockType type;
        public Mesh mesh;

        private MapBlock(int x, int y, BlockType type) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.mesh = Shape.rectangle(x * blockSize, y * blockSize, blockSize, blockSize);
        }
    }

    public enum BlockType{
        SPAWN, SHOP, WALL, AIR, STONE;
    }

}
