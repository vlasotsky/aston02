package ru.aston.aston02.web;

import ru.aston.aston02.Config;
import ru.aston.aston02.service.VinylDiscService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VinylDiscServlet extends HttpServlet {

    private VinylDiscService service;

    @Override
    public void init() throws ServletException {
        service = new VinylDiscService(Config.getRepository());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
