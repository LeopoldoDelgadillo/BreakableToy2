package com.spotiapp.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotiapp.demo.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:8080", allowCredentials = "true")
public class SpotifyController {
    @Value("${spotify.client_id}")
    private String clientID;

    @Value("${spotify.client_secret}")
    private String clientSecret;
    String accessTokenStream = null;

    @Autowired
    private UserService userService;
    
    @GetMapping("/login")
    public String authRequest(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID) throws FileNotFoundException {
        String encodedURL = "";
        if(sessionID.equals("Atta")) {
            System.out.println("Not logged in yet");
            String[] clientCred = {clientID,clientSecret};

            //Gotten from StackOverflow
            //https://stackoverflow.com/questions/73810586/can-someone-tell-me-how-can-i-get-query-parameters-from-a-url-in-java-that-may-c
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("client_id", clientCred[0]);
            requestParams.put("response_type", "code");
            requestParams.put("scope", "user-read-private user-read-email user-top-read user-follow-read");
            requestParams.put("redirect_uri", "http://127.0.0.1:8080/redirect");
            requestParams.put("show_dialog", "True");
            encodedURL = requestParams.keySet().stream()
            .map(key -> key + "=" + URLEncoder.encode(requestParams.get(key), StandardCharsets.UTF_8))
            .collect(Collectors.joining("&", "https://accounts.spotify.com/authorize?", ""));
        }
        else{
            System.out.println("Already logged in");
            encodedURL="http://127.0.0.1:8080/";}
        return encodedURL;
    }

    @PostMapping("/auth/spotify")
    public ResponseEntity<Object> authResponse (@RequestBody String body, HttpServletResponse cookieResponse) throws IOException {
        JSONObject jsonObject = new JSONObject(body);
        String code = jsonObject.getString("code");
        String sessionID = jsonObject.getString("ID");

        String url = "https://accounts.spotify.com/api/token";
        String response = userService.requestHTTPspotifyAPI(sessionID, url, code,"authSpotify",null,"Not needed","Not needed");
        
        File tokenFile = new File("D:/SpotifyUsers/"+sessionID+".txt");
        tokenFile.createNewFile();
        FileWriter writeToTokenFile = new FileWriter("D:/SpotifyUsers/"+sessionID+".txt");
        writeToTokenFile.write(new JSONObject(response).getString("access_token"));
        writeToTokenFile.write(":");
        writeToTokenFile.write(new JSONObject(response).getString("refresh_token"));
        writeToTokenFile.close();

        Cookie sessionCookie = new Cookie("sessionID",sessionID);
        sessionCookie.setHttpOnly(false);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(3000);
        cookieResponse.addCookie(sessionCookie);

        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }

