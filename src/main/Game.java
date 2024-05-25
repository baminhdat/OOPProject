package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Game{
    public JFrame window;
    public StartScreen startScreen;
    public GameScreen gameScreen;
    public BreakScene breakScene;
    public Game() throws IOException {
        window = new JFrame();
        window.setSize(1296,720);
        window.setTitle("Test");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        //window.setLayout(null);
        loadStartScene();
    }
    void loadStartScene() throws IOException {
        window.getContentPane().removeAll();
        StartScreen start = new StartScreen(this);
        this.startScreen = start;
        window.setContentPane(start);
        //window.add(start);
        window.setVisible(true);
    }
    void loadGame(String s) throws IOException {
        GameScreen gameScreen = new GameScreen(this,"");
        window.getContentPane().removeAll();
        this.gameScreen = gameScreen;
        window.setContentPane(gameScreen);
        window.setVisible(true);
        gameScreen.setFocusable(true);
        gameScreen.requestFocus();
    }

    public void loadStageCompletionMenu() {
        BreakScene breakScene = new BreakScene(this,"Break");
        this.breakScene = breakScene;
        //gameScreen.add(breakScene);
        window.add(breakScene);
        window.setVisible(true);
    }
    public void loadStageOverMenu(){
        BreakScene breakScene = new BreakScene(this,"Over");
        this.breakScene = breakScene;
        gameScreen.add(breakScene);
        window.setVisible(true);
    }

    public void loadPauseMenu(){
        BreakScene breakScene = new BreakScene(this,"Pause");
        this.breakScene = breakScene;
        gameScreen.add(breakScene);
        window.setVisible(true);
    }
}
