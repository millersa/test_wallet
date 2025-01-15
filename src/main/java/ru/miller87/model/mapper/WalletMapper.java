package ru.miller87.model.mapper;

import ru.miller87.model.dto.WalletRequestDto;
import ru.miller87.model.entity.WalletEntity;

public interface WalletMapper {
    WalletRequestDto fromEntityToDto(WalletEntity entity);
}