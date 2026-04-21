package nro.models.boss;

import nro.models.boss.event.*;
import nro.consts.ConstEvent;
import nro.consts.ConstMap;
import nro.models.boss.bill.*;
import nro.models.boss.tramtau.*;
import nro.models.boss.bosstuonglai.*;
import nro.models.boss.broly.*;
import nro.models.boss.cell.*;
import nro.models.boss.chill.*;
import nro.models.boss.cold.*;
import nro.models.boss.fide.*;
import nro.models.boss.mabu_war.*;
import nro.models.boss.nappa.*;
import nro.models.boss.robotsatthu.*;
import nro.models.boss.tieudoisatthu.*;
import nro.models.boss.NguHanhSon.*;
import nro.models.boss.NgucTu.*;
import nro.models.boss.traidat.*;
import nro.models.map.Map;
import nro.models.map.Zone;
import nro.models.map.mabu.MabuWar;
import nro.models.map.mabu.MabuWar14h;
import nro.server.Manager;
import nro.services.MapService;
import org.apache.log4j.Logger;

/**
 * @author DucSunIT
 * @copyright 💖 NROLOVE 💖
 */
public class BossFactory {

    //id boss
    public static final byte BROLY = -1;
    public static final byte SUPER_BROLY = -2;
    public static final byte TRUNG_UY_TRANG = -3;
    public static final byte TRUNG_UY_XANH_LO = -4;
    public static final byte TRUNG_UD_THEP = -5;
    public static final byte NINJA_AO_TIM = -6;
    public static final byte NINJA_AO_TIM_FAKE_1 = -7;
    public static final byte NINJA_AO_TIM_FAKE_2 = -8;
    public static final byte NINJA_AO_TIM_FAKE_3 = -9;
    public static final byte NINJA_AO_TIM_FAKE_4 = -10;
    public static final byte NINJA_AO_TIM_FAKE_5 = -11;
    public static final byte NINJA_AO_TIM_FAKE_6 = -12;
    public static final byte ROBOT_VE_SI_1 = -13;
    public static final byte ROBOT_VE_SI_2 = -14;
    public static final byte ROBOT_VE_SI_3 = -15;
    public static final byte ROBOT_VE_SI_4 = -16;
    public static final byte XEN_BO_HUNG_1 = -17;
    public static final byte XEN_BO_HUNG_2 = -18;
    public static final byte XEN_BO_HUNG_HOAN_THIEN = -19;
    public static final byte XEN_BO_HUNG = -20;
    public static final byte XEN_CON = -21;
    public static final byte SIEU_BO_HUNG = -22;
    public static final byte KUKU = -23;
    public static final byte MAP_DAU_DINH = -24;
    public static final byte RAMBO = -25;
    public static final byte COOLER = -26;
    public static final byte COOLER2 = -27;
    public static final byte SO4 = -28;
    public static final byte SO3 = -29;
    public static final byte SO2 = -30;
    public static final byte SO1 = -31;
    public static final byte TIEU_DOI_TRUONG = -32;
    public static final byte FIDE_DAI_CA_1 = -33;
    public static final byte FIDE_DAI_CA_2 = -34;
    public static final byte FIDE_DAI_CA_3 = -35;
    public static final byte ANDROID_19 = -36;
    public static final byte ANDROID_20 = -37;
    public static final byte ANDROID_13 = -38;
    public static final byte ANDROID_14 = -39;
    public static final byte ANDROID_15 = -40;
    public static final byte PIC = -41;
    public static final byte POC = -42;
    public static final byte KINGKONG = -43;
    public static final byte SUPER_BROLY_RED = -44;
    public static final byte LUFFY = -45;
    public static final byte ZORO = -46;
    public static final byte SANJI = -47;
    public static final byte USOPP = -48;
    public static final byte FRANKY = -49;
    public static final byte BROOK = -50;
    public static final byte NAMI = -51;
    public static final byte CHOPPER = -52;
    public static final byte ROBIN = -53;
    public static final byte WHIS = -54;
    public static final byte BILL = -55;
    public static final byte CHILL = -56;
    public static final byte CHILL2 = -57;
    public static final byte BULMA = -58;
    public static final byte POCTHO = -59;
    public static final byte CHICHITHO = -60;
    public static final byte BLACKGOKU = -61;
    public static final byte SUPERBLACKGOKU = -62;
    public static final byte SANTA_CLAUS = -63;
    public static final byte MABU_MAP = -64;
    public static final byte SUPER_BU = -65;
    public static final byte BU_TENK = -66;
    public static final byte DRABULA_TANG1 = -67;
    public static final byte BUIBUI_TANG2 = -68;
    public static final byte BUIBUI_TANG3 = -69;
    public static final byte YACON_TANG4 = -70;
    public static final byte DRABULA_TANG5 = -71;
    public static final byte GOKU_TANG5 = -72;
    public static final byte CADIC_TANG5 = -73;
    public static final byte DRABULA_TANG6 = -74;
    public static final byte XEN_MAX = -75;
    public static final byte HOA_HONG = -76;
    public static final byte SOI_HEC_QUYN = -77;
    public static final byte O_DO = -78;
    public static final byte XINBATO = -79;
    public static final byte CHA_PA = -80;
    public static final byte PON_PUT = -81;
    public static final byte CHAN_XU = -82;
    public static final byte TAU_PAY_PAY = -83;
    public static final byte YAMCHA = -84;
    public static final byte JACKY_CHUN = -85;
    public static final byte THIEN_XIN_HANG = -86;
    public static final byte LIU_LIU = -87;
    public static final byte THIEN_XIN_HANG_CLONE = -88;
    public static final byte THIEN_XIN_HANG_CLONE1 = -89;
    public static final byte THIEN_XIN_HANG_CLONE2 = -90;
    public static final byte THIEN_XIN_HANG_CLONE3 = -91;
    public static final byte QILIN = -92;
    public static final byte NGO_KHONG = -93;
    public static final byte BAT_GIOI = -94;
    public static final byte FIDEGOLD = -95;
    public static final byte CUMBER = -96;
    public static final byte CUMBER2 = -97;
    public static final byte SUPER_BLACK_ROSE = -98;
    public static final byte ZAMAS_TOI_THUONG = -99;
    public static final byte WHIS_DETU = -100;
    public static final byte ZENO = -101;
    public static final byte RONG_DEN = -102;
    public static final byte GOKU_SUPER = -103;
    public static final byte BONG_BANG = -104;
    public static final byte SOI_BASIL = -105;
    public static final byte VADOS = -106;
    public static final byte CHAMPA = -107;
    public static final byte ITACHI = -108;
    public static final byte ZAMAS_ZOMBIE = -109;
    public static final byte LUFFY_THAN_NIKA = -110;
    public static final byte LUFFY_GEAR5 = -113;
    public static final byte KAIDO = -114;
    public static final byte ALONG = -115;
    public static final byte MIHAWK = -116;

