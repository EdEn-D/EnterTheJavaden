package objects;

import framework.GameObject;
import framework.ObjectId;
import textures.SpriteSheet;
import window.Handler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HP extends GameObject {

    private BufferedImage image;

    private static final int SIZE = SpriteSheet.imageSize;

    public HP(int x, int y, ObjectId id, Handler handler, SpriteSheet ss) {
        super(x, y, id, handler, ss);
        image = ss.grabImage("HP");
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(image,x,y,null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y,SIZE,SIZE);
    }
}
