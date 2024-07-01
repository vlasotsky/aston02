package ru.aston.aston02.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.aston.aston02.TestUtil;
import ru.aston.aston02.model.dto.VinylDiscDto;
import ru.aston.aston02.service.VinylDiscServiceImpl;

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
import static ru.aston.aston02.TestData.FIRST_ELEMENT_ID;
import static ru.aston.aston02.TestData.JSON_DISC;

class VinylDiscServletTest {

    @Mock
    private VinylDiscServiceImpl service;

    @InjectMocks
    private VinylDiscServlet servlet;
    public static HttpServletRequest request;
    public static HttpServletResponse response;

    @BeforeEach
    void setUp() throws ServletException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        MockitoAnnotations.openMocks(this);
        servlet.init(mock(ServletConfig.class));
    }

    @Test
    void testDoGet() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        List<VinylDiscDto> discList = Collections.emptyList();

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
    void testDoPostDelete() throws Exception {
        ServletInputStream servletInputStream = mock(ServletInputStream.class);

        when(request.getInputStream()).thenReturn(servletInputStream);
        when(request.getInputStream().available()).thenReturn(0);
        when(request.getPathInfo()).thenReturn("/1");

        servlet.doPost(request, response);

        verify(service, times(1)).deleteVinylDisc(1L);
    }

    @Test
    void testDoPostSave() throws IOException, ServletException {
        when(request.getInputStream()).thenReturn(TestUtil.getServletInputStream(JSON_DISC));
        when(request.getPathInfo()).thenReturn("/");

        servlet.doPost(request, response);

        verify(request, times(1)).getInputStream();
        verify(service, times(1)).saveVinylDisc(any(VinylDiscDto.class));
    }

    @Test
    void testDoPostUpdate() throws IOException, ServletException {
        when(request.getInputStream()).thenReturn(TestUtil.getServletInputStream(JSON_DISC));
        when(request.getPathInfo()).thenReturn("/" + FIRST_ELEMENT_ID);

        servlet.doPost(request, response);

        verify(request, times(1)).getInputStream();
        verify(service, times(1)).updateVinylDisc(anyLong(), any(VinylDiscDto.class));
    }
}


