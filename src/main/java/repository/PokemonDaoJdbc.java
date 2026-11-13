package repository;

import config.DatabaseConfig;
import model.Attack;
import model.DamageAttack;
import model.HealAttack;
import model.Pokemon;
import model.Type;
import java.sql.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PokemonDaoJdbc implements PokemonDao {

    private static final String FIND_ALL_SQL = """
            SELECT p.pokemon_id,
                   p.name        AS pokemon_name,
                   p.max_hp,
                   ptype.name    AS pokemon_type,
                   a.name        AS attack_name,
                   a.kind,
                   a.accuracy,
                   a.base_damage,
                   a.heal_amount,
                   atype.name    AS attack_type
            FROM pokemon p
            JOIN type ptype        ON p.type_id = ptype.type_id
            JOIN pokemon_attack pa ON p.pokemon_id = pa.pokemon_id
            JOIN attack a          ON pa.attack_id = a.attack_id
            LEFT JOIN type atype   ON a.type_id = atype.type_id
            ORDER BY p.pokemon_id, a.attack_id
            """;

    private static final String INSERT_POKEMON_SQL = """
            INSERT INTO pokemon (name, max_hp, type_id)
            VALUES (?, ?, (SELECT type_id FROM type WHERE name = ?))
            """;

    private static final String INSERT_ATTACK_SQL = """
            INSERT INTO attack (name, kind, accuracy, base_damage, heal_amount, type_id)
            VALUES (?, ?, ?, ?, ?, (SELECT type_id FROM type WHERE name = ?))
            ON CONFLICT (name) DO NOTHING
            """;

    private static final String LINK_ATTACK_SQL = """
            INSERT INTO pokemon_attack (pokemon_id, attack_id)
            VALUES (?, (SELECT attack_id FROM attack WHERE name = ?))
            """;



    private record PokemonParts(String name, Type type, int maxHp, List<Attack> attacks) {
    }

    @Override
    public List<Pokemon> findAll() {
        Map<Integer, PokemonParts> partsById = new LinkedHashMap<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("pokemon_id");

                PokemonParts parts = partsById.get(id);
                if (parts == null) {
                    parts = new PokemonParts(
                            rs.getString("pokemon_name"),
                            Type.valueOf(rs.getString("pokemon_type")),
                            rs.getInt("max_hp"),
                            new ArrayList<>());
                    partsById.put(id, parts);
                }

                parts.attacks().add(mapAttack(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not load Pokémon from the database", e);
        }

        List<Pokemon> result = new ArrayList<>();
        for (PokemonParts parts : partsById.values()) {
            result.add(new Pokemon(parts.name(), parts.type(), parts.maxHp(), parts.attacks()));
        }
        return result;
    }

    @Override
    public void save(Pokemon pokemon) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int pokemonId = insertPokemon(conn, pokemon);
                for (Attack attack : pokemon.getAttacks()) {
                    insertAttackIfMissing(conn, attack);
                    linkAttack(conn, pokemonId, attack.getName());
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not save " + pokemon.getName() + " to the database", e);
        }
    }

    private int insertPokemon(Connection conn, Pokemon pokemon) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(INSERT_POKEMON_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, pokemon.getName());
            ps.setInt(2, pokemon.getMaxHp());
            ps.setString(3, pokemon.getType().name());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    private void insertAttackIfMissing(Connection conn, Attack attack) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(INSERT_ATTACK_SQL)) {
            ps.setString(1, attack.getName());
            ps.setInt(3, attack.getAccuracy());

            if (attack instanceof DamageAttack damage) {
                ps.setString(2, "DAMAGE");
                ps.setInt(4, damage.getBaseDamage());
                ps.setInt(5, 0);
                ps.setString(6, damage.getType().name());
            } else if (attack instanceof HealAttack heal) {
                ps.setString(2, "HEAL");
                ps.setInt(4, 0);
                ps.setInt(5, heal.getHealAmount());
                ps.setString(6, null);
            }
            ps.executeUpdate();
        }
    }

    private void linkAttack(Connection conn, int pokemonId, String attackName) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(LINK_ATTACK_SQL)) {
            ps.setInt(1, pokemonId);
            ps.setString(2, attackName);
            ps.executeUpdate();
        }
    }

    private Attack mapAttack(ResultSet rs) throws SQLException {
        String name = rs.getString("attack_name");
        int accuracy = rs.getInt("accuracy");

        if ("HEAL".equals(rs.getString("kind"))) {
            return new HealAttack(name, rs.getInt("heal_amount"), accuracy);
        }
        return new DamageAttack(
                name,
                rs.getInt("base_damage"),
                accuracy,
                Type.valueOf(rs.getString("attack_type")));
    }
}