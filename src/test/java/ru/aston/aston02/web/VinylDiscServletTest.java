package ru.aston.aston02.web;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.service.VinylDiscServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

import static org.mockito.Mockito.*;

class VinylDiscServletTest {

    @Mock
    private VinylDiscServiceImpl service;

    @InjectMocks
    private VinylDiscServlet servlet;

    @BeforeEach
    void setUp() throws ServletException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoGet() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(service.getAllVinylDiscs()).thenReturn(Collections.emptyList());

        when(request.getPathInfo()).thenReturn("/");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doGet(request, response);

        printWriter.flush();
        String expectedJson = "[]";
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).getWriter();
        verify(response.getWriter()).write(expectedJson);
    }

    @Test
    void testDoPost() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        InputStream inputStream = new ByteArrayInputStream("{\"title\":\"Test Disc\",\"artist\":\"Test Artist\",\"year\":2022}".getBytes());

        doNothing().when(service).saveVinylDisc(any(VinylDisc.class));

        when(request.getPathInfo()).thenReturn("/");
        when(request.getInputStream()).thenReturn((ServletInputStream) inputStream);

        servlet.doPost(request, response);

        verify(service).saveVinylDisc(any(VinylDisc.class));
    }
}


