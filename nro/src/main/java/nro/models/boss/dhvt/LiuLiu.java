package nro.models.boss.dhvt;

import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;

/**
 * @author DucSunIT
 */
public class LiuLiu extends BossDHVT {

    public LiuLiu(Player player) {
        super(BossFactory.LIU_LIU, BossData.LIU_LIU);
        this.playerAtt = player;
    }
}