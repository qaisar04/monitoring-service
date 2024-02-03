package kz.baltabayev.dao.impl;

import kz.baltabayev.dao.MeterReadingDAO;
import kz.baltabayev.model.MeterReading;
import kz.baltabayev.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the MeterReadingDAO interface using an in-memory map.
 * Provides methods for CRUD operations on MeterReading entities and additional
 * method to retrieve all MeterReadings associated with a specific user.
 */
public class MeterReadingDAOImpl implements MeterReadingDAO {

    /**
     * Retrieves a MeterReading entity by its ID.
     *
     * @param id The ID of the MeterReading entity to retrieve.
     * @return An Optional containing the found MeterReading entity, or an empty Optional if not found.
     */
    @Override
    public Optional<MeterReading> findById(Long id) {
        String sqlFindById = """
                SELECT * FROM develop.meter_reading WHERE id = ?
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ?
                    Optional.of(buildMeterReading(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    private MeterReading buildMeterReading(ResultSet resultSet) throws SQLException {
        return MeterReading.builder()
                .id(resultSet.getLong("id"))
                .userId(resultSet.getLong("user_id"))
                .typeId(resultSet.getLong("type_id"))
                .counterNumber(resultSet.getInt("counter_number"))
                .readingDate(resultSet.getString("reading_date"))
                .build();
    }

    /**
     * Retrieves a list of all MeterReading entities.
     *
     * @return A list containing all MeterReading entities stored in the in-memory map.
     */
    @Override
    public List<MeterReading> findAll() {
        String sqlFindAll = """
                SELECT * FROM develop.meter_reading
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<MeterReading> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(buildMeterReading(resultSet));
            }
            return result;
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Saves a MeterReading entity to the in-memory storage.
     *
     * @param meterReading The MeterReading entity to save.
     * @return The saved MeterReading entity with an assigned ID.
     */
    @Override
    public MeterReading save(MeterReading meterReading) {
        String sqlSave = """
                INSERT INTO develop.meter_reading (user_id, type_id, counter_number, reading_date)
                VALUES (?, ?, ?, ?)
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, meterReading.getUserId());
            preparedStatement.setLong(2, meterReading.getTypeId());
            preparedStatement.setDouble(3, meterReading.getCounterNumber());
            preparedStatement.setString(4, meterReading.getReadingDate());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                meterReading.setId(generatedKeys.getLong(1));
            }
            return meterReading;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save meter reading", e);
        }
    }

    /**
     * Retrieves a list of all MeterReading entities associated with a specific user.
     *
     * @param userId The ID of the user for whom to retrieve MeterReadings.
     * @return A list containing all MeterReading entities associated with the specified user.
     */
    @Override
    public List<MeterReading> findAllByUserId(Long userId) {
        String sqlFindAllByUserId = """
                SELECT * FROM develop.meter_reading WHERE user_id = ?
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAllByUserId)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<MeterReading> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(buildMeterReading(resultSet));
            }
            return result;
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }
}
