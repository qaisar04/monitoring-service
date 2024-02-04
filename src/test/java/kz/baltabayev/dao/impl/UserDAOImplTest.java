package kz.baltabayev.dao.impl;

import kz.baltabayev.containers.PostgresTestContainer;
import kz.baltabayev.liquibase.LiquibaseDemo;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.Role;
import kz.baltabayev.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOImplTest extends PostgresTestContainer{

    private UserDAOImpl userDao;

    @BeforeEach
    public void setUp() {
        ConnectionManager connectionManager = new ConnectionManager(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseDemo liquibaseTest = LiquibaseDemo.getInstance();
        liquibaseTest.runMigrations(connectionManager.getConnection());

        userDao = new UserDAOImpl(connectionManager);
    }

    @Test
    public void testFindById() {
        User user = User.builder()
                .login("Bob")
                .password("password")
                .registrationDate(LocalDateTime.now())
                .role(Role.USER)
                .build();
        userDao.save(user);

        Optional<User> foundUser = userDao.findById(1L);
        assertTrue(foundUser.isPresent());
        assertEquals("Bob", foundUser.get().getLogin());

        Optional<User> notFoundUser = userDao.findById(999L);
        assertFalse(notFoundUser.isPresent());
    }

    @Test
    public void testFindAll() {
        User user1 = User.builder()
                .login("Bob")
                .password("password")
                .registrationDate(LocalDateTime.now())
                .role(Role.USER)
                .build();
        User user2 = User.builder()
                .login("Kate")
                .password("password")
                .registrationDate(LocalDateTime.now())
                .role(Role.USER)
                .build();

        userDao.save(user1);
        userDao.save(user2);

        List<User> allUsers = userDao.findAll();
        assertFalse(allUsers.isEmpty());
        assertEquals(3, allUsers.size()); // + admin
    }

    @Test
    public void testSave() {
        User userToSave = User.builder()
                .login("Alice")
                .password("password")
                .registrationDate(LocalDateTime.now())
                .role(Role.USER)
                .build();

        User savedUser = userDao.save(userToSave);
        assertNotNull(savedUser.getId());
        assertEquals(userToSave.getLogin(), savedUser.getLogin());
        assertEquals(userToSave.getPassword(), savedUser.getPassword());
        assertEquals(userToSave.getRole(), savedUser.getRole());
    }

    @Test
    public void testFindByLogin() {
        User user = User.builder()
                .login("Bob")
                .password("password")
                .registrationDate(LocalDateTime.now())
                .role(Role.USER)
                .build();
        userDao.save(user);

        Optional<User> foundUser = userDao.findByLogin("Bob");
        assertTrue(foundUser.isPresent());
        assertEquals("Bob", foundUser.get().getLogin());

        Optional<User> notFoundUser = userDao.findByLogin("NonExistentLogin");
        assertFalse(notFoundUser.isPresent());
    }

    @Test
    public void testSave_NullUser() {
        assertThrows(NullPointerException.class, () -> userDao.save(null));
    }
}