package tbject.com.bombswepper;


import java.util.Comparator;

import tbject.com.bombswepper.pojo.Player;

public class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare(Player player1, Player player2) {
        return player1.getTime().compareTo(player2.getTime());
    }
}
