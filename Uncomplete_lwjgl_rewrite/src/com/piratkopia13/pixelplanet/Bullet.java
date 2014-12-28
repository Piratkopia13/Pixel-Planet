package com.piratkopia13.pixelplanet;

import com.piratkopia13.pixelplanet.engine.core.*;
import com.piratkopia13.pixelplanet.engine.physics.particle.ParticleEmitter;
import com.piratkopia13.pixelplanet.engine.physics.shape.Line;
import com.piratkopia13.pixelplanet.engine.physics.shape.Rectangle;
import com.piratkopia13.pixelplanet.engine.physics.shape.Shape;
import com.piratkopia13.pixelplanet.engine.rendering.Mesh;
import com.piratkopia13.pixelplanet.shaders.BasicShader;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;

public class Bullet {

    private final int TICKS_ALIVE = 600; // After these ticks the bullet we be forcedremoved
    private final int RAY_LENGTH = 100000000; // Max bullet travel distance

    private Vector2f position;
    private Vector2f trajectory;
    private float speed;
    private float rotation;
    private Mesh mesh;
    private Shape shape;
    private Vector2f size;
//    private Vector2f extraVelocity;
    private boolean isDead = false;
    private int currentTick;
    private Vector2f endPoint;
    private Line bulletRay;
    private GameMap.MapBlock blockToHit;
    private float travelDistance = 0;
    private float hypo;

    public Bullet(Vector2f startPos, float angle, float speed, Vector2f extraVelocity){
        this.position = startPos;
        this.trajectory = new Vector2f( (float) Math.cos(Math.toRadians(angle)), (float) Math.sin(Math.toRadians(angle)) );
        this.rotation = angle;
        this.speed = speed;
//        this.extraVelocity = extraVelocity;
        this.size = new Vector2f(10,5);
        this.hypo = (float) Math.sqrt(trajectory.x*speed*trajectory.x*speed+trajectory.y*speed*trajectory.y*speed)
                  + (float)Math.sqrt(extraVelocity.x*speed*extraVelocity.x*speed+extraVelocity.y*speed*extraVelocity.y*speed);

        // Create the ray/line from bullet starting position to that point extended to the ray length
        this.bulletRay = new Line(startPos, startPos);
        this.bulletRay = bulletRay.extendLine(RAY_LENGTH, rotation);

        // TODO: change this for better collision when rotating
        this.shape = new Rectangle(position, size);

        mesh = new Mesh();
        Vertex[] vertices = new Vertex[] { new Vertex(0, 0, 0, 0),
                                           new Vertex(0, size.getY(), 0, 1),
                                           new Vertex(size.getX(), size.getY(), 1, 1),
                                           new Vertex(size.getX(), 0, 1, 0) };
        int[] indices = new int[] { 0,1,3,
                                    3,1,2 };
        mesh.addVertices(vertices, indices);

        // "Cast the ray" and see on what mapblock it hits
        endPoint = Line.getRayHitOnMap(bulletRay, startPos);
        // If the ray didnt hit any block, then just set the endpoint to the extended point
        if (endPoint == null) endPoint = bulletRay.pointB;
        bulletRay = new Line(startPos, endPoint);
        // Debug for visual hitpoint
        GameVar.bltCol = endPoint;

        // Get which block it hit
        this.blockToHit = GameVar.getMap().getBlockAtWorldCoords(endPoint.add(trajectory));

        // Adds the bullet update task to the engine
        Game.addSynchronizedTask(task);
    }

    private SynchronizedTask task = new SynchronizedTask() {
        GameMap map = GameVar.getMap();
        int blockSize = map.getBlockSize();
        @Override
        public void update() {
            map = GameVar.getMap();

            updateBlt();
            if (travelDistance >= bulletRay.length){
                Texture tex = map.blockTextures.get(blockToHit.type);
//                System.out.println(endPoint);
                if (tex != null) GameVar.tColor = Util.colorAt( (int)(endPoint.x % blockSize)/2, (int)(endPoint.y % blockSize)/2, tex);
                ParticleEmitter.createSmallExplosion(endPoint);
                if (blockToHit.isBreakable())
                    map.damageBlock(blockToHit, 1);
//                System.out.println("rem");
                isDead = true;
            }
        }
    };

    public void destroy(){
        Game.removeSynchronizedTask(task);
    }

    public void updateBlt(){
        position = position.add( trajectory.mul(speed * 1.0f)/*.add(extraVelocity) commented cuz i cant figure out how to add the extra velocity to the ray*/ );
        if (currentTick >= TICKS_ALIVE)
            markAsDead();
        travelDistance+=hypo;
        currentTick++;
    }

    public void draw(){

        glPushMatrix();

        glTranslatef(position.getX(), position.getY(), 0);
        glRotatef(rotation, 0, 0, 1);
        BasicShader.setColor(0, 1, 0, 1);
        mesh.draw();
        BasicShader.resetColor();

        glPopMatrix();

    }

    public void markAsDead(){
        isDead = true;
    }

    public boolean isDead(){
        return isDead;
    }

    public Vector2f getPosition() {
        return position;
    }

    /**
     * @return Bullet rectangle shape, get rekt!
     */
    public Shape getRekt() {
        return shape;
    }
}
