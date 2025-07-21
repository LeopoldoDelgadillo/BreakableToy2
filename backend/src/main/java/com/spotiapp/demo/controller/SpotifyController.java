package com.spotiapp.demo.controller;

import com.spotiapp.demo.ParameterStringBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.Yaml;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:8080", allowCredentials = "true")
public class SpotifyController {
    String accessTokenStream = null;
    
    @GetMapping("/login")
    public String authRequest(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID) throws FileNotFoundException {
        String encodedURL = "";
        if(sessionID.equals("Atta")) {
            System.out.println("Not logged in yet");
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream("D:/SpotifyIDs.yml");
            HashMap yamlMap = yaml.load(inputStream);

            //Gotten from StackOverflow
            //https://stackoverflow.com/questions/73810586/can-someone-tell-me-how-can-i-get-query-parameters-from-a-url-in-java-that-may-c
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("client_id", yamlMap.get("clientID").toString());
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
    public String authResponse (@RequestBody String body, HttpServletResponse cookieResponse) throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = new FileInputStream("D:/SpotifyIDs.yml");
        HashMap yamlMap = yaml.load(inputStream);
        System.out.println("Received body: " + body);
        JSONObject jsonObject = new JSONObject(body);
        String code = jsonObject.getString("code");
        String sessionID = jsonObject.getString("ID");
        System.out.println("Code: " + code);
        System.out.println("session ID: " + sessionID);
        
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
        File tokenFile = new File("D:\\SpotifyUsers\\"+sessionID+".txt");
        tokenFile.createNewFile();
        FileWriter writeToTokenFile = new FileWriter("D:\\SpotifyUsers\\"+sessionID+".txt");
        writeToTokenFile.write(new JSONObject(response.toString()).getString("access_token"));
        writeToTokenFile.write(":");
        writeToTokenFile.write(new JSONObject(response.toString()).getString("refresh_token"));
        writeToTokenFile.close();
        System.out.println(tokenFile);
        Cookie sessionCookie = new Cookie("sessionID",sessionID);
        sessionCookie.setHttpOnly(false);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(3000);
        cookieResponse.addCookie(sessionCookie);

        return response.toString();
    }

    @GetMapping("/login/check")
    public String loginCheck(@CookieValue(value="sessionID",defaultValue = "Atta")String sessionID) {
        String message;
        System.out.println("sessionID: "+sessionID);
        if(!sessionID.equals("Atta")) {
            message="Valid Session";
        }
        else{
            message="Invalid Session";
        }
        System.out.println("message: "+message);
        return message;
    }

    @GetMapping("/token/refresh")
    public String refreshToken(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID) throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = new FileInputStream("D:/SpotifyIDs.yml");
        HashMap yamlMap = yaml.load(inputStream);
        System.out.println("session ID: " + sessionID);
        URL url = new URL("https://accounts.spotify.com/api/token");
        System.out.println("URL: " + url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        System.out.println("Connection established: " + con);
        con.setRequestMethod("POST");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "refresh_token");
        File TokenFile = new File("D:\\SpotifyUsers\\"+sessionID+".txt");
        Scanner readTokenFile = new Scanner(TokenFile);
        String data = readTokenFile.nextLine();
        String tokens[] = data.split(":"); //tokens[0] = access_token, tokens[1] = refresh_token
        System.out.println("refresh token: "+tokens[1]);
        readTokenFile.close(); 
        parameters.put("refresh_token",tokens[1]);
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
        String inputLine;
        StringBuilder response = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        FileWriter writeToTokenFile = new FileWriter(TokenFile,false);
        writeToTokenFile.write(new JSONObject(response.toString()).getString("access_token"));
        writeToTokenFile.write(":");
        JSONObject json = new JSONObject(response.toString());
        String newRefreshToken = json.has("refresh_token") ? json.getString("refresh_token") : tokens[1];
        writeToTokenFile.write(newRefreshToken);
        writeToTokenFile.close();
        System.out.println("Response: " + response.toString());
        return response.toString();
    }

