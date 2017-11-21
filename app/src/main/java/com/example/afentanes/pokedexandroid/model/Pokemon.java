package com.example.afentanes.pokedexandroid.model;

import com.example.afentanes.pokedexandroid.util.PokemonUtil;

/**
 * Created by afentanes on 11/13/17.
 */

public class Pokemon {
    Pokemon(){

    }

    public Pokemon(String id, String name, String url){
        this.id=id;
        this.name=name;
        this.url=url;
    }

    public String id, name, url, frontUrl;

    public void initPokemon(){
        this.id=url.split(PokemonUtil.patternPokemonIdUrl)[2];
        frontUrl= "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+id+".png";
    }
}
