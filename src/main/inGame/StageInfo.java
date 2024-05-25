package main.inGame;

import main.GameScreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import static java.lang.Math.abs;

//This class is responsible for the game logic of a stage
public class StageInfo {
    public int dimension;
    public int[][] map;
    public Entities entities;
    public HashSet<Integer> blockID;
    HashSet<String> blockList;
    public Player player;
    public Portal portal;
    public final int lastX;
    public final int lastY;
    public ArrayList<Minion> minions;
    GameScreen gameScreen;
    public StageInfo(String s, GameScreen gameScreen) throws IOException {
        this.gameScreen = gameScreen;
        player = new Player();
        portal = new Portal();
        minions = new ArrayList<>();
        s = "resources/data/" + s + ".txt";
        entities = new Entities();
        Scanner scanner = new Scanner(new File(s));
        dimension = scanner.nextInt();
        map = new int[dimension][dimension];
        blockID = new HashSet<>();
        blockList = new HashSet<>();
        for (int j = 0; j < dimension; j++) {
            for (int i = 0; i < dimension; i++) {
                map[i][j] = scanner.nextInt();
                if (!blockID.contains(Integer.valueOf(map[i][j]))) {
                    blockID.add(Integer.valueOf(map[i][j]));
                    blockList.add(entities.blocks.get(map[i][j] - 1).name);
                }
            }
        }
        //Read Initial Position
        player.coorX = scanner.nextInt();
        player.coorY = scanner.nextInt();
        //Read the Portal's position
        lastX = scanner.nextInt();
        lastY = scanner.nextInt();
        //Read the minions' positions
        int tmp;
        while(true) {
            tmp = scanner.nextInt();
            if(tmp==-1) break;
            else {
                Minion m = new Minion(tmp);
                m.name = entities.minions.get(tmp - 101).name;
                m.coorX = scanner.nextInt();
                m.coorY = scanner.nextInt();
                m.initializePosition();
                if(scanner.nextInt()==1){
                    m.isPatroling = true;
                }
                m.direction = scanner.nextInt();
                if (scanner.nextInt() == 1) {
                    m.movementType = true;
                }
                else m.movementType = false;
                m.loadImages();
                m.loadInitialImage();
                m.movementRadius = scanner.nextInt();
                m.sightLength = scanner.nextInt();
                m.speed = scanner.nextInt();
                minions.add(m);
            }
        }
        scanner.close();
    }
    public boolean stageOver = false;
    public boolean stageDone = false;
    public void update() {
//        if(player.completedMovingCheck()) {
//            gameScreen.offX -= player.directionX*gameScreen.sizeX;
//            gameScreen.offY -= player.directionY;
//            player.updateDirection();
//        }
        player.updateImage();
        player.completedMovingCheck();
        //System.out.println(player.coorX+" "+player.coorY);
//        if(player.movedR){
            gameScreen.offX -= player.speed*player.directionX;
//            gameScreen.offX -=gameScreen.sizeX;
//            player.movedR=false;
        //}if(player.movedL){
            gameScreen.offY -= player.speed*player.directionY;
//            gameScreen.offX += gameScreen.sizeX;
//            player.movedL=false;
//        }
        if(stageCompleted()){
            stageDone = true;
        }
        for(Minion m : minions){
            m.updateImage();
            m.completedMovingCheck();
            if(obstacleCheck(m,player)){
                stageOver = true;
                m.isAlerted = true;
//                m.loadAlertedImage();
            }
//            m.delay++;
//            if(m.delay==16){
//                m.delay = 0;
                m.updatePosition();
        }
//        }
    }

