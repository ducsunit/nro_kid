/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nro.services.func;

import java.util.ArrayList;
import java.util.List;

import nro.models.item.Item;
import nro.models.player.Player;
import nro.server.Client;
import nro.services.InventoryService;
import nro.services.Service;
import nro.utils.Util;

/**
 * @author DEV Ăn Trộm
 */
public class TaiXiu implements Runnable {

    public List<Player> danhSachNguoiChoi;
    public int goldTai;
    public int goldXiu;
    public boolean ketquaTai = false;
    public boolean ketquaXiu = false;
    public boolean ketquaTamhoa = false;

    public boolean baotri = false;
    public long lastTimeEnd;
    public List<Player> PlayersTai = new ArrayList<>();
    public List<Player> PlayersXiu = new ArrayList<>();
    private static TaiXiu instance;
    public int x, y, z;

    public static TaiXiu gI() {
        if (instance == null) {
            instance = new TaiXiu();
        }
        return instance;
    }

    public void addPlayerXiu(Player pl) {
        if (!PlayersXiu.equals(pl)) {
            PlayersXiu.add(pl);
        }
    }

    public void addPlayerTai(Player pl) {
        if (!PlayersTai.equals(pl)) {
            PlayersTai.add(pl);
        }
    }

    public void removePlayerXiu(Player pl) {
        if (PlayersXiu.equals(pl)) {
            PlayersXiu.remove(pl);
        }
    }

    public void removePlayerTai(Player pl) {
        if (PlayersTai.equals(pl)) {
            PlayersTai.remove(pl);
        }
    }

    

