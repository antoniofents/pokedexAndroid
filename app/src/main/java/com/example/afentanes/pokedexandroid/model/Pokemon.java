package com.example.afentanes.pokedexandroid.model;

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

    public String id, name, url;
}
