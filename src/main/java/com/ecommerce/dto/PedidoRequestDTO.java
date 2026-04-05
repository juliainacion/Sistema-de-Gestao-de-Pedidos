package com.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoRequestDTO(
        @NotNull(message = "O clienteId é obrigatório")
        Long clienteId,

        @NotNull(message = "O enderecoId é obrigatório")
        Long enderecoId,

        @Valid
        @NotEmpty(message = "O pedido deve conter ao menos um item")
        List<ItemPedidoRequestDTO> itens
) {
}
