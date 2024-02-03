package kz.baltabayev.dao.impl;

import kz.baltabayev.dao.UserDAO;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.Role;
import kz.baltabayev.util.ConnectionManager;
import kz.baltabayev.util.DateTimeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UserDAO interface using an in-memory map.
 * Provides methods for CRUD operations on User entities and initializes
 * the map with a predefined admin User entity during construction.
 */
public class UserDAOImpl implements UserDAO {

    /**
     * Constructs a new UserDAOImpl and initializes the in-memory storage
     * with a predefined admin User entity.
     */
    public UserDAOImpl() {
        save(User.builder().login("admin").password("admin").role(Role.ADMIN).registrationDate(DateTimeUtils.parseDateTime(LocalDateTime.now())).build());
    }

    /**
     * Retrieves a User entity by its ID.
     *
     * @param id The ID of the User entity to retrieve.
     * @return An Optional containing the found User entity, or an empty Optional if not found.
     */
    @Override
    public Optional<User> findById(Long id) {
        String sqlFindById = """
                SELECT * FROM develop.users WHERE id = ?
                """;
        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? Optional.of(buildUser(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    /**
     * Updates an existing User entity in the in-memory storage.
     *
     * @param user The User entity to update.
     * @return The updated User entity.
     * @throws IllegalArgumentException If the User with the given ID is not found.
     */
    @Override
    public User update(User user) {
        String sqlUpdate = """
                UPDATE develop.users SET login = ?, password = ?, registration_date = ? WHERE id = ?
                """;
        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRegistrationDate());
            preparedStatement.setLong(4, user.getId());
            preparedStatement.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }

    /**
     * Retrieves a list of all User entities.
     *
     * @return A list containing all User entities stored in the in-memory map.
     */
    @Override
    public List<User> findAll() {
        String sqlFindAll = """
                SELECT * FROM develop.users;
                """;
        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAll)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                users.add(buildUser(resultSet));
            }

            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve all users", e);
        }
    }

    /**
     * Saves a User entity to the in-memory storage.
     *
     * @param user The User entity to save.
     * @return The saved User entity with an assigned ID.
     */
    @Override
    public User save(User user) {
        String sqlSave = """
                INSERT INTO develop.users (login, password, registration_date, role) VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlSave)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRegistrationDate());
            preparedStatement.setString(4, user.getRole().toString());
            preparedStatement.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }

    /**
     * Retrieves a User entity by its login.
     *
     * @param login The login of the User entity to retrieve.
     * @return An Optional containing the found User entity, or an empty Optional if not found.
     */
    @Override
    public Optional<User> findByLogin(String login) {
        String sqlFindByLogin = """
                SELECT * FROM develop.users WHERE login = ?
                """;
        try (Connection connection = ConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlFindByLogin)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? Optional.of(buildUser(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        return User.builder().id(resultSet.getLong("id")).login(resultSet.getString("login")).registrationDate(resultSet.getString("registration_date")).role(Role.valueOf(resultSet.getString("role"))).password(resultSet.getString("password")).build();
    }
}
