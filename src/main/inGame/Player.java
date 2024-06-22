package main.inGame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends Entity {
    int hp;
    public int coorX;
    public int coorY;
    Player(){
        currentIndex = 0;
        images = new BufferedImage[2][8];
        loadImages();
        this.hp=20;
        this.name="resources/images/Player/SanZhang80.jpg";
    }
    public BufferedImage[][] images;
    public int currentIndex = 0;
    int updateCount = 0;
    public int facing=0;
    void loadImages(){
        String s_first = "resources/images/Player/SanZhang";
        String s_last = ".png";
        for(int i=0;i<8;i++){
            try {
                images[0][i] = ImageIO.read(new File(s_first+String.valueOf(i)+"L"+s_last));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for(int i=0;i<8;i++){
            try {
                images[1][i] = ImageIO.read(new File(s_first+String.valueOf(i)+"R"+s_last));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    void updateImage(){
        updateCount++;
        if(updateCount==25){
            currentIndex = (currentIndex+1)%8;
            updateCount = 0;
        }
    }
    public int speed = 20;
    public int toMoveX = 0;
    public int toMoveY = 0;
    public int isMovingX= 0;
    public int isMovingY = 0;
    public int directionX=0;
    public int directionY=0;
    public void completedMovingCheck() {
        if (toMoveX == isMovingX) {
            coorX += directionX;
            toMoveX = 0;
            isMovingX = 0;
            directionX = 0;
        } else {
            isMovingX += directionX * speed;
        }
        if (toMoveY == isMovingY) {
            coorY += directionY;
            toMoveY = 0;
            isMovingY = 0;
            directionY = 0;
            //System.out.println(coorX+" "+coorY);
        } else {
            isMovingY += directionY * speed;
        }
    }
}
