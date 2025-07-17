package com.spotiapp.demo.controller;

import com.spotiapp.demo.ParameterStringBuilder;
import com.spotiapp.demo.model.UserModel;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.Yaml;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:8080")
public class SpotifyController {
    HashMap<Integer, UserModel> userMap = new HashMap<>();
    Integer userID = 0;
    String accessTokenStream = null;
    
    @GetMapping("/login")
    public String authRequest() throws FileNotFoundException {
        Yaml yaml = new Yaml();
        String encodedURL = "";
        InputStream inputStream = new FileInputStream("D:/SpotifyIDs.yml");
        HashMap yamlMap = yaml.load(inputStream);

        //Gotten from StackOverflow
        //https://stackoverflow.com/questions/73810586/can-someone-tell-me-how-can-i-get-query-parameters-from-a-url-in-java-that-may-c
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("client_id", yamlMap.get("clientID").toString());
        requestParams.put("response_type", "code");
        requestParams.put("scope", "user-read-private user-read-email");
        requestParams.put("redirect_uri", "http://127.0.0.1:8080/redirect");
        requestParams.put("show_dialog", "True");
        encodedURL = requestParams.keySet().stream()
        .map(key -> key + "=" + URLEncoder.encode(requestParams.get(key), StandardCharsets.UTF_8))
        .collect(Collectors.joining("&", "https://accounts.spotify.com/authorize?", ""));
        return encodedURL;
    }

    @PostMapping("/auth/spotify")
    public String authResponse (@RequestBody String body) throws IOException {
        Yaml yaml = new Yaml();
        String encodedURL = "";
        InputStream inputStream = new FileInputStream("D:/SpotifyIDs.yml");
        HashMap yamlMap = yaml.load(inputStream);
        System.out.println("Received body: " + body);
        JSONObject jsonObject = new JSONObject(body);
        String code = jsonObject.getString("code");
        System.out.println("Code: " + code);

        //gotten from baeldung.com
        //https://www.baeldung.com/java-http-request
        URL url = new URL("https://accounts.spotify.com/api/token");
        System.out.println("URL: " + url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        System.out.println("Connection established: " + con);
        con.setRequestMethod("POST");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("code", code);
        parameters.put("grant_type", "authorization_code");
        parameters.put("redirect_uri", "http://127.0.0.1:8080/redirect");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        String base64String = Base64.getEncoder().encodeToString((yamlMap.get("clientID").toString() + ":" + yamlMap.get("clientSecret").toString()).getBytes(StandardCharsets.UTF_8));
        System.out.println("Base64 String: " + base64String);
        con.setRequestProperty("Authorization", "Basic "+ base64String);
        System.out.println("Parameters: " + parameters);
        System.out.println("connection: " + con);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        out.flush();
        out.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("Response: " + response.toString());
        userMap.put(userID, new UserModel(
            new JSONObject(response.toString()).getString("access_token"),
            new JSONObject(response.toString()).getString("refresh_token")
        ));
        userID++;
        return response.toString();
    }

    @GetMapping("/me/top/artists")
    public String top10() {
        return "top10 test";
    }

    @GetMapping("/artists/{id}")
    public String artistInfo(@PathVariable String artistID) {
        return artistID+" test";
    }

    @GetMapping("/albums/{id}")
    public String albumInfo(@PathVariable String albumID) {
        return albumID+" test";
    }

    @GetMapping("/search")
    public String search() {
        return "search test";
    }

    @GetMapping("/me")
    public String getUserProfile() throws IOException {
        //gotten from baeldung.com
        //https://www.baeldung.com/java-http-request
        URL url = new URL("https://api.spotify.com/v1/me");
        System.out.println("URL: " + url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        System.out.println("Connection established: " + con);
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer "+ userMap.get(0).getAccessToken());
        System.out.println("connection: " + con);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("Response: " + response.toString());
        return response.toString();
    }

}
