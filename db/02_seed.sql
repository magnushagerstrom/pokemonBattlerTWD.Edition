-- =============================================================
-- 02_seed.sql  –  Lägger in startdata (samma som SeedData.java).
-- Körs efter 01_schema.sql, när tabellerna redan finns.
-- Ordning: typer → Pokémon → attacker → kopplingar.
-- (Pokémon måste finnas före attackerna, eftersom en
--  signaturattack pekar på sin ägare.)
-- =============================================================

-- 1. Typerna (versaler, samma som enum Type i Java)
INSERT INTO type (name)
VALUES ('FIRE'),
       ('WATER'),
       ('GRASS'),
       ('ELECTRIC'),
       ('NORMAL');

-- 2. De sex Pokémonen
INSERT INTO pokemon (name, max_hp, type_id)
VALUES ('Daryl Dixonemon', 78, (SELECT type_id FROM type WHERE name = 'ELECTRIC')),
       ('Maggie Greenemon', 88, (SELECT type_id FROM type WHERE name = 'WATER')),
       ('Carol Peltierizard', 78, (SELECT type_id FROM type WHERE name = 'GRASS')),
       ('Michonnemon', 88, (SELECT type_id FROM type WHERE name = 'WATER')),
       ('Rickemon Grimes', 87, (SELECT type_id FROM type WHERE name = 'FIRE')),
       ('Neganizard', 88, (SELECT type_id FROM type WHERE name = 'WATER'));

-- 3a. Gemensamma attacker (owner_pokemon_id utelämnas = NULL = alla får välja dem)
--     (SELECT ...) slår upp rätt type_id via namnet,
--     så vi slipper gissa vilket nummer typen fick.
INSERT INTO attack (name, kind, accuracy, base_damage, heal_amount, type_id)
VALUES ('Tackle', 'DAMAGE', 95, 12, 0, (SELECT type_id FROM type WHERE name = 'NORMAL')),
       ('Fist', 'DAMAGE', 100, 20, 0, (SELECT type_id FROM type WHERE name = 'NORMAL')),
       ('Arrow', 'DAMAGE', 85, 28, 0, (SELECT type_id FROM type WHERE name = 'FIRE')),
       ('Blunt Force', 'DAMAGE', 100, 20, 0, (SELECT type_id FROM type WHERE name = 'WATER')),
       ('Knife Wife', 'DAMAGE', 90, 26, 0, (SELECT type_id FROM type WHERE name = 'WATER')),
       ('First Aid', 'HEAL', 100, 0, 25, NULL);

-- 3b. Signaturattacker (ägs av en enda Pokémon)
INSERT INTO attack (name, kind, accuracy, base_damage, heal_amount, type_id, owner_pokemon_id)
VALUES ('Revolver', 'DAMAGE', 66, 33, 0,
        (SELECT type_id FROM type WHERE name = 'FIRE'),
        (SELECT pokemon_id FROM pokemon WHERE name = 'Rickemon Grimes'));

-- 4a. Alla Pokémon får Tackle och First Aid
INSERT INTO pokemon_attack (pokemon_id, attack_id)
SELECT p.pokemon_id, a.attack_id
FROM pokemon p
         CROSS JOIN attack a
WHERE a.name IN ('Tackle', 'First Aid');

-- 4b. De övriga attackerna (två var)
INSERT INTO pokemon_attack (pokemon_id, attack_id)
SELECT p.pokemon_id, a.attack_id
FROM pokemon p
         CROSS JOIN attack a
WHERE (p.name, a.name) IN (
                           ('Daryl Dixonemon', 'Fist'), ('Daryl Dixonemon', 'Arrow'),
                           ('Maggie Greenemon', 'Blunt Force'), ('Maggie Greenemon', 'Knife Wife'),
                           ('Carol Peltierizard', 'Fist'), ('Carol Peltierizard', 'Arrow'),
                           ('Michonnemon', 'Blunt Force'), ('Michonnemon', 'Knife Wife'),
                           ('Rickemon Grimes', 'Fist'), ('Rickemon Grimes', 'Revolver'),
                           ('Neganizard', 'Blunt Force'), ('Neganizard', 'Knife Wife')
    );