    @GetMapping("/login/check")
    public String loginCheck(@CookieValue(value="sessionID",defaultValue = "Atta")String sessionID) {
        String message;
        if(!sessionID.equals("Atta")) {
            message="Valid Session";
        }
        else{
            message="Invalid Session";
        }
        return message;
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<Object> refreshToken(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID) throws IOException {
        String[] tokens = userService.readUserTokenFile(sessionID);

        String url = "https://accounts.spotify.com/api/token";

        String response = userService.requestHTTPspotifyAPI(sessionID, url,"Not needed","tokenRefresh",null,"Not needed","Not needed");

        File TokenFile = new File("D:\\SpotifyUsers\\"+sessionID+".txt");
        FileWriter writeToTokenFile = new FileWriter(TokenFile,false);
        JSONObject json = new JSONObject(response);
        writeToTokenFile.write(json.getString("access_token"));
        writeToTokenFile.write(":");
        String newRefreshToken = json.has("refresh_token") ? json.getString("refresh_token") : tokens[1];
        writeToTokenFile.write(newRefreshToken);
        writeToTokenFile.close();

        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }

    @GetMapping("/me/top/artists")
    public ResponseEntity<Object> top10(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID) throws IOException {
        String url = "https://api.spotify.com/v1/me/top/artists?time_range=medium_term&limit=10&offset=0";

        String response = userService.requestHTTPspotifyAPI(sessionID, url,"Not needed","generic",null,"Not needed","Not needed");
        
        System.out.println("Top Artists Response: " + response);
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<Object> artistInfo(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID, @PathVariable("id") String id) throws IOException {
        String url = "https://api.spotify.com/v1/artists/"+id;

        String response = userService.requestHTTPspotifyAPI(sessionID, url,"Not needed","generic",null,"Not needed","Not needed");

        System.out.println("Artist Response: " + response);
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }

    @GetMapping("/artists/{id}/top-tracks")
    public ResponseEntity<Object> artistToptracksInfo(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID, @PathVariable("id") String id, @RequestParam("country") String country) throws IOException {
        String url = "https://api.spotify.com/v1/artists/"+id+"/top-tracks?market="+country;

        String response = userService.requestHTTPspotifyAPI(sessionID, url,"Not needed","generic",null,"Not needed","Not needed");
        
        System.out.println("Artist Top Tracks Response: " + response);
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }

    @GetMapping("/artists/{id}/albums")
    public ResponseEntity<Object> artistAlbumsInfo(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID, @PathVariable("id") String id, @RequestParam("country") String country) throws IOException {
        String url = "https://api.spotify.com/v1/artists/"+id+"/albums?include_groups=album%2Csingle&market="+country;

        String response = userService.requestHTTPspotifyAPI(sessionID, url, "Not needed", "generic", null,"Not needed","Not needed");

        System.out.println("Artist Albums Response: " + response);
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }

    @GetMapping("/tracks/{id}")
    public ResponseEntity<Object> trackInfo(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID, @PathVariable("id") String id, @RequestParam("country") String country) throws IOException {
        String url = "https://api.spotify.com/v1/tracks/"+id+"?market="+country;

        String response = userService.requestHTTPspotifyAPI(sessionID, url,"Not needed", "generic", null,"Not needed","Not needed");
        
        System.out.println("Track Response: " + response);
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }

    @GetMapping("/albums/{id}")
    public ResponseEntity<Object> albumInfo(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID, @PathVariable("id") String id, @RequestParam("country") String country) throws IOException {
        String url = "https://api.spotify.com/v1/albums/"+id+"?market="+country;

        String response = userService.requestHTTPspotifyAPI(sessionID, url, "Not needed", "generic", null,"Not needed","Not needed");
        
        System.out.println("Album Response: " + response);
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }

    @GetMapping("/albums/{id}/tracks")
    public ResponseEntity<Object> albumSongsInfo(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID, @PathVariable("id") String id, @RequestParam("country") String country, @RequestParam("trackCount") Integer trackCount) throws IOException {
        String response = userService.requestHTTPspotifyAPI(sessionID, "", "Not needed", "albumTracks", trackCount, country, id);
        
        System.out.println("AlbumTracks Response: " + response);
        ObjectMapper mapper = new ObjectMapper();
        Object json = mapper.readValue(response, Object.class);
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(json);
    }

    @PostMapping("/search")
    public ResponseEntity<Object> search(@RequestBody String body, @CookieValue(value="sessionID",defaultValue = "Atta")String sessionID) throws IOException {
        JSONObject jsonObject = new JSONObject(body);
        String searchString = jsonObject.getString("searchString");
        String userCountry = jsonObject.getString("userCountry");

        String url = "https://api.spotify.com/v1/search?q="+searchString+"&type=track%2Cartist%2Calbum&market="+userCountry+"&limit=10&offset=0&include_external=audio";
        
        String response = userService.requestHTTPspotifyAPI(sessionID, url,"Not needed","generic",null,"Not needed","Not needed");
        
        System.out.println("Search Response: " + response);
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Object> getUserProfile(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID) throws IOException {
        String url = "https://api.spotify.com/v1/me";

        String response = userService.requestHTTPspotifyAPI(sessionID, url,"Not needed","generic",null,"Not needed","Not needed");
        
        System.out.println("User Profile Response: " + response);
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }
}
