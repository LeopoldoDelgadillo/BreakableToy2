package com.spotiapp.demo.controller;

import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.spotiapp.demo.service.UserService;
import jakarta.servlet.http.Cookie;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpotifyController.class)
public class SpotifyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    String[] client = {"",""};
    String[] user = {"",""};
    
    String sessionID = "sessionID";
    String userCountry = "Country";
    @BeforeEach
    void setUp() {
        client[0] = "client_id";
        client[1] = "client_secret";
        user[0] = "access_token";
        user[1] = "refresh_token";
    }

    @Test
    void shouldReturnUserTop10() throws Exception {
        String url = "https://api.spotify.com/v1/me/top/artists?time_range=medium_term&limit=10&offset=0";

        String response = """
                {
                    "items": [{"name": "top1"}]
                }
                """;

        when(userService.extractClientIDSecret()).thenReturn(client);
        when(userService.readUserTokenFile(sessionID)).thenReturn(user);
        when(userService.requestHTTPspotifyAPI(sessionID, url, "Not needed", "generic", null, "Not needed", "Not needed")).thenReturn(response);
        mockMvc.perform(get("/me/top/artists")
                .cookie(new Cookie("sessionID",sessionID))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.items[0].name").value("top1"));
    }
    
    @Test
    void shouldReturnArtist() throws Exception {
        String id = "ArtistID";
        String url = "https://api.spotify.com/v1/artists/"+id;

        String response = """
                {
                    "name": "Creeku",
                    "followers": {"total": 10}
                }
                """;

        when(userService.extractClientIDSecret()).thenReturn(client);
        when(userService.readUserTokenFile(sessionID)).thenReturn(user);
        when(userService.requestHTTPspotifyAPI(sessionID, url, "Not needed", "generic", null, "Not needed", "Not needed")).thenReturn(response);
        mockMvc.perform(get("/artists/"+id)
                .cookie(new Cookie("sessionID",sessionID))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("Creeku"))
            .andExpect(jsonPath("$.followers.total").value("10"));
    }

    @Test
    void shouldReturnArtistTopTracks() throws Exception {
        String id = "ArtistID";
        String url = "https://api.spotify.com/v1/artists/"+id+"/top-tracks?market="+userCountry;

        String response = """
                {
                    "tracks": [
                        {
                            "album": {
                                "album_type": "album"
                            },
                            "artists": [
                            {
                                "name": "Artist",
                                "type": "artist"
                            }
                            ],
                            "duration_ms": 1000,
                            "id": "TrackID",
                            "name": "TrackName",
                            "type": "track"
                        }
                    ]
                }
                """;
        when(userService.extractClientIDSecret()).thenReturn(client);
        when(userService.readUserTokenFile(sessionID)).thenReturn(user);
        when(userService.requestHTTPspotifyAPI(sessionID, url, "Not needed", "generic", null, "Not needed", "Not needed")).thenReturn(response);
        mockMvc.perform(get("/artists/"+id+"/top-tracks?country="+userCountry)
                .cookie(new Cookie("sessionID",sessionID))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.tracks").isArray())
            .andExpect(jsonPath("$.tracks[0].album.album_type").value("album"))
            .andExpect(jsonPath("$.tracks[0].artists[0].name").value("Artist"))
            .andExpect(jsonPath("$.tracks[0].artists[0].type").value("artist"))
            .andExpect(jsonPath("$.tracks[0].duration_ms").value(1000))
            .andExpect(jsonPath("$.tracks[0].id").value("TrackID"))
            .andExpect(jsonPath("$.tracks[0].name").value("TrackName"))
            .andExpect(jsonPath("$.tracks[0].type").value("track"));
    }

    @Test
    void shouldReturnArtistAlbums() throws Exception {
        String id = "ArtistID";
        String url = "https://api.spotify.com/v1/artists/"+id+"/albums?include_groups=album%2Csingle&market="+userCountry;
        String response = """
                {
                    "items": [
                        {
                            "album_type": "album",
                            "total_tracks": 10,
                            "images": [{"url": "url"}],
                            "name": "AlbumName",
                            "release_date": "2000-02",
                            "artists": [
                                {
                                    "name": "Artist",
                                    "type": "artist"
                                }
                            ],
                            "id": "AlbumID",
                            "type": "album"
                        }
                    ]
                }
                """;
        when(userService.extractClientIDSecret()).thenReturn(client);
        when(userService.readUserTokenFile(sessionID)).thenReturn(user);
        when(userService.requestHTTPspotifyAPI(sessionID, url, "Not needed", "generic", null, "Not needed", "Not needed")).thenReturn(response);
        mockMvc.perform(get("/artists/"+id+"/albums?country="+userCountry)
                .cookie(new Cookie("sessionID",sessionID))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.items[0].album_type").value("album"))
            .andExpect(jsonPath("$.items[0].artists[0].name").value("Artist"))
            .andExpect(jsonPath("$.items[0].artists[0].type").value("artist"))
            .andExpect(jsonPath("$.items[0].release_date").value("2000-02"))
            .andExpect(jsonPath("$.items[0].total_tracks").value(10))
            .andExpect(jsonPath("$.items[0].id").value("AlbumID"))
            .andExpect(jsonPath("$.items[0].name").value("AlbumName"))
            .andExpect(jsonPath("$.items[0].type").value("album"))
            .andExpect(jsonPath("$.items[0].images[0].url").value("url"));
    }

    @Test
    void shouldReturnTrack() throws Exception {
        String id = "TrackID";
        String url = "https://api.spotify.com/v1/tracks/"+id+"?market="+userCountry;
        String response = """
                {
                    "album": {
                        "album_type": "album",
                        "total_tracks": 10,
                        "images": [{"url": "url"}],
                        "name": "AlbumName",
                        "release_date": "2000-02",
                        "id": "AlbumID",
                        "type": "album"
                    },
                    "artists": [
                        {
                            "name": "Artist",
                            "type": "artist"
                        }
                    ],
                    "duration_ms": 1000,
                    "id": "TrackID",
                    "name": "TrackName",
                    "type": "track",
                }
                """;
        when(userService.extractClientIDSecret()).thenReturn(client);
        when(userService.readUserTokenFile(sessionID)).thenReturn(user);
        when(userService.requestHTTPspotifyAPI(sessionID, url, "Not needed", "generic", null, "Not needed", "Not needed")).thenReturn(response);
        mockMvc.perform(get("/tracks/"+id+"?country="+userCountry)
                .cookie(new Cookie("sessionID",sessionID))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$.album.album_type").value("album"))
            .andExpect(jsonPath("$.artists[0].name").value("Artist"))
            .andExpect(jsonPath("$.artists[0].type").value("artist"))
            .andExpect(jsonPath("$.album.release_date").value("2000-02"))
            .andExpect(jsonPath("$.album.total_tracks").value(10))
            .andExpect(jsonPath("$.album.id").value("AlbumID"))
            .andExpect(jsonPath("$.album.name").value("AlbumName"))
            .andExpect(jsonPath("$.album.type").value("album"))
            .andExpect(jsonPath("$.album.images[0].url").value("url"))
            .andExpect(jsonPath("$.duration_ms").value(1000))
            .andExpect(jsonPath("$.id").value("TrackID"))
            .andExpect(jsonPath("$.name").value("TrackName"))
            .andExpect(jsonPath("$.type").value("track"));

    }

    @Test
    void shouldReturnAlbum() throws Exception {
        String id = "AlbumID";
            String url = "https://api.spotify.com/v1/albums/"+id+"?market="+userCountry;
        String response = """
                {
                    "album_type": "album",
                    "total_tracks": 10,
                    "images": [{"url": "url"}],
                    "name": "AlbumName",
                    "release_date": "2000-02",
                    "id": "AlbumID",
                    "type": "album",
                    "artists": [
                        {
                            "name": "Artist",
                            "type": "artist"
                        }
                    ],
                    "tracks": {
                        "items": [
                            {
                                "duration_ms": 1000,
                                "id": "TrackID",
                                "name": "TrackName",
                                "type": "track"
                            }
                        ]
                    }
                }
                """;
        when(userService.extractClientIDSecret()).thenReturn(client);
        when(userService.readUserTokenFile(sessionID)).thenReturn(user);
        when(userService.requestHTTPspotifyAPI(sessionID, url, "Not needed", "generic", null, "Not needed", "Not needed")).thenReturn(response);
        mockMvc.perform(get("/albums/"+id+"?country="+userCountry)
                .cookie(new Cookie("sessionID",sessionID))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$.album_type").value("album"))
            .andExpect(jsonPath("$.artists[0].name").value("Artist"))
            .andExpect(jsonPath("$.artists[0].type").value("artist"))
            .andExpect(jsonPath("$.release_date").value("2000-02"))
            .andExpect(jsonPath("$.total_tracks").value(10))
            .andExpect(jsonPath("$.id").value("AlbumID"))
            .andExpect(jsonPath("$.name").value("AlbumName"))
            .andExpect(jsonPath("$.type").value("album"))
            .andExpect(jsonPath("$.images[0].url").value("url"))
            .andExpect(jsonPath("$.tracks.items[0].duration_ms").value(1000))
            .andExpect(jsonPath("$.tracks.items[0].id").value("TrackID"))
            .andExpect(jsonPath("$.tracks.items[0].name").value("TrackName"))
            .andExpect(jsonPath("$.tracks.items[0].type").value("track"));
    }

    @Test
    void shouldReturnAlbumTracks() throws Exception {
        String id = "AlbumID";
        Integer trackCount = 80;
        String response = """
                [{
                    "items": [
                        {
                            "artists": [
                                {
                                    "name": "Artist",
                                    "type": "artist"
                                }
                            ],
                            "duration_ms": 1000,
                            "id": "TrackID",
                            "name": "TrackName",
                            "track_number": 0,
                            "type": "track"
                        }
                    ]
                }]
                """;
        when(userService.extractClientIDSecret()).thenReturn(client);
        when(userService.readUserTokenFile(sessionID)).thenReturn(user);
        when(userService.requestHTTPspotifyAPI(sessionID, "", "Not needed", "albumTracks", trackCount, userCountry, id)).thenReturn(response);
        mockMvc.perform(get("/albums/"+id+"/tracks?country="+userCountry+"&trackCount="+trackCount)
                .cookie(new Cookie("sessionID",sessionID))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].items[0].artists[0].name").value("Artist"))
            .andExpect(jsonPath("$[0].items[0].artists[0].type").value("artist"))
            .andExpect(jsonPath("$[0].items[0].duration_ms").value(1000))
            .andExpect(jsonPath("$[0].items[0].id").value("TrackID"))
            .andExpect(jsonPath("$[0].items[0].name").value("TrackName"))
            .andExpect(jsonPath("$[0].items[0].type").value("track"));
    }

    @Test
    void shouldReturnSearch() throws Exception {
        
        String url = "https://api.spotify.com/v1/search?q=Memory&type=track%2Cartist%2Calbum&market=MX&limit=10&offset=0&include_external=audio";
        String body = """
                {
                    "searchString": "Memory",
                    "userCountry": "MX"
                }
                """;
        String response = """
            {
                "albums": {
                    items: [
                        {"name": "Fake Album"}
                    ]
                },
                "artists": {
                    items: [
                        {"name": "Fake Artist"}
                    ]
                },
                "tracks": {
                    items: [
                        {"name": "Fake Track"}
                    ]
                }
            }
            """;
        when(userService.extractClientIDSecret()).thenReturn(client);
        when(userService.readUserTokenFile(sessionID)).thenReturn(user);
        when(userService.requestHTTPspotifyAPI(sessionID, url, "Not needed", "generic", null, "Not needed", "Not needed")).thenReturn(response);
        mockMvc.perform(post("/search")
                .cookie(new Cookie("sessionID",sessionID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.albums").exists())
            .andExpect(jsonPath("$.artists").exists())
            .andExpect(jsonPath("$.tracks").exists())
            .andExpect(jsonPath("$.albums.items").isArray())
            .andExpect(jsonPath("$.artists.items").isArray())
            .andExpect(jsonPath("$.tracks.items").isArray());
    }

    @Test
    void shouldReturnProfile() throws Exception {
        String response = """
            {
                "display_name": "John Doe",
                "type": "user"
            }
            """;
        when(userService.extractClientIDSecret()).thenReturn(client);
        when(userService.readUserTokenFile(sessionID)).thenReturn(user);
        when(userService.requestHTTPspotifyAPI(sessionID, "https://api.spotify.com/v1/me", "Not needed", "generic", null, "Not needed", "Not needed")).thenReturn(response);
        mockMvc.perform(get("/me")
                .cookie(new Cookie("sessionID",sessionID))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.display_name").value("John Doe"))
            .andExpect(jsonPath("$.type").value("user"));
    }

    

    

    

    

    

    

    

    
}
