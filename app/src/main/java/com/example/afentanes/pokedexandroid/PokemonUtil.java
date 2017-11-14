package com.example.afentanes.pokedexandroid;

import android.os.AsyncTask;

import com.example.afentanes.pokedexandroid.model.Pokemon;

import java.util.ArrayList;
import java.util.List;


public class PokemonUtil {




    public static  List <Pokemon> getPokemonsList(){
        return  new PokemonRest().getPokemons();
    }


}