    @Override
    public void run() {
        while (true) {
            try {
                long timeLeft = (TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000;
                if (timeLeft <= 30 && timeLeft > 0) {
//                    int x, y, z;
                    // Thực hiện các hành động sau khi chờ 10 giây
//                    if (TaiXiu.gI().goldTai >= TaiXiu.gI().goldXiu) {
//                        if (Util.isTrue(60, 100)) {
//                            x = Util.nextInt(1, 2);
//                            y = Util.nextInt(1, 3);
//                            z = Util.nextInt(1, 3);
//                            TaiXiu.gI().x = x;
//                            TaiXiu.gI().y = y;
//                            TaiXiu.gI().z = z;
//                        } else {
//                            x = Util.nextInt(3, 6);
//                            y = Util.nextInt(4, 6);
//                            z = Util.nextInt(4, 6);
//                            TaiXiu.gI().x = x;
//                            TaiXiu.gI().y = y;
//                            TaiXiu.gI().z = z;
//                        }
//                    } else {
//                        if (Util.isTrue(60, 100)) {
//                            x = Util.nextInt(3, 6);
//                            y = Util.nextInt(4, 6);
//                            z = Util.nextInt(4, 6);
//                            TaiXiu.gI().x = x;
//                            TaiXiu.gI().y = y;
//                            TaiXiu.gI().z = z;
//                        } else {
//                            x = Util.nextInt(1, 2);
//                            y = Util.nextInt(1, 3);
//                            z = Util.nextInt(1, 3);
//                            TaiXiu.gI().x = x;
//                            TaiXiu.gI().y = y;
//                            TaiXiu.gI().z = z;
//                        }
//                    }
                    // Xác định kết quả xúc xắc
                    if (TaiXiu.gI().goldTai >= TaiXiu.gI().goldXiu) {
                        if (Util.isTrue(60, 100)) {
                            x = Util.nextInt(1, 2);
                            y = Util.nextInt(1, 3);
                            z = Util.nextInt(1, 3);
                        } else {
                            x = Util.nextInt(3, 6);
                            y = Util.nextInt(4, 6);
                            z = Util.nextInt(4, 6);
                        }
                    } else {
                        if (Util.isTrue(60, 100)) {
                            x = Util.nextInt(3, 6);
                            y = Util.nextInt(4, 6);
                            z = Util.nextInt(4, 6);
                        } else {
                            x = Util.nextInt(1, 2);
                            y = Util.nextInt(1, 3);
                            z = Util.nextInt(1, 3);
                        }
                    }

                    // Gán giá trị vào TaiXiu object
                    TaiXiu.gI().x = x;
                    TaiXiu.gI().y = y;
                    TaiXiu.gI().z = z;

                    // In giá trị xúc xắc trước khi công bố
                    System.out.println("Xúc xắc dự kiến: x = " + x + ", y = " + y + ", z = " + z);
                    // Đợi đến khi hết thời gian trước khi chính thức công bố
                    while ((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000 > 0) {
                        Thread.sleep(500); // Kiểm tra thời gian mỗi 0.5 giây
                    }
                    int tong = (x + y + z);

                    if (tong > 3 && tong < 11) {
                        ketquaTamhoa = false;
                        ketquaXiu = true;
                        ketquaTai = false;
                    }
                    if (tong > 10) {
                        ketquaTamhoa = false;
                        ketquaXiu = false;
                        ketquaTai = true;
                    }
                    if (x == y && y == z && z == x) {
                        ketquaTamhoa = true;
                        ketquaXiu = false;
                        ketquaTai = false;
                    }
                    if (ketquaTai) {
                        // tài xỉu bằng hồng ngọc
//                        if (!TaiXiu.gI().PlayersTai.isEmpty()) {
//                            for (int i = 0; i < PlayersTai.size(); i++) {
//                                Player pl = this.PlayersTai.get(i);
//                                if (pl != null && Client.gI().getPlayer(pl.name) != null) {
//                                    int goldC = pl.goldTai + pl.goldTai * 80 / 100;
//                                    Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
//                                            + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TÀI)\n\n|1|Bạn đã chiến thắng!!");
//                                    Service.getInstance().sendThongBao(pl, "Chúc mừng bạn đã dành chiến thắng và nhận được " + Util.format(goldC) + " Hồng ngọc");
//                                    pl.inventory.ruby += goldC;
//                                    Service.getInstance().sendMoney(pl);
//                                    InventoryService.gI().sendItemBags(pl);
//                                }
//                            }
//                        }
//                        for (int i = 0; i < PlayersXiu.size(); i++) {
//                            Player pl = this.PlayersXiu.get(i);
//                            if (pl != null && Client.gI().getPlayer(pl.name) != null) {
//                                Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
//                                        + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TÀI)\n\n|7|Trắng tay gòi, chơi lại đi!!!");
//                            }
//                        }
                        // tài xỉu bằng thỏi vàng
                        // Tài xỉu bằng thỏi vàng

                        if (!TaiXiu.gI().PlayersTai.isEmpty()) {
                            for (int i = 0; i < PlayersTai.size(); i++) {
                                Player pl = this.PlayersTai.get(i);
                                Item thoivang = null;
                                try {
                                    thoivang = InventoryService.gI().findItemBagByTemp(pl, 457); // Tìm Thỏi vàng
                                } catch (Exception ignored) {
                                }

                                if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                    int goldC = pl.goldTai + pl.goldTai * 80 / 100;
//                                    goldC = pl.goldTai + pl.goldTai * 80 / 100;

                                    // Thông báo cho người chơi
                                    Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : " + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TÀI)\n\n|1|Bạn đã chiến thắng!!");
//                                    Service.getInstance().sendThongBao(pl, "Chúc mừng bạn đã dành chiến thắng và nhận được " + Util.format(goldC) + " Thỏi vàng");
//                                    Service.getInstance().chatGlobal(pl, "Chúc mừng " + pl.name + " đã dành chiến thắng và nhận được " + Util.format(goldC) + " Thỏi vàng");
                                    Service.getInstance().HeThongChatGlobal("Chúc mừng " + pl.name + " đã dành chiến thắng và nhận được " + Util.format(goldC) + " Thỏi vàng");
                                    // Cộng thêm Thỏi vàng cho người chơi
                                    if (thoivang != null) {
                                        thoivang.quantity += goldC; // Thêm số Thỏi vàng vào kho đồ của người chơi
                                        InventoryService.gI().sendItemBags(pl);
                                    } else {
                                        // Nếu không tìm thấy Thỏi vàng, thông báo lỗi
                                        Service.getInstance().sendThongBao(pl, "Bạn không có Thỏi vàng để nhận phần thưởng.");
                                    }

                                    Service.getInstance().sendMoney(pl);
                                    InventoryService.gI().sendItemBags(pl);
                                }
                            }
                        }

                        // Thông báo cho người chơi Xiu thua
                        for (int i = 0; i < PlayersXiu.size(); i++) {
                            Player pl = this.PlayersXiu.get(i);
                            if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : " + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TÀI)\n\n|7|Trắng tay rồi, chơi lại đi!!!");
                                Service.getInstance().HeThongChatGlobal("HAHA " + pl.name + " đã thua " + Util.format(pl.goldXiu) + " Thỏi vàng");
                            }
                        }

                    } else if (ketquaXiu) {
                        // hồng ngọc
//                        if (!TaiXiu.gI().PlayersXiu.isEmpty()) {
//                            for (int i = 0; i < PlayersXiu.size(); i++) {
//                                Player pl = this.PlayersXiu.get(i);
//                                if (pl != null && Client.gI().getPlayer(pl.name) != null) {
//                                    int goldC = pl.goldXiu + pl.goldXiu * 80 / 100;
//                                    Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
//                                            + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(XỈU)\n\n|1|Bạn đã chiến thắng!!");
//                                    Service.getInstance().sendThongBao(pl, "Chúc mừng bạn đã dành chiến thắng và nhận được " + Util.format(goldC) + " Hồng ngọc");
//                                    pl.inventory.ruby += goldC;
//                                    Service.getInstance().sendMoney(pl);
//                                    InventoryService.gI().sendItemBags(pl);
//                                }
//                            }
//                        }
//                        for (int i = 0; i < PlayersTai.size(); i++) {
//                            Player pl = this.PlayersTai.get(i);
//                            if (pl != null && Client.gI().getPlayer(pl.name) != null) {
//                                Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
//                                        + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(XỈU)\n\n|7|Trắng tay gòi, chơi lại đi!!!");
//                            }
//                        }
                        // tài xỉu bằng thỏi vàng
                        if (!TaiXiu.gI().PlayersXiu.isEmpty()) {
                            for (int i = 0; i < PlayersXiu.size(); i++) {
                                Player pl = this.PlayersXiu.get(i);
                                Item thoivang = null;
                                try {
                                    thoivang = InventoryService.gI().findItemBagByTemp(pl, 457); // Tìm Thỏi vàng
                                } catch (Exception ignored) {
                                }

                                if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                    int goldC = pl.goldXiu + pl.goldXiu * 80 / 100;
//                                    goldC = pl.goldXiu + pl.goldXiu * 80 / 100;

                                    // Thông báo cho người chơi
                                    Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : " + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(XỈU)\n\n|1|Bạn đã chiến thắng!!");
//                                    Service.getInstance().sendThongBao(pl, "Chúc mừng bạn đã dành chiến thắng và nhận được " + Util.format(goldC) + " Thỏi vàng");
                                    Service.getInstance().HeThongChatGlobal("Chúc mừng " + pl.name + " đã dành chiến thắng và nhận được " + Util.format(goldC) + " Thỏi vàng");

                                    // Cộng thêm Thỏi vàng cho người chơi
                                    if (thoivang != null) {
                                        thoivang.quantity += goldC; // Thêm số Thỏi vàng vào kho đồ của người chơi
                                        InventoryService.gI().sendItemBags(pl);
                                    } else {
                                        // Nếu không tìm thấy Thỏi vàng, thông báo lỗi
                                        Service.getInstance().sendThongBao(pl, "Bạn không có Thỏi vàng để nhận phần thưởng.");
                                    }

                                    Service.getInstance().sendMoney(pl);
                                    InventoryService.gI().sendItemBags(pl);
                                }
                            }
                        }

                        // Thông báo cho người chơi Tai thua
                        for (int i = 0; i < PlayersTai.size(); i++) {
                            Player pl = this.PlayersTai.get(i);
                            if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : " + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(XỈU)\n\n|7|Trắng tay rồi, chơi lại đi!!!");
                                Service.getInstance().HeThongChatGlobal("HAHA " + pl.name + " đã thua " + Util.format(pl.goldTai) + " Thỏi vàng");
                            }
                        }

                    } else {
//                        for (int i = 0; i < PlayersTai.size(); i++) {
//                            Player pl = this.PlayersTai.get(i);
//                            if (pl != null && Client.gI().getPlayer(pl.name) != null) {
//                                Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
//                                        + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TAM HOA)\n\n|7|Hahaha Nhà cái lụm hết nha!!!");
//                            }
//                        }
//                        for (int i = 0; i < PlayersXiu.size(); i++) {
//                            Player pl = this.PlayersXiu.get(i);
//                            if (pl != null && Client.gI().getPlayer(pl.name) != null) {
//                                Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
//                                        + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TAM HOA)\n\n|7|Hahaha Nhà cái lụm hết nha!!!");
//                            }
//                        }
                        // Thông báo cho người chơi thua (TAM HOA)
                        for (int i = 0; i < PlayersTai.size(); i++) {
                            Player pl = this.PlayersTai.get(i);
                            if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : " + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TAM HOA)\n\n|7|Hahaha Nhà cái lụm hết nha!!!");
                                Service.getInstance().HeThongChatGlobal("HAHA " + pl.name + " đã thua " + Util.format(pl.goldTai) + " Thỏi vàng");
                            }
                        }

                        for (int i = 0; i < PlayersXiu.size(); i++) {
                            Player pl = this.PlayersXiu.get(i);
                            if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : " + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TAM HOA)\n\n|7|Hahaha Nhà cái lụm hết nha!!!");
                                Service.getInstance().HeThongChatGlobal("HAHA " + pl.name + " đã thua " + Util.format(pl.goldXiu) + " Thỏi vàng");
                            }
                        }
                    }
                    for (int i = 0; i < TaiXiu.gI().PlayersTai.size(); i++) {
                        Player pl = TaiXiu.gI().PlayersTai.get(i);
                        if (pl != null) {
                            pl.goldTai = 0;
                        }
                    }
                    for (int i = 0; i < TaiXiu.gI().PlayersXiu.size(); i++) {
                        Player pl = TaiXiu.gI().PlayersXiu.get(i);
                        if (pl != null) {
                            pl.goldXiu = 0;
                        }
                    }
                    ketquaXiu = false;
                    ketquaTai = false;
                    ketquaTamhoa = false;
                    TaiXiu.gI().goldTai = 0;
                    TaiXiu.gI().goldXiu = 0;
                    TaiXiu.gI().PlayersTai.clear();
                    TaiXiu.gI().PlayersXiu.clear();
//                    TaiXiu.gI().lastTimeEnd = System.currentTimeMillis() + 100000;
                    TaiXiu.gI().lastTimeEnd = System.currentTimeMillis() + 40000;
                }
                Thread.sleep(500);
            } catch (Exception ignored) {
            }
        }
    }
}
