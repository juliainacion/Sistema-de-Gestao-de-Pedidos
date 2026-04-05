package com.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoRequestDTO(
        @NotBlank(message = "A rua é obrigatória")
        @Size(max = 150, message = "A rua deve ter no máximo 150 caracteres")
        String rua,

        @NotBlank(message = "A cidade é obrigatória")
        @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres")
        String cidade,

        @NotBlank(message = "O CEP é obrigatório")
        @Size(max = 20, message = "O CEP deve ter no máximo 20 caracteres")
        String cep
) {
}
