package ru.aston.aston02.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.model.to.VinylDiscDto;
import ru.aston.aston02.service.VinylDiscServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ru.aston.aston02.util.DtoMapper.*;

@WebServlet(name = "VinylDiscServlet", value = "/v1/vinyl-collection/discs")
public class VinylDiscServlet extends HttpServlet {

    private final VinylDiscServiceImpl service;
    private final ObjectMapper objectMapper;

    public VinylDiscServlet(VinylDiscServiceImpl service) {
        this.service = service;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        final List<VinylDisc> allDiscs = service.getAllVinylDiscs();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.getWriter().write(objectMapper.writeValueAsString(getAllDtos(allDiscs)));
        } else {
            final String[] subPath = pathInfo.split("/");

            if (subPath.length != 2 || !subPath[1].matches("\\d+")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            final Long parsedId = Long.valueOf(subPath[1]);
            final VinylDisc retrieved = service.getVinylDisc(parsedId);

            if (retrieved == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            response.getWriter().write(objectMapper.writeValueAsString(getDto(retrieved, allDiscs)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();

        final ServletInputStream inputStream = request.getInputStream();
        boolean isRequestBodyEmpty = inputStream.available() == 0;
        final boolean isRoot = pathInfo == null || pathInfo.equals("/");

        if (isRequestBodyEmpty) {
            final Long parsedId = getParsedId(response, pathInfo);
            service.deleteVinylDisc(parsedId);

        } else {
            VinylDiscDto newDisc = objectMapper.readValue(new InputStreamReader(inputStream, StandardCharsets.UTF_8), VinylDiscDto.class);

            if (isRoot) {
                service.saveVinylDisc(getEntity(newDisc));
            } else {
                final Long parsedId = getParsedId(response, pathInfo);
                service.updateVinylDisc(Long.valueOf(parsedId), getEntity(newDisc));
            }
        }
    }

    private Long getParsedId(HttpServletResponse response, String pathInfo) throws IOException {
        final String[] subPath = pathInfo.split("/");

        if (subPath.length != 2 || !subPath[1].matches("\\d+")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        return Long.valueOf(subPath[1]);
    }
}
