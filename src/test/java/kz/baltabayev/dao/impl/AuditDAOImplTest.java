package kz.baltabayev.dao.impl;

import kz.baltabayev.containers.PostgresTestContainer;
import kz.baltabayev.model.Audit;
import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class AuditDAOImplTest {

    @Container
    public static PostgresTestContainer container = (PostgresTestContainer) PostgresTestContainer.getInstance();

    private AuditDAOImpl auditDao;

    @BeforeEach
    public void setUp() {
        auditDao = new AuditDAOImpl();
        auditDao.deleteAll();
        Audit audit1 = Audit.builder()
                .login("Bob")
                .auditType(AuditType.SUCCESS)
                .actionType(ActionType.AUTHORIZATION)
                .build();
        Audit audit2 = Audit.builder()
                .login("Kate")
                .auditType(AuditType.FAIL)
                .actionType(ActionType.REGISTRATION)
                .build();
        Audit audit3 = Audit.builder()
                .login("Zaur")
                .auditType(AuditType.SUCCESS)
                .actionType(ActionType.GETTING_HISTORY_OF_METER_READINGS)
                .build();

        auditDao.save(audit1);
        auditDao.save(audit2);
        auditDao.save(audit3);
    }

    @Test
    public void testFindById() {
        Optional<Audit> foundAudit = auditDao.findById(1L);
        assertTrue(foundAudit.isPresent());
        assertEquals("Bob", foundAudit.get().getLogin());

        Optional<Audit> notFoundAudit = auditDao.findById(999L);
        assertFalse(notFoundAudit.isPresent());
    }

    @Test
    public void testFindAll() {
        List<Audit> allAudits = auditDao.findAll();
        assertFalse(allAudits.isEmpty());
        assertEquals(3, allAudits.size());
    }

    @Test
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
    public void testDelete() {
        Optional<Audit> audit1 = auditDao.findById(1L);
        assertTrue(audit1.isPresent());

        auditDao.delete(audit1.get().getId());
        Optional<Audit> deletedAudit = auditDao.findById(audit1.get().getId());
        assertFalse(deletedAudit.isPresent());
    }

    @Test
    public void testDeleteAll() {
        List<Audit> auditsBeforeDelete = auditDao.findAll();
        assertFalse(auditsBeforeDelete.isEmpty());

        auditDao.deleteAll();
        List<Audit> auditsAfterDelete = auditDao.findAll();
        assertTrue(auditsAfterDelete.isEmpty());
    }

    @Test
    public void testFindById_NotFound() {
        Optional<Audit> notFoundAudit = auditDao.findById(999L);
        assertFalse(notFoundAudit.isPresent());
    }

    @Test
    public void testSave_NullAudit() {
        assertThrows(NullPointerException.class, () -> auditDao.save(null));
    }

    @Test
    public void testDelete_NonExistentId() {
        boolean deleted = auditDao.delete(999L);
        assertFalse(deleted);
    }
}