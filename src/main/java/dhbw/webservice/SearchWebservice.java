/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw.webservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import dhbw.pojo.result.search.SearchResult;
import dhbw.pojo.result.search.SearchResultList;
import dhbw.pojo.search.album.SearchAlbum;
import dhbw.pojo.search.artist.SearchArtist;
import dhbw.pojo.search.track.SearchTrack;
import dhbw.spotify.RequestCategory;
import dhbw.spotify.RequestType;
import dhbw.spotify.SpotifyRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Foecking, mrastetter and Lischka
 */
@RestController
public class SearchWebservice {

    @RequestMapping("/search")
    public String searchResultJson(@RequestParam(value = "query") String query, @RequestParam(value = "type") String type) {
        SpotifyRequest spotifyRequest = new SpotifyRequest(RequestType.SEARCH);
        Optional<String> stringOptional;
        RequestCategory category;
        category = RequestCategory.valueOf(type);
        String s = "";
        String json = "";
        
        // TRY-Block
        try {
            stringOptional = spotifyRequest.performeRequestSearch(category, query, 10, "DE");
            if (stringOptional.isPresent()) {
                s = stringOptional.get();
            }
            ObjectMapper om = new ObjectMapper();
            List<dhbw.pojo.search.track.Item> itemTrack = new ArrayList<>();
            List<dhbw.pojo.search.album.Item> itemAlbum = new ArrayList<>();
            List<dhbw.pojo.search.artist.Item> itemArtist = new ArrayList<>();
            List<SearchResultList> resultList = new ArrayList<>();
            switch (type) {
                
                // Falls Track gesucht wird
                
                case "TRACK":
                    SearchTrack track = om.readValue(s, SearchTrack.class);
                    itemTrack = track.getTracks().getItems();
                    for (dhbw.pojo.search.track.Item element : itemTrack) {
                        String id = element.getId();
                        String title = element.getName();
                        String description = element.getType();
                        String playLink = element.getUri();
                        SearchResultList resultItemList = new SearchResultList(id, title, description, playLink);
                        resultList.add(resultItemList);
                    }
                    break;
                    
                    // Falls nach Künstler gesucht wird
                    
                case "ARTIST":
                    SearchArtist artist = om.readValue(s, SearchArtist.class);
                    itemArtist = artist.getArtists().getItems();
                    for (dhbw.pojo.search.artist.Item element : itemArtist) {
                        String id = element.getId();
                        String title = element.getName();
                        String description = element.getType();
                        String playLink = element.getUri();
                        SearchResultList resultItemList = new SearchResultList(id, title, description, playLink);
                        resultList.add(resultItemList);
                    }
                    break;
                    
                //Falls nach Album gesucht wird    
                case "ALBUM":
                    SearchAlbum album = om.readValue(s, SearchAlbum.class);
                    itemAlbum = album.getAlbums().getItems();
                    for (dhbw.pojo.search.album.Item element : itemAlbum) {
                        String id = element.getId();
                        String title = element.getName();
                        String description = element.getType();
                        String playLink = element.getUri();
                        SearchResultList resultItemList = new SearchResultList(id, title, description, playLink);
                        resultList.add(resultItemList);
                    }
                    break;
                default:

            }
            SearchResult searchResult = new SearchResult();
            searchResult.setSearchTerm(query);
            searchResult.setSearchCategory(type);
            searchResult.setResults(resultList);
            json = om.writeValueAsString(searchResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}


