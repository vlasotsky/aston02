package ru.aston.aston02.model;

public enum Genre {
    // TODO: additional idea for genre characteristics - desired mood (rhythmic, moody, sentimental, motivational, concentrational, focusing)
    JAZZ("Jazz"),
    ROCK("Rock"),
    METAL("Metal"),
    POP("Pop");

    private final String name;

    Genre(String name) {
        this.name = name;
    }
}
