package kz.baltabayev.repository.impl;

import kz.baltabayev.repository.MeterTypeRepository;
import kz.baltabayev.model.MeterType;
import kz.baltabayev.util.ConnectionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the MeterTypeDAO interface using an in-memory map.
 * Provides methods for CRUD operations on MeterType entities and initializes
 * the map with predefined MeterType entities during construction.
 */
@Repository
@RequiredArgsConstructor
public class MeterTypeRepositoryImpl implements MeterTypeRepository {

    private final ConnectionManager connectionProvider;

    /**
     * Retrieves a MeterType entity by its ID.
     *
     * @param id The ID of the MeterType entity to retrieve.
     * @return An Optional containing the found MeterType entity, or an empty Optional if not found.
     */
    @Override
    public Optional<MeterType> findById(Long id) {
        String sqlFindById = """
                SELECT * FROM develop.meter_type WHERE id = ?
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ?
                    Optional.of(buildMeterType(resultSet))
                    : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves a list of all MeterType entities.
     *
     * @return A list containing all MeterType entities stored in the in-memory map.
     */
    @Override
    public List<MeterType> findAll() {
        String sqlFindAll = """
                SELECT * FROM develop.meter_type
                """;
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAll)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<MeterType> meterTypes = new ArrayList<>();
            while (resultSet.next()) {
                meterTypes.add(buildMeterType(resultSet));
            }
            return meterTypes;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private MeterType buildMeterType(ResultSet resultSet) {
        try {
            return MeterType.builder()
                    .id(resultSet.getLong("id"))
                    .typeName(resultSet.getString("type_name"))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to build meter type", e);
        }
    }

    /**
     * Saves a MeterType entity to the in-memory storage.
     *
     * @param type The MeterType entity to save.
     * @return The saved MeterType entity with an assigned ID.
     */
    @Override
    public MeterType save(MeterType type) {
        String sqlSave = """
                INSERT INTO develop.meter_type (type_name) VALUES (?)
                """;

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, type.getTypeName());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Failed to save meter type");
            }

            ResultSet keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                type.setId(keys.getLong(1));
            } else {
                throw new RuntimeException("Failed to save meter type");
            }

            return type;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save meter type", e);
        }
    }
}
