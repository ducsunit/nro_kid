# NRO Protocol Packet Map (Server <-> Client)

Tai lieu nay map cac packet quan trong de tu thay doi an toan ma khong lech protocol.

## Quy uoc doc nhanh

- `cmd`: command id cua packet.
- `sub`: subcommand khi packet la `cmd -30` (messageSubCommand).
- Kieu:
  - `byte`: 1 byte signed
  - `ubyte`: 1 byte unsigned (client dung `readUnsignedByte`)
  - `short`: 2 bytes signed
  - `ushort`: 2 bytes unsigned (client dung `readUnsignedShort`)
  - `int`: 4 bytes
  - `long`: 8 bytes
  - `bool`: 1 byte boolean
  - `utf`: chuoi UTF (2-byte length + bytes)

---

## 1) Session/Login handshake

### `cmd -27` (GET_SESSION_ID / session key)

- Huong:
  - Client -> Server: yeu cau handshake
  - Server -> Client: tra ve key + thong tin ket noi
- Server ghi (`MessageSender.sendSessionKey`):
  1. `byte` key length
  2. `byte[]` key da xor-chain
  3. `utf` db host
  4. `int` port
  5. `bool` isConnect2
- Client doc: `Session_ME.getKey`
- Framing note:
  - Cac session reader (`Session_ME`, `Session_ME2`) deu dung 3-byte length cho nhom cmd:
    `-32, -66, 11, -67, -74, -87, 66`.
  - Truoc khi `getKeyComplete`, length 2-byte duoc doc theo:
    `((b4 & 0xFF) << 8) | (b5 & 0xFF)`.

### `cmd -29` (NOT_LOGIN)

- `sub 0`: login username/password
  - Client ghi: `utf username`, `utf password`
  - Server doc: `Controller.messageNotLogin -> session.login(...)`
- `sub 2`: setClientType
  - Client ghi:
    1. `byte` clientType
    2. `byte` zoomLevel
    3. `bool` is_gprs
    4. `int` width
    5. `int` height
    6. `bool` is_qwerty
    7. `bool` is_touch
    8. `utf` platform|version
  - Server doc: `Session.setClientType`

---

## 2) Subcommand packet (`cmd -30`)

Client entry point: `Controller.messageSubCommand`.

### `sub 4` (money + hp/mp for self)

- Server ghi (`PlayerService.sendInfoHpMpMoney`):
  1. `long` gold
  2. `int` gem
  3. `int` hp
  4. `int` mp
  5. `int` ruby
- Client doc:
  1. `readLong` xu
  2. `readInt` luong
  3. `readInt3Byte` hp (thuc te dang doc 4-byte int)
  4. `readInt3Byte` mp
  5. `readInt` luongKhoa

### `sub 5` (update hp self)

- Server ghi (`PlayerService.sendInfoHp`):
  1. `int` hp
- Client doc:
  1. `readInt3Byte` hp

### `sub 6` (update mp self)

- Server ghi (`PlayerService.sendInfoMp`):
  1. `int` mp
- Client doc:
  1. `readInt3Byte` mp

### `sub 7` (update char info in map)

- Server ghi (`Service.sendPlayerInfo`):
  1. `int` charId
  2. `int` clanId
  3. `byte` level
  4. `bool` invisible
  5. `byte` typePk
  6. `byte` class/gender marker
  7. `byte` gender
  8. `short` head
  9. `utf` name
  10. `int` hp
  11. `int` hpMax
  12. `short` body
  13. `short` leg
  14. `byte` bag
  15. `byte` wp
  16. `short` x
  17. `short` y
  18. `short` eff5Hp
  19. `short` eff5Mp
  20. `byte` effectCount
- Client doc: `readCharInfo(...)`

### `sub 14` (update hp for others in map)

- Server ghi (`Service.Send_Info_NV`, `Service.sendInfoPlayerEatPea`, `SkillService`):
  1. `int` charId
  2. `int` hp
  3. `byte` effectType
  4. `int` hpMax (optional but dang gui day du)
- Client doc:
  1. `readInt` charId
  2. `readInt3Byte` hp
  3. `readByte` effectType
  4. `readInt3Byte` hpMax (try-catch)

### `sub 15` (revive/respawn in map)

- Server ghi (`Service.hsChar`):
  1. `int` charId
  2. `int` hp
  3. `int` hpMax
  4. `short` x
  5. `short` y
- Client doc:
  1. `readInt` charId
  2. `readInt3Byte` hp
  3. `readInt3Byte` hpMax
  4. `readShort` x
  5. `readShort` y

---

## 3) Main stat packet

### `cmd -42` (point/stat refresh)

