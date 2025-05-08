package com.mygdx.dinosaurio;

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

    public Obstacle(float startX, float startY, String type,
                    TextureRegion[] frames, float speed, float scale,
                    float frameDuration) {
        this.x = startX;
        this.y = startY;
        this.type = type;
        this.speed = speed;
        this.scale = scale;

        // Crear animación
        animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        // Usar el primer frame para las dimensiones
        TextureRegion firstFrame = frames[0];
        bounds = new Rectangle(x, y,
            firstFrame.getRegionWidth() * scale,
            firstFrame.getRegionHeight() * scale);
    }

    public void update(float delta) {
        stateTime += delta;
        x -= speed * delta;
        bounds.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame,
            x, y,
            currentFrame.getRegionWidth() * scale,
            currentFrame.getRegionHeight() * scale);
    }

    public boolean isOffScreen() {
        return x + bounds.width < 0;
    }

    public Rectangle getBounds() { return bounds; }
    public String getType() { return type; }
}
