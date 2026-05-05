package com.barak.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class MyGame extends ApplicationAdapter {
    private SpriteBatch batch;
    
    // מאמן
    private Texture trainerTexture;
    private Trainer trainer;
    
    // מפלצות
    private Array<Monster> monsters;
    private Texture fireTex, waterTex, grassTex, electricTex;

    @Override
    public void create() {
        batch = new SpriteBatch();
        
        // טעינת מאמן
        trainerTexture = new Texture("trainer.png");
        trainer = new Trainer(trainerTexture, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        
        // טעינת תמונות מפלצות
        fireTex = new Texture("fire.png");
        waterTex = new Texture("water.png");
        grassTex = new Texture("grass.png");
        electricTex = new Texture("electric.png");

        // יצירת מערך המפלצות (השתמשנו ב-Array של libGDX לביצועים טובים)
        monsters = new Array<>();
        
        // לולאה שמייצרת 10 מפלצות אקראיות
        for (int i = 0; i < 10; i++) {
            float randX = MathUtils.random(0, Gdx.graphics.getWidth() - 48);
            float randY = MathUtils.random(0, Gdx.graphics.getHeight() - 48);
            
            Monster.ElementType[] elements = Monster.ElementType.values();
            Monster.ElementType randomElement = elements[MathUtils.random(0, elements.length - 1)];
            
            Texture selectedTex = getTextureForElement(randomElement);
            monsters.add(new Monster(selectedTex, randX, randY, randomElement));
        }
    }

    // פונקציית עזר שמתאימה את התמונה לאלמנט
    private Texture getTextureForElement(Monster.ElementType element) {
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
        
        // 1. עדכון לוגיקה (Update)
        trainer.update(dt);
        for (Monster m : monsters) {
            m.update(dt);
        }

        // 2. ניקוי מסך
        Gdx.gl.glClearColor(0.2f, 0.6f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 3. ציור (Render)
        batch.begin();
        
        for (Monster m : monsters) {
            m.draw(batch); // מציירים קודם את המפלצות
        }
        trainer.draw(batch); // המאמן יצויר מעליהן
        
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        trainerTexture.dispose();
        fireTex.dispose();
        waterTex.dispose();
        grassTex.dispose();
        electricTex.dispose();
    }
}