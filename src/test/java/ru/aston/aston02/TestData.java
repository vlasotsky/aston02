package ru.aston.aston02;

import ru.aston.aston02.model.*;

import java.time.LocalDate;
import java.util.List;

public class TestData {
    public static final VinylDisc DUMMY_DISC;
    public static final VinylDisc DUMMY_DISC_CHANGED;

    public static final String DUMMY_STRING;
    public static final long DUMMY_LONG;
    public static final List<Artist> DUMMY_ARTISTS;
    public static final List<Song> DUMMY_SONGS;

    public static final String JSON_DISC;
    public static final String FIRST_ELEMENT_ID;

    public static final LocalDate DUMMY_LOCAL_DATE = LocalDate.of(2024, 6, 6);

    static {
        DUMMY_STRING = "DUMMY";
        DUMMY_LONG = 999999;
        DUMMY_ARTISTS = List.of(new Artist("Artist", "Artist", Instrument.VOCALS));
        DUMMY_SONGS = List.of(new Song(DUMMY_STRING, 0));

        DUMMY_DISC = new VinylDisc(DUMMY_STRING, DUMMY_ARTISTS, DUMMY_SONGS, Genre.FUNK, DUMMY_STRING, DUMMY_LOCAL_DATE);
        DUMMY_DISC_CHANGED = new VinylDisc(DUMMY_STRING, DUMMY_ARTISTS, DUMMY_SONGS, Genre.POP, DUMMY_STRING, LocalDate.now());

        FIRST_ELEMENT_ID = "1";
        JSON_DISC = "{" +
                "\"title\":\"Test Disc\"," +
                "\"artists\":[" +
                "{" +
                "\"first_name\":\"Test Artist\"," +
                "\"last_name\":\"test\"," +
                "\"main_instrument\":\"VOCALS\"" +
                "}]," +
                "\"songs\":[" +
                "{" +
                "\"title\":\"test\"," +
                "\"duration\":100" +
                "}]," +
                "\"genre\":\"ROCK\"," +
                "\"label\":\"test\"," +
                "\"release_date\":\"2022-01-01\"," +
                "\"is_few\":true" +
                "}";
    }
}
