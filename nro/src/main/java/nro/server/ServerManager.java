//package nro.server;
//
//import nro.attr.AttributeManager;
//import nro.jdbc.DBService;
//import nro.jdbc.daos.AccountDAO;
//import nro.jdbc.daos.HistoryTransactionDAO;
//import nro.jdbc.daos.PlayerDAO;
//import nro.login.LoginSession;
//import nro.manager.ConsignManager;
//import nro.manager.TopManager;
//import nro.models.boss.BossFactory;
//import nro.models.boss.BossManager;
//import nro.models.map.challenge.MartialCongressManager;
//import nro.models.map.dungeon.DungeonManager;
//import nro.models.map.phoban.BanDoKhoBau;
//import nro.models.map.phoban.DoanhTrai;
//import nro.models.player.Player;
//import nro.netty.NettyServer;
//import nro.server.io.Session;
//import nro.services.ClanService;
//import nro.utils.Log;
//import nro.utils.TimeUtil;
//import nro.utils.Util;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.net.InetSocketAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Scanner;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import javax.swing.ImageIcon;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import nro.models.map.challenge.sieuhang.SieuHangManager;
//
//public class ServerManager {
//
//    public static String timeStart;
//
//    public static final Map CLIENTS = new HashMap();
//
//    public static String NAME = "";
//    public static int PORT = 14445;
//
//    private Controller controller;
//
//    private static ServerManager instance;
//
//    public static ServerSocket listenSocket;
//    public static boolean isRunning;
//
//    @Getter
//    private LoginSession login;
//    public static boolean updateTimeLogin;
//    @Getter
//    @Setter
//    private AttributeManager attributeManager;
//    private long lastUpdateAttribute;
//    @Getter
//    private DungeonManager dungeonManager;
//
//    public void init() {
//        Manager.gI();
//        HistoryTransactionDAO.deleteHistory();
//        BossFactory.initBoss();
//        this.controller = new Controller();
//        if (updateTimeLogin) {
//            AccountDAO.updateLastTimeLoginAllAccount();
//        }
//    }
//
//    public static ServerManager gI() {
//        if (instance == null) {
//            instance = new ServerManager();
//            instance.init();
//        }
//        return instance;
//    }
//
//    public static void main(String[] args) {
//        timeStart = TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss");
//        ServerManager.gI().run();
//    }
//
//    public void run() {
//        isRunning = true;
//
//        JFrame frame = new JFrame("NROKID");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        ImageIcon icon = new ImageIcon("");
//        frame.setIconImage(icon.getImage());
//        JPanel panel = new panel();
//        frame.add(panel);
//        frame.pack();
//        frame.setVisible(true);
//
//        activeCommandLine();
//        activeGame();
//        activeLogin();
//        autoTask();
//        (new AutoMaintenance(5, 0, 0)).start(); //thời gian bảo trì định kì
////        (new AutoMaintenance(12, 0, 0)).start(); //thời gian bảo trì định kì
//        NettyServer nettyServer = new NettyServer();
//        nettyServer.start();
//        activeServerSocket();
//
//    }
//
//    public void activeLogin() {
//        login = new LoginSession();
//        login.connect(Manager.loginHost, Manager.loginPort);
//    }
//
//    private void activeServerSocket() {
//        try {
//            Log.log("Start server......... Current thread: " + Thread.activeCount());
//            listenSocket = new ServerSocket(PORT);
//            while (isRunning) {
//                try {
//                    Socket sc = listenSocket.accept();
//                    String ip = (((InetSocketAddress) sc.getRemoteSocketAddress()).getAddress()).toString().replace("/", "");
//                    if (canConnectWithIp(ip)) {
//                        Session session = new Session(sc, controller, ip);
//                        session.ipAddress = ip;
//                    } else {
//                        sc.close();
//                    }
//                } catch (Exception e) {
////                        Logger.logException(ServerManager.class, e);
//                }
//            }
//            listenSocket.close();
//        } catch (Exception e) {
//            Log.error(ServerManager.class, e, "Lỗi mở port");
//            System.exit(0);
//        }
//    }
//
//    private boolean canConnectWithIp(String ipAddress) {
//        Object o = CLIENTS.get(ipAddress);
//        if (o == null) {
//            CLIENTS.put(ipAddress, 1);
//            return true;
//        } else {
//            int n = Integer.parseInt(String.valueOf(o));
//            if (n < Manager.MAX_PER_IP) {
//                n++;
//                CLIENTS.put(ipAddress, n);
//                return true;
//            } else {
//                return false;
//            }
//        }
//    }
//
//    public void disconnect(Session session) {
//        Object o = CLIENTS.get(session.ipAddress);
//        if (o != null) {
//            int n = Integer.parseInt(String.valueOf(o));
//            n--;
//            if (n < 0) {
//                n = 0;
//            }
//            CLIENTS.put(session.ipAddress, n);
//        }
//    }
//
//    private void activeCommandLine() {
//        new Thread(() -> {
//            Scanner sc = new Scanner(System.in);
//            while (true) {
//                String line = sc.nextLine();
//                if (line.equals("baotri")) {
//                    new Thread(() -> {
//                        Maintenance.gI().start(5);
//                    }).start();
//                } else if (line.equals("athread")) {
//                    ServerNotify.gI().notify("Debug server: " + Thread.activeCount());
//                } else if (line.equals("nplayer")) {
//                    Log.error("Player in game: " + Client.gI().getPlayers().size());
//                } else if (line.equals("a")) {
//                    new Thread(() -> {
//                        Client.gI().close();
//                    }).start();
//                }
//            }
//        }, "Active line").start();
//    }
//
//    private void activeGame() {
//        long delay = 500;
//        new Thread(() -> {
//            while (isRunning) {
//                long l1 = System.currentTimeMillis();
//                BossManager.gI().updateAllBoss();
//                long l2 = System.currentTimeMillis() - l1;
//                if (l2 < delay) {
//                    try {
//                        Thread.sleep(delay - l2);
//                    } catch (InterruptedException e) {
//                    }
//                }
//            }
//        }, "Update boss").start();
//        new Thread(() -> {
//            while (isRunning) {
//                long start = System.currentTimeMillis();
//                for (DoanhTrai dt : DoanhTrai.DOANH_TRAIS) {
//                    dt.update();
//                }
//                for (BanDoKhoBau bdkb : BanDoKhoBau.BAN_DO_KHO_BAUS) {
//                    bdkb.update();
//                }
//                long timeUpdate = System.currentTimeMillis() - start;
////                System.out.println("time update all boss: " + timeUpdate);
//                if (timeUpdate < delay) {
//                    try {
//                        Thread.sleep(delay - timeUpdate);
//                    } catch (InterruptedException e) {
//                    }
//                }
//            }
//        }, "Update pho ban").start();
//        new Thread(() -> {
//            while (isRunning) {
//                try {
//                    long start = System.currentTimeMillis();
//                    if (attributeManager != null) {
//                        attributeManager.update();
//                        if (Util.canDoWithTime(lastUpdateAttribute, 600000)) {
//                            Manager.gI().updateAttributeServer();
//                        }
//                    }
//                    long timeUpdate = System.currentTimeMillis() - start;
//                    if (timeUpdate < delay) {
//                        Thread.sleep(delay - timeUpdate);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "Update Attribute Server").start();
//        dungeonManager = new DungeonManager();
//        dungeonManager.start();
//        new Thread(dungeonManager, "Phó bản").start();
//        new Thread(() -> {
//            while (isRunning) {
//                try {
//                    long start = System.currentTimeMillis();
//                    MartialCongressManager.gI().update();
//                    SieuHangManager.gI().update();
//                    long timeUpdate = System.currentTimeMillis() - start;
//                    if (timeUpdate < delay) {
//                        Thread.sleep(delay - timeUpdate);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "Update dai hoi vo thuat").start();
//    }
//
//    public void close(long delay) {
//        try {
//            dungeonManager.shutdown();
//        } catch (Exception e) {
//            Log.error(ServerManager.class, e);
//        }
//        try {
//            Manager.gI().updateEventCount();
//        } catch (Exception e) {
//            Log.error(ServerManager.class, e);
//        }
//        try {
//            Manager.gI().updateAttributeServer();
//        } catch (Exception e) {
//            Log.error(ServerManager.class, e);
//        }
//        try {
//            Client.gI().close();
//        } catch (Exception e) {
//            Log.error(ServerManager.class, e);
//        }
//        try {
//            ClanService.gI().close();
//        } catch (Exception e) {
//            Log.error(ServerManager.class, e);
//        }
//        try {
//            ConsignManager.getInstance().close();
//        } catch (Exception e) {
//            Log.error(ServerManager.class, e);
//        }
//        Client.gI().close();
//        Log.success("SUCCESSFULLY MAINTENANCE!...................................");
//        System.exit(0);
//    }
//
//    public void saveAll(boolean updateTimeLogout) {
//        try {
//            List<Player> list = Client.gI().getPlayers();
//            Connection conn = DBService.gI().getConnectionForAutoSave();
//            for (Player player : list) {
//                try {
//                    PlayerDAO.updateTimeLogout = updateTimeLogout;
//                    PlayerDAO.updatePlayer(player, conn);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void autoTask() {
////        ScheduledExecutorService autoSave = Executors.newScheduledThreadPool(1);
////        autoSave.scheduleWithFixedDelay(() -> {
////            saveAll(false);
////        }, 300000, 300000, TimeUnit.MILLISECONDS);
//// load bảng xếp hạng npc vados
//        ScheduledExecutorService autoTopPower = Executors.newScheduledThreadPool(1);
//        autoTopPower.scheduleWithFixedDelay(() -> {
//            TopManager.getInstance().load();
//            TopManager.getInstance().load1();
//            TopManager.getInstance().load2();
//            TopManager.getInstance().load3();
//        }, 0, 300000, TimeUnit.MILLISECONDS);
//    }
//}

