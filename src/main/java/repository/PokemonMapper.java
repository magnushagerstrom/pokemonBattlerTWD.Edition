package repository;

import exception.InvalidPokemonDataException;
import model.Attack;
import model.DamageAttack;
import model.HealAttack;
import model.Pokemon;

import java.util.ArrayList;
import java.util.List;

public class PokemonMapper {

    private PokemonMapper() {
    }

    // ===== Packlista "att spara" =====

    public static AttackData toAttackData(Attack attack) {
        AttackData data = new AttackData();
        data.name = attack.getName();
        data.accuracy = attack.getAccuracy();

        if (attack instanceof DamageAttack damageAttack) {
            data.kind = "DAMAGE";
            data.baseDamage = damageAttack.getBaseDamage();
            data.type = damageAttack.getType();
        } else if (attack instanceof HealAttack healAttack) {
            data.kind = "HEAL";
            data.healAmount = healAttack.getHealAmount();
        }
        return data;
    }

    public static PokemonData toPokemonData(Pokemon pokemon) {
        PokemonData data = new PokemonData();
        data.name = pokemon.getName();
        data.type = pokemon.getType();
        data.maxHp = pokemon.getMaxHp();
        for (Attack attack : pokemon.getAttacks()) {
            data.attacks.add(toAttackData(attack));
        }
        return data;
    }

    // ===== PACKAT EFTER LISTA "att packa upp" =====

    public static Attack toAttack(AttackData data) {
        if (data == null) {
            throw new InvalidPokemonDataException("Attack data is missing");
        }
        if ("DAMAGE".equals(data.kind)) {
            return new DamageAttack(data.name, data.baseDamage, data.accuracy, data.type);
        }
        if ("HEAL".equals(data.kind)) {
            return new HealAttack(data.name, data.healAmount, data.accuracy);
        }
        throw new InvalidPokemonDataException("Unknown attack kind: " + data.kind);
    }

    public static Pokemon toPokemon(PokemonData data) {
        if (data == null || data.attacks == null) {
            throw new InvalidPokemonDataException("Pokémon data is missing or has no attack list");
        }
        List<Attack> attacks = new ArrayList<>();
        for (AttackData attackData : data.attacks) {
            attacks.add(toAttack(attackData));
        }
        return new Pokemon(data.name, data.type, data.maxHp, attacks);
    }
}