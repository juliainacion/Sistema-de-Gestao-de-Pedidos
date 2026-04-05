
package com.ecommerce.dto; // Define a localização do arquivo no projeto

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Representa os dados de entrada para a criação/atualização de um cliente.
 * O uso de 'record' garante que o objeto seja imutável e reduz o código boilerplate.
 */
public record ClienteRequestDTO(
        
        // Garante que o nome não seja nulo nem apenas espaços vazios
        @NotBlank(message = "O nome é obrigatório")
        // Limita o tamanho para evitar erros de estouro de coluna no Banco de Dados
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres")
        String nome,

        // Valida se a String possui o formato correto de um e-mail (ex: usuario@dominio.com)
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 120, message = "O email deve ter no máximo 120 caracteres")
        String email
) {
}
