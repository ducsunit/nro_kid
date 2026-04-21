package nro.models.boss.mabu_war;

import java.util.logging.Level;
import java.util.logging.Logger;
import nro.consts.ConstRatio;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.MapService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

/**
 *
 * @author DUY
 */
public class SuperBu_14H extends BossMabuWar {

    public SuperBu_14H(int mapID, int zoneId) {
        super(BossFactory.SUPER_BU, BossData.SUPER_BU);
        this.mapID = mapID;
        this.zoneId = zoneId;
        this.zoneHold = zoneId;
        this.isMabuBoss = true;
    }

    @Override
    public void attack() {
        if (this.isDie()) {
            die();
            return;
        }
        try {
            if (Util.isTrue(50, 100)) {
                this.talk();
            }
            Player pl = getPlayerAttack();
            if (pl != null && !pl.effectSkill.isHoldMabu) {
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
            Log.error(SuperBu_14H.class, ex);
        }
    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if (this.isDie()) {
            return 0;
        } else {
            long dame = super.injuredNotCheckDie(plAtt, damage, piercing);
            if (this.isDie()) {
                rewards(plAtt);
            }
            return dame;
        }
    }

    @Override
    public void joinMap() {
        this.zone = getMapCanJoin(mapID);
        int x = Util.nextInt(50, this.zone.map.mapWidth - 50);
        ChangeMapService.gI().changeMap(this, this.zone, x, this.zone.map.yPhysicInTop(x, 0));
    }

    @Override
    public Zone getMapCanJoin(int mapId) {
        Zone map = MapService.gI().getZoneJoinByMapIdAndZoneId(this, mapId, zoneId);
        if (map.isBossCanJoin(this)) {
            return map;
        } else {
            return getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        }
    }

    @Override
    public void idle() {

    }

    @Override
    public void rewards(Player pl) {
        for (int i = 0; i < zone.getPlayers().size(); i++) {
            Player plAll = zone.getPlayers().get(i);
            if (plAll != null) {
                if (plAll.effectSkill.isHoldMabu) {
                    Service.getInstance().removeMabuEat(plAll);
                }
                plAll.effectSkill.lastTimeHoldMabu = System.currentTimeMillis();
                ChangeMapService.gI().changeMap(plAll, 127, this.zoneHold, (short) -1, (short) 5);
            }
        }
        try {
            if (Util.isTrue(1, 15)) {
                ItemMap itemMap = new ItemMap(this.zone, 1068, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x, 100), -1);
                Service.getInstance().dropItemMap(this.zone, itemMap);
            }
            int[] listitem = {15, 16, 861};
            ItemMap itemMap = new ItemMap(this.zone, listitem[Util.nextInt(0, listitem.length - 1)], 1, pl.location.x, this.zone.map.yPhysicInTop(pl.location.x, pl.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, itemMap);
        } catch (Exception ex) {
            Logger.getLogger(Mabu_Tang6.class.getName()).log(Level.SEVERE, null, ex);
        }
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{"Bư! Bư! Bư!", "Bư! Bư! Bư!"};
        this.textTalkMidle = new String[]{"Oe Oe Oe"};
        this.textTalkAfter = new String[]{"Huhu"};
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        this.changeToIdle();
    }
}
