package edu.hitsz.application;

import edu.hitsz.rank.Player;
import edu.hitsz.rank.PlayerDao;
import edu.hitsz.rank.PlayerDaoImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class DeleteMenu {
    private JDialog dialog;
    private JPanel mainPanel;
    private JButton yesButton;
    private JButton cancelButton;
    private JButton noButton;
    private JPanel bottomPanel;
    private JPanel topPanel;
    private JLabel dialogContext;
    private final PlayerDao playerDao = PlayerDaoImpl.getPlayerDaoImpl();
    private PlayersDisplayMenu playersDisplayMenu;

    public void setPlayersDisplayMenu(PlayersDisplayMenu playersDisplayMenu) {
        this.playersDisplayMenu = playersDisplayMenu;
    }

    public void setDialog(JDialog dialog) {
        this.dialog = dialog;

    }

    public void initDialog(){
        dialog.setLocationRelativeTo(null);
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    public DeleteMenu(int row,String gameModel) {
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerDao.getAllPlayer().remove(row);
                playersDisplayMenu.refreshScoreTable(playerDao.getAllPlayer());
                try {
                    playerDao.writeToFile(gameModel);
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
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
