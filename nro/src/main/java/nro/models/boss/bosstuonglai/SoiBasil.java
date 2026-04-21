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
public class SoiBasil extends Boss {

    public SoiBasil() {
        super(BossFactory.SOI_BASIL, BossData.SOI_BASIL);
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
        int[] tempIds1 = new int[]{ConstItem.CAI_TRANG_SOI_BASIL, ConstItem.CAI_TRANG_SOI_LAVENDER, ConstItem.CAI_TRANG_SOI_BERGAMO};

        int tempId = -1;
        tempId = tempIds1[Util.nextInt(0, tempIds1.length - 1)];
        if (tempId != -1) {
            ItemMap itemMap = new ItemMap(this.zone, tempId, 1,
                    pl.location.x, this.zone.map.yPhysicInTop(pl.location.x, pl.location.y - 24), pl.id);
            switch (tempId) {
                case ConstItem.CAI_TRANG_SOI_BASIL:
                    itemMap.options.add(new ItemOption(50, Util.nextInt(30, 40)));
                    itemMap.options.add(new ItemOption(77, Util.nextInt(80, 100)));
                    itemMap.options.add(new ItemOption(103, Util.nextInt(30, 40)));
                    itemMap.options.add(new ItemOption(93, 3));
                    itemMap.options.add(new ItemOption(30, 1));
                    break;
                case ConstItem.CAI_TRANG_SOI_LAVENDER:
                    itemMap.options.add(new ItemOption(50, Util.nextInt(70, 100)));
                    itemMap.options.add(new ItemOption(77, Util.nextInt(30, 40)));
                    itemMap.options.add(new ItemOption(103, Util.nextInt(30, 40)));
                    itemMap.options.add(new ItemOption(93, 3));
                    itemMap.options.add(new ItemOption(30, 1));
                    break;
                case ConstItem.CAI_TRANG_SOI_BERGAMO:
                    itemMap.options.add(new ItemOption(50, Util.nextInt(30, 40)));
                    itemMap.options.add(new ItemOption(77, Util.nextInt(30, 40)));
                    itemMap.options.add(new ItemOption(103, Util.nextInt(80, 100)));
                    itemMap.options.add(new ItemOption(93, 3));
                    itemMap.options.add(new ItemOption(30, 1));
                    break;
                default:
                    break;
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
        this.textTalkMidle = new String[]{"Oải rồi hả?", "Ê cố lên nhóc",
            "Chán", "Ta có nhầm không nhỉ"};

    }

    @Override
    public void leaveMap() {
        try {
            BossFactory.createBoss(BossFactory.SOI_BASIL).setJustRest();
            super.leaveMap();
            BossManager.gI().removeBoss(this);
        } catch (Exception e) {
        }
    }

}
