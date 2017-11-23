package com.example.afentanes.pokedexandroid.modelview;

import com.bumptech.glide.BitmapTypeRequest;
import com.example.afentanes.pokedexandroid.model.Pokemon;

import java.util.List;

/**
 * Created by afentanes on 11/20/17.
 */

public interface PokemonViewModel {

     public void onSearchQueryChanged();

     public List<Pokemon> getFilteredResults(String constraint, List <Pokemon> pokemons);

     public void initPokemon();

     public void pokemonSelected(Pokemon pokemon);

     public BitmapTypeRequest getImage(String url);


}
