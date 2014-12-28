package com.piratkopia13.pixelplanet.engine.core;

import com.piratkopia13.pixelplanet.GameMap;
import com.piratkopia13.pixelplanet.engine.physics.shape.Line;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.Texture;
import sun.org.mozilla.javascript.internal.ast.Block;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Util {

    public static FloatBuffer createFloatBuffer(int size){
        return BufferUtils.createFloatBuffer(size);
    }

    public static IntBuffer createIntBuffer(int size){
        return BufferUtils.createIntBuffer(size);
    }

    public static IntBuffer createFlippedBuffer(int... values){
        IntBuffer buffer = createIntBuffer(values.length);
        buffer.put(values);
        buffer.flip();

        return  buffer;
    }

    public static FloatBuffer createFlippedBuffer(Vertex[] vertices){
        FloatBuffer buffer = createFloatBuffer(vertices.length * Vertex.SIZE);

        for (int i = 0; i < vertices.length; i++){
            buffer.put(vertices[i].getPos().getX());
            buffer.put(vertices[i].getPos().getY());
            buffer.put(vertices[i].getTexCoord().getX());
            buffer.put(vertices[i].getTexCoord().getY());
        }

        buffer.flip();

        return buffer;
    }

    /**
     * Tmp method location
     * Raycasting, returns first collision
     */
    public static Vector2f getRayHit( Line line, GameMap map, Vector2f playerPos ){

        int blockSize = map.getBlockSize();

        Vector2f closest = new Vector2f(999, 999);

        for (GameMap.MapBlock block : map.getBlocks().values()){
            if (block.isCollidable()) {
                // TODO: change this code to something more efficient - line block intersection

                Vector2f intersection = new Vector2f(999, 999);

                Vector2f top = LineIntersection(line.pointA, line.pointB, new Vector2f(block.x, block.y), new Vector2f(block.x + blockSize, block.y));
                Vector2f bottom = LineIntersection(line.pointA, line.pointB, new Vector2f(block.x, block.y + blockSize), new Vector2f(block.x + blockSize, block.y + blockSize));
                Vector2f left = linelineIntersection(line.pointA, line.pointB, new Vector2f(block.x, block.y), new Vector2f(block.x, block.y + blockSize));
                Vector2f right = linelineIntersection(line.pointA, line.pointB, new Vector2f(block.x + blockSize, block.y), new Vector2f(block.x + blockSize, block.y + blockSize));

                if (top != null && top.distanceTo(playerPos) < intersection.distanceTo(playerPos))
                    intersection = top;
                if (bottom != null && bottom.distanceTo(playerPos) < intersection.distanceTo(playerPos))
                    intersection = bottom;
                if (left != null && left.distanceTo(playerPos) < intersection.distanceTo(playerPos))
                    intersection = left;
                if (right != null && right.distanceTo(playerPos) < intersection.distanceTo(playerPos))
                    intersection = right;

                if (intersection.distanceTo(playerPos) < closest.distanceTo(playerPos))
                    closest = intersection;
            }
        }
        if (closest.x != 999 && closest.y != 999) return closest;

        return null;
    }

    public static Vector3f colorAt(int x, int y, Texture tex){
        byte[] texData = tex.getTextureData();
        int red   = texData[ (y * tex.getImageWidth() + x) * 3 ];
        int green = texData[ (y * tex.getImageWidth() + x) * 3 + 1 ];
        int blue  = texData[ (y * tex.getImageWidth() + x) * 3 + 2 ];
        return new Vector3f( (red > 0) ? reMap(red, 0, 255f, 0, 1f) : reMap(red+256, 0, 255f, 0f, 1f),
                             (green > 0) ? reMap(green, 0, 255f, 0, 1f) : reMap(green+256, 0, 255f, 0f, 1f),
                             (blue > 0) ? reMap(blue, 0, 255f, 0, 1f) : reMap(blue+256, 0, 255f, 0f, 1f) );
    }
    public static float reMap(float value, float low1, float high1, float low2, float high2) {
        return low2 + (high2 - low2) * (value - low1) / (high1 - low1);
    }

    public static Vector2f LineIntersection(Vector2f a1, Vector2f a2, Vector2f b1, Vector2f b2){
        float d = (a1.x-a2.x)*(b1.y-b2.y) - (a1.y-a2.y)*(b1.x-b2.x);
        if (d == 0) return null;

        float xi = ((b1.x-b2.x)*(a1.x*a2.y-a1.y*a2.x)-(a1.x-a2.x)*(b1.x*b2.y-b1.y*b2.x))/d;
        float yi = ((b1.y-b2.y)*(a1.x*a2.y-a1.y*a2.x)-(a1.y-a2.y)*(b1.x*b2.y-b1.y*b2.x))/d;

        Vector2f p = new Vector2f(xi,yi);
        if (xi < Math.min(a1.x,a2.x) || xi > Math.max(a1.x,a2.x)) return null;
        if (xi < Math.min(b1.x,b2.x) || xi > Math.max(b1.x,b2.x)) return null;
        return p;
    }

    public static Vector2f linelineIntersection(Vector2f a1, Vector2f a2, Vector2f b1, Vector2f b2){
        Vector2f b = a2.add(a1.inverted());
        Vector2f d = b2.add(b1.inverted());
        float bDotDPerp = b.x * d.y - b.y * d.x;

        // if b dot d == 0, it means the lines are parallel so have infinite intersection points
        if (bDotDPerp == 0)
            return null;

        Vector2f c = b1.add(a1.inverted());
        float t = (c.x * d.y - c.y * d.x) / bDotDPerp;
        if (t < 0 || t > 1)
            return null;

        float u = (c.x * b.y - c.y * b.x) / bDotDPerp;
        if (u < 0 || u > 1)
            return null;

        return a1.add(b.mul(t));
    }

}
