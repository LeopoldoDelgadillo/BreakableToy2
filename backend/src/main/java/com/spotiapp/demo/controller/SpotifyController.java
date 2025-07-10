package com.spotiapp.demo.controller;

import com.spotiapp.demo.model.User;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;

@RestController
public class SpotifyController {

    @PostMapping("/auth/spotify")
    public String authRequest(@RequestBody User user) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(user.getClientID())
                .setClientSecret(user.getClientS())
                .setRedirectUri(SpotifyHttpManager.makeUri("http://127.0.0.1:9090/redirect"))
                .build();
        AuthorizationCodeUriRequest authCodeUriReq = spotifyApi.authorizationCodeUri()
                .build();
        URI uri = authCodeUriReq.execute();
        return "URI: "+uri.toString();
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

}
