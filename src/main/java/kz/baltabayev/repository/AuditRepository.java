package kz.baltabayev.repository;

import kz.baltabayev.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Long, Audit> {
}
