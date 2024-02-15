package kz.baltabayev.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.baltabayev.dto.UserDto;
import kz.baltabayev.exception.ValidationParametersException;
import kz.baltabayev.mapper.UserMapper;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.Role;
import kz.baltabayev.security.Authentication;
import kz.baltabayev.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/user/all")
public class ShowAllUsers extends HttpServlet {

    private UserService userService;
    private UserMapper userMapper;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = (UserService) getServletContext().getAttribute("userService");
        userMapper = (UserMapper) getServletContext().getAttribute("userMapper");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");

        if (authentication.isAuth()) {
            try {
                showAllUsers(resp, authentication);
            } catch (ValidationParametersException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(e.getMessage());
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(e.getMessage());
            }
        }
    }

    private void showAllUsers(HttpServletResponse resp, Authentication authentication) throws IOException {
        Role role = authentication.getRole();
        String login = authentication.getLogin();

        if (login == null) {
            throw new ValidationParametersException("Login parameter is null!");
        }

        if (role.equals(Role.ADMIN)) {
            List<User> allUsers = userService.showAllUsers();

            List<UserDto> allUserDto = allUsers.stream()
                    .map(userMapper::toDto)
                    .toList();

            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), allUserDto);
        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("You are not admin to view all users.");
        }
    }
}
