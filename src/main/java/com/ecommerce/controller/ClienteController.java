package com.ecommerce.controller;

import com.ecommerce.dto.ClienteRequestDTO;
import com.ecommerce.dto.ClienteResponseDTO;
import com.ecommerce.dto.EnderecoRequestDTO;
import com.ecommerce.dto.EnderecoResponseDTO;
import com.ecommerce.mapper.ClienteMapper;
import com.ecommerce.model.Endereco;
import com.ecommerce.service.ClienteService;
import com.ecommerce.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final EnderecoService enderecoService;
    private final ClienteMapper clienteMapper;

    public ClienteController(ClienteService clienteService,
                             EnderecoService enderecoService,
                             ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.enderecoService = enderecoService;
        this.clienteMapper = clienteMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDTO criar(@RequestBody @Valid ClienteRequestDTO dto) {
        return clienteMapper.toResponseDTO(clienteService.criar(dto));
    }

    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return clienteService.listar().stream()
                .map(clienteMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO buscarPorId(@PathVariable Long id) {
        return clienteMapper.toResponseDTO(clienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ClienteResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid ClienteRequestDTO dto) {
        return clienteMapper.toResponseDTO(clienteService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        clienteService.deletar(id);
    }

    @PostMapping("/{clienteId}/enderecos")
    @ResponseStatus(HttpStatus.CREATED)
    public EnderecoResponseDTO criarEndereco(@PathVariable Long clienteId,
                                             @RequestBody @Valid EnderecoRequestDTO dto) {
        Endereco endereco = enderecoService.criar(clienteId, dto);
        return new EnderecoResponseDTO(endereco.getId(), endereco.getRua(), endereco.getCidade(), endereco.getCep());
    }

    @GetMapping("/{clienteId}/enderecos")
    public List<EnderecoResponseDTO> listarEnderecos(@PathVariable Long clienteId) {
        return enderecoService.listarPorCliente(clienteId)
                .stream()
                .map(endereco -> new EnderecoResponseDTO(
                        endereco.getId(),
                        endereco.getRua(),
                        endereco.getCidade(),
                        endereco.getCep()))
                .toList();
    }
}
