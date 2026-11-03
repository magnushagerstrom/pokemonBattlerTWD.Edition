-- =============================================================
-- 01_schema.sql  –  Skapar alla tabeller (strukturen).
-- Ordningen är viktig: en tabell måste finnas innan någon
-- annan tabell kan peka på den med REFERENCES (främmande nyckel).
--
-- Id-kolumner: INT GENERATED ALWAYS AS IDENTITY
--   = databasen delar alltid ut id själv, och ingen kan skriva in egna.
--   INT (max ca 2,1 miljarder) räcker med god marginal för appens datamängd.
-- FK-kolumner: INT, samma typ som den PK de pekar på.
-- =============================================================

-- Typerna: FIRE, WATER, GRASS, ELECTRIC, NORMAL
CREATE TABLE type (
    type_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name    VARCHAR(20) NOT NULL UNIQUE
);

-- Pokémon. Varje Pokémon har exakt en typ.
CREATE TABLE pokemon (
    pokemon_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(50) NOT NULL UNIQUE,
    max_hp     INT         NOT NULL CHECK (max_hp BETWEEN 1 AND 100),
    type_id    INT         NOT NULL REFERENCES type (type_id)
);

-- Attacker. DAMAGE-attacker har typ, HEAL-attacker har ingen typ.
-- owner_pokemon_id: NULL = gemensam attack som alla får välja,
--                   ifyllt = signaturattack som bara ägaren får använda
--                   (regeln upprätthålls i Java när attacker väljs).
CREATE TABLE attack (
    attack_id        INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name             VARCHAR(50) NOT NULL UNIQUE,
    kind             VARCHAR(10) NOT NULL CHECK (kind IN ('DAMAGE', 'HEAL')),
    accuracy         INT         NOT NULL CHECK (accuracy BETWEEN 1 AND 100),
    base_damage      INT         NOT NULL DEFAULT 0 CHECK (base_damage >= 0),
    heal_amount      INT         NOT NULL DEFAULT 0 CHECK (heal_amount >= 0),
    type_id          INT         REFERENCES type (type_id),                          -- NULL för heal
    owner_pokemon_id INT         REFERENCES pokemon (pokemon_id) ON DELETE CASCADE,  -- NULL = gemensam
    CHECK (
        (kind = 'DAMAGE' AND type_id IS NOT NULL AND heal_amount = 0)
        OR
        (kind = 'HEAL'   AND type_id IS NULL     AND base_damage = 0)
    )
);

-- Kopplingstabell: vilken Pokémon har vilka attacker (N:M).
CREATE TABLE pokemon_attack (
    pokemon_id INT NOT NULL REFERENCES pokemon (pokemon_id) ON DELETE CASCADE,
    attack_id  INT NOT NULL REFERENCES attack (attack_id),
    PRIMARY KEY (pokemon_id, attack_id)
);

-- En strid. Fylls när man spelar, inte nu.
CREATE TABLE battle (
    battle_id   INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fighter_id  INT       NOT NULL REFERENCES pokemon (pokemon_id),
    opponent_id INT       NOT NULL REFERENCES pokemon (pokemon_id),
    winner_id   INT       NOT NULL REFERENCES pokemon (pokemon_id),
    played_at   TIMESTAMP NOT NULL DEFAULT now(),
    CHECK (fighter_id <> opponent_id),             -- man slåss inte mot sig själv
    CHECK (winner_id IN (fighter_id, opponent_id)) -- vinnaren var med i striden
);

-- Ett drag i en strid (svag entitet: finns bara inuti en strid).
CREATE TABLE battle_turn (
    battle_id    INT     NOT NULL REFERENCES battle (battle_id) ON DELETE CASCADE,
    turn_no      INT     NOT NULL CHECK (turn_no >= 1),
    attacker_id  INT     NOT NULL REFERENCES pokemon (pokemon_id),
    attack_id    INT     NOT NULL REFERENCES attack (attack_id),
    damage       INT     NOT NULL DEFAULT 0 CHECK (damage >= 0),
    was_critical BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY (battle_id, turn_no)
);
