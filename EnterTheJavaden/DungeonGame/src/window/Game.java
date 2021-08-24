// Handles most of the game stuff, execute the game loop, rendering, etc...

package window;

import Audio.AudioHandler;
import framework.*;
import objects.Player;
import textures.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Audio.AudioPlayer;

public class Game extends Canvas implements Runnable{
//    public static final int WIDTH = 1900;
//    public static final int WIDTH = 1200;
//    public static final int HEIGHT = 1000;
//    public static final int HEIGHT = 800;

    // Game speed - default 60.0
    private double gameSpeed = 60.0;

//    @Serial
//    private static final long serialVersionUID = -7516887150698980623L;

    private boolean running = false;
    private Thread thread;
    public Handler handler;
    public Camera camera;
    public SpriteSheet ss;
    public Window window;
    private KeyInput ki;

    private BufferedImage level = null;
    private BufferedImage level1 = null;
    private BufferedImage level2 = null;
    private BufferedImage sprite_sheet = null;
    private BufferedImage floor = null;
    private int floor_width = 500;
    private int floor_height = 500;

    public GameObject damagedObject = null;
    private int playerHP = 100;
    private boolean playerDead = false;
    private int playerScore;
    private boolean scoreUpdated = false;
    private boolean gotTreasure = false;
    private boolean roundWon = false; // make getter for menu to know if round was won.

    public AudioHandler audioHandler; // probably redundant

    MouseInput mInput;

    private int w,h;
    private int lvl;

    int roundEndWait = 0;


    // Constructor
    public Game(int lvl, int w, int h){
        this.lvl = lvl;
        this.w = w;
        this.h = h;

        // main game handler
        handler = new Handler();
        audioHandler = new AudioHandler();
        // Map level
        BufferedImageLoader loader = new BufferedImageLoader();
        level = loader.loadImage("/Levels/Lvl"+lvl+".png");
        sprite_sheet = loader.loadImage("/textures/Sprite_sheet_64x64.png");
        floor = loader.loadImage("/textures/space-tilesArtboard-1-copy-2 (2).png");
        ss = new SpriteSheet(sprite_sheet);
        // Camera setup
        int lvlX = level.getWidth() * SpriteSheet.imageSize;
        int lvlY = level.getHeight() * SpriteSheet.imageSize;
        camera = new Camera(0,0, lvlX, lvlY, w, h);
        // Define listeners
        this.ki = new KeyInput(handler);
        this.addKeyListener(ki);
        this.mInput = new MouseInput(handler, camera,ss);
        this.addMouseListener(mInput);
        this.addMouseMotionListener(mInput);



        // Load level
        loader.loadLevel(level, this);
    }

//    public synchronized void start(){
    public void start(){
        this.window = new Window(w, h, "Level " + this.lvl, this);
        // failsafe so we dont run multiple threads
        if (running)
            return;

        running = true;
        thread = new Thread(this) ;
        thread. start();
        System.out.println("Thread Started");
    }

