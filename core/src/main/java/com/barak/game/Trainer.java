package com.barak.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

/**
 * Represents the player character (Trainer) in the game.
 * Handles user input, movement logic, and screen boundary clamping.
 */
public class Trainer extends GameObject {
    
    private float speed;

    public Trainer(Texture texture, float x, float y) {
        super(texture, x, y, 64, 64); 
        this.speed = 250f; // Movement speed in pixels per second
    }

    @Override
    public void update(float dt) {
        // 1. Process Input and Movement (scaled by delta time for consistent speed)
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += speed * dt;
        }

        // 2. Screen Clamping (Prevent the trainer from moving outside the window)
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > screenWidth - width) x = screenWidth - width;
        if (y > screenHeight - height) y = screenHeight - height;

        // 3. Update collision boundaries after movement is resolved
        updateBounds();
    }
}