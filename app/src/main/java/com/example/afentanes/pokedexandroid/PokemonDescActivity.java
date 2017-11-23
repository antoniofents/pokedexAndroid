package com.example.afentanes.pokedexandroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.example.afentanes.pokedexandroid.model.Pokemon;
import com.example.afentanes.pokedexandroid.util.PokemonUtil;

public class PokemonDescActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            Pokemon pokemon= (Pokemon) savedInstanceState.getParcelable(PokemonUtil.POKEMON_BUNDLE);
            TextView viewById = findViewById(R.id.poke_name);
            TextView weight = findViewById(R.id.weight_id);
            TextView abilities = findViewById(R.id.abilities_id);

            viewById.setText(pokemon.name);
            weight.setText(pokemon.frontUrl);
            abilities.setText(pokemon.characteristics.get(0));
        }
        setContentView(R.layout.pokemon_expanded_view);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout((int)(dm.widthPixels*.8), (int)(dm.heightPixels*.6));
    }
}
