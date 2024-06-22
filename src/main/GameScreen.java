package main;

import main.inGame.Minion;
import main.inGame.StageInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static java.lang.Math.max;
import static java.lang.Math.min;

//This class is only responsible for the graphics of a stage
public class GameScreen extends JPanel implements Runnable {
    public Game game;
    StageInfo stage;
    private float UPS = 100;
    private float FPS = 100;
    HashMap<Integer,BufferedImage> blocksImg;
    boolean isCompleted = false;
    boolean isPaused = false;
    Move up;
    Move down;
    Move left;
    Move right;
    Thread thread;
    @Override
    public void run() {
        double time_for_update = System.nanoTime();
        double time_for_render = System.nanoTime();
        double diffU = 0;
        double diffR = 0;
        while(true){
            if(!isPaused) {
                float update_now = System.nanoTime();
                diffU += (update_now - time_for_update) / (1000000000.0/UPS);
                time_for_update = update_now;
                if(diffU>=1.0){
                    diffU--;
                    stage.update();
                }
                float render_now = System.nanoTime();
                diffR += (render_now-time_for_render) / (1000000000.0/FPS);
                time_for_render = render_now;
                if(diffR>=1.0){
                    diffR--;
                    repaint();
                }
            }
            if(stage.stageDone){
                game.loadStageCompletionMenu();
                break;
            }
            if(stage.stageOver){
                while(true){
                    if(System.nanoTime()-time_for_render>3000000000.0){
                        break;
                    }
                }
                game.loadStageOverMenu();
                break;
            }
        }
    }
    void startgameLoop(){
        thread = new Thread(this);
        thread.start();
    }
    private class Move extends AbstractAction{
        int x;
        int y;
        public Move(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!isPaused) {
                stage.characterMove(x,y);
                    //System.out.println(stage.player.coorX+" "+stage.player.coorY);
//                }
            }
        }
    }
    private class Pause extends AbstractAction{
        public Pause(){
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!isPaused){
                isPaused = true;
                game.loadPauseMenu();
            }
        }
    }
    Pause pause;
    public final int sizeX = 80;
    public final int sizeY = 80;
    private final int tilesX = 16;
    private final int tilesY = 9;
    public int offX;
    public int offY;
    public GameScreen(Game game, String fileName) throws IOException {
        this.game = game;
        //this.setBounds(0,0,sizeX*tilesX,sizeY*tilesY);
        this.setPreferredSize(new Dimension(sizeX*tilesX,sizeY*tilesY));
        this.setLayout(null);
        this.setBackground(Color.BLACK);
        stage = new StageInfo("Test",this);
        offX = 640 - stage.player.coorX*sizeX;
        offY = 360 - stage.player.coorY*sizeY;
        blocksImg = new HashMap<>();
        String s;
        //Collect all blocks from a stage to a Hashmap to draw
        for(int i: stage.blockID){
            s="resources/images/Map/"+ i +".png";
            BufferedImage image;
            try {
                image = ImageIO.read(new File(s));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            blocksImg.put(i,image);
        }
        //Define basic character movement
        up = new Move(0,-1);
        down = new Move(0,1);
        left = new Move(-1,0);
        right = new Move(1,0);
        //Add basic character movement
        this.getInputMap().put(KeyStroke.getKeyStroke("W"),"up");
        this.getActionMap().put("up",up);
        this.getInputMap().put(KeyStroke.getKeyStroke("A"),"left");
        this.getActionMap().put("left",left);
        this.getInputMap().put(KeyStroke.getKeyStroke("S"),"down");
        this.getActionMap().put("down",down);
        this.getInputMap().put(KeyStroke.getKeyStroke("D"),"right");
        this.getActionMap().put("right",right);
        //Pause game
        pause = new Pause();
        this.getInputMap().put(KeyStroke.getKeyStroke("P"),"pause");
        this.getActionMap().put("pause",pause);
        startgameLoop();
    }
    boolean isInMap = false;
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int lowX;
        int upX;
        int lowY;
        int upY;
        lowX = (stage.player.coorX-8);
        upX = (stage.player.coorX+9);
        lowY = (stage.player.coorY-4);
        upY = (stage.player.coorY+5);
        if(!isCompleted) {
            //Draw the map
            for (int i = max(lowX,0); i < min(upX,stage.dimension); i++) {
                for (int j = max(lowY,0); j < min(upY,stage.dimension); j++) {
                    g.drawImage(blocksImg.get(stage.map[i][j]).getSubimage(0, 0, sizeX, sizeY), sizeX * i + offX, sizeY * j + offY, null);
                }
            }
            //Draw the character
            g.drawImage(stage.player.images[stage.player.facing][stage.player.currentIndex], stage.player.coorX * sizeX +offX+stage.player.isMovingX, stage.player.coorY * sizeY + offY + stage.player.isMovingY, null);
            if(stage.stageOver){
                BufferedImage img;
                try {
                    img = ImageIO.read(new File("resources/images/Player/Alerted.png"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //g.drawImage(img,stage.player.coorX*sizeX+offX-stage.player.toMoveX+70,stage.player.coorY*sizeY+offY-stage.player.toMoveY+20,null);
                g.drawImage(img,stage.player.coorX*sizeX+offX,stage.player.coorY*sizeY+offY,null);
            }
            //Draw the Portal
            g.drawImage(stage.portal.img,stage.lastX*sizeX+offX,stage.lastY*sizeY+offY-24,null);
            //Draw the enemy
            BufferedImage img;
            for(Minion m: stage.minions){
                if(m.coorX>=lowX&&m.coorX<upX&&m.coorY>=lowY&&m.coorY<upY) {
                    g.drawImage(m.images[m.currentImage].getSubimage(m.imageIndexX*80, m.imageIndexY*80, 80, 80), m.coorX * sizeX + offX + m.isMovingX, m.coorY * sizeY + offY + m.isMovingY, null);
                    if(m.isAlerted){
                        try {
                            img = ImageIO.read(new File("resources/images/Enemies/Angry.png"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if(m.currentImage==0){
                            g.drawImage(img,m.coorX*sizeX+offX+m.isMovingX+60,m.coorY*sizeY+offY+m.isMovingY+20,null);
                        }
                        else{
                            g.drawImage(img,m.coorX*sizeX+offX+m.isMovingX-20,m.coorY*sizeY+offY+m.isMovingY-20,null);
                        }
                    }
                }
                //m.completedMovingCheck();
            }
        }
        else{
            //Draw the map to look at
        }
    }
}