package nro.models.boss.bill;

import java.util.logging.Level;
import java.util.logging.Logger;
import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

/**
 *
 * @author DucSunIT
 * @copyright 💖 GirlkuN 💖
 *
 */
public class Whis extends Boss {

    public Whis() {
        super(BossFactory.WHIS, BossData.WHIS);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void joinMap() {
        super.joinMap();
        try {
            BossFactory.createBoss(BossFactory.BILL).zone = this.zone;
        } catch (Exception ex) {
            Logger.getLogger(Whis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void rewards(Player pl) {

        ItemMap itemMap = null;
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
        if (Util.isTrue(1, 100)) {
            int[] set1 = {562, 564, 566, 561}; // găng thần
            itemMap = new ItemMap(this.zone, set1[Util.nextInt(0, set1.length - 1)], 1, x, y, pl.id);
            RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
            RewardService.gI().initStarOption(itemMap, new RewardService.RatioStar[]{
                new RewardService.RatioStar((byte) 1, 1, 2),
                new RewardService.RatioStar((byte) 2, 1, 3),
                new RewardService.RatioStar((byte) 3, 1, 4),
                new RewardService.RatioStar((byte) 4, 1, 10),
                new RewardService.RatioStar((byte) 5, 1, 20),
                new RewardService.RatioStar((byte) 6, 1, 30),
                new RewardService.RatioStar((byte) 7, 1, 40)
            });
        } else if (Util.isTrue(1, 50)) {
            int[] set2 = {555, 556, 563, 557, 558, 565, 559, 567, 560};
            itemMap = new ItemMap(this.zone, set2[Util.nextInt(0, set2.length - 1)], 1, x, y, pl.id);
            RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
            RewardService.gI().initStarOption(itemMap, new RewardService.RatioStar[]{
                new RewardService.RatioStar((byte) 1, 1, 2),
                new RewardService.RatioStar((byte) 2, 1, 3),
                new RewardService.RatioStar((byte) 3, 1, 4),
                new RewardService.RatioStar((byte) 4, 1, 10),
                new RewardService.RatioStar((byte) 5, 1, 20),
                new RewardService.RatioStar((byte) 6, 1, 30),
                new RewardService.RatioStar((byte) 7, 1, 40)
            });

        } else if (Util.isTrue(1, 10)) {
            itemMap = new ItemMap(this.zone, 16, 1, x, y, pl.id);
        } else if (Util.isTrue(1, 5)) {
            itemMap = new ItemMap(this.zone, 17, 1, x, y, pl.id);
        }
        if (Manager.EVENT_SEVER == 4 && itemMap == null) {
            itemMap = new ItemMap(this.zone, ConstItem.LIST_ITEM_NLSK_TET_2023[Util.nextInt(0, ConstItem.LIST_ITEM_NLSK_TET_2023.length - 1)], 1, x, y, pl.id);
            itemMap.options.add(new ItemOption(74, 0));
        }
        if (itemMap != null) {
            Service.getInstance().dropItemMap(zone, itemMap);
        }
        generalRewards(pl);
        pl.TangDiem(pl);
    }

    @Override
    public void idle() {

    }

    @Override
    public void leaveMap() {
        BossManager.gI().getBossById(BossFactory.BILL).changeToAttack();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {

    }

}
