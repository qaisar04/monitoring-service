package kz.baltabayev.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.baltabayev.dto.MeterTypeRequest;
import kz.baltabayev.dto.SuccessResponse;
import kz.baltabayev.exception.ValidationParametersException;
import kz.baltabayev.model.types.Role;
import kz.baltabayev.security.Authentication;
import kz.baltabayev.service.MeterTypeService;

import java.io.IOException;

@WebServlet("/meter-type/add")
public class AddMeterTypeServlet extends HttpServlet {

    private MeterTypeService meterTypeService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        meterTypeService = (MeterTypeService) getServletContext().getAttribute("meterTypeService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
        resp.setContentType("application/json");
        if (authentication.isAuth()) {
            try {
                addMeterType(req, resp, authentication);
            } catch (ValidationParametersException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(e.getMessage());
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(e.getMessage());
            }
        }
    }


    private void addMeterType(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException {
        Role role = authentication.getRole();
        String login = authentication.getLogin();

        if (login == null) {
            throw new ValidationParametersException("Login parameter is null!");
        }

        if (role.equals(Role.ADMIN)) {
            MeterTypeRequest request = objectMapper.readValue(req.getInputStream(), MeterTypeRequest.class);
            meterTypeService.save(request);


            resp.setStatus(HttpServletResponse.SC_OK);
            SuccessResponse successResponse = new SuccessResponse("The meter type was successfully saved!");
            objectMapper.writeValue(resp.getWriter(), successResponse);
        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("You are not admin to set meter type.");
        }
    }
}
