package ru.miller87.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.miller87.model.dto.WalletRequestDto;
import ru.miller87.service.WalletService;

import java.util.UUID;


/**
 * Контроллер для работы с кошельками
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WalletController {
    private final WalletService walletService;
    /**
     * Обновляет баланс кошелька
     *
     * @param walletRequestDto объект, содержащий данные о кошельке
     * @return сообщение об успешном обновлении баланса
     */
    @PostMapping("/wallet")
    public ResponseEntity<?> updateWallet(@RequestBody WalletRequestDto walletRequestDto) {
        walletService.updateWallet(walletRequestDto);
        return ResponseEntity.ok("Операция выполнена успешно");
    }

    /**
     * Получает информацию о кошельке по его UUID
     *
     * @param walletId уникальный идентификатор кошелька
     * @return текущий баланс кошелька
     */
    @GetMapping("/wallets/{WALLET_UUID}")
    public Long getWallet(@PathVariable UUID walletId) {
       return walletService.getWallet(walletId);
    }
}