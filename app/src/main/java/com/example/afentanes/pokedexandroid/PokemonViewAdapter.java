package com.example.afentanes.pokedexandroid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.afentanes.pokedexandroid.model.Pokemon;

import java.util.List;

/**
 * Created by afentanes on 11/13/17.
 */

public class PokemonViewAdapter extends RecyclerView.Adapter <PokemonViewAdapter.PokemonViewHolder> {

    List<Pokemon> pokemonList;

    public PokemonViewAdapter(List<Pokemon> pokemonList){
        this.pokemonList=pokemonList;
    }
    class PokemonViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout pokemon_view;

        public PokemonViewHolder(View itemView) {
            super(itemView);
            this.pokemon_view= (RelativeLayout) itemView;
        }
    }
    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_view, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder holder, int position) {
        ImageView img= holder.pokemon_view.findViewById(R.id.poke_image);
        Pokemon pokemon = pokemonList.get(position);
        Glide.with(holder.pokemon_view.getContext()).load(pokemon.url).into(img);
        TextView textView = holder.pokemon_view.findViewById(R.id.poke_name);
        textView.setText(pokemon.name );
    }



    @Override
    public int getItemCount() {
        return pokemonList.size();
    }
}
