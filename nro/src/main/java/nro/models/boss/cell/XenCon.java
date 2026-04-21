package nro.models.boss.cell;

import nro.consts.ConstItem;
import nro.consts.ConstRatio;
import nro.models.boss.*;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

/**
 *
 * @author DucSunIT
 * @copyright 💖 GirlkuN 💖
 *
 */
public class XenCon extends FutureBoss {

    public XenCon() {
        super(BossFactory.XEN_CON, BossData.XEN_CON);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }


    @Override
    public void rewards(Player pl) {
        ItemMap itemMap = null;
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
        if (Util.isTrue(1, 100)) {
            int[] set1 = {562, 564, 566, 561};
            itemMap = new ItemMap(this.zone, set1[Util.nextInt(0, set1.length - 1)], 1, x, y, pl.id);
            RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
        } else if (Util.isTrue(1, 20)) {
            int[] set2 = {555, 556, 563, 557, 558, 565, 559, 567, 560};
            itemMap = new ItemMap(this.zone, set2[Util.nextInt(0, set2.length - 1)], 1, x, y, pl.id);
            RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
        } else if (Util.isTrue(1, 10)) {
            itemMap = new ItemMap(this.zone, 15, 1, x, y, pl.id);
        } else if (Util.isTrue(1, 5)) {
            itemMap = new ItemMap(this.zone, 16, 1, x, y, pl.id);
        }
        if (Manager.EVENT_SEVER == 4 && itemMap == null) {
            itemMap = new ItemMap(this.zone, ConstItem.LIST_ITEM_NLSK_TET_2023[Util.nextInt(0, ConstItem.LIST_ITEM_NLSK_TET_2023.length - 1)], 1, x, y, pl.id);
            itemMap.options.add(new ItemOption(74, 0));
        }
        if (itemMap != null) {
            Service.getInstance().dropItemMap(zone, itemMap);
        }
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        generalRewards(pl);
    }

    @Override
    public void attack() {
        try {
            Player pl = getPlayerAttack();
            if (pl != null) {
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
        } catch (Exception ex) {
            Log.error(XenCon.class, ex);
        }
    }

    @Override
    public void idle() {

    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        BossManager.gI().getBossById(BossFactory.XEN_BO_HUNG).changeToAttack();
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void joinMap() {
        if (BossManager.gI().getBossById(BossFactory.SIEU_BO_HUNG) == null) {
            Boss xenBoHung = BossManager.gI().getBossById(BossFactory.XEN_BO_HUNG);
            this.zone = xenBoHung.zone;
            super.joinMap();
        }
    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{};
        this.textTalkMidle = new String[]{};
        this.textTalkAfter = new String[]{};
    }

}
