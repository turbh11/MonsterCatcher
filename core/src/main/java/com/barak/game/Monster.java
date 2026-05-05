package com.barak.game; 

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Monster extends GameObject {
    
    
    public enum ElementType {
        FIRE, WATER, GRASS, ELECTRIC;
        
        public boolean beats(ElementType other) {
            if (this == WATER && other == FIRE) return true;
            if (this == FIRE && other == GRASS) return true;
            if (this == GRASS && other == WATER) return true;
            if (this == ELECTRIC && other == WATER) return true;
            return false;
        }
    }
    
    public enum State { WANDERING, CAPTURED }

    private ElementType element;
    private State currentState;

    private float speed = 50f; 
    private Vector2 wanderDirection;
    private float wanderTimer;

    public Monster(Texture texture, float x, float y, ElementType element) {
        super(texture, x, y, 48, 48); // גודל 48x48
        this.element = element;
        this.currentState = State.WANDERING;
        this.wanderDirection = new Vector2();
        pickNewWanderDirection();
    }

    // direction and time randomness
    private void pickNewWanderDirection() {
        float angle = MathUtils.random(0f, 360f); // angle
        wanderDirection.set(1, 0).setAngleDeg(angle); 
        wanderTimer = MathUtils.random(1f, 3f); 
    }

    @Override
    public void update(float dt) {
        if (currentState == State.WANDERING) {
            wanderTimer -= dt;
            if (wanderTimer <= 0) {
                pickNewWanderDirection(); //switch direction
            }
            // תנועה לפי הכיוון
            x += wanderDirection.x * speed * dt;
            y += wanderDirection.y * speed * dt;
        } 
        
        updateBounds(); // update collation
    }

    public void capture() { this.currentState = State.CAPTURED; }
    public ElementType getElement() { return element; }
    public State getCurrentState() { return currentState; }
}