package com.piratkopia13.pixelplanet.engine.physics.shape;

import com.piratkopia13.pixelplanet.GameMap;
import com.piratkopia13.pixelplanet.GameVar;
import com.piratkopia13.pixelplanet.Player;
import com.piratkopia13.pixelplanet.engine.core.Vector2f;

import java.awt.geom.Line2D;

public class Line {

    public Vector2f pointA;
    public Vector2f pointB;
    public float length;

    public Line(Vector2f pointA, Vector2f pointB){
        this.pointA = pointA;
        this.pointB = pointB;
        try {
            this.length = (float) Math.sqrt((pointB.x - pointA.x) * (pointB.x - pointA.x) + (pointB.y - pointA.y) * (pointB.y - pointA.y));
        }catch (NullPointerException e){
            System.err.println("crash at "+pointA+", "+pointB);
        }
    }

    public Vector2f intersection(Line line2){
        Vector2f a1 = pointA;
        Vector2f a2 = pointB;
        Vector2f b1 = line2.pointA;
        Vector2f b2 = line2.pointB;

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
    public Vector2f intersection(Vector2f b1, Vector2f b2){
        return intersection(new Line(b1, b2));
    }

    public Vector2f lineIntersect(Vector2f b1, Vector2f b2) {
        double x1 = pointA.x;
        double y1 = pointA.y;
        double x2 = pointB.x;
        double y2 = pointB.y;
        double x3 = b1.x;
        double y3 = b1.y;
        double x4 = b2.x;
        double y4 = b2.y;

        double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (denom == 0.0) { // Lines are parallel.
            return null;
        }
        double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
        if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
            // Get the intersection point.
            return new Vector2f( (float)(x1 + ua * (x2 - x1)), (float)(y1 + ua * (y2 - y1)));
        }

        return null;
    }

    public Line extendLine(float amount, float rotation){
        return new Line(pointA, new Vector2f( pointB.x+(float)Math.cos(Math.toRadians(rotation))*amount,
                        pointB.y+(float)Math.sin(Math.toRadians(rotation))*amount ));
    }

    public static Vector2f getRayHitOnMap(Line line1, Vector2f rayStartPoint){
        GameMap map = GameVar.getMap();
        int blockSize = map.getBlockSize();

        Vector2f closest = new Vector2f(999, 999);

        for (GameMap.MapBlock block : map.getBlocks().values()){
            if (block.isCollidable()) {
                // TODO: change this code to something more efficient - line block intersection

                Vector2f intersection = new Vector2f(999, 999);

//                Vector2f top = line1.intersection(new Vector2f(block.x, block.y), new Vector2f(block.x + blockSize, block.y));
//                Vector2f bottom = line1.intersection(new Vector2f(block.x, block.y + blockSize), new Vector2f(block.x + blockSize, block.y + blockSize));
//                Vector2f left = line1.intersection(new Vector2f(block.x, block.y), new Vector2f(block.x, block.y + blockSize));
//                Vector2f right = line1.intersection(new Vector2f(block.x + blockSize, block.y), new Vector2f(block.x + blockSize, block.y + blockSize));
                Vector2f top = line1.lineIntersect(new Vector2f(block.x, block.y), new Vector2f(block.x + blockSize, block.y));
                Vector2f bottom = line1.lineIntersect(new Vector2f(block.x, block.y + blockSize), new Vector2f(block.x + blockSize, block.y + blockSize));
                Vector2f left = line1.lineIntersect(new Vector2f(block.x, block.y), new Vector2f(block.x, block.y + blockSize));
                Vector2f right = line1.lineIntersect(new Vector2f(block.x + blockSize, block.y), new Vector2f(block.x + blockSize, block.y + blockSize));

                if (top != null && top.distanceTo(rayStartPoint) < intersection.distanceTo(rayStartPoint))
                    intersection = top;
                if (bottom != null && bottom.distanceTo(rayStartPoint) < intersection.distanceTo(rayStartPoint))
                    intersection = bottom;
                if (left != null && left.distanceTo(rayStartPoint) < intersection.distanceTo(rayStartPoint))
                    intersection = left;
                if (right != null && right.distanceTo(rayStartPoint) < intersection.distanceTo(rayStartPoint))
                    intersection = right;

                if (intersection.distanceTo(rayStartPoint) < closest.distanceTo(rayStartPoint))
                    closest = intersection;
            }
        }
        if (closest.x != 999 && closest.y != 999) return closest;

        return null;
    }

}
