package com.mygdx.dinosaurio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.Timer;


public class DinoGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Contador contador;
    private BitmapFont fuente;
    private GlyphLayout layout;
    private RecordVentana recordVentana;
    private MenuVentana menuVentana;

    private Texture groundTex, cloudsTex, bushTex;
    private Texture[] seagullFrames;

    private Player player;
    private Array<Obstacle> obstacles;

    private float spawnTimer = 0f;
    private float spawnInterval = 3f;
    private float cloudSpeed = 50f;
    private float groundScale = 0.6f;
    private float cloudScale = 0.25f;
    private float bushScale = 2.5f;
    private float seagullScale = 1.8f;
    private float obstacleSpeed = 350f;

    private float[] cloudX;

    private static final float DOUBLE_TAP_INTERVAL = 0.2f;
    private Timer.Task scheduledJumpTask;


    @Override
    public void create() {

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        contador = new Contador();
        fuente = new BitmapFont();
        menuVentana = new MenuVentana();
        recordVentana = new RecordVentana(800, 400);
        fuente.getData().scale(2);
        fuente.setColor(Color.BLACK);
        layout = new GlyphLayout();
        camera.setToOrtho(false, 800, 480);
        camera.update();

        groundTex = new Texture("ground.png");
        cloudsTex = new Texture("clouds.png");
        bushTex = new Texture("bush.png");

        seagullFrames = new Texture[]{new Texture("seagull1.png"), new Texture("seagull2.png"), new Texture("seagull3.png")};

        player = new Player(50);
        obstacles = new Array<>();

        cloudX = new float[]{50, 300, 550};

        GestureDetector gd = new GestureDetector(new GestureDetector.GestureAdapter() {
            @Override
            public boolean tap(float x, float y, int count, int button) {
                if (count == 1) {
                    if (scheduledJumpTask != null) scheduledJumpTask.cancel();
                    scheduledJumpTask = new Timer.Task() {
                        @Override
                        public void run() {
                            player.requestJump();
                        }
                    };
                    Timer.schedule(scheduledJumpTask, DOUBLE_TAP_INTERVAL);
                } else if (count == 2) {
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


        if (menuVentana.isActive()) {
            if (menuVentana.update()) {
            }
            menuVentana.render(batch);
            return;
        }


        if (recordVentana.isActive()) {
            if (recordVentana.update()) {
                reiniciarJuego();
            }
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            recordVentana.render(batch);
            batch.end();
            return;
        }

        update(delta);

        Gdx.gl.glClearColor(0.53f, 0.81f, 0.92f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        for (int i = 0; i < cloudX.length; i++) {
            batch.draw(cloudsTex, cloudX[i], 350, cloudsTex.getWidth() * cloudScale, cloudsTex.getHeight() * cloudScale);
            cloudX[i] -= cloudSpeed * delta;
            if (cloudX[i] + cloudsTex.getWidth() * cloudScale < 0) {
                cloudX[i] = 800;
            }
        }

        float w = groundTex.getWidth() * groundScale;
        float h = groundTex.getHeight() * groundScale;
        for (float x = 0; x < 800; x += w) {
            batch.draw(groundTex, x, 0, w, h * 5.25f);
        }

        player.render(batch);

        for (Obstacle o : obstacles) {
            o.render(batch);
        }


        String metrosStr = "Metros: " + contador.getMetros();
        layout.setText(fuente, metrosStr);

        float x = 800 - layout.width - 20;
        float y = 480 - 20;
        fuente.draw(batch, layout, x, y);

        recordVentana.render(batch);

        batch.end();
    }


    private void update(float delta) {
        player.update(delta);

        if (contador.update(delta)) {
            obstacleSpeed += 5f;
        }


        // generar obstáculos
        spawnTimer += delta;
        if (spawnTimer >= spawnInterval) {
            spawnTimer = 0;
            if (Math.random() < 0.5) {
                obstacles.add(new Obstacle(800, Player.GROUND_Y + 10, "bush", new Texture[]{bushTex}, obstacleSpeed, bushScale));
            } else {
                obstacles.add(new Obstacle(800, Player.GROUND_Y + 50, "seagull", seagullFrames, obstacleSpeed, seagullScale));
            }
        }


        for (int i = obstacles.size - 1; i >= 0; i--) {
            Obstacle o = obstacles.get(i);
            o.update(delta);
            contador.update(delta);
            System.out.println("Velocidad " + o.getSpeed());
            if (o.isOffScreen()) obstacles.removeIndex(i);

        }

        for (Obstacle o : obstacles) {
            boolean hitSeagull = o.getType().equals("seagull") && !player.isDucking();
            boolean hitBush = o.getType().equals("bush") && !player.isJumping();
            if (player.getBounds().overlaps(o.getBounds()) && (hitSeagull || hitBush)) {
                recordVentana.show(contador.getMetros());
                return;
            }
        }
    }


    private void reiniciarJuego() {
        player.resetPosition();
        contador.reset();
        obstacles.clear();
        obstacleSpeed = 350f;
        spawnTimer = 0;
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
