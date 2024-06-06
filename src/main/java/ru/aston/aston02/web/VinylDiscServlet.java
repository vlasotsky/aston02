package ru.aston.aston02.web;

import ru.aston.aston02.Config;
import ru.aston.aston02.model.*;
import ru.aston.aston02.service.VinylDiscService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.aston.aston02.util.ConverterToDto.getSingleTo;
import static ru.aston.aston02.util.ConverterToDto.getTos;

public class VinylDiscServlet extends HttpServlet {

    private VinylDiscService service;

    @Override
    public void init() throws ServletException {
        service = new VinylDiscService(Config.getRepository());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String action = request.getParameter("action");
        final List<VinylDisc> discList = service.getAll();

        switch (action) {
            case "view" -> request.setAttribute("all_discs", getTos(discList));
            case "get" -> {
                String discId = Objects.requireNonNull(request.getParameter("disc_id"));

                request.setAttribute("disc", getSingleTo(service.get(Integer.parseInt(discId)), discList));

            }
            default -> throw new IllegalArgumentException("Unknown action " + action);
        }

        request.getRequestDispatcher("").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String id = Objects.requireNonNull(request.getParameter("id"));
        final String title = Objects.requireNonNull(request.getParameter("title"));
        final String genre = Objects.requireNonNull(request.getParameter("genre"));
        final String label = Objects.requireNonNull(request.getParameter("label"));
        final String releaseYear = Objects.requireNonNull(request.getParameter("release_date"));


        final String[] songTitles = Objects.requireNonNull(request.getParameterValues("titles"));
        final String[] songDurations = Objects.requireNonNull(request.getParameterValues("durations"));

        List<Song> songList = new ArrayList<>();

        for (int i = 0; i < songTitles.length; i++) {
            songList.add(new Song(songTitles[i], Integer.parseInt(songDurations[i])));
        }


        final String[] artistNames = Objects.requireNonNull(request.getParameterValues("first_names"));
        final String[] lastNames = Objects.requireNonNull(request.getParameterValues("last_names"));
        final String[] instruments = Objects.requireNonNull(request.getParameterValues("instrument"));

        List<Artist> artistList = new ArrayList<>();

        for (int i = 0; i < artistNames.length; i++) {
            artistList.add(new Artist(artistNames[i], lastNames[i], Instrument.valueOf(instruments[i])));
        }

        service.update(Integer.parseInt(id), new VinylDisc(title, artistList, songList, Genre.valueOf(genre), label, LocalDate.parse(releaseYear)));
        response.sendRedirect("");
    }
}
