// This method is the main method for running the game and the game menu
// The menu allows the user the pick a resolution before starting the game

package window;

import Audio.AudioPlayer;
import framework.BufferedImageLoader;
import framework.MenuKeyInput;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MainGameMenu extends Canvas implements Runnable{

    public boolean running = false;
    private boolean gameRunning = false;
    private Thread thread;
    public void setMenuSelection(int menuSelection) {
        this.menuSelection = menuSelection;
    }


    private int menuSelection = -1;
    private boolean selected = false;

    int w, h;
    boolean endGame;

    private Window window;
    private MenuKeyInput mki;

    private BufferedImage startWallpaper;
    private BufferedImage endWallpaper;

    private AudioPlayer menuMusic;
    private ArrayList<AudioPlayer> gameMusic = new ArrayList<AudioPlayer>(); // Create an ArrayList of AudioPlayers

    private static final int NUM_OF_LEVELS = 1;
    Game level;

    private int finalScore = 0;
    // array of available resolutions
    int[][] res = {
            {1900,1000},
            {1440,900},
            {1280,800},
            {3800,2000}
    };

    // Constructor
    public MainGameMenu(int w, int h, boolean endGame, int score){
        Window.closeCheck = false;
        this.finalScore = score;
        this.endGame = endGame;
        this.w=w;
        this.h=h;

        this.mki = new MenuKeyInput(this);
        this.addKeyListener(mki);
        BufferedImageLoader loader = new BufferedImageLoader();
        this.startWallpaper = loader.loadImage("/framework/spacecado.jpg");
        this.endWallpaper = loader.loadImage("/framework/spacecado.jpg");

        if (endGame)
            menuMusic = new AudioPlayer("/Audio/Music/The Final Countdown 8 Bit.mp3");
        else
            menuMusic = new AudioPlayer("/Audio/Music/Weeknd.mp3");

//        gameMusic.add(new AudioPlayer("/Audio/Music/Death Pamphlet.mp3"));
        gameMusic.add(new AudioPlayer("/Audio/Music/Blood Stain.mp3"));
//        gameMusic.add(new AudioPlayer("/Audio/Music/Sorrows.mp3"));

        if (endGame)
            start();
    }

    public void start(){
        if (endGame)
            this.window = new Window(w, h, "End Game Menu", this);
        else
            this.window = new Window(w, h, "Game Menu", this);

        menuMusic.play();

        // failsafe so we dont run multiple threads
        if (running)
            return;

        running = true;
        thread = new Thread(this) ;
        thread. start();
//        System.out.println("Menu Thread Started");
    }

    @Override
    public void run() {
        while (running){
            if (Window.closeCheck){
                closeMenu();
            }

            if (endGame)
                endGameMenu(finalScore);
            else
                preGameMenu();
        }

        levelCycler();
    }


    // an improved level cycler that can cycle through many levels
    private void levelCycler(){
        long timer = System.currentTimeMillis();
        int prevRoundScore = 0;
//        Window gameWindow = null;



        for (int i = 1; i <=NUM_OF_LEVELS; i++) {
            if (!Window.closeCheck) {

                gameMusic.get(i - 1).play();
                // do..while for restart upon death (unlimited retries)
                do {
                    this.level = new Game(i, res[menuSelection][0], res[menuSelection][1]);
                    level.setPlayerScore(prevRoundScore);
                    level.start();
                    while (level.isRunning() && !Window.closeCheck) {
                        if (System.currentTimeMillis() - timer > 1000) {
                            timer = System.currentTimeMillis();
                            System.out.println("Playing level " + i);
                        }
                    }
                } while (!level.isRoundWon() && !Window.closeCheck);
//            } while (!level.isRoundWon() && !level.window.isWinClosed()); // add game window check
                prevRoundScore = level.getPlayerScore();
                gameMusic.get(i - 1).stop();
            }
        }
        if (!Window.closeCheck)
            new MainGameMenu(1200, 800, true, prevRoundScore);
    }

    private boolean endGameMenu(int score){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            // triple frame preload buffer queue
            this.createBufferStrategy(3);
            return false;
        }

        Graphics g = bs.getDrawGraphics();

        g.drawImage(endWallpaper,0,0,null);


        Font ariel = new Font("Elephant", Font.PLAIN, 25);
        Font arielBig = new Font("Elephant", Font.PLAIN, 50);
        Font arielBig2 = new Font("Elephant", Font.PLAIN, 70);
        g.setFont(arielBig2);
        g.setColor(Color.WHITE);
        g.drawString("Space Avocados", w/2-280, 70);
        g.setFont(arielBig);
        g.setColor(Color.RED);
        g.drawString("Thanks for playing", w/2-230, 700);
        g.setColor(Color.YELLOW);
        g.drawString("Your score is: " + score, w/2-200, 750);


        g.dispose();
        bs.show();
        return false;
    }

    private boolean preGameMenu() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            // triple frame preload buffer queue
            this.createBufferStrategy(3);
            return false;
        }

        Graphics g = bs.getDrawGraphics();
//        Graphics2D g2d = (Graphics2D) g;

        g.drawImage(startWallpaper,0,0,null);


        // Show text
        Font ariel = new Font("Elephant", Font.PLAIN, 25);
        Font arielBig = new Font("Elephant", Font.PLAIN, 50);
        Font arielBig2 = new Font("Elephant", Font.PLAIN, 70);
        g.setFont(arielBig2);
        g.setColor(Color.WHITE);
        g.drawString("Space Avocados", w/2-280, 70);
        g.setFont(arielBig);
        g.setColor(Color.RED);
        g.drawString("PRESS ENTER TO START", w/2-400, 600);
        g.setFont(ariel);
        g.setColor(Color.WHITE);
        int Ymenu = 650;
        g.drawString("Rescue the sashimi", 50, Ymenu-150);
        g.drawString("WASD to move, left click to shoot", 50, Ymenu-120);
        g.drawString("Or Choose Resolution: (Default 1900 x 1000)", 50, Ymenu);
        g.drawString("1. 1440 x 900", 50, Ymenu + 30);
        g.drawString("2. 1280 x 800", 50, Ymenu+ 60 );
        g.drawString("3. 3800 x 2000 (4K)", 50, Ymenu+ 90);


        if (this.menuSelection > -1) {
            closeMenu();
        }

        g.dispose();
        bs.show();
        return false;
    }

    // stop music, close frame, remove key Listener
    private void closeMenu(){
        for (int i = 1; i <=NUM_OF_LEVELS; i++) {
            gameMusic.get(i - 1).stop();
        }
        this.menuMusic.stop();
        this.window.frame.setVisible(false);
        this.window.frame.dispose();
        this.removeKeyListener(mki);
//        thread.stop();
        running = false;
    }

    public static void main(String[] args){
        MainGameMenu m = new MainGameMenu(1200, 800, false, 0);
        m.start();
    }
}
