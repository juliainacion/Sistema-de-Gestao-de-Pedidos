package com.ecommerce.dto;

import java.util.List;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String email,
        List<EnderecoResponseDTO> enderecos
) {
}
