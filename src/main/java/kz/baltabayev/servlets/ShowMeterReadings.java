package kz.baltabayev.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.baltabayev.dto.ExceptionResponse;
import kz.baltabayev.dto.MeterReadingDateRequest;
import kz.baltabayev.exception.AuthorizeException;
import kz.baltabayev.exception.ValidationParametersException;
import kz.baltabayev.model.MeterReading;
import kz.baltabayev.model.User;
import kz.baltabayev.security.Authentication;
import kz.baltabayev.service.MeterReadingService;
import kz.baltabayev.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/reading/date")
public class ShowMeterReadings extends HttpServlet {

    private MeterReadingService meterReadingService;
    private UserService userService;
    private ObjectMapper objectMapper;


    @Override
    public void init() throws ServletException {
        super.init();
        meterReadingService = (MeterReadingService) getServletContext().getAttribute("meterReadingService");
        userService = (UserService) getServletContext().getAttribute("userService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");

        if (authentication.isAuth()) {
            try {
                showReadingsByDate(req, resp, authentication);
            } catch (ValidationParametersException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            } catch (AuthorizeException e) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    private void showReadingsByDate(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException {
        String login = authentication.getLogin();
        if (login == null) throw new ValidationParametersException("Login parameter is null!");
        Optional<User> optionalUser = userService.getUserByLogin(login);
        if (optionalUser.isPresent()) {
            if (!authentication.getLogin().equals(optionalUser.get().getLogin()))
                throw new AuthorizeException("Incorrect credentials.");

            MeterReadingDateRequest request = objectMapper.readValue(req.getInputStream(), MeterReadingDateRequest.class);
            List<MeterReading> meterReadings = meterReadingService.getMeterReadingsByMonthAndYear(request.month(), request.year(), optionalUser.get().getId());

            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), meterReadings);
        } else {
            throw new AuthorizeException("Incorrect credentials.");
        }
    }
}
