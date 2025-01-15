package ru.miller87.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.miller87.model.enumeration.OperationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_log")
@Data
public class TransactionLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "walled_id", nullable = false)
    private WalletEntity wallet;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false)
    private OperationType operationType;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}