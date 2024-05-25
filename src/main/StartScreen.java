package main;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StartScreen extends JPanel {
    Game game;
    int x = 540;
    int y = 200;
    JLabel jLabel;
    public StartScreen(Game game) throws IOException {
        this.game = game;
        this.setPreferredSize(new Dimension(1296,720));
        //this.setBounds(0,0,1280,720);
        this.setBackground(Color.BLACK);
        this.setLayout(null);
        Image img = ImageIO.read(new File("resources/images/Title.png"));
        ImageIcon icon = new ImageIcon(img);
        jLabel = new JLabel(icon);
        jLabel.setBounds(0,0,1000,300);
        this.add(jLabel);
        this.addButton("Bắt Đầu");
        this.addButton("Kết thúc");
    }
    public void addButton(String s){
        JButton button = new JButton(s);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    game.loadGame("Test");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        button.setOpaque(false);
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial",Font.BOLD,40));
        button.setBounds(x,y,200,80);
        y+=150;
        this.add(button);
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage bg = null;
        try {
            bg = ImageIO.read(new File("resources/images/SanZhang.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        g.drawImage(bg,850,400,null);
    }
}
