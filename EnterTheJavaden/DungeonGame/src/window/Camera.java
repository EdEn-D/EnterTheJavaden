// Define camera and camera rules

package window;

import framework.GameObject;
import objects.Player;

public class Camera {
    private float x,y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    int lvlX, lvlY;
    int resX, resY;
    public Camera(float x, float y, int lvlX, int lvlY, int resX, int resY) {
        this.x = x;
        this.y = y;
        this.lvlX = lvlX;
        this.resX = resX;
        this.lvlY = lvlY;
        this.resY = resY;
    }

    public void tick(GameObject object){
        // make camera smooth
        x += ((object.getX() - x) - (float)resX/2) * 0.05f;
//        x = object.getX();
        y += ((object.getY() - y) - (float)resY/2) * 0.05f;
//        y = object.getY();

        // max camera range
        float playerSize = (float)Player.getSize();
        if (x <= 0) x = 0;
        if (x >= lvlX - (resX)) x = lvlX - (resX);
        if (y <= 0) y = 0;
        if (y >= lvlY -(resY)) y = lvlY -(resY) ;
    }
}
