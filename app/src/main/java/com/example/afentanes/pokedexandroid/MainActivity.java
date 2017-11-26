package com.example.afentanes.pokedexandroid;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.afentanes.pokedexandroid.adapter.PokemonViewAdapter;
import com.example.afentanes.pokedexandroid.client.PokemonClient;
import com.example.afentanes.pokedexandroid.databinding.ActivityMainBinding;
import com.example.afentanes.pokedexandroid.model.Pokemon;
import com.example.afentanes.pokedexandroid.model.PokemonListWrapper;
import com.example.afentanes.pokedexandroid.modelview.PokemonView;
import com.example.afentanes.pokedexandroid.modelview.PokemonViewModel;
import com.example.afentanes.pokedexandroid.modelview.PokemonViewModelImpl;
import com.example.afentanes.pokedexandroid.util.PokemonUtil;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.Observer;

public class MainActivity extends AppCompatActivity implements PokemonView {

    List<Pokemon> pokemonsAvailable;
    PokemonViewModel pokemonViewModel;
    PokemonView pokemonView;
    PokemonViewAdapter adapter;
    ActivityMainBinding binding;
    Observable<CharSequence> searchText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        pokemonViewModel = new PokemonViewModelImpl(this);
        binding.setPokemonViewModel(pokemonViewModel);
        initNavigationBar();
        pokemonViewModel.initPokemon();
        addObserverForSearchField();
    }

    private void initNavigationBar() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    public void updatePokemonList(List<Pokemon> pokemons) {
        if (adapter == null) {
            RecyclerView pokemonListView = (RecyclerView) findViewById(R.id.pokemon_list);
            pokemonListView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            adapter = new PokemonViewAdapter(pokemons, MainActivity.this, pokemonViewModel);
            pokemonListView.setAdapter(adapter);
            pokemonListView.setHasFixedSize(true);
            pokemonsAvailable = pokemons;
            binding.setPokemonsAvailable(pokemonsAvailable);
        } else {
            adapter.setFilteredList(pokemons);
            adapter.notifyDataSetChanged();
        }

    }

    private void addObserverForSearchField() {
        EditText searchEditText
                = (EditText) findViewById(R.id.search_pokemon_text);

        searchText= RxTextView.textChanges(searchEditText);
        searchText.subscribe(new Observer<CharSequence>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CharSequence charSequence) {
                if(pokemonsAvailable!=null ){
                    updatePokemonList(pokemonViewModel.getFilteredResults(String.valueOf(charSequence), pokemonsAvailable));
                }
                 }
        });


       /* searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                updatePokemonList(pokemonViewModel.getFilteredResults(query, pokemonsAvailable));
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

        });*/

    }


    @Override
    public void displayPokemonDescription(Pokemon pokemon) {

        Intent intent = new Intent(MainActivity.this.getContext(), PokemonDescActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PokemonUtil.POKEMON_BUNDLE, pokemon);
        intent.putExtras(bundle);
       getApplicationContext().startActivity(intent);
    }

    @Override
    public Context getContext() {
        return  this.getApplicationContext();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
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
