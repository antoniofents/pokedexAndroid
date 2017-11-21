package com.example.afentanes.pokedexandroid.modelview;

import com.example.afentanes.pokedexandroid.model.Pokemon;

import java.util.List;

/**
 * Created by afentanes on 11/20/17.
 */

public interface PokemonViewModel {

     void onSearchQueryChanged();
     List<Pokemon> getFilteredResults(String constraint, List <Pokemon> pokemons);
     public void initPokemon();
}
