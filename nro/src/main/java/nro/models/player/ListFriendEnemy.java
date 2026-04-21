package nro.models.player;

import java.util.ArrayList;

/**
 *
 * @author DucSunIT
 * @copyright 💖 GirlkuN 💖
 *
 */
public class ListFriendEnemy<T> extends ArrayList<T> {

    public final Player player;

    public ListFriendEnemy(Player player) {
        this.player = player;
    }

}
