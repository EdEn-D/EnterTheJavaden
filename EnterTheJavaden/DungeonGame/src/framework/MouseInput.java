// Define mouse input rules and behaviours

package framework;

import Audio.AudioHandler;
import Audio.AudioPlayer;
import objects.Bullet;
import objects.Player;
import textures.SpriteSheet;
import window.Camera;
import window.Handler;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;

public class MouseInput extends MouseAdapter {
    private int ROF = 10;
    private Handler handler;
    private Camera camera;
    private SpriteSheet ss;
    private AudioHandler audioHandler;

    boolean shotgun;

    public MouseInput(Handler handler, Camera camera, SpriteSheet ss){
        this.audioHandler = audioHandler;
        this.handler = handler;
        this.camera = camera;
        this.ss = ss;
    }

//    public void mouseDragged(MouseEvent e){
//        if (ROF == 0) {
//            mousePressed(e);
//            ROF = 10;
//        }
//        else ROF -= 1;
//    }

    public void mousePressed(MouseEvent e) {
        int mx = (int) (e.getX() + camera.getX());
        int my = (int) (e.getY() + camera.getY());

        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            // only create object for player
            if (tempObject.getId() == ObjectId.Player) {
                shotgun = ((Player) tempObject).getShotgun();
                if (tempObject.getAmmo() > 0) {
                    handler.addObject(new Bullet(tempObject.getX() + Player.getSize() / 2, tempObject.getY() + Player.getSize() / 2, ObjectId.Bullet, handler,ss, mx, my, shotgun));
                    tempObject.decAmmo(shotgun);
                }
                else{
                    new AudioPlayer("/Audio/SFX/empty clip.mp3").play();
//                    System.out.println("Out of ammo");
                }
            }
        }
    }
}
