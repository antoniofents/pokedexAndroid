package com.example.afentanes.pokedexandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.afentanes.pokedexandroid.model.Pokemon;
import com.example.afentanes.pokedexandroid.util.PokemonUtil;

public class PokemonDescActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_expanded_view);
        if(getIntent()!=null){

            Pokemon pokemon= (Pokemon) getIntent().getExtras().getParcelable(PokemonUtil.POKEMON_BUNDLE);
            TextView nameById = findViewById(R.id.poke_name);
            TextView weight = findViewById(R.id.weight_id);
            TextView abilities = findViewById(R.id.abilities_id);
            nameById.setText(pokemon.name);
            weight.setText(pokemon.frontUrl);
            ImageView image= findViewById(R.id.pokemon_desc_image_id);
             Glide.with(getApplication()).load("https://img.pokemondb.net/artwork/"+pokemon.name+".jpg").asBitmap().into(image);
           if(pokemon.characteristics!=null && !pokemon.characteristics.isEmpty()){
               abilities.setText(pokemon.characteristics.get(0));
           }

        }


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int)(dm.widthPixels*.8), (int)(dm.heightPixels*.6));
    }

}
