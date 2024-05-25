package main.inGame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Portal extends Entity{
    public BufferedImage img;
    public Portal(){
        this.id = 100;
        this.name = "Portal";
        try{
            img = ImageIO.read(new File("resources/images/Portal.png"));
        }
        catch(Exception exception){
            exception.printStackTrace();
        }
    }
}
