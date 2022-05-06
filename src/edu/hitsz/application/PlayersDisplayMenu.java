package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.rank.Player;
import edu.hitsz.rank.PlayerDao;
import edu.hitsz.rank.PlayerDaoImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;

/**
 * @author xuhe
 */
public class PlayersDisplayMenu {
    private JPanel mainPanel;
    private JPanel bottomPanel;
    private JPanel topPanel;
    private JLabel diffShow;
    private JPanel middlePanel;
    private JTable scoreTable;
    private JButton deleteSelectedButton;
    private JLabel diff;
    private JLabel scoreRank;
    private JScrollPane scoreTablePane;
    private JButton restartButton;
    private JButton exitButton;
    private JPanel bottomBottomPanel;
    private final PlayerDao playerDao = PlayerDaoImpl.getPlayerDaoImpl();
    private static DefaultTableModel scoreModel;

    public PlayersDisplayMenu(JFrame frame){
        int score = Game.getScore();
        String gameModel = Game.getGameModel();
        scoreTable.setRowHeight(20);
        scoreTable.getTableHeader().setFont(new Font("TimesRoman",Font.BOLD,24));
        scoreTable.getTableHeader().setResizingAllowed(false);
        scoreTable.getTableHeader().setReorderingAllowed(false);
        if("easy".equals(gameModel)){
            diffShow.setText("简单");
        }else if("normol".equals(gameModel)){
            diffShow.setText("普通");
        }else {
            diffShow.setText("困难");
        }
        try {
            playerDao.readFromFile(gameModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshScoreTable(playerDao.getAllPlayer());
        scoreTablePane.setViewportView(scoreTable);

        JDialog newPlayerShow = new JDialog(frame,"输入",true);
        NewPlayerMenu newPlayerMenu = new NewPlayerMenu(score,gameModel);
        newPlayerMenu.setDialog(newPlayerShow);
        newPlayerMenu.setPlayersDisplayMenu(this);
        newPlayerMenu.initDialog();

        deleteSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = scoreTable.getSelectedRow();
                System.out.println(row);
                if (row != -1){
;                   JDialog deleteShow = new JDialog(frame,"选择一个选项",true);
                    DeleteMenu deleteMenu = new DeleteMenu(row,gameModel);
                    deleteMenu.setDialog(deleteShow);
                    deleteMenu.setPlayersDisplayMenu(PlayersDisplayMenu.this);
                    deleteMenu.initDialog();
                }
            }
        });
        restartButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                synchronized (Main.changeObject){
                    Main.changeObject.notify();
                    HeroAircraft.resetHeroAircraft();
                    Game.bulletMusicFlag = false;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e){
                restartButton.setText("针不撮");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                restartButton.setText("再来亿把");
            }
        });
        exitButton.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                Main.runGameFlag = false;
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
                exitButton.setText("别走，我会想你");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitButton.setText("退出");
            }
        });
    }

    public void refreshScoreTable(List<Player> players){
        int playersNum = players.size();
        String[] columeName = {"名次","玩家昵称","分数","记录时间"};
        String[][] columeData = new String[playersNum][4];
        for (int i = 0; i < playersNum; i++) {
            Player player = players.get(i);
            columeData[i][0] = Integer.toString(i+1);
            columeData[i][1] = player.getPlayerName();
            columeData[i][2] = Integer.toString(player.getPlayerScore());
            columeData[i][3] = player.getPlayerTime();
        }
        scoreModel =  new DefaultTableModel(columeData,columeName){
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        scoreTable.setModel(scoreModel);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
