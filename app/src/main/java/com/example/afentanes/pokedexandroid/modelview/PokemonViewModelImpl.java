package com.example.afentanes.pokedexandroid.modelview;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.example.afentanes.pokedexandroid.client.PokemonClient;
import com.example.afentanes.pokedexandroid.model.EffectEntry;
import com.example.afentanes.pokedexandroid.model.Pokemon;
import com.example.afentanes.pokedexandroid.model.PokemonListWrapper;
import com.example.afentanes.pokedexandroid.util.PokemonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jakewharton.rxbinding.widget.RxTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;


public class PokemonViewModelImpl extends AndroidViewModel implements PokemonViewModel {


    private List<Pokemon> pokemons;
    private MutableLiveData<List<Pokemon>> pokemonList;
    private MutableLiveData<Pokemon> pokemonSelected;
    public PokemonViewModelImpl(Application app){
        super(app);
        initPokemon();
    }


    @Override
    public void onSearchQueryChanged() {

    }

    public void getFilteredResults(String constraint) {

        Log.i(this.getClass().toString() , "filter pokemons");
        if(constraint.length()>0){
            List<Pokemon> result = new ArrayList<>();
            pokemonList.setValue(pokemons.stream().filter(pokemon -> pokemon.name.toLowerCase().contains(constraint)).collect(Collectors.toList()));
            return;
        }
        pokemonList.setValue(pokemons);
    }

    public void initPokemon() {

        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(PokemonUtil.ROOT_URL).build();
        PokemonClient pokeClient = adapter.create(PokemonClient.class);

        pokeClient.getPokemonList(new Callback<PokemonListWrapper>() {
            @Override
            public void success(PokemonListWrapper pokemonListWrapper, Response response) {
                Log.i("RETROFIT   :", "loading pokemons success");
                List<Pokemon> pokemonsAvailable = pokemonListWrapper.results;
                for(Pokemon pokemon : pokemonsAvailable){
                    initPokemon(pokemon);
                }
                pokemons=pokemonsAvailable;
                pokemonList.setValue(pokemons);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("RETROFIT   :", "failure");

            }
        });

    }

    public void initPokemon(Pokemon pokemon){
        pokemon.id=pokemon.url.split(PokemonUtil.patternPokemonIdUrl)[2];
        pokemon.frontUrl= "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+pokemon.id+".png";
    }


    @Override
    public void pokemonSelected(final Pokemon pokemon) {
        Log.i(this.getClass().toString(), "pokemon selected: " + pokemon.name);

        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(PokemonUtil.ROOT_URL).build();
        final PokemonClient pokeClient = adapter.create(PokemonClient.class);
        pokeClient.getPokemonAbilities(pokemon.id,
                new Callback<JSONObject>() {
                    @Override
                    public void success(JSONObject result, Response response) {

                        JSONArray effectEntries = null;
                        try {
                            if (result.has("effect_entries")) {
                                effectEntries = result.getJSONArray("effect_entries");
                                pokemon.effectEntries = new ArrayList<EffectEntry>();
                                for (int i = 0; i < effectEntries.length(); i++) {
                                    JSONObject entry = effectEntries.getJSONObject(i);
                                    pokemon.effectEntries.add(new EffectEntry(entry.getString("short_effect"), entry.getString("effect")));
                                }
                            }
                            pokemonSelected.setValue(pokemon);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("RETROFIT   :", "failure");

                    }
                });

        pokeClient.getPokemonCharasteristic(pokemon.id,
                new Callback<JSONObject>() {
                    @Override
                    public void success(JSONObject result, Response response) {

                        try {
                            if (result.has("descriptions")) {
                                JSONArray descriptions = result.getJSONArray("descriptions");
                                pokemon.characteristics = new ArrayList<String>();
                                for (int i = 0; i < descriptions.length(); i++) {
                                    pokemon.characteristics.add(descriptions.getJSONObject(i).getString("description"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("RETROFIT   :", "failure");

                    }
                });

        //pokemonView.displayPokemonDescription(pokemon);
    }

    @Override
    public BitmapTypeRequest getImage(String url) {
       return Glide.with(getApplication()).load(url).asBitmap();
    }


    public MutableLiveData<List<Pokemon>> getPokemonList() {
        if(pokemonList == null){
            pokemonList= new MutableLiveData<>();
            pokemonList.setValue(new ArrayList<>());
        }
        return pokemonList;
    }

    public MutableLiveData<Pokemon> getPokemonSelected() {
        if(pokemonSelected == null){
            pokemonSelected= new MutableLiveData<>();
        }
        return pokemonSelected;
    }
}
