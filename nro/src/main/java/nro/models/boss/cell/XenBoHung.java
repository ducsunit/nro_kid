package nro.models.boss.cell;

import java.util.logging.Level;
import java.util.logging.Logger;
import nro.consts.ConstItem;
import nro.consts.ConstPlayer;
import nro.consts.ConstRatio;
import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.services.PlayerService;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

/**
 *
 * @author DucSunIT
 * @copyright 💖 GirlkuN 💖
 *
 */
public class XenBoHung extends FutureBoss {

    public XenBoHung() {
        super(BossFactory.XEN_BO_HUNG, BossData.XEN_BO_HUNG);
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
//        if (BossManager.gI().getBossById(BossFactory.XEN_CON) != null) {
//            PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.NON_PK);
//            this.changeIdle();
//            return;
//        }
        if (this.isDie()) {
            tuSat();
            die();
            return;
        }
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
            Log.error(XenBoHung.class, ex);
        }
    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if (this.isDie() || this.playerSkill.prepareTuSat) {
            return 0;
        } else {
            long dame = super.injuredNotCheckDie(plAtt, damage, piercing);
            if (this.isDie()) {
                rewards(plAtt);
            }
            return dame;
        }
    }

    private void tuSat() {
        try {
            this.nPoint.hpg = 1000000000;
            this.nPoint.hp = 1;
            ChangeMapService.gI().changeMap(this, this.zone, this.location.x, this.location.y);
            PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.NON_PK);
            PlayerService.gI().changeTypePK(this, ConstPlayer.PK_ALL);
            this.playerSkill.skillSelect = this.getSkillById(Skill.TU_SAT);
            SkillService.gI().useSkill(this, null, null, null);
            Thread.sleep(3000);
            SkillService.gI().useSkill(this, null, null, null);
        } catch (Exception e) {
            Log.error(XenBoHung.class, e);
        }
    }

    @Override
    public void joinMap() {
        if (BossManager.gI().getBossById(BossFactory.SIEU_BO_HUNG) == null) {
            try {
                BossFactory.createBoss(BossFactory.XEN_CON);
                super.joinMap();
            } catch (Exception ex) {
                Logger.getLogger(XenBoHung.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void idle() {
        if (BossManager.gI().getBossById(BossFactory.XEN_CON) == null) {
            PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.PK_ALL);
            changeAttack();
        }
    }

    @Override
    public void rewards(Player pl) {
        if (pl != null) {
            ItemMap itemMap = null;
            int x = this.location.x;
            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
            if (Util.isTrue(1, 100)) {
                int[] set1 = {562, 564, 566, 561};
                itemMap = new ItemMap(this.zone, set1[Util.nextInt(0, set1.length - 1)], 1, x, y, pl.id);
                RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
            } else if (Util.isTrue(1, 30)) {
                int[] set2 = {555, 556, 563, 557, 558, 565, 559, 567, 560};
                itemMap = new ItemMap(this.zone, set2[Util.nextInt(0, set2.length - 1)], 1, x, y, pl.id);
                RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
            } else if (Util.isTrue(1, 5)) {
                itemMap = new ItemMap(this.zone, 15, 1, x, y, pl.id);
            } else if (Util.isTrue(1, 2)) {
                itemMap = new ItemMap(this.zone, 16, 1, x, y, pl.id);
            }
            if (Manager.EVENT_SEVER == 4 && itemMap == null) {
                itemMap = new ItemMap(this.zone, ConstItem.LIST_ITEM_NLSK_TET_2023[Util.nextInt(0, ConstItem.LIST_ITEM_NLSK_TET_2023.length - 1)], 1, x, y, pl.id);
            }
            if (itemMap != null) {
                Service.getInstance().dropItemMap(zone, itemMap);
            }
            notifyPlayeKill(pl);
        }
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        generalRewards(pl);
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{"Ta cho các ngươi 5 giây để chuẩn bị", "Cuộc chơi bắt đầu.."};
        this.textTalkMidle = new String[]{"Kame Kame Haaaaa!!", "Mi khá đấy nhưng so với ta chỉ là hạng tôm tép",
            "Tất cả nhào vô hết đi", "Cứ chưởng tiếp đi. haha", "Các ngươi yếu thế này sao hạ được ta đây. haha",
            "Khi công pháo!!", "Cho mi biết sự lợi hại của ta"};
        this.textTalkAfter = new String[]{};
    }

    @Override
    public void leaveMap() {
        try {
            Boss sieuBoHung = BossFactory.createBoss(BossFactory.SIEU_BO_HUNG);
            sieuBoHung.zone = this.zone;
            super.leaveMap();
            this.changeToIdle();
        } catch (Exception e) {
        }
    }
}
