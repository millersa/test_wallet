package ru.miller87.model.enumeration

import lombok.Getter
import lombok.NoArgsConstructor

@Getter
@NoArgsConstructor
public enum class OperationType {
    DEPOSIT,
    WITHDRAW;
}