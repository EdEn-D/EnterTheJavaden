// The object that the user will be controlling, the player

package objects;

import Audio.AudioHandler;
import Audio.AudioPlayer;
import framework.GameObject;
import framework.ObjectId;
import textures.Animation;
import textures.SpriteSheet;
import window.Camera;
import window.Game;
import window.Handler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends GameObject {

    private AudioHandler audioHandler;
    private BufferedImage[] player_image = new BufferedImage[3];
    Animation anim;

    private final int BASE_SPEED = 5;
    private final int PADDING = 10;
    private int shots = 0;
    private static final int SIZE = SpriteSheet.imageSize;
    private long damageTime = System.currentTimeMillis();
    private Camera cam;

    private int score;
    private double boost = 1.5;
    private boolean shotgun = false;

    private int prevX, prevY;
    private boolean blockCollision = false;
    private boolean pickedUpTreasure = false;

    private final String playerHurtSound = "Minecraft hurt  sound effect.mp3";

    // For testing
    private boolean invincible = false;

    // constructor
    public Player(int x, int y, ObjectId id, Handler h, SpriteSheet ss, Camera cam) {
        super(x, y, id, h, ss);
        this.SIZE_X = SIZE;
        this.SIZE_Y = SIZE;
        this.speed = BASE_SPEED;
//        this.hp = 10; // delete this shit

//        this.audioHandler = audioHandler;
        this.cam = cam;

        player_image[0] = ss.grabImage("Player1");
        player_image[1] = ss.grabImage("Player2");
        player_image[2] = ss.grabImage("Player3");

        anim = new Animation(5, player_image[0],player_image[1],player_image[2]);
    }

    public boolean getShotgun(){ return shotgun;
    }

    public boolean gotTreasure() {
        return pickedUpTreasure;
    }

    public void incScore(int addScore){
        score += addScore;
    }

    public int getScore() {
        return score;
    }

    public int getShots() {
        return shots;
    }

    // increment shots by arg
    public void incShots(int c) {
        this.shots += c;
    }

    public static int getSize(){
        return SIZE;
    }

    @Override
    public void tick() {
        if (invincible)
            this.hp = 100;
        blockCollision = false;
        collision(false);
        prevX = x;
        prevY = y;
        x += velX*boost;
        y += velY*boost;

//        collision();
//        bounds();

        // Player movement + input glitch avoidance
        if (handler.isUp()) velY = -(BASE_SPEED);
        else if (!handler.isDown()) velY = 0;

        if (handler.isDown()) velY = (BASE_SPEED);
        else if (!handler.isUp()) velY = 0;

        if (handler.isRight()) velX = (BASE_SPEED);
        else if (!handler.isLeft()) velX = 0;

        if (handler.isLeft()) velX = -(BASE_SPEED);
        else if (!handler.isRight()) velX = 0;

        anim.runAnimation();
    }

    // makes sure the player cant leave the screen
    private void bounds(){
        //System.out.println("X pos: " + x + "Y pos: " + y);
        if (this.x < 0) this.x += velX * -1;
        if (this.x + this.SIZE_X + PADDING > Game.WIDTH) this.x -= velX;
        if (this.y < 0) this.y += velY* -1;
        if (this.y + this.SIZE_Y + 4*PADDING > Game.HEIGHT) this.y -= velY;
    }

    private void collision(Boolean block){
        for (int i = 0; i < handler.object.size(); i++){
            try{
                GameObject tempObject = handler.object.get(i);

                // check intersections
                if(tempObject.getId() == ObjectId.Block || tempObject.getId() == ObjectId.BreakableBox) {
                    // if below occurs, we have a collision
                    if(getBounds().intersects(tempObject.getBounds())){
//                        this.x += velX * -1;
//                        this.y += velY* -1;
                        this.x = prevX;
                        this.y = prevY;
                        blockCollision = true;
                    }
                }
                if (!block) {
                    if (tempObject.getId() == ObjectId.AmmoBox) {
                        // if below occurs, we have a collision
                        if (getBounds().intersects(tempObject.getBounds())) {
                            tempObject.removed = true;
                            new AudioPlayer("/Audio/SFX/Gun Cocking Sound Effect.mp3").play();

                            this.refillAmmo();
                        }
                    }
                    if (tempObject.getId() == ObjectId.Treasure) {
                        // if below occurs, we have a collision
                        if (getBounds().intersects(tempObject.getBounds())) {
                            this.score += ((Treasure)tempObject).getScore();
                            new AudioPlayer("/Audio/SFX/treasure.mp3").play();
                            tempObject.removed = true;
                            pickedUpTreasure = true;
                        }
                    }
                    if (tempObject.getId() == ObjectId.HP) {
                        // if below occurs, we have a collision
                        if (getBounds().intersects(tempObject.getBounds())) {
                            tempObject.removed = true;
                            new AudioPlayer("/Audio/SFX/hp.mp3").play();
                            this.hp += 50;
                            if (this.hp > 100)
                                this.hp = 100;
                        }
                    }
                    if (tempObject.getId() == ObjectId.Shotgun) {
                        // if below occurs, we have a collision
                        if (getBounds().intersects(tempObject.getBounds())) {
                            tempObject.removed = true;
                            // Change to shotgun
                            shotgun = true;
                            new AudioPlayer("/Audio/SFX/shotgun_pickup.mp3").play();
                            this.refillAmmo();
                        }
                    }
                    if (tempObject.getId() == ObjectId.SpeedBoost) {
                        // if below occurs, we have a collision
                        if (getBounds().intersects(tempObject.getBounds())) {
                            tempObject.removed = true;
                            this.boost = boost*1.25;
                            new AudioPlayer("/Audio/SFX/Speedboost.mp3").play();

                        }
                    }
//                    if (tempObject.getId() == ObjectId.DamageBlock) {
//                        // if below occurs, we have a collision
//                        if (getBounds().intersects(tempObject.getBounds())) {
//                            if (System.currentTimeMillis() - damageTime > 500) { // take damage only once every 0.5 sec
//                                damageTime = System.currentTimeMillis();
//                                this.hp -= tempObject.getDamage();
//                                new AudioPlayer("/Audio/SFX/" + playerHurtSound).play(); // hurt sound
////                                System.out.println("Player HP: " + hp);
//                            }
//                            // Player flinch
////                            this.x += velX + BASE_SPEED * -1 * getVelXdir();
////                            this.y += velY + BASE_SPEED * -1 * getVelYdir();
//                        }
//                    }
//                    if (tempObject.getId() == ObjectId.EnemyBullet) {
//                        // if below occurs, we have a collision
//                        if (getBounds().intersects(tempObject.getBounds())) {
//                            this.hp -= tempObject.getDamage();
//                            tempObject.removed = true;
//                            new AudioPlayer("/Audio/SFX/Minecraft hurt  sound effect.mp3").play(); // shotgun sound
//                            System.out.println("Player HP: " + hp);
//                        }
//                    }
                    if (tempObject.getId() == ObjectId.Enemy || tempObject.getId() == ObjectId.EnemyBullet || tempObject.getId() == ObjectId.DamageBlock) {
                        // if below occurs, we have a collision
                        if (getBounds().intersects(tempObject.getBounds())) {
                            if (System.currentTimeMillis() - damageTime > 500) { // take damage only once every 0.5 sec
                                damageTime = System.currentTimeMillis();
                                if (tempObject.getId() == ObjectId.EnemyBullet)
                                    tempObject.removed = true;
                                this.hp -= tempObject.getDamage();
                                new AudioPlayer("/Audio/SFX/" + playerHurtSound).play(); // hurt sound
//                                System.out.println("Player HP: " + hp);
                            }
                            int preFlinchX = x;
                            int preFlinchY = y;
                            if (velY == 0 && velX == 0) {
//                            this.x +=  (int)tempObject.getVelX();
//                            this.y +=  (int)tempObject.getVelY();
                                this.x += tempObject.getVelXdir() * speed;
                                this.y += tempObject.getVelYdir() * speed;
                            } else {
                                this.x += BASE_SPEED * -1 * velX;
                                this.y += BASE_SPEED * -1 * velY;
                            }
                            // flinch glitch protection
                            collision(true);
                            if (blockCollision) {
                                x = preFlinchX;
                                y = preFlinchY;
                            }

                            if (tempObject.getId() == ObjectId.Enemy) {
                                // enemy flinch
                                tempObject.setX(tempObject.getX() + (int) tempObject.getVelX() * -1 * tempObject.getSpeed());
                                tempObject.setY(tempObject.getY() + (int) tempObject.getVelY() * -1 * tempObject.getSpeed());

                                if (!((Enemy) tempObject).getType().equals("Bounce")) {
                                    tempObject.setVelX(tempObject.getVelX() + tempObject.getVelX() * -1);
                                    tempObject.setVelY(tempObject.getVelY() + tempObject.getVelY() * -1);
                                }
                            }
//
//                        tempObject.setX(tempObject.getX()-(int)tempObject.getVelX());
//                        tempObject.setY(tempObject.getY()-(int)tempObject.getVelY());
//
//                        tempObject.setVelX(tempObject.getVelX() + tempObject.getVelX()*2*-1);
//                        tempObject.setVelY(tempObject.getVelY() + tempObject.getVelY()*2*-1);

                        }
                    }
                }
            }
            catch (IndexOutOfBoundsException e){
                System.out.println("Player error");
                continue;
            }
        }
    }

    @Override
    public void render(Graphics g) {
//        g.setColor(Color.RED);
//        g.fillRect(x,y,SIZE_X,SIZE_Y);
        if (velX == 0 && velY == 0)
            g.drawImage(player_image[0],x,y,null);
        else{
            anim.drawAnimation(g,x,y,0);
        }

        // Draw HUD
        // Ammo
        int camx = (int)cam.getX();
        int camy = (int)cam.getY();
        Font ariel = new Font("Elephant", Font.BOLD, 25);
        g.setFont(ariel);
        g.setColor(Color.red);
        g.drawString("Ammo: " + String.valueOf(this.ammo), camx+5, camy+70);

        // Score
        g.setColor(Color.yellow);
        g.drawString("Score: " + String.valueOf(this.score), camx+5, camy+100);

        // HP
        g.setColor(Color.gray);
        g.fillRect(camx+5,camy+5,200,32);
        if (hp >= 50)
            g.setColor(Color.green);
        if (hp < 50 && hp >= 25)
            g.setColor(Color.yellow);
        if (hp < 25)
            g.setColor(Color.red);
        g.fillRect(camx+5,camy+5,hp*2,32);
        g.setColor(Color.black);
        g.drawRect(camx+5,camy+5,200,32);


    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y,SIZE_X,SIZE_Y);
    }
}