package nro.server;
import lombok.Getter;
import lombok.Setter;
import nro.attr.AttributeManager;
import nro.jdbc.daos.AccountDAO;
import nro.jdbc.daos.HistoryTransactionDAO;
import nro.login.LoginSession;
import nro.manager.ConsignManager;
import nro.manager.TopManager;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.map.challenge.MartialCongressManager;
import nro.models.map.challenge.sieuhang.SieuHangManager;
import nro.models.map.dungeon.DungeonManager;
import nro.models.map.phoban.BanDoKhoBau;
import nro.models.map.phoban.DoanhTrai;
import nro.netty.NettyServer;
import nro.server.io.Session;
import nro.services.ClanService;
import nro.utils.Log;
import nro.utils.TimeUtil;

import javax.swing.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerManager {

    public static String timeStart;
    public static final Map<String, Integer> CLIENTS = new ConcurrentHashMap<>();
    public static String NAME = "";
    public static int PORT = 14445;

    private Controller controller;
    private static ServerManager instance;

    public static ServerSocket listenSocket;
    public static boolean isRunning;

    @Getter
    private LoginSession login;
    public static boolean updateTimeLogin;
    @Getter
    @Setter
    private AttributeManager attributeManager;
    private long lastUpdateAttribute;
    @Getter
    private DungeonManager dungeonManager;

    public void init() {
        Manager.gI();
        HistoryTransactionDAO.deleteHistory();
        BossFactory.initBoss();
        this.controller = new Controller();
        if (updateTimeLogin) {
            AccountDAO.updateLastTimeLoginAllAccount();
        }
    }

    public static ServerManager gI() {
        if (instance == null) {
            instance = new ServerManager();
            instance.init();
        }
        return instance;
    }

    public static void main(String[] args) {
        timeStart = TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss");
        ServerManager.gI().run();
    }

    public void run() {
        isRunning = true;

        JFrame frame = new JFrame("NROKID");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("");
        frame.setIconImage(icon.getImage());
        JPanel panel = new panel();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        // Khởi động các dịch vụ
        activeCommandLine();
        activeGame();
        activeLogin();
        autoTask();
//        new AutoMaintenance(5, 0, 0).start(); // Bảo trì định kỳ

        // Khởi động Netty Server
        NettyServer nettyServer = new NettyServer();
        nettyServer.start();

        // Khởi động ServerSocket
        activeServerSocket();
    }

    private void activeLogin() {
        login = new LoginSession();
        login.connect(Manager.loginHost, Manager.loginPort);
    }

    private void activeServerSocket() {
        try {
            Log.log("Starting server... Current thread count: " + Thread.activeCount());
            listenSocket = new ServerSocket(PORT);
            while (isRunning) {
                try {
                    Socket sc = listenSocket.accept();
                    String ip = ((InetSocketAddress) sc.getRemoteSocketAddress()).getAddress().toString().replace("/", "");
                    if (canConnectWithIp(ip)) {
                        Session session = new Session(sc, controller, ip);
                        session.ipAddress = ip;
                    } else {
                        sc.close();
                    }
                } catch (Exception e) {
                    Log.error(ServerManager.class, e, "Error accepting connection");
                }
            }
            listenSocket.close();
        } catch (Exception e) {
            Log.error(ServerManager.class, e, "Error opening port");
            System.exit(0);
        }
    }

    private boolean canConnectWithIp(String ipAddress) {
        CLIENTS.compute(ipAddress, (key, value) -> value == null ? 1 : value + 1);
        return CLIENTS.get(ipAddress) <= Manager.MAX_PER_IP;
    }

    public void disconnect(Session session) {
        CLIENTS.computeIfPresent(session.ipAddress, (key, value) -> value > 0 ? value - 1 : 0);
    }

    private void activeCommandLine() {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String line = sc.nextLine();
                switch (line) {
                    case "baotri" -> new Thread(() -> Maintenance.gI().start(5)).start();
                    case "athread" -> ServerNotify.gI().notify("Debug server: " + Thread.activeCount());
                    case "nplayer" -> Log.error("Player in game: " + Client.gI().getPlayers().size());
                    case "a" -> new Thread(() -> Client.gI().close()).start();
                }
            }
        }, "Command Line Thread").start();
    }

    private void activeGame() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleWithFixedDelay(() -> {
            long start = System.currentTimeMillis();
            BossManager.gI().updateAllBoss();
            DoanhTrai.DOANH_TRAIS.forEach(DoanhTrai::update);
            BanDoKhoBau.BAN_DO_KHO_BAUS.forEach(BanDoKhoBau::update);
            MartialCongressManager.gI().update();
            SieuHangManager.gI().update();
            long timeTaken = System.currentTimeMillis() - start;
            if (timeTaken < 500) {
                try {
                    Thread.sleep(500 - timeTaken);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 500, TimeUnit.MILLISECONDS);

        dungeonManager = new DungeonManager();
        dungeonManager.start();
        new Thread(dungeonManager, "Dungeon Manager").start();
    }

    public void close(long delay) {
        try {
            dungeonManager.shutdown();
            Manager.gI().updateEventCount();
            Manager.gI().updateAttributeServer();
            Client.gI().close();
            ClanService.gI().close();
            ConsignManager.getInstance().close();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        Log.success("SUCCESSFULLY MAINTENANCE!...................................");
        System.exit(0);
    }

    public void autoTask() {
        ScheduledExecutorService autoTopPower = Executors.newScheduledThreadPool(1);
        autoTopPower.scheduleWithFixedDelay(() -> {
            TopManager.getInstance().load();
            TopManager.getInstance().load1();
            TopManager.getInstance().load2();
            TopManager.getInstance().load3();
        }, 0, 300000, TimeUnit.MILLISECONDS);
    }
}