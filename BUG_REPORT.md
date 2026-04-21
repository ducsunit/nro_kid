# Bug Report — nro_kid

> Phân tích từ kiểm tra đồng bộ server (Java) ↔ client (C#)  
> Ngày: 2026-04-21

---

## Mục lục

- [BUG-01 — NullPointerException khi tăng điểm pet](#bug-01--nullpointerexception-khi-tăng-điểm-pet)
- [BUG-02 — lastTimeLogout bị gán nhầm giá trị lastTimeLogin](#bug-02--lasttimelogout-bị-gán-nhầm-giá-trị-lasttimelogin)
- [BUG-03 — Packet -29/2 thiếu 1 byte](#bug-03--packet--292-thiếu-1-byte)
- [BUG-04 — requestRegister bỏ qua tham số version](#bug-04--requestregister-bỏ-qua-tham-số-version)
- [BUG-05 — Tên biến gem/ruby không khớp với luongKhoa/luong](#bug-05--tên-biến-gemruby-không-khớp-với-luongkhoaluong)
- [BUG-06 — Session timeout bị comment out](#bug-06--session-timeout-bị-comment-out)

---

## 🔴 Critical

---

### BUG-01 — NullPointerException khi tăng điểm pet

| | |
|---|---|
| **Mức độ** | 🔴 Critical |
| **Loại** | NullPointerException / server crash |
| **File** | `nro/src/main/java/nro/server/Controller.java` |
| **Hàm** | `messageSubCommand()` — `case 17` |

**Mô tả:**

`case 17` dùng để tăng điểm cho pet. Logic check `player.nPoint != null` nhưng sau đó lại gọi `player.pet.nPoint.increasePoint()`. Nếu `player.pet == null` (người chơi chưa có pet), server sẽ ném `NullPointerException` ngay lập tức.

**Code hiện tại (lỗi):**

```java
case 17:
    byte typee = _msg.reader().readByte();
    short pointt = _msg.reader().readShort();
    if (player != null && player.nPoint != null) {   // ← check sai đối tượng
        player.pet.nPoint.increasePoint(typee, pointt); // ← crash nếu pet == null
    }
    break;
```

**Sửa thành:**

```java
case 17:
    byte typee = _msg.reader().readByte();
    short pointt = _msg.reader().readShort();
    if (player != null && player.pet != null && player.pet.nPoint != null) {
        player.pet.nPoint.increasePoint(typee, pointt);
    }
    break;
```

---

### BUG-02 — lastTimeLogout bị gán nhầm giá trị lastTimeLogin

| | |
|---|---|
| **Mức độ** | 🔴 Critical |
| **Loại** | Data sai / lưu DB sai |
| **File** | `nro/src/main/java/nro/login/LoginController.java` |
| **Hàm** | `login()` — dòng ~75 |

**Mô tả:**

Khi server nhận thông tin login thành công từ login server, có một dòng gán nhầm biến: `session.lastTimeLogout = lastTimeLogin`. Biến bên phải là `lastTimeLogin` thay vì `lastTimeLogout`. Hệ quả là trường `lastTimeLogout` trong session luôn lưu thời điểm login, làm hỏng mọi tính năng tính toán dựa vào thời gian logout (offline train, phần thưởng login hàng ngày, v.v.).

**Code hiện tại (lỗi):**

```java
int userID       = ms.reader().readInt();
boolean isAdmin  = ms.reader().readBoolean();
boolean actived  = ms.reader().readBoolean();
int goldBar      = ms.reader().readInt();
long lastTimeLogin  = ms.reader().readLong();
long lastTimeLogout = ms.reader().readLong();
// ...
session.lastTimeLogout = lastTimeLogin;  // ← sai biến
```

**Sửa thành:**

```java
session.lastTimeLogout = lastTimeLogout;  // đúng biến
```

---

## 🟡 Medium

---

### BUG-03 — Packet -29/2 thiếu 1 byte

| | |
|---|---|
| **Mức độ** | 🟡 Medium |
| **Loại** | Desync đọc/ghi packet |
| **File server** | `nro/src/main/java/nro/data/DataGame.java` — `sendLinkIP()` |
| **File client** | `client/Assets/Scripts/Controller.cs` — `messageNotLogin()` case `2` |

**Mô tả:**

Server gửi packet `-29/2` (server IP list) với 1 byte cuối. Client mong đợi đọc 2 byte: `CanNapTien` và `AdminLink`. Byte thứ 2 không tồn tại trong packet nên client ném exception trong `catch`, cả 2 flag không được set đúng giá trị.

Hệ quả: `Panel.CanNapTien` mặc định `false` (người chơi không thể nạp tiền), `AdminLink` không được lưu.

**Code server hiện tại (thiếu):**

```java
// DataGame.java
msg.writer().writeUTF(LINK_IP_PORT + ",0,0");
msg.writer().writeByte(1);   // CanNapTien — chỉ có 1 byte
// AdminLink ← thiếu byte này
```

**Code client đang đọc:**

```csharp
// Controller.cs
string text = msg.reader().readUTF();
sbyte b2 = msg.reader().readByte();          // CanNapTien ← OK
sbyte b3 = msg.reader().readByte();          // AdminLink  ← exception vì hết data
Rms.saveRMSInt("AdminLink", b3);             // không bao giờ chạy đến đây
```

**Sửa server — thêm 1 byte:**

```java
msg.writer().writeUTF(LINK_IP_PORT + ",0,0");
msg.writer().writeByte(1);   // CanNapTien: 1 = cho phép nạp tiền
msg.writer().writeByte(0);   // AdminLink:  0 = bình thường, 1 = dùng link admin
```

---

### BUG-04 — requestRegister bỏ qua tham số version

| | |
|---|---|
| **Mức độ** | 🟡 Medium |
| **Loại** | Tham số bị bỏ qua / packet thiếu trường |
| **File** | `client/Assets/Scripts/Service.cs` — `requestRegister()` |

**Mô tả:**

Hàm `requestRegister` khai báo nhận tham số `string version` và được gọi từ `LoginScr.cs` với `GameMidlet.VERSION`. Tuy nhiên bên trong hàm không có lệnh `writeUTF(version)` nào — tham số bị bỏ qua hoàn toàn. Server hiện tại chưa đọc version khi đăng ký nên chưa crash, nhưng nếu sau này thêm version check phía server sẽ phải sửa cả 2 phía.

**Code hiện tại (lỗi):**

```csharp
// Service.cs
public void requestRegister(string username, string pass, string usernameAo, string passAo, string version)
{
    Message message = messageNotLogin(1);
    message.writer().writeUTF(username);
    message.writer().writeUTF(pass);
    if (usernameAo != null && !usernameAo.Equals(string.Empty))
    {
        message.writer().writeUTF(usernameAo);
        message.writer().writeUTF("a");
    }
    // version không được ghi vào packet
    session.sendMessage(message);
}
```

**Sửa thành:**

```csharp
public void requestRegister(string username, string pass, string usernameAo, string passAo, string version)
{
    Message message = messageNotLogin(1);
    message.writer().writeUTF(username);
    message.writer().writeUTF(pass);
    message.writer().writeUTF(version);   // thêm dòng này
    if (usernameAo != null && !usernameAo.Equals(string.Empty))
    {
        message.writer().writeUTF(usernameAo);
        message.writer().writeUTF("a");
    }
    session.sendMessage(message);
}
```

> **Lưu ý:** Sau khi sửa client, phải cập nhật server `Controller.java` — `createChar()` để đọc thêm trường version từ packet:
> ```java
> // Controller.java - createChar()
> String name    = msg.reader().readUTF();
> String version = msg.reader().readUTF();  // thêm dòng này
> int gender     = msg.reader().readByte();
> int hair       = msg.reader().readByte();
> ```

---

## 🟢 Low

---

### BUG-05 — Tên biến gem/ruby không khớp với luongKhoa/luong

| | |
|---|---|
| **Mức độ** | 🟢 Low |
| **Loại** | Convention / maintainability |
| **File server** | `nro/src/main/java/nro/services/Service.java` — `player()` |
| **File client** | `client/Assets/Scripts/Controller.cs` — `messageSubCommand()` case `0` |

**Mô tả:**

Server ghi `gem` rồi `ruby` vào packet player info. Client đọc vào biến `luongKhoa` rồi `luong`. Mapping ngầm này có vẻ cố ý (gem = luongKhoa, ruby = luong theo thiết kế game cũ) nhưng không có comment giải thích, gây nhầm lẫn khi đọc code và rủi ro khi sửa chức năng liên quan.

**Server ghi (Service.java):**

```java
msg.writer().writeLong(gold);          // → client: xu (vàng)
msg.writer().writeInt(pl.inventory.gem);   // → client: luongKhoa
msg.writer().writeInt(pl.inventory.ruby);  // → client: luong
```

**Client đọc (Controller.cs):**

```csharp
Char.myCharz().xu         = msg.reader().readLong();   // vàng
Char.myCharz().luongKhoa  = msg.reader().readInt();    // thực ra là gem
Char.myCharz().luong      = msg.reader().readInt();    // thực ra là ruby
```

**Sửa — thêm comment mapping ở cả 2 phía:**

```java
// Service.java
msg.writer().writeLong(gold);              // [1] xu — vàng
msg.writer().writeInt(pl.inventory.gem);   // [2] → client.luongKhoa
msg.writer().writeInt(pl.inventory.ruby);  // [3] → client.luong
```

```csharp
// Controller.cs
Char.myCharz().xu        = msg.reader().readLong();  // [1] vàng
Char.myCharz().luongKhoa = msg.reader().readInt();   // [2] server: gem
Char.myCharz().luong     = msg.reader().readInt();   // [3] server: ruby
```

---

### BUG-06 — Session timeout bị comment out

| | |
|---|---|
| **Mức độ** | 🟢 Low |
| **Loại** | Logic bị vô hiệu hoá / resource leak |
| **File** | `nro/src/main/java/nro/server/io/Session.java` — `update()` dòng ~85 |

**Mô tả:**

Logic kick session khi client im lặng quá lâu đã bị comment out. Nếu một client mất kết nối mà không gửi disconnect packet, session sẽ tồn tại mãi mãi trên server, chiếm slot trong `MAX_PLAYER` và gây tốn tài nguyên.

**Code hiện tại:**

```java
public void update() {
    if (Util.canDoWithTime(lastTimeReadMessage, TIME_WAIT_READ_MESSAGE)) {
//      Client.gI().kickSession(this);   // ← bị comment out
    }
}
```

**Sửa — uncomment và thêm log để theo dõi:**

```java
public void update() {
    if (Util.canDoWithTime(lastTimeReadMessage, TIME_WAIT_READ_MESSAGE)) {
        System.out.println("[Session] Kick idle session: " + getName());
        Client.gI().kickSession(this);
    }
}
```

> **Lưu ý:** Kiểm tra giá trị `TIME_WAIT_READ_MESSAGE` trước khi bật lại — nếu quá ngắn sẽ kick nhầm người chơi đang ở màn hình tĩnh (chọn nhân vật, NPC menu, v.v.).

---

## Tóm tắt

| ID | Mức độ | File | Mô tả ngắn |
|---|---|---|---|
| BUG-01 | 🔴 Critical | `Controller.java` | NPE khi tăng điểm pet — check sai null |
| BUG-02 | 🔴 Critical | `LoginController.java` | `lastTimeLogout` gán nhầm `lastTimeLogin` |
| BUG-03 | 🟡 Medium | `DataGame.java` / `Controller.cs` | Packet `-29/2` thiếu 1 byte `AdminLink` |
| BUG-04 | 🟡 Medium | `Service.cs` | `requestRegister` không ghi `version` vào packet |
| BUG-05 | 🟢 Low | `Service.java` / `Controller.cs` | Tên biến gem/ruby ↔ luongKhoa/luong không nhất quán |
| BUG-06 | 🟢 Low | `Session.java` | Session timeout bị comment out — resource leak |
