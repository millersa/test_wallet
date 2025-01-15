package ru.miller87.model.dto;

import lombok.Data;
import ru.miller87.model.enumeration.OperationType;

import java.util.UUID;

@Data
public class WalletRequestDto {
    private UUID walletId;
    private OperationType operationType;
    private Long amount;
}