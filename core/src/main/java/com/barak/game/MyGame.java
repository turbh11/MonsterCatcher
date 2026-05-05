package com.barak.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * The main game loop and entry point for the application.
 * Manages game state updates, rendering, and resource disposal.
 */
public class MyGame extends ApplicationAdapter {
    private SpriteBatch batch;
    
    // Trainer entities
    private Texture trainerTexture;
    private Trainer trainer;
    
    // Monster entities
    private Array<Monster> monsters; // Using libGDX Array instead of ArrayList to minimize Garbage Collection
    private Texture fireTex, waterTex, grassTex, electricTex;

    @Override
    public void create() {
        batch = new SpriteBatch();
        
        // Load trainer
        trainerTexture = new Texture("trainer.png");
        float startX = (Gdx.graphics.getWidth() / 2f) - 32;
        float startY = (Gdx.graphics.getHeight() / 2f) - 32;
        trainer = new Trainer(trainerTexture, startX, startY);
        
        // Load monster textures
        fireTex = new Texture("fire.png");
        waterTex = new Texture("water.png");
        grassTex = new Texture("grass.png");
        electricTex = new Texture("electric.png");

        // Initialize and populate the monsters array
        monsters = new Array<>();
        ElementType[] elements = ElementType.values();
        
        for (int i = 0; i < 10; i++) {
            float randX = MathUtils.random(0, Gdx.graphics.getWidth() - 48);
            float randY = MathUtils.random(0, Gdx.graphics.getHeight() - 48);
            
            ElementType randomElement = elements[MathUtils.random(0, elements.length - 1)];
            Texture selectedTex = getTextureForElement(randomElement);
            
            monsters.add(new Monster(selectedTex, randX, randY, randomElement));
        }
    }

    /**
     * Helper method to map an ElementType to its corresponding Texture.
     */
    private Texture getTextureForElement(ElementType element) {
        switch (element) {
            case FIRE: return fireTex;
            case WATER: return waterTex;
            case GRASS: return grassTex;
            case ELECTRIC: return electricTex;
            default: return fireTex;
        }
    }

    @Override
    public void render() {
        // 1. Update Phase
        float dt = Gdx.graphics.getDeltaTime();
        trainer.update(dt);
        for (Monster m : monsters) {
            m.update(dt);
        }

        // 2. Clear Screen Phase
        Gdx.gl.glClearColor(0.2f, 0.6f, 0.3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 3. Render Phase (Z-indexing: Draw monsters first, then trainer on top)
        batch.begin();
        for (Monster m : monsters) {
            m.draw(batch);
        }
        trainer.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        // Free up GPU memory for all textures
        if (batch != null) batch.dispose();
        if (trainerTexture != null) trainerTexture.dispose();
        if (fireTex != null) fireTex.dispose();
        if (waterTex != null) waterTex.dispose();
        if (grassTex != null) grassTex.dispose();
        if (electricTex != null) electricTex.dispose();
    }
}