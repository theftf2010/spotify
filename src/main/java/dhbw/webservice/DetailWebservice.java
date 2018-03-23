/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw.webservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import dhbw.pojo.detail.album.DetailsAlbum;
import dhbw.pojo.detail.artist.DetailsArtist;
import dhbw.pojo.detail.track.DetailsTrack;
import dhbw.pojo.result.detail.DetailResult;
import dhbw.spotify.RequestCategory;
import dhbw.spotify.RequestType;
import dhbw.spotify.SpotifyRequest;
import dhbw.spotify.WrongRequestTypeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Foecking
 */
@RestController
public class DetailWebservice {

    @RequestMapping("/detail/{id}")
    public String detailResultJson(@PathVariable(value = "id") String id, @RequestParam(value = "type") String type) {
        //Deklaration und Initialisierung
        String s = "", json = "", info = "", title = "";
        long durationmin, durationsec, durationrestsec = 0;
        SpotifyRequest spotifyRequestDetail = new SpotifyRequest(RequestType.DETAIL);
        RequestCategory category = RequestCategory.valueOf(type);
        DetailResult result = new DetailResult();
        ObjectMapper om = new ObjectMapper();
        Optional<String> stringOptional = Optional.empty();

        // Try-Catch Anweisung
        try {
            stringOptional = spotifyRequestDetail.performeRequestDetail(category, id);

            // Wenn stringOptional exisitert
            if (stringOptional.isPresent()) // Aufruf get methode, exceptions im catch
            {
                s = stringOptional.get();
            }

            // Switch-Case Anweisung
            switch (type) {

                // Track
                case "TRACK":
                    DetailsTrack detailsTrack = om.readValue(s, DetailsTrack.class);

                    durationsec = detailsTrack.getDurationMs() / 1000;
                    durationmin = durationsec / 60;
                    durationrestsec = durationsec % 60;

                    //Umwandlung in ArrayLists
                    ArrayList<String> listTrackArtists = new ArrayList<>();
                    for (int i = 0; i < detailsTrack.getArtists().size(); i++) {
                        listTrackArtists.add(detailsTrack.getArtists().get(i).getName());
                    }

                    //Initialisieren
                    title = detailsTrack.getName();
                    info = "type: " + detailsTrack.getType() + "\n" + "artists: " + sb(listTrackArtists).toString() + "\n" + "album: " + detailsTrack.getAlbum().getName() + "\n" + "duration: " + Long.toString(durationmin) + " min " + Long.toString(durationrestsec) + " sec" + "\n" + "popularity: " + detailsTrack.getPopularity().toString() + " %";
                    result = new DetailResult(title, info);
                    break;

                // Artist
                case "ARTIST":
                    DetailsArtist detailsArtist = om.readValue(s, DetailsArtist.class);

                    //Umwandlung in ArrayLists
                    ArrayList<String> listArtistGenres = new ArrayList<>();
                    for (int i = 0; i < detailsArtist.getGenres().size(); i++) {
                        listArtistGenres.add(detailsArtist.getGenres().get(i));
                    }

                    //Initialisieren
                    title = detailsArtist.getName();
                    info = "type: " + detailsArtist.getType() + "\n" + "genres: " + sb(listArtistGenres).toString() + "\n" + "followers: " + detailsArtist.getFollowers().getTotal().toString() + "\n" + "popularity: " + detailsArtist.getPopularity().toString() + " %";
                    result = new DetailResult(title, info);

                    break;

                // Album
                case "ALBUM":
                    DetailsAlbum detailsAlbum = om.readValue(s, DetailsAlbum.class);

                    //Umwandlung in ArrayLists
                    ArrayList<String> listAlbumArtists = new ArrayList<>();
                    for (int i = 0; i < detailsAlbum.getArtists().size(); i++) {
                        listAlbumArtists.add(detailsAlbum.getArtists().get(i).getName());
                    }

                    ArrayList<String> listAlbumGenres = new ArrayList<>();
                    for (int i = 0; i < detailsAlbum.getGenres().size(); i++) {
                        listAlbumGenres.add(detailsAlbum.getGenres().get(i).toString());
                    }

                    //Initialisieren
                    title = detailsAlbum.getName();
                    info = "type: " + detailsAlbum.getType() + "\n" + "artists: " + sb(listAlbumArtists).toString() + "\n" + "label: " + detailsAlbum.getLabel() + "\n" + "genres: " + sb(listAlbumGenres).toString() + "\n" + "release date: " + detailsAlbum.getReleaseDate() + "\n" + "tracks: " + detailsAlbum.getTracks().getTotal().toString() + "\n" + "popularity: " + detailsAlbum.getPopularity().toString() + " %";
                    result = new DetailResult(title, info);
                    break;

                default:
            }

            // Initialisierung objectmapper
            json = new ObjectMapper().writeValueAsString(result);
        } // Abfangen Exceptions
        catch (WrongRequestTypeException | IOException e) {
            e.printStackTrace();
        }

        // Rückgabe JSON
        return json;
    }

    //StringBuilder Klasse um aus ArrayLists Strings zu bilden
    public StringBuilder sb(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();

        list.stream().map((string) -> {
            sb.append(string);
            return string;
        }).forEach((string) -> {
            if (string.equals(list.get(list.size() - 1))) {

            } else {
                sb.append(", ");
            }
        });
        return sb;
    }

}
