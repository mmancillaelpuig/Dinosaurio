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

    public Obstacle(float startX, float startY, String type, TextureRegion[] frames, float scale, float frameDuration) {
        this.x = startX;
        this.y = startY;
        this.type = type;
        this.scale = scale;

        animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion firstFrame = frames[0];
        bounds = new Rectangle(x, y, firstFrame.getRegionWidth() * scale, firstFrame.getRegionHeight() * scale);
    }

    public Obstacle(float startX, float startY, String type, Texture[] textures, float speed, float scale) {
        this(startX, startY, type, convertToRegions(textures), scale, 0.1f);
        this.speed = speed;

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

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }
}
