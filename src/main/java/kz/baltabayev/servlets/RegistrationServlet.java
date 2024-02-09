package kz.baltabayev.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import kz.baltabayev.service.SecurityService;
import lombok.RequiredArgsConstructor;

@WebServlet("/registration")
@RequiredArgsConstructor
public class RegistrationServlet extends HttpServlet {

    private final SecurityService securityService;
    private final ObjectMapper jacksonMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        securityService = (SecurityService) getServletContext().getAttribute("securityService");
        jacksonMapper = (ObjectMapper) getServletContext().getAttribute("jacksonMapper");
    }
}
