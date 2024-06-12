package ru.aston.aston02.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Artist {
    private final String firstName;
    private final String lastName;
    private final Instrument mainInstrument;
    private final List<VinylDisc> musicDiscs;

    public Artist(
            @JsonProperty("first_name") String firstName,
            @JsonProperty("last_name") String lastName,
            @JsonProperty("main_instrument") Instrument mainInstrument) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mainInstrument = mainInstrument;
        musicDiscs = new ArrayList<>();
    }

    public Artist(String firstName, String lastName, Instrument mainInstrument, List<VinylDisc> musicDiscs) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mainInstrument = mainInstrument;
        this.musicDiscs = musicDiscs;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Instrument getMainInstrument() {
        return mainInstrument;
    }

    public List<VinylDisc> getMusicDiscs() {
        return musicDiscs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artist)) return false;
        Artist artist = (Artist) o;
        return firstName.equals(artist.firstName) && lastName.equals(artist.lastName) && mainInstrument == artist.mainInstrument && musicDiscs.equals(artist.musicDiscs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, mainInstrument, musicDiscs);
    }
}
