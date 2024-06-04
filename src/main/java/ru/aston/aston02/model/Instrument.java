package ru.aston.aston02.model;

public enum Instrument {
    // TODO: additional functions for instruments
    VOCALS("Vocals"),
    GUITAR("Guitar"),
    KEYS("Keys"),
    BASS("Bass"),
    DOUBLE_BASS("Double Bass"),
    BANJO("Banjo"),
    DRUMS("Drums");

    private final String name;

    Instrument(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
