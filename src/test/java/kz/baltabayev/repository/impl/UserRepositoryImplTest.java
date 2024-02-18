package kz.baltabayev.repository.impl;

import kz.baltabayev.containers.PostgresTestContainer;
import kz.baltabayev.liquibase.LiquibaseDemo;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.Role;
import kz.baltabayev.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("user dao implementation test")
public class UserRepositoryImplTest extends PostgresTestContainer{

    private UserRepositoryImpl userDao;

    @BeforeEach
    public void setUp() {
//        ConnectionManager connectionManager = new ConnectionManager(
//                container.getJdbcUrl(), container.getUsername(), container.getPassword(),
//                "org.postgresql.Driver");

        ConnectionManager connectionManager = null;


        LiquibaseDemo liquibaseTest = new LiquibaseDemo(connectionManager.getConnection(), "db/changelog/changelog.xml", "migration");
        liquibaseTest.runMigrations();

        userDao = new UserRepositoryImpl(connectionManager);
    }

    @Test
    @DisplayName("find by id method verification test")
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
    @DisplayName("find all method verification test")
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
    @DisplayName("save method verification test")
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
    @DisplayName("find by login method verification test")
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
    @DisplayName("save null user method verification test")
    public void testSave_NullUser() {
        assertThrows(NullPointerException.class, () -> userDao.save(null));
    }
}