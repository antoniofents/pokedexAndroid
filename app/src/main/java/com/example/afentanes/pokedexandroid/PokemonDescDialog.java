package com.example.afentanes.pokedexandroid;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.afentanes.pokedexandroid.model.Pokemon;
import com.example.afentanes.pokedexandroid.util.PokemonUtil;


public class PokemonDescDialog extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
         super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(getResources().getString(R.string.pokemon_desc));
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View inflate = inflater.inflate(R.layout.pokemon_expanded_view, null);
        ;
        builder.setView(inflate).setPositiveButton(getResources().getString(R.string.close), (dialog, id)->{
            PokemonDescDialog.this.getDialog().hide();
        });
        if(getArguments() !=null&& getArguments().containsKey(PokemonUtil.POKEMON_BUNDLE)){
            initPokemon(inflate, (Pokemon) getArguments().getParcelable(PokemonUtil.POKEMON_BUNDLE));
        }
         return builder.create();
    }


    public void initPokemon( View view, Pokemon pokemon) {
        super.onStart();
        TextView nameById = view.findViewById(R.id.poke_name);

        TextView abilities = view.findViewById(R.id.abilities_id);
        nameById.setText(pokemon.name);

        ImageView image = view.findViewById(R.id.pokemon_desc_image_id);
        Glide.with(getContext()).load("https://img.pokemondb.net/artwork/" + pokemon.name + ".jpg").asBitmap().into(image);
        if (pokemon.characteristics != null && !pokemon.characteristics.isEmpty()) {
            abilities.setText(pokemon.characteristics.get(0));
        }
        if (pokemon.effectEntries != null && !pokemon.effectEntries.isEmpty()) {
            TextView description = view.findViewById(R.id.description_id);
            description.setText(pokemon.effectEntries.get(0).effect);
            TextView shortEffect = view.findViewById(R.id.short_effect_id);
            shortEffect.setText(pokemon.effectEntries.get(0).shortEffect);
        }
    }

}
