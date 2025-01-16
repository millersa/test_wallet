package ru.miller87.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "wallet")
@Data
public class WalletEntity {
    @Id
    @GeneratedValue
    @Column(name = "wallet_id")
    private UUID walletId;
    @Column(nullable = false)
    private Long balance = 0L;
}