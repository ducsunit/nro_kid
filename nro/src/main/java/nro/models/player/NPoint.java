package nro.models.player;

import nro.attr.Attribute;
import nro.card.Card;
import nro.card.CollectionBook;
import nro.consts.ConstAttribute;
import nro.consts.ConstPlayer;
import nro.consts.ConstRatio;
import nro.models.clan.Buff;
import nro.models.intrinsic.Intrinsic;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.skill.Skill;
import nro.power.PowerLimit;
import nro.power.PowerLimitManager;
import nro.server.Manager;
import nro.server.ServerManager;
import nro.services.*;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author DucSunIT
 * @copyright 💖 GirlkuN 💖
 */
public class NPoint {

    private static final long INT_MAX_VALUE = Integer.MAX_VALUE;
    private static final long INT_MIN_VALUE = Integer.MIN_VALUE;

    public static final byte MAX_LIMIT = 11;

    private Player player;
    public boolean isCrit;
    public boolean isCrit100;

    private Intrinsic intrinsic;
    private int percentDameIntrinsic;
    public int dameAfter;

    /*-----------------------Chỉ số cơ bản------------------------------------*/
    public byte numAttack;
    public short stamina, maxStamina;

    public byte limitPower;
    public long power;
    public long tiemNang;
    public long mpg, hpg;
    public long dameg;

    public long hp, hpMax;
    public long mp, mpMax;
    public long dame;
    public long def;
    public long defg;
    public int crit, critg;
    public byte speed = 5;

    public boolean teleport;

    /**
     * Chỉ số cộng thêm
     */
    public int hpAdd, mpAdd, dameAdd, defAdd, critAdd, hpHoiAdd, mpHoiAdd;

    /**
     * //+#% sức đánh chí mạng
     */
    public List<Integer> tlDameCrit;

    public boolean buffExpSatellite, buffDefenseSatellite;

    /**
     * Tỉ lệ hp, mp cộng thêm
     */
    public List<Integer> tlHp, tlMp;

    /**
     * Điểm tích lũy
     */
    public List<Integer> TichLuy;

    /**
     * Tỉ lệ giáp cộng thêm
     */
    public List<Integer> tlDef;

    /**
     * Tỉ lệ sức đánh/ sức đánh khi đánh quái
     */
    public List<Integer> tlDame, tlDameAttMob;

    /**
     * Lượng hp, mp hồi mỗi 30s, mp hồi cho người khác
     */
    public long hpHoi, mpHoi, mpHoiCute;

    /**
     * Tỉ lệ hp, mp hồi cộng thêm
     */
    public short tlHpHoi, tlMpHoi;

    /**
     * Tỉ lệ hp, mp hồi bản thân và đồng đội cộng thêm
     */
    public short tlHpHoiBanThanVaDongDoi, tlMpHoiBanThanVaDongDoi;

    /**
     * Tỉ lệ hút hp, mp khi đánh, hp khi đánh quái
     */
    public short tlHutHp, tlHutMp, tlHutHpMob;

    /**
     * Tỉ lệ hút hp, mp xung quanh mỗi 5s
     */
    public short tlHutHpMpXQ;

    /**
     * Tỉ lệ phản sát thương
     */
    public short tlPST;

    /**
     * Tỉ lệ tiềm năng sức mạnh
     */
    public List<Integer> tlTNSM;
    public int tlTNSMPet;

    /**
     * Tỉ lệ vàng cộng thêm
     */
    public short tlGold;

    /**
     * Tỉ lệ né đòn
     */
    public short tlNeDon;

    /**
     * Tỉ lệ sức đánh đẹp cộng thêm cho bản thân và người xung quanh
     */
    public List<Integer> tlSDDep;

    /**
     * Tỉ lệ giảm sức đánh
     */
    public short tlSubSD;
    public List<Integer> tlSpeed;
    public int mstChuong;
    public int tlGiamst;

    /*------------------------Effect skin-------------------------------------*/
    public Item trainArmor;
    public boolean wornTrainArmor;
    public boolean wearingTrainArmor;

    public boolean wearingVoHinh;
    public boolean isKhongLanh;

    public short tlHpGiamODo;

    private PowerLimit powerLimit;
    public boolean wearingDrabula;
    public boolean wearingMabu;
    public boolean wearingBuiBui;

    public boolean wearingNezuko;
    public boolean wearingTanjiro;
    public boolean wearingInosuke;
    public boolean wearingInoHashi;
    public boolean wearingZenitsu;
    public int tlDameChuong;
    public boolean xDameChuong;
    public boolean wearingYacon;
    public boolean wearingRedNoelHat;
    public boolean wearingGrayNoelHat;
    public boolean wearingBlueNoelHat;
    public boolean wearingNoelHat;

    public boolean khangTDHS;

    public NPoint(Player player) {
        this.player = player;
        this.tlHp = new ArrayList<>();
        this.tlMp = new ArrayList<>();
        this.tlDef = new ArrayList<>();
        this.tlDame = new ArrayList<>();
        this.tlDameAttMob = new ArrayList<>();
        this.tlSDDep = new ArrayList<>();
        this.tlTNSM = new ArrayList<>();
        this.tlDameCrit = new ArrayList<>();
        this.tlSpeed = new ArrayList<>();
    }

    public void initPowerLimit() {
        powerLimit = PowerLimitManager.getInstance().get(limitPower);
    }

    /*-------------------------------------------------------------------------*/