- Server ghi (`Service.point`):
  1. `int` hpg
  2. `int` mpg
  3. `int` dameg
  4. `int` hpFull
  5. `int` mpFull
  6. `int` hp
  7. `int` mp
  8. `byte` speed
  9. `byte` hp/1000TN
  10. `byte` mp/1000TN
  11. `byte` dame/1000TN
  12. `int` dameFull
  13. `int` defFull
  14. `byte` critFull
  15. `long` tiemNang
  16. `short` expForOneAdd
  17. `short` defGoc
  18. `byte` critGoc
- Client doc: `Controller case -42`

---

## 4) Pet packets

### `cmd -107`, `type 2` (show pet info)

- Server ghi (`Service.showInfoPet`):
  1. `byte` type (=2)
  2. `short` head
  3. `byte` itemBodyCount
  4. item body loop:
     - `short` templateId
     - `int` quantity
     - `utf` info
     - `utf` content
     - `byte` optionCount
     - option loop:
       - `byte` optionId
       - `short` optionParam
  5. `int` hp
  6. `int` hpMax
  7. `int` mp
  8. `int` mpMax
  9. `int` dameFull
  10. `utf` name
  11. `utf` currentLevelText
  12. `long` power
  13. `long` tiemNang
  14. `byte` status
  15. `short` stamina
  16. `short` maxStamina
  17. `byte` crit
  18. `short` def
  19. `byte` skillSlots
  20. skill loop: `short skillId` or `short -1` + `utf` lockText
- Client doc: `Controller case -107`

### `cmd -109` (pet base growth info)

- Server ghi (`Service.petPoint`):
  1. `long` hpg
  2. `long` mpg
  3. `long` dameg
  4. `long` defg
  5. `int` critg
- Client: can bo sung map chi tiet neu client co parse truc tiep packet nay.

---

## 4.2) Title/Effect packet width

### `cmd -128` (effect title/foot)

- Server ghi (`Service.sendTitle`, `Service.sendTitleRv`, `Service.sendFoot`, `Service.sendFootRv`):
  1. `byte` type
  2. `int` charId
  3. `short` effectId (luon co mat, neu khong hop le gui `-1`)
  4. cac field duoi theo tung type (`byte/short/...`)
- Rule quan trong:
  - Khong bo qua field `short effectId` trong nhanh khong du dieu kien title.
  - Neu bo qua se lam lech stream cac field phia sau.

---

## 4.1) Inventory/Shop/Trade option width

Tat ca cac packet co item option ma client doc `readUnsignedShort()` thi server dang gui `short`:

- Inventory:
  - `cmd -36` (`InventoryService.sendItemBags`)
  - `cmd -37` (`InventoryService.sendItemBody`)
  - `cmd -35` (`InventoryService.sendItemBox`)
- Full init inventory:
  - `cmd -30 sub 0` (`Service.player`)
- Pet info:
  - `cmd -107 type 2` (`Service.showInfoPet`)
- Shop/reward lists:
  - `cmd -44` (`ShopService` luong box/reward)
- Trade lock:
  - `cmd -86` (`Trade.lockTran`)
- Consignment:
  - `cmd -44` type `2` (`ConsignmentShop.show`, `nextPage`)
  - option param: `short`
  - quantity: `int` (khong tach theo version)
- Radar card:
  - `cmd 127` (`Cmd.RADA_CARD`, `RadaService.viewCollectionBook`)
  - option param: `short`

Field option hien tai:
- `byte optionId`
- `short optionParam`

---

## 5) Combat packets (Mob)

### `cmd -9` (mob bi danh nhung chua chet)

- Server ghi (`MobService.sendMobStillAliveAffterAttacked`):
  1. `byte` mobId
  2. `int` mobHp
  3. `int` dameHit
  4. `bool` crit
  5. `int` effectId (thuong `-1`)
- Client doc: `Controller case -9`

### `cmd -12` (mob chet)

- Server ghi (`MobService.sendMobDieAffterAttacked`):
  1. `byte` mobId
  2. `int` dameHit
  3. `bool` crit
  4. `byte` dropCount
  5. drop loop:
     - `short` itemMapId
     - `short` itemTemplateId
     - `short` x
     - `short` y
     - `int` playerId
- Client doc: `Controller case -12`

### `cmd -11` (mob danh me)

- Server ghi (`MobService.sendMobAttackMe`):
  1. `byte` mobId
  2. `int` dameHp
- Client doc: `Controller case -11` (+ optional mp damage field try-catch)

### `cmd -10` (mob danh player khac)

- Server ghi (`MobService.sendMobAttackPlayer`):
  1. `byte` mobId
  2. `int` playerId
  3. `int` playerHpNew
- Client doc: `Controller case -10`

### `cmd -13` (mob hoi sinh)

- Server ghi (`MobService.hoiSinhMob`):
  1. `byte` mobId
  2. `byte` tempId
  3. `byte` levelBoss
  4. `int` hp
- Client doc: `Controller case -13`

### `cmd -95` (MOB_ME_UPDATE)

