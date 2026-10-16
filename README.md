# Pokémon Battler – TWD Edition

A turn-based Pokémon battle engine for the console, written in Java 21 with Maven.
The characters are inspired by The Walking Dead (Daryl Dixonemon, Michonnemon, Rickemon Grimes ...).

You manage a Pokédex (list, add, edit, delete), pick a fighter and face a random wild opponent.
Pokémon and battle statistics are saved as JSON and loaded when the program starts.

## Features

- Manage a Pokédex: list, add, edit and delete Pokémon with guided, validated input
- Turn-based battles against a random wild opponent, with a visible coin flip for turn order
- Type effectiveness (Fire, Water, Grass, Electric, Normal) and critical hits
- Damage and healing attacks
- A readable battle log with turn headers
- Persistent battle statistics: wins, losses, win rate per Pokémon, most used attack and Pokémon
- Automatic save/load as JSON, with fallback to starter data if a file is missing or corrupt

## How to run

**Requirements:** Java 21 and Maven.

**IntelliJ:** open the project folder and run `Main.java`.

**Terminal**, from the project root:
```
mvn compile exec:java -Dexec.mainClass=Main
```

On the first run, the Pokédex is seeded with 6 starter Pokémon. When you exit (menu 0), everything
is saved to `pokedex.json` and `stats.json` in the project folder.

## Example usage

```
===== POKÉMON BATTLER =====
--------TWD:Edition--------
1. List all Pokémon
2. Add Pokémon
3. Edit Pokémon
4. Delete Pokémon
5. Save to file
6. Load from file
7. Reset to starting Pokémons
8. Pick a fighter & Start battle
9. Battle results
0. Exit
Choose: 8
Pick your fighter!
1. Daryl Dixonemon (Electric, HP 78/78)
2. Maggie Greenemon (Water, HP 88/88)
...
Fighter: 1
Opponent is: Carol Peltierizard. Fight with honor and glory!
3...
2...
1...
FIGHT!
--=* Daryl Dixonemon vs Carol Peltierizard *=--
Flipping a coin to decide who goes first...
Heads! Daryl Dixonemon goes first.

========== Turn 1 ==========
Choose an attack for Daryl Dixonemon:
1. Fist (Normal, 20 dmg, 100% accuracy)
2. Arrow (Fire, 28 dmg, 85% accuracy)
3. Tackle (Normal, 12 dmg, 95% accuracy)
4. First Aid (heals 25 HP, 100% accuracy)
Attack: 2
Daryl Dixonemon used Arrow! Carol Peltierizard took 56 damage.
Daryl Dixonemon (Electric, HP 78/78) | Carol Peltierizard (Grass, HP 22/78)

========== Turn 2 ==========
...

============================
You won! Daryl Dixonemon defeated Carol Peltierizard.
```

## Project structure

| Package | Responsibility |
|---|---|
| `model` | Domain classes: `Pokemon`, `Type`, `Attack`, `DamageAttack`, `HealAttack` |
| `battle` | Battle logic: `BattleEngine`, `CpuStrategy`, `RandomStrategy` |
| `service` | The Pokédex (`PokedexService`), starter data (`SeedData`), statistics (`BattleStats`) |
| `repository` | Saving and loading JSON: `PokemonRepository`, `StatsRepository`, DTOs and `PokemonMapper` |
| `ui` | The console menu (`ConsoleMenu`) and safe input (`InputHelper`) |
| `exception` | Custom exceptions: `InvalidPokemonDataException`, `PokemonNotFoundException` |

## OOP choices

### Inheritance and polymorphism: `Attack`
`Attack` is an **abstract** class holding what all attacks have in common (name, accuracy) and an
abstract method `execute(attacker, defender)`.
- `DamageAttack extends Attack` calculates damage using the type chart and critical hits.
- `HealAttack extends Attack` heals the attacker.

`BattleEngine` only calls `attack.execute(...)` and does not know which kind of attack it is. Each
subclass decides what happens (polymorphism). A new attack type can be added as a new subclass
without changing `BattleEngine` (Open/Closed principle).

### Interface: `CpuStrategy` (Strategy pattern)
The CPU chooses its attack through the interface `CpuStrategy` with the method `chooseAttack(...)`.
`RandomStrategy` picks at random. `BattleEngine` receives its strategy through the constructor
(dependency injection) and depends on the interface, not on a specific class (Dependency Inversion).
A smarter AI can be added without changing the battle engine.

