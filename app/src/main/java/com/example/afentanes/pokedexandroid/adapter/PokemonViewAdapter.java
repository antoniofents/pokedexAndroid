package com.example.afentanes.pokedexandroid.adapter;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.afentanes.pokedexandroid.PokemonDescActivity;
import com.example.afentanes.pokedexandroid.R;
import com.example.afentanes.pokedexandroid.databinding.PokemonViewBinding;
import com.example.afentanes.pokedexandroid.model.Pokemon;
import com.example.afentanes.pokedexandroid.modelview.PokemonView;
import com.example.afentanes.pokedexandroid.modelview.PokemonViewModel;

import java.util.List;



public class PokemonViewAdapter extends RecyclerView.Adapter <PokemonViewAdapter.PokemonViewHolder>{

    private List<Pokemon> filteredList;
    private PokemonView pokemonView;

    public PokemonViewAdapter(List<Pokemon> pokemonList, PokemonView activity){
        this.filteredList =pokemonList;
        this.pokemonView=activity;
    }


    class PokemonViewHolder extends RecyclerView.ViewHolder{
        PokemonViewBinding pokemonViewBinding;

        PokemonViewHolder(PokemonViewBinding binding) {
            super(binding.getRoot());
            this.pokemonViewBinding= binding;
        }

        public void bind(@NonNull Pokemon pokemon) {
            pokemonViewBinding.setPokemon(pokemon);
            pokemonViewBinding.setPokemonView(pokemonView);
            pokemonViewBinding.executePendingBindings();
        }
    }
    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      //  RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.pokemon_view, parent, false);
        PokemonViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.pokemon_view, parent, false);

        /*view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                activity.startActivity(new Intent(activity , PokemonDescActivity.class));
                return false;
            }
        });*/
        return new PokemonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder holder, int position) {
        Pokemon pokemon= filteredList.get(position);
        holder.bind(pokemon);

        /*ImageView img= holder.pokemon_view.findViewById(R.id.poke_image);
        Pokemon pokemon = filteredList.get(position);
        Glide.with(holder.pokemon_view.getContext()).load(pokemon.frontUrl).into(img);
        TextView textView = holder.pokemon_view.findViewById(R.id.poke_name);
        textView.setText(pokemon.name);*/
    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void setFilteredList(List <Pokemon> pokemons){
        this.filteredList=pokemons;

    }


      /*
    will be used for rx observable
    @Override
    public Filter getFilter() {

        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<Pokemon>) results.values;
                PokemonViewAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Pokemon> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = pokemonList;
                } else {
                   // filteredResults = pokemonViewModel.getFilteredResults(constraint.toString(), pokemonList);
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }*/
}