package ru.aston.aston02.model;

import java.util.ArrayList;
import java.util.List;

public class Artist {
    private String firstName;
    private String lastName;
    private Instrument mainInstrument;
    private List<VinylDisc> musicDiscs;

    public Artist(String firstName, String lastName, Instrument mainInstrument) {
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
}
