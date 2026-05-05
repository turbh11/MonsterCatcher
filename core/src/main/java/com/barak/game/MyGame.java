package com.barak.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * The main game loop and entry point for the application.
 * Manages game state updates, rendering, object pooling, and input handling.
 */
public class MyGame extends ApplicationAdapter {
    private SpriteBatch batch;
    
    // Trainer entities
    private Texture trainerTexture;
    private Trainer trainer;
    
    // Monster entities
    private Array<Monster> monsters;
    private Texture fireTex, waterTex, grassTex, electricTex;
    
    // Projectile entities & Object Pooling
    private Texture sphereTexture;
    private Array<CaptureSphere> activeSpheres;
    private Pool<CaptureSphere> spherePool;

    @Override
    public void create() {
        batch = new SpriteBatch();
        
        trainerTexture = new Texture("trainer.png");
        float startX = (Gdx.graphics.getWidth() / 2f) - 32;
        float startY = (Gdx.graphics.getHeight() / 2f) - 32;
        trainer = new Trainer(trainerTexture, startX, startY);
        
        fireTex = new Texture("fire.png");
        waterTex = new Texture("water.png");
        grassTex = new Texture("grass.png");
        electricTex = new Texture("electric.png");

        monsters = new Array<>();
        ElementType[] elements = ElementType.values();
        
        for (int i = 0; i < 10; i++) {
            float randX = MathUtils.random(0, Gdx.graphics.getWidth() - 48);
            float randY = MathUtils.random(0, Gdx.graphics.getHeight() - 48);
            ElementType randomElement = elements[MathUtils.random(0, elements.length - 1)];
            Texture selectedTex = getTextureForElement(randomElement);
            monsters.add(new Monster(selectedTex, randX, randY, randomElement));
        }
        
        // Initialize CaptureSphere assets and Object Pool
        sphereTexture = new Texture("sphere.png");
        activeSpheres = new Array<>();
        
        // Define the Object Pool behavior
        spherePool = new Pool<CaptureSphere>() {
            @Override
            protected CaptureSphere newObject() {
                // This is the ONLY place 'new' is called for spheres, preventing GC spikes
                return new CaptureSphere(sphereTexture);
            }
        };
    }

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
        float dt = Gdx.graphics.getDeltaTime();
        
        // 1. Input Handling for Shooting
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            shootSphere();
        }

        // 2. Update Logic
        trainer.update(dt);
        for (Monster m : monsters) {
            m.update(dt);
        }
        
        // Update active spheres and return inactive ones to the pool.
        // We iterate BACKWARDS to safely remove items from the array while iterating.
        for (int i = activeSpheres.size - 1; i >= 0; i--) {
            CaptureSphere sphere = activeSpheres.get(i);
            sphere.update(dt);
            
            if (!sphere.isActive()) {
                activeSpheres.removeIndex(i);
                spherePool.free(sphere); // Return object to the pool for future reuse
            }
        }

        // 3. Clear Screen
        Gdx.gl.glClearColor(0.2f, 0.6f, 0.3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 4. Render Phase
        batch.begin();
        for (CaptureSphere s : activeSpheres) s.draw(batch); // Draw spheres below monsters
        for (Monster m : monsters) m.draw(batch);
        trainer.draw(batch);
        batch.end();
    }
    
    /**
     * Calculates the vector between the trainer and the mouse cursor,
     * obtains a sphere from the pool, and fires it.
     */
    private void shootSphere() {
        // Calculate mouse coordinates (libGDX Y-axis is inverted by default for input)
        float targetX = Gdx.input.getX();
        float targetY = Gdx.graphics.getHeight() - Gdx.input.getY();
        
        // Calculate the vector pointing from the trainer to the mouse
        float originX = trainer.x + 32; // Center of the trainer
        float originY = trainer.y + 32;
        
        Vector2 direction = new Vector2(targetX - originX, targetY - originY);
        
        // Normalize the vector (set its length to 1) so it solely represents direction
        if (!direction.isZero()) {
            direction.nor();
            
            // Obtain a sphere from the pool instead of instantiating a new one
            CaptureSphere sphere = spherePool.obtain();
            sphere.init(originX - 12, originY - 12, direction); // -12 to center the 24x24 sphere
            activeSpheres.add(sphere);
        }
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (trainerTexture != null) trainerTexture.dispose();
        if (fireTex != null) fireTex.dispose();
        if (waterTex != null) waterTex.dispose();
        if (grassTex != null) grassTex.dispose();
        if (electricTex != null) electricTex.dispose();
        if (sphereTexture != null) sphereTexture.dispose();
    }
}