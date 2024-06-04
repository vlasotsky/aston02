package ru.aston.aston02.repository.jdbc;

import ru.aston.aston02.model.*;
import ru.aston.aston02.repository.VinylDiscRepository;
import ru.aston.aston02.util.SqlUtil;

import java.sql.*;
import java.time.Duration;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static ru.aston.aston02.util.SqlUtil.getInstrumentId;
import static ru.aston.aston02.util.SqlUtil.retrieveId;

public class JDBCVinylDiscRepository implements VinylDiscRepository {
    private final SqlUtil sqlUtil;

    public JDBCVinylDiscRepository(String url, String user, String password) {
        sqlUtil = new SqlUtil(() -> DriverManager.getConnection(url, user, password));
    }

    @Override
    public void save(VinylDisc disc) {
        sqlUtil.executeTransaction(connection -> {
            try {
                final int discId = saveVinyl(connection, disc);
                saveArtist(connection, disc, discId);
                saveSong(connection, disc, discId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public VinylDisc get(int id) {
        final VinylDisc[] foundDisc = new VinylDisc[1];

        sqlUtil.executeTransaction(connection -> {
                    try (PreparedStatement statement = connection.prepareStatement("" +
                            "SELECT vd.title, vd.label, vd.release_year, gr.name " +
                            "FROM vinyl_disc vd " +
                            "INNER JOIN genre AS gr USING(genre_id) " +
                            "WHERE disc_id=?;")
                    ) {
                        statement.setInt(1, id);
                        final ResultSet rs = statement.executeQuery();

                        if (rs.next()) {
                            final String title = rs.getString("vd.title");
                            final Genre genre = Genre.valueOf(rs.getString("gr.name"));
                            final String label = rs.getString("vd.label");
                            final Year releaseYear = Year.parse(rs.getString("vd.release_year"));

                            List<Song> songList = getSongs(connection, id);
                            List<Artist> artistList = getArtists(connection, id);

                            foundDisc[0] = new VinylDisc(title, artistList, songList, genre, label, releaseYear);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
        );

        return foundDisc[0];
    }


    @Override
    public void update(int id, VinylDisc disc) {
        sqlUtil.executeTransaction(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("" +
                    "INSERT INTO vinyl_disc(title, genre_id, label, release_year) " +
                    "VALUES(?, ?, ?, ?) " +
                    "ON CONFLICT(disc_id) DO UPDATE " +
                    "SET title=excluded.title, " +
                    "genre_id=excluded.genre_id, " +
                    "label=excluded.label, " +
                    "release_year=excluded.release_year " +
                    "WHERE title=?;")) {

                statement.setString(1, disc.getTitle());
                statement.setInt(2, getGenreIndex(connection, disc.getGenre()));
                statement.setString(3, disc.getLabel());
                statement.setString(4, disc.getReleaseYear().toString());

                statement.executeUpdate();

                // TODO: complete another updates of lists belonging to disc
            }
        });
    }

    @Override
    public void delete(int id) {
        sqlUtil.executeStatement("" +
                        "DELETE FROM vinyl_disc vd " +
                        "USING(vinyl_disc_artist) vda " +
                        "WHERE vd.disc_id=? AND vda.disc_id=?;",
                ps -> {
                    ps.setInt(1, id);
                    ps.setInt(2, id);

                    ps.executeUpdate();
                });
    }

    @Override
    public List<VinylDisc> getAll() {
        List<VinylDisc> allDiscs = new ArrayList<>();

        sqlUtil.executeTransaction(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("" +
                    "SELECT disc_id, title, gr.name, label, release_year " +
                    "FROM vinyl_disc vd " +
                    "INNER JOIN genre gr USING(genre_id);")
            ) {

                final ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    final String title = rs.getString("title");
                    final Genre genre = Genre.valueOf(rs.getString("gr.name"));
                    final String label = rs.getString("label");
                    final Year releaseYear = Year.parse(rs.getString("release_year"));

                    final int discId = rs.getInt("disc_id");
                    final List<Song> songs = getSongs(connection, discId);
                    final List<Artist> artists = getArtists(connection, discId);

                    allDiscs.add(new VinylDisc(title, artists, songs, genre, label, releaseYear));
                }
            }
        });

        return allDiscs;
    }

    private int getGenreIndex(Connection connection, Genre genre) throws SQLException {
        // TODO: do helper private methods with new "execute" ones
        try (PreparedStatement statement = connection.prepareStatement("" +
                "SELECT genre_id " +
                "FROM genre " +
                "WHERE name=?;")) {

            statement.setString(1, genre.name());
            final ResultSet rs = statement.executeQuery();

            return rs.getInt(1);
        }
    }

    private int saveVinyl(Connection connection, VinylDisc disc) throws SQLException {
        // TODO: do helper private methods with new "execute" ones
        try (PreparedStatement psVinyl = connection.prepareStatement("" +
                "INSERT INTO vinyl_disc(title, genre_id, label, release_year) " +
                "VALUES(?, ?, ?, ?);")) {
            psVinyl.setString(1, disc.getTitle());
            psVinyl.setString(2, disc.getGenre().name());
            psVinyl.setString(3, disc.getLabel());
            psVinyl.setInt(4, disc.getReleaseYear().getValue());

            psVinyl.executeUpdate();

            return retrieveId(psVinyl);
        }
    }

    private void saveArtist(Connection connection, VinylDisc disc, int discId) throws SQLException {
        // TODO: do helper private methods with new "execute" ones
        try (PreparedStatement psArtist = connection.prepareStatement("" +
                "INSERT INTO artist(first_name, last_name, instr_id) " +
                "VALUES(?, ?, ?);");
             PreparedStatement psJoining = connection.prepareStatement("" +
                     "INSERT INTO vinyl_disc_artist(artist_id, disc_id) " +
                     "VALUES (?, ?);")) {

            for (Artist artist : disc.getArtists()) {
                psArtist.setString(1, artist.getFirstName());
                psArtist.setString(2, artist.getLastName());
                psArtist.setInt(3, getInstrumentId(connection, artist.getMainInstrument()));

                psJoining.setInt(1, retrieveId(psArtist));
                psJoining.setInt(2, discId);

                psArtist.addBatch();
                psJoining.addBatch();
            }

            psArtist.executeBatch();
            psJoining.executeBatch();
        }
    }

    private void saveSong(Connection connection, VinylDisc disc, int discId) throws SQLException {
        // TODO: do helper private methods with new "execute" ones
        try (PreparedStatement psSong = connection.prepareStatement("" +
                "INSERT INTO song(title, duration, disc_id) " +
                "VALUES(?, ?, ?);")) {

            for (Song song : disc.getSongs()) {
                psSong.setString(1, song.getTitle());
                psSong.setString(2, song.getDuration().toString());
                psSong.setInt(3, discId);

                psSong.addBatch();
            }

            psSong.executeBatch();
        }
    }


    private List<Song> getSongs(Connection connection, int id) throws SQLException {
        // TODO: do helper private methods with new "execute" ones
        try (PreparedStatement statement = connection.prepareStatement("" +
                "SELECT title, duration " +
                "FROM song " +
                "WHERE disc_id=?;")) {
            statement.setInt(1, id);
            final ResultSet rs = statement.executeQuery();
            List<Song> songs = new ArrayList<>();

            while (rs.next()) {
                final String songTitle = rs.getString("title");
                final Duration duration = Duration.parse(rs.getString("duration"));

                songs.add(new Song(songTitle, duration));
            }
            return songs;
        }
    }

    private List<Artist> getArtists(Connection connection, int id) throws SQLException {
        // TODO: do helper private methods with new "execute" ones
        try (PreparedStatement statement = connection.prepareStatement("" +
                "SELECT ar.artist_id, ar.name, ar.last_name, instr.instr_name " +
                "FROM vinyl_disc_artist vda " +
                "INNER JOIN artist ar USING(artist_id) " +
                "INNER JOIN instrument instr ON ar.instr_id = instr.instr_id " +
                "WHERE vda.disc_id=?;")) {

            statement.setInt(1, id);
            final ResultSet rs = statement.executeQuery();
            List<Artist> artists = new ArrayList<>();

            while (rs.next()) {
                final String firstName = rs.getString("first_name");
                final String lastName = rs.getString("last_name");
                final String instrName = rs.getString("instr_name");
                final Instrument instrument = Instrument.valueOf(instrName);

                artists.add(new Artist(firstName, lastName, instrument));
            }

            return artists;
        }
    }

//    private List<VinylDisc> getDiscsByArtists(Connection connection, int id) throws SQLException {
//        try (PreparedStatement statement = connection.prepareStatement("" +
//                "SELECT  " +
//                "FROM vinyl_disc_artist " +
//                "INNER JOIN vinyl_disc USING(disc_id) " +
//                "WHERE artist_id=?;")) {
//
//        }
//        return null;
//    }
}
