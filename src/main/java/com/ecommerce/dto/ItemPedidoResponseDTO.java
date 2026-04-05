package com.ecommerce.dto;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        Long id,
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {
}
