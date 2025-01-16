package ru.miller87.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.miller87.model.enumeration.OperationType;

import java.util.UUID;

@Data
public class WalletRequestDto {
    @NotNull(message = "Необходимо указать UUID кошелька")
    private UUID walletId;
    @NotNull(message = "Необходимо указать тип операции")
    private OperationType operationType;
    @NotNull(message = "Необходимо указать сумму")
    @Min(value = 0, message = "Баланс не может быть отрицательным")
    private Long amount;
}