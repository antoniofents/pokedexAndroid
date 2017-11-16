package com.example.afentanes.pokedexandroid.client;

import com.example.afentanes.pokedexandroid.model.PokemonListWrapper;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by afentanes on 11/15/17.
 */

public interface PokemonClient {



    @GET("/api/v2/pokemon/?limit=150")
    public void getPokemonList(Callback<PokemonListWrapper> response);


}
