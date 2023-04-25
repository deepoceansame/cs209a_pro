package cse.java2.project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ConnectAPIExample {
    private static final String API_URL = "https://api.stackexchange.com/2.3/questions";
    private static final String API_KEY = "your-api-key-here";
    private static final String TAGGED = "java";
    private static final int PAGE_SIZE = 1;

    public static void main(String[] args) throws IOException, JSONException, IOException, JSONException {
        int threadId = 12345; // Replace with the ID of the thread you want to query

        URL url = new URL(API_URL + "?pagesize=" + PAGE_SIZE + "&tagged=" + TAGGED + "&filter=withbody&site=stackoverflow&key=" + API_KEY);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = reader.lines().reduce("", String::concat);
        reader.close();

        JSONObject json = new JSONObject(response);
        JSONArray items = json.getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            int id = item.getInt("question_id");

            if (id == threadId) {
                int answerCount = item.getInt("answer_count");
                System.out.println("Thread " + threadId + " has " + answerCount + " answers.");
                return;
            }
        }

        System.out.println("Thread " + threadId + " not found.");
    }
}
