package ru.aston.aston02.repository.jdbc;

import ru.aston.aston02.model.*;
import ru.aston.aston02.repository.Repository;
import ru.aston.aston02.util.SqlUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCVinylDiscRepository implements Repository<Long, VinylDisc> {
    private final SqlUtil sqlUtil;

    public JDBCVinylDiscRepository(String url, String user, String password) {
        sqlUtil = new SqlUtil(() -> DriverManager.getConnection(url, user, password));
    }

    @Override
    public void save(VinylDisc disc) {
        sqlUtil.executeTransaction(connection -> {
            try {
                final long discId = saveVinyl(connection, disc);
                saveArtists(connection, disc, discId);
                saveSong(connection, disc, discId);
            } catch (SQLException e) {
                throw new SQLException("Failed to save a new disc", e);
            }
        });
    }

    @Override
    public VinylDisc get(Long id) {
        final VinylDisc[] foundDisc = new VinylDisc[1];

        sqlUtil.executeTransaction(connection -> {
                    try (PreparedStatement statement = connection.prepareStatement("" +
                            "SELECT vd.title, vd.label, vd.release_date, gr.name " +
                            "FROM vinyl_disc vd " +
                            "INNER JOIN genre AS gr USING(genre_id) " +
                            "WHERE disc_id=?;")) {

                        statement.setLong(1, id);
                        final ResultSet rs = statement.executeQuery();

                        if (rs.next()) {
                            final String title = rs.getString("title");
                            final Genre genre = Genre.valueOf(rs.getString("name"));
                            final String label = rs.getString("label");
                            final LocalDate releaseDate = LocalDate.parse(rs.getString("release_date"));

                            List<Song> songList = getSongs(connection, id);
                            List<Artist> artistList = getArtists(connection, id);

                            foundDisc[0] = new VinylDisc(title, artistList, songList, genre, label, releaseDate);
                        } else {
                            throw new IllegalArgumentException("No such element in database with id: " + id);
                        }
                    } catch (SQLException e) {
                        throw new SQLException("Error while executing statement", e);
                    }
                }
        );

        return foundDisc[0];
    }


    @Override
    public void update(Long id, VinylDisc disc) {
        sqlUtil.executeTransaction(connection -> {
            try (PreparedStatement updateBasic = connection.prepareStatement("" +
                    "INSERT INTO vinyl_disc(disc_id, title, genre_id, label, release_date) " +
                    "VALUES(?, ?, ?, ?, ?) " +
                    "ON CONFLICT(disc_id) DO UPDATE " +
                    "SET title=excluded.title, " +
                    "genre_id=excluded.genre_id, " +
                    "label=excluded.label, " +
                    "release_date=excluded.release_date;");
                 PreparedStatement clearSongs = connection.prepareStatement("" +
                         "DELETE FROM song " +
                         "WHERE disc_id=?;");
                 PreparedStatement updateSongs = connection.prepareStatement("" +
                         "INSERT INTO song(disc_id, title, duration) " +
                         "VALUES(?, ?, ?);");
                 PreparedStatement clearArtists = connection.prepareStatement("" +
                         "DELETE FROM vinyl_disc_artist " +
                         "WHERE disc_id=?;");
                 PreparedStatement updateJoin = connection.prepareStatement("" +
                         "INSERT INTO vinyl_disc_artist(disc_id, artist_id) " +
                         "VALUES(?, ?);");
                 PreparedStatement updateArtists = connection.prepareStatement("" +
                         "INSERT INTO artist(first_name, last_name, instr_id) " +
                         "VALUES(?, ?, ?) ON CONFLICT DO NOTHING;")
            ) {

                updateBasic.setLong(1, id);
                updateBasic.setString(2, disc.getTitle());
                updateBasic.setInt(3, getGenreIndex(connection, disc.getGenre()));
                updateBasic.setString(4, disc.getLabel());
                updateBasic.setDate(5, Date.valueOf(disc.getReleaseDate()));

                updateBasic.executeUpdate();

                clearSongs.setLong(1, id);
                clearSongs.executeUpdate();

                for (Song song : disc.getSongs()) {
                    updateSongs.setLong(1, id);
                    updateSongs.setString(2, song.getTitle());
                    updateSongs.setInt(3, song.getDuration());

                    updateSongs.addBatch();
                }

                updateSongs.executeBatch();

                clearArtists.setLong(1, id);
                clearArtists.executeUpdate();

                saveArtists(connection, disc, id);

                updateArtists.executeBatch();
            }
        });
    }

    @Override
    public void delete(Long id) {
        sqlUtil.executeTransaction(connection -> {
            try (PreparedStatement psDeleteJoin = connection.prepareStatement("" +
                    "DELETE FROM vinyl_disc_artist " +
                    "WHERE disc_id=?;");
                 PreparedStatement psDeleteSong = connection.prepareStatement("" +
                         "DELETE FROM song " +
                         "WHERE disc_id=?;");
                 PreparedStatement psDeleteDisc = connection.prepareStatement("" +
                         "DELETE FROM vinyl_disc " +
                         "WHERE disc_id=?;")) {

                psDeleteJoin.setLong(1, id);
                psDeleteSong.setLong(1, id);
                psDeleteDisc.setLong(1, id);

                if (psDeleteJoin.executeUpdate() == 0) {
                    throw new IllegalArgumentException("No such disc with id: " + id);
                }

                psDeleteSong.executeUpdate();
                psDeleteDisc.executeUpdate();
            }
        });
    }

    @Override
    public List<VinylDisc> getAll() {
        List<VinylDisc> allDiscs = new ArrayList<>();

        sqlUtil.executeTransaction(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("" +
                    "SELECT disc_id, title, name, label, release_date " +
                    "FROM vinyl_disc vd " +
                    "INNER JOIN genre USING(genre_id);")
            ) {

                final ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    final String title = rs.getString("title");
                    final Genre genre = Genre.valueOf(rs.getString("name"));
                    final String label = rs.getString("label");
                    final LocalDate releaseDate = LocalDate.parse(rs.getString("release_date"));

                    final long discId = rs.getLong("disc_id");
                    final List<Song> songs = getSongs(connection, discId);
                    final List<Artist> artists = getArtists(connection, discId);

                    allDiscs.add(new VinylDisc(title, artists, songs, genre, label, releaseDate));
                }
            }
        });

        return allDiscs;
    }

    private int getGenreIndex(Connection connection, Genre genre) throws SQLException {
        try (PreparedStatement statementInsert = connection.prepareStatement("" +
                "INSERT INTO genre(name) " +
                "VALUES(?) ON CONFLICT DO NOTHING;");
             PreparedStatement statementGet = connection.prepareStatement("" +
                     "SELECT genre_id " +
                     "FROM genre " +
                     "WHERE name=?;")) {

            statementInsert.setString(1, genre.name());
            statementGet.setString(1, genre.name());

            statementInsert.executeUpdate();
            final ResultSet rs = statementGet.executeQuery();
            rs.next();

            return rs.getInt(1);
        }
    }

    private Long saveVinyl(Connection connection, VinylDisc disc) throws SQLException {
        try (PreparedStatement psVinyl = connection.prepareStatement("" +
                "INSERT INTO vinyl_disc(title, genre_id, label, release_date) " +
                "VALUES(?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            psVinyl.setString(1, disc.getTitle());
            psVinyl.setInt(2, getGenreIndex(connection, disc.getGenre()));
            psVinyl.setString(3, disc.getLabel());
            psVinyl.setDate(4, Date.valueOf(disc.getReleaseDate()));

            psVinyl.executeUpdate();

            return retrieveId(psVinyl);
        }
    }

    private void saveArtists(Connection connection, VinylDisc disc, Long discId) throws SQLException {
        try (PreparedStatement psArtist = connection.prepareStatement("" +
                "INSERT INTO artist(first_name, last_name, instr_id) " +
                "VALUES(?, ?, ?) ON CONFLICT DO NOTHING;", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psJoin = connection.prepareStatement("" +
                     "INSERT INTO vinyl_disc_artist(artist_id, disc_id) " +
                     "VALUES (?, ?) ON CONFLICT DO NOTHING;");
             PreparedStatement findArtistId = connection.prepareStatement("" +
                     "SELECT artist_id " +
                     "FROM artist " +
                     "WHERE first_name=? AND last_name=?;")
        ) {

            for (Artist artist : disc.getArtists()) {
                psArtist.setString(1, artist.getFirstName());
                psArtist.setString(2, artist.getLastName());
                psArtist.setInt(3, getInstrumentId(connection, artist.getMainInstrument()));

                psArtist.executeUpdate();

                findArtistId.setString(1, artist.getFirstName());
                findArtistId.setString(2, artist.getLastName());

                final ResultSet rsArtistId = findArtistId.executeQuery();
                if (rsArtistId.next()) {
                    psJoin.setInt(1, rsArtistId.getInt(1));
                }

                psJoin.setLong(2, discId);

                psJoin.addBatch();
            }
            psJoin.executeBatch();
        }
    }

    private void saveSong(Connection connection, VinylDisc disc, Long discId) throws SQLException {
        try (PreparedStatement psSong = connection.prepareStatement("" +
                "INSERT INTO song(title, duration, disc_id) " +
                "VALUES(?, ?, ?);")) {

            for (Song song : disc.getSongs()) {
                psSong.setString(1, song.getTitle());
                psSong.setInt(2, song.getDuration());
                psSong.setLong(3, discId);

                psSong.addBatch();
            }

            psSong.executeBatch();
        }
    }


    private List<Song> getSongs(Connection connection, Long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("" +
                "SELECT title, duration " +
                "FROM song " +
                "WHERE disc_id=?;")) {
            statement.setLong(1, id);
            final ResultSet rs = statement.executeQuery();
            List<Song> songs = new ArrayList<>();

            while (rs.next()) {
                final String songTitle = rs.getString("title");
                final int duration = rs.getInt("duration");

                songs.add(new Song(songTitle, duration));
            }
            return songs;
        }
    }

    private List<Artist> getArtists(Connection connection, Long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("" +
                "SELECT ar.artist_id, ar.first_name, ar.last_name, instr.instr_name " +
                "FROM vinyl_disc_artist vda " +
                "INNER JOIN artist ar USING(artist_id) " +
                "INNER JOIN instrument instr ON ar.instr_id = instr.instr_id " +
                "WHERE vda.disc_id=?;")) {

            statement.setLong(1, id);
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

    public static int getInstrumentId(Connection connection, Instrument instrument) throws SQLException {
        try (PreparedStatement statementInsert = connection.prepareStatement("" +
                "INSERT INTO instrument(instr_name) " +
                "VALUES(?) ON CONFLICT DO NOTHING;");
             PreparedStatement statementGet = connection.prepareStatement("" +
                     "SELECT instr_id " +
                     "FROM instrument " +
                     "WHERE instr_name=?;")) {

            statementInsert.setString(1, instrument.name());
            statementGet.setString(1, instrument.name());

            statementInsert.executeUpdate();
            final ResultSet rs = statementGet.executeQuery();
            rs.next();

            return rs.getInt("instr_id");
        }
    }

    public static int getSongId(Connection connection, Song song) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("" +
                "SELECT song_id " +
                "FROM song " +
                "WHERE title=?")) {

            ps.setString(1, song.getTitle());
            return ps.executeQuery().getInt("id");
        }
    }

    public static long retrieveId(PreparedStatement statement) throws SQLException {
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            return rs.getLong(1);
        } else {
            throw new SQLException("Failed to retrieve the ID: (" + statement.getMetaData() + ")");
        }
    }
}
