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
public class BongBang extends Boss {

    public BongBang() {
        super(BossFactory.BONG_BANG, BossData.BONG_BANG);
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
        int nro_bang = Util.nextInt(925, 931);

        if (Util.isTrue(1, 2) && this.zone != null && pl != null) {
            int dropX = (this.location != null) ? this.location.x - 5 : pl.location.x;
            int dropY = (this.zone.map != null) ? this.zone.map.yPhysicInTop(dropX, pl.location.y - 24) : pl.location.y;

            ItemMap itemMap1 = new ItemMap(this.zone, nro_bang, 1, dropX, dropY, pl.id);

            if (Service.getInstance() != null) {
                Service.getInstance().dropItemMap(this.zone, itemMap1);
            }
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
            "Chán", "Ta có nhầm không nhỉ"};

    }

    @Override
    public void leaveMap() {
        try {
            BossFactory.createBoss(BossFactory.BONG_BANG).setJustRest();
            super.leaveMap();
            BossManager.gI().removeBoss(this);
        } catch (Exception ignored) {
        }
    }

}