    private boolean obstacleCheck(Minion m, Player player) {
        if (!m.playerDetected(player.coorX, player.coorY)) {
            return false;
        }
        if (m.movementType) {
            if (m.coorX == player.coorX) {
                for (int i = m.coorY; i != player.coorY+m.direction; i += m.direction) {
                    if (!entities.blocks.get(map[m.coorX][i] - 1).passable) {
                        return false;
                    }
                }
                return true;
            }
            if (abs(m.coorX - player.coorX) == 1) {
                int y1 = player.coorY-m.direction;
                while (y1 != m.coorY) {
                    if (!entities.blocks.get(map[player.coorX][y1] - 1).passable) {
                        break;
                    } else {
                        y1 -= m.direction;
                    }
                }
                int y2 = m.coorY + m.direction;
                while (y2 != player.coorY) {
                    if (!entities.blocks.get(map[m.coorX][y2] - 1).passable) {
                        break;
                    } else {
                        y2 += m.direction;
                    }
                }
                if((y1-y2)*m.direction>=0){
                    return false;
                }
                else{
                    if(y1==m.coorY){
                        if((player.coorY-y2)*m.direction>0){
                            return false;
                        }
                        else return true;
                    }
                    if(y2==player.coorY){
                        if((y2-y1)*m.direction>1&&(y2-y1)*m.direction<=3&&(y1-m.coorX)*m.direction<=1){
                            return true;
                        }
                    }
                }
            }
            if (abs(m.coorX - player.coorX) == 2) {
                if (player.coorX > m.coorX) {
                    for (int j = m.coorY+m.direction; j!= player.coorY-2*m.direction; j += m.direction) {
                        if (!entities.blocks.get(map[m.coorX+1][j] - 1).passable) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    for (int j = m.coorY+m.direction; j!= player.coorY-2*m.direction; j += m.direction) {
                        if (!entities.blocks.get(map[m.coorX-1][j] - 1).passable) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        else {
            if (m.coorY == player.coorY) {
                for (int i = m.coorX; i != player.coorX+m.direction; i += m.direction) {
                    if (!entities.blocks.get(map[i][m.coorY] - 1).passable) {
                        return false;
                    }
                }
                return true;
            }
            if (abs(m.coorY - player.coorY) == 1) {
                if(abs(m.coorX-player.coorX)<=1){
                    return true;
                }
                int x1 = player.coorX-m.direction;
                while (x1 != m.coorX) {
                    if (!entities.blocks.get(map[x1][player.coorY] - 1).passable) {
                        break;
                    } else {
                        x1 -= m.direction;
                    }
                }
                int x2 = m.coorX + m.direction;
                while (x2 != player.coorX) {
                    if (!entities.blocks.get(map[x2][m.coorY] - 1).passable) {
                        break;
                    } else {
                        x2 += m.direction;
                    }
                }
                if((x1-x2)*m.direction>=0){
                    return false;
                }
                else{
                    if(x1==m.coorX){
                        if((player.coorX-x2)*m.direction>0){
                            return false;
                        }
                        else return true;
                    }
                    if(x2==player.coorX){
                        if((x2-x1)*m.direction>1&&(x2-x1)*m.direction<=3&&(x1-m.coorX)*m.direction<=1){
                            return true;
                        }
                    }
                }
            }
            if (abs(m.coorY - player.coorY) == 2) {
                if (player.coorY > m.coorY) {
                    for (int j = m.coorX+m.direction; j!= player.coorX-2*m.direction; j += m.direction) {
                        if (!entities.blocks.get(map[j][m.coorY + 1] - 1).passable) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    for (int j = m.coorX+m.direction; j!= player.coorX-2*m.direction; j += m.direction) {
                        if (!entities.blocks.get(map[j][m.coorY - 1] - 1).passable) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
    }
    final private class Entities {
        ArrayList<Block> blocks;
        ArrayList<Minion> minions;
        Entities() {
            blocks = new ArrayList<>();
            blocks.add(new Block(1,"Null",false));
            blocks.add(new Block(2, "BorderTopLeft",false));
            blocks.add(new Block(3, "BorderTopMid",false));
            blocks.add(new Block(4, "BorderTopRight",false));
            blocks.add(new Block(5, "BorderMidLeft",false));
            blocks.add(new Block(6, "BorderMidRight",false));
            blocks.add(new Block(7, "BorderBotLeft",false));
            blocks.add(new Block(8, "BorderBotMid",false));
            blocks.add(new Block(9, "BorderBotRight",false));
            blocks.add(new Block(10,"DirtRoad",true));
            blocks.add(new Block(11, "BorderAngle1",false));
            blocks.add(new Block(12, "BorderAngle2",false));
            blocks.add(new Block(13, "BorderAngle3",false));
            blocks.add(new Block(14, "BorderAngle4",false));
            minions = new ArrayList<>();
            minions.add(new Minion(101,"SkeletonSword"));
        }
    }

    public void characterMove(int x, int y) {
        int tempX = player.coorX+x;//+(int) ceil(player.toMoveX/80)+x;
        int tempY = player.coorY+y;//+(int) ceil(player.toMoveY/80)+y;
        //if(tempX>0&&tempY>0&&tempX<dimension&&tempY<dimension) {
            if (entities.blocks.get(map[tempX][tempY]-1).passable) {
                if(player.toMoveX==0&&player.toMoveY==0){
                    player.toMoveX += x * 80;
                    player.directionX = x;
                    player.toMoveY += y * 80;
                    player.directionY = y;
                    if(x==1){
                        player.facing = 0;
                    }
                    if(x==-1){
                        player.facing=1;
                    }
                }
            }
        System.out.println(tempX+" "+tempY);
        //}
    }
    public boolean stageCompleted(){
        if(player.coorX==lastX&&player.coorY==lastY){
            return true;
        }
        else return false;
    }
}
