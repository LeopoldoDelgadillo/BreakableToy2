package com.spotiapp.demo.controller;

import com.spotiapp.demo.model.UserModel;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:8080")
public class SpotifyController {
    HashMap<Integer, UserModel> userMap = new HashMap<>();
    String accessTokenStream = null;
    
    @GetMapping("/login")
    public String authRequest() throws FileNotFoundException {
        Yaml yaml = new Yaml();
        String encodedURL = "";
        InputStream inputStream = new FileInputStream("D:/SpotifyIDs.yml");
        HashMap yamlMap = yaml.load(inputStream);
        //Obtenido de stackOverflow
        //https://stackoverflow.com/questions/109484/how-to-url-encode-a-string-in-java
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
    public String authResponse (@RequestBody String body) throws UnsupportedEncodingException {
        System.out.println("Received body: " + body);
        JSONObject jsonObject = new JSONObject(body);
        String code = jsonObject.getString("code");
        System.out.println("Code: " + code);
        
        
        return "Access Token: " + accessToken;
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
    public String getUserProfile() {
        return "getUserProfile test";
    }

}
