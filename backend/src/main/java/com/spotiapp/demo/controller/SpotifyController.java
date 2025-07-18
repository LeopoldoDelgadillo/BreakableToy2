package com.spotiapp.demo.controller;

import com.spotiapp.demo.ParameterStringBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
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
import java.util.Base64;
import java.util.HashMap;
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
            requestParams.put("scope", "user-read-private user-read-email");
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
        Cookie sessionCookie = new Cookie("sessionID",sessionID);
        sessionCookie.setHttpOnly(false);
        sessionCookie.setPath("/");
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

    @PostMapping("/token/refresh")
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
        Scanner readTokenFile = new Scanner("D:\\SpotifyUsers\\"+sessionID+".txt");
        String data = readTokenFile.nextLine();
        String tokens[] = data.split(":"); //tokens[0] = clientID, tokens[1] = clientSecret
        readTokenFile.close(); 
        parameters.put("refresh_token",tokens[1]);
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
        File TokenFile = new File("D:\\SpotifyUsers\\"+sessionID+".txt");
        TokenFile.delete();
        TokenFile.createNewFile();
        FileWriter writeToTokenFile = new FileWriter("D:\\SpotifyUsers\\"+sessionID+".txt");
        writeToTokenFile.write(new JSONObject(response.toString()).getString("access_token"));
        writeToTokenFile.write(":");
        writeToTokenFile.write(new JSONObject(response.toString()).getString("refresh_token"));
        writeToTokenFile.close();
        System.out.println("Response: " + response.toString());
        return "a";
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
        System.out.println("Response: " + response.toString());
        return response.toString();
    }

}
