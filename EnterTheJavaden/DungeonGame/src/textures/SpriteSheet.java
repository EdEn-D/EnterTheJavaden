// Methods for slicing object textures from the main sprite sheet

package textures;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private BufferedImage image;
    public static int imageSize = 64, space = 20;
    private final int DEFUALT_SIZE = imageSize;

    public SpriteSheet(BufferedImage image){
        this.image = image;
    }

//    public BufferedImage grabImage(int col, int row, int width, int height){
//        return image.getSubimage((col*32)-32 + 10*(col - 1),(row*32)-32 + 10*(row - 1),width,height);
//    }

    // Takes input name from object constructors and defines margins to take textures from the sprite sheet
    public BufferedImage grabImage(String obj_name){
        int width = DEFUALT_SIZE, height = DEFUALT_SIZE;
        int col = imageSize + space, row = imageSize + space;

        switch (obj_name) {
            case "Player1":
                col *= 0;
                row *= 1;
                break;
            case "Player2":
                col *= 0;
                row *= 1;
                break;
            case "Player3":
                col *= 0;
                row *= 1;
                break;
            case "Block":
                col *= 1;
                row *= 0;
                break;
            case "AmmoBox":
                col *= 6;
                row *= 0;
                break;
            case "HP":
                col *= 3;
                row *= 1;
                break;
            case "Shotgun":
                col *= 5;
                row *= 0;
                break;
            case "DamageBlock": // fire
                col *= 4;
                row *= 1;
                break;
            case "SpeedBoost":
                col *= 5;
                row *= 1;
                break;
            case "BreakableBox":
                col *= 4;
                row *= 2;
                break;
            case "Turret":
                col *= 4;
                row *= 0;
                break;
            case "Treasure":
                col *= 2;
                row *= 1;
                break;
            // Enemies
            case "HLine":
            case "VLine":
            case "Bounce":
            case "Random":
            case "Slow":
                col *= 1;
                row *= 1;
                break;
            default:
                System.out.println("Bad image name: " + obj_name);
        }

        return image.getSubimage(col,row,width,height);
    }
}