    private void stop(){
        long timer = System.currentTimeMillis();
        while (System.currentTimeMillis() - timer < 6000){

        }


        this.window.frame.setVisible(false);
        this.window.frame.dispose();
        this.removeKeyListener(ki);
//        thread.stop();
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Call this methods when starting a new thread. Game loop is here
    @Override
    // Game loop cycles, updates, and renders the game. 60 times/sec
    public void run() {
        System.out.println("Thread running");
//        init();
//        this.requestFocus();
        // General Game loop used for basic game development from notch
        long lastTime = System.nanoTime();
        double amountOfTicks = gameSpeed; // default 60. Affects game speed
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;


        while(running){
//            if (this.window.isWinClosed()){
            if (Window.closeCheck){
                closeGame();
            }
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1){
                tick();
                updates++;
                delta--;
            }
            render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
//                System.out.println("FPS: " + frames + " TICKS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
        stop();
    }

    private void closeGame(){
        this.window.frame.setVisible(false);
        this.window.frame.dispose();
        this.removeKeyListener(ki);
        this.removeMouseListener(mInput);
//        thread.stop();
        running = false;
    }

    // define death graphics and rules
    private void death(Graphics g){
        int alpha = 200; // 50% transparent
        Color myColour = new Color(80, 80, 80, alpha);
        g.setColor(myColour);
        g.fillRect(0,0,w,h);

        Font Elephant = new Font("Elephant", Font.BOLD, 70);
        g.setFont(Elephant);
        g.setColor(Color.RED);
        g.drawString("WASTED", w/2-150, h/2);

        playerDead = true;
        this.removeMouseListener(mInput); // turn off mouse presses
    }

    // define round win graphics and rules
    private void treasure(Graphics g){
        int alpha = 200; // 50% transparent
        Color myColour = new Color(255, 215, 0, alpha);
        g.setColor(myColour);
        g.fillRect(0,0,w,h);

        Font Elephant = new Font("Elephant", Font.BOLD, 70);
        g.setFont(Elephant);
        g.setColor(Color.WHITE);
        g.drawString("YOU GOT GOLD!", w/2-280, h/2);

        roundWon = true;
        this.removeMouseListener(mInput); // turn off mouse presses
    }

    public boolean isRunning() {
        return running;
    }

    public void updateHP(Graphics g, Camera cam, int hp, int MAX_HP){
        float normalizer = 100/(float)MAX_HP;
        int normalHP = (int)(hp*normalizer);
        // Draw HUD
        int camx = (int)cam.getX();
        int camy = (int)cam.getY();
        int camXoffset = 5;
        int camYoffset = 110;
        int sliderHeight = 10;
        int sliderWidth = 100;

        // HP
        g.setColor(Color.gray);
        g.fillRect(camx+camXoffset,camy+camYoffset,sliderWidth,sliderHeight);
        if (normalHP >= 50)
            g.setColor(Color.green);
        if (normalHP < 50 && normalHP >= 25)
            g.setColor(Color.yellow);
        if (normalHP < 25)
            g.setColor(Color.red);
        g.fillRect(camx+camXoffset,camy+camYoffset, normalHP ,sliderHeight);
        g.setColor(Color.black);
        g.drawRect(camx+camXoffset,camy+camYoffset,sliderWidth,sliderHeight);
    }

    public void setPlayerScore(int score){
        this.playerScore = score;
    }
    // render/draw everything in the game
    private void render() {
        // The BufferStrategy class represents the mechanism with which
        // to organize complex memory on a particular Canvas or Window
        BufferStrategy bs = this.getBufferStrategy();
        // initially bs is null so we need to initiate it
        if(bs == null){
            // triple frame preload buffer queue
            this.createBufferStrategy(3);
            return;
        }

        // Creates a graphics context for the drawing buffer.
        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g; // cast g to 2D graphics
        // ############################## DRAW GAME ############################################################ DRAW GAME ##############################
        // translate origin point for camera
        g2d.translate(-camera.getX(), -camera.getY());

            for (int xx = 0; xx < 64*100; xx+= floor_width){
                for (int yy= 0;yy<64*90; yy+= floor_height){
                    g.drawImage(floor, xx, yy, null);
                }
            }
            handler.render(g);

            // Damaged object HUD
            if (damagedObject != null)
                updateHP(g, camera,  damagedObject.getHp(),  damagedObject.getMAX_HP());


//            g.drawString("BattleCunt 69", 100, 100);

        g2d.translate(camera.getX(), camera.getY());
        // HUD
//        g.setColor(Color.gray);
//        g.fillRect(5,5,200,64);
        // ##############################################################################################################################################

        // Death checker
        if (playerDead) {
            if (roundEndWait < 5)
                roundEndWait +=1;
            else{
                new AudioPlayer("/Audio/SFX/Gta v death sound effect.mp3").play();
                stop();
            }
        }
        if (playerHP <= 0){
            death(g);
        }


        if (roundWon) {
            if (roundEndWait < 5)
                roundEndWait +=1;
            else{
//                new AudioPlayer("/Audio/SFX/Gta v death sound effect.mp3").play(); // change to treasure sound
                stop();
            }
        }
        if (gotTreasure){
            treasure(g);
        }

        g.dispose();
        bs.show();
    }

    // update everything in the game
    private void tick() {
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            // only follow player
            if (tempObject.getId() == ObjectId.Player) {
                camera.tick(tempObject);
                playerHP = tempObject.getHp();

                // update score from last level
                if (!scoreUpdated){
//                    System.out.println("updating " + playerScore);
                    ((Player)tempObject).incScore(playerScore);
                    scoreUpdated = true;
                }
                playerScore = ((Player)tempObject).getScore();
                gotTreasure = ((Player)tempObject).gotTreasure();
            }
        }
        handler.tick();

    }

    public int getPlayerScore() {
        return this.playerScore;
    }

    public boolean isRoundWon() {
        return roundWon;
    }

//    public static void main(String args[]){
//        new Game(1);
//    }
}
