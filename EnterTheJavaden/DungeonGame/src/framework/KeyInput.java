// Keyboard input handling in the game


package framework;

import window.Handler;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
    Handler handler;

    public KeyInput(Handler h){
        this.handler = h;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Loop through all objects
        for (int i = 0; i < handler.object.size(); i++){
            GameObject tempObject = handler.object.get(i);

            // Handle player object
            if(tempObject.getId() == ObjectId.Player){
                if(key == KeyEvent.VK_W) handler.setUp(true);
                if(key == KeyEvent.VK_S) handler.setDown(true);
                if(key == KeyEvent.VK_A) handler.setLeft(true);
                if(key == KeyEvent.VK_D) handler.setRight(true);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        // Loop through all objects
        for (int i = 0; i < handler.object.size(); i++){
            GameObject tempObject = handler.object.get(i);

            // Handle player object
            if(tempObject.getId() == ObjectId.Player){
                if(key == KeyEvent.VK_W) handler.setUp(false);
                if(key == KeyEvent.VK_S) handler.setDown(false);
                if(key == KeyEvent.VK_A) handler.setLeft(false);
                if(key == KeyEvent.VK_D) handler.setRight(false);
            }
        }
    }
}
