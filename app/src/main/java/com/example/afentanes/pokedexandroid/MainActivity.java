package com.example.afentanes.pokedexandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.afentanes.pokedexandroid.adapter.PokemonViewAdapter;
import com.example.afentanes.pokedexandroid.client.PokemonClient;
import com.example.afentanes.pokedexandroid.model.Pokemon;
import com.example.afentanes.pokedexandroid.model.PokemonListWrapper;
import com.example.afentanes.pokedexandroid.modelview.PokemonView;
import com.example.afentanes.pokedexandroid.modelview.PokemonViewModel;
import com.example.afentanes.pokedexandroid.modelview.PokemonViewModelImpl;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements PokemonView {

    private TextView mTextMessage;
    List<Pokemon> pokemonsAvailable;
    PokemonViewModel pokemonViewModel= new PokemonViewModelImpl(this) ;
    PokemonViewAdapter adapter ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        pokemonViewModel.initPokemon();
    }




    public void updatePokemonList(List<Pokemon> pokemons) {
        if(adapter==null){

            RecyclerView pokemonListView = (RecyclerView) findViewById(R.id.pokemon_list);
            pokemonListView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            adapter = new PokemonViewAdapter(pokemons, MainActivity.this);
            pokemonListView.setAdapter(adapter);
            pokemonListView.setHasFixedSize(true);
            addObserverForSearchField();
            pokemonsAvailable=pokemons;
        }else{
            adapter.setFilteredList(pokemons);
            adapter.notifyDataSetChanged();
        }

    }

    private void addObserverForSearchField() {

        SearchView searchView = (SearchView) findViewById(R.id.search_pokemon_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               updatePokemonList(pokemonViewModel.getFilteredResults(query, pokemonsAvailable));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }

    };
}
