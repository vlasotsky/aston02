package ru.aston.aston02.model.to;

import ru.aston.aston02.model.Artist;
import ru.aston.aston02.model.Song;
import ru.aston.aston02.model.VinylDisc;

import java.time.Year;
import java.util.List;

public class VinylDiscTo {
    private final String title;
    private final List<Artist> artists;
    private final List<Song> songs;
    private final String label;
    private final Year releaseYear;
    private boolean isDuplicated;

    public VinylDiscTo(VinylDisc disc, boolean isDuplicated) {
        this.title = disc.getTitle();
        this.artists = disc.getArtists();
        this.songs = disc.getSongs();
        this.label = disc.getLabel();
        this.releaseYear = disc.getReleaseYear();
        this.isDuplicated = isDuplicated;
    }

    public VinylDiscTo(String title, List<Artist> artists, List<Song> songs, String label, Year releaseYear, boolean isDuplicated) {
        this.title = title;
        this.artists = artists;
        this.songs = songs;
        this.label = label;
        this.releaseYear = releaseYear;
        this.isDuplicated = isDuplicated;
    }
}
