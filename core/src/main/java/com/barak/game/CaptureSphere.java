package com.barak.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Represents a projectile used to capture monsters.
 * Implements Poolable to allow object reuse, preventing Garbage Collection spikes during gameplay.
 */
public class CaptureSphere extends GameObject implements Poolable {
    
    private Vector2 velocity;
    private boolean active;
    private float speed = 500f; // Travels faster than characters

    public CaptureSphere(Texture texture) {
        super(texture, 0, 0, 24, 24); // Spheres are small (24x24)
        this.velocity = new Vector2();
        this.active = false;
    }

    /**
     * Initializes the sphere when retrieved from the object pool.
     * 
     * @param startX    Starting X coordinate (usually the trainer's position)
     * @param startY    Starting Y coordinate
     * @param direction Normalized vector representing the travel direction
     */
    public void init(float startX, float startY, Vector2 direction) {
        this.x = startX;
        this.y = startY;
        // Multiply the normalized direction vector by the scalar speed
        this.velocity.set(direction).scl(speed);
        this.active = true;
        updateBounds();
    }

    @Override
    public void update(float dt) {
        if (!active) return;

        // Move the sphere
        x += velocity.x * dt;
        y += velocity.y * dt;
        updateBounds();

        // Deactivate the sphere if it travels off-screen
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        
        if (x < -width || x > screenWidth || y < -height || y > screenHeight) {
            active = false;
        }
    }

    /**
     * Required by the Poolable interface. 
     * Resets the object's state before returning it to the pool.
     */
    @Override
    public void reset() {
        x = 0;
        y = 0;
        velocity.setZero();
        active = false;
    }

    public boolean isActive() {
        return active;
    }
    
    public void deactivate() {
        this.active = false;
    }
}