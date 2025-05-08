package com.mygdx.dinosaurio;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {
    private float x, y, speed, scale;
    private Animation<TextureRegion> animation;
    private Rectangle bounds;
    private String type;
    private float stateTime = 0;

    // Constructor original que recibe frames y duración
    public Obstacle(float startX, float startY, String type, TextureRegion[] frames, float speed, float scale, float frameDuration) {
        this.x = startX;
        this.y = startY;
        this.type = type;
        this.speed = speed;
        this.scale = scale;

        animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion firstFrame = frames[0];
        bounds = new Rectangle(x, y, firstFrame.getRegionWidth() * scale, firstFrame.getRegionHeight() * scale);
    }

    // Nuevo constructor para recibir Textures directamente y usar duración por defecto
    public Obstacle(float startX, float startY, String type, Texture[] textures, float speed, float scale) {
        this(startX, startY, type, convertToRegions(textures), speed, scale, 0.1f);
    }

    private static TextureRegion[] convertToRegions(Texture[] textures) {
        TextureRegion[] regions = new TextureRegion[textures.length];
        for (int i = 0; i < textures.length; i++) {
            regions[i] = new TextureRegion(textures[i]);
        }
        return regions;
    }

    public void update(float delta) {
        stateTime += delta;
        x -= speed * delta;
        bounds.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y, currentFrame.getRegionWidth() * scale, currentFrame.getRegionHeight() * scale);
    }

    public boolean isOffScreen() {
        return x + bounds.width < 0;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public String getType() {
        return type;
    }
}
