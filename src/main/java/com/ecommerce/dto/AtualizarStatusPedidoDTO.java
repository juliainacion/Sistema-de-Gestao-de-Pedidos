package com.ecommerce.dto;

import com.ecommerce.model.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusPedidoDTO(
        @NotNull(message = "O novo status é obrigatório")
        StatusPedido novoStatus
) {
}package com.ecommerce.dto;

// Importação do enum que representa os possíveis status do pedido
import com.ecommerce.model.StatusPedido;

// Importação da anotação de validação
import jakarta.validation.constraints.NotNull;

/**
 * DTO (Data Transfer Object) utilizado para atualizar o status de um pedido.
 *
 * Este objeto é usado na camada de Controller para receber o novo status
 * enviado pelo cliente da API (ex: via requisição HTTP PATCH).
 *
 * A utilização de DTO evita expor diretamente a entidade do banco de dados,
 * seguindo boas práticas de arquitetura.
 */
public record AtualizarStatusPedidoDTO(

        /**
         * Campo que representa o novo status do pedido.
         *
         * A anotação @NotNull garante que o cliente da API
         * seja obrigado a enviar um valor para este campo.
         *
         * Caso não seja enviado, será retornado um erro de validação.
         */
        @NotNull(message = "O novo status é obrigatório")
        StatusPedido novoStatus

) {
}
