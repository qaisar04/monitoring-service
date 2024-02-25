package kz.baltabayev.repository;

import kz.baltabayev.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data Access Object (DAO) interface for managing User entities.
 * Extends the generic MainDAO interface with specific operations for User entities.
 */
public interface UserRepository extends JpaRepository<Long, User> {
}
