package objects;

import Audio.AudioPlayer;
import framework.GameObject;
import framework.ObjectId;
import textures.SpriteSheet;
import window.Camera;
import window.Handler;
import window.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class BreakableBox extends GameObject {

//    private Game game;
    private BufferedImage image;
    private Camera cam;

    private static final int SIZE = SpriteSheet.imageSize;
//    private static final int MAX_HP = 200;

    Random r = new Random();

    private int score = 10;
    private boolean updateHP = false;

    Game game;

    public BreakableBox(int x, int y, ObjectId id, Handler handler, SpriteSheet ss, Game game) {
        super(x, y, id, handler, ss);
        image = ss.grabImage("BreakableBox");
        this.MAX_HP = 200;
        this.hp = this.MAX_HP;
        this.game = game;
    }

    @Override
    public void tick() {
        collision();
    }


    private boolean collision(){
        for (int i = 0; i < handler.object.size(); i++){
            try{
                GameObject tempObject = handler.object.get(i);

                if(tempObject.getId() == ObjectId.Bullet) {
                    if(this.getBounds().intersects(tempObject.getBounds())){
                        // remove bullet
                        this.hp -= tempObject.getDamage();
                        // show hp of this object
                        game.damagedObject = this;
                        tempObject.removed = true;
                        if (this.hp <= 0){
                            new AudioPlayer("/Audio/SFX/box_breaking.mp3").play();
                            incPlayerScore(score);
                            this.removed = true;
                        }
                        else
                            new AudioPlayer("/Audio/SFX/bulletHitting.mp3").play();

                    }
                }
            }
            catch (IndexOutOfBoundsException e){
                System.out.println("Enemy error");
                continue;
            }
        }
        return false;
    }


    @Override
    public void render(Graphics g) {
        g.drawImage(image,x,y,null);


    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y,SIZE,SIZE);
    }}