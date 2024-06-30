package ru.aston.aston02.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "song")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songId;

    private String title;
    private int duration;

    @ManyToOne
    @JoinColumn(name = "disc_id")
    private VinylDisc vinylDisc;

    public Song() {
    }

    public Song(
            @JsonProperty("title") String title,
            @JsonProperty("duration") int duration) {
        this.title = title;
        this.duration = duration;
    }

    public Long getSongId() {
        return songId;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return duration == song.duration && title.equals(song.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, duration);
    }
}
