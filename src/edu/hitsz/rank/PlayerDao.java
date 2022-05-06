package edu.hitsz.rank;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public interface PlayerDao {
    Player findByPlayerName(String playerName);
    List<Player> getAllPlayer();
    void doAdd(Player player);
    void doDelete(String playerName);
    void writeToFile(String fileName) throws IOException;
    void readFromFile(String fileName) throws IOException;
}