- `type 0` (spawn mobMe):
  1. `int` ownerCharId
  2. `short` mobTemplateId
  3. `int` mobHp
- `type 2` (mobMe danh player):
  1. `int` ownerMobMeId
  2. `int` targetPlayerId
  3. `int` dameHit
  4. `int` hpPlayerAfterHit
- `type 3` (mobMe danh mob):
  1. `int` ownerMobMeId
  2. `int` targetMobId
  3. `int` hpMobAfterHit
  4. `int` dameHit
- Server nguon: `MobMe`, `ChangeMapService`
- Client doc: `Controller case -95`

---

## 6) Combat packets (Player vs Player / reflect)

### `cmd -60` (player attack player)

- Server ghi (`SkillService.playerAttackPlayer`):
  1. `int` attackerId
  2. `byte` skillId
  3. `byte` targetCount
  4. target loop: `int` targetId
  5. `byte` readContinue
  6. `byte` typeSkill
  7. `int` dameHit
  8. `bool` isDie
  9. `bool` isCrit
- Client doc: `Controller case -60`

### `cmd 56` (phan sat thuong / reflect damage)

- Server ghi (`SkillService.phanSatThuong`):
  1. `int` targetId
  2. `int` hpAfterReflect
  3. `int` dameReflect
  4. `bool` isCrit
  5. `byte` effectId
- Client doc: `Controller case 56`

---

## 7) Death and MobMe sync packets

### `cmd -17` (self die)

- Server ghi (`Service.charDie`):
  1. `byte` cPk
  2. `short` x
  3. `short` y
  4. `long` power (optional, client read in try-catch)
- Client doc: `Controller case -17`

### `cmd -8` (other player die in map)

- Server ghi (`Service.charDie`):
  1. `int` charId
  2. `byte` cPk
  3. `short` x
  4. `short` y
- Client doc: `Controller case -8`

### `cmd -95`, `type 0` (MOB_ME_UPDATE)

- Server ghi (`ChangeMapService.sendEffectMeToMap/sendEffectMapToMe`):
  1. `byte` type (`0`)
  2. `int` ownerCharId
  3. `short` mobTemplateId
  4. `int` mobHp
- Client doc: `Controller case -95`, branch `b48 == 0`

### `cmd -24` (map info / load map)

- Server mob block (`Zone.mapInfo`):
  - `hp` va `hpFull` cua mob gui `int` (da clamp)
- Client mob block (`Controller case -24`):
  - doc mob hp/maxhp bang `readInt()`

### `cmd 6` (send money)

- Server ghi (`Service.sendMoney`):
  1. `long` gold
  2. `int` gem
  3. `int` ruby
- Client doc (`Controller case 6`):
  1. `readLong` xu
  2. `readInt` luong
  3. `readInt` luongKhoa

---

## 7.1) Chat packet consistency

### `cmd 92` (`Cmd.CHAT_THEGIOI_SERVER`)

- Server ghi (tat ca luong `Service.chatGlobal`, `Service.chatPrivate`, `ChatGlobalService.chatGlobal`):
  1. `utf` playerName
  2. `utf` message
  3. `int` playerId
  4. `short` head
  5. `short` headIcon (hien dong bo bang head)
  6. `short` body
  7. `short` bag
  8. `short` leg
  9. `byte` type
- Rule:
  - Khong de 2 luong gui `cmd 92` dung 2 layout khac nhau theo version.
  - Can giu cung mot thu tu/width field cho moi nguon gui chat.

---

## 8) Rule de tu sua packet an toan

1. Neu client doc `readInt3Byte()` thi server gui `int` (4 bytes) trong codebase hien tai.
2. Tuyet doi khong doi thu tu field neu khong sua dong thoi o client.
3. Field nao client doc `ushort` thi server phai gui `short` hop le (khong vuot 65535 khi mong muon unsigned).
4. Khi can nang gioi han chi so lon, uu tien:
   - giu model server la `long`,
   - clamp khi serialize packet cho client.
5. Moi khi them field moi:
   - them o cuoi packet,
   - doc ben client trong `try-catch` de backward compatible.
6. Sau khi sua, test toi thieu:
   - dang nhap, vao map
   - update HP/MP
   - danh mob, mob chet, roi item
   - PVP 1 hit
   - xem stat/pet panel

---

## 9) File tham chieu chinh

- Server:
  - `nro_kid/src/main/java/nro/services/Service.java`
  - `nro_kid/src/main/java/nro/services/PlayerService.java`
  - `nro_kid/src/main/java/nro/services/MobService.java`
  - `nro_kid/src/main/java/nro/services/SkillService.java`
- Client:
  - `client/Assets/Scripts/Controller.cs`
  - `client/Assets/Scripts/Session_ME.cs`
  - `client/Assets/Scripts/Message.cs`
  - `client/Assets/Scripts/myReader.cs`
