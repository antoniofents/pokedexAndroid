package com.example.afentanes.pokedexandroid.client;

import android.telecom.Call;

import com.example.afentanes.pokedexandroid.model.PokemonListWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by afentanes on 11/15/17.
 */

public interface PokemonClient {



    @GET("/api/v2/pokemon/?limit=150")
    public void getPokemonList(Callback<PokemonListWrapper> response);

    @GET("/api/v2/ability/{id}/")
    public void getPokemonAbilities(@Path("id") String id,   Callback <JSONObject> callback);

    @GET("/api/v2/characteristic/{id}")
    public void getPokemonCharasteristic(@Path("id") String id,   Callback <JSONObject> callback);






}
