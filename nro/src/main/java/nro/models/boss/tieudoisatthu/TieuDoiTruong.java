package nro.models.boss.tieudoisatthu;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 *
 * @author DucSunIT
 * @copyright 💖 GirlkuN 💖
 *
 */
public class TieuDoiTruong extends FutureBoss {

    public TieuDoiTruong() {
        super(BossFactory.TIEU_DOI_TRUONG, BossData.TIEU_DOI_TRUONG);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
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
            "Chán", "Đại ca Fide có nhầm không nhỉ"};

    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        this.changeToIdle();
    }

    @Override
    public void joinMap() {
        try {
            if (this.zone == null) {
                this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
            }
            if (this.zone != null) {
                BossFactory.createBoss(BossFactory.SO4).zone = this.zone;
                BossFactory.createBoss(BossFactory.SO3).zone = this.zone;
                BossFactory.createBoss(BossFactory.SO2).zone = this.zone;
                BossFactory.createBoss(BossFactory.SO1).zone = this.zone;
                ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP);
                ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
            }
        } catch (Exception e) {
        }
    }

}
