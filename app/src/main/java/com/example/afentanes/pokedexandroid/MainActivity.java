package com.example.afentanes.pokedexandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.afentanes.pokedexandroid.client.PokemonClient;
import com.example.afentanes.pokedexandroid.model.Pokemon;
import com.example.afentanes.pokedexandroid.model.PokemonListWrapper;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    initPokemonsRetrofit();
                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private void initPokemonsRetrofit(){
        String ROOT_URL = "https://pokeapi.co/";
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(ROOT_URL).build();
        PokemonClient pokeClient= adapter.create(PokemonClient.class);
        pokeClient.getPokemonList(new Callback <PokemonListWrapper>() {
            @Override
            public void success(PokemonListWrapper pokemonListWrapper, Response response) {
                Log.i("RETROFIT   :", "loading pokemons success");
                pokemonListWrapper.results.stream().forEach(Pokemon::initPokemon);
                RecyclerView pokemonList = (RecyclerView) findViewById(R.id.pokemon_list);
                pokemonList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                pokemonList.setAdapter(new PokemonViewAdapter(pokemonListWrapper.results,MainActivity.this));
                pokemonList.setHasFixedSize(true);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("RETROFIT   :", "failure");

            }
        });
    }
}
