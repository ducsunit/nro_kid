package nro.models.player;

// đây
import nro.consts.ConstPlayer;
import nro.models.map.Map;
import nro.models.map.Zone;
import nro.server.Manager;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.Service;

/**
 * @author Hoàng Việt
 */
public class TestDame extends Player {

    public void initTestDame() {
        init();
    }

    @Override
    public short getHead() {
        return 1436;
    }

    @Override
    public short getBody() {
        return 1437;
    }

    @Override
    public short getLeg() {
        return 1438;
    }

    public void joinMap(Zone z, Player player) {
        MapService.gI().goToMap(player, z);
        z.load_Me_To_Another(player);
    }
    
    @Override
    public int version() {
        return 214;
    }
    
    public void active() {
        PlayerService.gI().changeTypePK(this, ConstPlayer.PK_ALL);
    }

    protected long lastTimeAttack;
    

    @Override
    public void update() {
        active();
        if(this.isDie()){
            Service.getInstance().sendMoney(this);
            PlayerService.gI().hoiSinh(this);
            Service.getInstance().hsChar(this, this.nPoint.hpMax, this.nPoint.mpMax);
            PlayerService.gI().sendInfoHpMp(this);
        }
    }

    private void init() {
        int id = -1000045;
        for (Map m : Manager.MAPS) {
            if (m.mapId == 165) {
                for (Zone z : m.zones) {
                    TestDame pl = new TestDame();
                    pl.name = "TEST DAME";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.nPoint.hpMax = 9000000000000000000L;
                    pl.nPoint.hpg = 100000000000L;
                    pl.nPoint.hp = 9000000000000000000L;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 360;
                    pl.location.y = 336;
                    joinMap(z, pl);
                    z.setReferee(pl);
                }
            }
        }
    }
}
