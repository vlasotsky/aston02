package ru.aston.aston02.model;

import java.time.Year;
import java.util.List;

public class VinylDisc {
    private final String title;
    private final List<Artist> artists;
    private final List<Song> songs;
    private final Genre genre;
    private final String label;
    private final Year releaseYear;

    public VinylDisc(String title, List<Artist> artists, List<Song> songs, Genre genre, String label, Year releaseYear) {
        this.title = title;
        this.artists = artists;
        this.songs = songs;
        this.genre = genre;
        this.label = label;
        this.releaseYear = releaseYear;
    }

    public String getTitle() {
        return title;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public Genre getGenre() {
        return genre;
    }

    public String getLabel() {
        return label;
    }

    public Year getReleaseYear() {
        return releaseYear;
    }
}
