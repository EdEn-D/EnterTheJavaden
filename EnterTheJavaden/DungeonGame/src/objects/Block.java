package objects;

import framework.BufferedImageLoader;
import framework.GameObject;
import framework.ObjectId;
import textures.SpriteSheet;
import window.Handler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Block extends GameObject {

    private BufferedImage block_image;
    private static final int SIZE = SpriteSheet.imageSize;


    public Block(int x, int y, ObjectId id, Handler handler, SpriteSheet ss) {
        super(x, y, id, handler, ss);
        SIZE_X = SIZE;
        SIZE_Y = SIZE;
        block_image = ss.grabImage("Block");
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
//        g.setColor(Color.BLACK);
//        g.fillRect(x,y,SIZE_X,SIZE_Y);
        g.drawImage(block_image,x,y,null);

        // see bounds box
        if (false){
            Graphics2D g2d = (Graphics2D) g;
            g.setColor(Color.GREEN);
            g2d.draw(getBounds());
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y,SIZE_X,SIZE_Y);
    }
}
