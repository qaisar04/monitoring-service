package kz.baltabayev.dao.impl;

import kz.baltabayev.containers.PostgresTestContainer;
import kz.baltabayev.liquibase.LiquibaseDemo;
import kz.baltabayev.model.Audit;
import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;
import kz.baltabayev.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("audit dao implementation test")
public class AuditDAOImplTest extends PostgresTestContainer{

    private AuditDAOImpl auditDao;

    @BeforeEach
    public void setUp() {
        ConnectionManager connectionManager = new ConnectionManager(
                container.getJdbcUrl(), container.getUsername(), container.getPassword(),
                "org.postgresql.Driver");

        LiquibaseDemo liquibaseTest = new LiquibaseDemo(connectionManager.getConnection(), "db/changelog/changelog.xml", "migration");
        liquibaseTest.runMigrations();

        auditDao = new AuditDAOImpl(connectionManager);
    }

    @Test
    @DisplayName("findById method verification test")
    public void testFindById() {
        Audit audit = Audit.builder()
                .login("Bob")
                .auditType(AuditType.SUCCESS)
                .actionType(ActionType.AUTHORIZATION)
                .build();
        auditDao.save(audit);

        Optional<Audit> foundAudit = auditDao.findById(1L);
        assertTrue(foundAudit.isPresent());
        assertEquals("Bob", foundAudit.get().getLogin());

        Optional<Audit> notFoundAudit = auditDao.findById(999L);
        assertFalse(notFoundAudit.isPresent());
    }

    @Test
    @DisplayName("findAll method verification test")
    public void testFindAll() {
        Audit audit1 = Audit.builder()
                .login("Bob")
                .auditType(AuditType.SUCCESS)
                .actionType(ActionType.AUTHORIZATION)
                .build();
        Audit audit2 = Audit.builder()
                .login("Kate")
                .auditType(AuditType.FAIL)
                .actionType(ActionType.SUBMIT_METER)
                .build();

        auditDao.save(audit1);
        auditDao.save(audit2);
        auditDao.save(audit2);

        List<Audit> allAudits = auditDao.findAll();
        assertFalse(allAudits.isEmpty());
        assertEquals(3, allAudits.size());
    }

    @Test
    @DisplayName("save method verification test")
    public void testSave() {
        Audit auditToSave = Audit.builder()
                .login("Alice")
                .auditType(AuditType.SUCCESS)
                .actionType(ActionType.AUTHORIZATION)
                .build();

        Audit savedAudit = auditDao.save(auditToSave);
        assertNotNull(savedAudit.getId());
        assertEquals(auditToSave.getLogin(), savedAudit.getLogin());
        assertEquals(auditToSave.getAuditType(), savedAudit.getAuditType());
        assertEquals(auditToSave.getActionType(), savedAudit.getActionType());
    }

    @Test
    @DisplayName("delete method verification test")
    public void testDelete() {
        Audit audit = Audit.builder()
                .login("Bob")
                .auditType(AuditType.SUCCESS)
                .actionType(ActionType.AUTHORIZATION)
                .build();
        Audit savedAudit = auditDao.save(audit);

        Optional<Audit> audit1 = auditDao.findById(savedAudit.getId());
        assertTrue(audit1.isPresent());

        auditDao.delete(audit1.get().getId());
        Optional<Audit> deletedAudit = auditDao.findById(audit1.get().getId());
        assertFalse(deletedAudit.isPresent());
    }

    @Test
    @DisplayName("deleteAll method verification test")
    public void testDeleteAll() {
        Audit audit = Audit.builder()
                .login("Bob")
                .auditType(AuditType.SUCCESS)
                .actionType(ActionType.AUTHORIZATION)
                .build();
        auditDao.save(audit);
        auditDao.save(audit);

        List<Audit> auditsBeforeDelete = auditDao.findAll();
        assertFalse(auditsBeforeDelete.isEmpty());

        auditDao.deleteAll();
        List<Audit> auditsAfterDelete = auditDao.findAll();
        assertTrue(auditsAfterDelete.isEmpty());
    }

    @Test
    @DisplayName("findById method verification test")
    public void testFindById_NotFound() {
        Optional<Audit> notFoundAudit = auditDao.findById(999L);
        assertFalse(notFoundAudit.isPresent());
    }

    @Test
    @DisplayName("save method verification test")
    public void testSave_NullAudit() {
        assertThrows(NullPointerException.class, () -> auditDao.save(null));
    }

    @Test
    @DisplayName("delete method verification test")
    public void testDelete_NonExistentId() {
        boolean deleted = auditDao.delete(999L);
        assertFalse(deleted);
    }
}