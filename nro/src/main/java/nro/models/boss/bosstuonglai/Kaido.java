package nro.models.boss.bosstuonglai;

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
public class Kaido extends Boss {

    public Kaido() {
        super(BossFactory.KAIDO, BossData.KAIDO);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if (damage >= 300000) {
            damage = 300000;
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
        } catch (Exception ex) {
        }
    }

    @Override
    public void rewards(Player pl) {
        int a = 0;
        if (Util.isTrue(10, 100)) {
            
            ItemMap itemMap = new ItemMap(this.zone, 1547, 1,
                    this.location.x - 10, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            
            Service.getInstance().dropItemMap(this.zone, itemMap);
        } else if (Util.isTrue(30, 100)) {
            for (int i = 0; i < 10; i++) {
                ItemMap itemMap = new ItemMap(this.zone, 1479, 1,
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), -1);
                Service.getInstance().dropItemMap(this.zone, itemMap);
                a += 10;
            }
        } else if (Util.isTrue(20, 100)) {
            for (int j = 0; j < 30; j++) {
                ItemMap itemMap1 = new ItemMap(this.zone, 1535, 1,
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), -1);
                Service.getInstance().dropItemMap(this.zone, itemMap1);
                a += 10;
            }
        } else {
            for (int j = 0; j < 20; j++) {
                ItemMap itemMap1 = new ItemMap(this.zone, 1237, 1,
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), -1);
                Service.getInstance().dropItemMap(this.zone, itemMap1);
                a += 10;
            }
        }
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
            "Chán", "Ta có nhầm không nhỉ"};

    }

    @Override
    public void leaveMap() {
        try{
            BossFactory.createBoss(BossFactory.KAIDO).setJustRest();
            super.leaveMap();
            BossManager.gI().removeBoss(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
