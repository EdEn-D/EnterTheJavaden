// defines object creating from pixels in 2D PNG image

package framework;

import objects.*;
import textures.SpriteSheet;
import window.Game;
import window.Handler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageLoader {
    private BufferedImage image;

    public BufferedImage loadImage(String path){
        try {
            image = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            System.out.println("Level image error");
            e.printStackTrace();
        }
        return image;
    }

    // Loading the level to the handler
    public void loadLevel(BufferedImage image, Game game) {
        int w = image.getWidth();
        int h = image.getHeight();

        // read image pixles
        for (int xx = 0; xx < w; xx++) {
            for (int yy = 0; yy < h; yy++) {
                int pixel = image.getRGB(xx, yy);
                // Mask to get color values that we need
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                int imgSize = SpriteSheet.imageSize;
                //  Pass data on to handler (add to) object list
                // Interpret color data from image to game objects
                if (
                        red == 255 &&
                        blue == 0 &&
                        green == 0)

                    game.handler.addObject(new Block(xx*imgSize, yy*imgSize, ObjectId.Block,  game.handler, game.ss));
                else if (
                        red == 0 &&
                        blue == 255 &&
                        green == 0)

                     game.handler.addObject(new Player(xx*imgSize, yy*imgSize, ObjectId.Player,  game.handler, game.ss,
                             game.camera));
                else if (
                        red == 0 &&
                        blue == 0 &&
                        green == 255)

                     game.handler.addObject(new Enemy(xx*imgSize, yy*imgSize, ObjectId.Enemy,  game.handler, game.ss, "Random", game,1));
                else if (
                        red == 200 &&
                        blue == 0 &&
                        green == 250)

                    game.handler.addObject(new Enemy(xx*imgSize, yy*imgSize, ObjectId.Enemy,  game.handler, game.ss, "HLine", game,1)); // make 2 more options
                else if (
                        red == 0 &&
                        blue == 200 &&
                        green == 250)

                    game.handler.addObject(new Enemy(xx*imgSize, yy*imgSize, ObjectId.Enemy,  game.handler, game.ss, "VLine", game,1)); // make 2 more options
                else if (
                        red == 120 &&
                        blue == 0 &&
                        green == 50)

                    game.handler.addObject(new Enemy(xx*imgSize, yy*imgSize, ObjectId.Enemy,  game.handler, game.ss, "Bounce", game,0));
                else if (
                        red == 50 &&
                        blue == 50 &&
                        green == 50)

                    game.handler.addObject(new Enemy(xx*imgSize, yy*imgSize, ObjectId.Enemy,  game.handler, game.ss, "Slow", game,0));
                else if (
                        red == 240 &&
                        blue == 240 &&
                        green == 0)

                     game.handler.addObject(new AmmoBox(xx*imgSize, yy*imgSize, ObjectId.AmmoBox,  game.handler, game.ss));
                else if (
                        red == 100 &&
                        blue == 150 &&
                        green == 0)

                    game.handler.addObject(new HP(xx*imgSize, yy*imgSize, ObjectId.HP,  game.handler, game.ss));
                else if (
                        red == 150 &&
                        blue == 150 &&
                        green == 0)

                    game.handler.addObject(new Shotgun(xx*imgSize, yy*imgSize, ObjectId.Shotgun,  game.handler, game.ss));
                else if (
                        red == 0 &&
                        blue == 150 &&
                        green == 150)

                    game.handler.addObject(new DamageBlock(xx*imgSize, yy*imgSize, ObjectId.DamageBlock,  game.handler, game.ss));
                else if (
                        red == 150 &&
                        blue == 0 &&
                        green == 150)

                    game.handler.addObject(new SpeedBoost(xx*imgSize, yy*imgSize, ObjectId.SpeedBoost,  game.handler, game.ss));
                else if (
                        red == 150 &&
                        blue == 255 &&
                        green == 150)

                    game.handler.addObject(new BreakableBox(xx*imgSize, yy*imgSize, ObjectId.BreakableBox,  game.handler, game.ss, game));
                else if (
                        red == 200 &&
                        blue == 80 &&
                        green == 80)

                    game.handler.addObject(new Turret(xx*imgSize, yy*imgSize, ObjectId.Turret,  game.handler, game.ss, 1, game));
                else if (
                        red == 200 &&
                        blue == 150 &&
                        green == 150)

                    game.handler.addObject(new Turret(xx*imgSize, yy*imgSize, ObjectId.Turret,  game.handler, game.ss, 2, game));
                else if (
                        red == 200 &&
                        blue == 250 &&
                        green == 250)

                    game.handler.addObject(new Turret(xx*imgSize, yy*imgSize, ObjectId.Turret,  game.handler, game.ss, 3, game));
                else if (
                        red == 55 &&
                        blue == 55 &&
                        green == 55)

                    game.handler.addObject(new Turret(xx*imgSize, yy*imgSize, ObjectId.Turret,  game.handler, game.ss, 4, game));
                else if (
                        red == 120 &&
                        blue == 190 &&
                        green == 190)

                    game.handler.addObject(new Treasure(xx*imgSize, yy*imgSize, ObjectId.Treasure,  game.handler, game.ss));
            }
        }
    }
}
