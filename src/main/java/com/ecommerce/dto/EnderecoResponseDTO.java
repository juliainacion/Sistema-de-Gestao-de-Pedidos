package com.ecommerce.dto;

public record EnderecoResponseDTO(
        Long id,
        String rua,
        String cidade,
        String cep
) {
}
