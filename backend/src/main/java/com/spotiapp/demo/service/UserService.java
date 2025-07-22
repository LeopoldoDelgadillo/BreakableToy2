package com.spotiapp.demo.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import com.spotiapp.demo.ParameterStringBuilder;

@Service
public class UserService {
    
    public String[] extractClientIDSecret() throws FileNotFoundException {
    Yaml yaml = new Yaml();
    InputStream inputStream = new FileInputStream("D:/SpotifyIDs.yml");
    HashMap yamlMap = yaml.load(inputStream);
    String[] clientCreds = {"clientID","clientSecret"};
    clientCreds[0] = yamlMap.get("clientID").toString();
    clientCreds[1] = yamlMap.get("clientSecret").toString();
        return clientCreds;
    }

    public String[] readUserTokenFile(String sessionID) throws FileNotFoundException {
        File tokenFile = new File("D:\\SpotifyUsers\\"+sessionID+".txt");
        Scanner readTokenFile = new Scanner(tokenFile);
        String data = readTokenFile.nextLine();
        String[] tokens = data.split(":"); //tokens[0] = access_token, tokens[1] = secret_tokens
        readTokenFile.close(); 
        return tokens;
    }

    public String requestHTTPspotifyAPI(String sessionID, String stringUrl, String code, String endpoint, Integer trackCount, String country, String id) throws IOException {
        String[] clientCred = extractClientIDSecret();
        String[] tokens = {};
        if(!endpoint.equals("authSpotify")){tokens = readUserTokenFile(sessionID);}
        StringBuilder response = new StringBuilder();
        
        //gotten from baeldung.com
        //https://www.baeldung.com/java-http-request
        if(!stringUrl.equals("")){
            URL url = new URL(stringUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            System.out.println("Connection established: " + con);
            if(endpoint.equals("authSpotify") || endpoint.equals("tokenRefresh")){
                con.setRequestMethod("POST");
                Map<String, String> parameters = new HashMap<>();
                if(endpoint.equals("authSpotify")) {
                    parameters.put("code", code);
                    parameters.put("grant_type", "authorization_code");
                    parameters.put("redirect_uri", "http://127.0.0.1:8080/redirect");
                }
                else if(endpoint.equals("tokenRefresh")) {
                    parameters.put("grant_type", "refresh_token");
                    parameters.put("refresh_token",tokens[1]);
                }
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                String base64String = Base64.getEncoder().encodeToString((clientCred[0] + ":" + clientCred[1]).getBytes(StandardCharsets.UTF_8));
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
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
        else if(endpoint == "generic"){
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer "+ tokens[0]); 
            System.out.println("connection: " + con);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } 
        }
        
        else if(endpoint == "albumTracks"){
            int offset = 0;
            ArrayList<String> fullJson = new ArrayList<String>();
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
                    StringBuilder responseAdder = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        responseAdder.append(inputLine);
                    }
                    in.close();
                    fullJson.add(responseAdder.toString());
                    System.out.println("Complete TrackList Progress: "+fullJson);
                    offset+=50;
                }
            }
            response.append(fullJson);
        }
        return response.toString();
    }
}
