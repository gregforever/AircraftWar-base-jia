package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.mode.EasyModeGame;
import edu.hitsz.application.mode.HardModeGame;
import edu.hitsz.application.mode.NormalModeGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.IOException;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;

/**
 * @author xuhe
 */
public class DiffAndMusicMenu {
    private static final int DEFAULT_WIDTH = 512;
    private static final int DEFAULT_HEIGHT = 768;

    private JPanel mainPanel;
    private JPanel gameModelPanel;
    private JButton easyButton;
    private JButton hardButton;
    private JButton normalButton;
    private JComboBox<String> musicChoiceCombo;
    private JLabel musicText;
    private JPanel musicControlPanel;
    private JLabel bulletMusicText;
    private JComboBox<String> bulletMusicChoiceCombo;

    public DiffAndMusicMenu() {
        easyButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg.jpg"));
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
                Main.game = new EasyModeGame();
                synchronized (Main.changeObject){
                    Main.changeObject.notify();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e){
                easyButton.setText("太拉了吧");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                easyButton.setText("简单模式");
            }
        });
        normalButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg3.jpg"));
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
                Main.game = new NormalModeGame();
                synchronized (Main.changeObject){
                    Main.changeObject.notify();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e){
                normalButton.setText("渍渍渍");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                normalButton.setText("普通模式");
            }
        });
        hardButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg5.jpg"));
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
                Main.game = new HardModeGame();
                synchronized (Main.changeObject){
                    Main.changeObject.notify();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e){
                hardButton.setText("你很勇嘛");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hardButton.setText("困难模式");
            }
        });
        musicChoiceCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String choice = musicChoiceCombo.getSelectedItem().toString();
                if (choice.equalsIgnoreCase("关")){
                    MusicThread.allRunFlag = false;
                }else if(choice.equalsIgnoreCase("开")){
                    MusicThread.allRunFlag = true;
                }
            }
        });
        bulletMusicChoiceCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String choice = bulletMusicChoiceCombo.getSelectedItem().toString();
                if (choice.equalsIgnoreCase("关")){
                    Game.bulletMusicFlag = false;
                }else if(choice.equalsIgnoreCase("开")){
                    Game.bulletMusicFlag = true;
                }
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
