package com.ecommerce.mapper;

import com.ecommerce.dto.ClienteResponseDTO;
import com.ecommerce.dto.EnderecoResponseDTO;
import com.ecommerce.model.Cliente;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClienteMapper {

    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        List<EnderecoResponseDTO> enderecos = cliente.getEnderecos()
                .stream()
                .map(endereco -> new EnderecoResponseDTO(
                        endereco.getId(),
                        endereco.getRua(),
                        endereco.getCidade(),
                        endereco.getCep()))
                .toList();

        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                enderecos
        );
    }
}
