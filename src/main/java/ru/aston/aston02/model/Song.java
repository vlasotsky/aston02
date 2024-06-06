package ru.aston.aston02.model;

import java.util.Objects;

public class Song {
    private final String title;
    private int duration;

    public Song(String title, int duration) {
        this.title = title;
        this.duration = duration;
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