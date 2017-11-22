package com.example.afentanes.pokedexandroid.modelview;

import com.example.afentanes.pokedexandroid.model.Pokemon;

import java.util.List;

/**
 * Created by afentanes on 11/20/17.
 */

public interface PokemonView {
    public void updatePokemonList(List<Pokemon> pokemons);
    public void displayPokemonDescription(Pokemon pokemon);

}
