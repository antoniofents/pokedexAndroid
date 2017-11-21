package com.example.afentanes.pokedexandroid.adapter;

import android.app.Activity;
import android.content.Intent;
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
import com.example.afentanes.pokedexandroid.model.Pokemon;

import java.util.List;



public class PokemonViewAdapter extends RecyclerView.Adapter <PokemonViewAdapter.PokemonViewHolder>{

    private List<Pokemon> filteredList;
    private Activity activity;

    public PokemonViewAdapter(List<Pokemon> pokemonList, Activity activity){
        this.filteredList =pokemonList;
        this.activity=activity;
    }


    class PokemonViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout pokemon_view;

        PokemonViewHolder(View itemView) {
            super(itemView);
            this.pokemon_view= (RelativeLayout) itemView;
        }
    }
    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_view, parent, false);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                activity.startActivity(new Intent(activity , PokemonDescActivity.class));
                return false;
            }
        });
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder holder, int position) {
        ImageView img= holder.pokemon_view.findViewById(R.id.poke_image);
        Pokemon pokemon = filteredList.get(position);
        Glide.with(holder.pokemon_view.getContext()).load(pokemon.frontUrl).into(img);
        TextView textView = holder.pokemon_view.findViewById(R.id.poke_name);
        textView.setText(pokemon.name);
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
