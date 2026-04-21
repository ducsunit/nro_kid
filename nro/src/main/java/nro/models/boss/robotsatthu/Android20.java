package nro.models.boss.robotsatthu;

import java.util.logging.Level;
import java.util.logging.Logger;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.PlayerService;
import nro.services.TaskService;

/**
 *
 * @author ❤Girlkun75❤
 * @copyright ❤Trần Lại❤
 */
public class Android20 extends Boss {

    public Android20() {
        super(BossFactory.ANDROID_20, BossData.ANDROID_20);
    }

    @Override
    public void joinMap() {
        try {
            super.joinMap();
            BossFactory.createBoss(BossFactory.ANDROID_19).zone = this.zone;
        } catch (Exception ex) {
            Logger.getLogger(Android20.class.getName()).log(Level.SEVERE, null, ex);
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

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if (plAtt != null) {
            switch (plAtt.playerSkill.skillSelect.template.id) {
                case Skill.KAMEJOKO:
                case Skill.MASENKO:
                case Skill.ANTOMIC:
                    PlayerService.gI().hoiPhuc(this, damage, 0);
                    return 0;
            }
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

}
