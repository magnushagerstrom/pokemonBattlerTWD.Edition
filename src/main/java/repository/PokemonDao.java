package repository;

import model.Pokemon;

import java.util.List;

public interface PokemonDao {

    List<Pokemon> findAll();
}