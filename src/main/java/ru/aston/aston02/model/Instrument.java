package ru.aston.aston02.model;

public enum Instrument {
    VOCALS("Vocals"),
    GUITAR("Guitar"),
    KEYS("Keys"),
    BASS("Bass"),
    DOUBLE_BASS("Double Bass"),
    BANJO("Banjo"),
    DRUMS("Drums"),
    SAX("Saxophone");

    private final String name;

    Instrument(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
