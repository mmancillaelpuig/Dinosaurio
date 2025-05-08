package com.mygdx.dinosaurio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    public static final float GRAVITY       = -1000f;
    public static final float JUMP_VELOCITY = 450f;
    public static final float GROUND_Y      = 100f;
    public static final float SCALE         = 0.2f;
    private static final float FRAME_TIME   = 0.1f;
    private static final float DUCK_TIME    = 1.0f; // duración del duck automático

    private float x, y, velocityY = 0, stateTime = 0, duckTimer = 0;
    private boolean isJumping = false, isDucking = false;
    private boolean jumpRequested = false, duckRequested = false;

    private Texture run1, run2, duckTex, jumpTex;
    private Rectangle bounds;

    public Player(float startX) {
        this.x = startX;
        this.y = GROUND_Y;
        run1    = new Texture("run.png");
        run2    = new Texture("run2.png");
        duckTex = new Texture("duck.png");
        jumpTex = new Texture("jump.png");
        bounds  = new Rectangle(x, y, run1.getWidth()*SCALE, run1.getHeight()*SCALE);
    }

    /** Llamado desde el GestureDetector para salto */
    public void requestJump() {
        jumpRequested = true;
    }

    /** Llamado desde el GestureDetector para duck */
    public void requestDuck() {
        duckRequested = true;
    }

    public void update(float delta) {
        stateTime += delta;

        // === Procesar Requests ===
        if (duckRequested && !isJumping) {
            isDucking = true;
            duckTimer = DUCK_TIME;
        } else if (jumpRequested && !isJumping) {
            isJumping = true;
            velocityY = JUMP_VELOCITY;
            isDucking = false;
        }
        // Resetear flags
        jumpRequested = duckRequested = false;

        // Tiempo de duck automático
        if (isDucking) {
            duckTimer -= delta;
            if (duckTimer <= 0) {
                isDucking = false;
                duckTimer = 0;
            }
        }

        // Agacharse con tecla abajo (si no está en duck automático ni saltando)
        if (!isDucking && !isJumping) {
            isDucking = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        }

        // === Física de salto/gravedad ===
        if (isJumping) {
            velocityY += GRAVITY * delta;
            y         += velocityY * delta;
            if (y <= GROUND_Y) {
                y = GROUND_Y;
                isJumping = false;
                velocityY = 0;
            }
        }

        bounds.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        Texture cur;
        if (isJumping)      cur = jumpTex;
        else if (isDucking) cur = duckTex;
        else                cur = ((int)(stateTime/FRAME_TIME)%2==0 ? run1 : run2);

        batch.draw(cur, x, y, cur.getWidth()*SCALE, cur.getHeight()*SCALE);
    }

    public Rectangle getBounds() { return bounds; }
    public boolean isDucking()  { return isDucking; }
    public boolean isJumping()  { return isJumping;  }

    public void resetPosition() {
        y = GROUND_Y;
        velocityY = 0;
        isJumping = false;
        isDucking = false;
        duckTimer = 0;
        stateTime = 0;
        jumpRequested = duckRequested = false;
    }

    public void dispose() {
        run1.dispose();
        run2.dispose();
        duckTex.dispose();
        jumpTex.dispose();
    }
}
