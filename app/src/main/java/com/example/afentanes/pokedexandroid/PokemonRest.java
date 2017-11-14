package com.example.afentanes.pokedexandroid;

import android.os.AsyncTask;
import android.util.Log;

import com.example.afentanes.pokedexandroid.model.Pokemon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by afentanes on 11/13/17.
 */

public class PokemonRest {

    public List <Pokemon> getPokemons() {

        try {
            return new FillPokemonsTask().execute(("https://pokeapi.co/api/v2/pokemon/?limit=150")).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    private class FillPokemonsTask extends AsyncTask<String, Void, List <Pokemon>> {



        @Override
        protected List <Pokemon> doInBackground(String... urls) {
            List <Pokemon> pokemons = new ArrayList<>();
            String jsonStr = makeServiceCall(urls[0]);
            try {
                JSONObject jObject = new JSONObject(jsonStr);
                JSONArray results = jObject.getJSONArray("results");

                for(int i=0 ;i<results.length();i++){
                    JSONObject pokemon = results.getJSONObject(i);
                    String id = i + 1 + "";
                    pokemons.add(new Pokemon(id, pokemon.getString("name"), "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+id+".png"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return pokemons;
        }




        public String makeServiceCall(String reqUrl) {

            try {
                URL url = new URL(reqUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(conn.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                return sb.toString();

            } catch (IOException e) {
                Log.e(this.getClass().getName(), "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(this.getClass().getName(), "Exception: " + e.getMessage());
            }
            return "";
        }
    }
}
