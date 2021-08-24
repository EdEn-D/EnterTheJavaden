package objects;

import framework.GameObject;
import framework.ObjectId;
import textures.SpriteSheet;
import window.Handler;

import java.awt.*;

public class EnemyBullet extends GameObject {

    private final int SIZE = 15;
    private int BASE_SPEED = 10;


    public EnemyBullet(int x, int y, ObjectId id, Handler handler, SpriteSheet ss, int dirX, int dirY) {
        super(x, y, id, handler, ss);
        SIZE_X = SIZE;
        SIZE_Y = SIZE;
        this.damage = 10;
        velX = dirX*BASE_SPEED;
        velY = dirY*BASE_SPEED;

    }

    @Override
    public void tick() {
        x += velX;
        y += velY;

        collision();
    }

    private void collision(){
        for (int i = 0; i < handler.object.size(); i++){
            try {
                GameObject tempObject = handler.object.get(i);
                // check intersections
                if(tempObject.getId() == ObjectId.Block || tempObject.getId() == ObjectId.BreakableBox) {
                    // if below occurs, we have a collision
                    if(getBounds().intersects(tempObject.getBounds())){
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
            g.setColor(Color.red);
            g.fillOval(x,y,SIZE,SIZE);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y,SIZE,SIZE);
    }

}