    public static final byte KID_BU = -111;
    public static final byte BU_HAN = -112;

    // sự kiện tết
    public static final byte THAN_TAI_TD = -113;
    public static final byte THAN_TAI_XD = -114;
    public static final byte THAN_TAI_NM = -115;

    public static final byte BROLY_TEST = -116;
    public static final byte BABY_VEGETA = -117;
    public static final byte BABY_KHI = -118;
    public static final byte QUY_CO_DOC_LAP = -119;

    private static final Logger logger = Logger.getLogger(BossFactory.class);

    public static final int[] MAP_APPEARED_QILIN = {
            ConstMap.LANG_ARU, ConstMap.LANG_MORI, ConstMap.LANG_KAKAROT, ConstMap.DOI_HOA_CUC, ConstMap.DOI_NAM_TIM, ConstMap.DOI_HOANG,
            ConstMap.LANG_PLANT, ConstMap.RUNG_NGUYEN_SINH,
            ConstMap.RUNG_CO, ConstMap.RUNG_THONG_XAYDA, ConstMap.RUNG_DA, ConstMap.THUNG_LUNG_DEN, ConstMap.BO_VUC_DEN, ConstMap.THANH_PHO_VEGETA,
            ConstMap.THUNG_LUNG_TRE, ConstMap.RUNG_NAM, ConstMap.RUNG_BAMBOO, ConstMap.RUNG_XUONG, ConstMap.RUNG_DUONG_XI, ConstMap.NAM_KAME,
            ConstMap.DAO_BULONG, ConstMap.DONG_KARIN, ConstMap.THI_TRAN_MOORI, ConstMap.THUNG_LUNG_MAIMA, ConstMap.NUI_HOA_TIM, ConstMap.NUI_HOA_VANG
    };