    @GetMapping("/me/top/artists")
    public ResponseEntity<String> top10(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID) throws IOException {
        File tokenFile = new File("D:\\SpotifyUsers\\"+sessionID+".txt");
        Scanner readTokenFile = new Scanner(tokenFile);
        String data = readTokenFile.nextLine();
        String tokens[] = data.split(":"); //tokens[0] = access_token, tokens[1] = refresh_token
        readTokenFile.close();
        //gotten from baeldung.com
        //https://www.baeldung.com/java-http-request
        URL url = new URL("https://api.spotify.com/v1/me/top/artists?time_range=medium_term&limit=10&offset=0");
        System.out.println("URL: " + url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        System.out.println("Connection established: " + con);
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer "+ tokens[0]); 
        System.out.println("connection: " + con);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("Top Artists Response: " + response.toString());
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.toString());
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<String> artistInfo(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID, @PathVariable("id") String id) throws IOException {
        File tokenFile = new File("D:\\SpotifyUsers\\"+sessionID+".txt");
        Scanner readTokenFile = new Scanner(tokenFile);
        String data = readTokenFile.nextLine();
        String tokens[] = data.split(":"); //tokens[0] = access_token, tokens[1] = refresh_token
        readTokenFile.close();
        //gotten from baeldung.com
        //https://www.baeldung.com/java-http-request
        URL url = new URL("https://api.spotify.com/v1/artists/"+id);
        System.out.println("URL: " + url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        System.out.println("Connection established: " + con);
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer "+ tokens[0]); 
        System.out.println("connection: " + con);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("Artist Response: " + response.toString());
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.toString());
    }

    @GetMapping("/artists/{id}/top-tracks")
    public ResponseEntity<String> artistToptracksInfo(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID, @PathVariable("id") String id, @RequestParam("country") String country) throws IOException {
        File tokenFile = new File("D:\\SpotifyUsers\\"+sessionID+".txt");
        Scanner readTokenFile = new Scanner(tokenFile);
        String data = readTokenFile.nextLine();
        String tokens[] = data.split(":"); //tokens[0] = access_token, tokens[1] = refresh_token
        readTokenFile.close();
        //gotten from baeldung.com
        //https://www.baeldung.com/java-http-request
        URL url = new URL("https://api.spotify.com/v1/artists/"+id+"/top-tracks?market="+country);
        System.out.println("URL: " + url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        System.out.println("Connection established: " + con);
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer "+ tokens[0]); 
        System.out.println("connection: " + con);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("Artist Top Tracks Response: " + response.toString());
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.toString());
    }

    @GetMapping("/artists/{id}/albums")
    public ResponseEntity<String> artistAlbumsInfo(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID, @PathVariable("id") String id, @RequestParam("country") String country) throws IOException {
        File tokenFile = new File("D:\\SpotifyUsers\\"+sessionID+".txt");
        Scanner readTokenFile = new Scanner(tokenFile);
        String data = readTokenFile.nextLine();
        String tokens[] = data.split(":"); //tokens[0] = access_token, tokens[1] = refresh_token
        readTokenFile.close();
        //gotten from baeldung.com
        //https://www.baeldung.com/java-http-request
        URL url = new URL("https://api.spotify.com/v1/artists/"+id+"/albums?include_groups=album%2Csingle&market="+country);
        System.out.println("URL: " + url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        System.out.println("Connection established: " + con);
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer "+ tokens[0]); 
        System.out.println("connection: " + con);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("Artist Albums Response: " + response.toString());
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.toString());
    }

    @GetMapping("/tracks/{id}")
    public ResponseEntity<String> trackInfo(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID, @PathVariable("id") String id, @RequestParam("country") String country) throws IOException {
        File tokenFile = new File("D:\\SpotifyUsers\\"+sessionID+".txt");
        Scanner readTokenFile = new Scanner(tokenFile);
        String data = readTokenFile.nextLine();
        String tokens[] = data.split(":"); //tokens[0] = access_token, tokens[1] = refresh_token
        readTokenFile.close();
        //gotten from baeldung.com
        //https://www.baeldung.com/java-http-request
        URL url = new URL("https://api.spotify.com/v1/tracks/"+id+"?market="+country);
        System.out.println("URL: " + url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        System.out.println("Connection established: " + con);
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer "+ tokens[0]); 
        System.out.println("connection: " + con);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("Track Response: " + response.toString());
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.toString());
    }

    @GetMapping("/albums/{id}")
    public ResponseEntity<String> albumInfo(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID, @PathVariable("id") String id, @RequestParam("country") String country) throws IOException {
        File tokenFile = new File("D:\\SpotifyUsers\\"+sessionID+".txt");
        Scanner readTokenFile = new Scanner(tokenFile);
        String data = readTokenFile.nextLine();
        String tokens[] = data.split(":"); //tokens[0] = access_token, tokens[1] = refresh_token
        readTokenFile.close();
        //gotten from baeldung.com
        //https://www.baeldung.com/java-http-request
        URL url = new URL("https://api.spotify.com/v1/albums/"+id+"?market="+country);
        System.out.println("URL: " + url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        System.out.println("Connection established: " + con);
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer "+ tokens[0]); 
        System.out.println("connection: " + con);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //System.out.println("Album Response: " + response.toString());
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.toString());
    }

    @GetMapping("/albums/{id}/tracks")
    public ResponseEntity<String> albumSongsInfo(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID, @PathVariable("id") String id, @RequestParam("country") String country, @RequestParam("trackCount") Integer trackCount) throws IOException {
        File tokenFile = new File("D:\\SpotifyUsers\\"+sessionID+".txt");
        Scanner readTokenFile = new Scanner(tokenFile);
        String data = readTokenFile.nextLine();
        String tokens[] = data.split(":"); //tokens[0] = access_token, tokens[1] = refresh_token
        readTokenFile.close();
        int offset = 0;
        ArrayList<String> fullJson = new ArrayList<String>();
        //gotten from baeldung.com
        //https://www.baeldung.com/java-http-request
        while(true){
            if(offset > trackCount) {break;}
            URL url = new URL("https://api.spotify.com/v1/albums/"+id+"/tracks?market="+country+"&limit="+50+"&offset="+offset);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer "+ tokens[0]); 
            System.out.println("connection: " + con);
            int responseCode = con.getResponseCode();
            System.out.println("response code: " + responseCode);
            if(responseCode==200){
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                fullJson.add(response.toString());
                System.out.println("Complete TrackList Progress: "+fullJson);
                offset+=50;
            }
        }
        System.out.println("AlbumTracks Response: " + fullJson);
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(fullJson.toString());
    }

    @PostMapping("/search")
    public ResponseEntity<String> search(@RequestBody String body, @CookieValue(value="sessionID",defaultValue = "Atta")String sessionID) throws IOException {
        File tokenFile = new File("D:\\SpotifyUsers\\"+sessionID+".txt");
        Scanner readTokenFile = new Scanner(tokenFile);
        String data = readTokenFile.nextLine();
        String tokens[] = data.split(":"); //tokens[0] = clientID, tokens[1] = clientSecret
        readTokenFile.close(); 
        JSONObject jsonObject = new JSONObject(body);
        String searchString = jsonObject.getString("searchString");
        String userCountry = jsonObject.getString("userCountry");
        //gotten from baeldung.com
        //https://www.baeldung.com/java-http-request
        URL url = new URL("https://api.spotify.com/v1/search?q="+searchString+"&type=track%2Cartist%2Calbum&market="+userCountry+"&limit=10&offset=0&include_external=audio");
        System.out.println("URL: " + url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        System.out.println("Connection established: " + con);
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer "+ tokens[0]); 
        System.out.println("connection: " + con);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("Search Response: " + response.toString());
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.toString());
    }

    @GetMapping("/me")
    public String getUserProfile(@CookieValue(value="sessionID",defaultValue = "Atta") String sessionID) throws IOException {
        File tokenFile = new File("D:\\SpotifyUsers\\"+sessionID+".txt");
        Scanner readTokenFile = new Scanner(tokenFile);
        String data = readTokenFile.nextLine();
        String tokens[] = data.split(":"); //tokens[0] = clientID, tokens[1] = clientSecret
        readTokenFile.close(); 
        //gotten from baeldung.com
        //https://www.baeldung.com/java-http-request
        URL url = new URL("https://api.spotify.com/v1/me");
        System.out.println("URL: " + url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        System.out.println("Connection established: " + con);
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer "+ tokens[0]); 
        System.out.println("connection: " + con);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("User Profile Response: " + response.toString());
        return response.toString();
    }

}
