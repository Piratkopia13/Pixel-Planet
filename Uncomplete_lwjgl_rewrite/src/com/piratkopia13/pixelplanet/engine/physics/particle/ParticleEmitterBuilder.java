package com.piratkopia13.pixelplanet.engine.physics.particle;

import com.piratkopia13.pixelplanet.engine.core.Vector2f;

public class ParticleEmitterBuilder {

    private Vector2f location = new Vector2f(0, 0);
    private float spawningRate = 3;
    private int particleLifeTime = 300;
    private Vector2f gravity = new Vector2f(0, 0);
    private Vector2f initialVelocity = new Vector2f(0, 0);
    private float velocityModifier = 1.0f;
    private float particleSize = 2;
    private float framesAlive = -1;

    public ParticleEmitterBuilder setLocation(Vector2f location) {
        this.location = location;
        return this;
    }

    public ParticleEmitterBuilder setVelocityModifier(float velocityModifier) {
        this.velocityModifier = velocityModifier;
        return this;
    }

    public ParticleEmitterBuilder setSpawningRate(float spawningRate) {
        this.spawningRate = spawningRate;
        return this;
    }

    public ParticleEmitterBuilder setParticleLifeTime(int particleLifeTime) {
        this.particleLifeTime = particleLifeTime;
        return this;
    }

    public ParticleEmitterBuilder setGravity(Vector2f gravity) {
        this.gravity = gravity;
        return this;
    }

    public ParticleEmitterBuilder setInitialVelocity(Vector2f initialVelocity) {
        this.initialVelocity = initialVelocity;
        return this;
    }

    public ParticleEmitterBuilder setParticleSize(float particleSize){
        this.particleSize = particleSize;
        return this;
    }

    public ParticleEmitterBuilder setFrameAlive(float framesAlive){
        this.framesAlive = framesAlive;
        return this;
    }

    public ParticleEmitter createParticleEmitter() {
        return new ParticleEmitter(location, spawningRate, particleLifeTime, gravity, initialVelocity, velocityModifier, particleSize, framesAlive);
    }

}
