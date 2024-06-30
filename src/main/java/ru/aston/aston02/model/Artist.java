package ru.aston.aston02.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistId;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Instrument mainInstrument;

    @ManyToMany(mappedBy = "artists")
    private List<VinylDisc> musicDiscs = new ArrayList<>();

    public Artist() {
    }

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

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public Long getArtistId() {
        return artistId;
    }
}
