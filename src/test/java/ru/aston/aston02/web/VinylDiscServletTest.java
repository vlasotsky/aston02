package ru.aston.aston02.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.aston.aston02.TestUtil;
import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.model.to.VinylDiscDto;
import ru.aston.aston02.service.VinylDiscServiceImpl;
import ru.aston.aston02.util.DtoMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class VinylDiscServletTest {

    @Mock
    private VinylDiscServiceImpl service;

    @InjectMocks
    private VinylDiscServlet servlet;

    @BeforeEach
    void setUp() throws ServletException {
        MockitoAnnotations.openMocks(this);
        servlet.init(mock(ServletConfig.class));
    }

    @Test
    void testDoGet() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        List<VinylDisc> discList = Collections.emptyList();
        List<VinylDiscDto> dtoList = DtoMapper.getAllDtos(discList);

        when(request.getPathInfo()).thenReturn("/");
        when(response.getWriter()).thenReturn(printWriter);
        when(service.getAllVinylDiscs()).thenReturn(discList);

        servlet.doGet(request, response);

        verify(response, times(1)).getWriter();
        verify(service, times(1)).getAllVinylDiscs();


        when(request.getPathInfo()).thenReturn("/1");

        servlet.doGet(request, response);

        verify(service, times(1)).getVinylDisc(1L);
    }

    @Test
    void testDoPostEmptyBody() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        ServletInputStream servletInputStream = mock(ServletInputStream.class);

        when(request.getInputStream()).thenReturn(servletInputStream);
        when(request.getInputStream().available()).thenReturn(0);
        when(request.getPathInfo()).thenReturn("/1");

        servlet.doPost(request, response);

        verify(service, times(1)).deleteVinylDisc(1L);
    }

    @Test
    void testDoPostJsonBody() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletInputStream inputStream = mock(ServletInputStream.class);

        String json = "{" +
                "\"title\":\"Test Disc\"," +
                "\"artists\":[" +
                "{" +
                "\"first_name\":\"Test Artist\"," +
                "\"last_name\":\"test\"," +
                "\"main_instrument\":\"Vocals\"" +
                "}]," +
                "\"songs\":[" +
                "{" +
                "\"title\":\"test\"," +
                "\"duration\":100" +
                "}]," +
                "\"genre\":{" +
                "\"name\":\"test\"" +
                "}," +
                "\"label\":\"test\"," +
                "\"releaseDate\":\"2022-01-01\"," +
                "\"isFew\":true" +
                "}";

        when(request.getInputStream()).thenReturn(TestUtil.getServletInputStream(json));
        when(request.getPathInfo()).thenReturn("/");

        servlet.doPost(request, response);

        verify(request, times(1)).getInputStream();
        verify(service, times(1)).saveVinylDisc(any(VinylDisc.class));
    }
}


