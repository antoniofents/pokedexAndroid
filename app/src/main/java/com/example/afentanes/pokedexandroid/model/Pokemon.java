package com.example.afentanes.pokedexandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by afentanes on 11/13/17.
 */

public class Pokemon implements Parcelable {
    Pokemon(){

    }

    public Pokemon(String id, String name, String url){
        this.id=id;
        this.name=name;
        this.url=url;
    }

    public String id, name, url, frontUrl;
    public ArrayList<EffectEntry> effectEntries;
    public ArrayList<String> characteristics;


    public static final Creator<Pokemon> CREATOR = new Creator<Pokemon>() {
        @Override
        public Pokemon createFromParcel(Parcel in) {
            return new Pokemon(in);
        }

        @Override
        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    protected Pokemon(Parcel in) {
        id = in.readString();
        name = in.readString();
        url = in.readString();
        characteristics= new ArrayList<>();
        in.readList(characteristics, null);
        effectEntries= new ArrayList<>();
        in.readList(effectEntries,null);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(url);
        parcel.writeList(characteristics);
        parcel.writeList(effectEntries);
    }
}
