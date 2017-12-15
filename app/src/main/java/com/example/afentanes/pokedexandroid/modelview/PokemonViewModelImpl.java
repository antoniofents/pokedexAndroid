package com.example.afentanes.pokedexandroid.modelview;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListProvider;
import android.arch.paging.PagedList;
import android.arch.paging.TiledDataSource;
import android.util.Log;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.example.afentanes.pokedexandroid.client.PokemonClient;
import com.example.afentanes.pokedexandroid.model.Pokemon;
import com.example.afentanes.pokedexandroid.model.PokemonListWrapper;
import com.example.afentanes.pokedexandroid.util.PokemonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PokemonViewModelImpl extends AndroidViewModel implements PokemonViewModel {


    private List<Pokemon> pokemons= new ArrayList<>();
    private MutableLiveData<List<Pokemon>> pokemonList;
    private MutableLiveData<Pokemon> pokemonSelected;
   // private LivePagedListProvider<Integer, Pokemon> pokemonPagedList;
    private LiveData<PagedList<Pokemon>> pokemonPagedList;
    public PokemonViewModelImpl(Application app){
        super(app);
        initPokemonList();
    }


    public void getFilteredResults(String constraint) {

        Log.i(this.getClass().toString() , "filter pokemons");
        if(constraint.length()>0){
            List<Pokemon> result = new ArrayList<>();
            pokemonList.setValue(pokemons.stream().filter(pokemon -> pokemon.name.toLowerCase().contains(constraint)).collect(Collectors.toList()));
            return;
        }
        getPokemonList().setValue(pokemons);
    }

    public void initPokemonList() {

        Retrofit adapter = new Retrofit.Builder().baseUrl(PokemonUtil.ROOT_URL).addConverterFactory(GsonConverterFactory.create()).build();
        PokemonClient pokeClient = adapter.create(PokemonClient.class);


        Call<PokemonListWrapper> pokemonCall = pokeClient.getPokemonList();
        pokemonCall.enqueue(new Callback<PokemonListWrapper>() {
            @Override
            public void onResponse(Call<PokemonListWrapper> call, Response<PokemonListWrapper> response) {
                Log.i("RETROFIT   :", "loading pokemons success");
                List<Pokemon> pokemonsAvailable = response.body().results;
                for(Pokemon pokemon : pokemonsAvailable){
                    initPokemon(pokemon);
                }
                pokemons=pokemonsAvailable;
                getPokemonList().setValue(pokemons);
                initLiveDataPokemonList();
            }
            @Override
            public void onFailure(Call<PokemonListWrapper> call, Throwable t) {
                Log.i("RETROFIT   :", "error");
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

        /*OkHttpClient.Builder httpClientBuilder= new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {

                Request originalRequest= chain.request();
                Request request = originalRequest.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .method(originalRequest.method(), originalRequest.body()).build();
                return chain.proceed(request);
            }
        });

        OkHttpClient okHttpClient=  httpClientBuilder.build();

        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(PokemonUtil.ROOT_URL).addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        final PokemonClient pokeClient = adapter.create(PokemonClient.class);

        pokeClient.getPokemonAbilities(pokemon.id).enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                JSONArray effectEntries = null;
                try {
                    if (response.body().has("effect_entries")) {
                        effectEntries = response.body().getJSONArray("effect_entries");
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
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.i("RETROFIT   :", "failure");
                t.printStackTrace();
            }
        });*/

        Retrofit adapterCharacteristics = new Retrofit.Builder()
                .baseUrl(PokemonUtil.ROOT_URL).addConverterFactory(GsonConverterFactory.create())
                .build();
        adapterCharacteristics.create(PokemonClient.class).getPokemonCharasteristic(pokemon.id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.body().has("descriptions")) {
                        JsonArray descriptions = response.body().getAsJsonArray("descriptions");

                        pokemon.characteristics = new ArrayList<String>();
                        for (int i = 0; i < descriptions.size(); i++) {
                            pokemon.characteristics.add(descriptions.get(i).getAsJsonObject().get("description").getAsString());
                        }
                    }
                    pokemonSelected.setValue(pokemon);
                } catch (Exception e) {
                    Log.i("RETROFIT   :", "failure");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });



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

    public LiveData<PagedList<Pokemon>> getPokemonPagedList() {
        if(pokemonPagedList==null){
            initLiveDataPokemonList();
        }
        return pokemonPagedList;
    }

    private void initLiveDataPokemonList() {
            pokemonPagedList = new LivePagedListProvider<Integer, Pokemon>() {
                @Override
                protected DataSource<Integer, Pokemon> createDataSource() {
                    return new TiledDataSource<Pokemon>() {
                        @Override
                        public int countItems() {
                            return pokemons.size();
                        }

                        @Override
                        public List<Pokemon> loadRange(int startPosition, int count) {
                            return pokemons.subList(startPosition, startPosition + count);
                        }
                    };
                }
            }.create(0, new PagedList.Config.Builder().setPageSize(9).setPageSize(pokemons.size()).setEnablePlaceholders(true).setPrefetchDistance(5).build());

    }
}
