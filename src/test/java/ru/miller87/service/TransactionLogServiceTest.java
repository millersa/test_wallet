package ru.miller87.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.miller87.model.entity.TransactionLogEntity;
import ru.miller87.model.entity.WalletEntity;
import ru.miller87.model.enumeration.OperationType;
import ru.miller87.repository.TransactionLogRepository;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionLogServiceTest {

    @Mock
    private TransactionLogRepository transactionLogRepository;

    @InjectMocks
    private TransactionLogService transactionLogService;

    @Test
    public void testSave() {
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setBalance(100L);
        OperationType operationType = OperationType.DEPOSIT;

        transactionLogService.save(walletEntity, operationType);

        verify(transactionLogRepository, times(1)).save(any(TransactionLogEntity.class));
    }
}