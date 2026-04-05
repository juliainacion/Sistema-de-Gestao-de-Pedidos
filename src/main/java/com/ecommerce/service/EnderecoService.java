package com.ecommerce.service;

import com.ecommerce.dto.EnderecoRequestDTO;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Cliente;
import com.ecommerce.model.Endereco;
import com.ecommerce.repository.ClienteRepository;
import com.ecommerce.repository.EnderecoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final ClienteRepository clienteRepository;

    public EnderecoService(EnderecoRepository enderecoRepository, ClienteRepository clienteRepository) {
        this.enderecoRepository = enderecoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Endereco criar(Long clienteId, EnderecoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        Endereco endereco = Endereco.builder()
                .rua(dto.rua())
                .cidade(dto.cidade())
                .cep(dto.cep())
                .cliente(cliente)
                .build();

        return enderecoRepository.save(endereco);
    }

    public List<Endereco> listarPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ResourceNotFoundException("Cliente não encontrado.");
        }
        return enderecoRepository.findByClienteId(clienteId);
    }
}
