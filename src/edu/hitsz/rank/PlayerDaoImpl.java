package edu.hitsz.rank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerDaoImpl implements PlayerDao{
    private List<Player> players;
    private volatile static PlayerDaoImpl playerDaoImpl;

    /**
     * 采用单例模式
     * 实例化对client隐蔽
     * 单例化通过双重检查锁定
     */
    public static PlayerDaoImpl getPlayerDaoImpl(){
        if(playerDaoImpl == null){
            synchronized (PlayerDaoImpl.class){
                if(playerDaoImpl == null){
                    playerDaoImpl = new PlayerDaoImpl();
                }
            }
        }
        return playerDaoImpl;
    }

    private PlayerDaoImpl() {
        players = new ArrayList<Player>();
    }

    @Override
    public void doDelete(String playerName) {
        players.removeIf(player -> Objects.equals(player.getPlayerName(), playerName));
    }

    @Override
    public List<Player> getAllPlayer() {
        return players;
    }

    @Override
    public Player findByPlayerName(String playerName) {
        for (Player player : players) {
            if (Objects.equals(player.getPlayerName(), playerName)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public void doAdd(Player player) {
        if(players.isEmpty()){
            players.add(player);
            return;
        }
        for(int i = 0;i < players.size();i++){
            if(players.get(i).getPlayerScore() < player.getPlayerScore()){
                players.add(i,player);
                return;
            }
        }
        players.add(player);
    }

    @Override
    public void writeToFile(String fileName) throws IOException {
        File file = new File(fileName+".dat");
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(players);
            out.flush();
            out.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void readFromFile(String fileName) throws IOException{
        File file = new File(fileName+".dat");
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(file));
            players = castList(in.readObject(),Player.class);
            in.close();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private static <T> List<T> castList(Object obj,Class<T> thisClass){
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>){
            for(Object o : (List<?>) obj){
                    result.add(thisClass.cast(o));
            }
            return result;
        }
        return null;
    }

}
