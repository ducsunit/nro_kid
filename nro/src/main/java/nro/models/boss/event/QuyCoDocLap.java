package nro.models.boss.event;

import nro.consts.ConstItem;
import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

import nro.models.boss.BossManager;
import nro.services.SkillService;
import nro.utils.SkillUtil;

/**
 * @author DucSunIT
 * @copyright 💖 GirlkuN 💖
 */
public class QuyCoDocLap extends Boss {

    public QuyCoDocLap() {
        super(BossFactory.QUY_CO_DOC_LAP, BossData.QUY_CO_DOC_LAP);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if (damage >= 20_000) {
            damage = 20_000;
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    @Override
    public void attack() {
        try {
            Player pl = getPlayerAttack();
            if (pl != null) {
                if (!useSpecialSkill()) {
                    this.playerSkill.skillSelect = this.getSkillAttack();
                    if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                        if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                            goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50), false);
                        }
                        SkillService.gI().useSkill(this, pl, null, null);
                        checkPlayerDie(pl);
                    } else {
                        goToPlayer(pl, false);
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void rewards(Player pl) {
        int[] tempIds2 = new int[]{15,16,17,18,19,20};
        int tempId = -1;

        if (Util.isTrue(1, 2)) {
            tempId = 589;
        } else if (Util.isTrue(1, 10)) {
            tempId = tempIds2[Util.nextInt(0, tempIds2.length - 1)];
        }

        if (tempId != -1) {
            ItemMap itemMap = new ItemMap(this.zone, tempId, 1,
                    pl.location.x, this.zone.map.yPhysicInTop(pl.location.x, pl.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, itemMap);
        }
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
        this.textTalkMidle = new String[]{"Oải rồi hả?", "Ê cố lên nhóc",
                "Chúc mừng ngày 8/3", "Ta là quý cô độc lập", "Bồ bịch gì chưa người đẹp"};

    }

    @Override
    public void leaveMap() {
        try {
            BossFactory.createBoss(BossFactory.QUY_CO_DOC_LAP).setJustRest();
            super.leaveMap();
            BossManager.gI().removeBoss(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
