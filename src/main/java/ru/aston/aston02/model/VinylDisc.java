package ru.aston.aston02.model;

import ru.aston.aston02.model.to.VinylDiscDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class VinylDisc {
    private final String title;
    private final List<Artist> artists;
    private final List<Song> songs;
    private final Genre genre;
    private final String label;
    private final LocalDate releaseDate;

    public VinylDisc(String title, List<Artist> artists, List<Song> songs, Genre genre, String label, LocalDate releaseDate) {
        this.title = title;
        this.artists = artists;
        this.songs = songs;
        this.genre = genre;
        this.label = label;
        this.releaseDate = releaseDate;
    }

    public VinylDisc(VinylDiscDto discDto) {
        this.title = discDto.getTitle();
        this.artists = discDto.getArtists();
        this.songs = discDto.getSongs();
        this.genre = discDto.getGenre();
        this.label = discDto.getLabel();
        this.releaseDate = discDto.getReleaseDate();
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

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VinylDisc)) return false;
        VinylDisc disc = (VinylDisc) o;
        return title.equals(disc.title) && artists.equals(disc.artists) && songs.equals(disc.songs) && genre == disc.genre && label.equals(disc.label) && releaseDate.equals(disc.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artists, songs, genre, label, releaseDate);
    }
}
