/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nro.models.boss.boss_ban_do_kho_bau;

import nro.consts.ConstRatio;
import nro.consts.MapName;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.map.phoban.BanDoKhoBau;
import nro.models.player.Player;
import nro.services.SkillService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

import java.util.List;
import nro.models.map.ItemMap;
import nro.services.Service;

/**
 *
 * @author DucSunIT
 */
public class TrungUyXanhLo extends BossBanDoKhoBau {

    private boolean activeAttack;

    public TrungUyXanhLo(BanDoKhoBau banDoKhoBau) {
        super(BossFactory.TRUNG_UY_XANH_LO, BossData.TRUNG_UY_XANH_LO_2, banDoKhoBau);
    }

    @Override
    public void attack() {
        try {
            if (activeAttack) {
                if (!useSpecialSkill()) {
                    Player pl = getPlayerAttack();
                    this.playerSkill.skillSelect = this.getSkillAttack();
                    if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                        if (Util.isTrue(10, ConstRatio.PER100)) {
                            goToXY(pl.location.x + Util.nextInt(-20, 20),
                                    Util.nextInt(pl.location.y - 80, this.zone.map.yPhysicInTop(pl.location.x, 0)), false);
                        }
                        SkillService.gI().useSkill(this, pl, null, null);
                        checkPlayerDie(pl);
                    } else {
                        goToPlayer(pl, false);
                    }
                }
            } else {
                List<Player> notBosses = this.zone.getNotBosses();
                for (Player pl : notBosses) {

                    if (pl.location.x >= 820 && !pl.effectSkin.isVoHinh) {
                        this.activeAttack = true;
                        break;
                    }
                }
            }
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }

    @Override
    protected boolean useSpecialSkill() {
        //boss này chỉ có chiêu thái dương hạ san
        this.playerSkill.skillSelect = this.getSkillSpecial();
        if (SkillService.gI().canUseSkillWithCooldown(this)) {
            SkillService.gI().useSkill(this, null, null, null);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void rewards(Player pl) {
        int a = 0;
//        int b = 5;
        for (int i = 0; i <= 20; i++) {
            ItemMap itemMap = new ItemMap(this.zone, 457, 1,
                    this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), -1);
            Service.getInstance().dropItemMap(this.zone, itemMap);
            a += 15;
        }
//        for (int i = 0; i < 2; i++) {
//            ItemMap itemMap1 = new ItemMap(this.zone, 15, 1,
//                    this.location.x + b, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), -1);
//            Service.getInstance().dropItemMap(this.zone, itemMap1);
//            b += 15;
//        }
    }

    @Override
    public void joinMap() {
        try {
            this.zone = banDoKhoBau.getMapById(MapName.DONG_KHO_BAU);
            ChangeMapService.gI().changeMap(this, this.zone, 1065, this.zone.map.yPhysicInTop(1065, 0));
        } catch (Exception e) {

        }
    }

}
