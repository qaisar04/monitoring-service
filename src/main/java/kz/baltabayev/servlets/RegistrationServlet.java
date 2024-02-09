package kz.baltabayev.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.baltabayev.service.SecurityService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@WebServlet("/registration")
@RequiredArgsConstructor
public class RegistrationServlet extends HttpServlet {

    private SecurityService securityService;
    private ObjectMapper jacksonMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        securityService = (SecurityService) getServletContext().getAttribute("securityService");
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
