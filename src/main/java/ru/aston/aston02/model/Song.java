package ru.aston.aston02.model;

import java.time.Duration;

public class Song {
    private String title;
    private Duration duration;

    public Song(String title, Duration duration) {
        this.title = title;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public Duration getDuration() {
        return duration;
    }
}
