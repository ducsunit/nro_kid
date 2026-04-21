package nro.models.boss.NgucTu;

import nro.consts.ConstItem;
import nro.models.boss.*;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

/**
 * @author 💖 Nothing 💖
 */
public class SuperCumber extends FutureBoss {

    public SuperCumber() {
        super(BossFactory.CUMBER2, BossData.CUMBER2);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }


    //    @Override
//    public void rewards(Player pl) {
//        // do than 1/20
//        int[] tempIds1 = new int[]{555, 556, 563, 557, 558, 565, 559, 567, 560};
//        // Nhan, gang than 1/30
//        int[] tempIds2 = new int[]{562, 564, 566, 561};
//
//        int tempId = -1;
//        if (Util.isTrue(1, 40)) {
//            tempId = tempIds1[Util.nextInt(0, tempIds1.length - 1)];
//        } else if (Util.isTrue(1, 100)) {
//            tempId = tempIds2[Util.nextInt(0, tempIds2.length - 1)];
//        }
//        if (Manager.EVENT_SEVER == 4 && tempId == -1) {
//            tempId = ConstItem.LIST_ITEM_NLSK_TET_2023[Util.nextInt(0, ConstItem.LIST_ITEM_NLSK_TET_2023.length - 1)];
//        }
//        if (tempId != -1) {
//            ItemMap itemMap = new ItemMap(this.zone, tempId, 1,
//                    pl.location.x, this.zone.map.yPhysicInTop(pl.location.x, pl.location.y - 24), pl.id);
//            RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
//            RewardService.gI().initStarOption(itemMap, new RewardService.RatioStar[]{
//                    new RewardService.RatioStar((byte) 1, 1, 2),
//                    new RewardService.RatioStar((byte) 2, 1, 3),
//                    new RewardService.RatioStar((byte) 3, 1, 4),
//                    new RewardService.RatioStar((byte) 4, 1, 5),
//                    new RewardService.RatioStar((byte) 5, 1, 6),
//            });
//
//            Service.getInstance().dropItemMap(this.zone, itemMap);
//        }
//        generalRewards(pl);
//    }
    @Override
    public void rewards(Player pl) {
        int tempId = -1;
        // Xác suất rơi vật phẩm
        if (Util.isTrue(1, 40)) {
            tempId = new int[]{555, 556, 563, 557, 558, 565, 559, 567, 560}[Util.nextInt(0, 8)]; // Đồ than
        } else if (Util.isTrue(1, 100)) {
            tempId = new int[]{562, 564, 566, 561}[Util.nextInt(0, 3)]; // Nhẫn, gang than
        } else {
            tempId = new int[]{15, 16}[Util.nextInt(0, 1)]; // Item NRO
        }

        // Nếu có sự kiện Tết và chưa có tempId
        if (Manager.EVENT_SEVER == 4 && tempId == -1) {
            tempId = ConstItem.LIST_ITEM_NLSK_TET_2023[Util.nextInt(0, ConstItem.LIST_ITEM_NLSK_TET_2023.length - 1)];
        }

        // Nếu tempId hợp lệ, tạo vật phẩm và drop vào map
        if (tempId != -1) {
            ItemMap itemMap = new ItemMap(this.zone, tempId, 1,
                    pl.location.x, this.zone.map.yPhysicInTop(pl.location.x, pl.location.y - 24), pl.id);

            // Nếu là item NRO (15,16), chỉ có option 73
            if (tempId == 15 || tempId == 16) {
                itemMap.options.add(new ItemOption(73, 1));
            } else {
                RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
                RewardService.gI().initStarOption(itemMap, new RewardService.RatioStar[]{
                        new RewardService.RatioStar((byte) 1, 1, 2),
                        new RewardService.RatioStar((byte) 2, 1, 3),
                        new RewardService.RatioStar((byte) 3, 1, 4),
                        new RewardService.RatioStar((byte) 4, 1, 10),
                        new RewardService.RatioStar((byte) 5, 1, 15),
                        new RewardService.RatioStar((byte) 6, 1, 20),
                        new RewardService.RatioStar((byte) 7, 1, 30)
                });
            }

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
        textTalkAfter = new String[]{"Ta đã giấu hết ngọc rồng rồi, các ngươi tìm vô ích hahaha"};
    }

    @Override
    public void leaveMap() {
        BossManager.gI().getBossById(BossFactory.CUMBER).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
