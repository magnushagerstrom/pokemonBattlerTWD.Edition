package repository;

import config.DatabaseConfig;
import model.Attack;
import model.DamageAttack;
import model.HealAttack;
import model.Pokemon;
import model.Type;

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