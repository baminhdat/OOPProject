package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BreakScene extends JPanel {
    Game game;
    Button continueButton;
    Button nextStageButton;
    Button returnToStartSceneButton;
    Button retryButton;
    public BreakScene(Game game,String s){
        this.game = game;
        this.setBounds(440,210,400,400);
        this.setLayout(null);
        this.setBackground(Color.BLUE);
        returnToStartSceneButton = new Button("Exit");
        //returnToStartSceneButton.setPreferredSize(new Dimension(240,150));
        returnToStartSceneButton.setBounds(100,50,200,100);
        returnToStartSceneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    game.loadStartScene();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        this.add(returnToStartSceneButton);
        if(s.compareTo("Pause")==0){
            continueButton = new Button("Continue");
            //continueButton.setPreferredSize(new Dimension(240,150));
            continueButton.setBounds(100,200,200,100);
            continueButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    continueGame();
                }
            });
            this.add(continueButton);
        }
        if(s.compareTo("Break")==0){
            nextStageButton = new Button("Next");
            //nextStageButton.setPreferredSize(new Dimension(240,150));
            nextStageButton.setBounds(100,200,200,100);
            nextStageButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        game.loadGame("");
                    }
                    catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
            });
            this.add(nextStageButton);
        }
        if(s.compareTo("Over")==0){
            retryButton = new Button("Retry");
            retryButton.setBounds(100,200,200,100);
            retryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        game.loadGame("Test");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            this.add(retryButton);
        }
    }
    void continueGame(){
        game.gameScreen.isPaused = false;
        game.window.getContentPane().remove(this);
    }
    public void paintComponent(Graphics g){
        BufferedImage img;
        try {
            img = ImageIO.read(new File("resources/images/SanZhang.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        g.drawImage(img,100,0,null);
    }

}
