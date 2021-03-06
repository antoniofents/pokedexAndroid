package com.example.afentanes.pokedexandroid.client;


import com.example.afentanes.pokedexandroid.model.PokemonListWrapper;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by afentanes on 11/15/17.
 */

public interface PokemonClient {



    @GET("/api/v2/pokemon/?limit=150")
    public Call<PokemonListWrapper> getPokemonList();

    @GET("/api/v2/ability/{id}/")
    public Call<JsonObject> getPokemonAbilities(@Path("id") String id);

    @GET("/api/v2/characteristic/{id}")
    public  Call <JsonObject>  getPokemonCharasteristic(@Path("id") String id);








}
