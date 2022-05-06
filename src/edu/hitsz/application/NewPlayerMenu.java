package edu.hitsz.application;

import edu.hitsz.rank.Player;
import edu.hitsz.rank.PlayerDao;
import edu.hitsz.rank.PlayerDaoImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewPlayerMenu {
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JTextField nameInput;
    private JButton yesButton;
    private JButton anonymButton;
    private JLabel nameTip;
    private JButton noButton;
    private JDialog dialog;
    private PlayersDisplayMenu playersDisplayMenu;
    private final PlayerDao playerDao = PlayerDaoImpl.getPlayerDaoImpl();
    private String playerName;

    public void setDialog(JDialog dialog) {
        this.dialog = dialog;
    }

    public void setPlayersDisplayMenu(PlayersDisplayMenu playersDisplayMenu) {
        this.playersDisplayMenu = playersDisplayMenu;
    }

    public void initDialog(){
        dialog.setLocationRelativeTo(null);
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    public NewPlayerMenu(int score, String gameModel) {
        Date day = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("MM-dd HH:mm:ss");
        nameTip.setText("游戏结束，你的得分为"+score+"请输入名字记录分数：");

        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerName = nameInput.getText();
                playerDao.doAdd(new Player(playerName,score,dt.format(day)));
                try {
                    playerDao.writeToFile(gameModel);
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
                playersDisplayMenu.refreshScoreTable(playerDao.getAllPlayer());
                dialog.dispose();
            }
        });
        anonymButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerDao.doAdd(new Player("匿名的",score,dt.format(day)));
                try {
                    playerDao.writeToFile(gameModel);
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
                playersDisplayMenu.refreshScoreTable(playerDao.getAllPlayer());
                dialog.dispose();
            }
        });
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
    }

}
