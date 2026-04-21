# Fix dame: long → int

> Mục tiêu: đưa `dame` và các field liên quan về `int` để loại bỏ dame trung gian vượt 2 tỉ,  
> trong khi **giữ nguyên `long` cho `power`, `tiemNang`, `hp`, `mp`** vì power thực tế có thể đến 100 tỉ.

---

## Phân tích trước khi sửa

### Giá trị dame thực tế trong game (worst-case)

| Bước | Nguồn | Giá trị tối đa |
|---|---|---|
| `dameg` gốc (từ `power_limit` level 12) | DB | **40,000** |
| `dame` sau `setDame()` (đồ + buff + fusion + cuồng nộ + title...) | NPoint | **~37 triệu** |
| `dameAttack` sau `getDameAttack()` (skill 1100%) | NPoint | **~411 triệu** |
| `dameAttack` sau crit ×2 | NPoint | **~822 triệu** |

→ Dame cuối cùng đi vào `Player.injured()` tối đa **~822 triệu**, nằm trong `int` (max 2,147,483,647).  
→ **`dame` field có thể đổi sang `int` an toàn.**

### Cạm bẫy overflow khi nhân trung gian

`calPercent(dame, percent) = dame * percent / 100`

Nếu `dame` là `int` và `percent = 1100`:
```
37_365_363 * 1100 = 41_101_899_300   ← vượt INT_MAX
→ overflow thành -1,847,773,660       ← kết quả sai hoàn toàn
```

**Giải pháp:** giữ `calPercent` tính bằng `long` — cast `dame` lên `long` trước khi nhân.

---

## Danh sách field cần đổi

### ✅ Đổi `long` → `int` (safe)

File: `nro/src/main/java/nro/models/player/NPoint.java`

```java
// TRƯỚC
public long dame;
public long dameg;
public long def;
public long defg;
public long hpg;
public long mpg;
public long hpHoi, mpHoi, mpHoiCute;
public long dameAfter; // nếu có

// SAU
public int dame;
public int dameg;
public int def;
public int defg;
public int hpg;
public int mpg;
public int hpHoi, mpHoi, mpHoiCute;
```

> **Lý do `hpg`/`mpg` cũng đổi được:** `power_limit.hp` và `power_limit.mp` tối đa là **600,000**  
> (level 12, power 500 tỉ). `hpMax` sau buff nhân đôi (~1.5 triệu) vẫn nằm trong `int`.  
> Tuy nhiên `hp` và `hpMax` giữ nguyên `long` vì `subHP` dùng long trong các bước tính toán.

### ❌ Giữ nguyên `long` (KHÔNG được đổi)

| Field | Lý do |
|---|---|
| `power`, `tiemNang` | Có thể đến 100 tỉ (100,000,000,000) |
| `hp`, `hpMax`, `mp`, `mpMax` | Server clamp tại `Integer.MAX_VALUE` nhưng tính toán trung gian dùng long; `setHpMax()` nhân `*= 2`, `+= calPercent(hpMax, 120)` nhiều lần |
| `Player.id`, `ItemMap.playerId` | ID từ DB dạng BIGINT |
| `Clan.powerPoint`, `ClanMember.powerPoint` | Tổng power nhiều người |
| `Inventory.gold` | Có ceiling riêng tại `Integer.MAX_VALUE` |

---

## Các thay đổi code cụ thể

### 1. `NPoint.java` — đổi field type

```java
// Trước
public long mpg, hpg;
public long dameg;
public long dame;
public long def;
public long defg;
public long hpHoi, mpHoi, mpHoiCute;

// Sau
public int hpg, mpg;
public int dameg;
public int dame;
public int def;
public int defg;
public int hpHoi, mpHoi, mpHoiCute;
```

---

### 2. `NPoint.java` — `calPercent` giữ nguyên `long`

Hàm này **không đổi** — vẫn nhận và trả `long` để tránh overflow khi nhân trung gian:

```java
// Giữ nguyên - KHÔNG sửa
public long calPercent(long param, long percent) {
    return param * percent / 100;
}
```

Khi dùng kết quả `calPercent` để gán vào field `int`, cần cast:

```java
// Trước (dame là long, gán trực tiếp)
this.dame += calPercent(this.dame, 50L);

// Sau (dame là int, cast kết quả)
this.dame += (int) calPercent(this.dame, 50L);
```

Làm tương tự cho **tất cả** các dòng `this.dame +=`, `this.def +=`, `this.hpg +=`, `this.mpg +=`  
trong các hàm: `setDame()`, `setDef()`, `setHpMax()`, `setMpMax()`.

---

### 3. `NPoint.java` — `setDame()`: cast toàn bộ

