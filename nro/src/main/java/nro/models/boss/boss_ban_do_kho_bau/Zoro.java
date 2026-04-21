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
public class Zoro extends BossBanDoKhoBau {

    public Zoro(BanDoKhoBau banDoKhoBau) {
        super(BossFactory.ZORO, BossData.ZORO, banDoKhoBau);
    }

    @Override
    public void initTalk() {
        this.textTalkMidle = new String[]{
            "Tôi là một thợ săn hải tặc",
            "Nếu ngươi chết, ta sẽ giết ngươi!",
            "Tốt thôi! Tôi thà làm hải tặc còn hơn chết ở đây!",
            "Chỉ những người đã chịu đựng lâu, mới có thể nhìn thấy ánh sáng trong bóng tối",
            "Ngươi muốn giết ta? Ngươi còn không có thể giết ta chán nản!",
            "Nếu tôi chết ở đây, thì tôi là một người đàn ông chỉ có thể đi xa đến mức này",
            "Tôi làm mọi thứ theo cách riêng của tôi! Vì vậy, đừng có nói với tôi về nó!"
        };
    }

    @Override
    public void idle() {
    }

    @Override
    public void joinMap() {
        try {
            this.zone = this.banDoKhoBau.getMapById(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
            ChangeMapService.gI().changeMap(this, this.zone, 240, 456);
        } catch (Exception e) {

        }
    }

    @Override
    public void leaveMap() {
        for (BossBanDoKhoBau boss : this.banDoKhoBau.bosses) {
            if (boss.id == BossFactory.LUFFY) {
                boss.changeToAttack();
                break;
            }
        }
        super.leaveMap();
    }

}
