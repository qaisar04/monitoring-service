package kz.baltabayev.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.baltabayev.dto.ExceptionResponse;
import kz.baltabayev.dto.MeterReadingRequest;
import kz.baltabayev.dto.ResponseMessage;
import kz.baltabayev.exception.AuthorizeException;
import kz.baltabayev.exception.DuplicateRecordException;
import kz.baltabayev.exception.NotValidArgumentException;
import kz.baltabayev.exception.ValidationParametersException;
import kz.baltabayev.model.User;
import kz.baltabayev.security.Authentication;
import kz.baltabayev.service.MeterReadingService;
import kz.baltabayev.service.UserService;

import java.io.IOException;

@WebServlet("/reading")
public class SubmitMeterReadings extends HttpServlet {

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
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
        resp.setContentType("application/json");
        if (authentication.isAuth()) {
            try {
                String login = authentication.getLogin();
                if (login == null) throw new ValidationParametersException("Login parameter is null!");
                User user = userService.getUserByLogin(login);
                MeterReadingRequest readingRequest = objectMapper.readValue(req.getInputStream(), MeterReadingRequest.class);
                meterReadingService.submitMeterReading(readingRequest.counterNumber(), readingRequest.meterTypeId(), user.getId());
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                objectMapper.writeValue(resp.getWriter(), new ResponseMessage("The reading was successfully saved!"));

            } catch (NotValidArgumentException | AuthorizeException | DuplicateRecordException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
            }
        }
    }
}