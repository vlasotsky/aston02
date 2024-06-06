package ru.aston.aston02.model.to;

import ru.aston.aston02.model.Artist;
import ru.aston.aston02.model.Song;
import ru.aston.aston02.model.VinylDisc;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class VinylDiscTo {
    private final String title;
    private final List<Artist> artists;
    private final List<Song> songs;
    private final String label;
    private final LocalDate releaseDate;
    private boolean isFew;

    public VinylDiscTo(VinylDisc disc, boolean isFew) {
        this.title = disc.getTitle();
        this.artists = disc.getArtists();
        this.songs = disc.getSongs();
        this.label = disc.getLabel();
        this.releaseDate = disc.getReleaseDate();
        this.isFew = isFew;
    }

    public VinylDiscTo(String title, List<Artist> artists, List<Song> songs, String label, LocalDate releaseDate, boolean isFew) {
        this.title = title;
        this.artists = artists;
        this.songs = songs;
        this.label = label;
        this.releaseDate = releaseDate;
        this.isFew = isFew;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VinylDiscTo)) return false;
        VinylDiscTo that = (VinylDiscTo) o;
        return isFew == that.isFew && title.equals(that.title) && artists.equals(that.artists) && songs.equals(that.songs) && label.equals(that.label) && releaseDate.equals(that.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artists, songs, label, releaseDate, isFew);
    }
}
