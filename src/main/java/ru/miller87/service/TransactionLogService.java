package ru.miller87.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.miller87.model.entity.TransactionLogEntity;
import ru.miller87.model.entity.WalletEntity;
import ru.miller87.model.enumeration.OperationType;
import ru.miller87.repository.TransactionLogRepository;

/**
 * Сервис для работы с логами транзакций.
 */
@Service
@RequiredArgsConstructor
public class TransactionLogService {
    private final TransactionLogRepository transactionLogRepository;

    /**
     * Сохраняет лог транзакции в базе данных.
     *
     * @param walletEntity  объект кошелька, с которым была произведена операция
     * @param operationType тип операции, которая была произведена
     * @param amount        сумма операции
     */
    public void save(WalletEntity walletEntity, OperationType operationType, Long amount) {
        var transactionLogEntity = new TransactionLogEntity();
        transactionLogEntity.setWallet(walletEntity);
        transactionLogEntity.setOperationType(operationType);
        transactionLogEntity.setAmount(amount);
        transactionLogRepository.save(transactionLogEntity);
    }
}