package kz.baltabayev.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.baltabayev.dto.ExceptionResponse;
import kz.baltabayev.dto.MeterReadingDto;
import kz.baltabayev.exception.AuthorizeException;
import kz.baltabayev.exception.ValidationParametersException;
import kz.baltabayev.mapper.MeterReadingMapper;
import kz.baltabayev.model.MeterReading;
import kz.baltabayev.model.User;
import kz.baltabayev.security.Authentication;
import kz.baltabayev.service.MeterReadingService;
import kz.baltabayev.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/reading/current")
public class CurrentMeterReadings extends HttpServlet {

    private MeterReadingService meterReadingService;
    private UserService userService;
    private MeterReadingMapper meterReadingMapper;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        meterReadingService = (MeterReadingService) getServletContext().getAttribute("meterReadingService");
        userService = (UserService) getServletContext().getAttribute("userService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
        meterReadingMapper = (MeterReadingMapper) getServletContext().getAttribute("meterReadingMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");

        if(authentication.isAuth()) {
            try {
                showCurrentReadings(req, resp, authentication);
            } catch (ValidationParametersException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            } catch (AuthorizeException e) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            }
        }
    }

    private void showCurrentReadings(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException {
        String login = req.getParameter("login");
        if (login == null) throw new ValidationParametersException("Login parameter is null!");
        Optional<User> optionalUser = userService.getUserByLogin(login);
        if(optionalUser.isPresent()) {
            if (!authentication.getLogin().equals(optionalUser.get().getLogin()))
                throw new AuthorizeException("Incorrect credentials.");
            List<MeterReading> currentMeterReadings = meterReadingService.getCurrentMeterReadings(optionalUser.get().getId());
            List<MeterReadingDto> currentMeterReadingsDto = new ArrayList<>();
            for (MeterReading meterReading : currentMeterReadings) {
                currentMeterReadingsDto.add(meterReadingMapper.toDto(meterReading));
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), currentMeterReadingsDto);
        } else {
            throw new AuthorizeException("Incorrect credentials.");
        }
    }
}