```java
private void setDame() {
    this.dame = this.dameg;
    this.dame += this.dameAdd;

    for (Integer n : tlDameCopy) {
        this.dame += (int) calPercent(this.dame, n);   // ← thêm (int) cast
    }
    for (Integer tl : tlSDDepCopy) {
        this.dame += (int) calPercent(this.dame, tl);  // ← thêm (int) cast
    }

    if (this.player.setClothes.nguyetan == 5) {
        this.dame += (int) calPercent(this.dame, 10L); // ← thêm (int) cast
    }

    // ... tất cả dòng dame += calPercent(...) đều thêm (int) cast

    if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo) {
        this.dame *= 2;   // int * int = int, OK (dame ~37M * 2 = ~74M < INT_MAX)
    }
    if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo2) {
        this.dame += (int) calPercent(this.dame, 120L); // ← thêm (int) cast
    }

    // fusion: dame += pet.dame (cả 2 đều int -> OK)
    if (this.player.fusion.typeFusion != 0) {
        this.dame += this.player.pet.nPoint.dame;
    }
    // ...
}
```

---

### 4. `NPoint.java` — `getDameAttack()`: trả `long`, tính bằng `long`

Hàm này vẫn trả `long` vì kết quả cuối (sau skill ×1100%) có thể ~400-800 triệu.  
Nhưng biến `dameAttack` khởi đầu từ `this.dame` (nay là `int`) nên cần gán vào `long`:

```java
public long getDameAttack(boolean isAttackMob) {
    // Trước: long dameAttack = this.dame;   (dame là long)
    long dameAttack = (long) this.dame;      // ← cast int -> long

    // ... các phép tính sau dùng long dameAttack -> OK, không overflow
    if (percentDameSkill != 0) {
        dameAttack = calPercent(dameAttack, percentDameSkill); // long * long -> long, OK
    }
    dameAttack += calPercent(dameAttack, percentDameIntrinsic);
    // ...
    if (isCrit) {
        dameAttack *= 2; // long * int -> long, OK
    }
    return dameAttack; // long
}
```

---

### 5. `NPoint.java` — `setAttributeOverLimit()`: đổi clamp sang `int` range

```java
public void setAttributeOverLimit() {
    // Trước
    this.hpg   = Math.max(1, Math.min(INT_MAX_VALUE, this.hpg));
    this.mpg   = Math.max(1, Math.min(INT_MAX_VALUE, this.mpg));
    this.dameg = Math.max(1, Math.min(INT_MAX_VALUE, this.dameg));
    this.defg  = Math.max(0, Math.min(INT_MAX_VALUE, this.defg));
    this.dame  = Math.max(1, Math.min(INT_MAX_VALUE, this.dame));
    this.def   = Math.max(0, Math.min(INT_MAX_VALUE, this.def));

    // Sau — INT_MAX_VALUE không cần vì đã là int, nhưng giữ clamp >= 1
    this.hpg   = Math.max(1, this.hpg);
    this.mpg   = Math.max(1, this.mpg);
    this.dameg = Math.max(1, this.dameg);
    this.defg  = Math.max(0, this.defg);
    this.dame  = Math.max(1, this.dame);
    this.def   = Math.max(0, this.def);

    // Giữ nguyên power và tiemNang clamp (vẫn là long)
    this.power    = Math.max(0, Math.min(INT_MAX_VALUE, this.power));
    this.tiemNang = Math.max(0, Math.min(INT_MAX_VALUE, this.tiemNang));
    this.crit = Math.max(0, Math.min(110, this.crit));
    setHp();
    setMp();
}
```

---

### 6. `NPoint.java` — `addHp`, `setHp`, `addMp`, `setMp`: giữ nguyên `long`

Các hàm này giữ nguyên `long` vì `hp`, `hpMax`, `mp`, `mpMax` **không đổi type**:

```java
// Không thay đổi gì
public void addHp(long hp) { ... }
public void setHp(long hp) { ... }
public void addMp(long mp) { ... }
public void setMp(long mp) { ... }
```

---

### 7. `NPoint.java` — `subDameInjureWithDeff`: nhận `long`, trả `long`

Tham số `dame` truyền vào là kết quả `getDameAttack()` (long ~822M) → hàm này giữ nguyên:

```java
// Không thay đổi gì
public long subDameInjureWithDeff(long dame) {
    long def = (long) this.def;  // ← cast int def lên long để phép trừ đúng
    dame -= def;
    // ...
    return dame;
}
```

> Chú ý thêm cast `(long) this.def` vì `def` nay là `int`.

---

### 8. `NPoint.java` — `increasePoint()` (tăng điểm)

```java
public void increasePoint(byte type, short point) {
    switch (type) {
        case 0: // hpg
            if ((this.hpg + point) <= powerLimit.getHp()) {  // int + short <= int -> OK
                this.hpg += point;
            }
            break;
        case 1: // mpg
            if ((this.mpg + point) <= powerLimit.getMp()) {
                this.mpg += point;
            }
            break;
        case 2: // dameg
            if ((this.dameg + point) <= powerLimit.getDamage()) { // int <= int -> OK
                this.dameg += point;
            }
            break;
        case 3: // defg
            if ((this.defg + point) <= powerLimit.getDefense()) {
                this.defg += point;
            }
            break;
        case 4: // critg
            if ((this.critg + point) <= powerLimit.getCritical()) {
                this.critg += point;
            }
            break;
    }
    // Không cần thay đổi logic, chỉ đổi type field ở trên là đủ
}
```

