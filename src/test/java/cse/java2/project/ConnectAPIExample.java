package cse.java2.project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.zip.GZIPInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ConnectAPIExample {
    private static final String API_QUESTION_URL = "https://api.stackexchange.com/2.3";
    private static final String API_KEY = "OzEDe9HvmTuTIQ7np0nV4A((";
    public static final String CLIENT_SECRET = "GsOsg3UOrgiTn6WBEd5kbg((";
    private static final String TAGGED = "java";
    private static final int PAGE_SIZE = 1;
    public static final String APP_NAME = "cs209a_project_stackoverflow_java_tag_stastic";
    public static final String CLIENT_ID = "26086";

    public static void main(String[] args) throws IOException, JSONException, IOException, JSONException {

        URI uri = UriComponentsBuilder.fromUriString(API_QUESTION_URL)
                .path("/questions")
                .queryParam("site", "stackoverflow")
                .queryParam("tagged", "java")
                .queryParam("key", API_KEY)
                .queryParam("page", 2)
                .queryParam("pagesize", 1)
                .build().toUri();
        System.out.println(uri.toString());

        URL url = new URL(uri.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("content-type", "application/x-www-form-urlencoded; charset=utf-8");

        BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new GZIPInputStream(connection.getInputStream()), StandardCharsets.UTF_8)
        );
        String response = reader.lines().reduce("", String::concat);
        reader.close();

        System.out.println("=====response=====");
        System.out.println(response);
        System.out.println("=====response=====");

        JSONObject json = new JSONObject(response);
        JSONArray items = json.getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            System.out.println("------item-------");
            System.out.println(item);
            System.out.println("------item-------");
        }

//        {
//              "items":[
//              {
//                  "tags":["java","xslt","xalan"],
//                  "owner":{
//                      "account_id":1119771,
//                      "reputation":319,
//                      "user_id":1108370,
//                      "user_type":"registered",
//                      "accept_rate":75,
//                      "profile_image":"https://www.gravatar.com/avatar/3a63c4fd8b867a68b8fbc6cf7c5df6a0?s=256&d=identicon&r=PG",
//                      "display_name":"kazvictor","link":"https://stackoverflow.com/users/1108370/kazvictor"
//                  },
//                  "is_answered":true,"view_count":1948,
//                  "accepted_answer_id":25352274,
//                  "answer_count":2,
//                  "score":5,
//                  "last_activity_date":1684255104,
//                  "creation_date":1407876396,
//                  "last_edit_date":1471979352,
//                  "question_id":25273763,
//                  "content_license":"CC BY-SA 3.0",
//                  "link":"https://stackoverflow.com/questions/25273763/execute-xslt-with-xalan-in-secure-mode-to-create-xhtml-throws-transformerconfigu",
//                  "title":"Execute XSLT with Xalan in secure mode to create XHTML throws TransformerConfigurationException on creating attributes"}
//              ],
//              "has_more":true,"quota_max":10000,"quota_remaining":9962
//          }
    }
}
