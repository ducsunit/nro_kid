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
public class LuffyThanNika extends Boss {

    public LuffyThanNika() {
        super(BossFactory.LUFFY_THAN_NIKA, BossData.LUFFY_THAN_NIKA);
    }

    @Override
    protected boolean useSpecialSkill() {
        this.playerSkill.skillSelect = this.getSkillSpecial();
        if (SkillService.gI().canUseSkillWithCooldown(this)) {
            SkillService.gI().useSkill(this, null, null, null);
            return true;
        } else {
            return false;
        }
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
        short[] temp = {1066, 1067, 1068, 1069, 1070};
        if (Util.isTrue(60, 100)) {
            for (int k = 0; k < 15; k++) {
                ItemMap itemMap2 = new ItemMap(this.zone, temp[Util.nextInt(0, temp.length - 1)], 10,
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, itemMap2);
                a += 10;
            }
        } else if (Util.isTrue(20, 100)) {
            for (int i = 0; i < 5; i++) {
                ItemMap itemMap = new ItemMap(this.zone, 1479, 1,
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, itemMap);
                a += 10;
            }
        } else {
            for (int j = 0; j < 10; j++) {
                ItemMap itemMap1 = new ItemMap(this.zone, 1535, 1,
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
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
            BossFactory.createBoss(BossFactory.LUFFY_THAN_NIKA).setJustRest();
            super.leaveMap();
            BossManager.gI().removeBoss(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
