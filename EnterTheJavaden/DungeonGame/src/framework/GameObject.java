// Base class for every object in the game. Includes position, velocity, Id

package framework;

import objects.Player;
import textures.SpriteSheet;
import window.Handler;

import java.awt.*;
import java.io.ObjectInput;
import java.util.LinkedList;

public abstract class GameObject {
    private static final int SIZE = SpriteSheet.imageSize;
    protected int SIZE_X = SIZE, SIZE_Y = SIZE;
    protected int x,y;
    protected float velX= 0,velY=0;
    protected int speed = 5;

    protected ObjectId id;
    protected Handler handler;
    protected SpriteSheet ss;

    public boolean removed = false;
    private final int MAX_AMMO = 100;
    protected int ammo = MAX_AMMO;


    protected int hp = 100;
    protected int MAX_HP = 100;
    protected int damage = 10;



    public GameObject(int x, int y, ObjectId id, Handler handler, SpriteSheet ss){
        this.x = x;
        this.y = y;
        this.id = id;
        this.handler = handler;
        this.ss = ss;
    }

    // used for collision detection
    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract Rectangle getBounds();


    public void incPlayerScore(int addScore){
        for (int i = 0; i < handler.object.size(); i++){
            try {
                GameObject tempObject = handler.object.get(i);
                if(tempObject.getId() == ObjectId.Player) {
                    ((Player) tempObject).incScore(addScore);
                }
            }
            catch (IndexOutOfBoundsException e){
                System.out.println("ScoreInc error");
                continue;
            }
        }
    }

    public void decAmmo(boolean shotgun) {
        if (!shotgun)
            this.ammo -= 1;
        else
            this.ammo -= 2;
    }

    public void refillAmmo() {
        this.ammo = MAX_AMMO;
    }

    // getters and setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getVelX() {
        return velX;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getId() {
        return id;
    }

    public int getVelXdir(){
        if (velX > 0)
            return 1;
        else if(velX < 0)
            return -1;
        else return 0;
    }

    public int getVelYdir(){
        if (velY > 0)
            return 1;
        else if(velY < 0)
            return -1;
        else return 0;
    }

    public int getHp() {
        return hp;
    }

    public int getMAX_HP() {
        return MAX_HP;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public int getAmmo() {
        return ammo;
    }
}
