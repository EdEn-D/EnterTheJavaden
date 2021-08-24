package objects;

import Audio.AudioPlayer;
import framework.ObjectType;
import framework.GameObject;
import framework.ObjectId;
import textures.SpriteSheet;
import window.Game;
import window.Handler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends GameObject {

    private BufferedImage image;

    Game game;

    private static final int SIZE = SpriteSheet.imageSize;
    Random r = new Random();
    private int choose = 0;
    private boolean collisionEvent = false;
    private String type; // Random, HLine
    private int directionX = 1;
    private int directionY = 1;

    private int randBounceAngle = 999;
    private int bounceEnemySpeed = 5;
    boolean HC = false;
    boolean VC = false;

    private int prevX, prevY;
    private int buggedCount = 0;
    private int secondBuggedCount = 0;
    private int lastUnbuggedX;
    private float lastUnbuggedVelX;
    private int lastUnbuggedY;
    private float lastUnbuggedVelY;

    private final int STEPS = 100;
    private int slowSteps = STEPS;
    int slowEnemySpeed = 2;
    private double boost = 1.75; // Universal enemy speed controller

    long timer = System.currentTimeMillis();


    int min = 0;
    int max = 90;
    boolean firstRun = true;

    int shootOption;


    public Enemy(int x, int y, ObjectId id, Handler handler, SpriteSheet ss, String type, Game game, int shootOption) {
        super(x, y, id, handler, ss);
        this.type = type;
        this.game = game;
        image = ss.grabImage(type);

        lastUnbuggedX = x;
        lastUnbuggedY = y;
        lastUnbuggedVelX = velX;
        lastUnbuggedVelY = velY;

        damage = 15;
        this.shootOption = shootOption;
    }

    @Override
    public void tick() {
        prevX = x;
        prevY = y;
        x += velX*boost;
        y += velY*boost;

        if (type.equals("Bounce"))
            checkBuggedCount();
        else
            collisionEvent = collision();

        switch (type) {
            case "Random" -> randomRetard();
            case "HLine" -> HLine();
            case "VLine" -> VLine();
            case "Bounce" -> Bounce(); // kinda bugged, not bouncing properly
            case "Slow" -> Slow();
        }
    }

    private void Slow() {
        slowSteps += 1;
//        System.out.println(slowSteps);
        if (collisionEvent) {
            slowSteps = STEPS;
            x -= velX * 2;
            y -= velY * 2;
            velX += (velX * 2 * -1);
            velY += (velY * 2 * -1);
        }
//        else {
        if (slowSteps >= STEPS) {
            slowSteps = 0;
            int max = 180;
            int min = -180;
            choose = (r.nextInt(max - min) + min);
            double xMultx = Math.cos(Math.toRadians(choose));
            double yMulty = Math.sin(Math.toRadians(choose));
            velX = (float) (xMultx * slowEnemySpeed);
            velY = (float) (yMulty * slowEnemySpeed);
        }
//        }
    }

    // Method written for the bounce enemy to deal with bugs, fix might have made this method redundant, kept just in case
    private void checkBuggedCount(){
        collisionEvent = collision();
        if (collisionEvent){
            buggedCount += 1;
//            System.out.println(buggedCount);
        }
        else{
            buggedCount = 0;
            lastUnbuggedX = x;
            lastUnbuggedY = y;
            lastUnbuggedVelX = velX;
            lastUnbuggedVelY = velY;
        }

        if (buggedCount >= 10){ //|| (velX == 0 && velY == 0)){
            buggedCount = 0;
            x= lastUnbuggedX;
            y=lastUnbuggedY;
            velX = -lastUnbuggedVelX;
            velY = -lastUnbuggedVelY;
            secondBuggedCount += 1;
            if (secondBuggedCount >= 3){
                secondBuggedCount =0;
//                int quad = 1+ r.nextInt(4);
                velX = -velX;
                velY = -velY;
            }
        }
    }

    private int getScoreByType(String type){
        switch (type) {
            case "Random":
                return 20;
            case "HLine":
            case "VLine":
                return 10;
            case "Bounce":
                return 15;
            default:
                return 10;
        }
    }

    private void Bounce() {
//        if (firstRun)
        if (HC || VC || firstRun){
//            System.out.println("Horiz? " + HC);
//            System.out.println("Vertic? " + VC);
            firstRun = false;
            if (HC && VC){
                if (velX > 0 && velY > 0) { // moving to bottom right
                    //System.out.println("11");
                    min = -180;
                    max = -90;
                }else if (velX > 0 && velY < 0) { // moving to top right
                    //System.out.println("22");
                    min = 90;
                    max = 180;
                } else if (velX < 0 && velY > 0) { // bottom left
                    //System.out.println("33");
                    min = -90;
                    max = 0;
                } else if (velX < 0 && velY < 0) { // top left
                    //System.out.println("44");
                    min = 0;
                    max = 90;
                }
            } else if (HC) {
                if (velX > 0 && velY > 0) { // moving to bottom right
                    //System.out.println("1");
                    min = 90;
                    max = 180;
                } else if (velX > 0 && velY < 0) { // moving to top right
                    //System.out.println("2");
                    min = -180;
                    max = -90;
                } else if (velX < 0 && velY > 0) { // bottom left
                    //System.out.println("3");
                    min = 0;
                    max = 90;
                } else if (velX < 0 && velY < 0) { // top left
                    //System.out.println("4");
                    min = -90;
                    max = 0;
                }
            } else if (VC){
                if (velX > 0 && velY > 0) { // moving to bottom right
                    //System.out.println("5");
                    min = -90;
                    max = 0;
                } else if (velX > 0 && velY < 0) { // moving to top right
                    //System.out.println("6");
                    min = 0;
                    max = 90;
                } else if (velX < 0 && velY > 0) { // bottom left
                    //System.out.println("7");
                    min = -180;
                    max = -90;
                } else if (velX < 0 && velY < 0) { // top left
                    //System.out.println("8");
                    min = 90;
                    max = 180;
                }
            }
            randBounceAngle = (r.nextInt(max - min) + min); // allows for random bounce
//            randBounceAngle = (min+max)/2;
//            System.out.println("min: " + min + " max: " + max + " deg: " +randBounceAngle + " BuggedCount = " + buggedCount);
            double xMult = Math.cos(Math.toRadians(randBounceAngle));
            double yMult = Math.sin(Math.toRadians(randBounceAngle));
            velX = (float) (xMult*bounceEnemySpeed);
            velY = (float) (yMult*bounceEnemySpeed);
        }
        HC = false;
        VC = false;
    }

    private void VLine() {
        if (collisionEvent){
            directionY = (-1)*directionY;
        }
        else {
            if (this.shootOption != 0 && System.currentTimeMillis() - timer > 500) {
                timer = System.currentTimeMillis();
                handler.addObject(new EnemyBullet(this.getX() + Player.getSize() / 2, this.getY() + Player.getSize() / 2, ObjectId.EnemyBullet, handler, ss, this.shootOption,0));

            }
        }
        velY = directionY*speed;
    }

    private void HLine() {
        if (collisionEvent){
            directionX = (-1)*directionX;
        }
        else {
            if (this.shootOption != 0 && System.currentTimeMillis() - timer > 500) {
                timer = System.currentTimeMillis();
                handler.addObject(new EnemyBullet(this.getX() + Player.getSize() / 2, this.getY() + Player.getSize() / 2, ObjectId.EnemyBullet, handler, ss, 0, this.shootOption));
                handler.addObject(new EnemyBullet(this.getX() + Player.getSize() / 2, this.getY() + Player.getSize() / 2, ObjectId.EnemyBullet, handler, ss, 0, -this.shootOption));

            }
        }

        velX = directionX*speed;

    }

    private void randomRetard(){
        if (collisionEvent){
            x -= velX;
            y -= velY;
            velX += (velX*2*-1);
            velY += (velY*2*-1);
        }
        else {
            if (this.shootOption == 1 && System.currentTimeMillis() - timer > 500){
                timer = System.currentTimeMillis();
                handler.addObject(new EnemyBullet(this.getX() + Player.getSize() / 2, this.getY() + Player.getSize() / 2, ObjectId.EnemyBullet, handler,ss, 1, 0));
                handler.addObject(new EnemyBullet(this.getX() + Player.getSize() / 2, this.getY() + Player.getSize() / 2, ObjectId.EnemyBullet, handler,ss, 0, 1));
                handler.addObject(new EnemyBullet(this.getX() + Player.getSize() / 2, this.getY() + Player.getSize() / 2, ObjectId.EnemyBullet, handler,ss, -1, 0));
                handler.addObject(new EnemyBullet(this.getX() + Player.getSize() / 2, this.getY() + Player.getSize() / 2, ObjectId.EnemyBullet, handler,ss, 0, -1));

            }
            // retarded random moving sequence
            int max = 6;
            int min = -6;
            choose = r.nextInt(20);
            if (choose == 0) {
                velX = r.nextInt(max - min) + min;
                velY = r.nextInt(max - min) + min;
            }
        }
    }

    private boolean horizCollision() {
        for (int i = 0; i < handler.object.size(); i++) {
            try {
                GameObject tempObject = handler.object.get(i);
                // check intersections
                if(tempObject.getId() == ObjectId.Block) {
                    if (this.getHorizLine().intersects(tempObject.getBounds())) {
//                        System.out.println("Horiz collision");
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Enemy error");
                continue;
            }
        }
        return false;
    }

    private boolean verticCollision() {
        for (int i = 0; i < handler.object.size(); i++) {
            try {
                GameObject tempObject = handler.object.get(i);
                // check intersections
                if(tempObject.getId() == ObjectId.Block) {
                    if (this.getVerticLine().intersects(tempObject.getBounds())) {
//                        System.out.println("Vertical collision");
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Enemy error");
                continue;
            }
        }
        return false;
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
                        new AudioPlayer("/Audio/SFX/hit marker.mp3").play();
                        if (this.hp <= 0){
                            incPlayerScore(getScoreByType(type));
                            this.removed = true;
                        }
                    }
                }
                // check intersections
                if(tempObject.getId() == ObjectId.Block || tempObject.getId() == ObjectId.BreakableBox) {
                    if (this.getHorizLine().intersects(tempObject.getBounds())) {
//                        System.out.println("Horiz collision");
                        HC = true;
                        this.x = prevX;
                        this.y = prevY;
                        return true;
                    }
                    if (this.getVerticLine().intersects(tempObject.getBounds())) {
//                        System.out.println("Vertical collision");
                        VC = true;
                        this.x = prevX;
                        this.y = prevY;
                        return true;
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

    public String getType() {
        return type;
    }

    @Override
    public void render(Graphics g) {
//        g.setColor(Color.YELLOW);
//        g.fillRoundRect(x,y,SIZE,SIZE,10,10);
        g.drawImage(image,x,y,null);


        // see bounds box
        if (false){
            Graphics2D g2d = (Graphics2D) g;
            g.setColor(Color.GREEN);
            g2d.draw(getBoundsBig());
            g2d.draw(getHorizLine());
            g2d.draw(getVerticLine());
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y,SIZE,SIZE);
    }

    public Rectangle getBoundsBig() {
        return new Rectangle(x-8,y-8,(int)(SIZE*1.5),(int) (SIZE*1.5));
    }

    // methods for bounce enemy
    public Rectangle getHorizLine() {
//        return new Rectangle(x-30,y+(int)(SIZE/2)-5,SIZE+30+30,10); // very long
//        return new Rectangle(x-20,y+(int)(SIZE/2)-5,SIZE+20*2,5); // med long
        return new Rectangle(x-10,y+(int)(SIZE/2)-5,SIZE+10*2,5); // short
//        return new Rectangle(x-10,y+(int)(SIZE/2)-7,SIZE+10*2,15); // thick
    }

    public Rectangle getVerticLine() {
//        return new Rectangle(x+(int)(SIZE/2)-5,y-30,10,SIZE+30+30);
//        return new Rectangle(x+(int)(SIZE/2)-5,y-20,5,SIZE+20*2);
        return new Rectangle(x+(int)(SIZE/2)-5,y-10,5,SIZE+10*2);
//        return new Rectangle(x+(int)(SIZE/2)-7,y-10,15,SIZE+10*2);
    }
}
