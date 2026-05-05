package com.barak.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents an elemental monster in the game.
 * Implements a simple Finite State Machine (FSM) for AI behavior (Wandering vs. Captured).
 */
public class Monster extends GameObject {
    
    public enum State { 
        WANDERING, 
        CAPTURED 
    }

    private ElementType element;
    private State currentState;

    // AI variables for wandering behavior
    private float movementSpeed = 100f; // Slower than the trainer
    private Vector2 wanderDirection;
    private float wanderTimer;

    public Monster(Texture texture, float x, float y, ElementType element) {
        super(texture, x, y, 48, 48); // Monsters are slightly smaller (48x48)
        this.element = element;
        this.currentState = State.WANDERING;
        this.wanderDirection = new Vector2();
        
        pickNewWanderDirection();
    }

    /**
     * Chooses a new random angle and duration for the monster to walk towards.
     */
    private void pickNewWanderDirection() {
        // MathUtils is highly optimized in libGDX compared to java.lang.Math
        float angle = MathUtils.random(0f, 360f);
        wanderDirection.set(1, 0).setAngleDeg(angle);
        
        // Walk in this direction for 1 to 3 seconds before picking a new one
        wanderTimer = MathUtils.random(1f, 3f);
    }

    @Override
    public void update(float dt) {
        if (currentState == State.WANDERING) {
            wanderTimer -= dt;
            
            // Choose a new direction if the timer runs out
            if (wanderTimer <= 0) {
                pickNewWanderDirection();
            }
            
            // Move the monster based on the direction vector
            x += wanderDirection.x * movementSpeed * dt;
            y += wanderDirection.y * movementSpeed * dt;
            
            // Screen clamping logic with automatic direction change upon hitting a wall
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();

            if (x < 0) { x = 0; pickNewWanderDirection(); }
            if (y < 0) { y = 0; pickNewWanderDirection(); }
            if (x > screenWidth - width) { x = screenWidth - width; pickNewWanderDirection(); }
            if (y > screenHeight - height) { y = screenHeight - height; pickNewWanderDirection(); }
            
        } else if (currentState == State.CAPTURED) {
            // Follow logic will be implemented in Step 6
        }
        
        // Always update collision boundaries after resolving movement
        updateBounds();
    }

    public void capture() {
        this.currentState = State.CAPTURED;
    }

    public State getCurrentState() {
        return currentState;
    }
    
    public ElementType getElement() {
        return element;
    }
}