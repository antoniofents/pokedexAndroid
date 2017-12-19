package com.example.afentanes.pokedexandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by afentanes on 11/22/17.
 */

public class EffectEntry implements Parcelable{

    public EffectEntry(String shortEffect, String effect){
        this.shortEffect= shortEffect;
        this.effect=effect;
    }

    public String shortEffect, effect;

    protected EffectEntry(Parcel in) {
        shortEffect = in.readString();
        effect = in.readString();
    }

    public static final Creator<EffectEntry> CREATOR = new Creator<EffectEntry>() {
        @Override
        public EffectEntry createFromParcel(Parcel in) {
            return new EffectEntry(in);
        }

        @Override
        public EffectEntry[] newArray(int size) {
            return new EffectEntry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shortEffect);
        parcel.writeString(effect);
    }
}
