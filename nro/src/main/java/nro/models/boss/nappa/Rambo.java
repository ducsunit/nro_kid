package nro.models.boss.nappa;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

/**
 *
 * @author DucSunIT
 * @copyright 💖 GirlkuN 💖
 *
 */
public class Rambo extends FutureBoss {

    public Rambo() {
        super(BossFactory.RAMBO, BossData.RAMBO);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }



    @Override
    public void rewards(Player pl) {
        if (Util.isTrue(1, 10)) {
            int[] tempId = new int[]{138, 142, 146, 150, 154, 158, 162, 166, 170, 174, 178, 182, 186};
            ItemMap itemMap = new ItemMap(this.zone, tempId[Util.nextInt(0, tempId.length - 1)],
                    1, pl.location.x, this.zone.map.yPhysicInTop(pl.location.x, pl.location.y - 24), pl.id);
            RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
            RewardService.gI().initStarOption(itemMap, new RewardService.RatioStar[]{
                new RewardService.RatioStar((byte) 1, 1, 2),
                new RewardService.RatioStar((byte) 2, 1, 3),
                new RewardService.RatioStar((byte) 3, 1, 4),
                new RewardService.RatioStar((byte) 4, 1, 5),
                new RewardService.RatioStar((byte) 5, 1, 50),
                new RewardService.RatioStar((byte) 6, 1, 100)
            });
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

    }

}
