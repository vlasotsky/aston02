package ru.aston.aston02.model.to;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.aston.aston02.model.Artist;
import ru.aston.aston02.model.Genre;
import ru.aston.aston02.model.Song;
import ru.aston.aston02.model.VinylDisc;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class VinylDiscDto {
    private final String title;
    private final List<Artist> artists;
    private final List<Song> songs;
    private final Genre genre;
    private final String label;
    private final LocalDate releaseDate;
    private final boolean isFew;

    public VinylDiscDto(VinylDisc disc, boolean isFew) {
        this.title = disc.getTitle();
        this.artists = disc.getArtists();
        this.songs = disc.getSongs();
        this.genre = disc.getGenre();
        this.label = disc.getLabel();
        this.releaseDate = disc.getReleaseDate();
        this.isFew = isFew;
    }

    @JsonCreator
    public VinylDiscDto(
            @JsonProperty("title") String title,
            @JsonProperty("artists") List<Artist> artists,
            @JsonProperty("songs") List<Song> songs,
            @JsonProperty("genre") Genre genre,
            @JsonProperty("label") String label,
            @JsonProperty("release_date") LocalDate releaseDate,
            @JsonProperty("is_few") boolean isFew) {
        this.title = title;
        this.artists = artists;
        this.songs = songs;
        this.genre = genre;
        this.label = label;
        this.releaseDate = releaseDate;
        this.isFew = isFew;
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

    public String getLabel() {
        return label;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public boolean isFew() {
        return isFew;
    }

    public Genre getGenre() {
        return genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VinylDiscDto)) return false;
        VinylDiscDto that = (VinylDiscDto) o;
        return isFew == that.isFew && title.equals(that.title) && artists.equals(that.artists) && songs.equals(that.songs) && label.equals(that.label) && releaseDate.equals(that.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artists, songs, label, releaseDate, isFew);
    }
}