### Encapsulation and validation
`Pokemon` and the attacks have `private final` fields and validate everything in the constructor
(name, HP 1–100, 1–4 attacks with at least one damage attack, accuracy 1–100). It is therefore impossible to create an invalid
Pokémon. Editing (menu 3) creates a **new** Pokémon through the constructor, so the validation
applies there too.

### Separation of concerns and DTOs
The model classes know nothing about files. Saving is handled by the repository classes. Since
Jackson needs public fields and an empty constructor, simple DTOs are used (`PokemonData`,
`AttackData`, `StatsData`). `PokemonMapper` translates between model and DTO. When loading, the
objects are created through the normal constructors, so a tampered file is stopped by the validation.

### Custom exceptions
- `InvalidPokemonDataException`: invalid data (e.g. HP outside 1–100, duplicate name, broken save file)
- `PokemonNotFoundException`: the requested Pokémon does not exist

Both are caught in the menu, so the user gets an error message and returns to the menu.

## Battle rules

- **Opponent:** randomly chosen from the 6 starter Pokémon, but never the same character as the
  player's fighter.
- **Turn order:** a coin flip decides who goes first, then the sides take turns. This gives both
  sides the same chance to start, regardless of which Pokémon was chosen. The result of the coin
  flip is shown in the battle log, and every turn gets its own header.
- **Hit:** `random.nextInt(100) < accuracy`. If the attack misses, nothing happens.
- **The CPU** picks a random attack from its own attacks.
- The battle ends when a Pokémon reaches 0 HP. HP is restored before and after every battle.

### Damage formula
```
damage = round(baseDamage × typeMultiplier × critical)
```
- **Type multiplier:** 2.0 (super effective), 1.0 (normal), 0.5 (not very effective)
- **Critical:** ×2 with a 1/16 chance, otherwise ×1. Shown in the battle log.

Example: Arrow (Fire, 28) against Carol (Grass) = 28 × 2.0 = **56**. With a critical hit:
28 × 2.0 × 2 = **112**.

### Type chart
| Attack → Defender | Fire | Water | Grass | Electric | Normal |
|---|---|---|---|---|---|
| **Fire** | 1 | 0.5 | 2 | 1 | 1 |
| **Water** | 2 | 1 | 0.5 | 1 | 1 |
| **Grass** | 0.5 | 2 | 1 | 1 | 1 |
| **Electric** | 1 | 2 | 1 | 1 | 1 |
| **Normal** | 1 | 1 | 1 | 1 | 1 |

## Persistence
- `pokedex.json`: all Pokémon with their attacks
- `stats.json`: wins, losses, win/loss per Pokémon, most used attack and Pokémon

If a file is missing on startup, the Pokédex is seeded and the statistics start at zero. If a file
is corrupt, a message is shown and the program continues with starter data. Everything is saved
automatically on exit. The save files are listed in `.gitignore`, since the program creates them.

## Crash test
| Scenario | Handling |
|---|---|
| Battle without any Pokémon | Check in `startBattle()` |
| 0 attacks / 0 HP | Impossible thanks to constructor validation; HP is restored before battle |
| Invalid attack index (-1, 99, "abc") | `InputHelper.readIntInRange` asks again |
| Corrupt or missing JSON | `try/catch` in the repository classes, fallback to starter data |
| Input closed mid-battle (Ctrl+D) | `NoSuchElementException` caught in `ConsoleMenu.run()`, data is saved |
| Statistics without any battles | Check in `showResults()` |
| Pokémon with only healing attacks | Rejected by the `Pokemon` constructor, so a battle can never get stuck |

## VG requirements I have attempted
- [x] Type effectiveness via a type chart (`Type.effectivenessAgainst`)
- [x] JSON via Jackson
- [x] Critical hits (1/16, ×2) shown in the battle log
- [x] Persistent statistics: wins/losses, ratio per Pokémon, most used attack and Pokémon
- [x] Clear OOP architecture: abstract class, interface, separation of concerns, custom exceptions

## Known limitations
- Wild opponents always come from the starter pool, not from Pokémon the player has added.
- `RandomStrategy` may choose to heal even at full HP.
- Statistics are tied to the name: renaming a Pokémon makes it count as a new one.
- Keys pressed during the countdown are buffered and may be used as the next choice.
- In the edit menu (3), the edited Pokémon is only validated when you choose
  "0. Save changes and go back". It is rejected if it has no damage attack left, or if its new
  name already belongs to another Pokémon. An error is then shown and **every** change from that
  edit session is discarded, including valid ones such as a new max HP. The original Pokémon
  stays unchanged.


## Author

Magnus Hagerström – student at Jensen YH, *Programmering med Java, grund* (2026)
