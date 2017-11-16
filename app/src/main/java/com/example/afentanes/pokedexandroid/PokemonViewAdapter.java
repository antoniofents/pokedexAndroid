package com.example.afentanes.pokedexandroid;

import android.app.Activity;
import android.content.Intent;
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



class PokemonViewAdapter extends RecyclerView.Adapter <PokemonViewAdapter.PokemonViewHolder> {

    List<Pokemon> pokemonList;
    private Activity activity;

    public PokemonViewAdapter(List<Pokemon> pokemonList, Activity activity){
        this.pokemonList=pokemonList;
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
        Pokemon pokemon = pokemonList.get(position);
        Glide.with(holder.pokemon_view.getContext()).load(pokemon.frontUrl).into(img);
        TextView textView = holder.pokemon_view.findViewById(R.id.poke_name);
        textView.setText(pokemon.name);
    }


    @Override
    public int getItemCount() {
        return pokemonList.size();
    }
}
