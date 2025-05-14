package com.mygdx.dinosaurio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuVentana {
    private final BitmapFont font, font2;
    private final GlyphLayout layout, layout2;
    private boolean active = true;
    private Texture cloudsTex;

    public MenuVentana() {
        font = new BitmapFont();
        font2 = new BitmapFont();
        layout = new GlyphLayout();
        layout2 = new GlyphLayout();
        cloudsTex = new Texture("clouds.png");
        font.getData().setScale(8f);
        font.setColor(Color.BLUE);
        font2.getData().setScale(3f);
        font2.setColor(Color.DARK_GRAY);
    }

    public boolean update() {
        if (active && Gdx.input.justTouched()) {
            active = false;
            return true;
        }
        return false;
    }


    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0.53f, 0.81f, 0.92f, 1f);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        String text = "JUGAR";
        String text2 = "By Marc Mancilla";
        layout.setText(font, text);
        layout2.setText(font2, text2);

        float x = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float x2 = (Gdx.graphics.getWidth() - layout2.width) / 2f;
        float y = (Gdx.graphics.getHeight() + layout.height) / 2.5f;
        float y2 = (Gdx.graphics.getHeight() + layout2.height) / 3.2f;

        batch.begin();
        font.draw(batch, layout, x, y);
        font2.draw(batch, layout2, x2, y2);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 1.25f, Gdx.graphics.getHeight() / 1.5f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 5.25f, Gdx.graphics.getHeight() / 1.5f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 1.8f, Gdx.graphics.getHeight() / 8.5f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 8.8f, Gdx.graphics.getHeight() / 9f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 6.8f, Gdx.graphics.getHeight() / 2.7f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 1.4f, Gdx.graphics.getHeight() / 7.1f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 2.4f, Gdx.graphics.getHeight() / 1.2f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 3.4f, Gdx.graphics.getHeight() / 1.6f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 5.4f, Gdx.graphics.getHeight() / 6.2f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 1.6f, Gdx.graphics.getHeight() / 3.2f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 1.8f, Gdx.graphics.getHeight() / 2.2f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 1.1f, Gdx.graphics.getHeight() / 9.2f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 2.2f, Gdx.graphics.getHeight() / 15.2f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 3.5f, Gdx.graphics.getHeight() / 15.2f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 3.5f, Gdx.graphics.getHeight() / 2.2f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 1.05f, Gdx.graphics.getHeight() / 2.2f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 1.15f, Gdx.graphics.getHeight() / 2.0f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 1.55f, Gdx.graphics.getHeight() / 1.8f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 18.55f, Gdx.graphics.getHeight() / 1.8f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 25.55f, Gdx.graphics.getHeight() / 10.8f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 45.55f, Gdx.graphics.getHeight() / 2.8f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 100.55f, Gdx.graphics.getHeight() / 1.1f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 2.1f, Gdx.graphics.getHeight() / 1.5f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 1.1f, Gdx.graphics.getHeight() / 1.1f);
        batch.draw(cloudsTex, Gdx.graphics.getWidth() / 1.3f, Gdx.graphics.getHeight() / 2.3f);

        batch.end();
    }

    public boolean isActive() {
        return active;
    }

    public void dispose() {
        font.dispose();
    }
}
