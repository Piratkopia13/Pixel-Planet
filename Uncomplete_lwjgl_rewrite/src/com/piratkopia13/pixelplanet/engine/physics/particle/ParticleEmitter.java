package com.piratkopia13.pixelplanet.engine.physics.particle;

import com.piratkopia13.pixelplanet.GameVar;
import com.piratkopia13.pixelplanet.engine.core.Vector2f;
import com.piratkopia13.pixelplanet.engine.core.Vector3f;
import com.piratkopia13.pixelplanet.shaders.BasicShader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class ParticleEmitter {

    private static Random rndGen = new Random();
    private final List<Particle> particles;
    private Vector2f location;
    private float spawningRate;
    private int particleLifeTime;
    private Vector2f gravity;
    private Vector2f initialVelocity;
    private float velocityModifier;
    private float particleSize;
    private float framesAlive;

    private float currentFrame = 0;
    private Vector3f clr = GameVar.tColor;

    public ParticleEmitter(){
        this(new Vector2f(0, 0), 3, 300, new Vector2f(0, -0.0003f), new Vector2f(-0.5f, -0.5f), 1.0f, 2, -1);
    }

    public ParticleEmitter(Vector2f location, float spawningRate, int particleLifeTime, Vector2f gravity, Vector2f initialVelocity, float velocityModifier, float particleSize, float framesAlive) {
        this.location = location;
        this.spawningRate = spawningRate;
        this.particleLifeTime = particleLifeTime;
        this.gravity = gravity;
        this.initialVelocity = initialVelocity;
        this.velocityModifier = velocityModifier;
        this.particleSize = particleSize;
        this.framesAlive = framesAlive;
        this.particles = new ArrayList<>( (int) spawningRate * particleLifeTime );
    }

    private Particle generateNewParticle(int dx, int dy) {
        Vector2f particleLocation = new Vector2f(0,0);
        particleLocation.set(location);
        Vector2f particleVelocity = new Vector2f(0,0);
        float randomX = (float) rndGen.nextDouble() - 0.5f;
        float randomY = (float) rndGen.nextDouble() - 0.5f;
        particleVelocity.x = (randomX + dx ) * initialVelocity.x;
        particleVelocity.y = (randomY + dy ) * initialVelocity.y;

        particleVelocity.mul(velocityModifier);
        return new Particle(particleLocation, particleVelocity, particleLifeTime);
    }

    public void update() {

        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            particle.update(gravity);
            if (particle.isDestroyed()) {
                particles.remove(i);
                i--;
            }
        }

        if (currentFrame < framesAlive){
            for (int i = 0; i < spawningRate; i++) {
                particles.add(generateNewParticle(0,0));
            }
        }

        currentFrame++;
    }
    public boolean isAlive(){
        return particles.size() > 0;
    }

    public void draw() {
        glPushAttrib(GL_ALL_ATTRIB_BITS);
        glPointSize(particleSize);
        glEnable(GL_BLEND);
//        BasicShader.setColor(1,0,0,1);
        for (Particle particle : particles) {
            float color = (float) particle.expireTime / particleLifeTime;
            BasicShader.setColor(clr.x * color, clr.y * color, clr.z * color, color);
            glBegin(GL_POINTS);
            glVertex2f(particle.position.x, particle.position.y);
            glEnd();
        }
        BasicShader.resetColor();
        glDisable(GL_BLEND);
        glPopAttrib();
    }

    private static class Particle {

        public Vector2f position;
        public Vector2f velocity;
        public int expireTime;

        private Particle(Vector2f position, Vector2f velocity, int expireTime) {
            this.position = position;
            this.velocity = velocity;
            this.expireTime = expireTime;
        }

        public boolean isDestroyed() {
            return expireTime <= 0;
        }

        public void update(Vector2f gravity) {
            position.addTo(velocity);
            velocity.addTo(gravity);
            expireTime -= 1;
        }

        @Override
        public String toString() {
            return "Particle{" +
                    "position=" + position +
                    ", velocity=" + velocity +
                    ", expireTime=" + expireTime +
                    '}';
        }
    }


    // SE = small explosion
    private static List<ParticleEmitter> smallEmitters = new ArrayList<>();
    private static final int SELifeTime = 40;
    private static Vector2f SEVelocity = new Vector2f(1, 1);
    private static final int SERate = 10;
    private static final int SESize = 3;

    public static void createSmallExplosion(Vector2f position){
        ParticleEmitter pe = new ParticleEmitter(position, SERate, SELifeTime, new Vector2f(0,0), SEVelocity, 1, SESize, 5);
        smallEmitters.add(pe);
    }

    public static void updateAndRenderSmallExplosions(){
        for (int i = 0; i < smallEmitters.size(); i++) {
            ParticleEmitter emitter = smallEmitters.get(i);
            emitter.update();
            emitter.draw();
            if (!emitter.isAlive())
                smallEmitters.remove(i);
        }
    }

}
