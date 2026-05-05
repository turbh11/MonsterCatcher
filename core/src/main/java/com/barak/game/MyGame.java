package com.barak.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture trainerTexture;
    private Trainer trainer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        
        // טעינת תמונת המאמן.
        // שים לב: אם אין לך כרגע תמונה כזאת, תוכל לשנות את השם ל-"libgdx.png" או "badlogic.jpg"
        // (תמונות ברירת המחדל שכנראה כבר קיימות בתיקיית ה-assets שלך).
        trainerTexture = new Texture("trainer.png");
        
        // מיקום המאמן באמצע המסך
        float startX = Gdx.graphics.getWidth() / 2f - 32;
        float startY = Gdx.graphics.getHeight() / 2f - 32;
        
        trainer = new Trainer(trainerTexture, startX, startY);
    }

    @Override
    public void render() {
        // 1. קריאה לעדכון הלוגיקה (תנועה)
        float dt = Gdx.graphics.getDeltaTime();
        trainer.update(dt);

        // 2. ניקוי המסך - שמנו צבע ירוק דשא כדי שירגיש כמו עולם פתוח
        Gdx.gl.glClearColor(0.2f, 0.6f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 3. ציור המאמן על המסך
        batch.begin();
        trainer.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        // שחרור משאבים בסיום למניעת דליפות זיכרון
        batch.dispose();
        trainerTexture.dispose();
    }
}