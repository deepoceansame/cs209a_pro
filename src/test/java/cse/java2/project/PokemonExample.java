package cse.java2.project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class PokemonExample {
    public static void main(String[] args) throws IOException, JSONException {
        URL url = new URL("https://pokeapi.co/api/v2/pokemon/pikachu/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = reader.lines().reduce("", String::concat);
        reader.close();

        JSONObject json = new JSONObject(response);
        int id = json.getInt("id");
        double height = json.getDouble("height");
        double weight = json.getDouble("weight");
        JSONArray abilities = json.getJSONArray("abilities");
        System.out.println(id);
        System.out.println(weight);
        System.out.println(height);
        System.out.println(abilities);
//        for (int i = 0; i < abilities.length(); i++) {
//            JSONObject ability = abilities.getJSONObject(i);
//        }
    }
}
