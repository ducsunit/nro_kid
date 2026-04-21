-- NRO data overflow scan + fix script
-- Safe target: keep values in [0, 2147483647] for client-int compatible fields.
-- Run on MySQL/MariaDB with permission to create tables and update data.

SET @INT_MAX := 2147483647;
SET @NOW_TAG := DATE_FORMAT(NOW(), '%Y%m%d_%H%i%s');

-- =========================================================
-- 0) PRE-SCAN (before fix)
-- =========================================================

SELECT 'PRECHECK_player_power' AS check_name, COUNT(*) AS bad_rows
FROM player
WHERE power < 0 OR pet_power < 0 OR power > @INT_MAX OR pet_power > @INT_MAX;

SELECT 'PRECHECK_account_currency' AS check_name, COUNT(*) AS bad_rows
FROM account
WHERE ruby < 0 OR ruby > @INT_MAX
   OR vnd < 0 OR vnd > @INT_MAX
   OR tongnap < 0 OR tongnap > @INT_MAX;

SELECT 'PRECHECK_consignment' AS check_name, COUNT(*) AS bad_rows
FROM consignment_shop
WHERE gold < 0 OR gold > @INT_MAX
   OR gem < 0 OR gem > @INT_MAX
   OR quantity < 0 OR quantity > @INT_MAX;

-- Optional sample rows to review before applying.
SELECT id, power, pet_power
FROM player
WHERE power < 0 OR pet_power < 0 OR power > @INT_MAX OR pet_power > @INT_MAX
LIMIT 30;

SELECT id, ruby, vnd, tongnap
FROM account
WHERE ruby < 0 OR ruby > @INT_MAX
   OR vnd < 0 OR vnd > @INT_MAX
   OR tongnap < 0 OR tongnap > @INT_MAX
LIMIT 30;

SELECT id, gold, gem, quantity
FROM consignment_shop
WHERE gold < 0 OR gold > @INT_MAX
   OR gem < 0 OR gem > @INT_MAX
   OR quantity < 0 OR quantity > @INT_MAX
LIMIT 30;

-- =========================================================
-- 1) LIGHT BACKUP TABLES (structure + data copy)
-- =========================================================

SET @sql := CONCAT('CREATE TABLE IF NOT EXISTS backup_player_overflow_', @NOW_TAG, ' AS SELECT * FROM player');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql := CONCAT('CREATE TABLE IF NOT EXISTS backup_account_overflow_', @NOW_TAG, ' AS SELECT * FROM account');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql := CONCAT('CREATE TABLE IF NOT EXISTS backup_consignment_overflow_', @NOW_TAG, ' AS SELECT * FROM consignment_shop');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- =========================================================
-- 2) FIX (clamp to int range)
-- =========================================================

START TRANSACTION;

UPDATE player
SET power = LEAST(GREATEST(power, 0), @INT_MAX),
    pet_power = LEAST(GREATEST(pet_power, 0), @INT_MAX)
WHERE power < 0 OR pet_power < 0 OR power > @INT_MAX OR pet_power > @INT_MAX;

UPDATE account
SET ruby = LEAST(GREATEST(ruby, 0), @INT_MAX),
    vnd = LEAST(GREATEST(vnd, 0), @INT_MAX),
    tongnap = LEAST(GREATEST(tongnap, 0), @INT_MAX)
WHERE ruby < 0 OR ruby > @INT_MAX
   OR vnd < 0 OR vnd > @INT_MAX
   OR tongnap < 0 OR tongnap > @INT_MAX;

UPDATE consignment_shop
SET gold = LEAST(GREATEST(gold, 0), @INT_MAX),
    gem = LEAST(GREATEST(gem, 0), @INT_MAX),
    quantity = LEAST(GREATEST(quantity, 0), @INT_MAX)
WHERE gold < 0 OR gold > @INT_MAX
   OR gem < 0 OR gem > @INT_MAX
   OR quantity < 0 OR quantity > @INT_MAX;

COMMIT;

-- =========================================================
-- 3) POST-SCAN (after fix)
-- =========================================================

SELECT 'POSTCHECK_player_power' AS check_name, COUNT(*) AS bad_rows
FROM player
WHERE power < 0 OR pet_power < 0 OR power > @INT_MAX OR pet_power > @INT_MAX;

SELECT 'POSTCHECK_account_currency' AS check_name, COUNT(*) AS bad_rows
FROM account
WHERE ruby < 0 OR ruby > @INT_MAX
   OR vnd < 0 OR vnd > @INT_MAX
   OR tongnap < 0 OR tongnap > @INT_MAX;

SELECT 'POSTCHECK_consignment' AS check_name, COUNT(*) AS bad_rows
FROM consignment_shop
WHERE gold < 0 OR gold > @INT_MAX
   OR gem < 0 OR gem > @INT_MAX
   OR quantity < 0 OR quantity > @INT_MAX;


