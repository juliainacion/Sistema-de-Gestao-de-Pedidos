package com.ecommerce.service;

import com.ecommerce.dto.ClienteRequestDTO;
import com.ecommerce.exception.EmailDuplicadoException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Cliente;
import com.ecommerce.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Cliente criar(ClienteRequestDTO dto) {
        validarEmailDuplicado(dto.email(), null);

        Cliente cliente = Cliente.builder()
                .nome(dto.nome())
                .email(dto.email())
                .build();

        return clienteRepository.save(cliente);
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
    }

    @Transactional
    public Cliente atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = buscarPorId(id);
        validarEmailDuplicado(dto.email(), cliente.getEmail());
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void deletar(Long id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }

    private void validarEmailDuplicado(String novoEmail, String emailAtual) {
        boolean emailMudou = emailAtual == null || !emailAtual.equalsIgnoreCase(novoEmail);
        if (emailMudou && clienteRepository.existsByEmail(novoEmail)) {
            throw new EmailDuplicadoException("Já existe cliente cadastrado com este email.");
        }
    }
}
