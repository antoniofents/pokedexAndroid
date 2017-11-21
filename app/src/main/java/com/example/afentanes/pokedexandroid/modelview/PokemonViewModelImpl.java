package com.example.afentanes.pokedexandroid.modelview;


import android.util.Log;

import com.example.afentanes.pokedexandroid.client.PokemonClient;
import com.example.afentanes.pokedexandroid.model.Pokemon;
import com.example.afentanes.pokedexandroid.model.PokemonListWrapper;
import com.example.afentanes.pokedexandroid.util.PokemonUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by afentanes on 11/20/17.
 */

public class PokemonViewModelImpl implements PokemonViewModel {


    private final PokemonView pokemonView;

    public PokemonViewModelImpl(PokemonView pokemonView) {
        this.pokemonView = pokemonView;
    }

    @Override
    public void onSearchQueryChanged() {

    }

    public List<Pokemon> getFilteredResults(String constraint, List<Pokemon> pokemons) {


        if(constraint.length()>0){
            List<Pokemon> results = new ArrayList<>();
            for (Pokemon item : pokemons) {
                if (item.name.toLowerCase().contains(constraint)) {
                    results.add(item);
                }
            }
            return results;
        }
        return pokemons;
    }

    public void initPokemon() {



        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(PokemonUtil.ROOT_URL).build();
        PokemonClient pokeClient = adapter.create(PokemonClient.class);
        pokeClient.getPokemonList(new Callback<PokemonListWrapper>() {
            @Override
            public void success(PokemonListWrapper pokemonListWrapper, Response response) {
                Log.i("RETROFIT   :", "loading pokemons success");
                List<Pokemon> pokemonsAvailable = pokemonListWrapper.results;
                pokemonsAvailable.stream().forEach(Pokemon::initPokemon);
                pokemonView.updatePokemonList(pokemonsAvailable);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("RETROFIT   :", "failure");

            }
        });

    }
}
