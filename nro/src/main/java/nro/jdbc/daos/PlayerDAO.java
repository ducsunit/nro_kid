package nro.jdbc.daos;

import com.google.gson.Gson;
import nro.consts.ConstMap;
import nro.jdbc.DBService;
import nro.manager.AchiveManager;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.item.ItemTime;
import nro.models.player.*;
import nro.models.skill.Skill;
import nro.models.task.Achivement;
import nro.models.task.AchivementTemplate;
import nro.server.Manager;
import nro.services.MapService;
import nro.utils.Log;
import nro.utils.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import nro.models.item.ItemTimeSieuCap;

/**
 * @author DucSunIT
 * @copyright 💖 GirlkuN 💖
 */
public class PlayerDAO {

    public static boolean updateTimeLogout;

    public static void createNewPlayer(Connection con, int userId, String name, byte gender, int hair) {
        PreparedStatement ps = null;
        try {
            JSONArray dataInventory = new JSONArray();

            dataInventory.add(2_000_000_000);
            dataInventory.add(200_000_000);
            dataInventory.add(200_000_000);
            String inventory = dataInventory.toJSONString();

            JSONArray dataLocation = new JSONArray();
            dataLocation.add(100);
            dataLocation.add(384);
            dataLocation.add(39 + gender);
            String location = dataLocation.toJSONString();

            JSONArray dataPoint = new JSONArray();
            dataPoint.add(0); //giới hạn sức mạnh
            dataPoint.add(20_000_000); //sức mạnh
            dataPoint.add(500_000_000); //tiềm năng
            dataPoint.add(1000); //thể lực
            dataPoint.add(1000); //thể lực đầy
            dataPoint.add(gender == 0 ? 200 : 100); //hp gốc
            dataPoint.add(gender == 1 ? 200 : 100); //ki gốc
            dataPoint.add(gender == 2 ? 15 : 10); //sức đánh gốc
            dataPoint.add(0); //giáp gốc
            dataPoint.add(0); //chí mạng gốc
            dataPoint.add(0); //năng động
            dataPoint.add(gender == 0 ? 200 : 100); //hp hiện tại
            dataPoint.add(gender == 1 ? 200 : 100); //ki hiện tại
            String point = dataPoint.toJSONString();

            JSONArray dataMagicTree = new JSONArray();
            dataMagicTree.add(0);//isupgr
            dataMagicTree.add(new Date().getTime());
            dataMagicTree.add(10);//LV
            dataMagicTree.add(new Date().getTime());
            dataMagicTree.add(10);//curr_pea
            String magicTree = dataMagicTree.toJSONString();
            
            JSONObject checkActive = new JSONObject();
            checkActive.put("active", 1);
            /**
             *
             * [
             * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {"temp_id":"-1","option":[],"create_time":"0""}, ... ]
             */
            int idAo = gender == 0 ? 0 : gender == 1 ? 1 : 2;
            int idQuan = gender == 0 ? 6 : gender == 1 ? 7 : 8;
            int def = gender == 2 ? 3 : 2;
            int hp = gender == 0 ? 30 : 20;

            JSONArray dataBody = new JSONArray();
            for (int i = 0; i < 13; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                JSONArray option = new JSONArray();
                if (i == 0) {
                    option.add(47);
                    option.add(def);
                    options.add(option);
                    item.put("temp_id", idAo);
                    item.put("create_time", System.currentTimeMillis());
                    item.put("quantity", 1);
                } else if (i == 1) {
                    option.add(6);
                    option.add(hp);
                    options.add(option);
                    item.put("temp_id", idQuan);
                    item.put("create_time", System.currentTimeMillis());
                    item.put("quantity", 1);
                } else {
                    item.put("temp_id", -1);
                    item.put("create_time", 0);
                    item.put("quantity", 1);
                }
                item.put("option", options);
                dataBody.add(item);
            }
            String itemsBody = dataBody.toJSONString();

            JSONArray dataBag = new JSONArray();
            for (int i = 0; i < 20; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                JSONArray option = new JSONArray();
                if (i == 0) {
                    option.add(30);
                    option.add(1);
                    options.add(option);
                    item.put("temp_id", 457);
                    item.put("create_time", System.currentTimeMillis());
                    item.put("quantity", 5_000);
                } else {
                    item.put("temp_id", -1);
                    item.put("create_time", 0);
                    item.put("quantity", 1);
                }
                item.put("option", options);
                dataBag.add(item);
            }
            String itemsBag = dataBag.toJSONString();

            JSONArray dataBox = new JSONArray();
            for (int i = 0; i < 20; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                JSONArray option = new JSONArray();
                if (i == 0) {
                    item.put("temp_id", 12);
                    option.add(14);
                    option.add(1);
                    options.add(option);
                    item.put("create_time", System.currentTimeMillis());
                } else {
                    item.put("temp_id", -1);
                    item.put("create_time", 0);
                }
                item.put("option", options);
                item.put("quantity", 1);
                dataBox.add(item);
            }
            String itemsBox = dataBox.toJSONString();

            JSONArray dataLuckyRound = new JSONArray();
            for (int i = 0; i < 110; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                item.put("temp_id", -1);
                item.put("option", options);
                item.put("create_time", 0);
                item.put("quantity", 1);
                dataLuckyRound.add(item);
            }
            String itemsBoxLuckyRound = dataLuckyRound.toJSONString();

            String friends = "[]";
            String enemies = "[]";

            JSONArray dataIntrinsic = new JSONArray();
            dataIntrinsic.add(0);
            dataIntrinsic.add(0);
            dataIntrinsic.add(0);
            dataIntrinsic.add(0);
            String intrinsic = dataIntrinsic.toJSONString();

            JSONArray dataItemTime = new JSONArray();
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            dataItemTime.add(0);
            String itemTime = dataItemTime.toJSONString();

            JSONArray dataItemTimeSC = new JSONArray();
            dataItemTimeSC.add(0);
            dataItemTimeSC.add(0);
            dataItemTimeSC.add(0);
            dataItemTimeSC.add(0);
            dataItemTimeSC.add(0);
            dataItemTimeSC.add(0);
            dataItemTimeSC.add(0);
            dataItemTimeSC.add(0);
            dataItemTimeSC.add(0);
            dataItemTimeSC.add(0);
            dataItemTimeSC.add(0);
            dataItemTimeSC.add(0);
            String itemTimeSC = dataItemTimeSC.toJSONString();

            JSONArray dataTask = new JSONArray();
            dataTask.add(0);
            dataTask.add(0);
            dataTask.add(0);
            String task = dataTask.toJSONString();

            JSONArray dataAchive = new JSONArray();
            for (AchivementTemplate a : AchiveManager.getInstance().getList()) {
                JSONObject jobj = new JSONObject();
                jobj.put("id", a.getId());
                jobj.put("count", 0);
                jobj.put("finish", 0);
                jobj.put("receive", 0);
                dataAchive.add(jobj);
            }
            String achive = dataAchive.toJSONString();

            String mabuEgg = "{}";

            JSONArray dataCharms = new JSONArray();
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            String charms = dataCharms.toJSONString();

            int[] skillsArr = gender == 0 ? new int[]{0, 1, 6, 9, 10, 20, 22, 24, 19}
                    : gender == 1 ? new int[]{2, 3, 7, 11, 12, 17, 18, 26, 19}
                    : new int[]{4, 5, 8, 13, 14, 21, 23, 25, 19};
            //[{"temp_id":"4","point":0,"last_time_use":0},]

//            for (int i = 0; i < skillsArr.length; i++) {
//                dataObject.put("temp_id", skillsArr[i]);
//                if (i == 0) {
//                    dataObject.put("point", 1);
//                } else {
//                    dataObject.put("point", 0);
//                }
//                dataObject.put("last_time_use", 0);
//                dataArray.add(dataObject.toJSONString());
//                dataObject.clear();
//            }
//            String skills = dataArray.toJSONString();
//            dataArray.clear();
//
            JSONArray dataSkills = new JSONArray();
            for (int i = 0; i < skillsArr.length; i++) {
                JSONArray skill = new JSONArray();
                skill.add(skillsArr[i]);
                skill.add(0);
                if (i == 0) {
                    skill.add(1);
                } else if (i == 1) {
                    skill.add(1);
                } else if (i == 2) {
                    skill.add(1);
                } else if (i == 3) {
                    skill.add(1);
                } else if (i == 4) {
                    skill.add(1);
                } else {
                    skill.add(0);
                }
                dataSkills.add(skill);
            }
            String skills = dataSkills.toJSONString();

            JSONArray dataSkillShortcut = new JSONArray();
            dataSkillShortcut.add(gender == 0 ? 0 : gender == 1 ? 2 : 4);
            dataSkillShortcut.add(gender == 0 ? 1 : gender == 1 ? 3 : 5);
            dataSkillShortcut.add(gender == 0 ? 6 : gender == 1 ? 7 : 8);
            for (int i = 0; i < 7; i++) {
                dataSkillShortcut.add(-1);
            }
            String skillsShortcut = dataSkillShortcut.toJSONString();

            String petInfo = "{}";
            String petPoint = "{}";
            String petBody = "[]";
            String petSkill = "[]";

            JSONArray dataBlackBall = new JSONArray();
            for (int i = 1; i <= 7; i++) {
                JSONArray arr = new JSONArray();
                arr.add(0);
                arr.add(0);
                dataBlackBall.add(arr);
            }
            String blackBall = dataBlackBall.toJSONString();

            ps = con.prepareStatement("insert into player"
                    + "(account_id, name, head, gender, have_tennis_space_ship, clan_id_sv" + Manager.SERVER + ", "
                    + "data_inventory, data_location, data_point, data_magic_tree, items_body, "
                    + "items_bag, items_box, items_box_lucky_round, friends, enemies, data_intrinsic, data_item_time,"
                    + "data_task, data_mabu_egg, data_charm, skills, skills_shortcut, pet_info, pet_point, pet_body, pet_skill,"
                    + "data_black_ball, thoi_vang, data_side_task,achivements, data_item_time_sieucap) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setInt(3, hair);
            ps.setByte(4, gender);
            ps.setBoolean(5, false);
            ps.setInt(6, -1);
            ps.setString(7, inventory);
            ps.setString(8, location);
            ps.setString(9, point);
            ps.setString(10, magicTree);
            ps.setString(11, itemsBody);
            ps.setString(12, itemsBag);
            ps.setString(13, itemsBox);
            ps.setString(14, itemsBoxLuckyRound);
            ps.setString(15, friends);
            ps.setString(16, enemies);
            ps.setString(17, intrinsic);
            ps.setString(18, itemTime);
            ps.setString(19, task);
            ps.setString(20, mabuEgg);
            ps.setString(21, charms);
            ps.setString(22, skills);
            ps.setString(23, skillsShortcut);
            ps.setString(24, petInfo);
            ps.setString(25, petPoint);
            ps.setString(26, petBody);
            ps.setString(27, petSkill);
            ps.setString(28, blackBall);
            ps.setInt(29, 10); //gold bar
            ps.setString(30, "{}");
            ps.setString(31, achive);
            ps.setString(32, itemTimeSC);
            ps.executeUpdate();
//            Log.success("Tạo player mới thành công!");
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi tạo player mới");
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }

    }

    public static void updatePlayer(Player player, Connection connection) {
        if (player.isDisposed() || player.isSaving()) {
            return;
        }
        player.setSaving(true);
        try {
            int n1s = 0;
            int n2s = 0;
            int n3s = 0;
            int tv = 0;
            if (player.loaded) {
                long st = System.currentTimeMillis();
                try {

                    JSONArray dataInventory = new JSONArray();
                    //data kim lượng
                    dataInventory.add(player.inventory.gold);
                    dataInventory.add(player.inventory.gem);
                    dataInventory.add(player.inventory.ruby);
                    dataInventory.add(player.inventory.goldLimit);
                    String inventory = dataInventory.toJSONString();

                    int mapId = -1;
                    mapId = player.mapIdBeforeLogout;
                    int x = player.location.x;
                    int y = player.location.y;
                    long hp = player.nPoint.hp;
                    long mp = player.nPoint.mp;
                    if (player.isDie()) {
                        mapId = player.gender + 21;
                        x = 300;
                        y = 336;
                        hp = 1;
                        mp = 1;
                    } else {
                        if (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isMapBlackBallWar(mapId) || mapId == 126 || mapId == ConstMap.CON_DUONG_RAN_DOC
                                || mapId == ConstMap.CON_DUONG_RAN_DOC_142 || mapId == ConstMap.CON_DUONG_RAN_DOC_143 || mapId == ConstMap.HOANG_MAC) {
                            mapId = player.gender + 21;
                            x = 300;
                            y = 336;
                        }
                    }

                    //data vị trí
                    JSONArray dataLocation = new JSONArray();
                    dataLocation.add(x);
                    dataLocation.add(y);
                    dataLocation.add(mapId);
                    String location = dataLocation.toJSONString();
                    //data chỉ số
                    JSONArray dataPoint = new JSONArray();
                    dataPoint.add(player.nPoint.limitPower);
                    dataPoint.add(player.nPoint.power);
                    dataPoint.add(player.nPoint.tiemNang);
                    dataPoint.add(player.nPoint.stamina);
                    dataPoint.add(player.nPoint.maxStamina);
                    dataPoint.add(player.nPoint.hpg);
                    dataPoint.add(player.nPoint.mpg);
                    dataPoint.add(player.nPoint.dameg);
                    dataPoint.add(player.nPoint.defg);
                    dataPoint.add(player.nPoint.critg);
                    dataPoint.add(0);
                    dataPoint.add(hp);
                    dataPoint.add(mp);
                    String point = dataPoint.toJSONString();

                    //data đậu thần
                    JSONArray dataMagicTree = new JSONArray();
                    dataMagicTree.add(player.magicTree.isUpgrade ? 1 : 0);
                    dataMagicTree.add(player.magicTree.lastTimeUpgrade);
                    dataMagicTree.add(player.magicTree.level);
                    dataMagicTree.add(player.magicTree.lastTimeHarvest);
                    dataMagicTree.add(player.magicTree.currPeas);
                    String magicTree = dataMagicTree.toJSONString();

                    //data body
                    JSONArray dataBody = new JSONArray();
                    for (Item item : player.inventory.itemsBody) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);
                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataBody.add(dataItem);
                    }
                    String itemsBody = dataBody.toJSONString();

                    //data bag
                    JSONArray dataBag = new JSONArray();
                    for (Item item : player.inventory.itemsBag) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            JSONArray options = new JSONArray();
                            switch (item.template.id) {
                                case 14:
                                    n1s += item.quantity;
                                    break;
                                case 15:
                                    n2s += item.quantity;
                                    break;
                                case 16:
                                    n3s += item.quantity;
                                    break;
                                case 457:
                                    tv += item.quantity;
                                    break;
                            }
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);

                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataBag.add(dataItem);
                    }
                    String itemsBag = dataBag.toJSONString();

                    //data box
                    JSONArray dataBox = new JSONArray();
                    for (Item item : player.inventory.itemsBox) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            JSONArray options = new JSONArray();
                            switch (item.template.id) {
                                case 14:
                                    n1s += item.quantity;
                                    break;
                                case 15:
                                    n2s += item.quantity;
                                    break;
                                case 16:
                                    n3s += item.quantity;
                                    break;
                                case 457:
                                    tv += item.quantity;
                                    break;
                            }
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);

                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataBox.add(dataItem);
                    }
                    String itemsBox = dataBox.toJSONString();

                    //data box crack ball
                    JSONArray dataCrackBall = new JSONArray();
                    for (Item item : player.inventory.itemsBoxCrackBall) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);
                            JSONArray options = new JSONArray();
                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataCrackBall.add(dataItem);
                    }
                    String itemsBoxLuckyRound = dataCrackBall.toJSONString();

                    //data danh hiệu
                    JSONArray dataDanhhieu = new JSONArray();
                    dataDanhhieu.add(player.isTitleUse1 == true ? 1 : 0);
                    dataDanhhieu.add(player.lastTimeTitle1);
                    dataDanhhieu.add(player.IdDanhHieu_1);
                    dataDanhhieu.add(player.isTitleUse2 == true ? 1 : 0);
                    dataDanhhieu.add(player.lastTimeTitle2);
                    dataDanhhieu.add(player.IdDanhHieu_2);
                    dataDanhhieu.add(player.isTitleUse3 == true ? 1 : 0);
                    dataDanhhieu.add(player.lastTimeTitle3);
                    dataDanhhieu.add(player.IdDanhHieu_3);
                    dataDanhhieu.add(player.isTitleUse4 == true ? 1 : 0);
                    dataDanhhieu.add(player.lastTimeTitle4);
                    dataDanhhieu.add(player.IdDanhHieu_4);
                    dataDanhhieu.add(player.isTitleUse5 == true ? 1 : 0);
                    dataDanhhieu.add(player.lastTimeTitle5);
                    dataDanhhieu.add(player.IdDanhHieu_5);
                    String dhtime = dataDanhhieu.toJSONString();

                    //data limit
                    JSONArray datavatphamNgay = new JSONArray();
                    datavatphamNgay.add(player.bongtai);
                    datavatphamNgay.add(player.thiensu);
                    String ResetDay = datavatphamNgay.toJSONString();

                    //data bạn bè
                    JSONArray dataFriends = new JSONArray();
                    for (Friend f : player.friends) {
                        JSONObject friend = new JSONObject();
                        friend.put("id", f.id);
                        friend.put("name", f.name);
                        friend.put("power", f.power);
                        friend.put("head", f.head);
                        friend.put("body", f.body);
                        friend.put("leg", f.leg);
                        friend.put("bag", f.bag);
                        dataFriends.add(friend);
                    }
                    String friend = dataFriends.toJSONString();

                    //data kẻ thù
                    JSONArray dataEnemies = new JSONArray();
                    for (Friend e : player.enemies) {
                        JSONObject enemy = new JSONObject();
                        enemy.put("id", e.id);
                        enemy.put("name", e.name);
                        enemy.put("power", e.power);
                        enemy.put("head", e.head);
                        enemy.put("body", e.body);
                        enemy.put("leg", e.leg);
                        enemy.put("bag", e.bag);
                        dataEnemies.add(enemy);
                    }
                    String enemy = dataEnemies.toJSONString();

                    //data nội tại
                    JSONArray dataIntrinsic = new JSONArray();
                    dataIntrinsic.add(player.playerIntrinsic.intrinsic.id);
                    dataIntrinsic.add(player.playerIntrinsic.intrinsic.param1);
                    dataIntrinsic.add(player.playerIntrinsic.countOpen);
                    dataIntrinsic.add(player.playerIntrinsic.intrinsic.param2);
                    String intrinsic = dataIntrinsic.toJSONString();

                    //data item time
                    JSONArray dataItemTime = new JSONArray();
                    dataItemTime.add(player.itemTime.isUseBoKhi ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi)) : 0);
                    dataItemTime.add(player.itemTime.isUseAnDanh ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh)) : 0);
                    dataItemTime.add(player.itemTime.isOpenPower ? (ItemTime.TIME_OPEN_POWER - (System.currentTimeMillis() - player.itemTime.lastTimeOpenPower)) : 0);
                    dataItemTime.add(player.itemTime.isUseCuongNo ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo)) : 0);
                    dataItemTime.add(player.itemTime.isUseMayDo ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo)) : 0);
                    dataItemTime.add(player.itemTime.isUseBoHuyet ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet)) : 0);
                    dataItemTime.add(player.itemTime.iconMeal);
                    dataItemTime.add(player.itemTime.isEatMeal ? (ItemTime.TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal)) : 0);
                    dataItemTime.add(player.itemTime.isUseGiapXen ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen)) : 0);
                    dataItemTime.add(player.itemTime.isUseBanhChung ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhChung)) : 0);
                    dataItemTime.add(player.itemTime.isUseBanhTet ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTet)) : 0);

                    dataItemTime.add(player.itemTime.isUseBoKhi2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi2)) : 0);
                    dataItemTime.add(player.itemTime.isUseGiapXen2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen2)) : 0);
                    dataItemTime.add(player.itemTime.isUseCuongNo2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo2)) : 0);
                    dataItemTime.add(player.itemTime.isUseBoHuyet2 ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet2)) : 0);
                    String itemTime = dataItemTime.toJSONString();

                    //data item time SC
                    JSONArray dataItemTimeSC = new JSONArray();
                    dataItemTimeSC.add((player.itemTimesieucap.isDuoikhi ? (ItemTimeSieuCap.TIME_ITEM_SC_10P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeDuoikhi)) : 0));
                    dataItemTimeSC.add((player.itemTimesieucap.isDaNgucTu ? (ItemTimeSieuCap.TIME_ITEM_SC_10P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeDaNgucTu)) : 0));
                    dataItemTimeSC.add((player.itemTimesieucap.isUseCaRot ? (ItemTimeSieuCap.TIME_ITEM_SC_10P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeCaRot)) : 0));
                    dataItemTimeSC.add((player.itemTimesieucap.isKeo ? (ItemTimeSieuCap.TIME_ITEM_SC_30P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeKeo)) : 0));
                    dataItemTimeSC.add((player.itemTimesieucap.isUseXiMuoi ? (ItemTimeSieuCap.TIME_ITEM_SC_10P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeUseXiMuoi)) : 0));
                    dataItemTimeSC.add(player.itemTimesieucap.iconBanh);
                    dataItemTimeSC.add((player.itemTimesieucap.isUseTrungThu ? (ItemTimeSieuCap.TIME_TRUNGTHU - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeUseBanh)) : 0));
                    dataItemTimeSC.add((player.itemTimesieucap.isBienhinh ? (ItemTimeSieuCap.TIME_ITEM_SC_10P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeBienhinh)) : 0));
                    dataItemTimeSC.add((player.itemTimesieucap.isRongSieuCap ? (ItemTimeSieuCap.TIME_ITEM_SC_30P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeRongSieuCap)) : 0));
                    dataItemTimeSC.add((player.itemTimesieucap.isRongBang ? (ItemTimeSieuCap.TIME_ITEM_SC_30P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeRongBang)) : 0));
                    dataItemTimeSC.add((player.itemTimesieucap.isEatMeal ? (ItemTimeSieuCap.TIME_ITEM_SC_10P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeMeal)) : 0));
                    dataItemTimeSC.add(player.itemTimesieucap.iconMeal);
                    String itemTimesieucap = dataItemTimeSC.toJSONString();

                    //data nhiệm vụ
                    JSONArray dataTask = new JSONArray();
                    dataTask.add(player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count);
                    dataTask.add(player.playerTask.taskMain.id);
                    dataTask.add(player.playerTask.taskMain.index);
                    String task = dataTask.toJSONString();

                    //data nhiệm vụ hàng ngày
                    JSONArray dataSideTask = new JSONArray();
                    dataSideTask.add(player.playerTask.sideTask.level);
                    dataSideTask.add(player.playerTask.sideTask.count);
                    dataSideTask.add(player.playerTask.sideTask.leftTask);
                    dataSideTask.add(player.playerTask.sideTask.template != null ? player.playerTask.sideTask.template.id : -1);
                    dataSideTask.add(player.playerTask.sideTask.receivedTime);
                    dataSideTask.add(player.playerTask.sideTask.maxCount);
                    String sideTask = dataSideTask.toJSONString();

                    JSONArray dataAchive = new JSONArray();
                    for (Achivement a : player.playerTask.achivements) {
                        JSONObject jobj = new JSONObject();
                        jobj.put("id", a.getId());
                        jobj.put("count", a.getCount());
                        jobj.put("finish", a.isFinish() ? 1 : 0);
                        jobj.put("receive", a.isReceive() ? 1 : 0);
                        dataAchive.add(jobj);
                    }
                    String achive = dataAchive.toJSONString();

                    //data trứng bư
                    JSONObject dataMaBu = new JSONObject();
                    if (player.mabuEgg != null) {
                        dataMaBu.put("create_time", player.mabuEgg.lastTimeCreate);
                        dataMaBu.put("time_done", player.mabuEgg.timeDone);
                    }
                    String mabuEgg = dataMaBu.toJSONString();

                    //data bùa
                    JSONArray dataCharms = new JSONArray();
                    dataCharms.add(player.charms.tdTriTue);
                    dataCharms.add(player.charms.tdManhMe);
                    dataCharms.add(player.charms.tdDaTrau);
                    dataCharms.add(player.charms.tdOaiHung);
                    dataCharms.add(player.charms.tdBatTu);
                    dataCharms.add(player.charms.tdDeoDai);
                    dataCharms.add(player.charms.tdThuHut);
                    dataCharms.add(player.charms.tdDeTu);
                    dataCharms.add(player.charms.tdTriTue3);
                    dataCharms.add(player.charms.tdTriTue4);
                    dataCharms.add(player.charms.tdDeTuMabu);
                    String charm = dataCharms.toJSONString();

                    //data drop vàng ngọc
                    JSONArray dataVangNgoc = new JSONArray();
                    dataVangNgoc.add(player.vangnhat);
                    dataVangNgoc.add(player.hngocnhat);
                    String VangNgoc = dataVangNgoc.toJSONString();

                    //data mốc nạp
                    JSONArray dataMocNap = new JSONArray();
                    dataMocNap.add(player.mot);
                    dataMocNap.add(player.hai);
                    dataMocNap.add(player.ba);
                    dataMocNap.add(player.bon);
                    dataMocNap.add(player.nam);
                    String MocNap = dataMocNap.toJSONString();

                    //data mốc nạp
                    JSONArray dataMocNap2 = new JSONArray();
                    dataMocNap2.add(player.sau);
                    dataMocNap2.add(player.bay);
                    dataMocNap2.add(player.tam);
                    dataMocNap2.add(player.chin);
                    dataMocNap2.add(player.muoi);
                    String MocNap2 = dataMocNap2.toJSONString();

                    //data chuyển sinh
                    JSONArray dataChuyensinh = new JSONArray();
                    dataChuyensinh.add(player.chuyensinh);
                    dataChuyensinh.add(player.MaxGoldTradeDay);
                    dataChuyensinh.add(player.chuaco2);
                    dataChuyensinh.add(player.chuaco3);
                    dataChuyensinh.add(player.chuaco4);
                    String ChuyenSinh = dataChuyensinh.toJSONString();

                    //data số may mắn
                    JSONArray dataSoMayMan = new JSONArray();
                    for (int i : player.soMayMan) {
                        dataSoMayMan.add(i);
                    }
                    String somayman = dataSoMayMan.toJSONString();

                    //data siêu hạng
                    JSONArray dataSieuHang = new JSONArray();
                    dataSieuHang.add(player.rankSieuHang == 0 ? (player.rankSieuHang = (int) player.id) : player.rankSieuHang);
                    dataSieuHang.add(player.timesieuhang);
                    dataSieuHang.add(player.isnhanthuong1 == true ? 1 : 0);
                    dataSieuHang.add(player.nPoint.hpMax);
                    dataSieuHang.add(player.nPoint.mpMax);
                    dataSieuHang.add(player.nPoint.dame);
                    String SieuHang = dataSieuHang.toJSONString();

                    //data vị trí
                    JSONArray dataOff = new JSONArray();
                    dataOff.add(player.typetrain);
                    dataOff.add(player.istrain ? 1 : 0);
                    String Luyentap = dataOff.toJSONString();

                    //data skill
                    JSONArray dataSkills = new JSONArray();
                    for (Skill skill : player.playerSkill.skills) {
//                    if (skill.skillId != -1) {
                        JSONArray dataskill = new JSONArray();
                        dataskill.add(skill.template.id);
                        dataskill.add(skill.lastTimeUseThisSkill);
                        dataskill.add(skill.point);
                        dataskill.add(skill.currLevel);
//                    } else {
//                        dataObject.put("temp_id", -1);
//                        dataObject.put("point", 0);
//                        dataObject.put("last_time_use", 0);
//                    }
                        dataSkills.add(dataskill);
                    }
                    String skills = dataSkills.toJSONString();

                    JSONArray dataSkillShortcut = new JSONArray();
                    //data skill shortcut
                    for (int skillId : player.playerSkill.skillShortCut) {
                        dataSkillShortcut.add(skillId);
                    }
                    String skillShortcut = dataSkillShortcut.toJSONString();

                    JSONObject jPetInfo = new JSONObject();
                    JSONObject jPetPoint = new JSONObject();
                    JSONArray jPetBody = new JSONArray();
                    JSONArray jPetSkills = new JSONArray();
                    String petInfo = jPetInfo.toJSONString();
                    String petPoint = jPetPoint.toJSONString();
                    String petBody = jPetBody.toJSONString();
                    String petSkill = jPetSkills.toJSONString();

                    JSONArray dataChallenge = new JSONArray();
                    dataChallenge.add(player.goldChallenge);
                    dataChallenge.add(player.levelWoodChest);
                    dataChallenge.add(player.receivedWoodChest ? 1 : 0);
                    String challenge = dataChallenge.toJSONString();

                    JSONArray dataSuKienTet = new JSONArray();
                    dataSuKienTet.add(player.event.getTimeCookTetCake());
                    dataSuKienTet.add(player.event.getTimeCookChungCake());
                    dataSuKienTet.add(player.event.isCookingTetCake() ? 1 : 0);
                    dataSuKienTet.add(player.event.isCookingChungCake() ? 1 : 0);
                    dataSuKienTet.add(player.event.isReceivedLuckyMoney() ? 1 : 0);
                    String skTet = dataSuKienTet.toJSONString();

                    JSONArray dataBuyLimit = new JSONArray();
                    for (int i = 0; i < player.buyLimit.length; i++) {
                        dataBuyLimit.add(player.buyLimit[i]);
                    }
                    String buyLimit = dataBuyLimit.toJSONString();

                    JSONArray dataRwLimit = new JSONArray();
                    for (int i = 0; i < player.getRewardLimit().length; i++) {
                        dataRwLimit.add(player.getRewardLimit()[i]);
                    }
                    String rwLimit = dataRwLimit.toJSONString();

                    //data pet
                    if (player.pet != null) {
                        jPetInfo.put("name", player.pet.name);
                        jPetInfo.put("gender", player.pet.gender);
                        jPetInfo.put("is_mabu", player.pet.typePet);
                        jPetInfo.put("status", player.pet.status);
                        jPetInfo.put("type_fusion", player.fusion.typeFusion);
                        int timeLeftFusion = (int) (Fusion.TIME_FUSION - (System.currentTimeMillis() - player.fusion.lastTimeFusion));
                        jPetInfo.put("left_fusion", timeLeftFusion < 0 ? 0 : timeLeftFusion);
                        petInfo = jPetInfo.toJSONString();

                        jPetPoint.put("power", player.pet.nPoint.power);
                        jPetPoint.put("tiem_nang", player.pet.nPoint.tiemNang);
                        jPetPoint.put("stamina", player.pet.nPoint.stamina);
                        jPetPoint.put("max_stamina", player.pet.nPoint.maxStamina);
                        jPetPoint.put("hpg", player.pet.nPoint.hpg);
                        jPetPoint.put("mpg", player.pet.nPoint.mpg);
                        jPetPoint.put("damg", player.pet.nPoint.dameg);
                        jPetPoint.put("defg", player.pet.nPoint.defg);
                        jPetPoint.put("critg", player.pet.nPoint.critg);
                        jPetPoint.put("limit_power", player.pet.nPoint.limitPower);
                        jPetPoint.put("hp", player.pet.nPoint.hp);
                        jPetPoint.put("mp", player.pet.nPoint.mp);
                        petPoint = jPetPoint.toJSONString();

                        for (Item item : player.pet.inventory.itemsBody) {
                            JSONObject dataItem = new JSONObject();
                            if (item.isNotNullItem()) {
                                dataItem.put("temp_id", item.template.id);
                                dataItem.put("quantity", item.quantity);
                                dataItem.put("create_time", item.createTime);
                                JSONArray options = new JSONArray();
                                for (ItemOption io : item.itemOptions) {
                                    JSONArray option = new JSONArray();
                                    option.add(io.optionTemplate.id);
                                    option.add(io.param);
                                    options.add(option);
                                }
                                dataItem.put("option", options);
                            } else {
                                JSONArray options = new JSONArray();
                                dataItem.put("temp_id", -1);
                                dataItem.put("quantity", 0);
                                dataItem.put("create_time", 0);
                                dataItem.put("option", options);
                            }
                            jPetBody.add(dataItem);
                        }
                        petBody = jPetBody.toJSONString();

                        for (Skill s : player.pet.playerSkill.skills) {
                            JSONArray pskill = new JSONArray();
                            if (s.skillId != -1) {
                                pskill.add(s.template.id);
                                pskill.add(s.point);
                            } else {
                                pskill.add(-1);
                                pskill.add(0);
                            }
                            jPetSkills.add(pskill);
                        }
                        petSkill = jPetSkills.toJSONString();
                    }

                    JSONArray dataBlackBall = new JSONArray();
                    //data thưởng ngọc rồng đen
                    for (int i = 1; i <= 7; i++) {
                        JSONArray data = new JSONArray();
                        data.add(player.rewardBlackBall.timeOutOfDateReward[i - 1]);
                        data.add(player.rewardBlackBall.lastTimeGetReward[i - 1]);
                        dataBlackBall.add(data);
                    }
                    String blackBall = dataBlackBall.toJSONString();
                    Gson gson = new Gson();
                    PreparedStatement ps = null;
                    try {
                        ps = connection.prepareStatement("update player set head = ?, gender = ?, have_tennis_space_ship = ?,"
                                + "clan_id_sv" + Manager.SERVER + " = ?, data_inventory = ?, data_location = ?, data_point = ?, data_magic_tree = ?,"
                                + "items_body = ?, items_bag = ?, items_box = ?, items_box_lucky_round = ?, friends = ?,"
                                + "enemies = ?, data_intrinsic = ?, data_item_time = ?, data_task = ?, data_mabu_egg = ?,"
                                + "pet_info = ?, pet_point = ?, pet_body = ?, pet_skill = ? , power = ?, pet_power = ?, "
                                + "data_black_ball = ?, data_side_task = ?, data_charm = ?, skills = ?, skills_shortcut = ?,"
                                + "thoi_vang = ?, 1sao = ?, 2sao = ?, 3sao = ?, collection_book = ?, event_point = ?, firstTimeLogin = ?,"
                                + " challenge = ?, sk_tet = ?, buy_limit = ?, moc_nap = ?,achivements = ? , reward_limit = ?, drop_vang_ngoc = ?"
                                + ", nhan_moc_nap = ?, data_item_time_sieucap = ?, chuyen_sinh = ?, tong_nap = ?, danh_hieu = ?"
                                + ", rank_sieu_hang = ?, so_may_man = ?, data_offtrain = ?, reset_ngay = ?, nhan_moc_nap2 = ? where id = ?");

                        ps.setShort(1, player.head);
                        ps.setInt(2, player.gender);
                        ps.setBoolean(3, player.haveTennisSpaceShip);
                        ps.setShort(4, (short) (player.clan != null ? player.clan.id : -1));
                        ps.setString(5, inventory);
                        ps.setString(6, location);
                        ps.setString(7, point);
                        ps.setString(8, magicTree);
                        ps.setString(9, itemsBody);
                        ps.setString(10, itemsBag);
                        ps.setString(11, itemsBox);
                        ps.setString(12, itemsBoxLuckyRound);
                        ps.setString(13, friend);
                        ps.setString(14, enemy);
                        ps.setString(15, intrinsic);
                        ps.setString(16, itemTime);
                        ps.setString(17, task);
                        ps.setString(18, mabuEgg);
                        ps.setString(19, petInfo);
                        ps.setString(20, petPoint);
                        ps.setString(21, petBody);
                        ps.setString(22, petSkill);
                        ps.setLong(23, player.nPoint.power);
                        ps.setLong(24, player.pet != null ? player.pet.nPoint.power : 0);
                        ps.setString(25, blackBall);
                        ps.setString(26, sideTask);
                        ps.setString(27, charm);
                        ps.setString(28, skills);
                        ps.setString(29, skillShortcut);
                        ps.setInt(30, tv);
                        ps.setInt(31, n1s);
                        ps.setInt(32, n2s);
                        ps.setInt(33, n3s);
                        ps.setString(34, gson.toJson(player.getCollectionBook().getCards()));
                        ps.setInt(35, player.event.getEventPoint());
                        ps.setString(36, Util.toDateString(player.firstTimeLogin));
                        ps.setString(37, challenge);
                        ps.setString(38, skTet);
                        ps.setString(39, buyLimit);
                        ps.setInt(40, player.event.getMocNapDaNhan());
                        ps.setString(41, achive);
                        ps.setString(42, rwLimit);
                        ps.setString(43, VangNgoc);
                        ps.setString(44, MocNap);
                        ps.setString(45, itemTimesieucap);
                        ps.setString(46, ChuyenSinh);
                        ps.setInt(47, player.tongnap);
                        ps.setString(48, dhtime);
                        ps.setString(49, SieuHang);
                        ps.setString(50, somayman);
                        ps.setString(51, Luyentap);
                        ps.setString(52, ResetDay);
                        ps.setString(53, MocNap2);
                        ps.setInt(54, (int) player.id);
                        ps.executeUpdate();
                        // ServerLogSavePlayer.gI().add(ps.toString());
//                        Log.success("Total time save player " + player.name + " thành công! " + (System.currentTimeMillis() - st));
                        if (updateTimeLogout) {
                            AccountDAO.updateAccoutLogout(player.getSession());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            ps.close();
                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e) {
                    Log.error(PlayerDAO.class, e, "Lỗi save player " + player.name);
                } finally {

                }
            }
        } finally {
            player.setSaving(false);
        }
    }

    public static void saveName(Player player) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update player set name = ? where id = ?");
            ps.setString(1, player.name);
            ps.setInt(2, (int) player.id);
            ps.executeUpdate();
        } catch (Exception e) {
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    public static boolean isExistName(String name) {
        boolean exist = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGame();) {
            ps = con.prepareStatement("select * from player where name = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return exist;
    }

    public static boolean subVnd(Player player, int vnd) {
        if (vnd <= 0) {
            return false; // Giá trị `num` không hợp lệ.
        }
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update account set vnd = (vnd - ?) where id = ?");
            ps.setInt(1, vnd);
            ps.setInt(2, player.getSession().userId);
//            ps.executeUpdate();
            int check = ps.executeUpdate();
            if (check == 0) {
                return false; // Không cập nhật bất kỳ hàng nào trong cơ sở dữ liệu.
            }
            player.getSession().vnd -= vnd; // Cập nhật số dư `vnd` của người chơi trong bộ nhớ.
            return true;
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update vnd " + player.name);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                Log.error(PlayerDAO.class, e, "Lỗi đóng kết nối cơ sở dữ liệu: " + player.name);
            }
        }
    }

    public static boolean subUpdateSieuHang(Player player, int hang) {
        if (hang <= 0) {
            return false; // Giá trị `num` không hợp lệ.
        }
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("UPDATE player SET rank_sieu_hang = JSON_REPLACE(rank_sieu_hang, '$[0]', ?) WHERE id = ?");
            ps.setInt(1, hang);
            ps.setInt(2, (int) player.id);
//            ps.executeUpdate();
            int check = ps.executeUpdate();
            if (check == 0) {
                return false; // Không cập nhật bất kỳ hàng nào trong cơ sở dữ liệu.
            }
            player.rankSieuHang = hang; // Cập nhật số dư `vnd` của người chơi trong bộ nhớ.
            return true;
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update rank sieu hang " + player.name);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                Log.error(PlayerDAO.class, e, "Lỗi đóng kết nối cơ sở dữ liệu: " + player.name);
            }
        }
    }

    public static boolean PhatThuongOfline(Player player, int ngoc) {
        if (ngoc <= 0) {
            return false; // Giá trị `num` không hợp lệ.
        }
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("UPDATE player SET data_inventory = JSON_REPLACE(data_inventory, '$[2]', ?) WHERE id = ?");
            ps.setInt(1, player.inventory.ruby + ngoc);
            ps.setInt(2, (int) player.id);
//            ps.executeUpdate();
            int check = ps.executeUpdate();
            if (check == 0) {
                return false; // Không cập nhật bất kỳ hàng nào trong cơ sở dữ liệu.
            }
            return true;
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update phat thuong " + player.name);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                Log.error(PlayerDAO.class, e, "Lỗi đóng kết nối cơ sở dữ liệu: " + player.name);
            }
        }
    }

    public static boolean XoaSoMayMan(Player player) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("UPDATE player SET so_may_man = ? WHERE id = ?");
            ps.setString(1, "[]");
            ps.setInt(2, (int) player.id);