---

### 9. `GodGK.java` — load từ DB

```java
// Trước
player.nPoint.dameg = Long.parseLong(dataArray.get(7).toString());
player.nPoint.hpg   = Long.parseLong(dataArray.get(5).toString());
player.nPoint.mpg   = Long.parseLong(dataArray.get(6).toString());
player.nPoint.defg  = Long.parseLong(dataArray.get(8).toString());

// Sau
player.nPoint.dameg = Integer.parseInt(dataArray.get(7).toString());
player.nPoint.hpg   = Integer.parseInt(dataArray.get(5).toString());
player.nPoint.mpg   = Integer.parseInt(dataArray.get(6).toString());
player.nPoint.defg  = Integer.parseInt(dataArray.get(8).toString());
```

Tương tự cho pet:

```java
// Trước
pet.nPoint.dameg = Long.parseLong(String.valueOf(dataObject.get("damg")));

// Sau
pet.nPoint.dameg = Integer.parseInt(String.valueOf(dataObject.get("damg")));
```

---

### 10. `PlayerDAO.java` — lưu vào DB

Không cần sửa — `dataPoint.add(player.nPoint.dameg)` với `int` vẫn serialize JSON bình thường.  
JSONArray nhận `int` và `long` như nhau.

---

### 11. `Service.java` — ghi packet

`clampToInt()` hiện đang bọc trước khi `writeInt`. Sau khi đổi sang `int` thì bỏ clamp:

```java
// Trước
msg.writer().writeInt(clampToInt(player.nPoint.dame));
msg.writer().writeInt(clampToInt(player.nPoint.def));

// Sau — dame/def đã là int, không cần clamp
msg.writer().writeInt(player.nPoint.dame);
msg.writer().writeInt(player.nPoint.def);
```

> Giữ nguyên `clampToInt` cho `hp`, `hpMax`, `mp`, `mpMax`, `power` (vẫn là long).

---

## DB schema — không cần migration

- `power_limit.damage` đã là `INT(11)` → không cần đổi
- `player.data_point` lưu JSON string → `dameg` đọc/ghi bình thường với `int`
- Không có column riêng cho `dame` (tính toán runtime)

---

## Kiểm tra sau khi sửa

### Compile check

```bash
cd nro
mvn compile 2>&1 | grep "ERROR\|error:"
```

Các lỗi compile điển hình cần fix thêm:

| Lỗi | Nguyên nhân | Fix |
|---|---|---|
| `incompatible types: long cannot be converted to int` | Gán `calPercent(...)` vào `int` field | Thêm `(int)` cast |
| `possible lossy conversion from long to int` | So sánh hoặc gán trực tiếp | Thêm `(int)` cast hoặc đổi kiểu biến local |
| `operator * cannot be applied to int, long` | Nhân `int * long` | Cast int lên long trước: `(long) this.dame * percent` |

### Test runtime

Đăng nhập nhân vật có đủ buff (cuồng nộ, fusion, title 3 danh hiệu) và kiểm tra:

1. Dame hiển thị trên màn hình không âm
2. Dame không phải `0` hoặc bằng `1` (trường hợp def > dame)
3. Boss/quái vật chết đúng với dame đánh
4. Skill Quả cầu kênh khí hoạt động bình thường (tính `totalHP`)
5. Skill Makankosappo hoạt động bình thường (tính `mpMax`)

---

## Tóm tắt thay đổi

| File | Thay đổi |
|---|---|
| `NPoint.java` | Đổi `dame`, `dameg`, `def`, `defg`, `hpg`, `mpg`, `hpHoi`, `mpHoi`, `mpHoiCute` từ `long` → `int`; thêm `(int)` cast tại mọi `this.dame += calPercent(...)` |
| `NPoint.java` | `getDameAttack()`: thêm `long dameAttack = (long) this.dame` |
| `NPoint.java` | `subDameInjureWithDeff()`: thêm `long def = (long) this.def` |
| `NPoint.java` | `setAttributeOverLimit()`: bỏ clamp INT_MAX_VALUE cho các field đã là `int` |
| `GodGK.java` | Đổi `Long.parseLong` → `Integer.parseInt` cho `dameg`, `hpg`, `mpg`, `defg` |
| `Service.java` | Bỏ `clampToInt()` wrapper cho `dame` và `def` trong `writeInt` |

**Không thay đổi:** `power`, `tiemNang`, `hp`, `hpMax`, `mp`, `mpMax`, `Inventory.gold`, `Player.id` — tất cả giữ nguyên `long`.
