package objects;

import framework.GameObject;
import framework.ObjectId;
import textures.SpriteSheet;
import window.Game;
import window.Handler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Turret extends GameObject {

    private BufferedImage image;
    Game game;

    private static final int SIZE = SpriteSheet.imageSize;
    private int dir;
    long timer = System.currentTimeMillis();
    private int score = 20;


    public Turret(int x, int y, ObjectId id, Handler handler, SpriteSheet ss, int dir, Game game) {
        super(x, y, id, handler, ss);
        image = ss.grabImage("Turret");
        this.game = game;
        this.dir = dir;
        this.MAX_HP = 400;
        this.hp = MAX_HP;
    }

    @Override
    public void tick() {
        collision();

        if (System.currentTimeMillis() - timer > 500) {
            timer = System.currentTimeMillis();
            if (dir == 1)
                handler.addObject(new EnemyBullet(this.getX() + Player.getSize() / 2, this.getY() + Player.getSize() / 2, ObjectId.EnemyBullet, handler, ss, 0, -1));
            else if (dir == 2)
                handler.addObject(new EnemyBullet(this.getX() + Player.getSize() / 2, this.getY() + Player.getSize() / 2, ObjectId.EnemyBullet, handler, ss, 0, 1));
            else if (dir == 3)
                handler.addObject(new EnemyBullet(this.getX() + Player.getSize() / 2, this.getY() + Player.getSize() / 2, ObjectId.EnemyBullet, handler, ss, 1, 0));
            else if (dir == 4)
                handler.addObject(new EnemyBullet(this.getX() + Player.getSize() / 2, this.getY() + Player.getSize() / 2, ObjectId.EnemyBullet, handler, ss, -1, 0));
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(image,x,y,null);
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
//                        System.out.println(this.hp);
                        if (this.hp <= 0){
                            incPlayerScore(score);
                            this.removed = true;
                        }
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
    public Rectangle getBounds() {
        return new Rectangle(x,y,SIZE,SIZE);
    }
}
