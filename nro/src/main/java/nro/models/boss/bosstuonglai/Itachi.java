package nro.models.boss.bosstuonglai;

import nro.consts.ConstItem;
import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.ItemService;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

import nro.models.boss.BossManager;
import nro.services.SkillService;
import nro.utils.SkillUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author DucSunIT
 * @copyright 💖 GirlkuN 💖
 */
public class Itachi extends Boss {

    public Itachi() {
        super(BossFactory.ITACHI, BossData.ITACHI);
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
        } catch (Exception ignored) {
        }
    }

    @Override
    public void rewards(Player pl) {
//        int a = 0;
//        if (Util.isTrue(60, 100)) {
//            for (int k = 0; k < 10; k++) {
//                ItemMap itemMap2 = new ItemMap(this.zone, 457, 1,
//                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
//                Service.getInstance().dropItemMap(this.zone, itemMap2);
//                a += 10;
//            }
//        } else if (Util.isTrue(1, 100)) {
//            for (int i = 0; i < 10; i++) {
//                ItemMap itemMap = new ItemMap(this.zone, 1525, 20,
//                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
//                Service.getInstance().dropItemMap(this.zone, itemMap);
//                a += 10;
//            }
//        } else {
//            int soluong = Util.nextInt(5, 10);
//            for (int j = 0; j < soluong; j++) {
//                ItemMap itemMap1 = new ItemMap(this.zone, 1535, 1,
//                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
//                Service.getInstance().dropItemMap(this.zone, itemMap1);
//                a += 10;
//            }
//        }
        ItemMap itemMap = null;
        int y = this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24);
        if (Util.isTrue(1, 50)) {
            int[] set1 = {1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059};
            itemMap = new ItemMap(ratiItemThienSu(zone, set1[Util.nextInt(0, set1.length - 1)], 1, this.location.x - 20, this.location.y, pl.id));
        } else if (Util.isTrue(1, 100)) {
            int[] set2 = {1060, 1061, 1062};
            itemMap = new ItemMap(ratiItemThienSu(zone, set2[Util.nextInt(0, set2.length - 1)], 1, this.location.x - 20, this.location.y, pl.id));
        } else {
            int[] set3 = {16, 15};
            itemMap = new ItemMap(this.zone, set3[Util.nextInt(0, set3.length - 1)], 1, this.location.x - 20, y, pl.id);
            generalRewards(pl);
        }
        Service.getInstance().dropItemMap(zone, itemMap);
    }

    public static ItemMap ratiItemThienSu(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> ao = Arrays.asList(1048, 1049, 1050);
        List<Integer> quan = Arrays.asList(1051, 1052, 1053);
        List<Integer> gang = Arrays.asList(1054, 1055, 1056);
        List<Integer> giay = Arrays.asList(1057, 1058, 1059);
        List<Integer> nhan = Arrays.asList(1060, 1061, 1062);

        // ao
        if (ao.contains(tempId)) {
            it.options.add(new ItemOption(47, Util.highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(1201) + 2800)));// áo từ 2800-4000 giáp
        }
        // quan
        if (quan.contains(tempId)) {
            it.options.add(new ItemOption(22, Util.highlightsItem(it.itemTemplate.gender == 0, new Random().nextInt(11) + 120)));
        }
        //gang
        if (gang.contains(tempId)) {
            it.options.add(new ItemOption(0, Util.highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(7651) + 11000)));
        }
        //giay
        if (giay.contains(tempId)) {
            it.options.add(new ItemOption(23, Util.highlightsItem(it.itemTemplate.gender == 1, new Random().nextInt(21) + 130)));
        }
        // nhan
        if (nhan.contains(tempId)) {
            it.options.add(new ItemOption(14, new Random().nextInt(3) + 18));
        }
        it.options.add(new ItemOption(21, 120));
        return it;
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
            BossFactory.createBoss(BossFactory.ITACHI).setJustRest();
            super.leaveMap();
            BossManager.gI().removeBoss(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
