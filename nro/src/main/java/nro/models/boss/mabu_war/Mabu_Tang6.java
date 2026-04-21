package nro.models.boss.mabu_war;

import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author DucSunIT
 */
public class Mabu_Tang6 extends BossMabuWar {

    public Mabu_Tang6() {
        super(BossFactory.MABU_MAP, BossData.MABU_MAP);
    }

    @Override
    public void attack() {
        if (this.isDie()) {
            this.zone.finishMabuWar = true;
            die();
            return;
        }
        try {
            if (Util.isTrue(50, 100)) {
                this.talk();
            }
            Player pl = getPlayerAttack();
            if (pl != null) {
                if (!useSpecialSkill()) {
                    this.playerSkill.skillSelect = this.getSkillAttack();
                    if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                        if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                            goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50), false);
                        }
                        SkillService.gI().useSkill(this, pl, null,null);
                        checkPlayerDie(pl);
                    } else {
                        goToPlayer(pl, false);
                    }
                }
            }
        } catch (Exception ex) {
            Log.error(Mabu_Tang6.class, ex);
        }
    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if (this.isDie()) {
            return 0;
        } else {
            if (plAtt != null) {
                if (Util.isTrue(20, 100)) {
                    plAtt.addPercentPowerPoint(1);
                    Service.getInstance().sendPowerInfo(plAtt, "%", plAtt.getPercentPowerPont());
                }
                int skill = plAtt.playerSkill.skillSelect.template.id;
                if (skill == Skill.KAMEJOKO || skill == Skill.ANTOMIC || skill == Skill.MASENKO || skill == Skill.LIEN_HOAN) {
                    damage = 1;
                    Service.getInstance().chat(this, "Chưởng trúng cho con bò..");
                } else if (Util.isTrue(25,100)) {
                    damage = 1;
                    Service.getInstance().chat(this, "Xí hụt..");
                }
            }
            long dame = super.injuredNotCheckDie(plAtt, damage, piercing);
            if (this.isDie()) {
                rewards(plAtt);
            }
            return dame;
        }
    }

    @Override
    public void joinMap() {
        if (this.zone != null) {
            ChangeMapService.gI().changeMap(this, this.zone, 360, 336);
        }
    }

    @Override
    public void idle() {

    }

    @Override
    public void rewards(Player pl) {
        try {
            if (Util.isTrue(1, 15)) {
                ItemMap itemMap = new ItemMap(this.zone, 1068, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x, 100), -1);
                Service.getInstance().dropItemMap(this.zone, itemMap);
            }
            int[] listitem = {15, 16, 861};
            ItemMap itemMap = new ItemMap(this.zone, listitem[Util.nextInt(0, listitem.length - 1)], 1, pl.location.x, this.zone.map.yPhysicInTop(pl.location.x, pl.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, itemMap);
        } catch (Exception ex) {
            Logger.getLogger(Mabu_Tang6.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{"Bư! Bư! Bư!", "Bư! Bư! Bư!"};
        this.textTalkMidle = new String[]{"Oe Oe Oe"};
        this.textTalkAfter = new String[]{"Huhu"};
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        this.changeToIdle();
    }
}
