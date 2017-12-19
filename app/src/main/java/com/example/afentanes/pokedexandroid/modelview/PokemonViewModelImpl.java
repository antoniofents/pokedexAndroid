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
import com.example.afentanes.pokedexandroid.model.EffectEntry;
import com.example.afentanes.pokedexandroid.model.Pokemon;
import com.example.afentanes.pokedexandroid.model.PokemonListWrapper;
import com.example.afentanes.pokedexandroid.util.PokemonUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PokemonViewModelImpl extends AndroidViewModel implements PokemonViewModel {


    private List<Pokemon> pokemons= new ArrayList<>();
    private MutableLiveData<List<Pokemon>> pokemonList;
    private MutableLiveData<Pokemon> pokemonSelected;
    private MutableLiveData<String> userLogged;
    private MutableLiveData<Boolean> loading;
    private LiveData<PagedList<Pokemon>> pokemonPagedList;
    public PokemonViewModelImpl(Application app){
        super(app);
        initPokemonList();
        checkUserlogin();
    }


    public void getFilteredResults(String constraint) {
        Log.i(this.getClass().toString() , "filter pokemons");
        if(constraint.length()>0){
            List<Pokemon> result = new ArrayList<>();
            for(Pokemon pokF: pokemons){
                if( pokF.name.toLowerCase().contains(constraint.toLowerCase())){
                    result.add(pokF);
                }
            }
           getPokemonList().setValue( result);
            return;
        }
        getPokemonList().setValue(pokemons);
    }

    public void initPokemonList() {

        getLoading().setValue(true);
        OkHttpClient okHttpClient=  new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).build();

        Retrofit adapter = new Retrofit.Builder().baseUrl(PokemonUtil.ROOT_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
        PokemonClient pokeClient = adapter.create(PokemonClient.class);

        Log.i("RETROFIT   :", "LOADING POKEMONS");
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

    private void checkUserlogin(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            getUserLogged().setValue(currentUser.getEmail());
        }
    }

    public void initPokemon(Pokemon pokemon){
        pokemon.id=pokemon.url.split(PokemonUtil.patternPokemonIdUrl)[2];
        pokemon.frontUrl= "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+pokemon.id+".png";
    }


    @Override
    public void pokemonSelected(final Pokemon pokemon) {
        Log.i(this.getClass().toString(), "pokemon selected: " + pokemon.name);
        getLoading().setValue(true);

        OkHttpClient.Builder httpClientBuilder= new OkHttpClient.Builder();
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

        OkHttpClient okHttpClient=  httpClientBuilder.connectTimeout(5, TimeUnit.SECONDS).build();

        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(PokemonUtil.ROOT_URL).addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        final PokemonClient pokeClient = adapter.create(PokemonClient.class);

        pokeClient.getPokemonAbilities(pokemon.id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray effectEntries = null;
                try {
                    if (response.body().has("effect_entries")) {
                        effectEntries = response.body().getAsJsonArray("effect_entries");
                        pokemon.effectEntries = new ArrayList<EffectEntry>();
                        for (int i = 0; i < effectEntries.size(); i++) {
                            JsonObject entry = effectEntries.get(i).getAsJsonObject();
                            pokemon.effectEntries.add(new EffectEntry(entry.get("short_effect").getAsString(), entry.get("effect").getAsString()));
                        }
                    }
                            pokemonSelected.setValue(pokemon);
                } catch (JsonIOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }

        });

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
                            return pokemonList.getValue().size();
                        }

                        @Override
                        public List<Pokemon> loadRange(int startPosition, int count) {
                            return pokemonList.getValue().subList(startPosition, startPosition + count);
                        }
                    };
                }
            }.create(0, new PagedList.Config.Builder()
                    .setPageSize(9)
                    .setPageSize(pokemonList.getValue().size())
                    .setEnablePlaceholders(true)
                    .setPrefetchDistance(5)
                    .build());

    }

    public MutableLiveData<String> getUserLogged() {
        if(userLogged==null){
            userLogged= new MutableLiveData<>();
        }
        return userLogged;
    }

    public void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        userLogged.setValue(null);
    }

    public MutableLiveData<Boolean> getLoading() {
        if(loading==null){
            loading=new MutableLiveData<>();
        }
        return loading;
    }
}
