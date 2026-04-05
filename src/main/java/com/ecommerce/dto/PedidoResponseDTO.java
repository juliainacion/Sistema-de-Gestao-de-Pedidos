package com.ecommerce.dto;

import com.ecommerce.model.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        LocalDateTime data,
        StatusPedido status,
        BigDecimal total,
        Long clienteId,
        String nomeCliente,
        EnderecoResponseDTO enderecoEntrega,
        List<ItemPedidoResponseDTO> itens
) {
}
