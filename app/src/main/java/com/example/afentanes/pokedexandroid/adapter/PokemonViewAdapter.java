package com.example.afentanes.pokedexandroid.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.afentanes.pokedexandroid.R;
import com.example.afentanes.pokedexandroid.databinding.PokemonViewBinding;
import com.example.afentanes.pokedexandroid.model.Pokemon;
import com.example.afentanes.pokedexandroid.modelview.PokemonViewModel;

import java.util.List;



public class PokemonViewAdapter extends RecyclerView.Adapter <PokemonViewAdapter.PokemonViewHolder>{

    private List<Pokemon> filteredList;
    private PokemonViewModel pokemonViewModel;

    public PokemonViewAdapter(List<Pokemon> pokemonList, PokemonViewModel pokemonViewModel){
        this.filteredList =pokemonList;
        this.pokemonViewModel=pokemonViewModel;
    }


    class PokemonViewHolder extends RecyclerView.ViewHolder{
        PokemonViewBinding pokemonViewBinding;

        PokemonViewHolder(PokemonViewBinding binding) {
            super(binding.getRoot());
            this.pokemonViewBinding= binding;
        }

        public void bind(@NonNull Pokemon pokemon) {
            pokemonViewBinding.setPokemon(pokemon);
            pokemonViewBinding.setPokemonViewModel(pokemonViewModel);
            pokemonViewBinding.executePendingBindings();
        }
    }
    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PokemonViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.pokemon_view, parent, false);
        return new PokemonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder holder, int position) {
        Pokemon pokemon= filteredList.get(position);
        holder.bind(pokemon);
        pokemonViewModel.getImage(pokemon.frontUrl).into(holder.pokemonViewBinding.pokeImage);

    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void setFilteredList(List <Pokemon> pokemons){
        this.filteredList=pokemons;

    }
}