//           ps.executeUpdate();
            int check = ps.executeUpdate();
            if (check == 0) {
                return false; // Không cập nhật bất kỳ hàng nào trong cơ sở dữ liệu.
            }
            return true;
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi XoaSoMayMan");
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                Log.error(PlayerDAO.class, e, "Lỗi đóng kết nối cơ sở dữ liệu");
            }
        }
    }

    public static void subRuby(Player player, int userId, int ruby) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update account set ruby = ruby - ? where id = ?");
            ps.setInt(1, ruby);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update ruby " + player.name);
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void subGoldBar(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update account set thoi_vang = (thoi_vang - ?) where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update thỏi vàng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void subActive(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update account set active = ? where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update active " + player.name);
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void addHistoryReceiveGoldBar(Player player, int goldBefore, int goldAfter,
            int goldBagBefore, int goldBagAfter, int goldBoxBefore, int goldBoxAfter) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("insert into history_receive_goldbar(player_id,player_name,gold_before_receive,"
                    + "gold_after_receive,gold_bag_before,gold_bag_after,gold_box_before,gold_box_after) values (?,?,?,?,?,?,?,?)");
            ps.setInt(1, (int) player.id);
            ps.setString(2, player.name);
            ps.setInt(3, goldBefore);
            ps.setInt(4, goldAfter);
            ps.setInt(5, goldBagBefore);
            ps.setInt(6, goldBagAfter);
            ps.setInt(7, goldBoxBefore);
            ps.setInt(8, goldBoxAfter);
            ps.executeUpdate();
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update thỏi vàng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    public static void updateItemReward(Player player) {
        String dataItemReward = "";
        for (Item item : player.getSession().itemsReward) {
            if (item.isNotNullItem()) {
                dataItemReward += "{" + item.template.id + ":" + item.quantity;
                if (!item.itemOptions.isEmpty()) {
                    dataItemReward += "|";
                    for (ItemOption io : item.itemOptions) {
                        dataItemReward += "[" + io.optionTemplate.id + ":" + io.param + "],";
                    }
                    dataItemReward = dataItemReward.substring(0, dataItemReward.length() - 1) + "};";
                }
            }
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("update account set reward = ? where id = ?");
            ps.setString(1, dataItemReward);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update phần thưởng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    public static void saveBag(Connection con, Player player) {
        if (player.loaded) {
            PreparedStatement ps = null;
            try {
                JSONArray dataBag = new JSONArray();
                for (Item item : player.inventory.itemsBag) {
                    JSONObject dataItem = new JSONObject();

                    if (item.isNotNullItem()) {
                        dataItem.put("temp_id", item.template.id);
                        dataItem.put("quantity", item.quantity);
                        dataItem.put("create_time", item.createTime);
                        JSONArray options = new JSONArray();
                        for (ItemOption io : item.itemOptions) {
                            JSONArray option = new JSONArray();
                            option.add(io.optionTemplate.id);
                            option.add(io.param);
                            options.add(option);
                        }
                        dataItem.put("option", options);
                    } else {
                        JSONArray options = new JSONArray();
                        dataItem.put("temp_id", -1);
                        dataItem.put("quantity", 0);
                        dataItem.put("create_time", 0);
                        dataItem.put("option", options);
                    }
                    dataBag.add(dataItem);
                }
                String itemsBag = dataBag.toJSONString();

                ps = con.prepareStatement("update player set items_bag = ? where id = ?");
                ps.setString(1, itemsBag);
                ps.setInt(2, (int) player.id);
                ps.executeUpdate();
                ps.close();
            } catch (Exception e) {
                Log.error(PlayerDAO.class, e, "Lỗi save bag player " + player.name);
            } finally {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
}