    /**
     * Tính toán mọi chỉ số sau khi có thay đổi
     */
    public void calPoint() {
        try {
            if (this.player.pet != null) {
                this.player.pet.nPoint.setPointWhenWearClothes();
            }
            this.setPointWhenWearClothes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPoint(ItemOption io) {
        switch (io.optionTemplate.id) {
            case 0: //Tấn công +#
                this.dameAdd += io.param;
                break;
            case 2: //HP, KI+#000
                this.hpAdd += io.param * 1000;
                this.mpAdd += io.param * 1000;
                break;
            case 3: // vô hiệu vả biến st chưởng thành ki
                this.mstChuong += io.param;
                break;
            case 5: //+#% sức đánh chí mạng
                this.tlDameCrit.add(io.param);
                break;
            case 6: //HP+#
                this.hpAdd += io.param;
                break;
            case 7: //KI+#
                this.mpAdd += io.param;
                break;
            case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                this.tlHutHpMpXQ += io.param;
                break;
            case 14: //Chí mạng+#%
                this.critAdd += io.param;
                break;
            case 19: //Tấn công+#% khi đánh quái
                this.tlDameAttMob.add(io.param);
                break;
            case 22: //HP+#K
                this.hpAdd += io.param * 1000;
                break;
            case 23: //MP+#K
                this.mpAdd += io.param * 1000;
                break;
            case 24:
                this.wearingBuiBui = true;
                break;
            case 25:
                this.wearingYacon = true;
                break;
            case 26:
                this.wearingDrabula = true;
                this.player.effectSkin.lastTimeDrabula = System.currentTimeMillis();
                break;
            case 29:
                this.wearingMabu = true;
                break;
            case 27: //+# HP/30s
                this.hpHoiAdd += io.param;
                break;
            case 28: //+# KI/30s
                this.mpHoiAdd += io.param;
                break;
            case 33: //dịch chuyển tức thời
                this.teleport = true;
                break;
            case 47: //Giáp+#
                this.defAdd += io.param;
                break;
            case 48: //HP/KI+#
                this.hpAdd += io.param;
                this.mpAdd += io.param;
                break;
            case 49: //Tấn công+#%
            case 50: //Sức đánh+#%
                this.tlDame.add(io.param);
                break;
            case 77: //HP+#%
                this.tlHp.add(io.param);
                break;
            case 80: //HP+#%/30s
                this.tlHpHoi += io.param;
                break;
            case 81: //MP+#%/30s
                this.tlMpHoi += io.param;
                break;
            case 88: //Cộng #% exp khi đánh quái
                this.tlTNSM.add(io.param);
                break;
            case 94: //Giáp #%
                this.tlDef.add(io.param);
                break;
            case 95: //Biến #% tấn công thành HP
                this.tlHutHp += io.param;
                break;
            case 96: //Biến #% tấn công thành MP
                this.tlHutMp += io.param;
                break;
            case 97: //Phản #% sát thương
                this.tlPST += io.param;
                break;
            case 100: //+#% vàng từ quái
                this.tlGold += io.param;
                break;
            case 101: //+#% TN,SM
                this.tlTNSM.add(io.param);
                break;
            case 103: //KI +#%
                this.tlMp.add(io.param);
                break;
            case 104: //Biến #% tấn công quái thành HP
                this.tlHutHpMob += io.param;
                break;
            case 105: //Vô hình khi không đánh quái và boss
                this.wearingVoHinh = true;
                break;
            case 106: //Không ảnh hưởng bởi cái lạnh
                this.isKhongLanh = true;
                break;
            case 108: //#% Né đòn
                this.tlNeDon += io.param;
                break;
            case 109: //Hôi, giảm #% HP
                this.tlHpGiamODo += io.param;
                break;
            case 114:
                this.tlSpeed.add(io.param);
                break;
            case 117: //Đẹp +#% SĐ cho mình và người xung quanh
                this.tlSDDep.add(io.param);
                break;
            case 147: //+#% sức đánh
                this.tlDame.add(io.param);
                break;
            case 156: //Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                this.tlSubSD += 50;
                this.tlTNSM.add(io.param);
                this.tlGold += io.param;
                break;
            case 160:
                this.tlTNSMPet += io.param;
                break;
            case 162: //Cute hồi #% KI/s bản thân và xung quanh
                this.mpHoiCute += io.param;
                break;
            case 173: //Phục hồi #% HP và KI cho đồng đội
                this.tlHpHoiBanThanVaDongDoi += io.param;
                this.tlMpHoiBanThanVaDongDoi += io.param;
                break;
            case 189:
                this.wearingNezuko = true;
                break;
            case 190:
                this.wearingTanjiro = true;
                break;
            case 191:
                this.wearingInoHashi = true;
                break;
            case 192:
                this.wearingInosuke = true;
                break;
            case 193:
                this.wearingZenitsu = true;
                break;
            case 194:
                this.tlDameChuong = 3;
                break;
            case 195:
                this.tlDameChuong = 4;
                break;
            case 229: //HP pháp sư +#%
                this.tlHp.add(io.param);
                break;
            case 230: //KI pháp sư +#%
                this.tlMp.add(io.param);
                break;
            case 231: //Sức đánh pháp sư +#%
                this.tlDame.add(io.param);
                break;
            case 232: //Sức đánh chí mạng pháp sư +#%
                this.tlDameCrit.add(io.param);
                break;
            case 240: //HP pháp sư +#%
                this.TichLuy.add(io.param);
                break;
            case 116:
                this.khangTDHS = true;
                this.player.effectSkin.lastTimeDrabula = System.currentTimeMillis();
                break;
        }
    }

    private void setPointWhenWearClothes() {
        resetPoint();
        for (Item item : this.player.inventory.itemsBody) {
            if (item.isNotNullItem()) {
                int tempID = item.template.id;
                if (tempID >= 592 && tempID <= 594) {
                    teleport = true;
                }
                for (ItemOption io : item.itemOptions) {
                    setPoint(io);
                }
            }
        }
        List<Item> itemsBody = player.inventory.itemsBody;
        if (!player.isBoss && !player.isMiniPet) {
            if (player.inventory.itemsBody.get(1).isNotNullItem()) {
                Item pants = itemsBody.get(1);
                if (pants.isNotNullItem() && pants.getId() >= 691 && pants.getId() >= 693) {
                    player.event.setUseQuanHoa(true);
                }
            }
        }
        if (Manager.EVENT_SEVER == 3) {
            if (!this.player.isBoss && !this.player.isMiniPet) {
                if (itemsBody.get(5).isNotNullItem()) {
                    int tempID = itemsBody.get(5).getId();
                    switch (tempID) {
                        case 386:
                        case 389:
                        case 392:
                            wearingGrayNoelHat = true;
                            wearingNoelHat = true;
                            break;
                        case 387:
                        case 390:
                        case 393:
                            wearingRedNoelHat = true;
                            wearingNoelHat = true;
                            break;
                        case 388:
                        case 391:
                        case 394:
                            wearingBlueNoelHat = true;
                            wearingNoelHat = true;
                            break;
                        default:
                            wearingRedNoelHat = false;
                            wearingBlueNoelHat = false;
                            wearingGrayNoelHat = false;
                            wearingNoelHat = false;
                    }
                }
            }
        }
        CollectionBook book = player.getCollectionBook();

        if (book != null) {
            List<Card> cards = book.getCards();
            if (cards != null) {
                for (Card c : cards) {
                    if (c.getLevel() > 0) {
                        int index = 0;
                        for (ItemOption o : c.getCardTemplate().getOptions()) {
                            if ((index == 0 || c.isUse()) && c.getLevel() >= o.activeCard) {
                                setPoint(o);
                            }
                            index++;
                        }
                    }
                }
            }
        }
        setChisoPorata();
        setDameTrainArmor();
        setBasePoint();
    }

    private void setChisoPorata() {
        if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 921) {
                    for (ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                        }
                    }
                    break;
                }
            }
        }
        if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 1165) {
                    for (ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                        }
                    }
                    break;
                }
            }
        }
        if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 1129) {
                    for (ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                        }
                    }
                    break;
                }
            }
        }
        if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 1550) {
                    for (ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                        }
                    }
                    break;
                }
            }
        }
    }

    private void setDameTrainArmor() {
        if (!this.player.isPet && !this.player.isBoss && !this.player.isMiniPet) {
            try {
                Item gtl = this.player.inventory.itemsBody.get(6);
                if (gtl.isNotNullItem()) {
                    this.wearingTrainArmor = true;
                    this.wornTrainArmor = true;
                    this.player.inventory.trainArmor = gtl;
                    this.tlSubSD += ItemService.gI().getPercentTrainArmor(gtl);
                } else {
                    if (this.wornTrainArmor) {
                        this.wearingTrainArmor = false;
                        for (ItemOption io : this.player.inventory.trainArmor.itemOptions) {
                            if (io.optionTemplate.id == 9 && io.param > 0) {
                                this.tlDame.add(ItemService.gI().getPercentTrainArmor(this.player.inventory.trainArmor));
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.error("Lỗi get giáp tập luyện " + this.player.name);
            }
        }
    }

    private void setNeDon() {
        //ngọc rồng đen 6 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[5] > System.currentTimeMillis()) {
            this.tlNeDon += RewardBlackBall.R6S;
        }
    }

    private void setHpHoi() {
        this.hpHoi = calPercent(this.hpMax, 1);
        this.hpHoi += this.hpHoiAdd;
        this.hpHoi += calPercent(this.hpMax, this.tlHpHoi);
        this.hpHoi += calPercent(this.hpMax, this.tlHpHoiBanThanVaDongDoi);
        if (this.player.effectSkin.isNezuko) {
            this.hpHoi += calPercent(this.hpMax, 3);
        }
    }

    private void setMpHoi() {
        this.mpHoi = calPercent(this.mpMax, 1);
        this.mpHoi += this.mpHoiAdd;
        this.mpHoi += calPercent(this.mpMax, this.tlMpHoi);
        this.mpHoi += calPercent(this.mpMax, this.tlMpHoiBanThanVaDongDoi);
        if (this.player.effectSkin.isNezuko) {
            this.mpHoi += calPercent(this.mpMax, 3);
        }
    }

//    private void setHpMax() {
//        this.hpMax = this.hpg;
//        this.hpMax += this.hpAdd;
//        //đồ
//        for (Integer tl : this.tlHp) {
//            this.hpMax += calPercent(this.hpMax, tl);
//        }
//        //set tinh ấn
//        if (this.player.setClothes.tinhan == 5) {
//            this.hpMax += calPercent(this.hpMax, 15);
//        }
//        //set nappa
//        if (this.player.setClothes.nappa == 5) {
//            this.hpMax += calPercent(this.hpMax, 100);
//        }
//        //ngọc rồng đen 2 sao
//        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {
//            this.hpMax += calPercent(this.hpMax, RewardBlackBall.R2S);
//        }
//        //khỉ
//        if (this.player.effectSkill.isMonkey) {
//            if (!this.player.isPet || (this.player.isPet
//                    && ((Pet) this.player).status != Pet.FUSION)) {
//                int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
//                this.hpMax += calPercent(this.hpMax, percent);
//            }
//        }
//        //chỉ số pet khi hợp thể
//        if (this.player.isPet && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
//            if (((Pet) this.player).typePet == 1) {//mabu
//                this.hpMax += calPercent(this.hpMax, 20);
//            } else if (((Pet) this.player).typePet == 2) {//berus
//                this.hpMax += calPercent(this.hpMax, 30);
//            } else if (((Pet) this.player).typePet == 3) {//zeno
//                this.hpMax += calPercent(this.hpMax, 50);
//            }

    /// /            else if (((Pet) this.player).typePet == 4) {//luffy
    /// /                this.hpMax += calPercent(this.hpMax, 30);
    /// /            }
//        }
//        if (this.player.isPet && this.player.PorataVIP == true && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
//            this.hpMax += calPercent(this.hpMax, 10);
//        }
//        //phù
//        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
//            this.hpMax *= this.player.effectSkin.xHPKI;
//        }
//        //phù mabu 14h
//        if (this.player.zone != null && MapService.gI().isMapMabuWar14H(this.player.zone.map.mapId)) {
//            this.hpMax += 1000000;
//        }
//        //+hp đệ
//        if (this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
//            this.hpMax += this.player.pet.nPoint.hpMax;
//        }
//        //huýt sáo
//        if (!this.player.isPet
//                || (this.player.isPet
//                && ((Pet) this.player).status != Pet.FUSION)) {
//            if (this.player.effectSkill.tiLeHPHuytSao != 0) {
//                this.hpMax += calPercent(this.hpMax, this.player.effectSkill.tiLeHPHuytSao);
//            }
//        }
//        //bổ huyết
//        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet) {
//            this.hpMax *= 2;
//        }
//        //bổ huyết 2
//        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet2) {
//            this.hpMax += calPercent(hpMax, 120);
//        }
//        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
//                && !this.isKhongLanh) {
//            this.hpMax /= 2;
//        }
//        if (!player.isBoss) {
//            Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.HP);
//            if (at != null && !at.isExpired()) {
//                hpMax += calPercent(hpMax, at.getValue());
//            }
//        }
//        if (this.player.itemTime != null) {
//            if (this.player.itemTime.isUseBanhTet) {
//                hpMax += calPercent(hpMax, 20);
//            }
//        }
//        if (player.getBuff() == Buff.BUFF_HP) {
//            hpMax += calPercent(hpMax, 20);
//        }
//        //đuôi khỉ
//        if (!this.player.isPet && this.player.itemTimesieucap.isDuoikhi
//                || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDuoikhi) {
//            this.hpMax += calPercent(hpMax, 5);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isDaNgucTu) {
//            this.hpMax += calPercent(hpMax, 10);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isBienhinh) {
//            this.hpMax += calPercent(hpMax, 20);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseXiMuoi) {
//            this.hpMax += calPercent(hpMax, 5);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4042) {
//            this.hpMax += calPercent(hpMax, 10);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4043) {
//            this.hpMax += calPercent(hpMax, 20);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4125) {
//            this.hpMax += calPercent(hpMax, 30);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4126) {
//            this.hpMax += calPercent(hpMax, 40);
//        }
//
//        if (this.player.chuyensinh > 0) {
//            if (this.player.chuyensinh <= 10) {
//                this.hpMax += (15650) * this.player.chuyensinh;
//            }
//            if (this.player.chuyensinh <= 20 && this.player.chuyensinh > 10) {
//                this.hpMax += (30750) * (this.player.chuyensinh);
//            }
//            if (this.player.chuyensinh > 20 && this.player.chuyensinh <= 30) {
//                this.hpMax += (45875) * (this.player.chuyensinh);
//            }
//            if (this.player.chuyensinh > 30) {
//                this.hpMax += (60000) * (this.player.chuyensinh);
//            }
//        }
//
//        if (this.player.isPl() && this.player.isTitleUse3 == true && this.player.lastTimeTitle3 > 0) {
//            this.hpMax += calPercent(hpMax, 20);
//        }
//        if (this.player.isPl() && this.player.isTitleUse2 == true && this.player.lastTimeTitle2 > 0) {
//            this.hpMax += calPercent(hpMax, 20);
//        }
//        if (this.player.isPl() && this.player.isTitleUse1 == true && this.player.lastTimeTitle1 > 0) {
//            this.hpMax += calPercent(hpMax, 20);
//        }
//        //rồng băng
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongBang) {
//            this.hpMax += calPercent(hpMax, 15);
//        }
//        //rồng siêu cấp
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongSieuCap) {
//            this.hpMax += calPercent(hpMax, 50);
//        }
//    }
    // fix current
//    private synchronized void setHpMax() {
//        this.hpMax = this.hpg;
//        this.hpMax += this.hpAdd;
//
//        // Sử dụng Iterator để tránh ConcurrentModificationException
//        Iterator<Integer> iterator = this.tlHp.iterator();
//        while (iterator.hasNext()) {
//            Integer tl = iterator.next();
//            this.hpMax += calPercent(this.hpMax, tl);
//        }
//
//        //set tinh ấn
//        if (this.player.setClothes.tinhan == 5) {
//            this.hpMax += calPercent(this.hpMax, 15);
//        }
//        //set nappa
//        if (this.player.setClothes.nappa == 5) {
//            this.hpMax += calPercent(this.hpMax, 100);
//        }
//        //ngọc rồng đen 2 sao
//        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {
//            this.hpMax += calPercent(this.hpMax, RewardBlackBall.R2S);
//        }
//        //khỉ
//        if (this.player.effectSkill.isMonkey) {
//            if (!this.player.isPet || (this.player.isPet
//                    && ((Pet) this.player).status != Pet.FUSION)) {
//                int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
//                this.hpMax += calPercent(this.hpMax, percent);
//            }
//        }
//        //chỉ số pet khi hợp thể
//        if (this.player.isPet && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
//            if (((Pet) this.player).typePet == 1) {//mabu
//                this.hpMax += calPercent(this.hpMax, 30);
//            } else if (((Pet) this.player).typePet == 2) {//berus
//                this.hpMax += calPercent(this.hpMax, 50);
//            } else if (((Pet) this.player).typePet == 3) {//zeno
//                this.hpMax += calPercent(this.hpMax, 100);
//            }
//        }
//        if (this.player.isPet && this.player.PorataVIP && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
//            this.hpMax += calPercent(this.hpMax, 10);
//        }
//        //phù
//        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
//            this.hpMax *= this.player.effectSkin.xHPKI;
//        }
//        //phù mabu 14h
//        if (this.player.zone != null && MapService.gI().isMapMabuWar14H(this.player.zone.map.mapId)) {
//            this.hpMax += 1000000;
//        }
//        //+hp đệ
//        if (this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
//            this.hpMax += this.player.pet.nPoint.hpMax;
//        }
//        //huýt sáo
//        if (!this.player.isPet
//                || (this.player.isPet
//                && ((Pet) this.player).status != Pet.FUSION)) {
//            if (this.player.effectSkill.tiLeHPHuytSao != 0) {
//                this.hpMax += calPercent(this.hpMax, this.player.effectSkill.tiLeHPHuytSao);
//            }
//        }
//        //bổ huyết
//        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet) {
//            this.hpMax *= 2;
//        }
//        //bổ huyết 2
//        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet2) {
//            this.hpMax += calPercent(hpMax, 120);
//        }
//        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
//                && !this.isKhongLanh) {
//            this.hpMax /= 2;
//        }
//        if (!player.isBoss) {
//            Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.HP);
//            if (at != null && !at.isExpired()) {
//                hpMax += calPercent(hpMax, at.getValue());
//            }
//        }
//        if (this.player.itemTime != null) {
//            if (this.player.itemTime.isUseBanhTet) {
//                hpMax += calPercent(hpMax, 20);
//            }
//        }
//        if (player.getBuff() == Buff.BUFF_HP) {
//            hpMax += calPercent(hpMax, 20);
//        }
//        //đuôi khỉ
//        if (!this.player.isPet && this.player.itemTimesieucap.isDuoikhi
//                || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDuoikhi) {
//            this.hpMax += calPercent(hpMax, 5);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isDaNgucTu) {
//            this.hpMax += calPercent(hpMax, 10);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isBienhinh) {
//            this.hpMax += calPercent(hpMax, 20);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseXiMuoi) {
//            this.hpMax += calPercent(hpMax, 5);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4042) {
//            this.hpMax += calPercent(hpMax, 10);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4043) {
//            this.hpMax += calPercent(hpMax, 20);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4125) {
//            this.hpMax += calPercent(hpMax, 30);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4126) {
//            this.hpMax += calPercent(hpMax, 40);
//        }
//
//        if (this.player.chuyensinh > 0) {
//            if (this.player.chuyensinh <= 10) {
//                this.hpMax += (15650) * this.player.chuyensinh;
//            }
//            if (this.player.chuyensinh <= 20 && this.player.chuyensinh > 10) {
//                this.hpMax += (30750) * (this.player.chuyensinh);
//            }
//            if (this.player.chuyensinh > 20 && this.player.chuyensinh <= 30) {
//                this.hpMax += (45875) * (this.player.chuyensinh);
//            }
//            if (this.player.chuyensinh > 30) {
//                this.hpMax += (60000) * (this.player.chuyensinh);
//            }
//        }
//
//        if (this.player.isPl() && this.player.isTitleUse3 && this.player.lastTimeTitle3 > 0) {
//            this.hpMax += calPercent(hpMax, 20);
//        }
//        if (this.player.isPl() && this.player.isTitleUse2 && this.player.lastTimeTitle2 > 0) {
//            this.hpMax += calPercent(hpMax, 20);
//        }
//        if (this.player.isPl() && this.player.isTitleUse1 && this.player.lastTimeTitle1 > 0) {
//            this.hpMax += calPercent(hpMax, 20);
//        }
//        //rồng băng
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongBang) {
//            this.hpMax += calPercent(hpMax, 15);
//        }
//        //rồng siêu cấp
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongSieuCap) {
//            this.hpMax += calPercent(hpMax, 50);
//        }
//    }
    private synchronized void setHpMax() {
        Attribute at;
        this.hpMax = this.hpg;
        this.hpMax += (long) this.hpAdd;
//        for (Integer tl : this.tlHp) {
//            this.hpMax += this.calPercent(this.hpMax, tl.intValue());
//        }
        List<Integer> tempTlHp = new ArrayList<>(this.tlHp);
        for (Integer tl : tempTlHp) {
            this.hpMax += this.calPercent(this.hpMax, tl.intValue());
        }

        if (this.player.setClothes.tinhan == 5) {
            this.hpMax += this.calPercent(this.hpMax, 15L);
        }
        if (this.player.setClothes.nappa == 5) {
            this.hpMax += this.calPercent(this.hpMax, 100L);
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {
            this.hpMax += this.calPercent(this.hpMax, 40L);
        }
        if (this.player.effectSkill.isMonkey && (!this.player.isPet || this.player.isPet && ((Pet) this.player).status != 4)) {
            int percent = SkillUtil.getPercentHpMonkey(this.player.effectSkill.levelMonkey);
            this.hpMax += this.calPercent(this.hpMax, percent);
        }
        if (this.player.isPet && (((Pet) this.player).master.fusion.typeFusion == 6 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            if (((Pet) this.player).typePet == 1) {
                this.hpMax += this.calPercent(this.hpMax, 30L);
            } else if (((Pet) this.player).typePet == 2) {
                this.hpMax += this.calPercent(this.hpMax, 50L);
            } else if (((Pet) this.player).typePet == 3) {
                this.hpMax += this.calPercent(this.hpMax, 100L);
            }
        }
        if (this.player.isPet && this.player.PorataVIP && (((Pet) this.player).master.fusion.typeFusion == 6 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.hpMax += this.calPercent(this.hpMax, 10L);
        }
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            this.hpMax *= (long) this.player.effectSkin.xHPKI;
        }
        if (this.player.zone != null && MapService.gI().isMapMabuWar14H(this.player.zone.map.mapId)) {
            this.hpMax += 1000000L;
        }
        if (this.player.fusion.typeFusion != 0) {
            this.hpMax += this.player.pet.nPoint.hpMax;
        }
        if ((!this.player.isPet || this.player.isPet && ((Pet) this.player).status != 4) && this.player.effectSkill.tiLeHPHuytSao != 0) {
            this.hpMax += this.calPercent(this.hpMax, this.player.effectSkill.tiLeHPHuytSao);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet) {
            this.hpMax *= 2L;
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet2) {
            this.hpMax += this.calPercent(this.hpMax, 120L);
        }
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map) && !this.isKhongLanh) {
            this.hpMax /= 2L;
        }
        if (!this.player.isBoss && (at = ServerManager.gI().getAttributeManager().find(4)) != null && !at.isExpired()) {
            this.hpMax += this.calPercent(this.hpMax, at.getValue());
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhTet) {
            this.hpMax += this.calPercent(this.hpMax, 20L);
        }
        if (this.player.getBuff() == Buff.BUFF_HP) {
            this.hpMax += this.calPercent(this.hpMax, 20L);
        }
        if (!this.player.isPet && this.player.itemTimesieucap.isDuoikhi || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDuoikhi) {
            this.hpMax += this.calPercent(this.hpMax, 5L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isDaNgucTu) {
            this.hpMax += this.calPercent(this.hpMax, 10L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isBienhinh) {
            this.hpMax += this.calPercent(this.hpMax, 20L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseXiMuoi) {
            this.hpMax += this.calPercent(this.hpMax, 5L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4042) {
            this.hpMax += this.calPercent(this.hpMax, 10L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4043) {
            this.hpMax += this.calPercent(this.hpMax, 20L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4125) {
            this.hpMax += this.calPercent(this.hpMax, 30L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4126) {
            this.hpMax += this.calPercent(this.hpMax, 40L);
        }
        if (this.player.chuyensinh > 0) {
            if (this.player.chuyensinh <= 10) {
                this.hpMax += (long) (15650 * this.player.chuyensinh);
            }
            if (this.player.chuyensinh <= 20 && this.player.chuyensinh > 10) {
                this.hpMax += (long) (30750 * this.player.chuyensinh);
            }
            if (this.player.chuyensinh > 20 && this.player.chuyensinh <= 30) {
                this.hpMax += (long) (45875 * this.player.chuyensinh);
            }
            if (this.player.chuyensinh > 30) {
                this.hpMax += (long) (60000 * this.player.chuyensinh);
            }
        }
        if (this.player.isPl() && this.player.isTitleUse3 && this.player.lastTimeTitle3 > 0L) {
            this.hpMax += this.calPercent(this.hpMax, 20L);
        }
        if (this.player.isPl() && this.player.isTitleUse2 && this.player.lastTimeTitle2 > 0L) {
            this.hpMax += this.calPercent(this.hpMax, 20L);
        }
        if (this.player.isPl() && this.player.isTitleUse1 && this.player.lastTimeTitle1 > 0L) {
            this.hpMax += this.calPercent(this.hpMax, 20L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongBang) {
            this.hpMax += this.calPercent(this.hpMax, 15L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongSieuCap) {
            this.hpMax += this.calPercent(this.hpMax, 50L);
        }
    }

    // (hp sư phụ + hp đệ tử ) + 15%
    // (hp sư phụ + 15% +hp đệ tử)
    private void setHp() {
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    //    private void setMpMax() {
//        this.mpMax = this.mpg;
//        this.mpMax += this.mpAdd;
//        //đồ
//        for (Integer tl : this.tlMp) {
//            this.mpMax += calPercent(this.mpMax, tl);
//        }
//        if (this.player.setClothes.picolo == 5) {
//            this.mpMax *= 3;
//        }
//        //set nhật ấn
//        if (this.player.setClothes.nhatan == 5) {
//            this.mpMax += calPercent(this.mpMax, 15);
//        }
//        //ngọc rồng đen 3 sao
//        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
//            this.mpMax += calPercent(this.mpMax, RewardBlackBall.R3S);
//        }
//        //chỉ số pet khi hợp thể
//        if (this.player.isPet && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
//            if (((Pet) this.player).typePet == 1) {//mabu
//                this.mpMax += calPercent(this.mpMax, 30);
//            } else if (((Pet) this.player).typePet == 2) {//berus
//                this.mpMax += calPercent(this.mpMax, 50);
//            } else if (((Pet) this.player).typePet == 3) {//zeno
//                this.mpMax += calPercent(this.mpMax, 100);
//            }
//
//    / /            else if (((Pet) this.player).typePet == 4) {//luffy
//    / /                this.mpMax += calPercent(this.mpMax, 30);
//    / /            }
//        }
//        if (this.player.isPet && this.player.PorataVIP == true && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
//            this.mpMax += calPercent(this.mpMax, 10);
//        }
//        //hợp thể
//        if (this.player.fusion.typeFusion != 0) {
//            this.mpMax += this.player.pet.nPoint.mpMax;
//        }
//        //bổ khí
//        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi) {
//            this.mpMax *= 2;
//        }
//        //bổ khí 2
//        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi2) {
//            this.mpMax += calPercent(mpMax, 120);
//        }
//        //phù
//        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
//            this.mpMax *= this.player.effectSkin.xHPKI;
//        }
//        //phù mabu 14h
//        if (this.player.zone != null && MapService.gI().isMapMabuWar14H(this.player.zone.map.mapId)) {
//            this.mpMax += 1000000;
//        }
//        //xiên cá
//        if (this.player.effectFlagBag.useXienCa) {
//            this.mpMax += calPercent(this.mpMax, 15);
//        }
//        //Kiem z
//        if (this.player.effectFlagBag.useKiemz) {
//            this.mpMax += calPercent(this.mpMax, 20);
//        }
//        if (this.player.effectFlagBag.useDieuRong) {
//            this.mpMax += calPercent(this.mpMax, 30);
//        }
//        if (this.player.effectFlagBag.useHoaVang || this.player.effectFlagBag.useHoaHong) {
//            this.mpMax += calPercent(this.mpMax, 20);
//        }
//        if (!player.isBoss) {
//            Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.KI);
//            if (at != null && !at.isExpired()) {
//                mpMax += calPercent(mpMax, at.getValue());
//            }
//        }
//        if (this.player.itemTime != null) {
//            if (this.player.itemTime.isUseBanhTet) {
//                mpMax += calPercent(mpMax, 20);
//            }
//        }
//        if (player.getBuff() == Buff.BUFF_KI) {
//            mpMax += calPercent(mpMax, 20);
//        }
//        //đuôi khỉ
//        if (!this.player.isPet && this.player.itemTimesieucap.isDuoikhi
//                || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDuoikhi) {
//            this.mpMax += calPercent(mpMax, 5);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isDaNgucTu) {
//            this.mpMax += calPercent(mpMax, 10);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isBienhinh) {
//            this.mpMax += calPercent(mpMax, 20);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseXiMuoi) {
//            this.mpMax += calPercent(mpMax, 5);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4042) {
//            this.mpMax += calPercent(mpMax, 10);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4043) {
//            this.mpMax += calPercent(mpMax, 20);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4125) {
//            this.mpMax += calPercent(mpMax, 30);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4126) {
//            this.mpMax += calPercent(mpMax, 40);
//        }
//        if (this.player.chuyensinh > 0) {
//            if (this.player.chuyensinh <= 10) {
//                this.mpMax += (15650) * this.player.chuyensinh;
//            }
//            if (this.player.chuyensinh <= 20 && this.player.chuyensinh > 10) {
//                this.mpMax += (30750) * (this.player.chuyensinh);
//            }
//            if (this.player.chuyensinh > 20 && this.player.chuyensinh <= 30) {
//                this.mpMax += (45875) * (this.player.chuyensinh);
//            }
//            if (this.player.chuyensinh > 30) {
//                this.mpMax += (60000) * (this.player.chuyensinh);
//            }
//        }
//
//        if (this.player.isPl() && this.player.isTitleUse3 == true && this.player.lastTimeTitle3 > 0) {
//            this.mpMax += calPercent(mpMax, 20);
//        }
//        if (this.player.isPl() && this.player.isTitleUse2 == true && this.player.lastTimeTitle2 > 0) {
//            this.mpMax += calPercent(mpMax, 20);
//        }
//        if (this.player.isPl() && this.player.isTitleUse1 == true && this.player.lastTimeTitle1 > 0) {
//            this.mpMax += calPercent(mpMax, 20);
//        }
//        //rồng băng
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongBang) {
//            this.mpMax += calPercent(mpMax, 15);
//        }
//        //rồng siêu cấp
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongSieuCap) {
//            this.mpMax += calPercent(mpMax, 50);
//        }
//    }
    // fix curent MP
//    private void setMpMax() {
//        this.mpMax = this.mpg;
//        this.mpMax += this.mpAdd;
//
//        // Đồ
//        Iterator<Integer> iterator = this.tlMp.iterator();
//        while (iterator.hasNext()) {
//            Integer tl = iterator.next();
//            this.mpMax += calPercent(this.mpMax, tl);
//        }
//
//        // Set nhật ấn
//        if (this.player.setClothes.picolo == 5) {
//            this.mpMax *= 3;
//        }
//
//        // Ngọc rồng đen 3 sao
//        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
//            this.mpMax += calPercent(this.mpMax, RewardBlackBall.R3S);
//        }
//
//        // Chỉ số pet khi hợp thể
//        if (this.player.isPet && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
//            if (((Pet) this.player).typePet == 1) {
//                this.mpMax += calPercent(this.mpMax, 30); // Mabu
//            } else if (((Pet) this.player).typePet == 2) {
//                this.mpMax += calPercent(this.mpMax, 50); // Berus
//            } else if (((Pet) this.player).typePet == 3) {
//                this.mpMax += calPercent(this.mpMax, 100); // Zeno
//            }
//        }
//
//        // Kiểm tra Porata VIP
//        if (this.player.isPet && this.player.PorataVIP && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
//            this.mpMax += calPercent(this.mpMax, 10);
//        }
//
//        // Hợp thể
//        if (this.player.fusion.typeFusion != 0) {
//            this.mpMax += this.player.pet.nPoint.mpMax;
//        }
//
//        // Bổ khí
//        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi) {
//            this.mpMax *= 2;
//        }
//
//        // Bổ khí 2
//        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi2) {
//            this.mpMax += calPercent(mpMax, 120);
//        }
//
//        // Phù
//        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
//            this.mpMax *= this.player.effectSkin.xHPKI;
//        }
//
//        // Phù Mabu 14h
//        if (this.player.zone != null && MapService.gI().isMapMabuWar14H(this.player.zone.map.mapId)) {
//            this.mpMax += 1000000;
//        }
//
//        // Xiên cá
//        if (this.player.effectFlagBag.useXienCa) {
//            this.mpMax += calPercent(this.mpMax, 15);
//        }
//
//        // Kiem z
//        if (this.player.effectFlagBag.useKiemz) {
//            this.mpMax += calPercent(this.mpMax, 20);
//        }
//
//        if (this.player.effectFlagBag.useDieuRong) {
//            this.mpMax += calPercent(this.mpMax, 30);
//        }
//
//        if (this.player.effectFlagBag.useHoaVang || this.player.effectFlagBag.useHoaHong) {
//            this.mpMax += calPercent(this.mpMax, 20);
//        }
//
//        if (!player.isBoss) {
//            Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.KI);
//            if (at != null && !at.isExpired()) {
//                mpMax += calPercent(mpMax, at.getValue());
//            }
//        }
//
//        if (this.player.itemTime != null) {
//            if (this.player.itemTime.isUseBanhTet) {
//                mpMax += calPercent(mpMax, 20);
//            }
//        }
//
//        if (player.getBuff() == Buff.BUFF_KI) {
//            mpMax += calPercent(mpMax, 20);
//        }
//
//        // Đuôi khỉ
//        if (!this.player.isPet && this.player.itemTimesieucap.isDuoikhi
//                || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDuoikhi) {
//            this.mpMax += calPercent(mpMax, 5);
//        }
//
//        // Các item timesieucap khác
//        if (this.player.itemTimesieucap != null) {
//            if (this.player.itemTimesieucap.isDaNgucTu) {
//                this.mpMax += calPercent(mpMax, 10);
//            }
//            if (this.player.itemTimesieucap.isBienhinh) {
//                this.mpMax += calPercent(mpMax, 20);
//            }
//            if (this.player.itemTimesieucap.isUseXiMuoi) {
//                this.mpMax += calPercent(mpMax, 5);
//            }
//            if (this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4042) {
//                this.mpMax += calPercent(mpMax, 10);
//            }
//            if (this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4043) {
//                this.mpMax += calPercent(mpMax, 20);
//            }
//            if (this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4125) {
//                this.mpMax += calPercent(mpMax, 30);
//            }
//            if (this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4126) {
//                this.mpMax += calPercent(mpMax, 40);
//            }
//        }
//
//        // Chuyển sinh
//        if (this.player.chuyensinh > 0) {
//            if (this.player.chuyensinh <= 10) {
//                this.mpMax += (15650) * this.player.chuyensinh;
//            }
//            if (this.player.chuyensinh <= 20 && this.player.chuyensinh > 10) {
//                this.mpMax += (30750) * (this.player.chuyensinh);
//            }
//            if (this.player.chuyensinh > 20 && this.player.chuyensinh <= 30) {
//                this.mpMax += (45875) * (this.player.chuyensinh);
//            }
//            if (this.player.chuyensinh > 30) {
//                this.mpMax += (60000) * (this.player.chuyensinh);
//            }
//        }
//
//        // Title
//        if (this.player.isPl() && this.player.isTitleUse3 && this.player.lastTimeTitle3 > 0) {
//            this.mpMax += calPercent(mpMax, 20);
//        }
//        if (this.player.isPl() && this.player.isTitleUse2 && this.player.lastTimeTitle2 > 0) {
//            this.mpMax += calPercent(mpMax, 20);
//        }
//        if (this.player.isPl() && this.player.isTitleUse1 && this.player.lastTimeTitle1 > 0) {
//            this.mpMax += calPercent(mpMax, 20);
//        }
//
//        // Rồng băng
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongBang) {
//            this.mpMax += calPercent(mpMax, 15);
//        }
//
//        // Rồng siêu cấp
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongSieuCap) {
//            this.mpMax += calPercent(mpMax, 50);
//        }
//    }
    private void setMpMax() {
        Attribute at;
        this.mpMax = this.mpg;
        this.mpMax += (long) this.mpAdd;
        for (Integer tl : this.tlMp) {
            this.mpMax += this.calPercent(this.mpMax, tl.intValue());
        }
        if (this.player.setClothes.picolo == 5) {
            this.mpMax *= 3L;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
            this.mpMax += this.calPercent(this.mpMax, 40L);
        }
        if (this.player.isPet && (((Pet) this.player).master.fusion.typeFusion == 6 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            if (((Pet) this.player).typePet == 1) {
                this.mpMax += this.calPercent(this.mpMax, 30L);
            } else if (((Pet) this.player).typePet == 2) {
                this.mpMax += this.calPercent(this.mpMax, 50L);
            } else if (((Pet) this.player).typePet == 3) {
                this.mpMax += this.calPercent(this.mpMax, 100L);
            }
        }
        if (this.player.isPet && this.player.PorataVIP && (((Pet) this.player).master.fusion.typeFusion == 6 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.mpMax += this.calPercent(this.mpMax, 10L);
        }
        if (this.player.fusion.typeFusion != 0) {
            this.mpMax += this.player.pet.nPoint.mpMax;
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi) {
            this.mpMax *= 2L;
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi2) {
            this.mpMax += this.calPercent(this.mpMax, 120L);
        }
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            this.mpMax *= (long) this.player.effectSkin.xHPKI;
        }
        if (this.player.zone != null && MapService.gI().isMapMabuWar14H(this.player.zone.map.mapId)) {
            this.mpMax += 1000000L;
        }
        if (this.player.effectFlagBag.useXienCa) {
            this.mpMax += this.calPercent(this.mpMax, 15L);
        }
        if (this.player.effectFlagBag.useKiemz) {
            this.mpMax += this.calPercent(this.mpMax, 20L);
        }
        if (this.player.effectFlagBag.useDieuRong) {
            this.mpMax += this.calPercent(this.mpMax, 30L);
        }
        if (this.player.effectFlagBag.useHoaVang || this.player.effectFlagBag.useHoaHong) {
            this.mpMax += this.calPercent(this.mpMax, 20L);
        }
        if (!this.player.isBoss && (at = ServerManager.gI().getAttributeManager().find(3)) != null && !at.isExpired()) {
            this.mpMax += this.calPercent(this.mpMax, at.getValue());
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhTet) {
            this.mpMax += this.calPercent(this.mpMax, 20L);
        }
        if (this.player.getBuff() == Buff.BUFF_KI) {
            this.mpMax += this.calPercent(this.mpMax, 20L);
        }
        if (!this.player.isPet && this.player.itemTimesieucap.isDuoikhi || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDuoikhi) {
            this.mpMax += this.calPercent(this.mpMax, 5L);
        }
        if (this.player.itemTimesieucap != null) {
            if (this.player.itemTimesieucap.isDaNgucTu) {
                this.mpMax += this.calPercent(this.mpMax, 10L);
            }
            if (this.player.itemTimesieucap.isBienhinh) {
                this.mpMax += this.calPercent(this.mpMax, 20L);
            }
            if (this.player.itemTimesieucap.isUseXiMuoi) {
                this.mpMax += this.calPercent(this.mpMax, 5L);
            }
            if (this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4042) {
                this.mpMax += this.calPercent(this.mpMax, 10L);
            }
            if (this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4043) {
                this.mpMax += this.calPercent(this.mpMax, 20L);
            }
            if (this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4125) {
                this.mpMax += this.calPercent(this.mpMax, 30L);
            }
            if (this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4126) {
                this.mpMax += this.calPercent(this.mpMax, 40L);
            }
        }
        if (this.player.chuyensinh > 0) {
            if (this.player.chuyensinh <= 10) {
                this.mpMax += (long) (15650 * this.player.chuyensinh);
            }
            if (this.player.chuyensinh <= 20 && this.player.chuyensinh > 10) {
                this.mpMax += (long) (30750 * this.player.chuyensinh);
            }
            if (this.player.chuyensinh > 20 && this.player.chuyensinh <= 30) {
                this.mpMax += (long) (45875 * this.player.chuyensinh);
            }
            if (this.player.chuyensinh > 30) {
                this.mpMax += (long) (60000 * this.player.chuyensinh);
            }
        }
        if (this.player.isPl() && this.player.isTitleUse3 && this.player.lastTimeTitle3 > 0L) {
            this.mpMax += this.calPercent(this.mpMax, 20L);
        }
        if (this.player.isPl() && this.player.isTitleUse2 && this.player.lastTimeTitle2 > 0L) {
            this.mpMax += this.calPercent(this.mpMax, 20L);
        }
        if (this.player.isPl() && this.player.isTitleUse1 && this.player.lastTimeTitle1 > 0L) {
            this.mpMax += this.calPercent(this.mpMax, 20L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongBang) {
            this.mpMax += this.calPercent(this.mpMax, 15L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongSieuCap) {
            this.mpMax += this.calPercent(this.mpMax, 50L);
        }
    }

    private void setMp() {
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

//    private void setDame() {
//        this.dame = this.dameg;
//        this.dame += this.dameAdd;
//        //đồ
//        for (Integer tl : this.tlDame) {
//            this.dame += calPercent(this.dame, tl);
//        }
//        for (Integer tl : this.tlSDDep) {
//            this.dame += calPercent(this.dame, tl);
//        }
//        //set nguyệt ấn
//        if (this.player.setClothes.nguyetan == 5) {
//            this.dame += calPercent(this.dame, 10);
//        }
//        //chỉ số pet khi hợp thể
//        if (this.player.isPet && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
//            if (((Pet) this.player).typePet == 1) {//mabu
//                this.dame += calPercent(this.dame, 30);
//            } else if (((Pet) this.player).typePet == 2) {//berus
//                this.dame += calPercent(this.dame, 50);
//            } else if (((Pet) this.player).typePet == 3) {//zeno
//                this.dame += calPercent(this.dame, 100);
//            }
    /// /            else if (((Pet) this.player).typePet == 4) {//luffy
    /// /                this.dame += calPercent(this.dame, 30);
    /// /            }
//        }
//        if (this.player.isPet && this.player.PorataVIP && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
//            this.dame += calPercent(this.dame, 10);
//        }
//        //thức ăn
//        if (!this.player.isPet && this.player.itemTime.isEatMeal
//                || this.player.isPet && ((Pet) this.player).master.itemTime.isEatMeal) {
//            this.dame += calPercent(this.dame, 10);
//        }
//        //hợp thể
//        if (this.player.fusion.typeFusion != 0) {
//            this.dame += this.player.pet.nPoint.dame;
//        }
//        //cuồng nộ
//        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo) {
//            this.dame *= 2;
//        }
//        //cuồng nộ 2
//        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo2) {
//            this.dame += calPercent(dame, 120);
//        }
//        //phù mabu 14h
//        if (this.player.zone != null && MapService.gI().isMapMabuWar14H(this.player.zone.map.mapId)) {
//            this.dame += 10000;
//        }
//        //giảm dame
//        this.dame -= calPercent(this.dame, tlSubSD);
//        //map cold
//        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
//                && !this.isKhongLanh) {
//            this.dame /= 2;
//        }
//        //ngọc rồng đen 1 sao
//        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {
//            this.dame += calPercent(this.dame, RewardBlackBall.R1S);
//        }
//        if (!player.isBoss) {
//            Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.SUC_DANH);
//            if (at != null && !at.isExpired()) {
//                this.dame += calPercent(dame, at.getValue());
//            }
//        }
//        if (this.player.itemTime != null) {
//            if (this.player.itemTime.isUseBanhChung) {
//                dame += calPercent(dame, 20);
//            }
//        }
//        if (player.getBuff() == Buff.BUFF_ATK) {
//            dame += calPercent(dame, 20);
//        }
//        if (this.player.effectSkin.isNezuko) {
//            this.dame += calPercent(this.dame, 15);
//        }
//        //khỉ
//        if (this.player.effectSkill.isMonkey) {
//            if (!this.player.isPet || (this.player.isPet
//                    && ((Pet) this.player).status != Pet.FUSION)) {
//                int percent = SkillUtil.getPercentDameMonkey(player.effectSkill.levelMonkey);
//                this.dame += calPercent(dame, percent);
//            }
//        }
//        //thức ăn
//        if (!this.player.isPet && this.player.itemTime.isEatMeal
//                || this.player.isPet && ((Pet) this.player).master.itemTime.isEatMeal) {
//            this.dame += calPercent(dame, 10);
//        }
//
//        //đuôi khỉ
//        if (!this.player.isPet && this.player.itemTimesieucap.isDuoikhi
//                || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDuoikhi) {
//            this.dame += calPercent(dame, 5);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isDaNgucTu) {
//            this.dame += calPercent(dame, 10);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isBienhinh) {
//            this.dame += calPercent(dame, 20);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseXiMuoi) {
//            this.dame += calPercent(dame, 5);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4042) {
//            this.dame += calPercent(dame, 10);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4043) {
//            this.dame += calPercent(dame, 20);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4125) {
//            this.dame += calPercent(dame, 30);
//        }
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4126) {
//            this.dame += calPercent(dame, 40);
//        }
//        if (this.player.chuyensinh > 0) {
//            if (this.player.chuyensinh <= 10) {
//                this.dame += (1750) * this.player.chuyensinh;
//            }
//            if (this.player.chuyensinh <= 20 && this.player.chuyensinh > 10) {
//                this.dame += (3350) * (this.player.chuyensinh);
//            }
//            if (player.chuyensinh > 20 && this.player.chuyensinh <= 30) {
//                this.dame += (4950) * (this.player.chuyensinh);
//            }
//            if (player.chuyensinh > 30) {
//                this.dame += (6000) * (this.player.chuyensinh);
//            }
//        }
//
//        if (this.player.isPl() && this.player.isTitleUse3 && this.player.lastTimeTitle3 > 0) {
//            this.dame += calPercent(dame, 20);
//        }
//        if (this.player.isPl() && this.player.isTitleUse2 && this.player.lastTimeTitle2 > 0) {
//            this.dame += calPercent(dame, 20);
//        }
//        if (this.player.isPl() && this.player.isTitleUse1 && this.player.lastTimeTitle1 > 0) {
//            this.dame += calPercent(dame, 20);
//        }
//        //rồng băng
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongBang) {
//            this.dame += calPercent(dame, 15);
//        }
//        //rồng băng
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongSieuCap) {
//            this.dame += calPercent(dame, 50);
//        }
//    }
//    private void setDame() {
//        this.dame = this.dameg;
//        this.dame += this.dameAdd;
//
//        // đồ
//        List<Integer> tlDameCopy = new ArrayList<>(this.tlDame);  // Tạo bản sao của danh sách tlDame
//        for (Integer tl : tlDameCopy) {
//            this.dame += calPercent(this.dame, tl);
//        }
//

    /// /        List<Integer> tlDameCopy = (this.tlDame != null) ? new ArrayList<>(this.tlDame) : new ArrayList<>();
    /// /        for (Integer tl : tlDameCopy) {
    /// /            if (tl != null) {
    /// /                this.dame += calPercent(this.dame, tl);
    /// /            } else {
    /// /                System.err.println("Warning: tl is null in tlDameCopy");
    /// /            }
    /// /        }
//        // set nguyệt ấn
//        List<Integer> tlSDDepCopy = new ArrayList<>(this.tlSDDep);  // Tạo bản sao của danh sách tlSDDep
//        for (Integer tl : tlSDDepCopy) {
//            this.dame += calPercent(this.dame, tl);
//        }
//
//        // set nguyệt ấn
//        if (this.player.setClothes.nguyetan == 5) {
//            this.dame += calPercent(this.dame, 10);
//        }
//
//        // chỉ số pet khi hợp thể
//        if (this.player.isPet && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
//            if (((Pet) this.player).typePet == 1) { // mabu
//                this.dame += calPercent(this.dame, 30);
//            } else if (((Pet) this.player).typePet == 2) { // berus
//                this.dame += calPercent(this.dame, 50);
//            } else if (((Pet) this.player).typePet == 3) { // zeno
//                this.dame += calPercent(this.dame, 100);
//            }
//        }
//
//        if (this.player.isPet && this.player.PorataVIP && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
//                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
//            this.dame += calPercent(this.dame, 10);
//        }
//
//        // thức ăn
//        if (!this.player.isPet && this.player.itemTime.isEatMeal
//                || this.player.isPet && ((Pet) this.player).master.itemTime.isEatMeal) {
//            this.dame += calPercent(this.dame, 10);
//        }
//
//        // hợp thể
//        if (this.player.fusion.typeFusion != 0) {
//            this.dame += this.player.pet.nPoint.dame;
//        }
//
//        // cuồng nộ
//        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo) {
//            this.dame *= 2;
//        }
//
//        // cuồng nộ 2
//        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo2) {
//            this.dame += calPercent(dame, 120);
//        }
//
//        // phù mabu 14h
//        if (this.player.zone != null && MapService.gI().isMapMabuWar14H(this.player.zone.map.mapId)) {
//            this.dame += 10000;
//        }
//
//        // giảm dame
//        this.dame -= calPercent(this.dame, tlSubSD);
//
//        // map cold
//        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
//                && !this.isKhongLanh) {
//            this.dame /= 2;
//        }
//
//        // ngọc rồng đen 1 sao
//        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {
//            this.dame += calPercent(this.dame, RewardBlackBall.R1S);
//        }
//
//        if (!player.isBoss) {
//            Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.SUC_DANH);
//            if (at != null && !at.isExpired()) {
//                this.dame += calPercent(dame, at.getValue());
//            }
//        }
//
//        if (this.player.itemTime != null) {
//            if (this.player.itemTime.isUseBanhChung) {
//                dame += calPercent(dame, 20);
//            }
//        }
//
//        if (player.getBuff() == Buff.BUFF_ATK) {
//            dame += calPercent(dame, 20);
//        }
//
//        if (this.player.effectSkin.isNezuko) {
//            this.dame += calPercent(this.dame, 15);
//        }
//
//        // khỉ
//        if (this.player.effectSkill.isMonkey) {
//            if (!this.player.isPet || (this.player.isPet
//                    && ((Pet) this.player).status != Pet.FUSION)) {
//                int percent = SkillUtil.getPercentDameMonkey(player.effectSkill.levelMonkey);
//                this.dame += calPercent(dame, percent);
//            }
//        }
//
//        // thức ăn
//        if (!this.player.isPet && this.player.itemTime.isEatMeal
//                || this.player.isPet && ((Pet) this.player).master.itemTime.isEatMeal) {
//            this.dame += calPercent(dame, 10);
//        }
//
//        // đuôi khỉ
//        if (!this.player.isPet && this.player.itemTimesieucap.isDuoikhi
//                || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDuoikhi) {
//            this.dame += calPercent(dame, 5);
//        }
//
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isDaNgucTu) {
//            this.dame += calPercent(dame, 10);
//        }
//
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isBienhinh) {
//            this.dame += calPercent(dame, 20);
//        }
//
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseXiMuoi) {
//            this.dame += calPercent(dame, 5);
//        }
//
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4042) {
//            this.dame += calPercent(dame, 10);
//        }
//
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4043) {
//            this.dame += calPercent(dame, 20);
//        }
//
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4125) {
//            this.dame += calPercent(dame, 30);
//        }
//
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4126) {
//            this.dame += calPercent(dame, 40);
//        }
//
//        if (this.player.chuyensinh > 0) {
//            if (this.player.chuyensinh <= 10) {
//                this.dame += (1750) * this.player.chuyensinh;
//            }
//            if (this.player.chuyensinh <= 20 && this.player.chuyensinh > 10) {
//                this.dame += (3350) * (this.player.chuyensinh);
//            }
//            if (player.chuyensinh > 20 && this.player.chuyensinh <= 30) {
//                this.dame += (4950) * (this.player.chuyensinh);
//            }
//            if (player.chuyensinh > 30) {
//                this.dame += (6000) * (this.player.chuyensinh);
//            }
//        }
//
//        if (this.player.isPl() && this.player.isTitleUse3 && this.player.lastTimeTitle3 > 0) {
//            this.dame += calPercent(dame, 20);
//        }
//        if (this.player.isPl() && this.player.isTitleUse2 && this.player.lastTimeTitle2 > 0) {
//            this.dame += calPercent(dame, 20);
//        }
//        if (this.player.isPl() && this.player.isTitleUse1 && this.player.lastTimeTitle1 > 0) {
//            this.dame += calPercent(dame, 20);
//        }
//
//        // rồng băng
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongBang) {
//            this.dame += calPercent(dame, 15);
//        }
//
//        // rồng băng
//        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongSieuCap) {
//            this.dame += calPercent(dame, 50);
//        }
//    }
    private void setDame() {
        Attribute attribute;
        this.dame = this.dameg;
        this.dame += (long) this.dameAdd;
        ArrayList<Integer> tlDameCopy = new ArrayList<Integer>(this.tlDame);
        for (Integer n : tlDameCopy) {
            this.dame += this.calPercent(this.dame, n.intValue());
        }
        ArrayList<Integer> tlSDDepCopy = new ArrayList<Integer>(this.tlSDDep);
        for (Integer tl : tlSDDepCopy) {
            this.dame += this.calPercent(this.dame, tl.intValue());
        }
        if (this.player.setClothes.nguyetan == 5) {
            this.dame += this.calPercent(this.dame, 10L);
        }
        if (this.player.isPet && (((Pet) this.player).master.fusion.typeFusion == 6 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            if (((Pet) this.player).typePet == 1) {
                this.dame += this.calPercent(this.dame, 30L);
            } else if (((Pet) this.player).typePet == 2) {
                this.dame += this.calPercent(this.dame, 50L);
            } else if (((Pet) this.player).typePet == 3) {
                this.dame += this.calPercent(this.dame, 100L);
            }
        }
        if (this.player.isPet && this.player.PorataVIP && (((Pet) this.player).master.fusion.typeFusion == 6 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4 || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.dame += this.calPercent(this.dame, 10L);
        }
        if (!this.player.isPet && this.player.itemTime.isEatMeal || this.player.isPet && ((Pet) this.player).master.itemTime.isEatMeal) {
            this.dame += this.calPercent(this.dame, 10L);
        }
        if (this.player.fusion.typeFusion != 0) {
            this.dame += this.player.pet.nPoint.dame;
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo) {
            this.dame *= 2L;
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo2) {
            this.dame += this.calPercent(this.dame, 120L);
        }
        if (this.player.zone != null && MapService.gI().isMapMabuWar14H(this.player.zone.map.mapId)) {
            this.dame += 10000L;
        }
        this.dame -= this.calPercent(this.dame, this.tlSubSD);
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map) && !this.isKhongLanh) {
            this.dame /= 2L;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {
            this.dame += this.calPercent(this.dame, 24L);
        }
        if (!this.player.isBoss && (attribute = ServerManager.gI().getAttributeManager().find(5)) != null && !attribute.isExpired()) {
            this.dame += this.calPercent(this.dame, attribute.getValue());
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhChung) {
            this.dame += this.calPercent(this.dame, 20L);
        }
        if (this.player.getBuff() == Buff.BUFF_ATK) {
            this.dame += this.calPercent(this.dame, 20L);
        }
        if (this.player.effectSkin.isNezuko) {
            this.dame += this.calPercent(this.dame, 15L);
        }
        if (this.player.effectSkill.isMonkey && (!this.player.isPet || this.player.isPet && ((Pet) this.player).status != 4)) {
            int n = SkillUtil.getPercentDameMonkey(this.player.effectSkill.levelMonkey);
            this.dame += this.calPercent(this.dame, n);
        }
        if (!this.player.isPet && this.player.itemTime.isEatMeal || this.player.isPet && ((Pet) this.player).master.itemTime.isEatMeal) {
            this.dame += this.calPercent(this.dame, 10L);
        }
        if (!this.player.isPet && this.player.itemTimesieucap.isDuoikhi || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDuoikhi) {
            this.dame += this.calPercent(this.dame, 5L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isDaNgucTu) {
            this.dame += this.calPercent(this.dame, 10L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isBienhinh) {
            this.dame += this.calPercent(this.dame, 20L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseXiMuoi) {
            this.dame += this.calPercent(this.dame, 5L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4042) {
            this.dame += this.calPercent(this.dame, 10L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4043) {
            this.dame += this.calPercent(this.dame, 20L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4125) {
            this.dame += this.calPercent(this.dame, 30L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4126) {
            this.dame += this.calPercent(this.dame, 40L);
        }
        if (this.player.chuyensinh > 0) {
            if (this.player.chuyensinh <= 10) {
                this.dame += (long) (1750 * this.player.chuyensinh);
            }
            if (this.player.chuyensinh <= 20 && this.player.chuyensinh > 10) {
                this.dame += (long) (3350 * this.player.chuyensinh);
            }
            if (this.player.chuyensinh > 20 && this.player.chuyensinh <= 30) {
                this.dame += (long) (4950 * this.player.chuyensinh);
            }
            if (this.player.chuyensinh > 30) {
                this.dame += (long) (6000 * this.player.chuyensinh);
            }
        }
        if (this.player.isPl() && this.player.isTitleUse3 && this.player.lastTimeTitle3 > 0L) {
            this.dame += this.calPercent(this.dame, 20L);
        }
        if (this.player.isPl() && this.player.isTitleUse2 && this.player.lastTimeTitle2 > 0L) {
            this.dame += this.calPercent(this.dame, 20L);
        }
        if (this.player.isPl() && this.player.isTitleUse1 && this.player.lastTimeTitle1 > 0L) {
            this.dame += this.calPercent(this.dame, 20L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongBang) {
            this.dame += this.calPercent(this.dame, 15L);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongSieuCap) {
            this.dame += this.calPercent(this.dame, 50L);
        }
    }

    private void setDef() {
        this.def = this.defg * 4;
        this.def += this.defAdd;
        //đồ
        for (Integer tl : this.tlDef) {
            this.tlGiamst += tl;
        }
        if (tlGiamst > 60) {
            tlGiamst = 60;
        }
        //ngọc rồng đen 5 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[4] > System.currentTimeMillis()) {
            this.def += calPercent(this.def, RewardBlackBall.R5S);
        }
        if (this.player.effectSkin.isInosuke) {
            this.def += calPercent(this.def, 50);
        }
        if (this.player.effectSkin.isInoHashi) {
            this.def += calPercent(this.def, 60);
        }
    }

    private void setCrit() {
        this.crit = this.critg;
        this.crit += this.critAdd;
        //ngọc rồng đen 4 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[3] > System.currentTimeMillis()) {
            this.crit += RewardBlackBall.R4S;
        }
        //biến khỉ
        if (this.player.effectSkill.isMonkey) {
            this.crit = 110;
        }
        if (player.getBuff() == Buff.BUFF_CRIT) {
            crit += 10;
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isBienhinh) {
            this.crit = 110;
        }
    }

    private void setCritDame() {
        if (this.player.effectSkin.isTanjiro) {
            this.tlDameCrit.add(30);
        }
        if (this.player.itemTime != null) {
            if (this.player.itemTime.isUseBanhChung) {
                this.tlDameCrit.add(15);
            }
        }
    }

    private void setSpeed() {
        for (Integer tl : this.tlSpeed) {
            this.speed += calPercent(this.speed, tl);
        }
        if (this.player.effectSkin.isSlow) {
            this.speed = 1;
        }
    }

    private void resetPoint() {
        this.hpAdd = 0;
        this.mpAdd = 0;
        this.dameAdd = 0;
        this.defAdd = 0;
        this.critAdd = 0;
        this.tlHp.clear();
        this.tlMp.clear();
        this.tlDef.clear();
        this.tlDame.clear();
        this.tlDameAttMob.clear();
        this.tlDameCrit.clear();
        this.tlHpHoiBanThanVaDongDoi = 0;
        this.tlMpHoiBanThanVaDongDoi = 0;
        this.hpHoi = 0;
        this.mpHoi = 0;
        this.mpHoiCute = 0;
        this.tlHpHoi = 0;
        this.tlMpHoi = 0;
        this.tlHutHp = 0;
        this.tlHutMp = 0;
        this.tlHutHpMob = 0;
        this.tlHutHpMpXQ = 0;
        this.tlPST = 0;
        this.tlTNSM.clear();
        this.tlDameAttMob.clear();
        this.tlDameCrit.clear();
        this.tlGold = 0;
        this.tlNeDon = 0;
        this.tlSDDep.clear();
        this.tlSubSD = 0;
        this.tlHpGiamODo = 0;
        this.teleport = false;
        this.tlSpeed.clear();
        this.speed = 5;
        this.mstChuong = 0;
        this.tlGiamst = 0;
        this.tlTNSMPet = 0;

        this.wearingVoHinh = false;
        this.isKhongLanh = false;
        this.wearingDrabula = false;
        this.wearingNezuko = false;
        this.wearingZenitsu = false;
        this.wearingInosuke = false;
        this.wearingInoHashi = false;
        this.wearingTanjiro = false;
        this.wearingMabu = false;
        this.wearingBuiBui = false;
        this.xDameChuong = false;
        this.wearingYacon = false;
    }

    public void addHp(long hp) {
        this.hp += hp;
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
        if (this.hp < 0) {
            this.hp = 0;
        }
        this.hp = Math.max(INT_MIN_VALUE, Math.min(INT_MAX_VALUE, this.hp));
    }

    public void addMp(long mp) {
        this.mp += mp;
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
        if (this.mp < 0) {
            this.mp = 0;
        }
        this.mp = Math.max(INT_MIN_VALUE, Math.min(INT_MAX_VALUE, this.mp));
    }

    public void setHp(long hp) {
        if (hp > this.hpMax) {
            this.hp = this.hpMax;
        } else {
            this.hp = (long) hp;
        }
        if (this.hp < 0) {
            this.hp = 0;
        }
        this.hp = Math.max(INT_MIN_VALUE, Math.min(INT_MAX_VALUE, this.hp));
    }

    public void setMp(long mp) {
        if (mp > this.mpMax) {
            this.mp = this.mpMax;
        } else {
            this.mp = (long) mp;
        }
        if (this.mp < 0) {
            this.mp = 0;
        }
        this.mp = Math.max(INT_MIN_VALUE, Math.min(INT_MAX_VALUE, this.mp));
    }

    private void setIsCrit() {
        if (intrinsic != null && intrinsic.id == 25
                && this.getCurrPercentHP() <= intrinsic.param1) {
            isCrit = true;
        } else if (isCrit100) {
            isCrit100 = false;
            isCrit = true;
        } else {
            isCrit = Util.isTrue(this.crit, ConstRatio.PER100);
        }
    }

    public long getDameAttack(boolean isAttackMob) {
        setIsCrit();
        long dameAttack = this.dame;
        intrinsic = this.player.playerIntrinsic.intrinsic;
        percentDameIntrinsic = 0;
        long percentDameSkill = 0;
        long percentXDame = 0;
        Skill skillSelect = player.playerSkill.skillSelect;
        switch (skillSelect.template.id) {
            case Skill.DRAGON:
                if (intrinsic.id == 1) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.KAMEJOKO:
                if (intrinsic.id == 2) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.songoku == 5) {
                    percentXDame = 80;
                }
//                if (this.player.effectSkin.xDameChuong) {
//                    percentXDame += tlDameChuong;
//                    this.player.effectSkin.xDameChuong = false;
//                }
                break;
            case Skill.GALICK:
                if (intrinsic.id == 16) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kakarot == 5) {
                    percentXDame = 100;
                }
                break;
            case Skill.ANTOMIC:
                if (intrinsic.id == 17) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
//                if (this.player.effectSkin.xDameChuong) {
//                    percentXDame += tlDameChuong;
//                    this.player.effectSkin.xDameChuong = false;
//                }
                break;
            case Skill.DEMON:
                if (intrinsic.id == 8) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.MASENKO:
                if (intrinsic.id == 9) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
//                if (this.player.effectSkin.xDameChuong) {
//                    percentXDame += tlDameChuong * 100;
//                    this.player.effectSkin.xDameChuong = false;
//                }
                break;
            case Skill.KAIOKEN:
                if (intrinsic.id == 26) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.thienXinHang == 5) {
                    percentXDame = 100;
                }
                break;
            case Skill.LIEN_HOAN:
                if (intrinsic.id == 13) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.ocTieu == 5) {
                    percentXDame = 100;
                }
                break;
            case Skill.DICH_CHUYEN_TUC_THOI:
                dameAttack *= 2;
                dameAttack = Util.nextdame((long) (dameAttack - calPercent(dameAttack, 5)),
                        (long) (dameAttack + calPercent(dameAttack, 5)));
                return dameAttack;
            case Skill.MAKANKOSAPPO:
                percentDameSkill = skillSelect.damage;
                long dameSkill = calPercent(this.mpMax, percentDameSkill);
                return dameSkill;
            case Skill.QUA_CAU_KENH_KHI:
                long totalHP = 0;
                if (player.zone != null) {
                    totalHP = player.zone.getTotalHP();
                }
                long damage = ((totalHP / 10) + (this.dame * 10));
                if (this.player.setClothes.kirin == 5) {
                    damage *= 3;
                }
                return damage;
        }
        if (intrinsic.id == 18 && this.player.effectSkill.isMonkey) {
            percentDameIntrinsic = intrinsic.param1;
        }
        if (percentDameSkill != 0) {
            dameAttack = calPercent(dameAttack, percentDameSkill);
        }
        dameAttack += calPercent(dameAttack, percentDameIntrinsic);
        dameAttack += calPercent(dameAttack, dameAfter);

        if (isAttackMob) {
            for (Integer tl : this.tlDameAttMob) {
                dameAttack += calPercent(dameAttack, tl);
            }
        }
        dameAfter = 0;
        if (this.player.isPet && ((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
            dameAttack *= 2;
        }
        if (isCrit) {
            dameAttack *= 2;
            for (Integer tl : this.tlDameCrit) {
                dameAttack += calPercent(dameAttack, tl);
            }
        }
        dameAttack += calPercent((long) dameAttack, (long) percentXDame);
//        System.out.println(dameAttack);
        dameAttack = Util.nextdame((long) (dameAttack - calPercent(dameAttack, 5)), (long) (dameAttack + calPercent(dameAttack, 5)));

        if (player.isPl()) {
            if (player.inventory.haveOption(player.inventory.itemsBody, 5, 159)) {
                if (Util.canDoWithTime(player.lastTimeUseOption, 60000) && (player.playerSkill.skillSelect.template.id == Skill.KAMEJOKO || player.playerSkill.skillSelect.template.id == Skill.ANTOMIC || player.playerSkill.skillSelect.template.id == Skill.MASENKO)) {
                    dameAttack *= player.inventory.getParam(player.inventory.itemsBody.get(5), 159);
                    player.lastTimeUseOption = System.currentTimeMillis();
                    Service.getInstance().sendThongBao(player, "|1|Bạn vừa gây ra x" + player.inventory.getParam(player.inventory.itemsBody.get(5), 159) + " Sát thương Chưởng");
                }
            }
        }

        //check activation set
        return dameAttack;
    }

    public long getDameAttackSkillNotFocus() {
        setIsCrit();
        long dameAttack = this.dame;
        intrinsic = this.player.playerIntrinsic.intrinsic;
        percentDameIntrinsic = 0;
        int percentDameSkill = 0;
        int percentXDame = 0;
        Skill skillSelect = player.playerSkill.skillSelect;
        switch (skillSelect.template.id) {

        }
        if (intrinsic.id == 18 && this.player.effectSkill.isMonkey) {
            percentDameIntrinsic = intrinsic.param1;
        }
        if (percentDameSkill != 0) {
            dameAttack = calPercent(dameAttack, percentDameSkill);
        }
        dameAttack += calPercent(dameAttack, percentDameIntrinsic);
        dameAttack += calPercent(dameAttack, dameAfter);
        dameAfter = 0;
        if (this.player.isPet && ((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
            dameAttack *= 2;
        }
        if (isCrit) {
            dameAttack *= 2;
            for (Integer tl : this.tlDameCrit) {
                dameAttack += calPercent(dameAttack, tl);
            }
        }
        dameAttack += calPercent(dameAttack, percentXDame);
        dameAttack = Util.nextdame((long) (dameAttack - calPercent(dameAttack, 5)), (long) (dameAttack + calPercent(dameAttack, 5)));
        return (long) dameAttack;
    }

    public long getCurrPercentHP() {
        if (this.hpMax == 0) {
            return 100;
        }
        return this.hp * 100 / this.hpMax;
    }

    public long getCurrPercentMP() {
        return this.mp * 100 / this.mpMax;
    }

    public void setFullHpMp() {
        this.hp = this.hpMax;
        this.mp = this.mpMax;
    }

    public void subHP(long sub) {
        this.hp -= sub;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public void subMP(long sub) {
        this.mp -= sub;
        if (this.mp < 0) {
            this.mp = 0;
        }
    }

    public long calSucManhTiemNang(long tiemNang) {
        if (power < getPowerLimit()) {
            for (Integer tl : this.tlTNSM) {
                tiemNang += calPercent(tiemNang, tl);
            }
            if (this.player.cFlag != 0) {
                if (this.player.cFlag == 8) {
                    tiemNang += calPercent(tiemNang, 10);
                } else {
                    tiemNang += calPercent(tiemNang, 5);
                }
            }
            if (buffExpSatellite) {
                tiemNang += calPercent(tiemNang, 20);
            }
            if (player.isPet) {
                Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.TNSM);
                if (at != null && !at.isExpired()) {
                    tiemNang += calPercent(tiemNang, at.getValue());
                }
            }
            if (this.player.isPet) {
                int tltnsm = ((Pet) this.player).master.nPoint.tlTNSMPet;
                if (tltnsm > 0) {
                    tiemNang += calPercent(tiemNang, tltnsm);
                }
            }
            long tn = tiemNang;
            if (this.player.charms.tdTriTue > System.currentTimeMillis()) {
                tiemNang += tn;
            }
            if (this.player.charms.tdTriTue3 > System.currentTimeMillis()) {
                tiemNang += tn * 2;
            }
            if (this.player.charms.tdTriTue4 > System.currentTimeMillis()) {
                tiemNang += tn * 3;
            }
            if (!this.player.isPet && this.player.itemTimesieucap.isDuoikhi
                    || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDuoikhi) {
                tiemNang += tn * 3;
            }
            if (!this.player.isPet && this.player.itemTimesieucap.isKeo
                    || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isKeo) {
                tiemNang += tn * 2;
            }
            if (this.intrinsic != null && this.intrinsic.id == 24) {
                tiemNang += calPercent(tiemNang, this.intrinsic.param1);
            }
            if (this.power >= 60000000000L) {
                tiemNang -= calPercent(tiemNang, 70);
            }
            if (this.player.isPet) {
                if (((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
                    tiemNang += tn * 2;
                }
            }
            tiemNang *= Manager.RATE_EXP_SERVER;
            tiemNang = calSubTNSM(tiemNang);
            if (tiemNang <= 0) {
                tiemNang = 1;
            }
        } else {
            tiemNang = 10;
        }
        return tiemNang;
    }

    public long calSubTNSM(long tiemNang) {
        if (power >= 350000000000L) {
            tiemNang -= calPercent(tiemNang, 98);
        } else if (power >= 210000000000L) {
            tiemNang -= calPercent(tiemNang, 95);
        } else if (power >= 110000000000L) {
            tiemNang -= calPercent(tiemNang, 90);
        } else if (power >= 100000000000L) {
            tiemNang -= calPercent(tiemNang, 85);
        } else if (power >= 90000000000L) {
            tiemNang -= calPercent(tiemNang, 80);
        } else if (power >= 80000000000L) {
            tiemNang -= calPercent(tiemNang, 75);
        }
        if (tiemNang > 10000000000L) {
            tiemNang = 10000000000L;
        }
        return tiemNang;
    }

    public short getTileHutHp(boolean isMob) {
        if (isMob) {
            return (short) (this.tlHutHp + this.tlHutHpMob);
        } else {
            return this.tlHutHp;
        }
    }

    public short getTiLeHutMp() {
        return this.tlHutMp;
    }

    public long subDameInjureWithDeff(long dame) {
        long def = this.def;
        dame -= def;
        if (this.player.itemTime.isUseGiapXen) {
            dame /= 2;
        }
        if (this.player.itemTime.isUseGiapXen2) {
            dame -= calPercent(dame, 60);
        }
        if (dame < 0) {
            dame = 1;
        }
        return dame;
    }

    /*------------------------------------------------------------------------*/
    public boolean canOpenPower() {
        return this.power >= getPowerLimit();
    }

    public long getPowerLimit() {
        if (powerLimit != null) {
            return powerLimit.getPower();
        }
        return 0;
    }

    public long getPowerNextLimit() {
        PowerLimit powerLimit = PowerLimitManager.getInstance().get(limitPower + 1);
        if (powerLimit != null) {
            return powerLimit.getPower();
        }
        return 0;
    }

    //**************************************************************************
    //POWER - TIEM NANG
    public void powerUp(long power) {
        this.power += power;
        TaskService.gI().checkDoneTaskPower(player, this.power);
    }

    public void tiemNangUp(long tiemNang) {
        this.tiemNang += tiemNang;
    }

    public void increasePoint(byte type, short point) {
        if (powerLimit == null) {
            return;
        }
        if (point <= 0) {
            return;
        }
        boolean updatePoint = false;
        double tiemNangUse = 0;
        if (type == 0) {
            int pointHp = point * 20;
            tiemNangUse = point * (2 * (this.hpg + 1000) + pointHp - 20) / 2;
            if ((this.hpg + pointHp) <= powerLimit.getHp()) {
                if (doUseTiemNang(tiemNangUse)) {
                    hpg += pointHp;
                    updatePoint = true;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 1) {
            int pointMp = point * 20;
            tiemNangUse = point * (2 * (this.mpg + 1000) + pointMp - 20) / 2;
            if ((this.mpg + pointMp) <= powerLimit.getMp()) {
                if (doUseTiemNang(tiemNangUse)) {
                    mpg += pointMp;
                    updatePoint = true;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 2) {
            tiemNangUse = point * (2 * this.dameg + point - 1) / 2 * 100;
            if ((this.dameg + point) <= powerLimit.getDamage()) {
                if (doUseTiemNang(tiemNangUse)) {
                    dameg += point;
                    updatePoint = true;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 3) {
            tiemNangUse = point * 2 * (this.defg + 5) / 2 * 100000;
            if ((this.defg + point) <= powerLimit.getDefense()) {
                if (doUseTiemNang(tiemNangUse)) {
                    defg += point;
                    updatePoint = true;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 4) {
            tiemNangUse = 50000000L;
            for (int i = 0; i < this.critg; i++) {
                tiemNangUse *= 5L;
            }
            if ((this.critg + point) <= powerLimit.getCritical()) {
                if (doUseTiemNang(tiemNangUse)) {
                    critg += point;
                    updatePoint = true;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (updatePoint) {
            Service.getInstance().point(player);
        }
    }

    private boolean doUseTiemNang(double tiemNang) {
        if (this.tiemNang < tiemNang) {
            Service.getInstance().sendThongBaoOK(player, "Bạn không đủ tiềm năng");
            return false;
        }
        if (this.tiemNang >= tiemNang) {
            this.tiemNang -= tiemNang;
            TaskService.gI().checkDoneTaskUseTiemNang(player);
            return true;
        }
        return false;
    }

    //--------------------------------------------------------------------------
    private long lastTimeHoiPhuc;
    private long lastTimeHoiStamina;

    public void update() {
        if (player != null && player.effectSkill != null) {
            if (player.effectSkill.isCharging && player.effectSkill.countCharging < 10) {
                long tiLeHoiPhuc = SkillUtil.getPercentCharge(player.playerSkill.skillSelect.point);
                if (player.effectSkill.isCharging && !player.isDie() && !player.effectSkill.isHaveEffectSkill()
                        && (hp < hpMax || mp < mpMax)) {
                    PlayerService.gI().hoiPhuc(player, calPercent(hpMax, tiLeHoiPhuc), calPercent(mpMax, tiLeHoiPhuc));
                    if (player.effectSkill.countCharging % 3 == 0) {
                        Service.getInstance().chat(player, "Phục hồi năng lượng " + Util.LongGioihan(getCurrPercentHP()) + "%");
                    }
                } else {
                    EffectSkillService.gI().stopCharge(player);
                }
                if (++player.effectSkill.countCharging >= 10) {
                    EffectSkillService.gI().stopCharge(player);
                }
            }
            if (Util.canDoWithTime(lastTimeHoiPhuc, 30000)) {
                PlayerService.gI().hoiPhuc(this.player, hpHoi, mpHoi);
                this.lastTimeHoiPhuc = System.currentTimeMillis();
            }
            if (Util.canDoWithTime(lastTimeHoiStamina, 60000) && this.stamina < this.maxStamina) {
                this.stamina++;
                this.lastTimeHoiStamina = System.currentTimeMillis();
                if (!this.player.isBoss && !this.player.isPet) {
                    PlayerService.gI().sendCurrentStamina(this.player);
                }
            }
        }
        //hồi phục 30s
        //hồi phục thể lực
    }

    private void setBasePoint() {
        setHpMax();
        setMpMax();
        setDame();
        setDef();
        setCrit();
        setHpHoi();
        setMpHoi();
        setNeDon();
        setCritDame();
        setSpeed();
        setAttributeOverLimit();
    }

    public void setAttributeOverLimit() {
        this.hpg = Math.max(1, Math.min(INT_MAX_VALUE, this.hpg));
        this.mpg = Math.max(1, Math.min(INT_MAX_VALUE, this.mpg));
        this.dameg = Math.max(1, Math.min(INT_MAX_VALUE, this.dameg));
        this.defg = Math.max(0, Math.min(INT_MAX_VALUE, this.defg));
        this.hpMax = Math.max(1, Math.min(INT_MAX_VALUE, this.hpMax));
        this.mpMax = Math.max(1, Math.min(INT_MAX_VALUE, this.mpMax));
        this.dame = Math.max(1, Math.min(INT_MAX_VALUE, this.dame));
        this.def = Math.max(0, Math.min(INT_MAX_VALUE, this.def));
        this.power = Math.max(0, Math.min(INT_MAX_VALUE, this.power));
        this.tiemNang = Math.max(0, Math.min(INT_MAX_VALUE, this.tiemNang));
        this.crit = Math.max(0, Math.min(110, this.crit));
        setHp();
        setMp();
    }

    public long calPercent(long param, long percent) {
        return param * percent / 100;
    }

    public void dispose() {
        this.intrinsic = null;
        this.player = null;
        this.tlHp = null;
        this.tlMp = null;
        this.tlDef = null;
        this.tlDame = null;
        this.tlDameAttMob = null;
        this.tlSDDep = null;
        this.tlTNSM = null;
        this.tlDameCrit = null;
        this.tlSpeed = null;
    }
}
