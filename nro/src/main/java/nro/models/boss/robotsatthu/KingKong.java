package nro.models.boss.robotsatthu;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;
import nro.services.TaskService;

/**
 *
 * @author ❤Girlkun75❤
 * @copyright ❤Trần Lại❤
 */
public class KingKong extends Boss {

    public KingKong() {
        super(BossFactory.KINGKONG, BossData.KINGKONG);
    }

    @Override
    public void joinMap() {
        try {
            super.joinMap();
            BossFactory.createBoss(BossFactory.POC).zone = this.zone;
            BossFactory.createBoss(BossFactory.PIC).zone = this.zone;
        } catch (Exception e) {
        }
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        generalRewards(pl);
    }

    @Override
    public void idle() {
    }

    @Override
    public void checkPlayerDie(Player pl) {
    }

    @Override
    public void initTalk() {
    }

}
