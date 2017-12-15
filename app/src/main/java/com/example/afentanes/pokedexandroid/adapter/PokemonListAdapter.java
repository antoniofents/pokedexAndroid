package com.example.afentanes.pokedexandroid.adapter;

import android.arch.paging.PagedListAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.afentanes.pokedexandroid.R;
import com.example.afentanes.pokedexandroid.databinding.PokemonViewBinding;
import com.example.afentanes.pokedexandroid.model.Pokemon;
import com.example.afentanes.pokedexandroid.modelview.PokemonViewModel;

import java.util.List;

/**
 * Created by afentanes on 12/14/17.
 */

public class PokemonListAdapter extends PagedListAdapter <Pokemon, PokemonListAdapter.PokemonListViewHolder> {

    private PokemonViewModel pokemonViewModel;
    public PokemonListAdapter( PokemonViewModel pokemonViewModel) {
        super(DIFF_CALLBACK);
        this.pokemonViewModel= pokemonViewModel;
    }

    @Override
    public PokemonListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PokemonViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.pokemon_view, parent, false);
        return new PokemonListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PokemonListViewHolder holder, int position) {
        Pokemon pokemon= getItem(position);
        holder.bind(pokemon);
        pokemonViewModel.getImage(pokemon.frontUrl).into(holder.pokemonViewBinding.pokeImage);

    }

    class PokemonListViewHolder extends RecyclerView.ViewHolder{
        PokemonViewBinding pokemonViewBinding;

        PokemonListViewHolder(PokemonViewBinding binding) {
            super(binding.getRoot());
            this.pokemonViewBinding= binding;
        }

        public void bind(@NonNull Pokemon pokemon) {
            pokemonViewBinding.setPokemonSelected(pokemon);
            pokemonViewBinding.setPokemonViewModel(pokemonViewModel);
            pokemonViewBinding.executePendingBindings();
        }
    }


    public static final DiffCallback<Pokemon> DIFF_CALLBACK = new DiffCallback<Pokemon>() {
        @Override
        public boolean areItemsTheSame(@NonNull Pokemon oldPokemon, @NonNull Pokemon newPokemon) {
            return oldPokemon.id == newPokemon.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Pokemon oldPokemon, @NonNull Pokemon newPokemon) {
            return oldPokemon.name.equals(newPokemon.name);
        }
    };
}
