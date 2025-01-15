package ru.miller87.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.miller87.model.entity.TransactionLogEntity;

public interface TransactionLogRepository extends JpaRepository<TransactionLogEntity, Long> {
}