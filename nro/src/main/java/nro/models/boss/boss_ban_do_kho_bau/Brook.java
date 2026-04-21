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
public class Brook extends BossBanDoKhoBau {

    public Brook(BanDoKhoBau banDoKhoBau) {
        super(BossFactory.BROOK, BossData.BROOK, banDoKhoBau);
    }

    @Override
    public void idle() {
    }

    @Override
    public void joinMap() {
        try {
            this.zone = this.banDoKhoBau.getMapById(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
            ChangeMapService.gI().changeMap(this, this.zone, 1340, 552);
        } catch (Exception e) {

        }
    }

}
