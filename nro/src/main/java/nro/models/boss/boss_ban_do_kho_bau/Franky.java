package nro.models.boss.boss_ban_do_kho_bau;

import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.map.phoban.BanDoKhoBau;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 *
 * @author DucSunIT
 * @copyright 💖 GirlkuN 💖
 *
 */
public class Franky extends BossBanDoKhoBau {

    public Franky(BanDoKhoBau banDoKhoBau) {
        super(BossFactory.FRANKY, BossData.FRANKY, banDoKhoBau);
    }

    @Override
    public void idle() {
    }

    @Override
    public void joinMap() {
        try {
            this.zone = this.banDoKhoBau.getMapById(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
            ChangeMapService.gI().changeMap(this, this.zone, 1270, 552);
        } catch (Exception e) {

        }
    }

    @Override
    public void initTalk() {
        this.textTalkMidle = new String[]{
            "Superrrr....."
        };
    }

    @Override
    public void leaveMap() {
        for (BossBanDoKhoBau boss : this.banDoKhoBau.bosses) {
            if (boss.id == BossFactory.BROOK) {
                boss.changeToAttack();
                break;
            }
        }
        super.leaveMap();
    }

}
