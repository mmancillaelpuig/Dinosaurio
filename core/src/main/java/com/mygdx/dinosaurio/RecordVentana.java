package com.mygdx.dinosaurio;

import static com.badlogic.gdx.Input.Keys.CENTER;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class RecordVentana {
    private final BitmapFont fontRed;
    private final BitmapFont fontWhite;
    private final GlyphLayout layout = new GlyphLayout();
    private TextButton menu;

    private boolean active = false;
    private int metros = 0;
    private final float worldWidth, worldHeight;

    public RecordVentana(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        fontRed = new BitmapFont();
        fontRed.getData().setScale(3f);
        fontRed.setColor(Color.RED);

        fontWhite = new BitmapFont();
        fontWhite.getData().setScale(2f);
        fontWhite.setColor(Color.WHITE);
    }

    public void show(int metros) {
        this.metros = metros;
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }

    public boolean update() {
        if (!active) return false;
        if (Gdx.input.justTouched()) {
            active = false;
            return true;
        }
        return false;
    }

    public void render(SpriteBatch batch) {
        if (!active) return;

        batch.end();
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        batch.begin();

        String over = "ELIMINADO";
        layout.setText(fontRed, over);
        fontRed.draw(batch, over, (worldWidth - layout.width) / 2, (worldHeight + layout.height) / 2 + 100);

        String info = "Metros: " + metros;
        layout.setText(fontWhite, info, Color.WHITE, worldWidth, CENTER, false);
        float textY = (worldHeight - layout.height) / 2;
        fontWhite.draw(batch, layout, 0, textY + layout.height);
    }

    public void dispose() {
        fontRed.dispose();
        fontWhite.dispose();
    }
}
