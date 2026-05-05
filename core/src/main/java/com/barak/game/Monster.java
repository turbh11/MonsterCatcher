package com.barak.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Updated Monster class with follow-the-leader logic.
 */
public class Monster extends GameObject {
    
    public enum State { WANDERING, CAPTURED }

    private ElementType element;
    private State currentState;
    private float movementSpeed = 100f;
    private Vector2 wanderDirection;
    private float wanderTimer;

    public Monster(Texture texture, float x, float y, ElementType element) {
        super(texture, x, y, 48, 48);
        this.element = element;
        this.currentState = State.WANDERING;
        this.wanderDirection = new Vector2();
        pickNewWanderDirection();
    }

    private void pickNewWanderDirection() {
        float angle = MathUtils.random(0f, 360f);
        wanderDirection.set(1, 0).setAngleDeg(angle);
        wanderTimer = MathUtils.random(1f, 3f);
    }

    /**
     * Updated update method to handle following a target if captured.
     * @param dt Delta time.
     * @param target The GameObject to follow (Trainer or another Monster).
     */
    public void update(float dt, GameObject target) {
        if (currentState == State.WANDERING) {
            handleWandering(dt);
        } else if (currentState == State.CAPTURED && target != null) {
            handleFollowing(dt, target);
        }
        updateBounds();
    }

    private void handleWandering(float dt) {
        wanderTimer -= dt;
        if (wanderTimer <= 0) pickNewWanderDirection();
        
        x += wanderDirection.x * movementSpeed * dt;
        y += wanderDirection.y * movementSpeed * dt;
        
        // שימוש ב-Gdx.graphics כדי לקבל את גודל המסך האמיתי בכל רגע
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // בדיקת גבולות ותיקון מיקום (Clamping)
        if (x < 0) {
            x = 0;
            pickNewWanderDirection(); // שינוי כיוון מידי כדי שלא ייתקע על הקיר
        } else if (x > screenWidth - width) {
            x = screenWidth - width;
            pickNewWanderDirection();
        }

        if (y < 0) {
            y = 0;
            pickNewWanderDirection();
        } else if (y > screenHeight - height) {
            y = screenHeight - height;
            pickNewWanderDirection();
        }
    }

    private void handleFollowing(float dt, GameObject target) {
        // Calculate distance to target
        float dx = (target.x + target.width/2) - (this.x + this.width/2);
        float dy = (target.y + target.height/2) - (this.y + this.height/2);
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // Follow only if distance is greater than 60 units to prevent overlapping
        if (distance > 60f) {
            Vector2 direction = new Vector2(dx, dy).nor();
            x += direction.x * (movementSpeed * 2.5f) * dt; // Captured monsters move faster to keep up
            y += direction.y * (movementSpeed * 2.5f) * dt;
        }
    }

    @Override
    public void update(float dt) {
        // Placeholder for the abstract method, usually called without a target
        update(dt, null);
    }

    public void capture() {
        this.currentState = State.CAPTURED;
    }

    public State getCurrentState() { return currentState; }
    public ElementType getElement() { return element; }
}