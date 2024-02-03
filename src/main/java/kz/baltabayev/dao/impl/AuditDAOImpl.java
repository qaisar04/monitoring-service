package kz.baltabayev.dao.impl;

import kz.baltabayev.dao.AuditDAO;
import kz.baltabayev.model.Audit;
import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;
import kz.baltabayev.util.ConnectionManager;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the AuditDAO interface using an in-memory map.
 * Provides methods for CRUD operations on Audit entities.
 */
@Slf4j
public class AuditDAOImpl implements AuditDAO {

    /**
     * Retrieves an Audit entity by its ID.
     *
     * @param id The ID of the Audit entity to retrieve.
     * @return An Optional containing the found Audit entity, or an empty Optional if not found.
     */
    @Override
    public Optional<Audit> findById(Long id) {
        String sqlFindById = """
                SELECT * FROM develop.audit WHERE id = ?
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ?
                    Optional.of(buildAudit(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    private Audit buildAudit(ResultSet resultSet) throws SQLException {
        String auditTypeString = resultSet.getString("audit_type");
        AuditType auditType = AuditType.valueOf(auditTypeString);

        String actionTypeString = resultSet.getString("action_type");
        ActionType actionType = ActionType.valueOf(actionTypeString);

        return Audit.builder()
                .id(resultSet.getLong("id"))
                .auditType(auditType)
                .actionType(actionType)
                .login(resultSet.getString("login"))
                .build();
    }

    /**
     * Retrieves a list of all Audit entities.
     *
     * @return A list containing all Audit entities stored in the in-memory map.
     */
    @Override
    public List<Audit> findAll() {
        String sqlFindAll = """
                SELECT * FROM develop.audit;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAll)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Audit> audits = new ArrayList<>();

            while (resultSet.next()) {
                audits.add(buildAudit(resultSet));
            }

            return audits;
        } catch (SQLException e) {
            log.error("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Saves an Audit entity to the in-memory storage.
     *
     * @param audit The Audit entity to save.
     * @return The saved Audit entity with an assigned ID.
     */
    @Override
    public Audit save(Audit audit) {
        String sqlSave = """
                INSERT INTO develop.audit(login, audit_type, action_type)
                VALUES (?,?,?);
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, audit.getLogin());
            preparedStatement.setObject(2, audit.getAuditType(), Types.OTHER);
            preparedStatement.setObject(3, audit.getActionType(), Types.OTHER);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                    if (keys.next()) {
                        audit.setId(keys.getObject(1, Long.class));
                    }
                } catch (SQLException e) {
                    log.error("Ошибка при получении сгенерированного ключа: " + e.getMessage());
                }
            } else {
                log.error("Ошибка при выполнении SQL-запроса. Нет добавленных записей.");
            }

            return audit;
        } catch (SQLException e) {
            log.error("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        }
        return null;
    }
}
