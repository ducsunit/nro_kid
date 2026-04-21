package nro.models.boss.tramtau;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

/**
 * @author DucSunIT
 * @copyright 💖 GirlkuN 💖
 */
public class Champa extends Boss {

    public Champa() {
        super(BossFactory.CHAMPA, BossData.CHAMPA);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        ItemMap itemMap = null;
        int y = this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24);
        if (Util.isTrue(1, 20)) {
            int[] set1 = {650, 651, 652, 653, 654, 655, 657, 658, 659, 660, 661, 662};
            itemMap = new ItemMap(ratiItemHuyDiet(zone, set1[Util.nextInt(0, set1.length - 1)], 1, this.location.x - 20, this.location.y, pl.id));
        } else if (Util.isTrue(1, 50)) {
            itemMap = new ItemMap(ratiItemHuyDiet(zone, 656, 1, this.location.x - 20, this.location.y, pl.id));
        } else {
            int[] set2 = {16, 15};
            itemMap = new ItemMap(this.zone, set2[Util.nextInt(0, set2.length - 1)], 1, this.location.x - 20, y, pl.id);
            generalRewards(pl);
        }
        Service.getInstance().dropItemMap(zone, itemMap);
    }

    public static ItemMap ratiItemHuyDiet(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> ao = Arrays.asList(650, 652, 654);
        List<Integer> quan = Arrays.asList(651, 653, 655);
        List<Integer> gang = Arrays.asList(657, 659, 661);
        List<Integer> giay = Arrays.asList(658, 660, 662);
        int nhd = 656;
        if (ao.contains(tempId)) {
            it.options.add(new ItemOption(47, Util.highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(1001) + 1800))); // áo từ 1800-2800 giáp
        }
        if (quan.contains(tempId)) {
            it.options.add(new ItemOption(22, Util.highlightsItem(it.itemTemplate.gender == 0, new Random().nextInt(16) + 85))); // hp 85-100k
        }
        if (gang.contains(tempId)) {
            it.options.add(new ItemOption(0, Util.highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(1500) + 8500))); // 8500-10000
        }
        if (giay.contains(tempId)) {
            it.options.add(new ItemOption(23, Util.highlightsItem(it.itemTemplate.gender == 1, new Random().nextInt(11) + 80))); // ki 80-90k
        }
        if (nhd == tempId) {
            it.options.add(new ItemOption(14, new Random().nextInt(3) + 17));
        }
        it.options.add(new ItemOption(21, 80));
        return it;
    }

    @Override
    public Player getPlayerAttack() throws Exception {
        if (countChangePlayerAttack < targetCountChangePlayerAttack
                && plAttack != null && plAttack.zone != null && plAttack.zone.equals(this.zone)
                && !plAttack.effectSkin.isVoHinh) {
            if (!plAttack.isDie()) {
                this.countChangePlayerAttack++;
                return plAttack;
            } else {
                plAttack = null;
            }
        } else {
            this.targetCountChangePlayerAttack = Util.nextInt(10, 20);
            this.countChangePlayerAttack = 0;
            plAttack = this.zone.getRandomPlayerInMap();
        }
        return plAttack;
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        textTalkMidle = new String[]{"Ta chính là đệ nhất vũ trụ cao thủ"};
        textTalkAfter = new String[]{"Ta sẽ huỷ diệt bọn ngươi..."};
    }

    @Override
    public void leaveMap() {
        try {
            BossFactory.createBoss(BossFactory.VADOS);
            this.setJustRestToFuture();
            super.leaveMap();
            BossManager.gI().removeBoss(this);
        } catch (Exception ignored) {
        }

    }
}
