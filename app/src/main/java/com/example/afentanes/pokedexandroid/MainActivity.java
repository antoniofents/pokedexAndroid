package com.example.afentanes.pokedexandroid;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.afentanes.pokedexandroid.adapter.PokemonListAdapter;
import com.example.afentanes.pokedexandroid.databinding.ActivityMainBinding;
import com.example.afentanes.pokedexandroid.model.Pokemon;
import com.example.afentanes.pokedexandroid.modelview.PokemonViewModelImpl;
import com.example.afentanes.pokedexandroid.util.PokemonUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements  LifecycleOwner {

    PokemonViewModelImpl pokemonViewModel;
    PokemonListAdapter adapter;
    ActivityMainBinding binding;
    private LifecycleRegistry mLifecycleRegistry;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        displayLoadingFragment();
        pokemonViewModel = ViewModelProviders.of(this).get(PokemonViewModelImpl.class);
        addObservers();
        binding.setPokemonViewModel(pokemonViewModel);

    }

    private void addObservers() {
        pokemonViewModel.getPokemonList().observe(this, this::updatePokemonList);
        pokemonViewModel.getPokemonSelected().observe(this, pokemon -> {displayPokemonDescription(pokemon);});
        RxTextView.textChanges((EditText) findViewById(R.id.search_pokemon_text)).subscribe(text -> {
            if(adapter!=null)
            pokemonViewModel.getFilteredResults(String.valueOf(text));
        });
        pokemonViewModel.getUserLogged().observe(this, user->{userChanged(user);});
        pokemonViewModel.getLoading().observe(this, loading -> {if(loading){displayLoadingFragment();}else{hideLoadingFragment();}});

    }



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().signOut();
    }



    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }



    public void updatePokemonList(List<Pokemon> pokemons) {
        if (adapter == null) {
            if(pokemons.size()>0){
                adapter = new PokemonListAdapter(pokemonViewModel);
                pokemonViewModel.getPokemonPagedList().observe(this, pokemonList -> {
                    ((PokemonListAdapter)adapter).setList(pokemonList);
                    RecyclerView pokemonListView = (RecyclerView) findViewById(R.id.pokemon_list);
                    pokemonListView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    pokemonListView.setAdapter(adapter);
                    pokemonListView.setHasFixedSize(true);
                    pokemonViewModel.getLoading().setValue(false);
                });
            }
        } else {
            TextInputLayout search_label = (TextInputLayout) findViewById(R.id.input_layout_search);
            if(pokemons.size()==0){
                search_label.setError(getString(R.string.not_found));
                search_label.requestFocus();
            }else{
                search_label.setErrorEnabled(false);
            }
            adapter.notifyDataSetChanged();
            pokemonViewModel.getLoading().setValue(false);
        }

    }

    public void displayPokemonDescription(Pokemon pokemon) {
        PokemonDescDialog pokemonDescDialog= new PokemonDescDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PokemonUtil.POKEMON_BUNDLE, pokemon);
        pokemonDescDialog.setArguments(bundle);
        pokemonViewModel.getLoading().setValue(false);
        pokemonDescDialog.show(getSupportFragmentManager(),"pokemonDesc");
    }


    private void userChanged(String user){
        TextView userLabel = findViewById(R.id.user_id_label);
        Button logout = findViewById(R.id.log_out);
        if(user!=null && !user.isEmpty()){
            logout.setVisibility(View.VISIBLE);
            logout.setOnClickListener(view -> {
                pokemonViewModel.logoutUser();
            });
            userLabel.setText(user);
        }else{
            userLabel.setText("non logged user");
            logout.setVisibility(View.INVISIBLE);
        }

    }

    public Context getContext() {
        return  this.getApplicationContext();
    }



    public void displayLoadingFragment(){
        LoadingFragment loadingFragment= new LoadingFragment();
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, loadingFragment,"loading");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void hideLoadingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.hide(getSupportFragmentManager().findFragmentByTag("loading"));
        ft.commit();
    }
}
