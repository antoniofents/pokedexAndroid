package com.example.afentanes.pokedexandroid;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.afentanes.pokedexandroid.adapter.PokemonViewAdapter;
import com.example.afentanes.pokedexandroid.databinding.ActivityMainBinding;
import com.example.afentanes.pokedexandroid.model.Pokemon;
import com.example.afentanes.pokedexandroid.modelview.PokemonView;
import com.example.afentanes.pokedexandroid.modelview.PokemonViewModelImpl;
import com.example.afentanes.pokedexandroid.util.PokemonUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PokemonView,LifecycleOwner {

    PokemonViewModelImpl pokemonViewModel;
    PokemonView pokemonView;
    PokemonViewAdapter adapter;
    ActivityMainBinding binding;
    private LifecycleRegistry mLifecycleRegistry;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        pokemonViewModel = ViewModelProviders.of(this).get(PokemonViewModelImpl.class);
        addObservers();
        binding.setPokemonViewModel(pokemonViewModel);
        initNavigationBar();
    }

    private void addObservers() {
        pokemonViewModel.getPokemonList().observe(this, pokemons -> {if(pokemons!=null)updatePokemonList(pokemons);});
        pokemonViewModel.getPokemonSelected().observe(this, pokemon -> {displayPokemonDescription(pokemon);});
        RxTextView.textChanges((EditText) findViewById(R.id.search_pokemon_text)).subscribe(text -> {
            pokemonViewModel.getFilteredResults(String.valueOf(text));
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        showLogin(currentUser);


    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().signOut();
    }

    public void showLogin(FirebaseUser firebaseUser) {
        if (firebaseUser == null) {
            LoginFragment loginFragment = new LoginFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, loginFragment, "login");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
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
        } else {
            adapter.setFilteredList(pokemons);
            adapter.notifyDataSetChanged();
        }

    }



    @Override
    public void displayPokemonDescription(Pokemon pokemon) {
        Intent intent = new Intent(MainActivity.this.getContext(), PokemonDescActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PokemonUtil.POKEMON_BUNDLE, pokemon);
        intent.putExtras(bundle);
        intent.putExtra(PokemonUtil.POKEMON_BUNDLE, pokemon);
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
