package com.ecommerce.dto;

import com.ecommerce.model.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusPedidoDTO(
        @NotNull(message = "O novo status é obrigatório")
        StatusPedido novoStatus
) {
}
