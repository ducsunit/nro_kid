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
import nro.services.TaskService;

/**
 * @author DucSunIT
 * @copyright 💖 GirlkuN 💖
 */
public class BLACKROSE extends Boss {

    public BLACKROSE() {
        super(BossFactory.SUPER_BLACK_ROSE, BossData.SUPER_BLACK_ROSE);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        int[] tempIds1 = new int[]{865, 883}; // kiếm Z, ctrang black rose
        int[] tempIds2 = new int[]{17, 16}; // ngọc rồng 3s,4s

        int tempId = -1;
        if (Util.isTrue(1, 10)) {
            tempId = tempIds1[Util.nextInt(0, tempIds1.length - 1)];
        } else {
            tempId = tempIds2[Util.nextInt(0, tempIds2.length - 1)];
        }
        if (tempId != -1) {
            ItemMap itemMap = new ItemMap(this.zone, tempId, 1,
                    pl.location.x, this.zone.map.yPhysicInTop(pl.location.x, pl.location.y - 24), pl.id);
            if (tempId == 883) {
                itemMap.options.add(new ItemOption(77, Util.nextInt(20, 40))); // hp
                itemMap.options.add(new ItemOption(103, Util.nextInt(20, 40))); // ki
                itemMap.options.add(new ItemOption(50, Util.nextInt(20, 40))); // sức đánh
                itemMap.options.add(new ItemOption(117, Util.nextInt(20, 30))); // đẹp + sd xung quanh
                itemMap.options.add(new ItemOption(30, 1));// k gd
                itemMap.options.add(new ItemOption(93, Util.nextInt(1, 7))); // hsd
            }
            RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
            Service.getInstance().dropItemMap(this.zone, itemMap);
        }
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
        this.textTalkMidle = new String[]{"Oải rồi hả?", "Ê cố lên nhóc",
            "Chán", "Ta có nhầm không nhỉ"};

    }

    @Override
    public void leaveMap() {
        try {
            BossFactory.createBoss(BossFactory.SUPER_BLACK_ROSE).setJustRest();
            super.leaveMap();
            BossManager.gI().removeBoss(this);
        } catch (Exception e) {
        }
    }

}
