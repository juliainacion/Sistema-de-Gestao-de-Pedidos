package com.ecommerce.controller;

// Importação dos DTOs usados para receber e devolver dados da API
import com.ecommerce.dto.ClienteRequestDTO;
import com.ecommerce.dto.ClienteResponseDTO;
import com.ecommerce.dto.EnderecoRequestDTO;
import com.ecommerce.dto.EnderecoResponseDTO;

// Importação do mapper responsável por converter entidade em DTO de resposta
import com.ecommerce.mapper.ClienteMapper;

// Importação da entidade Endereco
import com.ecommerce.model.Endereco;

// Importação das classes de serviço, onde ficam as regras de negócio
import com.ecommerce.service.ClienteService;
import com.ecommerce.service.EnderecoService;

// Importação da anotação de validação dos dados recebidos
import jakarta.validation.Valid;

// Importações de recursos do Spring para controle REST e códigos HTTP
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável por expor os endpoints relacionados aos clientes.
 *
 * Esta classe faz parte da camada de Controller na arquitetura em 3 camadas.
 * Sua função é receber as requisições HTTP, encaminhar os dados para a camada
 * de serviço e retornar a resposta apropriada ao cliente da API.
 *
 * Além das operações de CRUD de cliente, também possui endpoints para
 * gerenciar os endereços vinculados a um cliente.
 */
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    // Serviço responsável pelas regras de negócio de cliente
    private final ClienteService clienteService;

    // Serviço responsável pelas regras de negócio de endereço
    private final EnderecoService enderecoService;

    // Mapper responsável por converter entidades Cliente para ClienteResponseDTO
    private final ClienteMapper clienteMapper;

    /**
     * Construtor com injeção de dependências.
     *
     * O Spring injeta automaticamente as instâncias necessárias para que
     * o controller funcione corretamente.
     */
    public ClienteController(ClienteService clienteService,
                             EnderecoService enderecoService,
                             ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.enderecoService = enderecoService;
        this.clienteMapper = clienteMapper;
    }

    /**
     * Endpoint responsável por criar um novo cliente.
     *
     * Método HTTP: POST
     * URL: /clientes
     *
     * @param dto objeto com os dados do cliente recebidos no corpo da requisição
     * @return ClienteResponseDTO com os dados do cliente criado
     *
     * A anotação @Valid garante que as validações definidas no DTO sejam aplicadas.
     * A anotação @ResponseStatus(HttpStatus.CREATED) define o status HTTP 201.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDTO criar(@RequestBody @Valid ClienteRequestDTO dto) {
        return clienteMapper.toResponseDTO(clienteService.criar(dto));
    }

    /**
     * Endpoint responsável por listar todos os clientes cadastrados.
     *
     * Método HTTP: GET
     * URL: /clientes
     *
     * @return lista de clientes convertidos para DTO de resposta
     */
    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return clienteService.listar().stream()
                .map(clienteMapper::toResponseDTO)
                .toList();
    }

    /**
     * Endpoint responsável por buscar um cliente pelo ID.
     *
     * Método HTTP: GET
     * URL: /clientes/{id}
     *
     * @param id identificador do cliente
     * @return ClienteResponseDTO correspondente ao cliente encontrado
     */
    @GetMapping("/{id}")
    public ClienteResponseDTO buscarPorId(@PathVariable Long id) {
        return clienteMapper.toResponseDTO(clienteService.buscarPorId(id));
    }

    /**
     * Endpoint responsável por atualizar os dados de um cliente existente.
     *
     * Método HTTP: PUT
     * URL: /clientes/{id}
     *
     * @param id identificador do cliente que será atualizado
     * @param dto novos dados do cliente
     * @return ClienteResponseDTO com os dados atualizados
     */
    @PutMapping("/{id}")
    public ClienteResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid ClienteRequestDTO dto) {
        return clienteMapper.toResponseDTO(clienteService.atualizar(id, dto));
    }

    /**
     * Endpoint responsável por remover um cliente do sistema.
     *
     * Método HTTP: DELETE
     * URL: /clientes/{id}
     *
     * @param id identificador do cliente que será removido
     *
     * O status HTTP retornado é 204 (No Content), indicando que a exclusão
     * foi realizada com sucesso e não há conteúdo para retorno.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        clienteService.deletar(id);
    }

    /**
     * Endpoint responsável por cadastrar um novo endereço para um cliente específico.
     *
     * Método HTTP: POST
     * URL: /clientes/{clienteId}/enderecos
     *
     * @param clienteId identificador do cliente ao qual o endereço será vinculado
     * @param dto dados do endereço recebidos no corpo da requisição
     * @return EnderecoResponseDTO com os dados do endereço criado
     */
    @PostMapping("/{clienteId}/enderecos")
    @ResponseStatus(HttpStatus.CREATED)
    public EnderecoResponseDTO criarEndereco(@PathVariable Long clienteId,
                                             @RequestBody @Valid EnderecoRequestDTO dto) {

        // Chama a camada de serviço para criar e vincular o endereço ao cliente
        Endereco endereco = enderecoService.criar(clienteId, dto);

        // Retorna apenas os dados necessários por meio do DTO de resposta
        return new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getRua(),
                endereco.getCidade(),
                endereco.getCep()
        );
    }

    /**
     * Endpoint responsável por listar todos os endereços de um cliente específico.
     *
     * Método HTTP: GET
     * URL: /clientes/{clienteId}/enderecos
     *
     * @param clienteId identificador do cliente
     * @return lista de endereços vinculados ao cliente
     */
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
