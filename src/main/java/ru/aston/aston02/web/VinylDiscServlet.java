package ru.aston.aston02.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.aston.aston02.Config;
import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.model.dto.VinylDiscDto;
import ru.aston.aston02.service.VinylDiscService;
import ru.aston.aston02.service.VinylDiscServiceFactory;
import ru.aston.aston02.service.VinylDiscServiceImpl;
import ru.aston.aston02.service.VinylDiscServiceImplFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class VinylDiscServlet extends HttpServlet {

    private final VinylDiscService service;
    private final ObjectMapper objectMapper;

    public VinylDiscServlet() {
        VinylDiscServiceFactory<Long, VinylDisc> serviceFactory = new VinylDiscServiceImplFactory();
        this.service = serviceFactory.getVinylDiscService(Config.getRepository());
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

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

        final List<VinylDiscDto> allDiscs = service.getAllVinylDiscs();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.getWriter().write(objectMapper.writeValueAsString(allDiscs));
        } else {
            final String[] subPath = pathInfo.split("/");

            if (subPath.length != 2 || !subPath[1].matches("\\d+")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            final Long parsedId = Long.valueOf(subPath[1]);
            final VinylDiscDto retrieved = service.getVinylDisc(parsedId);

            if (retrieved == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            response.getWriter().write(objectMapper.writeValueAsString(retrieved));
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
                service.saveVinylDisc(newDisc);
            } else {
                final Long parsedId = getParsedId(response, pathInfo);
                service.updateVinylDisc(parsedId, newDisc);
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