    private BossFactory() {

    }

    // khởi tạo boss 
    public static void initBoss() {
        new Thread(() -> {
            try {
//                createBoss(THAN_TAI);
////                createBoss(NGO_KHONG);
////                createBoss(CUMBER);
////                createBoss(BULMA);
////                createBoss(CHICHITHO);
////                createBoss(POCTHO);
                createBoss(BLACKGOKU);
                createBoss(CHILL);
                createBoss(COOLER);
                createBoss(CUMBER);
                createBoss(CUMBER2);
                createBoss(BABY_VEGETA);
                createBoss(BABY_KHI);
                createBoss(XEN_BO_HUNG);
                createBoss(ANDROID_20);
                createBoss(KINGKONG);
                createBoss(XEN_BO_HUNG_1);
                createBoss(XEN_MAX);
                createBoss(KUKU);
                createBoss(MAP_DAU_DINH);
                createBoss(RAMBO);
                createBoss(TIEU_DOI_TRUONG);
                createBoss(FIDE_DAI_CA_1);
                createBoss(SUPER_BLACK_ROSE);
                createBoss(ZAMAS_TOI_THUONG);
                createBoss(BONG_BANG);
                createBoss(SOI_BASIL);
                createBoss(SANTA_CLAUS);
                createBoss(WHIS_DETU);
                createBoss(WHIS);
                createBoss(VADOS);
                createBoss(RONG_DEN);
                createBoss(ZENO);
                createBoss(GOKU_SUPER);
////                createBoss(ITACHI);
                createBoss(ZAMAS_ZOMBIE);
////                createBoss(LUFFY_GEAR5);
////                createBoss(KAIDO);
////                createBoss(LUFFY_THAN_NIKA);
////                createBoss(QILIN);
////                createBoss(ALONG);
////                createBoss(MIHAWK);
                createBoss(FIDEGOLD);
                createBoss(SUPER_BROLY);
                createBoss(ITACHI);
////                for (int i = 0; i < 5; i++) {
////                    createBoss(SUPER_BROLY);
////                }
                if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_8_3) {
                    createBoss(QUY_CO_DOC_LAP);
                }
                for (Map map : Manager.MAPS) {
                    if (map != null && !map.zones.isEmpty()) {
                        if (!map.isMapOffline && map.type == ConstMap.MAP_NORMAL
                                && map.tileId > 0 && !MapService.gI().isMapVS(map.mapId)) {
                            if (map.mapWidth > 50 && map.mapHeight > 50) {
                                if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_20_11) {
                                    new HoaHong(map.mapId);
                                }
                                if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_NOEL) {
                                    new SantaClaus(map.mapId);
                                }
                            }
                        }
                    }
                }
                if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_TET) {
                    for (int mapID : MAP_APPEARED_QILIN) {
                        new Qilin(mapID);
                        new ThanTai();
                    }
                }
            } catch (Exception e) {
                logger.error("Err initboss", e);
            }
        }).start();
    }

    public static void initBossMabuWar14H() {
        new Thread(() -> {
            Map map = MapService.gI().getMapById(127);
            for (Zone zone : map.zones) {
                Boss boss = new Mabu_14H(127, zone.zoneId);
                MabuWar14h.gI().bosses.add(boss);
            }
            map = MapService.gI().getMapById(128);
            for (Zone zone : map.zones) {
                Boss boss = new SuperBu_14H(128, zone.zoneId);
                MabuWar14h.gI().bosses.add(boss);
            }
        }).start();
    }

    public static void initBossMabuWar() {
        new Thread(() -> {
            for (short mapid : BossData.DRABULA_TANG1.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new Drabula_Tang1(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.DRABULA_TANG6.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new Drabula_Tang6(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.GOKU_TANG5.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new Goku_Tang5(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.CALICH_TANG5.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new Calich_Tang5(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.BUIBUI_TANG2.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new BuiBui_Tang2(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.BUIBUI_TANG3.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new BuiBui_Tang3(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.YACON_TANG4.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new Yacon_Tang4(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
        }).start();
    }

    public static Boss createBoss(byte bossId) throws Exception {
        Boss boss = switch (bossId) {
            case BROLY -> new Broly();
            case SUPER_BROLY -> new SuperBroly();
            case SUPER_BROLY_RED -> new SuperBrolyRed();
            case XEN_BO_HUNG_1 -> new XenBoHung1();
            case XEN_BO_HUNG_2 -> new XenBoHung2();
            case XEN_BO_HUNG_HOAN_THIEN -> new XenBoHungHoanThien();
            case XEN_BO_HUNG -> new XenBoHung();
            case XEN_CON -> new XenCon();
            case SIEU_BO_HUNG -> new SieuBoHung();
            case KUKU -> new Kuku();
            case MAP_DAU_DINH -> new MapDauDinh();
            case RAMBO -> new Rambo();
            case COOLER -> new Cooler();
            case COOLER2 -> new Cooler2();
            case SO4 -> new So4();
            case SO3 -> new So3();
            case SO2 -> new So2();
            case SO1 -> new So1();
            case TIEU_DOI_TRUONG -> new TieuDoiTruong();
            case FIDE_DAI_CA_1 -> new FideDaiCa1();
            case FIDE_DAI_CA_2 -> new FideDaiCa2();
            case FIDE_DAI_CA_3 -> new FideDaiCa3();
            case ANDROID_19 -> new Android19();
            case ANDROID_20 -> new Android20();
            case POC -> new Poc();
            case PIC -> new Pic();
            case KINGKONG -> new KingKong();
            case WHIS -> new Whis();
            case BILL -> new Bill();
            case VADOS -> new Vados();
            case CHAMPA -> new Champa();
            case CHILL -> new Chill();
            case CHILL2 -> new Chill2();
            case BULMA -> new BULMA();
            case POCTHO -> new POCTHO();
            case CHICHITHO -> new CHICHITHO();
            case SUPER_BLACK_ROSE -> new BLACKROSE();
            case ZAMAS_TOI_THUONG -> new ZamasToiThuong();
            case BONG_BANG -> new BongBang();
            case SOI_BASIL -> new SoiBasil();
            case ITACHI -> new Itachi();
//            case ALONG:
//                boss = new Along();
//                break;
//            case MIHAWK:
//                boss = new Mihawk();
//                break;
            case ZAMAS_ZOMBIE -> new ZamasZombie();
            case LUFFY_GEAR5 -> new LuffyGear5();
            case KAIDO -> new Kaido();
            case LUFFY_THAN_NIKA -> new LuffyThanNika();
            case GOKU_SUPER -> new GokuSuper();
            case WHIS_DETU -> new WhisDetu();
            case ZENO -> new ZenoDetu();
            case RONG_DEN -> new RongDen();
//             case BROLYDEN:
//                boss = new Brolyden();
//                break;
//             case BROLYXANH:
//                boss = new Brolyxanh();
//                break;
//             case BROLYVANG:
//                boss = new Brolyvang();
//                break;
            case BLACKGOKU -> new Blackgoku();
            case SUPERBLACKGOKU -> new Superblackgoku();
            case MABU_MAP -> new Mabu_Tang6();
            case XEN_MAX -> new XenMax();
            case NGO_KHONG -> new NgoKhong();
            case BAT_GIOI -> new BatGioi();
            case FIDEGOLD -> new FideGold();
            case CUMBER -> new Cumber();
            case CUMBER2 -> new SuperCumber();
            case BABY_VEGETA -> new BabyVegeta();
            case BABY_KHI -> new BabyKhi();
            case QUY_CO_DOC_LAP -> new QuyCoDocLap();
            default -> null;
        };
        return boss;
    }

}
