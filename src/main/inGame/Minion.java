package main.inGame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.abs;

public class Minion extends Enemy {
    public int coorX;
    public int coorY;
    private int oriX;
    private int oriY;
    Minion(int id){
        super();
        this.id = id;
        images = new BufferedImage[2];
    }
    Minion(int id, String name){
        super(id,name);
    }
    boolean isPatroling;
    //True if patrols vertically, else horizontally
    boolean movementType;
    //1 if is facing right/down, -1 if left/up
    int direction;
    int movementRadius;
    int sightLength;
    int speed;
    void initializePosition(){
        oriX = coorX;
        oriY = coorY;
    }
    int delay = 0;
    public void updatePosition(){
        if(isPatroling) {
            if (movementType && toMoveY == 0) {
                //Adjust coorY
                toMoveY += 80 * direction;
            }
            if (!movementType && toMoveX == 0) {
                //Adjust coorX
                toMoveX += 80 * direction;
            }
        }
    }
    public int isMovingX=0;
    public int isMovingY=0;
    int toMoveX=0;
    int toMoveY=0;
    boolean playerDetected(int x,int y){
        if(movementType){
            if(direction==1){
                if(y<=coorY+direction*sightLength&&y>coorY){
//                    if(y-coorY==1&&x==coorX){
//                        return true;
//                    }
                    if(y-coorY==3||y-coorY==2||y-coorY==1){
                        if(abs(x-coorX)<=1){
                            return true;
                        }
                    }
                    if(y-coorY==5||y-coorY==4){
                        if(abs(x-coorX)<=2){
                            return true;
                        }
                    }
                }
                return false;
            }
            else{
                if(y>=coorY+direction*sightLength&&y<coorY){
//                    if(coorY-y==1&&x==coorX){
//                        return true;
//                    }
                    if(coorY-y<=3){
                        if(abs(x-coorX)<=1){
                            return true;
                        }
                    }
                    if(coorY-y==5||coorY-y==4){
                        if(abs(x-coorX)<=2){
                            return true;
                        }
                    }
                }
                return false;
            }
        }
        else{
            if(direction==1){
                if(x<=coorX+direction*sightLength&&x>coorX){
                    if(x-coorX==3||x-coorX==2||x-coorX==1){
                        if(abs(y-coorY)<=1){
                            return true;
                        }
                    }
                    if(x-coorX==5||x-coorX==4){
                        if(abs(y-coorY)<=2){
                            return true;
                        }
                    }
                }
                return false;
            }
            else{
                if(x>=coorX+direction*sightLength&&x<coorX){
//                    if(coorX-x==1&&y==coorY){
//                        return true;
//                    }
                    if(coorX-x==3||coorX-x==2||coorX-x==1){
                        if(abs(y-coorY)<=1){
                            return true;
                        }
                    }
                    if(coorX-x==5||coorX-x==4){
                        if(abs(y-coorY)<=2){
                            return true;
                        }
                    }
                }
                return false;
            }
        }
//        if(x==coorX){
//            if(y<=coorY+sightLength&&y>=coorY-sightLength){
//                return true;
//            }
//        }
//        if(y==coorY){
//            if(x<=coorX+sightLength*direction){
//                return true;
//            }
//        }
//        return false;
    }
    public BufferedImage[] images;
    public void completedMovingCheck() {
        if(movementType){
            if(isMovingY==toMoveY){
                isMovingY=0;
                toMoveY=0;
                coorY+=direction;
                if(abs(coorY-oriY)==movementRadius){
                    direction = -1*direction;
                    loadInitialImage();
                }
            }
            else{
                isMovingY+=speed*direction;
            }
        }
        else{
            if(isMovingX==toMoveX){
                isMovingX=0;
                toMoveX=0;
                coorX+=direction;
                if(abs(coorX-oriX)==movementRadius){
                    direction = -1*direction;
                    loadInitialImage();
                }
            }
            else{
                isMovingX+=speed*direction;
            }
        }
    }
    public int currentImage = 0;
    public void loadImages() throws IOException {
        if(movementType){
            images[0] = ImageIO.read(new File("resources/images/Enemies/SkeletonWalkingU.png"));
            images[1] = ImageIO.read(new File("resources/images/Enemies/SkeletonWalkingD.png"));
        }
        else{
            images[0] = ImageIO.read(new File("resources/images/Enemies/SkeletonWalkingL.png"));
            images[1] = ImageIO.read(new File("resources/images/Enemies/SkeletonWalkingR.png"));
        }
    }

    public void loadInitialImage() {
        if(direction==-1){
            currentImage = 0;
        }
        else{
            currentImage = 1;
        }
    }
    int updateCount = 0;
    public int imageIndexX = 0;
    public int imageIndexY = 0;
    public void updateImage() {
        updateCount++;
        if(updateCount==25){
            updateCount=0;
            if(imageIndexY==1&&imageIndexX==3){
                imageIndexX = 0 ;
                imageIndexY = 0;
                return;
            }
            if(imageIndexY==0&&imageIndexX==3){
                imageIndexY+=1;
                imageIndexX=0;
                return;
            }
            imageIndexX++;
        }
    }
    public boolean isAlerted = false;
}
