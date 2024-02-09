package kz.baltabayev.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.baltabayev.dto.ExceptionResponse;
import kz.baltabayev.dto.SecurityRequest;
import kz.baltabayev.dto.SuccessResponse;
import kz.baltabayev.exception.NotValidArgumentException;
import kz.baltabayev.exception.RegisterException;
import kz.baltabayev.model.User;
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
        resp.setContentType("application/json");

        try {
            SecurityRequest request = jacksonMapper.readValue(req.getInputStream(), SecurityRequest.class);
            User registeredUser = securityService.register(request.login(), request.password());
            resp.setStatus(HttpServletResponse.SC_CREATED);
            jacksonMapper.writeValue(resp.getWriter(), new SuccessResponse("Player with login " + registeredUser.getLogin() + " successfully created."));
        } catch (NotValidArgumentException | JsonParseException | RegisterException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jacksonMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }

        super.doPost(req, resp);
    }
}
