package com.barak.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The main game loop and entry point for the application.
 * Manages game state updates, rendering, and resource disposal.
 */
public class MyGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture trainerTexture;
    private Trainer trainer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        
        // Load the trainer texture from the assets folder
        trainerTexture = new Texture("trainer.png");
        
        // Position the trainer exactly in the center of the screen
        float startX = (Gdx.graphics.getWidth() / 2f) - 32;
        float startY = (Gdx.graphics.getHeight() / 2f) - 32;
        
        trainer = new Trainer(trainerTexture, startX, startY);
    }

    @Override
    public void render() {
        // 1. Update Phase: Calculate delta time and update game logic
        float dt = Gdx.graphics.getDeltaTime();
        trainer.update(dt);

        // 2. Clear Screen Phase: Fill background with a nice grass-green color
        Gdx.gl.glClearColor(0.2f, 0.6f, 0.3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 3. Render Phase: Draw all visible game objects
        batch.begin();
        trainer.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        // Free up GPU memory to prevent memory leaks
        if (batch != null) batch.dispose();
        if (trainerTexture != null) trainerTexture.dispose();
    }
}