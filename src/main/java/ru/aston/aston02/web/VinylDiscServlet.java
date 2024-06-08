package ru.aston.aston02.web;

import com.google.gson.Gson;
import ru.aston.aston02.Config;
import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.model.to.VinylDiscDto;
import ru.aston.aston02.service.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static ru.aston.aston02.util.DtoMapper.*;

@WebServlet(name = "VinylDiscServlet", value = "/v1/vinyl-collection/discs")
public class VinylDiscServlet extends HttpServlet {

    private Service service;
    private Gson gson;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        service = Config.getService();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        final List<VinylDisc> allDiscs = service.getAll();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.getWriter().write(gson.toJson(getAllDtos(allDiscs)));
        } else {

            final String[] subPath = pathInfo.split("/");

            if (subPath.length != 2 || !subPath[1].matches("\\d+")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            final Long parsedId = Long.valueOf(subPath[1]);
            final VinylDisc retrieved = service.get(parsedId);

            if (retrieved == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            response.getWriter().write(gson.toJson(getDto(retrieved, allDiscs)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();

        boolean isRequestBodyEmpty = request.getInputStream().available() == 0;
        final boolean isRoot = pathInfo == null || pathInfo.equals("/");

        if (isRequestBodyEmpty) {
            final String[] subPath = pathInfo.split("/");

            if (subPath.length != 2 || !subPath[1].matches("\\d+")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }

            final Long parsedId = Long.valueOf(subPath[1]);
            service.delete(parsedId);

        } else {
            VinylDiscDto newDisc = gson.fromJson(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8), VinylDiscDto.class);

            if (isRoot) {
                service.save(getEntity(newDisc));
            } else {
                final String disc_id = Objects.requireNonNull(request.getParameter("disc_id"));
                service.update(Long.valueOf(disc_id), getEntity(newDisc));
            }
        }
    }
}
