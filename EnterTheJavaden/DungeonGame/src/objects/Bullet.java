package objects;

import Audio.AudioHandler;
import Audio.AudioPlayer;
import framework.GameObject;
import framework.ObjectId;
import textures.SpriteSheet;
import window.Game;
import window.Handler;

import java.awt.*;

public class Bullet extends GameObject {

    private final int SIZE = 8;
    private int BASE_SPEED = 15;
    private final int SPREAD_ANGLE = 5;
    private boolean shotgun;
    private int shotgunDamage = 35;
    static AudioPlayer bullet_hit = new AudioPlayer("/Audio/SFX/bulletHitting.mp3");


//    private Handler handler;

    public Bullet(int x, int y, ObjectId id, Handler handler, SpriteSheet ss, int mx, int my, boolean spread) {
        super(x, y, id, handler, ss);
        SIZE_X = SIZE;
        SIZE_Y = SIZE;


        this.shotgun = spread;
        this.damage = 50;
        if (spread){
            new AudioPlayer("/Audio/SFX/shotgunshot.mp3").play(); // shotgun sound
            this.damage = shotgunDamage;
            BASE_SPEED += 15;
            shotgun(mx, my);
        }
        else
            new AudioPlayer("/Audio/SFX/gunshot.mp3").play(); // regular gun sound
        setVelocity(mx, my);
    }

    private Bullet(int x, int y, ObjectId id, Handler handler, SpriteSheet ss, float vx, float vy) {
        super(x, y, id, handler, ss);
        SIZE_X = SIZE;
        SIZE_Y = SIZE;
        this.velX = vx;
        this.velY = vy;
        this.shotgun = true;
        this.damage = shotgunDamage;
    }

    private void shotgun(int mx, int my) {
        double degree = Math.toDegrees(Math.atan2(my-y,mx-x));

        double degree1 =degree+ SPREAD_ANGLE;
        double xMult = Math.cos(Math.toRadians(degree1));
        double yMult = Math.sin(Math.toRadians(degree1));
        float vx = (float) (xMult*BASE_SPEED);
        float vy = (float) (yMult*BASE_SPEED);
        handler.addObject(new Bullet(x, y, ObjectId.Bullet, handler,ss, vx, vy));


        double degree2 = degree - SPREAD_ANGLE;
         xMult = Math.cos(Math.toRadians(degree2));
         yMult = Math.sin(Math.toRadians(degree2));
         vx = (float) (xMult*BASE_SPEED);
         vy = (float) (yMult*BASE_SPEED);
        handler.addObject(new Bullet(x, y, ObjectId.Bullet, handler,ss, vx, vy));

    }

    private void setVelocity(int mx, int my){
        double degree = Math.toDegrees(Math.atan2(my-y,mx-x));
        double xMult = Math.cos(Math.toRadians(degree));
        double yMult = Math.sin(Math.toRadians(degree));
        velX = (float) (xMult*BASE_SPEED);
        velY = (float) (yMult*BASE_SPEED);
    }

    private double moveAngle (double deg){
        double newDeg = deg;
        if (newDeg<0)
            newDeg += 180;
        return newDeg;
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;

        collision();
//        bounds();
    }

    private void bounds(){
        if (x - SIZE*2 < 0 || x + SIZE*2 > Game.WIDTH || y - SIZE*2 < 0 || y + SIZE*2 > Game.HEIGHT)
            this.removed = true;
    }

    private void collision(){
        for (int i = 0; i < handler.object.size(); i++){
            try {
                GameObject tempObject = handler.object.get(i);
                // check intersections
                if(tempObject.getId() == ObjectId.Block) {
                    // if below occurs, we have a collision
                    if(getBounds().intersects(tempObject.getBounds())){
                        bullet_hit.play();
                       this.removed = true;
                    }
                }
            }
            catch (IndexOutOfBoundsException e){
                System.out.println("Bullet error");
                continue;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if (shotgun)
            g.setColor(Color.magenta);
        else
            g.setColor(Color.GREEN);
        g.fillOval(x,y,SIZE,SIZE);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y,SIZE,SIZE);
    }
}
