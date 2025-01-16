package ru.miller87.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.miller87.model.dto.WalletRequestDto;
import ru.miller87.model.enumeration.OperationType;
import ru.miller87.repository.WalletRepository;

import java.util.UUID;

/**
 * Сервис для работы с кошельками
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {
    private final WalletRepository walletRepository;
    private final TransactionLogService transactionLogService;

    /**
     * Получает информацию о кошельке по его UUID
     *
     * @param walletId уникальный идентификатор кошелька
     * @return текущий баланс кошелька
     */
    public Long getWallet(UUID walletId) {
        var walletEntity = walletRepository.findById(walletId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Кошелек не существует"));
        return walletEntity.getBalance();
    }

    /**
     * Обновляет баланс кошелька
     *
     * @param walletRequestDto объект, содержащий данные о кошельке
     */
    @Transactional
    public void updateWallet(WalletRequestDto walletRequestDto) {
        if (log.isDebugEnabled()) {
            log.debug("Method updateWallet with parameters: walletRequestDto {} ", walletRequestDto);
        }
        var walletEntity = walletRepository.findByUuidForUpdate(walletRequestDto.getWalletId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Кошелек не существует"));
        if (walletRequestDto.getOperationType() == OperationType.WITHDRAW && walletEntity.getBalance().compareTo(walletRequestDto.getAmount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Недостаточно средств");
        }
        if (walletRequestDto.getOperationType() == OperationType.DEPOSIT) {
            walletEntity.setBalance(walletEntity.getBalance() + walletRequestDto.getAmount());
        } else if (walletRequestDto.getOperationType() == OperationType.WITHDRAW) {
            walletEntity.setBalance(walletEntity.getBalance() - walletRequestDto.getAmount());
        }
        walletRepository.save(walletEntity);
        transactionLogService.save(walletEntity, walletRequestDto.getOperationType(), walletRequestDto.getAmount());
    }
}