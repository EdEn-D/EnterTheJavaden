// Update/handle all game objects

package window;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import framework.GameObject;
import framework.ObjectId;
import objects.Block;
import objects.Player;

import Audio.AudioPlayer;

public class Handler {
    public ArrayList<GameObject> object = new ArrayList<GameObject>();
    private GameObject tempObject;
    private boolean up = false, down = false, left = false, right = false; // for player movement

    private HashMap<String , AudioPlayer> sfx;
    private AudioPlayer sound;

    public boolean isUp() {
        return up;
    }
    public void setUp(boolean up) {
        this.up = up;
    }
    public boolean isDown() {
        return down;
    }
    public void setDown(boolean down) {
        this.down = down;
    }
    public boolean isLeft() {
        return left;
    }
    public void setLeft(boolean left) {
        this.left = left;
    }
    public boolean isRight() {
        return right;
    }
    public void setRight(boolean right) {
        this.right = right;
    }

    // Loop thorough objects
    public void tick() {
        // tick all objects
        for (int i = 0; i < object.size(); i++) {
            tempObject = object.get(i);
            tempObject.tick();
            if (object.get(i).removed) {
//                System.out.println("removed " + object.get(i).getId());
                object.remove(i);
            }
        }
    }

    public void render(Graphics g) {
        // render all objects
        for (int i = 0; i < object.size(); i++) {
            tempObject = object.get(i);
            tempObject.render(g);
        }
    }

    public void addObject (GameObject object){
        this.object.add(object);
    }

    public void removeObject(GameObject object){
        this.object.remove(object);
    }

    public int getEnemies(){
        int enemyCount = 0;
        for (int i = 0; i < object.size(); i++) {
            tempObject = object.get(i);
            if (tempObject.getId() == ObjectId.Enemy)
                enemyCount++;
        }
        return enemyCount;
    }
}
