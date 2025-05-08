package com.mygdx.dinosaurio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.utils.Timer;


public class DinoGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Texture groundTex, cloudsTex, bushTex;
    private Texture[] seagullFrames;

    private Player player;
    private Array<Obstacle> obstacles;

    private float spawnTimer = 0f;
    private float spawnInterval = 3f;
    private float cloudSpeed = 50f;
    private float groundScale = 0.6f;   // suelo más grande
    private float cloudScale = 0.25f;  // nubes pequeñas
    private float bushScale = 2.5f;   // arbustos más grandes
    private float seagullScale = 1.8f;   // gaviotas más grandes

    private float[] cloudX;

    private static final float DOUBLE_TAP_INTERVAL = 0.2f;
    private Timer.Task scheduledJumpTask;


    @Override
    public void create() {

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        camera.update();

        groundTex = new Texture("ground.png");
        cloudsTex = new Texture("clouds.png");
        bushTex = new Texture("bush.png");

        seagullFrames = new Texture[]{
            new Texture("seagull1.png"),
            new Texture("seagull2.png"),
            new Texture("seagull3.png")
        };

        player = new Player(50);
        obstacles = new Array<>();

        cloudX = new float[]{50, 300, 550};

        GestureDetector gd = new GestureDetector(new GestureDetector.GestureAdapter() {
            @Override
            public boolean tap(float x, float y, int count, int button) {
                if (count == 1) {
                    // 1er tap: programamos un salto dentro de DOUBLE_TAP_INTERVAL
                    if (scheduledJumpTask != null) scheduledJumpTask.cancel();
                    scheduledJumpTask = new Timer.Task() {
                        @Override
                        public void run() {
                            player.requestJump();
                        }
                    };
                    Timer.schedule(scheduledJumpTask, DOUBLE_TAP_INTERVAL);
                }
                else if (count == 2) {
                    // 2º tap: cancelamos el salto y hacemos duck
                    if (scheduledJumpTask != null) {
                        scheduledJumpTask.cancel();
                        scheduledJumpTask = null;
                    }
                    player.requestDuck();
                }
                return true;
            }
        });
        Gdx.input.setInputProcessor(gd);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        update(delta);

        // fondo cielo
        Gdx.gl.glClearColor(0.53f, 0.81f, 0.92f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // nubes móviles
        for (int i = 0; i < cloudX.length; i++) {
            batch.draw(cloudsTex,
                cloudX[i], 350,
                cloudsTex.getWidth() * cloudScale,
                cloudsTex.getHeight() * cloudScale
            );
            cloudX[i] -= cloudSpeed * delta;
            if (cloudX[i] + cloudsTex.getWidth() * cloudScale < 0) {
                cloudX[i] = 800;
            }
        }

        // suelo repetido y grande
        float w = groundTex.getWidth() * groundScale;
        float h = groundTex.getHeight() * groundScale;
        for (float x = 0; x < 800; x += w) {
            batch.draw(groundTex, x, 0, w, h * 5.25f);
        }

        // jugador
        player.render(batch);

        // obstáculos
        for (Obstacle o : obstacles) {
            o.render(batch);
        }

        batch.end();
    }


    private void update(float delta) {
        player.update(delta);

        // generar obstáculos
        spawnTimer += delta;
        if (spawnTimer >= spawnInterval) {
            spawnTimer = 0;
            if (Math.random() < 0.5) {
                obstacles.add(new Obstacle(
                    800, Player.GROUND_Y,
                    "bush", new Texture[]{bushTex},
                    350f, bushScale
                ));
            } else {
                obstacles.add(new Obstacle(
                    800, Player.GROUND_Y + 50,
                    "seagull", seagullFrames,
                    350f, seagullScale
                ));
            }
        }

        // actualizar y eliminar
        for (int i = obstacles.size - 1; i >= 0; i--) {
            Obstacle o = obstacles.get(i);
            o.update(delta);
            if (o.isOffScreen()) obstacles.removeIndex(i);
        }

        // colisiones
        for (Obstacle o : obstacles) {
            boolean hitSeagull = o.getType().equals("seagull") && !player.isDucking();
            boolean hitBush = o.getType().equals("bush") && !player.isJumping();
            if (player.getBounds().overlaps(o.getBounds()) && (hitSeagull || hitBush)) {
                player.resetPosition();
                obstacles.clear();
                break;
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        groundTex.dispose();
        cloudsTex.dispose();
        bushTex.dispose();
        for (Texture t : seagullFrames) t.dispose();
        player.dispose();
    }
}
